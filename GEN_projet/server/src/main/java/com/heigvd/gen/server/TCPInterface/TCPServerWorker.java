/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server.TCPInterface;

import com.heigvd.gen.server.GENServer;
import com.heigvd.gen.server.Player;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class TCPServerWorker implements Runnable {
   
   private final GENServer server;
   private final Socket socket;
   private WorkerState state;
   private Player player;

   public TCPServerWorker(GENServer server, Socket socket) throws IOException {
      this.server = server;
      this.socket = socket;
      this.player = new Player("Player");
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
   
   public void SetState(WorkerState state) {
      this.state = state;
   }
   
   public GENServer getServer() {
      return server;
   }
   
   public Player getPlayer() {
      return player;
   }
   
   public void setPlayer(Player player) {
      this.player = player;
   }
   
   
}
