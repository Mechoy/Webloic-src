package weblogic.xml.util.cache.entitycache;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.EntityCacheCumulativeRuntimeMBean;

public class EntityCacheCumulativeStatsBeanInfo extends EntityCacheStatsBeanInfo {
   public static Class INTERFACE_CLASS = EntityCacheCumulativeRuntimeMBean.class;

   public EntityCacheCumulativeStatsBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public EntityCacheCumulativeStatsBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = EntityCacheCumulativeStats.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.xml.util.cache.entitycache");
      String var3 = (new String("This class is used for monitoring an XML Cache.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.EntityCacheCumulativeRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AvgEntrySizeDiskPurged")) {
         var3 = "getAvgEntrySizeDiskPurged";
         var4 = null;
         var2 = new PropertyDescriptor("AvgEntrySizeDiskPurged", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("AvgEntrySizeDiskPurged", var2);
         var2.setValue("description", "<p>Provides the cumulative average size of entries that have been purged from the disk cache.</p> ");
      }

      if (!var1.containsKey("AvgEntrySizeMemoryPurged")) {
         var3 = "getAvgEntrySizeMemoryPurged";
         var4 = null;
         var2 = new PropertyDescriptor("AvgEntrySizeMemoryPurged", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("AvgEntrySizeMemoryPurged", var2);
         var2.setValue("description", "<p>Provides the average size of the all the entries that have been purged from the memory.</p> ");
      }

      if (!var1.containsKey("AvgPerEntryDiskSize")) {
         var3 = "getAvgPerEntryDiskSize";
         var4 = null;
         var2 = new PropertyDescriptor("AvgPerEntryDiskSize", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("AvgPerEntryDiskSize", var2);
         var2.setValue("description", "<p>Provides the current average size of the entries in the entity disk cache.</p>  <p>Returns the current average size of the entries in the entity disk cache.</p> ");
      }

      if (!var1.containsKey("AvgPerEntryMemorySize")) {
         var3 = "getAvgPerEntryMemorySize";
         var4 = null;
         var2 = new PropertyDescriptor("AvgPerEntryMemorySize", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("AvgPerEntryMemorySize", var2);
         var2.setValue("description", "<p>Provides the current average size of the entries in the entity memory cache.</p>  <p>Returns the current average size of the entries in the entity memory cache.</p> ");
      }

      if (!var1.containsKey("AvgPercentPersistent")) {
         var3 = "getAvgPercentPersistent";
         var4 = null;
         var2 = new PropertyDescriptor("AvgPercentPersistent", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("AvgPercentPersistent", var2);
         var2.setValue("description", "<p>Provides the current average percentage of entries in the entity cache that have been persisted to the disk cache.</p>  <p>Returns current average percentage of entries in the entity cache that have been persisted to the disk cache.</p> ");
      }

      if (!var1.containsKey("AvgPercentTransient")) {
         var3 = "getAvgPercentTransient";
         var4 = null;
         var2 = new PropertyDescriptor("AvgPercentTransient", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("AvgPercentTransient", var2);
         var2.setValue("description", "<p>Provides the current average percentage of entries in the entity cache that are transient, or have not been persisted.</p>  <p>Returns current average percentage of entries in the entity cache that are transient, or have not been persisted.</p> ");
      }

      if (!var1.containsKey("AvgTimeout")) {
         var3 = "getAvgTimeout";
         var4 = null;
         var2 = new PropertyDescriptor("AvgTimeout", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("AvgTimeout", var2);
         var2.setValue("description", "<p>Provides the average amount of time that the entity cache has timed out when trying to retrieve an entity.</p>  <p>Returns the average amount of time that the entity cache has timed out when trying to retrieve an entity.</p> ");
      }

      if (!var1.containsKey("DiskPurgesPerHour")) {
         var3 = "getDiskPurgesPerHour";
         var4 = null;
         var2 = new PropertyDescriptor("DiskPurgesPerHour", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("DiskPurgesPerHour", var2);
         var2.setValue("description", "<p>Provides cumulative average number of purges from the disk cache per hour.</p> ");
      }

      if (!var1.containsKey("MaxEntryMemorySize")) {
         var3 = "getMaxEntryMemorySize";
         var4 = null;
         var2 = new PropertyDescriptor("MaxEntryMemorySize", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxEntryMemorySize", var2);
         var2.setValue("description", "<p>Provides the current maximum size of the entries in the entity memory cache.</p>  <p>Returns the current maximum size of the entries in the entity memory cache.</p> ");
      }

      if (!var1.containsKey("MaxEntryTimeout")) {
         var3 = "getMaxEntryTimeout";
         var4 = null;
         var2 = new PropertyDescriptor("MaxEntryTimeout", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxEntryTimeout", var2);
         var2.setValue("description", "<p>Provides the largest timeout value for any current entry in the entity cache.</p>  <p>Returns the largest timeout value for any current entry in the entity cache.</p> ");
      }

      if (!var1.containsKey("MemoryPurgesPerHour")) {
         var3 = "getMemoryPurgesPerHour";
         var4 = null;
         var2 = new PropertyDescriptor("MemoryPurgesPerHour", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("MemoryPurgesPerHour", var2);
         var2.setValue("description", "<p>Provides the cumulative average number of entries that have been purged from the entity cache.</p> ");
      }

      if (!var1.containsKey("MinEntryMemorySize")) {
         var3 = "getMinEntryMemorySize";
         var4 = null;
         var2 = new PropertyDescriptor("MinEntryMemorySize", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("MinEntryMemorySize", var2);
         var2.setValue("description", "<p>Provides the current minimum size of the entries in the entity memory cache.</p>  <p>Returns the current minimum size of the entries in the entity memory cache.</p> ");
      }

      if (!var1.containsKey("MinEntryTimeout")) {
         var3 = "getMinEntryTimeout";
         var4 = null;
         var2 = new PropertyDescriptor("MinEntryTimeout", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("MinEntryTimeout", var2);
         var2.setValue("description", "<p>Provides the smallest timeout value for any current entry in the entity cache.</p>  <p>Returns the smallest timeout value for any current entry in the entity cache.</p> ");
      }

      if (!var1.containsKey("MostRecentDiskPurge")) {
         var3 = "getMostRecentDiskPurge";
         var4 = null;
         var2 = new PropertyDescriptor("MostRecentDiskPurge", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("MostRecentDiskPurge", var2);
         var2.setValue("description", "<p>Provides the date of the most recent purge from the disk cache.</p> ");
      }

      if (!var1.containsKey("MostRecentMemoryPurge")) {
         var3 = "getMostRecentMemoryPurge";
         var4 = null;
         var2 = new PropertyDescriptor("MostRecentMemoryPurge", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("MostRecentMemoryPurge", var2);
         var2.setValue("description", "<p>Provides the date of the most recent purge of the entity cache.</p> ");
      }

      if (!var1.containsKey("PercentRejected")) {
         var3 = "getPercentRejected";
         var4 = null;
         var2 = new PropertyDescriptor("PercentRejected", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("PercentRejected", var2);
         var2.setValue("description", "<p>Provides the cumulative percent of the potential entries to the entity cache that have been rejected.</p> ");
      }

      if (!var1.containsKey("TotalCurrentEntries")) {
         var3 = "getTotalCurrentEntries";
         var4 = null;
         var2 = new PropertyDescriptor("TotalCurrentEntries", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalCurrentEntries", var2);
         var2.setValue("description", "<p>Provides a count of the total current number of entries in the entity cache.</p>  <p>Returns the total current number of entries in the entity cache.</p> ");
      }

      if (!var1.containsKey("TotalItemsDiskPurged")) {
         var3 = "getTotalItemsDiskPurged";
         var4 = null;
         var2 = new PropertyDescriptor("TotalItemsDiskPurged", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalItemsDiskPurged", var2);
         var2.setValue("description", "<p>Provides the total number of items that have been purged from the disk cache.</p> ");
      }

      if (!var1.containsKey("TotalItemsMemoryPurged")) {
         var3 = "getTotalItemsMemoryPurged";
         var4 = null;
         var2 = new PropertyDescriptor("TotalItemsMemoryPurged", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalItemsMemoryPurged", var2);
         var2.setValue("description", "<p>Provides the cumulative number of items that have been purged from the entity cache.</p> ");
      }

      if (!var1.containsKey("TotalNumberDiskPurges")) {
         var3 = "getTotalNumberDiskPurges";
         var4 = null;
         var2 = new PropertyDescriptor("TotalNumberDiskPurges", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalNumberDiskPurges", var2);
         var2.setValue("description", "<p>Provides a count of the total number of entries that have been purged from the disk cache.</p> ");
      }

      if (!var1.containsKey("TotalNumberMemoryPurges")) {
         var3 = "getTotalNumberMemoryPurges";
         var4 = null;
         var2 = new PropertyDescriptor("TotalNumberMemoryPurges", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalNumberMemoryPurges", var2);
         var2.setValue("description", "<p>Provides the cumulative number of entries that have been purged from the entity cache.</p> ");
      }

      if (!var1.containsKey("TotalNumberOfRejections")) {
         var3 = "getTotalNumberOfRejections";
         var4 = null;
         var2 = new PropertyDescriptor("TotalNumberOfRejections", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalNumberOfRejections", var2);
         var2.setValue("description", "<p>Provides the cumulative total number of rejections of entries from the entity cache for the current session.</p> ");
      }

      if (!var1.containsKey("TotalNumberOfRenewals")) {
         var3 = "getTotalNumberOfRenewals";
         var4 = null;
         var2 = new PropertyDescriptor("TotalNumberOfRenewals", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalNumberOfRenewals", var2);
         var2.setValue("description", "<p>Provides a count of the cumulative number of entries that have been refreshed in the entity cache.</p> ");
      }

      if (!var1.containsKey("TotalPersistentCurrentEntries")) {
         var3 = "getTotalPersistentCurrentEntries";
         var4 = null;
         var2 = new PropertyDescriptor("TotalPersistentCurrentEntries", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalPersistentCurrentEntries", var2);
         var2.setValue("description", "<p>Provides a count of the total current number of entries in the cache that have been persisted to disk.</p>  <p>Returns the total current number of entries in the cache that have been persisted to disk.</p> ");
      }

      if (!var1.containsKey("TotalSizeOfRejections")) {
         var3 = "getTotalSizeOfRejections";
         var4 = null;
         var2 = new PropertyDescriptor("TotalSizeOfRejections", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalSizeOfRejections", var2);
         var2.setValue("description", "<p>Provides the cumulative total size of the rejections from the entity cache.</p> ");
      }

      if (!var1.containsKey("TotalTransientCurrentEntries")) {
         var3 = "getTotalTransientCurrentEntries";
         var4 = null;
         var2 = new PropertyDescriptor("TotalTransientCurrentEntries", EntityCacheCumulativeRuntimeMBean.class, var3, (String)var4);
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
      Method var3 = EntityCacheCumulativeRuntimeMBean.class.getMethod("preDeregister");
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
