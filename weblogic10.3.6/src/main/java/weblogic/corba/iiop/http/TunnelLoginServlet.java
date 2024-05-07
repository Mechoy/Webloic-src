package weblogic.corba.iiop.http;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.Enumeration;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.common.internal.VersionInfo;
import weblogic.protocol.LocalServerIdentity;
import weblogic.socket.Login;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.StringUtils;

public final class TunnelLoginServlet extends HttpServlet {
   private static final DebugCategory debug = Debug.getCategory("weblogic.iiop.http.tunnelLogin");

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      String var3 = Utils.getQueryStringParameter(var1, "wl-login");
      if (var3 == null) {
         this.rejectConnection(1, "No version information", var2);
      } else {
         if (debug.isEnabled()) {
            Enumeration var4 = var1.getHeaderNames();

            while(var4.hasMoreElements()) {
               System.out.println("<TunnelLogin>: " + var1.getHeader((String)var4.nextElement()));
            }
         }

         String[] var12 = StringUtils.splitCompletely(var3, " \t");
         String var5 = null;
         if (var12.length == 1) {
            var5 = var12[0];
         } else {
            if (var12.length <= 3) {
               this.rejectConnection(1, "Malformed first line - perhaps an attempt to onnect to a plaintext port using SSL or vice versa?", var2);
               return;
            }

            var5 = var12[3];
         }

         if (!VersionInfo.theOne().compatible(var5)) {
            this.rejectConnection(6, VersionInfo.theOne().rejectionReason(var5), var2);
         } else {
            boolean var6 = false;
            int var7 = 0;

            try {
               var7 = Integer.parseInt(Utils.getQueryStringParameter(var1, "HL"));
            } catch (NumberFormatException var11) {
               this.rejectConnection(1, "Malformed first line", var2);
            }

            String var8 = null;

            try {
               var8 = ServerConnection.acceptConnection(var1, var7, var2);
            } catch (ProtocolException var10) {
               this.rejectConnection(1, StackTraceUtils.throwable2StackTrace(var10), var2);
               return;
            }

            Utils.addTunnelCookie(var1, var2);
            var2.setHeader("Conn-Id", var8);
            var2.setHeader("WL-Dest", Integer.toString(LocalServerIdentity.getIdentity().hashCode()));
            Utils.addClusterList(var1, var2);
            Utils.sendOKResponse(var2);
         }
      }
   }

   private void rejectConnection(int var1, String var2, HttpServletResponse var3) throws IOException {
      ServletOutputStream var4 = var3.getOutputStream();
      if (var1 >= Login.RET_CODES.length) {
         var1 = 7;
      }

      var4.println(Login.RET_CODES[var1] + ":" + var2);
      this.log("Login rejected with code: '" + Login.RET_TEXT[var1] + "', reason: " + var2 + "'");
   }
}
