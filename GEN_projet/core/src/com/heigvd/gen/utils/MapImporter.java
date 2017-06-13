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
         File[] list = folder.listFiles();

         int i = 0;

         for(File file : list) {

            Road r = new Road(file.getName());

            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null) {

               char color = line.charAt(0);

               switch (color) {
                  case 'R' : r.addLine(Constants.LineColor.RED, (int) line.charAt(2), line.charAt(4));
                  break;
                  case 'G' : r.addLine(Constants.LineColor.GREEN, (int) line.charAt(2), line.charAt(4));
                     break;
                  case 'B' : r.addLine(Constants.LineColor.BLUE, (int) line.charAt(2), line.charAt(4));
                     break;
                  case 'E' : r.addWhiteLine();
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
