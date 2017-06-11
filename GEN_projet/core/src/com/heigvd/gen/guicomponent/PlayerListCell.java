package com.heigvd.gen.guicomponent;

import com.heigvd.gen.useraccess.UserPrivilege;

/**
 *
 * @author mathieu
 */
public class PlayerListCell {

   private String username;
   private String state;
   private int role;

   public PlayerListCell(String username) {
      this(username, null, -1);
   }

   public PlayerListCell(String username, String state) {
      this(username, state, -1);
   }
   
   public PlayerListCell(String username, String state, int role) {
      this.username = username;
      this.state = state;
      this.role = role;
   }

   public String getUsername() {
      return username;
   }

   public String getState() {
      return state;
   }
   
   public int getRole() {
      return role;
   }

   @Override
   public String toString() {
      String output = String.format("%s", getUsername());
      if (state != null) {
         output += " - " + String.format("%s", getState());
      }
      if (role != -1) {
         output += " - " + String.format("%s", UserPrivilege.Privilege.values()[getRole()].toString());
      }

      return output;
   }
}
