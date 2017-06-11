package com.heigvd.gen.client.TCPClient;

import com.heigvd.gen.protocol.tcp.TCPProtocol;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.protocol.tcp.message.TCPScoreMessage;
import com.heigvd.gen.useraccess.UserPrivilege;
import com.heigvd.gen.utils.JSONObjectConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class TCPClient implements Runnable {

   private final Socket socket;
   private final BufferedReader in;
   private final PrintWriter out;
   private TCPClientListener listener;

   private String currentCommand = "";

   /**
    * Construct a TCPClient connecting to a given IP address and a given port.
    *
    * @param address the IP address of the server
    * @param port the port number on which to connect
    * @param listener
    * @throws java.io.IOException
    */
   public TCPClient(String address, int port, TCPClientListener listener) throws IOException {
      socket = new Socket(address, port);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream());
      this.listener = listener;
   }

   private void write(String s) {
      out.println(s);
      out.flush();
   }

   /**
    * Connect a user to the server
    *
    * @param username the username
    * @param password the password
    */
   public void connectUser(String username, String password) {
      currentCommand = TCPProtocol.CONNECT_USER;
      write(TCPProtocol.CONNECT_USER);
      write(username);
      write(password);
   }

   /**
    * Register a new user to the server
    *
    * @param mail the mail of the user
    * @param username the username
    * @param password the password
    */
   public void registerUser(String username, String password) {
      currentCommand = TCPProtocol.REGISTER_USER;
      write(TCPProtocol.REGISTER_USER);
      write(username);
      write(password);
   }

   /**
    * Get a list of active rooms on the server. The rooms are represented by a
    * TCPRoomInfoMessage from the protocol package.
    *
    * @return a list of rooms as TCPRoomInfoMessage
    */
   public void listRooms() throws IOException {
      currentCommand = TCPProtocol.LIST_ROOMS;
      write(TCPProtocol.LIST_ROOMS);
   }

   /**
    * Join a given room by a given ID
    *
    * @param roomID the ID of the room to join
    * @return
    * @throws java.io.IOException
    */
   public void joinRoom(String roomID) throws IOException {
      currentCommand = TCPProtocol.JOIN_ROOM;
      write(TCPProtocol.JOIN_ROOM);
      write(roomID);
   }
   
   /**
    * Ask for the information in which the player is 
    * @throws IOException 
    */
   public void getRoomInfo() throws IOException {
      currentCommand = TCPProtocol.GET_ROOM_INFOS;
      write(TCPProtocol.GET_ROOM_INFOS);
   }
   
   /**
    * Signal that the player is ready
    */
   public void playerReady() {
      write(TCPProtocol.USER_READY);
   }
   
   /**
    * Signal that the players quits a room
    */
   public void quitRoom() {
      write(TCPProtocol.QUIT_ROOM);
   }
   
   /**
    * Get a list of all scores
    */
   public void getScores() {
      currentCommand = TCPProtocol.GET_SCORES;
      write(TCPProtocol.GET_SCORES);
      write("");
   }
   
   /**
    * Disconnect the user
    */
   public void disconnect() {
      write(TCPProtocol.DISCONNECT_USER);
   }
   
   /**
    * Get a list of the scores of the user username
    * @param username the username which scores we are interested in
    */
   public void getScores(String username) {
      currentCommand = TCPProtocol.GET_SCORES;
      write(TCPProtocol.GET_SCORES);
      write(username);
   }
   
   public void createRoom(String name) {
      write(TCPProtocol.CREATE_ROOMS);
      write(name);
   }
   
   public void deleteRoom(String id) {
      write(TCPProtocol.DELETE_ROOMS);
      write(id);
   }
   
   public void banUser(String username) {
      currentCommand = TCPProtocol.BAN_USER;
      write(TCPProtocol.BAN_USER);
      write(username);
   }
   
   public void getUsers() {
      currentCommand = TCPProtocol.GET_USERS;
      write(TCPProtocol.GET_USERS);
   }
   
   public void userRights(String username, UserPrivilege.Privilege newRights) {
      currentCommand = TCPProtocol.USER_RIGHTS;
      write(TCPProtocol.USER_RIGHTS);
      write(username);
      write(newRights.toString());
   }

   private void listenServer() throws IOException {
      String answer = in.readLine();
      
      if (answer == null) {
         throw new IOException();
      }
      
      System.out.println("Context: " + currentCommand + " - msg : " + answer);
      
      if (answer.equals(TCPProtocol.DISCONNECTION)) {
         listener.disconnection();
         return;
      }
      if (currentCommand.equals(TCPProtocol.BAN_USER)) {
         if (answer.equals(TCPProtocol.SUCCESS)) {
            listener.banUser();
         } else if (answer.equals(TCPProtocol.ERROR)){
            String error = in.readLine();
            listener.errorNotification(TCPErrors.Error.WRONG_COMMAND);
         }
      } else if (currentCommand.equals(TCPProtocol.USER_RIGHTS)) {
         if (answer.equals(TCPProtocol.SUCCESS)) {
            listener.userRights();
         }
      } else if (currentCommand.equals(TCPProtocol.GET_USERS)) {
         if (answer.equals(TCPProtocol.SUCCESS)) {
            String line = in.readLine();
            List<TCPPlayerInfoMessage> users = Arrays.asList(JSONObjectConverter.fromJSON(line, TCPPlayerInfoMessage[].class));
            listener.getUsers(users);
         }
      } else if (currentCommand.equals(TCPProtocol.LIST_ROOMS)) {
         List<TCPRoomMessage> rooms = Arrays.asList(JSONObjectConverter.fromJSON(answer, TCPRoomMessage[].class));
         listener.listRooms(rooms);

      } else if (currentCommand.equals(TCPProtocol.JOIN_ROOM)) {

         if (answer.equals(TCPProtocol.SUCCESS)) {
            String roomInfoCmd = in.readLine();
            if (roomInfoCmd.equals(TCPProtocol.ROOM_INFOS)) {
               String roomInfo = in.readLine();
               TCPRoomInfoMessage msg = JSONObjectConverter.fromJSON(roomInfo, TCPRoomInfoMessage.class);
               listener.joinRoom();
            }
         } else if (answer.equals(TCPProtocol.ERROR)) {
            String error = in.readLine();
            if (error.equals(TCPProtocol.FULL_ROOM)) {
               listener.errorNotification(TCPErrors.Error.FULL_ROOM);
            } else if (error.equals(TCPProtocol.BANNED_USER)) {
               listener.errorNotification(TCPErrors.Error.BANNED_USER);
            } else if (error.equals(TCPProtocol.WRONG_ROOM_ID)) {
               listener.errorNotification(TCPErrors.Error.WRONG_ROOM_ID);
            }
            
         }

      } else if (currentCommand.equals(TCPProtocol.GET_ROOM_INFOS)) {

         if (answer.equals(TCPProtocol.ROOM_INFOS)) {
            String roomInfo = in.readLine();
            TCPRoomInfoMessage msg = JSONObjectConverter.fromJSON(roomInfo, TCPRoomInfoMessage.class);
            listener.roomInfo(msg);
         } else if (answer.equals(TCPProtocol.RACE_START)) {
            System.out.println("Race Start !");
         } else if (answer.equals(TCPProtocol.ERROR)) {
            String error = in.readLine();
         }
      } else if (currentCommand.equals(TCPProtocol.CONNECT_USER)) {
         if (answer.equals(TCPProtocol.SUCCESS)) {
            int role = Integer.parseInt(in.readLine());
            listener.connectUser(role);
         } else if (answer.equals(TCPProtocol.ERROR)){
            String error = in.readLine();
            if (error.equals(TCPProtocol.BAD_AUTHENTIFICATION)) {
               listener.errorNotification(TCPErrors.Error.BAD_AUTHENTIFICATION);
            }
         }
      } else if (currentCommand.equals(TCPProtocol.REGISTER_USER)) {
         if (answer.equals(TCPProtocol.SUCCESS)) {
            listener.registerUser();
         } else if (answer.equals(TCPProtocol.ERROR)){
            String error = in.readLine();
            
            if (error.equals(TCPProtocol.USED_USERNAME)) {
               listener.errorNotification(TCPErrors.Error.USED_USERNAME);
            }
         }
      } else if (currentCommand.equals(TCPProtocol.GET_SCORES)) {
         List<TCPScoreMessage> scores = Arrays.asList(JSONObjectConverter.fromJSON(answer, TCPScoreMessage[].class));
         listener.getScores(scores);
      }
      
   }
   
   public void setListener(TCPClientListener listener) {
      this.listener = listener;
   }

   public void run() {
      while (true) {
         try {
            listenServer();

         } catch (IOException ex) {
            try {
               socket.close();
            } catch (IOException ex1) {
               Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return;
         }
      }
   }

}
