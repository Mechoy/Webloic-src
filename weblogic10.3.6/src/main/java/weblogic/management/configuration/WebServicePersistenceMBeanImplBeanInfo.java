package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WebServicePersistenceMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebServicePersistenceMBean.class;

   public WebServicePersistenceMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebServicePersistenceMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebServicePersistenceMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Represents persistence configuration for web services.</p> <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://edocs.bea.com\" shape=\"rect\">http://edocs.bea.com</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebServicePersistenceMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("DefaultLogicalStoreName")) {
         var3 = "getDefaultLogicalStoreName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultLogicalStoreName";
         }

         var2 = new PropertyDescriptor("DefaultLogicalStoreName", WebServicePersistenceMBean.class, var3, var4);
         var1.put("DefaultLogicalStoreName", var2);
         var2.setValue("description", "Get the name of the logical store to use, by default, for all web services persistent state in this server. ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#getWebServiceLogicalStores()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "WseeStore");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WebServiceLogicalStores")) {
         var3 = "getWebServiceLogicalStores";
         var4 = null;
         var2 = new PropertyDescriptor("WebServiceLogicalStores", WebServicePersistenceMBean.class, var3, var4);
         var1.put("WebServiceLogicalStores", var2);
         var2.setValue("description", "Get an array of all defined logical stores for this VM (non-WLS). ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createWebServiceLogicalStore");
         var2.setValue("destroyer", "destroyWebServiceLogicalStore");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WebServicePhysicalStores")) {
         var3 = "getWebServicePhysicalStores";
         var4 = null;
         var2 = new PropertyDescriptor("WebServicePhysicalStores", WebServicePersistenceMBean.class, var3, var4);
         var1.put("WebServicePhysicalStores", var2);
         var2.setValue("description", "Get an array of all defined physical stores for this VM (non-WLS). ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWebServicePhysicalStore");
         var2.setValue("creator", "createWebServicePhysicalStore");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WebServicePersistenceMBean.class.getMethod("createWebServiceLogicalStore", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "Name of the new store. Logical store names must start with a letter, and can contain only letters, numbers, spaces and underscores. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Create a new logical store with the given name. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WebServiceLogicalStores");
      }

      var3 = WebServicePersistenceMBean.class.getMethod("destroyWebServiceLogicalStore", WebServiceLogicalStoreMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("store", "The store to destroy/remove. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Destroy/remove a logical store previously defined by a call to createLogicalStore (or retrieved via a call to getLogicalStores). ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WebServiceLogicalStores");
      }

      var3 = WebServicePersistenceMBean.class.getMethod("createWebServicePhysicalStore", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "Name of the new store. Physical store names must start with a letter, and can contain only letters, numbers, spaces and underscores. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Create a new physical store with the given name. Used only for standalone VM (non-WLS). ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WebServicePhysicalStores");
      }

      var3 = WebServicePersistenceMBean.class.getMethod("destroyWebServicePhysicalStore", WebServicePhysicalStoreMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("store", "The store to destroy/remove. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Destroy/remove a physical store previously defined by a call to createPhysicalStore (or retrieved via a call to getPhysicalStores). ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WebServicePhysicalStores");
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WebServicePersistenceMBean.class.getMethod("lookupWebServiceLogicalStore", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Lookup a logical store by name ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WebServiceLogicalStores");
      }

      var3 = WebServicePersistenceMBean.class.getMethod("lookupWebServicePhysicalStore", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Get a named physical store for this VM (non-WLS). ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WebServicePhysicalStores");
      }

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
