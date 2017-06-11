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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.heigvd.gen.RaceSimulation;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.guicomponent.GuiComponent;
import java.io.IOException;

/**
 * Special State for the Home Screen of the Game. This State offers to enter the
 * IP address of the server application, and to connect to this server.
 */
public class HomeScreenState extends State {

   // Hame State manager
   private GameStateManager gsm;

   // Stage to display content
   private Stage stage;

   // Background texture
   private Texture background;

   /**
    * Constructor. Create the field to enter the IP address and the button to
    * act on it.
    *
    * @param gsm the Game State manager
    */
   public HomeScreenState(GameStateManager gsm) {
      super(gsm);
      this.gsm = gsm;

      // Get width of the game
      int gameWidth = Gdx.graphics.getWidth();
      int gameHeight = Gdx.graphics.getHeight();

      // Create the stage
      stage = new Stage(new StretchViewport(gameWidth, gameHeight));

      // Create the background texture
      background = new Texture(Gdx.files.internal("GEN_home_bg.jpg"));

      // Create the text field to enter the IP address
      final TextField address = GuiComponent.createTextField("Server IP address");
      GuiComponent.centerGuiComponent(address, stage, 0, 30);
      stage.addActor(address);

      // Create the button to connect
      TextButton connect = GuiComponent.createButton("Connect to server...", 200, 60);
      GuiComponent.centerGuiComponent(connect, stage, 0, -30);
      // Add action on click
      connect.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {

            try {
               // Try to create a TCP client that will connect to the server
               TCPClient tcpClient = new TCPClient(address.getText(), 2525, null);
               // Start the client in another thread
               new Thread(tcpClient).start();

               // Change to the user connection state
               HomeScreenState.this.gsm.set(new UserConnectionState(HomeScreenState.this.gsm, tcpClient));
            } catch (IOException ex) {

               /*
§                 If the TCP client couldn't connect to the server, open a dialog
                  to notify the user.
                */
               GuiComponent.showDialog(stage, "Couldn't connect to the server...", "Try again...");
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
      // Draw background texture
      sb.begin();
      sb.draw(background, 0, 0, RaceSimulation.WIDTH, RaceSimulation.HEIGHT);
      sb.end();
      
      // Display the stage
      stage.act();
      stage.draw();
   }

   @Override
   public void dispose() {
      stage.dispose();
   }

}
