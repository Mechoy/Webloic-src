package weblogic.management.mbeanservers.domainruntime.internal;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;
import javax.management.OperationsException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMXMBean;
import weblogic.management.jmx.mbeanserver.WLSMBeanServer;
import weblogic.management.jmx.mbeanserver.WLSMBeanServerBuilder;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;
import weblogic.management.jmx.modelmbean.WLSModelMBeanFactory;
import weblogic.management.mbeanservers.internal.JMXContextInterceptor;
import weblogic.management.mbeanservers.internal.MBeanServerServiceBase;
import weblogic.management.mbeanservers.internal.RuntimeMBeanAgent;
import weblogic.management.mbeanservers.internal.SecurityInterceptor;
import weblogic.management.mbeanservers.internal.SecurityMBeanMgmtOpsInterceptor;
import weblogic.management.mbeanservers.internal.WLSObjectSecurityManagerImpl;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.EditFailedException;
import weblogic.management.provider.EditNotEditorException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.ServiceFailureException;

public class DomainRuntimeServerService extends MBeanServerServiceBase {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXDomain");
   private MBeanServerConnectionManager connectionManager = null;
   private FederatedObjectNameManager objectNameManager = null;
   private DomainAccess domainAccess;
   private DomainRuntimeMBean domainRuntime;
   WLSModelMBeanContext context = null;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public String getName() {
      return "DomainRuntimeService";
   }

   public String getVersion() {
      return null;
   }

   public void start() throws ServiceFailureException {
      boolean var1 = this.isEnabled();
      if (debug.isDebugEnabled()) {
         debug.debug("DomainRuntimeServerService start: isEnabled(" + var1 + ") jndiName(" + "weblogic.management.mbeanservers.domainruntime" + ")");
      }

      if (var1) {
         RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
         WLSMBeanServerBuilder var3 = new WLSMBeanServerBuilder();
         this.connectionManager = new MBeanServerConnectionManager();
         MBeanServerDelegate var4 = var3.newMBeanServerDelegate();
         FederatedMBeanServerDelegate var5 = new FederatedMBeanServerDelegate(var4, this.connectionManager);
         String var6 = var2.getDomainName();
         FederatedMBeanServerInterceptor var7 = new FederatedMBeanServerInterceptor(this.connectionManager, var6);
         WLSMBeanServer var8 = (WLSMBeanServer)var3.newMBeanServer(var6, (MBeanServer)null, var5);
         this.objectNameManager = new FederatedObjectNameManager(this.connectionManager, var6);
         SecurityInterceptor var9 = new SecurityInterceptor(var8, "weblogic.management.mbeanservers.domainruntime");
         var8.addInterceptor(var9);
         SecurityMBeanMgmtOpsInterceptor var10 = new SecurityMBeanMgmtOpsInterceptor(2);
         var8.addInterceptor(var10);
         JMXContextInterceptor var11 = new JMXContextInterceptor(var6);
         var8.addInterceptor(var11);
         var8.addInterceptor(var7);
         this.context = new WLSModelMBeanContext(var8, this.objectNameManager, WLSObjectSecurityManagerImpl.getInstance());
         this.context.setVersion("12.0.0.0");
         this.initialize("weblogic.management.mbeanservers.domainruntime", var8);
         var2 = ManagementService.getRuntimeAccess(kernelId);
         this.domainAccess = ManagementService.getDomainAccess(kernelId);
         this.domainRuntime = this.domainAccess.getDomainRuntime();
         this.domainAccess.setDomainRuntimeService(new DomainRuntimeServiceMBeanImpl(this.connectionManager, this.domainRuntime));
         var8.setFirstAccessCallback(this.createAccessCallback());
         ManagementService.initializeDomainRuntimeMBeanServer(kernelId, var8);
         if (!debug.isDebugEnabled()) {
            Logger.getLogger("javax.management.remote.misc").setLevel(Level.OFF);
            Logger.getLogger("javax.management.remote.rmi").setLevel(Level.OFF);
         }

         super.start();
         if (debug.isDebugEnabled()) {
            debug.debug("DomainRuntimeService start: completed - weblogic.management.mbeanservers.domainruntime");
         }

      }
   }

   private void registerAllMBeans() {
      if (debug.isDebugEnabled()) {
         debug.debug("DomainRuntimeServerService.registerAllMBeans - starting ");
      }

      try {
         WLSModelMBeanContext var1 = new WLSModelMBeanContext(this.getMBeanServer(), this.objectNameManager, WLSObjectSecurityManagerImpl.getInstance());
         var1.setRecurse(false);
         var1.setVersion("12.0.0.0");
         var1.setReadOnly(false);
         new RuntimeMBeanAgent(var1, this.domainAccess);
         this.registerTypeService(this.context);
         WLSModelMBeanContext var2 = new WLSModelMBeanContext(this.getMBeanServer(), this.objectNameManager, WLSObjectSecurityManagerImpl.getInstance());
         var2.setRecurse(true);
         var2.setVersion("12.0.0.0");
         var2.setReadOnly(true);
         WLSModelMBeanFactory.registerWLSModelMBean(DomainRuntimeServerService.SINGLETON.getInstance().getDomainConfig(), var2);
         WLSModelMBeanContext var3 = new WLSModelMBeanContext(this.getMBeanServer(), this.objectNameManager, WLSObjectSecurityManagerImpl.getInstance());
         var3.setRecurse(true);
         var3.setVersion("12.0.0.0");
         var3.setReadOnly(true);
         var3.setFilteringEnabled(true);
         WLSModelMBeanFactory.registerWLSModelMBean(DomainRuntimeServerService.SINGLETON.getInstance().getDomainEdit(), var3);
         this.connectionManager.initializeConnectivity();
      } catch (InstanceAlreadyExistsException var4) {
      } catch (OperationsException var5) {
         throw new Error("Unable to register Federated Domain Runtime Access ", var5);
      } catch (MBeanRegistrationException var6) {
         throw new Error("Unable to register Federated Domain Runtime Access ", var6);
      }

      if (debug.isDebugEnabled()) {
         debug.debug("DomainRuntimeServerService.registerAllMBeans - completed ");
      }

   }

   private WLSMBeanServer.FirstAccessCallback createAccessCallback() {
      return new WLSMBeanServer.FirstAccessCallback() {
         public void accessed(MBeanServer var1) {
            SecurityServiceManager.runAs(DomainRuntimeServerService.kernelId, DomainRuntimeServerService.kernelId, new PrivilegedAction() {
               public Object run() {
                  DomainRuntimeServerService.this.registerAllMBeans();
                  return null;
               }
            });
         }
      };
   }

   private boolean isEnabled() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      if (!var1.isAdminServer()) {
         return false;
      } else {
         JMXMBean var2 = var1.getDomain().getJMX();
         return var2.isDomainMBeanServerEnabled() || var2.isManagementEJBEnabled();
      }
   }

   public void stop() throws ServiceFailureException {
      boolean var1 = this.isEnabled();
      if (debug.isDebugEnabled()) {
         debug.debug("DomainRuntimeServerService stop: isEnabled(" + var1 + ") jndiName(" + "weblogic.management.mbeanservers.domainruntime" + ")");
      }

      if (var1) {
         this.connectionManager.stop();
         super.stop();
         if (debug.isDebugEnabled()) {
            debug.debug("DomainRuntimeService stop: completed - weblogic.management.mbeanservers.domainruntime");
         }

      }
   }

   private static class SINGLETON {
      private DomainMBean domainConfig = null;
      private DomainMBean domainEdit = null;
      private static SINGLETON instance = null;

      public static SINGLETON getInstance() {
         if (instance == null) {
            instance = new SINGLETON();
         }

         return instance;
      }

      private SINGLETON() {
         EditAccess var1 = ManagementServiceRestricted.getEditAccess(DomainRuntimeServerService.kernelId);

         try {
            this.domainConfig = var1.getCurrentDomainBean();
            this.domainEdit = var1.getDomainBeanWithoutLock();
         } catch (EditFailedException var3) {
            throw new AssertionError(var3);
         } catch (EditNotEditorException var4) {
            throw new AssertionError(var4);
         }
      }

      public DomainMBean getDomainConfig() {
         return this.domainConfig;
      }

      public DomainMBean getDomainEdit() {
         return this.domainEdit;
      }
   }
}
