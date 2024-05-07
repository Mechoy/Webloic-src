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
import weblogic.management.runtime.QueryCacheRuntimeMBean;

public class QueryCacheRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = QueryCacheRuntimeMBean.class;

   public QueryCacheRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public QueryCacheRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = QueryCacheRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.ejb.container.monitoring");
      String var3 = (new String("This interface contains accessor methods for all query cache runtime information collected for an EJB. The cache miss counts need some explaining. A query-cache miss can occur due to one of five reasons:  1. The query result was not found in the query-cache 2. The query result has timed out 3. A bean which satisfies the query wasnot found in the entity cache 4. A query with relationship-caching turned on did not find the related-beans query result 5. A query which loads multiple EJBs could not load one or more of them  To better aid tuning, there are separate counters provided for each of the last four of the above causes. The fifth counter is a total cache miss counter. This counter takes into account all five causes of a cache miss.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.QueryCacheRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("CacheAccessCount")) {
         var3 = "getCacheAccessCount";
         var4 = null;
         var2 = new PropertyDescriptor("CacheAccessCount", QueryCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CacheAccessCount", var2);
         var2.setValue("description", "<p>Provides a count of the number of accesses of the query cache for this EJB.</p>  <p>Returns the number of accesses of the query cache for this EJB.</p> ");
      }

      if (!var1.containsKey("CacheHitCount")) {
         var3 = "getCacheHitCount";
         var4 = null;
         var2 = new PropertyDescriptor("CacheHitCount", QueryCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CacheHitCount", var2);
         var2.setValue("description", "<p>Provides a count of the number of cache hits of the query cache for this EJB.</p>  <p>Returns the number of cache hits of the query cache for this EJB.</p> ");
      }

      if (!var1.containsKey("CacheMissByBeanEvictionCount")) {
         var3 = "getCacheMissByBeanEvictionCount";
         var4 = null;
         var2 = new PropertyDescriptor("CacheMissByBeanEvictionCount", QueryCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CacheMissByBeanEvictionCount", var2);
         var2.setValue("description", "<p>Provides a count of the number of cache misses of the query cache for this EJB because corresponding beans were not found in the entity cache.</p>  <p>Returns the number of times a cache miss occurred for this EJB because corresponding beans were not found in the entity cache.</p> ");
      }

      if (!var1.containsKey("CacheMissByDependentQueryMissCount")) {
         var3 = "getCacheMissByDependentQueryMissCount";
         var4 = null;
         var2 = new PropertyDescriptor("CacheMissByDependentQueryMissCount", QueryCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CacheMissByDependentQueryMissCount", var2);
         var2.setValue("description", "<p>Provides a count of the number of cache misses of the query cache for this EJB because a dependent query was not found in another EJB's query cache.</p>  <p>Returns the number of times a cache miss occurred for this EJB because a dependent query was not found in another EJB's query cache. ");
      }

      if (!var1.containsKey("CacheMissByRelatedQueryMissCount")) {
         var3 = "getCacheMissByRelatedQueryMissCount";
         var4 = null;
         var2 = new PropertyDescriptor("CacheMissByRelatedQueryMissCount", QueryCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CacheMissByRelatedQueryMissCount", var2);
         var2.setValue("description", "<p>Provides a count of the number of cache misses of the query cache for this EJB because a related query was not found in another EJB's query cache.</p>  <p>Returns the number of times a cache miss occurred for this EJB because a related query was not found in another EJB's query cache. ");
      }

      if (!var1.containsKey("CacheMissByTimeoutCount")) {
         var3 = "getCacheMissByTimeoutCount";
         var4 = null;
         var2 = new PropertyDescriptor("CacheMissByTimeoutCount", QueryCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("CacheMissByTimeoutCount", var2);
         var2.setValue("description", "<p>Provides a count of the number of cache misses of the query cache for this EJB due to query results timing out.</p>  <p>Returns the number of cache misses due to query result timeout for this EJB.</p> ");
      }

      if (!var1.containsKey("TotalCacheMissCount")) {
         var3 = "getTotalCacheMissCount";
         var4 = null;
         var2 = new PropertyDescriptor("TotalCacheMissCount", QueryCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalCacheMissCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of cache misses of the query cache for this EJB.</p>  <p>Returns the total number of cache misses of the query cache for this EJB.</p> ");
      }

      if (!var1.containsKey("TotalCachedQueriesCount")) {
         var3 = "getTotalCachedQueriesCount";
         var4 = null;
         var2 = new PropertyDescriptor("TotalCachedQueriesCount", QueryCacheRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalCachedQueriesCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of query results for this EJB currently in the query cache.</p>  <p>Returns the total number of query results for this EJB currently in the EJB cache.</p> ");
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
      Method var3 = QueryCacheRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
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
