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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.heigvd.gen.button.Buttons;

/**
 *
 * @author mathieu
 */
public class InitState extends State {

   private Stage stage;
   TextButton button;

   public InitState(GameStateManager gsm) {
      super(gsm);
      
      final GameStateManager g = gsm;

      stage = new Stage();
      Gdx.input.setInputProcessor(stage);

      button = Buttons.createButton("Connect", 200, 60);
      button.addListener(new ChangeListener() {
        @Override
        public void changed (ChangeEvent event, Actor actor) {
            g.set(new MenuState(g));
        }
    });
      stage.addActor(button);
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

}
