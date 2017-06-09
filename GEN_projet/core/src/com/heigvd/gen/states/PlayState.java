package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.heigvd.gen.RaceSimulation;
import com.heigvd.gen.sprites.*;
import com.heigvd.gen.utils.Constants;

import java.util.ArrayList;

public class PlayState extends State {

   private Bike player;
   private ArrayList<Bike> opponents;
   private Road road;
   private boolean gameRunning;
   private boolean hasControllers = true;
   private boolean reachedEnd = false;
   private Controller controller;
   private Texture bg;
   private Vector2 bgPos1, bgPos2;
   private int parallax = 0;

   public PlayState(GameStateManager gsm, Road road) {
      super(gsm);
      this.road = road;
      gameRunning = true;
      bg = new Texture("bg.png");
      bgPos1 = new Vector2(cam.position.x - cam.viewportWidth /2, 0);
      bgPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + Gdx.graphics.getWidth(), 0);


      //TODO connect to server and wait for other player
      //TODO retrieve list of other player to show them
      player = new Bike(50,100, false);
      cam.setToOrtho(false, RaceSimulation.WIDTH / 2, RaceSimulation.HEIGHT /2);

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
            player.addVelocity(20, 0);
         }
      }
   }

   @Override
   public void update(float dt) {
      handleInput();
      updateBG();
      player.update(dt);

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

      /*
      if(cam.zoom >= 1) {
         if(player.getVelocity().x != 0)
            cam.zoom = player.getVelocity().x / 10000 + 1;
      } else {
         cam.zoom = 1;
      }*/

      if(!reachedEnd) {
         cam.position.x = player.getPosition().x + 300;
      }

      for(RoadLine rl : road.getRoadColors()) {


         if(rl.collides(player.getBounds())) { //If the player hits a colored road

            reachedEnd = rl.isEnd();
            Vector2 bikeVelocity = player.getVelocity();

            player.setPosition(new Vector2(player.getPosition().x, rl.getPosition().y)); //Hit the ground
            bikeVelocity.set(bikeVelocity.x, 0);

            if(player.getColor() != rl.getColor() && rl.getColor() != Constants.LineColor.WHITE) { //if the color of the player does not match the one of the road
              if(bikeVelocity.x >= Constants.MAX_SLOWED_SPEED) { //Be sure that you don't go under minimum speed
                  player.addVelocity(-50,0); //Slow down the player
               } else {
                  player.addVelocity(Constants.MAX_SLOWED_SPEED, 0);
               }
            }
         }
      }

      if(reachedEnd) {
         gameRunning = false;
      }

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

      //Then the current player
      if(player.isGhost()) {
         sb.setColor(1,1,1,0.2f);
         sb.draw(player.getTexture(), player.getPosition().x, player.getPosition().y, Bike.WIDTH, Bike.HEIGHT);
         sb.setColor(1,1,1,1);
      } else {
         sb.draw(player.getTexture(), player.getPosition().x, player.getPosition().y, Bike.WIDTH, Bike.HEIGHT);
      }

      //And finally the road
      int concat = 0;
      for(RoadLine rl : road.getRoadColors()) {
         for(int i = 0; i < rl.getLength(); i++) {
            sb.draw(rl.getLine(), concat, rl.getPosition().y);
            concat += rl.getLine().getWidth();
         }
      }

      sb.end();
   }

   @Override
   public void dispose() {
      player.dispose();
      road.dispose();
      bg.dispose();
      System.out.println("Play State Disposed");
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
}
