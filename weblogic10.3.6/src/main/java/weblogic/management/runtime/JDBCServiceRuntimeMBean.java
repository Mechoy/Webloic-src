package weblogic.management.runtime;

import weblogic.health.HealthFeedback;
import weblogic.health.HealthState;

public interface JDBCServiceRuntimeMBean extends RuntimeMBean, HealthFeedback {
   JDBCDataSourceRuntimeMBean[] getJDBCDataSourceRuntimeMBeans();

   JDBCMultiDataSourceRuntimeMBean[] getJDBCMultiDataSourceRuntimeMBeans();

   JDBCDriverRuntimeMBean[] getJDBCDriverRuntimeMBeans();

   HealthState getHealthState();
}
