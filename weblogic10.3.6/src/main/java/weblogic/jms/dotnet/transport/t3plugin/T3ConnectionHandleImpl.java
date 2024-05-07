package weblogic.jms.dotnet.transport.t3plugin;

import java.io.IOException;
import weblogic.jms.dotnet.t3.server.spi.T3Connection;
import weblogic.jms.dotnet.t3.server.spi.T3ConnectionGoneEvent;
import weblogic.jms.dotnet.t3.server.spi.T3ConnectionHandle;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.dotnet.transport.TransportFactory;
import weblogic.utils.io.ChunkedDataInputStream;

public class T3ConnectionHandleImpl implements T3ConnectionHandle {
   private final Object lock = new Object();
   private final T3Connection client;
   private final Transport transport;
   private boolean isClosed;

   T3ConnectionHandleImpl(T3Connection var1) {
      this.client = var1;
      SPIImpl var2 = new SPIImpl(this, var1);
      ThreadPoolImpl var3 = new ThreadPoolImpl();
      this.transport = TransportFactory.createTransport(var2, var3);
   }

   Transport getTransport() {
      return this.transport;
   }

   public void onPeerGone(T3ConnectionGoneEvent var1) {
      IOException var2 = var1.getReason();
      if (var2 == null) {
         var2 = new IOException("unknown");
      }

      synchronized(this.lock) {
         if (this.isClosed) {
            return;
         }

         this.isClosed = true;
      }

      this.transport.shutdown(new TransportError(var2));
   }

   public void onMessage(ChunkedDataInputStream var1) {
      MarshalReaderImpl var2 = new MarshalReaderImpl(this.transport, var1);
      this.transport.dispatch(var2);
   }

   void shutdown(TransportError var1) {
      this.transport.shutdown(var1);
      this.client.shutdown();
   }
}
