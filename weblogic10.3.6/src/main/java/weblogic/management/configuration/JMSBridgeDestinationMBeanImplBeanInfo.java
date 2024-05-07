package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JMSBridgeDestinationMBeanImplBeanInfo extends BridgeDestinationCommonMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSBridgeDestinationMBean.class;

   public JMSBridgeDestinationMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JMSBridgeDestinationMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JMSBridgeDestinationMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This MBean represents a messaging bridge destination for a JMS messaging product. Each messaging bridge consists of two destinations that are being bridged:</p>  <ul> <li> <p>Source: The message producing destination. A bridge instance consumes messages from the source destination.</p> </li>  <li> <p>Target: The destination where a bridge instance forwards messages produced by the source destination.</p> </li> </ul>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JMSBridgeDestinationMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ConnectionFactoryJNDIName")) {
         var3 = "getConnectionFactoryJNDIName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionFactoryJNDIName";
         }

         var2 = new PropertyDescriptor("ConnectionFactoryJNDIName", JMSBridgeDestinationMBean.class, var3, var4);
         var1.put("ConnectionFactoryJNDIName", var2);
         var2.setValue("description", "<p>The connection factory's JNDI name for this JMS bridge destination.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ConnectionURL")) {
         var3 = "getConnectionURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionURL";
         }

         var2 = new PropertyDescriptor("ConnectionURL", JMSBridgeDestinationMBean.class, var3, var4);
         var1.put("ConnectionURL", var2);
         var2.setValue("description", "<p>The connection URL for this JMS bridge destination.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DestinationJNDIName")) {
         var3 = "getDestinationJNDIName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDestinationJNDIName";
         }

         var2 = new PropertyDescriptor("DestinationJNDIName", JMSBridgeDestinationMBean.class, var3, var4);
         var1.put("DestinationJNDIName", var2);
         var2.setValue("description", "<p>The destination JNDI name for this JMS bridge destination.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DestinationType")) {
         var3 = "getDestinationType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDestinationType";
         }

         var2 = new PropertyDescriptor("DestinationType", JMSBridgeDestinationMBean.class, var3, var4);
         var1.put("DestinationType", var2);
         var2.setValue("description", "<p>The destination type (queue or topic) for this JMS bridge destination.</p> ");
         setPropertyDescriptorDefault(var2, "Queue");
         var2.setValue("legalValues", new Object[]{"Queue", "Topic"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("InitialContextFactory")) {
         var3 = "getInitialContextFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInitialContextFactory";
         }

         var2 = new PropertyDescriptor("InitialContextFactory", JMSBridgeDestinationMBean.class, var3, var4);
         var1.put("InitialContextFactory", var2);
         var2.setValue("description", "<p>The initial context factory name for this JMS bridge destination.</p> ");
         setPropertyDescriptorDefault(var2, "weblogic.jndi.WLInitialContextFactory");
         var2.setValue("dynamic", Boolean.FALSE);
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
