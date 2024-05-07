package weblogic.management.deploy.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.DeploymentProgressObjectMBean;

public class DeploymentProgressObjectImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = DeploymentProgressObjectMBean.class;

   public DeploymentProgressObjectImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DeploymentProgressObjectImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DeploymentProgressObjectImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.deploy.internal");
      String var3 = (new String("<p>This MBean is the user API for monitoring deployment operations and exists only on an Administration Server. Currently only start and stop operations initiated by {@link AppDeploymentRuntimeMBean} are supported.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.DeploymentProgressObjectMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AppDeploymentMBean")) {
         var3 = "getAppDeploymentMBean";
         var4 = null;
         var2 = new PropertyDescriptor("AppDeploymentMBean", DeploymentProgressObjectMBean.class, var3, (String)var4);
         var1.put("AppDeploymentMBean", var2);
         var2.setValue("description", "<p>The AppDeploymentMBean for the current deployment operation.</p> ");
      }

      if (!var1.containsKey("ApplicationName")) {
         var3 = "getApplicationName";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationName", DeploymentProgressObjectMBean.class, var3, (String)var4);
         var1.put("ApplicationName", var2);
         var2.setValue("description", "<p>The name of the application for the current deployment operation.</p> ");
      }

      if (!var1.containsKey("BeginTime")) {
         var3 = "getBeginTime";
         var4 = null;
         var2 = new PropertyDescriptor("BeginTime", DeploymentProgressObjectMBean.class, var3, (String)var4);
         var1.put("BeginTime", var2);
         var2.setValue("description", "<p>The time that the current deployment operation began. The value is in milliseconds consistent with the system time.</p> ");
      }

      if (!var1.containsKey("EndTime")) {
         var3 = "getEndTime";
         var4 = null;
         var2 = new PropertyDescriptor("EndTime", DeploymentProgressObjectMBean.class, var3, (String)var4);
         var1.put("EndTime", var2);
         var2.setValue("description", "<p>The time that the current deployment operation ended. The value is in milliseconds consistent with the system time. If the operation has not ended, the value will be zero.</p> ");
      }

      if (!var1.containsKey("FailedTargets")) {
         var3 = "getFailedTargets";
         var4 = null;
         var2 = new PropertyDescriptor("FailedTargets", DeploymentProgressObjectMBean.class, var3, (String)var4);
         var1.put("FailedTargets", var2);
         var2.setValue("description", "<p>The targets on which the current deployment operation failed.</p> ");
      }

      if (!var1.containsKey("Id")) {
         var3 = "getId";
         var4 = null;
         var2 = new PropertyDescriptor("Id", DeploymentProgressObjectMBean.class, var3, (String)var4);
         var1.put("Id", var2);
         var2.setValue("description", "<p>The unique ID for the current deployment operation. ");
      }

      if (!var1.containsKey("Messages")) {
         var3 = "getMessages";
         var4 = null;
         var2 = new PropertyDescriptor("Messages", DeploymentProgressObjectMBean.class, var3, (String)var4);
         var1.put("Messages", var2);
         var2.setValue("description", "<p>Provides an ordered array of status messages generated for the current deployment operation.</p> ");
      }

      if (!var1.containsKey("OperationType")) {
         var3 = "getOperationType";
         var4 = null;
         var2 = new PropertyDescriptor("OperationType", DeploymentProgressObjectMBean.class, var3, (String)var4);
         var1.put("OperationType", var2);
         var2.setValue("description", "<p>The deployment operation type for the current deployment operation. Possible values are 1 (start) and 2 (stop).</p> ");
      }

      if (!var1.containsKey("RootExceptions")) {
         var3 = "getRootExceptions";
         var4 = null;
         var2 = new PropertyDescriptor("RootExceptions", DeploymentProgressObjectMBean.class, var3, (String)var4);
         var1.put("RootExceptions", var2);
         var2.setValue("description", "<p>If the current deployment operation has failed, this method may return zero or more exception(s) which represent the root cause of the failure. The array will not contain WLS exception classes; instead they will be new Exceptions containing the stack traces and messages from the original WLS Exceptions.</p> ");
      }

      if (!var1.containsKey("State")) {
         var3 = "getState";
         var4 = null;
         var2 = new PropertyDescriptor("State", DeploymentProgressObjectMBean.class, var3, (String)var4);
         var1.put("State", var2);
         var2.setValue("description", "<p>The state of the current deployment operation. Possible values are STATE_INITIALIZED, STATE_RUNNING, STATE_COMPLETED, STATE_FAILED and STATE_DEFERRED.</p> ");
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         var2 = new PropertyDescriptor("Targets", DeploymentProgressObjectMBean.class, var3, (String)var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>The targets specified for the current deployment operation.</p> ");
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
      Method var3 = DeploymentProgressObjectMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = DeploymentProgressObjectMBean.class.getMethod("getExceptions", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("target", "the target where exceptions might have occurred. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the current deployment operation has failed, this method may return zero or more exception(s) which represent the errors for this target. The array will not contain WLS exception classes; instead they will be new RuntimeExceptions containing the stack traces and messages from the original WLS Exceptions.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeploymentProgressObjectMBean.class.getMethod("cancel");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Attempt to cancel the deployment operation. Any actions which have yet to start will be inhibited. Any completed actions will remain in place.</p> ");
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
