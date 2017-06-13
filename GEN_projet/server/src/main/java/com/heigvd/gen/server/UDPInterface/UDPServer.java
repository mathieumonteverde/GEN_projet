package com.heigvd.gen.server.UDPInterface;

import com.heigvd.gen.protocol.udp.message.UDPMessage;
import com.heigvd.gen.protocol.udp.message.UDPPlayerMessage;
import com.heigvd.gen.utils.JSONObjectConverter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class UDPServer implements Runnable {

   private DatagramSocket socket;

   private UDPServerListener listener;

   public UDPServer(UDPServerListener listener, int port) throws SocketException {
      this.listener = listener;
      socket = new DatagramSocket(port);
      System.out.println(port);
   }

   @Override
   public void run() {

      while (true) {

         byte[] buffer = new byte[4096];

         DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

         try {
            socket.receive(dp);
            String jsonData = new String(dp.getData());
            
            /*
               Reading the type information of the message. We know it 
               is after the 8th character ('{"type"='). Then we get the first part 
               of the remaining String and remove the '"' characters.
            
            */
            String type = jsonData.substring(8).split(",")[0].replace("\"", "");
            
            // Process the data according to the type
            switch (UDPMessage.TYPE.valueOf(type)) {
               case PLAYER_MESSAGE:
                  listener.receivePlayerData(JSONObjectConverter.fromJSON(jsonData, UDPPlayerMessage.class));
                  break;
            }
            
         } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
         }
      }

   }
}
