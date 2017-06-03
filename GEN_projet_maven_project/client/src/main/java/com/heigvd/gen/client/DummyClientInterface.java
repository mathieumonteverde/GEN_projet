/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.client;

import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.client.TCPClient.views.ConnectionView;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author mathieu
 */
public class DummyClientInterface extends GridPane {

   private TCPClient tcpClient;
   private final Stage primaryStage;
   private Node view;

   public DummyClientInterface(Stage primaryStage) {

      this.primaryStage = primaryStage;
      
      this.view = new ConnectionView(this, primaryStage);
      
      getChildren().add(view);

   }

   public void errorNotification(TCPErrors.Error error) {

      /*
         Error management 
       */
      switch (error) {
         case CONNECTION_TO_SERVER:
            System.out.println("Error: Unable to connect to the server.");
            break;

         case BAD_AUTHENTIFICATION:
            System.out.println("Error: Wrong username or password.");
            break;

         case USED_USERNAME:
            System.out.println("Error: This username is already used.");
            break;

         case WRONG_ROOM_ID:
            System.out.println("Error: The room ID is invalid.");
            break;

         case FULL_ROOM:
            System.out.println("Error: this room is full.");
            break;
      }
   }
   
   public void setView(Node view) {
      getChildren().remove(this.view);
      this.view = view;
      getChildren().add(view);
   }
}
