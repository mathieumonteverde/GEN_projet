package com.heigvd.gen.client;

import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FXMLController implements Initializable {

   private TCPClient tcpClient;

   @FXML
   private Label label;

   @FXML
   private void handleListRooms(ActionEvent event) {
      try {
         List<TCPRoomMessage> rooms = tcpClient.listRooms();
         for (TCPRoomMessage room : rooms) {
            System.out.println("Room: ------------------------------------------");
            System.out.println("Name: " + room.getName());
            System.out.println("ID: " + room.getID());
            System.out.println("Number of players: " + room.getNbPlayers());
            System.out.println("------------------------------------------------");
            System.out.println("");
         }
         
      } catch (IOException ex) {
         System.out.println("Error : Couldn't retrieve rooms list.");
         System.out.println("Cause: ");
         Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
      }
      System.out.println("You clicked me!");
      label.setText("Hello World!");
   }

   @Override
   public void initialize(URL url, ResourceBundle rb) {

      try {
         tcpClient = new TCPClient("localhost", 2525);
      } catch (IOException ex) {
         Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
