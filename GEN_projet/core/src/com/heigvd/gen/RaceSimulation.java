package com.heigvd.gen;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.heigvd.gen.client.UDPClient.UDPClient;
import com.heigvd.gen.protocol.udp.UDPProtocol;
import com.heigvd.gen.states.GameStateManager;
import com.heigvd.gen.states.HomeScreenState;
import com.heigvd.gen.states.MenuState;
import com.heigvd.gen.states.UserConnectionState;

import java.net.SocketException;

public class RaceSimulation extends ApplicationAdapter {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final String TITLE = "Race";
	private GameStateManager gsm;
	private SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
      Gdx.gl.glClearColor(0, 0, 0, 1);
		try {
			UDPClient udp = new UDPClient(UDPProtocol.SERVER_PORT);
			new Thread(udp).start();

			gsm.push(new MenuState(gsm, udp));

		} catch (SocketException ex) {

		}
	}

	@Override
	public void render () {

      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
