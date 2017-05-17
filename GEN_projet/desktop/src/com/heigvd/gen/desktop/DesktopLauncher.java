package com.heigvd.gen.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.heigvd.gen.RaceSimulation;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = RaceSimulation.WIDTH;
		config.height = RaceSimulation.HEIGHT;
		config.title = RaceSimulation.TITLE;
		new LwjglApplication(new RaceSimulation(), config);
	}
}
