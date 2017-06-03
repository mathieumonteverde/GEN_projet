/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.exception;

/**
 *
 * @author mathieu
 */
public class WrongRoomIDException extends Exception{
   public WrongRoomIDException() {
      super();
   }

   public WrongRoomIDException(String msg) {
      super(msg);
   }

   public WrongRoomIDException(Throwable t) {
      super(t);
   }

   public WrongRoomIDException(String msg, Throwable t) {
      super(msg, t);
   }
}
