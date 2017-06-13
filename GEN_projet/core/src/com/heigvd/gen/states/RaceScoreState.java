/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPClientListener;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.guicomponent.GuiComponent;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.protocol.tcp.message.TCPScoreMessage;
import java.util.List;

/**
 *
 * @author mathieu
 */
public class RaceScoreState extends State implements TCPClientListener {

   private GameStateManager gsm;

   private Stage stage;

   private TCPClient tcpClient;

   // Table des scores
   private Table table;

   // ScrollPane to contain list of scores
   private ScrollPane scrollPane;

   public RaceScoreState(GameStateManager gsm, TCPClient tcpClient, List<TCPScoreMessage> scores) {
      super(gsm);

      this.gsm = gsm;

      // Set the TCPClient
      this.tcpClient = tcpClient;
      this.tcpClient.setListener(this);

      int gameWidth = Gdx.graphics.getWidth();
      int gameHeight = Gdx.graphics.getHeight();
      stage = new Stage(new StretchViewport(gameWidth, gameHeight));


      TextButton toRoom = GuiComponent.createButton("Back to room...", 180, 50);
      toRoom.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            Gdx.app.postRunnable(new Runnable() {

               @Override
               public void run() {
                  RaceScoreState.this.gsm.set(new RoomState(RaceScoreState.this.gsm, RaceScoreState.this.tcpClient));
               }
            });
         }
      });
      toRoom.setY(gameHeight - toRoom.getHeight() - 20);
      toRoom.setX(20);
      stage.addActor(toRoom);

      table = new Table(GuiComponent.getSkin());
      table.setFillParent(true);
      table.setBackground(GuiComponent.getSkin().getDrawable("default-window"));

      table.add("Race Name").expandX();
      table.add("Username").expandX();
      table.add("Pos").expandX();
      table.add("Time").expandX();
      table.add("Date").expandX();
      table.row();

      // Initialize an array of RoomListCell and fill it
      for (TCPScoreMessage score : scores) {
         table.add(score.getRaceName()).expandX();
         table.add(score.getUsername()).expandX();
         table.add(String.valueOf(score.getPosition())).expandX();
         table.add(String.valueOf(score.getTime())).expandX();
         table.add(score.getDate()).expandX();
         table.row();
      }

      if (scrollPane != null) {
         scrollPane.remove();
      }

      // Create a ScrollPane and add it to the stage
      scrollPane = new ScrollPane(table);
      scrollPane.setWidth(600);
      scrollPane.setHeight(gameHeight);
      scrollPane.setX(gameWidth / 2 - scrollPane.getWidth() / 2);
      scrollPane.setY(0);
      scrollPane.setTransform(true);
      scrollPane.setColor(Color.RED);

      stage.addActor(scrollPane);
      Gdx.input.setInputProcessor(stage);

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
      stage.act();
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
   }

   @Override
   public void registerUser() {
   }

   @Override
   public void getScores(List<TCPScoreMessage> msgs) {
   }

   @Override
   public void banUser() {
   }

   @Override
   public void disconnection() {
   }

   @Override
   public void getUsers(List<TCPPlayerInfoMessage> users) {
   }

   @Override
   public void userRights() {
   }

   @Override
   public void raceStart() {
   }

   @Override
   public void countDown(int count) {
   }

   @Override
   public void quitRoom() {
   }

   @Override
   public void raceEnd(List<TCPScoreMessage> scores) {
   }

   @Override
   public void errorNotification(TCPErrors.Error error) {
   }

}
