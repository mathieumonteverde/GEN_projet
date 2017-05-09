/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author mathieu
 */
public class ServerRoom extends Observable {
   private static int count = 0;
   
   private final String name;
   private final String ID;
   private final int maxPlayers = 4;
   
   private LinkedList<Player> players;
   
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
   
   public void addPlayer(Player p) throws Exception {
      if (players.size() == maxPlayers) {
         throw new Exception("Error: The room is already full.");
      }
      players.add(p);
      setChanged();
      notifyObservers();
   }
   
   public List<Player> getPlayers() {
      return players;
   }
}
