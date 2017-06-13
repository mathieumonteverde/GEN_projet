package com.heigvd.gen.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.heigvd.gen.utils.Constants.*;

/**
 * Representation of a Bike.
 * This class handles all the physics of the Bike, including not falling down forever (just disappear).
 * The hitbox of the bike is a small square on the front bottom of the Bike.
 */
public class Bike {

   public static final int HEIGHT = 50;
   public static final int WIDTH = 80;
   private static final int GRAVITY = -15;
   private static final int HB_WIDTH = 2;
   private static final int DECELERATION = -8;
   private static final int MAX_SLOWED_SPEED = 100;
   private boolean ghost;
   private boolean jumping;
   private String name;
   private Vector2 position;
   private Vector2 velocity;
   private LineColor color;
   private Rectangle bounds;

   private Texture bike;
   private static Texture blueBike = new Texture("BikeBlue.png");
   private static Texture greenBike = new Texture("BikeGreen.png");
   private static Texture redBike = new Texture("BikeRed.png");

   /**
    * Creates a new Bike
    *
    * @param x Position on the x axis
    * @param y Position on the y axis
    * @param name Name of the Bike
    * @param ghost Is ghost ? 2spooky4me
    */
   public Bike(float x, float y, String name, boolean ghost) {
      position = new Vector2(x,y);
      velocity = new Vector2(0,0);
      this.ghost = ghost;
      this.name = name;
      color = LineColor.BLUE;
      bike = blueBike;
      bounds = new Rectangle(x+WIDTH-HB_WIDTH, y, HB_WIDTH, HB_WIDTH);
   }

   /**
    * Updates the state of the Bike by applying diverse physics rules to it:
    *
    *
    * @param dt
    */
   public void update(float dt) {

      //Ajoute la gravité
      if(position.y > 0)
         velocity.y += GRAVITY;
      //Ajoute le freinage
      if(velocity.x > 0) {
         velocity.x += DECELERATION;
      } else {
         velocity.x = 0;
      }

      position.add(velocity.x*dt, velocity.y*dt);

      //Empèche la moto de tomber dans le vide (mais disparait tout de même de l'écran)
      if(position.y < -HEIGHT) {
         position.y = -HEIGHT;
         hitGround();
      }
      bounds.setPosition(position.x+WIDTH-5, position.y);
   }

   /**
    * Resets vertical velocity and is not jumping anymore
    */
   public void hitGround() {
      velocity.y = 0;
      jumping = false;
   }

   /**
    * Applies an active break to the bike and blocks it to a min speed
    */
   public void slowDown() {
      if(velocity.x >= MAX_SLOWED_SPEED) {
         velocity.x -= 50;
      }
   }

   /**
    * Speeds up the bike if not jumping
    */
   public void speedUp() {
      if(!jumping) {
         velocity.x += 20;
      }
   }

   /**
    * Get the current position of the Bike
    *
    * @return current position
    */
   public Vector2 getPosition() {
      return position;
   }

   public void setPosition(Vector2 pos) {
      this.position = pos;
   }

   public Texture getTexture() {
      return bike;
   }

   public String getName() {
      return name;
   }

   public Vector2 getVelocity() {
      return velocity;
   }

   public void addVelocity(float x, float y) {
      velocity.add(x,y);
   }

   public void jump() {
      if(!jumping) {
         jumping = true;
         position.add(0,5);
         velocity.y = 400;
      }
   }

   public Rectangle getBounds() {
      return bounds;
   }

   public void dispose() {
      bike.dispose();
      redBike.dispose();
      greenBike.dispose();
      blueBike.dispose();
   }

   public LineColor getColor() {
      return color;
   }

   public boolean isGhost() {
      return ghost;
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
