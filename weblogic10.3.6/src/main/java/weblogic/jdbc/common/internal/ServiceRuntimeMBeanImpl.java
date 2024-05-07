package weblogic.jdbc.common.internal;

import weblogic.health.HealthState;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JDBCDataSourceRuntimeMBean;
import weblogic.management.runtime.JDBCDriverRuntimeMBean;
import weblogic.management.runtime.JDBCMultiDataSourceRuntimeMBean;
import weblogic.management.runtime.JDBCServiceRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class ServiceRuntimeMBeanImpl extends RuntimeMBeanDelegate implements JDBCServiceRuntimeMBean {
   private JDBCService service;

   public ServiceRuntimeMBeanImpl(JDBCService var1) throws ManagementException {
      this.service = var1;
   }

   public JDBCDataSourceRuntimeMBean[] getJDBCDataSourceRuntimeMBeans() {
      JDBCService var10000 = this.service;
      return JDBCService.getJDBCDataSourceRuntimeMBeans();
   }

   public JDBCMultiDataSourceRuntimeMBean[] getJDBCMultiDataSourceRuntimeMBeans() {
      JDBCService var10000 = this.service;
      return JDBCService.getJDBCMultiDataSourceRuntimeMBeans();
   }

   public JDBCDriverRuntimeMBean[] getJDBCDriverRuntimeMBeans() {
      JDBCService var10000 = this.service;
      return JDBCService.getJDBCDriverRuntimeMBeans();
   }

   public HealthState getHealthState() {
      return this.service.getHealthState();
   }
}
