package weblogic.spring.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.SpringViewRuntimeMBean;

public class SpringViewRuntimeMBeanImpl extends SpringBaseRuntimeMBeanImpl implements SpringViewRuntimeMBean {
   private long viewRenderCount;
   private long failedViewRenders;
   private double elapsedTimes;

   public SpringViewRuntimeMBeanImpl(Object var1, String var2, Object var3) throws ManagementException {
      super(var1, var3, var2, false);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringViewRuntimeMBeanImpl(" + var2 + ")");
      }

   }

   public synchronized long getRenderCount() {
      return this.viewRenderCount;
   }

   public synchronized long getRenderFailedCount() {
      return this.failedViewRenders;
   }

   public synchronized double getAverageRenderTime() {
      return this.viewRenderCount == 0L ? 0.0 : this.elapsedTimes / 1000000.0 / (double)this.viewRenderCount;
   }

   public synchronized void addRender(boolean var1, long var2) {
      ++this.viewRenderCount;
      if (!var1) {
         ++this.failedViewRenders;
      }

      this.elapsedTimes += (double)var2;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringViewRuntimeMBeanImpl.addRender() : " + this.name);
      }

   }
}
