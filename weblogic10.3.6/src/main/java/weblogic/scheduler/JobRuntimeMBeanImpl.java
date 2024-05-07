package weblogic.scheduler;

import java.io.Serializable;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JobRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class JobRuntimeMBeanImpl extends RuntimeMBeanDelegate implements JobRuntimeMBean {
   private final String id;
   private final String description;
   private long lastLocalExecutionTime;
   private long localExecutionCount;
   private long period;
   private String state = "Running";

   public JobRuntimeMBeanImpl(RuntimeMBean var1, String var2, String var3, long var4) throws ManagementException {
      super("JobRuntime-" + var2, var1, true);
      this.id = var2;
      this.lastLocalExecutionTime = System.currentTimeMillis();
      this.localExecutionCount = 1L;
      this.period = var4;
      this.description = var3;
   }

   public void cancel() {
      if ((new TimerImpl(this.id)).cancel()) {
         this.state = "Cancelled";
      }

   }

   public Serializable getTimerListener() {
      return (Serializable)(new TimerImpl(this.id)).getListener();
   }

   public long getPeriod() {
      return this.period;
   }

   public long getTimeout() {
      return (new TimerImpl(this.id)).getTimeout();
   }

   public long getLastLocalExecutionTime() {
      return this.lastLocalExecutionTime;
   }

   public long getLocalExecutionCount() {
      return this.localExecutionCount;
   }

   public void update() {
      this.lastLocalExecutionTime = System.currentTimeMillis();
      ++this.localExecutionCount;
   }

   public String getDescription() {
      return this.description;
   }

   public String getID() {
      return this.id;
   }

   public String getState() {
      if (this.state.equals("Cancelled")) {
         return this.state;
      } else {
         try {
            TimerState var1 = TimerBasisAccess.getTimerBasis().getTimerState(this.id);
         } catch (NoSuchObjectLocalException var2) {
            this.state = "Cancelled";
         } catch (TimerException var3) {
            this.state = "Cancelled";
         }

         return this.state;
      }
   }
}
