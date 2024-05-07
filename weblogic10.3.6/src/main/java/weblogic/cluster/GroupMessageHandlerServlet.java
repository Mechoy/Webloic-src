package weblogic.cluster;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.AccessController;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.common.internal.WLObjectInputStream;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Hex;
import weblogic.utils.io.DataIO;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public class GroupMessageHandlerServlet extends HttpServlet {
   private static final boolean DEBUG = true;
   public static final String SERVER_NAME = "server-name";
   public static final String SERVER_HASH = "server-hash";
   private static final String SERVER_URI = "/bea_wls_cluster_internal/0056FABC093BDF49C8AE091F74400598";
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final String CURRENT_SERVER_HASH = encodeServerHash();
   public static final String CURRENT_SERVER_NAME;

   private static String encodeServerHash() {
      byte[] var0 = ClusterService.getClusterService().getSecureHash();
      return Hex.asHex(var0, var0.length, false);
   }

   private static Object deserialize(InputStream var0, int var1) throws IOException, ClassNotFoundException {
      byte[] var2 = new byte[var1];
      DataIO.readFully(var0, var2);
      WLObjectInputStream var3 = new WLObjectInputStream(new UnsyncByteArrayInputStream(var2));
      Object var4 = var3.readObjectWL();
      var3.close();
      return var4;
   }

   private static byte[] serialize(Object var0) throws IOException {
      UnsyncByteArrayOutputStream var1 = new UnsyncByteArrayOutputStream();
      WLObjectOutputStream var2 = new WLObjectOutputStream(var1);
      var2.writeObject(var0);
      var2.flush();
      byte[] var3 = var1.toByteArray();
      var2.close();
      return var3;
   }

   protected void service(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getParameter("server-name");
      String var4 = var1.getParameter("server-hash");
      if (var1.getMethod().equals("POST") && var1.getContentLength() > 0 && var3 != null && var4 != null) {
         System.out.println("Servlet received state dump request from:" + var3 + " hash:" + var4);
         byte[] var5 = Hex.fromHexString(var4);
         if (!ClusterService.getClusterService().checkRequest(var3, var5)) {
            var2.sendError(403);
         } else {
            ServerIdentity var6 = null;
            ClusterServices var7 = ClusterService.getServices();
            if (var7 == null) {
               throw new ServletException("This server is not in a cluster.");
            } else {
               Collection var8 = var7.getRemoteMembers();
               Iterator var9 = var8.iterator();

               while(var9.hasNext()) {
                  ClusterMemberInfo var10 = (ClusterMemberInfo)var9.next();
                  if (var10.serverName().equals(var3)) {
                     var6 = var10.identity();
                     break;
                  }
               }

               if (var6 == null) {
                  throw new ServletException("Sender is not in the cluster view of this server " + var3);
               } else {
                  var9 = null;

                  HttpGroupMessage var15;
                  try {
                     var15 = (HttpGroupMessage)deserialize(var1.getInputStream(), var1.getContentLength());
                  } catch (ClassNotFoundException var13) {
                     throw new ServletException("Error deserializing stream.", var13);
                  }

                  GroupMessage var14 = var15.executeAndGetResponse(var6);
                  byte[] var11 = serialize(var14);
                  var2.setContentLength(var11.length);
                  ServletOutputStream var12 = var2.getOutputStream();
                  var12.write(var11);
                  var12.close();
                  var2.flushBuffer();
                  System.out.println("Writing response len:" + var11.length);
               }
            }
         }
      } else {
         var2.sendError(403);
      }
   }

   public static void executeMessageOnRemoteOverHttp(HostID var0, HttpGroupMessage var1) throws IOException, ClassNotFoundException {
      String var2 = "/bea_wls_cluster_internal/0056FABC093BDF49C8AE091F74400598?server-name=" + CURRENT_SERVER_NAME + "&" + "server-hash" + "=" + CURRENT_SERVER_HASH;
      URL var3 = ClusterHelper.fabricateHTTPURL(var2, var0);
      System.out.println("Sending statedump request:" + var3);
      HttpURLConnection var4 = (HttpURLConnection)var3.openConnection();
      var4.setRequestMethod("POST");
      var4.setDoOutput(true);
      byte[] var5 = serialize(var1);
      var4.setRequestProperty("Content-Length", Integer.toString(var5.length));
      OutputStream var6 = var4.getOutputStream();
      var6.write(var5);
      var6.close();
      if (var4.getResponseCode() != 200) {
         throw new IOException("Unexpected error when requesting statedump  code:" + var4.getResponseCode() + " url:" + var3);
      } else {
         int var7 = var4.getContentLength();
         if (var7 > 0) {
            GroupMessage var8 = (GroupMessage)deserialize(var4.getInputStream(), var4.getContentLength());
            var8.execute(var0);
         }

      }
   }

   static {
      CURRENT_SERVER_NAME = ManagementService.getRuntimeAccess(kernelId).getServer().getName();
   }

   public interface HttpGroupMessage extends GroupMessage {
      GroupMessage executeAndGetResponse(HostID var1);
   }
}
