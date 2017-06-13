package com.heigvd.gen.DBInterface;

import com.heigvd.gen.DBInterface.exception.BadAuthentificationException;
import com.heigvd.gen.DBInterface.exception.UsedUsernameException;
import com.heigvd.gen.server.Score;
import com.heigvd.gen.useraccess.UserPrivilege;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents an interface to interact with the server MySQL
 * Database.
 *
 * In order to properly run this interface, you should have 1) a MySQL DB named
 * gen located on loacalhost 2) this DB should have a user access {username:
 * root, password: root}
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
    *
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
    *
    * @param username the username, must be unique
    * @param password the password of the user
    * @throws SQLException if an SQL error occurs, more specifically if the
    * username already exists
    * @throws com.heigvd.gen.DBInterface.exception.UsedUsernameException
    */
   public void registerUser(String username, String password) throws SQLException, UsedUsernameException {
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
      } catch (SQLIntegrityConstraintViolationException e) {
         if (conn != null) {
            try {
               System.err.print("Transaction is being rolled back");
               conn.rollback();
            } catch (SQLException ex) {
               Logger.getLogger(DBInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
         throw new UsedUsernameException(e);
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
            } catch (SQLException e) {
            }
         }
         disconnect();
      }
   }

   /**
    * Connect a user. This method returns true if the specific username -
    * password association given as parameters exists in the Database.
    *
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
            } catch (SQLException e) {
            }
         }
         disconnect();
      }

      return false;
   }

   /**
    * Adds a score in the Database.
    *
    * @param raceName the name of the race
    * @param position the position of the player, starting at 1
    * @param time the amount time the player used to finish the race (unity:
    * hundredth of a second)
    * @param date the Date the scored occurred format: "YYYY-MM-DD".
    * @param username the username of the player, must exist
    * @throws SQLException if an error occurs during the transaction
    */
   public void addScore(String raceName, int position, int time, String date, String username) throws SQLException {

      PreparedStatement addScore = null;
      try {
         // Connect to DB
         connect();
         conn.setAutoCommit(false);

         // Prepare userAuth statement
         String addScoreString = "CALL insertScore(?, ?, ?, ?, ?)";
         addScore = conn.prepareStatement(addScoreString);

         addScore.setString(1, raceName);
         addScore.setInt(2, position);
         addScore.setInt(3, time);
         addScore.setString(4, date);
         addScore.setString(5, username);

         addScore.executeUpdate();
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
         if (addScore != null) {
            try {
               addScore.close();
            } catch (SQLException e) {
            }
         }
         disconnect();
      }
   }

   /**
    * Changes a user password. The method takes responsibility to verify the
    * current user identity by calling connectUser(username, oldPswd) before
    * trying to update its password.
    *
    * @param username the user username
    * @param oldPswd the old password
    * @param newPswd the new password
    * @throws SQLException if a SQL exception occurs
    * @throws com.heigvd.gen.DBInterface.exception.BadAuthentificationException if the
    * username isn't correctly authenticated
    */
   public void changeUserPassword(String username, String oldPswd, String newPswd) throws SQLException, BadAuthentificationException {
      try {
         boolean b1 = connectUser("Valomat", "1234");
         if (!b1) {
            throw new BadAuthentificationException();
         }

         PreparedStatement userPswUpd = null;
         try {
            // Connect to DB
            connect();
            conn.setAutoCommit(false);

            // Prepare userReg statement
            String userRegString = "CALL updateUserPassword(?, ?)";
            userPswUpd = conn.prepareStatement(userRegString);

            userPswUpd.setString(1, username);
            userPswUpd.setString(2, newPswd);
            userPswUpd.executeUpdate();
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
            if (userPswUpd != null) {
               try {
                  userPswUpd.close();
               } catch (SQLException e) {
               }
            }
            disconnect();
         }

      } catch (SQLException ex) {
         throw ex;
      }
   }

   /**
    * Changes a user role
    *
    * @param username of the user to change
    * @param role the new role
    * @throws java.sql.SQLException
    */
   public void changeUserRole(String username, UserPrivilege.Privilege role) throws SQLException {
      PreparedStatement usrRoleUpd = null;
      try {
         // Connect to DB
         connect();
         conn.setAutoCommit(false);

         // Prepare userReg statement
         String userRegString = "CALL updateUserRole(?, ?)";
         usrRoleUpd = conn.prepareStatement(userRegString);

         usrRoleUpd.setString(1, username);
         usrRoleUpd.setInt(2, role.ordinal());
         usrRoleUpd.executeUpdate();
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
         if (usrRoleUpd != null) {
            try {
               usrRoleUpd.close();
            } catch (SQLException e) {
            }
         }
         disconnect();
      }
   }

   public List<Score> getScores(String user) throws SQLException {
      PreparedStatement scoreRequest = null;

      try {
         // Result set
         ResultSet rs;

         // Connect to DB
         connect();
         conn.setAutoCommit(false);

         if (user == null) {
            // Prepare userReg statement
            String scoreRequestString = "CALL getScores()";
            scoreRequest = conn.prepareStatement(scoreRequestString);
            rs = scoreRequest.executeQuery();
         } else {
            String scoreRequestString = "CALL getScoresByUser(?)";
            scoreRequest = conn.prepareStatement(scoreRequestString);
            scoreRequest.setString(1, user);
            rs = scoreRequest.executeQuery();
         }

         List<Score> scores = new LinkedList<Score>();

         while (rs.next()) {
            int id = rs.getInt("id");
            String raceName = rs.getString("raceName");
            int position = rs.getInt("position");
            int time = rs.getInt("time");
            String date = rs.getDate("date").toString();
            String username = rs.getString("username");

            scores.add(new Score(id, raceName, position, time, date, username));

         }

         return scores;
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
         if (scoreRequest != null) {
            try {
               scoreRequest.close();
            } catch (SQLException e) {
            }
         }
         disconnect();
      }
   }

   public UserInfo getUserInfo(String username) throws SQLException {
      PreparedStatement infoRequest = null;

      try {
         // Result set
         ResultSet rs;

         // Connect to DB
         connect();
         conn.setAutoCommit(false);
         String infoRequestString = "CALL getUserInfo(?)";
         infoRequest = conn.prepareStatement(infoRequestString);
         infoRequest.setString(1, username);
         rs = infoRequest.executeQuery();
         
         if (rs.next()) {
            return new UserInfo(username, rs.getInt("role"));
         }
         
         return null;
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
         if (infoRequest != null) {
            try {
               infoRequest.close();
            } catch (SQLException e) {
            }
         }
         disconnect();
      }
   }
   
   public List<UserInfo> getUsers() throws SQLException {
      PreparedStatement infoRequest = null;

      try {
         // Result set
         ResultSet rs;

         // Connect to DB
         connect();
         conn.setAutoCommit(false);
         String infoRequestString = "SELECT User.username, User.role FROM User";
         infoRequest = conn.prepareStatement(infoRequestString);
         rs = infoRequest.executeQuery();
         
         List<UserInfo> userInfos = new LinkedList<>();
         
         while (rs.next()) {
            userInfos.add(new UserInfo(rs.getString("username"), rs.getInt("role")));
         }
         
         return userInfos  ;
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
         if (infoRequest != null) {
            try {
               infoRequest.close();
            } catch (SQLException e) {
            }
         }
         disconnect();
      }
   }

}
