/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.client.TCPClient;

import com.heigvd.gen.protocol.tcp.TCPProtocol;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
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

   }

   /**
    * Register a new user to the server
    *
    * @param mail the mail of the user
    * @param username the username
    * @param password the password
    */
   public void registerUser(String mail, String username, String password) {

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

   public void getRoomInfo() throws IOException {
      currentCommand = TCPProtocol.GET_ROOM_INFOS;
      write(TCPProtocol.GET_ROOM_INFOS);
   }

   private void listenServer() throws IOException {
      String answer = in.readLine();
      System.out.println("Context: " + currentCommand + " - msg : " + answer);

      if (currentCommand.equals(TCPProtocol.LIST_ROOMS)) {
         List<TCPRoomMessage> rooms = Arrays.asList(JSONObjectConverter.fromJSON(answer, TCPRoomMessage[].class));
         listener.listRooms(rooms);

      } else if (currentCommand.equals(TCPProtocol.JOIN_ROOM)) {

         if (answer.equals(TCPProtocol.SUCCESS)) {
            String roomInfoCmd = in.readLine();
            if (roomInfoCmd.equals(TCPProtocol.ROOM_INFOS)) {
               String roomInfo = in.readLine();
               TCPRoomInfoMessage msg = JSONObjectConverter.fromJSON(roomInfo, TCPRoomInfoMessage.class);
               listener.joinRoom(null, msg);
            }
         } else if (answer.equals(TCPProtocol.ERROR)) {
            String error = in.readLine();
            listener.joinRoom(error, null);
         }

      } else if (currentCommand.equals(TCPProtocol.GET_ROOM_INFOS)) {

         if (answer.equals(TCPProtocol.ROOM_INFOS)) {
            String roomInfo = in.readLine();
            TCPRoomInfoMessage msg = JSONObjectConverter.fromJSON(roomInfo, TCPRoomInfoMessage.class);
            listener.roomInfo(msg);
         } else if (answer.equals(TCPProtocol.ERROR)) {
            String error = in.readLine();
         }
      }
   }

   public void run() {
      while (true) {
         try {
            listenServer();

         } catch (IOException ex) {
            Logger.getLogger(TCPClient.class
                    .getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

}
