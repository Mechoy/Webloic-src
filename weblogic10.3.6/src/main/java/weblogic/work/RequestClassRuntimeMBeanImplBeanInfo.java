package weblogic.work;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.RequestClassRuntimeMBean;

public class RequestClassRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = RequestClassRuntimeMBean.class;

   public RequestClassRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public RequestClassRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = RequestClassRuntimeMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.work");
      String var3 = (new String("RequestClassRuntimeMBean presents runtime information about RequestClasses. A request class represents a class of work. Work using the same request class shares the same priority.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.RequestClassRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("CompletedCount")) {
         var3 = "getCompletedCount";
         var4 = null;
         var2 = new PropertyDescriptor("CompletedCount", RequestClassRuntimeMBean.class, var3, (String)var4);
         var1.put("CompletedCount", var2);
         var2.setValue("description", "Total number of completions since server start ");
      }

      if (!var1.containsKey("DeltaFirst")) {
         var3 = "getDeltaFirst";
         var4 = null;
         var2 = new PropertyDescriptor("DeltaFirst", RequestClassRuntimeMBean.class, var3, (String)var4);
         var1.put("DeltaFirst", var2);
         var2.setValue("description", "Undocumented attribute that exposes a value used in determining priority ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DeltaRepeat")) {
         var3 = "getDeltaRepeat";
         var4 = null;
         var2 = new PropertyDescriptor("DeltaRepeat", RequestClassRuntimeMBean.class, var3, (String)var4);
         var1.put("DeltaRepeat", var2);
         var2.setValue("description", "Undocumented attribute that exposes a value used in determining priority ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Interval")) {
         var3 = "getInterval";
         var4 = null;
         var2 = new PropertyDescriptor("Interval", RequestClassRuntimeMBean.class, var3, (String)var4);
         var1.put("Interval", var2);
         var2.setValue("description", "Undocumented attribute that exposes a value used in determining priority. This attribute is applicable only for ResponseTimeRequestClass. -1 is returned for FairShareRequestClasses. ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MyLast")) {
         var3 = "getMyLast";
         var4 = null;
         var2 = new PropertyDescriptor("MyLast", RequestClassRuntimeMBean.class, var3, (String)var4);
         var1.put("MyLast", var2);
         var2.setValue("description", "Undocumented attribute that exposes a value used in determining priority ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PendingRequestCount")) {
         var3 = "getPendingRequestCount";
         var4 = null;
         var2 = new PropertyDescriptor("PendingRequestCount", RequestClassRuntimeMBean.class, var3, (String)var4);
         var1.put("PendingRequestCount", var2);
         var2.setValue("description", "Number of requests waiting for a thread to become available. ");
      }

      if (!var1.containsKey("RequestClassType")) {
         var3 = "getRequestClassType";
         var4 = null;
         var2 = new PropertyDescriptor("RequestClassType", RequestClassRuntimeMBean.class, var3, (String)var4);
         var1.put("RequestClassType", var2);
         var2.setValue("description", "Returns the type of RequestClass. Either <code>FAIR_SHARE</code> or <code>RESPONSE_TIME</code> or <code>CONTEXT</code> ");
      }

      if (!var1.containsKey("ThreadUseSquares")) {
         var3 = "getThreadUseSquares";
         var4 = null;
         var2 = new PropertyDescriptor("ThreadUseSquares", RequestClassRuntimeMBean.class, var3, (String)var4);
         var1.put("ThreadUseSquares", var2);
         var2.setValue("description", "Undocumented attribute that exposes a value used in determining priority ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("TotalThreadUse")) {
         var3 = "getTotalThreadUse";
         var4 = null;
         var2 = new PropertyDescriptor("TotalThreadUse", RequestClassRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalThreadUse", var2);
         var2.setValue("description", "Total amount of thread use time in millisec's used by the request class since server start. ");
      }

      if (!var1.containsKey("VirtualTimeIncrement")) {
         var3 = "getVirtualTimeIncrement";
         var4 = null;
         var2 = new PropertyDescriptor("VirtualTimeIncrement", RequestClassRuntimeMBean.class, var3, (String)var4);
         var1.put("VirtualTimeIncrement", var2);
         var2.setValue("description", "Current priority of the request class. The priority is relative to other request class priorities. The priority is calculated dynamically frequently and can change. ");
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
      Method var3 = RequestClassRuntimeMBean.class.getMethod("preDeregister");
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
