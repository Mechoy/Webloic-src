package weblogic.connector.monitoring.outbound;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Hashtable;
import weblogic.connector.common.Debug;
import weblogic.connector.exception.RAOutboundException;
import weblogic.connector.outbound.ConnectionInfo;
import weblogic.connector.outbound.ConnectionPool;
import weblogic.connector.outbound.RAOutboundManager;
import weblogic.connector.security.outbound.SecurityContext;
import weblogic.jdbc.common.internal.ConnectionLeakProfile;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ConnectorConnectionPoolRuntimeMBean;
import weblogic.management.runtime.ConnectorConnectionRuntimeMBean;
import weblogic.management.runtime.LogRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.EISResource;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public final class ConnectionPoolRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ConnectorConnectionPoolRuntimeMBean {
   private ConnectionPool pool;
   private String applicationName;
   private String componentName;
   private RAOutboundManager poolsManager;
   private Hashtable connRuntimeMBeans = new Hashtable();
   private int nextConnectionId = 0;

   public ConnectionPoolRuntimeMBeanImpl(String var1, String var2, ConnectionPool var3, RuntimeMBean var4, RAOutboundManager var5) throws ManagementException {
      super(var3.getKey(), var4, false);
      this.pool = var3;
      this.applicationName = var1;
      this.componentName = var2;
      this.poolsManager = var5;
      this.register();
   }

   public void addConnectionRuntimeMBean(ConnectionInfo var1) {
      ConnectionRuntimeMBeanImpl var2 = this.createConnectionRuntimeMBean(var1);
      this.connRuntimeMBeans.put(var1, var2);
   }

   public void removeConnectionRuntimeMBean(ConnectionInfo var1) {
      ConnectionRuntimeMBeanImpl var2 = (ConnectionRuntimeMBeanImpl)this.connRuntimeMBeans.get(var1);
      this.connRuntimeMBeans.remove(var1);
      this.destroyConnectionRuntimeMBean(var2);
   }

   private ConnectionRuntimeMBeanImpl createConnectionRuntimeMBean(ConnectionInfo var1) {
      final ConnectionInfo var2 = var1;
      ConnectionRuntimeMBeanImpl var3 = null;
      AuthenticatedSubject var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      try {
         var3 = (ConnectionRuntimeMBeanImpl)SecurityServiceManager.runAs(var4, var4, new PrivilegedAction() {
            public Object run() {
               try {
                  return new ConnectionRuntimeMBeanImpl(ConnectionPoolRuntimeMBeanImpl.this.pool, var2);
               } catch (Exception var2x) {
                  Debug.logInitConnRTMBeanError(ConnectionPoolRuntimeMBeanImpl.this.pool.getName(), var2x.toString());
                  return null;
               }
            }
         });
      } catch (Exception var6) {
         Debug.logInitConnRTMBeanError(this.pool.getName(), var6.toString());
      }

      return var3;
   }

   private void destroyConnectionRuntimeMBean(ConnectionRuntimeMBeanImpl var1) {
      final ConnectionRuntimeMBeanImpl var2 = var1;

      try {
         AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         SecurityServiceManager.runAs(var3, var3, new PrivilegedAction() {
            public Object run() {
               try {
                  if (var2 != null) {
                     var2.unregister();
                  }

                  return null;
               } catch (Exception var2x) {
                  Debug.logUnregisterConnRTMBeanError(ConnectionPoolRuntimeMBeanImpl.this.pool.getName(), var2x.toString());
                  return null;
               }
            }
         });
      } catch (Exception var4) {
         Debug.logUnregisterConnRTMBeanError(this.pool.getName(), var4.toString());
      }

   }

   public String getPoolName() {
      return this.pool.getName();
   }

   public String getJNDIName() {
      return this.pool.getJNDIName();
   }

   public String getKey() {
      return this.pool.getKey();
   }

   public String getConnectionFactoryName() {
      return this.pool.getConnectionFactoryName();
   }

   public String getResourceAdapterLinkRefName() {
      return this.pool.getRALinkRefName();
   }

   public boolean isLoggingEnabled() {
      return this.pool.isLoggingEnabled();
   }

   public String getLogFileName() {
      return this.pool.getLogFileName();
   }

   public LogRuntimeMBean getLogRuntime() {
      return this.pool.getLogRuntime();
   }

   public String getTransactionSupport() {
      return this.pool.getTransactionSupport();
   }

   public int getMaxCapacity() {
      return this.pool.getMaxCapacity();
   }

   public int getMaxIdleTime() {
      return this.getInactiveResourceTimeoutSeconds();
   }

   public int getInactiveResourceTimeoutSeconds() {
      return this.pool.getInactiveResourceTimeoutSeconds();
   }

   public boolean getConnectionProfilingEnabled() {
      return this.pool.getConnectionProfilingEnabled();
   }

   public int getNumberDetectedLeaks() {
      return this.pool.getNumLeaked();
   }

   public int getNumberDetectedIdle() {
      return this.pool.getNumIdleDetected();
   }

   public int getInitialCapacity() {
      return this.pool.getInitialCapacity();
   }

   public int getCapacityIncrement() {
      return this.pool.getCapacityIncrement();
   }

   public boolean isShrinkingEnabled() {
      return this.pool.isShrinkingEnabled();
   }

   /** @deprecated */
   public int getShrinkPeriodMinutes() {
      return this.getShrinkFrequencySeconds() / 60;
   }

   public int getShrinkFrequencySeconds() {
      return this.pool.getShrinkFrequencySeconds();
   }

   public int getActiveConnectionsCurrentCount() {
      return this.pool.getNumReserved();
   }

   public int getActiveConnectionsHighCount() {
      return this.pool.getHighestNumReserved();
   }

   public int getFreeConnectionsCurrentCount() {
      return this.pool.getNumAvailable();
   }

   public int getFreeConnectionsHighCount() {
      return this.pool.getHighestNumAvailable();
   }

   public int getAverageActiveUsage() {
      return this.pool.getAverageReserved();
   }

   public int getShrinkCountDownTime() {
      return this.pool.getTimeToNextShrinkOperation();
   }

   public long getLastShrinkTime() {
      return this.pool.getLastShrinkTime();
   }

   public int getRecycledTotal() {
      return this.pool.getNumRecycled();
   }

   public int getConnectionsCreatedTotalCount() {
      return this.pool.getTotalNumAllocated();
   }

   public int getConnectionsMatchedTotalCount() {
      return this.pool.getConnectionsMatchedTotalCount();
   }

   public int getConnectionsDestroyedTotalCount() {
      return this.pool.getTotalNumDestroyed();
   }

   public int getConnectionsDestroyedByErrorTotalCount() {
      return this.pool.getConnectionsDestroyedByErrorCount();
   }

   public int getConnectionsDestroyedByShrinkingTotalCount() {
      return this.pool.getResourcesDestroyedByShrinkingCount();
   }

   public int getConnectionsRejectedTotalCount() {
      return this.pool.getConnectionsRejectedTotalCount();
   }

   public int getConnectionLeakProfileCount() {
      return this.pool.getLeakProfileCount();
   }

   public ConnectionLeakProfile[] getConnectionLeakProfiles(int var1, int var2) {
      return this.pool.getConnectionLeakProfiles(var1, var2);
   }

   public ConnectionLeakProfile[] getConnectionLeakProfiles() {
      return this.pool.getConnectionLeakProfiles();
   }

   public int getConnectionIdleProfileCount() {
      return this.pool.getIdleProfileCount();
   }

   public ConnectionLeakProfile[] getConnectionIdleProfiles(int var1, int var2) {
      return this.pool.getConnectionIdleProfiles(var1, var2);
   }

   public ConnectionLeakProfile[] getConnectionIdleProfiles() {
      return this.pool.getConnectionIdleProfiles();
   }

   public int getNumWaitersCurrentCount() {
      return this.pool.getNumWaiters();
   }

   public int getNumUnavailableCurrentCount() {
      return this.pool.getNumUnavailable();
   }

   public int getNumUnavailableHighCount() {
      return this.pool.getHighestNumUnavailable();
   }

   public boolean isProxyOn() {
      return this.pool.isProxyOn();
   }

   public ConnectorConnectionRuntimeMBean[] getConnections() {
      Collection var1 = this.connRuntimeMBeans.values();
      return (ConnectorConnectionRuntimeMBean[])((ConnectorConnectionRuntimeMBean[])var1.toArray(new ConnectorConnectionRuntimeMBean[var1.size()]));
   }

   public String getConnectorEisType() {
      return this.pool.getOutboundInfo().getEisType();
   }

   public String getEISResourceId() {
      EISResource var1 = SecurityContext.getPoolEISResource(this.applicationName, this.componentName, this.pool.getOutboundInfo());
      return var1.toString();
   }

   int getNextConnectionId() {
      synchronized(this) {
         ++this.nextConnectionId;
         return this.nextConnectionId;
      }
   }

   public long getCloseCount() {
      return this.pool.getCloseCount();
   }

   public long getFreePoolSizeHighWaterMark() {
      return this.pool.getFreePoolSizeHighWaterMark();
   }

   public long getFreePoolSizeLowWaterMark() {
      return this.pool.getFreePoolSizeLowWaterMark();
   }

   public long getCurrentCapacity() {
      return (long)this.pool.getCurrCapacity();
   }

   public long getPoolSizeHighWaterMark() {
      return this.pool.getPoolSizeHighWaterMark();
   }

   public long getPoolSizeLowWaterMark() {
      return this.pool.getPoolSizeLowWaterMark();
   }

   public String getManagedConnectionFactoryClassName() {
      return this.pool.getManagedConnectionFactoryClassName();
   }

   public String getConnectionFactoryClassName() {
      return this.pool.getConnectionFactoryClassName();
   }

   public long getNumWaiters() {
      return (long)this.pool.getNumWaiters();
   }

   public long getHighestNumWaiters() {
      return (long)this.pool.getHighestNumWaiters();
   }

   public boolean isTestable() {
      return this.pool.isTestable();
   }

   public boolean testPool() {
      return this.pool.testPool();
   }

   public String getState() {
      return this.pool.getState();
   }

   public String getMCFClassName() {
      return this.pool.getManagedConnectionFactoryClassName();
   }

   public String getResourceLink() {
      return this.pool.getResourceLink();
   }

   public void forceLogRotation() throws ManagementException {
      this.pool.forceLogRotation();
   }

   public void ensureLogOpened() throws ManagementException {
      this.pool.ensureLogOpened();
   }

   public void forceReset() throws ManagementException {
      try {
         this.poolsManager.forceResetPool(this.getKey());
      } catch (RAOutboundException var2) {
         throw new ManagementException(var2);
      }
   }

   public boolean reset() throws ManagementException {
      try {
         return this.poolsManager.resetPool(this.getKey());
      } catch (RAOutboundException var2) {
         throw new ManagementException(var2);
      }
   }
}
