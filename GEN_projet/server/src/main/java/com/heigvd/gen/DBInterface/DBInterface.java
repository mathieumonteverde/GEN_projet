package com.heigvd.gen.DBInterface;

import com.heigvd.gen.useraccess.UserPrivilege;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents an interface to interact with the server MySQL 
 * Database.
 * 
 * In order to properly run this interface, you should have 
 * 1) a MySQL DB named gen located on loacalhost
 * 2) this DB should have a user access {username: root, password: root}
 * 
 * @author mathieu
 */
public class DBInterface {

   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/gen?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "root";

   private Connection conn;

   /**
    * Constructor. Creates register the JDBC_DRIVER
    */
   public DBInterface() {
      try {
         // Register JDBC driver
         Class.forName(DBInterface.JDBC_DRIVER);

      } catch (ClassNotFoundException ex) {
         Logger.getLogger(DBInterface.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
   
   /**
    * Connect to the Database using JDBC. 
    * @throws SQLException if an error occurs 
    */
   private void connect() throws SQLException {
      // Open the connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
   }
   
   /**
    * Disconnect from the Database using JDBC.
    */
   public void disconnect() {
      System.out.println("Disconnect from database");
      try {
         if (conn != null) {
            conn.close();
            conn = null;
         }
      } catch (SQLException se) {
         se.printStackTrace();
      }//end finally try
   }
   
   /**
    * Register a user inside the database
    * @param username the username, must be unique
    * @param password the password of the user
    * @throws SQLException if an SQL error occurs, more specifically if the username
    * already exists
    */
   public void registerUser(String username, String password) throws SQLException {
      PreparedStatement userReg = null;
      try {
         // Connect to DB
         connect();
         conn.setAutoCommit(false);

         // Prepare userReg statement
         String userRegString = "CALL insertUser(?, ?, ?)";
         userReg = conn.prepareStatement(userRegString);

         userReg.setString(1, username);
         userReg.setString(2, password);
         userReg.setInt(3, UserPrivilege.Privilege.DEFAULT.ordinal());
         userReg.executeUpdate();
         conn.commit();
      } catch (SQLException e) {
         if (conn != null) {
            Logger.getLogger(DBInterface.class.getName()).log(Level.SEVERE, null, e);
            try {
               System.err.print("Transaction is being rolled back");
               conn.rollback();
            } catch (SQLException ex) {
               Logger.getLogger(DBInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
         throw e;
      } finally {
         if (userReg != null) {
            try {
               userReg.close();
            } catch (SQLException e) {}
         }
         disconnect();
      }
   }
   
   /**
    * Connect a user. This method returns true if the specific username - password
    * association given as parameters exists in the Database.
    * @param username the user username
    * @param password the user password
    * @return true if the user exists
    * @throws SQLException if a SQL error occurs during the verification
    */
   public boolean connectUser(String username, String password) throws SQLException {
      PreparedStatement userAuth = null;
      try {
         // Connect to DB
         connect();

         // Prepare userAuth statement
         String userAuthString = "CALL userAuthentification(?, ?)";
         userAuth = conn.prepareStatement(userAuthString);

         userAuth.setString(1, username);
         userAuth.setString(2, password);
         ResultSet userResult = userAuth.executeQuery();
         
         // Count number of users returned by the SQL query
         int count = 0;
         while (userResult.next()) {
            ++count;
         }
         
         // If we find exactly on user correspondance, we return true
         if (count == 1) {
            return true;
         }

      } catch (SQLException e) {
         if (conn != null) {
            Logger.getLogger(DBInterface.class.getName()).log(Level.SEVERE, null, e);
            try {
               System.err.print("Transaction is being rolled back");
               conn.rollback();
            } catch (SQLException ex) {
               Logger.getLogger(DBInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      } finally {
         if (userAuth != null) {
            try {
               userAuth.close();
            } catch (SQLException e) {}
         }
         disconnect();
      }

      return false;
   }

}
