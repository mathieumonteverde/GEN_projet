package com.heigvd.gen.protocol.tcp.message;

/**
 * This class represents a TCP message describing a player information 
 * inside a waiting room. These are sent when updating a client on the status 
 * of the current waiting room in which the client is waiting
 * @author mathieu
 */
public class TCPPlayerInfoMessage {
   private String username; // The username of the player
   private String state; // The state of the player as a String
   private int role;
   
   /**
    * Deafult constructor
    */
   public TCPPlayerInfoMessage() {
      username = null;
      state = null;
      role = -1;
   }
   
   /**
    * Constructor 
    * @param  the username
    * @param state the state ("READY"/"WAITING")
    */
   public TCPPlayerInfoMessage(String username, String state) {
      this.username = username;
      this.state = state;
   }
   
   /**
    * set username
    * @param username 
    */
   public void setUsername(String username) {
      this.username = username;
   }
   
   /**
    * returns the username
    * @return 
    */
   public String getUsername() {
      return username;
   }
   
   /**
    * set the state
    * @param state 
    */
   public void setState(String state) {
      this.state = state;
   }
   
   /**
    * returns the state
    * @return 
    */
   public String getState() {
      return state;
   }
   
   /**
    * Get String representation
    * @return 
    */
   public String toString() {
      String s = "Player information - ";
      s += "Username: " + getUsername() + " - ";
      s += "State: " + getState();
      
      return s;
   }

   /**
    * @return the role
    */
   public int getRole() {
      return role;
   }

   /**
    * @param role the role to set
    */
   public void setRole(int role) {
      this.role = role;
   }
}
