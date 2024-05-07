package weblogic.ejb.container.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.EJBCacheRuntimeMBean;

public class EJBCacheRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = EJBCacheRuntimeMBean.class;

   public EJBCacheRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public EJBCacheRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = EJBCacheRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.ejb.container.monitoring");
      String var3 = (new String("This interface contains accessor methods for all cache runtime information collected for an EJB.  Note that the sum of the cacheHitCount and cacheMissCount may not add up to the cacheAccessCount in a running server because these metrics are retrieved using multiple calls and the counts could change between the calls.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.EJBCacheRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActivationCount")) {
         var3 = "getActivationCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActivationCount", EJBCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("ActivationCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of beans from this EJB Home that have been activated.</p> ");
      }

      if (!var1.containsKey("CacheAccessCount")) {
         var3 = "getCacheAccessCount";
         var4 = null;
         var2 = new PropertyDescriptor("CacheAccessCount", EJBCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CacheAccessCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of attempts to access a bean from the cache.</p>  <p> The sum of the Cache Hit Count and Cache Miss Count may not add up to the cacheAccessCount in a running server because these metrics are retrieved using multiple calls and the counts could change between the calls.</p>* ");
      }

      if (!var1.containsKey("CacheHitCount")) {
         var3 = "getCacheHitCount";
         var4 = null;
         var2 = new PropertyDescriptor("CacheHitCount", EJBCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CacheHitCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of times an attempt to access a bean from the cache succeeded.</p>  <p> The sum of the Cache Hit Count and Cache Miss Count may not add up to the cacheAccessCount in a running server because these metrics are retrieved using multiple calls and the counts could change between the calls.</p> ");
         var2.setValue("deprecated", "28-Aug-2002.  The cache hit count can be calculated by  subtracting the cache miss count from the cache access count. ");
      }

      if (!var1.containsKey("CacheMissCount")) {
         var3 = "getCacheMissCount";
         var4 = null;
         var2 = new PropertyDescriptor("CacheMissCount", EJBCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CacheMissCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of times an attempt to access a bean from the cache failed.</p>  <p> The sum of the Cache Hit Count and Cache Miss Count may not add up to the cacheAccessCount in a running server because these metrics are retrieved using multiple calls and the counts could change between the calls.</p> ");
      }

      if (!var1.containsKey("CachedBeansCurrentCount")) {
         var3 = "getCachedBeansCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("CachedBeansCurrentCount", EJBCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CachedBeansCurrentCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of beans from this EJB Home currently in the EJB cache.</p> ");
      }

      if (!var1.containsKey("PassivationCount")) {
         var3 = "getPassivationCount";
         var4 = null;
         var2 = new PropertyDescriptor("PassivationCount", EJBCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("PassivationCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of beans from this EJB Home that have been passivated.</p> ");
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
      Method var3 = EJBCacheRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = EJBCacheRuntimeMBean.class.getMethod("reInitializeCacheAndPools");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Instructs the cache to initialize itself all of its associated pools to their configured initial sizes.</p> ");
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
