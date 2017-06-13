package com.heigvd.gen.useraccess;

/**
 * Class to represent the User privilege. A user can have DEFAULT, ADMIN or SUPER_ADMIN
 * rights.
 */
public class UserPrivilege {
   public enum Privilege {DEFAULT, ADMIN, SUPER_ADMIN};
   
   /**
    * Method to determine if a role index is ADMIN or higher
    * @param role the index of the role
    * @return true if the index corresponds to an ADMIN or right rights
    */
   public static boolean isAdmin(int role) {
      return role >= Privilege.ADMIN.ordinal();
   }
      
   /**
    * Method to determine if a role index is SUPER_ADMIN or higher
    * @param role the index of the role
    * @return true if the index corresponds to a SUPER_ADMIN or higher
    */
    public static boolean isSuperAdmin(int role) {
      return role >= Privilege.SUPER_ADMIN.ordinal();
   }
}
