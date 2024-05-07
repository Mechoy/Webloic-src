package weblogic.corba.iiop.http;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class TunnelSendServlet extends HttpServlet {
   private static final DebugCategory debug = Debug.getCategory("weblogic.iiop.http.tunnelSend");

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      ServerConnection var3 = Utils.getConnectionFromID(var1);
      if (debug.isEnabled()) {
         Enumeration var4 = var1.getHeaderNames();

         while(var4.hasMoreElements()) {
            System.out.println("<TunnelSend>: " + var1.getHeader((String)var4.nextElement()));
         }
      }

      if (var3 != null && Utils.requestIntended(var1)) {
         try {
            var3.dispatch(var1, var2);
         } catch (IOException var7) {
            IOException var8 = var7;

            try {
               if (debug.isEnabled()) {
                  this.log("Problem sending msg", var8);
               }

               Utils.sendDeadResponse(var2);
            } catch (IOException var6) {
            }

            return;
         }

         Utils.sendOKResponse(var2);
      } else {
         if (debug.isEnabled()) {
            this.log("Null JVMConnection");
         }

         Utils.sendDeadResponse(var2);
      }
   }
}
