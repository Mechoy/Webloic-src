package weblogic.time.server;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.TimeServiceRuntimeMBean;

public class TimerMBeanBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = TimeServiceRuntimeMBean.class;

   public TimerMBeanBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public TimerMBeanBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = TimerMBean.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.time.server");
      String var3 = (new String("This class is used for monitoring the WebLogic Time Service. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.TimeServiceRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ExceptionCount")) {
         var3 = "getExceptionCount";
         var4 = null;
         var2 = new PropertyDescriptor("ExceptionCount", TimeServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("ExceptionCount", var2);
         var2.setValue("description", "<p>Returns the total number of exceptions thrown while executing scheduled triggers.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ExecutionCount")) {
         var3 = "getExecutionCount";
         var4 = null;
         var2 = new PropertyDescriptor("ExecutionCount", TimeServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecutionCount", var2);
         var2.setValue("description", "<p>Returns the total number of triggers executed</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ExecutionsPerMinute")) {
         var3 = "getExecutionsPerMinute";
         var4 = null;
         var2 = new PropertyDescriptor("ExecutionsPerMinute", TimeServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecutionsPerMinute", var2);
         var2.setValue("description", "<p>Returns the average number of triggers executed per minute.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ScheduledTriggerCount")) {
         var3 = "getScheduledTriggerCount";
         var4 = null;
         var2 = new PropertyDescriptor("ScheduledTriggerCount", TimeServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("ScheduledTriggerCount", var2);
         var2.setValue("description", "<p>Returns the number of currently active scheduled triggers.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
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
      Method var3 = TimeServiceRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
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
