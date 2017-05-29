package com.heigvd.gen.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
   private Texture blueBike;
   private Texture greenBike;
   private Texture redBike;

   public Bike(int x, int y, boolean ghost) {
      speed = 1;
      position = new Vector2(x,y);
      velocity = new Vector2(0,0);
      this.ghost = ghost;
      blueBike = new Texture("BikeBlue.png");
      greenBike = new Texture("BikeGreen.png");
      redBike = new Texture("BikeRed.png");
      bike = blueBike;
      bounds = new Rectangle(x,y,WIDTH, HEIGHT);
   }

   public void update(float dt) {
      if(position.y > 0) {
         velocity.add(0, GRAVITY);
      }
      velocity.scl(dt);
      position.add(speed*dt + velocity.x, velocity.y);
      if(position.y < 0) {
         position.y = 0;
      }
      velocity.scl(1/dt);
      bounds.setPosition(position.x, position.y);
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
      if(velocity.y == 0) {
         velocity.y = 250;
      }
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
