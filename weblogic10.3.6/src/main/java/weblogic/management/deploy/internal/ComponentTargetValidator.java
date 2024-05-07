package weblogic.management.deploy.internal;

import java.security.AccessController;
import java.util.HashSet;
import java.util.Set;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.configuration.WebDeploymentMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ComponentTargetValidator {
   ComponentMBean component;
   Set clusterTargets = new HashSet();
   Set serverTargets = new HashSet();
   Set hostTargets = new HashSet();
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public ComponentTargetValidator(ComponentMBean var1) throws ManagementException {
      this.component = var1;
      TargetMBean[] var2 = var1.getTargets();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         TargetMBean var4 = var2[var3];
         if (var4 instanceof ServerMBean) {
            this.addServerTarget((ServerMBean)var4, true);
         } else if (var4 instanceof ClusterMBean) {
            this.addClusterTarget((ClusterMBean)var4);
         } else if (var4 instanceof MigratableTargetMBean) {
         }
      }

      if (var1 instanceof WebDeploymentMBean) {
         VirtualHostMBean[] var5 = ((WebDeploymentMBean)var1).getVirtualHosts();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            this.addHostTarget(var5[var6]);
         }
      }

   }

   public ComponentMBean getComponent() {
      return this.component;
   }

   public void addClusterTarget(ClusterMBean var1) throws ManagementException {
      ServerMBean[] var2 = var1.getServers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (this.serverTargets.contains(var2[var3])) {
            Loggable var4 = DeployerRuntimeLogger.logClusterMemberAlreadyTargetedLoggable(var1.getName(), var2[var3].getName(), this.component.getName());
            var4.log();
            throw new ManagementException(var4.getMessage());
         }
      }

      this.clusterTargets.add(var1);
   }

   public void addServerTarget(ServerMBean var1, boolean var2) throws ManagementException {
      if (var2) {
         ClusterMBean var3 = var1.getCluster();
         if (var3 != null) {
            if (this.clusterTargets.contains(var3)) {
               Loggable var8 = DeployerRuntimeLogger.logServerAlreadyTargetedByClusterLoggable(var3.getName(), var1.getName(), this.component.getName());
               var8.log();
               throw new ManagementException(var8.getMessage());
            }

            ServerMBean[] var4 = var3.getServers();
            if (var4 != null) {
               for(int var5 = 0; var5 < var4.length; ++var5) {
                  ServerMBean var6 = var4[var5];
                  if (!var6.getName().equals(var1.getName()) && this.serverTargets.contains(var6)) {
                     Loggable var7 = DeployerRuntimeLogger.logServerAlreadyTargetedByOtherClusterMemberLoggable(var3.getName(), var1.getName(), var6.getName(), this.component.getName());
                     var7.log();
                  }
               }
            }
         }
      }

      this.serverTargets.add(var1);
   }

   public void addHostTarget(VirtualHostMBean var1) throws ManagementException {
      if (this.component instanceof WebDeploymentMBean) {
         this.hostTargets.add(var1);
      } else {
         Loggable var2 = DeployerRuntimeLogger.logHostTargetForNonWebAppLoggable(var1.getName(), this.component.getName());
         var2.log();
         throw new ManagementException(var2.getMessage());
      }
   }

   public void addTarget(String var1, DomainMBean var2, boolean var3) throws ManagementException {
      DomainMBean var4 = null;
      if (var2 != null) {
         var4 = var2;
      } else {
         var4 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      }

      ServerMBean var5 = var4.lookupServer(var1);
      if (var5 != null) {
         this.addServerTarget(var5, var3);
      } else {
         ClusterMBean var6 = var4.lookupCluster(var1);
         if (var6 != null) {
            this.addClusterTarget(var6);
         } else {
            VirtualHostMBean var7 = var4.lookupVirtualHost(var1);
            if (var7 != null) {
               this.addHostTarget(var7);
            } else {
               Loggable var8 = DeployerRuntimeLogger.logNoSuchTargetLoggable(var1);
               var8.log();
               throw new ManagementException(var8.getMessage());
            }
         }
      }
   }
}
