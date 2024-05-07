package weblogic.management.security.authentication;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class IdentityAsserterMBeanImplBeanInfo extends AuthenticationProviderMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = IdentityAsserterMBean.class;

   public IdentityAsserterMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public IdentityAsserterMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = IdentityAsserterMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.authentication");
      String var3 = (new String("The SSPI MBean that all Identity Assertion providers must extend. This MBean enables an Identity Assertion provider to specify the token types for which it is capable of asserting identity.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.authentication.IdentityAsserterMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ActiveTypes")) {
         var3 = "getActiveTypes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setActiveTypes";
         }

         var2 = new PropertyDescriptor("ActiveTypes", IdentityAsserterMBean.class, var3, var4);
         var1.put("ActiveTypes", var2);
         var2.setValue("description", "Returns the token types that the Identity Assertion provider is currently configured to process. ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Base64DecodingRequired")) {
         var3 = "getBase64DecodingRequired";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBase64DecodingRequired";
         }

         var2 = new PropertyDescriptor("Base64DecodingRequired", IdentityAsserterMBean.class, var3, var4);
         var1.put("Base64DecodingRequired", var2);
         var2.setValue("description", "Returns whether the tokens that are passed to the Identity Assertion provider will be base64 decoded first.  If <code>false</code> then the server will not base64 decode the token before passing it to the identity asserter. This defaults to <code>true</code> for backwards compatibility but most providers will probably want to set this to <code>false</code>. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Realm")) {
         var3 = "getRealm";
         var4 = null;
         var2 = new PropertyDescriptor("Realm", IdentityAsserterMBean.class, var3, var4);
         var1.put("Realm", var2);
         var2.setValue("description", "Returns the realm that contains this security provider. Returns null if this security provider is not contained by a realm. ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SupportedTypes")) {
         var3 = "getSupportedTypes";
         var4 = null;
         var2 = new PropertyDescriptor("SupportedTypes", IdentityAsserterMBean.class, var3, var4);
         var1.put("SupportedTypes", var2);
         var2.setValue("description", "Returns the list of token types supported by the Identity Assertion provider. <p> To see a list of default token types, refer the Javadoc for <code>weblogic.security.spi.IdentityAsserter</code> ");
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
