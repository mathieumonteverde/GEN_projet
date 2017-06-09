package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.heigvd.gen.RaceSimulation;
import com.heigvd.gen.sprites.*;
import com.heigvd.gen.utils.Constants;

import java.util.ArrayList;

public class PlayState extends State {

   private static final int TUBE_SPACING = 125;
   private static final int TUBE_COUNT = 4;
   private static final int GROUND_Y_OFFSET = -30;

   private Bike bike;
   private Road road;
   private ArrayList<RoadLine> renderedLines;

   public PlayState(GameStateManager gsm, Road road) {
      super(gsm);
      this.road = road;
      renderedLines = new ArrayList<RoadLine>();
      bike = new Bike(50,100, false);
      cam.setToOrtho(false, RaceSimulation.WIDTH / 2, RaceSimulation.HEIGHT /2);
   }

   @Override
   protected void handleInput() {
      if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
         bike.jump();
      }

      if(Gdx.input.isKeyPressed(Input.Keys.A)) {
         bike.switchColor(Constants.LineColor.RED);
      }

      if(Gdx.input.isKeyPressed(Input.Keys.S)) {
         bike.switchColor(Constants.LineColor.GREEN);
      }

      if(Gdx.input.isKeyPressed(Input.Keys.D)) {
         bike.switchColor(Constants.LineColor.BLUE);
      }

      if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
         bike.addVelocity(20,0);
      }
   }

   @Override
   public void update(float dt) {
      handleInput();
      bike.update(dt);

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
         if(bike.getVelocity().x != 0)
            cam.zoom = bike.getVelocity().x / 10000 + 1;
      } else {
         cam.zoom = 1;
      }*/

      cam.position.x = bike.getPosition().x + 300;

      for(RoadLine rl : road.getRoadColors()) {

         if(rl.collides(bike.getBounds())) { //If the bike hits a colored road

            Vector2 bikeVelocity = bike.getVelocity();

            bike.setPosition(new Vector2(bike.getPosition().x, rl.getPosition().y)); //Hit the ground
            bikeVelocity.set(bikeVelocity.x, 0);

            if(bike.getColor() != rl.getColor() && rl.getColor() != Constants.LineColor.WHITE) { //if the color of the bike does not match the one of the road
              if(bikeVelocity.x >= Constants.MIN_SPEED) { //Be sure that you don't go under minimum speed
                  bike.addVelocity(-50,0); //Slow down the bike
               } else {
                  bike.addVelocity(Constants.MIN_SPEED, 0);
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

      if(bike.isGhost()) {
         sb.setColor(1,1,1,0.2f);
         sb.draw(bike.getTexture(), bike.getPosition().x, bike.getPosition().y, Bike.WIDTH, Bike.HEIGHT);
         sb.setColor(1,1,1,1);
      } else {
         sb.draw(bike.getTexture(), bike.getPosition().x, bike.getPosition().y, Bike.WIDTH, Bike.HEIGHT);
      }

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
      bike.dispose();
      road.dispose();
      System.out.println("Play State Disposed");
   }
}
