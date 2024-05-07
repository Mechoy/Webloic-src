package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class WebServicePhysicalStoreMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebServicePhysicalStoreMBean.class;

   public WebServicePhysicalStoreMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebServicePhysicalStoreMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebServicePhysicalStoreMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Represents a physical store for web services. Used only for non-WLS containers (e.g. other app-server or standalone client).</p> <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://edocs.bea.com\" shape=\"rect\">http://edocs.bea.com</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebServicePhysicalStoreMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Location")) {
         var3 = "getLocation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLocation";
         }

         var2 = new PropertyDescriptor("Location", WebServicePhysicalStoreMBean.class, var3, var4);
         var1.put("Location", var2);
         var2.setValue("description", "For file stores, specifies the directory that will hold all files related to the store. The actual file names are controlled internally by the file store implementation. For other types of stores, this location may be a URL or URI, or other description string. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", WebServicePhysicalStoreMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "Get the name of this physical store. ");
      }

      if (!var1.containsKey("StoreType")) {
         var3 = "getStoreType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStoreType";
         }

         var2 = new PropertyDescriptor("StoreType", WebServicePhysicalStoreMBean.class, var3, var4);
         var1.put("StoreType", var2);
         var2.setValue("description", "Get the type of this physical store. ");
         setPropertyDescriptorDefault(var2, "FILE");
         var2.setValue("legalValues", new Object[]{"FILE", "JDBC"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SynchronousWritePolicy")) {
         var3 = "getSynchronousWritePolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSynchronousWritePolicy";
         }

         var2 = new PropertyDescriptor("SynchronousWritePolicy", WebServicePhysicalStoreMBean.class, var3, var4);
         var1.put("SynchronousWritePolicy", var2);
         var2.setValue("description", "Specifies the algorithm used when performing synchronous writes to the physical store. ");
         setPropertyDescriptorDefault(var2, "CACHE_FLUSH");
         var2.setValue("legalValues", new Object[]{"DISABLED", "CACHE_FLUSH", "DIRECT_WRITE"});
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
