package com.heigvd.gen.protocol.tcp.message;

/**
 *
 * @author mathieu
 */
public class TCPScoreMessage {
   private int id;
   private String raceName;
   private int position;
   private int time;
   private String date;
   private String username;
   
   public TCPScoreMessage() {
      id = 0;
      raceName = "";
      position = 0;
      time = 0;
      date = "";
      username = "";
   }

   /**
    * @return the id
    */
   public int getId() {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId(int id) {
      this.id = id;
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
    * @return the position
    */
   public int getPosition() {
      return position;
   }

   /**
    * @param position the position to set
    */
   public void setPosition(int position) {
      this.position = position;
   }

   /**
    * @return the time
    */
   public int getTime() {
      return time;
   }

   /**
    * @param time the time to set
    */
   public void setTime(int time) {
      this.time = time;
   }

   /**
    * @return the date
    */
   public String getDate() {
      return date;
   }

   /**
    * @param date the date to set
    */
   public void setDate(String date) {
      this.date = date;
   }

   /**
    * @return the username
    */
   public String getUsername() {
      return username;
   }

   /**
    * @param username the username to set
    */
   public void setUsername(String username) {
      this.username = username;
   }
}
