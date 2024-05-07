package weblogic.management.mbeanservers.edit.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.mbeanservers.edit.ActivationTaskMBean;
import weblogic.management.mbeanservers.internal.ServiceImplBeanInfo;

public class ActivationTaskMBeanImplBeanInfo extends ServiceImplBeanInfo {
   public static Class INTERFACE_CLASS = ActivationTaskMBean.class;

   public ActivationTaskMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ActivationTaskMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ActivationTaskMBeanImpl.class;
      } catch (Throwable var6) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("ConfigurationManagerMBean#activate()")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.management.mbeanservers.edit.internal");
      String var4 = (new String("<p>Provides information about an activation task, which is initiated by invoking the <code>ConfigurationManagerMBean activate</code> operation.</p> <p>To describe the changes that are being activated in the domain, this MBean contains one <code>Configuration</code> object for each change that was saved to the domain's pending configuration files.</p> <p>This MBean also contains attributes that describe the status of the activation operation as it attempts to distribute changes to all servers in the domain.</p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      String[] var5 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var5);
      var2.setValue("interfaceclassname", "weblogic.management.mbeanservers.edit.ActivationTaskMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("Changes")) {
         var3 = "getChanges";
         var4 = null;
         var2 = new PropertyDescriptor("Changes", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("Changes", var2);
         var2.setValue("description", "<p>Contains the <code>Change</code> objects that describe the changes that are being activated.</p>Each <code>Change</code> object describes a change to a single MBean attribute.<p> ");
      }

      if (!var1.containsKey("CompletionTime")) {
         var3 = "getCompletionTime";
         var4 = null;
         var2 = new PropertyDescriptor("CompletionTime", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("CompletionTime", var2);
         var2.setValue("description", "<p>The time at which the activation task completes, either by successfully activating changes on all servers, rolling back all changes (because a server was unable to consume the changes), or by timing out.</p> ");
      }

      if (!var1.containsKey("Details")) {
         var3 = "getDetails";
         var4 = null;
         var2 = new PropertyDescriptor("Details", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("Details", var2);
         var2.setValue("description", "<p>Contains all available information about the current activation task in a single <code>String</code> object.<p> ");
      }

      if (!var1.containsKey("Error")) {
         var3 = "getError";
         var4 = null;
         var2 = new PropertyDescriptor("Error", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("Error", var2);
         var2.setValue("description", "<p>Returns the exception that describes why the activation has failed.</p> <p>To see how each server responded to the activation request, get the value of this MBean's <code>StatusByServer</code> attribute. ");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>A unique key that WebLogic Server generates to identify the current instance of this MBean type.</p>  <p>For a singleton, such as <code>DomainRuntimeServiceMBean</code>, this key is often just the bean's short class name.</p> ");
      }

      if (!var1.containsKey("ParentAttribute")) {
         var3 = "getParentAttribute";
         var4 = null;
         var2 = new PropertyDescriptor("ParentAttribute", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("ParentAttribute", var2);
         var2.setValue("description", "<p>The name of the attribute of the parent that refers to this bean</p> ");
      }

      if (!var1.containsKey("ParentService")) {
         var3 = "getParentService";
         var4 = null;
         var2 = new PropertyDescriptor("ParentService", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("ParentService", var2);
         var2.setValue("description", "<p>The MBean that created the current MBean instance.</p>  <p>In the data model for WebLogic Server MBeans, an MBean that creates another MBean is called a <i>parent</i>. MBeans at the top of the hierarchy have no parents.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("Path")) {
         var3 = "getPath";
         var4 = null;
         var2 = new PropertyDescriptor("Path", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("Path", var2);
         var2.setValue("description", "<p>Returns the path to the bean relative to the reoot of the heirarchy of services</p> ");
      }

      if (!var1.containsKey("StartTime")) {
         var3 = "getStartTime";
         var4 = null;
         var2 = new PropertyDescriptor("StartTime", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("StartTime", var2);
         var2.setValue("description", "<p>The time at which the <code>ConfigurationManagerMBean activate</code> operation was invoked.</p> ");
      }

      if (!var1.containsKey("State")) {
         var3 = "getState";
         var4 = null;
         var2 = new PropertyDescriptor("State", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("State", var2);
         var2.setValue("description", "<p>The state of the activation task, which is initiated by invoking the <code>ConfigurationManagerMBean activate</code> operation.</p> States are: <br><ul> <li>STATE_NEW - Indicates that the task has been created but distribution has not started.</li> <li>STATE_DISTRIBUTING - Indicates that the changes have been validated and are being distributed to the various servers.</li> <li>STATE_DISTRIBUTED -  Indicates that the changes have been distributed to all servers.</li> <li>STATE_PENDING - Indicates that the configuration changes require that the server be restarted for the changes to become available.</li> <li> STATE_COMMITTED - Indicates that the changes have been distributed to all servers and made permanent.</li> <li> STATE_FAILED -  Indicates that the changes failed in the distribution phase.</li> <li> STATE_CANCELING - Indicates that the changes are canceling in the distribution phase.</li> <li> STATE_COMMIT_FAILING - Indicates that the changes are failing in the commit phase. </li> </ul> ");
      }

      if (!var1.containsKey("StatusByServer")) {
         var3 = "getStatusByServer";
         var4 = null;
         var2 = new PropertyDescriptor("StatusByServer", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("StatusByServer", var2);
         var2.setValue("description", "<p>The state of the activation task on each server in the domain.</p> <p>If any server fails to activate the changes, the activation task is rolled back for all servers in the domain.</p> ");
      }

      if (!var1.containsKey("Type")) {
         var3 = "getType";
         var4 = null;
         var2 = new PropertyDescriptor("Type", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("Type", var2);
         var2.setValue("description", "<p>The MBean type for this instance. This is useful for MBean types that support multiple intances, such as <code>ActivationTaskMBean</code>.</p> ");
      }

      if (!var1.containsKey("User")) {
         var3 = "getUser";
         var4 = null;
         var2 = new PropertyDescriptor("User", ActivationTaskMBean.class, var3, (String)var4);
         var1.put("User", var2);
         var2.setValue("description", "<p>The name of the user who invoked the <code>ConfigurationManagerMBean activate</code> operation.<p> ");
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
      Method var3 = ActivationTaskMBean.class.getMethod("waitForTaskCompletion");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Allows the caller to wait until the activation task completes (either by successfully distributing changes or by rolling back the task) or times out.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ActivationTaskMBean.class.getMethod("waitForTaskCompletion", Long.TYPE);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("timeout", "long that specifies the time (milliseconds) to wait for the activate deployment request to complete. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Allows the caller to wait until any of the following occurs: <ul><li>The activation task completes (either by successfully distributing changes or by rolling back the task) </li> <li>The activation task times out</li> <li>The number of specified milliseconds elapses</li> </ul> ");
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
