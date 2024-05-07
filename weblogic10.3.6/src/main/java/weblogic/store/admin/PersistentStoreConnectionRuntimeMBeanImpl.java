package weblogic.store.admin;

import weblogic.management.ManagementException;
import weblogic.management.runtime.PersistentStoreConnectionRuntimeMBean;
import weblogic.management.runtime.PersistentStoreRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.store.OperationStatistics;
import weblogic.store.PersistentStoreConnection;

public class PersistentStoreConnectionRuntimeMBeanImpl extends RuntimeMBeanDelegate implements PersistentStoreConnectionRuntimeMBean {
   private final OperationStatistics statistics;

   public PersistentStoreConnectionRuntimeMBeanImpl(PersistentStoreConnection var1, PersistentStoreRuntimeMBean var2) throws ManagementException {
      super(var1.getName(), var2, true, "Connections");
      this.statistics = var1.getStatistics();
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
}
