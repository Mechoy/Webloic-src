package weblogic.management.mbeanservers.runtime.internal;

import java.lang.management.ManagementFactory;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.OperationsException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.image.ImageSourceNotFoundException;
import weblogic.management.ManagementLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMXMBean;
import weblogic.management.jmx.ObjectNameManager;
import weblogic.management.jmx.mbeanserver.WLSMBeanServer;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;
import weblogic.management.jmx.modelmbean.WLSModelMBeanFactory;
import weblogic.management.mbeanservers.internal.JMXContextInterceptor;
import weblogic.management.mbeanservers.internal.MBeanServerServiceBase;
import weblogic.management.mbeanservers.internal.RuntimeMBeanAgent;
import weblogic.management.mbeanservers.internal.SecurityInterceptor;
import weblogic.management.mbeanservers.internal.WLSObjectNameManager;
import weblogic.management.mbeanservers.internal.WLSObjectSecurityManagerImpl;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.ServiceFailureException;

public class RuntimeServerService extends MBeanServerServiceBase {
   private static final String MANAGEMENT_RUNTIME_SOURCE = "ManagementRuntimeImageSource";
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXRuntime");
   RuntimeAccess runtimeAccess;
   static DiagnosticSupportService diagnosticSupportService = null;
   private ManagementRuntimeImageSource imageSource;

   static void setSupportService(DiagnosticSupportService var0) {
      diagnosticSupportService = var0;
   }

   private ObjectNameManager getObjectNameManager() {
      return (ObjectNameManager)(diagnosticSupportService != null ? diagnosticSupportService.getObjectNameManager() : new WLSObjectNameManager(ManagementService.getRuntimeAccess(kernelId).getDomainName()));
   }

   public void start() throws ServiceFailureException {
      if (!this.isEnabled()) {
         if (debug.isDebugEnabled()) {
            debug.debug("Runtime MBeanServer Disabledweblogic.management.mbeanservers.runtime");
         }

      } else {
         if (debug.isDebugEnabled()) {
            debug.debug("Starting MBeanServer weblogic.management.mbeanservers.runtime");
         }

         this.runtimeAccess = ManagementService.getRuntimeAccess(kernelId);
         if (this.isPlatformMBeanServerUsed()) {
            System.setProperty("javax.management.builder.initial", "weblogic.management.jmx.mbeanserver.WLSMBeanServerBuilder");
            if (debug.isDebugEnabled()) {
               debug.debug("Set system property javax.management.builder.initial=weblogic.management.jmx.mbeanserver.WLSMBeanServerBuilder");
            }
         }

         MBeanServer var1 = null;
         ArrayList var2 = MBeanServerFactory.findMBeanServer((String)null);
         if (var2.size() != 0 || this.isPlatformMBeanServerEnabled() || this.isPlatformMBeanServerUsed()) {
            var1 = ManagementFactory.getPlatformMBeanServer();
         }

         WLSMBeanServer var3;
         if (this.isPlatformMBeanServerUsed()) {
            if (var1 != null && !(var1 instanceof WLSMBeanServer)) {
               ManagementLogger.logPlatformMBeanServerInitFailure();
               this.initialize("weblogic.management.mbeanservers.runtime", (MBeanServer)null, (MBeanServer)null);
            } else {
               if (var1 instanceof WLSMBeanServer) {
                  var3 = (WLSMBeanServer)var1;
                  var3.setDefaultDomainName(this.runtimeAccess.getDomainName());
               }

               this.initialize("weblogic.management.mbeanservers.runtime", (MBeanServer)null, var1);
            }
         } else {
            this.initialize("weblogic.management.mbeanservers.runtime", (MBeanServer)null, (MBeanServer)null);
         }

         var3 = (WLSMBeanServer)this.getMBeanServer();
         SecurityInterceptor var4 = new SecurityInterceptor(var3, "weblogic.management.mbeanservers.runtime");
         var3.addInterceptor(var4);
         JMXContextInterceptor var5 = new JMXContextInterceptor();
         var3.addInterceptor(var5);
         new RuntimeServiceMBeanImpl(this.runtimeAccess);
         var3.setFirstAccessCallback(this.createAccessCallback());
         ManagementService.initializeRuntimeMBeanServer(kernelId, var3);
         super.start();

         try {
            this.imageSource = new ManagementRuntimeImageSource(var3);
            ImageManager.getInstance().registerImageSource("ManagementRuntimeImageSource", this.imageSource);
         } catch (Exception var7) {
            if (debug.isDebugEnabled()) {
               debug.debug("Caught exception registering Diagnostics image source for RuntimeService", var7);
            }
         }

      }
   }

   private void registerAllMBeans() {
      try {
         DomainMBean var1 = this.runtimeAccess.getDomain();
         ObjectNameManager var2 = this.getObjectNameManager();
         WLSModelMBeanContext var3 = new WLSModelMBeanContext(this.getMBeanServer(), var2, WLSObjectSecurityManagerImpl.getInstance());
         var3.setRecurse(false);
         var3.setVersion("12.0.0.0");
         var3.setReadOnly(false);
         new RuntimeMBeanAgent(var3, this.runtimeAccess);
         this.registerTypeService(var3);
         WLSModelMBeanContext var4 = new WLSModelMBeanContext(this.getMBeanServer(), var2, WLSObjectSecurityManagerImpl.getInstance());
         var4.setRecurse(true);
         var4.setVersion("12.0.0.0");
         var4.setReadOnly(true);
         WLSModelMBeanFactory.registerWLSModelMBean(var1, var4);
      } catch (OperationsException var5) {
         throw new Error(var5);
      } catch (MBeanRegistrationException var6) {
         throw new Error(var6);
      }
   }

   private WLSMBeanServer.FirstAccessCallback createAccessCallback() {
      return new WLSMBeanServer.FirstAccessCallback() {
         public void accessed(MBeanServer var1) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
               public Object run() {
                  SecurityServiceManager.runAs(RuntimeServerService.kernelId, RuntimeServerService.kernelId, new PrivilegedAction() {
                     public Object run() {
                        RuntimeServerService.this.registerAllMBeans();
                        return null;
                     }
                  });
                  return null;
               }
            });
         }
      };
   }

   private boolean isPlatformMBeanServerEnabled() {
      return ManagementService.getRuntimeAccess(kernelId).getDomain().getJMX().isPlatformMBeanServerEnabled();
   }

   private boolean isPlatformMBeanServerUsed() {
      return ManagementService.getRuntimeAccess(kernelId).getDomain().getJMX().isPlatformMBeanServerUsed();
   }

   private boolean isEnabled() {
      JMXMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().getJMX();
      return var1.isRuntimeMBeanServerEnabled() || var1.isDomainMBeanServerEnabled() || var1.isManagementEJBEnabled();
   }

   public void stop() throws ServiceFailureException {
      if (this.isEnabled()) {
         try {
            ImageManager.getInstance().unregisterImageSource("ManagementRuntimeImageSource");
         } catch (ImageSourceNotFoundException var2) {
            if (debug.isDebugEnabled()) {
               debug.debug("Caught exception unregistering RuntimeService image source:", var2);
            }
         }

         super.stop();
      }
   }
}
