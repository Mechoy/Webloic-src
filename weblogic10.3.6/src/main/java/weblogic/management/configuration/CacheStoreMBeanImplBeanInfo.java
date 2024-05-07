package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class CacheStoreMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = CacheStoreMBean.class;

   public CacheStoreMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CacheStoreMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CacheStoreMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String(" ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.CacheStoreMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("BufferMaxSize")) {
         var3 = "getBufferMaxSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBufferMaxSize";
         }

         var2 = new PropertyDescriptor("BufferMaxSize", CacheStoreMBean.class, var3, var4);
         var1.put("BufferMaxSize", var2);
         var2.setValue("description", "Sets the upper limit for the store buffer that's used to write out updates to the store. A value of 0 indicates no limit ");
         setPropertyDescriptorDefault(var2, new Integer(100));
         var2.setValue("legalMin", new Integer(1));
      }

      if (!var1.containsKey("BufferWriteAttempts")) {
         var3 = "getBufferWriteAttempts";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBufferWriteAttempts";
         }

         var2 = new PropertyDescriptor("BufferWriteAttempts", CacheStoreMBean.class, var3, var4);
         var1.put("BufferWriteAttempts", var2);
         var2.setValue("description", "Sets the number of attempts that the user thread will make to write to the store buffer. ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BufferWriteTimeout")) {
         var3 = "getBufferWriteTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBufferWriteTimeout";
         }

         var2 = new PropertyDescriptor("BufferWriteTimeout", CacheStoreMBean.class, var3, var4);
         var1.put("BufferWriteTimeout", var2);
         var2.setValue("description", "Sets the time in milliseconds that the user thread will wait before aborting an attempt to write to the buffer. The attempt to write to the store buffer fails only in case the buffer is full. After the timeout, futher attempts may be made to write to the buffer based on the value of StoreBufferWriteAttempts ");
         setPropertyDescriptorDefault(var2, new Long(100L));
         var2.setValue("legalMin", new Long(1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomStore")) {
         var3 = "getCustomStore";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomStore";
         }

         var2 = new PropertyDescriptor("CustomStore", CacheStoreMBean.class, var3, var4);
         var1.put("CustomStore", var2);
         var2.setValue("description", "The cache store to be used for store backed caches ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("StoreBatchSize")) {
         var3 = "getStoreBatchSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStoreBatchSize";
         }

         var2 = new PropertyDescriptor("StoreBatchSize", CacheStoreMBean.class, var3, var4);
         var1.put("StoreBatchSize", var2);
         var2.setValue("description", "Sets the number of user updates that are picked up from the store buffer to write back to the backing store ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WorkManager")) {
         var3 = "getWorkManager";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWorkManager";
         }

         var2 = new PropertyDescriptor("WorkManager", CacheStoreMBean.class, var3, var4);
         var1.put("WorkManager", var2);
         var2.setValue("description", "Sets the work manager that schedules the thread that writes to the backing store asynchronously ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WritePolicy")) {
         var3 = "getWritePolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWritePolicy";
         }

         var2 = new PropertyDescriptor("WritePolicy", CacheStoreMBean.class, var3, var4);
         var1.put("WritePolicy", var2);
         var2.setValue("description", " ");
         var2.setValue("legalValues", new Object[]{"None", "WriteThrough", "WriteBehind"});
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
