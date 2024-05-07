package weblogic.management.mbeanservers.edit.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.mbeanservers.internal.ServiceImplBeanInfo;

public class ConfigurationManagerMBeanImplBeanInfo extends ServiceImplBeanInfo {
   public static Class INTERFACE_CLASS = ConfigurationManagerMBean.class;

   public ConfigurationManagerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ConfigurationManagerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ConfigurationManagerMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.mbeanservers.edit.internal");
      String var3 = (new String("<p>Manages changes to the configuration of the current WebLogic Server domain. The operations in this MBean start and stop edit sessions, save, undo, and activate configuration changes.</p>  <p>The general process for changing the configuration of a domain is as follows:</p>  <ol> <li> <p>Use this MBean's <code>startEdit()</code> operation to start an edit session.</p>  <p>When you start an edit session, WebLogic Server locks other users from editing the pending configuration MBean hierarchy. If two users start an edit session under the same user identity, changes from both users are collected into a single set of changes.</p>  <p>The operation returns the pending <code>DomainMBean</code>, which is the root of the configuration MBean hierarchy.</p> </li>  <li> <p>Navigate to an MBean and change the value of its attributes or add or remove a child MBean.</p> </li>  <li> <p>Save your changes.</p>  <p>Your saved changes are written to the domain's pending configuration files.</p> </li>  <li> <p>(Optional) Make additional changes or undo the changes.</p> </li>  <li> <p>Use this MBean's <code>activate()</code> operation to activate the saved changes.</p>  <p>When you activate, the changes are propagated to all the servers in the domain and applied to the running configuration.</p> </li> </ol> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.mbeanservers.edit.ConfigurationManagerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ActivationTasks")) {
         var3 = "getActivationTasks";
         var4 = null;
         var2 = new PropertyDescriptor("ActivationTasks", ConfigurationManagerMBean.class, var3, var4);
         var1.put("ActivationTasks", var2);
         var2.setValue("description", "<p>Returns the list of all <code>ActivationTaskMBean</code> instances that have been created. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ActiveActivationTasks")) {
         var3 = "getActiveActivationTasks";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveActivationTasks", ConfigurationManagerMBean.class, var3, var4);
         var1.put("ActiveActivationTasks", var2);
         var2.setValue("description", "<p>Contains the <code>ActivationTaskMBeans</code> that provide information about activation tasks that are in progress.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("ActivationTaskMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Changes")) {
         var3 = "getChanges";
         var4 = null;
         var2 = new PropertyDescriptor("Changes", ConfigurationManagerMBean.class, var3, var4);
         var1.put("Changes", var2);
         var2.setValue("description", "<p>Contains <code>Change</code> objects for all of the unsaved changes in the current edit session. Each change to an MBean attribute is represented in its own <code>Change</code> object.</p> ");
      }

      if (!var1.containsKey("CompletedActivationTasks")) {
         var3 = "getCompletedActivationTasks";
         var4 = null;
         var2 = new PropertyDescriptor("CompletedActivationTasks", ConfigurationManagerMBean.class, var3, var4);
         var1.put("CompletedActivationTasks", var2);
         var2.setValue("description", "<p>Contains all <code>ActivationTaskMBeans</code> that are stored in memory and that describe activation tasks that have completed.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("CompletedActivationTasksCount")) {
         var3 = "getCompletedActivationTasksCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompletedActivationTasksCount";
         }

         var2 = new PropertyDescriptor("CompletedActivationTasksCount", ConfigurationManagerMBean.class, var3, var4);
         var1.put("CompletedActivationTasksCount", var2);
         var2.setValue("description", "<p>The maximum number of <code>ActivationTaskMBeans</code> that WebLogic Server keeps in memory.</p>  <p>Each <code>ActivationTaskMBean</code> contains one <code>Change</code> object for each change that was activated. The MBean and its <code>Change</code> objects describe which user activated the changes, when the changes were activated, and which MBean attributes were modified.</p>  <p>WebLogic Server does not save this data to disk, and therefore it is not available across sessions of the Administration Server.</p>  <p>Because a large collection of <code>ActivationTaskMBean</code>s could potentially use a significant amount of memory, the default number is 10.</p> ");
      }

      if (!var1.containsKey("CurrentEditor")) {
         var3 = "getCurrentEditor";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentEditor", ConfigurationManagerMBean.class, var3, var4);
         var1.put("CurrentEditor", var2);
         var2.setValue("description", "<p>The name of the user who started the current edit session.</p> ");
      }

      if (!var1.containsKey("CurrentEditorExpirationTime")) {
         var3 = "getCurrentEditorExpirationTime";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentEditorExpirationTime", ConfigurationManagerMBean.class, var3, var4);
         var1.put("CurrentEditorExpirationTime", var2);
         var2.setValue("description", "<p>The time at which the current edit session expires as determined by the timeout parameter of the <code>startEdit</code> operation.</p> ");
      }

      if (!var1.containsKey("CurrentEditorStartTime")) {
         var3 = "getCurrentEditorStartTime";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentEditorStartTime", ConfigurationManagerMBean.class, var3, var4);
         var1.put("CurrentEditorStartTime", var2);
         var2.setValue("description", "<p>The time at which the current edit session started.</p> ");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", ConfigurationManagerMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>A unique key that WebLogic Server generates to identify the current instance of this MBean type.</p>  <p>For a singleton, such as <code>DomainRuntimeServiceMBean</code>, this key is often just the bean's short class name.</p> ");
      }

      if (!var1.containsKey("ParentAttribute")) {
         var3 = "getParentAttribute";
         var4 = null;
         var2 = new PropertyDescriptor("ParentAttribute", ConfigurationManagerMBean.class, var3, var4);
         var1.put("ParentAttribute", var2);
         var2.setValue("description", "<p>The name of the attribute of the parent that refers to this bean</p> ");
      }

      if (!var1.containsKey("ParentService")) {
         var3 = "getParentService";
         var4 = null;
         var2 = new PropertyDescriptor("ParentService", ConfigurationManagerMBean.class, var3, var4);
         var1.put("ParentService", var2);
         var2.setValue("description", "<p>The MBean that created the current MBean instance.</p>  <p>In the data model for WebLogic Server MBeans, an MBean that creates another MBean is called a <i>parent</i>. MBeans at the top of the hierarchy have no parents.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("Path")) {
         var3 = "getPath";
         var4 = null;
         var2 = new PropertyDescriptor("Path", ConfigurationManagerMBean.class, var3, var4);
         var1.put("Path", var2);
         var2.setValue("description", "<p>Returns the path to the bean relative to the reoot of the heirarchy of services</p> ");
      }

      if (!var1.containsKey("Type")) {
         var3 = "getType";
         var4 = null;
         var2 = new PropertyDescriptor("Type", ConfigurationManagerMBean.class, var3, var4);
         var1.put("Type", var2);
         var2.setValue("description", "<p>The MBean type for this instance. This is useful for MBean types that support multiple intances, such as <code>ActivationTaskMBean</code>.</p> ");
      }

      if (!var1.containsKey("UnactivatedChanges")) {
         var3 = "getUnactivatedChanges";
         var4 = null;
         var2 = new PropertyDescriptor("UnactivatedChanges", ConfigurationManagerMBean.class, var3, var4);
         var1.put("UnactivatedChanges", var2);
         var2.setValue("description", "<p>Contains <code>Change</code> objects for all changes (saved or unsaved) that have been made since the <code>activate</code> operation completed successfully. This includes any changes that have been saved but not activated in the current and previous edit sessions. </p> <p>Each change to an MBean attribute is described in its own <code>Change</code> object.</p> ");
      }

      if (!var1.containsKey("CurrentEditorExclusive")) {
         var3 = "isCurrentEditorExclusive";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentEditorExclusive", ConfigurationManagerMBean.class, var3, var4);
         var1.put("CurrentEditorExclusive", var2);
         var2.setValue("description", "<p>Whether the edit session is exclusive as determined by the exclusive parameter of the <code>startEdit</code> operation.</p> ");
      }

      if (!var1.containsKey("CurrentEditorExpired")) {
         var3 = "isCurrentEditorExpired";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentEditorExpired", ConfigurationManagerMBean.class, var3, var4);
         var1.put("CurrentEditorExpired", var2);
         var2.setValue("description", "<p>Whether the edit session is expired as determined by the timeout parameter of the <code>startEdit</code> operation.</p> ");
      }

      if (!var1.containsKey("Editor")) {
         var3 = "isEditor";
         var4 = null;
         var2 = new PropertyDescriptor("Editor", ConfigurationManagerMBean.class, var3, var4);
         var1.put("Editor", var2);
         var2.setValue("description", "<p>Returns true if the caller started the current edit session.</p> ");
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
      Method var3 = ConfigurationManagerMBean.class.getMethod("startEdit", Integer.TYPE, Integer.TYPE);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("waitTimeInMillis", "Prevents other uses from starting an edit session until waitTimeInMillis expires, edits are activated, or the edit session is stopped. If the value of waitTimeInMillis is 0 and an edit session is active, this operation returns immediately. To block indefinitely, specify a value of -1. "), createParameterDescriptor("timeOutInMillis", "Specifies the number of milliseconds after which the lock on the configuration is no longer guaranteed. This time out is enforced lazily. If no other user starts an edit session after the timeout expires, the unsaved changes are left intact and may be saved. If another user starts an edit session after the timeout expires, unsaved changes are automatically reverted and the lock is given to that new user. Specify a value of -1 to indicate that you do not want the edit to time out. In this case, if you do not stop your edit session, only an administrator can stop the edit session by invokeing the cancelEdit operation. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("EditTimedOutException thrown when the start edit request times out because the wait time has elasped and another user still has the edit session.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Starts an edit session on behalf of the currently authenticated user and prevents other users from editing the configuration for the duration of the session. A user must call this operation before modifying the configuration of the domain.</p>  <p>If two users or processes start an edit session under the same user identity, changes from both users are collected into a single set of changes.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("startEdit", Integer.TYPE, Integer.TYPE, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("waitTimeInMillis", "Prevents other uses from starting an edit session until waitTimeInMillis expires, edits are activated, or the edit session is stopped. If the value of waitTimeInMillis is 0 and an edit session is active, this operation returns immediately. To block indefinitely, specify a value of -1. "), createParameterDescriptor("timeOutInMillis", "Specifies the number of milliseconds after which the lock on the configuration is no longer guaranteed. This time out is enforced lazily. If no other user starts an edit session after the timeout expires, the unsaved changes are left intact and may be saved. If another user starts an edit session after the timeout expires, unsaved changes are automatically reverted and the lock is given to that new user. Specify a value of -1 to indicate that you do not want the edit to time out. In this case, if you do not stop your edit session, only an administrator can stop the edit session by invokeing the cancelEdit operation. "), createParameterDescriptor("exclusive", "Specifies whether the edit session should be exclusive. An edit session will cause a subsequent call to startEdit by the same owner to wait until the edit session lock is released. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("EditTimedOutException thrown when the start edit request times out because the wait time has elasped and another user still has the edit session.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Starts an edit session on behalf of the currently authenticated user and prevents other users from editing the configuration for the duration of the session. A user must call this operation before modifying the configuration of the domain.</p>  <p>Prevents multiple users or processes from starting an edit session under the same user identity.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("stopEdit");
      String var7 = BeanInfoHelper.buildMethodKey(var3);
      String[] var8;
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var8 = new String[]{BeanInfoHelper.encodeEntities("NotEditorException thrown if the caller did not start the current edit session.")};
         var2.setValue("throws", var8);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Stops the current edit session, releases the edit lock, and enables other users to start an edit session. Any unsaved changes are discarded.</p>  <p>This operation can be invoked only by the user who started the edit session.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("cancelEdit");
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Cancels the current edit session, releases the edit lock, and enables other users to start an edit session. Any unsaved changes are discarded; saved changes remain pending.</p>  <p>This operation can be called by any user with administrator privileges, even if the user is not the one who started the edit session. Use this operation to cancel an edit session when the current editor can not be contacted to stop an edit session and release the lock. To instead discard all changes, saved and unsaved, see the undoUnactivatedChanges operation.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("validate");
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var8 = new String[]{BeanInfoHelper.encodeEntities("NotEditorException thrown if the caller did not start the current edit session."), BeanInfoHelper.encodeEntities("ValidationException thrown if an validation error occurs while validating changes.")};
         var2.setValue("throws", var8);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Verifies that all unsaved changes satisfy dependencies between MBean attributes and makes other checks that cannot be made at the time that you set the value of a single attribute.</p>  <p>The <code>save</code> operation also validates changes, but you can use this (<code>validate</code>) operation to check that changes are valid before saving them.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("reload");
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var8 = new String[]{BeanInfoHelper.encodeEntities("NotEditorException thrown if the caller did not start the current edit session."), BeanInfoHelper.encodeEntities("ValidationException thrown if an validation error occurs  while reloading files.")};
         var2.setValue("throws", var8);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Reloads the configuration files from the pending directory updates the configuration contained in the Edit MBeanServer.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("save");
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var8 = new String[]{BeanInfoHelper.encodeEntities("NotEditorException thrown if the caller did not start the current edit session."), BeanInfoHelper.encodeEntities("ValidationException thrown if an validation error occurs while saving changes.")};
         var2.setValue("throws", var8);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Validates unsaved changes and saves them to the pending configuration files on disk.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("undo");
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var8 = new String[]{BeanInfoHelper.encodeEntities("NotEditorException thrown if the caller did not start the current edit session.")};
         var2.setValue("throws", var8);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Undoes all unsaved changes. This reverts the hierarchy of pending configuration MBeans to the last saved state of the pending configuration files, discarding in-memory changes.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("haveUnactivatedChanges");
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Returns true if any changes (saved or unsaved) have been made since the <code>activate</code> operation completed successfully. This includes any changes that have been saved but not activated in the current and previous edit sessions.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("undoUnactivatedChanges");
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var8 = new String[]{BeanInfoHelper.encodeEntities("NotEditorException thrown if the caller did not start the current edit session.")};
         var2.setValue("throws", var8);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Undoes all of the changes (saved or unsaved) that have been made since the <code>activate</code> operation completed successfully. This includes any changes that have been saved but not activated in the current and previous edit sessions.</p>  <p>This reverts the hierarchy of pending configuration MBeans to the last successful activate state of the domain, discarding any changes made but not activated.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("activate", Long.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("timeout", "long that contains the time (milliseconds) for the operation to complete. If the elasped time exceeds that value, then the activation of the configuration changes will be canceled. If -1, then the activation will not timeout. If a non-zero timeout is specified, then the activate will wait   until the activate operation has completed or until the timeout period   has elasped. If a zero timeout is specified, then the activate will return   immediately and the caller can wait for completion using the ActivationTaskMBean. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("NotEditorException thrown if the caller did not start the current edit session.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Activates the changes that have been saved to the pending configuration files.</p>  <p>To activate changes, WebLogic Server copies the pending configuration files to a pending directory within each server instance's root directory. Each server instance determines whether it can consume the changes. If all servers can, then the pending configuration files become the active configuration files and the in-memory hierarchy of active configuration MBeans is updated for each server.</p>  <p>If any server is unable to consume the change, then the activation fails for all servers. All saved changes remain in the pending configuration files and can be activated later.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("purgeCompletedActivationTasks");
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Purges from memory all <code>ActivationTaskMBeans</code> that represent completed activation tasks.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("getChangesToDestroyBean", DescriptorBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("configurationMBean", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Contains <code>Change</code> objects for the changes required to destroy the specified instance of a configuration bean. Each change to an MBean attribute is represented in its own <code>Change</code> object.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConfigurationManagerMBean.class.getMethod("removeReferencesToBean", DescriptorBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("configurationMBean", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("NotEditorException thrown if the caller did not start the current edit session.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes references to bean that must be removed in order to destroy the specified instance of a configuration bean.</p> ");
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
