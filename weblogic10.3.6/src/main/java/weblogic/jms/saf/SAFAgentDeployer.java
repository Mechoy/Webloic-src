package weblogic.jms.saf;

import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSTargetsListener;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.ModuleCoordinator;
import weblogic.jms.module.TargetingHelper;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.GenericAdminHandler;
import weblogic.messaging.saf.internal.SAFServerService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class SAFAgentDeployer implements GenericAdminHandler {
   private final HashMap agents = new HashMap();
   private HashSet safAgentListeners = new HashSet();
   private HashMap safAgentBeanListeners = new HashMap();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private boolean migrationInProgress = false;

   public boolean isMigrationInProgress() {
      return this.migrationInProgress;
   }

   public void setMigrationInProgress(boolean var1) {
      this.migrationInProgress = var1;
   }

   void start() {
      this.initializeSAFAgentListeners();
   }

   void stop() {
      this.removeSAFAgentListeners();
   }

   public void prepare(DeploymentMBean var1) throws DeploymentException {
      if (var1 instanceof SAFAgentMBean) {
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug("Preparing saf agent " + var1.getName());
         }

         SAFAgentMBean var2 = (SAFAgentMBean)var1;
         boolean var3 = "Receiving-only".equals(var2.getServiceType());
         SAFAgentAdmin var4 = new SAFAgentAdmin(var3);
         var4.prepare(var2);
         this.agents.put(var2.getName(), var4);
         synchronized(this) {
            ImportedDestinationGroup.prepareLocalSAFAgent(var2);

            try {
               this.startAddSAFAgents(var2);
            } catch (BeanUpdateRejectedException var8) {
               throw new DeploymentException(var8);
            }

         }
      }
   }

   public void activate(DeploymentMBean var1) throws DeploymentException {
      if (var1 instanceof SAFAgentMBean) {
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug("Activating saf agent " + var1.getName());
         }

         SAFAgentMBean var2 = (SAFAgentMBean)var1;
         SAFAgentAdmin var3 = (SAFAgentAdmin)this.agents.get(var2.getName());
         var3.activate(var2);
         synchronized(this) {
            ImportedDestinationGroup.activateLocalSAFAgent(var2);
            this.finishAddSAFAgents(var2);
         }
      }
   }

   public void deactivate(DeploymentMBean var1) throws UndeploymentException {
      if (var1 instanceof SAFAgentMBean) {
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug("Deactivating saf agent " + var1.getName());
         }

         SAFAgentMBean var2 = (SAFAgentMBean)var1;
         synchronized(this) {
            ImportedDestinationGroup.deactivateLocalSAFAgent(var2);

            try {
               this.startRemoveSAFAgents(var2);
            } catch (BeanUpdateRejectedException var6) {
               throw new UndeploymentException(var6);
            }
         }

         SAFAgentAdmin var3 = (SAFAgentAdmin)this.agents.get(var2.getName());
         if (var3 != null) {
            var3.deactivate();
         }

      }
   }

   public void unprepare(DeploymentMBean var1) throws UndeploymentException {
      if (var1 instanceof SAFAgentMBean) {
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug("Unpreparing saf agent " + var1.getName());
         }

         SAFAgentMBean var2 = (SAFAgentMBean)var1;
         synchronized(this) {
            ImportedDestinationGroup.unprepareLocalSAFAgent(var2);
            this.finishRemoveSAFAgents(var2);
         }

         SAFAgentAdmin var3 = (SAFAgentAdmin)this.agents.remove(var2.getName());
         if (var3 != null) {
            var3.unprepare();
         }

      }
   }

   public SAFAgentAdmin getAgent(String var1) {
      return (SAFAgentAdmin)this.agents.get(var1);
   }

   SAFAgentAdmin[] getSAFAgents() {
      SAFAgentAdmin[] var1 = new SAFAgentAdmin[this.agents.size()];
      return (SAFAgentAdmin[])((SAFAgentAdmin[])this.agents.values().toArray(var1));
   }

   private void initializeSAFAgentListeners() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      SAFAgentMBean[] var2 = var1.getSAFAgents();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         SAFAgentMBean var4 = var2[var3];
         SAFAgentBeanListener var5 = new SAFAgentBeanListener(var2[var3]);
         var4.addBeanUpdateListener(var5);
         synchronized(this.safAgentBeanListeners) {
            this.safAgentBeanListeners.put(var2[var3].getName(), new DescriptorAndListener(var4, var5));
         }

         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug("Listening for changes to SAFAgent " + var2[var3].getName());
         }
      }

   }

   private void removeSAFAgentListeners() {
      synchronized(this.safAgentBeanListeners) {
         Iterator var2 = this.safAgentBeanListeners.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            DescriptorAndListener var4 = (DescriptorAndListener)this.safAgentBeanListeners.get(var3);
            DescriptorBean var5 = var4.getDescriptorBean();
            SAFAgentBeanListener var6 = var4.getListener();
            var5.removeBeanUpdateListener(var6);
            var6.close();
         }

      }
   }

   public void addSAFAgentListener(JMSTargetsListener var1) {
      synchronized(this.safAgentListeners) {
         this.safAgentListeners.add(var1);
      }
   }

   public void removeSAFAgentListener(JMSTargetsListener var1) {
      synchronized(this.safAgentListeners) {
         this.safAgentListeners.remove(var1);
      }
   }

   private void fireListenersPrepare(DomainMBean var1, SAFAgentMBean var2, int var3) throws BeanUpdateRejectedException {
      boolean var4 = false;
      LinkedList var5 = new LinkedList();
      LinkedList var6 = new LinkedList();
      LinkedList var7 = new LinkedList();
      synchronized(this.safAgentListeners) {
         Iterator var9 = this.safAgentListeners.iterator();

         JMSTargetsListener var10;
         while(var9.hasNext()) {
            var10 = (JMSTargetsListener)var9.next();
            if (var10 instanceof ModuleCoordinator.JMSTargetsListenerImpl) {
               var6.add(var10);
            } else if (var10 instanceof ImportedDestinationGroup) {
               var7.add(var10);
            }
         }

         try {
            var9 = var6.iterator();

            while(var9.hasNext()) {
               var10 = (JMSTargetsListener)var9.next();
               var10.prepareUpdate(var1, var2, var3, this.migrationInProgress);
               var5.addLast(var10);
            }

            var9 = var7.iterator();

            while(var9.hasNext()) {
               var10 = (JMSTargetsListener)var9.next();
               var10.prepareUpdate(var1, var2, var3, this.migrationInProgress);
               var5.addLast(var10);
            }

            var4 = true;
         } finally {
            if (!var4) {
               var9 = var5.iterator();

               while(var9.hasNext()) {
                  JMSTargetsListener var13 = (JMSTargetsListener)var9.next();
                  var13.rollbackUpdate();
               }
            }

         }
      }
   }

   private void fireListenersActivateOrRollback(boolean var1) {
      LinkedList var2 = new LinkedList();
      LinkedList var3 = new LinkedList();
      synchronized(this.safAgentListeners) {
         Iterator var5 = this.safAgentListeners.iterator();

         JMSTargetsListener var6;
         while(var5.hasNext()) {
            var6 = (JMSTargetsListener)var5.next();
            if (var6 instanceof ModuleCoordinator.JMSTargetsListenerImpl) {
               var2.add(var6);
            } else if (var6 instanceof ImportedDestinationGroup) {
               var3.add(var6);
            }
         }

         var5 = var2.iterator();

         while(var5.hasNext()) {
            var6 = (JMSTargetsListener)var5.next();
            if (var1) {
               var6.activateUpdate();
            } else {
               var6.rollbackUpdate();
            }
         }

         var5 = var3.iterator();

         while(var5.hasNext()) {
            var6 = (JMSTargetsListener)var5.next();
            if (var1) {
               var6.activateUpdate();
            } else {
               var6.rollbackUpdate();
            }
         }

      }
   }

   public void startAddSAFAgents(SAFAgentMBean var1) throws BeanUpdateRejectedException {
      DomainMBean var2;
      try {
         var2 = JMSBeanHelper.getDomain(var1);
      } catch (IllegalArgumentException var4) {
         throw new BeanUpdateRejectedException(var4.getMessage(), var4);
      }

      this.fireListenersPrepare(var2, var1, 1);
   }

   public void finishAddSAFAgents(SAFAgentMBean var1) {
      this.fireListenersActivateOrRollback(true);
      SAFAgentBeanListener var2 = new SAFAgentBeanListener(var1);
      var1.addBeanUpdateListener(var2);
      synchronized(this.safAgentBeanListeners) {
         this.safAgentBeanListeners.put(var1.getName(), new DescriptorAndListener(var1, var2));
      }

      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("Listening for changes to SAFAgent " + var1.getName());
      }

   }

   public void startRemoveSAFAgents(SAFAgentMBean var1) throws BeanUpdateRejectedException {
      DomainMBean var2 = null;

      try {
         var2 = JMSBeanHelper.getDomain(var1);
      } catch (IllegalArgumentException var4) {
         throw new BeanUpdateRejectedException(var4.getMessage(), var4);
      }

      this.fireListenersPrepare(var2, var1, 0);
   }

   public void finishRemoveSAFAgents(SAFAgentMBean var1) {
      this.fireListenersActivateOrRollback(true);
      synchronized(this.safAgentBeanListeners) {
         DescriptorAndListener var3 = (DescriptorAndListener)this.safAgentBeanListeners.remove(var1.getName());
         if (var3 == null) {
            return;
         }

         DescriptorBean var4 = var3.getDescriptorBean();
         SAFAgentBeanListener var5 = var3.getListener();
         var4.removeBeanUpdateListener(var5);
         var5.close();
      }

      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("Not listening for changes to removed SAFAgent " + var1.getName());
      }

   }

   private static class DescriptorAndListener {
      private DescriptorBean db;
      private SAFAgentBeanListener listener;

      private DescriptorAndListener(DescriptorBean var1, SAFAgentBeanListener var2) {
         this.db = var1;
         this.listener = var2;
      }

      private DescriptorBean getDescriptorBean() {
         return this.db;
      }

      private SAFAgentBeanListener getListener() {
         return this.listener;
      }

      // $FF: synthetic method
      DescriptorAndListener(DescriptorBean var1, SAFAgentBeanListener var2, Object var3) {
         this(var1, var2);
      }
   }

   private class SAFAgentBeanListener implements BeanUpdateListener {
      private SAFAgentMBean safAgent;
      private SAFAgentMBean proposedSAFAgent;
      private MigratableTargetMBean migratableTarget;
      int numFound;
      boolean safAgentChanged;

      private SAFAgentBeanListener(SAFAgentMBean var2) {
         this.safAgent = var2;
         TargetMBean[] var3 = this.safAgent.getTargets();
         if (var3.length == 1) {
            TargetMBean var4 = var3[0];
            if (var4 instanceof MigratableTargetMBean) {
               this.migratableTarget = (MigratableTargetMBean)var4;
               this.migratableTarget.addBeanUpdateListener(this);
            }

         }
      }

      public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
         if (this.migratableTarget == null) {
            this.safAgentChanged = true;
         } else {
            DescriptorBean var2 = var1.getProposedBean();
            if (var2 instanceof SAFAgentMBean) {
               this.safAgentChanged = true;
            } else {
               this.safAgentChanged = false;
            }
         }

         boolean var10 = false;
         boolean var3 = false;
         BeanUpdateEvent.PropertyUpdate[] var4 = var1.getUpdateList();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            BeanUpdateEvent.PropertyUpdate var6 = var4[var5];
            if (this.safAgentChanged && var6.getPropertyName().equals("Targets")) {
               var3 = true;
               var10 = true;
               break;
            }

            if (!this.safAgentChanged && var6.getPropertyName().equals("UserPreferredServer")) {
               var10 = true;
               break;
            }
         }

         if (var10) {
            ++this.numFound;
            this.proposedSAFAgent = this.safAgentChanged ? (SAFAgentMBean)var1.getProposedBean() : this.safAgent;
            if (var3 && !SAFServerService.getService().isTargetsChangeAllowed(this.proposedSAFAgent)) {
               throw new BeanUpdateRejectedException("Cannot retarget SAF agent " + this.safAgent.getName() + " to a MigrableTarget while it is currently targeted to a Server/Cluster or handling WSRM messages");
            } else {
               DomainMBean var11;
               try {
                  var11 = this.safAgentChanged ? JMSBeanHelper.getDomain(this.proposedSAFAgent) : JMSBeanHelper.getDomain((WebLogicMBean)var1.getProposedBean());
               } catch (IllegalArgumentException var9) {
                  throw new BeanUpdateRejectedException(var9.getMessage(), var9);
               }

               synchronized(SAFAgentDeployer.this) {
                  if (ImportedDestinationGroup.getLocalSAFAgents().get(this.proposedSAFAgent.getName()) == null && !this.isLocallyTargeted(this.proposedSAFAgent)) {
                     SAFAgentDeployer.this.fireListenersPrepare(var11, this.proposedSAFAgent, 2);
                  }

               }
            }
         }
      }

      public void activateUpdate(BeanUpdateEvent var1) {
         if (this.numFound > 0) {
            --this.numFound;
            synchronized(SAFAgentDeployer.this) {
               if (ImportedDestinationGroup.getLocalSAFAgents().get(this.proposedSAFAgent.getName()) == null && !this.isLocallyTargeted(this.proposedSAFAgent)) {
                  SAFAgentDeployer.this.fireListenersActivateOrRollback(true);
               }
            }

            if (this.safAgentChanged) {
               MigratableTargetMBean var2 = this.migratableTarget;
               if (var2 != null) {
                  var2.removeBeanUpdateListener(this);
               }

               this.migratableTarget = null;
               TargetMBean[] var3 = this.safAgent.getTargets();
               if (var3.length == 1) {
                  TargetMBean var4 = var3[0];
                  if (var4 instanceof MigratableTargetMBean) {
                     this.migratableTarget = (MigratableTargetMBean)var4;
                     this.migratableTarget.addBeanUpdateListener(this);
                  }

               }
            }
         }
      }

      public void rollbackUpdate(BeanUpdateEvent var1) {
         if (this.numFound > 0) {
            --this.numFound;
            synchronized(SAFAgentDeployer.this) {
               if (ImportedDestinationGroup.getLocalSAFAgents().get(this.proposedSAFAgent.getName()) == null && !this.isLocallyTargeted(this.proposedSAFAgent)) {
                  SAFAgentDeployer.this.fireListenersActivateOrRollback(false);
               }

            }
         }
      }

      private void close() {
         if (this.migratableTarget != null) {
            this.migratableTarget.removeBeanUpdateListener(this);
         }
      }

      private boolean isLocallyTargeted(SAFAgentMBean var1) {
         ServerMBean var2 = ManagementService.getRuntimeAccess(SAFAgentDeployer.kernelId).getServer();
         return var2 == null ? false : TargetingHelper.isLocallyTargeted(var1, var2.getCluster() == null ? null : var2.getCluster().getName(), var2.getName());
      }

      // $FF: synthetic method
      SAFAgentBeanListener(SAFAgentMBean var2, Object var3) {
         this(var2);
      }
   }
}
