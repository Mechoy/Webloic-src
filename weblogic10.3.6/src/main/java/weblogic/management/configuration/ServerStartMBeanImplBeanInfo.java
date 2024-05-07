package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class ServerStartMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ServerStartMBean.class;

   public ServerStartMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServerStartMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServerStartMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("dynamic", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This bean is used to configure the attributes necessary to start up a server on a remote machine.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ServerStartMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Arguments")) {
         var3 = "getArguments";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setArguments";
         }

         var2 = new PropertyDescriptor("Arguments", ServerStartMBean.class, var3, var4);
         var1.put("Arguments", var2);
         var2.setValue("description", "<p>The arguments to use when starting this server.</p>  <p>These are the first arguments appended immediately after <code>java</code> portion of the startup command. For example, you can set Java heap memory or specify any <code>weblogic.Server</code> option.</p>  <p>This property should not be used to specify weblogic.management.username or weblogic.management.password as these values will be ignored during server startup.  Instead the username and password properties should be set. This will also enable node manager to properly encrypt these values on the managed server's machine.</p>  <p>Separate arguments with a space.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BeaHome")) {
         var3 = "getBeaHome";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBeaHome";
         }

         var2 = new PropertyDescriptor("BeaHome", ServerStartMBean.class, var3, var4);
         var1.put("BeaHome", var2);
         var2.setValue("description", "<p>The BEA home directory (path on the machine running Node Manager) to use when starting this server.</p>  <p>Specify the directory on the Node Manager machine under which all of Oracle's BEA products were installed. For example, <code>c:&#92;bea</code>.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.1.0.0", (String)null, this.targetVersion) && !var1.containsKey("BootProperties")) {
         var3 = "getBootProperties";
         var4 = null;
         var2 = new PropertyDescriptor("BootProperties", ServerStartMBean.class, var3, var4);
         var1.put("BootProperties", var2);
         var2.setValue("description", "<p>Get the boot properties to be used for a server</p> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("since", "9.1.0.0");
      }

      if (!var1.containsKey("ClassPath")) {
         var3 = "getClassPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClassPath";
         }

         var2 = new PropertyDescriptor("ClassPath", ServerStartMBean.class, var3, var4);
         var1.put("ClassPath", var2);
         var2.setValue("description", "<p>The classpath (path on the machine running Node Manager) to use when starting this server.</p>  <p>At a minimum you will need to specify the following values for the class path option: <code>WL_HOME/server/lib/weblogic_sp.jar;WL_HOME/server/lib/weblogic.jar</code></p>  <p>where <code>WL_HOME</code> is the directory in which you installed WebLogic Server on the Node Manager machine.</p>  <p>The shell environment determines which character you use to separate path elements. On Windows, you typically use a semicolon (;). In a BASH shell, you typically use a colon (:).</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JavaHome")) {
         var3 = "getJavaHome";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJavaHome";
         }

         var2 = new PropertyDescriptor("JavaHome", ServerStartMBean.class, var3, var4);
         var1.put("JavaHome", var2);
         var2.setValue("description", "<p>The Java home directory (path on the machine running Node Manager) to use when starting this server.</p>  <p>Specify the parent directory of the JDK's <code>bin</code> directory. For example, <code>c:&#92;bea&#92;jdk141</code>.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JavaVendor")) {
         var3 = "getJavaVendor";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJavaVendor";
         }

         var2 = new PropertyDescriptor("JavaVendor", ServerStartMBean.class, var3, var4);
         var1.put("JavaVendor", var2);
         var2.setValue("description", "<p>The Java Vendor value to use when starting this server For example, <code>BEA, Sun, HP etc </code> </p> <p> If the server is part of a cluster and configured for automatic migration across possibly different platforms with different vendors providing the JDKs, then, both JavaVendor and JavaHome should be set in the generated configuration file instead. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", ServerStartMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("Password")) {
         var3 = "getPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPassword";
         }

         var2 = new PropertyDescriptor("Password", ServerStartMBean.class, var3, var4);
         var1.put("Password", var2);
         var2.setValue("description", "<p>The password of the username used to boot the server and perform server health monitoring.</p>  <p>As of 8.1 sp4, when you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>PasswordEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>PasswordEncrypted</code> attribute to the encrypted value.</li> </ol>  <p>Using this attribute (<code>Password</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>PasswordEncrypted</code>.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#getPasswordEncrypted()")};
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

         var2 = new PropertyDescriptor("PasswordEncrypted", ServerStartMBean.class, var3, var4);
         var1.put("PasswordEncrypted", var2);
         var2.setValue("description", "<p>The encrypted password of the username used to boot the server and perform server health monitoring.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RootDirectory")) {
         var3 = "getRootDirectory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRootDirectory";
         }

         var2 = new PropertyDescriptor("RootDirectory", ServerStartMBean.class, var3, var4);
         var1.put("RootDirectory", var2);
         var2.setValue("description", "<p>The directory that this server uses as its root directory. This directory must be on the computer that hosts the Node Manager. If you do not specify a Root Directory value, the domain directory is used by default. </p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SecurityPolicyFile")) {
         var3 = "getSecurityPolicyFile";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSecurityPolicyFile";
         }

         var2 = new PropertyDescriptor("SecurityPolicyFile", ServerStartMBean.class, var3, var4);
         var1.put("SecurityPolicyFile", var2);
         var2.setValue("description", "<p>The security policy file (directory and filename on the machine running Node Manager) to use when starting this server.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.1.0.0", (String)null, this.targetVersion) && !var1.containsKey("StartupProperties")) {
         var3 = "getStartupProperties";
         var4 = null;
         var2 = new PropertyDescriptor("StartupProperties", ServerStartMBean.class, var3, var4);
         var1.put("StartupProperties", var2);
         var2.setValue("description", "<p>Get the boot properties to be used for a server</p> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("since", "9.1.0.0");
      }

      if (!var1.containsKey("Username")) {
         var3 = "getUsername";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUsername";
         }

         var2 = new PropertyDescriptor("Username", ServerStartMBean.class, var3, var4);
         var1.put("Username", var2);
         var2.setValue("description", "<p>The user name to use when booting this server.</p>  <p>The Administration Console inserts the user name that you supplied when you logged in to the console. The Domain Configuration Wizard inserts the user name that you defined when you created the domain.</p> ");
         setPropertyDescriptorDefault(var2, "");
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
      Method var3 = ServerStartMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = ServerStartMBean.class.getMethod("restoreDefaultValue", String.class);
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
