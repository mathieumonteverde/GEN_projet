package com.heigvd.gen.server.TCPInterface;

import com.heigvd.gen.protocol.tcp.TCPProtocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class represents the default state of a TCPServerWorker. Typically, it
 * will manage the different actions a client can make when it is already logged
 * in and not in a waiting ServerRoom.
 *
 * For example it manages LIST_ROOMS, JOIN_ROOM and other commands that don't
 * belong in a particular section of the communication process lifecycle.
 *
 * @author mathieu
 */
public class WorkerDefaultState extends WorkerState {

   /**
    * Constructor to pass on the information to manage the worker
    *
    * @param worker
    * @param in
    * @param out
    */
   public WorkerDefaultState(TCPServerWorker worker, BufferedReader in, PrintWriter out) {
      super(worker, in, out);
   }

   /**
    * For this state, the manageClient method tests the commands and take action
    * in consequences.
    */
   @Override
   public void manageClient(String line) throws IOException {
      try {
         if (line == null) {
            throw new IOException("Disconnected");
         }
         System.out.println(line);

         // If the player asks to list the rooms
         switch (line) {
            case TCPProtocol.LIST_ROOMS:
               worker.getListener().listRooms(worker);
               break;
            case TCPProtocol.JOIN_ROOM:
               String roomID = in.readLine();
               worker.getListener().joinRoom(worker, roomID);
               break;
            case TCPProtocol.GET_SCORES:
               {
                  // Read the usernamte
                  String username = in.readLine();
                  worker.getListener().getScores(worker, username);
                  break;
               }
            case TCPProtocol.DISCONNECT_USER:
               throw new IOException();
            case TCPProtocol.CREATE_ROOMS:
               String name = in.readLine();
               worker.getListener().createRoom(worker, name);
               break;
            case TCPProtocol.DELETE_ROOMS:
               String id = in.readLine();
               worker.getListener().deleteRoom(worker, id);
               break;
            case TCPProtocol.GET_USERS:
               worker.getListener().getUsers(worker);
               break;
            case TCPProtocol.USER_RIGHTS:
               {
                  String username = in.readLine();
                  String newRights = in.readLine();
                  worker.getListener().userRights(worker, username, newRights);
                  break;
               }
            default:
               notifyError(TCPProtocol.WRONG_COMMAND);
               break;
         }

      } catch (IOException ex) {
         System.out.println("The User was disconnected...");
         throw new IOException();
      }
   }
}
