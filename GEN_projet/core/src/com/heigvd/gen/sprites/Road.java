package com.heigvd.gen.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.heigvd.gen.utils.Constants;
import javafx.util.Pair;
import java.util.ArrayList;

public class Road {

   private ArrayList<RoadLine> roadColors;
   int currentLength;

   public Road() {
      roadColors = new ArrayList<RoadLine>();
      currentLength = 0;
   }

   public void addLine(Constants.LineColor color, int length) {
      RoadLine rl = new RoadLine(color, currentLength, length);
      roadColors.add(rl);
      currentLength += rl.getWidth();
   }

   public ArrayList<RoadLine> getRoadColors() {
      return roadColors;
   }

   public void dispose() {
      for (RoadLine line : roadColors) {
         line.dispose();
      }
   }
}
