package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class GenericJDBCStoreMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = GenericJDBCStoreMBean.class;

   public GenericJDBCStoreMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public GenericJDBCStoreMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = GenericJDBCStoreMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This MBean defines the parameters for the JDBC store. It is the parent of the JDBCStoreMBean and the deprecated JMSJDBCStoreMBean.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.GenericJDBCStoreMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CreateTableDDLFile")) {
         var3 = "getCreateTableDDLFile";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCreateTableDDLFile";
         }

         var2 = new PropertyDescriptor("CreateTableDDLFile", GenericJDBCStoreMBean.class, var3, var4);
         var1.put("CreateTableDDLFile", var2);
         var2.setValue("description", "<p>Specifies the DDL (Data Definition Language) file to use for creating the JDBC store's backing table.</p>  <ul> <li> <p>This field is ignored when the JDBC store's backing table, <code>WLStore</code>, already exists.</p> </li>  <li> <p>If a DDL file is not specified and the JDBC store detects that a backing table doesn't already exist, the JDBC store automatically creates the table by executing a preconfigured DDL file that is specific to the database vendor. These preconfigured files are located in the  <code>weblogic\\store\\io\\jdbc\\ddl</code> directory of the <code><i>MIDDLEWARE_HOME</i>\\modules\\com.bea.core.store.jdbc_x.x.x.x.jar</code> file.</p> </li>  <li> <p>If a DDL file is specified and the JDBC store detects that a backing table doesn't already exist, then the JDBC store searches for the DDL file in the file path first, and then if the file is not found, it searches for it in the CLASSPATH. Once found, the SQL within the DDL file is executed to create the JDBC store's database table. If the DDL file is not found and the backing table doesn't already exist, the JDBC store will fail to boot.</p> </li> </ul> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("PrefixName")) {
         var3 = "getPrefixName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrefixName";
         }

         var2 = new PropertyDescriptor("PrefixName", GenericJDBCStoreMBean.class, var3, var4);
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
