/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server.TCPInterface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heigvd.gen.protocol.tcp.TCPProtocol;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.server.Player;
import com.heigvd.gen.server.ServerRoom;
import com.heigvd.gen.utils.JSONObjectConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class WorkerRoomState extends WorkerState implements Observer {
   
   // The room in which the player is right now
   private ServerRoom room;
   
   public WorkerRoomState(TCPServerWorker worker, BufferedReader in, PrintWriter out, ServerRoom room) throws JsonProcessingException {
      super(worker, in, out);
      this.room = room;
      room.addObserver(this);
      sendRoomInformations();
   }

   @Override
   public void manageClient() {
      try {
         String line = in.readLine();
         if (line.equals(TCPProtocol.USER_READY)) {
            worker.getPlayer().setState(Player.State.READY);
            sendRoomInformations();
         } else if (line.equals(TCPProtocol.GET_ROOM_INFOS)) {
            sendRoomInformations();
         }
         
         
      } catch (IOException ex) {
         Logger.getLogger(WorkerRoomState.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Override
   public void update(Observable o, Object arg) {
      if (o == room) {
         try {
            sendRoomInformations();
         } catch (JsonProcessingException ex) {
            Logger.getLogger(WorkerRoomState.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
   
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
