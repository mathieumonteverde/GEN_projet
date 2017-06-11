package com.heigvd.gen.server;

import com.heigvd.gen.useraccess.UserPrivilege;

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
   private UserPrivilege.Privilege privilege;
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
      this(username, password, UserPrivilege.Privilege.DEFAULT);
   }

   /**
    * Constructor
    *
    * @param username
    * @param password
    * @param privilege
    */
   public Player(String username, String password, UserPrivilege.Privilege privilege) {
      this.username = username;
      this.state = State.WAITING;
      this.password = password;
      this.privilege = privilege;
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

   /**
    * @return the privilege
    */
   public UserPrivilege.Privilege getPrivilege() {
      return privilege;
   }

   /**
    * @param privilege the privilege to set
    */
   public void setPrivilege(UserPrivilege.Privilege privilege) {
      this.privilege = privilege;
   }
}
