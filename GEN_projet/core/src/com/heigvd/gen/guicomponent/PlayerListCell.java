package com.heigvd.gen.guicomponent;

/**
 *
 * @author mathieu
 */
public class PlayerListCell {

   private String username;
   private String state;

   public PlayerListCell(String username, String state) {
      this.username = username;
      this.state = state;
   }

   public String getUsername() {
      return username;
   }

   public String getState() {
      return state;
   }

   public String toString() {
      return String.format("%1$-20s", getUsername())
              + " - " + String.format("%1$20s", getState());
   }
}
