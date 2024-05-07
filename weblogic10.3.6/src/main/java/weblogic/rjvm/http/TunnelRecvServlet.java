package weblogic.rjvm.http;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import weblogic.protocol.OutgoingMessage;
import weblogic.servlet.FutureResponseServlet;
import weblogic.servlet.FutureServletResponse;

public final class TunnelRecvServlet extends FutureResponseServlet {
   public void service(HttpServletRequest var1, FutureServletResponse var2) throws IOException {
      boolean var3 = false;

      try {
         HTTPServerJVMConnection var4 = Utils.getConnectionFromID(var1);
         if (var4 == null) {
            Utils.sendDeadResponse(var2);
         } else {
            OutgoingMessage var5;
            synchronized(var4) {
               if (var4.isClosed()) {
                  Utils.sendDeadResponse(var2);
                  return;
               }

               if (var4.getQueueCount() == 0) {
                  var4.registerPending(var2);
                  var3 = true;
                  return;
               }

               var5 = var4.getNextMessage();
            }

            var2.setContentType("application/octet-stream");
            var2.setContentLength(var5.getLength());
            var2.setHeader("WL-Result", "OK");
            var5.writeTo(var2.getOutputStream());
         }
      } finally {
         if (!var3) {
            var2.send();
         }

      }
   }
}
