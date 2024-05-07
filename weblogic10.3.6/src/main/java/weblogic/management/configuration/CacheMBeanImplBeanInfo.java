package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class CacheMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = CacheMBean.class;

   public CacheMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CacheMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CacheMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String(" ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.CacheMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AsyncListeners")) {
         var3 = "getAsyncListeners";
         var4 = null;
         var2 = new PropertyDescriptor("AsyncListeners", CacheMBean.class, var3, var4);
         var1.put("AsyncListeners", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("EvictionPolicy")) {
         var3 = "getEvictionPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEvictionPolicy";
         }

         var2 = new PropertyDescriptor("EvictionPolicy", CacheMBean.class, var3, var4);
         var1.put("EvictionPolicy", var2);
         var2.setValue("description", "The eviction policy to choose when the number of entries in cache hits the maximum ");
         setPropertyDescriptorDefault(var2, "LFU");
         var2.setValue("legalValues", new Object[]{"LRU", "NRU", "FIFO", "LFU"});
      }

      if (!var1.containsKey("Expiration")) {
         var3 = "getExpiration";
         var4 = null;
         var2 = new PropertyDescriptor("Expiration", CacheMBean.class, var3, var4);
         var1.put("Expiration", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("JNDIName")) {
         var3 = "getJNDIName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDIName";
         }

         var2 = new PropertyDescriptor("JNDIName", CacheMBean.class, var3, var4);
         var1.put("JNDIName", var2);
         var2.setValue("description", "The JNDI name that the cache should to be bound to ");
         var2.setValue("legalNull", Boolean.TRUE);
      }

      if (!var1.containsKey("Loader")) {
         var3 = "getLoader";
         var4 = null;
         var2 = new PropertyDescriptor("Loader", CacheMBean.class, var3, var4);
         var1.put("Loader", var2);
         var2.setValue("description", "The configuration parameters for self-loading caches ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("MaxCacheUnits")) {
         var3 = "getMaxCacheUnits";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxCacheUnits";
         }

         var2 = new PropertyDescriptor("MaxCacheUnits", CacheMBean.class, var3, var4);
         var1.put("MaxCacheUnits", var2);
         var2.setValue("description", "Maximum number of cache elements in memory after which eviction/paging occurs. This value is defined as an Integer. ");
         setPropertyDescriptorDefault(var2, new Integer(64));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Store")) {
         var3 = "getStore";
         var4 = null;
         var2 = new PropertyDescriptor("Store", CacheMBean.class, var3, var4);
         var1.put("Store", var2);
         var2.setValue("description", "The configuraiton parameters for self-backing caches ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Transactional")) {
         var3 = "getTransactional";
         var4 = null;
         var2 = new PropertyDescriptor("Transactional", CacheMBean.class, var3, var4);
         var1.put("Transactional", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("WorkManager")) {
         var3 = "getWorkManager";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWorkManager";
         }

         var2 = new PropertyDescriptor("WorkManager", CacheMBean.class, var3, var4);
         var1.put("WorkManager", var2);
         var2.setValue("description", "Set the default work manager to use for all asynchronous caching tasks. If none of the specific work managers are specified, this work manager is used. This work manager may be overriden by other work managers configured for specific tasks like store backup, listeners etc ");
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
