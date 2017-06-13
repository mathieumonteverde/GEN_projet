/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server.TCPInterface;

import com.heigvd.gen.DBInterface.DBInterface;
import com.heigvd.gen.DBInterface.UserInfo;
import com.heigvd.gen.DBInterface.exception.UsedUsernameException;
import com.heigvd.gen.protocol.tcp.TCPProtocol;
import com.heigvd.gen.server.Player;
import com.heigvd.gen.useraccess.UserPrivilege;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class WorkerConnectState extends WorkerState {

   public WorkerConnectState(TCPServerWorker worker, BufferedReader in, PrintWriter out) {
      super(worker, in, out);
   }

   @Override
   public void manageClient(String line) throws IOException {

      try {
         if (line == null) {
            throw new IOException("Disconnected");
         }

         if (line.equals(TCPProtocol.CONNECT_USER)) {
            DBInterface dbi = worker.getServer().getDatabaseInterface();

            String username = in.readLine();
            String password = in.readLine();

            try {
               if (dbi.connectUser(username, password)) {
                  write(TCPProtocol.SUCCESS);

                  UserInfo ui = dbi.getUserInfo(username);

                  if (ui != null) {
                     write(String.valueOf(ui.getRole()));
                  }

                  //creating a new player
                  worker.setPlayer(new Player(username, password, UserPrivilege.Privilege.values()[ui.getRole()]));
                  // Change current worker state
                  worker.setState(new WorkerDefaultState(worker, in, out));

               } else {
                  notifyError(TCPProtocol.BAD_AUTHENTIFICATION);
               }
            } catch (SQLException ex) {
               Logger.getLogger(WorkerConnectState.class.getName()).log(Level.SEVERE, null, ex);
               notifyError(TCPProtocol.BAD_AUTHENTIFICATION);
            }

         } else if (line.equals(TCPProtocol.REGISTER_USER)) {
            DBInterface dbi = worker.getServer().getDatabaseInterface();

            // the client types both his username and password in two different command lines
            String username = in.readLine(); // username du client
            String password = in.readLine(); // son new password

            try {

               dbi.registerUser(username, password);
               write(TCPProtocol.SUCCESS);
               
               //creating a new player
               worker.setPlayer(new Player(username, password));
               // Change current worker state
               worker.setState(new WorkerDefaultState(worker, in, out));

            } catch (SQLException ex) {
               Logger.getLogger(WorkerConnectState.class.getName()).log(Level.SEVERE, null, ex);
               notifyError(TCPProtocol.USED_USERNAME);

            } catch (UsedUsernameException ex) {
               Logger.getLogger(WorkerConnectState.class.getName()).log(Level.SEVERE, null, ex);
               notifyError(TCPProtocol.USED_USERNAME);

            }

         } else {
            notifyError(TCPProtocol.WRONG_COMMAND);
         }
      } catch (IOException ex) {
         System.out.println("User was diconnected");
         throw new IOException("Disconnected");
      }

   }

}
