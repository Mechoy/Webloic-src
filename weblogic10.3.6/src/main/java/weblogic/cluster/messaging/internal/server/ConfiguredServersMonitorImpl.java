package weblogic.cluster.messaging.internal.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import weblogic.cluster.messaging.internal.ConfiguredServersMonitor;
import weblogic.cluster.messaging.internal.Environment;
import weblogic.cluster.messaging.internal.ServerConfigurationInformation;
import weblogic.cluster.messaging.internal.ServerConfigurationInformationImpl;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.configuration.NodeManagerMBean;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ConfiguredServersMonitorImpl implements ConfiguredServersMonitor {
   private static final boolean DEBUG;
   private static final AuthenticatedSubject kernelId;
   private final ServerConfigurationInformation localInfo;
   private final TreeSet servers;

   public static ConfiguredServersMonitor getInstance() {
      return ConfiguredServersMonitorImpl.Factory.THE_ONE;
   }

   private ConfiguredServersMonitorImpl() {
      this.servers = new TreeSet();
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      this.localInfo = this.createConfiguration(var1);
      if (DEBUG) {
         this.debug("localInfo=" + this.localInfo);
      }

      ClusterMBean var2 = var1.getCluster();
      ServerMBean[] var3 = var2.getServers();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         ServerConfigurationInformation var5 = this.createConfiguration(var3[var4]);
         if (DEBUG) {
            this.debug("created " + var5);
         }

         this.servers.add(var5);
      }

   }

   private void debug(String var1) {
      Environment.getLogService().debug("[ConfiguredServersMonitor] " + var1);
   }

   private ServerConfigurationInformation createConfiguration(ServerMBean var1) {
      NetworkAccessPointMBean[] var2 = var1.getNetworkAccessPoints();
      InetAddress var3;
      if (var2 != null) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].getName().equals(var1.getCluster().getClusterBroadcastChannel())) {
               try {
                  var3 = InetAddress.getByName(var2[var4].getListenAddress());
               } catch (UnknownHostException var7) {
                  throw new AssertionError(var7);
               }

               if (DEBUG) {
                  this.debug("obtained NAP [" + var3 + ":" + var2[var4].getListenPort() + "] for " + var1.getName());
               }

               return new ServerConfigurationInformationImpl(var3, var2[var4].getListenPort(), var1.getName(), 1L, ChannelHelper.isNAPSecure(var2[var4]));
            }
         }
      }

      String var9 = var1.getListenAddress();
      if (var9 == null || var9.length() == 0) {
         MachineMBean var5 = var1.getMachine();
         if (var5 != null) {
            NodeManagerMBean var6 = var5.getNodeManager();
            if (var6 != null) {
               var9 = var6.getListenAddress();
            }
         }
      }

      try {
         if (var9 != null) {
            var3 = InetAddress.getByName(var9);
         } else {
            var3 = InetAddress.getLocalHost();
         }
      } catch (UnknownHostException var8) {
         throw new AssertionError(var8);
      }

      ServerMBean var10 = ManagementService.getRuntimeAccess(kernelId).getServer();
      if (var1.isListenPortEnabled() && var10.isListenPortEnabled()) {
         return new ServerConfigurationInformationImpl(var3, var1.getListenPort(), var1.getName(), 1L, false);
      } else {
         SSLMBean var11 = var1.getSSL();
         if (var11.isListenPortEnabled() && var10.getSSL().isListenPortEnabled()) {
            return new ServerConfigurationInformationImpl(var3, var11.getListenPort(), var1.getName(), 1L, true);
         } else {
            throw new AssertionError("Servers do not have a common channel to communicate over");
         }
      }
   }

   public ServerConfigurationInformation getLocalServerConfiguration() {
      return this.localInfo;
   }

   public SortedSet getConfiguredServers() {
      return this.servers;
   }

   public ServerConfigurationInformation getConfiguration(String var1) {
      Iterator var2 = this.servers.iterator();

      ServerConfigurationInformation var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ServerConfigurationInformation)var2.next();
      } while(!var3.getServerName().equals(var1));

      return var3;
   }

   // $FF: synthetic method
   ConfiguredServersMonitorImpl(Object var1) {
      this();
   }

   static {
      DEBUG = Environment.DEBUG;
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private static final class Factory {
      static final ConfiguredServersMonitor THE_ONE = new ConfiguredServersMonitorImpl();
   }
}
