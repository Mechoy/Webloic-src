package weblogic.xml.util.cache.entitycache;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.EntityCacheCurrentStateRuntimeMBean;

public class EntityCacheCurrentStatsBeanInfo extends EntityCacheStatsBeanInfo {
   public static Class INTERFACE_CLASS = EntityCacheCurrentStateRuntimeMBean.class;

   public EntityCacheCurrentStatsBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public EntityCacheCurrentStatsBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = EntityCacheCurrentStats.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.xml.util.cache.entitycache");
      String var3 = (new String("<p>This class is used for monitoring the size and usage of an XML Cache.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.EntityCacheCurrentStateRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AvgPerEntryDiskSize")) {
         var3 = "getAvgPerEntryDiskSize";
         var4 = null;
         var2 = new PropertyDescriptor("AvgPerEntryDiskSize", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("AvgPerEntryDiskSize", var2);
         var2.setValue("description", "<p>Provides the current average size of the entries in the entity disk cache.</p>  <p>Returns the current average size of the entries in the entity disk cache.</p> ");
      }

      if (!var1.containsKey("AvgPerEntryMemorySize")) {
         var3 = "getAvgPerEntryMemorySize";
         var4 = null;
         var2 = new PropertyDescriptor("AvgPerEntryMemorySize", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("AvgPerEntryMemorySize", var2);
         var2.setValue("description", "<p>Provides the current average size of the entries in the entity memory cache.</p>  <p>Returns the current average size of the entries in the entity memory cache.</p> ");
      }

      if (!var1.containsKey("AvgPercentPersistent")) {
         var3 = "getAvgPercentPersistent";
         var4 = null;
         var2 = new PropertyDescriptor("AvgPercentPersistent", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("AvgPercentPersistent", var2);
         var2.setValue("description", "<p>Provides the current average percentage of entries in the entity cache that have been persisted to the disk cache.</p>  <p>Returns current average percentage of entries in the entity cache that have been persisted to the disk cache.</p> ");
      }

      if (!var1.containsKey("AvgPercentTransient")) {
         var3 = "getAvgPercentTransient";
         var4 = null;
         var2 = new PropertyDescriptor("AvgPercentTransient", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("AvgPercentTransient", var2);
         var2.setValue("description", "<p>Provides the current average percentage of entries in the entity cache that are transient, or have not been persisted.</p>  <p>Returns current average percentage of entries in the entity cache that are transient, or have not been persisted.</p> ");
      }

      if (!var1.containsKey("AvgTimeout")) {
         var3 = "getAvgTimeout";
         var4 = null;
         var2 = new PropertyDescriptor("AvgTimeout", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("AvgTimeout", var2);
         var2.setValue("description", "<p>Provides the average amount of time that the entity cache has timed out when trying to retrieve an entity.</p>  <p>Returns the average amount of time that the entity cache has timed out when trying to retrieve an entity.</p> ");
      }

      if (!var1.containsKey("DiskUsage")) {
         var3 = "getDiskUsage";
         var4 = null;
         var2 = new PropertyDescriptor("DiskUsage", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("DiskUsage", var2);
         var2.setValue("description", "<p>Provides the current size of the entity disk cache.</p>  <p>Returns the current size of the entity disk cache.</p> ");
      }

      if (!var1.containsKey("MaxEntryMemorySize")) {
         var3 = "getMaxEntryMemorySize";
         var4 = null;
         var2 = new PropertyDescriptor("MaxEntryMemorySize", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxEntryMemorySize", var2);
         var2.setValue("description", "<p>Provides the current maximum size of the entries in the entity memory cache.</p>  <p>Returns the current maximum size of the entries in the entity memory cache.</p> ");
      }

      if (!var1.containsKey("MaxEntryTimeout")) {
         var3 = "getMaxEntryTimeout";
         var4 = null;
         var2 = new PropertyDescriptor("MaxEntryTimeout", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxEntryTimeout", var2);
         var2.setValue("description", "<p>Provides the largest timeout value for any current entry in the entity cache.</p>  <p>Returns the largest timeout value for any current entry in the entity cache.</p> ");
      }

      if (!var1.containsKey("MemoryUsage")) {
         var3 = "getMemoryUsage";
         var4 = null;
         var2 = new PropertyDescriptor("MemoryUsage", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("MemoryUsage", var2);
         var2.setValue("description", "<p>Provides the current size of the entity memory cache.</p>  <p>Returns current size of the entity memory cache.</p> ");
      }

      if (!var1.containsKey("MinEntryMemorySize")) {
         var3 = "getMinEntryMemorySize";
         var4 = null;
         var2 = new PropertyDescriptor("MinEntryMemorySize", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("MinEntryMemorySize", var2);
         var2.setValue("description", "<p>Provides the current minimum size of the entries in the entity memory cache.</p>  <p>Returns the current minimum size of the entries in the entity memory cache.</p> ");
      }

      if (!var1.containsKey("MinEntryTimeout")) {
         var3 = "getMinEntryTimeout";
         var4 = null;
         var2 = new PropertyDescriptor("MinEntryTimeout", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("MinEntryTimeout", var2);
         var2.setValue("description", "<p>Provides the smallest timeout value for any current entry in the entity cache.</p>  <p>Returns the smallest timeout value for any current entry in the entity cache.</p> ");
      }

      if (!var1.containsKey("TotalCurrentEntries")) {
         var3 = "getTotalCurrentEntries";
         var4 = null;
         var2 = new PropertyDescriptor("TotalCurrentEntries", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalCurrentEntries", var2);
         var2.setValue("description", "<p>Provides a count of the total current number of entries in the entity cache.</p>  <p>Returns the total current number of entries in the entity cache.</p> ");
      }

      if (!var1.containsKey("TotalPersistentCurrentEntries")) {
         var3 = "getTotalPersistentCurrentEntries";
         var4 = null;
         var2 = new PropertyDescriptor("TotalPersistentCurrentEntries", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalPersistentCurrentEntries", var2);
         var2.setValue("description", "<p>Provides a count of the total current number of entries in the cache that have been persisted to disk.</p>  <p>Returns the total current number of entries in the cache that have been persisted to disk.</p> ");
      }

      if (!var1.containsKey("TotalTransientCurrentEntries")) {
         var3 = "getTotalTransientCurrentEntries";
         var4 = null;
         var2 = new PropertyDescriptor("TotalTransientCurrentEntries", EntityCacheCurrentStateRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalTransientCurrentEntries", var2);
         var2.setValue("description", "<p>Provides a count of the total current number of transient (not yet persisted to disk) entries in the entity cache.</p>  <p>Returns the total current number of transient entries in the entity cache.</p> ");
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
      Method var3 = EntityCacheCurrentStateRuntimeMBean.class.getMethod("preDeregister");
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
