package com.heigvd.gen.protocol.udp.message;

/**
 * UDP Protocol messages that can be exchanged between the server and the client.
 */
public class UDPMessage {
   
   // Type of message (String representation of the TYPE enum)
   private String type;
   
   // Enum to represent types of messages
   public enum TYPE {
      PLAYER_MESSAGE, 
      RACE_MESSAGE
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
