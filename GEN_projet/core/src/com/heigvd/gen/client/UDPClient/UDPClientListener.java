package com.heigvd.gen.client.UDPClient;

import com.heigvd.gen.protocol.udp.message.UDPRaceMessage;

/**
 * This interface represent object that can use the UDPClient class to be 
 * notified when a message arrives. UDPClient objects contain a reference to a
 * UDPClientListener which methods will be called when a message arrives
 * on its UDP socket.
 * 
 */
public interface UDPClientListener {
   /**
    * Callback method when race information arrives on the UDP socket.
    * @param raceMessage the race information as an object
    */
   public void receiveRaceData(UDPRaceMessage raceMessage);
}
