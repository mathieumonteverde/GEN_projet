package com.heigvd.gen.server.TCPInterface;

import com.heigvd.gen.DBInterface.DBInterface;
import com.heigvd.gen.protocol.tcp.TCPProtocol;
import com.heigvd.gen.server.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
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
            String username = in.readLine();
            String password = in.readLine();
            
            worker.setPlayer(new Player(username, password));
            
            worker.getListener().connectUser(worker);

         } else if (line.equals(TCPProtocol.REGISTER_USER)) {
            DBInterface dbi = worker.getServer().getDatabaseInterface();

            // the client types both his username and password in two different command lines
            String username = in.readLine(); // username du client
            String password = in.readLine(); // son new password
            
            worker.setPlayer(new Player(username, password));
            
            worker.getListener().registerUser(worker);


         }
      } catch (IOException ex) {
         System.out.println("User was diconnected");
         throw new IOException("Disconnected");
      }

   }

}
