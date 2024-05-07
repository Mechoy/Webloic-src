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
import weblogic.management.runtime.PageFlowActionRuntimeMBean;

public class PageFlowActionRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = PageFlowActionRuntimeMBean.class;

   public PageFlowActionRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public PageFlowActionRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = PageFlowActionRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.1.0");
      var2.setValue("package", "weblogic.servlet.internal");
      String var3 = (new String("This MBean represents the statistics for a single action of a PageFlow  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.PageFlowActionRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActionName")) {
         var3 = "getActionName";
         var4 = null;
         var2 = new PropertyDescriptor("ActionName", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("ActionName", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", "<p>Returns the name of the Action being described.</p> ");
      }

      if (!var1.containsKey("ExceptionCount")) {
         var3 = "getExceptionCount";
         var4 = null;
         var2 = new PropertyDescriptor("ExceptionCount", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("ExceptionCount", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("HandledExceptionCount")) {
         var3 = "getHandledExceptionCount";
         var4 = null;
         var2 = new PropertyDescriptor("HandledExceptionCount", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("HandledExceptionCount", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("HandledExceptionDispatchTimeAverage")) {
         var3 = "getHandledExceptionDispatchTimeAverage";
         var4 = null;
         var2 = new PropertyDescriptor("HandledExceptionDispatchTimeAverage", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("HandledExceptionDispatchTimeAverage", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("HandledExceptionDispatchTimeHigh")) {
         var3 = "getHandledExceptionDispatchTimeHigh";
         var4 = null;
         var2 = new PropertyDescriptor("HandledExceptionDispatchTimeHigh", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("HandledExceptionDispatchTimeHigh", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("HandledExceptionDispatchTimeLow")) {
         var3 = "getHandledExceptionDispatchTimeLow";
         var4 = null;
         var2 = new PropertyDescriptor("HandledExceptionDispatchTimeLow", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("HandledExceptionDispatchTimeLow", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("HandledExceptionDispatchTimeTotal")) {
         var3 = "getHandledExceptionDispatchTimeTotal";
         var4 = null;
         var2 = new PropertyDescriptor("HandledExceptionDispatchTimeTotal", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("HandledExceptionDispatchTimeTotal", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("LastExceptions")) {
         var3 = "getLastExceptions";
         var4 = null;
         var2 = new PropertyDescriptor("LastExceptions", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("LastExceptions", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("SuccessCount")) {
         var3 = "getSuccessCount";
         var4 = null;
         var2 = new PropertyDescriptor("SuccessCount", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("SuccessCount", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("SuccessDispatchTimeAverage")) {
         var3 = "getSuccessDispatchTimeAverage";
         var4 = null;
         var2 = new PropertyDescriptor("SuccessDispatchTimeAverage", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("SuccessDispatchTimeAverage", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("SuccessDispatchTimeHigh")) {
         var3 = "getSuccessDispatchTimeHigh";
         var4 = null;
         var2 = new PropertyDescriptor("SuccessDispatchTimeHigh", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("SuccessDispatchTimeHigh", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("SuccessDispatchTimeLow")) {
         var3 = "getSuccessDispatchTimeLow";
         var4 = null;
         var2 = new PropertyDescriptor("SuccessDispatchTimeLow", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("SuccessDispatchTimeLow", var2);
         var2.setValue("description", " ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("SuccessDispatchTimeTotal")) {
         var3 = "getSuccessDispatchTimeTotal";
         var4 = null;
         var2 = new PropertyDescriptor("SuccessDispatchTimeTotal", PageFlowActionRuntimeMBean.class, var3, (String)var4);
         var1.put("SuccessDispatchTimeTotal", var2);
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
      Method var3 = PageFlowActionRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
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
