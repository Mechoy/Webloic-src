package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class XMLEntityCacheMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = XMLEntityCacheMBean.class;

   public XMLEntityCacheMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public XMLEntityCacheMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = XMLEntityCacheMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Configure the behavior of JAXP (Java API for XML Parsing) in the server.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.XMLEntityCacheMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CacheDiskSize")) {
         var3 = "getCacheDiskSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCacheDiskSize";
         }

         var2 = new PropertyDescriptor("CacheDiskSize", XMLEntityCacheMBean.class, var3, var4);
         var1.put("CacheDiskSize", var2);
         var2.setValue("description", "<p>The disk size, in MB, of the persistent disk cache. The default value is 5 MB.</p>  <p>Return the disk size in MBytes of the cache.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CacheLocation")) {
         var3 = "getCacheLocation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCacheLocation";
         }

         var2 = new PropertyDescriptor("CacheLocation", XMLEntityCacheMBean.class, var3, var4);
         var1.put("CacheLocation", var2);
         var2.setValue("description", "<p>Provides the path name for the persistent cache files.</p> ");
         setPropertyDescriptorDefault(var2, "xmlcache");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CacheMemorySize")) {
         var3 = "getCacheMemorySize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCacheMemorySize";
         }

         var2 = new PropertyDescriptor("CacheMemorySize", XMLEntityCacheMBean.class, var3, var4);
         var1.put("CacheMemorySize", var2);
         var2.setValue("description", "<p>The memory size, in KB, of the cache. The default value is 500 KB.</p>  <p>Return the memory size in KBytes of the cache.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(500));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CacheTimeoutInterval")) {
         var3 = "getCacheTimeoutInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCacheTimeoutInterval";
         }

         var2 = new PropertyDescriptor("CacheTimeoutInterval", XMLEntityCacheMBean.class, var3, var4);
         var1.put("CacheTimeoutInterval", var2);
         var2.setValue("description", "<p>The default timeout interval, in seconds, for the cache. The default value is 120 seconds.</p>  <p>Return the default timeout interval in seconds for the cache.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(120));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("EntityCacheCurrentRuntime")) {
         var3 = "getEntityCacheCurrentRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("EntityCacheCurrentRuntime", XMLEntityCacheMBean.class, var3, var4);
         var1.put("EntityCacheCurrentRuntime", var2);
         var2.setValue("description", "<p>Provides the name of an MBean representing the runtime state of the cache. Returns null if the given server is not running</p> ");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("EntityCacheHistoricalRuntime")) {
         var3 = "getEntityCacheHistoricalRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("EntityCacheHistoricalRuntime", XMLEntityCacheMBean.class, var3, var4);
         var1.put("EntityCacheHistoricalRuntime", var2);
         var2.setValue("description", "<p>Provides the name of an MBean representing the runtime state of the cache. Returns null if the given server is not running</p> ");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("EntityCacheSessionRuntime")) {
         var3 = "getEntityCacheSessionRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("EntityCacheSessionRuntime", XMLEntityCacheMBean.class, var3, var4);
         var1.put("EntityCacheSessionRuntime", var2);
         var2.setValue("description", "<p>Provides the name of an MBean representing the runtime state of the cache. Returns null if the given server is not running</p> ");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxSize")) {
         var3 = "getMaxSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxSize";
         }

         var2 = new PropertyDescriptor("MaxSize", XMLEntityCacheMBean.class, var3, var4);
         var1.put("MaxSize", var2);
         var2.setValue("description", "<p>Provides the maximum number of entries that can be stored in the cache at any given time.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
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
