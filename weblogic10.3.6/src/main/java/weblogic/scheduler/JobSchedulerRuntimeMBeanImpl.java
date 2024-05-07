package weblogic.scheduler;

import java.util.LinkedHashMap;
import java.util.Map;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JobRuntimeMBean;
import weblogic.management.runtime.JobSchedulerRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class JobSchedulerRuntimeMBeanImpl extends RuntimeMBeanDelegate implements JobSchedulerRuntimeMBean {
   private static final boolean DEBUG = false;
   private static JobSchedulerRuntimeMBeanImpl THE_ONE;
   private LinkedHashMap jobMap = new JobHashMap();

   JobSchedulerRuntimeMBeanImpl(RuntimeMBean var1) throws ManagementException {
      super("JobSchedulerRuntime", var1, true);
      THE_ONE = this;
   }

   public static JobSchedulerRuntimeMBeanImpl getInstance() {
      return THE_ONE;
   }

   public synchronized JobRuntimeMBean getJob(String var1) {
      JobRuntimeMBeanImpl var2 = (JobRuntimeMBeanImpl)this.jobMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         try {
            JobRuntimeMBeanImpl var3 = new JobRuntimeMBeanImpl(this, var1, (String)null, -1L);
            this.jobMap.put(var1, var3);
            return var3;
         } catch (ManagementException var4) {
            return null;
         }
      }
   }

   public synchronized JobRuntimeMBean[] getExecutedJobs() {
      JobRuntimeMBean[] var1 = new JobRuntimeMBean[this.jobMap.size()];
      this.jobMap.values().toArray(var1);
      return var1;
   }

   synchronized void timerExecuted(String var1, String var2, long var3) {
      JobRuntimeMBeanImpl var5 = (JobRuntimeMBeanImpl)this.jobMap.get(var1);
      if (var5 != null) {
         var5.update();
      } else {
         try {
            this.jobMap.put(var1, new JobRuntimeMBeanImpl(this, var1, var2, var3));
         } catch (ManagementException var7) {
         }
      }

   }

   private static final class JobHashMap extends LinkedHashMap {
      private static final int JOB_MBEAN_SIZE = initProperty("weblogic.scheduler.JobRuntimeMBeanPoolSize", 50);

      JobHashMap() {
         super((JOB_MBEAN_SIZE + 1) / 2, 2.0F, true);
      }

      private static int initProperty(String var0, int var1) {
         try {
            return Integer.getInteger(var0, var1);
         } catch (SecurityException var3) {
            return var1;
         } catch (NumberFormatException var4) {
            return var1;
         }
      }

      protected boolean removeEldestEntry(Map.Entry var1) {
         if (JOB_MBEAN_SIZE > 0 && this.size() > JOB_MBEAN_SIZE) {
            JobRuntimeMBeanImpl var2 = (JobRuntimeMBeanImpl)var1.getValue();

            try {
               var2.unregister();
            } catch (ManagementException var4) {
            }

            return true;
         } else {
            return false;
         }
      }

      private static void p(String var0) {
         System.out.println("[JobRuntimeMBean] " + var0);
      }
   }
}
