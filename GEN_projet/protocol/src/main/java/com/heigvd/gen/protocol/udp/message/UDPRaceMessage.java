package com.heigvd.gen.protocol.udp.message;

import java.util.LinkedList;
import java.util.List;

/**
 * UDP message object to represent the state of a race when sending it by UDP.
 */
public class UDPRaceMessage extends UDPMessage {
   // The name of the race
   private String raceName;
   
   // The time since the start of the race (in hundredth of seconds)
   private long time;
   
   // List of player messages
   private List<UDPPlayerMessage> players;
   
   /**
    * Constructor.
    */
   public UDPRaceMessage() {
      players = new LinkedList<>();
      time = 0;
      raceName = "";
      setType(UDPMessage.TYPE.RACE_MESSAGE.toString());
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
