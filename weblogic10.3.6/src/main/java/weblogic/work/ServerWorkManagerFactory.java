package weblogic.work;

import java.security.AccessController;
import javax.management.InvalidAttributeValueException;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.diagnostics.image.ImageManager;
import weblogic.health.HealthMonitorService;
import weblogic.kernel.ExecuteQueueMBeanStub;
import weblogic.kernel.Kernel;
import weblogic.kernel.KernelStatus;
import weblogic.kernel.WorkManagerWrapper;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ExecuteQueueMBean;
import weblogic.management.configuration.KernelMBean;
import weblogic.management.configuration.WorkManagerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class ServerWorkManagerFactory extends SelfTuningWorkManagerFactory {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static ServerWorkManagerFactory SINGLETON;

   private ServerWorkManagerFactory() {
   }

   static ServerWorkManagerFactory get() {
      return SINGLETON;
   }

   public static synchronized void initialize(KernelMBean var0) throws ManagementException {
      if (SINGLETON == null) {
         SINGLETON = new ServerWorkManagerFactory();
         WorkManagerFactory.set(SINGLETON);
         WorkManagerLogger.logInitializingSelfTuning();
         SINGLETON.initializeHere(var0);
      }
   }

   public static synchronized void initialize(int var0) {
      if (SINGLETON == null) {
         SINGLETON = new ServerWorkManagerFactory();
         WorkManagerFactory.set(SINGLETON);
         WorkManagerLogger.logInitializingSelfTuning();
         SINGLETON.initializeHere(var0);
      }
   }

   private void initializeHere(KernelMBean var1) throws ManagementException {
      ServerWorkManagerImpl.initialize();
      IncrementAdvisor.setIncrementByCPUCount(var1.isAddWorkManagerThreadsByCpuCount());
      GlobalWorkManagerComponentsFactory.getInstance().initialize();
      super.initializeHere();
      Kernel.addDummyDefaultQueue(new WorkManagerWrapper("weblogic.kernel.Default"));
      ExecuteQueueMBean[] var2 = var1.getExecuteQueues();
      if (var2 != null) {
         boolean var3 = false;
         int var4 = var2.length;

         while(true) {
            --var4;
            if (var4 < 0) {
               if (var3) {
                  WorkManagerLogger.logNoSelfTuningForExecuteQueues();
               }
               break;
            }

            ExecuteQueueMBean var5 = var2[var4];
            String var6 = var5.getName();
            if (!"weblogic.kernel.Default".equalsIgnoreCase(var6) && !"default".equalsIgnoreCase(var6)) {
               if (var6.startsWith("wl_bootstrap_")) {
                  var6 = var6.substring(13);
               }

               this.create(var6, var5);
               var3 = true;
            }
         }
      }

      ImageManager.getInstance().registerImageSource("WORK_MANAGER", WorkManagerImageSource.getInstance());

      try {
         ThreadPoolRuntimeMBeanImpl var8 = new ThreadPoolRuntimeMBeanImpl(RequestManager.getInstance());
         HealthMonitorService.register("threadpool", var8, false);
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   private WorkManager createGlobalInternalWorkManagerFromTemplate(String var1) {
      WorkManager var2 = null;
      GlobalWorkManagerComponentsFactory.WorkManagerTemplate var3 = GlobalWorkManagerComponentsFactory.getInstance().removeWorkManagerTemplate(var1);
      if (var3 != null) {
         var2 = create(var1, (String)null, (String)null, var3.getRequestClass(), var3.getMaxThreadsConstraint(), var3.getMinThreadsConstraint(), var3.getCapacity(), (StuckThreadManager)null);
         WorkManagerLogger.logGlobalInternalWorkManagerOverriden(var1);
      }

      return var2;
   }

   private void initializeHere(int var1) {
      ServerWorkManagerImpl.initialize(var1);
      super.initializeHere();
      Kernel.addDummyDefaultQueue(new WorkManagerWrapper("weblogic.kernel.Default"));
   }

   protected WorkManager create(String var1, int var2, int var3, int var4, int var5) {
      MinThreadsConstraint var6 = null;
      if (var4 != -1) {
         var6 = new MinThreadsConstraint(var1, var4);
      }

      MaxThreadsConstraint var7 = null;
      if (var5 != -1) {
         var7 = new MaxThreadsConstraint(var1, var5);
      }

      Object var8 = null;
      if (var3 > 0) {
         var8 = new ResponseTimeRequestClass(var1, var3);
      } else if (var2 > 0) {
         var8 = new FairShareRequestClass(var1, var2);
      }

      WorkManager var9 = this.createGlobalInternalWorkManagerFromTemplate(var1);
      if (var9 == null) {
         var9 = create(var1, (String)null, (String)null, (RequestClass)var8, var7, var6, (OverloadManager)null, (StuckThreadManager)null);
      }

      if (KernelStatus.isServer()) {
         createRuntimeMBean(var9, (RequestClass)var8, var6, var7);
      }

      return var9;
   }

   private static void createRuntimeMBean(WorkManager var0, RequestClass var1, MinThreadsConstraint var2, MaxThreadsConstraint var3) {
      if (var0 instanceof ServerWorkManagerImpl) {
         try {
            WorkManagerRuntimeMBeanImpl var4 = new WorkManagerRuntimeMBeanImpl((ServerWorkManagerImpl)var0);
            if (var1 != null) {
               var4.setRequestClassRuntime(new RequestClassRuntimeMBeanImpl(var1));
            }

            if (var2 != null) {
               var4.setMinThreadsConstraintRuntime(new MinThreadsConstraintRuntimeMBeanImpl(var2));
            }

            if (var3 != null) {
               var4.setMaxThreadsConstraintRuntime(new MaxThreadsConstraintRuntimeMBeanImpl(var3));
            }

            ManagementService.getRuntimeAccess(kernelId).getServerRuntime().addWorkManagerRuntime(var4);
         } catch (ManagementException var5) {
            WorkManagerLogger.logRuntimeMBeanCreationError(var0.getName(), var5);
         }
      }

   }

   private WorkManager create(String var1, ExecuteQueueMBean var2) {
      WorkManagerLogger.logCreatingExecuteQueueFromMBean(var1);
      KernelDelegator var3 = new KernelDelegator(var1, var2);
      this.byName.put(var1, var3);
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

      return get().create(var0, var2);
   }

   protected WorkManager findAppScoped(String var1, String var2, String var3, boolean var4) {
      ApplicationContextInternal var5;
      if (var2 != null && var2.length() != 0) {
         var5 = ApplicationAccess.getApplicationAccess().getApplicationContext(this.getSanitizedAppName(var2));
      } else {
         var5 = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
      }

      if (var5 == null) {
         return null;
      } else {
         String var6 = var3 != null ? var3 : ApplicationAccess.getApplicationAccess().getCurrentModuleName();
         WorkManager var7 = var5.getWorkManagerCollection().get(var6, var1, var4);
         return var7 != null ? var7 : this.DEFAULT;
      }
   }

   private String getSanitizedAppName(String var1) {
      if (var1 != null && var1.length() != 0) {
         int var2 = var1.indexOf("@");
         if (var2 >= 0) {
            var1 = var1.substring(0, var2);
         }
      }

      return var1;
   }

   static WorkManager create(String var0, String var1, String var2) {
      return create(var0, var1, var2, (RequestClass)null, (MaxThreadsConstraint)null, (MinThreadsConstraint)null, (OverloadManager)null, (StuckThreadManager)null);
   }

   static WorkManager create(String var0, String var1, String var2, StuckThreadManager var3) {
      return create(var0, var1, var2, (RequestClass)null, (MaxThreadsConstraint)null, (MinThreadsConstraint)null, (OverloadManager)null, var3);
   }

   protected static WorkManager create(String var0, String var1, String var2, RequestClass var3, MaxThreadsConstraint var4, MinThreadsConstraint var5, OverloadManager var6, StuckThreadManager var7) {
      return new ServerWorkManagerImpl(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   static WorkManager create(String var0, String var1, WorkManagerMBean var2, StuckThreadManager var3) {
      GlobalWorkManagerComponentsFactory.WorkManagerTemplate var4 = GlobalWorkManagerComponentsFactory.getInstance().findWorkManagerTemplate(var2);
      return create(var2.getName(), var0, var1, var4.getRequestClass(), var4.getMaxThreadsConstraint(), var4.getMinThreadsConstraint(), var4.getCapacity(), var3);
   }
}
