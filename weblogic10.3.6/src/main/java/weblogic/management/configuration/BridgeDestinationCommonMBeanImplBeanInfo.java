package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class BridgeDestinationCommonMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = BridgeDestinationCommonMBean.class;

   public BridgeDestinationCommonMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public BridgeDestinationCommonMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = BridgeDestinationCommonMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This MBean represents a bridge destination for a messaging bridge instance. Each messaging bridge instance consists of the following destination types:</p>  <ul> <li> <p>Source: The message producing destination. A bridge instance consumes messages from the source destination.</p> </li>  <li> <p>Target: The destination where a bridge instance forwards messages produced by the source destination.</p> </li> </ul>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.BridgeDestinationCommonMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AdapterJNDIName")) {
         var3 = "getAdapterJNDIName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdapterJNDIName";
         }

         var2 = new PropertyDescriptor("AdapterJNDIName", BridgeDestinationCommonMBean.class, var3, var4);
         var1.put("AdapterJNDIName", var2);
         var2.setValue("description", "<p>The JNDI name of the adapter used to communicate with the specified destination.</p>  <p>This name is specified in the adapter's deployment descriptor file and is used by the WebLogic Server Connector container to bind the adapter in WebLogic Server JNDI.</p> ");
         setPropertyDescriptorDefault(var2, "eis.jms.WLSConnectionFactoryJNDIXA");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Classpath")) {
         var3 = "getClasspath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClasspath";
         }

         var2 = new PropertyDescriptor("Classpath", BridgeDestinationCommonMBean.class, var3, var4);
         var1.put("Classpath", var2);
         var2.setValue("description", "<p>The <tt>CLASSPATH</tt> of the bridge destination.</p>  <ul> <li> <p>Used mainly to connect to another release of WebLogic Server.</p> </li>  <li> <p>When connecting to a destination that is running on WebLogic Server 6.0 or earlier, the bridge destination must supply a <tt>CLASSPATH</tt> that indicates the locations of the classes for the earlier WebLogic Server implementation.</p> </li>  <li> <p>When connecting to a third-party JMS product, the bridge destination must supply the product's <tt>CLASSPATH</tt> in the WebLogic Server <tt>CLASSPATH</tt>.</p> </li> </ul> ");
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("UserName")) {
         var3 = "getUserName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserName";
         }

         var2 = new PropertyDescriptor("UserName", BridgeDestinationCommonMBean.class, var3, var4);
         var1.put("UserName", var2);
         var2.setValue("description", "<p>The optional user name the adapter uses to access the bridge destination.</p>  <p>All operations on the specified destination are done using this user name and the corresponding password. Therefore, the User Name/Password for the source and target destinations must have permission to the access the underlying destinations in order for the messaging bridge to work.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("UserPassword")) {
         var3 = "getUserPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserPassword";
         }

         var2 = new PropertyDescriptor("UserPassword", BridgeDestinationCommonMBean.class, var3, var4);
         var1.put("UserPassword", var2);
         var2.setValue("description", "<p>The user password that the adapter uses to access the bridge destination.</p>  <p>As of 8.1 sp4, when you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>UserPasswordEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>UserPasswordEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>UserPassword</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>UserPasswordEncrypted</code>.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#getUserPasswordEncrypted")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("UserPasswordEncrypted")) {
         var3 = "getUserPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("UserPasswordEncrypted", BridgeDestinationCommonMBean.class, var3, var4);
         var1.put("UserPasswordEncrypted", var2);
         var2.setValue("description", "<p>The encrypted user password that the adapter uses to access the bridge destination.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
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
