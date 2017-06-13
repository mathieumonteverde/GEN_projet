package com.heigvd.gen.server;

import com.heigvd.gen.protocol.udp.UDPProtocol;
import com.heigvd.gen.protocol.udp.message.UDPPlayerMessage;
import com.heigvd.gen.protocol.udp.message.UDPRaceMessage;
import com.heigvd.gen.server.UDPInterface.UDPServer;
import com.heigvd.gen.server.UDPInterface.UDPServerListener;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a Server room which will contain Players.
 *
 * For now, it is only a container but it will probably need to implement
 * Runnable in order to have a behaviour of its own.
 *
 * It extends Observable in order to be watchable by the TCP communication for
 * example
 *
 * @author mathieu
 */
public class ServerRoom extends Observable implements UDPServerListener {

   private static int count = 0; // Dummy id for now

   private final String name; // Name of the room
   private final String ID; // ID of the room
   private final int maxPlayers = 4; // May of players
   private boolean isDeleted = false;

   private UDPServer udp;

   // List of players waiting inside the room
   private LinkedList<Player> players;

   // 
   private LinkedList<Player> bannedPlayers;

   /**
    * Constructor
    *
    * @param name
    */
   public ServerRoom(String name) {
      this.name = name;
      ID = String.valueOf(ServerRoom.count++);//UUID.randomUUID().toString();
      players = new LinkedList<>();
      bannedPlayers = new LinkedList<>();
      
      /*
         TODO remove all this crap
      */
      players.add(new Player("Valomat", "1234"));
      players.add(new Player("Mika", "1234"));
      players.add(new Player("Chaymae", "1234"));
   }

   public String getName() {
      return name;
   }

   public String getID() {
      return ID;
   }

   /**
    * Add a player to the room
    *
    * @param p the player to add
    * @throws Exception if the number max of players is already reached
    */
   public synchronized void addPlayer(Player p) throws Exception {
      if (players.size() >= maxPlayers || bannedPlayers.contains(p)) {
         throw new Exception("Error: The room is already full.");
      }
      players.add(p);
      p.setState(Player.State.WAITING);
      setChanged();
      notifyObservers();
   }

   /**
    * Checks if the ServerRoom contains given player
    *
    * @param p the player
    * @return true if the ServerRoom contains the player
    */
   public boolean hasPlayer(Player p) {
      return players.contains(p);
   }

   public boolean hasPlayer(String username) {
      for (Player p : players) {
         if (p.getUsername().equals(username)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Remove a player from the ServerRoom
    *
    * @param p the player to remove
    */
   public synchronized void removePlayer(Player p) {
      players.remove(p);
      setChanged();
      notifyObservers();
   }

   /**
    * Return the list of players actually inside the room
    *
    * @return
    */
   public synchronized List<Player> getPlayers() {
      return players;
   }

   private Player getPlayer(String username) {

      for (Player p : players) {
         if (p.getUsername().equals(username)) {
            return p;
         }
      }
      return null;
   }

   /**
    * Set a player to be ready
    *
    * @param p
    */
   public synchronized void setPlayerReady(Player p) {
      if (hasPlayer(p)) {
         p.setState(Player.State.READY);
         setChanged();
         notifyObservers();
      }
   }

   public synchronized boolean isReady() {

      if (players.size() < 2) {
         return false;
      }

      for (Player p : players) {
         if (p.getState() == Player.State.WAITING) {
            return false;
         }
      }

      return true;
   }

   public synchronized void banUser(String username) {
      Player player = null;
      for (Player p : players) {
         if (p.getUsername().equals(username)) {
            player = p;
         }
      }

      if (player != null) {
         players.remove(player);
         bannedPlayers.add(player);
      }

      setChanged();
      notifyObservers();
   }

   public synchronized boolean isBanned(Player p) {
      return bannedPlayers.contains(p);
   }

   public void delete() {
      isDeleted = true;
      setChanged();
      notifyObservers();
   }

   public boolean isDeleted() {
      return isDeleted;
   }

   public void startRace() {
      createUDPServer();

      new Thread(new Runnable() {
         @Override
         public void run() {
            while (true) {
               System.out.println("Sending data");
               sendRaceData();
               try {
                  synchronized(ServerRoom.this) {
                     ServerRoom.this.wait(1000);
                  }
               } catch (InterruptedException ex) {
                  Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
         }
      }).start();
   }

   public void createUDPServer() {
      if (udp == null) {
         try {
            udp = new UDPServer(this, UDPProtocol.SERVER_PORT);
            new Thread(udp).start();
         } catch (SocketException ex) {
            Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   @Override
   public void receivePlayerData(UDPPlayerMessage player) {

      Player p = getPlayer(player.getUsername());

      if (p != null) {
         p.setX(player.getPosX());
         p.setY(player.getPosY());
         p.setVx(player.getVelX());
         p.setVy(player.getVelY());
         p.setColor(player.getColor());
      }
   }

   private void sendRaceData() {

      UDPRaceMessage race = new UDPRaceMessage();
      System.out.println(race.getType());

      race.setRaceName("Hello");
      race.setTime(0);

      LinkedList<UDPPlayerMessage> playerMsgs = new LinkedList<>();

      for (Player p : players) {
         UDPPlayerMessage msg = new UDPPlayerMessage();
         msg.setPosX(p.getX());
         msg.setPosY(p.getY());
         msg.setVelX(p.getVx());
         msg.setVelX(p.getVy());
         msg.setUsername(p.getUsername());
         playerMsgs.add(msg);
      }
      race.setPlayers(playerMsgs);
      udp.sendRaceData(race);

   }
}
