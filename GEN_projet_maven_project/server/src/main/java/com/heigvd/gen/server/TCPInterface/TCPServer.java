/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server.TCPInterface;

import com.heigvd.gen.server.GENServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is an active object that manages TCPCommunication of the 
 * server.
 * 
 * It create workers (TCPServerWorker) when a client arrives and let them manage 
 * their own clients.
 * @author mathieu
 */
public class TCPServer implements Runnable{

   private int port; // The port on which to create the TCPServer
   private GENServer mainServer; // The main server application
   private ServerSocket server; 
   LinkedList<Thread> threads = new LinkedList<>();
   
   private boolean isShutDownRequested = false;
   
   /**
    * Constructor
    * @param mainServer
    * @param port 
    */
   public TCPServer(GENServer mainServer, int port) {
      this.port = port;
      this.mainServer = mainServer;
   }

   /**
    * Accept clients and create Workers.
    */
   @Override
   public void run() {
      try {
         // Create the server socket
         server = new ServerSocket(port);
         while (true) {
            Socket socket = server.accept();
            System.out.println("New client...");
            Thread t = new Thread(new TCPServerWorker(mainServer, socket));
            threads.add(t);
            t.start();
         }

      } catch (IOException ex) {
         if (isShutDownRequested) {
            for (Thread t : threads) {
               t.interrupt();
            }
            System.out.println("Ending the server");
         } else {
            Logger.getLogger(GENServer.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
   
   /**
    * Method to call to request a shut down of the server. 
    * @throws IOException 
    */
   public void shutDown() throws IOException {
      isShutDownRequested = true;
      server.close();
   }
   
}
