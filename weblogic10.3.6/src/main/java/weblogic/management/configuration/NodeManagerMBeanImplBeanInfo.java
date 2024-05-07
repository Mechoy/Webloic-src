package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class NodeManagerMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = NodeManagerMBean.class;

   public NodeManagerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public NodeManagerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = NodeManagerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This bean is represents a Node Manager that is associated with a machine.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.NodeManagerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Adapter")) {
         var3 = "getAdapter";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdapter";
         }

         var2 = new PropertyDescriptor("Adapter", NodeManagerMBean.class, var3, var4);
         var1.put("Adapter", var2);
         var2.setValue("description", "Gets the node manager client adapter name_version when using a VMM adapter to connect to OVM or other VMM adapter providers ");
      }

      if (!var1.containsKey("AdapterName")) {
         var3 = "getAdapterName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdapterName";
         }

         var2 = new PropertyDescriptor("AdapterName", NodeManagerMBean.class, var3, var4);
         var1.put("AdapterName", var2);
         var2.setValue("description", "Gets the node manager client adapter name when using a VMM adapter to connect to OVM or other VMM adapters providers ");
         var2.setValue("setterDeprecated", "10.3.4.0 Replaced by {@link weblogic.management.configuration.NodeManagerMBean#getAdapter()} ");
      }

      if (!var1.containsKey("AdapterVersion")) {
         var3 = "getAdapterVersion";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdapterVersion";
         }

         var2 = new PropertyDescriptor("AdapterVersion", NodeManagerMBean.class, var3, var4);
         var1.put("AdapterVersion", var2);
         var2.setValue("description", "Gets the node manager client adapter version when using a VMM adapter to connect to OVM or other VMM adapters providers ");
         var2.setValue("setterDeprecated", "10.3.4.0 Replaced by {@link weblogic.management.configuration.NodeManagerMBean#setAdapter()} ");
      }

      if (!var1.containsKey("InstalledVMMAdapters")) {
         var3 = "getInstalledVMMAdapters";
         var4 = null;
         var2 = new PropertyDescriptor("InstalledVMMAdapters", NodeManagerMBean.class, var3, var4);
         var1.put("InstalledVMMAdapters", var2);
         var2.setValue("description", "<p>Gets a list of the names and versions of the installed Virtual Machine Manager (VMM) adapters</p> ");
      }

      if (!var1.containsKey("ListenAddress")) {
         var3 = "getListenAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenAddress";
         }

         var2 = new PropertyDescriptor("ListenAddress", NodeManagerMBean.class, var3, var4);
         var1.put("ListenAddress", var2);
         var2.setValue("description", "<p>The host name or IP address where Node Manager listens for connection requests.</p> ");
         setPropertyDescriptorDefault(var2, "localhost");
         var2.setValue("secureValue", "127.0.0.1");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ListenPort")) {
         var3 = "getListenPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenPort";
         }

         var2 = new PropertyDescriptor("ListenPort", NodeManagerMBean.class, var3, var4);
         var1.put("ListenPort", var2);
         var2.setValue("description", "<p>The port number where Node Manager listens for connection requests.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5556));
         var2.setValue("legalMax", new Integer(65534));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("NMType")) {
         var3 = "getNMType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNMType";
         }

         var2 = new PropertyDescriptor("NMType", NodeManagerMBean.class, var3, var4);
         var1.put("NMType", var2);
         var2.setValue("description", "Returns the node manager type. ");
         setPropertyDescriptorDefault(var2, "SSL");
         var2.setValue("legalValues", new Object[]{"SSH", "RSH", "Plain", "SSL", "ssh", "rsh", "ssl", "plain", "VMM", "vmm", "VMMS", "vmms"});
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", NodeManagerMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("NodeManagerHome")) {
         var3 = "getNodeManagerHome";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNodeManagerHome";
         }

         var2 = new PropertyDescriptor("NodeManagerHome", NodeManagerMBean.class, var3, var4);
         var1.put("NodeManagerHome", var2);
         var2.setValue("description", "Returns the node manager home directory that will be used to substitute for the shell command template ");
      }

      if (!var1.containsKey("Password")) {
         var3 = "getPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPassword";
         }

         var2 = new PropertyDescriptor("Password", NodeManagerMBean.class, var3, var4);
         var1.put("Password", var2);
         var2.setValue("description", "<p>The password used by a Node Manager client to connect to the underlying service to which the Node Manager client delegates operations.</p>  <p>When you get the value of this attribute, WebLogic Server does the following:</p> <ol> <li>Retrieves the value of the <code>PasswordEncrypted</code> attribute.</li> <li>Decrypts the value and returns the unencrypted password as a String.</li> </ol> <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol> <li>Encrypts the value.</li> <li>Sets the value of the <code>PasswordEncrypted</code> attribute to the encrypted value.</li> </ol> <p><b>Caution:</b> Using the (<code>Password</code>) attribute is a potential security risk because the String object (which contains the unencrypted password), remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory. Therefore, you should use the <code>PasswordEncrypted()</code> attribute instead.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("getPasswordEncrypted();")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PasswordEncrypted")) {
         var3 = "getPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("PasswordEncrypted", NodeManagerMBean.class, var3, var4);
         var1.put("PasswordEncrypted", var2);
         var2.setValue("description", "<p>The encrypted Node Manager client user password.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the <code>encrypt()</code> method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("ShellCommand")) {
         var3 = "getShellCommand";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setShellCommand";
         }

         var2 = new PropertyDescriptor("ShellCommand", NodeManagerMBean.class, var3, var4);
         var1.put("ShellCommand", var2);
         var2.setValue("description", "Returns the local command line to use when invoking SSH or RSH node manager functions. ");
      }

      if (!var1.containsKey("UserName")) {
         var3 = "getUserName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserName";
         }

         var2 = new PropertyDescriptor("UserName", NodeManagerMBean.class, var3, var4);
         var1.put("UserName", var2);
         var2.setValue("description", "<p>The Node Manager client user name used to connect to the underlying service to which the client delegates operations. </p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEnabled")) {
         var3 = "isDebugEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEnabled";
         }

         var2 = new PropertyDescriptor("DebugEnabled", NodeManagerMBean.class, var3, var4);
         var1.put("DebugEnabled", var2);
         var2.setValue("description", "<p>Specifies whether communication with this Node Manager needs to be debugged. When enabled, Node Manager provides more information about request processing. This information is sent to the log of the server making requests to Node Manager.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
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
      Method var3 = NodeManagerMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = NodeManagerMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
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
