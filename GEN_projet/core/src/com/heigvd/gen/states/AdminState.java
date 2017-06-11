/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPClientListener;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.guicomponent.GuiComponent;
import com.heigvd.gen.guicomponent.PlayerListCell;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.protocol.tcp.message.TCPScoreMessage;
import com.heigvd.gen.useraccess.UserPrivilege;

/**
 *
 * @author mathieu
 */
public class AdminState extends State implements TCPClientListener {

   private GameStateManager gsm;

   private TCPClient tcpClient;

   private Stage stage;

   private List<PlayerListCell> playerList;

   private ScrollPane scrollPane;

   public AdminState(GameStateManager gsm, TCPClient tcpClient) {
      super(gsm);
      this.gsm = gsm;

      this.tcpClient = tcpClient;
      tcpClient.setListener(this);

      int gameWidth = Gdx.graphics.getWidth();
      int gameHeight = Gdx.graphics.getHeight();
      stage = new Stage(new StretchViewport(gameWidth, gameHeight));
      
      playerList = new List<PlayerListCell>(GuiComponent.getSkin());
      
      TextButton back = GuiComponent.createButton("Back to menu", 160, 50);
      back.setX(gameWidth - back.getWidth() - 20);
      back.setY(gameHeight - back.getHeight() - 20);
      back.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            AdminState.this.gsm.set(new MainMenuState(AdminState.this.gsm, AdminState.this.tcpClient));
         }
      });
      stage.addActor(back);
      
      TextButton def = GuiComponent.createButton("Change to default...", 160, 50);
      def.setX(gameWidth - def.getWidth() - 20);
      def.setY(back.getY() - def.getHeight() - 40);
      def.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            PlayerListCell p = playerList.getSelected();
            if (p != null) {
               AdminState.this.tcpClient.userRights(p.getUsername(), UserPrivilege.Privilege.DEFAULT);
            }
         }
      });
      stage.addActor(def);
      
      TextButton admin = GuiComponent.createButton("Change to admin...", 160, 50);
      admin.setX(gameWidth - admin.getWidth() - 20);
      admin.setY(def.getY() - admin.getHeight() - 20);
      admin.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            PlayerListCell p = playerList.getSelected();
            if (p != null) {
               AdminState.this.tcpClient.userRights(p.getUsername(), UserPrivilege.Privilege.ADMIN);
            }
         }
      });
      stage.addActor(admin);
      

      Gdx.input.setInputProcessor(stage);
      
      tcpClient.getUsers();
   }

   @Override
   protected void handleInput() {
   }

   @Override
   public void update(float dt) {
   }

   @Override
   public void render(SpriteBatch sb) {
      stage.draw();
   }

   @Override
   public void dispose() {
      stage.dispose();
   }

   @Override
   public void listRooms(java.util.List<TCPRoomMessage> rooms) {
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
   public void getScores(java.util.List<TCPScoreMessage> msgs) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void disconnection() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void errorNotification(TCPErrors.Error error) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void getUsers(java.util.List<TCPPlayerInfoMessage> users) {

      final java.util.List<TCPPlayerInfoMessage> msgs = users;

      // Do things in an another Thread
      Gdx.app.postRunnable(new Runnable() {

         @Override
         public void run() {

            // Get width of the game
            int gameWidth = Gdx.graphics.getWidth();
            int gameHeight = Gdx.graphics.getHeight();

            // Initialize an array of RoomListCell and fill it
            PlayerListCell[] buttons = new PlayerListCell[msgs.size()];
            int i = 0;
            for (TCPPlayerInfoMessage user : msgs) {
               buttons[i++] = new PlayerListCell(user.getUsername(), null, user.getRole());
            }

            // Set the list content
            playerList.setItems(buttons);

            if (scrollPane != null) {
               scrollPane.remove();
            }

            // Create a ScrollPane and add it to the stage
            scrollPane = new ScrollPane(playerList);
            scrollPane.setWidth(600);
            scrollPane.setHeight(250);
            scrollPane.setX(gameWidth / 2 - scrollPane.getWidth() / 2);
            scrollPane.setY(gameHeight - scrollPane.getHeight() - 20);
            scrollPane.setSmoothScrolling(false);
            scrollPane.setTransform(true);
            stage.addActor(scrollPane);

            Gdx.input.setInputProcessor(stage);
         }
      });

   }

   @Override
   public void userRights() {
      tcpClient.getUsers();
   }

}
