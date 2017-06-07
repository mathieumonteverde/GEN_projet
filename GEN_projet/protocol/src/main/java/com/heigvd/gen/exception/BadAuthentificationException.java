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
public class BadAuthentificationException extends Exception {
   public BadAuthentificationException() {
      super();
   }

   public BadAuthentificationException(String msg) {
      super(msg);
   }

   public BadAuthentificationException(Throwable t) {
      super(t);
   }

   public BadAuthentificationException(String msg, Throwable t) {
      super(msg, t);
   }
}
