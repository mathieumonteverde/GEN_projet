package com.heigvd.gen.protocol.tcp;

/**
 * This class contains the TCP protocol specification. It contains all commands 
 * exchanged from the client to the server or from the server to the client.
 * 
 */
public class TCPProtocol {
   
   /*
    * User commands
   */
   public static final String REGISTER_USER = "REGISTER_USER";
   public static final String CONNECT_USER = "CONNECT_USER";
   public static final String DISCONNECT_USER = "DISCONNECT_USER";
   public static final String BAN_USER = "BAN_USER";
   public static final String USER_RIGHTS = "USER_RIGHTS";
   public static final String GET_USERS = "GET_USERS";
   public static final String GET_SCORES = "GET_SCORES";
   public static final String LIST_ROOMS = "LIST_ROOMS";
   public static final String JOIN_ROOM = "JOIN_ROOM";
   public static final String QUIT_ROOM = "QUIT_ROOM";
   public static final String ROOM_INFOS = "ROOM_INFOS";
   public static final String CREATE_ROOMS = "CREATE_ROOMS";
   public static final String DELETE_ROOMS = "DELETE_ROOM";
   public static final String USER_READY = "USER_READY";
   public static final String USER_FINISHED = "USER_FINISHED";
   
   
   /**
    * Server commands
    */
   public static final String ROOM_DISCONNECTION = "ROOM_DISCONNECTION";
   public static final String RACE_START = "RACE_START";
   public static final String RACE_END = "RACE_END";
   public static final String COUNTDOWN = "COUNTDOWN";
   
   
   /*
    * General commands 
   */
   public static final String SUCCESS = "SUCCESS";
   public static final String ERROR = "ERROR";
   
   /*
    * Error descriptions
   */
   public static final String BAD_AUTHENTIFICATION = "BAD_AUTHENTIFICATION";
   public static final String FULL_ROOM = "FULL_ROOM";
   public static final String WRONG_ROOM_ID = "WRONG_ROOM_ID";
   public static final String USED_USERNAME = "USED_USERNAME";
   public static final String WRONG_COMMAND = "WRONG_COMMAND";
   public static final String BANNED_USER = "BANNED_USER";
}
