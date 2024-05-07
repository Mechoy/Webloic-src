package weblogic.t3.srvr;

import java.security.AccessController;
import java.util.Locale;
import weblogic.health.HealthLogger;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.JVMRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.platform.GCListener;
import weblogic.platform.GarbageCollectionEvent;
import weblogic.platform.VM;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class JVMRuntime extends RuntimeMBeanDelegate implements JVMRuntimeMBean, GCListener {
   private static final long serialVersionUID = -6411120496639444681L;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Runtime runtime;
   private static JVMRuntime singleton;
   private static final long OOME_IMMINENT = 5000000L;
   private static final int FREE_MEM_PERCENT_DIFF_THRESHOLD = 10;
   private static final DebugCategory debugMemory = Debug.getCategory("weblogic.debug.memory");
   private int previousFreeMemPercent;
   private boolean sendNotificationOOME = true;

   protected JVMRuntime() throws ManagementException {
      super(ManagementService.getRuntimeAccess(kernelId).getServerName(), ManagementService.getRuntimeAccess(kernelId).getServerRuntime(), true, "JVMuntime");
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().setJVMRuntime(this);
      this.runtime = Runtime.getRuntime();
   }

   public static synchronized JVMRuntime init() throws ManagementException {
      if (singleton != null) {
         throw new IllegalStateException("Attempt to double initialize");
      } else {
         if (isJrockit()) {
            singleton = new JRockitRuntime();
         } else {
            singleton = new JVMRuntime();
            VM.getVM().addGCListener(singleton);
         }

         return singleton;
      }
   }

   public static synchronized JVMRuntime theOne() {
      return singleton;
   }

   private static boolean isJrockit() {
      String var0 = System.getProperty("java.vm.name");
      if (var0 == null) {
         var0 = "";
      }

      var0 = var0.toLowerCase(Locale.ENGLISH);
      return var0.indexOf("jrockit", 0) >= 0;
   }

   public void shutdown() {
      this.runtime.exit(0);
   }

   public void runGC() {
      this.runtime.gc();
   }

   public long getHeapFreeCurrent() {
      return this.runtime.freeMemory();
   }

   public long getHeapSizeCurrent() {
      return this.runtime.totalMemory();
   }

   public long getHeapSizeMax() {
      return this.runtime.maxMemory();
   }

   public String getJavaVersion() {
      return System.getProperty("java.version");
   }

   public String getJavaVendor() {
      return System.getProperty("java.vendor");
   }

   public String getJavaVMVendor() {
      return System.getProperty("java.vm.vendor");
   }

   public String getOSName() {
      return System.getProperty("os.name");
   }

   public String getOSVersion() {
      return System.getProperty("os.version");
   }

   public String getThreadStackDump() {
      return VM.getVM().threadDumpAsString();
   }

   public long getUptime() {
      return System.currentTimeMillis() - ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getActivationTime();
   }

   public int getHeapFreePercent() {
      return this.getHeapSizeMax() == Long.MAX_VALUE ? (int)(this.getHeapFreeCurrent() * 100L / this.getHeapSizeCurrent()) : (int)((this.getHeapSizeMax() - this.getHeapSizeCurrent() + this.getHeapFreeCurrent()) * 100L / this.getHeapSizeMax());
   }

   public synchronized void sendMemoryNotification(int var1, int var2) {
      this._postSet("HeapFreePercent", var1, var2);
   }

   public void onGarbageCollection(GarbageCollectionEvent var1) {
      long var2 = Runtime.getRuntime().freeMemory();
      long var4 = Runtime.getRuntime().totalMemory();
      long var6 = Runtime.getRuntime().maxMemory();
      int var8 = (int)(var2 * 100L / var4);
      this.logDebug(var8, var2, var4, var6);
      if (var2 + (var6 - var4) < 5000000L) {
         if (this.sendNotificationOOME) {
            HealthLogger.logOOMEImminent(var2);
            this.sendMemoryNotification(this.previousFreeMemPercent, var8);
            this.previousFreeMemPercent = var8;
            this.sendNotificationOOME = false;
         }

      } else {
         int var9 = var8 - this.previousFreeMemPercent;
         this.sendNotificationOOME = true;
         if (Math.abs(var9) > 10) {
            HealthLogger.logFreeMemoryChanged(var8);
            this.sendMemoryNotification(this.previousFreeMemPercent, var8);
            this.previousFreeMemPercent = var8;
         }

      }
   }

   private void logDebug(int var1, long var2, long var4, long var6) {
      if (debugMemory.isEnabled()) {
         HealthLogger.logDebugMsg("free mem " + var1 + "%, " + "free mem bytes " + var2 + ", " + "total mem " + var4 + ", " + "max mem " + var6);
      }

   }
}
