package weblogic.jdbc.common.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.common.resourcepool.ResourcePoolGroup;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JDBCOracleDataSourceInstanceRuntimeMBean;
import weblogic.management.runtime.JDBCOracleDataSourceRuntimeMBean;
import weblogic.management.runtime.ONSClientRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;

public class OracleDataSourceRuntimeImpl extends DataSourceRuntimeMBeanImpl implements JDBCOracleDataSourceRuntimeMBean, HADataSourceRuntime {
   List<HADataSourceInstanceRuntime> racInstances = new ArrayList();
   ONSClientRuntimeMBean onsClient;

   public OracleDataSourceRuntimeImpl(ConnectionPool var1, String var2, RuntimeMBean var3, DescriptorBean var4) throws ManagementException {
      super(var1, var2, var3, var4);
      ((HAConnectionPool)var1).setHADataSourceRuntime(this);
      List var5 = var1.getGroups();

      ResourcePoolGroup var7;
      for(Iterator var6 = var5.iterator(); var6.hasNext(); this.createInstanceRuntime(var7)) {
         var7 = (ResourcePoolGroup)var6.next();
         String var8 = var7.getName();
         if (JdbcDebug.JDBCRAC.isDebugEnabled()) {
            JdbcDebug.JDBCRAC.debug("Creating runtime MBean for instance=" + var8);
         }
      }

      this.onsClient = new ONSClientRuntimeImpl(var1.dsBean, var2, this);
   }

   public HADataSourceInstanceRuntime createInstanceRuntime(ResourcePoolGroup var1) throws ManagementException {
      if (JdbcDebug.JDBCRAC.isDebugEnabled()) {
         JdbcDebug.JDBCRAC.debug("Creating runtime MBean for instance=" + var1.getName());
      }

      OracleDataSourceInstanceRuntimeImpl var2 = new OracleDataSourceInstanceRuntimeImpl(var1, this);
      this.racInstances.add(var2);
      return var2;
   }

   public boolean instanceExists(ResourcePoolGroup var1) {
      Iterator var2 = this.racInstances.iterator();

      ResourcePoolGroup var4;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         HADataSourceInstanceRuntime var3 = (HADataSourceInstanceRuntime)var2.next();
         var4 = var3.getGroup();
      } while(var4 == null || !var4.getName().equals(var1.getName()));

      return true;
   }

   public JDBCOracleDataSourceInstanceRuntimeMBean[] getInstances() {
      return (JDBCOracleDataSourceInstanceRuntimeMBean[])((JDBCOracleDataSourceInstanceRuntimeMBean[])this.racInstances.toArray(new JDBCOracleDataSourceInstanceRuntimeMBean[this.racInstances.size()]));
   }

   public ONSClientRuntimeMBean getONSClientRuntime() {
      return this.onsClient;
   }

   public String getServiceName() {
      return ((HAConnectionPool)this.pool).getServiceName();
   }

   public long getFailedAffinityBasedBorrowCount() {
      return ((HAConnectionPool)this.pool).getFailedAffinityBasedBorrowCount();
   }

   public long getFailedRCLBBasedBorrowCount() {
      return ((HAConnectionPool)this.pool).getFailedRCLBBasedBorrowCount();
   }

   public long getSuccessfulAffinityBasedBorrowCount() {
      return ((HAConnectionPool)this.pool).getSuccessfulAffinityBasedBorrowCount();
   }

   public long getSuccessfulRCLBBasedBorrowCount() {
      return ((HAConnectionPool)this.pool).getSuccessfulRCLBBasedBorrowCount();
   }
}
