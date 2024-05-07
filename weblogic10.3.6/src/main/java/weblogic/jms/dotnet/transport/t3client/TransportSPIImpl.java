package weblogic.jms.dotnet.transport.t3client;

import weblogic.jms.dotnet.transport.MarshalReadableFactory;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.dotnet.transport.TransportFactory;
import weblogic.jms.dotnet.transport.TransportPluginSPI;
import weblogic.jms.dotnet.transport.TransportThreadPool;

public class TransportSPIImpl implements Runnable, TransportPluginSPI {
   private final T3Connection t3conn;
   private Transport transport;
   private static final long HALFLONG = 4611686018427387903L;

   private TransportSPIImpl(T3Connection var1) {
      this.t3conn = var1;
   }

   public static Transport startClient(String var0, int var1, MarshalReadableFactory var2, TransportThreadPool var3) throws Exception {
      T3Connection var4 = new T3Connection(var0, var1, (T3PeerInfo)null, (byte)1);
      TransportSPIImpl var5 = new TransportSPIImpl(var4);
      Transport var6 = TransportFactory.createTransport(var5, var3);
      var6.addMarshalReadableFactory(var2);
      synchronized(var5) {
         var5.transport = var6;
      }

      Thread var7 = new Thread(var5);
      var7.setDaemon(true);
      var7.start();
      long var8 = var6.allocateServiceID();
      var6.registerService(var8, new ShutdownListener(var4));
      return var6;
   }

   public MarshalWriter createMarshalWriter() {
      MarshalWriterImpl var1 = new MarshalWriterImpl(this.transport);
      T3Connection var10000 = this.t3conn;
      return T3Connection.createOneWay(var1);
   }

   public synchronized void send(MarshalWriter var1) {
      MarshalWriterImpl var2 = (MarshalWriterImpl)var1;
      Object var3 = null;

      try {
         this.t3conn.sendOneWay(var2);
      } catch (Error var5) {
         var3 = var5;
      } catch (RuntimeException var6) {
         var3 = var6;
      } catch (Exception var7) {
         var3 = var7;
      }

      if (var3 != null) {
         this.transport.shutdown(new TransportError((Throwable)var3));
      }

   }

   public void run() {
      try {
         while(true) {
            MarshalReaderImpl var1 = this.t3conn.receiveOneWay(this.transport);

            try {
               this.transport.dispatch(var1);
            } catch (Throwable var3) {
            }
         }
      } catch (Throwable var4) {
         this.transport.shutdown(new TransportError(var4));
      }
   }

   public long getScratchID() {
      long var1 = this.t3conn.getScratchID();
      long var3 = var1 / 11L * 6L & 4699868874176267647L & 4611686018427387903L;
      return var3;
   }

   public void terminateConnection() {
      this.t3conn.close();
   }
}
