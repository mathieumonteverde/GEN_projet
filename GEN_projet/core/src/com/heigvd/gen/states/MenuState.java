package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.heigvd.gen.RaceSimulation;
import com.heigvd.gen.sprites.Road;
import com.heigvd.gen.sprites.RoadLine;
import com.heigvd.gen.utils.Constants;
import javafx.util.Pair;

import java.util.ArrayList;

public class MenuState extends State {

   private Texture background;
   private Texture playBtn;
   Road road;

   public MenuState(GameStateManager gsm) {
      super(gsm);
      final GameStateManager game = gsm;
      background = new Texture(Gdx.files.internal("bg.png"));
      playBtn = new Texture(Gdx.files.internal("playbtn.png"));
      Drawable drawable = new TextureRegionDrawable(new TextureRegion(playBtn));
      ImageButton playButton = new ImageButton(drawable);
      road = new Road();
      road.addLine(Constants.LineColor.BLUE, 10);
      road.addLine(Constants.LineColor.RED, 10);
      road.addLine(Constants.LineColor.GREEN, 10);
      road.addLine(Constants.LineColor.BLUE, 10);
      road.addLine(Constants.LineColor.GREEN, 10);
      road.addLine(Constants.LineColor.BLUE, 10);
      road.addLine(Constants.LineColor.RED, 10);
      road.addLine(Constants.LineColor.GREEN, 10);
      road.addLine(Constants.LineColor.BLUE, 10);
      road.addLine(Constants.LineColor.GREEN, 10);
      road.addLine(Constants.LineColor.BLUE, 10);
      road.addLine(Constants.LineColor.RED, 10);
      road.addLine(Constants.LineColor.GREEN, 10);
      road.addLine(Constants.LineColor.BLUE, 10);
      road.addLine(Constants.LineColor.GREEN, 10);
      road.addLine(Constants.LineColor.BLUE, 10);
      road.addLine(Constants.LineColor.RED, 10);
      road.addLine(Constants.LineColor.GREEN, 10);
      road.addLine(Constants.LineColor.BLUE, 10);
      road.addLine(Constants.LineColor.GREEN, 10);
      road.addLine(Constants.LineColor.BLUE, 10);
      road.addLine(Constants.LineColor.RED, 10);
      road.addLine(Constants.LineColor.GREEN, 10);
      road.addLine(Constants.LineColor.BLUE, 10);
      road.addLine(Constants.LineColor.GREEN, 10);


   }

   @Override
   public void handleInput() {
      if(Gdx.input.justTouched()) {
         gsm.set(new PlayState(gsm, road));
      }

   }

   @Override
   public void update(float dt) {
      handleInput();
   }

   @Override
   public void render(SpriteBatch sb) {
      sb.begin();
      sb.draw(background,0,0, RaceSimulation.WIDTH, RaceSimulation.HEIGHT);
      sb.draw(playBtn, (RaceSimulation.WIDTH/2) - (playBtn.getWidth()/2), (RaceSimulation.HEIGHT/2));
      sb.end();
   }

   @Override
   public void dispose() {
      background.dispose();
      playBtn.dispose();

      System.out.println("Menu State Disposed");
   }
}