/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server;

/**
 *
 * @author mathieu
 */
public class Score {
   private int id;
   private String raceName;
   private int position;
   private int time;
   private String date;
   private String username;
   
   public Score(int id, String raceName, int position, int time, String date, String username) {
      this.id = id;
      this.raceName = raceName;
      this.position = position;
      this.time = time;
      this.date = date;
      this.username = username;
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
   
   
   public String getUsername() {
      return username;
   }
   
   public void setUsername(String username) {
      this.username = username;
   }
   
}
