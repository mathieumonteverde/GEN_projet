package com.heigvd.gen.protocol.udp;

/**
 *
 */
public class UDPProtocol {
   private static int clientPort = 2527;
   public static final int SERVER_PORT = 2526;
   public static final String MULT_CAST = "239.0.0.1";
   public static final int MAX_LENGTH = 1024;
   
   public synchronized static int nextPort() {
      return clientPort++;
   }
}
