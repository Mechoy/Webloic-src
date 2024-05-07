package weblogic.socket;

import java.io.IOException;
import java.net.Socket;
import weblogic.server.channels.ServerThrottle;
import weblogic.utils.concurrent.Latch;

public class WeblogicSocketImpl extends WeblogicSocket {
   private final Latch decrementLatch = new Latch();

   WeblogicSocketImpl(Socket var1) {
      super(var1);
   }

   public final void close() throws IOException {
      super.close();
      if (this.decrementLatch.tryLock()) {
         ServerThrottle.getServerThrottle().decrementOpenSocketCount();
      }

   }

   protected final void finalize() throws Throwable {
      this.close();
      super.finalize();
   }
}
