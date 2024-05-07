package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JoltConnectionPoolMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JoltConnectionPoolMBean.class;

   public JoltConnectionPoolMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JoltConnectionPoolMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JoltConnectionPoolMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This bean defines a Jolt connection pool. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JoltConnectionPoolMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      String[] var5;
      String[] var6;
      if (!var1.containsKey("ApplicationPassword")) {
         var3 = "getApplicationPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setApplicationPassword";
         }

         var2 = new PropertyDescriptor("ApplicationPassword", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("ApplicationPassword", var2);
         var2.setValue("description", "<p>The application password for this Jolt connection pool. (This is required only when the security level in the Tuxedo domain is <tt>USER_AUTH</tt>, <tt>ACL</tt> or <tt>MANDATORY_ACL</tt>).</p> <p>As of 8.1 sp4, when you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>ApplicationPasswordEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>ApplicationPasswordEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>ApplicationPassword</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>ApplicationPasswordEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getApplicationPasswordEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowedGet", var6);
      }

      if (!var1.containsKey("ApplicationPasswordEncrypted")) {
         var3 = "getApplicationPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setApplicationPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("ApplicationPasswordEncrypted", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("ApplicationPasswordEncrypted", var2);
         var2.setValue("description", "<p>The encrypted application password for this connection pool.</p> <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var5 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowedGet", var5);
      }

      if (!var1.containsKey("FailoverAddresses")) {
         var3 = "getFailoverAddresses";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFailoverAddresses";
         }

         var2 = new PropertyDescriptor("FailoverAddresses", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("FailoverAddresses", var2);
         var2.setValue("description", "<p>The list of Jolt Server Listeners (JSLs) addresses that is used if the connection pool cannot estabilish connections to the Primary Addresses, or if the primary connections fail.</p>  <p>The format of each address is: <tt>//hostname:port</tt>. Multiple addresses should be separated by commas.</p>  <p>These JSLs need not reside on the same host as the primary JSLs.</p> ");
      }

      if (!var1.containsKey("KeyPassPhrase")) {
         var3 = "getKeyPassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeyPassPhrase";
         }

         var2 = new PropertyDescriptor("KeyPassPhrase", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("KeyPassPhrase", var2);
         var2.setValue("description", "<p>The encrypted identity passphrase.</p>  <p>When you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>KeyPassPhraseEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>KeyPassPhraseEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>KeyPassPhrase</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>KeyPassPhraseEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getKeyPassPhraseEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("KeyPassPhraseEncrypted")) {
         var3 = "getKeyPassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeyPassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("KeyPassPhraseEncrypted", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("KeyPassPhraseEncrypted", var2);
         var2.setValue("description", "<p>Returns encrypted identity pass phrase.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("KeyStoreName")) {
         var3 = "getKeyStoreName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeyStoreName";
         }

         var2 = new PropertyDescriptor("KeyStoreName", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("KeyStoreName", var2);
         var2.setValue("description", "<p>The path and file name of the keystore containing the private key used in SSL mutual authentication.</p> ");
      }

      if (!var1.containsKey("KeyStorePassPhrase")) {
         var3 = "getKeyStorePassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeyStorePassPhrase";
         }

         var2 = new PropertyDescriptor("KeyStorePassPhrase", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("KeyStorePassPhrase", var2);
         var2.setValue("description", "<p>The encrypted identity keystore's passphrase. If empty or null, then the keystore will be opened without a passphrase.</p>  <p>When you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>KeyStorePassPhraseEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>KeyStorePassPhraseEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>KeyStorePassPhrase</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>KeyStorePassPhraseEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getKeyStorePassPhraseEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("KeyStorePassPhraseEncrypted")) {
         var3 = "getKeyStorePassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeyStorePassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("KeyStorePassPhraseEncrypted", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("KeyStorePassPhraseEncrypted", var2);
         var2.setValue("description", "<p>Returns encrypted pass phrase defined when creating the keystore.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaximumPoolSize")) {
         var3 = "getMaximumPoolSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaximumPoolSize";
         }

         var2 = new PropertyDescriptor("MaximumPoolSize", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("MaximumPoolSize", var2);
         var2.setValue("description", "<p>The maximum number of connections that can be made from this Jolt connection pool.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
      }

      if (!var1.containsKey("MinimumPoolSize")) {
         var3 = "getMinimumPoolSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMinimumPoolSize";
         }

         var2 = new PropertyDescriptor("MinimumPoolSize", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("MinimumPoolSize", var2);
         var2.setValue("description", "<p>The minimum number of connections to be added to this Jolt connection pool when WebLogic Server starts.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("PrimaryAddresses")) {
         var3 = "getPrimaryAddresses";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrimaryAddresses";
         }

         var2 = new PropertyDescriptor("PrimaryAddresses", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("PrimaryAddresses", var2);
         var2.setValue("description", "<p>The list of addresses for the primary Jolt Server Listeners (JSLs) on the Tuxedo system.</p>  <p>The format of each address is: <tt>//hostname:port</tt>. Multiple addresses should be separated by commas.</p> ");
      }

      if (!var1.containsKey("RecvTimeout")) {
         var3 = "getRecvTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRecvTimeout";
         }

         var2 = new PropertyDescriptor("RecvTimeout", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("RecvTimeout", var2);
         var2.setValue("description", "<p>The number of seconds the client waits to receive a response before timing out.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("TrustStoreName")) {
         var3 = "getTrustStoreName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTrustStoreName";
         }

         var2 = new PropertyDescriptor("TrustStoreName", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("TrustStoreName", var2);
         var2.setValue("description", "<p>The path and file name of the keystore containing the trust certificates.</p> ");
      }

      if (!var1.containsKey("TrustStorePassPhrase")) {
         var3 = "getTrustStorePassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTrustStorePassPhrase";
         }

         var2 = new PropertyDescriptor("TrustStorePassPhrase", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("TrustStorePassPhrase", var2);
         var2.setValue("description", "<p>The encrypted trust keystore's passphrase. If empty or null, then the keystore will be opened without a passphrase.</p>  <p>When you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>TrustStorePassPhraseEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>TrustStorePassPhraseEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>TrustStorePassPhrase</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>TrustStorePassPhraseEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getTrustStorePassPhraseEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("TrustStorePassPhraseEncrypted")) {
         var3 = "getTrustStorePassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTrustStorePassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("TrustStorePassPhraseEncrypted", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("TrustStorePassPhraseEncrypted", var2);
         var2.setValue("description", "<p>Returns encrypted pass phrase defined when creating the keystore.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UserName")) {
         var3 = "getUserName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserName";
         }

         var2 = new PropertyDescriptor("UserName", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("UserName", var2);
         var2.setValue("description", "<p>A user name that applications specify to connect to this Jolt connection pool. If Security Context is enabled, this name must be the name of an authorized Tuxedo user. (Specifying a Tuxedo user name is required if the Tuxedo authentication level is <code>USER_AUTH</code>.)</p> ");
      }

      if (!var1.containsKey("UserPassword")) {
         var3 = "getUserPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserPassword";
         }

         var2 = new PropertyDescriptor("UserPassword", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("UserPassword", var2);
         var2.setValue("description", "<p>The user password for this Jolt connection pool.</p> <p>As of 8.1 sp4, when you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>UserPasswordEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>UserPasswordEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>UserPassword</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>UserPasswordEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getUserPasswordEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
         var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowedGet", var6);
      }

      if (!var1.containsKey("UserPasswordEncrypted")) {
         var3 = "getUserPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("UserPasswordEncrypted", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("UserPasswordEncrypted", var2);
         var2.setValue("description", "<p>The user password for this connection pool.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
         var5 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowedGet", var5);
      }

      if (!var1.containsKey("UserRole")) {
         var3 = "getUserRole";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserRole";
         }

         var2 = new PropertyDescriptor("UserRole", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("UserRole", var2);
         var2.setValue("description", "<p>The Tuxedo user role for this Jolt connection pool. (This is required only when the security level in the Tuxedo domain is <tt>USER_AUTH</tt>, <tt>ACL</tt>, or <tt>MANDATORY_ACL</tt>).</p> ");
      }

      if (!var1.containsKey("SecurityContextEnabled")) {
         var3 = "isSecurityContextEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSecurityContextEnabled";
         }

         var2 = new PropertyDescriptor("SecurityContextEnabled", JoltConnectionPoolMBean.class, var3, var4);
         var1.put("SecurityContextEnabled", var2);
         var2.setValue("description", "<p>Indicates whether this Jolt connection pool passes the connection pool's security context (user name, password and other information) from the WebLogic Server user to the Tuxedo domain.</p>  <p>If you enable the connection pool to pass the security context, you must start the Jolt Service Handler (JSH) with the <code>-a</code> option. When the JSH gets a message with the caller's identity, it calls <code>impersonate_user()</code> to get the appkey for the user. JSH caches the appkey, so the next time the caller makes a request, the appkey is retrieved from the cache and the request is forwarded to the service. A cache is maintained by each JSH, which means that there will be a cache maintained for all the session pools connected to the same JSH.</p>  <p>You must enable Security Context if Tuxedo requires secured connections.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JoltConnectionPoolMBean.class.getMethod("addPrimaryAddress", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("address", "The feature to be added to the PrimaryAddress attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a feature to the PrimaryAddress attribute of the JoltConnectionPoolMBean object</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "PrimaryAddresses");
      }

      var3 = JoltConnectionPoolMBean.class.getMethod("removePrimaryAddress", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("address", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "PrimaryAddresses");
      }

      var3 = JoltConnectionPoolMBean.class.getMethod("addFailoverAddress", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("address", "The feature to be added to the FailoverAddress attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a feature to the FailoverAddress attribute of the JoltConnectionPoolMBean object</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "FailoverAddresses");
      }

      var3 = JoltConnectionPoolMBean.class.getMethod("removeFailoverAddress", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("address", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "FailoverAddresses");
      }

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
