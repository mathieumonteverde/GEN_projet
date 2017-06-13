package com.heigvd.gen.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heigvd.gen.DBInterface.UserInfo;
import com.heigvd.gen.protocol.tcp.TCPProtocol;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomInfoMessage;
import com.heigvd.gen.protocol.udp.UDPProtocol;
import com.heigvd.gen.protocol.udp.message.UDPPlayerMessage;
import com.heigvd.gen.protocol.udp.message.UDPRaceMessage;
import com.heigvd.gen.server.TCPInterface.TCPServer;
import com.heigvd.gen.server.TCPInterface.TCPServerListener;
import com.heigvd.gen.server.TCPInterface.TCPServerWorker;
import com.heigvd.gen.server.UDPInterface.UDPServer;
import com.heigvd.gen.server.UDPInterface.UDPServerListener;
import com.heigvd.gen.useraccess.UserPrivilege;
import com.heigvd.gen.utils.JSONObjectConverter;
import com.sun.javafx.fxml.expression.UnaryExpression;
import com.sun.org.apache.xpath.internal.operations.UnaryOperation;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a Server room which will contain Players.
 *
 * @author mathieu
 */
public class ServerRoom implements UDPServerListener, TCPServerListener {

   //Number max of players
   public static final int MAX_PLAYERS = 4;
   // Incremental ID system
   private static int count = 0;
   // Name of the room
   private final String name;
   // ID of the room
   private final String ID;
   // Max number of players
   private final int maxPlayers = MAX_PLAYERS;
   // UDP server to send player information
   private UDPServer udp;

   // List of playerWorkers (players) in the room
   private LinkedList<TCPServerWorker> playerWorkers;

   // List of banned usernames
   private LinkedList<String> bannedPlayers;

   // Main server
   private GENServer server;

   /**
    * Constructor. Assigns the name of the room and sets data.
    *
    * @param name the name of the room
    * @param server the main server
    */
   public ServerRoom(String name, GENServer server) {
      this.name = name;
      ID = String.valueOf(ServerRoom.count++);//UUID.randomUUID().toString();
      playerWorkers = new LinkedList<>();
      bannedPlayers = new LinkedList<>();
      this.server = server;

      /*
         TODO remove all this crap
       */
//      playerWorkers.add(new Player("Valomat", "1234"));
//      playerWorkers.add(new Player("Mika", "1234"));
//      playerWorkers.add(new Player("Chaymae", "1234"));
//
//      playerWorkers.get(0).setX(150);
//      playerWorkers.get(0).setY(100);
//      playerWorkers.get(1).setX(100);
//      playerWorkers.get(1).setY(100);
//      playerWorkers.get(1).setColor(0);
//      playerWorkers.get(2).setX(250);
//      playerWorkers.get(2).setY(100);
//      playerWorkers.get(2).setColor(1);
   }

   /**
    * Get the name of the room
    *
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * Get the ID of the room
    *
    * @return the ID
    */
   public String getID() {
      return ID;
   }

   /**
    * Add a player worker to the room.
    *
    * @param worker
    * @throws Exception if the number max of playerWorkers is already reached
    */
   public synchronized void addPlayer(TCPServerWorker worker) throws Exception {
      if (playerWorkers.size() >= maxPlayers || isBanned(worker.getPlayer().getUsername())) {
         throw new Exception("Error: The room is already full.");
      }
      playerWorkers.add(worker);
      worker.getPlayer().setState(Player.State.WAITING);
      
      // Update all the workers
      for (TCPServerWorker w : playerWorkers) {
         if (w != worker) {
            roomInfos(worker);
         }
      }
   }

   /**
    * Checks if the ServerRoom contains given player
    *
    * @param worker
    * @return true if the ServerRoom contains the player
    */
   public synchronized boolean hasPlayer(TCPServerWorker worker) {
      return playerWorkers.contains(worker);
   }
   
   /**
    * Check if the room contiains a player identified by its username.
    * @param username the username of the player
    * @return true if the room contains this player
    */
   public synchronized boolean hasPlayer(String username) {
      return getPlayerWorker(username) != null;
   }

   /**
    * Remove a player from the ServerRoom
    *
    * @param p the player to remove
    */
   public synchronized void removePlayer(TCPServerWorker worker) {
      playerWorkers.remove(worker);
   }
   
   /**
    * Get a player worker by username
    * @param username the username of the player
    * @return the worker of the player or null if it doesn't exist
    */
   private TCPServerWorker getPlayerWorker(String username) {

      for (TCPServerWorker worker : playerWorkers) {
         if (worker.getPlayer().getUsername().equals(username)) {
            return worker;
         }
      }
      return null;
   }
   
   /**
    * Get the number of players
    * @return 
    */
   public synchronized int getNumberOfPlayers() {
      return playerWorkers.size();
   }
   
   /**
    * Return true if the room is ready to start the game
    * @return true if the room is ready to start
    */
   private boolean isReady() {

      if (playerWorkers.size() < 2) {
         return false;
      }

      for (TCPServerWorker worker : playerWorkers) {
         if (worker.getPlayer().getState() == Player.State.WAITING) {
            return false;
         }
      }

      return true;
   }
   
   /**
    * Ban a user from the room
    * @param username the user to ban 
    */
   private synchronized void banUser(String username) {
      TCPServerWorker worker = getPlayerWorker(username);

      if (worker != null) {
         playerWorkers.remove(worker);
         bannedPlayers.add(username);
         worker.roomDisconnection();
      }
   }
   
   /**
    * Check if the user is banned
    * @param p
    * @return 
    */
   public synchronized boolean isBanned(String username) {
      return bannedPlayers.contains(username);
   }
   
   /**
    * Delete the room
    */
   public synchronized void delete() {
      for (TCPServerWorker worker : playerWorkers) {
         worker.roomDisconnection();
      }
    }
   
   /**
    * Start the race
    */
   public synchronized void startRace() {
      createUDPServer();
      
      int port = udp.getPort();
      System.out.println(port);

      for (TCPServerWorker w : playerWorkers) {
         w.sendStart(port);
      }

      new Thread(new Runnable() {
         @Override
         public void run() {

            while (true) {
               sendRaceData();
               try {
                  synchronized (ServerRoom.this) {
                     ServerRoom.this.wait(10);
                  }
               } catch (InterruptedException ex) {
                  Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
         }
      }).start();
   }
   
   /**
    * Create the UDP server on which to send packets
    */
   public void createUDPServer() {
      if (udp == null) {
         try {
            udp = new UDPServer(this);
            new Thread(udp).start();
         } catch (SocketException ex) {
            Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
   
   /**
    * Receive player data from the udp client
    * @param player the player information
    */
   @Override
   public void receivePlayerData(UDPPlayerMessage player) {

      Player p = getPlayerWorker(player.getUsername()).getPlayer();

      if (p != null) {
         p.setX(player.getPosX());
         p.setY(player.getPosY());
         p.setVx(player.getVelX());
         p.setVy(player.getVelY());
         p.setColor(player.getColor());
      }
   }
   
   /**
    * Send race data to the clients
    */
   private void sendRaceData() {

      UDPRaceMessage race = new UDPRaceMessage();

      race.setRaceName("Hello");
      race.setTime(0);

      LinkedList<UDPPlayerMessage> playerMsgs = new LinkedList<>();

      for (TCPServerWorker worker : playerWorkers) {
         Player p = worker.getPlayer();
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

   /**
    * =========================================================================
    * TCPServerListener methods implementation
    * =========================================================================
    */
   @Override
   public void registerUser(TCPServerWorker worker) {
   }

   @Override
   public void connectUser(TCPServerWorker worker) {
   }

   @Override
   public void disconnectUser(TCPServerWorker worker) {
      playerWorkers.remove(worker.getPlayer());
   }

   @Override
   public void banUser(TCPServerWorker worker, String username) {

      UserInfo userToBan;
      try {
         userToBan = server.dbi.getUserInfo(username);
         if (UserPrivilege.isAdmin(worker.getPlayer().getPrivilege().ordinal())
                 && !UserPrivilege.isAdmin(userToBan.getRole())) {
            banUser(username);
            worker.banUser();
         } else {
            worker.notifyError(TCPProtocol.BAN_USER, TCPProtocol.WRONG_COMMAND);
         }
      } catch (SQLException ex) {
         Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
      }

   }

   @Override
   public void userRights(TCPServerWorker worker, String username, String role) {
   }

   @Override
   public void getUsers(TCPServerWorker worker) {
   }

   @Override
   public void getScores(TCPServerWorker worker, String username) {
   }

   @Override
   public void listRooms(TCPServerWorker worker) {
   }

   @Override
   public void joinRoom(TCPServerWorker worker, String roomID) {
   }

   @Override
   public void quitRoom(TCPServerWorker worker) {
      removePlayer(worker);
      worker.quitRoom();
   }

   @Override
   public void roomInfos(TCPServerWorker worker) {
      TCPRoomInfoMessage roomInfo = new TCPRoomInfoMessage();
      roomInfo.setName(getName());
      roomInfo.setID(getID());
      for (TCPServerWorker w : playerWorkers) {
         Player p = w.getPlayer();
         roomInfo.addPlayer(new TCPPlayerInfoMessage(p.getUsername(), p.getState().name()));
      }
      try {
         worker.sendRoomInfos(JSONObjectConverter.toJSON(roomInfo));
      } catch (JsonProcessingException ex) {
         Logger.getLogger(ServerRoom.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Override
   public void createRoom(TCPServerWorker worker, String roomName) {
   }

   @Override
   public void deleteRoom(TCPServerWorker worker, String roomID) {
   }

   @Override
   public void userReady(TCPServerWorker worker) {
      
      for (TCPServerWorker wr : playerWorkers) {
         roomInfos(worker);
      }
      
      worker.getPlayer().setState(Player.State.READY);
      if (isReady()) {
         startRace();
      }
   }

   @Override
   public void userFinished(TCPServerWorker worker) {

   }
}
