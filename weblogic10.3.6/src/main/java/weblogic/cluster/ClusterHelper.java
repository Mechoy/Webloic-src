package weblogic.cluster;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import weblogic.common.internal.PeerInfo;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.URLManager;
import weblogic.rmi.spi.HostID;
import weblogic.server.channels.AddressUtils;
import weblogic.servlet.security.Utils;
import weblogic.utils.StringUtils;
import weblogic.utils.io.DataIO;
import weblogic.utils.net.SocketResetException;

public final class ClusterHelper {
   public static final String STRINGFIED_PEERINFO = PeerInfo.getPeerInfo().getMajor() + "," + PeerInfo.getPeerInfo().getMinor() + "," + PeerInfo.getPeerInfo().getServicePack();

   public static URL fabricateHTTPURL(String var0, HostID var1) throws MalformedURLException {
      String var2 = URLManager.findAdministrationURL((ServerIdentity)var1);
      if (var2 == null) {
         throw new MalformedURLException("Could not construct URL for: " + var1);
      } else {
         var2 = URLManager.normalizeToHttpProtocol(var2);
         return new URL(new URL(var2), var0);
      }
   }

   public static String getMachineName() {
      return AddressUtils.getLocalHost().getHostName();
   }

   public static PeerInfo getPeerInfo(String var0) {
      return PeerInfo.getPeerInfo(var0);
   }

   static void logStateDumpRequestRejection(HttpURLConnection var0, IOException var1, String var2) {
      String var3 = null;
      if (var0 != null) {
         int var4 = var0.getContentLength();
         InputStream var5 = var0.getErrorStream();
         if (var4 > 0 && var5 != null) {
            DataInputStream var6 = new DataInputStream(var5);
            byte[] var7 = new byte[var4];

            try {
               DataIO.readFully(var6, var7);
               var3 = StringUtils.getString(var7);
            } catch (IOException var18) {
            } finally {
               try {
                  if (var6 != null) {
                     var6.close();
                  }
               } catch (IOException var17) {
               }

            }
         }
      }

      if (var3 != null) {
         ClusterLogger.logFailedWhileReceivingStateDumpWithMessage(var2, var1, var3, ClusterService.getClusterService().getLocalServerDetails());
      } else if (ClusterAnnouncementsDebugLogger.isDebugEnabled() || !SocketResetException.isResetException(var1)) {
         ClusterLogger.logFailedWhileReceivingStateDump(var2, var1);
      }

   }

   public static String encodeXSS(String var0) {
      return Utils.encodeXSS(var0);
   }
}
