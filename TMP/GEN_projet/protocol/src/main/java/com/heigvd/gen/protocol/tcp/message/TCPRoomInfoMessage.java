package com.heigvd.gen.protocol.tcp.message;

import java.util.LinkedList;
import java.util.List;

/**
 * These messages objetcs represent the information sent by the server
 * to the players inside a ServerRoom. These object are sent during the waiting
 * period.
 * @author mathieu
 */
public class TCPRoomInfoMessage {
   private String name; // Name of the room
   private String ID; // Id of the toom
   private List<TCPPlayerInfoMessage> players; // List of player information
   
   /**
    * Default constructor
    */
   public TCPRoomInfoMessage() {
      this.name = null;
      this.ID = name;
      players = new LinkedList<>();
   }
   
   /**
    * returns the name of the room
    * @return the name as a String
    */
   public String getName() {
      return name;
   }
   
   /**
    * Set the name of the room
    * @param name the new name
    */
   public void setName(String name) {
      this.name = name;
   }
   
   /**
    * returns the ID of the room
    * @return the id
    */
   public String getID() {
      return ID;
   }
   
   /**
    * Set the ID of the room 
    * @param ID the new ID
    */
   public void setID(String ID) {
      this.ID = ID;
   }
   
   /**
    * Add information concerning a player as a TCPPlayerInfoMessage object.
    * @param p the player information
    */
   public void addPlayer(TCPPlayerInfoMessage p) {
      players.add(p);
   }
   
   /**
    * Set directly a list of players.
    * @param players the list of player informations objects
    */
   public void setPlayers(List<TCPPlayerInfoMessage> players) {
      this.players = players;
   }
   
   /**
    * Returns the list of player informations inside this object
    * @return the player informations objects
    */
   public List<TCPPlayerInfoMessage> getPlayers() {
      return players;
   }
   
}
