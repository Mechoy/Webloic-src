package weblogic.deploy.beans.factory.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.management.InvalidAttributeValueException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.beans.factory.DeploymentBeanFactory;
import weblogic.deploy.beans.factory.InvalidTargetException;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.TargetHelper;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.LibraryMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.internal.MBeanConverter;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class DeploymentBeanFactoryImpl implements DeploymentBeanFactory {
   private DomainMBean editableDomain = null;
   private boolean callerOwnsEditLock = false;
   private DomainMBean runtimeDomain = null;
   private HashMap deployerInitiatedBeanUpdates = new HashMap();
   private static final String LIBRARIES_PROP_NAME = "Libraries";
   private static final String APP_DEPLOYMENTS_PROP_NAME = "AppDeployments";
   private static final String SUB_DEPLOYMENTS_PROP_NAME = "SubDeployments";
   private static final String TARGETS_PROP_NAME = "Targets";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public final AppDeploymentMBean createAppDeploymentMBean(String var1, File var2, DeploymentData var3) throws InvalidTargetException, FileNotFoundException, ManagementException {
      Object var4;
      if (var3.isLibrary()) {
         var4 = this.getEditableDomain().createLibrary(var1, var2.getPath());
         this.addBeanUpdate(this.getEditableDomain(), "Libraries", 2, var4);
      } else {
         try {
            var4 = this.getEditableDomain().createAppDeployment(var1, var2.getPath());
            this.addBeanUpdate(this.getEditableDomain(), "AppDeployments", 2, var4);
         } catch (IllegalArgumentException var8) {
            Throwable var6 = var8.getCause();
            if (var6 != null) {
               if (var6 instanceof ManagementException) {
                  throw (ManagementException)var6;
               }

               throw new ManagementException(var6);
            }

            throw new ManagementException(var8);
         }
      }

      try {
         this.addTargetsInDeploymentData(var3, (BasicDeploymentMBean)var4);
      } catch (InvalidTargetException var7) {
         AppDeploymentHelper.destroyAppOrLib((AppDeploymentMBean)var4, this.getEditableDomain());
         throw var7;
      }

      ((AppDeploymentMBean)var4).setModuleType(WebLogicModuleType.getFileModuleTypeAsString(var2));
      initialize((AppDeploymentMBean)var4, var3);
      return (AppDeploymentMBean)var4;
   }

   public final BasicDeploymentMBean addTargetsInDeploymentData(DeploymentData var1, BasicDeploymentMBean var2) throws InvalidTargetException, ManagementException {
      this.addGlobalTargetsToDeployable(var1, var2);
      this.addModuleTargetsToDeployable(var1, var2);
      this.addSubModuleTargetsToDeployable(var1, var2);
      return var2;
   }

   public final BasicDeploymentMBean removeTargetsInDeploymentData(DeploymentData var1, BasicDeploymentMBean var2) throws InvalidTargetException {
      if (var1 == null) {
         return var2;
      } else {
         InvalidTargetException var3 = null;

         try {
            this.rmGlobalTargets(var1, var2);
         } catch (InvalidTargetException var6) {
            var3 = var6;
         }

         this.rmModuleTargets(var1, var2);
         this.rmSubModuleTargets(var1, var2);
         boolean var4 = this.rmGlobalTargetsPropagateToModules(var1, var2);
         boolean var5 = this.rmGlobalTargetsPropagateToSubModules(var1, var2);
         if (!var4 && !var5 && null != var3) {
            throw var3;
         } else {
            return var2;
         }
      }
   }

   public final void removeMBean(AppDeploymentMBean var1) throws ManagementException {
      DomainMBean var2 = this.getEditableDomain();
      if (MBeanConverter.isDebugEnabled()) {
         MBeanConverter.debug("DeploymentBeanFactory: Destroy " + var1.getObjectName() + " from " + var2.getObjectName());
      }

      AppDeploymentHelper.destroyAppOrLib(var1, var2);
      String var3 = var1 instanceof LibraryMBean ? "Libraries" : "AppDeployments";
      this.addBeanUpdate(var2, var3, 3, var1);
   }

   public final void setEditableDomain(DomainMBean var1, boolean var2) {
      this.editableDomain = var1;
      this.callerOwnsEditLock = var2;
   }

   public final void resetEditableDomain() {
      this.editableDomain = null;
      this.callerOwnsEditLock = false;
   }

   public final DomainMBean getEditableDomain() {
      if (this.editableDomain != null) {
         return this.editableDomain;
      } else {
         if (this.runtimeDomain == null) {
            this.runtimeDomain = ManagementService.getRuntimeAccess(kernelId).getDomain();
         }

         return this.runtimeDomain;
      }
   }

   public boolean isDeployerInitiatedBeanUpdate(DescriptorBean var1, BeanUpdateEvent.PropertyUpdate var2) {
      ArrayList var3 = (ArrayList)this.deployerInitiatedBeanUpdates.get(this.getBeanId(var1));
      return var3 != null && var3.size() != 0 ? this.contains(var3, var2) : false;
   }

   public void resetDeployerInitiatedBeanUpdates() {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("DeploymentBeanFactoryImpl.resetDeployerInitiatedBeanUpdates");
      }

      this.deployerInitiatedBeanUpdates.clear();
   }

   private void rmSubModuleTargets(DeploymentData var1, BasicDeploymentMBean var2) throws InvalidTargetException {
      Map var3 = var1.getAllSubModuleTargets();
      if (var1.isStandaloneModule()) {
         this.rmSubTargets(var2.getSubDeployments(), (Map)var3.get("_the_standalone_module"));
      } else {
         Iterator var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            SubDeploymentMBean var6 = var2.lookupSubDeployment(var5);
            if (var6 == null) {
               throw new InvalidTargetException(var5);
            }

            this.rmSubTargets(var6.getSubDeployments(), (Map)var3.get(var5));
         }
      }

   }

   private void rmSubTargets(SubDeploymentMBean[] var1, Map var2) throws InvalidTargetException {
      Iterator var3 = var2.keySet().iterator();

      while(true) {
         int var5;
         TargetMBean[] var6;
         label33:
         do {
            while(var3.hasNext()) {
               String var4 = (String)var3.next();

               for(var5 = 0; var1 != null && var5 < var1.length; ++var5) {
                  if (var1[var5].getName().equals(var4)) {
                     this.removeTarget(var1[var5], (String[])((String[])var2.get(var4)));
                     var6 = var1[var5].getTargets();
                     continue label33;
                  }
               }
            }

            return;
         } while(var6 != null && var6.length != 0);

         this.removeSubDeployment(var1[var5]);
      }
   }

   private void rmModuleTargets(DeploymentData var1, BasicDeploymentMBean var2) throws InvalidTargetException {
      if (!var1.isStandaloneModule()) {
         Map var3 = var1.getAllModuleTargets();
         Iterator var4 = var3.keySet().iterator();

         while(true) {
            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               SubDeploymentMBean var6 = var2.lookupSubDeployment(var5);
               String[] var7 = (String[])((String[])var3.get(var5));
               if (var6 != null) {
                  this.removeTarget(var6, var7);
               } else {
                  SubDeploymentMBean var8 = this.findOrCreateSubDeployment(var5, var2);
                  TargetMBean[] var9 = var2.getTargets();
                  String[] var10 = new String[var9.length];

                  for(int var11 = 0; var9 != null && var11 < var9.length; ++var11) {
                     var10[var11] = var9[var11].getName();
                  }

                  this.addTargets(var8, TargetHelper.lookupTargetMBeans(this.getEditableDomain(), var10));
                  this.removeTarget(var8, var7);
               }
            }

            return;
         }
      }
   }

   private boolean rmGlobalTargetsPropagateToModules(DeploymentData var1, BasicDeploymentMBean var2) {
      boolean var3 = false;
      String[] var4 = var1.getGlobalTargets();
      SubDeploymentMBean[] var5 = var2.getSubDeployments();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         try {
            this.removeTarget(var5[var6], var4, true);
            var3 = true;
         } catch (InvalidTargetException var8) {
         }
      }

      return var3;
   }

   private boolean rmGlobalTargetsPropagateToSubModules(DeploymentData var1, BasicDeploymentMBean var2) {
      if (var1.isStandaloneModule()) {
         return false;
      } else {
         boolean var3 = false;
         String[] var4 = var1.getGlobalTargets();
         SubDeploymentMBean[] var5 = var2.getSubDeployments();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            SubDeploymentMBean[] var7 = var5[var6].getSubDeployments();

            for(int var8 = 0; var8 < var7.length; ++var8) {
               try {
                  this.removeTarget(var7[var8], var4);
                  var3 = true;
               } catch (InvalidTargetException var10) {
               }
            }
         }

         return var3;
      }
   }

   private void rmGlobalTargets(DeploymentData var1, BasicDeploymentMBean var2) throws InvalidTargetException {
      String[] var3 = var1.getGlobalTargets();
      this.removeTarget(var2, var3);
   }

   private void removeTarget(TargetInfoMBean var1, String[] var2) throws InvalidTargetException {
      this.removeTarget(var1, var2, false);
   }

   private void removeTarget(TargetInfoMBean var1, String[] var2, boolean var3) throws InvalidTargetException {
      for(int var5 = 0; var2 != null && var5 < var2.length; ++var5) {
         boolean var6 = var3;
         String var7 = var2[var5];
         TargetMBean[] var4 = var1.getTargets();

         for(int var8 = 0; var4 != null && var8 < var4.length; ++var8) {
            if (var4[var8].getName().equals(var7)) {
               try {
                  var6 = true;
                  var1.removeTarget(var4[var8]);
                  this.addBeanUpdate(var1, "Targets", 3, var4[var8]);
               } catch (InvalidAttributeValueException var10) {
                  throw new InvalidTargetException(var10.toString());
               } catch (DistributedManagementException var11) {
                  throw new InvalidTargetException(var11.toString());
               }
            }
         }

         if (!var6) {
            throw new InvalidTargetException(var7);
         }
      }

   }

   private void addGlobalTargetsToDeployable(DeploymentData var1, BasicDeploymentMBean var2) throws InvalidTargetException {
      this.addTargets(var2, TargetHelper.lookupTargetMBeans(this.getEditableDomain(), var1.getGlobalTargets()));
   }

   private void addModuleTargetsToDeployable(DeploymentData var1, BasicDeploymentMBean var2) throws InvalidTargetException {
      Map var3 = var1.getAllModuleTargets();
      Iterator var4 = var3.keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         String[] var6 = (String[])((String[])var3.get(var5));
         if (var6 != null) {
            SubDeploymentMBean var7 = this.findOrCreateSubDeployment(var5, var2);
            this.addTargets(var7, TargetHelper.lookupTargetMBeans(this.getEditableDomain(), var6));
         }
      }

   }

   private SubDeploymentMBean findOrCreateSubDeployment(String var1, BasicDeploymentMBean var2) {
      SubDeploymentMBean var3 = var2.lookupSubDeployment(var1);
      if (var3 == null) {
         var3 = var2.createSubDeployment(var1);
         this.addBeanUpdate(var2, "SubDeployments", 2, var3);
      }

      return var3;
   }

   private void addTargets(TargetInfoMBean var1, TargetMBean[] var2) throws InvalidTargetException {
      TargetMBean[] var3 = var1.getTargets();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         TargetMBean var5 = var2[var4];
         if (isNewTarget(var3, var5)) {
            try {
               var1.addTarget(var5);
               this.addBeanUpdate(var1, "Targets", 2, var5);
            } catch (InvalidAttributeValueException var7) {
               throw new InvalidTargetException(var7.toString());
            } catch (DistributedManagementException var8) {
               throw new InvalidTargetException(var8.toString());
            }
         }
      }

   }

   private static boolean isNewTarget(TargetMBean[] var0, TargetMBean var1) {
      if (var0 == null) {
         return false;
      } else {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            TargetMBean var3 = var0[var2];
            if (var3.getName().equals(var1.getName())) {
               return false;
            }
         }

         return true;
      }
   }

   private void addSubModuleTargetsToDeployable(DeploymentData var1, BasicDeploymentMBean var2) throws InvalidTargetException {
      Map var3 = var1.getAllSubModuleTargets();
      if (!var3.isEmpty()) {
         Iterator var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            Map var6 = (Map)var3.get(var5);
            Iterator var7 = var6.keySet().iterator();

            while(var7.hasNext()) {
               String var9 = (String)var7.next();
               String[] var10 = (String[])((String[])var6.get(var9));
               SubDeploymentMBean var8;
               if (var5.equals("_the_standalone_module")) {
                  var8 = this.findOrCreateSubDeployment(var9, var2);
               } else {
                  var8 = this.findOrCreateSubSubDeployment(var2, var5, var9);
               }

               if (var10 != null) {
                  this.addTargets(var8, TargetHelper.lookupTargetMBeans(this.getEditableDomain(), var10));
               }
            }
         }

      }
   }

   private SubDeploymentMBean findOrCreateSubSubDeployment(BasicDeploymentMBean var1, String var2, String var3) {
      SubDeploymentMBean var4 = var1.lookupSubDeployment(var2);
      SubDeploymentMBean var5;
      if (var4 == null) {
         var4 = var1.createSubDeployment(var2);
         var5 = var4.createSubDeployment(var3);
         this.addBeanUpdate(var4, "SubDeployments", 2, var5);
      } else {
         var5 = var4.lookupSubDeployment(var3);
         if (var5 == null) {
            var5 = var4.createSubDeployment(var3);
            this.addBeanUpdate(var4, "SubDeployments", 2, var5);
         }
      }

      return var5;
   }

   private static void initialize(AppDeploymentMBean var0, DeploymentData var1) {
      if (var1.getDeploymentPlan() != null) {
         if (var1.getConfigDirectory() != null) {
            File var2 = new File(var1.getConfigDirectory());
            String[] var3 = var2.list();
            if (var3 != null && var3.length > 0) {
               var0.setPlanDir(var1.getConfigDirectory());
            }
         }

         var0.setPlanPath(var1.getDeploymentPlan());
      }

      if (var1.getAltDescriptorPath() != null) {
         var0.setAltDescriptorPath(var1.getAltDescriptorPath());
      }

      if (var1.getAltWLSDescriptorPath() != null) {
         var0.setAltWLSDescriptorPath(var1.getAltWLSDescriptorPath());
      }

      if (var1.isSecurityValidationEnabled()) {
         var0.setValidateDDSecurityData(true);
      }

      if (var1.getSecurityModel() != null) {
         var0.setSecurityDDModel(var1.getSecurityModel());
      }

      if (var0.getVersionIdentifier() != null) {
         AppDeploymentMBean var4 = ApplicationVersionUtils.getActiveAppDeployment(var0.getApplicationName());
         if (var4 != null) {
            var0.setSecurityDDModel(var4.getSecurityDDModel());
            if (var4.isValidateDDSecurityData() != var0.isValidateDDSecurityData()) {
               var0.setValidateDDSecurityData(var4.isValidateDDSecurityData());
            }
         }
      }

      if (var1.getDeploymentPrincipalName() != null) {
         var0.setDeploymentPrincipalName(var1.getDeploymentPrincipalName());
      }

   }

   private void addBeanUpdate(DescriptorBean var1, String var2, int var3, Object var4) {
      if (this.callerOwnsEditLock) {
         String var5 = this.getBeanId(var1);
         ArrayList var6 = (ArrayList)this.deployerInitiatedBeanUpdates.get(var5);
         if (var6 == null) {
            var6 = new ArrayList();
            this.deployerInitiatedBeanUpdates.put(var5, var6);
         }

         Iterator var7 = var6.iterator();

         BeanUpdateEvent.PropertyUpdate var8;
         do {
            do {
               if (!var7.hasNext()) {
                  BeanUpdateEvent.PropertyUpdate var9 = new BeanUpdateEvent.PropertyUpdate(var2, var3, var4, true, false, false);
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("DeploymentBeanFactoryImpl.addPropertyUpdate  source=" + var1 + ", update=" + var9);
                  }

                  var6.add(var9);
                  return;
               }

               var8 = (BeanUpdateEvent.PropertyUpdate)var7.next();
            } while(!var2.equals(var8.getPropertyName()));
         } while((var3 != 2 || var8.getUpdateType() != 3 || !var4.equals(var8.getAddedObject())) && (var3 != 3 || var8.getUpdateType() != 2 || !var4.equals(var8.getRemovedObject())));

         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("DeploymentBeanFactoryImpl.removePropertyUpdate " + var8);
         }

         var7.remove();
      }
   }

   private boolean contains(ArrayList var1, BeanUpdateEvent.PropertyUpdate var2) {
      String var3 = var2.getPropertyName();
      int var4 = var2.getUpdateType();
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("DeploymentBeanFactoryImpl.contains deployerUpdates=" + var1 + ", update=" + var2);
      }

      Iterator var5 = var1.iterator();

      BeanUpdateEvent.PropertyUpdate var6;
      do {
         do {
            do {
               if (!var5.hasNext()) {
                  return false;
               }

               var6 = (BeanUpdateEvent.PropertyUpdate)var5.next();
            } while(!var3.equals(var6.getPropertyName()));
         } while(var4 != var6.getUpdateType());
      } while((var4 != 2 || !this.isEqualBean(var2.getAddedObject(), var6.getAddedObject())) && (var4 != 3 || !this.isEqualBean(var2.getRemovedObject(), var6.getRemovedObject())));

      return true;
   }

   private String getBeanId(DescriptorBean var1) {
      return var1 instanceof WebLogicMBean ? var1.getClass().getName() + ":" + ((WebLogicMBean)var1).getName() : var1.toString();
   }

   private boolean isEqualBean(Object var1, Object var2) {
      if (var1 instanceof WebLogicMBean && var2 instanceof WebLogicMBean) {
         return var1.getClass().equals(var2.getClass()) && ((WebLogicMBean)var1).getName().equals(((WebLogicMBean)var2).getName());
      } else {
         return var1.equals(var2);
      }
   }

   private void removeSubDeployment(SubDeploymentMBean var1) {
      WebLogicMBean var2 = var1.getParent();
      if (var2 instanceof AppDeploymentMBean) {
         AppDeploymentMBean var3 = (AppDeploymentMBean)var2;
         var3.destroySubDeployment(var1);
         this.addBeanUpdate(var3, "SubDeployments", 3, var1);
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("DeploymentBeanFactoryImpl.removeSubDeployment('" + var1 + "') from '" + var3 + "'");
         }
      } else if (var2 instanceof SubDeploymentMBean) {
         SubDeploymentMBean var5 = (SubDeploymentMBean)var2;
         var5.destroySubDeployment(var1);
         this.addBeanUpdate(var5, "SubDeployments", 3, var1);
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("DeploymentBeanFactoryImpl.removeSubDeployment('" + var1 + "') from '" + var5 + "'");
         }

         SubDeploymentMBean[] var4 = var5.getSubDeployments();
         if (var4 == null || var4.length == 0) {
            this.removeSubDeployment(var5);
         }
      }

   }
}
