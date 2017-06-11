/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.protocol.tcp;

/**
 *
 * @author mathieu
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
   
   
   public static final String GET_SCORES = "GET_SCORES";
   
   
   public static final String LIST_ROOMS = "LIST_ROOMS";
   public static final String JOIN_ROOM = "JOIN_ROOM";
   public static final String QUIT_ROOM = "QUIT_ROOM";
   public static final String ROOM_INFOS = "ROOM_INFOS";
   public static final String GET_ROOM_INFOS = "GET_ROOM_INFOS";
   
   public static final String CREATE_ROOMS = "CREATE_ROOMS";
   public static final String DELETE_ROOMS = "DELETE_ROOM";
   
   
   public static final String USER_READY = "USER_READY";
   
   public static final String DISCONNECTION = "DISCONNECTION";
   
   
   public static final String RACE_START = "RACE_START";
   public static final String RACE_END = "RACE_END";
   
   
   
   /*
    * General commands 
   */
   public static final String LISTENING = "LISTENING";
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
}
