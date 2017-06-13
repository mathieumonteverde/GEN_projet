package com.heigvd.gen.protocol.udp.message;

/**
 *
 * @author mathieu
 */
public class UDPPlayerMessage extends UDPMessage {
   
   /**
    * Game data
    */
   // Position
   private float posX, posY;
   
   // Velocity
   private float velX, velY;
   
   // Bike color
   private int color;
   
   /**
    * Player data
    */
   private String username;
   
   public UDPPlayerMessage() {
      setType(UDPMessage.TYPE.PLAYER_MESSAGE.toString());
   }

   /**
    * @return the posX
    */
   public float getPosX() {
      return posX;
   }

   /**
    * @param posX the posX to set
    */
   public void setPosX(float posX) {
      this.posX = posX;
   }

   /**
    * @return the posY
    */
   public float getPosY() {
      return posY;
   }

   /**
    * @param posY the posY to set
    */
   public void setPosY(float posY) {
      this.posY = posY;
   }

   /**
    * @return the vX
    */
   public float getVelX() {
      return velX;
   }

   /**
    * @param vX the vX to set
    */
   public void setVelX(float vX) {
      this.velX = vX;
   }

   /**
    * @return the vY
    */
   public float getVelY() {
      return velY;
   }

   /**
    * @param vY the vY to set
    */
   public void setVelY(float vY) {
      this.velY = vY;
   }

   /**
    * @return the color
    */
   public int getColor() {
      return color;
   }

   /**
    * @param color the color to set
    */
   public void setColor(int color) {
      this.color = color;
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
