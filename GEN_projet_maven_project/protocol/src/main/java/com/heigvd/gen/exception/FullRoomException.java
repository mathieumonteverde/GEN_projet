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
public class FullRoomException extends Exception {

   public FullRoomException() {
      super();
   }

   public FullRoomException(String msg) {
      super(msg);
   }

   public FullRoomException(Throwable t) {
      super(t);
   }

   public FullRoomException(String msg, Throwable t) {
      super(msg, t);
   }
}
