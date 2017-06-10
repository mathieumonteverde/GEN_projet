package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
   
   private TextButton userFilter;
   
   private TextField userField;
   
   private TextButton allScores;
   
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
      
      // Create a field to choose a username
      userField = GuiComponent.createTextField("Search a username...");
      userField.setY(gameHeight - userField.getHeight() - 20);
      userField.setX(20);
      stage.addActor(userField);
      
      
      // Create a button to filter
      userFilter = GuiComponent.createButton("Get user scores...", 180, 50);
      userFilter.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            String username = userField.getText();
            ScoreState.this.tcpClient.getScores(username);
            allScores.setVisible(true);
         }
      });
      userFilter.setY(gameHeight - userField.getHeight() - 40 - userFilter.getHeight());
      userFilter.setX(20);
      stage.addActor(userFilter);
      
      
      allScores = GuiComponent.createButton("All scores...", 180, 50);
      allScores.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            allScores.setVisible(false);
            ScoreState.this.tcpClient.getScores();
         }
      });
      allScores.setVisible(false);
      allScores.setY(gameHeight - userField.getHeight() - 60 - userFilter.getHeight() * 2);
      allScores.setX(20);
      stage.addActor(allScores);
      
      TextButton back = GuiComponent.createButton("Back to menu", 160, 50);
      back.setX(gameWidth - back.getWidth() - 20);
      back.setY(gameHeight - back.getHeight() - 20);
      back.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            ScoreState.this.gsm.set(new MainMenuState(ScoreState.this.gsm, ScoreState.this.tcpClient));
         }
      });
      stage.addActor(back);
      

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
            
            table.add("Race Name").expandX();
            table.add("Username").expandX();
            table.add("Pos").expandX();
            table.add("Time").expandX();
            table.add("Date").expandX();
            table.row();
            
            
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
