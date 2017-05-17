/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.client.TCPClient;

import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import java.util.List;

/**
 *
 * @author mathieu
 */
public interface TCPClientListener {
   public void listRooms(List<TCPRoomMessage> rooms);
   public void joinRoom(String error, TCPRoomInfoMessage msg);
   public void roomInfo(TCPRoomInfoMessage msg);
   public void connectUser(String error);
   public void registerUser(String error);
}
