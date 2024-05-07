package weblogic.cluster.messaging.internal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PingRoutineImpl implements PingRoutine {
   protected static final boolean DEBUG;

   public static PingRoutineImpl getInstance() {
      return PingRoutineImpl.Factory.THE_ONE;
   }

   public long ping(ServerConfigurationInformation var1) {
      Socket var2 = new Socket();
      InetSocketAddress var3 = new InetSocketAddress(var1.getAddress(), var1.getPort());

      long var5;
      try {
         var2.connect(var3, 3000);
         if (DEBUG) {
            this.debug("Successfully pinged " + var1.getServerName());
         }

         long var4 = 1L;
         return var4;
      } catch (IOException var16) {
         var5 = 0L;
      } finally {
         try {
            var2.close();
         } catch (IOException var15) {
         }

      }

      return var5;
   }

   protected void debug(String var1) {
      Environment.getLogService().debug("[Group - PingRoutine] " + var1);
   }

   static {
      DEBUG = Environment.DEBUG;
   }

   private static final class Factory {
      static final PingRoutineImpl THE_ONE = new PingRoutineImpl();
   }
}
