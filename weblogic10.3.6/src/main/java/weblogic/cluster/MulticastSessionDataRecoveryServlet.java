package weblogic.cluster;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerChannelStream;
import weblogic.utils.Debug;
import weblogic.utils.io.DataIO;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public final class MulticastSessionDataRecoveryServlet extends HttpServlet {
   private static final boolean DEBUG = false;
   private static final int DEFAULT_BUF_SIZE = 10240;

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getParameter("ServerName");
      ServletInputStream var4 = var1.getInputStream();
      int var5 = var1.getContentLength();
      if (var5 <= 0) {
         var2.setStatus(403);
      } else {
         byte[] var6 = new byte[var5];
         DataIO.readFully(var1.getInputStream(), var6);
         if (!ClusterService.getClusterService().checkRequest(var3, var6)) {
            var2.setStatus(403);
         } else if (ClusterService.getClusterService().multicastDataEncryptionEnabled() && !var1.isSecure()) {
            ClusterLogger.logEnforceSecureRequest();
            var2.setStatus(403);
         } else {
            String var7 = var1.getParameter("senderNum");
            String var8 = var1.getParameter("lastSeqNum");
            String var9 = var1.getParameter("PeerInfo");
            PeerInfo var10 = ClusterHelper.getPeerInfo(var9);
            Debug.assertion(var10 != null, "Peer info cannot be null");
            int var11 = Integer.valueOf(var7);
            int var12 = Integer.valueOf(var8);
            if (var1.getParameter("PingOnly") != null) {
               this.executePingRequest(var12);
            } else {
               UnsyncByteArrayOutputStream var13 = null;
               WLObjectOutputStream var14 = null;
               ServletOutputStream var15 = null;

               try {
                  var13 = new UnsyncByteArrayOutputStream(10240);
                  var14 = UpgradeUtils.getInstance().getOutputStream(var13, (ServerChannel)null, var10);
                  ServerChannel var16 = ((ServerChannelStream)var1).getServerChannel();
                  var14.setServerChannel(ServerChannelManager.findDefaultLocalServerChannel());
                  MulticastSender var17 = MulticastManager.theOne().findSender(var11);
                  GroupMessage var18 = var17.createRecoverMessage();
                  var14.writeObject(AttributeManager.theOne().getLocalAttributes());
                  var14.writeObject(var18);
                  var14.writeLong(var17.getCurrentSeqNum());
                  var14.flush();
                  var2.setContentType("application/unknown");
                  var15 = var2.getOutputStream();
                  var2.setContentLength(var13.size());
                  var13.writeTo(var15);
                  var15.flush();
               } finally {
                  try {
                     if (var13 != null) {
                        var13.close();
                     }
                  } catch (IOException var30) {
                  }

                  try {
                     if (var15 != null) {
                        var15.close();
                     }
                  } catch (IOException var29) {
                  }

                  try {
                     if (var14 != null) {
                        var14.close();
                     }
                  } catch (IOException var28) {
                  }

               }

            }
         }
      }
   }

   private void executePingRequest(int var1) throws ServletException {
      MulticastSender var2 = MulticastManager.theOne().findSender(2);
      if (var2.getCurrentSeqNum() != (long)var1) {
         throw new ServletException("Incompatible sender sequence numbers. local value " + var2.getCurrentSeqNum() + " received value " + var1);
      }
   }
}
