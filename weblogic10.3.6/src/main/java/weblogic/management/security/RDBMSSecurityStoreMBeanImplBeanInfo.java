package weblogic.management.security;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.commo.AbstractCommoConfigurationBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class RDBMSSecurityStoreMBeanImplBeanInfo extends AbstractCommoConfigurationBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = RDBMSSecurityStoreMBean.class;

   public RDBMSSecurityStoreMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public RDBMSSecurityStoreMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = RDBMSSecurityStoreMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.security");
      String var3 = (new String("The MBean that represents configuration attributes for a RDBMS security store. It is used to specify the required and optional properties for connecting to a RDBMS back-end store. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.RDBMSSecurityStoreMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ConnectionProperties")) {
         var3 = "getConnectionProperties";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionProperties";
         }

         var2 = new PropertyDescriptor("ConnectionProperties", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("ConnectionProperties", var2);
         var2.setValue("description", "The JDBC driver specific connection parameters. This attribute is a comma-delimited list of key-value properties to pass to the driver for configuration of JDBC connection pool, in the form of <i>xx</i>Key=<i>xx</i>Value, <i>xx</i>Key=<i>xx</i>Value. <p/> The syntax of the attribute will be validated and an <code>InvalidAttributeValueException</code> is thrown if the check failed. ");
      }

      if (!var1.containsKey("ConnectionURL")) {
         var3 = "getConnectionURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionURL";
         }

         var2 = new PropertyDescriptor("ConnectionURL", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("ConnectionURL", var2);
         var2.setValue("description", "The URL of the database to which to connect. The format of the URL varies by JDBC driver. <p/> <p>The URL is passed to the JDBC driver to create the physical database connections.</p> ");
         var2.setValue("legalNull", Boolean.TRUE);
      }

      if (!var1.containsKey("DriverName")) {
         var3 = "getDriverName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDriverName";
         }

         var2 = new PropertyDescriptor("DriverName", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("DriverName", var2);
         var2.setValue("description", "The full package name of the JDBC driver class used to create the physical database connections in the connection pool. Note that this driver class must be in the classpath of any server to which it is deployed. <p/> <p>For example:</p> <ol> <li><code>oracle.jdbc.OracleDriver</code></li> <li><code>com.microsoft.sqlserver.jdbc.SQLServerDriver</code></li> </ol> <p>It must be the name of a class that implements the <code>java.sql.Driver</code> interface. The full pathname of the JDBC driver is available in the documentation. </p> ");
         var2.setValue("legalNull", Boolean.TRUE);
      }

      if (!var1.containsKey("JMSExceptionReconnectAttempts")) {
         var3 = "getJMSExceptionReconnectAttempts";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJMSExceptionReconnectAttempts";
         }

         var2 = new PropertyDescriptor("JMSExceptionReconnectAttempts", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("JMSExceptionReconnectAttempts", var2);
         var2.setValue("description", "The number of times to attempt to reconnect if the JMS system notifies Kodo of a serious connection error. <p/> The default is 0, and by default the error is logged but ignored. The value cannot be less than 0. ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("JMSTopic")) {
         var3 = "getJMSTopic";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJMSTopic";
         }

         var2 = new PropertyDescriptor("JMSTopic", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("JMSTopic", var2);
         var2.setValue("description", "The JMS topic to which the Kodo remote commit provider should publish notifications and subscribe for notifications sent from other JVMs. This setting varies depending on the application server in use. ");
      }

      if (!var1.containsKey("JMSTopicConnectionFactory")) {
         var3 = "getJMSTopicConnectionFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJMSTopicConnectionFactory";
         }

         var2 = new PropertyDescriptor("JMSTopicConnectionFactory", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("JMSTopicConnectionFactory", var2);
         var2.setValue("description", "The JNDI name of a <code>javax.jms.TopicConnectionFactory</code> instance to use for finding JMS topics. <p/> This setting varies depending on the application server in use. Consult the JMS documentation for details about how this parameter should be specified. ");
      }

      String[] var5;
      if (!var1.containsKey("JNDIPassword")) {
         var3 = "getJNDIPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDIPassword";
         }

         var2 = new PropertyDescriptor("JNDIPassword", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("JNDIPassword", var2);
         var2.setValue("description", "The password to authenticate the user defined in the <code>JNDIUsername</code> attribute for Kodo notification. <p/> <p>When getting the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>JNDIPasswordEncrypted</code> attribute.</li> <li>Decrypts the value and returns the unencrypted password as a String. </li> </ol> <p/> <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>JNDIPasswordEncrypted</code> attribute to the encrypted value.</li> </ol> <p/> <p>Using this attribute (<code>JNDIPassword</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p> <p/> <p>Instead of using this attribute, use <code>JNDIPasswordEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getJNDIPasswordEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("JNDIPasswordEncrypted")) {
         var3 = "getJNDIPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDIPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("JNDIPasswordEncrypted", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("JNDIPasswordEncrypted", var2);
         var2.setValue("description", "Returns the encrypted password to authenticate the user defined in the <code>JNDIUsername</code> attribute for Kodo notification. <p/> <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p> <p/> <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("JNDIUsername")) {
         var3 = "getJNDIUsername";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDIUsername";
         }

         var2 = new PropertyDescriptor("JNDIUsername", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("JNDIUsername", var2);
         var2.setValue("description", "The JNDI user name used for Kodo notification. ");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "The name of this configuration. ");
         setPropertyDescriptorDefault(var2, "RDBMSSecurityStore");
         var2.setValue("legal", "");
      }

      if (!var1.containsKey("NotificationProperties")) {
         var3 = "getNotificationProperties";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNotificationProperties";
         }

         var2 = new PropertyDescriptor("NotificationProperties", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("NotificationProperties", var2);
         var2.setValue("description", "The comma-delimited list of key-value properties to pass to the JNDI InitialContext on construction, in the form of <i>xx</i>Key=<i>xx</i>Value, <i>xx</i>Key=<i>xx</i>Value. </p> <p>The following are examples of keys:</p> <ol><li><code>java.naming.provider.url:</code> property for specifying configuration information for the service provider to use. The value of the property should contain a URL string (For example: <code>iiops://localhost:7002</code>).</li> <li><code>java.naming.factory.initial:</code> property for specifying the initial context factory to use. The value of the property should be the fully qualified class name of the factory class that will create an initial context (For example: <code>weblogic.jndi.WLInitialContextFactory</code>).</li> </ol> </p> <p>When setting the attribute, the syntax of its value is validated, and an <code>InvalidAttributeValueException</code> is thrown if the check fails.</p> ");
      }

      if (!var1.containsKey("Password")) {
         var3 = "getPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPassword";
         }

         var2 = new PropertyDescriptor("Password", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("Password", var2);
         var2.setValue("description", "The password for the user specified in the <code>Username</code> attribute for connecting to the datastore. <p/> <p>When getting the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>PasswordEncrypted</code> attribute.</li> <li>Decrypts the value and returns the unencrypted password as a String. </li> </ol> <p/> <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>PasswordEncrypted</code> attribute to the encrypted value.</li> </ol> <p/> <p>Note that use of the <code>Password</code> attribute is a potential security risk because the String object that contains the unencrypted password remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p> <p/> <p>Instead of using this attribute, use <code>PasswordEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getPasswordEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("PasswordEncrypted")) {
         var3 = "getPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("PasswordEncrypted", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("PasswordEncrypted", var2);
         var2.setValue("description", "Returns the encrypted password to authenticate the user defined in the <code>Username</code> attribute when connecting to the data store. <p/> <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p> <p/> <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("Realm")) {
         var3 = "getRealm";
         var4 = null;
         var2 = new PropertyDescriptor("Realm", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("Realm", var2);
         var2.setValue("description", "Returns the realm that contains this RDBMS security store. Returns null if this RDBMS security store is not contained by a realm. ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("Username")) {
         var3 = "getUsername";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUsername";
         }

         var2 = new PropertyDescriptor("Username", RDBMSSecurityStoreMBean.class, var3, var4);
         var1.put("Username", var2);
         var2.setValue("description", "The username to use when connecting to the datastore. ");
         var2.setValue("legalNull", Boolean.TRUE);
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
