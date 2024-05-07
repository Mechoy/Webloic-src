package weblogic.diagnostics.instrumentation;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WLDFInstrumentationRuntimeMBean;

public class InstrumentationRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFInstrumentationRuntimeMBean.class;

   public InstrumentationRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public InstrumentationRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = InstrumentationRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.instrumentation");
      String var3 = (new String("<p>This interface defines various methods for accessing runtime information about the diagnostic instrumentation system.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WLDFInstrumentationRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("CallJoinpointCount")) {
         var3 = "getCallJoinpointCount";
         var4 = null;
         var2 = new PropertyDescriptor("CallJoinpointCount", WLDFInstrumentationRuntimeMBean.class, var3, (String)var4);
         var1.put("CallJoinpointCount", var2);
         var2.setValue("description", "<p>The number of affected CALL joinpoints for all classes that were inspected. (CALL joinpoints are on the caller side.)</p> ");
      }

      if (!var1.containsKey("ClassweaveAbortCount")) {
         var3 = "getClassweaveAbortCount";
         var4 = null;
         var2 = new PropertyDescriptor("ClassweaveAbortCount", WLDFInstrumentationRuntimeMBean.class, var3, (String)var4);
         var1.put("ClassweaveAbortCount", var2);
         var2.setValue("description", "<p>Number of classes for which the class weaving aborted with some exceptional situation</p> ");
      }

      if (!var1.containsKey("ExecutionJoinpointCount")) {
         var3 = "getExecutionJoinpointCount";
         var4 = null;
         var2 = new PropertyDescriptor("ExecutionJoinpointCount", WLDFInstrumentationRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecutionJoinpointCount", var2);
         var2.setValue("description", "<p>The number of affected EXECUTION joinpoints for all classes that were inspected. (EXECUTION joinpoints are on the callee side.)</p> ");
      }

      if (!var1.containsKey("InspectedClassesCount")) {
         var3 = "getInspectedClassesCount";
         var4 = null;
         var2 = new PropertyDescriptor("InspectedClassesCount", WLDFInstrumentationRuntimeMBean.class, var3, (String)var4);
         var1.put("InspectedClassesCount", var2);
         var2.setValue("description", "<p>The number of classes inspected for weaving (weaving is the insertion of diagnostic code).</p> ");
      }

      if (!var1.containsKey("MaxWeavingTime")) {
         var3 = "getMaxWeavingTime";
         var4 = null;
         var2 = new PropertyDescriptor("MaxWeavingTime", WLDFInstrumentationRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxWeavingTime", var2);
         var2.setValue("description", "<p>For all classes, the weaving time in nanoseconds for the class that required the most time to process (includes the time spent both for inspection and for modification).</p> ");
      }

      if (!var1.containsKey("MethodInvocationStatistics")) {
         var3 = "getMethodInvocationStatistics";
         var4 = null;
         var2 = new PropertyDescriptor("MethodInvocationStatistics", WLDFInstrumentationRuntimeMBean.class, var3, (String)var4);
         var1.put("MethodInvocationStatistics", var2);
         var2.setValue("description", "<p>Map containing the method invocation statistics for this scope. It is a nested Map structure. The first level Map is keyed by the fully qualified class names within the instrumentation scope. It yields another Map containing the method data within an instrumented class. The method data Map is keyed by the method name and it yields another Map structure that is keyed by the method signatures. Method signature key is represented by a comma separated list of the input parameters. Each method signature key's value is the ultimate statistics Map object that contains entries with predefined keys: count, min, max, avg, sum, sum_of_squares, and std_deviation. The value for these keys indicate the associated metric.</p>  <p>When specifying this attribute as part of a variable within a WLDF Watch rule expression, you must explicitly declare the WLDFInstrumentationRuntime type. Otherwise, the system can't determine the type when validating the attribute expression, and the expression won't work.</p> ");
         var2.setValue("harvesterAttributeNormalizerClass", "weblogic.diagnostics.instrumentation.HarvesterAttributeNormalizer");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3", (String)null, this.targetVersion) && !var1.containsKey("MethodMemoryAllocationStatistics")) {
         var3 = "getMethodMemoryAllocationStatistics";
         var4 = null;
         var2 = new PropertyDescriptor("MethodMemoryAllocationStatistics", WLDFInstrumentationRuntimeMBean.class, var3, (String)var4);
         var1.put("MethodMemoryAllocationStatistics", var2);
         var2.setValue("description", "<p>Map containing the method memory allocation statistics for this scope. It is a nested Map structure. The first level Map is keyed by the fully qualified class names within the instrumentation scope. It yields another Map containing the method data within an instrumented class. The method data Map is keyed by the method name and it yields another Map structure that is keyed by the method signatures. Method signature key is represented by a comma separated list of the input parameters. Each method signature key's value is the ultimate statistics Map object that contains entries with predefined keys: count, min, max, avg, sum, sum_of_squares, and std_deviation. The value for these keys indicate the associated metric.</p>  <p>When specifying this attribute as part of a variable within a WLDF Watch rule expression, you must explicitly declare the WLDFInstrumentationRuntime type. Otherwise, the system can't determine the type when validating the attribute expression, and the expression won't work.</p> ");
         var2.setValue("since", "10.3.3");
         var2.setValue("harvesterAttributeNormalizerClass", "weblogic.diagnostics.instrumentation.HarvesterAttributeNormalizer");
      }

      if (!var1.containsKey("MinWeavingTime")) {
         var3 = "getMinWeavingTime";
         var4 = null;
         var2 = new PropertyDescriptor("MinWeavingTime", WLDFInstrumentationRuntimeMBean.class, var3, (String)var4);
         var1.put("MinWeavingTime", var2);
         var2.setValue("description", "<p>For all classes, the weaving time in nanoseconds for the class that required the least time to process (includes the time spent both for inspection and for modification).</p> ");
      }

      if (!var1.containsKey("ModifiedClassesCount")) {
         var3 = "getModifiedClassesCount";
         var4 = null;
         var2 = new PropertyDescriptor("ModifiedClassesCount", WLDFInstrumentationRuntimeMBean.class, var3, (String)var4);
         var1.put("ModifiedClassesCount", var2);
         var2.setValue("description", "<p>The number of modified classes (classes where diagnostic code has been inserted).</p> ");
      }

      if (!var1.containsKey("TotalWeavingTime")) {
         var3 = "getTotalWeavingTime";
         var4 = null;
         var2 = new PropertyDescriptor("TotalWeavingTime", WLDFInstrumentationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalWeavingTime", var2);
         var2.setValue("description", "<p>For all classes, the total weaving time in nanoseconds for processing (includes the time spent both for inspection and for modification).</p> ");
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
      Method var3 = WLDFInstrumentationRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFInstrumentationRuntimeMBean.class.getMethod("getMethodInvocationStatisticsData", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("expr", "Expression conforming to the harvester syntax for the MethodInvocationStatistics property without the attribute name prefix. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Drills down into the nested MethodInvocationStatistics Map structure and returns the object at the specified level. ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFInstrumentationRuntimeMBean.class.getMethod("resetMethodInvocationStatisticsData", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("expr", "Expression conforming to the harvester syntax for the MethodInvocationStatistics property without the attribute name prefix. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Resets the nested MethodInvocationStatistics structure and reinitializes the underlying metrics. ");
         var2.setValue("role", "operation");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3", (String)null, this.targetVersion)) {
         var3 = WLDFInstrumentationRuntimeMBean.class.getMethod("getMethodMemoryAllocationStatisticsData", String.class);
         var6 = new ParameterDescriptor[]{createParameterDescriptor("expr", "Expression conforming to the harvester syntax for the MethodMemoryAllocationStatistics property without the attribute name prefix. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var6);
            var2.setValue("since", "10.3.3");
            var1.put(var5, var2);
            var2.setValue("description", "Drills down into the nested MethodMemoryAllocationStatistics Map structure and returns the object at the specified level. ");
            var2.setValue("role", "operation");
            var2.setValue("since", "10.3.3");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3", (String)null, this.targetVersion)) {
         var3 = WLDFInstrumentationRuntimeMBean.class.getMethod("resetMethodMemoryAllocationStatisticsData", String.class);
         var6 = new ParameterDescriptor[]{createParameterDescriptor("expr", "Expression conforming to the harvester syntax for the MethodMemoryAllocationStatistics property without the attribute name prefix. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var6);
            var2.setValue("since", "10.3.3");
            var1.put(var5, var2);
            var2.setValue("description", "Resets the nested MethodMemoryAllocationStatistics structure and reinitializes the underlying metrics. ");
            var2.setValue("role", "operation");
            var2.setValue("since", "10.3.3");
         }
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
