package com.heigvd.gen.protocol.udp.message;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class UDPRaceMessage extends UDPMessage {
   private String raceName;
   
   private long time;
   
   private List<UDPPlayerMessage> players;
   
   public UDPRaceMessage() {
      players = new LinkedList<UDPPlayerMessage>();
      long time = 0;
      raceName = "";
   }

   /**
    * @return the raceName
    */
   public String getRaceName() {
      return raceName;
   }

   /**
    * @param raceName the raceName to set
    */
   public void setRaceName(String raceName) {
      this.raceName = raceName;
   }

   /**
    * @return the time
    */
   public long getTime() {
      return time;
   }

   /**
    * @param time the time to set
    */
   public void setTime(long time) {
      this.time = time;
   }

   /**
    * @return the players
    */
   public List<UDPPlayerMessage> getPlayers() {
      return players;
   }

   /**
    * @param players the players to set
    */
   public void setPlayers(List<UDPPlayerMessage> players) {
      this.players = players;
   }
}
