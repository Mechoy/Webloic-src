package weblogic.management.runtime;

import weblogic.jdbc.common.internal.ConnectionLeakProfile;
import weblogic.management.ManagementException;

public interface ConnectorConnectionPoolRuntimeMBean extends LogRuntimeMBean {
   String getPoolName();

   /** @deprecated */
   String getJNDIName();

   String getConnectionFactoryName();

   /** @deprecated */
   String getResourceAdapterLinkRefName();

   boolean isLoggingEnabled();

   String getLogFileName();

   LogRuntimeMBean getLogRuntime();

   String getTransactionSupport();

   int getMaxCapacity();

   int getInitialCapacity();

   int getCapacityIncrement();

   boolean isShrinkingEnabled();

   int getShrinkPeriodMinutes();

   int getActiveConnectionsCurrentCount();

   int getActiveConnectionsHighCount();

   int getFreeConnectionsCurrentCount();

   int getFreeConnectionsHighCount();

   int getAverageActiveUsage();

   int getShrinkCountDownTime();

   int getRecycledTotal();

   int getConnectionsCreatedTotalCount();

   int getConnectionsMatchedTotalCount();

   int getConnectionsDestroyedTotalCount();

   int getConnectionsRejectedTotalCount();

   ConnectorConnectionRuntimeMBean[] getConnections();

   /** @deprecated */
   int getConnectionIdleProfileCount();

   /** @deprecated */
   ConnectionLeakProfile[] getConnectionIdleProfiles(int var1, int var2);

   /** @deprecated */
   ConnectionLeakProfile[] getConnectionLeakProfiles();

   /** @deprecated */
   ConnectionLeakProfile[] getConnectionIdleProfiles();

   /** @deprecated */
   int getConnectionLeakProfileCount();

   /** @deprecated */
   ConnectionLeakProfile[] getConnectionLeakProfiles(int var1, int var2);

   /** @deprecated */
   boolean getConnectionProfilingEnabled();

   int getMaxIdleTime();

   /** @deprecated */
   int getNumberDetectedIdle();

   /** @deprecated */
   int getNumberDetectedLeaks();

   String getConnectorEisType();

   String getEISResourceId();

   long getCloseCount();

   long getFreePoolSizeHighWaterMark();

   long getFreePoolSizeLowWaterMark();

   long getCurrentCapacity();

   long getPoolSizeHighWaterMark();

   long getPoolSizeLowWaterMark();

   String getManagedConnectionFactoryClassName();

   String getConnectionFactoryClassName();

   long getNumWaiters();

   long getHighestNumWaiters();

   boolean isTestable();

   long getLastShrinkTime();

   int getConnectionsDestroyedByErrorTotalCount();

   int getConnectionsDestroyedByShrinkingTotalCount();

   int getNumWaitersCurrentCount();

   int getNumUnavailableCurrentCount();

   int getNumUnavailableHighCount();

   boolean isProxyOn();

   boolean testPool();

   String getState();

   String getMCFClassName();

   String getResourceLink();

   String getKey();

   void forceReset() throws ManagementException;

   boolean reset() throws ManagementException;
}
