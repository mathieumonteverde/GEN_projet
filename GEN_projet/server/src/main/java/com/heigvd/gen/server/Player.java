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
   
   private float x;
   private float y;
   private float vx;
   private float vy;
   private int color;
   

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

   /**
    * @return the x
    */
   public float getX() {
      return x;
   }

   /**
    * @param x the x to set
    */
   public void setX(float x) {
      this.x = x;
   }

   /**
    * @return the y
    */
   public float getY() {
      return y;
   }

   /**
    * @param y the y to set
    */
   public void setY(float y) {
      this.y = y;
   }

   /**
    * @return the vx
    */
   public float getVx() {
      return vx;
   }

   /**
    * @param vx the vx to set
    */
   public void setVx(float vx) {
      this.vx = vx;
   }

   /**
    * @return the vy
    */
   public float getVy() {
      return vy;
   }

   /**
    * @param vy the vy to set
    */
   public void setVy(float vy) {
      this.vy = vy;
   }

   /**
    * @return the color
    */
   public int getColor() {
      return color;
   }

   /**
    * @param color the color to set
    */
   public void setColor(int color) {
      this.color = color;
   }
}
