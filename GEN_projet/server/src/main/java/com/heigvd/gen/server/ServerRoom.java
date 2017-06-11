package com.heigvd.gen.server;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 * This class represents a Server room which will contain Players.
 * 
 * For now, it is only a container but it will probably need to implement 
 * Runnable in order to have a behaviour of its own.
 * 
 * It extends Observable in order to be watchable by the TCP communication
 * for example
 * @author mathieu
 */
public class ServerRoom extends Observable {
   private static int count = 0; // Dummy id for now
   
   private final String name; // Name of the room
   private final String ID; // ID of the room
   private final int maxPlayers = 4; // May of players
   
   // List of players waiting inside the room
   private LinkedList<Player> players;
   
   /**
    * Constructor
    * @param name 
    */
   public ServerRoom(String name) {
      this.name = name;
      ID = String.valueOf(ServerRoom.count++);//UUID.randomUUID().toString();
      players = new LinkedList<>();
   }
   
   public String getName() {
      return name;
   }
   public String getID() {
      return ID;
   }
   
   /**
    * Add a player to the room
    * @param p the player to add
    * @throws Exception if the number max of players is already reached
    */
   public synchronized void addPlayer(Player p) throws Exception {
      if (players.size() == maxPlayers) {
         throw new Exception("Error: The room is already full.");
      }
      players.add(p);
      p.setState(Player.State.WAITING);
      setChanged();
      notifyObservers();
   }
   
   /**
    * Checks if the ServerRoom contains given player
    * @param p the player
    * @return true if the ServerRoom contains the player
    */
   public boolean hasPlayer(Player p) {
      return players.contains(p);
   }
   
   /**
    * Remove a player from the ServerRoom
    * @param p the player to remove
    */
   public synchronized void removePlayer(Player p) {
      players.remove(p);
      setChanged();
      notifyObservers();
   }
   
   /**
    * Return the list of players actually inside the room
    * @return 
    */
   public List<Player> getPlayers() {
      return players;
   }
   
   /**
    * Set a player to be ready
    * @param p 
    */
   public synchronized void setPlayerReady(Player p) {
      if ( hasPlayer(p) ) {
         p.setState(Player.State.READY);
         setChanged();
         notifyObservers();
      }
   }
   
   public synchronized boolean isReady() {
      
      if (players.size() < 2) {
         return false;
      }
      
      for(Player p : players) {
         if (p.getState() == Player.State.WAITING)
            return false;
      }
      
      return true;
   }
   
   
   public void delete() {
      
   }
}
