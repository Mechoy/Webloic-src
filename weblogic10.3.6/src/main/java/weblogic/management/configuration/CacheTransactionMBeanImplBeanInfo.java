package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class CacheTransactionMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = CacheTransactionMBean.class;

   public CacheTransactionMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CacheTransactionMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CacheTransactionMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String(" ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.CacheTransactionMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Concurrency")) {
         var3 = "getConcurrency";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConcurrency";
         }

         var2 = new PropertyDescriptor("Concurrency", CacheTransactionMBean.class, var3, var4);
         var1.put("Concurrency", var2);
         var2.setValue("description", "Setting this property to something other than none will make this cache transactional ");
         setPropertyDescriptorDefault(var2, "None");
         var2.setValue("legalValues", new Object[]{"Pessimistic", "Optimistic", "None"});
      }

      if (!var1.containsKey("IsolationLevel")) {
         var3 = "getIsolationLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIsolationLevel";
         }

         var2 = new PropertyDescriptor("IsolationLevel", CacheTransactionMBean.class, var3, var4);
         var1.put("IsolationLevel", var2);
         var2.setValue("description", " ");
         setPropertyDescriptorDefault(var2, "RepeatableRead");
         var2.setValue("legalValues", new Object[]{"ReadUncommitted", "ReadCommitted", "RepeatableRead"});
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
