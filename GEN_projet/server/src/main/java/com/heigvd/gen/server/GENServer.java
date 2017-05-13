/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server;

import com.heigvd.gen.DBInterface.DBInterface;
import com.heigvd.gen.exception.BadAuthentificationException;
import com.heigvd.gen.exception.UsedUsernameException;
import com.heigvd.gen.server.TCPInterface.TCPServer;
import com.heigvd.gen.useraccess.UserPrivilege;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main server application
 *
 * @author mathieu
 */
public class GENServer {

   // List of rooms
   private LinkedList<ServerRoom> rooms;
   // The TCPServer object
   private TCPServer tcpServer;

   /**
    * Constructor, creates the TCPServer active object
    */
   public GENServer() {
      rooms = new LinkedList<>();

      // Create 3 rooms
      rooms.add(new ServerRoom("Funny room"));
      rooms.add(new ServerRoom("Competition room"));
      rooms.add(new ServerRoom("OkeyDokey room"));

      // Create the TCPServer
      tcpServer = new TCPServer(this, 2525);
      new Thread(tcpServer).start();
   }

   /**
    * Returns the rooms on the server
    *
    * @return
    */
   public List<ServerRoom> getServerRooms() {
      return rooms;
   }

   /**
    * Returns the TCP server
    *
    * @return
    */
   public TCPServer getTCPServer() {
      return tcpServer;
   }

   /**
    * Return a specific room by ID
    *
    * @param ID the ID of the room
    * @return the room of null if invalid ID
    */
   public ServerRoom getServerRoom(String ID) {
      for (ServerRoom room : rooms) {
         if (room.getID().equals(ID)) {
            return room;
         }
      }
      return null;
   }

   /**
    * Main app function
    *
    * @param args
    */
   public static void main(String[] args) {
      GENServer server = new GENServer();
   }

}
