package weblogic.kodo.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.KodoDataCacheRuntimeMBean;

public class KodoDataCacheRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = KodoDataCacheRuntimeMBean.class;

   public KodoDataCacheRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public KodoDataCacheRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = KodoDataCacheRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.0.0.0");
      var2.setValue("package", "weblogic.kodo.monitoring");
      String var3 = (new String("Base class for all runtime mbeans that provide status of running modules.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.KodoDataCacheRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("CacheHitCount")) {
         var3 = "getCacheHitCount";
         var4 = null;
         var2 = new PropertyDescriptor("CacheHitCount", KodoDataCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CacheHitCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("CacheHitRatio")) {
         var3 = "getCacheHitRatio";
         var4 = null;
         var2 = new PropertyDescriptor("CacheHitRatio", KodoDataCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CacheHitRatio", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("CacheMissCount")) {
         var3 = "getCacheMissCount";
         var4 = null;
         var2 = new PropertyDescriptor("CacheMissCount", KodoDataCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CacheMissCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("Statistics")) {
         var3 = "getStatistics";
         var4 = null;
         var2 = new PropertyDescriptor("Statistics", KodoDataCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("Statistics", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("TotalCurrentEntries")) {
         var3 = "getTotalCurrentEntries";
         var4 = null;
         var2 = new PropertyDescriptor("TotalCurrentEntries", KodoDataCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalCurrentEntries", var2);
         var2.setValue("description", " ");
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
      Method var3 = KodoDataCacheRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = KodoDataCacheRuntimeMBean.class.getMethod("clear");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
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
