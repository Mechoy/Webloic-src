package weblogic.jms.deployer;

import java.io.IOException;
import java.security.AccessController;
import java.util.HashMap;
import javax.jms.JMSException;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BEUOWCallbackFactory;
import weblogic.jms.backend.BackEnd;
import weblogic.jms.backend.udd.UDDEntity;
import weblogic.jms.common.JMSDebug;
import weblogic.logging.jms.JMSMessageLogger;
import weblogic.logging.jms.JMSMessageLoggerFactory;
import weblogic.management.DeploymentException;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.JMSMessageLogFileMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSSessionPoolMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.GenericAdminHandler;
import weblogic.management.utils.GenericBeanListener;
import weblogic.messaging.kernel.internal.UOWSequenceImpl;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentStoreManager;
import weblogic.store.admin.FileAdminHandler;
import weblogic.store.xa.PersistentStoreXA;

public final class BEAdminHandler implements GenericAdminHandler {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final HashMap backendBeanSignatures = new HashMap();
   private static final HashMap backendAdditionSignatures = new HashMap();
   private BackEnd backEnd;
   private GenericBeanListener changeListener;

   public boolean isMigrationInProgress() {
      return JMSService.getJMSService().isMigrationInProgress();
   }

   public void setMigrationInProgress(boolean var1) {
      JMSService.getJMSService().setMigrationInProgress(var1);
   }

   public void prepare(DeploymentMBean var1) throws DeploymentException {
      try {
         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("Adding JMSServer: " + var1.getName());
         }

         if (var1.getTargets().length > 1) {
            JMSLogger.logErrorBEMultiDeployed(var1.getName());
            throw new DeploymentException("The JMS Server " + var1.getName() + " may only be deployed on one server");
         }

         JMSServerMBean var2 = (JMSServerMBean)var1;
         SecurityServiceManager.pushSubject(KERNEL_ID, KERNEL_ID);

         try {
            this.backEnd = new BackEnd(var2.getName(), "JMSServer");
         } finally {
            SecurityServiceManager.popSubject(KERNEL_ID);
         }

         this.changeListener = new GenericBeanListener(var2, this.backEnd, backendBeanSignatures, backendAdditionSignatures);
         this.changeListener.setCustomizer(this.backEnd);
         this.changeListener.initialize();
         this.backEnd.setSessionPoolMBeans(var2.getJMSSessionPools());
         JMSService.getJMSService().getBEDeployer().addBackEnd(this.backEnd);
         synchronized(this) {
            UDDEntity.prepareLocalJMSServer((JMSServerMBean)var1);

            try {
               JMSService.getJMSService().startAddJMSServers(var2);
            } catch (BeanUpdateRejectedException var12) {
               throw new DeploymentException(var12);
            }
         }

         if (var2.isBytesPagingEnabled() || var2.isMessagesPagingEnabled()) {
            JMSLogger.logServerPagingParametersDeprecated(var1.getName());
         }
      } catch (JMSException var15) {
         JMSLogger.logErrorCreateBE(var1.getName(), var15);
         throw new DeploymentException("Error preparing the JMS Server " + var1.getName() + ": " + var15.toString(), var15);
      } catch (ManagementException var16) {
         JMSLogger.logErrorCreateBE(var1.getName(), var16);
         throw new DeploymentException("Error preparing the JMS Server " + var1.getName() + ": " + var16.toString(), var16);
      } catch (Throwable var17) {
         JMSLogger.logErrorCreateBE(var1.getName(), var17);
         throw new DeploymentException("Internal error preparing the JMS server " + var1.getName() + ": " + var17.toString(), var17);
      }

      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("Successfully prepared JMSServer: " + var1.getName());
      }

   }

   public void activate(DeploymentMBean var1) throws DeploymentException {
      JMSServerMBean var2 = (JMSServerMBean)var1;
      DeploymentException var3 = null;
      if (this.changeListener != null) {
         this.changeListener.close();
         this.changeListener = null;
      }

      this.changeListener = new GenericBeanListener(var2, this.backEnd, backendBeanSignatures, backendAdditionSignatures);
      this.changeListener.setCustomizer(this.backEnd);
      if (this.backEnd == null) {
         JMSLogger.logErrorDeployingBE(var1.getName(), "activate", "prepare");
         throw new DeploymentException("Cannot activate the JMS Server " + var1.getName() + " because it was not prepared");
      } else {
         try {
            this.backEnd.setPersistentStore(findPersistentStore(var2));
            this.backEnd.setPagingDirectory(findPagingDirectory(var2));
            this.backEnd.setPagingFileLockingEnabled(var2.isPagingFileLockingEnabled());
            this.backEnd.setJMSMessageLogger(findJMSMessageLogger(var2));
            this.backEnd.open();
         } catch (JMSException var7) {
            JMSLogger.logErrorStartBE(var1.getName(), var7);
            var3 = new DeploymentException("Error activating the JMS Server " + var1.getName() + ": " + var7.toString(), var7);
         } catch (Throwable var8) {
            JMSLogger.logErrorStartBE(var1.getName(), var8);
            var3 = new DeploymentException("Internal error activating the JMS Server " + var1.getName() + ": " + var8.toString(), var8);
         }

         if (var3 != null) {
            this.backEnd.setHealthFailed(var3);
            throw var3;
         } else {
            if (JMSDebug.JMSConfig.isDebugEnabled()) {
               JMSDebug.JMSConfig.debug("Successfully activated JMSServer: " + var1.getName());
            }

            synchronized(this) {
               UDDEntity.activateLocalJMSServer((JMSServerMBean)var1);
               JMSService.getJMSService().finishAddJMSServers((JMSServerMBean)var1, true);
            }
         }
      }
   }

   public void deactivate(DeploymentMBean var1) throws UndeploymentException {
      if (this.backEnd == null) {
         JMSLogger.logErrorDeployingBE(var1.getName(), "deactivate", "activate");
         throw new UndeploymentException("Cannot deactivate the JMS Server " + var1.getName() + " because it was not activated");
      } else {
         this.backEnd.close();
         synchronized(this) {
            UDDEntity.deactivateLocalJMSServer((JMSServerMBean)var1);

            try {
               JMSService.getJMSService().startRemoveJMSServers((JMSServerMBean)var1);
            } catch (BeanUpdateRejectedException var5) {
               throw new UndeploymentException(var5);
            }
         }

         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("Successffuly deactivated JMSServer: " + var1.getName());
         }

      }
   }

   public void unprepare(DeploymentMBean var1) throws UndeploymentException {
      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("Removing JMSServer: " + var1.getName());
      }

      if (this.backEnd == null) {
         JMSLogger.logErrorDeployingBE(var1.getName(), "unprepare", "deactivate");
         throw new UndeploymentException("Cannot unprepare the JMS Server " + var1.getName() + " because it was not deactivated");
      } else {
         this.backEnd.destroy();
         if (this.changeListener != null) {
            this.changeListener.close();
         }

         JMSService.getJMSService().getBEDeployer().removeBackEnd(this.backEnd);
         synchronized(this) {
            UDDEntity.unprepareLocalJMSServer((JMSServerMBean)var1);
            JMSService.getJMSService().finishRemoveJMSServers((JMSServerMBean)var1, true);
         }

         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("Successfuly removed JMSServer: " + var1.getName());
         }

      }
   }

   private static PersistentStoreXA findPersistentStore(JMSServerMBean var0) throws DeploymentException {
      if (!var0.getStoreEnabled()) {
         return null;
      } else if (var0.getPersistentStore() != null) {
         String var3 = var0.getPersistentStore().getName();
         PersistentStoreXA var2 = (PersistentStoreXA)PersistentStoreManager.getManager().getStore(var3);
         if (var2 == null) {
            throw new DeploymentException("The persistent store \"" + var3 + "\" does not exist");
         } else {
            if (JMSDebug.JMSBoot.isDebugEnabled()) {
               JMSDebug.JMSBoot.debug("JMSServer using the 9.0 store " + var3);
            }

            return var2;
         }
      } else {
         PersistentStoreXA var1 = (PersistentStoreXA)PersistentStoreManager.getManager().getDefaultStore();
         if (var1 == null) {
            throw new DeploymentException("The default persistent store does not exist");
         } else {
            if (JMSDebug.JMSBoot.isDebugEnabled()) {
               JMSDebug.JMSBoot.debug("JMSServer using the default store");
            }

            return var1;
         }
      }
   }

   private static String findPagingDirectory(JMSServerMBean var0) {
      String var1;
      if (var0.getPagingDirectory() != null) {
         var1 = var0.getPagingDirectory();
         var1 = FileAdminHandler.canonicalizeDirectoryName(var1);
      } else {
         String var2 = ManagementService.getRuntimeAccess(KERNEL_ID).getServer().getName();
         var1 = DomainDir.getTempDirForServer(var2);
      }

      return var1;
   }

   private static JMSMessageLogger findJMSMessageLogger(JMSServerMBean var0) throws DeploymentException {
      try {
         JMSMessageLogFileMBean var1 = var0.getJMSMessageLogFile();
         return JMSMessageLoggerFactory.findOrCreateJMSMessageLogger(var1);
      } catch (IOException var2) {
         throw new DeploymentException("Cannot find or create JMS message log file for JMS server " + var0.getName(), var2);
      }
   }

   static {
      backendBeanSignatures.put("BytesMaximum", Long.TYPE);
      backendBeanSignatures.put("BytesThresholdHigh", Long.TYPE);
      backendBeanSignatures.put("BytesThresholdLow", Long.TYPE);
      backendBeanSignatures.put("MessagesMaximum", Long.TYPE);
      backendBeanSignatures.put("MessagesThresholdHigh", Long.TYPE);
      backendBeanSignatures.put("MessagesThresholdLow", Long.TYPE);
      backendBeanSignatures.put("BlockingSendPolicy", String.class);
      backendBeanSignatures.put("ExpirationScanInterval", Integer.TYPE);
      backendBeanSignatures.put("MaximumMessageSize", Integer.TYPE);
      backendBeanSignatures.put("MessageBufferSize", Long.TYPE);
      backendBeanSignatures.put("PagingDirectory", String.class);
      backendBeanSignatures.put("PagingFileLockingEnabled", Boolean.TYPE);
      backendBeanSignatures.put("PagingMinWindowBufferSize", Integer.TYPE);
      backendBeanSignatures.put("PagingMaxWindowBufferSize", Integer.TYPE);
      backendBeanSignatures.put("PagingIoBufferSize", Integer.TYPE);
      backendBeanSignatures.put("PagingBlockSize", Integer.TYPE);
      backendBeanSignatures.put("PagingMaxFileSize", Long.TYPE);
      backendBeanSignatures.put("HostingTemporaryDestinations", Boolean.TYPE);
      backendBeanSignatures.put("TemporaryTemplateName", String.class);
      backendBeanSignatures.put("TemporaryTemplateResource", String.class);
      backendBeanSignatures.put("ProductionPausedAtStartup", String.class);
      backendBeanSignatures.put("InsertionPausedAtStartup", String.class);
      backendBeanSignatures.put("ConsumptionPausedAtStartup", String.class);
      backendBeanSignatures.put("AllowsPersistentDowngrade", Boolean.TYPE);
      backendAdditionSignatures.put("JMSSessionPools", JMSSessionPoolMBean.class);
      UOWSequenceImpl.setCallbackFactory(new BEUOWCallbackFactory());
   }
}
