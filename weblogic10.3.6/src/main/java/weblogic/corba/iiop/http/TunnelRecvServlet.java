package weblogic.corba.iiop.http;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import weblogic.protocol.AsyncOutgoingMessage;
import weblogic.servlet.FutureResponseServlet;
import weblogic.servlet.FutureServletResponse;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class TunnelRecvServlet extends FutureResponseServlet {
   private static final DebugCategory debug = Debug.getCategory("weblogic.iiop.http.tunnelRecv");

   public void service(HttpServletRequest var1, FutureServletResponse var2) throws IOException {
      boolean var3 = false;

      try {
         ServerConnection var4 = Utils.getConnectionFromID(var1);
         Enumeration var5;
         if (debug.isEnabled()) {
            var5 = var1.getHeaderNames();

            while(var5.hasMoreElements()) {
               System.out.println("<TunnelRecv>: " + var1.getHeader((String)var5.nextElement()));
            }
         }

         if (var4 == null || !Utils.requestIntended(var1)) {
            Utils.sendDeadResponse(var2);
            return;
         }

         var5 = null;
         AsyncOutgoingMessage var13;
         synchronized(var4) {
            if (var4.isClosed()) {
               Utils.sendDeadResponse(var2);
               return;
            }

            if (var4.getQueueCount() == 0) {
               var4.registerPending(var1, var2);
               var3 = true;
               return;
            }

            var13 = var4.getNextMessage();
         }

         Utils.sendResponse(var2, var13);
      } finally {
         if (!var3) {
            var2.send();
         }

      }

   }
}
