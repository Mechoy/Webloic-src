package weblogic.management.deploy.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.internal.targetserver.AppDeployment;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.AccessCallback;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.UpdateException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.ArrayUtils;

public class ApplicationCompatibilityEditProcessor extends ApplicationCompatibilityProcessor implements AccessCallback, PropertyChangeListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   DomainMBean root;
   private boolean expectingPeerCreationCallback = false;

   public void shutdown() {
      this.root.removePropertyChangeListener(this);
   }

   public void accessed(DomainMBean var1) {
      this.root = var1;

      try {
         this.updateConfiguration(var1);
      } catch (UpdateException var3) {
         throw new RuntimeException(var3);
      }

      var1.addPropertyChangeListener(this);
   }

   ArrayUtils.DiffHandler createAppDeploymentHandler() {
      return new ArrayUtils.DiffHandler() {
         public void addObject(Object var1) {
            AppDeploymentMBean var2 = (AppDeploymentMBean)var1;

            try {
               ApplicationCompatibilityEditProcessor.this.createApplication(var2);
               var2.addPropertyChangeListener(ApplicationCompatibilityEditProcessor.this.createAppDeploymentListener(var2));
            } catch (DeploymentException var4) {
               DeploymentManagerLogger.logConfigureAppMBeanFailed(var2.getName());
            }

         }

         public void removeObject(Object var1) {
            AppDeploymentMBean var2 = (AppDeploymentMBean)var1;
            ApplicationMBean var3 = ApplicationCompatibilityEditProcessor.this.root.lookupApplication(var2.getName());
            if (var3 != null) {
               if (MBeanConverter.isDebugEnabled()) {
                  MBeanConverter.debug("EditProcessor: Destroy " + var3.getObjectName() + " from " + ApplicationCompatibilityEditProcessor.this.root.getObjectName());
               }

               ApplicationCompatibilityEditProcessor.this.root.destroyApplication(var3);
            }
         }
      };
   }

   private void createApplication(AppDeploymentMBean var1) throws DeploymentException {
      String var2 = null;
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         var2 = var1.getAbsoluteSourcePath();
      } else {
         var2 = AppDeployment.getFile(var1).getAbsolutePath();
      }

      if (MBeanConverter.isDebugEnabled()) {
         MBeanConverter.debug(" AppPath : " + var2);
      }

      MBeanConverter.createApplicationForAppDeployment(this.root, var1, var2);
   }

   private PropertyChangeListener createAppDeploymentListener(final AppDeploymentMBean var1) {
      return new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent var1x) {
            if (var1x.getPropertyName().equals("Targets") && TargetHelper.isTargetedLocaly(var1)) {
               try {
                  ApplicationCompatibilityEditProcessor.this.createApplication(var1);
               } catch (DeploymentException var3) {
                  throw new RuntimeException(var3);
               }
            }

         }
      };
   }

   ArrayUtils.DiffHandler createApplicationHandler() {
      return new ArrayUtils.DiffHandler() {
         public void addObject(Object var1) {
            ApplicationMBean var2 = (ApplicationMBean)var1;
            if (ApplicationCompatibilityEditProcessor.this.root.lookupAppDeployment(var2.getName()) == null) {
               ApplicationCompatibilityEditProcessor.this.root.createAppDeployment(var2.getName(), "bea_wls_nullApp");
               var2.setDelegationEnabled(true);
            }

         }

         public void removeObject(Object var1) {
            ApplicationMBean var2 = (ApplicationMBean)var1;
            if (var2 != null) {
               AppDeploymentMBean var3 = ApplicationCompatibilityEditProcessor.this.root.lookupAppDeployment(var2.getName());
               if (var3 != null) {
                  if (MBeanConverter.isDebugEnabled()) {
                     MBeanConverter.debug("EditProcessor: Destroy " + var3.getObjectName() + " from " + ApplicationCompatibilityEditProcessor.this.root.getObjectName());
                  }

                  ApplicationCompatibilityEditProcessor.this.root.destroyAppDeployment(var3);
               }

            }
         }
      };
   }

   public void propertyChange(PropertyChangeEvent var1) {
      if (var1.getPropertyName().equals("AppDeployments")) {
         AppDeploymentMBean[] var2 = (AppDeploymentMBean[])((AppDeploymentMBean[])var1.getNewValue());
         if (this.expectingPeerCreationCallback) {
            this.expectingPeerCreationCallback = false;
            return;
         }

         if (MBeanConverter.isDebugEnabled()) {
            MBeanConverter.debug("EditProcessor: AppDeployment change event");
         }

         this.expectingPeerCreationCallback = true;

         try {
            ArrayUtils.computeDiff((Object[])((Object[])var1.getOldValue()), var2, this.createAppDeploymentHandler());
         } finally {
            this.expectingPeerCreationCallback = false;
         }
      }

      if (var1.getPropertyName().equals("Applications")) {
         if (MBeanConverter.isDebugEnabled()) {
            MBeanConverter.debug("EditProcessor: Applications change event");
         }

         ApplicationMBean[] var13 = (ApplicationMBean[])((ApplicationMBean[])var1.getNewValue());
         if (this.expectingPeerCreationCallback) {
            this.expectingPeerCreationCallback = false;
            return;
         }

         for(int var3 = 0; var3 < var13.length; ++var3) {
            this.expectingPeerCreationCallback = true;

            try {
               ArrayUtils.computeDiff((Object[])((Object[])var1.getOldValue()), var13, this.createApplicationHandler());
            } finally {
               this.expectingPeerCreationCallback = false;
            }
         }
      }

   }
}
