package weblogic.spring.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.SpringTransactionTemplateRuntimeMBean;

public class SpringTransactionTemplateRuntimeMBeanImpl extends SpringBaseRuntimeMBeanImpl implements SpringTransactionTemplateRuntimeMBean {
   private long executions;
   private long failedExecutions;
   private double elapsedTimes;

   public SpringTransactionTemplateRuntimeMBeanImpl(Object var1, String var2, Object var3) throws ManagementException {
      super(var1, var3, var2, false);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringTransactionTemplateRuntimeMBeanImpl(" + var2 + ")");
      }

   }

   public synchronized long getExecuteCount() {
      return this.executions;
   }

   public synchronized long getExecuteFailedCount() {
      return this.failedExecutions;
   }

   public synchronized double getAverageExecuteTime() {
      return this.executions == 0L ? 0.0 : this.elapsedTimes / 1000000.0 / (double)this.executions;
   }

   public synchronized void addExecute(boolean var1, long var2) {
      ++this.executions;
      if (!var1) {
         ++this.failedExecutions;
      }

      this.elapsedTimes += (double)var2;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringTransactionTemplateRuntimeMBeanImpl.addExecute() : " + this.name);
      }

   }
}
