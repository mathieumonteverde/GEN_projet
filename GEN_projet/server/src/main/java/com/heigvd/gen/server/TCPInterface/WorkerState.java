/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server.TCPInterface;

import com.heigvd.gen.protocol.tcp.TCPProtocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Abstract class that represent a state of the TCPServerWorker communication
 * process.
 * 
 * Each subclass should represesnt a distinc state in the communication process.
 * 
 * For example, the Login State would only manage Login or Inscription cases
 * of the defined protocol and not accept other queries. 
 * 
 * 
 * 
 * @author mathieu
 */
public abstract class WorkerState {
   protected final TCPServerWorker worker;
   protected final BufferedReader in;
   protected final PrintWriter out;
   
   /**
    * Constructor
    * @param worker the worker we are the state of
    * @param in the entry BufferedReader
    * @param out the ouput writer
    */
   public WorkerState(TCPServerWorker worker, BufferedReader in, PrintWriter out) {
      this.worker = worker;
      this.in = in;
      this.out = out;
   }
   
   /**
    * The main managing method
    */
   public abstract void manageClient(String line) throws IOException;
   
   /**
    * Close the socket in order to finish the communication
    * @throws IOException 
    */
   public void close() throws IOException {
      in.close();
      out.close();
   }
   
   /**
    * Write a command in the socket.
    * @param s 
    */
   synchronized public void write(String ... lines) {
      for (String line : lines) {
         out.println(line);
      }
      out.flush();
   }
   
   /**
    * Notify the client of an error in the protocol conversation
    * @param errorMessage the message error
    */
   public void notifyError(String errorMessage) {
      write(TCPProtocol.ERROR);
      write(errorMessage);
   }
}
