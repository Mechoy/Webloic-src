package weblogic.management.security.pk;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.security.ProviderMBeanImplBeanInfo;

public class KeyStoreMBeanImplBeanInfo extends ProviderMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = KeyStoreMBean.class;

   public KeyStoreMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public KeyStoreMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = KeyStoreMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("deprecated", "8.1.0.0 ");
      var2.setValue("package", "weblogic.management.security.pk");
      String var3 = (new String("The SSPI MBean that all Keystore providers must extend.  It was deprecated in WLS 8.1.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.pk.KeyStoreMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("PrivateKeyStoreLocation")) {
         var3 = "getPrivateKeyStoreLocation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrivateKeyStoreLocation";
         }

         var2 = new PropertyDescriptor("PrivateKeyStoreLocation", KeyStoreMBean.class, var3, var4);
         var1.put("PrivateKeyStoreLocation", var2);
         var2.setValue("description", "Returns the location of the keystore used to store identities - that is, certificate and private key pairs. <p> The configured Keystore provider implementation determines the requirements for this attribute. For more information about legal values, refer to the documentation supplied by the Keystore security vendor. ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      String[] var5;
      if (!var1.containsKey("PrivateKeyStorePassPhrase")) {
         var3 = "getPrivateKeyStorePassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrivateKeyStorePassPhrase";
         }

         var2 = new PropertyDescriptor("PrivateKeyStorePassPhrase", KeyStoreMBean.class, var3, var4);
         var1.put("PrivateKeyStorePassPhrase", var2);
         var2.setValue("description", "Returns the passphrase used to access the keystore specified by the <code>PrivateKeyStoreLocation</code> attribute. If the passphase is null, no passphrase will be used to access the keystore. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getPrivateKeyStorePassPhraseEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("PrivateKeyStorePassPhraseEncrypted")) {
         var3 = "getPrivateKeyStorePassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrivateKeyStorePassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("PrivateKeyStorePassPhraseEncrypted", KeyStoreMBean.class, var3, var4);
         var1.put("PrivateKeyStorePassPhraseEncrypted", var2);
         var2.setValue("description", "Returns the passphrase used to access the keystore specified by the <code>PrivateKeyStoreLocation</code> attribute. If the passphase is null, no passphrase will be used to access the keystore. ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("RootCAKeyStoreLocation")) {
         var3 = "getRootCAKeyStoreLocation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRootCAKeyStoreLocation";
         }

         var2 = new PropertyDescriptor("RootCAKeyStoreLocation", KeyStoreMBean.class, var3, var4);
         var1.put("RootCAKeyStoreLocation", var2);
         var2.setValue("description", "Returns the location of the keystore used to store trusted certificate authority certificates. <p> The configured Keystore provider implementation determines the requirements for this attribute. For more information about legal values, refer to the documentation supplied by the Keystore security vendor. ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("RootCAKeyStorePassPhrase")) {
         var3 = "getRootCAKeyStorePassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRootCAKeyStorePassPhrase";
         }

         var2 = new PropertyDescriptor("RootCAKeyStorePassPhrase", KeyStoreMBean.class, var3, var4);
         var1.put("RootCAKeyStorePassPhrase", var2);
         var2.setValue("description", "Returns the passphrase used to access the keystore specified by the <code>RootCAKeyStoreLocation</code> attribute. If the passphase is null, no passphrase will be used to access the keystore. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getRootCAKeyStorePassPhraseEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("RootCAKeyStorePassPhraseEncrypted")) {
         var3 = "getRootCAKeyStorePassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRootCAKeyStorePassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("RootCAKeyStorePassPhraseEncrypted", KeyStoreMBean.class, var3, var4);
         var1.put("RootCAKeyStorePassPhraseEncrypted", var2);
         var2.setValue("description", "Returns the passphrase used to access the keystore specified by the <code>RootCAKeyStoreLocation</code> attribute. If the passphase is null, no passphrase will be used to access the keystore. ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Type")) {
         var3 = "getType";
         var4 = null;
         var2 = new PropertyDescriptor("Type", KeyStoreMBean.class, var3, var4);
         var1.put("Type", var2);
         var2.setValue("description", "Returns the type of the Keystore implementation that this provider supports, as defined by the JavaSoft Cryptography Architecture specification. ");
         setPropertyDescriptorDefault(var2, "jks");
         var2.setValue("transient", Boolean.TRUE);
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
