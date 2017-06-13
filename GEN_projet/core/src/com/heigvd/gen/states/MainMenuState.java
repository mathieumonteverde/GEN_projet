package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
 * This specialized State offers to the user to go to the list of available
 * rooms, the score board, or the administration page if the user is an admin.
 */
public class MainMenuState extends State implements TCPClientListener {
   // Game State manager
   private GameStateManager gsm;
   
   // Stage to display content
   private Stage stage;
   
   // TCP client
   private TCPClient tcpClient;
   
   /**
    * Constructor. Create the buttons to navigate to the next states.
    * @param gsm the Game State Manager
    * @param tcpClient the TCP client to use to communicate with the server
    */
   public MainMenuState(GameStateManager gsm, TCPClient tcpClient) {
      super(gsm);
      this.gsm = gsm;

      // Set TCP client
      this.tcpClient = tcpClient;
      tcpClient.setListener(this);

      // Get width of the game
      int gameWidth = Gdx.graphics.getWidth();
      int gameHeight = Gdx.graphics.getHeight();

      // Créer une stage pour contenir les éléments
      stage = new Stage();
      Gdx.input.setInputProcessor(stage);

      // Create buttons
      TextButton rooms = GuiComponent.createButton("Rooms", 200, 70);
      TextButton scores = GuiComponent.createButton("Scores", 200, 70);
      TextButton disconnect = GuiComponent.createButton("Disconnect", 160, 50);

      // Register actions on click
      rooms.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            MainMenuState.this.gsm.set(
                    new RoomMenuState(MainMenuState.this.gsm, MainMenuState.this.tcpClient)
            );
         }
      });
      scores.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            MainMenuState.this.gsm.set(
                    new ScoreState(MainMenuState.this.gsm, MainMenuState.this.tcpClient)
            );
         }
      });
      disconnect.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            MainMenuState.this.tcpClient.disconnect();
            MainMenuState.this.gsm.set(new HomeScreenState(MainMenuState.this.gsm));
         }
      });
      
      // Place elements
      GuiComponent.centerGuiComponent(rooms, stage, 0, 100);
      GuiComponent.centerGuiComponent(scores, stage, 0, 0);
      disconnect.setX(gameWidth - disconnect.getWidth() - 20);
      disconnect.setY(gameHeight - disconnect.getHeight() - 20);

      // Add them to the stage
      stage.addActor(rooms);
      stage.addActor(scores);
      stage.addActor(disconnect);
      
      /**
       * If the user has admin privileges, add the corresponding options,
       */
      if (UserPrivilege.isAdmin(Player.getInstance().getRole())) {
         // Create the button
         TextButton admin = GuiComponent.createButton("Admin", 200, 70);
         admin.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
               MainMenuState.this.gsm.set(new AdminState(MainMenuState.this.gsm, MainMenuState.this.tcpClient));
            }
         });
         GuiComponent.centerGuiComponent(admin, stage, 0, -100);
         stage.addActor(admin);
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
      stage.act();
      stage.draw();
   }

   @Override
   public void dispose() {
      stage.dispose();
   }

   @Override
   public void listRooms(List<TCPRoomMessage> rooms) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void joinRoom() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void roomInfo(TCPRoomInfoMessage msg) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void connectUser(int role) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void registerUser() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void errorNotification(TCPErrors.Error error) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
   public void banUser() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void raceStart() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

}
