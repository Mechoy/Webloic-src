package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class ForeignJNDIProviderMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ForeignJNDIProviderMBean.class;

   public ForeignJNDIProviderMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ForeignJNDIProviderMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ForeignJNDIProviderMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ForeignJNDIProviderMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ForeignJNDILinks")) {
         var3 = "getForeignJNDILinks";
         var4 = null;
         var2 = new PropertyDescriptor("ForeignJNDILinks", ForeignJNDIProviderMBean.class, var3, var4);
         var1.put("ForeignJNDILinks", var2);
         var2.setValue("description", "<p>The foreign links.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyForeignJNDILink");
         var2.setValue("creator", "createForeignJNDILink");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("InitialContextFactory")) {
         var3 = "getInitialContextFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInitialContextFactory";
         }

         var2 = new PropertyDescriptor("InitialContextFactory", ForeignJNDIProviderMBean.class, var3, var4);
         var1.put("InitialContextFactory", var2);
         var2.setValue("description", "<p>The initial context factory to use to connect. This class name depends on the JNDI provider and the vendor you are using. The value corresponds to the standard JNDI property, <code>java.naming.factory.initial</code>.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Password")) {
         var3 = "getPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPassword";
         }

         var2 = new PropertyDescriptor("Password", ForeignJNDIProviderMBean.class, var3, var4);
         var1.put("Password", var2);
         var2.setValue("description", "<p>The remote server's user password.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PasswordEncrypted")) {
         var3 = "getPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("PasswordEncrypted", ForeignJNDIProviderMBean.class, var3, var4);
         var1.put("PasswordEncrypted", var2);
         var2.setValue("description", "<p>The remote server's encrypted user password.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Properties")) {
         var3 = "getProperties";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setProperties";
         }

         var2 = new PropertyDescriptor("Properties", ForeignJNDIProviderMBean.class, var3, var4);
         var1.put("Properties", var2);
         var2.setValue("description", "<p>Any additional properties that must be set for the JNDI provider. These properties will be passed directly to the constructor for the JNDI provider's <tt>InitialContext</tt> class.</p>  <p><i>Note:</i> This value must be filled in using a <tt>name=value&lt;return&gt;name=value</tt> format.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ProviderURL")) {
         var3 = "getProviderURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setProviderURL";
         }

         var2 = new PropertyDescriptor("ProviderURL", ForeignJNDIProviderMBean.class, var3, var4);
         var1.put("ProviderURL", var2);
         var2.setValue("description", "<p>The foreign JNDI provider URL. This value corresponds to the standard JNDI property, <code>java.naming.provider.url</code>.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("User")) {
         var3 = "getUser";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUser";
         }

         var2 = new PropertyDescriptor("User", ForeignJNDIProviderMBean.class, var3, var4);
         var1.put("User", var2);
         var2.setValue("description", "<p>The remote server's user name.</p> ");
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
         var3 = ForeignJNDIProviderMBean.class.getMethod("createForeignJNDILink", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "Name of the foreign JNDI link ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Create a ForeignJNDILink resource with the given name.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ForeignJNDILinks");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ForeignJNDIProviderMBean.class.getMethod("destroyForeignJNDILink", ForeignJNDILinkMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", "foreign link ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Destroy the given ForeignJNDILink resource.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ForeignJNDILinks");
            var2.setValue("since", "9.0.0.0");
         }
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         Method var3 = ForeignJNDIProviderMBean.class.getMethod("lookupForeignJNDILink", String.class);
         ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "Name of the foreign JNDI link ")};
         String var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            MethodDescriptor var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Find a ForeignJNDILink resource with the given name.</p> ");
            var2.setValue("role", "finder");
            var2.setValue("property", "ForeignJNDILinks");
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
