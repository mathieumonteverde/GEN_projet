package com.heigvd.gen.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.heigvd.gen.utils.Constants;
import com.heigvd.gen.utils.Constants.*;

import java.util.Random;

public class RoadLine {

   private LineColor color;
   private int length;
   private static int LINE_WIDTH = 50;
   private static int LINE_HEIGHT = 10;

   private Texture line;
   private Vector2 position;
   private Rectangle bounds;

   public RoadLine(LineColor color, int pos, int length, int height) {
      this.color = color;
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

      position = new Vector2(pos, height);
      bounds = new Rectangle(position.x, position.y, length,LINE_HEIGHT);
   }
   public int getLength() {
      return length;
   }

   public Texture getLine() {
      return line;
   }

   public Vector2 getPosition() {
      return position;
   }

   public LineColor getColor() {
      return color;
   }

   public boolean collides(Rectangle player) {
      return player.overlaps(bounds);
   }

   public void dispose() {
      line.dispose();
   }


}
