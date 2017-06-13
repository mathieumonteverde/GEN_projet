package com.heigvd.gen.DBInterface;

/**
 * Class to represent user information extracted from the DB. The extracted 
 * data concerns the username and the role index.
 */
public class UserInfo {
   // Username of the user
   private String username;
   // Index of the user rights
   private int role;
   
   /**
    * Constructor to create user information
    * @param username
    * @param role 
    */
   public UserInfo(String username, int role) {
      this.username = username;
      this.role = role;
   }

   /**
    * @return the username
    */
   public String getUsername() {
      return username;
   }
   
   /**
    * @return the role
    */
   public int getRole() {
      return role;
   }
   
}
