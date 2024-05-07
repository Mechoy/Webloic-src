package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class RDBMSRealmMBeanImplBeanInfo extends BasicRealmMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = RDBMSRealmMBean.class;

   public RDBMSRealmMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public RDBMSRealmMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = RDBMSRealmMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "7.0.0.0 ");
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean holds the configuration properties of the RDBMS security realm.  The RDBMSRealm's mbean is hard-coded and is part of the product while the rest of the RDBMSRealm is an example which customers are free to change.  However, they must live within the configuration parameters of this mbean.  This MBean is associated with a CachingRealmMBean.  Deprecated in WebLogic Server version 7.0. Replaced by the new Security architecture that includes Authentication, Authorization, and Auditing providers. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.RDBMSRealmMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("DatabaseDriver")) {
         var3 = "getDatabaseDriver";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDatabaseDriver";
         }

         var2 = new PropertyDescriptor("DatabaseDriver", RDBMSRealmMBean.class, var3, var4);
         var1.put("DatabaseDriver", var2);
         var2.setValue("description", "<p>The Java class name for the database driver used with the RDBMS security realm.</p> ");
         setPropertyDescriptorDefault(var2, "");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DatabasePassword")) {
         var3 = "getDatabasePassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDatabasePassword";
         }

         var2 = new PropertyDescriptor("DatabasePassword", RDBMSRealmMBean.class, var3, var4);
         var1.put("DatabasePassword", var2);
         var2.setValue("description", "<p>The password required to log into the database.</p>  <p>As of 8.1 sp4, when you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>DatabasePasswordEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol> <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>DatabasePasswordEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>DatabasePassword</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>DatabasePasswordEncrypted</code>.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#getDatabasePasswordEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DatabasePasswordEncrypted")) {
         var3 = "getDatabasePasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDatabasePasswordEncrypted";
         }

         var2 = new PropertyDescriptor("DatabasePasswordEncrypted", RDBMSRealmMBean.class, var3, var4);
         var1.put("DatabasePasswordEncrypted", var2);
         var2.setValue("description", "<p>The encrypted password required to log into the database.</p> <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DatabaseURL")) {
         var3 = "getDatabaseURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDatabaseURL";
         }

         var2 = new PropertyDescriptor("DatabaseURL", RDBMSRealmMBean.class, var3, var4);
         var1.put("DatabaseURL", var2);
         var2.setValue("description", "<p>The location of the database.</p>  <p>Change the URL to the name of the computer on which the database is running and the number of the port at which the database is listening.</p> ");
         setPropertyDescriptorDefault(var2, "");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DatabaseUserName")) {
         var3 = "getDatabaseUserName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDatabaseUserName";
         }

         var2 = new PropertyDescriptor("DatabaseUserName", RDBMSRealmMBean.class, var3, var4);
         var1.put("DatabaseUserName", var2);
         var2.setValue("description", "<p>The user name used to login into the database.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("RealmClassName")) {
         var3 = "getRealmClassName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRealmClassName";
         }

         var2 = new PropertyDescriptor("RealmClassName", RDBMSRealmMBean.class, var3, var4);
         var1.put("RealmClassName", var2);
         var2.setValue("description", "<p>The name of the Java class that implements the RDBMS security realm.</p>  <p>This class should be included in the CLASSPATH of WebLogic Server.</p> ");
         setPropertyDescriptorDefault(var2, "");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SchemaProperties")) {
         var3 = "getSchemaProperties";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSchemaProperties";
         }

         var2 = new PropertyDescriptor("SchemaProperties", RDBMSRealmMBean.class, var3, var4);
         var1.put("SchemaProperties", var2);
         var2.setValue("description", "<p>The schema properties (the prepared statements) for manipulating the database.</p>  <p>Specify an open-ended properties list so that additional properties can be added to the code for the RDBMS security realm.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
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
