package weblogic.cache.management;

import java.util.ArrayList;
import java.util.List;
import weblogic.server.AbstractServerService;

public final class CacheServerService extends AbstractServerService {
   private static List shutdownListeners = new ArrayList();

   public static synchronized void addShutdownListener(ServerShutdownListener var0) {
      shutdownListeners.add(var0);
   }

   public void stop() {
      this.halt();
   }

   public void halt() {
      ServerShutdownListener[] var1 = getListeners();
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2].serverShutdown();
         }

      }
   }

   private static synchronized ServerShutdownListener[] getListeners() {
      if (shutdownListeners.size() == 0) {
         return null;
      } else {
         ServerShutdownListener[] var0 = new ServerShutdownListener[shutdownListeners.size()];
         shutdownListeners.toArray(var0);
         return var0;
      }
   }
}
