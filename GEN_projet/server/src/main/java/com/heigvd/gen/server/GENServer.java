/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server;

import com.heigvd.gen.server.TCPInterface.TCPServer;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author mathieu
 */
public class GENServer {

   private LinkedList<ServerRoom> rooms;
   private TCPServer tcpServer;
   
   public GENServer() {
      rooms = new LinkedList<>();
      
      // Create 3 rooms
      rooms.add(new ServerRoom("Funny room"));
      rooms.add(new ServerRoom("Competition room"));
      rooms.add(new ServerRoom("OkeyDokey room"));
      
      // Create the TCPServer
      tcpServer = new TCPServer(this, 2525);
      new Thread( tcpServer ).start();
   }
   
   public List<ServerRoom> getServerRooms() {
      return rooms;
   }
   
   public TCPServer getTCPServer() {
      return tcpServer;
   }
   
   public static void main(String[] args) {
      GENServer server = new GENServer();
   }
   
}
