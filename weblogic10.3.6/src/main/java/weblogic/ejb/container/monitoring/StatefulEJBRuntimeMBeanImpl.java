package weblogic.ejb.container.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.EJBCacheRuntimeMBean;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.EJBLockingRuntimeMBean;
import weblogic.management.runtime.StatefulEJBRuntimeMBean;

public final class StatefulEJBRuntimeMBeanImpl extends EJBRuntimeMBeanImpl implements StatefulEJBRuntimeMBean {
   private static final long serialVersionUID = 2679142992650106674L;
   private EJBCacheRuntimeMBean cacheRtMBean;
   private EJBLockingRuntimeMBean lockingRtMBean;

   public StatefulEJBRuntimeMBeanImpl(String var1, String var2, EJBComponentRuntimeMBean var3) throws ManagementException {
      super(var1, var2, var3);
      this.cacheRtMBean = new EJBCacheRuntimeMBeanImpl(var1, this);
      this.lockingRtMBean = new EJBLockingRuntimeMBeanImpl(var1, this);
   }

   public EJBCacheRuntimeMBean getCacheRuntime() {
      return this.cacheRtMBean;
   }

   public EJBLockingRuntimeMBean getLockingRuntime() {
      return this.lockingRtMBean;
   }
}
