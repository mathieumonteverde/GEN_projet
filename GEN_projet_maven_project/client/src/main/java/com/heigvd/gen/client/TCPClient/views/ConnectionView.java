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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author mathieu
 */
public class ConnectionView extends GridPane implements TCPClientListener {

   private final DummyClientInterface parent;
   private Stage primaryStage;
   private TCPClient tcpClient;

   public ConnectionView(DummyClientInterface parent, Stage primaryStage) {
      this.parent = parent;
      this.primaryStage = primaryStage;

      // Set design
      setAlignment(Pos.CENTER);
      setHgap(10);
      setVgap(10);
      setPadding(new Insets(25, 25, 25, 25));

      // Add title
      Text connection = new Text("Connection.");

      // Add a Connect Button
      Button connect = new Button("Connect to server");
      connect.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent e) {
            try {
               // Try to connect to the server
               tcpClient = new TCPClient("localhost", 2525, ConnectionView.this);
               new Thread(tcpClient).start();
               System.out.println("Connected to the server on port 2525.");
            } catch (IOException ex) {

               Platform.runLater(new Runnable() {
                  @Override
                  public void run() {
                     PopupMessage dialog = new PopupMessage("Erreur de connexion", "Réessayer");
                     dialog.showAndWait();
                     ConnectionView.this.parent.setView(new ConnectionView(ConnectionView.this.parent, ConnectionView.this.primaryStage));
                  }
               });

            }
         }
      });

      // Login title 
      Text login = new Text("Login");
      final TextField username = new TextField();
      final PasswordField pswd = new PasswordField();

      Button connectUser = new Button("Connect");
      connectUser.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent t) {
            tcpClient.connectUser(username.getText(), pswd.getText());
         }
      });
      Button register = new Button("Register");
      register.setOnAction(new EventHandler<ActionEvent>() {
         public void handle(ActionEvent t) {
            tcpClient.registerUser(username.getText(), pswd.getText());
         }
      });

      add(connection, 0, 0);
      add(connect, 0, 1);
      add(login, 0, 2);
      add(username, 0, 3);
      add(pswd, 0, 4);
      add(connectUser, 0, 5);
      add(register, 1, 5);

   }

   public void listRooms(List<TCPRoomMessage> rooms) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void joinRoom() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void roomInfo(TCPRoomInfoMessage msg) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void connectUser() {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            parent.setView(new RoomListView(parent, primaryStage, tcpClient));
         }
      });
   }

   public void registerUser() {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            parent.setView(new RoomListView(parent, primaryStage, tcpClient));
         }
      });
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

         case BAD_AUTHENTIFICATION:
            Platform.runLater(new Runnable() {
               @Override
               public void run() {
                  PopupMessage dialog = new PopupMessage("Le nom d'utilisateur ou le mot de passe est incorrect.", "Réessayer");
                  dialog.showAndWait();
               }
            });
            break;

         case USED_USERNAME:
            Platform.runLater(new Runnable() {
               @Override
               public void run() {
                  PopupMessage dialog = new PopupMessage("Le nom d'utilisateur est déjà utilisé.", "Réessayer");
                  dialog.showAndWait();
               }
            });
            break;
      }
   }

}
