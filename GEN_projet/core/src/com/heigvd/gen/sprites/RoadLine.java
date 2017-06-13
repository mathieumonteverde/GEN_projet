package com.heigvd.gen.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.heigvd.gen.utils.Constants.*;

/**
 * Representation of a colored line.
 * It is conscious of its own position and manages his collisions.
 */
public class RoadLine {

   private LineColor color;
   private int length;
   public static final int LINE_WIDTH = 50;
   public static final int LINE_HEIGHT = 5;

   private Texture line;
   private Vector2 position;
   private Rectangle bounds;

   /**
    * Constructor of a new Road Line
    * The length given in parameter is symbolic and is not the final size in pixels
    *
    * @param color    The color of the line
    * @param position Its position (bottom left corner)
    * @param length   Its length
    */
   public RoadLine(LineColor color, Vector2 position, int length) {

      this.color = color;
      this.position = position;
      this.length = length * LINE_WIDTH;

      switch (color) {
         case GREEN:
            line = new Texture("lineGreen.png");
            break;
         case RED:
            line = new Texture("lineRed.png");
            break;
         case BLUE:
            line = new Texture("lineBlue.png");
            break;
         default:
            line = new Texture("lineWhite.png");
      }

      bounds = new Rectangle(position.x, position.y, this.length, LINE_HEIGHT);
   }

   /**
    * Returns the real length of the line
    *
    * @return a length
    */
   public int getLength() {
      return length;
   }

   /**
    * Returns the Texture of the line
    *
    * @return a Texture
    */
   public Texture getLine() {
      return line;
   }

   /**
    * Returns the current position of the line
    * (bottom left corner of the line)
    *
    * @return a position
    */
   public Vector2 getPosition() {
      return position;
   }

   /**
    * Returns the current color of the line
    *
    * @return a color
    */
   public LineColor getColor() {
      return color;
   }

   /**
    * Checks if the given hitbox in parameter is overlapping the bounds
    * of the current line.
    *
    * @param hitbox Rectangle to check
    * @return true if overlap exists, else false
    */
   public boolean collides(Rectangle hitbox) {
      return hitbox.overlaps(bounds);
   }

   /**
    * Disposes the different ressources used
    */
   public void dispose() {
      line.dispose();
   }
}
