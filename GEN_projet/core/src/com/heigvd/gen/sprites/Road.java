package com.heigvd.gen.sprites;

import com.badlogic.gdx.math.Vector2;
import com.heigvd.gen.utils.Constants;
import java.util.ArrayList;

public class Road {

   public static final int START_END_LINE_LENGTH = 5;
   public static final int START_END_LINE_HEIGHT = 50;
   private ArrayList<RoadLine> roadColors;
   private int currentLength;
   private String name;

   public Road(String name) {
      this.name = name;
      roadColors = new ArrayList<RoadLine>();
      currentLength = 0;
      //Add starting line
      addStartLine();
   }

   /**
    * This method adds a new RoadLine at the end of the current Road
    * The length value is the the number of times the width of
    * the road line png should be repeated.
    *
    * @param color  The color of the RoadLine
    * @param length A subjective length value
    * @param height The height position on the screen
    */
   public void addLine(Constants.LineColor color, int length, int height) {
      System.out.println("New RoadLine length : "+length+" | height : "+height);
      RoadLine rl = new RoadLine(color, new Vector2(currentLength, height), length);
      roadColors.add(rl);
      currentLength += rl.getLength();
   }

   public void addStartLine() {
      currentLength += addWhiteLine();
   }

   public void addEndLine() {
      addWhiteLine();
   }

   public int addWhiteLine() {
      RoadLine rl = new RoadLine(Constants.LineColor.WHITE, new Vector2( currentLength, START_END_LINE_HEIGHT ), START_END_LINE_LENGTH);
      roadColors.add(rl);
      return rl.getLength();
   }

   public int getCurrentLength() {
      return currentLength;
   }

   public ArrayList<RoadLine> getRoadColors() {
      return roadColors;
   }

   public String getName() {
      return name;
   }

   public void dispose() {
      for (RoadLine line : roadColors) {
         line.dispose();
      }
   }
}
