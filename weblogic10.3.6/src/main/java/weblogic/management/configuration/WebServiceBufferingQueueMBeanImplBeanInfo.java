package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class WebServiceBufferingQueueMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebServiceBufferingQueueMBean.class;

   public WebServiceBufferingQueueMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebServiceBufferingQueueMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebServiceBufferingQueueMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Represents buffering queue configuration for web services (either for requests or responses).</p> <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://edocs.bea.com\" shape=\"rect\">http://edocs.bea.com</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebServiceBufferingQueueMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ConnectionFactoryJndiName")) {
         var3 = "getConnectionFactoryJndiName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionFactoryJndiName";
         }

         var2 = new PropertyDescriptor("ConnectionFactoryJndiName", WebServiceBufferingQueueMBean.class, var3, var4);
         var1.put("ConnectionFactoryJndiName", var2);
         var2.setValue("description", "The JNDI name of the connection factory to use when buffering messages onto this queue. Defaults to the 'default' JMS connection factory. ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", WebServiceBufferingQueueMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "Get the name of this buffering queue. This name is the JNDI name of the queue to be used for buffering. ");
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", WebServiceBufferingQueueMBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "A boolean flag indicating whether buffering is enabled (request buffering if this is the request queue, or response buffering if this is the response queue). Defaults to false. ");
         setPropertyDescriptorDefault(var2, false);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TransactionEnabled")) {
         var3 = "isTransactionEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTransactionEnabled";
         }

         var2 = new PropertyDescriptor("TransactionEnabled", WebServiceBufferingQueueMBean.class, var3, var4);
         var1.put("TransactionEnabled", var2);
         var2.setValue("description", "A boolean flag indicating whether transactions should be used when buffering a message onto or consuming a message off of this queue. Defaults to false. ");
         setPropertyDescriptorDefault(var2, false);
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
