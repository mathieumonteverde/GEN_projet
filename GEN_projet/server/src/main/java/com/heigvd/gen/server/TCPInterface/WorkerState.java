/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.server.TCPInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author mathieu
 */
public abstract class WorkerState {
   protected final TCPServerWorker worker;
   protected final BufferedReader in;
   protected final PrintWriter out;
   
   public WorkerState(TCPServerWorker worker, BufferedReader in, PrintWriter out) {
      this.worker = worker;
      this.in = in;
      this.out = out;
   }
   public WorkerState(TCPServerWorker worker, Socket socket) throws IOException {
      this.worker = worker;
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream());
   }
   
   public abstract void manageClient();
   public void close() throws IOException {
      in.close();
      out.close();
   }
   
   synchronized public void write(String s) {
      out.println(s);
      out.flush();
   }
}
