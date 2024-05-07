package weblogic.corba.iiop.http;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class TunnelCloseServlet extends HttpServlet {
   public void service(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      ServerConnection var3 = Utils.getConnectionFromID(var1);
      if (var3 != null && Utils.requestIntended(var1)) {
         synchronized(var3) {
            if (var3.isClosed()) {
               Utils.sendDeadResponse(var2);
               return;
            }

            var3.close();
         }

         Utils.sendOKResponse(var2);
      } else {
         Utils.sendDeadResponse(var2);
      }
   }
}
