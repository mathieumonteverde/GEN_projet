/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server;

/**
 * This class represents a user/player inside the server. 
 * It should contain all useful information to identify a user at a given time. 
 * 
 * Feel free to update this class ;)
 * 
 * @author mathieu
 */
public class Player {
   private String username; // Username
   private State state; // Ist it ready to play or not ?
   
   // Enumeration that represents the Waiting/Ready state of a player inside a room
   public static enum State {WAITING, READY};
   
   /**
    * Constructor
    * @param username 
    */
   public Player(String username) {
      this.username = username;
      this.state = State.WAITING;
   }
   
   /**
    * Returns the username
    * @return 
    */
   public String getUsername() {
      return username;
   }
   
   /**
    * Returns the state
    * @return 
    */
   public State getState() {
      return state;
   }
   
   /**
    * Set the state
    * @param state 
    */
   public void setState(State state) {
      this.state = state;
   }
}
