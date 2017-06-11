package com.heigvd.gen;

/**
 *
 * @author mathieu
 */
public class Player {
   private String username;
   private String password;
   private int role;
   
   private static Player instance = null;
   
   private Player() {
      this(null, null, -1);
   }
   
   private Player(String username) {
      this(username, null,  -1);
   }
   
   private Player(String username, String password) {
      this(username, password,  -1);
   }
   
   private Player(String username, String password, int role) {
      this.username = username;
      this.password = password;
      this.role = role;
   }
   
   public static Player getInstance() {
      if (instance == null) {
         instance = new Player();
      }
      return instance;
   }

   /**
    * @return the username
    */
   public String getUsername() {
      return username;
   }

   /**
    * @param username the username to set
    */
   public void setUsername(String username) {
      this.username = username;
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

   /**
    * @return the password
    */
   public String getPassword() {
      return password;
   }

   /**
    * @param password the password to set
    */
   public void setPassword(String password) {
      this.password = password;
   }
   
}
