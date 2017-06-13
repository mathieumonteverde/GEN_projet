package com.heigvd.gen.protocol.udp.message;

/**
 *
 */
public class UDPMessage {
   private String type;
   
   public enum TYPE {
      PLAYER_MESSAGE, 
      RACE_MESSAGE, 
      START_SIGNAL
   }
   
   public UDPMessage() {
      type = null;
   }

   /**
    * @return the type
    */
   public String getType() {
      return type;
   }

   /**
    * @param type the type to set
    */
   public void setType(String type) {
      this.type = type;
   }
}
