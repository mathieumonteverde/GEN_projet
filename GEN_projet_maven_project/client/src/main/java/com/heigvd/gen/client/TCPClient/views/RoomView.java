/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.client.TCPClient.views;

import com.heigvd.gen.client.DummyClientInterface;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPClientListener;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author mathieu
 */
public class RoomView extends GridPane implements TCPClientListener {
   
   private DummyClientInterface parent;
   private Stage primaryStage;
   private TCPClient tcpClient;
   private VBox playerList;
   
   public RoomView(DummyClientInterface parent, Stage primaryStage, TCPClient tcpClient) {
      this.parent = parent;
      this.primaryStage = primaryStage;
      this.tcpClient = tcpClient;
      this.tcpClient.setListener(this);
      playerList = new VBox();
      
      
      setAlignment(Pos.CENTER);
      setHgap(10);
      setVgap(10);
      setPadding(new Insets(25, 25, 25, 25));

      

      

      // Button to get Room info
      Button getRoomInfo = new Button("Refresh room info");
      getRoomInfo.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent e) {
            try {
               TCPRoomInfoMessage msg;
               RoomView.this.tcpClient.getRoomInfo();
            } catch (IOException ex) {
               Logger.getLogger(DummyClientInterface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
               Logger.getLogger(DummyClientInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      });
      add(getRoomInfo, 0, 1);
      add(playerList, 0, 2);
   }

   public void listRooms(List<TCPRoomMessage> rooms) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void joinRoom() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void roomInfo(TCPRoomInfoMessage msg) {
      final TCPRoomInfoMessage m = msg;
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            playerList.getChildren().clear();
            List<TCPPlayerInfoMessage> players = m.getPlayers();
            for (TCPPlayerInfoMessage p : players) {
               playerList.getChildren().add(new Text(p.getUsername() + " - " + p.getState()));
            }
         }
      });
      System.out.println(msg);
   }

   public void connectUser() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void registerUser() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void errorNotification(TCPErrors.Error error) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
   
}
