package weblogic.deploy.internal.targetserver.operations;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weblogic.deploy.beans.factory.InvalidTargetException;
import weblogic.deploy.internal.InternalDeploymentData;
import weblogic.deploy.internal.TargetHelper;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.deploy.internal.MBeanConverter;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;

public final class RemoveOperation extends DeactivateOperation {
   private final String[] moduleIds;
   private String[] subModuleIds;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public RemoveOperation(long var1, String var3, InternalDeploymentData var4, BasicDeploymentMBean var5, DomainMBean var6, AuthenticatedSubject var7, boolean var8) throws DeploymentException {
      super(var1, var3, var4, var5, var6, var7, var8);
      this.operation = 4;
      this.moduleIds = TargetHelper.getNonGlobalModules(this.deploymentData, this.getApplication(), var6);
      this.subModuleIds = TargetHelper.getNonGlobalSubModules(this.deploymentData, this.getApplication());
      this.dumpModuleInfo();
      this.controlOperation = false;
   }

   private void dumpModuleInfo() {
      if (this.isDebugEnabled()) {
         int var1;
         String var2;
         if (this.moduleIds != null) {
            this.debug("Module Ids:");

            for(var1 = 0; var1 < this.moduleIds.length; ++var1) {
               var2 = this.moduleIds[var1];
               this.debug("  " + var2);
            }
         }

         if (this.subModuleIds != null) {
            this.debug("SubModule Ids:");

            for(var1 = 0; var1 < this.subModuleIds.length; ++var1) {
               var2 = this.subModuleIds[var1];
               this.debug("  " + var2);
            }
         }

      }
   }

   protected void compatibilityProcessor() {
      MBeanConverter.remove81MBean((AppDeploymentMBean)this.mbean);
   }

   protected void doCommit() throws DeploymentException {
      boolean var1 = true;
      boolean var2;
      if (cluster != null) {
         var2 = this.isFullRemoveFromCluster();
         if (!var2) {
            var1 = false;
            if (!this.isTargetListContainsCurrentServer()) {
               return;
            }
         }
      }

      var2 = this.isFullRemove();
      this.appcontainer = this.getApplication().findDeployment();
      if (this.isDebugEnabled()) {
         this.debug("RemoveOperation.doCommit for " + this.getApplication().getName());
      }

      if (this.appcontainer != null) {
         if (!var2) {
            this.stop(this.appcontainer, this.moduleIds);
         } else {
            this.removeDeployment();
         }
      } else if (this.isDebugEnabled()) {
         this.debug("RemoveOperation.doCommit: No app container found for " + this.getApplication().getName());
      }

      if (var2) {
         this.getApplication().remove(var1);
         this.appcontainer = null;
      }

      this.complete(3, (Exception)null);
      if (var2 && !ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         try {
            AppRuntimeStateManager.getManager().remove(this.mbean.getName());
         } catch (Throwable var4) {
         }
      }

   }

   private boolean isFullRemove() {
      boolean var1 = this.moduleIds == null && this.subModuleIds == null;
      var1 = var1 && (this.deploymentData.hasModuleTargets() || this.deploymentData.getGlobalTargets().length != 0 || !this.deploymentData.hasSubModuleTargets());
      if (this.isDebugEnabled()) {
         this.debug("isFullRemove set to : " + var1);
      }

      return var1;
   }

   protected final boolean isDeploymentRequestValidForCurrentServer() {
      if (this.isTargetListContainsCurrentServer()) {
         if (this.isDebugEnabled()) {
            this.debug("TargetList Contains Current Server");
         }

         return true;
      } else {
         String[] var1 = this.deploymentData.getGlobalTargets();
         if (cluster != null) {
            Set var2 = cluster.getServerNames();

            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (var2.contains(var1[var3])) {
                  return true;
               }
            }
         }

         if (!(this.mbean instanceof AppDeploymentMBean)) {
            return false;
         } else {
            AppDeploymentMBean var13 = (AppDeploymentMBean)this.mbean;
            Set var14 = TargetHelper.getAllTargetedServers(var13);
            boolean var4 = false;
            if (cluster == null) {
               return false;
            } else {
               String var5 = cluster.getName();
               ServerMBean[] var6 = cluster.getServers();

               for(int var7 = 0; var7 < var1.length; ++var7) {
                  if (var1[var7].equals(var5)) {
                     return true;
                  }
               }

               String[] var15 = new String[var6.length];

               for(int var8 = 0; var8 < var15.length; ++var8) {
                  var15[var8] = var6[var8].getName();
               }

               List var16 = Arrays.asList(var1);
               List var9 = Arrays.asList(var15);
               ArrayList var10 = new ArrayList();
               if (var14.containsAll(var9)) {
                  var10.addAll(var9);
               } else {
                  Iterator var11 = var14.iterator();

                  while(var11.hasNext()) {
                     Object var12 = var11.next();
                     if (var9.contains(var12)) {
                        var10.add(var12);
                     }
                  }
               }

               if (this.isDebugEnabled()) {
                  this.debug("pinned servers " + var10);
                  this.debug("listOfTargetsInRequest " + var16);
                  this.debug("mbean targets " + var14);
               }

               return var16.containsAll(var10);
            }
         }
      }
   }

   private boolean isFullRemoveFromCluster() {
      Debug.assertion(cluster != null);
      if (!this.isFullRemove()) {
         return false;
      } else if (this.deploymentData.isTargetsFromConfig()) {
         return true;
      } else {
         TargetMBean[] var1 = this.mbean.getTargets();
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.addAll(var1[var3].getServerNames());
         }

         String[] var8 = this.deploymentData.getGlobalTargets();

         for(int var4 = 0; var4 < var8.length; ++var4) {
            var2.remove(var8[var4]);
         }

         if (var2.isEmpty()) {
            return true;
         } else {
            try {
               HashSet var9 = new HashSet();
               var9.addAll(Arrays.asList(var8));
               Set var5 = this.deploymentData.getAllTargetedServers(var9);
               Iterator var6 = var5.iterator();

               while(var6.hasNext()) {
                  var2.remove(var6.next());
               }
            } catch (InvalidTargetException var7) {
            }

            return var2.isEmpty();
         }
      }
   }

   private void removeDeployment() {
      try {
         this.deactivate();
      } catch (DeploymentException var2) {
         SlaveDeployerLogger.logOperationFailed("Deactivate", this.getMBean().getName(), var2);
      }

      if (this.appcontainer != null) {
         if (this.getState(this.appcontainer) >= 1) {
            this.silentUnprepare(this.appcontainer);
         }

         this.silentRemove(this.appcontainer);
      }

   }
}
