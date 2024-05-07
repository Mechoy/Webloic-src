package weblogic.rjvm.http;

import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;
import weblogic.common.internal.VersionInfo;
import weblogic.rjvm.MsgAbbrevJVMConnection;

final class Utils {
   static final String TUNNEL_SEND = "/bea_wls_internal/HTTPClntSend";
   static final String TUNNEL_RECV = "/bea_wls_internal/HTTPClntRecv";
   static final String TUNNEL_LOGIN = "/bea_wls_internal/HTTPClntLogin";
   static final String TUNNEL_CLOSE = "/bea_wls_internal/HTTPClntClose";
   static final String TUNNEL_OK = "OK";
   static final String TUNNEL_DEAD = "DEAD";
   static final String TUNNEL_RETRY = "RETRY";
   static final String TUNNEL_UNAVAIL = "UNAVAIL";
   static final String RESULT_HEADER = "WL-Result";
   static final String VERSION_HEADER = "WL-Version";
   static final String ID_HEADER = "Conn-Id";

   private Utils() {
   }

   static final HTTPServerJVMConnection getConnectionFromID(HttpServletRequest var0) throws IOException {
      String var1 = getQueryStringParameter(var0, "connectionID");
      if (var1 == null) {
         return null;
      } else {
         HTTPServerJVMConnection var2 = HTTPServerJVMConnection.findByID(var1);
         return var2;
      }
   }

   static final void sendDeadResponse(HttpServletResponse var0) throws IOException {
      var0.setHeader("WL-Result", "DEAD");
      var0.getOutputStream().print("DEAD");
      var0.getOutputStream().flush();
   }

   static final void sendOKResponse(HttpServletResponse var0) throws IOException {
      var0.setContentType("application/octet-stream");
      var0.setHeader("WL-Result", "OK");
      var0.setHeader("WL-Version", VersionInfo.theOne().getMajor() + "." + VersionInfo.theOne().getMinor() + "." + VersionInfo.theOne().getServicePack() + "." + VersionInfo.theOne().getRollingPatch() + "." + VersionInfo.theOne().hasTemporaryPatch());
      ServletOutputStream var1 = var0.getOutputStream();
      var1.print("OK\n");
      var1.print("AS:" + MsgAbbrevJVMConnection.ABBREV_TABLE_SIZE + "\n" + "HL" + ":" + 19 + "\n\n");
   }

   static final String getQueryStringParameter(HttpServletRequest var0, String var1) {
      String var2 = var0.getQueryString();
      Hashtable var3 = HttpUtils.parseQueryString(var2);
      String[] var4 = (String[])((String[])var3.get(var1));
      return var4 == null ? null : var4[0];
   }
}
