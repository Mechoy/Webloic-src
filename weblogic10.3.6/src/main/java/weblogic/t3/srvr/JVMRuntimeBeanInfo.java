package weblogic.t3.srvr;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JVMRuntimeMBean;

public class JVMRuntimeBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JVMRuntimeMBean.class;

   public JVMRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JVMRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JVMRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.t3.srvr");
      String var3 = (new String("Provides methods for retrieving information about the Java Virtual Machine (JVM) within with the current server instance is running. You cannot change the JVM's operating parameters while the JVM is active. Instead, use the startup options that are described in the JVM's documentation.  <p>The WebLogic JVM contains only one of these Runtime MBeans: <ul> <li> <p>If the JVM is an instance of a JRockit JDK, then the JVM contains <code>JRockitRuntime MBean</code>.</p> </li>  <li> <p> Otherwise, it contains the <code>JVMRuntimeMBean</code>.</p> </li> </ul></p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JVMRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("HeapFreeCurrent")) {
         var3 = "getHeapFreeCurrent";
         var4 = null;
         var2 = new PropertyDescriptor("HeapFreeCurrent", JVMRuntimeMBean.class, var3, (String)var4);
         var1.put("HeapFreeCurrent", var2);
         var2.setValue("description", "<p>The current amount of memory (in bytes) that is available in the JVM heap.</p> ");
      }

      if (!var1.containsKey("HeapFreePercent")) {
         var3 = "getHeapFreePercent";
         var4 = null;
         var2 = new PropertyDescriptor("HeapFreePercent", JVMRuntimeMBean.class, var3, (String)var4);
         var1.put("HeapFreePercent", var2);
         var2.setValue("description", "<p>Percentage of the maximum memory that is free.</p> ");
      }

      if (!var1.containsKey("HeapSizeCurrent")) {
         var3 = "getHeapSizeCurrent";
         var4 = null;
         var2 = new PropertyDescriptor("HeapSizeCurrent", JVMRuntimeMBean.class, var3, (String)var4);
         var1.put("HeapSizeCurrent", var2);
         var2.setValue("description", "<p>The current size (in bytes) of the JVM heap.</p> ");
      }

      if (!var1.containsKey("HeapSizeMax")) {
         var3 = "getHeapSizeMax";
         var4 = null;
         var2 = new PropertyDescriptor("HeapSizeMax", JVMRuntimeMBean.class, var3, (String)var4);
         var1.put("HeapSizeMax", var2);
         var2.setValue("description", "<p>The maximum free memory configured for this JVM.</p> ");
      }

      if (!var1.containsKey("JavaVMVendor")) {
         var3 = "getJavaVMVendor";
         var4 = null;
         var2 = new PropertyDescriptor("JavaVMVendor", JVMRuntimeMBean.class, var3, (String)var4);
         var1.put("JavaVMVendor", var2);
         var2.setValue("description", "Returns the vendor of the JVM.  <p>The vendor of the JVM that this server runs.</p>  system property java.vm.vendor is returned ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("JavaVendor")) {
         var3 = "getJavaVendor";
         var4 = null;
         var2 = new PropertyDescriptor("JavaVendor", JVMRuntimeMBean.class, var3, (String)var4);
         var1.put("JavaVendor", var2);
         var2.setValue("description", "Returns the vendor of Java.  <p>The vendor of Java that this server runs.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("JavaVersion")) {
         var3 = "getJavaVersion";
         var4 = null;
         var2 = new PropertyDescriptor("JavaVersion", JVMRuntimeMBean.class, var3, (String)var4);
         var1.put("JavaVersion", var2);
         var2.setValue("description", "<p>The Java version of the JVM.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("OSName")) {
         var3 = "getOSName";
         var4 = null;
         var2 = new PropertyDescriptor("OSName", JVMRuntimeMBean.class, var3, (String)var4);
         var1.put("OSName", var2);
         var2.setValue("description", "Returns the operating system on which the JVM is running.  <p>The operating system on which the JVM is running.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("OSVersion")) {
         var3 = "getOSVersion";
         var4 = null;
         var2 = new PropertyDescriptor("OSVersion", JVMRuntimeMBean.class, var3, (String)var4);
         var1.put("OSVersion", var2);
         var2.setValue("description", "<p>The version of the operating system on which the JVM is running.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ThreadStackDump")) {
         var3 = "getThreadStackDump";
         var4 = null;
         var2 = new PropertyDescriptor("ThreadStackDump", JVMRuntimeMBean.class, var3, (String)var4);
         var1.put("ThreadStackDump", var2);
         var2.setValue("description", "<p>JVM thread dump. Thread dump is available only on 1.5 VM</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("Uptime")) {
         var3 = "getUptime";
         var4 = null;
         var2 = new PropertyDescriptor("Uptime", JVMRuntimeMBean.class, var3, (String)var4);
         var1.put("Uptime", var2);
         var2.setValue("description", "<p>The number of milliseconds that the Virtual Machine has been running.</p> ");
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
      Method var3 = JVMRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JVMRuntimeMBean.class.getMethod("runGC");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Starts garbage collection and finalization algorithms within the JVM.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
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
