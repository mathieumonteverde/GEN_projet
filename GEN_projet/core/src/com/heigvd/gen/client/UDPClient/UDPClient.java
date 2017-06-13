package com.heigvd.gen.client.UDPClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heigvd.gen.Player;
import com.heigvd.gen.protocol.udp.UDPProtocol;
import com.heigvd.gen.protocol.udp.message.UDPMessage;
import com.heigvd.gen.protocol.udp.message.UDPPlayerMessage;
import com.heigvd.gen.sprites.Bike;
import com.heigvd.gen.utils.JSONObjectConverter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

   // Socket to send and receive datagrams 
   private DatagramSocket socket;

   // Multicast receiver
   private MulticastSocket multiSocket;

   // Listener to call when datagrams are received
   private UDPClientListener listener;

   private int serverPort;

   public UDPClient(int port) throws SocketException {
      this(port, null);
   }

   public UDPClient(int port, UDPClientListener listener) throws SocketException {
      System.out.println(port);
      // Create the socket
      socket = new DatagramSocket(port);

      this.listener = listener;

      this.serverPort = port;

      try {
         multiSocket = new MulticastSocket(UDPProtocol.nextPort());
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
         DatagramPacket dataSent = new DatagramPacket(buffer, length, serveur, 2526);
         socket.send(dataSent);
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

   @Override
   public void run() {
      
      byte[] buffer = new byte[UDPProtocol.MAX_LENGTH];
      DatagramPacket dgram = new DatagramPacket(buffer, buffer.length);
      
      System.out.println("LOLOLOL");
      
      try {
         multiSocket.receive(dgram); // blocks until a datagram is received
         System.out.println(new String(dgram.getData()));
      } catch (IOException ex) {
         Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
