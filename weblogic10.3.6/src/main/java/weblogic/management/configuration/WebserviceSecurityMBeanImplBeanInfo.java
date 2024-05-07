package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WebserviceSecurityMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebserviceSecurityMBean.class;

   public WebserviceSecurityMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebserviceSecurityMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebserviceSecurityMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Encapsulates information about a Web Service security configuration.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebserviceSecurityMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion) && !var1.containsKey("CompatibilityOrderingPreference")) {
         var3 = "getCompatibilityOrderingPreference";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompatibilityOrderingPreference";
         }

         var2 = new PropertyDescriptor("CompatibilityOrderingPreference", WebserviceSecurityMBean.class, var3, var4);
         var1.put("CompatibilityOrderingPreference", var2);
         var2.setValue("description", "<p>Gets the value of the compatiblityOrderingPreference attribute.</p> ");
         var2.setValue("since", "10.3.3.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.0.0", (String)null, this.targetVersion) && !var1.containsKey("CompatibilityPreference")) {
         var3 = "getCompatibilityPreference";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompatibilityPreference";
         }

         var2 = new PropertyDescriptor("CompatibilityPreference", WebserviceSecurityMBean.class, var3, var4);
         var1.put("CompatibilityPreference", var2);
         var2.setValue("description", "<p>Gets the value of the compatiblityPreference attribute.</p> ");
         var2.setValue("since", "10.3.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.0.0", (String)null, this.targetVersion) && !var1.containsKey("DefaultCredentialProviderSTSURI")) {
         var3 = "getDefaultCredentialProviderSTSURI";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultCredentialProviderSTSURI";
         }

         var2 = new PropertyDescriptor("DefaultCredentialProviderSTSURI", WebserviceSecurityMBean.class, var3, var4);
         var1.put("DefaultCredentialProviderSTSURI", var2);
         var2.setValue("description", "<p>Gets the default STS endpoint URL for all WS-Trust enabled credential providers of this Web Service security configuration.</p> ");
         var2.setValue("since", "10.3.0.0");
      }

      if (!var1.containsKey("PolicySelectionPreference")) {
         var3 = "getPolicySelectionPreference";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPolicySelectionPreference";
         }

         var2 = new PropertyDescriptor("PolicySelectionPreference", WebserviceSecurityMBean.class, var3, var4);
         var1.put("PolicySelectionPreference", var2);
         var2.setValue("description", "Gets the value of the PolicySelectionPreference attribute. </p> <p> The preference value can be one of the following: <ul> <li>NONE <li>SCP <li>SPC <li>CSP <li>CPS <li>PCS <li>PSC </ul> <p> Where: S: Security or functionality; C: Compatibility or Interoperability; P: Performance <p> If NONE is specified, no preference is applied - the first policy alternative is always chosen, and optional policy assertions are ignored. ");
         setPropertyDescriptorDefault(var2, "NONE");
         var2.setValue("legalValues", new Object[]{"NONE", "SCP", "SPC", "CSP", "CPS", "PCS", "PSC"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WebserviceCredentialProviders")) {
         var3 = "getWebserviceCredentialProviders";
         var4 = null;
         var2 = new PropertyDescriptor("WebserviceCredentialProviders", WebserviceSecurityMBean.class, var3, var4);
         var1.put("WebserviceCredentialProviders", var2);
         var2.setValue("description", "<p>Specifies the list of credential providers that have been configured for this Web Service security configuration.<p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createWebserviceCredentialProvider");
         var2.setValue("destroyer", "destroyWebserviceCredentialProvider");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WebserviceSecurityTokens")) {
         var3 = "getWebserviceSecurityTokens";
         var4 = null;
         var2 = new PropertyDescriptor("WebserviceSecurityTokens", WebserviceSecurityMBean.class, var3, var4);
         var1.put("WebserviceSecurityTokens", var2);
         var2.setValue("description", "<p>Specifies the list of tokens that have been configured for this Web Service security configuration.<p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWebserviceSecurityToken");
         var2.setValue("creator", "createWebserviceSecurityToken");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WebserviceTimestamp")) {
         var3 = "getWebserviceTimestamp";
         var4 = null;
         var2 = new PropertyDescriptor("WebserviceTimestamp", WebserviceSecurityMBean.class, var3, var4);
         var1.put("WebserviceTimestamp", var2);
         var2.setValue("description", "<p>Specifies the timestamp information that has been configured for this Web Service security configuration.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WebserviceTokenHandlers")) {
         var3 = "getWebserviceTokenHandlers";
         var4 = null;
         var2 = new PropertyDescriptor("WebserviceTokenHandlers", WebserviceSecurityMBean.class, var3, var4);
         var1.put("WebserviceTokenHandlers", var2);
         var2.setValue("description", "<p>Specifies the list of token handlers that have been configured for this Web Service security configuration.<p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWebserviceTokenHandler");
         var2.setValue("creator", "createWebserviceTokenHandler");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = WebserviceSecurityMBean.class.getMethod("createWebserviceTokenHandler", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of WebserviceTokenHandler ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "create WebserviceTokenHandler object ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WebserviceTokenHandlers");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = WebserviceSecurityMBean.class.getMethod("destroyWebserviceTokenHandler", WebserviceTokenHandlerMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("wsth", "WebserviceTokenHandler mbean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "destroy WebserviceTokenHandler object ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WebserviceTokenHandlers");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = WebserviceSecurityMBean.class.getMethod("createWebserviceCredentialProvider", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of WebserviceCredentialProvider ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "create WebserviceCredentialProvider object ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WebserviceCredentialProviders");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = WebserviceSecurityMBean.class.getMethod("destroyWebserviceCredentialProvider", WebserviceCredentialProviderMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("wcp", "WebserviceCredentialProvider mbean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "destroy WebserviceCredentialProvider object ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WebserviceCredentialProviders");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = WebserviceSecurityMBean.class.getMethod("createWebserviceSecurityToken", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of WebserviceSecurityToken ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "create WebserviceSecurityToken object ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WebserviceSecurityTokens");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = WebserviceSecurityMBean.class.getMethod("destroyWebserviceSecurityToken", WebserviceSecurityTokenMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("wcp", "WebserviceSecurityToken mbean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "destroy WebserviceSecurityToken object ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WebserviceSecurityTokens");
            var2.setValue("since", "9.0.0.0");
         }
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = WebserviceSecurityMBean.class.getMethod("lookupWebserviceTokenHandler", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "name of WebserviceSecurity ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "look up WebserviceSecurity object ");
            var2.setValue("role", "finder");
            var2.setValue("property", "WebserviceTokenHandlers");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = WebserviceSecurityMBean.class.getMethod("lookupWebserviceCredentialProvider", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "name of WebserviceSecurity ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "look up WebserviceSecurity object ");
            var2.setValue("role", "finder");
            var2.setValue("property", "WebserviceCredentialProviders");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = WebserviceSecurityMBean.class.getMethod("lookupWebserviceSecurityToken", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "name of WebserviceSecurityToken ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "look up WebserviceSecurityToken object ");
            var2.setValue("role", "finder");
            var2.setValue("property", "WebserviceSecurityTokens");
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
