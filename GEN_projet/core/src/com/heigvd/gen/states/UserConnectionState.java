package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
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
import java.util.List;

/**
 * Specialized State that offers to the user to connect or rgister to the
 * server. The user can enter his username and password, and then choose to
 * connect or register as a new user.
 */
public class UserConnectionState extends State implements TCPClientListener {

   // Game State manager
   private final GameStateManager gsm;

   // Stage to display elements
   private Stage stage;

   // Text fields and buttons to manage the state
   private TextButton connect;
   private TextButton register;
   private TextField username;
   private TextField password;

   // TCP client to communicate with server
   private TCPClient tcpClient;

   /**
    * Constructor. Creates the text fields and the buttons to manage the user
    * information.
    *
    * @param gsm the Game State manager
    * @param tcpClient the TCP client to use
    */
   public UserConnectionState(GameStateManager gsm, TCPClient tcpClient) {
      super(gsm);
      this.gsm = gsm;

      // Créer une stage pour contenir les éléments
      stage = new Stage();
      Gdx.input.setInputProcessor(stage);

      // Assign the TCP client
      this.tcpClient = tcpClient;
      tcpClient.setListener(this);

      // Create text fields
      username = GuiComponent.createTextField("Username...");
      password = GuiComponent.createTextField("Password...");
      password.setPasswordMode(true);
      password.setPasswordCharacter('*');

      // Create the register button
      register = GuiComponent.createButton("Register", 200, 60);
      register.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeEvent event, Actor actor) {
            UserConnectionState.this.tcpClient.registerUser(username.getText(), password.getText());
            Player.getInstance().setUsername(username.getText());
            Player.getInstance().setPassword(password.getText());
         }
      });

      // Create the connection button
      connect = GuiComponent.createButton("Connect", 200, 60);
      connect.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeEvent event, Actor actor) {
            UserConnectionState.this.tcpClient.connectUser(username.getText(), password.getText());
            Player.getInstance().setUsername(username.getText());
            Player.getInstance().setPassword(password.getText());
         }
      });

      // Center components
      GuiComponent.centerGuiComponent(username, stage, 0, 80);
      GuiComponent.centerGuiComponent(password, stage, 0, 30);
      GuiComponent.centerGuiComponent(connect, stage, 0, -30);
      GuiComponent.centerGuiComponent(register, stage, 0, -100);

      // Add it to the stage
      stage.addActor(username);
      stage.addActor(password);
      stage.addActor(connect);
      stage.addActor(register);
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
      stage.act();
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

   /**
    * If the user was successfully connected, set the Player instance role
    * attribute and go to the main menu.
    *
    * @param role the role of the user connected
    */
   @Override
   public void connectUser(int role) {
      final int r = role;
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            Player.getInstance().setRole(r);
            gsm.set(new MainMenuState(gsm, UserConnectionState.this.tcpClient));
         }
      });

   }

   /**
    * If the user was successfully registered, set the player instance role
    * attribute to DEFAULT, and go the main menu
    */
   @Override
   public void registerUser() {
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            Player.getInstance().setRole(UserPrivilege.Privilege.DEFAULT.ordinal());
            gsm.set(new MainMenuState(gsm, UserConnectionState.this.tcpClient));
         }
      });
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

   @Override
   public void userRights() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void errorNotification(TCPErrors.Error error) {
      // Filter the error
      String msg = "An error occured...";
      switch (error) {
         case BAD_AUTHENTIFICATION:
            msg = "Wrong username or password...";
            break;

         case USED_USERNAME:
            msg = "This username is already used...";
            break;
      }
      
      GuiComponent.showDialog(stage, msg, "Try again...");
   }

}
