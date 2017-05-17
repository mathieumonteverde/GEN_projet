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
 *
 * @author mathieu
 */
public class TCPServer implements Runnable{

   private int port;
   private GENServer mainServer;
   private ServerSocket server;
   LinkedList<Thread> threads = new LinkedList<>();
   
   private boolean isShutDownRequested = false;
   
   public TCPServer(GENServer mainServer, int port) {
      this.port = port;
      this.mainServer = mainServer;
   }

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
   
   public void shutDown() throws IOException {
      isShutDownRequested = true;
      server.close();
   }
   
}
