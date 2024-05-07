package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class RemoteSAFContextMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = RemoteSAFContextMBean.class;

   public RemoteSAFContextMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public RemoteSAFContextMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = RemoteSAFContextMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Fake RemoteSAFContextMBean</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.RemoteSAFContextMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("JndiInitialContextFactory")) {
         var3 = "getJndiInitialContextFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJndiInitialContextFactory";
         }

         var2 = new PropertyDescriptor("JndiInitialContextFactory", RemoteSAFContextMBean.class, var3, var4);
         var1.put("JndiInitialContextFactory", var2);
         var2.setValue("description", "<p>JndiInitialContextFactory</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("JndiProperty")) {
         var3 = "getJndiProperty";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJndiProperty";
         }

         var2 = new PropertyDescriptor("JndiProperty", RemoteSAFContextMBean.class, var3, var4);
         var1.put("JndiProperty", var2);
         var2.setValue("description", "<p>JndiProperty</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Password")) {
         var3 = "getPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPassword";
         }

         var2 = new PropertyDescriptor("Password", RemoteSAFContextMBean.class, var3, var4);
         var1.put("Password", var2);
         var2.setValue("description", "<p>Password</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Protocol")) {
         var3 = "getProtocol";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setProtocol";
         }

         var2 = new PropertyDescriptor("Protocol", RemoteSAFContextMBean.class, var3, var4);
         var1.put("Protocol", var2);
         var2.setValue("description", "<p>Protocol</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Url")) {
         var3 = "getUrl";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUrl";
         }

         var2 = new PropertyDescriptor("Url", RemoteSAFContextMBean.class, var3, var4);
         var1.put("Url", var2);
         var2.setValue("description", "<p>URL</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Username")) {
         var3 = "getUsername";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUsername";
         }

         var2 = new PropertyDescriptor("Username", RemoteSAFContextMBean.class, var3, var4);
         var1.put("Username", var2);
         var2.setValue("description", "<p>UserName</p> ");
         var2.setValue("configurable", Boolean.TRUE);
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
