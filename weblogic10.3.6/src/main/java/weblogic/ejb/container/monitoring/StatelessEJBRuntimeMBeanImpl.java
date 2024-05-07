package weblogic.ejb.container.monitoring;

import weblogic.ejb.container.interfaces.TimerManager;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.EJBPoolRuntimeMBean;
import weblogic.management.runtime.EJBTimerRuntimeMBean;
import weblogic.management.runtime.StatelessEJBRuntimeMBean;

public final class StatelessEJBRuntimeMBeanImpl extends EJBRuntimeMBeanImpl implements StatelessEJBRuntimeMBean {
   private EJBPoolRuntimeMBean poolRtMBean;
   private EJBTimerRuntimeMBean timerRtMBean;

   public StatelessEJBRuntimeMBeanImpl(String var1, String var2, EJBComponentRuntimeMBean var3, TimerManager var4) throws ManagementException {
      super(var1, var2, var3);
      this.poolRtMBean = new EJBPoolRuntimeMBeanImpl(var1, this);
      if (var4 != null) {
         this.timerRtMBean = new EJBTimerRuntimeMBeanImpl(var1, this, var4);
      }

   }

   public EJBPoolRuntimeMBean getPoolRuntime() {
      return this.poolRtMBean;
   }

   public EJBTimerRuntimeMBean getTimerRuntime() {
      return this.timerRtMBean;
   }
}
