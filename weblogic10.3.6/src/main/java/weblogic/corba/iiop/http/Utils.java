package weblogic.corba.iiop.http;

import java.io.IOException;
import java.security.AccessController;
import java.util.Hashtable;
import java.util.Iterator;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterServiceActivator;
import weblogic.cluster.ClusterServices;
import weblogic.common.internal.VersionInfo;
import weblogic.iiop.MessageHeader;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.AsyncOutgoingMessage;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerChannel;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.internal.HttpServer;
import weblogic.servlet.internal.ServerHelper;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;

final class Utils {
   private static final String CONTENT_LENGTH = "Content-Length";
   private static Cookie tunnelCookie = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private Utils() {
   }

   static final ServerConnection getConnectionFromID(HttpServletRequest var0) throws IOException {
      String var1 = getQueryStringParameter(var0, "connectionID");
      if (var1 == null) {
         return null;
      } else {
         ServerConnection var2 = ServerConnection.findByID(var1);
         return var2;
      }
   }

   static final void sendDeadResponse(HttpServletResponse var0) throws IOException {
      var0.setHeader("WL-Result", "DEAD");
      var0.getOutputStream().print("DEAD");
      var0.getOutputStream().flush();
   }

   static final void sendOKResponse(HttpServletResponse var0) throws IOException {
      var0.setHeader("WL-Result", "OK");
      var0.setHeader("WL-Version", VersionInfo.theOne().getMajor() + "." + VersionInfo.theOne().getMinor() + "." + VersionInfo.theOne().getServicePack() + "." + VersionInfo.theOne().getRollingPatch() + "." + VersionInfo.theOne().hasTemporaryPatch());
      ServletOutputStream var1 = var0.getOutputStream();
      var1.print("OK\n");
      var1.print("HL:12\n\n");
   }

   static final void sendResponse(HttpServletResponse var0, AsyncOutgoingMessage var1) throws IOException {
      int var2 = var1.getLength();
      var0.setHeader("Content-Length", Integer.toString(var2));
      var0.setHeader("WL-Result", "OK");
      var0.setHeader("WL-Type", Integer.toString(MessageHeader.getMsgType(var1.getChunks().buf)));
      ServletOutputStream var3 = var0.getOutputStream();
      var1.writeTo(var3);
      var3.flush();
   }

   static final String getQueryStringParameter(HttpServletRequest var0, String var1) {
      String var2 = var0.getQueryString();
      Hashtable var3 = HttpUtils.parseQueryString(var2);
      String[] var4 = (String[])((String[])var3.get(var1));
      return var4 == null ? null : var4[0];
   }

   static final boolean requestIntended(HttpServletRequest var0) {
      String var1 = var0.getHeader("WL-Dest");
      return var1 == null || Integer.parseInt(var1) == LocalServerIdentity.getIdentity().hashCode();
   }

   static final void addTunnelCookie(HttpServletRequest var0, HttpServletResponse var1) {
      if (tunnelCookie == null) {
         StringBuffer var2 = new StringBuffer(52);

         for(int var3 = 0; var3 < 52; ++var3) {
            var2.append("w");
         }

         var2.append("!").append(Integer.toString(LocalServerIdentity.getIdentity().hashCode()));
         tunnelCookie = new Cookie("JSESSIONID", var2.toString());
      }

      var1.addCookie(tunnelCookie);
   }

   static final void addClusterList(HttpServletRequest var0, HttpServletResponse var1) {
      ClusterServices var2 = ClusterServiceActivator.INSTANCE.getClusterService();
      if (ManagementService.getRuntimeAccess(kernelId).getServer().getCluster() != null && var2 != null) {
         ServletRequestImpl var3 = (ServletRequestImpl)var0;
         ServletResponseImpl var4 = (ServletResponseImpl)var1;
         HttpServer var5 = var3.getContext().getServer();
         ServerChannel var6 = var3.getServerChannel();
         var1.addHeader("WL-Scheme", var6.getProtocol().getProtocolName());
         StringBuffer var7 = new StringBuffer();
         Iterator var8 = var2.getRemoteMembers().iterator();

         ClusterMemberInfo var9;
         while(var8.hasNext()) {
            var9 = (ClusterMemberInfo)var8.next();
            String var10 = ServerHelper.createServerEntry(var6.getChannelName(), var9.identity(), "!", false);
            if (var10 != null) {
               var7.append(var10).append('|');
            }
         }

         var9 = var2.getLocalMember();
         var7.append(ServerHelper.createServerEntry(var6.getChannelName(), var9.identity(), "!", false));
         var1.addHeader("WL-List", var7.toString());
      }
   }
}
