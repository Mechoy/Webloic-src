package weblogic.application.internal;

import weblogic.j2ee.ComponentRuntimeMBeanImpl;
import weblogic.management.ManagementException;
import weblogic.management.runtime.AppClientComponentRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;

public class AppClientComponentRuntimeMBeanImpl extends ComponentRuntimeMBeanImpl implements AppClientComponentRuntimeMBean {
   public AppClientComponentRuntimeMBeanImpl(String var1, String var2, RuntimeMBean var3) throws ManagementException {
      super(var1, var2, var3);
   }
}
