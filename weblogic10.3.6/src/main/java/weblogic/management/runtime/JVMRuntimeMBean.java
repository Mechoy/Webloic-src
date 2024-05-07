package weblogic.management.runtime;

public interface JVMRuntimeMBean extends RuntimeMBean {
   void shutdown();

   void runGC();

   long getHeapFreeCurrent();

   long getHeapSizeCurrent();

   long getHeapSizeMax();

   int getHeapFreePercent();

   String getJavaVersion();

   String getJavaVendor();

   String getJavaVMVendor();

   String getOSName();

   String getOSVersion();

   String getThreadStackDump();

   long getUptime();
}
