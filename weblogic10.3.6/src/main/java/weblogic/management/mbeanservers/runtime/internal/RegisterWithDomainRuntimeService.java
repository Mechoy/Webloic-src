package weblogic.management.mbeanservers.runtime.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.jndi.Environment;
import weblogic.management.configuration.JMXMBean;
import weblogic.management.jmx.JMXLogger;
import weblogic.management.mbeanservers.domainruntime.MBeanServerConnectionManagerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.ConnectMonitorFactory;
import weblogic.protocol.ServerURL;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.t3.srvr.EnableListenersIfAdminChannelAbsentService;

public abstract class RegisterWithDomainRuntimeService extends AbstractServerService {
   private static final String JNDI = "/jndi/";
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXRuntime");
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static boolean doNotPingPort = Boolean.getBoolean("weblogic.management.failover.doNotPingAdminPort");
   JMXConnector domainRuntimeConnector = null;
   RuntimeAccess runtimeAccess;

   public void start() throws ServiceFailureException {
      this.runtimeAccess = ManagementService.getRuntimeAccess(kernelId);
      if (this.isEnabled() && !this.runtimeAccess.isAdminServer()) {
         if (debug.isDebugEnabled()) {
            debug.debug("Starting MBeanServer weblogic.management.mbeanservers.runtime");
         }

         this.notifyDomainRuntime();
      } else {
         if (debug.isDebugEnabled()) {
            debug.debug("Runtime MBeanServer Disabledweblogic.management.mbeanservers.runtime");
         }

      }
   }

   public void stop() throws ServiceFailureException {
      if (this.runtimeAccess == null) {
         this.runtimeAccess = ManagementService.getRuntimeAccess(kernelId);
      }

      if (this.isEnabled() && !this.runtimeAccess.isAdminServer()) {
         try {
            ConnectMonitorFactory.unregister();
         } catch (Throwable var2) {
            if (debug.isDebugEnabled()) {
               debug.debug("Unable to unregister disconnect listener");
            }
         }

      }
   }

   private boolean isEnabled() {
      JMXMBean var1 = this.runtimeAccess.getDomain().getJMX();
      return var1.isRuntimeMBeanServerEnabled() && (var1.isDomainMBeanServerEnabled() || var1.isManagementEJBEnabled());
   }

   private void notifyDomainRuntime() {
      if (debug.isDebugEnabled()) {
         debug.debug("Contacting Domain MBeanServer " + MBeanServerConnectionManagerMBean.OBJECT_NAME);
      }

      MBeanServerConnection var1 = this.getDomainMBeanServerConnection();
      if (var1 != null) {
         try {
            ObjectName var2 = new ObjectName(MBeanServerConnectionManagerMBean.OBJECT_NAME);
            var1.invoke(var2, "notifyNewMBeanServer", new Object[]{ManagementService.getRuntimeAccess(kernelId).getServerName()}, new String[]{String.class.getName()});
         } catch (MalformedObjectNameException var19) {
            JMXLogger.logExceptionEstablishingJMXConnectivity(var19);
         } catch (InstanceNotFoundException var20) {
            JMXLogger.logExceptionEstablishingJMXConnectivity(var20);
         } catch (MBeanException var21) {
            JMXLogger.logExceptionEstablishingJMXConnectivity(var21);
         } catch (ReflectionException var22) {
            JMXLogger.logExceptionEstablishingJMXConnectivity(var22);
         } catch (IOException var23) {
            JMXLogger.logExceptionEstablishingJMXConnectivity(var23);
         } finally {
            try {
               this.domainRuntimeConnector.close();
               this.domainRuntimeConnector = null;
            } catch (IOException var18) {
               debug.debug("Failed to close DomainMBeanServerConnection", var18);
            }

         }

      }
   }

   private MBeanServerConnection getDomainMBeanServerConnection() {
      JMXServiceURL var1 = null;
      String var2 = ManagementService.getRuntimeAccess(kernelId).getAdminServerName();
      String var3 = null;

      try {
         var3 = URLManager.findAdministrationURL(var2);
      } catch (UnknownHostException var8) {
         String var5 = "<JMXServiceURL:null>";
         JMXLogger.logAdminstrationServerNotAvailable(var2, var5);
         var3 = ManagementService.getPropertyService(kernelId).getAdminHost();
         if (!Boolean.getBoolean("weblogic.management.failover.useSpecifiedProtocol") && var3.startsWith("http")) {
            var3 = "t3" + var3.substring(4);
         }

         this.registerForDisconnects(var3);
         return null;
      }

      try {
         this.registerForDisconnects(var3);
         ServerURL var4 = new ServerURL(var3);
         var1 = new JMXServiceURL(var4.getProtocol(), var4.getHost(), var4.getPort(), "/jndi/weblogic.management.mbeanservers.domainruntime");
      } catch (MalformedURLException var7) {
         JMXLogger.logExceptionEstablishingJMXConnectivity(var7);
         return null;
      }

      MBeanServerConnection var10;
      try {
         Hashtable var11 = new Hashtable();
         var11.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
         int var12 = ManagementService.getRuntimeAccess(kernelId).getDomain().getJMX().getInvocationTimeoutSeconds();
         if (var12 > 0) {
            var11.put("jmx.remote.x.request.waiting.timeout", new Long((long)(var12 * 1000)));
         }

         if (var3 != null && var3.length() > 0) {
            var11.put("java.naming.provider.url", var3);
         }

         this.domainRuntimeConnector = JMXConnectorFactory.connect(var1, var11);
         var10 = this.domainRuntimeConnector.getMBeanServerConnection();
      } catch (IOException var9) {
         String var6 = var1 != null ? var1.toString() : "<JMXServiceURL:null>";
         JMXLogger.logUnableToEstablishJMXConnectivity(var2, var6, var9);
         return null;
      }

      JMXLogger.logEstablishedJMXConnectivity(var2, var1.toString());
      return var10;
   }

   private void registerForDisconnects(String var1) {
      ArrayList var2 = new ArrayList();
      Environment var3 = new Environment();
      var3.setProviderUrl(var1);
      var2.add(var3);
      String var4 = ManagementService.getPropertyService(kernelId).getAdminHost();
      String[] var5 = URLManager.findAllAddressesForHost(var4);
      if (debug.isDebugEnabled()) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            debug.debug("In register for alternate disconnects - adminURL[" + var6 + "] = " + var5[var6]);
         }
      }

      if (var5 != null && var5.length > 1) {
         String var13 = this.getHostFromURL(var1);
         String[] var7 = URLManager.findAllAddressesForHost(var1);

         for(int var8 = 0; var8 < var5.length; ++var8) {
            String var9 = var5[var8];
            if (!Boolean.getBoolean("weblogic.management.failover.useSpecifiedProtocol") && var9.startsWith("http")) {
               var9 = "t3" + var9.substring(4);
            }

            if (!this.getHostFromURL(var5[var8]).equalsIgnoreCase(var13)) {
               boolean var10 = false;

               for(int var11 = 0; var11 < var7.length; ++var11) {
                  if (this.getHostFromURL(var5[var8]).equalsIgnoreCase(this.getHostFromURL(var7[var11]))) {
                     var10 = true;
                     break;
                  }
               }

               if (!var10 && !this.isReachable(var9)) {
                  var3 = new Environment();
                  var3.setProviderUrl(var9);
                  var2.add(var3);
               }
            }
         }
      }

      try {
         if (!ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
            if (debug.isDebugEnabled()) {
               debug.debug("Registering for disconnects for " + var1);
            }

            ConnectMonitorFactory.registerForever((List)var2);
         }
      } catch (Throwable var12) {
         if (debug.isDebugEnabled()) {
            debug.debug("Unable to register disconnect listener for " + var1);
         }
      }

   }

   private String getHostFromURL(String var1) {
      try {
         ServerURL var2 = new ServerURL(var1);
         return var2.getHost();
      } catch (Exception var3) {
         if (debug.isDebugEnabled()) {
            debug.debug("Error getting host from URL " + var3.getMessage());
         }

         return var1;
      }
   }

   private boolean isReachable(String var1) {
      if (doNotPingPort) {
         return false;
      } else {
         SocketChannel var2 = null;

         boolean var4;
         try {
            ServerURL var3 = new ServerURL(var1);
            String var20 = var3.getHost();
            int var5 = var3.getPort();
            InetSocketAddress var6 = new InetSocketAddress(InetAddress.getByName(var20), var5);
            var2 = SocketChannel.open();
            Socket var7 = var2.socket();
            var7.connect(var6, 1000);
            boolean var8 = true;
            return var8;
         } catch (Exception var18) {
            if (debug.isDebugEnabled()) {
               debug.debug("Error pinging socket " + var18.getMessage());
            }

            var4 = false;
         } finally {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Exception var17) {
               }
            }

         }

         return var4;
      }
   }

   protected boolean doEarly() {
      return EnableListenersIfAdminChannelAbsentService.isOpenForManagementConnectionsEarly();
   }
}
