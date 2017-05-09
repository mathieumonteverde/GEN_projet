/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.protocol.tcp.message;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author mathieu
 */
public class TCPRoomListMessage {
   private LinkedList<TCPRoomMessage> rooms;
   
   public TCPRoomListMessage() {
      this.rooms = new LinkedList<>();
   }
   
   public void addRoomMessage(TCPRoomMessage room) {
      this.rooms.add(room);
   }
   
   public List<TCPRoomMessage> getRoomMessages() {
      return rooms;
   }
}
