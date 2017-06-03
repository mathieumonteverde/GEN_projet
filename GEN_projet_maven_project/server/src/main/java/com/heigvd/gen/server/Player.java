/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server;

/**
 * This class represents a user/player inside the server. It should contain all
 * useful information to identify a user at a given time.
 *
 * Feel free to update this class ;)
 *
 * @author mathieu
 */
public class Player {

   private String username; // Username
   private String password; // Password of the username
   private State state; // Is the player ready to play or not ?

   // Enumeration that represents the Waiting/Ready state of a player inside a room
   public static enum State {
      WAITING, READY
   };

   /**
    * Constructor
    *
    * @param username
    * @param password
    */
   public Player(String username, String password) {
      this.username = username;
      this.state = State.WAITING;
      this.password = password;
   }

   /**
    * Returns the username
    *
    * @return
    */
   public String getUsername() {
      return username;
   }
   
   public String getPassword() {
      return password;
   }

   /**
    * Returns the state
    *
    * @return
    */
   public State getState() {
      return state;
   }

   /**
    * Set the state
    *
    * @param state
    */
   public void setState(State state) {
      this.state = state;
   }
}
