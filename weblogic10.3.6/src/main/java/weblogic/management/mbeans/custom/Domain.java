package weblogic.management.mbeans.custom;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.management.RuntimeOperationsException;
import weblogic.descriptor.Descriptor;
import weblogic.management.DomainDir;
import weblogic.management.ManagementLogger;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.BasicRealmMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ForeignJMSConnectionFactoryMBean;
import weblogic.management.configuration.ForeignJMSDestinationMBean;
import weblogic.management.configuration.JMSDistributedQueueMemberMBean;
import weblogic.management.configuration.JMSDistributedTopicMemberMBean;
import weblogic.management.configuration.JMSQueueMBean;
import weblogic.management.configuration.JMSSessionPoolMBean;
import weblogic.management.configuration.JMSTopicMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.management.provider.internal.DescriptorHelper;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerLifecycleException;

public final class Domain extends ConfigurationMBeanCustomizer {
   private boolean debug = false;
   private String path = null;
   static int instanceCounter = 0;
   static Map instanceMap = new HashMap();
   private boolean clusterConstraintsEnabled = false;
   private boolean productionModeEnabled = false;

   public Domain(ConfigurationMBeanCustomized var1) {
      super(var1);
      String var2 = System.getProperty("weblogic.ClusterConstraintsEnabled");
      if (var2 != null) {
         var2 = var2.toLowerCase(Locale.US);
         if (var2.equals("true")) {
            this.setClusterConstraintsEnabled(true);
         } else {
            this.setClusterConstraintsEnabled(false);
         }
      }

      if (this.debug) {
         ++instanceCounter;
         instanceMap.put(this.toString(), Thread.currentThread().getStackTrace());
         System.out.println("Constructed DomainMBean customizer current count " + instanceCounter);
         Thread.dumpStack();
      }

   }

   public String getRootDirectory() {
      try {
         if (this.path == null) {
            this.path = DomainDir.getRootDir();
         }
      } catch (Exception var2) {
         ManagementLogger.logExceptionInCustomizer(var2);
      }

      return this.path;
   }

   public boolean isClusterConstraintsEnabled() {
      return this.clusterConstraintsEnabled;
   }

   public void setClusterConstraintsEnabled(boolean var1) {
      this.clusterConstraintsEnabled = var1;
   }

   public boolean isProductionModeEnabled() {
      return this.productionModeEnabled;
   }

   public void setProductionModeEnabled(boolean var1) {
      this.productionModeEnabled = var1;
      Descriptor var2 = ((DomainMBean)this.getMbean()).getDescriptor();
      DescriptorHelper.setDescriptorTreeProductionMode(var2, var1);
      DescriptorHelper.setDescriptorManagerProductionModeIfNeeded(var2, var1);
   }

   public void discoverManagedServers() {
   }

   public boolean discoverManagedServer(String var1) {
      return false;
   }

   public Object[] getDisconnectedManagedServers() {
      return new Object[0];
   }

   public HashMap start() {
      if (this.isConfig()) {
         return null;
      } else {
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         DomainAccess var2 = ManagementService.getDomainAccess(var1);
         ServerMBean[] var3 = ((DomainMBean)this.getMbean()).getServers();
         HashMap var4 = new HashMap();

         try {
            for(int var5 = 0; var5 < var3.length; ++var5) {
               ServerMBean var10 = var3[var5];
               var4.put(var10.getName(), var2.lookupServerLifecycleRuntime(var10.getName()).start());

               try {
                  Thread.currentThread();
                  Thread.sleep(1000L);
               } catch (Exception var8) {
               }
            }

            return var4;
         } catch (ServerLifecycleException var9) {
            RuntimeException var6 = new RuntimeException(var9);
            throw new RuntimeOperationsException(var6);
         }
      }
   }

   public HashMap kill() {
      if (this.isConfig()) {
         return null;
      } else {
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         DomainAccess var2 = ManagementService.getDomainAccess(var1);
         ServerMBean[] var3 = ((DomainMBean)this.getMbean()).getServers();
         HashMap var4 = new HashMap();

         try {
            for(int var5 = 0; var5 < var3.length; ++var5) {
               ServerMBean var10 = var3[var5];
               var4.put(var10.getName(), var2.lookupServerLifecycleRuntime(var10.getName()).forceShutdown());

               try {
                  Thread.currentThread();
                  Thread.sleep(1000L);
               } catch (Exception var8) {
               }
            }

            return var4;
         } catch (ServerLifecycleException var9) {
            RuntimeException var6 = new RuntimeException(var9);
            throw new RuntimeOperationsException(var6);
         }
      }
   }

   public DeploymentMBean[] getDeployments() {
      return (DeploymentMBean[])((DeploymentMBean[])Domain.DEPLOYMENTAGGREGATOR.instance.getAll(this.getMbean()));
   }

   public BasicDeploymentMBean[] getBasicDeployments() {
      DomainMBean var1 = (DomainMBean)this.getMbean();
      AppDeploymentMBean[] var2 = AppDeploymentHelper.getAppsAndLibs(var1);
      SystemResourceMBean[] var3 = var1.getSystemResources();
      if (var2 != null && var2.length != 0) {
         if (var3 != null && var3.length != 0) {
            BasicDeploymentMBean[] var4 = new BasicDeploymentMBean[var2.length + var3.length];
            System.arraycopy(var3, 0, var4, 0, var3.length);
            System.arraycopy(var2, 0, var4, var3.length, var2.length);
            return var4;
         } else {
            return var2;
         }
      } else {
         return var3;
      }
   }

   public SystemResourceMBean[] getSystemResources() {
      return (SystemResourceMBean[])((SystemResourceMBean[])Domain.SYSTEMRESOURCEAGGREGATOR.instance.getAll(this.getMbean()));
   }

   public SystemResourceMBean lookupSystemResource(String var1) {
      return (SystemResourceMBean)Domain.SYSTEMRESOURCEAGGREGATOR.instance.lookup(this.getMbean(), var1);
   }

   public TargetMBean[] getTargets() {
      return (TargetMBean[])((TargetMBean[])Domain.TARGETAGGREGATOR.instance.getAll(this.getMbean()));
   }

   public TargetMBean lookupTarget(String var1) {
      return (TargetMBean)Domain.TARGETAGGREGATOR.instance.lookup(this.getMbean(), var1);
   }

   public BasicRealmMBean[] getBasicRealms() {
      return (BasicRealmMBean[])((BasicRealmMBean[])Domain.BASICREALMAGGREGATOR.instance.getAll(this.getMbean()));
   }

   public JMSSessionPoolMBean createJMSSessionPool(String var1, JMSSessionPoolMBean var2) {
      JMSSessionPoolMBean var3 = (JMSSessionPoolMBean)this.getMbean().createChildCopyIncludingObsolete("JMSSessionPool", var2);
      return var3;
   }

   public ForeignJMSDestinationMBean createForeignJMSDestination(String var1, ForeignJMSDestinationMBean var2) {
      ForeignJMSDestinationMBean var3 = (ForeignJMSDestinationMBean)this.getMbean().createChildCopyIncludingObsolete("ForeignJMSDestination", var2);
      return var3;
   }

   public ForeignJMSConnectionFactoryMBean createForeignJMSConnectionFactory(String var1, ForeignJMSConnectionFactoryMBean var2) {
      ForeignJMSConnectionFactoryMBean var3 = (ForeignJMSConnectionFactoryMBean)this.getMbean().createChildCopyIncludingObsolete("ForeignJMSConnectionFactory", var2);
      return var3;
   }

   public JMSDistributedQueueMemberMBean createJMSDistributedQueueMember(String var1, JMSDistributedQueueMemberMBean var2) {
      JMSDistributedQueueMemberMBean var3 = (JMSDistributedQueueMemberMBean)this.getMbean().createChildCopy("JMSDistributedQueueMember", var2);
      return var3;
   }

   public JMSDistributedTopicMemberMBean createJMSDistributedTopicMember(String var1, JMSDistributedTopicMemberMBean var2) {
      JMSDistributedTopicMemberMBean var3 = (JMSDistributedTopicMemberMBean)this.getMbean().createChildCopy("JMSDistributedTopicMember", var2);
      return var3;
   }

   public JMSTopicMBean createJMSTopic(String var1, JMSTopicMBean var2) {
      JMSTopicMBean var3 = (JMSTopicMBean)this.getMbean().createChildCopyIncludingObsolete("JMSTopic", var2);
      return var3;
   }

   public JMSQueueMBean createJMSQueue(String var1, JMSQueueMBean var2) {
      JMSQueueMBean var3 = (JMSQueueMBean)this.getMbean().createChildCopyIncludingObsolete("JMSQueue", var2);
      return var3;
   }

   private static class BASICREALMAGGREGATOR {
      static AttributeAggregator instance = new AttributeAggregator("weblogic.management.configuration.DomainMBean", BasicRealmMBean.class);
   }

   private static class TARGETAGGREGATOR {
      static AttributeAggregator instance = new AttributeAggregator("weblogic.management.configuration.DomainMBean", TargetMBean.class);
   }

   private static class SYSTEMRESOURCEAGGREGATOR {
      static AttributeAggregator instance = new AttributeAggregator(DomainMBean.class, SystemResourceMBean.class, "getSystemResources");
   }

   private static class DEPLOYMENTAGGREGATOR {
      static AttributeAggregator instance = new AttributeAggregator(DomainMBean.class, DeploymentMBean.class, "getDeployments");
   }
}
