/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server;

import java.util.UUID;

/**
 *
 * @author mathieu
 */
public class ServerRoom {
   private final String name;
   private final String ID;
   
   public ServerRoom(String name) {
      this.name = name;
      ID = UUID.randomUUID().toString();
   }
   
   public String getName() {
      return name;
   }
   public String getID() {
      return ID;
   }
}
