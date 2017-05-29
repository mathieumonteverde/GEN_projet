package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
   private Texture ground;
   private Vector2 groundPos1, groundPos2;

   private Array<Tube> tubes;
   private Road road;
   private ArrayList<RoadLine> renderedLines;

   public PlayState(GameStateManager gsm, Road road) {
      super(gsm);
      this.road = road;
      renderedLines = new ArrayList<RoadLine>();
      bike = new Bike(50,300, true);
      cam.setToOrtho(false, RaceSimulation.WIDTH / 2, RaceSimulation.HEIGHT /2);
      ground = new Texture("ground.png");
      groundPos1 = new Vector2(cam.position.x - cam.viewportWidth /2, GROUND_Y_OFFSET);
      groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);

      tubes = new Array<Tube>();
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
         bike.setVelocity(new Vector2(bike.getVelocity().x + 20, bike.getVelocity().y));
      }

      if(bike.getVelocity().x > 0) {
         bike.setVelocity(new Vector2(bike.getVelocity().x - 10, bike.getVelocity().y));
      } else {
         bike.setVelocity(new Vector2(0, bike.getVelocity().y));
      }
   }

   @Override
   public void update(float dt) {
      handleInput();
      bike.update(dt);

      //Créer une liste des routes que l'on voit
      int l = 0;
      for(RoadLine rl : road.getRoadColors()) {
         if(l < RaceSimulation.WIDTH) {
            l += (rl.getLength()*rl.getLine().getWidth());
            renderedLines.add(rl);
         } else {
            break;
         }
      }

      cam.position.x = bike.getPosition().x + 300;

      for(RoadLine rl : renderedLines) {

         Vector2 bikeVelocity = bike.getVelocity();
         if(rl.collides(bike.getBounds())) { //If the bike hits a colored road

            bikeVelocity.set(bikeVelocity.x, 0); //Hit the ground

            if(bike.getColor() != rl.getColor() ) { //if the color of the bike does not match the one of the road

               if(bikeVelocity.x >= Constants.MIN_SPEED) { //Be sure that you don't go under minimum speed
                  bike.setVelocity(new Vector2(bikeVelocity.x-20, bikeVelocity.y)); //Slow down the bike
               } else {
                  bike.setVelocity(new Vector2(Constants.MIN_SPEED, bikeVelocity.y));
               }
            }
         }

         bike.setVelocity(bikeVelocity);

      }

      cam.update();
   }

   @Override
   public void render(SpriteBatch sb) {
      sb.setProjectionMatrix(cam.combined);
      sb.begin();
      //sb.draw(bg, cam.position.x - cam.viewportWidth / 2, 0);
      sb.draw(bike.getTexture(), bike.getPosition().x, bike.getPosition().y, Bike.WIDTH, Bike.HEIGHT);

      int concat = 0;
      System.out.println();
      for(RoadLine rl : renderedLines) {
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
      for(Tube tube : tubes) {
         tube.dispose();
      }
      road.dispose();
      System.out.println("Play State Disposed");
   }
}
