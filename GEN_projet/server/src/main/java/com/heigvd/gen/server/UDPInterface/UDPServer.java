package com.heigvd.gen.server.UDPInterface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heigvd.gen.protocol.udp.UDPProtocol;
import com.heigvd.gen.protocol.udp.message.UDPMessage;
import com.heigvd.gen.protocol.udp.message.UDPPlayerMessage;
import com.heigvd.gen.protocol.udp.message.UDPRaceMessage;
import com.heigvd.gen.utils.JSONObjectConverter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class UDPServer implements Runnable {

   private DatagramSocket socket;

   private UDPServerListener listener;

   public UDPServer(UDPServerListener listener) throws SocketException {
      this.listener = listener;
      socket = new DatagramSocket();
   }
   
   public int getPort() {
      return socket.getLocalPort();
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
            String type = UDPProtocol.parseJsonObjectType(jsonData);
            
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
   
   public void sendRaceData(UDPRaceMessage race) {
      try {
         String jsonString = JSONObjectConverter.toJSON(race);
         
         byte[] buffer = jsonString.getBytes();
         
DatagramPacket dgram = new DatagramPacket(buffer, buffer.length,
  InetAddress.getByName(UDPProtocol.MULT_CAST), UDPProtocol.CLIENT_PORT);
         socket.send(dgram);
      } catch (JsonProcessingException ex) {
         Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
      } catch (UnknownHostException ex) {
         Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
      }
      
   }
}
