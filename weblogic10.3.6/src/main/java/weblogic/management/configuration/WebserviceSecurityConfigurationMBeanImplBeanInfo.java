package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WebserviceSecurityConfigurationMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebserviceSecurityConfigurationMBean.class;

   public WebserviceSecurityConfigurationMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebserviceSecurityConfigurationMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebserviceSecurityConfigurationMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Encapsulates information about a Web Service security configuration.</p>  <p>This information includes the list of tokens, credential providers, token handlers, and the timestamp.  After you have created a Web Service security configuration, you can associate it to a Web Service.  </p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebserviceSecurityConfigurationMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ClassName")) {
         var3 = "getClassName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClassName";
         }

         var2 = new PropertyDescriptor("ClassName", WebserviceSecurityConfigurationMBean.class, var3, var4);
         var1.put("ClassName", var2);
         var2.setValue("description", "<p>The fully qualified name of the class that implements a particular credential provider or token handler.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ConfigurationProperties")) {
         var3 = "getConfigurationProperties";
         var4 = null;
         var2 = new PropertyDescriptor("ConfigurationProperties", WebserviceSecurityConfigurationMBean.class, var3, var4);
         var1.put("ConfigurationProperties", var2);
         var2.setValue("description", "<p>Specifies the list of properties that are associated with this credential provider or token handler.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createConfigurationProperty");
         var2.setValue("destroyer", "destroyConfigurationProperty");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("TokenType")) {
         var3 = "getTokenType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTokenType";
         }

         var2 = new PropertyDescriptor("TokenType", WebserviceSecurityConfigurationMBean.class, var3, var4);
         var1.put("TokenType", var2);
         var2.setValue("description", "<p>Specifies the type of token used for the particular credential provider or token handler.</p>  <p>WebLogic Server supports, by default, three types of tokens: X.509, UsernameToken, and SAML, as specified by the following WS-Security specifications:</p> <ul> <li>Web Services Security: Username Token Profile</li> <li>Web Services Security: X.509 Token Profile</li> <li>Web Services Security: SAML Token Profile</li> </ul> <p>To specify one of these out-of-the-box types, use these values respectively: \"ut\", \"x509\", or \"saml\". </p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = WebserviceSecurityConfigurationMBean.class.getMethod("createConfigurationProperty", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of ConfigurationProperty ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "create ConfigurationProperty object ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ConfigurationProperties");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = WebserviceSecurityConfigurationMBean.class.getMethod("destroyConfigurationProperty", ConfigurationPropertyMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("wsc", "ConfigurationProperty mbean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "destroy ConfigurationProperty object ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ConfigurationProperties");
            var2.setValue("since", "9.0.0.0");
         }
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         Method var3 = WebserviceSecurityConfigurationMBean.class.getMethod("lookupConfigurationProperty", String.class);
         ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "name of ConfigurationProperty ")};
         String var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            MethodDescriptor var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "look up ConfigurationProperty object ");
            var2.setValue("role", "finder");
            var2.setValue("property", "ConfigurationProperties");
            var2.setValue("since", "9.0.0.0");
         }
      }

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
