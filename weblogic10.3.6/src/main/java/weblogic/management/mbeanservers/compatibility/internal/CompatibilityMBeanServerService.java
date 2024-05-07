package weblogic.management.mbeanservers.compatibility.internal;

import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.ManagementLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.jmx.mbeanserver.WLSMBeanServer;
import weblogic.management.jmx.mbeanserver.WLSMBeanServerBuilder;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;
import weblogic.management.jmx.modelmbean.WLSModelMBeanFactory;
import weblogic.management.mbeanservers.internal.RuntimeMBeanAgent;
import weblogic.management.mbeanservers.internal.SecurityInterceptor;
import weblogic.management.mbeanservers.internal.WLSObjectSecurityManagerImpl;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.EditFailedException;
import weblogic.management.provider.MSIService;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.ConnectMonitorFactory;
import weblogic.protocol.URLManager;
import weblogic.rmi.extensions.ConnectEvent;
import weblogic.rmi.extensions.ConnectListener;
import weblogic.rmi.extensions.ConnectMonitor;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.ServerDisconnectEvent;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class CompatibilityMBeanServerService extends AbstractServerService {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXCompatibility");
   private static final String COMPATABILITY_VERSION = "8.1.0.0";
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private WLSModelMBeanContext context = null;
   private Context localJNDIContext;
   MBeanHome adminHome;

   public void start() throws ServiceFailureException {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      if (!var1.getDomain().getJMX().isCompatibilityMBeanServerEnabled()) {
         debug.debug("Compatability MBeanServer is Disabled");
      } else {
         if (debug.isDebugEnabled()) {
            debug.debug("Starting Compatability MBeanServer ");
         }

         WLSMBeanServerBuilder var2 = new WLSMBeanServerBuilder();
         WLSMBeanServer var3 = (WLSMBeanServer)var2.newMBeanServer(var1.getDomainName(), (MBeanServer)null, (MBeanServerDelegate)null);
         CompatibilityObjectNameManager var4 = new CompatibilityObjectNameManager();
         SecurityInterceptor var5 = new SecurityInterceptor(var3);
         var3.addInterceptor(var5);
         CompatibilityInterceptor var6 = new CompatibilityInterceptor();
         var3.addInterceptor(var6);
         if (var1.isAdminServer()) {
            EditAccess var7 = ManagementServiceRestricted.getEditAccess(kernelId);
            EditServiceInterceptor var8 = new EditServiceInterceptor(var7, var3);
            var3.addInterceptor(var8);
            var3.addInterceptor(new ReparentingInterceptor());
         }

         var3.setFirstAccessCallback(this.createAccessCallback());
         RemoteMBeanServerImpl var23 = new RemoteMBeanServerImpl(var3);
         this.context = new WLSModelMBeanContext(var3, var4, WLSObjectSecurityManagerImpl.getInstance());
         this.context.setVersion("8.1.0.0");
         this.context.setReadOnly(false);
         this.context.setRecurse(true);
         this.context.setFilteringEnabled(true);
         this.context.setNotificationFactoryClassName(PropertyChangeNotificationTranslator.class.getName());
         MBeanHomeImpl var24 = new MBeanHomeImpl(var23);
         var23.setMBeanHome(var24);
         Environment var9 = this.getBaseEnvironment();

         try {
            this.localJNDIContext = var9.getInitialContext();
            this.localJNDIContext.rebind("weblogic.management.server", var23);
         } catch (NamingException var18) {
            throw new ServiceFailureException("Unable to initialize Compatability Service", var18);
         }

         if (var1.isAdminServer()) {
            this.adminHome = new AdminMBeanHomeImpl(var24);

            try {
               this.localJNDIContext.rebind("weblogic.management.adminhome", this.adminHome);
            } catch (NamingException var17) {
               throw new ServiceFailureException("Unable to initialize Compatability Service", var17);
            }

            ConnectMonitor var10 = ConnectMonitorFactory.getConnectMonitor();
            var10.addConnectDisconnectListener(this.createAdminServerConnectListener(), this.createAdminServerDisconnectListener());
         } else {
            Environment var25 = this.getBaseEnvironment();
            String var11 = var1.getAdminServerName();
            String var12 = this.getAdminstrationServerURL(var11);
            var25.setProviderUrl(var12);

            try {
               if (!var1.isAdminServerAvailable()) {
                  this.initForMSI(var24);
                  MSIService.getMSIService().registerForReconnectToAdminServer();
                  ConnectMonitor var13 = ConnectMonitorFactory.getConnectMonitor();
                  var13.addConnectListener(this.createMSIConnectListener());
               } else {
                  Context var26 = var25.getInitialContext();
                  MBeanHome var14 = (MBeanHome)PortableRemoteObject.narrow(var26.lookup("weblogic.management.adminhome"), MBeanHome.class);
                  String var15 = var1.getServerName();
                  String var16 = URLManager.findAdministrationURL(var15);
                  if (var14 != null) {
                     var14.addManagedHome(var24, var15, var16);
                  }
               }
            } catch (NamingException var20) {
               if (debug.isDebugEnabled()) {
                  debug.debug("Unable to connect to the administration server.", var20);
               }
            } catch (UnknownHostException var21) {
               if (debug.isDebugEnabled()) {
                  debug.debug("Unable to find the administration server URL.", var21);
               }
            } catch (RemoteRuntimeException var22) {
               if (debug.isDebugEnabled()) {
                  debug.debug("Unable to reach the administration server URL.", var22);
               }

               ManagementLogger.logASNotReachable();
            }
         }

         try {
            this.localJNDIContext.bind("weblogic.management.home.localhome", var24);
            this.localJNDIContext.bind("weblogic.management.home." + var1.getServerName(), var24);
         } catch (NamingException var19) {
            if (debug.isDebugEnabled()) {
               debug.debug("Unable to connect to the administration server.", var19);
            }
         }

         if (debug.isDebugEnabled()) {
            debug.debug("Started Compatability MBeanServer ");
         }

      }
   }

   String getAdminstrationServerURL(String var1) {
      String var2 = null;

      try {
         var2 = URLManager.findAdministrationURL(var1);
      } catch (UnknownHostException var4) {
         if (debug.isDebugEnabled()) {
            debug.debug("Unable to get administration url.", var4);
         }

         var2 = ManagementService.getPropertyService(kernelId).getAdminBinaryURL();
      }

      return var2;
   }

   private void initForMSI(MBeanHome var1) throws ServiceFailureException {
      this.adminHome = new AdminMBeanHomeImpl(var1);

      try {
         this.localJNDIContext.rebind("weblogic.management.adminhome", this.adminHome);
      } catch (NamingException var3) {
         throw new ServiceFailureException("Unable to initialize Compatability Service", var3);
      }
   }

   private Environment getBaseEnvironment() {
      Environment var1 = new Environment();
      var1.setReplicateBindings(false);
      var1.setCreateIntermediateContexts(true);
      return var1;
   }

   private WLSMBeanServer.FirstAccessCallback createAccessCallback() {
      return new WLSMBeanServer.FirstAccessCallback() {
         public void accessed(MBeanServer var1) {
            SecurityServiceManager.runAs(CompatibilityMBeanServerService.kernelId, CompatibilityMBeanServerService.kernelId, new PrivilegedAction() {
               public Object run() {
                  CompatibilityMBeanServerService.this.registerAllMBeans();
                  return null;
               }
            });
         }
      };
   }

   private void registerAllMBeans() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      WLSModelMBeanContext var2 = new WLSModelMBeanContext(this.context.getMBeanServer(), this.context.getNameManager(), this.context.getSecurityManager());
      var2.setVersion(this.context.getVersion());
      var2.setReadOnly(false);
      var2.setRecurse(false);
      var2.setNotificationFactoryClassName(PropertyChangeNotificationTranslator.class.getName());
      new RuntimeMBeanAgent(var2, var1);
      DomainMBean var3;
      if (var1.isAdminServer() || !var1.isAdminServerAvailable()) {
         if (var1.isAdminServer()) {
            new RuntimeMBeanAgent(var2, ManagementService.getDomainAccess(kernelId));
         }

         var3 = null;

         try {
            var3 = ManagementServiceRestricted.getEditAccess(kernelId).getDomainBeanWithoutLock();
            var1.initializeCallbacks(var3);
         } catch (EditFailedException var5) {
            throw new Error(var5);
         }

         WLSModelMBeanContext var4 = new WLSModelMBeanContext(this.context.getMBeanServer(), this.context.getNameManager(), this.context.getSecurityManager());
         var4.setVersion(this.context.getVersion());
         var4.setReadOnly(false);
         var4.setRecurse(true);
         var4.setFilteringEnabled(true);
         var4.setNotificationFactoryClassName(PropertyChangeNotificationTranslator.class.getName());
         WLSModelMBeanFactory.registerWLSModelMBean(var3, var4);
      }

      var3 = var1.getDomain();
      var1.initializeCallbacks(var3);
      this.context.setReadOnly(true);
      this.context.setFilteringEnabled(false);
      WLSModelMBeanFactory.registerWLSModelMBean(var3, this.context);
      this.context.setReadOnly(false);
      this.context.setFilteringEnabled(true);
   }

   public void stop() throws ServiceFailureException {
   }

   public void halt() throws ServiceFailureException {
      this.stop();
   }

   private ConnectListener createAdminServerConnectListener() {
      return new ConnectListener() {
         public void onConnect(ConnectEvent var1) {
            String var2 = var1.getServerName();
            if (CompatibilityMBeanServerService.debug.isDebugEnabled()) {
               CompatibilityMBeanServerService.debug.debug("onConnect received for = " + var2);
            }

            CompatibilityMBeanServerService.this.connectToManagedServer(var2);
         }
      };
   }

   private void connectToManagedServer(String var1) {
      String var2;
      try {
         var2 = URLManager.findAdministrationURL(var1);
      } catch (UnknownHostException var7) {
         if (debug.isDebugEnabled()) {
            debug.debug("Unable to connect to " + var1, var7);
         }

         return;
      }

      MBeanHome var3;
      try {
         Environment var4 = this.getBaseEnvironment();
         var4.setProviderUrl(var2);
         Context var5 = var4.getInitialContext();
         var3 = (MBeanHome)var5.lookup("weblogic.management.home.localhome");
      } catch (NamingException var6) {
         if (debug.isDebugEnabled()) {
            debug.debug("Unable to connect to " + var1, var6);
         }

         return;
      }

      this.adminHome.addManagedHome(var3, var1, var2);
      if (debug.isDebugEnabled()) {
         debug.debug("registered mbean home for = " + var1);
      }

   }

   private DisconnectListener createAdminServerDisconnectListener() {
      return new DisconnectListener() {
         public void onDisconnect(DisconnectEvent var1) {
            String var2 = ((ServerDisconnectEvent)var1).getServerName();
            if (CompatibilityMBeanServerService.debug.isDebugEnabled()) {
               CompatibilityMBeanServerService.debug.debug("onDisconnect event from server = " + var2);
            }

            CompatibilityMBeanServerService.this.removeMBeanHome(var2);
         }
      };
   }

   private void removeMBeanHome(String var1) {
      ((AdminMBeanHomeImpl)this.adminHome).removeManagedHome(var1);

      try {
         this.localJNDIContext.unbind("weblogic.management.home." + var1);
      } catch (NamingException var3) {
         if (debug.isDebugEnabled()) {
            debug.debug("Unable to unbind to " + var1, var3);
         }
      }

   }

   private ConnectListener createMSIConnectListener() {
      return new ConnectListener() {
         public void onConnect(ConnectEvent var1) {
            if (CompatibilityMBeanServerService.debug.isDebugEnabled()) {
               CompatibilityMBeanServerService.debug.debug("onConnect event, server = " + var1.getServerName());
            }

            RuntimeAccess var2 = ManagementService.getRuntimeAccess(CompatibilityMBeanServerService.kernelId);
            if (var1.getServerName().equals(var2.getAdminServerName())) {
               if (CompatibilityMBeanServerService.debug.isDebugEnabled()) {
                  CompatibilityMBeanServerService.debug.debug("onConnect setting admin server available.");
               }

            }
         }
      };
   }
}
