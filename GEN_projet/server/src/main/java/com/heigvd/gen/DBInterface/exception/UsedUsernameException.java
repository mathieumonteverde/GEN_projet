package com.heigvd.gen.DBInterface.exception;

/**
 * Custom exception to represent a used username error when trying to 
 * register a user.
 */
public class UsedUsernameException extends Exception {
   public UsedUsernameException() {
      super();
   }

   public UsedUsernameException(String msg) {
      super(msg);
   }

   public UsedUsernameException(Throwable t) {
      super(t);
   }

   public UsedUsernameException(String msg, Throwable t) {
      super(msg, t);
   }
}
