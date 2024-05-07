package weblogic.jdbc.common.internal;

import weblogic.management.ManagementException;
import weblogic.management.runtime.JDBCDriverRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class DriverRuntimeMBeanImpl extends RuntimeMBeanDelegate implements JDBCDriverRuntimeMBean {
   public DriverRuntimeMBeanImpl(String var1) throws ManagementException {
      super(var1);
   }
}
