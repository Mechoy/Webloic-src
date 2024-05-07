package weblogic.management.deploy.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessController;
import java.util.Locale;
import javax.management.InvalidAttributeValueException;
import weblogic.application.DeploymentManager;
import weblogic.application.MBeanFactory;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.internal.targetserver.AppDeployment;
import weblogic.management.ApplicationException;
import weblogic.management.DeploymentException;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.ArrayUtils;
import weblogic.utils.NestedException;

public class MBeanConverter {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static ApplicationMBean createApplicationForAppDeployment(DomainMBean var0, AppDeploymentMBean var1, String var2) throws DeploymentException {
      if (!ManagementService.getRuntimeAccess(kernelId).isAdminServer() && !TargetHelper.isTargetedLocaly(var1) && !TargetHelper.isPinnedToServerInCluster(var1)) {
         return null;
      } else {
         ApplicationMBean var3 = var0.lookupApplication(var1.getName());

         try {
            if (var3 != null) {
               return var3;
            }

            if (isDebugEnabled()) {
               debug("Creating appmeans for " + var1.getObjectName() + " In " + var0.getObjectName() + " from " + var2);
            }

            if (var1.getSourcePath().toLowerCase(Locale.US).endsWith(".xml")) {
               var3 = createTEMPCompatMBean(var1, var0, var2);
            } else {
               var3 = createApplicationMBean(var1, var0, var2);
               setApplicationTargets(var1, var3);
            }

            var3.setLoadOrder(var1.getDeploymentOrder());
            var3.setDelegationEnabled(true);
         } catch (Throwable var5) {
            handleException(var1, var5);
         }

         return var3;
      }
   }

   private static ApplicationMBean createApplicationMBean(AppDeploymentMBean var0, DomainMBean var1, String var2) throws InvalidAttributeValueException, IOException, ApplicationException {
      File var4 = BootStrap.apply(var2);
      if (!var4.isAbsolute()) {
         var4 = var4.getCanonicalFile();
      }

      ApplicationMBean var3 = MBeanFactory.getMBeanFactory().initializeMBeans(var1, var4, var0.getName(), (String)null, (String)null, var0);
      return var3;
   }

   private static void setApplicationTargets(AppDeploymentMBean var0, ApplicationMBean var1) throws DistributedManagementException, InvalidAttributeValueException {
      ComponentMBean[] var2 = var1.getComponents();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         ComponentMBean var4 = var2[var3];
         var4.setTargets(getTargetsForComponent(var0, var4));
      }

   }

   public static TargetMBean[] getTargetsForComponent(AppDeploymentMBean var0, ComponentMBean var1) {
      SubDeploymentMBean var2 = findSubDeployment(var0, var1);
      return var2 != null && var2.getTargets() != null ? var2.getTargets() : var0.getTargets();
   }

   public static void addTargetFromComponent(AppDeploymentMBean var0, ComponentMBean var1, TargetMBean var2) {
      SubDeploymentMBean var3 = findOrCreateSubDeployment(var0, var1);

      try {
         var3.addTarget(var2);
      } catch (InvalidAttributeValueException var5) {
         throw new AssertionError(var5);
      } catch (DistributedManagementException var6) {
         throw new AssertionError(var6);
      }
   }

   public static void removeTargetFromComponent(AppDeploymentMBean var0, ComponentMBean var1, TargetMBean var2) {
      SubDeploymentMBean var3 = findOrCreateSubDeployment(var0, var1);

      try {
         var3.removeTarget(var2);
      } catch (InvalidAttributeValueException var5) {
         throw new AssertionError(var5);
      } catch (DistributedManagementException var6) {
         throw new AssertionError(var6);
      }
   }

   private static SubDeploymentMBean findSubDeployment(AppDeploymentMBean var0, ComponentMBean var1) {
      SubDeploymentMBean var2 = var0.lookupSubDeployment(var1.getName());
      if (var2 == null) {
         var2 = var0.lookupSubDeployment(var1.getURI());
      }

      return var2;
   }

   private static SubDeploymentMBean findOrCreateSubDeployment(AppDeploymentMBean var0, ComponentMBean var1) {
      SubDeploymentMBean var2 = findSubDeployment(var0, var1);
      if (var2 == null) {
         var2 = var0.createSubDeployment(var1.getName());
      }

      return var2;
   }

   private static void handleException(AppDeploymentMBean var0, Throwable var1) throws DeploymentException {
      DeploymentException var2 = null;
      String var3 = DeploymentManagerLogger.logConfigureAppMBeanFailedLoggable(var0.getName()).getMessage();
      if (var1 instanceof DeploymentException) {
         var2 = (DeploymentException)var1;
      } else if (var1 instanceof NestedException) {
         Throwable var4 = ((NestedException)var1).getNestedException();
         if (var4 == null) {
            var2 = new DeploymentException(var3, var1);
         } else if (var4 instanceof DeploymentException) {
            var2 = (DeploymentException)var4;
         } else {
            var2 = new DeploymentException(var3, var4);
         }
      } else {
         var2 = new DeploymentException(var3, var1);
      }

      DeploymentManagerLogger.logConversionToAppMBeanFailed(var0.getName(), var2);
      if (isDebugEnabled()) {
         var2.printStackTrace();
      }

      throw var2;
   }

   public static void setTargetsForComponent(AppDeploymentMBean var0, ComponentMBean var1, TargetMBean[] var2) {
   }

   private static ApplicationMBean createTEMPCompatMBean(AppDeploymentMBean var0, DomainMBean var1, String var2) throws InvalidAttributeValueException, ManagementException, FileNotFoundException {
      File var4 = BootStrap.apply(var2);
      if (!var4.exists()) {
         throw new FileNotFoundException(var4 + " not found.");
      } else {
         ApplicationMBean var3 = var1.lookupApplication(var0.getName());
         if (var3 == null) {
            var3 = var1.createApplication(var0.getName());
            var3.setAppDeployment(var0);
            var3.setPath(var4.getParent());
            var3.setDelegationEnabled(true);
         }

         ComponentMBean[] var5 = var3.getComponents();
         if (var5 == null || var5.length == 0) {
            ComponentMBean var6 = createComponent(var3, var4.getName());
            var6.setTargets(var0.getTargets());
            var6.setURI(var4.getName());
         }

         return var3;
      }
   }

   private static ComponentMBean createComponent(ApplicationMBean var0, String var1) throws ManagementException {
      Object var2;
      if (var1.toLowerCase(Locale.US).endsWith("-jms.xml")) {
         var2 = var0.createDummyComponent(var0.getName());
      } else if (var1.toLowerCase(Locale.US).endsWith("-jdbc.xml")) {
         var2 = var0.createJDBCPoolComponent(var0.getName());
      } else {
         if (!var1.toLowerCase(Locale.US).endsWith("-interception.xml")) {
            String var3 = DeploymentManagerLogger.logUnknownDeployable(var1);
            throw new DeploymentException(var3);
         }

         var2 = var0.createDummyComponent(var0.getName());
      }

      return (ComponentMBean)var2;
   }

   public static void debug(String var0) {
      Debug.deploymentDebug(var0);
   }

   public static boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   public static void reconcile81MBeans(DeploymentData var0, AppDeploymentMBean var1) throws DeploymentException {
      if (var1.getAppMBean() == null) {
         setupNew81MBean(var1);
      } else {
         if (isDebugEnabled()) {
            debug("Reconcile appmbean for" + var1.getObjectName());
         }

         String[] var2 = var0.getFiles();

         try {
            DeploymentManager.getDeploymentManager().getMBeanFactory().reconcileMBeans(var1, AppDeployment.getFile(var1));
         } catch (FileNotFoundException var4) {
            throw new DeploymentException(var4);
         } catch (ApplicationException var5) {
            throw new DeploymentException(var5);
         }
      }

   }

   public static void setupNew81MBean(AppDeploymentMBean var0) throws DeploymentException {
      DomainMBean var1 = (DomainMBean)var0.getParent();
      if (!var0.getSourcePath().toLowerCase(Locale.US).endsWith("-jms.xml")) {
         ApplicationMBean var2 = createApplicationForAppDeployment(var1, var0, AppDeployment.getFile(var0).getPath());
         if (var2 != null) {
            var2.setAppDeployment(var0);
         }

      }
   }

   public static void remove81MBean(AppDeploymentMBean var0) {
      DomainMBean var1 = (DomainMBean)var0.getParent();
      ApplicationMBean var2 = var1.lookupApplication(var0.getName());
      if (var2 != null) {
         var1.destroyApplication(var2);
      }

   }

   public static void addStagedTarget(AppDeploymentMBean var0, String var1) {
      ApplicationMBean var2 = var0.getAppMBean();
      if (var2 != null) {
         var2.addStagedTarget(var1);
      }

   }

   private static class SubDTargetDiffHandler implements ArrayUtils.DiffHandler {
      private final SubDeploymentMBean subDeployment;

      public SubDTargetDiffHandler(SubDeploymentMBean var1) {
         this.subDeployment = var1;
      }

      public void addObject(Object var1) {
         try {
            this.subDeployment.addTarget((TargetMBean)var1);
         } catch (InvalidAttributeValueException var3) {
            throw new AssertionError(var3);
         } catch (DistributedManagementException var4) {
            throw new AssertionError(var4);
         }
      }

      public void removeObject(Object var1) {
         try {
            this.subDeployment.removeTarget((TargetMBean)var1);
         } catch (InvalidAttributeValueException var3) {
            throw new AssertionError(var3);
         } catch (DistributedManagementException var4) {
            throw new AssertionError(var4);
         }
      }
   }
}
