package weblogic.work;

import javax.management.InvalidAttributeValueException;
import weblogic.kernel.ExecuteQueueMBeanStub;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.ExecuteQueueMBean;
import weblogic.management.configuration.KernelMBean;

public final class ExecuteQueueFactory extends WorkManagerFactory {
   private static ExecuteQueueFactory SINGLETON;

   private ExecuteQueueFactory() {
   }

   public static synchronized void initialize(KernelMBean var0) {
      if (SINGLETON == null) {
         SINGLETON = new ExecuteQueueFactory();
         WorkManagerFactory.set(SINGLETON);
         SINGLETON.initializeHere(var0);
      }
   }

   private void initializeHere(KernelMBean var1) {
      if (var1 != null) {
         ExecuteQueueMBean[] var2 = var1.getExecuteQueues();
         int var3 = var2.length;

         while(true) {
            --var3;
            if (var3 < 0) {
               break;
            }

            ExecuteQueueMBean var4 = var2[var3];
            String var5 = var4.getName();
            if (var5.startsWith("wl_bootstrap_")) {
               var5 = var5.substring(13);
            }

            this.create(var5, var4);
         }
      }

      if (this.DEFAULT == null) {
         ExecuteQueueMBeanStub var7 = new ExecuteQueueMBeanStub();
         if (var1 != null && var1.getThreadPoolSize() > 0) {
            try {
               var7.setThreadCount(var1.getThreadPoolSize());
            } catch (Throwable var6) {
            }
         }

         this.create(var7.getName(), var7);
      }

      KernelDelegator var8;
      if (Kernel.isServer()) {
         var8 = new KernelDelegator("weblogic.kernel.Non-Blocking", 3);
         this.byName.put("weblogic.kernel.Non-Blocking", var8);
         this.SYSTEM = new KernelDelegator("weblogic.kernel.System", var1.getSystemThreadPoolSize());
         this.byName.put("weblogic.kernel.System", this.SYSTEM);
         this.REJECTOR = new KernelDelegator("weblogic.Rejector", 2);
      } else {
         this.SYSTEM = this.DEFAULT;
      }

      var8 = new KernelDelegator();
      this.byName.put("direct", var8);
   }

   protected WorkManager create(String var1, int var2, int var3, int var4) {
      return this.create(var1, var3, var4);
   }

   private WorkManager create(String var1, int var2, int var3) {
      int var4 = Math.max(var3, var2);
      return new KernelDelegator(var1, var4);
   }

   private WorkManager create(String var1, ExecuteQueueMBean var2) {
      KernelDelegator var3 = new KernelDelegator(var1, var2);
      if (!"weblogic.kernel.Default".equalsIgnoreCase(var1) && !"default".equalsIgnoreCase(var1)) {
         this.byName.put(var1, var3);
      } else {
         this.DEFAULT = var3;
      }

      return var3;
   }

   public static WorkManager createExecuteQueue(String var0, int var1) {
      ExecuteQueueMBeanStub var2 = new ExecuteQueueMBeanStub();

      try {
         var2.setThreadCount(var1);
         var2.setThreadsIncrease(0);
         var2.setThreadsMaximum(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new AssertionError("Invalid ExecuteQueueMBean attributes specified for " + var0);
      }

      assert SINGLETON != null;

      return SINGLETON.create(var0, var2);
   }
}
