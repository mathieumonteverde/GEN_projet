package com.heigvd.gen.guicomponent;

import com.heigvd.gen.protocol.tcp.message.TCPRoomMessage;

/**
 *
 * @author mathieu
 */
public class RoomListCell {

   private String name;
   private String ID;
   private int nbPlayers;

   /**
    * Construct the object
    *
    * @param msg the msg to represent
    */
   public RoomListCell(TCPRoomMessage msg) {
      this.name = msg.getName();
      this.ID = msg.getID();
      this.nbPlayers = msg.getNbPlayers();
   }

   /**
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * @return the ID
    */
   public String getID() {
      return ID;
   }

   /**
    * @param ID the ID to set
    */
   public void setID(String ID) {
      this.ID = ID;
   }

   /**
    * @return the nbPlayers
    */
   public int getNbPlayers() {
      return nbPlayers;
   }

   /**
    * @param nbPlayers the nbPlayers to set
    */
   public void setNbPlayers(int nbPlayers) {
      this.nbPlayers = nbPlayers;
   }
}
