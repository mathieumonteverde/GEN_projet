/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.DBInterface.exception;

/**
 *
 * @author mathieu
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
