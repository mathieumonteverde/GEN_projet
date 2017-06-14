package com.heigvd.gen.utils;

import com.heigvd.gen.sprites.Road;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class MapImporter {

   public static ArrayList<Road> importRoads() {

      System.out.println("\n#### Map importation...");

      ArrayList<Road> roads = new ArrayList<Road>();

      try {
         File folder = new File("roads");
         File[] list = folder.listFiles();
         
         Arrays.sort(list);

         for(File file : list) {

            System.out.println("Importing "+file.getName());

            Road r = new Road(file.getName());
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null) {

               String[] data = line.split(",");
               char color = data[0].charAt(0);

               switch (color) {
                  case 'R' : r.addLine(Constants.LineColor.RED, Integer.valueOf(data[1]), Integer.valueOf(data[2]));
                  break;
                  case 'G' : r.addLine(Constants.LineColor.GREEN, Integer.valueOf(data[1]), Integer.valueOf(data[2]));
                     break;
                  case 'B' : r.addLine(Constants.LineColor.BLUE, Integer.valueOf(data[1]), Integer.valueOf(data[2]));
                     break;
                  case 'E' : r.addEndLine();
                     break;
               }

            }
            roads.add(r);
         }
      } catch (Exception e) {
         System.err.println("Failed to import maps");
         e.printStackTrace();
      }

      System.out.println("#### Import Successful !\n");

      return roads;
   }
}
