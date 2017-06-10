package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPClientListener;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.guicomponent.GuiComponent;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import java.util.List;

/**
 *
 * @author mathieu
 */
public class MainMenuState extends State implements TCPClientListener {
   
   private Stage stage;
   
   private TCPClient tcpClient;
   
   public MainMenuState(GameStateManager gsm, TCPClient tcpClient) {

      super(gsm);
      
      this.tcpClient = tcpClient;
      tcpClient.setListener(this);

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
            g.set(new RoomMenuState(g, MainMenuState.this.tcpClient));
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

   @Override
   public void listRooms(List<TCPRoomMessage> rooms) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void joinRoom() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void roomInfo(TCPRoomInfoMessage msg) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void connectUser() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void registerUser() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void errorNotification(TCPErrors.Error error) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

}