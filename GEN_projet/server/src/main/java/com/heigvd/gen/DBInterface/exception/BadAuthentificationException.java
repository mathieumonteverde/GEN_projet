package com.heigvd.gen.DBInterface.exception;

/**
 * Custom exception to represent bad authentication from a user.
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
