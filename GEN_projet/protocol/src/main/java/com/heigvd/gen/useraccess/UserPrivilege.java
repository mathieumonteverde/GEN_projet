package com.heigvd.gen.useraccess;

/**
 *
 * @author mathieu
 */
public class UserPrivilege {
   public enum Privilege {DEFAULT, ADMIN, SUPER_ADMIN};
   
   public static boolean isAdmin(int role) {
      return role >= Privilege.ADMIN.ordinal();
   }
   
    public static boolean isSuperAdmin(int role) {
      return role >= Privilege.SUPER_ADMIN.ordinal();
   }
}
