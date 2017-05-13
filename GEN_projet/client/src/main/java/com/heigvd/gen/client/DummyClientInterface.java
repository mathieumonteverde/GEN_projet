/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.client;

import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author mathieu
 */
public class DummyClientInterface extends GridPane {

   private TCPClient tcpClient;

   public DummyClientInterface() {
      // Set design
      setAlignment(Pos.CENTER);
      setHgap(10);
      setVgap(10);
      setPadding(new Insets(25, 25, 25, 25));

      // Add title
      Text connection = new Text("Connection");

      // Add a Connect Button
      Button connect = new Button("Connect to server");
      connect.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent e) {
            try {
               // Try to connect to the server
               tcpClient = new TCPClient("localhost", 2525);
               System.out.println("Connected to the server on port 2525.");
            } catch (IOException ex) {
               System.out.println("Error: No server seem to be currently running...");
               Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      });

      // text rooms
      Text rooms = new Text("Rooms");

      // Add a button to list rooms
      Button listRooms = new Button("List rooms");
      listRooms.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent e) {
            try {
               List<TCPRoomMessage> rooms = tcpClient.listRooms();
               for (TCPRoomMessage room : rooms) {
                  System.out.println(room);
               }

            } catch (IOException ex) {
               System.out.println("Error : Couldn't retrieve rooms list.");
               System.out.println("Cause: ");
               Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      });

      // Add a room id text field
      final TextField roomIDField = new TextField();

      // Add a Button to join a room
      Button joinRoom = new Button("Join room by ID");
      joinRoom.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent e) {
            String roomID = roomIDField.getText();
            try {
               TCPRoomInfoMessage msg = tcpClient.joinRoom(roomID);
               System.out.println(msg);
            } catch (IOException ex) {
               Logger.getLogger(DummyClientInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      });
      
      // Button to get Room info
      Button getRoomInfo = new Button("Refresh room info");
      getRoomInfo.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent e) {
            try {
               TCPRoomInfoMessage msg;
               msg = tcpClient.getRoomInfo();
               System.out.println(msg);
            } catch (IOException ex) {
               Logger.getLogger(DummyClientInterface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
               Logger.getLogger(DummyClientInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      });

      add(connection, 0, 0);
      add(connect, 0, 1);
      add(rooms, 0, 2);
      add(listRooms, 0, 3);
      add(roomIDField, 0, 4);
      add(joinRoom, 0, 5);
      add(getRoomInfo, 0, 6);

   }
}
