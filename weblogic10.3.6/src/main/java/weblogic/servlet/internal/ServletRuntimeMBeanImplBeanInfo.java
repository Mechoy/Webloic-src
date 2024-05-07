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
import weblogic.management.runtime.ServletRuntimeMBean;

public class ServletRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ServletRuntimeMBean.class;

   public ServletRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServletRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServletRuntimeMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.servlet.internal");
      String var3 = (new String("Describes a servlet.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ServletRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ContextPath")) {
         var3 = "getContextPath";
         var4 = null;
         var2 = new PropertyDescriptor("ContextPath", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("ContextPath", var2);
         var2.setValue("description", "<p>Provides the context path for this servlet.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ExecutionTimeAverage")) {
         var3 = "getExecutionTimeAverage";
         var4 = null;
         var2 = new PropertyDescriptor("ExecutionTimeAverage", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecutionTimeAverage", var2);
         var2.setValue("description", "<p>Provides the average amount of time all invocations of the servlet have executed since created.</p> ");
      }

      if (!var1.containsKey("ExecutionTimeHigh")) {
         var3 = "getExecutionTimeHigh";
         var4 = null;
         var2 = new PropertyDescriptor("ExecutionTimeHigh", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecutionTimeHigh", var2);
         var2.setValue("description", "<p>Provides the amount of time the single longest invocation of the servlet has executed since created.</p> ");
      }

      if (!var1.containsKey("ExecutionTimeLow")) {
         var3 = "getExecutionTimeLow";
         var4 = null;
         var2 = new PropertyDescriptor("ExecutionTimeLow", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecutionTimeLow", var2);
         var2.setValue("description", "<p>Provides the amount of time the single shortest invocation of the servlet has executed since created. Note that for the CounterMonitor, the difference option must be used.</p> ");
      }

      if (!var1.containsKey("ExecutionTimeTotal")) {
         var3 = "getExecutionTimeTotal";
         var4 = null;
         var2 = new PropertyDescriptor("ExecutionTimeTotal", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecutionTimeTotal", var2);
         var2.setValue("description", "<p>Provides the total amount of time all invocations of the servlet have executed since created.</p> ");
      }

      if (!var1.containsKey("InvocationTotalCount")) {
         var3 = "getInvocationTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("InvocationTotalCount", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("InvocationTotalCount", var2);
         var2.setValue("description", "<p>Provides a total count of the times this servlet has been invoked.</p> ");
      }

      if (!var1.containsKey("PoolMaxCapacity")) {
         var3 = "getPoolMaxCapacity";
         var4 = null;
         var2 = new PropertyDescriptor("PoolMaxCapacity", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolMaxCapacity", var2);
         var2.setValue("description", "<p>Provides the maximum capacity of this servlet for single thread model servlets.</p> ");
      }

      if (!var1.containsKey("ReloadTotalCount")) {
         var3 = "getReloadTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ReloadTotalCount", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("ReloadTotalCount", var2);
         var2.setValue("description", "<p>Provides a total count of the number of times this servlet has been reloaded.</p> ");
      }

      if (!var1.containsKey("ServletClassName")) {
         var3 = "getServletClassName";
         var4 = null;
         var2 = new PropertyDescriptor("ServletClassName", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("ServletClassName", var2);
         var2.setValue("description", "<p>Provides the servlet class name</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ServletName")) {
         var3 = "getServletName";
         var4 = null;
         var2 = new PropertyDescriptor("ServletName", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("ServletName", var2);
         var2.setValue("description", "<p>Provides the name of this instance of a servlet.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ServletPath")) {
         var3 = "getServletPath";
         var4 = null;
         var2 = new PropertyDescriptor("ServletPath", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("ServletPath", var2);
         var2.setValue("description", "<p>Provides the servlet path.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("URL")) {
         var3 = "getURL";
         var4 = null;
         var2 = new PropertyDescriptor("URL", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("URL", var2);
         var2.setValue("description", "<p>Provides the value of the URL for this servlet.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("URLPatterns")) {
         var3 = "getURLPatterns";
         var4 = null;
         var2 = new PropertyDescriptor("URLPatterns", ServletRuntimeMBean.class, var3, (String)var4);
         var1.put("URLPatterns", var2);
         var2.setValue("description", "<p>Provides a description of the URL patterns for this servlet.</p> ");
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
      Method var3 = ServletRuntimeMBean.class.getMethod("preDeregister");
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
