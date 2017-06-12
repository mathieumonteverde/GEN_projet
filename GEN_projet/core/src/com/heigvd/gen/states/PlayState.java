package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.heigvd.gen.RaceSimulation;
import com.heigvd.gen.sprites.*;
import com.heigvd.gen.utils.Constants;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlayState extends State {

   private Bike player;
   private ArrayList<Bike> oppenents;
   private Road road;
   private boolean gameRunning;
   private float gameTime;
   private String labelTime;
   BitmapFont font;

   public PlayState(GameStateManager gsm, Road road) {
      super(gsm);
      this.road = road;
      gameRunning = false;
      gameTime = 0;
      font = new BitmapFont(Gdx.files.internal("TeXGyreAdventor.fnt"), false);
      labelTime = "";


      //TODO connect to server and wait for other player
      //TODO retrieve list of other player to show them
      player = new Bike(50,100, false);
      cam.setToOrtho(false, RaceSimulation.WIDTH / 2, RaceSimulation.HEIGHT /2);

      //TODO Countdown

   }

   @Override
   protected void handleInput() {
      //Check if the game is running before handling the inputs
      if(gameRunning) {
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
            player.addVelocity(20, 0);
         }
      }
   }

   @Override
   public void update(float dt) {
      handleInput();
      player.update(dt);

      gameTime += dt;
      float minutes = (float)Math.floor(gameTime / 60.0f);
      float seconds = gameTime - minutes * 60.0f;
      labelTime = String.format("%.0fm%.0fs", minutes, seconds);

      //Créer une liste des routes que l'on voit
      /*int l = 0;
      for(RoadLine rl : road.getRoadColors()) {
         if(l < RaceSimulation.WIDTH) {
            l += (rl.getLength()*rl.getLine().getWidth());
            renderedLines.add(rl);
         } else {
            break;
         }
      }*/

      /*
      if(cam.zoom >= 1) {
         if(player.getVelocity().x != 0)
            cam.zoom = player.getVelocity().x / 10000 + 1;
      } else {
         cam.zoom = 1;
      }*/

      cam.position.x = player.getPosition().x + 300;

      for(RoadLine rl : road.getRoadColors()) {

         if(rl.collides(player.getBounds())) { //If the player hits a colored road

            Vector2 bikeVelocity = player.getVelocity();

            player.setPosition(new Vector2(player.getPosition().x, rl.getPosition().y)); //Hit the ground
            bikeVelocity.set(bikeVelocity.x, 0);

            if(player.getColor() != rl.getColor() && rl.getColor() != Constants.LineColor.WHITE) { //if the color of the player does not match the one of the road
              if(bikeVelocity.x >= Constants.MIN_SPEED) { //Be sure that you don't go under minimum speed
                  player.addVelocity(-50,0); //Slow down the player
               } else {
                  player.addVelocity(Constants.MIN_SPEED, 0);
               }
            }
         }
      }

      cam.update();
   }

   @Override
   public void render(SpriteBatch sb) {
      sb.setProjectionMatrix(cam.combined);
      sb.begin();

      if(player.isGhost()) {
         sb.setColor(1,1,1,0.2f);
         sb.draw(player.getTexture(), player.getPosition().x, player.getPosition().y, Bike.WIDTH, Bike.HEIGHT);
         sb.setColor(1,1,1,1);
      } else {
         sb.draw(player.getTexture(), player.getPosition().x, player.getPosition().y, Bike.WIDTH, Bike.HEIGHT);
      }

      int concat = 0;
      for(RoadLine rl : road.getRoadColors()) {
         for(int i = 0; i < rl.getLength(); i++) {
            sb.draw(rl.getLine(), concat, rl.getPosition().y);
            concat += rl.getLine().getWidth();
         }
      }

      font.draw(sb, labelTime, 100, 100,10,10,false);

      sb.end();
   }

   @Override
   public void dispose() {
      player.dispose();
      road.dispose();
      System.out.println("Play State Disposed");
   }
}
