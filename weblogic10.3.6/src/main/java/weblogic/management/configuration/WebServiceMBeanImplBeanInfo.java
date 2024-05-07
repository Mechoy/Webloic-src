package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class WebServiceMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebServiceMBean.class;

   public WebServiceMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebServiceMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebServiceMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.2.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Encapsulates information about a Web Service configuration.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebServiceMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CallbackQueue")) {
         var3 = "getCallbackQueue";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCallbackQueue";
         }

         var2 = new PropertyDescriptor("CallbackQueue", WebServiceMBean.class, var3, var4);
         var1.put("CallbackQueue", var2);
         var2.setValue("description", "For use only with the JAX-RPC stack. For JAX-WS, use WebServiceBufferingMBean instead. <p> ");
         setPropertyDescriptorDefault(var2, "weblogic.wsee.DefaultCallbackQueue");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CallbackQueueMDBRunAsPrincipalName")) {
         var3 = "getCallbackQueueMDBRunAsPrincipalName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCallbackQueueMDBRunAsPrincipalName";
         }

         var2 = new PropertyDescriptor("CallbackQueueMDBRunAsPrincipalName", WebServiceMBean.class, var3, var4);
         var1.put("CallbackQueueMDBRunAsPrincipalName", var2);
         var2.setValue("description", "For use only with the JAX-RPC stack. For JAX-WS, use WebServiceBufferingMBean instead. <p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JmsConnectionFactory")) {
         var3 = "getJmsConnectionFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJmsConnectionFactory";
         }

         var2 = new PropertyDescriptor("JmsConnectionFactory", WebServiceMBean.class, var3, var4);
         var1.put("JmsConnectionFactory", var2);
         var2.setValue("description", "For use only with the JAX-RPC stack. For JAX-WS, use WebServiceBufferingMBean instead. <p> ");
         setPropertyDescriptorDefault(var2, "weblogic.jms.XAConnectionFactory");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessagingQueue")) {
         var3 = "getMessagingQueue";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagingQueue";
         }

         var2 = new PropertyDescriptor("MessagingQueue", WebServiceMBean.class, var3, var4);
         var1.put("MessagingQueue", var2);
         var2.setValue("description", "For use only with the JAX-RPC stack. For JAX-WS, use WebServiceBufferingMBean instead. <p> ");
         setPropertyDescriptorDefault(var2, "weblogic.wsee.DefaultQueue");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessagingQueueMDBRunAsPrincipalName")) {
         var3 = "getMessagingQueueMDBRunAsPrincipalName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagingQueueMDBRunAsPrincipalName";
         }

         var2 = new PropertyDescriptor("MessagingQueueMDBRunAsPrincipalName", WebServiceMBean.class, var3, var4);
         var1.put("MessagingQueueMDBRunAsPrincipalName", var2);
         var2.setValue("description", "For use only with the JAX-RPC stack. For JAX-WS, use WebServiceBufferingMBean instead. <p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WebServiceBuffering")) {
         var3 = "getWebServiceBuffering";
         var4 = null;
         var2 = new PropertyDescriptor("WebServiceBuffering", WebServiceMBean.class, var3, var4);
         var1.put("WebServiceBuffering", var2);
         var2.setValue("description", "Get buffering config for this server. <p> NOTE: Not used by the JAX-RPC stack. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WebServicePersistence")) {
         var3 = "getWebServicePersistence";
         var4 = null;
         var2 = new PropertyDescriptor("WebServicePersistence", WebServiceMBean.class, var3, var4);
         var1.put("WebServicePersistence", var2);
         var2.setValue("description", "Get persistence config for this server. <p> NOTE: Not used by the JAX-RPC stack. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WebServiceReliability")) {
         var3 = "getWebServiceReliability";
         var4 = null;
         var2 = new PropertyDescriptor("WebServiceReliability", WebServiceMBean.class, var3, var4);
         var1.put("WebServiceReliability", var2);
         var2.setValue("description", "Get reliability config for this server. <p> NOTE: Not used by the JAX-RPC stack. </p> ");
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
