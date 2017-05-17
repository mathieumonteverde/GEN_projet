/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server;

/**
 *
 * @author mathieu
 */
public class Player {
   private String username;
   private State state;
   
   public static enum State {WAITING, READY};
   
   public Player(String username) {
      this.username = username;
      this.state = State.WAITING;
   }
   
   public String getUsername() {
      return username;
   }
   
   public State getState() {
      return state;
   }
   
   public void setState(State state) {
      this.state = state;
   }
}
