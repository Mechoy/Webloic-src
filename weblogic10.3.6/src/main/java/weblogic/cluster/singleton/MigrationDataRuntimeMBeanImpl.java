package weblogic.cluster.singleton;

import weblogic.management.ManagementException;
import weblogic.management.runtime.MigrationDataRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class MigrationDataRuntimeMBeanImpl extends RuntimeMBeanDelegate implements MigrationDataRuntimeMBean {
   private static int count;
   private String serverName;
   private String clusterMasterName;
   private String machineMigratedTo;
   private String machineMigratedFrom;
   private String clusterName;
   private int status;
   private long startTime;
   private long endTime;
   private String[] machinesAttempted;

   MigrationDataRuntimeMBeanImpl(RuntimeMBean var1, MigrationData var2) throws ManagementException {
      super("MigrationData-" + var2.getServerName() + var2.getMigrationStartTime(), var1, true);
      this.initialize(var2);
   }

   public MigrationDataRuntimeMBeanImpl(MigrationData var1) throws ManagementException {
      super("MigrationData-" + var1.getServerName() + var1.getMigrationStartTime());
      this.initialize(var1);
   }

   void initialize(MigrationData var1) {
      this.serverName = var1.getServerName();
      this.machineMigratedFrom = var1.getMachineMigratedFrom();
      this.machineMigratedTo = var1.getMachineMigratedTo();
      this.clusterMasterName = var1.getClusterMasterName();
      this.clusterName = var1.getClusterName();
      this.status = 1;
      this.startTime = var1.getMigrationStartTime();
      this.machinesAttempted = new String[1];
      this.machinesAttempted[0] = this.machineMigratedTo;
   }

   private static synchronized int getCount() {
      return count++;
   }

   public String getServerName() {
      return this.serverName;
   }

   public int getStatus() {
      return this.status;
   }

   public String getMachineMigratedFrom() {
      return this.machineMigratedFrom;
   }

   public synchronized String[] getMachinesAttempted() {
      return this.machinesAttempted;
   }

   public String getMachineMigratedTo() {
      return this.machineMigratedTo;
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

   public String getClusterMasterName() {
      return this.clusterMasterName;
   }

   public synchronized void update(MigrationData var1) {
      this.machineMigratedFrom = var1.getMachineMigratedFrom();
      this.machineMigratedTo = var1.getMachineMigratedTo();
      this.status = var1.getStatus();
      if (this.status == 1) {
         String[] var2 = this.machinesAttempted;
         this.machinesAttempted = new String[this.machinesAttempted.length + 1];
         System.arraycopy(var2, 0, this.machinesAttempted, 0, var2.length);
         this.machinesAttempted[this.machinesAttempted.length - 1] = this.machineMigratedTo;
      }

      this.endTime = var1.getMigrationEndTime();
   }
}
