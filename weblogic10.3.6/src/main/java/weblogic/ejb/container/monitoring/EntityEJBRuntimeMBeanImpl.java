package weblogic.ejb.container.monitoring;

import weblogic.ejb.container.interfaces.TimerManager;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EJBCacheRuntimeMBean;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.EJBLockingRuntimeMBean;
import weblogic.management.runtime.EJBPoolRuntimeMBean;
import weblogic.management.runtime.EJBTimerRuntimeMBean;
import weblogic.management.runtime.EntityEJBRuntimeMBean;
import weblogic.management.runtime.QueryCacheRuntimeMBean;

public final class EntityEJBRuntimeMBeanImpl extends EJBRuntimeMBeanImpl implements EntityEJBRuntimeMBean {
   private EJBPoolRuntimeMBean poolRtMBean;
   private EJBCacheRuntimeMBean cacheRtMBean;
   private EJBLockingRuntimeMBean lockingRtMBean;
   private EJBTimerRuntimeMBean timerRtMBean;
   private QueryCacheRuntimeMBean queryCacheRtMBean;

   public EntityEJBRuntimeMBeanImpl(String var1, String var2, EJBComponentRuntimeMBean var3, boolean var4, TimerManager var5) throws ManagementException {
      super(var1, var2, var3);
      this.poolRtMBean = new EJBPoolRuntimeMBeanImpl(var1, this);
      this.cacheRtMBean = new EJBCacheRuntimeMBeanImpl(var1, this);
      if (var4) {
         this.lockingRtMBean = new EJBLockingRuntimeMBeanImpl(var1, this);
      }

      if (var5 != null) {
         this.timerRtMBean = new EJBTimerRuntimeMBeanImpl(var1, this, var5);
      }

      this.queryCacheRtMBean = new QueryCacheRuntimeMBeanImpl(var1, this);
   }

   public EJBPoolRuntimeMBean getPoolRuntime() {
      return this.poolRtMBean;
   }

   public EJBCacheRuntimeMBean getCacheRuntime() {
      return this.cacheRtMBean;
   }

   public EJBLockingRuntimeMBean getLockingRuntime() {
      return this.lockingRtMBean;
   }

   public EJBTimerRuntimeMBean getTimerRuntime() {
      return this.timerRtMBean;
   }

   public QueryCacheRuntimeMBean getQueryCacheRuntime() {
      return this.queryCacheRtMBean;
   }
}
