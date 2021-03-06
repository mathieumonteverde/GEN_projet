package com.heigvd.gen.client.TCPClient;

import com.heigvd.gen.protocol.tcp.TCPProtocol;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.protocol.tcp.message.TCPScoreMessage;
import com.heigvd.gen.protocol.udp.UDPProtocol;
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
 * Class to interact with the server using a TCP socket. TCPClient objects offer
 * method to send commands to the server and receive server commands or answers.
 *
 * This class uses a reference to a TCPClientListener to call its callback
 * methods when data is received.
 */
public class TCPClient implements Runnable {

   // Socket to communicate with the server
   private final Socket socket;
   // BufferedReader to read data
   private final BufferedReader in;
   // Writer to send data
   private final PrintWriter out;
   // Listener to notify when data is received
   private TCPClientListener listener;

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

   /**
    * Write a command and flush the writer
    *
    * @param s the command to send
    */
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
      write(TCPProtocol.JOIN_ROOM);
      write(roomID);
   }

   /**
    * Ask for the information in which the player is
    *
    * @throws IOException
    */
   public void getRoomInfo() throws IOException {
      write(TCPProtocol.ROOM_INFOS);
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
    *
    * @param username the username which scores we are interested in
    */
   public void getScores(String username) {
      write(TCPProtocol.GET_SCORES);
      write(username);
   }

   /**
    * Create a room from the server
    *
    * @param name the name of the room to create
    */
   public void createRoom(String name) {
      write(TCPProtocol.CREATE_ROOMS);
      write(name);
   }

   /**
    * Delete a room from the server
    *
    * @param id the id of the room to delete
    */
   public void deleteRoom(String id) {
      write(TCPProtocol.DELETE_ROOMS);
      write(id);
   }

   /**
    * Ban a user from the current server room
    *
    * @param username the username of the user to ban
    */
   public void banUser(String username) {
      write(TCPProtocol.BAN_USER);
      write(username);
   }

   /**
    * Retrieve a list of users from the server
    */
   public void getUsers() {
      write(TCPProtocol.GET_USERS);
   }

   /**
    * Change a user rights.
    *
    * @param username the user to change
    * @param newRights the new rights index
    */
   public void userRights(String username, UserPrivilege.Privilege newRights) {
      write(TCPProtocol.USER_RIGHTS);
      write(username);
      write(newRights.toString());
   }

   /**
    * Method to signal that the user player has finished the race
    */
   public void signalFinish() {
      write(TCPProtocol.USER_FINISHED);
   }

   /**
    * Listen in a loop for server communication
    *
    * @throws IOException
    */
   private void listenServer() throws IOException {
      // Read the server communication
      String answer = in.readLine();

      if (answer == null) {
         throw new IOException();
      }

      if (answer.equals(TCPProtocol.ROOM_DISCONNECTION)) {
         listener.disconnection();
         return;
      }

      if (answer.equals(TCPProtocol.CONNECT_USER)) {
         String status = in.readLine();
         if (status.equals(TCPProtocol.SUCCESS)) {
            int role = Integer.parseInt(in.readLine());
            listener.connectUser(role);
         } else if (status.equals(TCPProtocol.ERROR)) {
            String error = in.readLine();
            if (error.equals(TCPProtocol.BAD_AUTHENTIFICATION)) {
               listener.errorNotification(TCPErrors.Error.BAD_AUTHENTIFICATION);
            }
         }
      } else if (answer.equals(TCPProtocol.REGISTER_USER)) {
         String status = in.readLine();
         if (status.equals(TCPProtocol.SUCCESS)) {
            listener.registerUser();
         } else if (status.equals(TCPProtocol.ERROR)) {
            String error = in.readLine();

            if (error.equals(TCPProtocol.USED_USERNAME)) {
               listener.errorNotification(TCPErrors.Error.USED_USERNAME);
            }
         }
      } else if (answer.equals(TCPProtocol.LIST_ROOMS)) {
         String roomString = in.readLine();
         List<TCPRoomMessage> rooms = Arrays.asList(JSONObjectConverter.fromJSON(roomString, TCPRoomMessage[].class));
         listener.listRooms(rooms);
      } else if (answer.equals(TCPProtocol.JOIN_ROOM)) {
         String status = in.readLine();
         if (status.equals(TCPProtocol.SUCCESS)) {
            listener.joinRoom();
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
      } else if (answer.equals(TCPProtocol.GET_SCORES)) {
         String score = in.readLine();
         List<TCPScoreMessage> scores = Arrays.asList(JSONObjectConverter.fromJSON(score, TCPScoreMessage[].class));
         listener.getScores(scores);
      } else if (answer.equals(TCPProtocol.GET_USERS)) {
         String line = in.readLine();
         List<TCPPlayerInfoMessage> users = Arrays.asList(JSONObjectConverter.fromJSON(line, TCPPlayerInfoMessage[].class));
         listener.getUsers(users);
      } else if (answer.equals(TCPProtocol.USER_RIGHTS)) {
         String status = in.readLine();
         if (status.equals(TCPProtocol.SUCCESS)) {
            listener.userRights();
         }
      } else if (answer.equals(TCPProtocol.BAN_USER)) {
         String status = in.readLine();
         if (status.equals(TCPProtocol.SUCCESS)) {
            listener.banUser();
         } else if (status.equals(TCPProtocol.ERROR)) {
            String error = in.readLine();
            listener.errorNotification(TCPErrors.Error.WRONG_COMMAND);
         }
      } else if (answer.equals(TCPProtocol.ROOM_INFOS)) {
         String roomInfo = in.readLine();
         TCPRoomInfoMessage msg = JSONObjectConverter.fromJSON(roomInfo, TCPRoomInfoMessage.class);
         listener.roomInfo(msg);
      } else if (answer.equals(TCPProtocol.QUIT_ROOM)) {
         listener.quitRoom();
      } else if (answer.equals(TCPProtocol.RACE_START)) {
         int port = Integer.parseInt(in.readLine());
         UDPProtocol.SERVER_PORT = port;
         listener.raceStart();
      } else if (answer.equals(TCPProtocol.COUNTDOWN)) {
         int count = Integer.parseInt(in.readLine());
         listener.countDown(count);
      } else if (answer.equals(TCPProtocol.RACE_END)) {
         String scores = in.readLine();
         listener.raceEnd(Arrays.asList(JSONObjectConverter.fromJSON(scores, TCPScoreMessage[].class)));
      }
   }

   /**
    * Set the TCPClientListener
    *
    * @param listener
    */
   public void setListener(TCPClientListener listener) {
      this.listener = listener;
   }

   /**
    * Main method of the TCPClient
    */
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
