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
 * Class that represent a TCP communication worker
 * taht manages a single client communication.
 * 
 * It should keep track of the player from the moment it is connected to the 
 * moment it disconnects.
 * 
 * It works using a State pattern in the WorkerState class hierarchy.
 * 
 * @author mathieu
 */
public class TCPServerWorker implements Runnable {
   
   private final GENServer server; // Main server
   private final Socket socket; // In case we need it
   private WorkerState state; // The current state of the worker
   private Player player; // The player it is managing
   
   /**
    * Constructor
    * @param server the main application server
    * @param socket the socket on which to communicate
    * @throws IOException when errors occur
    */
   public TCPServerWorker(GENServer server, Socket socket) throws IOException {
      this.server = server;
      this.socket = socket;
      this.player = new Player("Player");
      state = new WorkerDefaultState(this, socket);
      System.out.println("New ServerWorker");
   }
   
   /**
    * In a while loop, call manageClient of the state object that is in charge.
    */
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
   
   /**
    * Change the current state of the Worker
    * @param state 
    */
   public void setState(WorkerState state) {
      this.state = state;
   }
   
   /**
    * Returns the main server application..
    * //Could use Singleton
    * @return 
    */
   public GENServer getServer() {
      return server;
   }
   
   /**
    * Returns the player it is managing
    * @return 
    */
   public Player getPlayer() {
      return player;
   }
   
   /**
    * Set the player it is managing
    * @param player 
    */
   public void setPlayer(Player player) {
      this.player = player;
   }
   
   
}
