package weblogic.jms.dotnet.transport.socketplugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import weblogic.jms.dotnet.transport.MarshalReadableFactory;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.dotnet.transport.TransportFactory;
import weblogic.jms.dotnet.transport.TransportPluginSPI;
import weblogic.jms.dotnet.transport.TransportThreadPool;

public class PlainSocket implements Runnable, TransportPluginSPI {
   static final int HEADER_SIZE = 32;
   static final int HEADER_SIZE_POS = 0;
   private InputStream is;
   private OutputStream os;
   private boolean started;
   private final Stats stats;
   private final Transport transport;
   private final MarshalReadableFactory marshalReadableFactory;
   private final TransportThreadPool pool;
   private Socket workerSocket;

   PlainSocket(Stats var1, MarshalReadableFactory var2, TransportThreadPool var3) throws Exception {
      this.stats = var1;
      this.marshalReadableFactory = var2;
      this.pool = var3;
      this.transport = TransportFactory.createTransport(this, var3);
      this.transport.addMarshalReadableFactory(var2);
   }

   public String toString() {
      return "PlainSocket";
   }

   public static Transport startClient(String var0, int var1, MarshalReadableFactory var2, TransportThreadPool var3) throws Exception {
      PlainSocket var4 = new PlainSocket(new Stats("Client"), var2, var3);
      var4.start(new Socket(var0, var1));
      return var4.transport;
   }

   public static void startServer(int var0, TransportThreadPool var1) throws Exception {
      PlainSocketServer var2 = new PlainSocketServer(var0, (MarshalReadableFactory)null, var1);
      Thread var3 = new Thread(var2);
      var3.setDaemon(true);
      var3.start();
   }

   public MarshalWriter createMarshalWriter() {
      try {
         ChunkOutputStream var1 = new ChunkOutputStream(this.transport);
         var1.reposition(32);
         return var1;
      } catch (Exception var2) {
         throw new AssertionError(var2);
      }
   }

   void start(Socket var1) throws Exception {
      if (this.started) {
         throw new AssertionError();
      } else {
         this.workerSocket = var1;
         this.started = true;
         this.is = var1.getInputStream();
         this.os = var1.getOutputStream();
         Thread var2 = new Thread(this);
         var2.setDaemon(true);
         var2.start();
      }
   }

   public void run() {
      try {
         while(true) {
            ChunkInputStream var1 = this.recv();
            var1.skip((long)(32 - var1.getPosition()));

            try {
               this.transport.dispatch(var1);
            } catch (Throwable var3) {
               var3.printStackTrace();
            }
         }
      } catch (Throwable var4) {
         this.transport.shutdown(new TransportError(var4));
      }
   }

   public synchronized void send(MarshalWriter var1) {
      ChunkOutputStream var2 = (ChunkOutputStream)var1;
      int var3 = var2.size();
      var2.reposition(0);
      var2.writeInt(var3);
      var2.reposition(var3);

      try {
         this.os.write(var2.getBuf(), 0, var2.size());
         this.os.flush();
         this.stats.incSend((long)var3);
      } catch (Throwable var5) {
         this.transport.shutdown(new TransportError(var5));
      }

      var2.internalClose();
   }

   private ChunkInputStream recv() throws Exception {
      ChunkInputStream var1 = new ChunkInputStream(this.transport);
      byte[] var2 = var1.getBuf();

      int var3;
      int var4;
      for(var3 = 0; 4 - var3 > 0; var3 += var4) {
         var4 = this.is.read(var2, var3, 4 - var3);
      }

      int var5;
      for(var4 = var1.readInt() - 4; var4 - var3 + 4 > 0; var3 += var5) {
         var5 = this.is.read(var2, var3, var4 - var3 + 4);
      }

      this.stats.incRecv((long)var4);
      var1.setCount(var4 + 4);
      return var1;
   }

   public long getScratchID() {
      return -1L;
   }

   public void terminateConnection() {
      try {
         if (this.workerSocket != null) {
            this.workerSocket.close();
         }
      } catch (IOException var2) {
      }

   }
}
