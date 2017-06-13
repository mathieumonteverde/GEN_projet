package com.heigvd.gen.protocol.udp;

/**
 * UDP Protocol specification for the GEN project. IT defines the client port
 * on which the multi-cast will be used, the server port on which the server will 
 * listen UDP packets, the multi-cast address on which the server will communicate, 
 * and the upper bound length for exchanged messages.
 */
public class UDPProtocol {
   public static final int CLIENT_PORT = 2527;
   public static final int SERVER_PORT = 2526;
   public static final String MULT_CAST = "239.0.0.1";
   public static final int MAX_LENGTH = 1024;
   
   /**
    * Get the String representation of the type of the JSON object transmitted by 
    * UDP.
    * @param jsonData the JSON representation of the data
    * @return the String representation of the type of message (UDPMessage.TYPE)
    */
   public static String parseJsonObjectType(String jsonData) {
      return jsonData.substring(8).split(",")[0].replace("\"", "");
   }
}
