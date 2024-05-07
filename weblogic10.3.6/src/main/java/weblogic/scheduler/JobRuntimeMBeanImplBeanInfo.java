package weblogic.scheduler;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JobRuntimeMBean;

public class JobRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JobRuntimeMBean.class;

   public JobRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JobRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JobRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.scheduler");
      String var3 = (new String("RuntimeMBean that provides information about a particular job. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JobRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("Description")) {
         var3 = "getDescription";
         var4 = null;
         var2 = new PropertyDescriptor("Description", JobRuntimeMBean.class, var3, (String)var4);
         var1.put("Description", var2);
         var2.setValue("description", "Get the description of the submitted <code>commonj.timers.TimerListener</code>. Returns <code>commonj.timers.TimerListener#toString</code>. ");
      }

      if (!var1.containsKey("ID")) {
         var3 = "getID";
         var4 = null;
         var2 = new PropertyDescriptor("ID", JobRuntimeMBean.class, var3, (String)var4);
         var1.put("ID", var2);
         var2.setValue("description", "The unique ID corresponding to this job ");
      }

      if (!var1.containsKey("LastLocalExecutionTime")) {
         var3 = "getLastLocalExecutionTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastLocalExecutionTime", JobRuntimeMBean.class, var3, (String)var4);
         var1.put("LastLocalExecutionTime", var2);
         var2.setValue("description", "Returns the most recent execution time of this job in the local server. Note that multiple executions of the same job are load-balanced across the cluster and this time indicates when the job was last executed locally. ");
      }

      if (!var1.containsKey("LocalExecutionCount")) {
         var3 = "getLocalExecutionCount";
         var4 = null;
         var2 = new PropertyDescriptor("LocalExecutionCount", JobRuntimeMBean.class, var3, (String)var4);
         var1.put("LocalExecutionCount", var2);
         var2.setValue("description", "Returns the number of times this job was executed locally. Job executions are load-balanced across the cluster. This count specifies the number of executions of the job in the local server. ");
      }

      if (!var1.containsKey("Period")) {
         var3 = "getPeriod";
         var4 = null;
         var2 = new PropertyDescriptor("Period", JobRuntimeMBean.class, var3, (String)var4);
         var1.put("Period", var2);
         var2.setValue("description", "Returns the specified periodicity of this job ");
      }

      if (!var1.containsKey("State")) {
         var3 = "getState";
         var4 = null;
         var2 = new PropertyDescriptor("State", JobRuntimeMBean.class, var3, (String)var4);
         var1.put("State", var2);
         var2.setValue("description", "Returns the state of the task. A Job is either in running state or in cancelled state ");
      }

      if (!var1.containsKey("Timeout")) {
         var3 = "getTimeout";
         var4 = null;
         var2 = new PropertyDescriptor("Timeout", JobRuntimeMBean.class, var3, (String)var4);
         var1.put("Timeout", var2);
         var2.setValue("description", "Returns when the job will be executed next ");
      }

      if (!var1.containsKey("TimerListener")) {
         var3 = "getTimerListener";
         var4 = null;
         var2 = new PropertyDescriptor("TimerListener", JobRuntimeMBean.class, var3, (String)var4);
         var1.put("TimerListener", var2);
         var2.setValue("description", "Get the {@link commonj.timers.TimerListener} associated with this job. This call involves a database roundtrip and should be used too frequently. ");
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
      Method var3 = JobRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JobRuntimeMBean.class.getMethod("cancel");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Cancel this job and prevent it from executing again in any server, not just this server. ");
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
