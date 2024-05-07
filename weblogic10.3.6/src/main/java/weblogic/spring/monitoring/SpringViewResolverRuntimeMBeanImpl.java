package weblogic.spring.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.SpringViewResolverRuntimeMBean;

public class SpringViewResolverRuntimeMBeanImpl extends SpringBaseRuntimeMBeanImpl implements SpringViewResolverRuntimeMBean {
   private long viewResolveCount;
   private long failedViewResolves;
   private double elapsedTimes;

   public SpringViewResolverRuntimeMBeanImpl(Object var1, String var2, Object var3) throws ManagementException {
      super(var1, var3, var2, false);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringViewResolverRuntimeMBeanImpl(" + var2 + ")");
      }

   }

   public synchronized long getResolveViewNameCount() {
      return this.viewResolveCount;
   }

   public synchronized long getResolveViewNameFailedCount() {
      return this.failedViewResolves;
   }

   public synchronized double getAverageResolveViewNameTime() {
      return this.viewResolveCount == 0L ? 0.0 : this.elapsedTimes / 1000000.0 / (double)this.viewResolveCount;
   }

   public synchronized void addViewResolved(boolean var1, long var2) {
      ++this.viewResolveCount;
      if (!var1) {
         ++this.failedViewResolves;
      }

      this.elapsedTimes += (double)var2;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringViewResolverRuntimeMBeanImpl.addViewResolved() : " + this.name);
      }

   }
}
