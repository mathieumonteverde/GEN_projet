package com.heigvd.gen.DBInterface;

/**
 *
 * @author mathieu
 */
public class UserInfo {
   private String username;
   private int role;
   
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
   
   public int getRole() {
      return role;
   }
   
   
}
