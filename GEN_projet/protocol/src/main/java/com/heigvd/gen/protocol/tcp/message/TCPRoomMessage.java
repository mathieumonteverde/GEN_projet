/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.protocol.tcp.message;

/**
 * This class represents a TCP message describing a room on the 
 * game server. 
 * @author mathieu
 */
public class TCPRoomMessage {
   
   private String name;
   private String ID;
   private int nbPlayers;
   
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
   
   public TCPRoomMessage() {
      this.name = null;
      this.ID = null;
      this.nbPlayers = -1;
   }
   
   /**
    * return the nice user friendly name of the room. 
    * @return the name as a String
    */
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
   
   /**
    * returns the room ID generated with UUID.
    * @return the room ID as a String
    */
   public String getID() {
      return ID;
   }
   public void setID(String ID) {
      this.ID = ID;
   }
   
   /**
    * Return the number of players currently occupying the room.
    * @return the number of players
    */
   public int getNbPlayers() {
      return nbPlayers;
   }
   public void setNbPlayers(int nbPlayers) {
      this.nbPlayers = nbPlayers;
   }
}
