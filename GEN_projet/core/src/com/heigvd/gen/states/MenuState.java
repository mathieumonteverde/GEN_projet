package com.heigvd.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.heigvd.gen.RaceSimulation;
import com.heigvd.gen.client.UDPClient.UDPClient;
import com.heigvd.gen.client.UDPClient.UDPClientListener;
import com.heigvd.gen.protocol.udp.message.UDPRaceMessage;
import com.heigvd.gen.sprites.Road;
import com.heigvd.gen.sprites.RoadLine;
import com.heigvd.gen.utils.Constants;
import com.heigvd.gen.utils.MapImporter;
import javafx.util.Pair;

import java.util.ArrayList;

public class MenuState extends State implements UDPClientListener {

   private Texture background;
   private Texture playBtn;
   
   private UDPClient udpClient;
   
   Road road;

   public MenuState(GameStateManager gsm, UDPClient udpClient) {
      super(gsm);
      final GameStateManager game = gsm;
      background = new Texture(Gdx.files.internal("bg.png"));
      playBtn = new Texture(Gdx.files.internal("playbtn.png"));
      Drawable drawable = new TextureRegionDrawable(new TextureRegion(playBtn));
      ImageButton playButton = new ImageButton(drawable);
      ArrayList<Road> roads = MapImporter.importRoads();
      road = roads.get(0);
      System.out.println("Loaded : "+road.getName());
      this.udpClient = udpClient;
      udpClient.setListener(this);
   }

   @Override
   public void handleInput() {
      if(Gdx.input.justTouched()) {
         gsm.set(new PlayState(gsm, road, udpClient));
      }

   }

   @Override
   public void update(float dt) {
      handleInput();
   }

   @Override
   public void render(SpriteBatch sb) {
      sb.begin();
      sb.draw(background,0,0, RaceSimulation.WIDTH, RaceSimulation.HEIGHT);
      sb.draw(playBtn, (RaceSimulation.WIDTH/2) - (playBtn.getWidth()/2), (RaceSimulation.HEIGHT/2));
      sb.end();
   }

   @Override
   public void dispose() {
      background.dispose();
      playBtn.dispose();

      System.out.println("Menu State Disposed");
   }

   @Override
   public void receiveRaceData(UDPRaceMessage raceMessage) {
   }
}
