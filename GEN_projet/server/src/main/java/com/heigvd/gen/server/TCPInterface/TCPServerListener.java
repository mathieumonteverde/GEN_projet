package com.heigvd.gen.server.TCPInterface;

import com.heigvd.gen.server.Player;

/**
 * 
 */
public interface TCPServerListener {
   
   /**
    * Callback to notify that a user has requested to register to the server
    * @param worker the worker that emitted the callback
    */
   public void registerUser(TCPServerWorker worker);
   
   /**
    * Callback to notify that a user has requested to connect to the server
    * @param worker the worker that emitted the callback
    */
   public void connectUser(TCPServerWorker worker);
   
   /**
    * Callback to notify that a user has requested a disconnection.
    * @param worker the worker that emitted the callback
    */
   public void disconnectUser(TCPServerWorker worker);
   
   /**
    * Callback to notify that a user requested a ban of a user.
    * @param worker the worker that emitted the callback
    * @param username the username of the player to ban
    */
   public void banUser(TCPServerWorker worker, String username);
   
   /**
    * Callback to notify that a user requested to change a user right
    * @param worker the worker that emitted the callback
    * @param username the username to change the rights of
    * @param role the new role of the user
    */
   public void userRights(TCPServerWorker worker, String username, String role);
   
   /**
    * Callback to notify that a user requested the list of registered users
    * @param worker the worker that emitted the callback
    */
   public void getUsers(TCPServerWorker worker);
   
   /**
    * Callback to notify that a user requested the list of scores
    * @param worker the worker that emitted the callback
    */
   public void getScores(TCPServerWorker worker, String username);
   
   /**
    * Callback to notify that a user has requested the list of rooms.
    * @param worker the worker that emitted the callback
    */
   public void listRooms(TCPServerWorker worker);
   
   /**
    * Callback to notify that a user has requested to join a room
    * @param worker the worker that emitted the callback
    * @param roomID the id of the room as a String
    */
   public void joinRoom(TCPServerWorker worker, String roomID);
   
   /**
    * Callback to notify that a user has requested to quit the current room
    * @param worker the worker that emitted the callback
    */
   public void quitRoom(TCPServerWorker worker);
   
   /**
    * Callback to notify that a user has requested the room information
    * @param worker the worker that emitted the callback
    */
   public void roomInfos(TCPServerWorker worker);
   
   /**
    * Callback to notify that a user has requested to create a room
    * @param worker the worker that emitted the callback
    * @param roomName the name of the room to create
    */
   public void createRoom(TCPServerWorker worker, String roomName);
   
   /**
    * Callback to notify that a user has requested to delete a room by ID
    * @param worker the worker that emitted the callback
    * @param roomID the ID of the room to delete
    */
   public void deleteRoom(TCPServerWorker worker, String roomID);
   
   /**
    * Callback to notify that a user is ready to start a race.
    * @param worker the worker that emitted the callback
    */
   public void userReady(TCPServerWorker worker);
   
   /**
    * Callback to notify that a user has finished a race.
    * @param worker the worker that emitted the callback
    */
   public void userFinished(TCPServerWorker worker);
   
}
