/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.client.TCPClient;

import com.heigvd.gen.protocol.tcp.TCPProtocol;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.utils.JSONObjectConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mathieu
 */
public class TCPClient {
   
   private final Socket socket;
   private final BufferedReader in;
   private final PrintWriter out;
   
   /**
    * Construct a TCPClient connecting to a given IP address and a given port.
    * @param address the IP address of the server
    * @param port the port number on which to connect
    */
   public TCPClient(String address, int port) throws IOException {
      socket = new Socket(address, port);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream());
   }
   
   private void write(String s) {
      out.println(s);
      out.flush();
   }
   
   /**
    * Connect a user to the server
    * @param username the username
    * @param password the password
    */
   public void connectUser(String username, String password) {
      
   }
   
   /**
    * Register a new user to the server
    * @param mail the mail of the user
    * @param username the username
    * @param password the password
    */
   public void registerUser(String mail, String username, String password) {
      
   }
   
   /**
    * Get a list of active rooms on the server. The rooms are represented
    * by a TCPRoomInfoMessage from the protocol package.
    * @return a list of rooms as TCPRoomInfoMessage
    */
   public List<TCPRoomInfoMessage> listRooms() throws IOException {
      write(TCPProtocol.LIST_ROOMS);
      String answer = in.readLine();
      
      List<TCPRoomInfoMessage> rooms = Arrays.asList(JSONObjectConverter.fromJSON(answer, TCPRoomInfoMessage[].class));
      
      
      
      return null;
   }
   
   /**
    * Join a given room by a given ID
    * @param roomID the ID of the room to join
    */
   public void joinRoom(String roomID) {
      
   }
   
}
