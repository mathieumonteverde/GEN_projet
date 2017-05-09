/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server.TCPInterface;

import com.heigvd.gen.protocol.tcp.TCPProtocol;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.server.Player;
import com.heigvd.gen.server.ServerRoom;
import com.heigvd.gen.utils.JSONObjectConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class WorkerDefaultState extends WorkerState {

   public WorkerDefaultState(TCPServerWorker worker, Socket socket) throws IOException {
      super(worker, socket);
   }

   public WorkerDefaultState(TCPServerWorker worker, BufferedReader in, PrintWriter out) {
      super(worker, in, out);
   }

   @Override
   public void manageClient() {
      try {
         String line = in.readLine();
         if (line.equals(TCPProtocol.LIST_ROOMS)) {
            List<ServerRoom> rooms = worker.getServer().getServerRooms();
            LinkedList<TCPRoomMessage> msgs = new LinkedList<>();
            for (ServerRoom room : rooms) {
               msgs.add(new TCPRoomMessage(room.getName(), room.getID(), 10));
            }

            String roomList = JSONObjectConverter.toJSON(msgs);
            write(roomList);
         } else if (line.equals(TCPProtocol.JOIN_ROOM)) {
            String roomID = in.readLine();
            ServerRoom room = worker.getServer().getServerRoom(roomID);
            if (room == null) {
               write(TCPProtocol.ERROR);
            } else {
               try {
                  room.addPlayer(worker.getPlayer());
                  write(TCPProtocol.SUCCESS);
                  worker.SetState(new WorkerRoomState(worker, in, out, room));
               } catch (Exception e) {
                  write(TCPProtocol.ERROR);
               }
            }
         }

      } catch (IOException ex) {
         Logger.getLogger(WorkerDefaultState.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
