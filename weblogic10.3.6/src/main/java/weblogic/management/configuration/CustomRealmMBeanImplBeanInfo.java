package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class CustomRealmMBeanImplBeanInfo extends BasicRealmMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = CustomRealmMBean.class;

   public CustomRealmMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CustomRealmMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CustomRealmMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "7.0.0.0 ");
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean holds configuration properties for a custom security realm. A custom security realm is a user implementation of the WebLogic realm API.  Since the configuration properties are not known, the MBean specifies an attribute that contains a properties list. The writers decides which properties should be in the list.  Deprecated in WebLogic Server version 7.0. Replaced by the new Security architecture that includes Authentication, Authorization, and Auditing providers ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.CustomRealmMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ConfigurationData")) {
         var3 = "getConfigurationData";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConfigurationData";
         }

         var2 = new PropertyDescriptor("ConfigurationData", CustomRealmMBean.class, var3, var4);
         var1.put("ConfigurationData", var2);
         var2.setValue("description", "<p>The information needed to connect to the security store of the Custom security realm.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("Password")) {
         var3 = "getPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPassword";
         }

         var2 = new PropertyDescriptor("Password", CustomRealmMBean.class, var3, var4);
         var1.put("Password", var2);
         var2.setValue("description", "<p>The password for the custom security realm.</p>  <p>As of 8.1 sp4, when you get this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>PasswordEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol>  <p>When you set this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>PasswordEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using <code>Password()</code> is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>PasswordEncrypted()</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getPasswordEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PasswordEncrypted")) {
         var3 = "getPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("PasswordEncrypted", CustomRealmMBean.class, var3, var4);
         var1.put("PasswordEncrypted", var2);
         var2.setValue("description", "<p>The encrypted password for the custom security realm.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("<a href=\"../../javadocs/weblogic/management/EncryptionHelper.html\">weblogic.management.EncryptionHelper</a>")};
         var2.setValue("see", var5);
         var2.setValue("secureValue", "this value must be undefined when you initialize a <code>CustomRealmMBean</code>");
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("RealmClassName")) {
         var3 = "getRealmClassName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRealmClassName";
         }

         var2 = new PropertyDescriptor("RealmClassName", CustomRealmMBean.class, var3, var4);
         var1.put("RealmClassName", var2);
         var2.setValue("description", "<p>The name of Java class that implements the Custom security realm.</p> ");
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
