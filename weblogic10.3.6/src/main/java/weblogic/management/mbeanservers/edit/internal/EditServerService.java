package weblogic.management.mbeanservers.edit.internal;

import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.OperationsException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.JMXMBean;
import weblogic.management.jmx.mbeanserver.WLSMBeanServer;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;
import weblogic.management.jmx.modelmbean.WLSModelMBeanFactory;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.mbeanservers.edit.EditServiceMBean;
import weblogic.management.mbeanservers.edit.RecordingManagerMBean;
import weblogic.management.mbeanservers.internal.JMXContextInterceptor;
import weblogic.management.mbeanservers.internal.MBeanServerServiceBase;
import weblogic.management.mbeanservers.internal.SecurityInterceptor;
import weblogic.management.mbeanservers.internal.SecurityMBeanMgmtOpsInterceptor;
import weblogic.management.mbeanservers.internal.WLSObjectNameManager;
import weblogic.management.mbeanservers.internal.WLSObjectSecurityManagerImpl;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.ServiceFailureException;

public class EditServerService extends MBeanServerServiceBase {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXEdit");
   private static final String JNDI = "/jndi/";
   private WLSModelMBeanContext context;
   private static EditServerService singleton;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public EditServerService() {
      singleton = this;
   }

   public void start() throws ServiceFailureException {
      if (this.isEnabled()) {
         if (debug.isDebugEnabled()) {
            debug.debug("Starting MBeanServer weblogic.management.mbeanservers.edit");
         }

         this.initialize("weblogic.management.mbeanservers.edit", (MBeanServer)null);
         WLSMBeanServer var1 = (WLSMBeanServer)this.getMBeanServer();
         SecurityInterceptor var2 = new SecurityInterceptor(var1);
         var1.addInterceptor(var2);
         EditLockInterceptor var3 = new EditLockInterceptor(var1);
         var1.addInterceptor(var3);
         SecurityMBeanMgmtOpsInterceptor var4 = new SecurityMBeanMgmtOpsInterceptor(1);
         var1.addInterceptor(var4);
         RecordingInterceptor var5 = new RecordingInterceptor();
         var1.addInterceptor(var5);
         JMXContextInterceptor var6 = new JMXContextInterceptor();
         var1.addInterceptor(var6);
         var1.setFirstAccessCallback(this.createAccessCallback());
         super.start();
         if (debug.isDebugEnabled()) {
            debug.debug("Starting MBeanServer weblogic.management.mbeanservers.edit");
         }

      }
   }

   private WLSMBeanServer.FirstAccessCallback createAccessCallback() {
      return new WLSMBeanServer.FirstAccessCallback() {
         public void accessed(MBeanServer var1) {
            SecurityServiceManager.runAs(EditServerService.kernelId, EditServerService.kernelId, new PrivilegedAction() {
               public Object run() {
                  EditServerService.this.registerAllMBeans();
                  return null;
               }
            });
         }
      };
   }

   private void registerAllMBeans() {
      try {
         EditAccess var1 = ManagementServiceRestricted.getEditAccess(kernelId);
         RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
         WLSObjectNameManager var3 = new WLSObjectNameManager(var2.getDomainName());
         WLSMBeanServer var4 = (WLSMBeanServer)this.getMBeanServer();
         this.context = new WLSModelMBeanContext(var4, var3, WLSObjectSecurityManagerImpl.getInstance());
         this.context.setRecurse(false);
         this.context.setVersion("12.0.0.0");
         this.context.setReadOnly(false);
         EditServiceMBeanImpl var5 = new EditServiceMBeanImpl(var1, this.context);
         this.registerTypeService(this.context);
         WLSModelMBeanFactory.registerWLSModelMBean(var5, new ObjectName(EditServiceMBean.OBJECT_NAME), this.context);
         WLSModelMBeanFactory.registerWLSModelMBean(var5.getConfigurationManager(), new ObjectName(ConfigurationManagerMBean.OBJECT_NAME), this.context);
         WLSModelMBeanFactory.registerWLSModelMBean(var5.getRecordingManager(), new ObjectName(RecordingManagerMBean.OBJECT_NAME), this.context);
         this.context.setRecurse(true);
         WLSModelMBeanFactory.registerWLSModelMBean(var5.getDomainConfiguration(), this.context);
      } catch (InstanceAlreadyExistsException var6) {
      } catch (OperationsException var7) {
         if (debug.isDebugEnabled()) {
            debug.debug("Operations exception starting MBeanServer ", var7);
         }

         throw new Error("Unable to register Edit Access ", var7);
      } catch (MBeanRegistrationException var8) {
         if (debug.isDebugEnabled()) {
            debug.debug("MBean registration exception starting MBeanServer ", var8);
         }

         throw new Error("Unable to register Edit Access ", var8);
      }

   }

   private boolean isEnabled() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      if (!var1.isAdminServer()) {
         return false;
      } else {
         JMXMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain().getJMX();
         return var2.isEditMBeanServerEnabled();
      }
   }

   public void stop() throws ServiceFailureException {
      if (this.isEnabled()) {
         if (debug.isDebugEnabled()) {
            debug.debug("Stopping MBeanServer weblogic.management.mbeanservers.edit");
         }

         super.stop();
      }
   }

   public static WLSModelMBeanContext getContext() {
      return singleton.context;
   }
}
