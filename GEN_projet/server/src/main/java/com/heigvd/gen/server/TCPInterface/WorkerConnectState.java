/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server.TCPInterface;

import com.heigvd.gen.DBInterface.DBInterface;
import com.heigvd.gen.protocol.tcp.TCPProtocol;
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
   
   public WorkerConnectState(TCPServerWorker worker, Socket socket) throws IOException {
      super(worker, socket);
   }
   
   public WorkerConnectState(TCPServerWorker worker, BufferedReader in, PrintWriter out) {
      super(worker, in, out);
   }

   @Override
   public void manageClient() {

      try {
         String line = in.readLine();
         if (line.equals(TCPProtocol.CONNECT_USER)) {
            DBInterface dbi = worker.getServer().getDatabaseInterface();

            String username = in.readLine();
            String password = in.readLine();
            
            try {
               if (dbi.connectUser(username, password)) {
                  write(TCPProtocol.SUCCESS);
                  
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
            
            
            
            // TODO Faire les action DB pour ajouter un utilisateur
            
            
            
            
         } else {
            notifyError(TCPProtocol.WRONG_COMMAND);
         }
      } catch (IOException ex) {
         Logger.getLogger(WorkerConnectState.class.getName()).log(Level.SEVERE, null, ex);
      }

   }

}
