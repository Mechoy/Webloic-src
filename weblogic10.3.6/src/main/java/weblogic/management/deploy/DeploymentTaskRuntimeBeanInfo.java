package weblogic.management.deploy;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.management.runtime.TaskRuntimeMBeanImplBeanInfo;

public class DeploymentTaskRuntimeBeanInfo extends TaskRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = DeploymentTaskRuntimeMBean.class;

   public DeploymentTaskRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DeploymentTaskRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DeploymentTaskRuntime.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.deploy.api.spi.WebLogicDeploymentManager} ");
      var2.setValue("package", "weblogic.management.deploy");
      String var3 = (new String("Base interface for deployment task MBeans. These MBeans track the progress of a deployment task.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.DeploymentTaskRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ApplicationName")) {
         var3 = "getApplicationName";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationName", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("ApplicationName", var2);
         var2.setValue("description", "<p>The name for the application that was specified to DeployerRuntime.activate</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ApplicationVersionIdentifier")) {
         var3 = "getApplicationVersionIdentifier";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationVersionIdentifier", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("ApplicationVersionIdentifier", var2);
         var2.setValue("description", "Returns the associated AppDeploymentMBean's VersionIdentifier. This is valid only for AppDeploymentMBean or LibraryMBean and for all other for all other types of BasicDeploymentMBeans, it will return null. And also if there is no BasicDeploymentMBean associated with this task, it will return null. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("BeginTime")) {
         var3 = "getBeginTime";
         var4 = null;
         var2 = new PropertyDescriptor("BeginTime", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("BeginTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("CancelState")) {
         var3 = "getCancelState";
         var4 = null;
         var2 = new PropertyDescriptor("CancelState", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("CancelState", var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DeploymentData")) {
         var3 = "getDeploymentData";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentData", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("DeploymentData", var2);
         var2.setValue("description", "<p>Provides the data associated with this task</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("DeploymentMBean")) {
         var3 = "getDeploymentMBean";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentMBean", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("DeploymentMBean", var2);
         var2.setValue("description", " ");
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("DeploymentObject")) {
         var3 = "getDeploymentObject";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentObject", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("DeploymentObject", var2);
         var2.setValue("description", "<p>Lists the Application MBean involved in this task. This returns the Admin MBean, the one based on config.xml, that applies to all servers this application is associated with.</p> ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("Description")) {
         var3 = "getDescription";
         var4 = null;
         var2 = new PropertyDescriptor("Description", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("Description", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("EndTime")) {
         var3 = "getEndTime";
         var4 = null;
         var2 = new PropertyDescriptor("EndTime", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("EndTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("Error")) {
         var3 = "getError";
         var4 = null;
         var2 = new PropertyDescriptor("Error", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("Error", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("Id")) {
         var3 = "getId";
         var4 = null;
         var2 = new PropertyDescriptor("Id", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("Id", var2);
         var2.setValue("description", "<p>Provides a reference id assigned to a task.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("NotificationLevel")) {
         var3 = "getNotificationLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNotificationLevel";
         }

         var2 = new PropertyDescriptor("NotificationLevel", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("NotificationLevel", var2);
         var2.setValue("description", "<p>Provides the notification level applied to this task.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowedSet", var5);
      }

      if (!var1.containsKey("ParentTask")) {
         var3 = "getParentTask";
         var4 = null;
         var2 = new PropertyDescriptor("ParentTask", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("ParentTask", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Source")) {
         var3 = "getSource";
         var4 = null;
         var2 = new PropertyDescriptor("Source", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("Source", var2);
         var2.setValue("description", "<p>Provides the name of the source file that was specified to DeployerRuntime.activate.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("State")) {
         var3 = "getState";
         var4 = null;
         var2 = new PropertyDescriptor("State", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("State", var2);
         var2.setValue("description", "<p>Provides notice of the overall state of this task.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("Status")) {
         var3 = "getStatus";
         var4 = null;
         var2 = new PropertyDescriptor("Status", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("Status", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("SubTasks")) {
         var3 = "getSubTasks";
         var4 = null;
         var2 = new PropertyDescriptor("SubTasks", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("SubTasks", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         var2 = new PropertyDescriptor("Targets", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>Provides target based deployment status information for this deployment. For distributed deployment, there is one TargetStatus for each target</p> ");
      }

      if (!var1.containsKey("Task")) {
         var3 = "getTask";
         var4 = null;
         var2 = new PropertyDescriptor("Task", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("Task", var2);
         var2.setValue("description", "<p>Indicates a specific task associated with this MBean</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("TaskMessages")) {
         var3 = "getTaskMessages";
         var4 = null;
         var2 = new PropertyDescriptor("TaskMessages", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("TaskMessages", var2);
         var2.setValue("description", "<p>Provides an ordered list of messages generated for the task. Each member in the list is a String object.</p> ");
      }

      if (!var1.containsKey("InUse")) {
         var3 = "isInUse";
         var4 = null;
         var2 = new PropertyDescriptor("InUse", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("InUse", var2);
         var2.setValue("description", "<p>Indicates whether the MBean is free for deletion or timeout.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("NewSource")) {
         var3 = "isNewSource";
         var4 = null;
         var2 = new PropertyDescriptor("NewSource", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("NewSource", var2);
         var2.setValue("description", "Indicates whether a new source for the application was specified in an deployment request. ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("PendingActivation")) {
         var3 = "isPendingActivation";
         var4 = null;
         var2 = new PropertyDescriptor("PendingActivation", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("PendingActivation", var2);
         var2.setValue("description", "Tells us if the task in a pending state due to it being requested while a non-exclusive lock is held on the domain configuration. The task will be processed only after the session is activated ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("Retired")) {
         var3 = "isRetired";
         var4 = null;
         var2 = new PropertyDescriptor("Retired", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("Retired", var2);
         var2.setValue("description", "Tells us if the task is retired after completion of the task. This will allow Deployer tool to remove it when user makes purgeTasks call. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("Running")) {
         var3 = "isRunning";
         var4 = null;
         var2 = new PropertyDescriptor("Running", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("Running", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("SystemTask")) {
         var3 = "isSystemTask";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSystemTask";
         }

         var2 = new PropertyDescriptor("SystemTask", DeploymentTaskRuntimeMBean.class, var3, var4);
         var1.put("SystemTask", var2);
         var2.setValue("description", " ");
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
      Method var3 = DeploymentTaskRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = DeploymentTaskRuntimeMBean.class.getMethod("findTarget", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("target", "is the name of a target (server or cluster name) ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Lists the status for a specific target of this deployment.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeploymentTaskRuntimeMBean.class.getMethod("cancel");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Attempts to cancel the task. Any actions which have yet to start will be inhibited. Any completed actions will remain in place.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeploymentTaskRuntimeMBean.class.getMethod("printLog", PrintWriter.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = DeploymentTaskRuntimeMBean.class.getMethod("start");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Initiates the request. When invoking {@link DeployerRuntimeMBean#activate(String, String, String, DeploymentData, String, boolean)}, {@link DeployerRuntimeMBean#deactivate(String, DeploymentData, String, boolean)}, {@link DeployerRuntimeMBean#unprepare(String, DeploymentData, String, boolean)}, or {@link DeployerRuntimeMBean#remove(String, DeploymentData, String, boolean)} with the startTask option set to false, this method is used to initiate the task. throws ManagementException if task is already started or any failures occur during task processing.</p> ");
         var2.setValue("role", "operation");
         String[] var7 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var7);
      }

      var3 = DeploymentTaskRuntimeMBean.class.getMethod("waitForTaskCompletion", Long.TYPE);
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
