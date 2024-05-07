package weblogic.jdbc.common.internal;

import weblogic.common.resourcepool.ResourcePoolGroup;
import weblogic.jdbc.common.rac.RACInstance;
import weblogic.jdbc.common.rac.RACModule;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JDBCOracleDataSourceInstanceRuntimeMBean;
import weblogic.management.runtime.JDBCOracleDataSourceRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class OracleDataSourceInstanceRuntimeImpl extends RuntimeMBeanDelegate implements JDBCOracleDataSourceInstanceRuntimeMBean, HADataSourceInstanceRuntime {
   ResourcePoolGroup group;
   String instanceName;
   HAConnectionPool hacp;
   String signature;

   public OracleDataSourceInstanceRuntimeImpl(ResourcePoolGroup var1, RuntimeMBean var2) throws ManagementException {
      super(var1.getName(), var2, true);
      this.group = var1;
      this.instanceName = var1.getName();
      this.hacp = (HAConnectionPool)((OracleDataSourceRuntimeImpl)var2).pool;
      RACModule var3 = this.hacp.getRACModule();
      RACInstance var4 = var3.getRACInstance(this.instanceName);
      if (var4 == null) {
         this.signature = "instance=" + this.instanceName + ",service=" + ((JDBCOracleDataSourceRuntimeMBean)var2).getServiceName();
      } else {
         this.signature = "instance=" + var4.getInstance() + ",service=" + var4.getService() + ",database=" + var4.getDatabase() + ",host=" + var4.getHost();
      }

   }

   public ResourcePoolGroup getGroup() {
      return this.group;
   }

   public int getActiveConnectionsCurrentCount() {
      return this.group.getNumReserved();
   }

   public int getConnectionsTotalCount() {
      return this.group.getTotalNumAllocated();
   }

   public int getCurrCapacity() {
      return this.group.getCurrCapacity();
   }

   public int getCurrentWeight() {
      return this.hacp.getWeightForInstance(this.instanceName);
   }

   public String getInstanceName() {
      return this.instanceName;
   }

   public int getNumAvailable() {
      return this.group.getNumAvailable();
   }

   public int getNumUnavailable() {
      return this.group.getNumUnavailable();
   }

   public long getReserveRequestCount() {
      return (long)this.group.getNumReserveRequests();
   }

   public String getSignature() {
      return this.signature;
   }

   public String getState() {
      return this.group.getState();
   }

   public boolean isEnabled() {
      return this.group.isEnabled();
   }

   public boolean isAffEnabled() {
      return this.hacp.getAffForInstance(this.instanceName);
   }

   private void debug(String var1) {
      System.out.println("OracleDataSourceInstanceRuntimeImpl: name=" + this.name + ": " + var1);
   }
}
