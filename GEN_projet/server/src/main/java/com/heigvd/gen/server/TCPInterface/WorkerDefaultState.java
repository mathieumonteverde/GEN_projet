/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server.TCPInterface;

import com.heigvd.gen.DBInterface.DBInterface;
import com.heigvd.gen.protocol.tcp.TCPProtocol;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.server.Player;
import com.heigvd.gen.server.Score;
import com.heigvd.gen.server.ServerRoom;
import com.heigvd.gen.utils.JSONObjectConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the default state of a TCPServerWorker. Typically, it
 * will manage the different actions a client can make when it is already logged
 * in and not in a waiting ServerRoom.
 *
 * For example it manages LIST_ROOMS, JOIN_ROOM and other commands that don't
 * belong in a particular section of the communication process lifecycle.
 *
 * @author mathieu
 */
public class WorkerDefaultState extends WorkerState {

   /**
    * Constructor to call when creating the state the first time when the worker
    * is created
    *
    * @param worker the worker to be the state of
    * @param socket the socket
    * @throws IOException
    */
   public WorkerDefaultState(TCPServerWorker worker, Socket socket) throws IOException {
      super(worker, socket);
   }

   /**
    * Constructor to pass on the information to manage the worker
    *
    * @param worker
    * @param in
    * @param out
    */
   public WorkerDefaultState(TCPServerWorker worker, BufferedReader in, PrintWriter out) {
      super(worker, in, out);
   }

   /**
    * For this state, the manageClient method tests the commands and take action
    * in consequences.
    */
   @Override
   public void manageClient() throws IOException {
      try {
         String line = in.readLine();

         if (line == null) {
            throw new IOException("Disconnected");
         }

         // If the player asks to list the rooms
         if (line.equals(TCPProtocol.LIST_ROOMS)) {
            List<ServerRoom> rooms = worker.getServer().getServerRooms();
            LinkedList<TCPRoomMessage> msgs = new LinkedList<>();
            for (ServerRoom room : rooms) {
               msgs.add(new TCPRoomMessage(room.getName(), room.getID(), room.getPlayers().size()));
            }

            String roomList = JSONObjectConverter.toJSON(msgs);
            write(roomList);
         } // If the player asks to join a room
         else if (line.equals(TCPProtocol.JOIN_ROOM)) {
            String roomID = in.readLine();
            ServerRoom room = worker.getServer().getServerRoom(roomID);
            if (room == null) {
               notifyError(TCPProtocol.WRONG_ROOM_ID);
            } else {
               try {
                  room.addPlayer(worker.getPlayer());
                  write(TCPProtocol.SUCCESS);
                  // Change the current state to a RoomState
                  worker.setState(new WorkerRoomState(worker, in, out, room));
               } catch (Exception e) {
                  notifyError(TCPProtocol.FULL_ROOM);
               }
            }
         } else if (line.equals(TCPProtocol.GET_SCORES)) {
            DBInterface dbi = worker.getServer().getDatabaseInterface();
            
            // Read the usernamte
            String username = in.readLine();
            List<Score> scores;
            if (username.length() == 0) {
               scores = dbi.getScores(null);
            } else {
               scores = dbi.getScores(username);
            }
            
            write("" + scores.size());

         } else {
            notifyError(TCPProtocol.WRONG_COMMAND);
         }

      } catch (IOException ex) {
         System.out.println("The User was disconnected...");
         throw new IOException();
      } catch (SQLException ex) {
         Logger.getLogger(WorkerDefaultState.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
