package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WebServiceLogicalStoreMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebServiceLogicalStoreMBean.class;

   public WebServiceLogicalStoreMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebServiceLogicalStoreMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebServiceLogicalStoreMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Represents a logical store for web services.</p> <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://edocs.bea.com\" shape=\"rect\">http://edocs.bea.com</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebServiceLogicalStoreMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CleanerInterval")) {
         var3 = "getCleanerInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCleanerInterval";
         }

         var2 = new PropertyDescriptor("CleanerInterval", WebServiceLogicalStoreMBean.class, var3, var4);
         var1.put("CleanerInterval", var2);
         var2.setValue("description", "Get the interval at which the persistent store will be cleaned ");
         setPropertyDescriptorDefault(var2, "PT10M");
      }

      if (!var1.containsKey("DefaultMaximumObjectLifetime")) {
         var3 = "getDefaultMaximumObjectLifetime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultMaximumObjectLifetime";
         }

         var2 = new PropertyDescriptor("DefaultMaximumObjectLifetime", WebServiceLogicalStoreMBean.class, var3, var4);
         var1.put("DefaultMaximumObjectLifetime", var2);
         var2.setValue("description", "Get the default max time an object can remain in the store. This can be overridden on individual objects placed in the store (internally, but not via this API). ");
         setPropertyDescriptorDefault(var2, "P1D");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", WebServiceLogicalStoreMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "Get the name of this logical store. ");
      }

      if (!var1.containsKey("PersistenceStrategy")) {
         var3 = "getPersistenceStrategy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPersistenceStrategy";
         }

         var2 = new PropertyDescriptor("PersistenceStrategy", WebServiceLogicalStoreMBean.class, var3, var4);
         var1.put("PersistenceStrategy", var2);
         var2.setValue("description", "Get the persistence strategy in use by this logical store. Any physical store configured for use with this logical store should support this strategy. ");
         setPropertyDescriptorDefault(var2, "LOCAL_ACCESS_ONLY");
         var2.setValue("legalValues", new Object[]{"LOCAL_ACCESS_ONLY", "IN_MEMORY"});
      }

      String[] var5;
      if (!var1.containsKey("PhysicalStoreName")) {
         var3 = "getPhysicalStoreName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPhysicalStoreName";
         }

         var2 = new PropertyDescriptor("PhysicalStoreName", WebServiceLogicalStoreMBean.class, var3, var4);
         var1.put("PhysicalStoreName", var2);
         var2.setValue("description", "Get the name of the physical store to be used by this logical store. This property is recommended for use only when running off server or in other cases where a buffering queue JNDI name cannot be set via setBufferingQueueJndiName. If a buffering queue JNDI name is set to a non-null/non-empty value, this property is ignored. Defaults to \"\" to indicate the default WLS file store should be used. <p> This property is ignored if persistence strategy is IN_MEMORY. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setRequestBufferingQueueJndiName(String)")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RequestBufferingQueueJndiName")) {
         var3 = "getRequestBufferingQueueJndiName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRequestBufferingQueueJndiName";
         }

         var2 = new PropertyDescriptor("RequestBufferingQueueJndiName", WebServiceLogicalStoreMBean.class, var3, var4);
         var1.put("RequestBufferingQueueJndiName", var2);
         var2.setValue("description", "Get the JNDI name of the buffering queue that web services should use. (Both for buffering and to find the physical store for this logical store). Defaults to \"\" to indicate the PhysicalStoreName property should be used. <p> This property is ignored if persistence strategy is IN_MEMORY. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setRequestBufferingQueueJndiName(String)")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "");
      }

      if (!var1.containsKey("ResponseBufferingQueueJndiName")) {
         var3 = "getResponseBufferingQueueJndiName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setResponseBufferingQueueJndiName";
         }

         var2 = new PropertyDescriptor("ResponseBufferingQueueJndiName", WebServiceLogicalStoreMBean.class, var3, var4);
         var1.put("ResponseBufferingQueueJndiName", var2);
         var2.setValue("description", "Get the JNDI name of the response buffering queue that web services should use. If this is null, the request buffering queue is used. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getRequestBufferingQueueJndiName()")};
         var2.setValue("see", var5);
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
