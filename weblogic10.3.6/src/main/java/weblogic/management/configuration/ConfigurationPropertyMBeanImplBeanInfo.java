package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class ConfigurationPropertyMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ConfigurationPropertyMBean.class;

   public ConfigurationPropertyMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ConfigurationPropertyMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ConfigurationPropertyMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Encapsulates information about a property, such as its value and whether it is encrypted.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ConfigurationPropertyMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("EncryptedValue")) {
         var3 = "getEncryptedValue";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEncryptedValue";
         }

         var2 = new PropertyDescriptor("EncryptedValue", ConfigurationPropertyMBean.class, var3, var4);
         var1.put("EncryptedValue", var2);
         var2.setValue("description", "<p>Specifies the decrypted value of the property.</p>  <p>Note: In release 10.3.1 of WebLogic Server, the behavior of the MBean encryption algorithm changed. In previous releases, if the newly set value was identical to the existing value, the encrypted value did not change.  That is, you would always get the same encrypted value for a given password   The action was not treated as a (non-dynamic) change. The behavior has been modified so that use of the setter on any existing encrypted value is considered to be a (dynamic) change, regardless of whether the new value matches the old value. Therefore, even if you set the password to the existing value, the setter now generates a different encrypted value for the given password.</P>  <p>Use this attribute if you have specified that property should be encrypted.</p> ");
         setPropertyDescriptorDefault(var2, "");
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("EncryptedValueEncrypted")) {
         var3 = "getEncryptedValueEncrypted";
         var4 = null;
         var2 = new PropertyDescriptor("EncryptedValueEncrypted", ConfigurationPropertyMBean.class, var3, var4);
         var1.put("EncryptedValueEncrypted", var2);
         var2.setValue("description", "<p>Get the encrytped bytes from EncryptedValue attribute</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("Value")) {
         var3 = "getValue";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setValue";
         }

         var2 = new PropertyDescriptor("Value", ConfigurationPropertyMBean.class, var3, var4);
         var1.put("Value", var2);
         var2.setValue("description", "<p>Specifies the value of the property.</p>  <p>If the property is encrypted, then attribute is null and one should use the EncryptedValue attribute to get the decrypted value.</p> ");
         setPropertyDescriptorDefault(var2, "");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("EncryptValueRequired")) {
         var3 = "isEncryptValueRequired";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEncryptValueRequired";
         }

         var2 = new PropertyDescriptor("EncryptValueRequired", ConfigurationPropertyMBean.class, var3, var4);
         var1.put("EncryptValueRequired", var2);
         var2.setValue("description", "<p>Specifies whether the property should be encrypted.</p>  <p> By default, the value of a property is not encrypted and anyone using the Administration Console can view the value of the property. If this attribute is set to true, then the value of the property on the Administration Console will be set to all asterisks.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
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
