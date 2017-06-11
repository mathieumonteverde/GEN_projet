package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.heigvd.gen.Player;
import com.heigvd.gen.RaceSimulation;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.guicomponent.GuiComponent;
import com.heigvd.gen.useraccess.UserPrivilege;
import java.io.IOException;
import java.util.Timer;

/**
 *
 * @author mathieu
 */
public class HomeScreenState extends State {

   private GameStateManager gsm;

   private Stage stage;

   private TCPClient tcpClient;

   private Texture background;

   public HomeScreenState(GameStateManager gsm) {
      super(gsm);
      this.gsm = gsm;

      // Get width of the game
      int gameWidth = Gdx.graphics.getWidth();
      int gameHeight = Gdx.graphics.getHeight();
      stage = new Stage(new StretchViewport(gameWidth, gameHeight));

      background = new Texture(Gdx.files.internal("GEN_home_bg.jpg"));

      final TextField address = GuiComponent.createTextField("Server IP address");
      GuiComponent.centerGuiComponent(address, stage, 0, 30);
      stage.addActor(address);

      TextButton connect = GuiComponent.createButton("Connect to server...", 200, 60);
      GuiComponent.centerGuiComponent(connect, stage, 0, -30);
      connect.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {

            try {
               // Connect to the db
               tcpClient = new TCPClient(address.getText(), 2525, null);
               new Thread(tcpClient).start();

               HomeScreenState.this.gsm.set(new UserConnectionState(HomeScreenState.this.gsm, tcpClient));

            } catch (IOException ex) {

               final Dialog endDialog = new Dialog("Error when trying to connect to the server...", GuiComponent.getSkin()) {
                  protected void result(Object object) {

                     Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                           hide();
                        }
                     });
                  }
               };
               endDialog.button("Try Again...", 1L);

               Gdx.app.postRunnable(new Runnable() {
                  @Override
                  public void run() {
                     endDialog.show(stage);
                  }
               });
            }
         }
      });
      stage.addActor(connect);

      Gdx.input.setInputProcessor(stage);

   }

   @Override
   protected void handleInput() {
   }

   @Override
   public void update(float dt) {
   }

   @Override
   public void render(SpriteBatch sb) {
      sb.begin();
      sb.draw(background, 0, 0, RaceSimulation.WIDTH, RaceSimulation.HEIGHT);
      sb.end();
      stage.act();
      stage.draw();
   }

   @Override
   public void dispose() {
      stage.dispose();
   }

}
