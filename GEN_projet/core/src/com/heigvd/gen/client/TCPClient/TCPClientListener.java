package com.heigvd.gen.client.TCPClient;

import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.protocol.tcp.message.TCPScoreMessage;
import java.util.List;

/**
 * This interface specifies the list of callback methods a TCPClient
 * object is going to call when an event occurs during its TCP communication
 * with the server.
 * 
 * 
 * @author mathieu
 */
public interface TCPClientListener {
   
   /**
    * The listRooms callback is called when when the TCPClient receives information
    * that concerns the list of rooms available on the server. 
    * 
    * @param rooms The list of rooms on the server
    */
   public void listRooms(List<TCPRoomMessage> rooms);
   
   /**
    * The joinRoom callback is called when the TCPClient has received the result
    * of a join room request
    * @param msg the room info message that is displayed when entering the room
    */
   public void joinRoom();
   
   /**
    * The roomInfo callback is called when the TCPClient has received information
    * concerning
    * @param msg 
    */
   public void roomInfo(TCPRoomInfoMessage msg);
   public void connectUser(int role);
   public void registerUser();
   
   public void getScores(List<TCPScoreMessage> msgs);
   
   public void errorNotification(TCPErrors.Error error);
}
