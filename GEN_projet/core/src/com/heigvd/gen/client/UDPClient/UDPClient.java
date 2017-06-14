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
 * This class implements an UDPClient to manage the GEN project UDP protocol.
 * 
 * A user can register a listener to be notified when arrives on the 
 * UDP socket and to receive this data by callback.
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
         multiSocket.joinGroup(InetAddress.getByName(UDPProtocol.MULT_CAST));
      } catch (IOException ex) {
         Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
   
   /**
    * Set the listener to call when data is received.
    * @param listener the listener to call
    */
   public void setListener(UDPClientListener listener) {
      this.listener = listener;
   }
   
   /**
    * Send data regarding the player to the server. The method is executed in 
    * another thread and extracts data from the the object passed as parameters.
    * @param bike the bike played by the player
    * @param player player object
    */
   public void sendPlayerInfo(Bike bike, Player player) {
      // Save data as final objects
      final Bike b = bike;
      final Player p = player;

      // Execute everything in another thread
      new Thread(new Runnable() {
         @Override
         public void run() {
            // Create and populate the message object
            UDPPlayerMessage msg = new UDPPlayerMessage();
            msg.setType(UDPMessage.TYPE.PLAYER_MESSAGE.toString());
            msg.setPosX(b.getPosition().x);
            msg.setPosY(b.getPosition().y);
            msg.setVelX(b.getVelocity().x);
            msg.setVelY(b.getVelocity().y);
            msg.setColor(b.getColor().ordinal());
            msg.setUsername(p.getUsername());

            try {
               // Convert the object to JSON
               String jsonMsg = JSONObjectConverter.toJSON(msg);
                
               // Send the object data as a DatagramPacket
               InetAddress serveur = InetAddress.getByName(UDPProtocol.SERVER_ADDR);
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
      }).start();

   }
   
   /**
    * Signal to the server that a client has finished its race.
    */
   public void signalFinish() {

   }
   
   /**
    * Main programm of the UDPClient. The UDPClient will wait to receive
    * data on the multicast socket, and call the callback methods from the 
    * listener.
    */
   @Override
   public void run() {

      while (true) {
         
         // Where to store the data
         byte[] buffer = new byte[UDPProtocol.MAX_LENGTH];
         DatagramPacket dgram = new DatagramPacket(buffer, buffer.length);

         try {
            // Wait to receive the data
            multiSocket.receive(dgram); // blocks until a datagram is received
            
            // If a listener is set, process the data, and call the method associated
            if (listener != null) {

               String jsonData = new String(dgram.getData());

               /*
                  Reading the type information of the message. We know it 
                  is after the 8th character ('{"type"='). Then we get the first part 
                  of the remaining String and remove the '"' characters.
                */
               String type = UDPProtocol.parseJsonObjectType(jsonData);

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
