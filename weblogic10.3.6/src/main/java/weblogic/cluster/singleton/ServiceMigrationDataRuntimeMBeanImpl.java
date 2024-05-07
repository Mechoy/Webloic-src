package weblogic.cluster.singleton;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServiceMigrationDataRuntimeMBean;

public class ServiceMigrationDataRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ServiceMigrationDataRuntimeMBean {
   private static int count;
   private String serviceName;
   private String coordinatorName;
   private String migratedTo;
   private String migratedFrom;
   private String clusterName;
   private int status;
   private long startTime;
   private long endTime;
   private String[] destinationsAttempted;

   ServiceMigrationDataRuntimeMBeanImpl(RuntimeMBean var1, MigrationData var2) throws ManagementException {
      super("MigrationData-" + var2.getServerName() + var2.getMigrationStartTime() + var1.toString() + count++, var1, true);
      this.initialize(var2);
   }

   public ServiceMigrationDataRuntimeMBeanImpl(MigrationData var1) throws ManagementException {
      super("MigrationData-" + var1.getServerName() + var1.getMigrationStartTime() + "-adminServer" + count++);
      this.initialize(var1);
   }

   void initialize(MigrationData var1) {
      this.serviceName = var1.getServerName();
      this.migratedFrom = var1.getMachineMigratedFrom();
      this.migratedTo = var1.getMachineMigratedTo();
      this.coordinatorName = var1.getClusterMasterName();
      this.clusterName = var1.getClusterName();
      this.status = 1;
      this.startTime = var1.getMigrationStartTime();
      this.destinationsAttempted = new String[1];
      this.destinationsAttempted[0] = this.migratedTo;
   }

   public String getServerName() {
      return this.serviceName;
   }

   public int getStatus() {
      return this.status;
   }

   public String getMigratedFrom() {
      return this.migratedFrom;
   }

   public synchronized String[] getDestinationsAttempted() {
      return this.destinationsAttempted;
   }

   public String getMigratedTo() {
      return this.migratedTo;
   }

   public long getMigrationStartTime() {
      return this.startTime;
   }

   public long getMigrationEndTime() {
      return this.endTime;
   }

   public String getClusterName() {
      return this.clusterName;
   }

   public String getCoordinatorName() {
      return this.coordinatorName;
   }

   public synchronized void update(MigrationData var1) {
      this.migratedFrom = var1.getMachineMigratedFrom();
      this.migratedTo = var1.getMachineMigratedTo();
      this.status = var1.getStatus();
      if (this.status == 1) {
         String[] var2 = this.destinationsAttempted;
         this.destinationsAttempted = new String[this.destinationsAttempted.length + 1];
         System.arraycopy(var2, 0, this.destinationsAttempted, 0, var2.length);
         this.destinationsAttempted[this.destinationsAttempted.length - 1] = this.migratedTo;
      }

      this.endTime = var1.getMigrationEndTime();
   }
}
