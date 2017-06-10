package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPClientListener;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.guicomponent.GuiComponent;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.protocol.tcp.message.TCPScoreMessage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class RoomState extends State implements TCPClientListener {
   
   private GameStateManager gsm;

   private Stage stage;

   private TCPClient tcpClient;

   private List<String> playerList;

   private ScrollPane scrollPane;

   // Name of the room
   private String name = "";

   // Batch to draw the text
   private SpriteBatch batch;

   // Font to display the text
   private BitmapFont font;
   
   private TextButton refresh;
   
   private TextButton quit;

   public RoomState(GameStateManager gsm, TCPClient tcpClient) {
      super(gsm);
      
      this.gsm = gsm;

      // Set the TCPClient
      this.tcpClient = tcpClient;
      this.tcpClient.setListener(this);

      // Things for drawing title
      batch = new SpriteBatch();
      font = new BitmapFont();
      font.setColor(Color.LIGHT_GRAY);
      font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
      
      // Set the playerList
      playerList = new List<String>(GuiComponent.getSkin());

      int gameWidth = Gdx.graphics.getWidth();
      int gameHeight = Gdx.graphics.getHeight();
      stage = new Stage(new StretchViewport(gameWidth, gameHeight));

      // Create a join button and add it to the stage
      final TextButton ready = GuiComponent.createButton("I'm ready !", 200, 60);
      GuiComponent.centerGuiComponent(ready, stage, 0, -200);
      ready.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            RoomState.this.tcpClient.playerReady();
            ready.remove();
            GuiComponent.centerGuiComponent(refresh, stage, 150, -200);
         }
      });
      stage.addActor(ready);
      
      refresh = GuiComponent.createButton("Refresh info...", 200, 60);
      refresh.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            try {
               RoomState.this.tcpClient.getRoomInfo();
               refresh.setDisabled(true);
               refresh.setText("Loading...");
            } catch (IOException ex) {
               Logger.getLogger(RoomState.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      });
      GuiComponent.centerGuiComponent(refresh, stage, 150, -300);
      stage.addActor(refresh);
      
      quit = GuiComponent.createButton("Quit Room", 200, 60);
      quit.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            RoomState.this.tcpClient.quitRoom();
            RoomState.this.gsm.set(new RoomMenuState(RoomState.this.gsm, RoomState.this.tcpClient));
         }
      });
      GuiComponent.centerGuiComponent(quit, stage, -150, -300);
      stage.addActor(quit);

      Gdx.input.setInputProcessor(stage);

      try {
         tcpClient.getRoomInfo();
      } catch (IOException ex) {
         Logger.getLogger(RoomState.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Override
   protected void handleInput() {
   }

   @Override
   public void update(float dt) {
   }

   @Override
   public void render(SpriteBatch sb) {
      Gdx.gl.glClearColor(0, 0, 0, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      stage.draw();

      double gameWidth = Gdx.graphics.getWidth();
      double gameHeight = Gdx.graphics.getHeight();

      batch.begin();
      final GlyphLayout layout = new GlyphLayout(font, name);
      font.getData().setScale(2);
      font.draw(batch, layout, (int) (gameWidth - layout.width) / 2, (int) (gameHeight - 20));
      batch.end();
   }

   @Override
   public void dispose() {
      stage.dispose();
      batch.dispose();
      font.dispose();
   }

   @Override
   public void listRooms(java.util.List<TCPRoomMessage> rooms) {
   }

   @Override
   public void joinRoom() {
   }

   @Override
   public void roomInfo(TCPRoomInfoMessage msg) {

      final TCPRoomInfoMessage message = msg;

      // Do things in an another Thread
      Gdx.app.postRunnable(new Runnable() {

         @Override
         public void run() {
            
            // display refresh again
            refresh.setDisabled(false);
            refresh.setText("Refresh info...");
            
            // Get width of the game
            int gameWidth = Gdx.graphics.getWidth();
            int gameHeight = Gdx.graphics.getHeight();

            // Get the name of the room
            name = message.getName();

            // Retrieve the player information
            java.util.List<TCPPlayerInfoMessage> players = message.getPlayers();

            // Initialize an array of RoomListCell and fill it
            String[] buttons = new String[players.size()];
            int i = 0;
            for (TCPPlayerInfoMessage p : players) {
               buttons[i++] = String.format("%1$-20s", p.getUsername()) + 
                       " - " + String.format("%1$20s", p.getState());
            }

            // Set the list content
            playerList.setItems(buttons);
            
            if (scrollPane != null) {
               scrollPane.remove();
            }

            // Create a ScrollPane and add it to the stage
            scrollPane = new ScrollPane(playerList);
            scrollPane.setWidth(600);
            scrollPane.setHeight(500);
            scrollPane.setX(gameWidth / 2 - scrollPane.getWidth() / 2);
            scrollPane.setY(gameHeight / 2 - scrollPane.getHeight() / 2);
            scrollPane.setSmoothScrolling(false);
            scrollPane.setTransform(true);
            stage.addActor(scrollPane);

            Gdx.input.setInputProcessor(stage);
         }
      });
   }

   @Override
   public void connectUser() {
   }

   @Override
   public void registerUser() {
   }

   @Override
   public void errorNotification(TCPErrors.Error error) {
   }

   @Override
   public void getScores(java.util.List<TCPScoreMessage> msgs) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

}
