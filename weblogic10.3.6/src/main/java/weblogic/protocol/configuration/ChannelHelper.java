package weblogic.protocol.configuration;

import java.io.ObjectOutput;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import weblogic.cluster.ClusterAddressHelper;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ChannelHelperBase;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolHandlerAdmin;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerChannelStream;
import weblogic.rjvm.RJVMLogger;
import weblogic.rmi.spi.Channel;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerLogger;
import weblogic.server.channels.AddressUtils;
import weblogic.servlet.internal.WebService;
import weblogic.utils.UnsyncStringBuffer;

public final class ChannelHelper extends ChannelHelperBase {
   private static boolean DEBUG = false;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static boolean checkConsistency(ServerMBean var0) {
      HashSet var1 = new HashSet();
      HashMap var2 = new HashMap();
      String[] var3;
      int var5;
      if (var0.getListenAddress() != null && var0.getListenAddress().length() > 0) {
         var3 = new String[]{var0.getListenAddress()};
      } else {
         InetAddress[] var4 = AddressUtils.getIPAny();
         var3 = new String[var4.length];

         for(var5 = 0; var5 < var4.length; ++var5) {
            var3[var5] = var4[var5].getHostAddress();
         }
      }

      for(int var14 = 0; var14 < var3.length; ++var14) {
         if (var0.isListenPortEnabled()) {
            var1.add(var3[var14] + var0.getListenPort());
         }

         if (var0.getSSL().isListenPortEnabled()) {
            var1.add(var3[var14] + var0.getSSL().getListenPort());
         }

         if (var0.isAdministrationPortEnabled()) {
            var1.add(var3[var14] + var0.getAdministrationPort());
         }
      }

      NetworkAccessPointMBean[] var15 = (NetworkAccessPointMBean[])((NetworkAccessPointMBean[])var0.getNetworkAccessPoints().clone());
      Arrays.sort(var15, new Comparator() {
         public int compare(Object var1, Object var2) {
            return ((NetworkAccessPointMBean)var1).getProtocol().compareTo(((NetworkAccessPointMBean)var2).getProtocol());
         }
      });

      for(var5 = 0; var5 < var15.length; ++var5) {
         NetworkAccessPointMBean var6 = var15[var5];
         if (var6.isEnabled()) {
            String[] var7 = new String[]{var6.getListenAddress()};
            if (var7[0] != null && var7[0].length() > 0) {
               try {
                  var7[0] = InetAddress.getByName(var7[0]).getHostAddress();
               } catch (UnknownHostException var13) {
                  return false;
               }
            } else {
               var7 = var3;
            }

            for(int var8 = 0; var8 < var7.length; ++var8) {
               String var9 = var7[var8] + var6.getListenPort();
               if (var1.contains(var9)) {
                  ServerLogger.logPortConflict(var6.getName(), var6.getProtocol() + "://" + var7[var8] + ":" + var6.getListenPort(), "Default Channel", var0.getListenAddress() + ":" + var0.getListenPort());
                  return false;
               }

               NetworkAccessPointMBean var10 = (NetworkAccessPointMBean)var2.get(var9);
               if (var10 == null) {
                  var2.put(var9, var6);
               } else if (var10.isSDPEnabled() != var6.isSDPEnabled()) {
                  ServerLogger.logPortSDPConflict(var6.getName(), var10.getName(), var7[var8] + ":" + var6.getListenPort());
                  return false;
               }

               String var11 = var6.getProtocol() + var7[var8] + var6.getListenPort();
               NetworkAccessPointMBean var12 = (NetworkAccessPointMBean)var2.get(var11);
               if (var12 == null && !var6.getProtocol().equalsIgnoreCase("ADMIN")) {
                  var12 = (NetworkAccessPointMBean)var2.get("ADMIN".toLowerCase(Locale.ENGLISH) + var7[var8] + var6.getListenPort());
               }

               if (var12 != null) {
                  ServerLogger.logPortConflict(var6.getName(), var6.getProtocol() + "://" + var7[var8] + ":" + var6.getListenPort(), var12.getName(), var12.getProtocol() + "://" + var7[var8] + ":" + var12.getListenPort());
                  return false;
               }

               var2.put(var11, var6);
            }
         }
      }

      if (DEBUG) {
         p(var1.toString());
      }

      if (DEBUG) {
         p(var2.toString());
      }

      return true;
   }

   public static void logChannelConfiguration(ServerMBean var0) {
      boolean var1 = isAdminChannelEnabled(var0);
      if (var0.isListenPortEnabled()) {
         RJVMLogger.logChannelConfiguration(var0.getName(), var0.getListenAddress() == null ? "*" : var0.getListenAddress() + ":" + var0.getListenPort(), var0.getExternalDNSName() == null ? "N/A" : "<all>://" + var0.getExternalDNSName() + ":" + var0.getListenPort(), var0.isHttpdEnabled(), var0.isTunnelingEnabled(), false, !var1);
      }

      if (var0.getSSL().isEnabled()) {
         RJVMLogger.logChannelConfiguration(var0.getName(), var0.getListenAddress() + ":" + var0.getSSL().getListenPort() + " (SSL)", var0.getExternalDNSName() == null ? "N/A" : var0.getExternalDNSName() + " (SSL)" + ":" + var0.getSSL().getListenPort(), var0.isHttpdEnabled(), var0.isTunnelingEnabled(), false, !var1);
      }

      RJVMLogger.logChannelSettings(var0.getName(), 50, var0.getAcceptBacklog(), var0.getLoginTimeoutMillis(), var0.getMaxMessageSize(), var0.getCompleteMessageTimeout(), var0.getIdleConnectionTimeout(), var0.getTunnelingClientTimeoutSecs(), var0.getTunnelingClientPingSecs());
      NetworkAccessPointMBean[] var2 = var0.getNetworkAccessPoints();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         NetworkAccessPointMBean var4 = var2[var3];
         if (var4.isEnabled()) {
            RJVMLogger.logChannelConfiguration(var4.getName(), var4.getProtocol() + "://" + var4.getListenAddress() + ":" + var4.getListenPort(), var4.getProtocol() + "://" + var4.getPublicAddress() + ":" + var4.getPublicPort(), var4.isHttpEnabledForThisProtocol(), var4.isTunnelingEnabled(), var4.isOutboundEnabled(), !var1 || var4.getProtocol().equalsIgnoreCase("ADMIN"));
            RJVMLogger.logChannelSettings(var4.getName(), var4.getChannelWeight(), var4.getAcceptBacklog(), var4.getLoginTimeoutMillis(), var4.getMaxMessageSize(), var4.getCompleteMessageTimeout(), var4.getIdleConnectionTimeout(), var4.getTunnelingClientTimeoutSecs(), var4.getTunnelingClientPingSecs());
         }
      }

   }

   public static boolean isAdminChannelEnabled(ServerMBean var0) {
      if (ManagementService.getRuntimeAccess(kernelId).getDomain().isAdministrationPortEnabled()) {
         return true;
      } else {
         NetworkAccessPointMBean[] var1 = var0.getNetworkAccessPoints();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2].isEnabled() && var1[var2].getProtocol().equalsIgnoreCase("ADMIN")) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean isSSLChannelEnabled(ServerMBean var0) {
      if (var0.getSSL().isListenPortEnabled()) {
         return true;
      } else {
         NetworkAccessPointMBean[] var1 = var0.getNetworkAccessPoints();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (isNAPSecure(var1[var2])) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean isNAPSecure(NetworkAccessPointMBean var0) {
      byte var1 = ProtocolManager.getProtocolByName(var0.getProtocol()).getQOS();
      return var1 == 102 || var1 == 103;
   }

   public static String getLocalAdministrationURL() {
      String var0 = getURL(ProtocolHandlerAdmin.PROTOCOL_ADMIN);
      if (var0 == null) {
         var0 = getURL(ProtocolManager.getDefaultProtocol());
         if (var0 == null) {
            var0 = getURL(ProtocolManager.getDefaultSecureProtocol());
         }
      }

      return var0;
   }

   public static String getURL(Protocol var0) {
      return createURL(ServerChannelManager.findLocalServerChannel(var0));
   }

   public static String getIPv4URL(Protocol var0) {
      return createURL(ServerChannelManager.findIPv4LocalServerChannel(var0));
   }

   public static String getIPv6URL(Protocol var0) {
      return createURL(ServerChannelManager.findIPv6LocalServerChannel(var0));
   }

   public static String createCodebaseURL(Channel var0) {
      UnsyncStringBuffer var1 = new UnsyncStringBuffer();
      if (var0.supportsTLS()) {
         var1.append("https://");
      } else {
         var1.append("http://");
      }

      var1.append(var0.getPublicAddress()).append(":").append(var0.getPublicPort());
      if (!WebService.getInternalWebAppContextPath().startsWith("/")) {
         var1.append('/');
      }

      var1.append(WebService.getInternalWebAppContextPath()).append("/classes").append('/');
      return var1.toString();
   }

   public static String createClusterURL(ServerChannel var0) {
      return ClusterAddressHelper.getClusterAddressURL(var0);
   }

   public static String createDefaultClusterURL() {
      return ClusterAddressHelper.getClusterAddressURL(ServerChannelManager.findDefaultLocalServerChannel());
   }

   public static String getClusterURL(ObjectOutput var0) {
      if (var0 instanceof ServerChannelStream) {
         ServerChannel var1 = ((ServerChannelStream)var0).getServerChannel();
         if (var1 != null) {
            return createClusterURL(var1);
         }
      }

      return createDefaultClusterURL();
   }

   public static boolean isMultipleReplicationChannelsConfigured(ServerMBean var0) {
      String var1 = var0.getReplicationPorts();
      if (var1 != null && var1.length() > 0) {
         ClusterMBean var2 = var0.getCluster();
         if (var2 != null) {
            String var3 = var2.getReplicationChannel();
            NetworkAccessPointMBean[] var4 = var0.getNetworkAccessPoints();
            NetworkAccessPointMBean[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               NetworkAccessPointMBean var8 = var5[var7];
               if (var8.isEnabled() && var8.getName().equals(var3)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private static void p(String var0) {
      System.out.println("<ChannelHelper>: " + var0);
   }
}
