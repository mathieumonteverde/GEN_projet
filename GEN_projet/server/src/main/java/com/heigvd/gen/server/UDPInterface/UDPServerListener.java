package com.heigvd.gen.server.UDPInterface;

import com.heigvd.gen.protocol.udp.message.UDPPlayerMessage;

/**
 *
 */
public interface UDPServerListener {
   public void receivePlayerData(UDPPlayerMessage player);
}
