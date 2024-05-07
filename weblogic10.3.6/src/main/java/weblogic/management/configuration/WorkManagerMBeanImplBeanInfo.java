package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WorkManagerMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WorkManagerMBean.class;

   public WorkManagerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WorkManagerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WorkManagerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Configuration MBean representing WorkManager parameters. A WorkManager configuration can have a RequestClass(FairShare, ResponseTime, ContextBased), MinThreadsConstraint, MaxThreadsConstraint, Capacity and ShutdownTrigger. All these are optional and need to be configured as needed. An empty WorkManager without configuration gets its own default fair share. The default fair share value is 50. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WorkManagerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      String[] var5;
      if (!var1.containsKey("Capacity")) {
         var3 = "getCapacity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCapacity";
         }

         var2 = new PropertyDescriptor("Capacity", WorkManagerMBean.class, var3, var4);
         var1.put("Capacity", var2);
         var2.setValue("description", "<p>The total number of requests that can be queued or executing before WebLogic Server begins rejecting requests.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("CapacityMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("ContextRequestClass")) {
         var3 = "getContextRequestClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setContextRequestClass";
         }

         var2 = new PropertyDescriptor("ContextRequestClass", WorkManagerMBean.class, var3, var4);
         var1.put("ContextRequestClass", var2);
         var2.setValue("description", "<p>The mapping of Request Classes to security names and groups.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("ContextRequestClassMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("FairShareRequestClass")) {
         var3 = "getFairShareRequestClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFairShareRequestClass";
         }

         var2 = new PropertyDescriptor("FairShareRequestClass", WorkManagerMBean.class, var3, var4);
         var1.put("FairShareRequestClass", var2);
         var2.setValue("description", "<p>Get the FairShareRequestClass for this WorkManager</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("FairShareRequestClassMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("IgnoreStuckThreads")) {
         var3 = "getIgnoreStuckThreads";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIgnoreStuckThreads";
         }

         var2 = new PropertyDescriptor("IgnoreStuckThreads", WorkManagerMBean.class, var3, var4);
         var1.put("IgnoreStuckThreads", var2);
         var2.setValue("description", "<p>Specifies whether this Work Manager ignores \"stuck\" threads. Typically, stuck threads will cause the associated Work Manager to take some action: either switching the application to Admin mode, shutting down the server, or shutting down the Work Manager. If this flag is set, then no thread in this Work Manager is ever considered stuck. </p> <p>If you do not explicitly specify IGNORE_STUCK_THREADS=TRUE, the default behavior is that upon encountering stuck threads, the server will take one of the aforementioned actions. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("WorkManagerShutdownTriggerMBean")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("MaxThreadsConstraint")) {
         var3 = "getMaxThreadsConstraint";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxThreadsConstraint";
         }

         var2 = new PropertyDescriptor("MaxThreadsConstraint", WorkManagerMBean.class, var3, var4);
         var1.put("MaxThreadsConstraint", var2);
         var2.setValue("description", "<p>The maximum number of concurrent threads that can be allocated to execute requests.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("MaxThreadsConstraintMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("MinThreadsConstraint")) {
         var3 = "getMinThreadsConstraint";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMinThreadsConstraint";
         }

         var2 = new PropertyDescriptor("MinThreadsConstraint", WorkManagerMBean.class, var3, var4);
         var1.put("MinThreadsConstraint", var2);
         var2.setValue("description", "<p>The minimum number of threads allocated to resolve deadlocks.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("MinThreadsConstraintMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("ResponseTimeRequestClass")) {
         var3 = "getResponseTimeRequestClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setResponseTimeRequestClass";
         }

         var2 = new PropertyDescriptor("ResponseTimeRequestClass", WorkManagerMBean.class, var3, var4);
         var1.put("ResponseTimeRequestClass", var2);
         var2.setValue("description", "<p>The response time goal (in milliseconds).</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("ResponseTimeRequestClassMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WorkManagerShutdownTrigger")) {
         var3 = "getWorkManagerShutdownTrigger";
         var4 = null;
         var2 = new PropertyDescriptor("WorkManagerShutdownTrigger", WorkManagerMBean.class, var3, var4);
         var1.put("WorkManagerShutdownTrigger", var2);
         var2.setValue("description", "<p>Configure a shutdown trigger for this WorkManager. Specifies the condition to be used to shutdown the WorkManager. The Server health monitoring periodically checks to see if the conidtion is met and shuts down the work manager if needed.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWorkManagerShutdownTrigger");
         var2.setValue("creator", "createWorkManagerShutdownTrigger");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion)) {
         var3 = WorkManagerMBean.class.getMethod("createWorkManagerShutdownTrigger");
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "10.3.3.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>Configure the shutdown trigger for the WorkManager.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WorkManagerShutdownTrigger");
            var2.setValue("since", "10.3.3.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion)) {
         var3 = WorkManagerMBean.class.getMethod("destroyWorkManagerShutdownTrigger");
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "10.3.3.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>Remove the configured shutdown trigger for the WorkManager.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WorkManagerShutdownTrigger");
            var2.setValue("since", "10.3.3.0");
         }
      }

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
