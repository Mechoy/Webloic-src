package weblogic.management.mbeans.custom;

import java.util.Locale;
import weblogic.descriptor.SettableBean;
import weblogic.management.configuration.KernelMBean;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.protocol.ProtocolManager;

public final class NetworkAccessPoint {
   private static final long serialVersionUID = 7259163894528106650L;
   private static final String LISTEN_ADDRESS = "ListenAddress";
   private static final String PUBLIC_ADDRESS = "PublicAddress";
   private static final String EXTERNAL_ADDRESS = "ExternalDNSName";
   private static final String CLUSTER_ADDRESS = "ClusterAddress";

   public static String getPublicAddress(NetworkAccessPointMBean var0) {
      if (var0.isSet("ExternalDNSName")) {
         return var0.getExternalDNSName();
      } else if (var0.isSet("ListenAddress")) {
         return var0.getListenAddress();
      } else {
         return ((SettableBean)var0.getParent()).isSet("ExternalDNSName") ? ((ServerMBean)var0.getParent()).getExternalDNSName() : var0.getListenAddress();
      }
   }

   public static String getClusterAddress(NetworkAccessPointMBean var0) {
      if (var0.isSet("PublicAddress")) {
         return var0.getPublicAddress();
      } else if (var0.isSet("ExternalDNSName")) {
         return var0.getExternalDNSName();
      } else {
         return ((ServerMBean)var0.getParent()).getCluster() != null && ((ServerMBean)var0.getParent()).getCluster().isSet("ClusterAddress") ? ((ServerMBean)var0.getParent()).getCluster().getClusterAddress() : var0.getListenAddress();
      }
   }

   public static int getMaxMessageSize(NetworkAccessPointMBean var0) {
      String var1 = var0.getProtocol().toUpperCase(Locale.US);
      if (var1.endsWith("S")) {
         var1 = var1.substring(0, var1.length() - 1);
      }

      int var2 = -1;
      if (var1.equals("HTTP")) {
         var2 = ((KernelMBean)var0.getParent()).getMaxHTTPMessageSize();
      } else if (var1.equals("T3")) {
         var2 = ((KernelMBean)var0.getParent()).getMaxT3MessageSize();
      } else if (var1.equals("IIOP")) {
         var2 = ((KernelMBean)var0.getParent()).getMaxIIOPMessageSize();
      } else if (var1.equals("COM")) {
         var2 = ((KernelMBean)var0.getParent()).getMaxCOMMessageSize();
      }

      if (var2 == -1) {
         var2 = ((KernelMBean)var0.getParent()).getMaxMessageSize();
      }

      return var2;
   }

   public static boolean isSecure(NetworkAccessPointMBean var0) {
      byte var1 = ProtocolManager.getProtocolByName(var0.getProtocol()).getQOS();
      return var1 == 102 || var1 == 103;
   }
}
