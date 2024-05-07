package weblogic.kernel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.management.InvalidAttributeValueException;
import weblogic.common.internal.VersionInfoFactory;
import weblogic.management.configuration.ExecuteQueueMBean;
import weblogic.management.configuration.KernelDebugMBean;
import weblogic.management.configuration.KernelMBean;
import weblogic.protocol.ClientEnvironment;
import weblogic.utils.StackTraceUtils;
import weblogic.work.ExecuteQueueFactory;

public final class Kernel extends KernelStatus {
   private static final String ALLOW_QUEUE_THROTTLE = "weblogic.kernel.allowQueueThrottling";
   private static final int DIRECT = 0;
   private static KernelMBean config;
   private static ExecuteThreadManager[] queues = new ExecuteThreadManager[1];
   private static final HashMap policyNameAlias = new HashMap();
   private static final ArrayList applicationQueueNames = new ArrayList();
   private static int defaultDispatchIndex;
   private static boolean isTracingEnabled = false;
   private static final boolean allowQueueThrottling = initAllowThrottleProp();

   private static final boolean initAllowThrottleProp() {
      return isApplet() ? false : Boolean.getBoolean("weblogic.kernel.allowQueueThrottling");
   }

   public static final boolean isTracingEnabled() {
      return isTracingEnabled;
   }

   public static boolean isInitialized() {
      return config != null;
   }

   public static synchronized void ensureInitialized() {
      initialized();
      if (config == null) {
         try {
            KernelMBeanStub var0 = new KernelMBeanStub();
            initialize(var0);
         } catch (Throwable var1) {
            throw new InternalError("error initializing kernel caused by: " + StackTraceUtils.throwable2StackTrace(var1));
         }
      }

   }

   public static synchronized void initialize(KernelMBean var0) {
      initialized();
      if (config != null) {
         throw new AssertionError("Kernel was initialized more than once");
      } else {
         config = var0;
         setIsConfigured();
         isTracingEnabled = config.getTracingEnabled();
         VersionInfoFactory.initialize(isServer());
         ClientEnvironment.loadEnvironment();
         policyNameAlias.put("weblogic.kernel.Default", "default");
         if (var0.getUse81StyleExecuteQueues()) {
            if (isServer()) {
               KernelLogger.logInitializingKernelDelegator();
            }

            ExecuteQueueFactory.initialize(var0);
         }

         defaultDispatchIndex = getDispatchPolicyIndex("weblogic.kernel.Default");
      }
   }

   public static KernelMBean getConfig() {
      if (!isServer() && !isInitialized()) {
         ensureInitialized();
      }

      return config;
   }

   public static KernelDebugMBean getDebug() {
      return config.getKernelDebug();
   }

   public static ExecuteThreadManager getExecuteThreadManager(String var0) {
      int var1 = getDispatchPolicyIndex(var0);
      return queues[var1];
   }

   static ExecuteThreadManager[] getExecuteThreadManagers() {
      return queues;
   }

   public static void execute(ExecuteRequest var0) {
      execute(var0, false);
   }

   public static void execute(ExecuteRequest var0, boolean var1) {
      queues[defaultDispatchIndex].execute(var0, var1);
   }

   public static void execute(ExecuteRequest var0, int var1) {
      execute(var0, var1, false);
   }

   public static void execute(ExecuteRequest var0, int var1, boolean var2) {
      if (var1 == 0) {
         executeLocally(var0);
      } else {
         queues[var1].execute(var0, var2);
      }

   }

   private static void executeLocally(ExecuteRequest var0) {
      try {
         var0.execute((ExecuteThread)null);
      } catch (ThreadDeath var2) {
         throw var2;
      } catch (ExecuteThreadManager.ShutdownError var3) {
         throw var3;
      } catch (Throwable var4) {
         if (!isApplet()) {
            KernelLogger.logExecuteFailed(var4);
         } else {
            var4.printStackTrace();
         }
      }

   }

   public static void execute(ExecuteRequest var0, String var1) {
      execute(var0, var1, false);
   }

   public static void execute(ExecuteRequest var0, String var1, boolean var2) {
      execute(var0, getDispatchPolicyIndex(var1), var2);
   }

   public static void executeIfIdle(ExecuteRequest var0, int var1) {
      if (var1 == 0 || !queues[var1].executeIfIdle(var0)) {
         Thread var2 = Thread.currentThread();
         if (var2 instanceof ExecuteThread) {
            ((ExecuteThread)var2).execute(var0);
         } else {
            try {
               var0.execute((ExecuteThread)null);
            } catch (Exception var4) {
               throw new InternalError("Error executing the request on a Non-kernel Thread: " + StackTraceUtils.throwable2StackTrace(var4));
            }
         }
      }

   }

   public static void executeIfIdle(ExecuteRequest var0, String var1) {
      executeIfIdle(var0, getDispatchPolicyIndex(var1));
   }

   public static int getExecuteQueueDepth() {
      return queues[defaultDispatchIndex].getExecuteQueueDepth();
   }

   public static int getExecuteQueueDepth(int var0) {
      return var0 == 0 ? 0 : queues[var0].getExecuteQueueDepth();
   }

   public static int getDispatchPolicyIndex(String var0) {
      if ("direct".equalsIgnoreCase(var0)) {
         return 0;
      } else {
         int var1 = queues.length;

         for(int var2 = 1; var2 < var1; ++var2) {
            String var3 = queues[var2].getName();
            if (var3.equalsIgnoreCase(var0) || var3.equalsIgnoreCase((String)policyNameAlias.get(var0))) {
               return var2;
            }
         }

         ensureInitialized();
         return getDispatchPolicyIndex("weblogic.kernel.Default");
      }
   }

   public static boolean isDispatchPolicy(String var0) {
      if ("direct".equalsIgnoreCase(var0)) {
         return true;
      } else {
         int var1 = queues.length;

         for(int var2 = 1; var2 < var1; ++var2) {
            String var3 = queues[var2].getName();
            if (var3.equalsIgnoreCase(var0) || var3.equalsIgnoreCase((String)policyNameAlias.get(var0))) {
               return true;
            }
         }

         return false;
      }
   }

   public static void addExecuteQueue(String var0, int var1) {
      addInternalExecuteQueue(var0, var1, 0, var1);
   }

   public static void addExecuteQueue(String var0, String var1, int var2) {
      policyNameAlias.put(var0, var1);
      addInternalExecuteQueue(var0, var2, 0, var2);
   }

   public static void addExecuteQueue(String var0, int var1, int var2, int var3) {
      addInternalExecuteQueue(var0, var1, var2, var3);
   }

   private static void addInternalExecuteQueue(String var0, int var1, int var2, int var3) {
      ExecuteQueueMBeanStub var4 = new ExecuteQueueMBeanStub();

      try {
         var4.setThreadCount(var1);
         var4.setThreadsIncrease(var2);
         var4.setThreadsMaximum(var3);
      } catch (InvalidAttributeValueException var6) {
         throw new AssertionError("Invalid ExecuteQueueMBean attributes specified for " + var0);
      }

      addExecuteQueue(var0, var4, false);
   }

   private static void addExecuteQueue(String var0, ExecuteQueueMBean var1) {
      addExecuteQueue(var0, var1, false);
   }

   public static void addExecuteQueue(String var0, ExecuteQueueMBean var1, boolean var2) {
      if (var2) {
         if (!applicationQueueNames.contains(var0)) {
            applicationQueueNames.add(var0);
         }
      } else {
         applicationQueueNames.remove(var0);
         applicationQueueNames.remove(policyNameAlias.get(var0));
      }

      if (!isDispatchPolicy(var0)) {
         Class var3 = Kernel.class;
         synchronized(Kernel.class) {
            if (!isDispatchPolicy(var0)) {
               ExecuteThreadManager var4 = new ExecuteThreadManager(var0, var1);
               KernelEnvironment.getKernelEnvironment().addExecuteQueueRuntime(var4);
               ExecuteThreadManager[] var5 = new ExecuteThreadManager[queues.length + 1];
               System.arraycopy(queues, 0, var5, 0, queues.length);
               var5[queues.length] = var4;
               queues = var5;
            }
         }
      }
   }

   public static void initializeExecuteQueue(ExecuteQueueMBean var0) {
      addExecuteQueue(var0.getName(), var0);
   }

   public static void shutdown() {
      KernelStatus.shutdown();
      Class var0 = Kernel.class;
      synchronized(Kernel.class) {
         int var1 = queues.length;

         for(int var2 = 1; var2 < var1; ++var2) {
            queues[var2].shutdown();
         }

      }
   }

   public static boolean checkStuckThreads(String var0, long var1) {
      boolean var3 = false;
      ExecuteThreadManager[] var4 = getExecuteThreadManagers();
      if (var0 != null) {
         int var5 = getDispatchPolicyIndex(var0);
         if (var1 > 0L && var5 > 0) {
            ExecuteThreadManager var6 = var4[var5];
            if (var6 != null) {
               ExecuteThread[] var7 = var6.getStuckExecuteThreads(var1);
               if (var7 != null && var7.length == var6.getExecuteThreadCount()) {
                  var3 = true;
               }
            }
         }
      }

      return var3;
   }

   public static int getPendingTasksCount(String var0) {
      int var1 = 0;
      ExecuteThreadManager[] var2 = getExecuteThreadManagers();
      if (var0 != null) {
         int var3 = getDispatchPolicyIndex(var0);
         if (var3 > 0) {
            ExecuteThreadManager var4 = var2[var3];
            if (var4 != null) {
               var1 = var4.getPendingTasksCount();
            }
         }
      }

      return var1;
   }

   public static List getApplicationDispatchPolicies() {
      return applicationQueueNames;
   }

   public static boolean isApplicationDispatchPolicy(int var0) {
      if (var0 == 0) {
         return false;
      } else {
         return applicationQueueNames.contains(queues[var0].getName());
      }
   }

   private Kernel() {
   }

   static boolean isQueueThrottleAllowed() {
      return allowQueueThrottling;
   }

   public static void addDummyDefaultQueue(ExecuteThreadManager var0) {
      Class var1 = Kernel.class;
      synchronized(Kernel.class) {
         ExecuteThreadManager[] var2 = new ExecuteThreadManager[queues.length + 1];
         System.arraycopy(queues, 0, var2, 0, queues.length);
         var2[queues.length] = var0;
         queues = var2;
      }
   }
}
