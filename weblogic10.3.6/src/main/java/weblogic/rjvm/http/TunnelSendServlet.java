package weblogic.rjvm.http;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class TunnelSendServlet extends HttpServlet {
   private static final boolean DEBUG = false;

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      HTTPServerJVMConnection var3 = Utils.getConnectionFromID(var1);
      if (var3 == null) {
         Utils.sendDeadResponse(var2);
      } else {
         try {
            var3.dispatch(var1, var2);
         } catch (IOException var7) {
            try {
               Utils.sendDeadResponse(var2);
            } catch (IOException var6) {
            }

            return;
         }

         Utils.sendOKResponse(var2);
      }
   }
}
