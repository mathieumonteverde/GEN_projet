package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.heigvd.gen.RaceSimulation;

public class MenuState extends State {

   private Texture background;
   private Texture playBtn;

   public MenuState(GameStateManager gsm) {
      super(gsm);
      background = new Texture(Gdx.files.internal("bg.png"));
      playBtn = new Texture(Gdx.files.internal("playbtn.png"));
   }

   @Override
   public void handleInput() {

   }

   @Override
   public void update(float dt) {

   }

   @Override
   public void render(SpriteBatch sb) {
      sb.begin();
      sb.draw(background,0,0, RaceSimulation.WIDTH, RaceSimulation.HEIGHT);
      sb.draw(playBtn, (RaceSimulation.WIDTH/2) - (playBtn.getWidth()/2), (RaceSimulation.HEIGHT/2));
      sb.end();
   }
}
