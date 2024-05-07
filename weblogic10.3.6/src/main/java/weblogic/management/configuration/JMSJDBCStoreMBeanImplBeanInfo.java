package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JMSJDBCStoreMBeanImplBeanInfo extends JMSStoreMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSJDBCStoreMBean.class;

   public JMSJDBCStoreMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JMSJDBCStoreMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JMSJDBCStoreMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("obsolete", "9.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.JDBCStoreMBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class represents a JMS JDBC store for storing persistent messages and durable subscribers in a JDBC-accessible database.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JMSJDBCStoreMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant((String)null, (String)null, this.targetVersion) && !var1.containsKey("ConnectionPool")) {
         var3 = "getConnectionPool";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionPool";
         }

         var2 = new PropertyDescriptor("ConnectionPool", JMSJDBCStoreMBean.class, var3, var4);
         var1.put("ConnectionPool", var2);
         var2.setValue("description", "The JDBC Connection Pool used by this JDBC store to access its backing table -- the specified JDBC connection pool must use a non-XA JDBC driver, connection pools for XA JDBC drivers are not supported. <p>This attribute is not dynamically configurable. ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("obsolete", "true");
      }

      if (!var1.containsKey("CreateTableDDLFile")) {
         var3 = "getCreateTableDDLFile";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCreateTableDDLFile";
         }

         var2 = new PropertyDescriptor("CreateTableDDLFile", JMSJDBCStoreMBean.class, var3, var4);
         var1.put("CreateTableDDLFile", var2);
         var2.setValue("description", "<p>Specifies the DDL (Data Definition Language) file to use for creating the JDBC store's backing table.</p>  <ul> <li> <p>This field is ignored when the JDBC store's backing table, <code>WLStore</code>, already exists.</p> </li>  <li> <p>If a DDL file is not specified and the JDBC store detects that a backing table doesn't already exist, the JDBC store automatically creates the table by executing a preconfigured DDL file that is specific to the database vendor. These preconfigured files are located in the  <code>weblogic\\store\\io\\jdbc\\ddl</code> directory of the <code><i>MIDDLEWARE_HOME</i>\\modules\\com.bea.core.store.jdbc_x.x.x.x.jar</code> file.</p> </li>  <li> <p>If a DDL file is specified and the JDBC store detects that a backing table doesn't already exist, then the JDBC store searches for the DDL file in the file path first, and then if the file is not found, it searches for it in the CLASSPATH. Once found, the SQL within the DDL file is executed to create the JDBC store's database table. If the DDL file is not found and the backing table doesn't already exist, the JDBC store will fail to boot.</p> </li> </ul> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", JMSJDBCStoreMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("PrefixName")) {
         var3 = "getPrefixName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrefixName";
         }

         var2 = new PropertyDescriptor("PrefixName", JMSJDBCStoreMBean.class, var3, var4);
         var1.put("PrefixName", var2);
         var2.setValue("description", "<p>The prefix for the JDBC store's database table (<code>WLStore</code>), in the following format: <code>[[[catalog.]schema.]prefix]</code>.</p>  <p>Each period symbol in the <code>[[catalog.]schema.]prefix</code> format is significant, where schema generally corresponds to username in many databases. When no prefix is specified, the JDBC store table name is simply <code>WLStore</code> and the database implicitly determines the schema according to the JDBC connection's user. As a best practice, you should always configure a prefix for the JDBC <code>WLStore</code> table name.</p> <p> For specific guidelines about using JDBC store prefixes, refer to the \"Using the WebLogic Store\" section of <i>Designing and Configuring WebLogic Server Environments\"</i>.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
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
      Method var3 = JMSJDBCStoreMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = JMSJDBCStoreMBean.class.getMethod("restoreDefaultValue", String.class);
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
