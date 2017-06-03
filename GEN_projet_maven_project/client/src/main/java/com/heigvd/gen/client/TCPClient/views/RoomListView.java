/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.client.TCPClient.views;

import com.heigvd.gen.client.DummyClientInterface;
import com.heigvd.gen.client.FXMLController;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPClientListener;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.client.TCPClient.views.popup.PopupMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author mathieu
 */
public class RoomListView extends GridPane implements TCPClientListener {

   private DummyClientInterface parent;
   private Stage primaryStage;
   private TCPClient tcpClient;
   private VBox roomList;

   public RoomListView(DummyClientInterface parent, Stage primaryStage, TCPClient tcpClient) {
      this.parent = parent;
      this.primaryStage = primaryStage;
      this.tcpClient = tcpClient;
      this.tcpClient.setListener(this);

      setAlignment(Pos.CENTER);
      setHgap(10);
      setVgap(10);
      setPadding(new Insets(25, 25, 25, 25));

      // text rooms
      Text rooms = new Text("Rooms");

      // Add a button to list rooms
      Button listRooms = new Button("List rooms");
      listRooms.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent e) {
            try {
               RoomListView.this.tcpClient.listRooms();

            } catch (IOException ex) {
               System.out.println("Error : Couldn't retrieve rooms list.");
               System.out.println("Cause: ");
               Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      });

      roomList = new VBox();

      // Add a room id text field
      final TextField roomIDField = new TextField();

      // Add a Button to join a room
      add(rooms, 0, 1);
      add(listRooms, 0, 2);
      add(roomList, 0, 3);
   }

   public void listRooms(List<TCPRoomMessage> rooms) {
      final List<TCPRoomMessage> lol = rooms;
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            for (TCPRoomMessage room : lol) {
               HBox line = new HBox();
               line.getChildren().add(new Text(room.getName()));
               line.getChildren().add(new Separator());
               line.getChildren().add(new Text(room.getNbPlayers() + " joueurs"));
               line.getChildren().add(new Separator());
               final Text id = new Text(room.getID());
               id.setVisible(false);
               Button joinRoom = new Button("Join room by ID");
               joinRoom.setOnAction(new EventHandler<ActionEvent>() {
                  public void handle(ActionEvent e) {
                     String roomID = id.getText();
                     try {
                        RoomListView.this.tcpClient.joinRoom(roomID);
                     } catch (IOException ex) {
                        Logger.getLogger(DummyClientInterface.class.getName()).log(Level.SEVERE, null, ex);
                     }
                  }
               });
               line.getChildren().add(joinRoom);
               roomList.getChildren().add(line);
            }
         }
      });

   }

   public void joinRoom() {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            parent.setView(new RoomView(parent, primaryStage, tcpClient));
         }
      });
   }

   public void roomInfo(TCPRoomInfoMessage msg) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void connectUser() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void registerUser() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void errorNotification(TCPErrors.Error error) {/*
         Error management 
       */
      switch (error) {
         case CONNECTION_TO_SERVER:
            Platform.runLater(new Runnable() {
               @Override
               public void run() {
                  PopupMessage dialog = new PopupMessage("Erreur de connexion au serveur", "Réessayer");
                  dialog.showAndWait();
               }
            });
            break;

         case FULL_ROOM:
            Platform.runLater(new Runnable() {
               @Override
               public void run() {
                  PopupMessage dialog = new PopupMessage("La salle est pleine", "Réessayer");
                  dialog.showAndWait();
               }
            });
            break;
      }
   }
}
