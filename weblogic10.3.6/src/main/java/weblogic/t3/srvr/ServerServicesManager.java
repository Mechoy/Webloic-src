package weblogic.t3.srvr;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.RunningStateListener;
import weblogic.server.ServerService;
import weblogic.server.ServiceActivator;
import weblogic.server.ServiceFailureException;
import weblogic.server.servicegraph.Service;
import weblogic.servlet.internal.WebAppShutdownService;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public final class ServerServicesManager implements ServerServices {
   private static final DebugCategory debugSLC = Debug.getCategory("weblogic.slc");
   private static final DebugLogger debugSLCWLDF = DebugLogger.getDebugLogger("DebugServerLifeCycle");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   static final ServiceFailureException STARTUP_ABORTED = new ServiceFailureException("Startup aborted");
   static final ServiceFailureException STARTUP_TIMED_OUT = new ServiceFailureException("Startup timed out");
   private static final SubsystemRequest subsystemRequest = new SubsystemRequest();
   private static final ArrayList<ServerService> servicesBeforeAdminState = new ArrayList();
   private static final ArrayList<ServerService> servicesAfterAdminState = new ArrayList();
   private static boolean startupCompleted = false;
   private static int startFromIndex = 0;
   private static boolean forceShutdown;
   private static int currentState;
   private static boolean startupAborted;
   private static boolean checkServicesMemory = Boolean.getBoolean("checkServices");
   private static final ArrayList<RunningStateListener> runningListeners = new ArrayList();

   private ServerServicesManager() {
   }

   static synchronized void startInStandbyState() throws ServiceFailureException {
      if (debugEnabled()) {
         debug("starting server in standby state");
      }

      long var0 = 0L;
      long var2 = 0L;
      long var4 = 0L;
      long var6 = 0L;
      long var8 = 0L;
      long var10 = 0L;
      if (checkServicesMemory && debugEnabled()) {
         var8 = ServerServicesManager.MEMORY_MX_BEAN.instance.getHeapMemoryUsage().getUsed();
         var10 = ServerServicesManager.MEMORY_MX_BEAN.instance.getNonHeapMemoryUsage().getUsed();
         debug("[Memory(Heap,NonHeap) used by pre-service-startup before forced gc: (" + var8 + "," + var10 + ")");
         ServerServicesManager.MEMORY_MX_BEAN.instance.gc();
         debug("[Memory(Heap,NonHeap) used by pre-service-startup after gc: (" + ServerServicesManager.MEMORY_MX_BEAN.instance.getHeapMemoryUsage().getUsed() + "," + ServerServicesManager.MEMORY_MX_BEAN.instance.getNonHeapMemoryUsage().getUsed() + ")");
         debug("[Memory(Heap,NonHeap) freed by forced gc: (" + (var8 - ServerServicesManager.MEMORY_MX_BEAN.instance.getHeapMemoryUsage().getUsed()) + "," + (var10 - ServerServicesManager.MEMORY_MX_BEAN.instance.getNonHeapMemoryUsage().getUsed()) + ")");
      }

      Iterator var12 = getServicesBeforeStandbyState().iterator();

      while(var12.hasNext()) {
         Service var13 = (Service)var12.next();
         String var14 = var13.getServiceClassName();
         if (debugEnabled()) {
            debug("creating service: " + var14);
         }

         if (checkServicesMemory) {
            if (debugEnabled()) {
               debug("Forcing garbage collection to measure memory usage.");
            }

            ServerServicesManager.MEMORY_MX_BEAN.instance.gc();
            var8 = ServerServicesManager.MEMORY_MX_BEAN.instance.getHeapMemoryUsage().getUsed();
            var10 = ServerServicesManager.MEMORY_MX_BEAN.instance.getNonHeapMemoryUsage().getUsed();
         }

         updateStartupSnapshot(var14, var0, var2);
         ServerService var15 = createService(var13);
         if (debugEnabled()) {
            debug("starting service: " + var14);
         }

         var0 = System.currentTimeMillis();
         startService(var15, var6);
         servicesBeforeAdminState.add(var15);
         var2 = System.currentTimeMillis();
         long var16 = 0L;
         if (var4 == 0L) {
            var16 = getStartupTimeout();
         }

         if (var4 == 0L && var16 > 0L) {
            var4 = var16;
            var6 = var16;
         }

         if (var4 > 0L) {
            var6 = var4 - (var2 - var0);
         }

         if (debugEnabled()) {
            debug("Time taken to start " + var14 + ": " + (var2 - var0) + " ms");
            if (checkServicesMemory) {
               debug("[Memory(Heap,NonHeap) used by " + var14 + ": (" + (ServerServicesManager.MEMORY_MX_BEAN.instance.getHeapMemoryUsage().getUsed() - var8) + "," + (ServerServicesManager.MEMORY_MX_BEAN.instance.getNonHeapMemoryUsage().getUsed() - var10) + ")");
            }
         }
      }

   }

   private static long getStartupTimeout() {
      return ManagementService.isRuntimeAccessInitialized() ? (long)(ManagementService.getRuntimeAccess(kernelId).getServer().getStartupTimeout() * 1000) : 0L;
   }

   static synchronized void startInAdminState(long var0) throws ServiceFailureException {
      if (debugEnabled()) {
         debug("starting server in admin state");
      }

      long var2 = 0L;
      long var4 = 0L;
      long var6 = var0;
      long var8 = 0L;
      long var10 = 0L;
      Iterator var12 = getServicesBeforeAdminState().iterator();

      while(var12.hasNext()) {
         Service var13 = (Service)var12.next();
         String var14 = var13.getServiceClassName();
         if (debugEnabled()) {
            debug("creating service: " + var14);
         }

         if (checkServicesMemory) {
            if (debugEnabled()) {
               debug("Forcing garbage collection to measure memory usage.");
            }

            ServerServicesManager.MEMORY_MX_BEAN.instance.gc();
            var8 = ServerServicesManager.MEMORY_MX_BEAN.instance.getHeapMemoryUsage().getUsed();
            var10 = ServerServicesManager.MEMORY_MX_BEAN.instance.getNonHeapMemoryUsage().getUsed();
         }

         updateStartupSnapshot(var14, var2, var4);
         ServerService var15 = createService(var13);
         if (debugEnabled()) {
            debug("starting service: " + var14);
         }

         var2 = System.currentTimeMillis();
         startService(var15, var6);
         servicesBeforeAdminState.add(var15);
         var4 = System.currentTimeMillis();
         if (var0 > 0L) {
            var6 -= var4 - var2;
         }

         if (debugEnabled()) {
            debug("Time taken to start " + var14 + ": " + (var4 - var2) + " ms");
            if (checkServicesMemory) {
               debug("[Memory(Heap,NonHeap) used by " + var14 + ": (" + (ServerServicesManager.MEMORY_MX_BEAN.instance.getHeapMemoryUsage().getUsed() - var8) + "," + (ServerServicesManager.MEMORY_MX_BEAN.instance.getNonHeapMemoryUsage().getUsed() - var10) + ")");
            }
         }
      }

   }

   static synchronized void resume(long var0) throws ServiceFailureException {
      long var2 = var0;
      long var4 = 0L;
      long var6 = 0L;
      Iterator var8;
      if (!startupCompleted) {
         if (debugEnabled()) {
            debug("starting services after admin state");
         }

         var8 = getServicesAfterAdminState().iterator();

         while(var8.hasNext()) {
            Service var9 = (Service)var8.next();
            String var10 = var9.getServiceClassName();
            if (debugEnabled()) {
               debug("creating service: " + var10);
            }

            updateStartupSnapshot(var10, var4, var6);
            ServerService var11 = createService(var9);
            if (debugEnabled()) {
               debug("starting service: " + var10);
            }

            var4 = System.currentTimeMillis();
            startService(var11, var2);
            servicesAfterAdminState.add(var11);
            var6 = System.currentTimeMillis();
            if (var0 > 0L) {
               var2 -= var6 - var4;
            }

            if (debugEnabled()) {
               debug("Time taken to start " + var10 + ": " + (var6 - var4) + " ms");
            }
         }

         startupCompleted = true;
      } else {
         if (debugEnabled()) {
            debug("resuming a suspended server ...");
         }

         var8 = servicesAfterAdminState.iterator();

         while(var8.hasNext()) {
            ServerService var12 = (ServerService)var8.next();
            if (debugEnabled()) {
               debug("resuming: " + var12);
            }

            var4 = System.currentTimeMillis();
            startService(var12, var2);
            var6 = System.currentTimeMillis();
            if (var0 > 0L) {
               var2 -= var6 - var4;
            }
         }
      }

   }

   static void stop(int var0, boolean var1) throws ServiceFailureException {
      if (var0 == 17) {
         if (debugEnabled()) {
            debug("graceful suspend of a running server. Will stop services till admin state ...");
         }

         stopInternal(servicesAfterAdminState.toArray(), var1);
         currentState = 17;
      } else {
         if (debugEnabled()) {
            debug("will attempt to stop all services ...");
         }

         if (currentState != 17) {
            stopInternal(servicesAfterAdminState.toArray(), var1);
         }

         stopInternal(servicesBeforeAdminState.toArray(), var1);
      }

   }

   static void halt(int var0) {
      if (var0 == 17) {
         if (debugEnabled()) {
            debug("force suspend a running server. Will halt services till admin state ...");
         }

         haltInternal(servicesAfterAdminState.toArray());
         currentState = 17;
      } else {
         forceShutdown = true;
         if (debugEnabled()) {
            debug("will attempt to halt all services ...");
         }

         if (currentState != 17) {
            haltInternal(servicesAfterAdminState.toArray());
         }

         haltInternal(servicesBeforeAdminState.toArray());
      }

   }

   private static List<Service> getServicesBeforeAdminState() {
      ArrayList var0 = new ArrayList();
      if (debugEnabled()) {
         debug("getting classnames that should be started before admin state");
      }

      for(int var1 = startFromIndex; var1 < ServerServices.ORDERED_SUBSYSTEM_LIST.length; ++var1) {
         Service var2 = ServerServices.ORDERED_SUBSYSTEM_LIST[var1];
         String var3 = var2.getServiceClassName();
         if ("admin_state".equals(var3)) {
            break;
         }

         if (debugEnabled()) {
            debug("[start before admin state] " + var3);
         }

         var0.add(var2);
      }

      return var0;
   }

   private static List<Service> getServicesBeforeStandbyState() {
      ArrayList var0 = new ArrayList();
      if (debugEnabled()) {
         debug("getting classnames that should be started before standby state");
      }

      for(int var1 = 0; var1 < ServerServices.ORDERED_SUBSYSTEM_LIST.length; ++var1) {
         Service var2 = ServerServices.ORDERED_SUBSYSTEM_LIST[var1];
         String var3 = var2.getServiceClassName();
         if ("standby_state".equals(var3)) {
            startFromIndex = var1 + 1;
            break;
         }

         if (debugEnabled()) {
            debug("[start before standby state] " + var3);
         }

         var0.add(var2);
      }

      return var0;
   }

   private static List<Service> getServicesAfterAdminState() {
      ArrayList var0 = new ArrayList();

      int var1;
      for(var1 = 0; var1 < ServerServices.ORDERED_SUBSYSTEM_LIST.length; ++var1) {
         Service var2 = ServerServices.ORDERED_SUBSYSTEM_LIST[var1];
         String var3 = var2.getServiceClassName();
         if ("admin_state".equals(var3)) {
            if (debugEnabled()) {
               debug("admin index is at " + var1);
            }
            break;
         }
      }

      Debug.assertion(var1 > 0, "admin state not defined in ServerServicesList");
      if (debugEnabled()) {
         debug("getting class names that should be started after admin state");
      }

      for(int var4 = var1 + 1; var4 < ServerServices.ORDERED_SUBSYSTEM_LIST.length; ++var4) {
         Service var5 = ServerServices.ORDERED_SUBSYSTEM_LIST[var4];
         if (debugEnabled()) {
            debug("[start after admin state] " + var5.getServiceClassName());
         }

         var0.add(var5);
      }

      return var0;
   }

   private static ServerService createService(Service var0) throws ServiceFailureException {
      String var1 = null;

      try {
         if (forceShutdown) {
            throw new ServiceFailureException("Startup aborted. Server is shutting down");
         } else {
            ServiceActivator var2 = var0.getActivator();
            if (var2 == null) {
               var1 = var0.getServiceClassName();
               Object var3 = Class.forName(var1).newInstance();
               return (ServerService)var3;
            } else {
               return var2;
            }
         }
      } catch (ClassCastException var4) {
         throw new AssertionError("ServerService class " + var1 + " doesn't implement ServerService", var4);
      } catch (ClassNotFoundException var5) {
         throw new AssertionError("ServerService class " + var1 + " not found", var5);
      } catch (InstantiationException var6) {
         throw new AssertionError("ServerService class " + var1 + " can't be instantiated", var6);
      } catch (IllegalAccessException var7) {
         throw new AssertionError("ServerService class " + var1 + " doesn't hava public constructor", var7);
      }
   }

   private static void startService(ServerService var0, long var1) throws ServiceFailureException {
      if (Kernel.isInitialized()) {
         getSubsystemRequest(var0).start(var1);
      } else {
         var0.start();
      }

   }

   private static SubsystemRequest getSubsystemRequest(ServerService var0) throws ServiceFailureException {
      synchronized(subsystemRequest) {
         if (startupAborted) {
            throw STARTUP_ABORTED;
         } else {
            subsystemRequest.setRequest(var0);
            return subsystemRequest;
         }
      }
   }

   static void abortStartup() {
      synchronized(subsystemRequest) {
         startupAborted = true;
         subsystemRequest.notify(STARTUP_ABORTED);
      }
   }

   private static void stopInternal(Object[] var0, boolean var1) throws ServiceFailureException {
      for(int var2 = var0.length - 1; var2 >= 0; --var2) {
         ServerService var3 = (ServerService)var0[var2];
         if (var1 && var3 instanceof WebAppShutdownService) {
            WebAppShutdownService.ignoreSessionsDuringShutdown();
         }

         if (debugEnabled()) {
            debug("calling stop on " + var3);
         }

         var3.stop();
      }

   }

   private static void haltInternal(Object[] var0) {
      for(int var1 = var0.length - 1; var1 >= 0; --var1) {
         try {
            ServerService var2 = (ServerService)var0[var1];
            if (debugEnabled()) {
               debug("calling halt on " + var2);
            }

            var2.halt();
         } catch (ServiceFailureException var3) {
            T3SrvrLogger.logServiceFailure("halt call on " + var0[var1] + " failed", var3);
         }
      }

   }

   public static int getServicesCount() {
      return ServerServices.ORDERED_SUBSYSTEM_LIST.length;
   }

   public static void addRunningStateListener(RunningStateListener var0) {
      runningListeners.add(var0);
   }

   static void invokeRunningStateListeners() {
      WorkManagerFactory.getInstance().getSystem().schedule(new WorkAdapter() {
         public void run() {
            synchronized(ServerServicesManager.runningListeners) {
               Iterator var2 = ServerServicesManager.runningListeners.iterator();

               while(var2.hasNext()) {
                  RunningStateListener var3 = (RunningStateListener)var2.next();
                  var3.onRunning();
               }

            }
         }
      });
   }

   static HashMap getVersionsOnline() {
      if (!startupCompleted) {
         throw new IllegalStateException("Cannot get ServerServices information till startup is complete");
      } else {
         HashMap var0 = new HashMap();
         Iterator var1 = servicesBeforeAdminState.iterator();

         ServerService var2;
         while(var1.hasNext()) {
            var2 = (ServerService)var1.next();
            if (var2.getVersion() != null && var2.getVersion().trim().length() > 0) {
               var0.put(var2.getName(), var2.getVersion());
            }
         }

         var1 = servicesAfterAdminState.iterator();

         while(var1.hasNext()) {
            var2 = (ServerService)var1.next();
            if (var2.getVersion() != null && var2.getVersion().trim().length() > 0) {
               var0.put(var2.getName(), var2.getVersion());
            }
         }

         return var0;
      }
   }

   private static void updateStartupSnapshot(String var0, long var1, long var3) {
      ServerServicesManager.STARTUP_SNAPSHOT.instance.updateStartupSnapShot(var0, var1, var3);
   }

   public static StartupSnapshot getStartupSnapshot() {
      return ServerServicesManager.STARTUP_SNAPSHOT.instance;
   }

   private static boolean debugEnabled() {
      return debugSLC.isEnabled() || debugSLCWLDF.isDebugEnabled();
   }

   private static void debug(String var0) {
      debugSLCWLDF.debug(var0);
   }

   public static final class StartupSnapshot {
      private static int index = 0;
      private String currentServiceName;
      private String previousServiceName;
      private long previousServiceStartupTime;

      StartupSnapshot() {
      }

      synchronized void updateStartupSnapShot(String var1, long var2, long var4) {
         this.previousServiceName = this.currentServiceName;
         this.previousServiceStartupTime = var4 - var2;
         this.currentServiceName = var1;
         ++index;
      }

      public int getCurrentServiceIndex() {
         return index;
      }

      public String getCurrentServiceName() {
         return this.currentServiceName;
      }

      public long getPreviousServiceStartupTime() {
         return this.previousServiceStartupTime;
      }

      public String getPreviousServiceName() {
         return this.previousServiceName;
      }
   }

   private static final class STARTUP_SNAPSHOT {
      static final StartupSnapshot instance = new StartupSnapshot();
   }

   private static final class MEMORY_MX_BEAN {
      static final MemoryMXBean instance = ManagementFactory.getMemoryMXBean();
   }
}
