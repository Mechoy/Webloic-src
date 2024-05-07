package weblogic.management.mbeans.custom;

import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.management.RuntimeOperationsException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerLifecycleException;

public final class Cluster extends ConfigurationMBeanCustomizer {
   private static final long serialVersionUID = 6825873886824636463L;
   private String multicastAddress;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public Cluster(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public ServerMBean[] getServers() {
      HashSet var1 = new HashSet();
      DomainMBean var2 = (DomainMBean)((DomainMBean)this.getMbean().getParent());
      ServerMBean[] var3 = var2.getServers();
      String var4 = this.getMbean().getName();

      for(int var5 = 0; var5 < var3.length; ++var5) {
         ServerMBean var6 = var3[var5];
         if (var6.getCluster() != null && var6.getCluster().getName().equals(var4)) {
            var1.add(var6);
         }
      }

      ServerMBean[] var7 = new ServerMBean[var1.size()];
      return (ServerMBean[])((ServerMBean[])var1.toArray(var7));
   }

   public MigratableTargetMBean[] getMigratableTargets() {
      HashSet var1 = new HashSet();
      DomainMBean var2 = (DomainMBean)((DomainMBean)this.getMbean().getParent());
      MigratableTargetMBean[] var3 = var2.getMigratableTargets();
      String var4 = this.getMbean().getName();

      for(int var5 = 0; var5 < var3.length; ++var5) {
         MigratableTargetMBean var6 = var3[var5];
         if (var6.getCluster() != null && var6.getCluster().getName().equals(var4)) {
            var1.add(var6);
         }
      }

      MigratableTargetMBean[] var7 = new MigratableTargetMBean[var1.size()];
      return (MigratableTargetMBean[])((MigratableTargetMBean[])var1.toArray(var7));
   }

   public String getMulticastAddress() {
      String var1 = System.getProperty("weblogic.cluster.multicastAddress");
      if (var1 != null) {
         return var1;
      } else {
         var1 = this.multicastAddress;
         if (var1 == null) {
            var1 = "237.0.0.1";
            this.multicastAddress = var1;
         }

         return var1;
      }
   }

   public void setMulticastAddress(String var1) {
      this.multicastAddress = var1;
   }

   public HashMap start() throws RuntimeOperationsException {
      if (this.isConfig()) {
         return null;
      } else {
         ServerMBean[] var1 = this.getServers();
         HashMap var2 = new HashMap();

         try {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               ServerMBean var8 = var1[var3];
               var2.put(var8.getName(), this.getServerLifeCycleRuntime(var8.getName()).start());

               try {
                  Thread.currentThread();
                  Thread.sleep(1000L);
               } catch (Exception var6) {
               }
            }

            return var2;
         } catch (ServerLifecycleException var7) {
            RuntimeException var4 = new RuntimeException(var7);
            throw new RuntimeOperationsException(var4);
         }
      }
   }

   public HashMap kill() throws RuntimeOperationsException {
      if (this.isConfig()) {
         return null;
      } else {
         ServerMBean[] var1 = this.getServers();
         HashMap var2 = new HashMap();

         try {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               ServerMBean var8 = var1[var3];
               var2.put(var8.getName(), this.getServerLifeCycleRuntime(var8.getName()).forceShutdown());

               try {
                  Thread.currentThread();
                  Thread.sleep(1000L);
               } catch (Exception var6) {
               }
            }

            return var2;
         } catch (ServerLifecycleException var7) {
            RuntimeException var4 = new RuntimeException(var7);
            throw new RuntimeOperationsException(var4);
         }
      }
   }

   private ServerLifeCycleRuntimeMBean getServerLifeCycleRuntime(String var1) {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         DomainAccess var2 = ManagementService.getDomainAccess(kernelId);
         return var2.lookupServerLifecycleRuntime(var1);
      } else {
         return null;
      }
   }

   public Set getServerNames() {
      ServerMBean[] var1 = this.getServers();
      HashSet var2 = new HashSet(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.add(var1[var3].getName());
      }

      return var2;
   }
}
