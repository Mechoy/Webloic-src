package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class ContextCaseMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ContextCaseMBean.class;

   public ContextCaseMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ContextCaseMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ContextCaseMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This mbean defines the mapping between the current context (security principal, group etc) and the request class to use.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ContextCaseMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("GroupName")) {
         var3 = "getGroupName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGroupName";
         }

         var2 = new PropertyDescriptor("GroupName", ContextCaseMBean.class, var3, var4);
         var1.put("GroupName", var2);
         var2.setValue("description", "<p>The name of the user group whose requests are to be processed by the request class with the name specified in RequestClassName.</p> ");
      }

      if (!var1.containsKey("RequestClassName")) {
         var3 = "getRequestClassName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRequestClassName";
         }

         var2 = new PropertyDescriptor("RequestClassName", ContextCaseMBean.class, var3, var4);
         var1.put("RequestClassName", var2);
         var2.setValue("description", "<p>The name of the request class to be used for processing requests for the specified user and/or group.</p> ");
      }

      if (!var1.containsKey("UserName")) {
         var3 = "getUserName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserName";
         }

         var2 = new PropertyDescriptor("UserName", ContextCaseMBean.class, var3, var4);
         var1.put("UserName", var2);
         var2.setValue("description", "<p>The name of the user whose requests are to be processed by the request class with the name specified in RequestClassName.</p> ");
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
