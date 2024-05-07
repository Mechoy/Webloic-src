package weblogic.cluster.migration.management;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.MigrationTaskRuntimeMBean;
import weblogic.management.runtime.TaskRuntimeMBeanImplBeanInfo;

public class MigrationTaskBeanInfo extends TaskRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = MigrationTaskRuntimeMBean.class;

   public MigrationTaskBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MigrationTaskBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MigrationTask.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.cluster.migration.management");
      String var3 = (new String("<p>Exposes monitoring information about an ongoing and potentially long-running administrative process.  This should be taken to mean, at minimum, any OAM operation involving IO.  Examples include starting and stopping servers, deploying and undeploying applications, or migrating services.</p>  <p>An MBean operation method of this sort should place the actual work on an ExecuteQueue and immediately return an instance of TaskRuntimeMBean to the caller.  The caller can then use this to track the task's progress as desired, and if appropriate, provide facilities for user interaction with the task, e.g. \"cancel\" or \"continue anyway.\"  OA&M clients can also query for all instances of TaskRuntimeMBean to get a summary of both currently-running and recently-completed tasks.</p> <p>Instance of TaskRuntimeMBean continue to exist in the MBeanServer after the completion of the work they describe.  They will eventually either be explicitly deregistered by an OA&M client, or removed by a scavenger process which periodically purges TaskRuntimeMBeans that have been completed for some time.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.MigrationTaskRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("BeginTime")) {
         var3 = "getBeginTime";
         var4 = null;
         var2 = new PropertyDescriptor("BeginTime", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("BeginTime", var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Description")) {
         var3 = "getDescription";
         var4 = null;
         var2 = new PropertyDescriptor("Description", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("Description", var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DestinationServer")) {
         var3 = "getDestinationServer";
         var4 = null;
         var2 = new PropertyDescriptor("DestinationServer", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("DestinationServer", var2);
         var2.setValue("description", "<p>Provides the identity of the server to which the migration is moving.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("EndTime")) {
         var3 = "getEndTime";
         var4 = null;
         var2 = new PropertyDescriptor("EndTime", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("EndTime", var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Error")) {
         var3 = "getError";
         var4 = null;
         var2 = new PropertyDescriptor("Error", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("Error", var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MigratableTarget")) {
         var3 = "getMigratableTarget";
         var4 = null;
         var2 = new PropertyDescriptor("MigratableTarget", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("MigratableTarget", var2);
         var2.setValue("description", "<p>Provides the MigratableTarget object that is being migrated.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ParentTask")) {
         var3 = "getParentTask";
         var4 = null;
         var2 = new PropertyDescriptor("ParentTask", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("ParentTask", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SourceServer")) {
         var3 = "getSourceServer";
         var4 = null;
         var2 = new PropertyDescriptor("SourceServer", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("SourceServer", var2);
         var2.setValue("description", "<p>Provides the identity of the server from which the migration is moving.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Status")) {
         var3 = "getStatus";
         var4 = null;
         var2 = new PropertyDescriptor("Status", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("Status", var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("StatusCode")) {
         var3 = "getStatusCode";
         var4 = null;
         var2 = new PropertyDescriptor("StatusCode", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("StatusCode", var2);
         var2.setValue("description", "<p>Provides an int describing the status of this Task.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SubTasks")) {
         var3 = "getSubTasks";
         var4 = null;
         var2 = new PropertyDescriptor("SubTasks", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("SubTasks", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("JTA")) {
         var3 = "isJTA";
         var4 = null;
         var2 = new PropertyDescriptor("JTA", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("JTA", var2);
         var2.setValue("description", "<p>Indicates whether the migration task moves a JTA recovery manager.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Running")) {
         var3 = "isRunning";
         var4 = null;
         var2 = new PropertyDescriptor("Running", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("Running", var2);
         var2.setValue("description", "<p>Indicates whether the Task is still running.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SystemTask")) {
         var3 = "isSystemTask";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSystemTask";
         }

         var2 = new PropertyDescriptor("SystemTask", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("SystemTask", var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Terminal")) {
         var3 = "isTerminal";
         var4 = null;
         var2 = new PropertyDescriptor("Terminal", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("Terminal", var2);
         var2.setValue("description", "<p>Indicates whether the Task was successful, failed or was canceled.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("WaitingForUser")) {
         var3 = "isWaitingForUser";
         var4 = null;
         var2 = new PropertyDescriptor("WaitingForUser", MigrationTaskRuntimeMBean.class, var3, var4);
         var1.put("WaitingForUser", var2);
         var2.setValue("description", "<p>Indicates whether the Task is waiting for user input.</p> ");
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
      Method var3 = MigrationTaskRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = MigrationTaskRuntimeMBean.class.getMethod("continueWithSourceServerDown", Boolean.TYPE);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("isSourceServerDown", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Provides the user input to continue if the task is in the wait state STATUS_AWAITING_IS_SOURCE_DOWN.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = MigrationTaskRuntimeMBean.class.getMethod("continueWithDestinationServerDown", Boolean.TYPE);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("isDestinationServerDown", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Provides the user input to continue if the task is in the wait state STATUS_AWAITING_IS_DESINATION_DOWN.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = MigrationTaskRuntimeMBean.class.getMethod("cancel");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = MigrationTaskRuntimeMBean.class.getMethod("printLog", PrintWriter.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
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
