package weblogic.management.mbeanservers.edit.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.mbeanservers.edit.RecordingManagerMBean;
import weblogic.management.mbeanservers.internal.ServiceImplBeanInfo;

public class RecordingManagerMBeanImplBeanInfo extends ServiceImplBeanInfo {
   public static Class INTERFACE_CLASS = RecordingManagerMBean.class;

   public RecordingManagerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public RecordingManagerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = RecordingManagerMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.mbeanservers.edit.internal");
      String var3 = (new String("<p>This MBean records configuration actions that occur during an edit session and writes the actions as series of WebLogic Scripting Tool (WLST) commands. You can then use WLST to replay the commands.</p>  <p>WLST is a command-line scripting environment that you can use to create, manage, and monitor WebLogic Server domains. It is installed on your system when you install WebLogic Server. </p> <p> This MBean does <b>not</b> record WLST commands for the following: </p> <ul> <li> Changes to the security data that is maintained by a security provider. For example, you cannot record the commands to add or remove users, roles, and policies. </li> <li>Changes to deployment plans.</li> <li> Runtime operations found on Control or Monitoring pages, such as starting and stopping applications or servers. </li> </ul> <p> You cannot remove or undo a command once it has been recorded. Instead, you can edit the script file after you stop recording. </p> <p>If you record commands that get or set the values of encrypted attributes (such the password for a server's Java Standard Trust keystore), this MBean creates two files in addition to the script file: a user configuration file that contains the encrypted data and a key file that contains the key used to encrypt the data. Use the file system to limit read and write access to the key file. Users who can read the key file can read all of the encrypted data that you recorded. </p>  <p>The key file and user configuration files are created in the same directory as the recorded script file and are named <code><i>recording-file</i>Config</code> and <code><i>recording-file</i>Secret</code> where <code><i>recording-file</i></code> is the name of the recorded script file.</p> <p>When you use WLST to replay the commands, the user configuration and key files must be in the same directory as the script file. If you move the script file, you must also move the user configuration and key files. Only the key file that was used to encrypt the data can be used to unencrypt the data.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.mbeanservers.edit.RecordingManagerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", RecordingManagerMBean.class, var3, (String)var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>A unique key that WebLogic Server generates to identify the current instance of this MBean type.</p>  <p>For a singleton, such as <code>DomainRuntimeServiceMBean</code>, this key is often just the bean's short class name.</p> ");
      }

      if (!var1.containsKey("ParentAttribute")) {
         var3 = "getParentAttribute";
         var4 = null;
         var2 = new PropertyDescriptor("ParentAttribute", RecordingManagerMBean.class, var3, (String)var4);
         var1.put("ParentAttribute", var2);
         var2.setValue("description", "<p>The name of the attribute of the parent that refers to this bean</p> ");
      }

      if (!var1.containsKey("ParentService")) {
         var3 = "getParentService";
         var4 = null;
         var2 = new PropertyDescriptor("ParentService", RecordingManagerMBean.class, var3, (String)var4);
         var1.put("ParentService", var2);
         var2.setValue("description", "<p>The MBean that created the current MBean instance.</p>  <p>In the data model for WebLogic Server MBeans, an MBean that creates another MBean is called a <i>parent</i>. MBeans at the top of the hierarchy have no parents.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("Path")) {
         var3 = "getPath";
         var4 = null;
         var2 = new PropertyDescriptor("Path", RecordingManagerMBean.class, var3, (String)var4);
         var1.put("Path", var2);
         var2.setValue("description", "<p>Returns the path to the bean relative to the reoot of the heirarchy of services</p> ");
      }

      if (!var1.containsKey("RecordingFileName")) {
         var3 = "getRecordingFileName";
         var4 = null;
         var2 = new PropertyDescriptor("RecordingFileName", RecordingManagerMBean.class, var3, (String)var4);
         var1.put("RecordingFileName", var2);
         var2.setValue("description", "<p>Returns the full path of the recording file. </p> ");
      }

      if (!var1.containsKey("Type")) {
         var3 = "getType";
         var4 = null;
         var2 = new PropertyDescriptor("Type", RecordingManagerMBean.class, var3, (String)var4);
         var1.put("Type", var2);
         var2.setValue("description", "<p>The MBean type for this instance. This is useful for MBean types that support multiple intances, such as <code>ActivationTaskMBean</code>.</p> ");
      }

      if (!var1.containsKey("Recording")) {
         var3 = "isRecording";
         var4 = null;
         var2 = new PropertyDescriptor("Recording", RecordingManagerMBean.class, var3, (String)var4);
         var1.put("Recording", var2);
         var2.setValue("description", "<p>Indicates whether a recording session is currently in progress.</p> ");
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
      Method var3 = RecordingManagerMBean.class.getMethod("startRecording", String.class, Boolean.TYPE);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("fileName", "Absolute path and file name for the file in which to write WLST commands. "), createParameterDescriptor("append", "If set to true, this method writes WLST commands at the end of the recording file instead of the beginning. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("RecordingException If a recording session is already started or            the specified file cannot be opened.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Starts a recording session. The recorded actions will be saved as WLST commands in the specified file. Actions are recorded and written as you invoke them.</p> <p>If the specified file already exists, this method adds the WLST commands to the beginning or end of the file, depending on which value you pass in the <code>append</code> argument. This method does not overwrite an existing file.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = RecordingManagerMBean.class.getMethod("startRecording", String.class, Map.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("fileName", "recording filename "), createParameterDescriptor("options", "contains flags to control recording behavior. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("RecordingException if a recording session is already started or the specified file           cannot be opened for some reason")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Starts a recording session. The generated WLST scripts will be saved to the specified file.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = RecordingManagerMBean.class.getMethod("stopRecording");
      String var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         String[] var8 = new String[]{BeanInfoHelper.encodeEntities("RecordingException If there is no active recording session")};
         var2.setValue("throws", var8);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Stops a recording session. </p> ");
         var2.setValue("role", "operation");
      }

      var3 = RecordingManagerMBean.class.getMethod("record", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("str", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("RecordingException if there is no active recording session")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Writes a string to the script file.</p> <p>If you invoke this method while a recording session is in progress, the method writes the string immediately after the WLST command that it has most recently recorded.</p> ");
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
