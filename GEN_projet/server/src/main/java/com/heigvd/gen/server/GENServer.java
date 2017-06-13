/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heigvd.gen.DBInterface.DBInterface;
import com.heigvd.gen.DBInterface.UserInfo;
import com.heigvd.gen.DBInterface.exception.UsedUsernameException;
import com.heigvd.gen.protocol.tcp.TCPProtocol;
import com.heigvd.gen.protocol.tcp.message.TCPPlayerInfoMessage;
import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;
import com.heigvd.gen.protocol.tcp.message.TCPScoreMessage;
import com.heigvd.gen.server.TCPInterface.TCPServer;
import com.heigvd.gen.server.TCPInterface.TCPServerListener;
import com.heigvd.gen.server.TCPInterface.TCPServerWorker;
import com.heigvd.gen.server.TCPInterface.WorkerConnectState;
import com.heigvd.gen.server.TCPInterface.WorkerDefaultState;
import com.heigvd.gen.server.TCPInterface.WorkerRoomState;
import com.heigvd.gen.useraccess.UserPrivilege;
import com.heigvd.gen.utils.JSONObjectConverter;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main server application
 *
 * @author mathieu
 */
public class GENServer implements TCPServerListener {

   // List of rooms
   private LinkedList<ServerRoom> rooms;
   // The TCPServer object
   private TCPServer tcpServer;

   // DB interface
   DBInterface dbi = new DBInterface();

   /**
    * Constructor, creates the TCPServer active object
    */
   public GENServer() {
      rooms = new LinkedList<>();

      // Create 3 rooms
      rooms.add(new ServerRoom("Funny room", this));
      rooms.add(new ServerRoom("Competition room", this));
      rooms.add(new ServerRoom("OkeyDokey room", this));

      //rooms.get(0).createUDPServer();
      //rooms.get(0).startRace();
      // Create the TCPServer
      tcpServer = new TCPServer(this, 2525);
      new Thread(tcpServer).start();

   }

   /**
    * Returns the rooms on the server
    *
    * @return
    */
   public List<ServerRoom> getServerRooms() {
      return rooms;
   }

   /**
    * Returns the TCP server
    *
    * @return
    */
   public TCPServer getTCPServer() {
      return tcpServer;
   }

   /**
    * Return a specific room by ID
    *
    * @param ID the ID of the room
    * @return the room of null if invalid ID
    */
   public ServerRoom getServerRoom(String ID) {
      for (ServerRoom room : rooms) {
         if (room.getID().equals(ID)) {
            return room;
         }
      }
      return null;
   }

   public void createServerRoom(String name) {
      rooms.add((new ServerRoom(name, this)));
   }

   public boolean deleteServerRoom(String id) {
      ServerRoom room = getServerRoom(id);
      if (room == null) {
         return false;
      }

      // Remove the room
      rooms.remove(room);

      // Notifie the room it has been deleted
      room.delete();

      return true;
   }

   /**
    * Return The DBInterface used by ther server
    *
    * @return the DBInterface
    */
   public DBInterface getDatabaseInterface() {
      return dbi;
   }

   /**
    * Main app function
    *
    * @param args
    */
   public static void main(String[] args) {
      GENServer server = new GENServer();
   }

   @Override
   public void registerUser(TCPServerWorker worker) {
      Player player = worker.getPlayer();

      try {

         dbi.registerUser(player.getUsername(), player.getPassword());
         worker.registerUser();

      } catch (SQLException ex) {
         worker.notifyError(TCPProtocol.REGISTER_USER, TCPProtocol.USED_USERNAME);

      } catch (UsedUsernameException ex) {
         worker.notifyError(TCPProtocol.REGISTER_USER, TCPProtocol.USED_USERNAME);
      }
   }

   @Override
   public void connectUser(TCPServerWorker worker) {

      Player player = worker.getPlayer();

      try {
         if (dbi.connectUser(player.getUsername(), player.getPassword())) {
            UserInfo ui = dbi.getUserInfo(player.getUsername());
            int role = ui.getRole();
            player.setPrivilege(UserPrivilege.Privilege.values()[role]);
            worker.connectUser(role);

         } else {
            worker.notifyError(TCPProtocol.CONNECT_USER, TCPProtocol.BAD_AUTHENTIFICATION);
         }
      } catch (SQLException ex) {
         worker.notifyError(TCPProtocol.CONNECT_USER, TCPProtocol.BAD_AUTHENTIFICATION);
      }
   }

   @Override
   public void disconnectUser(TCPServerWorker worker) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void banUser(TCPServerWorker worker, String username) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void userRights(TCPServerWorker worker, String username, String role) {
      if (UserPrivilege.isAdmin(worker.getPlayer().getPrivilege().ordinal())) {

         try {
            UserInfo userInfo = dbi.getUserInfo(username);

            if (!UserPrivilege.isSuperAdmin(userInfo.getRole())) {
               dbi.changeUserRole(username, UserPrivilege.Privilege.valueOf(role));
               worker.changeRights();
            } else {
               worker.notifyError(TCPProtocol.USER_RIGHTS, TCPProtocol.WRONG_COMMAND);
            }
         } catch (SQLException ex) {
            Logger.getLogger(GENServer.class.getName()).log(Level.SEVERE, null, ex);
         }

      } else {
         worker.notifyError(TCPProtocol.USER_RIGHTS, TCPProtocol.WRONG_COMMAND);
      }
   }

   @Override
   public void getUsers(TCPServerWorker worker) {

      if (UserPrivilege.isAdmin(worker.getPlayer().getPrivilege().ordinal())) {
         try {
            List<UserInfo> users = dbi.getUsers();
            LinkedList<TCPPlayerInfoMessage> msgs = new LinkedList<>();
            for (UserInfo user : users) {
               TCPPlayerInfoMessage msg = new TCPPlayerInfoMessage();
               msg.setUsername(user.getUsername());
               msg.setRole(user.getRole());
               msgs.add(msg);
            }

            String userList = JSONObjectConverter.toJSON(msgs);
            worker.sendUsers(userList);
         } catch (SQLException ex) {
            Logger.getLogger(GENServer.class.getName()).log(Level.SEVERE, null, ex);
         } catch (JsonProcessingException ex) {
            Logger.getLogger(GENServer.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   @Override
   public void getScores(TCPServerWorker worker, String username) {
      try {
         List<Score> scores;
         if (username.length() == 0) {
            scores = dbi.getScores(null);
         } else {
            scores = dbi.getScores(username);
         }

         LinkedList<TCPScoreMessage> msgs = new LinkedList<>();
         for (Score score : scores) {
            TCPScoreMessage m = new TCPScoreMessage();
            m.setId(score.getId());
            m.setRaceName(score.getRaceName());
            m.setPosition(score.getPosition());
            m.setTime(score.getTime());
            m.setDate(score.getDate());
            m.setUsername(score.getUsername());
            msgs.add(m);
         }

         String scoreList = JSONObjectConverter.toJSON(msgs);
         worker.sendScores(scoreList);
      } catch (SQLException ex) {
         Logger.getLogger(GENServer.class.getName()).log(Level.SEVERE, null, ex);
      } catch (JsonProcessingException ex) {
         Logger.getLogger(GENServer.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Override
   public void listRooms(TCPServerWorker worker) {
      LinkedList<TCPRoomMessage> msgs = new LinkedList<>();
      for (ServerRoom room : getServerRooms()) {
         msgs.add(new TCPRoomMessage(room.getName(), room.getID(), room.getNumberOfPlayers()));
      }
      try {
         String roomList = JSONObjectConverter.toJSON(msgs);
         worker.listRooms(roomList);
      } catch (JsonProcessingException ex) {
         Logger.getLogger(GENServer.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Override
   public void joinRoom(TCPServerWorker worker, String roomID) {

      ServerRoom room = getServerRoom(roomID);
      if (room == null) {
         worker.notifyError(TCPProtocol.JOIN_ROOM, TCPProtocol.WRONG_ROOM_ID);
      } else if (room.isBanned(worker.getPlayer().getUsername())) {
         worker.notifyError(TCPProtocol.JOIN_ROOM, TCPProtocol.BANNED_USER);
      } else {
         try {
            room.addPlayer(worker);
            worker.joinRoom(room);
         } catch (Exception e) {
            worker.notifyError(TCPProtocol.JOIN_ROOM, TCPProtocol.FULL_ROOM);
         }
      }
   }

   @Override
   public void quitRoom(TCPServerWorker worker) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void roomInfos(TCPServerWorker worker) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void createRoom(TCPServerWorker worker, String roomName) {
      if (UserPrivilege.isAdmin(worker.getPlayer().getPrivilege().ordinal())) {
         createServerRoom(roomName);
      }
   }

   @Override
   public void deleteRoom(TCPServerWorker worker, String roomID) {
      if (UserPrivilege.isAdmin(worker.getPlayer().getPrivilege().ordinal())) {
         worker.getServer().deleteServerRoom(roomID);
      }
   }

   @Override
   public void userReady(TCPServerWorker worker) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void userFinished(TCPServerWorker worker) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

}
