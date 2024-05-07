package weblogic.servlet.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.PageFlowsRuntimeMBean;

public class PageFlowsRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = PageFlowsRuntimeMBean.class;

   public PageFlowsRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public PageFlowsRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = PageFlowsRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.1.0");
      var2.setValue("package", "weblogic.servlet.internal");
      String var3 = (new String("This MBean just hides the lazy construction of PageFlowRuntimeMBeans and provides a top-level reset() operation for all PageFlows in the webapp. It also minimizes the impact on the WebAppRuntimeMBean.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.PageFlowsRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AppName")) {
         var3 = "getAppName";
         var4 = null;
         var2 = new PropertyDescriptor("AppName", PageFlowsRuntimeMBean.class, var3, (String)var4);
         var1.put("AppName", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("ContextPath")) {
         var3 = "getContextPath";
         var4 = null;
         var2 = new PropertyDescriptor("ContextPath", PageFlowsRuntimeMBean.class, var3, (String)var4);
         var1.put("ContextPath", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("HttpServerName")) {
         var3 = "getHttpServerName";
         var4 = null;
         var2 = new PropertyDescriptor("HttpServerName", PageFlowsRuntimeMBean.class, var3, (String)var4);
         var1.put("HttpServerName", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("PageFlows")) {
         var3 = "getPageFlows";
         var4 = null;
         var2 = new PropertyDescriptor("PageFlows", PageFlowsRuntimeMBean.class, var3, (String)var4);
         var1.put("PageFlows", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("ServerName")) {
         var3 = "getServerName";
         var4 = null;
         var2 = new PropertyDescriptor("ServerName", PageFlowsRuntimeMBean.class, var3, (String)var4);
         var1.put("ServerName", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
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
      Method var3 = PageFlowsRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = PageFlowsRuntimeMBean.class.getMethod("getPageFlow", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("pageFlowClassName", "the classname of the target pageflow ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", " ");
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = PageFlowsRuntimeMBean.class.getMethod("reset");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "Reset counters for this PageFlow type and all actions. ");
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

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
