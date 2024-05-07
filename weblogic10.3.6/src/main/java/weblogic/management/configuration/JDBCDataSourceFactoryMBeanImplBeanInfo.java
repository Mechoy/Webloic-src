package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JDBCDataSourceFactoryMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JDBCDataSourceFactoryMBean.class;

   public JDBCDataSourceFactoryMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JDBCDataSourceFactoryMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JDBCDataSourceFactoryMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.AppDeploymentMBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This MBean represents the object used to create data sources that applications use to access application-scoped JDBC connection pools.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JDBCDataSourceFactoryMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("DriverClassName")) {
         var3 = "getDriverClassName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDriverClassName";
         }

         var2 = new PropertyDescriptor("DriverClassName", JDBCDataSourceFactoryMBean.class, var3, var4);
         var1.put("DriverClassName", var2);
         var2.setValue("description", "<p>The full package name of JDBC driver class used to create the physical database connections in the connection pool.</p>  <p>This may be overridden by <code>driver-name</code> in the descriptor.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("FactoryName")) {
         var3 = "getFactoryName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFactoryName";
         }

         var2 = new PropertyDescriptor("FactoryName", JDBCDataSourceFactoryMBean.class, var3, var4);
         var1.put("FactoryName", var2);
         var2.setValue("description", "<p>The name used in deployment descriptor files to reference this JDBC data source factory.</p>  <p>This is referenced from the <code>connection-factory</code> element in <code>weblogic-application.xml</code>.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", JDBCDataSourceFactoryMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("Password")) {
         var3 = "getPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPassword";
         }

         var2 = new PropertyDescriptor("Password", JDBCDataSourceFactoryMBean.class, var3, var4);
         var1.put("Password", var2);
         var2.setValue("description", "<p>The database user password. If the user password is specified in the descriptor, the descriptor value overrides this value.</p>  <p>As of 8.1 sp4, when you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>PasswordEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol> <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>PasswordEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>Password</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>PasswordEncrypted()</code>.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("getPasswordEncrypted();")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("PasswordEncrypted")) {
         var3 = "getPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("PasswordEncrypted", JDBCDataSourceFactoryMBean.class, var3, var4);
         var1.put("PasswordEncrypted", var2);
         var2.setValue("description", "<p>The encrypted database user password. If the user password is specified in the descriptor, the descriptor value overrides this value.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("Properties")) {
         var3 = "getProperties";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setProperties";
         }

         var2 = new PropertyDescriptor("Properties", JDBCDataSourceFactoryMBean.class, var3, var4);
         var1.put("Properties", var2);
         var2.setValue("description", "<p>The list of properties passed to the JDBC driver that are used to create physical database connections. For example: <tt>server=dbserver1</tt>.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("URL")) {
         var3 = "getURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setURL";
         }

         var2 = new PropertyDescriptor("URL", JDBCDataSourceFactoryMBean.class, var3, var4);
         var1.put("URL", var2);
         var2.setValue("description", "<p>The URL of the database to connect to. The format of the URL varies by JDBC driver.</p>  <p>This may be overridden by <code>url</code> in the descriptor.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("UserName")) {
         var3 = "getUserName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserName";
         }

         var2 = new PropertyDescriptor("UserName", JDBCDataSourceFactoryMBean.class, var3, var4);
         var1.put("UserName", var2);
         var2.setValue("description", "<p>The database account user name used in physical database connections. This may be overridden by <code>user-name</code> in the descriptor.</p> ");
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
      Method var3 = JDBCDataSourceFactoryMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCDataSourceFactoryMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
      }

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
