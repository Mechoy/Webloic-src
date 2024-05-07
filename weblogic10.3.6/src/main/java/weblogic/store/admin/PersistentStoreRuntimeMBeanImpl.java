package weblogic.store.admin;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import weblogic.health.HealthMonitorService;
import weblogic.health.HealthState;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.PersistentStoreMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.PersistentStoreConnectionRuntimeMBean;
import weblogic.management.runtime.PersistentStoreRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreConnection;
import weblogic.store.PersistentStoreException;
import weblogic.store.RuntimeUpdater;
import weblogic.store.StoreStatistics;
import weblogic.work.WorkManagerFactory;

public class PersistentStoreRuntimeMBeanImpl extends RuntimeMBeanDelegate implements PersistentStoreRuntimeMBean, RuntimeUpdater {
   private final PersistentStore store;
   private final StoreStatistics statistics;
   private final ArrayList connections = new ArrayList();
   private static final HealthState OK_STATE = new HealthState(0);
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final String HEALTH_NAME_PREFIX = "PersistentStore.";

   public PersistentStoreRuntimeMBeanImpl(PersistentStore var1) throws ManagementException {
      super(var1.getName());
      this.store = var1;
      this.statistics = var1.getStatistics();
      ServerRuntimeMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      var2.addPersistentStoreRuntime(this);
      HealthMonitorService.register("PersistentStore." + this.getName(), this, false);
   }

   public void unregister() throws ManagementException {
      synchronized(this.connections) {
         Iterator var2 = this.connections.iterator();

         while(true) {
            if (!var2.hasNext()) {
               break;
            }

            PersistentStoreConnectionRuntimeMBeanImpl var3 = (PersistentStoreConnectionRuntimeMBeanImpl)var2.next();
            var3.unregister();
         }
      }

      HealthMonitorService.unregister("PersistentStore." + this.getName());
      ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      var1.removePersistentStoreRuntime(this);
      super.unregister();
   }

   public long getAllocatedWindowBufferBytes() {
      return this.statistics.getMappedBufferBytes();
   }

   public long getAllocatedIoBufferBytes() {
      return this.statistics.getIOBufferBytes();
   }

   public long getCreateCount() {
      return this.statistics.getCreateCount();
   }

   public long getReadCount() {
      return this.statistics.getReadCount();
   }

   public long getUpdateCount() {
      return this.statistics.getUpdateCount();
   }

   public long getDeleteCount() {
      return this.statistics.getDeleteCount();
   }

   public long getObjectCount() {
      return this.statistics.getObjectCount();
   }

   public long getPhysicalWriteCount() {
      return this.statistics.getPhysicalWriteCount();
   }

   public PersistentStoreConnectionRuntimeMBean[] getConnections() {
      synchronized(this.connections) {
         PersistentStoreConnectionRuntimeMBean[] var2 = new PersistentStoreConnectionRuntimeMBean[this.connections.size()];
         this.connections.toArray(var2);
         return var2;
      }
   }

   public void addConnection(PersistentStoreConnectionRuntimeMBean var1) {
      synchronized(this.connections) {
         this.connections.add(var1);
      }
   }

   public void removeConnection(PersistentStoreConnection var1) throws ManagementException {
      String var2 = var1.getName();
      synchronized(this.connections) {
         Iterator var4 = this.connections.iterator();

         while(var4.hasNext()) {
            PersistentStoreConnectionRuntimeMBeanImpl var5 = (PersistentStoreConnectionRuntimeMBeanImpl)var4.next();
            if (var5.getName().equals(var2)) {
               var4.remove();
               var5.unregister();
            }
         }

      }
   }

   public HealthState getHealthState() {
      PersistentStoreException var1 = this.store.getFatalException();
      return var1 != null ? new HealthState(3, var1.toString()) : OK_STATE;
   }

   public void setHealthFailed(PersistentStoreException var1) {
      WorkManagerFactory.getInstance().getSystem().schedule(new HealthSetter(var1));
   }

   private boolean isMigratableStore(String var1) {
      DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      Object var3 = var2.lookupFileStore(var1);
      if (var3 == null) {
         var3 = var2.lookupJDBCStore(var1);
      }

      if (var3 == null) {
         System.err.println("No matching store mbean found for =" + var1 + ", must be a default store.");
         return false;
      } else {
         TargetMBean[] var4 = ((PersistentStoreMBean)var3).getTargets();
         if (var4 != null && var4.length == 1) {
         }

         return var4[0] instanceof MigratableTargetMBean;
      }
   }

   private final class HealthSetter implements Runnable {
      private PersistentStoreException exception;

      HealthSetter(PersistentStoreException var2) {
         this.exception = var2;
      }

      public void run() {
         if (PersistentStoreRuntimeMBeanImpl.this.isMigratableStore(PersistentStoreRuntimeMBeanImpl.this.store.getName())) {
            HealthMonitorService.subsystemFailedNonFatal("PersistentStore." + PersistentStoreRuntimeMBeanImpl.this.getName(), this.exception.toString());
         } else {
            HealthMonitorService.subsystemFailed("PersistentStore." + PersistentStoreRuntimeMBeanImpl.this.getName(), this.exception.toString());
         }

      }
   }
}
