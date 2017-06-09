/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPClientListener;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.guicomponent.GuiComponent;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class RoomMenuState extends State implements TCPClientListener {

   private Stage stage;

   private GameStateManager g;

   private TCPClient tcpClient;

   private List<String> list;

   private ScrollPane scrollPane;

   public RoomMenuState(GameStateManager gsm, TCPClient tcpClient) {
      super(gsm);

      this.g = gsm;

      this.tcpClient = tcpClient;
      tcpClient.setListener(this);

      list = new List<String>(GuiComponent.getSkin());
      
      stage = new Stage();

      try {
         tcpClient.listRooms();
      } catch (IOException ex) {
         Logger.getLogger(RoomMenuState.class.getName()).log(Level.SEVERE, null, ex);
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
      stage.draw();
   }

   @Override
   public void dispose() {
      stage.dispose();
   }

   @Override
   public void listRooms(java.util.List<TCPRoomMessage> rooms) {

      final java.util.List<TCPRoomMessage> r = rooms;
      Gdx.app.postRunnable(new Runnable() {

         @Override
         public void run() {

            int gameWidth = Gdx.graphics.getWidth();
            int gameHeight = Gdx.graphics.getHeight();
            
            String[] buttons = new String[r.size()];
            int i = 0;
            for (TCPRoomMessage room : r) {
               buttons[i++] = room.getName() + " - " + "Nombre de joueurs: " + room.getNbPlayers();
            }
            list.setItems(buttons);

            scrollPane = new ScrollPane(list);
            scrollPane.setWidth(600);
            scrollPane.setWidth(500);
            scrollPane.setX(gameWidth / 2 - scrollPane.getWidth() / 2);
            scrollPane.setY(gameHeight / 2 - scrollPane.getHeight() / 2);
            scrollPane.setSmoothScrolling(false);
            scrollPane.setTransform(true);
            stage = new Stage(new StretchViewport(gameWidth, gameHeight));
            stage.addActor(scrollPane);

            Gdx.input.setInputProcessor(stage);
         }
      });
   }

   @Override
   public void joinRoom() {
   }

   @Override
   public void roomInfo(TCPRoomInfoMessage msg) {
   }

   @Override
   public void connectUser() {
   }

   @Override
   public void registerUser() {

   }

   @Override
   public void errorNotification(TCPErrors.Error error) {
   }

}
