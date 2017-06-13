package com.heigvd.gen.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.heigvd.gen.RaceSimulation;
import com.heigvd.gen.client.UDPClient.UDPClient;
import com.heigvd.gen.protocol.udp.UDPProtocol;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = RaceSimulation.WIDTH;
		config.height = RaceSimulation.HEIGHT;
		config.title = RaceSimulation.TITLE;
                
           try {
              UDPClient udp = new UDPClient(UDPProtocol.nextPort());
              new Thread(udp).start();
           } catch (SocketException ex) {
              Logger.getLogger(DesktopLauncher.class.getName()).log(Level.SEVERE, null, ex);
           }
                
		new LwjglApplication(new RaceSimulation(), config);
	}
}