package com.heigvd.gen.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.heigvd.gen.utils.Constants;
import com.heigvd.gen.utils.Constants.*;

public class Bike {

   private static final int GRAVITY = -15;
   public static final int HEIGHT = 50;
   public static final int WIDTH = 80;
   private int speed;
   private Vector2 position;
   private Vector2 velocity;
   private LineColor color;
   private Rectangle bounds;
   private boolean ghost;

   private Texture bike;
   private static Texture blueBike = new Texture("BikeBlue.png");
   private static Texture greenBike = new Texture("BikeGreen.png");
   private static Texture redBike = new Texture("BikeRed.png");

   public Bike(int x, int y, boolean ghost) {
      speed = 0;
      position = new Vector2(x,y);
      velocity = new Vector2(0,0);
      this.ghost = ghost;
      color = LineColor.BLUE;
      bike = blueBike;
      bounds = new Rectangle(x+WIDTH-5, y, 5, HEIGHT);
   }

   /**
    * S'occupe juste de faire tomber la moto et quelle ne sorte pas de l'écran
    * @param dt
    */
   public void update(float dt) {

      if(position.y > 0)
         velocity.add(0, GRAVITY);
      velocity.scl(dt);
      position.add(speed*dt+velocity.x, velocity.y);
      if(position.y < 0) {
         position.y = 0;
      }
      velocity.scl(1/dt);
      bounds.setPosition(position.x+WIDTH-5, position.y);
   }

   public Vector2 getPosition() {
      return position;
   }

   public void setPosition(Vector2 pos) {
      this.position = pos;
   }

   public Texture getTexture() {
      return bike;
   }

   public Vector2 getVelocity() {
      return velocity;
   }

   public void setVelocity(Vector2 velocity) {
      this.velocity = velocity;
   }

   public void jump() {
      velocity.y = 250;
   }

   public Rectangle getBounds() {
      return bounds;
   }

   public void dispose() {
      bike.dispose();
   }

   public int getSpeed() {
      return speed;
   }

   public void setSpeed(int speed) {
      this.speed = speed;
   }

   public LineColor getColor() {
      return color;
   }

   public void switchColor(LineColor color) {
      this.color = color;
      switch (color) {
         case BLUE:
            bike = blueBike;
            break;
         case GREEN:
            bike = greenBike;
            break;
         case RED:
            bike = redBike;
            break;
      }
   }
}
