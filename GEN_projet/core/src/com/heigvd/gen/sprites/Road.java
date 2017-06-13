package com.heigvd.gen.sprites;

import com.heigvd.gen.utils.Constants;
import java.util.ArrayList;

public class Road {

   public static final int START_END_LINE_LENGTH = 5;
   public static final int ROAD_HEIGHT = 50;
   private ArrayList<RoadLine> roadColors;
   private int currentLength;
   private String name;

   public Road(String name) {
      this.name = name;
      roadColors = new ArrayList<RoadLine>();
      currentLength = 0;
      //Add starting line
      addWhiteLine();
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
      RoadLine rl = new RoadLine(color, currentLength, length, height);
      roadColors.add(rl);
      currentLength += rl.getLength();
   }

   public void addWhiteLine() {
      addLine(Constants.LineColor.WHITE, START_END_LINE_LENGTH, ROAD_HEIGHT);
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
