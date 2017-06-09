package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.controllers.ControlType.button;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.heigvd.gen.guicomponent.GuiComponent;

/**
 *
 * @author mathieu
 */
public class MainMenuState extends State {
   
   private Stage stage;
   
   public MainMenuState(GameStateManager gsm) {

      super(gsm);

      final GameStateManager g = gsm;

      // Créer une stage pour contenir les éléments
      stage = new Stage();
      Gdx.input.setInputProcessor(stage);
      
      TextButton rooms = GuiComponent.createButton("Liste des salles...", 200, 70);
      TextButton scores = GuiComponent.createButton("Scores...", 200, 70);
      TextButton admin = GuiComponent.createButton("Espace administration...", 200, 70);
      
      rooms.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            g.set(new MenuState(g));
         }
      });
      
      GuiComponent.centerGuiComponent(rooms, stage, 0, 100);
      GuiComponent.centerGuiComponent(scores, stage, 0, 0);
      GuiComponent.centerGuiComponent(admin, stage, 0, -100);

      // Ajouter au stage
      stage.addActor(rooms);
      stage.addActor(scores);
      stage.addActor(admin);
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
