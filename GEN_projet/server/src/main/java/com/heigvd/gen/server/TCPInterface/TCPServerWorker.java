/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server.TCPInterface;

import com.heigvd.gen.server.GENServer;
import com.heigvd.gen.server.ServerRoom;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class TCPServerWorker implements Runnable {
   
   private final GENServer server;
   private final Socket socket;
   private final WorkerState state;

   public TCPServerWorker(GENServer server, Socket socket) throws IOException {
      this.server = server;
      this.socket = socket;
      state = new WorkerDefaultState(this, socket);
      System.out.println("New ServerWorker");
   }

   @Override
   public void run() {
      while (true) {
         if (Thread.currentThread().interrupted()) {
            try {
               state.close();
               return;
            } catch (IOException ex) {
               Logger.getLogger(TCPServerWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
         state.manageClient();
      }
   }
   
   public List<ServerRoom> getServerRooms() {
      return server.getServerRooms();
   }
}
