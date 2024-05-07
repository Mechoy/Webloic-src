package weblogic.management.internal;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementLogger;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ForeignJMSServerMBean;
import weblogic.management.configuration.JDBCConnectionPoolMBean;
import weblogic.management.configuration.JDBCDataSourceMBean;
import weblogic.management.configuration.JDBCMultiPoolMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.JDBCTxDataSourceMBean;
import weblogic.management.configuration.JMSConnectionFactoryMBean;
import weblogic.management.configuration.JMSDistributedQueueMBean;
import weblogic.management.configuration.JMSDistributedTopicMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.MailSessionMBean;
import weblogic.management.configuration.MessagingBridgeMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.PersistentStoreMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.SNMPAgentDeploymentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.StartupClassMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.WTCServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class DeploymentHandlerHome {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationEdit");
   private static final int DEPLOYMENT_PREPARE = 0;
   private static final int DEPLOYMENT_UNPREPARE = 1;
   private static final int DEPLOYMENT_ACTIVATE = 2;
   private static final int DEPLOYMENT_DEACTIVATE = 3;
   private Set deploymentHandlers = new HashSet();
   private Set resourceDependentDeploymentHandlers = new HashSet();
   private Set regularTargets = new HashSet();
   private List exceptionList = new ArrayList();
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final BeanUpdateListener deploymentListener = this.createDeploymentUpdateListener();
   public static Comparator deploymentComparator = new Comparator() {
      public int compare(Object var1, Object var2) {
         byte var3 = 0;

         int var4;
         for(var4 = 0; var4 < DeploymentHandlerHome.DEPLOYMENT_ORDER.length; ++var4) {
            boolean var5 = DeploymentHandlerHome.DEPLOYMENT_ORDER[var4].isAssignableFrom(var1.getClass());
            boolean var6 = DeploymentHandlerHome.DEPLOYMENT_ORDER[var4].isAssignableFrom(var2.getClass());
            if (var5 && var6) {
               break;
            }

            if (var5) {
               var3 = -1;
               break;
            }

            if (var6) {
               var3 = 1;
               break;
            }
         }

         if (var3 == 0) {
            var4 = ((DeploymentMBean)var1).getDeploymentOrder();
            int var8 = ((DeploymentMBean)var2).getDeploymentOrder();
            boolean var7;
            if (var4 < var8) {
               var7 = true;
            }

            if (var8 < var4) {
               var7 = true;
            }

            return ((DeploymentMBean)var1).getName().compareTo(((DeploymentMBean)var2).getName());
         } else {
            return var3;
         }
      }
   };
   public static Comparator deploymentComparatorReverse = new Comparator() {
      public int compare(Object var1, Object var2) {
         return DeploymentHandlerHome.deploymentComparator.compare(var2, var1);
      }
   };
   private static final Class[] DEPLOYMENT_ORDER = new Class[]{StartupClassMBean.class, JDBCConnectionPoolMBean.class, JDBCSystemResourceMBean.class, PersistentStoreMBean.class, JDBCMultiPoolMBean.class, JDBCDataSourceMBean.class, JDBCTxDataSourceMBean.class, SAFAgentMBean.class, JMSConnectionFactoryMBean.class, JMSDistributedQueueMBean.class, JMSDistributedTopicMBean.class, JMSServerMBean.class, ForeignJMSServerMBean.class, MessagingBridgeMBean.class, MailSessionMBean.class, SNMPAgentDeploymentMBean.class};
   private static final Class[] RESOURCE_DEPENDENT_DEPLOYMENTS = new Class[]{WTCServerMBean.class};

   public static DeploymentHandlerHome getInstance() {
      return DeploymentHandlerHome.Initializer.SINGLETON;
   }

   private BeanUpdateListener createDomainUpdateListener() {
      return new BeanUpdateListener() {
         public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
            DeploymentHandlerHome.this.recalcTargetsIfNeeded(var1);
            DeploymentMBean[] var2 = this.getDeploymentUpdates(var1, 3);

            for(int var3 = var2.length - 1; var3 >= 0; --var3) {
               DeploymentMBean var4 = var2[var3];
               DeploymentHandlerHome.debug("Add listener to " + var4.getName());
               var4.addBeanUpdateListener(DeploymentHandlerHome.this.deploymentListener);
               if (DeploymentHandlerHome.this.isTargetedToThisServer(var4)) {
                  DeploymentHandlerHome.debug("domain listener de-activate " + var4.getName());
                  DeploymentHandlerHome.this.invokeHandlers(var4, 3, (DeploymentHandlerContext)null);
               }
            }

            DeploymentMBean[] var6 = this.getDeploymentUpdates(var1, 2);

            for(int var7 = 0; var7 < var6.length; ++var7) {
               DeploymentMBean var5 = var6[var7];
               DeploymentHandlerHome.debug("Add listener to " + var5.getName());
               var5.addBeanUpdateListener(DeploymentHandlerHome.this.deploymentListener);
               if (DeploymentHandlerHome.this.isTargetedToThisServer(var5)) {
                  DeploymentHandlerHome.debug("domain listener prepare " + var5.getName());
                  DeploymentHandlerHome.this.invokeHandlers(var5, 0, (DeploymentHandlerContext)null);
               }
            }

         }

         public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
            BeanUpdateFailedException var2 = null;
            DeploymentMBean[] var3 = this.getDeploymentUpdates(var1, 3);

            for(int var4 = var3.length - 1; var4 >= 0; --var4) {
               DeploymentMBean var5 = var3[var4];
               var5.addBeanUpdateListener(DeploymentHandlerHome.this.deploymentListener);
               if (DeploymentHandlerHome.this.isTargetedToThisServer(var5)) {
                  DeploymentHandlerHome.debug("domain listener un-prepare " + var5.getName());
                  DeploymentHandlerHome.this.invokeHandlers(var5, 1, (DeploymentHandlerContext)null);
                  if (!DeploymentHandlerHome.this.exceptionList.isEmpty()) {
                     if (var2 == null) {
                        var2 = new BeanUpdateFailedException();
                     }

                     Iterator var6 = DeploymentHandlerHome.this.exceptionList.iterator();

                     while(var6.hasNext()) {
                        var2.addException((Exception)var6.next());
                     }
                  }
               }
            }

            DeploymentMBean[] var8 = this.getDeploymentUpdates(var1, 2);

            for(int var9 = 0; var9 < var8.length; ++var9) {
               DeploymentMBean var10 = var8[var9];
               var10.addBeanUpdateListener(DeploymentHandlerHome.this.deploymentListener);
               if (DeploymentHandlerHome.this.isTargetedToThisServer(var10)) {
                  DeploymentHandlerHome.debug("domain listener activate " + var10.getName());
                  DeploymentHandlerHome.this.invokeHandlers(var10, 2, (DeploymentHandlerContext)null);
                  if (!DeploymentHandlerHome.this.exceptionList.isEmpty()) {
                     if (var2 == null) {
                        var2 = new BeanUpdateFailedException();
                     }

                     Iterator var7 = DeploymentHandlerHome.this.exceptionList.iterator();

                     while(var7.hasNext()) {
                        var2.addException((Exception)var7.next());
                     }
                  }
               }
            }

            if (var2 != null) {
               throw var2;
            }
         }

         public void rollbackUpdate(BeanUpdateEvent var1) {
         }

         private boolean isIgnored(BeanUpdateEvent.PropertyUpdate var1) {
            String var2 = var1.getPropertyName();
            if (var2.equals("Targets")) {
               return true;
            } else {
               return var2.equals("Deployments");
            }
         }

         private DeploymentMBean[] getDeploymentUpdates(BeanUpdateEvent var1, int var2) {
            TreeSet var3 = new TreeSet(DeploymentHandlerHome.deploymentComparator);
            BeanUpdateEvent.PropertyUpdate[] var4 = var1.getUpdateList();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               BeanUpdateEvent.PropertyUpdate var6 = var4[var5];
               if (!this.isIgnored(var6)) {
                  Object var7 = DeploymentHandlerHome.this.getChangedObject(var6);
                  if (var7 instanceof DeploymentMBean && var2 == var6.getUpdateType()) {
                     var3.add(var7);
                  }
               }
            }

            DeploymentMBean[] var8 = (DeploymentMBean[])((DeploymentMBean[])var3.toArray(new DeploymentMBean[var3.size()]));
            return var8;
         }
      };
   }

   private BeanUpdateListener createDeploymentUpdateListener() {
      return new BeanUpdateListener() {
         public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
            BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
            DescriptorBean var3 = var1.getProposedBean();
            if (var3 instanceof DeploymentMBean) {
               DeploymentMBean var4 = (DeploymentMBean)var3;
               TargetMBean[] var5 = this.getTargetUpdates(var1, 3);

               for(int var6 = 0; var6 < var5.length; ++var6) {
                  TargetMBean var7 = var5[var6];
                  if (DeploymentHandlerHome.this.regularTargets.contains(var7.getName())) {
                     DeploymentHandlerHome.debug("deploy listener de-activate " + var4.getName());
                     DeploymentHandlerHome.this.invokeHandlers(var4, 3, (DeploymentHandlerContext)null);
                  }
               }

               TargetMBean[] var9 = this.getTargetUpdates(var1, 2);

               for(int var10 = 0; var10 < var9.length; ++var10) {
                  TargetMBean var8 = var9[var10];
                  if (DeploymentHandlerHome.this.regularTargets.contains(var8.getName())) {
                     DeploymentHandlerHome.debug("deploy listener prepare " + var4.getName());
                     DeploymentHandlerHome.this.invokeHandlers(var4, 0, (DeploymentHandlerContext)null);
                  }
               }

            }
         }

         public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
            BeanUpdateFailedException var2 = null;
            BeanUpdateEvent.PropertyUpdate[] var3 = var1.getUpdateList();
            DescriptorBean var4 = var1.getSourceBean();
            if (var4 instanceof DeploymentMBean) {
               DeploymentMBean var5 = (DeploymentMBean)var4;
               TargetMBean[] var6 = this.getTargetUpdates(var1, 3);

               for(int var7 = 0; var7 < var6.length; ++var7) {
                  TargetMBean var8 = var6[var7];
                  if (DeploymentHandlerHome.this.regularTargets.contains(var8.getName())) {
                     DeploymentHandlerHome.debug("deploy listener un-prepare " + var5.getName());
                     DeploymentHandlerHome.this.invokeHandlers(var5, 1, (DeploymentHandlerContext)null);
                     if (!DeploymentHandlerHome.this.exceptionList.isEmpty()) {
                        if (var2 == null) {
                           var2 = new BeanUpdateFailedException();
                        }

                        Iterator var9 = DeploymentHandlerHome.this.exceptionList.iterator();

                        while(var9.hasNext()) {
                           var2.addException((Exception)var9.next());
                        }
                     }
                  }
               }

               TargetMBean[] var11 = this.getTargetUpdates(var1, 2);

               for(int var12 = 0; var12 < var11.length; ++var12) {
                  TargetMBean var13 = var11[var12];
                  if (DeploymentHandlerHome.this.regularTargets.contains(var13.getName())) {
                     DeploymentHandlerHome.debug("deploy listener activate " + var5.getName());
                     DeploymentHandlerHome.this.invokeHandlers(var5, 2, (DeploymentHandlerContext)null);
                     if (!DeploymentHandlerHome.this.exceptionList.isEmpty()) {
                        if (var2 == null) {
                           var2 = new BeanUpdateFailedException();
                        }

                        Iterator var10 = DeploymentHandlerHome.this.exceptionList.iterator();

                        while(var10.hasNext()) {
                           var2.addException((Exception)var10.next());
                        }
                     }
                  }
               }

               if (var2 != null) {
                  throw var2;
               }
            }
         }

         public void rollbackUpdate(BeanUpdateEvent var1) {
         }

         private TargetMBean[] getTargetUpdates(BeanUpdateEvent var1, int var2) {
            HashSet var3 = new HashSet();
            BeanUpdateEvent.PropertyUpdate[] var4 = var1.getUpdateList();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               BeanUpdateEvent.PropertyUpdate var6 = var4[var5];
               String var7 = var6.getPropertyName();
               Object var8 = DeploymentHandlerHome.this.getChangedObject(var6);
               if ("Targets".equals(var7) && var2 == var6.getUpdateType()) {
                  DeploymentHandlerHome.debug("ADD " + ((TargetMBean)var8).getName());
                  var3.add(var8);
               }
            }

            DeploymentHandlerHome.debug("Updated target set: " + var3);
            TargetMBean[] var9 = (TargetMBean[])((TargetMBean[])var3.toArray(new TargetMBean[var3.size()]));
            return var9;
         }
      };
   }

   public synchronized Set prepareInitialDeployments() throws DeploymentException {
      debug("Prepare initial deployments");
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      DomainMBean var2 = var1.getDomain();
      this.recalculateTargets(var2);
      TreeSet var3 = new TreeSet(deploymentComparator);
      DeploymentMBean[] var4 = var2.getDeployments();

      DeploymentMBean var6;
      for(int var5 = 0; var5 < var4.length; ++var5) {
         var6 = var4[var5];
         if (!isResourceDependent(var6)) {
            if (this.isTargetedToThisServer(var6)) {
               var3.add(var6);
            }

            var6.addBeanUpdateListener(this.deploymentListener);
         }
      }

      Iterator var7 = var3.iterator();

      while(var7.hasNext()) {
         var6 = (DeploymentMBean)var7.next();
         this.invokeHandlers(var6, 0, (DeploymentHandlerContext)null);
      }

      return var3;
   }

   public synchronized Set prepareResourceDependentInitialDeployments() throws DeploymentException {
      debug("Prepare resource dependent initial deployments");
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      DomainMBean var2 = var1.getDomain();
      this.recalculateTargets(var2);
      TreeSet var3 = new TreeSet(deploymentComparator);
      DeploymentMBean[] var4 = var2.getDeployments();

      DeploymentMBean var6;
      for(int var5 = 0; var5 < var4.length; ++var5) {
         var6 = var4[var5];
         if (isResourceDependent(var6)) {
            if (this.isTargetedToThisServer(var6)) {
               var3.add(var6);
            }

            var6.addBeanUpdateListener(this.deploymentListener);
         }
      }

      Iterator var7 = var3.iterator();

      while(var7.hasNext()) {
         var6 = (DeploymentMBean)var7.next();
         this.invokeHandlers(var6, 0, (DeploymentHandlerContext)null);
      }

      return var3;
   }

   public synchronized void activateInitialDeployments(Set var1) throws DeploymentException {
      debug("Activate initial deployments");
      this.activateInitialDeployments(var1, false);
   }

   public synchronized void activateResourceDependentInitialDeployments(Set var1) throws DeploymentException {
      debug("Activate resource dependent initial deployments");
      this.activateInitialDeployments(var1, true);
   }

   private synchronized void activateInitialDeployments(Set var1, boolean var2) throws DeploymentException {
      if (var1 != null && var1.size() > 0) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            DeploymentMBean var4 = (DeploymentMBean)var3.next();
            debug("activateInitialDeployments " + var4.getName());
            this.invokeHandlers(var4, 2, (DeploymentHandlerContext)null);
         }
      }

      if (!var2) {
         DomainMBean var5 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         var5.addBeanUpdateListener(this.createDomainUpdateListener());
      }

   }

   public synchronized Set deactivateCurrentDeployments() throws UndeploymentException {
      return this.deactivateCurrentDeployments(false);
   }

   public synchronized Set deactivateResourceDependentCurrentDeployments() throws UndeploymentException {
      return this.deactivateCurrentDeployments(true);
   }

   private synchronized Set deactivateCurrentDeployments(boolean var1) throws UndeploymentException {
      RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
      DomainMBean var3 = var2.getDomain();
      this.recalculateTargets(var3);
      TreeSet var4 = new TreeSet(deploymentComparatorReverse);
      DeploymentMBean[] var5 = var3.getDeployments();

      DeploymentMBean var7;
      for(int var6 = 0; var6 < var5.length; ++var6) {
         var7 = var5[var6];
         if (isResourceDependent(var7) == var1 && this.isTargetedToThisServer(var7)) {
            var4.add(var7);
         }
      }

      Iterator var8 = var4.iterator();

      while(var8.hasNext()) {
         var7 = (DeploymentMBean)var8.next();
         debug("deactivateCurrentDeployments " + var7.getName());
         this.invokeHandlers(var7, 3, (DeploymentHandlerContext)null);
      }

      return var4;
   }

   public synchronized void unprepareCurrentDeployments(Set var1) throws UndeploymentException {
      this.unprepareCurrentDeployments(var1, false);
   }

   public synchronized void unprepareResourceDependentCurrentDeployments(Set var1) throws UndeploymentException {
      this.unprepareCurrentDeployments(var1, true);
   }

   private synchronized void unprepareCurrentDeployments(Set var1, boolean var2) throws UndeploymentException {
      if (var1 != null) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            DeploymentMBean var4 = (DeploymentMBean)var3.next();
            debug("prepareInitialDeployments " + var4.getName());
            this.invokeHandlers(var4, 1, (DeploymentHandlerContext)null);
         }
      }

   }

   private void invokeHandlers(DeploymentMBean var1, int var2, DeploymentHandlerContext var3) {
      this.exceptionList.clear();
      Iterator var4;
      if (!isResourceDependent(var1)) {
         debug("Invoking resource independent handlers on " + var1.getName());
         var4 = this.deploymentHandlers.iterator();
      } else {
         debug("Invoking Resource dependent handlers on " + var1.getName());
         var4 = this.resourceDependentDeploymentHandlers.iterator();
      }

      while(var4.hasNext()) {
         DeploymentHandler var5 = (DeploymentHandler)var4.next();

         try {
            switch (var2) {
               case 0:
                  debug("  Call prepare on " + var5 + " with " + var1.getName());
                  var5.prepareDeployment(var1, var3);
                  break;
               case 1:
                  debug("  Call unprepare on " + var5 + " with " + var1.getName());
                  var5.unprepareDeployment(var1, var3);
                  break;
               case 2:
                  debug("  Call activate on " + var5 + " with " + var1.getName());
                  var5.activateDeployment(var1, var3);
                  break;
               case 3:
                  debug("  Call deactivate on " + var5 + " with " + var1.getName());
                  var5.deactivateDeployment(var1, var3);
            }
         } catch (DeploymentException var7) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception in DeploymentHandlerHome: ", var7);
            }

            ManagementLogger.logDeploymentFailed(var1.getName(), var7);
            this.exceptionList.add(var7);
         } catch (UndeploymentException var8) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception in DeploymentHandlerHome: ", var8);
            }

            ManagementLogger.logUndeploymentFailed(var1.getName(), var8);
            this.exceptionList.add(var8);
         }
      }

   }

   private boolean isTargetedToThisServer(DeploymentMBean var1) {
      TargetMBean[] var2 = var1.getTargets();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (this.regularTargets.contains(var2[var3].getName())) {
            return true;
         }
      }

      return false;
   }

   private Object getChangedObject(BeanUpdateEvent.PropertyUpdate var1) {
      int var2 = var1.getUpdateType();
      return var2 == 2 ? var1.getAddedObject() : var1.getRemovedObject();
   }

   private void recalcTargetsIfNeeded(BeanUpdateEvent var1) {
      boolean var2 = false;
      BeanUpdateEvent.PropertyUpdate[] var3 = var1.getUpdateList();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         BeanUpdateEvent.PropertyUpdate var5 = var3[var4];
         Object var6 = this.getChangedObject(var5);
         if (var6 instanceof DeploymentMBean && var6 instanceof TargetMBean) {
            var2 = true;
         }

         if (var2) {
            this.recalculateTargets((DomainMBean)var1.getProposedBean());
         }
      }

   }

   private void recalculateTargets(DomainMBean var1) {
      String var2 = ManagementService.getRuntimeAccess(kernelId).getServerName();
      ServerMBean var3 = var1.lookupServer(var2);
      this.regularTargets.clear();
      this.regularTargets.add(var2);
      String var4 = null;
      ClusterMBean var5 = var3.getCluster();
      if (var5 != null) {
         var4 = var5.getName();
         this.regularTargets.add(var4);
         MigratableTargetMBean[] var6 = var5.getMigratableTargets();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            MigratableTargetMBean var8 = var6[var7];
            ServerMBean[] var9 = var8.getAllCandidateServers();

            for(int var10 = 0; var10 < var9.length; ++var10) {
               if (var2.equals(var9[var10].getName())) {
                  this.regularTargets.add(var8.getName());
                  break;
               }
            }
         }
      }

   }

   private static void debug(String var0) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug(var0);
      }

   }

   private static boolean isResourceDependent(ConfigurationMBean var0) {
      for(int var1 = 0; var1 < RESOURCE_DEPENDENT_DEPLOYMENTS.length; ++var1) {
         if (RESOURCE_DEPENDENT_DEPLOYMENTS[var1].isAssignableFrom(var0.getClass())) {
            return true;
         }
      }

      return false;
   }

   public static boolean addDeploymentHandler(DeploymentHandler var0) {
      if (var0 == null) {
         throw new NullPointerException("null handler");
      } else if (var0 instanceof ResourceDependentDeploymentHandler) {
         debug("Add resource dependent deployment handler: " + var0);
         return getInstance().resourceDependentDeploymentHandlers.add(var0);
      } else {
         debug("Add resource independent deployment handler: " + var0);
         return getInstance().deploymentHandlers.add(var0);
      }
   }

   public static boolean removeDeploymentHandler(DeploymentHandler var0) {
      if (var0 == null) {
         return false;
      } else if (var0 instanceof ResourceDependentDeploymentHandler) {
         debug("Remove resource dependent deployment handler: " + var0);
         return getInstance().resourceDependentDeploymentHandlers.remove(var0);
      } else {
         debug("Remove resource independent deployment handler: " + var0);
         return getInstance().deploymentHandlers.remove(var0);
      }
   }

   private static class Initializer {
      static final DeploymentHandlerHome SINGLETON = new DeploymentHandlerHome();
   }
}
