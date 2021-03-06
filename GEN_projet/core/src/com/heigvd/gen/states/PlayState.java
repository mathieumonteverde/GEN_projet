package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.heigvd.gen.Player;
import com.heigvd.gen.RaceSimulation;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPClientListener;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.client.UDPClient.UDPClient;
import com.heigvd.gen.client.UDPClient.UDPClientListener;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.protocol.tcp.message.TCPScoreMessage;
import com.heigvd.gen.protocol.udp.message.UDPPlayerMessage;
import com.heigvd.gen.protocol.udp.message.UDPRaceMessage;
import com.heigvd.gen.sprites.*;
import com.heigvd.gen.utils.Constants;
import com.heigvd.gen.utils.Constants.LineColor;

import java.util.ArrayList;
import java.util.List;

public class PlayState extends State implements UDPClientListener, TCPClientListener {

   private boolean gameRunning = false; //If the race has started, is true
   private boolean hasControllers = true;
   private boolean reachedEnd = false;
   private float gameTime;
   private long time;

   private Bike player;
   private ArrayList<Bike> opponents;
   private Road road;
   private Controller controller;

   private Texture bg;
   private Vector2 bgPos1, bgPos2;

   private String labelTime;
   private BitmapFont font;
   private GlyphLayout gl;

   private UDPClient udpClient;
   private TCPClient tcpClient;
   
   private boolean hasSignalFInished = false;

   public PlayState(GameStateManager gsm, Road road, UDPClient udpClient, TCPClient tcpClient) {
      super(gsm);
      this.road = road;
      this.udpClient = udpClient;
      udpClient.setListener(this);
      this.tcpClient = tcpClient;
      tcpClient.setListener(this);
      cam.setToOrtho(false, RaceSimulation.WIDTH / 1.5f, RaceSimulation.HEIGHT / 1.5f);

      opponents = new ArrayList<Bike>();
      bg = new Texture("bg.png");
      bgPos1 = new Vector2(cam.position.x - cam.viewportWidth /2, 0);
      bgPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + Gdx.graphics.getWidth(), 0);

      //Setup of the timer display
      gameTime = 0;
      gl = new GlyphLayout();
      Texture texture = new Texture(Gdx.files.internal("IPAGothic.png"), true); // true enables mipmaps
      texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
      font = new BitmapFont(Gdx.files.internal("IPAGothic.fnt"), new TextureRegion(texture),false);
      font.setUseIntegerPositions(false);
      labelTime = "0:00.00";

      //TODO connect to server and wait for other player
      player = new Bike(50,100, "You", false);

      //Checks if controller is connected
      if(Controllers.getControllers().size == 0)
      {
         hasControllers = false;
      } else {
         controller = Controllers.getControllers().first();
      }
      
   }

   @Override
   protected void handleInput() {
      //Check if the game is running before handling the inputs
      if(gameRunning && !hasControllers) {
         if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.jump();
         }

         if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.switchColor(Constants.LineColor.RED);
         }

         if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.switchColor(Constants.LineColor.GREEN);
         }

         if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.switchColor(Constants.LineColor.BLUE);
         }

         if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            player.speedUp();
         }
      }
   }

   @Override
   public void update(float dt) {
      handleInput();
      updateBG();
      player.update(dt);

      reachedEnd = (player.getPosition().x + Bike.WIDTH) >= road.getCurrentLength();

      //If the end has been reached change bool, if not the camera continues to update
      if(reachedEnd) {
         gameRunning = false;
      } else {
         cam.position.x = player.getPosition().x + 300;
      }

      float minutes = (float)Math.floor(gameTime / 60.0f);
      float seconds = gameTime - minutes * 60.0f;


      //Update the displayed time only if the game is running
      //if(gameRunning) {
         labelTime = String.format("%.0f:%05.2f", minutes, seconds);
      //}

      //If a controller is connected, check for inputs
      if(hasControllers && gameRunning) {

         float velocity = -controller.getAxis(4)*20;
         if(velocity <= 0) {
            velocity = 0;
         }
         player.addVelocity(velocity,0);

         if(controller.getButton(0))
            player.switchColor(Constants.LineColor.GREEN);
         if(controller.getButton(1))
            player.switchColor(Constants.LineColor.RED);
         if(controller.getButton(2))
            player.switchColor(Constants.LineColor.BLUE);
      }

      //Collision detector between bike and road
      for(RoadLine rl : road.getRoadColors()) {

         if(rl.collides(player.getBounds())) { //If the player hits a colored road

            Constants.LineColor roadColor = rl.getColor();

            player.setPosition(new Vector2(player.getPosition().x, rl.getPosition().y)); //Hit the ground
            player.hitGround();

            if(roadColor != player.getColor() && roadColor != Constants.LineColor.WHITE) { //if the color of the player does not match the one of the road
               player.slowDown();
            }
         }
      }

      if(reachedEnd && !hasSignalFInished) {
         gameRunning = false;
         hasSignalFInished = true;
         tcpClient.signalFinish();
      } else {
         cam.position.x = player.getPosition().x + 300;
      }
      
      udpClient.sendPlayerInfo(player, Player.getInstance());
      
      cam.update();
   }

   @Override
   public void render(SpriteBatch sb) {
      sb.setProjectionMatrix(cam.combined);
      sb.begin();

      //First draw the background
      sb.draw(bg, bgPos1.x,bgPos1.y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      sb.draw(bg, bgPos2.x,bgPos2.y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

      //Then the opponents
      for(Bike bike : opponents) {
         sb.setColor(1,1,1,0.4f);
         sb.draw(bike.getTexture(), bike.getPosition().x, bike.getPosition().y, Bike.WIDTH, Bike.HEIGHT);
         sb.setColor(1,1,1,1);
      }

      //Then the current player
      sb.draw(player.getTexture(), player.getPosition().x, player.getPosition().y, Bike.WIDTH, Bike.HEIGHT);


      //And finally the road
      for(RoadLine rl : road.getRoadColors()) {
         sb.draw(rl.getLine(), rl.getPosition().x, rl.getPosition().y, rl.getLength(), RoadLine.LINE_HEIGHT);
      }

      font.getData().setScale(0.25f);
      gl.setText(font, labelTime, Color.WHITE, 0, Align.left,false);
      font.draw(sb, gl, cam.position.x - gl.width /2, cam.viewportHeight -gl.height);

      sb.end();
   }

   @Override
   public void dispose() {
      player.dispose();
      road.dispose();
      bg.dispose();
      font.dispose();
      for(Bike b : opponents) { b.dispose();}
   }

   private void updateBG() {
      if(cam.position.x - (cam.viewportWidth / 2) > bgPos1.x + Gdx.graphics.getWidth())
         bgPos1.add(Gdx.graphics.getWidth()*2, 0);
      if(cam.position.x - (cam.viewportWidth / 2) > bgPos2.x + Gdx.graphics.getWidth())
         bgPos2.add(Gdx.graphics.getWidth()*2, 0);

      if(!reachedEnd) {
         bgPos1.add(player.getVelocity().x/100,0);
         bgPos2.add(player.getVelocity().x/100,0);
      }
   }

   @Override
   public void receiveRaceData(UDPRaceMessage raceMessage) {

      final List<UDPPlayerMessage> list = raceMessage.getPlayers();
      //While the game is not running, update the this of opponnents with missing players
      if (!gameRunning && !reachedEnd) {

         Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {

               for (UDPPlayerMessage pm : list) {
                  Bike b = getBikeByUsername(pm.getUsername());
                  //If the player has'nt been found, add it to the list
                  if (b == null && !pm.getUsername().equals(Player.getInstance().getUsername())) {
                     opponents.add(new Bike(pm.getPosX(), pm.getPosY(), pm.getUsername(), true));
                  }
               }
            }
         });

      }

      if (gameRunning) {
         gameTime = raceMessage.getTime() / 100.f;
      }

      //Update bike positions
      for (UDPPlayerMessage pm : list) {
         Bike b = getBikeByUsername(pm.getUsername());
         if (b != null) {
            b.setPosition(new Vector2(pm.getPosX(), pm.getPosY()));
            b.switchColor(LineColor.values()[pm.getColor()]);
         }
      }

   }

   private Bike getBikeByUsername(String name) {
      for(Bike bike: opponents) {
         if(bike.getName().equals(name)) {
            return bike;
         }
      }

      return null;
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
   public void getScores(List<TCPScoreMessage> msgs) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void banUser() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void disconnection() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
   public void errorNotification(TCPErrors.Error error) {
   }

   @Override
   public void countDown(int count) {
      gameTime = count;
      if (count == 0) {
         gameRunning = true;
      }
   }

   @Override
   public void quitRoom() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void raceEnd(List<TCPScoreMessage> scores) {
      
      final List<TCPScoreMessage> s = scores;
      Gdx.app.postRunnable(new Runnable() {

         @Override
         public void run() {
            gsm.set(new RaceScoreState(gsm, tcpClient, s));
         }
      });
   }
}
