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
public class WrongCommandException extends Exception{
   public WrongCommandException() {
      super();
   }

   public WrongCommandException(String msg) {
      super(msg);
   }

   public WrongCommandException(Throwable t) {
      super(t);
   }

   public WrongCommandException(String msg, Throwable t) {
      super(msg, t);
   }
}
