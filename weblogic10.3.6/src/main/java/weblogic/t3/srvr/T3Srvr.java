package weblogic.t3.srvr;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.security.AccessController;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import weblogic.common.T3ServicesDef;
import weblogic.common.internal.VersionInfoFactory;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.kernel.T3SrvrLogger;
import weblogic.logging.LogOutputStream;
import weblogic.logging.LoggingHelper;
import weblogic.management.configuration.OverloadProtectionMBean;
import weblogic.management.internal.InteractiveConfigurationException;
import weblogic.management.provider.CommandLine;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerStates;
import weblogic.platform.OperatingSystem;
import weblogic.platform.VM;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.SecurityInitializationException;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PluginUtils;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.ServerResource;
import weblogic.security.service.SubjectManagerImpl;
import weblogic.security.subject.SubjectManager;
import weblogic.server.ServerLifecycleException;
import weblogic.server.ServiceFailureException;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.FileUtils;
import weblogic.utils.collections.NumericKeyHashMap;

public final class T3Srvr {
   private static final DebugCategory debugSLC = Debug.getCategory("weblogic.slc");
   private static final DebugLogger debugSLCWLDF = DebugLogger.getDebugLogger("DebugServerLifeCycle");
   private static final DebugLogger loggerSERVER_START_STATISTICS = DebugLogger.getDebugLogger("DebugServerStartStatistics");
   public static final int RESTARTABLE_EXIT_CODE = 1;
   public static final int NON_RESTARTABLE_EXIT_CODE = -1;
   public static final int PANIC_EXIT_CODE = 65;
   private static AuthenticatedSubject kernelId;
   private static T3Srvr singleton;
   private final LogOutputStream log = new LogOutputStream("WebLogicServer");
   private final ThreadGroup tg = new ThreadGroup("WebLogicServer");
   private AuthorizationManager am;
   private ServerLockoutManager lockoutManager;
   private ServerRuntime serverRuntimeMBean;
   private long startTime;
   private long startupTime;
   private boolean abortStartupAfterAdminState;
   private boolean started;
   private int srvrState = 0;
   private int exitCode = 0;
   private boolean preventShutdownHook = Boolean.getBoolean("weblogic.system.disableShutdownHook");
   private int fallbackState = 0;
   private boolean isShuttingDown = false;
   private static final String restartFileName = "wls.restartfile";
   private static File restartFile = null;
   private static final DebugCategory debugExceptions = Debug.getCategory("weblogic.slc.exceptions.verbose");
   private int shutWaitSecs = 0;
   private final T3ServerServices svcs = new T3ServerServices();
   private static final String PAUSE = "weblogic.sleepOnStartSecs";
   private static int numRestartsSoFar;

   public static T3Srvr getT3Srvr() {
      if (singleton == null) {
         throw new IllegalStateException("Calling getT3Srvr() too early. This can happen when you have a static initializer or static variable pointing to T3Srvr.getT3Srvr() and your class is getting loaded prior to T3Srvr.");
      } else {
         return singleton;
      }
   }

   public ServerLockoutManager getLockoutManager() {
      return this.lockoutManager;
   }

   public LogOutputStream getLog() {
      return this.log;
   }

   long getStartTime() {
      return this.startTime;
   }

   long getStartupTime() {
      return this.startupTime;
   }

   ThreadGroup getStartupThreadGroup() {
      return this.tg;
   }

   private synchronized void setState(int var1) {
      if (!T3Srvr.StateChangeValidator.validate(this.srvrState, var1)) {
         String var2 = "trying to set illegal state, present state " + this.getState() + ", new state " + ServerStates.SERVERSTATES[var1];
         throw new IllegalStateException(var2);
      } else {
         this.srvrState = var1;
         if (this.serverRuntimeMBean != null) {
            this.serverRuntimeMBean.updateRunState(var1);
         }

         T3SrvrLogger.logServerStateChange(this.getState());
      }
   }

   public synchronized void failed(String var1) {
      if (this.srvrState != 8) {
         int var2 = this.srvrState;
         if (var1 != null) {
            T3SrvrLogger.logServerHealthFailed(var1);
         }

         this.setState(8);
         logThreadDump();

         try {
            OverloadProtectionMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServer().getOverloadProtection();
            if ("force-shutdown".equals(var3.getFailureAction())) {
               T3SrvrLogger.logShuttingDownOnFailure();
               this.exitCode = 1;
               singleton.shutdown(false, true);
            } else if (isServerBeyondAdminState(var2) && "admin-state".equals(var3.getFailureAction())) {
               T3SrvrLogger.logSuspendingOnFailure();
               singleton.suspend(false, true);
            }
         } catch (ServerLifecycleException var4) {
            var4.printStackTrace();
         }

      }
   }

   public void failedForceShutdown(String var1) {
      if (var1 != null) {
         T3SrvrLogger.logServerHealthFailed(var1);
      }

      this.setState(8);
      logThreadDump();
      T3SrvrLogger.logShuttingDownOnFailure();
      this.exitCode = 1;

      try {
         singleton.shutdown(false, true);
      } catch (ServerLifecycleException var3) {
         this.exitImmediately(var3);
      }

   }

   public boolean isShutdownDueToFailure() {
      return this.exitCode == 1;
   }

   public boolean isStarted() {
      return this.started;
   }

   public boolean isShuttingDown() {
      return this.isShuttingDown;
   }

   static void logThreadDump() {
      StringWriter var0 = new StringWriter();
      PrintWriter var1 = new PrintWriter(var0);
      VM.getVM().threadDump(var1);
      var1.close();
      LoggingHelper.getServerLogger().severe(var0.toString());
   }

   private synchronized void setFailedState(Throwable var1, boolean var2) {
      if (var1 != ServerServicesManager.STARTUP_ABORTED) {
         if (var1 == ServerServicesManager.STARTUP_TIMED_OUT) {
            logThreadDump();
         }

         if (var1 == null || !var2 && !debugExceptions.isEnabled()) {
            T3SrvrLogger.logServerSubsystemFailed(var1.getMessage());
         } else {
            T3SrvrLogger.logServerSubsystemFailedWithTrace(var1);
         }

         this.setState(8);

         try {
            if (this.fallbackState == 17) {
               T3SrvrLogger.logSuspendingOnFailure();
               singleton.suspend(false, true);
               return;
            }

            if (this.fallbackState == 0) {
               T3SrvrLogger.logShuttingDownOnFailure();
               singleton.shutdown(false, true);
            }
         } catch (ServerLifecycleException var4) {
            var4.printStackTrace();
         }

      }
   }

   public void exitImmediately(Throwable var1) {
      signalCriticalFailure("There is a panic condition in the server. The server is configured to exit on panic", var1);
      this.setPreventShutdownHook();
      T3Srvr.JrveVM.setShutdownAction(65);
      System.exit(65);
   }

   public int getRunState() {
      return this.srvrState;
   }

   public String getState() {
      return ServerStates.SERVERSTATES[this.srvrState];
   }

   public int getStableState() {
      int var1 = this.srvrState;
      switch (this.srvrState) {
         case 1:
            try {
               String var2 = this.getStartupMode();
               if (!this.abortStartupAfterAdminState && (var2 == null || !"ADMIN".equalsIgnoreCase(var2))) {
                  var1 = 2;
               } else {
                  var1 = 17;
               }
            } catch (ServerLifecycleException var3) {
               var1 = 9;
            }
            break;
         case 2:
         case 6:
            var1 = 2;
         case 3:
         case 8:
         case 9:
         case 10:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         default:
            break;
         case 4:
         case 5:
         case 17:
            if (this.isShuttingDown()) {
               var1 = 9;
            } else {
               var1 = 17;
            }
            break;
         case 7:
         case 11:
         case 18:
            var1 = 9;
      }

      return var1;
   }

   public void setShutdownWaitSecs(int var1) {
      this.shutWaitSecs = var1;
   }

   public T3ServicesDef getT3Services() {
      return this.svcs;
   }

   public static int run(String[] var0) {
      pauseBeforeStartup();

      try {
         initSubjectManager();
         SecurityServiceManager.initJava2Security();
         kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         SecurityManager var1 = System.getSecurityManager();
         if (var1 != null) {
            System.setSecurityManager(new SecurityManager() {
               public void checkRead(String var1) {
               }
            });
         }

         new T3Srvr();
         SecurityServiceManager.pushSubject(kernelId, kernelId);
         singleton.addShutdownHook();
         singleton.startup();
         if (var1 != null) {
            System.setSecurityManager(var1);
         }
      } catch (ServerLifecycleException var3) {
         singleton.preventShutdownHook = true;
         T3Srvr.JrveVM.setShutdownAction(-1);
         return -1;
      } catch (Throwable var4) {
         singleton.preventShutdownHook = true;
         handleFatalInitializationException(var4);
         if (singleton != null) {
            singleton.setState(18);
         }

         T3Srvr.JrveVM.setShutdownAction(-1);
         return -1;
      }

      try {
         singleton.waitForDeath();
         T3Srvr.JrveVM.setShutdownAction(singleton.exitCode);
         return singleton.exitCode;
      } catch (Throwable var2) {
         T3SrvrLogger.logErrorWhileServerShutdown(var2);
         singleton.preventShutdownHook = true;
         T3Srvr.JrveVM.setShutdownAction(-1);
         return -1;
      }
   }

   private static void deleteRestartFile() {
      if (restartFile != null) {
         restartFile.delete();
      }

   }

   private void startup() throws ServerLifecycleException {
      long var3 = System.currentTimeMillis();
      long var5 = var3;
      VersionInfoFactory.initialize(true);
      if (OperatingSystem.isJRVE()) {
         try {
            restartFile = new File("wls.restartfile");
            long var1;
            if (!restartFile.exists()) {
               if (debugSLC.isEnabled()) {
                  debugSLCWLDF.debug("Creating new HA restart file: " + restartFile.getName());
               }

               restartFile.createNewFile();
               numRestartsSoFar = 0;
               var1 = var5;
            } else {
               DataInputStream var7 = new DataInputStream(new FileInputStream(restartFile));

               try {
                  numRestartsSoFar = var7.readInt();
                  var1 = var7.readLong();
                  long var8 = (var5 - var1) / 1000L / 60L;
                  long var10 = (var5 - var1) / 1000L - var8 * 60L;
                  if (debugSLC.isEnabled()) {
                     debugSLCWLDF.debug(String.format("There have been %d restarts since the start %d minutes and %d seconds ago at %s%n", numRestartsSoFar, var8, var10, new Date(var1)));
                  }
               } catch (EOFException var12) {
                  numRestartsSoFar = 0;
                  var1 = var5;
               }
            }

            ++numRestartsSoFar;
            DataOutputStream var16 = new DataOutputStream(new FileOutputStream(restartFile));
            var16.writeInt(numRestartsSoFar);
            var16.writeLong(var1);
            var16.close();
         } catch (FileNotFoundException var13) {
            throw new ServerLifecycleException(var13);
         } catch (IOException var14) {
            throw new ServerLifecycleException(var14);
         }
      }

      singleton.initializeStandby();
      long var15 = (long)ManagementService.getRuntimeAccess(kernelId).getServer().getStartupTimeout();
      if (var15 > 0L) {
         var15 = updateStartTimeout(var15 * 1000L, var3);
      }

      String var9 = this.getStartupMode();
      if (debugSLC.isEnabled()) {
         debugSLCWLDF.debug("Server startup mode is " + var9);
      }

      if (!"STANDBY".equalsIgnoreCase(var9)) {
         var5 = System.currentTimeMillis();
         singleton.initializeAdmin(var15);
         if (!this.abortStartupAfterAdminState && !"ADMIN".equalsIgnoreCase(var9)) {
            if (var15 > 0L) {
               var15 = updateStartTimeout(var15, var5);
            }

            singleton.resume(var15);
         } else {
            this.fallbackState = 17;
         }
      }

      T3SrvrLogger.logServerStarted1(singleton.getState());
      this.startupTime = this.getElapsedStartTime(var3);
      this.logStartupStatistics();
      this.started = true;
      this.isShuttingDown = false;
   }

   private long getStartBeginTime() {
      long var1;
      try {
         ClassLoader var3 = this.getClass().getClassLoader();
         Method var4 = var3.getClass().getMethod("getCreationTime");
         var1 = (Long)var4.invoke(var3);
         this.startTime = var1;
      } catch (ThreadDeath var5) {
         throw var5;
      } catch (Throwable var6) {
         var1 = System.currentTimeMillis();
      }

      return var1;
   }

   private long getElapsedStartTime(long var1) throws ServerLifecycleException {
      long var3 = System.currentTimeMillis();
      long var5 = var3 - var1;
      String var7 = System.getProperty("launch.time.log");
      if (var7 != null) {
         System.out.println("Startup completed in " + var5 + "ms.");

         try {
            String var8 = var5 + "\n";
            File var9 = new File(var7);
            if (!var9.exists() || var9.length() == 0L) {
               var8 = System.getProperty("java.vm.name") + " " + System.getProperty("java.runtime.version") + " " + (new Date()).toString() + System.getProperty("line.separator") + var8;
            }

            FileOutputStream var10 = new FileOutputStream(var9, true);
            var10.write(var8.getBytes());
            var10.close();
            System.out.println("Logged to " + var9.getAbsolutePath());
         } catch (Exception var11) {
            System.out.println("Failed to log: " + var11);
         }

         this.shutdown(false, true);
      }

      return var5;
   }

   private void logStartupStatistics() {
      if (loggerSERVER_START_STATISTICS.isDebugEnabled()) {
         MemoryMXBean var1 = ManagementFactory.getMemoryMXBean();
         var1.gc();
         MemoryUsage var2 = var1.getHeapMemoryUsage();
         MemoryUsage var3 = var1.getNonHeapMemoryUsage();
         List var4 = ManagementFactory.getMemoryPoolMXBeans();
         long var5 = 0L;
         long var7 = 0L;
         Iterator var9 = var4.iterator();

         while(var9.hasNext()) {
            MemoryPoolMXBean var10 = (MemoryPoolMXBean)var9.next();
            MemoryUsage var11 = var10.getUsage();
            MemoryUsage var12 = var10.getPeakUsage();
            loggerSERVER_START_STATISTICS.debug("MemoryPool(name,type,current,peak):" + var10.getName() + "," + var10.getType() + "," + var11.getUsed() + "," + var12.getUsed());
            if (var10.getType() == MemoryType.HEAP) {
               var5 += var12.getUsed();
            } else {
               var7 += var12.getUsed();
            }
         }

         loggerSERVER_START_STATISTICS.debug("Statistics(time,heap,peakheap,nonheap,peaknonheap):" + this.startupTime + "," + var2.getUsed() + "," + var5 + "," + var3.getUsed() + "," + var7);
      }
   }

   private String getStartupMode() throws ServerLifecycleException {
      String var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getStartupMode();

      try {
         if ("STANDBY".equalsIgnoreCase(var1)) {
            ensureAdminChannel();
         }

         return var1;
      } catch (ServerLifecycleException var3) {
         singleton.setFailedState(var3, false);
         throw var3;
      }
   }

   private static long updateStartTimeout(long var0, long var2) throws ServerLifecycleException {
      long var4 = var0 - (System.currentTimeMillis() - var2);
      if (var4 <= 0L) {
         singleton.setFailedState(ServerServicesManager.STARTUP_TIMED_OUT, false);
         throw new ServerLifecycleException("Startup timed out");
      } else {
         return var4;
      }
   }

   private static void ensureAdminChannel() throws ServerLifecycleException {
      if (!isAdminChannelEnabled()) {
         T3SrvrTextTextFormatter var0 = new T3SrvrTextTextFormatter();
         throw new ServerLifecycleException(var0.getStartupWithoutAdminChannel());
      }
   }

   private static boolean isAdminChannelEnabled() {
      return ChannelHelper.isLocalAdminChannelEnabled();
   }

   private static void pauseBeforeStartup() {
      try {
         int var0 = 30;
         if (System.getProperty("weblogic.sleepOnStartSecs") != null) {
            try {
               var0 = Integer.parseInt(System.getProperty("weblogic.sleepOnStartSecs"));
            } catch (Exception var2) {
               System.out.println("Server Failed parse time, using default of: '" + var0 + "'");
            }

            System.out.println("Server Sleeping for: '" + var0 + "' seconds");
            Thread.sleep((long)(var0 * 1000));
            System.out.println("Server Waking");
         }
      } catch (AccessControlException var3) {
      } catch (Exception var4) {
         System.out.println("Server Failed to sleep");
      }

   }

   private static void initSubjectManager() {
      Object var0 = PluginUtils.createPlugin(SubjectManager.class, CommandLine.getCommandLine().getSecurityFWSubjectManagerClassNameProp());
      if (var0 != null) {
         SubjectManager.setSubjectManager((SubjectManager)var0);
      } else {
         SubjectManager.setSubjectManager(new SubjectManagerImpl());
      }

   }

   private static void handleFatalInitializationException(Throwable var0) {
      String var1 = var0.getMessage();
      Throwable var2 = var0.getCause();
      if (!(var0 instanceof Error) && !(var2 instanceof InteractiveConfigurationException) && !(var2 instanceof SecurityInitializationException) && !(var2 instanceof AccessControlException) && !(var0 instanceof SecurityInitializationException)) {
         StringBuffer var3;
         if (var1 == null) {
            var3 = new StringBuffer();
         } else {
            var3 = new StringBuffer(var1);
         }

         while(var2 != null) {
            String var4 = var2.getMessage();
            if (var1 != null) {
               if (var4 != null && var1.indexOf(var4) == -1) {
                  var3.append(var4);
               }
            } else if (var4 != null) {
               var3.append(var4);
            }

            var0 = var2;
            var2 = var2.getCause();
         }

         try {
            if (Kernel.isInitialized()) {
               T3SrvrLogger.logNotInitialized(var3.toString());
            }

            signalCriticalFailure(var3.toString(), var0);
         } catch (Throwable var5) {
            System.err.println("Unable to dump log: '" + var5.getMessage() + "'");
         }

      } else {
         signalCriticalFailure(var1, (Throwable)null);
      }
   }

   private static void signalCriticalFailure(String var0, Throwable var1) {
      System.out.flush();
      System.err.println("***************************************************************************");
      System.err.println("The WebLogic Server encountered a critical failure");
      if (var1 != null) {
         if (!(var1 instanceof RuntimeException) && !(var1 instanceof Error) && !(var1 instanceof ServiceFailureException)) {
            System.err.println("Exception raised: '" + var1 + "'");
         } else {
            var1.printStackTrace();
         }
      }

      if (var0 != null && var0.length() > 0) {
         System.err.println("Reason: " + var0);
      }

      if (var1 instanceof AccessControlException) {
         System.err.println("Check you have both java.security.manager and java.security.policy defined");
      }

      System.err.println("***************************************************************************");
   }

   private T3Srvr() {
      T3Srvr.StateChangeValidator.initialize();
      singleton = this;
   }

   void initializeServerRuntime(ServerRuntime var1) {
      this.serverRuntimeMBean = var1;
      this.setState(1);
   }

   private void initializeStandby() throws ServerLifecycleException {
      try {
         this.startTime = System.currentTimeMillis();
         ServerServicesManager.startInStandbyState();
         this.lockoutManager = new ServerLockoutManager();
         this.am = SecurityServiceManager.getAuthorizationManager(kernelId, "weblogicDEFAULT");
         singleton.setState(3);
      } catch (ServiceFailureException var2) {
         singleton.setFailedState(var2, false);
         throw new ServerLifecycleException(var2);
      } catch (Throwable var3) {
         singleton.setFailedState(var3, true);
         throw new ServerLifecycleException(var3);
      }
   }

   private void initializeAdmin(long var1) throws ServerLifecycleException {
      try {
         this.startTime = System.currentTimeMillis();
         this.setState(1);
         ServerServicesManager.startInAdminState(var1);
         singleton.setState(17);
      } catch (ServiceFailureException var4) {
         singleton.setFailedState(var4, false);
         throw new ServerLifecycleException(var4);
      } catch (Throwable var5) {
         singleton.setFailedState(var5, true);
         throw new ServerLifecycleException(var5);
      }
   }

   void resume() throws ServerLifecycleException {
      long var1 = (long)(ManagementService.getRuntimeAccess(kernelId).getServer().getStartupTimeout() * 1000);
      this.checkPrivileges("unlock");
      this.resume(var1);
   }

   private void resume(long var1) throws ServerLifecycleException {
      try {
         if (this.srvrState != 2) {
            long var3 = var1;
            if (this.srvrState == 3) {
               long var5 = System.currentTimeMillis();
               this.initializeAdmin(var1);
               if (var1 > 0L) {
                  var3 = updateStartTimeout(var1, var5);
               }
            }

            singleton.setState(6);
            ServerServicesManager.resume(var3);
            this.setState(2);
            ServerServicesManager.invokeRunningStateListeners();
            this.fallbackState = -1;
         }
      } catch (ServiceFailureException var7) {
         singleton.setFailedState(var7, false);
         throw new ServerLifecycleException(var7);
      } catch (IllegalStateException var8) {
         throw new ServerLifecycleException(var8);
      } catch (Throwable var9) {
         singleton.setFailedState(var9, true);
         throw new ServerLifecycleException(var9);
      }
   }

   private void waitForDeath() {
      do {
         if (ManagementService.getRuntimeAccess(kernelId).getServer().isConsoleInputEnabled()) {
            T3SrvrConsole var1 = new T3SrvrConsole();
            var1.processCommands();
         } else {
            synchronized(this) {
               while(this.isWaitingToDie()) {
                  try {
                     this.wait();
                  } catch (InterruptedException var4) {
                  }
               }
            }
         }
      } while(this.isWaitingToDie());

   }

   private boolean isWaitingToDie() {
      return this.srvrState == 2 || this.srvrState == 3 || this.srvrState == 17;
   }

   void gracefulShutdown(boolean var1) throws ServerLifecycleException {
      this.checkShutdownPrivileges();
      this.shutdown(true, var1);
   }

   void forceShutdown() throws ServerLifecycleException {
      this.checkShutdownPrivileges();
      this.shutdown(false, true);
   }

   private void addShutdownHook() {
      Runtime.getRuntime().addShutdownHook(new Thread() {
         public void run() {
            if (T3Srvr.this.setPreventShutdownHook()) {
               T3SrvrLogger.logShutdownHookCalled();

               try {
                  T3Srvr.getT3Srvr().forceShutdown();
               } catch (ServerLifecycleException var2) {
                  var2.printStackTrace();
                  FileUtils.removeLockFiles();
                  T3Srvr.JrveVM.setShutdownAction(-1);
                  Runtime.getRuntime().halt(-1);
               }

            }
         }
      });
   }

   private synchronized boolean setPreventShutdownHook() {
      if (this.preventShutdownHook) {
         return false;
      } else {
         this.preventShutdownHook = true;
         return true;
      }
   }

   void requestShutdownFromConsole() throws SecurityException {
      this.setShutdownWaitSecs(0);

      try {
         this.forceShutdown();
      } catch (ServerLifecycleException var2) {
         throw new SecurityException(var2.toString());
      }
   }

   void gracefulSuspend(boolean var1) throws ServerLifecycleException {
      this.checkPrivileges("lock");
      this.suspend(true, var1);
   }

   void forceSuspend() throws ServerLifecycleException {
      this.checkPrivileges("lock");
      this.suspend(false, true);
   }

   private void shutdown(boolean var1, boolean var2) throws ServerLifecycleException {
      try {
         if (!var1) {
            this.prepareForForceShutdown();
         }

         ServerServicesManager.abortStartup();
         if (this.shutWaitSecs > 0) {
            this.setState(11);
            if (this.isShutdownCancelled()) {
               return;
            }
         }

         this.isShuttingDown = true;
         if (isServerBeyondAdminState(this.srvrState)) {
            this.suspend(var1, var2);
         }

         this.fallbackState = -1;
         if (var1) {
            this.setState(7);
            ServerServicesManager.stop(0, var2);
         } else {
            this.setState(18);
            ServerServicesManager.halt(0);
         }

         this.setPreventShutdownHook();
         synchronized(this) {
            singleton.notifyAll();
         }
      } catch (ServerLifecycleException var6) {
         throw var6;
      } catch (IllegalStateException var7) {
         throw new ServerLifecycleException(var7);
      } catch (Exception var8) {
         T3SrvrLogger.logFailedToShutdownServer(var8);
         this.setFailedState(var8, false);
         throw new ServerLifecycleException(var8);
      }
   }

   private void prepareForForceShutdown() {
      this.setPreventShutdownHook();
      ServerLifeCycleTimerThread.startTimeBomb();
   }

   private void suspend(boolean var1, boolean var2) throws ServerLifecycleException {
      try {
         if (var1) {
            this.setState(4);
            ServerServicesManager.stop(17, var2);
         } else {
            this.setState(5);
            ServerServicesManager.halt(17);
         }

         this.fallbackState = 17;
         if (this.srvrState != 17) {
            this.setState(17);
         }
      } catch (IllegalStateException var4) {
         throw new ServerLifecycleException(var4);
      } catch (Exception var5) {
         T3SrvrLogger.logFailedToShutdownServer(var5);
         this.setFailedState(var5, true);
         throw new ServerLifecycleException(var5);
      }
   }

   private static synchronized boolean isServerBeyondAdminState(int var0) {
      return var0 == 2 || var0 == 6 || var0 == 4;
   }

   public String cancelShutdown() {
      this.checkShutdownPrivileges();
      synchronized(this) {
         if (this.srvrState != 10) {
            if (this.srvrState == 11) {
               return T3SrvrLogger.logNoCancelShutdownTooLate();
            }

            return T3SrvrLogger.logNoCancelShutdownAlreadyNotShutting();
         }

         this.srvrState = 2;
      }

      this.setShutdownWaitSecs(0);
      T3SrvrLogger.logCancelShutdownInitiated();
      synchronized(this) {
         this.notifyAll();
      }

      return T3SrvrLogger.logCancelShutdownHappened();
   }

   private boolean isShutdownCancelled() {
      if (this.shutWaitSecs > 0) {
         T3SrvrLogger.logWaitingForShutdown(this.shutWaitSecs);
         synchronized(this) {
            this.srvrState = 10;
         }

         synchronized(this) {
            try {
               int var2 = this.shutWaitSecs;
               this.setShutdownWaitSecs(0);
               this.wait((long)(var2 * 1000));
            } catch (InterruptedException var4) {
            }

            if (this.srvrState == 10) {
               this.srvrState = 11;
            }
         }
      }

      if (this.srvrState == 11) {
         T3SrvrLogger.logNotWaitingForShutdown();
         return false;
      } else {
         return true;
      }
   }

   private void checkShutdownPrivileges() throws SecurityException {
      if (ManagementService.isRuntimeAccessInitialized()) {
         if (ManagementService.getRuntimeAccess(kernelId).getServer().isConsoleInputEnabled()) {
            T3SrvrLogger.logShutdownFromCommandLineOnly();
            throw new SecurityException("shutdown from command line only when weblogic.ConsoleInputEnabled=true");
         } else {
            this.checkPrivileges("shutdown");
         }
      }
   }

   private void checkPrivileges(String var1) throws SecurityException {
      String var2 = null;
      AuthenticatedSubject var3 = SecurityServiceManager.getCurrentSubject(kernelId);
      if (var3 != null && (var2 = SubjectUtils.getUsername(var3)) != null && var2.trim().length() != 0) {
         T3SrvrLogger.logOperationRequested(var1, var2);
         if (ManagementService.isRuntimeAccessInitialized()) {
            ServerResource var4 = new ServerResource((String)null, ManagementService.getRuntimeAccess(kernelId).getServerName(), var1);
            if (this.am != null && !this.am.isAccessAllowed(var3, var4, (ContextHandler)null)) {
               throw new SecurityException("User: '" + var2 + "' does not have permission to " + var1 + " server");
            }
         }
      } else {
         throw new SecurityException("Cannot " + var1 + " the server, " + "the request was from a nameless user (Principal)");
      }
   }

   public synchronized void abortStartupAfterAdminState() throws ServerLifecycleException {
      if (this.srvrState != 2 && this.srvrState != 6) {
         this.abortStartupAfterAdminState = true;
      } else {
         throw new ServerLifecycleException("cannot abort startup in admin state as current state is " + this.getState());
      }
   }

   boolean isAbortStartupAfterAdminState() {
      return this.abortStartupAfterAdminState;
   }

   private static class JrveVM {
      private static final boolean isJRVE = OperatingSystem.isJRVE();
      private static boolean wipeOutRestartFile;
      private static Class jrveVM;
      private static Class shutdownAction;
      private static Class VM_CLASS;
      private static Object VM;
      private static Field POWEROFF;
      private static Field REBOOT;
      private static Field CRASH;
      private static Method setShutdownAction;
      private static final Class[] VM_SHUTDOWN_ACTION = new Class[1];

      private static int getMaxNumRestarts() {
         return ManagementService.getRuntimeAccess(T3Srvr.kernelId).getServer().getRestartMax();
      }

      private static boolean isAutoRestartEnabled() {
         return ManagementService.getRuntimeAccess(T3Srvr.kernelId).getServer().getAutoRestart();
      }

      private static int getRestartDelaySeconds() {
         return ManagementService.getRuntimeAccess(T3Srvr.kernelId).getServer().getRestartDelaySeconds();
      }

      private static int getRestartIntervalSeconds() {
         return ManagementService.getRuntimeAccess(T3Srvr.kernelId).getServer().getRestartIntervalSeconds();
      }

      private static void setShutdownAction(int var0) {
         wipeOutRestartFile = true;

         try {
            if (isJRVE) {
               if (jrveVM == null) {
                  shutdownAction = Class.forName("com.oracle.jrockitve.VM$ShutdownAction");
                  POWEROFF = shutdownAction.getField("POWEROFF");
                  REBOOT = shutdownAction.getField("REBOOT");
                  CRASH = shutdownAction.getField("CRASH");
                  jrveVM = Class.forName("com.oracle.jrockitve.VM");
                  Method var1 = jrveVM.getMethod("getVM", (Class[])null);
                  VM_CLASS = var1.getReturnType();
                  Class[] var2 = jrveVM.getClasses();
                  Class[] var3 = var2;
                  int var4 = var2.length;

                  for(int var5 = 0; var5 < var4; ++var5) {
                     Class var6 = var3[var5];
                     if (var6.getName().equals("com.oracle.jrockitve.VM$ShutdownAction")) {
                        VM_SHUTDOWN_ACTION[0] = var6;
                        break;
                     }
                  }

                  setShutdownAction = VM_CLASS.getMethod("setShutdownAction", VM_SHUTDOWN_ACTION);
                  VM = var1.invoke((Object)null, (Object[])null);
               }

               switch (var0) {
                  case -1:
                     if (T3Srvr.debugSLC.isEnabled()) {
                        T3Srvr.debugSLCWLDF.debug("Setting VM shutdown action to POWEROFF");
                     }

                     setShutdownAction.invoke(VM, POWEROFF.get((Object)null));
                     break;
                  case 0:
                     if (T3Srvr.debugSLC.isEnabled()) {
                        T3Srvr.debugSLCWLDF.debug("Setting VM shutdown action to POWEROFF");
                     }

                     setShutdownAction.invoke(VM, POWEROFF.get((Object)null));
                     break;
                  case 1:
                  case 65:
                     if (isAutoRestartEnabled() && T3Srvr.numRestartsSoFar < getMaxNumRestarts()) {
                        if (T3Srvr.debugSLC.isEnabled()) {
                           T3Srvr.debugSLCWLDF.debug("Setting VM shutdown action to REBOOT");
                        }

                        setShutdownAction.invoke(VM, REBOOT.get((Object)null));
                        wipeOutRestartFile = System.currentTimeMillis() - T3Srvr.singleton.getStartTime() > (long)(getRestartIntervalSeconds() * 1000);
                     } else {
                        if (T3Srvr.debugSLC.isEnabled()) {
                           T3Srvr.debugSLCWLDF.debug("Setting VM shutdown action to POWEROFF");
                        }

                        setShutdownAction.invoke(VM, POWEROFF.get((Object)null));
                     }
               }
            }
         } catch (Exception var10) {
            System.err.println("Error in invoking JRVE setShutdownAction: ");
            var10.printStackTrace();
         } finally {
            if (wipeOutRestartFile) {
               T3Srvr.deleteRestartFile();
            }

         }

      }
   }

   private static final class StateChangeValidator {
      private static final NumericKeyHashMap validStateTransitions = new NumericKeyHashMap();
      private static final int[] statesAlwaysAllowed = new int[]{8, 15, 14};

      private static void initialize() {
         validStateTransitions.put(1L, new int[]{3, 17, 7, 18});
         validStateTransitions.put(3L, new int[]{7, 18, 1});
         validStateTransitions.put(17L, new int[]{7, 18, 6});
         validStateTransitions.put(6L, new int[]{2, 5});
         validStateTransitions.put(2L, new int[]{4, 5, 10, 11});
         validStateTransitions.put(4L, new int[]{5, 17});
         validStateTransitions.put(5L, new int[]{17});
         validStateTransitions.put(7L, new int[]{18, 0});
         validStateTransitions.put(18L, new int[]{0});
         validStateTransitions.put(10L, new int[]{5, 4, 2});
      }

      private static boolean validate(int var0, int var1) {
         if (isNewStateAlwaysAllowed(var1)) {
            return true;
         } else {
            int[] var2 = (int[])((int[])validStateTransitions.get((long)var0));
            if (var2 == null) {
               return true;
            } else {
               for(int var3 = 0; var3 < var2.length; ++var3) {
                  if (var1 == var2[var3]) {
                     return true;
                  }
               }

               return false;
            }
         }
      }

      private static boolean isNewStateAlwaysAllowed(int var0) {
         for(int var1 = 0; var1 < statesAlwaysAllowed.length; ++var1) {
            if (var0 == statesAlwaysAllowed[var1]) {
               return true;
            }
         }

         return false;
      }
   }
}
