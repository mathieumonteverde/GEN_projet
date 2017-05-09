/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.protocol.tcp.message;

/**
 * This class reprensents a TCP message describing a room on the 
 * game server. 
 * @author mathieu
 */
public class TCPRoomMessage {
   
   private final String name;
   private final String ID;
   private final int nbPlayers;
   
   /**
    * Constructs a RoomMessage containing the name, the ID and the number of players
    * in a room.
    * @param name the userfriendly name of the room
    * @param ID the ID of the room
    * @param nbPlayers the number of players in the room
    */
   public TCPRoomMessage(String name, String ID, int nbPlayers) {
      this.name = name;
      this.ID = ID;
      this.nbPlayers = nbPlayers;
   }
   
   /**
    * return the nice user friendly name of the room. 
    * @return the name as a String
    */
   public String getName() {
      return name;
   }
   
   /**
    * returns the room ID generated with UUID.
    * @return the room ID as a String
    */
   public String getID() {
      return ID;
   }
   
   /**
    * Return the number of players currently occupying the room.
    * @return the number of players
    */
   public int nbPlayers() {
      return nbPlayers;
   }
}
