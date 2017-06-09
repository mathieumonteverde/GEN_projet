package com.heigvd.gen.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.heigvd.gen.utils.Constants;
import com.heigvd.gen.utils.Constants.*;

public class RoadLine {

   private LineColor color;
   private int length;

   private Texture line;
   private Vector2 position;
   private Rectangle bounds;
   private boolean isEnd;

   public RoadLine(LineColor color, int pos, int length, boolean isEnd) {
      this.color = color;
      this.length = length;
      this.isEnd = isEnd;

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

      position = new Vector2(pos, Constants.ROAD_HEIGHT);
      bounds = new Rectangle(position.x, position.y, line.getWidth()*length,line.getHeight());
   }
   public int getLength() {
      return length;
   }

   public int getWidth() {
      return line.getWidth()*length;
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

   public boolean isEnd() {
      return isEnd;
   }

   public void dispose() {
      line.dispose();
   }


}
