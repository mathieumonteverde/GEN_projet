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
 *
 * @author mathieu
 */
public class MainMenuState extends State implements TCPClientListener {

   private Stage stage;

   private TCPClient tcpClient;

   public MainMenuState(GameStateManager gsm, TCPClient tcpClient) {

      super(gsm);

      this.tcpClient = tcpClient;
      tcpClient.setListener(this);

      final GameStateManager g = gsm;

      // Get width of the game
      int gameWidth = Gdx.graphics.getWidth();
      int gameHeight = Gdx.graphics.getHeight();

      // Créer une stage pour contenir les éléments
      stage = new Stage();
      Gdx.input.setInputProcessor(stage);

      TextButton rooms = GuiComponent.createButton("Rooms", 200, 70);
      TextButton scores = GuiComponent.createButton("Scores", 200, 70);

      rooms.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            g.set(new RoomMenuState(g, MainMenuState.this.tcpClient));
         }
      });

      scores.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            g.set(new ScoreState(g, MainMenuState.this.tcpClient));
         }
      });

      GuiComponent.centerGuiComponent(rooms, stage, 0, 100);
      GuiComponent.centerGuiComponent(scores, stage, 0, 0);

      // Ajouter au stage
      stage.addActor(rooms);
      stage.addActor(scores);

      if (UserPrivilege.isAdmin(Player.getInstance().getRole())) {
         TextButton admin = GuiComponent.createButton("Admin", 200, 70);

         admin.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
               g.set(new AdminState(g, MainMenuState.this.tcpClient));
            }
         });
         GuiComponent.centerGuiComponent(admin, stage, 0, -100);
         stage.addActor(admin);
      }

      TextButton disconnect = GuiComponent.createButton("Disconnect", 160, 50);
      disconnect.setX(gameWidth - disconnect.getWidth() - 20);
      disconnect.setY(gameHeight - disconnect.getHeight() - 20);
      disconnect.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            MainMenuState.this.tcpClient.disconnect();
            MainMenuState.this.gsm.set(new HomeScreenState(MainMenuState.this.gsm));
         }
      });
      stage.addActor(disconnect);
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

}
