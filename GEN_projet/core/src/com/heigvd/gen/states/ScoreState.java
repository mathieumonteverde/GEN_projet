package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPClientListener;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.guicomponent.GuiComponent;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.protocol.tcp.message.TCPScoreMessage;

/**
 *
 * @author mathieu
 */
public class ScoreState extends State implements TCPClientListener {
   
   private GameStateManager gsm;

   private Stage stage;

   private TCPClient tcpClient;
   
   // Table des scores
   private Table table;
   
   // ScrollPane to contain list of scores
   private ScrollPane scrollPane;
   
   public ScoreState(GameStateManager gsm, TCPClient tcpClient) {
      super(gsm);
      
      this.gsm = gsm;

      // Set the TCPClient
      this.tcpClient = tcpClient;
      this.tcpClient.setListener(this);
      
      int gameWidth = Gdx.graphics.getWidth();
      int gameHeight = Gdx.graphics.getHeight();
      stage = new Stage(new StretchViewport(gameWidth, gameHeight));
      

      Gdx.input.setInputProcessor(stage);

      tcpClient.getScores();
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

   @Override
   public void listRooms(java.util.List<TCPRoomMessage> rooms) {
   }

   @Override
   public void joinRoom() {
   }

   @Override
   public void roomInfo(TCPRoomInfoMessage msg) {
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
      
      
      final java.util.List<TCPScoreMessage> scores = msgs;
      
      // Do things in an another Thread
      Gdx.app.postRunnable(new Runnable() {

         @Override
         public void run() {
            
            // Get width of the game
            int gameWidth = Gdx.graphics.getWidth();
            int gameHeight = Gdx.graphics.getHeight();
            
            table = new Table(GuiComponent.getSkin());
            table.setFillParent(true);
            table.setBackground(GuiComponent.getSkin().getDrawable("default-window"));
            
            
            // Initialize an array of RoomListCell and fill it
            for (TCPScoreMessage score : scores) {
               table.add(score.getRaceName()).expandX();
               table.add(score.getUsername()).expandX();
               table.add(String.valueOf(score.getPosition())).expandX();
               table.add(String.valueOf(score.getTime())).expandX();
               table.add(score.getDate()).expandX();
               table.row();
            }
            
            
            if (scrollPane != null) {
               scrollPane.remove();
            }
            
            // Create a ScrollPane and add it to the stage
            scrollPane = new ScrollPane(table);
            scrollPane.setWidth(600);
            scrollPane.setHeight(gameHeight);
            scrollPane.setX(gameWidth / 2 - scrollPane.getWidth() / 2);
            scrollPane.setY(0);
            scrollPane.setTransform(true);
            scrollPane.setColor(Color.RED);
            
            stage.addActor(scrollPane);
         }
      });
      
      
      
   }
   
}
