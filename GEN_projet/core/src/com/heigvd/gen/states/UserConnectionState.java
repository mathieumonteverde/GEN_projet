package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.heigvd.gen.Player;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPClientListener;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.guicomponent.GuiComponent;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.protocol.tcp.message.TCPScoreMessage;
import com.heigvd.gen.useraccess.UserPrivilege;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class UserConnectionState extends State implements TCPClientListener {

   // Stage to display elements
   private Stage stage;

   // Buttons to manage the state
   private TextButton connect;
   private TextButton register;
   private TextField username;
   private TextField password;

   // TCP client to communicate with server
   private TCPClient tcpClient;

   private final GameStateManager g;

   public UserConnectionState(GameStateManager gsm) {
      super(gsm);

      g = gsm;

      // Créer une stage pour contenir les éléments
      stage = new Stage();
      Gdx.input.setInputProcessor(stage);

      // Créer les champs textes
      username = GuiComponent.createTextField("Username...");
      password = GuiComponent.createTextField("Password...");
      password.setPasswordMode(true);
      password.setPasswordCharacter('*');

      // Créer le bouton de d'inscription
      register = GuiComponent.createButton("Register", 200, 60);
      register.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeEvent event, Actor actor) {
            tcpClient.registerUser(username.getText(), password.getText());
            Player.getInstance().setUsername(username.getText());
            Player.getInstance().setPassword(password.getText());
         }
      });

      // Créer le bouton de connection
      connect = GuiComponent.createButton("Connect", 200, 60);
      connect.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeEvent event, Actor actor) {
            tcpClient.connectUser(username.getText(), password.getText());
            Player.getInstance().setUsername(username.getText());
            Player.getInstance().setPassword(password.getText());
         }
      });

      // Utiliser l'utilitaire maison pour centrer les éléments et offset
      GuiComponent.centerGuiComponent(username, stage, 0, 80);
      GuiComponent.centerGuiComponent(password, stage, 0, 30);
      GuiComponent.centerGuiComponent(connect, stage, 0, -30);
      GuiComponent.centerGuiComponent(register, stage, 0, -100);

      // Ajouter au stage
      stage.addActor(username);
      stage.addActor(password);
      stage.addActor(connect);
      stage.addActor(register);

      try {
         // Connect to the db
         tcpClient = new TCPClient("localhost", 2525, this);
         new Thread(tcpClient).start();

      } catch (IOException ex) {
         Logger.getLogger(UserConnectionState.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Override
   protected void handleInput() {
   }

   @Override
   public void update(float dt) {
   }

   @Override
   public void render(SpriteBatch sb) {
      // Dessiner le stage
      stage.draw();
   }

   @Override
   public void dispose() {
      stage.dispose();
   }

   @Override
   public void listRooms(List<TCPRoomMessage> rooms) {
   }

   @Override
   public void joinRoom() {
   }

   @Override
   public void roomInfo(TCPRoomInfoMessage msg) {
   }

   @Override
   public void connectUser(int role) {
      final int r = role;
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            Player.getInstance().setRole(r);
            g.set(new MainMenuState(g, UserConnectionState.this.tcpClient));
         }
      });

   }

   @Override
   public void registerUser() {
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            Player.getInstance().setRole(UserPrivilege.Privilege.DEFAULT.ordinal());
            g.set(new MainMenuState(g, UserConnectionState.this.tcpClient));
         }
      });
   }

   @Override
   public void errorNotification(TCPErrors.Error error) {

   }

   @Override
   public void getScores(List<TCPScoreMessage> msgs) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void disconnection() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void getUsers(List<TCPPlayerInfoMessage> users) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

}
