package com.heigvd.gen.client.TCPClient;

/**
 * This class lists the errors that can occur during a TCP communication
 * with the server. 
 * 
 * The error codes are sent to the TCPListener to inform it that something
 * went wrong.
 * 
 * @author mathieu
 */
public class TCPErrors {
   public enum Error {
     CONNECTION_TO_SERVER,
     BAD_AUTHENTIFICATION,
     USED_USERNAME,
     WRONG_ROOM_ID,
     FULL_ROOM
   };
}
