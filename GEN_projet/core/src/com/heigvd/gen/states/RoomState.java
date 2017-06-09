package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.heigvd.gen.client.TCPClient.TCPClient;
import com.heigvd.gen.client.TCPClient.TCPClientListener;
import com.heigvd.gen.client.TCPClient.TCPErrors;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import static java.awt.SystemColor.text;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class RoomState extends State implements TCPClientListener {

   private Stage stage;

   private TCPClient tcpClient;

   // Name of the room
   private String name = "Hello World !";

   // Batch to draw the text
   private SpriteBatch batch;

   // Font to display the text
   private BitmapFont font;

   public RoomState(GameStateManager gsm, TCPClient tcpClient) {
      super(gsm);

      this.tcpClient = tcpClient;
      this.tcpClient.setListener(this);

      batch = new SpriteBatch();
      font = new BitmapFont();
      font.setColor(Color.LIGHT_GRAY);
      font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

      stage = new Stage();
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
      font.draw(batch, layout, (int)(gameWidth - layout.width) / 2, (int)(gameHeight - 20));
      batch.end();
   }

   @Override
   public void dispose() {
      stage.dispose();
      batch.dispose();
      font.dispose();
   }

   @Override
   public void listRooms(List<TCPRoomMessage> rooms) {
   }

   @Override
   public void joinRoom() {
   }

   @Override
   public void roomInfo(TCPRoomInfoMessage msg) {
      name = msg.getName();
   }

   @Override
   public void connectUser() {
   }

   @Override
   public void registerUser() {
   }

   @Override
   public void errorNotification(TCPErrors.Error error) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

}
