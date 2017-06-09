package com.heigvd.gen.utils;

import com.heigvd.gen.sprites.Road;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class MapImporter {

   public static ArrayList<Road> importRoads() {

      ArrayList<Road> roads = new ArrayList<Road>();

      try {
         File folder = new File("roads");
         folder.mkdir();

         for(File file : folder.listFiles()) {

            Road r = new Road(file.getName());

            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null) {

               char color = line.charAt(0);

               switch (color) {
                  case 'R' : r.addLine(Constants.LineColor.RED, Integer.valueOf(line.substring(2)));
                  break;
                  case 'G' : r.addLine(Constants.LineColor.GREEN, Integer.valueOf(line.substring(2)));
                     break;
                  case 'B' : r.addLine(Constants.LineColor.BLUE, Integer.valueOf(line.substring(2)));
                     break;
                  case 'E' : r.addEnd();
                     break;
               }

            }
            roads.add(r);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }



      return roads;
   }


}
