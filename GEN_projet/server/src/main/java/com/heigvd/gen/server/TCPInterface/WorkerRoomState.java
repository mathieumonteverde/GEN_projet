/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server.TCPInterface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heigvd.gen.DBInterface.DBInterface;
import com.heigvd.gen.DBInterface.UserInfo;
import com.heigvd.gen.protocol.tcp.TCPProtocol;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.server.Player;
import com.heigvd.gen.server.ServerRoom;
import com.heigvd.gen.useraccess.UserPrivilege;
import com.heigvd.gen.utils.JSONObjectConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This worker state represent the communication options inside a waiting ServerRoom
 * It should keep track of the room from which it  manages the communication.
 * 
 * It observes the room in order to communicate the changes that occur inside of it.
 * 
 * @author mathieu
 */
public class WorkerRoomState extends WorkerState implements Observer {
   
   // The room in which the player is right now
   private ServerRoom room;
   
   /**
    * Constructor to pass on the state informations
    * @param worker the worker
    * @param in the reader
    * @param out the writer
    * @param room the room we are inside of
    * @throws JsonProcessingException 
    */
   public WorkerRoomState(TCPServerWorker worker, BufferedReader in, PrintWriter out, ServerRoom room) throws JsonProcessingException {
      super(worker, in, out);
      this.room = room;
      room.addObserver(this);
      sendRoomInformations();
   }
   
   /**
    * The manageClient purpose is to wait for the client to say it is ready to play.
    * 
    */
   @Override
   public void manageClient(String line) throws IOException {
      try {
         if (line == null) {
            throw new IOException("Disconnected");
         }
         
         // If the user say it is ready
         if (line.equals(TCPProtocol.USER_READY)) {
            room.setPlayerReady(worker.getPlayer());
         } 
         // If the user actively asks for the information
         else if (line.equals(TCPProtocol.GET_ROOM_INFOS)) {
            sendRoomInformations();
         } else if (line.equals(TCPProtocol.QUIT_ROOM)) {
            room.deleteObserver(this);
            room.removePlayer(worker.getPlayer());
            worker.setState(new WorkerDefaultState(worker, in, out));
         } else if (line.equals(TCPProtocol.BAN_USER)) {
            String username = in.readLine();
            
            DBInterface dbi = worker.getServer().getDatabaseInterface();
            UserInfo userToBan = dbi.getUserInfo(username);
            if (UserPrivilege.isAdmin(worker.getPlayer().getPrivilege().ordinal())
                    && !UserPrivilege.isAdmin(userToBan.getRole())) {
               room.banUser(username);
               write(TCPProtocol.SUCCESS);
            } else {
               notifyError(TCPProtocol.WRONG_COMMAND);
            }
            
         } else {
            notifyError(TCPProtocol.WRONG_COMMAND);
         }
         
         
      } catch (IOException ex) {
         room.deleteObserver(this);
         System.out.println("The player was unexpectedly disconnected...");
         System.out.println("Removing the player from the room...");
         room.removePlayer(worker.getPlayer());
         throw new IOException("User disconnected");
      } catch (SQLException ex) {
         Logger.getLogger(WorkerRoomState.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * On room update, the state will norify its player of the room informations
    * @param o
    * @param arg 
    */
   @Override
   public void update(Observable o, Object arg) {
      if (o == room) {
         
         if (room.isBanned(worker.getPlayer()) || room.isDeleted()) {
            write(TCPProtocol.DISCONNECTION);
            room.deleteObserver(this);
            worker.setState(new WorkerDefaultState(worker, in, out));
            return;
         }
         
         try {
            sendRoomInformations();
            System.out.println("Sending room info");
         } catch (JsonProcessingException ex) {
            Logger.getLogger(WorkerRoomState.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         if (room.isReady()) {
            write(TCPProtocol.RACE_START);
            System.out.println("Sending Race start");
         }
      }
   }
   
   /**
    * Sends room information to its player.
    * @throws JsonProcessingException 
    */
   private void sendRoomInformations() throws JsonProcessingException {
      write(TCPProtocol.ROOM_INFOS);
      TCPRoomInfoMessage roomInfo = new TCPRoomInfoMessage();
      roomInfo.setName(room.getName());
      roomInfo.setID(room.getID());
      List<Player> players = room.getPlayers();
      for (Player p : players) {
         roomInfo.addPlayer(new TCPPlayerInfoMessage(p.getUsername(), p.getState().name()));
      }
      write(JSONObjectConverter.toJSON(roomInfo));
   }
   
}
