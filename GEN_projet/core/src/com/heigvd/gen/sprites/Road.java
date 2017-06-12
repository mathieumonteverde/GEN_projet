package com.heigvd.gen.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.heigvd.gen.utils.Constants;
import javafx.util.Pair;
import java.util.ArrayList;

public class Road {

   private final int START_END_LINE_LENGTH = 5;
   private ArrayList<RoadLine> roadColors;
   private int currentLength;
   private int end;
   private String name;

   public Road(String name) {
      this.name = name;
      roadColors = new ArrayList<RoadLine>();

      //Add starting line
      RoadLine rl = new RoadLine(Constants.LineColor.WHITE, 0, START_END_LINE_LENGTH, false);
      roadColors.add(rl);
      currentLength = rl.getWidth();
      end = 0;
   }

   public void addLine(Constants.LineColor color, int length) {
      RoadLine rl = new RoadLine(color, currentLength, length, false);
      roadColors.add(rl);
      currentLength += rl.getWidth();
   }

   public void addEnd() {
      RoadLine rl = new RoadLine(Constants.LineColor.WHITE, currentLength, START_END_LINE_LENGTH, true);
      roadColors.add(rl);
      currentLength += rl.getWidth();
      end = currentLength;
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
