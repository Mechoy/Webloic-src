package weblogic.connector.monitoring.outbound;

import weblogic.connector.common.Debug;
import weblogic.connector.outbound.ConnectionInfo;
import weblogic.connector.outbound.ConnectionPool;
import weblogic.connector.outbound.EisMetaData;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ConnectorConnectionRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class ConnectionRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ConnectorConnectionRuntimeMBean {
   private static final long serialVersionUID = -4867452571996075418L;
   private ConnectionInfo connectionInfo = null;
   private ConnectionPool connectionPool = null;

   public ConnectionRuntimeMBeanImpl(ConnectionPool var1, ConnectionInfo var2) throws ManagementException {
      super(var1.getKey() + "_" + var1.getRuntimeMBean().getNextConnectionId() + "@" + System.currentTimeMillis(), var1.getRuntimeMBean(), false);
      this.connectionInfo = var2;
      this.connectionPool = var1;
      this.register();
   }

   public void delete() throws ManagementException {
      if (this.isDeletable()) {
         this.connectionInfo.getConnectionHandler().destroyConnection();
      } else {
         String var1 = Debug.getExceptionCannotDeleteConnection();
         throw new ManagementException(var1);
      }
   }

   public int getActiveHandlesCurrentCount() {
      return this.connectionInfo.getConnectionHandler().getNumActiveConns();
   }

   public int getActiveHandlesHighCount() {
      return this.connectionInfo.getConnectionHandler().getActiveHandlesHighCount();
   }

   public int getHandlesCreatedTotalCount() {
      return this.connectionInfo.getConnectionHandler().getHandlesCreatedTotalCount();
   }

   public long getLastUsage() {
      return this.connectionInfo.getLastUsedTime();
   }

   public String getLastUsageString() {
      return this.connectionInfo.getLastUsageString();
   }

   public String getStackTrace() {
      return this.connectionInfo.getAllocationCallStack();
   }

   public boolean isCurrentlyInUse() {
      return this.connectionInfo.getConnectionHandler().getNumActiveConns() > 0;
   }

   public boolean isInTransaction() {
      return this.connectionInfo.getConnectionHandler().isInTransaction();
   }

   public boolean isShared() {
      return this.connectionInfo.isBeingShared();
   }

   public boolean isIdle() {
      return 0 != this.connectionPool.getHighestWaitSeconds() && this.isCurrentlyInUse() && System.currentTimeMillis() - this.connectionInfo.getLastUsedTime() > (long)this.connectionPool.getHighestWaitSeconds();
   }

   public boolean isDeletable() {
      return !this.isInTransaction() && this.isIdle() && !this.isShared();
   }

   public String getEISProductName() {
      EisMetaData var1 = EisMetaData.getMetaData(this.connectionInfo.getConnectionHandler().getManagedConnection(), this.connectionPool);
      return var1.productName;
   }

   public String getEISProductVersion() {
      EisMetaData var1 = EisMetaData.getMetaData(this.connectionInfo.getConnectionHandler().getManagedConnection(), this.connectionPool);
      return var1.productVersion;
   }

   public String getMaxConnections() {
      EisMetaData var1 = EisMetaData.getMetaData(this.connectionInfo.getConnectionHandler().getManagedConnection(), this.connectionPool);
      return var1.maxConnections;
   }

   public String getUserName() {
      EisMetaData var1 = EisMetaData.getMetaData(this.connectionInfo.getConnectionHandler().getManagedConnection(), this.connectionPool);
      return var1.userName;
   }

   public long getCreationDurationTime() {
      return this.connectionInfo.getCreationDurationTime();
   }

   public long getReserveDurationTime() {
      return this.connectionInfo.getReserveDurationTime();
   }

   public String getTransactionId() {
      return this.connectionInfo.getTransactionId();
   }

   public long getReserveTime() {
      return this.connectionInfo.getReserveTime();
   }

   public String getManagedConnectionFactoryClassName() {
      return this.connectionPool.getManagedConnectionFactoryClassName();
   }

   public String getConnectionFactoryClassName() {
      return this.connectionPool.getConnectionFactoryClassName();
   }

   public boolean testConnection() {
      boolean var1 = false;
      boolean var2 = true;

      int var5;
      try {
         var5 = this.connectionInfo.test();
      } catch (Exception var4) {
         var5 = -1;
      }

      if (var5 != 0) {
         var2 = false;
      }

      return var2;
   }

   public boolean hasError() {
      return this.connectionInfo.hasError();
   }
}
