package com.heigvd.gen.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import javafx.util.Pair;
import java.util.ArrayList;

public class Road {

   private ArrayList<RoadLine> roadColors;


   public Road(ArrayList<RoadLine> roadColors) {
      this.roadColors = roadColors;
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
