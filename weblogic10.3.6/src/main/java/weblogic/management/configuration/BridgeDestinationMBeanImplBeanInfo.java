package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class BridgeDestinationMBeanImplBeanInfo extends BridgeDestinationCommonMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = BridgeDestinationMBean.class;

   public BridgeDestinationMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public BridgeDestinationMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = BridgeDestinationMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "9.0.0.0 Replaced with nothing (upgrade to JMSBridgeDestination if it maps to a JMS destination). ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This MBean represents a bridge destination for non-JMS messaging products. Each messaging bridge instance consists of the following destination types:</p>  <ul> <li> <p>Source: The message producing destination. A bridge instance consumes messages from the source destination.</p> </li>  <li> <p>Target: The destination where a bridge instance forwards messages produced by the source destination.</p> </li> </ul>  <b>Note:</b> <p>Although WebLogic JMS includes a \"General Bridge Destination\" framework for accessing non-JMS messaging products, WebLogic Server does not provide supported adapters for such products. Therefore, you need to obtain a custom adapter from a third-party OEM vendor or contact Oracle.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.BridgeDestinationMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      if (!var1.containsKey("Properties")) {
         String var3 = "getProperties";
         String var4 = null;
         if (!this.readOnly) {
            var4 = "setProperties";
         }

         var2 = new PropertyDescriptor("Properties", BridgeDestinationMBean.class, var3, var4);
         var1.put("Properties", var2);
         var2.setValue("description", "<p>Specifies all the properties of the bridge destination. The destination properties are string values that must be separated by a semicolon (;).</p>  <p>The following properties are required for all JMS implementations:</p>  <dl> <dt><tt>ConnectionURL=</tt></dt>  <dd> <p>The URL used to establish a connection to the destination.</p> </dd>  <dt><tt>ConnectionFactoryJNDIName=</tt></dt>  <dd> <p>The JNDI name of the JMS connection factory used to create a connection for the actual destination being mapped to the general bridge destination.</p> </dd>  <dt><tt>DestinationJNDIName=</tt></dt>  <dd> <p>The JNDI name of the actual destination being mapped to the general bridge destination.</p> </dd>  <dt><tt>DestinationType=</tt></dt>  <dd> <p>Specifies whether the destination type is either a Queue or Topic.</p> </dd>  <dt><tt>InitialContextFactory=</tt></dt>  <dd> <p>The factory used to get the JNDI context.</p> </dd> </dl> ");
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
