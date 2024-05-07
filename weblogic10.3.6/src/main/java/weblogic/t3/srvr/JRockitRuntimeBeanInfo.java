package weblogic.t3.srvr;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JRockitRuntimeMBean;

public class JRockitRuntimeBeanInfo extends JVMRuntimeBeanInfo {
   public static Class INTERFACE_CLASS = JRockitRuntimeMBean.class;

   public JRockitRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JRockitRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JRockitRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.t3.srvr");
      String var3 = (new String("Exposes runtime data about the JRockit Virtual Machine (VM) that is running the current WebLogic Server instance. You cannot change the VM's operating parameters while the VM is active. Instead, use the startup options that are described in the JRockit documentation.  <p>The WebLogic JVM contains only one of these Runtime MBeans: <ul> <li> <p>If the JVM is an instance of a JRockit JDK, then the JVM contains <code>JRockitRuntime MBean</code>.</p> </li>  <li> <p> Otherwise, it contains the <code>JVMRuntimeMBean</code>.</p> </li> </ul></p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JRockitRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      String[] var5;
      if (!var1.containsKey("AllProcessorsAverageLoad")) {
         var3 = "getAllProcessorsAverageLoad";
         var4 = null;
         var2 = new PropertyDescriptor("AllProcessorsAverageLoad", JRockitRuntimeMBean.class, var3, var4);
         var1.put("AllProcessorsAverageLoad", var2);
         var2.setValue("description", "<p>A snapshot of the average load of all processors in the host computer. If the computer has only one processor, this method returns the same value as <code>getJvmProcessorLoad(0)</code>.</p>  <p>The value is returned as a double, where 1.0 represents 100% load (no idle time) and 0.0 represents 0% load (pure idle time).</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getProcessorLoad(int)")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("FreeHeap")) {
         var3 = "getFreeHeap";
         var4 = null;
         var2 = new PropertyDescriptor("FreeHeap", JRockitRuntimeMBean.class, var3, var4);
         var1.put("FreeHeap", var2);
         var2.setValue("description", "<p>The amount (in bytes) of Java heap memory that is currently free in the Virtual Machine.</p> ");
      }

      if (!var1.containsKey("FreePhysicalMemory")) {
         var3 = "getFreePhysicalMemory";
         var4 = null;
         var2 = new PropertyDescriptor("FreePhysicalMemory", JRockitRuntimeMBean.class, var3, var4);
         var1.put("FreePhysicalMemory", var2);
         var2.setValue("description", "<p>The amount (in bytes) of physical memory that is currently free on the host computer.</p> ");
      }

      if (!var1.containsKey("GcAlgorithm")) {
         var3 = "getGcAlgorithm";
         var4 = null;
         var2 = new PropertyDescriptor("GcAlgorithm", JRockitRuntimeMBean.class, var3, var4);
         var1.put("GcAlgorithm", var2);
         var2.setValue("description", "<p>The type of garbage collector (GC) that the Virtual Machine is using.</p>  <p>JRockit provides the following types of GCs:</p>  <ul> <li> <p>Generational Copying, which is suitable for testing applications on a desktop machine with a small (less then 128 MB) heap.</p> </li>  <li> <p>Single Spaced Concurrent, which reduces or eliminates pauses in the VM that are due to garbage collection. Because it trades memory throughput for reduced pause time, you generally need a larger heap size than with other GC types. If your ordinary Java threads create more garbage than this GC can collect, the VM will pause while the Java threads wait for the garbage collection to finish.</p> </li>  <li> <p>Generational Concurrent, which creates a \"nursery\" space within the heap. New objects are created within the nursery. When the nursery is full, JRockit \"stops-the-world,\" removes the dead objects from the nursery, and moves live objects to a different space within the heap. Another thread runs in the background to remove dead objects from the non-nursery space. This GC type has a higher memory throughput than a single spaced concurrent GC.</p> </li>  <li> <p>Parallel, which allocates all objects to a single spaced heap. When the heap is full, all Java threads are stopped and every CPU is used to perform a complete garbage collection of the entire heap. This behavior causes longer pause times than for the concurrent collectors but maximizes memory throughput.</p> </li> </ul> ");
      }

      if (!var1.containsKey("HeapFreeCurrent")) {
         var3 = "getHeapFreeCurrent";
         var4 = null;
         var2 = new PropertyDescriptor("HeapFreeCurrent", JRockitRuntimeMBean.class, var3, var4);
         var1.put("HeapFreeCurrent", var2);
         var2.setValue("description", "<p>The current amount of memory (in bytes) that is available in the JVM heap.</p> ");
      }

      if (!var1.containsKey("HeapFreePercent")) {
         var3 = "getHeapFreePercent";
         var4 = null;
         var2 = new PropertyDescriptor("HeapFreePercent", JRockitRuntimeMBean.class, var3, var4);
         var1.put("HeapFreePercent", var2);
         var2.setValue("description", "<p>Percentage of the maximum memory that is free.</p> ");
      }

      if (!var1.containsKey("HeapSizeCurrent")) {
         var3 = "getHeapSizeCurrent";
         var4 = null;
         var2 = new PropertyDescriptor("HeapSizeCurrent", JRockitRuntimeMBean.class, var3, var4);
         var1.put("HeapSizeCurrent", var2);
         var2.setValue("description", "<p>The current size (in bytes) of the JVM heap.</p> ");
      }

      if (!var1.containsKey("HeapSizeMax")) {
         var3 = "getHeapSizeMax";
         var4 = null;
         var2 = new PropertyDescriptor("HeapSizeMax", JRockitRuntimeMBean.class, var3, var4);
         var1.put("HeapSizeMax", var2);
         var2.setValue("description", "<p>The maximum free memory configured for this JVM.</p> ");
      }

      if (!var1.containsKey("JVMDescription")) {
         var3 = "getJVMDescription";
         var4 = null;
         var2 = new PropertyDescriptor("JVMDescription", JRockitRuntimeMBean.class, var3, var4);
         var1.put("JVMDescription", var2);
         var2.setValue("description", "<p>A description of the Java Virtual Machine. For example, \"BEA WebLogic JRockit Java Virtual Machine.\"</p> ");
      }

      if (!var1.containsKey("JavaVMVendor")) {
         var3 = "getJavaVMVendor";
         var4 = null;
         var2 = new PropertyDescriptor("JavaVMVendor", JRockitRuntimeMBean.class, var3, var4);
         var1.put("JavaVMVendor", var2);
         var2.setValue("description", "Returns the vendor of the JVM.  <p>The vendor of the JVM that this server runs.</p>  system property java.vm.vendor is returned ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("JavaVendor")) {
         var3 = "getJavaVendor";
         var4 = null;
         var2 = new PropertyDescriptor("JavaVendor", JRockitRuntimeMBean.class, var3, var4);
         var1.put("JavaVendor", var2);
         var2.setValue("description", "Returns the vendor of Java.  <p>The vendor of Java that this server runs.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("JavaVersion")) {
         var3 = "getJavaVersion";
         var4 = null;
         var2 = new PropertyDescriptor("JavaVersion", JRockitRuntimeMBean.class, var3, var4);
         var1.put("JavaVersion", var2);
         var2.setValue("description", "<p>The Java version of the JVM.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("JvmProcessorLoad")) {
         var3 = "getJvmProcessorLoad";
         var4 = null;
         var2 = new PropertyDescriptor("JvmProcessorLoad", JRockitRuntimeMBean.class, var3, var4);
         var1.put("JvmProcessorLoad", var2);
         var2.setValue("description", "<p>A snapshot of the load that the Virtual Machine is placing on all processors in the host computer. If the host contains multiple processors, the value represents a snapshot of the average load.</p>  <p>The value is returned as a double, where 1.0 represents 100% load (no idle time) and 0.0 represents 0% load (pure idle time).</p> ");
      }

      if (!var1.containsKey("LastGCEnd")) {
         var3 = "getLastGCEnd";
         var4 = null;
         var2 = new PropertyDescriptor("LastGCEnd", JRockitRuntimeMBean.class, var3, var4);
         var1.put("LastGCEnd", var2);
         var2.setValue("description", "<p>The time at which the last garbage collection run ended.</p> ");
      }

      if (!var1.containsKey("LastGCStart")) {
         var3 = "getLastGCStart";
         var4 = null;
         var2 = new PropertyDescriptor("LastGCStart", JRockitRuntimeMBean.class, var3, var4);
         var1.put("LastGCStart", var2);
         var2.setValue("description", "<p>The time at which the last garbage collection run started.</p> ");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", JRockitRuntimeMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The name of the Java Virtual Machine. For example, \"WebLogic JRockit.\"</p> ");
      }

      if (!var1.containsKey("NumberOfDaemonThreads")) {
         var3 = "getNumberOfDaemonThreads";
         var4 = null;
         var2 = new PropertyDescriptor("NumberOfDaemonThreads", JRockitRuntimeMBean.class, var3, var4);
         var1.put("NumberOfDaemonThreads", var2);
         var2.setValue("description", "<p>The number of daemon Java threads currently running in the Virtual Machine across all processors.</p> ");
      }

      if (!var1.containsKey("NumberOfProcessors")) {
         var3 = "getNumberOfProcessors";
         var4 = null;
         var2 = new PropertyDescriptor("NumberOfProcessors", JRockitRuntimeMBean.class, var3, var4);
         var1.put("NumberOfProcessors", var2);
         var2.setValue("description", "<p>The number of processors on the Virtual Machine's host computer. If this is not a Symmetric Multi Processor (SMP) system, the value will be <code>1</code>.</p> ");
      }

      if (!var1.containsKey("OSName")) {
         var3 = "getOSName";
         var4 = null;
         var2 = new PropertyDescriptor("OSName", JRockitRuntimeMBean.class, var3, var4);
         var1.put("OSName", var2);
         var2.setValue("description", "Returns the operating system on which the JVM is running.  <p>The operating system on which the JVM is running.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("OSVersion")) {
         var3 = "getOSVersion";
         var4 = null;
         var2 = new PropertyDescriptor("OSVersion", JRockitRuntimeMBean.class, var3, var4);
         var1.put("OSVersion", var2);
         var2.setValue("description", "<p>The version of the operating system on which the JVM is running.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("PauseTimeTarget")) {
         var3 = "getPauseTimeTarget";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPauseTimeTarget";
         }

         var2 = new PropertyDescriptor("PauseTimeTarget", JRockitRuntimeMBean.class, var3, var4);
         var1.put("PauseTimeTarget", var2);
         var2.setValue("description", "<p>Gets the maximum GC pause time if set</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ThreadStackDump")) {
         var3 = "getThreadStackDump";
         var4 = null;
         var2 = new PropertyDescriptor("ThreadStackDump", JRockitRuntimeMBean.class, var3, var4);
         var1.put("ThreadStackDump", var2);
         var2.setValue("description", "<p>JVM thread dump. Thread dump is available only on 1.5 VM</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("TotalGarbageCollectionCount")) {
         var3 = "getTotalGarbageCollectionCount";
         var4 = null;
         var2 = new PropertyDescriptor("TotalGarbageCollectionCount", JRockitRuntimeMBean.class, var3, var4);
         var1.put("TotalGarbageCollectionCount", var2);
         var2.setValue("description", "<p>The number of garbage collection runs that have occurred since the Virtual Machine was started.</p> ");
      }

      if (!var1.containsKey("TotalGarbageCollectionTime")) {
         var3 = "getTotalGarbageCollectionTime";
         var4 = null;
         var2 = new PropertyDescriptor("TotalGarbageCollectionTime", JRockitRuntimeMBean.class, var3, var4);
         var1.put("TotalGarbageCollectionTime", var2);
         var2.setValue("description", "<p>The number of milliseconds that the Virtual Machine has spent on all garbage collection runs since the VM was started.</p> ");
      }

      if (!var1.containsKey("TotalHeap")) {
         var3 = "getTotalHeap";
         var4 = null;
         var2 = new PropertyDescriptor("TotalHeap", JRockitRuntimeMBean.class, var3, var4);
         var1.put("TotalHeap", var2);
         var2.setValue("description", "<p>The amount (in bytes) of memory currently allocated to the Virtual Machine's Java heap.</p>  <p>This value, which is also known as the \"heap size,\" may grow up to the specified maximum heap size.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getMaxHeapSize")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("TotalNumberOfThreads")) {
         var3 = "getTotalNumberOfThreads";
         var4 = null;
         var2 = new PropertyDescriptor("TotalNumberOfThreads", JRockitRuntimeMBean.class, var3, var4);
         var1.put("TotalNumberOfThreads", var2);
         var2.setValue("description", "<p>The number of Java threads (daemon and non-daemon) that are currently running in the Virtual Machine across all processors.</p> ");
      }

      if (!var1.containsKey("TotalNurserySize")) {
         var3 = "getTotalNurserySize";
         var4 = null;
         var2 = new PropertyDescriptor("TotalNurserySize", JRockitRuntimeMBean.class, var3, var4);
         var1.put("TotalNurserySize", var2);
         var2.setValue("description", "<p>The amount (in bytes) of memory that is currently allocated to the nursery.</p>  <p>The nursery is the area of the Java heap that the VM allocates to most objects. Instead of garbage collecting the entire heap, generational garbage collectors focus on the nursery. Because most objects die young, most of the time it is sufficient to garbage collect only the nursery and not the entire heap.</p>  <p>If you are not using a generational garbage collector, the nursery size is <code><code>0</code></code>.</p> ");
      }

      if (!var1.containsKey("TotalPhysicalMemory")) {
         var3 = "getTotalPhysicalMemory";
         var4 = null;
         var2 = new PropertyDescriptor("TotalPhysicalMemory", JRockitRuntimeMBean.class, var3, var4);
         var1.put("TotalPhysicalMemory", var2);
         var2.setValue("description", "<p>The amount (in bytes) of physical memory on the host computer.</p>  <p>The value does not include memory that an operating system makes available through swap space on a disk or other types of virtual memory.</p> ");
      }

      if (!var1.containsKey("Uptime")) {
         var3 = "getUptime";
         var4 = null;
         var2 = new PropertyDescriptor("Uptime", JRockitRuntimeMBean.class, var3, var4);
         var1.put("Uptime", var2);
         var2.setValue("description", "<p>The number of milliseconds that the Virtual Machine has been running.</p> ");
      }

      if (!var1.containsKey("UsedHeap")) {
         var3 = "getUsedHeap";
         var4 = null;
         var2 = new PropertyDescriptor("UsedHeap", JRockitRuntimeMBean.class, var3, var4);
         var1.put("UsedHeap", var2);
         var2.setValue("description", "<p>The amount (in bytes) of Java heap memory that is currently being used by the Virtual Machine.</p> ");
      }

      if (!var1.containsKey("UsedPhysicalMemory")) {
         var3 = "getUsedPhysicalMemory";
         var4 = null;
         var2 = new PropertyDescriptor("UsedPhysicalMemory", JRockitRuntimeMBean.class, var3, var4);
         var1.put("UsedPhysicalMemory", var2);
         var2.setValue("description", "<p>The amount (in bytes) of physical memory that is currently being used on the host computer.</p>  <p>The value describes the memory that is being used by all processes on the computer, not just by the Virtual Machine.</p> ");
      }

      if (!var1.containsKey("Vendor")) {
         var3 = "getVendor";
         var4 = null;
         var2 = new PropertyDescriptor("Vendor", JRockitRuntimeMBean.class, var3, var4);
         var1.put("Vendor", var2);
         var2.setValue("description", "<p>The name of the JVM vendor. </p> ");
      }

      if (!var1.containsKey("Version")) {
         var3 = "getVersion";
         var4 = null;
         var2 = new PropertyDescriptor("Version", JRockitRuntimeMBean.class, var3, var4);
         var1.put("Version", var2);
         var2.setValue("description", "<p>The current version of Java Virtual Machine.</p> ");
      }

      if (!var1.containsKey("Concurrent")) {
         var3 = "isConcurrent";
         var4 = null;
         var2 = new PropertyDescriptor("Concurrent", JRockitRuntimeMBean.class, var3, var4);
         var1.put("Concurrent", var2);
         var2.setValue("description", "<p>Indicates whether the VM's garbage collector runs in a separate Java thread concurrently with other Java threads.</p> ");
      }

      if (!var1.containsKey("GCHandlesCompaction")) {
         var3 = "isGCHandlesCompaction";
         var4 = null;
         var2 = new PropertyDescriptor("GCHandlesCompaction", JRockitRuntimeMBean.class, var3, var4);
         var1.put("GCHandlesCompaction", var2);
         var2.setValue("description", "<p>Indicates whether the VM's garbage collector compacts the Java heap.</p>  <p>Usually the heap is scattered throughout available memory. A garbage collector that compacts the heap defragments the memory space in addition to deleting unused objects.</p> ");
      }

      if (!var1.containsKey("Generational")) {
         var3 = "isGenerational";
         var4 = null;
         var2 = new PropertyDescriptor("Generational", JRockitRuntimeMBean.class, var3, var4);
         var1.put("Generational", var2);
         var2.setValue("description", "<p>Indicates whether the VM's garbage collector uses a nursery space.</p>  <p>A nursery is the area of the Java heap that the VM allocates to most objects. Instead of garbage collecting the entire heap, generational garbage collectors focus on the nursery. Because most objects die young, most of the time it is sufficient to garbage collect only the nursery and not the entire heap.</p> ");
      }

      if (!var1.containsKey("Incremental")) {
         var3 = "isIncremental";
         var4 = null;
         var2 = new PropertyDescriptor("Incremental", JRockitRuntimeMBean.class, var3, var4);
         var1.put("Incremental", var2);
         var2.setValue("description", "<p>Indicates whether the VM's garbage collector collects (increments) garbage as it scans the memory space and dumps the garbage at the end of its cycle.</p>  <p>With a non-incremental garbage collector, garbage is dumped as soon as it is encountered.</p> ");
      }

      if (!var1.containsKey("Parallel")) {
         var3 = "isParallel";
         var4 = null;
         var2 = new PropertyDescriptor("Parallel", JRockitRuntimeMBean.class, var3, var4);
         var1.put("Parallel", var2);
         var2.setValue("description", "<p>Indicates whether the VM's garbage collector is able to run in parallel on multiple processors if multiple processors are available.</p> ");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JRockitRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JRockitRuntimeMBean.class.getMethod("runGC");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Starts garbage collection and finalization algorithms within the JVM.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = JRockitRuntimeMBean.class.getMethod("isMethodTimingEnabled", Method.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("method", "the method you want to check. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "10.0.0.0 There will be no replacement for this method in future, since it will not be supported by JRockit management API. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Indicates whether the Virtual Machine measures how much time it spends in a method.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JRockitRuntimeMBean.class.getMethod("getMethodTiming", Method.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("Method", "the method we wish to check. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "10.0.0.0 There will be no replacement for this method in future, since it will not be supported by JRockit management API. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>The amount of time (in milliseconds) the Virtual Machine has spent in the method since enabling time measuring.</p>  <p>If time measuring has not been enabled for the specified method, this method returns a null value.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JRockitRuntimeMBean.class.getMethod("isMethodInvocationCountEnabled", Method.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("method", "the method you want to check. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "10.0.0.0 There will be no replacement for this method in future, since it will not be supported by JRockit management API. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Indicates whether the Virtual Machine counts how many times a method is invoked.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JRockitRuntimeMBean.class.getMethod("getMethodInvocationCount", Method.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("method", "the method you want to check. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "10.0.0.0 There will be no replacement for this method in future, since it will not be supported by JRockit management API. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>The number of times a method has been invoked since enabling invocation counting.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JRockitRuntimeMBean.class.getMethod("isConstructorTimingEnabled", Constructor.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("constructor", "the constructor you want to check. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "10.0.0.0 There will be no replacement for this method in future, since it will not be supported by JRockit management API. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Indicates whether the Virtual Machine measures how much time it spends in a constructor.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JRockitRuntimeMBean.class.getMethod("getConstructorTiming", Constructor.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("constructor", "the constructor you want to check. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "10.0.0.0 There will be no replacement for this method in future, since it will not be supported by JRockit management API. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>The amount of time (in milliseconds) the Virtual Machine has spent in the constructor since enabling time measuring.</p>  <p>If time measuring hasn't been enabled for the specified constructor, this method returns a null value.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JRockitRuntimeMBean.class.getMethod("isConstructorInvocationCountEnabled", Constructor.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("cons", "the constructor you want to check. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "10.0.0.0 There will be no replacement for this method in future, since it will not be supported by JRockit management API. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Indicates whether the Virtual Machine counts how many times a constructor is invoked.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JRockitRuntimeMBean.class.getMethod("getConstructorInvocationCount", Constructor.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("constructor", "- the constructor for which to return the invocation count. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "10.0.0.0 There will be no replacement for this method in future, since it will not be supported by JRockit management API. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>The number of times a constructor has been invoked since enabling invocation counting.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JRockitRuntimeMBean.class.getMethod("isExceptionCountEnabled", Class.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("throwableClass", "the exception class to get the counter for. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "10.0.0.0 There will be no replacement for this method in future, since it will not be supported by JRockit management API. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Indicates whether the Virtual Machine counts how many times an exception is thrown.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JRockitRuntimeMBean.class.getMethod("getExceptionCount", Class.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("throwableClass", "the exception class to get the counter for. If the  throwableClass is null, a NullPointerException will be thrown. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "10.0.0.0 There will be no replacement for this method in future, since it will not be supported by JRockit management API. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>The number of times an exception type has been thrown since enabling exception counting. If exception counting has not been enabled for the specified type, the result is unspecified.</p> ");
         var2.setValue("role", "operation");
      }

   }

   protected void buildMethodDescriptors(Map var1) throws IntrospectionException, NoSuchMethodException {
      this.fillinFinderMethodInfos(var1);
      if (!this.readOnly) {
         this.fillinCollectionMethodInfos(var1);
         this.fillinFactoryMethodInfos(var1);
      }

      this.fillinOperationMethodInfos(var1);
      super.buildMethodDescriptors(var1);
   }

   protected void buildEventSetDescriptors(Map var1) throws IntrospectionException {
   }
}
