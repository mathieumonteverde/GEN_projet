package com.heigvd.gen.client.UDPClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heigvd.gen.Player;
import com.heigvd.gen.protocol.udp.UDPProtocol;
import com.heigvd.gen.protocol.udp.message.UDPMessage;
import com.heigvd.gen.protocol.udp.message.UDPPlayerMessage;
import com.heigvd.gen.protocol.udp.message.UDPRaceMessage;
import com.heigvd.gen.sprites.Bike;
import com.heigvd.gen.utils.JSONObjectConverter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class UDPClient implements Runnable {

   // Multicast receiver
   private MulticastSocket multiSocket;

   // Listener to call when datagrams are received
   private UDPClientListener listener;

   public UDPClient(int port) throws SocketException {
      this(port, null);
   }

   public UDPClient(int port, UDPClientListener listener) throws SocketException {

      this.listener = listener;

      try {

         //multiSocket = new MulticastSocket();
         multiSocket = new MulticastSocket(UDPProtocol.CLIENT_PORT);
         //multiSocket.setReuseAddress(true);
         //multiSocket.bind(new InetSocketAddress(3030));
         multiSocket.joinGroup(InetAddress.getByName(UDPProtocol.MULT_CAST));
      } catch (IOException ex) {
         Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void sendPlayerInfo(Bike bike, Player player) {
      UDPPlayerMessage msg = new UDPPlayerMessage();
      msg.setType(UDPMessage.TYPE.PLAYER_MESSAGE.toString());
      msg.setPosX(bike.getPosition().x);
      msg.setPosY(bike.getPosition().y);
      msg.setVelX(bike.getVelocity().x);
      msg.setVelY(bike.getVelocity().y);
      msg.setColor(bike.getColor().ordinal());
      msg.setUsername(player.getUsername());

      try {
         String jsonMsg = JSONObjectConverter.toJSON(msg);

         InetAddress serveur = InetAddress.getByName("localhost");
         int length = jsonMsg.length();
         byte buffer[] = jsonMsg.getBytes();
         DatagramPacket dataSent = new DatagramPacket(buffer, length, serveur, UDPProtocol.SERVER_PORT);
         multiSocket.send(dataSent);
      } catch (JsonProcessingException ex) {
         Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
      } catch (UnknownHostException ex) {
         Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void signalFinish() {

   }

   public void setListener(UDPClientListener listener) {
      this.listener = listener;
   }

   @Override
   public void run() {

      while (true) {

         byte[] buffer = new byte[UDPProtocol.MAX_LENGTH];
         DatagramPacket dgram = new DatagramPacket(buffer, buffer.length);

         try {
            multiSocket.receive(dgram); // blocks until a datagram is received

            if (listener != null) {

               String jsonData = new String(dgram.getData());

               /*
               Reading the type information of the message. We know it 
               is after the 8th character ('{"type"='). Then we get the first part 
               of the remaining String and remove the '"' characters.
            
                */
               String type = jsonData.substring(8).split(",")[0].replace("\"", "");

               // Process the data according to the type
               switch (UDPMessage.TYPE.valueOf(type)) {
                  case RACE_MESSAGE:
                     listener.receiveRaceData(JSONObjectConverter.fromJSON(jsonData, UDPRaceMessage.class));
                     break;
               }
            }
         } catch (IOException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
}
