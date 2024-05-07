package weblogic.cluster;

import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerChannelStream;
import weblogic.protocol.ServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.io.DataIO;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public final class StateDumpServlet extends HttpServlet implements MulticastSessionIDConstants {
   private static final int DEFAULT_BUF_SIZE = 10240;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getParameter("ServerName");
      ServletInputStream var4 = var1.getInputStream();
      int var5 = var1.getContentLength();
      if (var5 <= 0) {
         var2.sendError(403, "Invalid content-length: " + var5);
      } else {
         byte[] var6 = new byte[var5];
         DataIO.readFully(var1.getInputStream(), var6);
         String var7;
         if (!ClusterService.getClusterService().checkRequest(var3, var6)) {
            var7 = ClusterHelper.encodeXSS(var3);
            var2.sendError(403, this.getServerHashInvalidMessage(var7));
         } else if (ClusterService.getClusterService().multicastDataEncryptionEnabled() && !var1.isSecure()) {
            ClusterLogger.logEnforceSecureRequest();
            var7 = ClusterHelper.encodeXSS(var3);
            var2.sendError(403, this.getUnsecureMulticastMessage(var7));
         } else {
            var7 = var1.getParameter("PeerInfo");
            PeerInfo var8 = ClusterHelper.getPeerInfo(var7);
            Debug.assertion(var8 != null, "Peer info cannot be null");
            UnsyncByteArrayOutputStream var9 = null;
            WLObjectOutputStream var10 = null;
            ServletOutputStream var11 = null;

            try {
               var9 = new UnsyncByteArrayOutputStream(10240);
               ServerChannel var12 = ((ServerChannelStream)var1).getServerChannel();
               var10 = UpgradeUtils.getInstance().getOutputStream(var9, ServerChannelManager.findDefaultLocalServerChannel(), var8);
               ArrayList var13 = (ArrayList)MemberManager.theOne().getRemoteMembers();
               if (ClusterAnnouncementsDebugLogger.isDebugEnabled()) {
                  ClusterAnnouncementsDebugLogger.debug("Sending statedump for " + (var13.size() + 1) + " servers");
               }

               var10.writeInt(var13.size());

               for(int var14 = 0; var14 < var13.size(); ++var14) {
                  MemberAttributes var15 = (MemberAttributes)var13.get(var14);
                  RemoteMemberInfo var16 = MemberManager.theOne().findOrCreate(var15.identity());

                  try {
                     ServerIdentity var17 = var16.getAttributes().identity();
                     var10.setReplacer(new MulticastReplacer(var17));
                     MemberAttributes var18 = var16.getAttributes();
                     var10.writeObjectWL(var18);
                     ArrayList var19 = var16.getMemberServices().getAllOffers();
                     long var20 = var16.findOrCreateReceiver(2, true).getCurrentSeqNum();
                     StateDumpMessage var22 = new StateDumpMessage(var19, 2, var20);
                     long var23 = 3L * ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getServerStartupTime();
                     if (var23 == 0L) {
                        var23 = 3600000L;
                     }

                     if (AttributeManager.theOne().getLocalAttributes().joinTime() - var18.joinTime() < var23 || (long)var19.size() != var20) {
                        var22 = null;
                     }

                     if (ClusterAnnouncementsDebugLogger.isDebugEnabled()) {
                        if (var22 != null) {
                           ClusterAnnouncementsDebugLogger.debug("Sending offers of size " + var19.size() + " of " + var17 + " and seq num " + var22.currentSeqNum);
                        } else {
                           ClusterAnnouncementsDebugLogger.debug("Not sending statedump on behalf  of other server because other server has not been up long enough  for data to be guaranteed consistent.");
                        }
                     }

                     var10.writeObject(var22);
                  } finally {
                     MemberManager.theOne().done(var16);
                  }
               }

               var10.setReplacer(new MulticastReplacer(LocalServerIdentity.getIdentity()));
               var10.writeObject(AttributeManager.theOne().getLocalAttributes());
               var10.writeObject(AnnouncementManager.theOne().createRecoverMessage());
               var10.flush();
               var2.setContentType("application/unknown");
               var11 = var2.getOutputStream();
               var2.setContentLength(var9.size());
               var9.writeTo(var11);
               var11.flush();
            } finally {
               try {
                  if (var9 != null) {
                     var9.close();
                  }
               } catch (IOException var43) {
               }

               try {
                  if (var10 != null) {
                     var10.close();
                  }
               } catch (IOException var42) {
               }

               try {
                  if (var11 != null) {
                     var11.close();
                  }
               } catch (IOException var41) {
               }

            }
         }
      }
   }

   private String getServerHashInvalidMessage(String var1) {
      return "The server hash received from '" + var1 + "' is invalid. " + "The request was rejected by " + ClusterService.getClusterService().getLocalServerDetails();
   }

   private String getUnsecureMulticastMessage(String var1) {
      return "An unsecure statedump request was sent by '" + var1 + "'. Statedump requests must be encrypted when encrypted multicast " + "option is turned on. The request was rejected by " + ClusterService.getClusterService().getLocalServerDetails();
   }
}
