package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
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
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class RoomMenuState extends State implements TCPClientListener {
   
   // Stage to display content of the state
   private Stage stage;
   
   // State manager
   private GameStateManager g;
   
   // TCPClient to communicate with the server
   private TCPClient tcpClient;
   
   // List of rooms
   private List<RoomListCell> list;
   
   // ScrollPane to contain list of rooms
   private ScrollPane scrollPane;

   public RoomMenuState(GameStateManager gsm, TCPClient tcpClient) {
      super(gsm);
      this.g = gsm;
      
      // Set the TCPClient
      this.tcpClient = tcpClient;
      tcpClient.setListener(this);
      
      // Initialize the list of rooms
      list = new List<RoomListCell>(GuiComponent.getSkin());
      
      // Initialize an empty Stage
      stage = new Stage();
      
      // Get the list of rooms
      try {
         tcpClient.listRooms();
      } catch (IOException ex) {
         Logger.getLogger(RoomMenuState.class.getName()).log(Level.SEVERE, null, ex);
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
      stage.draw();
   }

   @Override
   public void dispose() {
      stage.dispose();
   }
   
   /**
    * Manage the response to the listRooms request
    * @param rooms the list of rooms
    */
   @Override
   public void listRooms(java.util.List<TCPRoomMessage> rooms) {
      final java.util.List<TCPRoomMessage> r = rooms;
      
      // Do things in an another Thread
      Gdx.app.postRunnable(new Runnable() {

         @Override
         public void run() {
            
            // Get width of the game
            int gameWidth = Gdx.graphics.getWidth();
            int gameHeight = Gdx.graphics.getHeight();
            
            // Initialize an array of RoomListCell and fill it
            RoomListCell[] buttons = new RoomListCell[r.size()];
            int i = 0;
            for (TCPRoomMessage room : r) {
               buttons[i++] = new RoomListCell(room);
            }
            
            // Set the list content
            list.setItems(buttons);
            
            // Create a ScrollPane and add it to the stage
            scrollPane = new ScrollPane(list);
            scrollPane.setWidth(600);
            scrollPane.setWidth(500);
            scrollPane.setX(gameWidth / 2 - scrollPane.getWidth() / 2);
            scrollPane.setY(gameHeight / 2 - scrollPane.getHeight() / 2);
            scrollPane.setSmoothScrolling(false);
            scrollPane.setTransform(true);
            stage = new Stage(new StretchViewport(gameWidth, gameHeight));
            stage.addActor(scrollPane);
            
            // Create a join button and add it to the stage
            TextButton join = GuiComponent.createButton("Rejoindre la salle", 200, 60);
            GuiComponent.centerGuiComponent(join, stage, 0, -250);
            join.addListener(new ChangeListener() {
               @Override
               public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                  RoomListCell selectedRoom = list.getSelected();
                  if (selectedRoom != null) {
                     try {
                        tcpClient.joinRoom(selectedRoom.getRoomInfo().getID());
                     } catch (IOException ex) {
                        Logger.getLogger(RoomMenuState.class.getName()).log(Level.SEVERE, null, ex);
                     }
                  }
               }
            });
            
            stage.addActor(join);

            Gdx.input.setInputProcessor(stage);
         }
      });
   }

   @Override
   public void joinRoom() {
      // Do things in an another Thread
      Gdx.app.postRunnable(new Runnable() {

         @Override
         public void run() {
            gsm.set(new RoomState(gsm, tcpClient));
         }
      });
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

   /**
    * Class to represent Room information and be able to display it 
    * and keep track of the essential information in an object
    */
   private class RoomListCell {
      // the correpsonding message
      private final TCPRoomMessage msg;
      
      /**
       * Construct the object
       * @param msg the msg to represent
       */
      public RoomListCell(TCPRoomMessage msg) {
         this.msg = msg;
      }
      
      /**
       * Retrieve the room information
       * @return 
       */
      public TCPRoomMessage getRoomInfo() {
         return msg;
      }
      
      @Override
      public String toString() {
         return msg.getName() + " - " + "Nombre de joueurs: " + msg.getNbPlayers();
      }
   }
}
