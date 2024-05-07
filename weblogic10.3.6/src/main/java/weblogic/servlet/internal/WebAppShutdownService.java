package weblogic.servlet.internal;

import java.util.Collection;
import java.util.Iterator;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.servlet.internal.session.GracefulShutdownHelper;

public class WebAppShutdownService extends AbstractServerService {
   private static boolean isSuspending;
   private static boolean isSuspended;
   private static boolean ignoreSessions;

   public static void ignoreSessionsDuringShutdown() {
      ignoreSessions = true;
   }

   static boolean isSuspending() {
      return isSuspending;
   }

   static boolean isSuspended() {
      return isSuspended;
   }

   public void start() throws ServiceFailureException {
      isSuspending = false;
      isSuspended = false;
      ignoreSessions = false;
   }

   public void stop() throws ServiceFailureException {
      isSuspending = true;
      if (!ignoreSessions) {
         GracefulShutdownHelper.waitForPendingSessions();
      }

      this.halt();
   }

   public void halt() {
      Collection var1 = WebService.getHttpServers();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         HttpServer var3 = (HttpServer)var2.next();
         var3.shutdown();
      }

      isSuspended = true;
   }
}
