package weblogic.jms.dotnet.transport.t3plugin;

import weblogic.jms.dotnet.t3.server.spi.T3Connection;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.dotnet.transport.TransportPluginSPI;

class SPIImpl implements TransportPluginSPI {
   private final T3ConnectionHandleImpl chi;
   private final T3Connection t3Conn;
   private static final long HALFLONG = 4611686018427387903L;

   SPIImpl(T3ConnectionHandleImpl var1, T3Connection var2) {
      this.t3Conn = var2;
      this.chi = var1;
   }

   public MarshalWriter createMarshalWriter() {
      try {
         return new MarshalWriterImpl(this.chi.getTransport(), this.t3Conn.getRequestStream());
      } catch (Throwable var2) {
         return new MarshalWriterImpl(this.chi.getTransport(), var2);
      }
   }

   public void send(MarshalWriter var1) {
      Throwable var2 = null;
      MarshalWriterImpl var3 = (MarshalWriterImpl)var1;
      var2 = var3.getThrowable();
      if (var2 == null) {
         try {
            this.t3Conn.send(var3.getChunkedDataOutputStream());
            return;
         } catch (Throwable var5) {
            var2 = var5;
         }
      } else {
         var3.closeInternal();
      }

      this.chi.shutdown(new TransportError(var2));
   }

   public long getScratchID() {
      long var1 = this.t3Conn.getRJVMId().getDifferentiator();
      long var3 = var1 / 11L * 6L & 4699868874176267647L & 4611686018427387903L;
      return var3;
   }

   public void terminateConnection() {
      this.t3Conn.shutdown();
   }
}
