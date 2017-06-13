package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.heigvd.gen.Player;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPClientListener;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.guicomponent.GuiComponent;
import com.heigvd.gen.guicomponent.RoomListCell;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.protocol.tcp.message.TCPScoreMessage;
import com.heigvd.gen.useraccess.UserPrivilege;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This specialized State offers to the user to brows the list of rooms 
 * available on the server, and to choose one to join. 
 * 
 * If the user has admin rights, he can also create or delete rooms.
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
   
   /**
    * Constructor. Create the UI elements and at the end, ask the TCP
    * client to fetch the list of rooms.
    * @param gsm the Game State Manager
    * @param tcpClient the TCP client to use
    */
   public RoomMenuState(GameStateManager gsm, TCPClient tcpClient) {
      super(gsm);
      this.g = gsm;

      // Set the TCPClient
      this.tcpClient = tcpClient;
      tcpClient.setListener(this);

      // Initialize the list of rooms
      list = new List<RoomListCell>(GuiComponent.getSkin());

      // Get width of the game
      int gameWidth = Gdx.graphics.getWidth();
      int gameHeight = Gdx.graphics.getHeight();

      // Initialize an empty Stage
      stage = new Stage(new StretchViewport(gameWidth, gameHeight));

      // Create a join button and add it to the stage
      TextButton join = GuiComponent.createButton("Join the room", 200, 60);
      GuiComponent.centerGuiComponent(join, stage, 0, -250);
      join.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            RoomListCell selectedRoom = list.getSelected();
            if (selectedRoom != null) {
               try {
                  RoomMenuState.this.tcpClient.joinRoom(selectedRoom.getID());
               } catch (IOException ex) {
                  Logger.getLogger(RoomMenuState.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
         }
      });
      stage.addActor(join);
      
      // Create button to go back to the main menu and add it to the stage
      TextButton back = GuiComponent.createButton("Back to menu", 160, 50);
      back.setX(gameWidth - back.getWidth() - 20);
      back.setY(gameHeight - back.getHeight() - 20);
      back.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            RoomMenuState.this.gsm.set(new MainMenuState(RoomMenuState.this.gsm, RoomMenuState.this.tcpClient));
         }
      });
      stage.addActor(back);
      
      /** 
       * If the user has admin rights, add the ui elements to manage rooms.
       */
      if (UserPrivilege.isAdmin(Player.getInstance().getRole())) {
         
         // Create and add the button to delete selected room
         TextButton delete = GuiComponent.createButton("Delete room", 160, 50);
         delete.setX(gameWidth - delete.getWidth() - 20);
         delete.setY(gameHeight - back.getHeight() - 100 - delete.getHeight());
         delete.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
               RoomListCell selectedRoom = list.getSelected();
               if (selectedRoom != null) {
                  RoomMenuState.this.tcpClient.deleteRoom(selectedRoom.getID());
                  try {
                     RoomMenuState.this.tcpClient.listRooms();
                  } catch (IOException ex) {
                     Logger.getLogger(RoomMenuState.class.getName()).log(Level.SEVERE, null, ex);
                  }
               }
            }
         });
         stage.addActor(delete);
         
         // Create and add text field to choose a name for a new room
         final TextField roomName = GuiComponent.createTextField("Room name");
         roomName.setX(gameWidth - roomName.getWidth() - 20);
         roomName.setY(delete.getY() - 20 - roomName.getHeight());
         stage.addActor(roomName);
         
         // Create and add the button to create a room
         TextButton create = GuiComponent.createButton("Create Room", 160, 50);
         create.setX(gameWidth - create.getWidth() - 20);
         create.setY(roomName.getY() - 20 - create.getHeight());
         create.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
               RoomMenuState.this.tcpClient.createRoom(roomName.getText());
               try {
                  RoomMenuState.this.tcpClient.listRooms();
               } catch (IOException ex) {
                  Logger.getLogger(RoomMenuState.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
         });
         stage.addActor(create);
      }

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
      stage.act();
      stage.draw();
   }

   @Override
   public void dispose() {
      stage.dispose();
   }

   /**
    * Manage the response to the listRooms request
    *
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
            if (scrollPane != null) {
               scrollPane.remove();
            }

            scrollPane = new ScrollPane(list);
            scrollPane.setWidth(600);
            scrollPane.setHeight(200);
            scrollPane.setX(gameWidth / 2 - scrollPane.getWidth() / 2);
            scrollPane.setY(gameHeight - scrollPane.getHeight() - 20);
            scrollPane.setSmoothScrolling(false);
            scrollPane.setTransform(true);
            stage.addActor(scrollPane);

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
   public void connectUser(int role) {
   }

   @Override
   public void registerUser() {

   }

   @Override
   public void getScores(java.util.List<TCPScoreMessage> msgs) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void disconnection() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void getUsers(java.util.List<TCPPlayerInfoMessage> users) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void userRights() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void errorNotification(TCPErrors.Error error) {
      
      // Filter errors
      switch(error) {
         case FULL_ROOM:
         GuiComponent.showDialog(stage, "This room is full", "Ok...");
         break;
         
         case BANNED_USER:
         GuiComponent.showDialog(stage, "You are banned from this room", "Ok...");
         break;
      }
   }

   @Override
   public void banUser() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void raceStart() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
}
