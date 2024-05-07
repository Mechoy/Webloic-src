package weblogic.application.internal;

import java.io.File;
import java.security.AccessController;
import java.util.Iterator;
import javax.management.InvalidAttributeValueException;
import weblogic.application.ApplicationFactoryManager;
import weblogic.application.ComponentMBeanFactory;
import weblogic.application.MBeanFactory;
import weblogic.management.ApplicationException;
import weblogic.management.DeploymentException;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.internal.MBeanConverter;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class MBeanFactoryImpl extends MBeanFactory {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private ApplicationFactoryManager afm = ApplicationFactoryManager.getApplicationFactoryManager();

   private ComponentMBeanFactory findOrCreateComponentMBeans(ApplicationMBean var1, File var2, AppDeploymentMBean var3) throws DeploymentException {
      Iterator var4 = this.afm.getComponentMBeanFactories();

      ComponentMBeanFactory var5;
      ComponentMBean[] var6;
      do {
         if (!var4.hasNext()) {
            return ComponentMBeanFactory.DEFAULT_FACTORY;
         }

         var5 = (ComponentMBeanFactory)var4.next();
         var6 = var5.findOrCreateComponentMBeans(var1, var2, var3);
      } while(var6 == null);

      return var5;
   }

   public ApplicationMBean initializeMBeans(DomainMBean var1, File var2, String var3, String var4, String var5, AppDeploymentMBean var6) throws ApplicationException {
      ApplicationMBean var7 = null;
      boolean var8 = false;

      ApplicationMBean var10;
      try {
         var7 = this.createApplicationMBean(var1, var3, var4, var5);
         ComponentMBeanFactory var9 = this.createComponentMBeans(var2, var7, var6);
         this.initializeAppMBeanPath(var9, var7, var2);
         var8 = true;
         var10 = var7;
      } catch (ManagementException var15) {
         throw new ApplicationException(var15);
      } catch (InvalidAttributeValueException var16) {
         throw new AssertionError(var16);
      } finally {
         if (!var8) {
            this.cleanupMBeans(var1, var7);
         } else if (var7 != null) {
            var7.setDelegationEnabled(true);
         }

      }

      return var10;
   }

   private ComponentMBeanFactory createComponentMBeans(File var1, ApplicationMBean var2, AppDeploymentMBean var3) throws DeploymentException {
      File var4 = var1;
      if (!var1.isAbsolute()) {
         var4 = new File(this.resolveWithRootDirectory(var1.getPath()));
      }

      return this.findOrCreateComponentMBeans(var2, var4, var3);
   }

   private void initializeAppMBeanPath(ComponentMBeanFactory var1, ApplicationMBean var2, File var3) throws ManagementException, InvalidAttributeValueException {
      if (var1.needsApplicationPathMunging()) {
         var2.setPath(var3.getParent());
      } else {
         var2.setPath(var3.getPath());
      }

   }

   private ApplicationMBean createApplicationMBean(DomainMBean var1, String var2, String var3, String var4) {
      ApplicationMBean var5 = var1.lookupApplication(var2);
      if (var5 == null) {
         var5 = var1.createApplication(var2);
      }

      var5.setAltDescriptorPath(var3);
      var5.setAltWLSDescriptorPath(var4);
      return var5;
   }

   public void reconcileMBeans(AppDeploymentMBean var1, File var2) throws ApplicationException {
      try {
         this.findOrCreateComponentMBeans(var1.getAppMBean(), var2, var1);
      } catch (ManagementException var4) {
         throw new ApplicationException(var4);
      }
   }

   public void cleanupMBeans(DomainMBean var1, ApplicationMBean var2) {
      if (var2 != null) {
         AppDeploymentMBean var3 = AppDeploymentHelper.lookupAppOrLib(var2.getName(), var1);
         if (var3 != null) {
            DomainMBean var4 = ManagementService.getRuntimeAccess(kernelId).getDomain();
            if (var1 != var4) {
               MBeanConverter.debug("MBeanFactoryImpl: Destroy " + var3.getName() + " from " + var1.getName());
               AppDeploymentHelper.destroyAppOrLib(var3, var1);
            }
         }

         MBeanConverter.debug("MBeanFactoryImpl: Destroy " + var2.getName() + " from " + var1.getName());
         var1.destroyApplication(var2);
      }

   }

   private String resolveWithRootDirectory(String var1) {
      return DomainDir.getRootDir() + File.separatorChar + var1;
   }
}
