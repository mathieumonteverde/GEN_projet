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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.heigvd.gen.guicomponent.GuiComponent;

/**
 *
 * @author mathieu
 */
public class UserConnectionState extends State {

   private Stage stage;
   TextButton button;
   TextField username;
   TextField password;

   public UserConnectionState(GameStateManager gsm) {
      super(gsm);

      final GameStateManager g = gsm;
      
      // Créer une stage pour contenir les éléments
      stage = new Stage();
      Gdx.input.setInputProcessor(stage);
      
      // Créer les champs textes
      username = GuiComponent.createTextField("Username...");
      password = GuiComponent.createTextField("Password...");
      
      // Créer le bouton de connection
      button = GuiComponent.createButton("Connect", 200, 60);
      button.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeEvent event, Actor actor) {
            g.set(new MainMenuState(g));
         }
      });
      
      // Utiliser l'utilitaire maison pour centrer les éléments et offset
      GuiComponent.centerGuiComponent(username, stage, 0, 0);
      GuiComponent.centerGuiComponent(password, stage, 0, -50);
      GuiComponent.centerGuiComponent(button, stage, 0, -120);
      
      // Ajouter au stage
      stage.addActor(username);
      stage.addActor(password);
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
      // Dessiner le stage
      stage.draw();
   }

   @Override
   public void dispose() {
      stage.dispose();
   }

}
