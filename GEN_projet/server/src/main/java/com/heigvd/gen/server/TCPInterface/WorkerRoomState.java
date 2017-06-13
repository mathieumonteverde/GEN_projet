package com.heigvd.gen.server.TCPInterface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heigvd.gen.protocol.tcp.TCPProtocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This worker state represent the communication options inside a waiting
 * ServerRoom It should keep track of the room from which it manages the
 * communication.
 *
 * It observes the room in order to communicate the changes that occur inside of
 * it.
 */
public class WorkerRoomState extends WorkerState {

   /**
    * Constructor to pass on the state informations
    *
    * @param worker the worker
    * @param in the reader
    * @param out the writer
    * @param room the room we are inside of
    * @throws JsonProcessingException
    */
   public WorkerRoomState(TCPServerWorker worker, BufferedReader in, PrintWriter out) {
      super(worker, in, out);
   }

   /**
    * The manageClient purpose is to wait for the client to say it is ready to
    * play.
    *
    */
   @Override
   public void manageClient(String line) throws IOException {
      try {
         if (line == null) {
            worker.getListener().disconnectUser(worker);
            throw new IOException("Disconnected");
         }

         // If the user say it is ready
         switch (line) {
         // If the user actively asks for the information
            case TCPProtocol.USER_READY:
               worker.getListener().userReady(worker);
               break;
            case TCPProtocol.ROOM_INFOS:
               worker.getListener().roomInfos(worker);
               break;
            case TCPProtocol.QUIT_ROOM:
               worker.getListener().quitRoom(worker);
               break;
            case TCPProtocol.BAN_USER:
               String username = in.readLine();
               worker.getListener().banUser(worker, username);
               break;
            default:
               notifyError(TCPProtocol.WRONG_COMMAND);
               break;
         }

      } catch (IOException ex) {
         System.out.println("The player was unexpectedly disconnected...");
         System.out.println("Removing the player from the room...");
         worker.getListener().disconnectUser(worker);
         throw new IOException("User disconnected");
      }
   }
//   @Override
//   public void update(Observable o, Object arg) {
//      if (o == room) {
//
//         if (room.isBanned(worker.getPlayer()) || room.isDeleted()) {
//            write(TCPProtocol.DISCONNECTION);
//            room.deleteObserver(this);
//            worker.setState(new WorkerDefaultState(worker, in, out));
//         } else if (room.isReady() && !room.isStarted()) {
//            room.startRace();
//            write(TCPProtocol.RACE_START);
//         } else if (room.isStarted() && room.getCountdown() != -1) {
//            write(TCPProtocol.COUNTDOWN);
//            write(String.valueOf(room.getCountdown()));
//         } else {
//            sendRoomInformations();
//            System.out.println("Sending room info");
//         }
//      }
//   }

}
