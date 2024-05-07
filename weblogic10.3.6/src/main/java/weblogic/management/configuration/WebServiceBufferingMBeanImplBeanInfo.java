package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class WebServiceBufferingMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebServiceBufferingMBean.class;

   public WebServiceBufferingMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebServiceBufferingMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebServiceBufferingMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Represents buffering configuration for web services.</p> <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://edocs.bea.com\" shape=\"rect\">http://edocs.bea.com</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebServiceBufferingMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("RetryCount")) {
         var3 = "getRetryCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRetryCount";
         }

         var2 = new PropertyDescriptor("RetryCount", WebServiceBufferingMBean.class, var3, var4);
         var1.put("RetryCount", var2);
         var2.setValue("description", "The number of times a buffered request or response can be retried before it is abandoned (and moved to any error queue defined for the buffer queue) ");
         setPropertyDescriptorDefault(var2, new Integer(3));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RetryDelay")) {
         var3 = "getRetryDelay";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRetryDelay";
         }

         var2 = new PropertyDescriptor("RetryDelay", WebServiceBufferingMBean.class, var3, var4);
         var1.put("RetryDelay", var2);
         var2.setValue("description", "The amount time between retries of a buffered request and response. Note, this value is only applicable when RetryCount > 0. String value in ‘Duration’ format. Defaults to ‘P0DT30S (30 seconds). ");
         setPropertyDescriptorDefault(var2, "P0DT30S");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WebServiceRequestBufferingQueue")) {
         var3 = "getWebServiceRequestBufferingQueue";
         var4 = null;
         var2 = new PropertyDescriptor("WebServiceRequestBufferingQueue", WebServiceBufferingMBean.class, var3, var4);
         var1.put("WebServiceRequestBufferingQueue", var2);
         var2.setValue("description", "Configuration for the request buffering queue ");
         var2.setValue("relationship", "containment");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WebServiceResponseBufferingQueue")) {
         var3 = "getWebServiceResponseBufferingQueue";
         var4 = null;
         var2 = new PropertyDescriptor("WebServiceResponseBufferingQueue", WebServiceBufferingMBean.class, var3, var4);
         var1.put("WebServiceResponseBufferingQueue", var2);
         var2.setValue("description", "Configuration for the response buffering queue ");
         var2.setValue("relationship", "containment");
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
