package weblogic.wsee.cluster;

import java.security.AccessController;
import java.util.Locale;
import javax.xml.rpc.JAXRPCException;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.subject.SubjectManager;

public class ClusterUtil {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static String getProtocol(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.indexOf(58);
         return var1 <= 0 ? null : var0.substring(0, var1);
      }
   }

   public static String getExternalAddress(String var0) {
      String var1 = System.getProperty("weblogic.wsee.proxy.address");
      if (var1 != null) {
         return var1;
      } else {
         String var2 = var0.toLowerCase(Locale.ENGLISH);
         ServerChannel var3 = ServerChannelManager.findLocalServerChannel("weblogic-wsee-proxy-channel-" + var2);
         if (var3 != null) {
            String var6 = var3.getPublicAddress();
            if (var6 == null) {
               throw new JAXRPCException("Public address for the proxy channel is not set");
            } else if (!var0.equalsIgnoreCase(var3.getProtocolPrefix())) {
               throw new JAXRPCException("weblogic-wsee-proxy-channel-" + var0 + " should have the protocol be " + var0);
            } else {
               return var0 + "://" + var6 + ":" + var3.getPublicPort();
            }
         } else {
            ServerChannel var4 = ServerChannelManager.findLocalServerChannel(ProtocolManager.getProtocolByName(var2));
            if (var4 == null) {
               throw new JAXRPCException("No default channel established for protocol " + var0);
            } else {
               String var5 = ChannelHelper.createClusterURL(var4);
               if (var5.contains(",")) {
                  throw new JAXRPCException("Network channel weblogic-wsee-proxy-channel-" + var0 + " should be set.");
               } else {
                  return var5;
               }
            }
         }
      }
   }

   public static String getAddress() {
      String var0 = System.getProperty("weblogic.proxy.server");
      if (var0 != null) {
         int var3 = var0.indexOf("//");
         int var2 = var0.lastIndexOf(58);
         if (var3 >= 0 && var3 + 1 < var2) {
            return var0.substring(var3 + 2, var2);
         } else {
            throw new JAXRPCException("Invalid proxy server specified: " + var0 + ", correct format: t3://[host]:[port]");
         }
      } else {
         String var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getListenAddress();
         if (var1 == null) {
            throw new JAXRPCException("Server listen address is not set");
         } else {
            return var1;
         }
      }
   }

   public static int getPort() {
      String var0 = System.getProperty("weblogic.proxy.server");
      if (var0 != null) {
         int var1 = var0.lastIndexOf(58);
         if (var1 >= 6) {
            try {
               int var2 = Integer.parseInt(var0.substring(var1 + 1));
               return var2;
            } catch (NumberFormatException var3) {
               throw new JAXRPCException("Invalid proxy server specified: " + var0 + ", correct format: t3://[host]:[port]", var3);
            }
         } else {
            throw new JAXRPCException("Invalid proxy server specified: " + var0 + ", correct format: t3://[host]:[port]");
         }
      } else {
         return ManagementService.getRuntimeAccess(kernelId).getServer().getListenPort();
      }
   }

   public static AuthenticatedSubject getSubject(AuthenticatedSubject var0) {
      return (AuthenticatedSubject)SubjectManager.getSubjectManager().getCurrentSubject(var0);
   }
}
