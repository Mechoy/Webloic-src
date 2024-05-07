package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class ServerMBeanImplBeanInfo extends KernelMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ServerMBean.class;

   public ServerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This class represents a WebLogic Server. A WebLogic Server is a Java process that is a container for J2EE applications.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ServerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      String[] var5;
      if (!var1.containsKey("AcceptBacklog")) {
         var3 = "getAcceptBacklog";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAcceptBacklog";
         }

         var2 = new PropertyDescriptor("AcceptBacklog", ServerMBean.class, var3, var4);
         var1.put("AcceptBacklog", var2);
         var2.setValue("description", "<p>The number of backlogged, new TCP connection requests that should be allowed for this server's regular and SSL ports.</p>  <p>Setting the backlog to <code>0</code> may prevent this server from accepting any incoming connection on some operating systems.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getListenPort"), BeanInfoHelper.encodeEntities("#getAcceptBacklog"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getAcceptBacklog")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(300));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AdminReconnectIntervalSeconds")) {
         var3 = "getAdminReconnectIntervalSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdminReconnectIntervalSeconds";
         }

         var2 = new PropertyDescriptor("AdminReconnectIntervalSeconds", ServerMBean.class, var3, var4);
         var1.put("AdminReconnectIntervalSeconds", var2);
         var2.setValue("description", "<p>The number of seconds between reconnection attempts to the admin server. When the admin server fails the managed server will periodically try to connect back to it.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AdministrationPort")) {
         var3 = "getAdministrationPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdministrationPort";
         }

         var2 = new PropertyDescriptor("AdministrationPort", ServerMBean.class, var3, var4);
         var1.put("AdministrationPort", var2);
         var2.setValue("description", "<p>The secure administration port for the server. This port requires that you enable the domain's administration port and that SSL is configured and enabled.</p>  <p>By default, the server uses the administration port that is specified at the domain level. To override the domain-level administration port for the current server instance, set this server's administration port. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.DomainMBean#isAdministrationPortEnabled"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.DomainMBean#getAdministrationPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getSSL")};
         var2.setValue("see", var5);
         var2.setValue("secureValue", new Integer(7002));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AutoKillIfFailed")) {
         var3 = "getAutoKillIfFailed";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAutoKillIfFailed";
         }

         var2 = new PropertyDescriptor("AutoKillIfFailed", ServerMBean.class, var3, var4);
         var1.put("AutoKillIfFailed", var2);
         var2.setValue("description", "<p>Specifies whether the Node Manager should automatically kill this server if its health state is <code>failed</code>.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AutoRestart")) {
         var3 = "getAutoRestart";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAutoRestart";
         }

         var2 = new PropertyDescriptor("AutoRestart", ServerMBean.class, var3, var4);
         var1.put("AutoRestart", var2);
         var2.setValue("description", "<p>Specifies whether the Node Manager can automatically restart this server if it crashes or otherwise goes down unexpectedly.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("COM")) {
         var3 = "getCOM";
         var4 = null;
         var2 = new PropertyDescriptor("COM", ServerMBean.class, var3, var4);
         var1.put("COM", var2);
         var2.setValue("description", "<p>Returns the server's COM configuration.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("CandidateMachines")) {
         var3 = "getCandidateMachines";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCandidateMachines";
         }

         var2 = new PropertyDescriptor("CandidateMachines", ServerMBean.class, var3, var4);
         var1.put("CandidateMachines", var2);
         var2.setValue("description", "<p>Limits the list of candidate machines that the cluster specifies. (Requires you to enable this server for automatic migration and to configure the cluster with a set of candidate machines.)</p>  <p>If this server fails and if it is enabled for automatic migration, Node Manager automatically restarts it. By default, Node Manager restarts the server on any of the candidate machines that the cluster specifies (in order of preference that you configured in the cluster). To change the default, you use this server's list of candidate machines to create a subset of the cluster-wide candidates. You can also change the order of preference.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("ClusterMBean#getCandidateMachines")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("Cluster")) {
         var3 = "getCluster";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCluster";
         }

         var2 = new PropertyDescriptor("Cluster", ServerMBean.class, var3, var4);
         var1.put("Cluster", var2);
         var2.setValue("description", "<p>The cluster, or group of WebLogic Server instances, to which this server belongs.</p>  <p>If set, the server will listen for cluster multicast events.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getClusterWeight"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ClusterMBean#getMulticastPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ClusterMBean#getMulticastAddress")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("ClusterRuntime")) {
         var3 = "getClusterRuntime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClusterRuntime";
         }

         var2 = new PropertyDescriptor("ClusterRuntime", ServerMBean.class, var3, var4);
         var1.put("ClusterRuntime", var2);
         var2.setValue("description", "<p>This method is unsupported and always returns null. Use ServerRuntimeMBean.getClusterRuntime instead.</p> ");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("ClusterWeight")) {
         var3 = "getClusterWeight";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClusterWeight";
         }

         var2 = new PropertyDescriptor("ClusterWeight", ServerMBean.class, var3, var4);
         var1.put("ClusterWeight", var2);
         var2.setValue("description", "<p>The proportion of the load that this server will bear, relative to other servers in a cluster.</p>  <p>If all servers have the default weight or the same weight, each bears an equal proportion of the load. If one server has weight 50 and all other servers have weight 100, the 50-weight server will bear half as much load as any other server.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getCluster")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(100));
         var2.setValue("legalMax", new Integer(100));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("CoherenceClusterSystemResource")) {
         var3 = "getCoherenceClusterSystemResource";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCoherenceClusterSystemResource";
         }

         var2 = new PropertyDescriptor("CoherenceClusterSystemResource", ServerMBean.class, var3, var4);
         var1.put("CoherenceClusterSystemResource", var2);
         var2.setValue("description", "The system-level Coherence cluster resource associated with this server. ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("ConsensusProcessIdentifier")) {
         var3 = "getConsensusProcessIdentifier";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConsensusProcessIdentifier";
         }

         var2 = new PropertyDescriptor("ConsensusProcessIdentifier", ServerMBean.class, var3, var4);
         var1.put("ConsensusProcessIdentifier", var2);
         var2.setValue("description", "<p>Specifies the identifier to be used for consensus-based algorithms. Each server should have a unique identifier indexed from 0.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomIdentityKeyStoreFileName")) {
         var3 = "getCustomIdentityKeyStoreFileName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomIdentityKeyStoreFileName";
         }

         var2 = new PropertyDescriptor("CustomIdentityKeyStoreFileName", ServerMBean.class, var3, var4);
         var1.put("CustomIdentityKeyStoreFileName", var2);
         var2.setValue("description", "<p>The path and file name of the identity keystore.</p>  <p>The path name must either be absolute or relative to where the server was booted. The custom identity key store file name is only used if KeyStores is CUSTOM_IDENTITY_AND_JAVA_STANDARD_TRUST, CUSTOM_IDENTITY_AND_CUSTOM_TRUST or CUSTOM_IDENTITY_AND_COMMAND_LINE_TRUST.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomIdentityKeyStorePassPhrase")) {
         var3 = "getCustomIdentityKeyStorePassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomIdentityKeyStorePassPhrase";
         }

         var2 = new PropertyDescriptor("CustomIdentityKeyStorePassPhrase", ServerMBean.class, var3, var4);
         var1.put("CustomIdentityKeyStorePassPhrase", var2);
         var2.setValue("description", "<p>The encrypted custom identity keystore's passphrase. If empty or null, then the keystore will be opened without a passphrase.</p>  <p>This attribute is only used if KeyStores is CUSTOM_IDENTITY_AND_JAVA_STANDARD_TRUST, CUSTOM_IDENTITY_AND_CUSTOM_TRUST or CUSTOM_IDENTITY_AND_COMMAND_LINE_TRUST.</p>  <p>When you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>CustomIdentityKeyStorePassPhraseEncrypted</code> attribute.</li> <li>Decrypts the value and returns the unencrypted password as a String.</li> </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>CustomIdentityKeyStorePassPhraseEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>CustomIdentityKeyStorePassPhrase</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>CustomIdentityKeyStorePassPhraseEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getCustomIdentityKeyStorePassPhraseEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomIdentityKeyStorePassPhraseEncrypted")) {
         var3 = "getCustomIdentityKeyStorePassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomIdentityKeyStorePassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("CustomIdentityKeyStorePassPhraseEncrypted", ServerMBean.class, var3, var4);
         var1.put("CustomIdentityKeyStorePassPhraseEncrypted", var2);
         var2.setValue("description", "<p>Returns encrypted pass phrase defined when creating the keystore.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomIdentityKeyStoreType")) {
         var3 = "getCustomIdentityKeyStoreType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomIdentityKeyStoreType";
         }

         var2 = new PropertyDescriptor("CustomIdentityKeyStoreType", ServerMBean.class, var3, var4);
         var1.put("CustomIdentityKeyStoreType", var2);
         var2.setValue("description", "<p>The type of the keystore. Generally, this is <code>JKS</code>.</p>  <p>If empty or null, then the JDK's default keystore type (specified in <code>java.security</code>) is used. The custom identity key store type is only used if KeyStores is CUSTOM_IDENTITY_AND_JAVA_STANDARD_TRUST, CUSTOM_IDENTITY_AND_CUSTOM_TRUST or CUSTOM_IDENTITY_AND_COMMAND_LINE_TRUST.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomTrustKeyStoreFileName")) {
         var3 = "getCustomTrustKeyStoreFileName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomTrustKeyStoreFileName";
         }

         var2 = new PropertyDescriptor("CustomTrustKeyStoreFileName", ServerMBean.class, var3, var4);
         var1.put("CustomTrustKeyStoreFileName", var2);
         var2.setValue("description", "<p>The path and file name of the custom trust keystore.</p>  <p>The path name must either be absolute or relative to where the server was booted. This file name is only used if KeyStores is CUSTOM_IDENTITY_AND_CUSTOM_TRUST.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomTrustKeyStorePassPhrase")) {
         var3 = "getCustomTrustKeyStorePassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomTrustKeyStorePassPhrase";
         }

         var2 = new PropertyDescriptor("CustomTrustKeyStorePassPhrase", ServerMBean.class, var3, var4);
         var1.put("CustomTrustKeyStorePassPhrase", var2);
         var2.setValue("description", "<p>The custom trust keystore's passphrase. If empty or null, then the keystore will be opened without a passphrase.</p>  <p>This attribute is only used if KeyStores is CUSTOM_IDENTITY_AND_CUSTOM_TRUST.</p>  <p>When you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>CustomTrustKeyStorePassPhraseEncrypted</code> attribute.</li> <li>Decrypts the value and returns the unencrypted password as a String.</li> </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>CustomTrustKeyStorePassPhraseEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>CustomTrustKeyStorePassPhrase</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>CustomTrustKeyStorePassPhraseEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getCustomTrustKeyStorePassPhraseEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomTrustKeyStorePassPhraseEncrypted")) {
         var3 = "getCustomTrustKeyStorePassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomTrustKeyStorePassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("CustomTrustKeyStorePassPhraseEncrypted", ServerMBean.class, var3, var4);
         var1.put("CustomTrustKeyStorePassPhraseEncrypted", var2);
         var2.setValue("description", "<p>The custom trust keystore's encrypted passphrase. If empty or null, then the keystore will be opened without a passphrase.</p>  <p>This attribute is only used if KeyStores is CUSTOM_IDENTITY_AND_CUSTOM_TRUST.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method. </p>  <p>To compare a password that a user enters with the encrypted value of this attribute, use the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomTrustKeyStoreType")) {
         var3 = "getCustomTrustKeyStoreType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomTrustKeyStoreType";
         }

         var2 = new PropertyDescriptor("CustomTrustKeyStoreType", ServerMBean.class, var3, var4);
         var1.put("CustomTrustKeyStoreType", var2);
         var2.setValue("description", "<p>The type of the keystore. Generally, this is <code>JKS</code>.</p>  <p>If empty or null, then the JDK's default keystore type (specified in java.security) is used. This keystore type is only used if KeyStores is CUSTOM_IDENTITY_AND_CUSTOM_TRUST.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DataSource")) {
         var3 = "getDataSource";
         var4 = null;
         var2 = new PropertyDescriptor("DataSource", ServerMBean.class, var3, var4);
         var1.put("DataSource", var2);
         var2.setValue("description", "<p>The data source configured for the persistent TLOG JDBC store used for transaction logging. A server has exactly one DataSource.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("DefaultFileStore")) {
         var3 = "getDefaultFileStore";
         var4 = null;
         var2 = new PropertyDescriptor("DefaultFileStore", ServerMBean.class, var3, var4);
         var1.put("DefaultFileStore", var2);
         var2.setValue("description", "<p>Controls the configuration of the default persistent store on this server. Each server has a default store, which is a file-based object repository used by various subsystems.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("DefaultIIOPPassword")) {
         var3 = "getDefaultIIOPPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultIIOPPassword";
         }

         var2 = new PropertyDescriptor("DefaultIIOPPassword", ServerMBean.class, var3, var4);
         var1.put("DefaultIIOPPassword", var2);
         var2.setValue("description", "<p>The password for the default IIOP user. (Requires you to enable IIOP.)</p> <p>As of 8.1 sp4, when you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>DefaultIIOPPasswordEncrypted</code> attribute.</li> <li>Decrypts the value and returns the unencrypted password as a String.</li> </ol>  <p>Using this attribute (<code>DefaultIIOPPassword</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>DefaultIIOPPasswordEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isIIOPEnabled"), BeanInfoHelper.encodeEntities("#getDefaultIIOPPasswordEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultIIOPPasswordEncrypted")) {
         var3 = "getDefaultIIOPPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultIIOPPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("DefaultIIOPPasswordEncrypted", ServerMBean.class, var3, var4);
         var1.put("DefaultIIOPPasswordEncrypted", var2);
         var2.setValue("description", "<p>The encrypted password for the default IIOP user.</p> <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultIIOPUser")) {
         var3 = "getDefaultIIOPUser";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultIIOPUser";
         }

         var2 = new PropertyDescriptor("DefaultIIOPUser", ServerMBean.class, var3, var4);
         var1.put("DefaultIIOPUser", var2);
         var2.setValue("description", "<p>The user name of the default IIOP user. (Requires you to enable IIOP.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isIIOPEnabled")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultTGIOPPassword")) {
         var3 = "getDefaultTGIOPPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultTGIOPPassword";
         }

         var2 = new PropertyDescriptor("DefaultTGIOPPassword", ServerMBean.class, var3, var4);
         var1.put("DefaultTGIOPPassword", var2);
         var2.setValue("description", "<p>The password for the default user associated with the Tuxedo GIOP (TGIOP) protocol. (Requires you to configure WebLogic Tuxedo Connector (WTC) for this server.)</p>  <p>As of 8.1 sp4, when you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>DefaultTGIOPPasswordEncrypted</code> attribute.</li> <li>Decrypts the value and returns the unencrypted password as a String.</li> </ol> <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>DefaultTGIOPPasswordEncrypted</code> attribute to the encrypted value.</li> </ol>  <p>Using this attribute (<code>DefaultTGIOPPassword</code>) is a potential security risk in because the String object (which contains the unencrypted password) remains the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>DefaultTGIOPPasswordEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getDefaultTGIOPPasswordEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultTGIOPPasswordEncrypted")) {
         var3 = "getDefaultTGIOPPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultTGIOPPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("DefaultTGIOPPasswordEncrypted", ServerMBean.class, var3, var4);
         var1.put("DefaultTGIOPPasswordEncrypted", var2);
         var2.setValue("description", "<p>The encrypted password for the default TGIOP user.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultTGIOPUser")) {
         var3 = "getDefaultTGIOPUser";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultTGIOPUser";
         }

         var2 = new PropertyDescriptor("DefaultTGIOPUser", ServerMBean.class, var3, var4);
         var1.put("DefaultTGIOPUser", var2);
         var2.setValue("description", "<p>The default user associated with the Tuxedo GIOP (TGIOP) protocol. (Requires you to configure WebLogic Tuxedo Connector (WTC) for this server.)</p> ");
         setPropertyDescriptorDefault(var2, "guest");
         var2.setValue("secureValueNull", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("DomainLogFilter")) {
         var3 = "getDomainLogFilter";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDomainLogFilter";
         }

         var2 = new PropertyDescriptor("DomainLogFilter", ServerMBean.class, var3, var4);
         var1.put("DomainLogFilter", var2);
         var2.setValue("description", "<p>Determines which messages this server sends to the domain log. (Requires you to enable domain logging for this server.)</p> <p>If you specify <code>none</code>, the server sends all messages of severity <code>WARNING</code> and higher. This list contains all Domain Log Filters that have been defined for the domain. A server can user only one Domain Log Filter.</p> ");
         var2.setValue("secureValue", "none");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 Replaced by LogMBean.LogBroadcastFilter The severity of messages going to the domain log is configured separately through LogMBean.LogBroadcastSeverity, For backward compatibility the changes to this attribute will be propagated to the LogMBean. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("ExpectedToRun")) {
         var3 = "getExpectedToRun";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExpectedToRun";
         }

         var2 = new PropertyDescriptor("ExpectedToRun", ServerMBean.class, var3, var4);
         var1.put("ExpectedToRun", var2);
         var2.setValue("description", "<p>If this server expected to run if the domain is started.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#stop"), BeanInfoHelper.encodeEntities("#kill")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ExternalDNSName")) {
         var3 = "getExternalDNSName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExternalDNSName";
         }

         var2 = new PropertyDescriptor("ExternalDNSName", ServerMBean.class, var3, var4);
         var1.put("ExternalDNSName", var2);
         var2.setValue("description", "<p>The external IP address or DNS name for this server.</p>  <p>This address will be sent with HTTP session cookies and with dynamic server lists to HTTP proxies. It will also be used by external application clients to enable the propagation of RMI traffic through network address translating (NAT) firewalls.</p>  <p>You must specify an external DNS name for configurations in which a firewall is performing network address translation, unless clients are accessing WebLogic Server using t3 and the default channel. For example, define the external DNS name for configurations in which a firewall is performing network address translation, and clients are accessing WebLogic Server using HTTP via a proxy plug-in.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getListenAddress"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getExternalDNSName")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("ExtraEjbcOptions")) {
         var3 = "getExtraEjbcOptions";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExtraEjbcOptions";
         }

         var2 = new PropertyDescriptor("ExtraEjbcOptions", ServerMBean.class, var3, var4);
         var1.put("ExtraEjbcOptions", var2);
         var2.setValue("description", "<p>The options passed to the EJB compiler during server-side generation.</p>  <p>Each EJB component can override the compiler options that you specify here. The following options are valid:</p>  <dl> <dt>-forcegeneration</dt> <dd>Forces generation of wrapper classes. Without this flag the classes may not be regenerated if it is determined to be unnecessary.</dd>  <dt>-disableHotCodeGen</dt> <dd>Generate ejb stub and skel as part of ejbc. Avoid HotCodeGen to have better performance. </dd>  <dt>-keepgenerated</dt> <dd>Keep the generated .java files.</dd>   <dt>-compiler javac</dt> <dd>Java compiler to exec.  If not specified, the -compilerclass option will be used.</dd>  <dt>-compilerclass com.sun.tools.javac.Main</dt> <dd>Specifies the compiler class to invoke.</dd>  <dt>-g</dt> <dd>Compile debugging info into class file.</dd>  <dt>-normi</dt> <dd>Passed through to Symantec's sj.</dd>  <dt>-classpath path</dt> <dd>Classpath to use.</dd>  <dt>-source source</dt> <dd>Source version.</dd>  <dt>-J<i>option</i></dt> <dd>Flags passed through to java runtime.</dd> </dl> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.EJBContainerMBean#getExtraEjbcOptions()")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
      }

      if (!var1.containsKey("ExtraRmicOptions")) {
         var3 = "getExtraRmicOptions";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExtraRmicOptions";
         }

         var2 = new PropertyDescriptor("ExtraRmicOptions", ServerMBean.class, var3, var4);
         var1.put("ExtraRmicOptions", var2);
         var2.setValue("description", "<p>The options passed to the RMIC compiler during server-side generation.</p>  <p>Each EJB component can override the compiler options that you specify here.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.EJBContainerMBean#getExtraRmicOptions()")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.1.0.0", (String)null, this.targetVersion) && !var1.containsKey("FederationServices")) {
         var3 = "getFederationServices";
         var4 = null;
         var2 = new PropertyDescriptor("FederationServices", ServerMBean.class, var3, var4);
         var1.put("FederationServices", var2);
         var2.setValue("description", "<p>Gets the Federation Services MBean</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.1.0.0");
      }

      if (!var1.containsKey("GracefulShutdownTimeout")) {
         var3 = "getGracefulShutdownTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGracefulShutdownTimeout";
         }

         var2 = new PropertyDescriptor("GracefulShutdownTimeout", ServerMBean.class, var3, var4);
         var1.put("GracefulShutdownTimeout", var2);
         var2.setValue("description", "<p>Number of seconds a graceful shutdown operation waits before forcing a shut down. A graceful shutdown gives WebLogic Server subsystems time to complete certain application processing currently in progress. If subsystems are unable to complete processing within the number of seconds that you specify here, then the server will force shutdown automatically.</p>  <p>A value of <code>0</code> means that the server will wait indefinitely for graceful shutdown to complete.</p>  <p>The graceful shutdown timeout applies only to graceful shutdown operations.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getServerLifeCycleTimeoutVal")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HealthCheckIntervalSeconds")) {
         var3 = "getHealthCheckIntervalSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHealthCheckIntervalSeconds";
         }

         var2 = new PropertyDescriptor("HealthCheckIntervalSeconds", ServerMBean.class, var3, var4);
         var1.put("HealthCheckIntervalSeconds", var2);
         var2.setValue("description", "<p>The number of seconds that defines the frequency of this server's self-health monitoring. The server monitors the health of it's subsystems every HealthCheckIntervalSeconds and changes the Server's overall state if required. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(180));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HealthCheckStartDelaySeconds")) {
         var3 = "getHealthCheckStartDelaySeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHealthCheckStartDelaySeconds";
         }

         var2 = new PropertyDescriptor("HealthCheckStartDelaySeconds", ServerMBean.class, var3, var4);
         var1.put("HealthCheckStartDelaySeconds", var2);
         var2.setValue("description", "<p>The number of seconds the Node Manager should wait before starting to monitor the server.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(120));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HealthCheckTimeoutSeconds")) {
         var3 = "getHealthCheckTimeoutSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHealthCheckTimeoutSeconds";
         }

         var2 = new PropertyDescriptor("HealthCheckTimeoutSeconds", ServerMBean.class, var3, var4);
         var1.put("HealthCheckTimeoutSeconds", var2);
         var2.setValue("description", "<p>The number of seconds the Node Manager should wait before timing out its health query to this server.</p>  <p>If the timeout is reached, Node Managed assumes the Managed Server has failed.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("deprecated", "9.0.0.0 Replaced by Server self-health monitoring that takes action without NodeManager intervention. NodeManager identifies if a running server was shutdown due to a restartable failure and restarts the server. ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HostsMigratableServices")) {
         var3 = "getHostsMigratableServices";
         var4 = null;
         var2 = new PropertyDescriptor("HostsMigratableServices", ServerMBean.class, var3, var4);
         var1.put("HostsMigratableServices", var2);
         var2.setValue("description", "<p>Gets the hostsMigratableServices attribute of the ServerMBean object</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("InterfaceAddress")) {
         var3 = "getInterfaceAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInterfaceAddress";
         }

         var2 = new PropertyDescriptor("InterfaceAddress", ServerMBean.class, var3, var4);
         var1.put("InterfaceAddress", var2);
         var2.setValue("description", "<p>The IP address of the NIC that this server should use for multicast traffic.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getCluster"), BeanInfoHelper.encodeEntities("#setInterfaceAddress"), BeanInfoHelper.encodeEntities("ClusterMBean#getMulticastAddress")};
         var2.setValue("see", var5);
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("JDBCLLRTableName")) {
         var3 = "getJDBCLLRTableName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJDBCLLRTableName";
         }

         var2 = new PropertyDescriptor("JDBCLLRTableName", ServerMBean.class, var3, var4);
         var1.put("JDBCLLRTableName", var2);
         var2.setValue("description", "<p>The table name for this server's Logging Last Resource (LLR) database table(s). WebLogic Server creates the table(s) and then uses them during transaction processing for the LLR transaction optimization. This setting must be unique for each server. The default table name is <code>WL_LLR_<i>SERVERNAME</i></code>.</p>  <p>This setting only applies if this server hosts one or more LLR-enabled JDBC data sources.</p>  <p>The format for the tables that WebLogic Server creates is [[[catalog.]schema.]name.] Each \".\" in the table name is significant, and schema generally corresponds to username in many databases.</p>  <p><b>IMPORTANT:</b> If this value is changed but the LLR table already exists in the database, you must preserve the existing table's data. Consequently, when changing the table name, the existing database table must be renamed by a database administrator to match the new configured table name. Otherwise, transaction records may be lost, resulting in heuristic failures that aren't logged.</p>  <p><b>IMPORTANT:</b> Each server's table name must be unique. Multiple LLR-enabled data sources within the same server may share the same table, but multiple servers must not share the same table. If multiple same-named servers share a table, the behavior is undefined and it is likely that transactions will not recover properly after a crash, creating heuristic hazards.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JDBCLogFileName")) {
         var3 = "getJDBCLogFileName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJDBCLogFileName";
         }

         var2 = new PropertyDescriptor("JDBCLogFileName", ServerMBean.class, var3, var4);
         var1.put("JDBCLogFileName", var2);
         var2.setValue("description", "<p>The name of the JDBC log file. If the pathname is not absolute, it is assumed to be relative to the root directory of the machine on which this server is running. (Requires you to enable JDBC logging.)</p>  <p>If the log has no path element and is atomic (for example, <code>jdbc.log</code>), the file will be placed relative to the root directory in ./SERVER_NAME/ to avoid name space conflicts. This attribute is deprecated because the JDBC output now goes in the server log.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isJDBCLoggingEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "jdbc.log");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("JDBCLoginTimeoutSeconds")) {
         var3 = "getJDBCLoginTimeoutSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJDBCLoginTimeoutSeconds";
         }

         var2 = new PropertyDescriptor("JDBCLoginTimeoutSeconds", ServerMBean.class, var3, var4);
         var1.put("JDBCLoginTimeoutSeconds", var2);
         var2.setValue("description", "The JDBC Login Timeout value. Specified value is passed into java.sql.DriverManager.setLoginTimeout(). Note that this DriverManager setting will impact *all* JDBC drivers loaded into this JVM. Feature is disabled by default. ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(300));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("JNDITransportableObjectFactoryList")) {
         var3 = "getJNDITransportableObjectFactoryList";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDITransportableObjectFactoryList";
         }

         var2 = new PropertyDescriptor("JNDITransportableObjectFactoryList", ServerMBean.class, var3, var4);
         var1.put("JNDITransportableObjectFactoryList", var2);
         var2.setValue("description", "<p>List of factories that create transportable objects.</p> ");
      }

      if (!var1.containsKey("JTAMigratableTarget")) {
         var3 = "getJTAMigratableTarget";
         var4 = null;
         var2 = new PropertyDescriptor("JTAMigratableTarget", ServerMBean.class, var3, var4);
         var1.put("JTAMigratableTarget", var2);
         var2.setValue("description", "<p>Returns the JTAMigratableTargetMBean that is used to deploy the JTA Recovery Service to (is the server's cluster is not null).</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("JavaCompiler")) {
         var3 = "getJavaCompiler";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJavaCompiler";
         }

         var2 = new PropertyDescriptor("JavaCompiler", ServerMBean.class, var3, var4);
         var1.put("JavaCompiler", var2);
         var2.setValue("description", "<p>The Java compiler to use for all applications hosted on this server that need to compile Java code.</p> ");
         setPropertyDescriptorDefault(var2, "javac");
         var2.setValue("secureValue", "javac");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JavaCompilerPostClassPath")) {
         var3 = "getJavaCompilerPostClassPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJavaCompilerPostClassPath";
         }

         var2 = new PropertyDescriptor("JavaCompilerPostClassPath", ServerMBean.class, var3, var4);
         var1.put("JavaCompilerPostClassPath", var2);
         var2.setValue("description", "<p>The options to append to the Java compiler classpath when compiling Java code.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValueNull", Boolean.TRUE);
      }

      if (!var1.containsKey("JavaCompilerPreClassPath")) {
         var3 = "getJavaCompilerPreClassPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJavaCompilerPreClassPath";
         }

         var2 = new PropertyDescriptor("JavaCompilerPreClassPath", ServerMBean.class, var3, var4);
         var1.put("JavaCompilerPreClassPath", var2);
         var2.setValue("description", "<p>The options to prepend to the Java compiler classpath when compiling Java code.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValueNull", Boolean.TRUE);
      }

      if (!var1.containsKey("JavaStandardTrustKeyStorePassPhrase")) {
         var3 = "getJavaStandardTrustKeyStorePassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJavaStandardTrustKeyStorePassPhrase";
         }

         var2 = new PropertyDescriptor("JavaStandardTrustKeyStorePassPhrase", ServerMBean.class, var3, var4);
         var1.put("JavaStandardTrustKeyStorePassPhrase", var2);
         var2.setValue("description", "<p>The password for the Java Standard Trust keystore. This password is defined when the keystore is created.</p> <p>If empty or null, then the keystore will be opened without a passphrase.</p>  <p>This attribute is only used if KeyStores is CUSTOM_IDENTITY_AND_JAVA_STANDARD_TRUST or DEMO_IDENTITY_AND_DEMO_TRUST.</p>  <p>When you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>JavaStandardTrustKeyStorePassPhraseEncrypted</code> attribute.</li> <li>Decrypts the value and returns the unencrypted password as a String.</li> </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>JavaStandardTrustKeyStorePassPhraseEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>JavaStandardTrustKeyStorePassPhrase</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>JavaStandardTrustKeyStorePassPhraseEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getJavaStandardTrustKeyStorePassPhraseEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("JavaStandardTrustKeyStorePassPhraseEncrypted")) {
         var3 = "getJavaStandardTrustKeyStorePassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJavaStandardTrustKeyStorePassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("JavaStandardTrustKeyStorePassPhraseEncrypted", ServerMBean.class, var3, var4);
         var1.put("JavaStandardTrustKeyStorePassPhraseEncrypted", var2);
         var2.setValue("description", "<p>The encrypted password for the Java Standard Trust keystore. This password is defined when the keystore is created.</p>  <p>To set this attribute,  use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("KernelDebug")) {
         var3 = "getKernelDebug";
         var4 = null;
         var2 = new PropertyDescriptor("KernelDebug", ServerMBean.class, var3, var4);
         var1.put("KernelDebug", var2);
         var2.setValue("description", "<p>Get the debug flags for this kernel (will return a KernelDebugMBean if this is a KernelMBean) or the server (will return a ServerDebugMBean if this is a ServerMBean)</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("KeyStores")) {
         var3 = "getKeyStores";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeyStores";
         }

         var2 = new PropertyDescriptor("KeyStores", ServerMBean.class, var3, var4);
         var1.put("KeyStores", var2);
         var2.setValue("description", "<p>Which configuration rules should be used for finding the server's identity and trust keystores?</p> ");
         setPropertyDescriptorDefault(var2, "DemoIdentityAndDemoTrust");
         var2.setValue("legalValues", new Object[]{"DemoIdentityAndDemoTrust", "CustomIdentityAndJavaStandardTrust", "CustomIdentityAndCustomTrust", "CustomIdentityAndCommandLineTrust"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ListenAddress")) {
         var3 = "getListenAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenAddress";
         }

         var2 = new PropertyDescriptor("ListenAddress", ServerMBean.class, var3, var4);
         var1.put("ListenAddress", var2);
         var2.setValue("description", "<p>The IP address or DNS name this server uses to listen for incoming connections.</p>  <p>Servers can be reached through the following URL:<br> <i>protocol://listen-address:listen-port</i></p>  <p>Any network channel that you configure for this server can override this listen address.</p>  <p>If a server's listen address is undefined, clients can reach the server through an IP address of the computer that hosts the server, a DNS name that resolves to the host, or the localhost string. The localhost string can be used only for requests from clients that running on the same computer as the server.</p>  <p>If you want to limit the valid addresses for a server instance, specify one of the following:</p>  <ul> <li> <p>If you provide an IP address, clients can specify either the IP address or a DNS name that maps to the IP address. Clients that specify an IP address and attempt to connect through an SSL port must disable hostname verification.</p> </li>  <li> <p>If you provide a DNS name, clients can specify either the DNS name or the corresponding IP address.</p> </li> </ul>  <p>Do not leave the listen address undefined on a computer that uses multiple IP address (a multihomed computer). On such a computer, the server will bind to all available IP addresses.</p>  <dl> <dt>Notes:</dt>  <dd> <p>To resolve a DNS name to an IP address, WebLogic Server must be able to contact an appropriate DNS server or obtain the IP address mapping locally. Therefore, if you specify a DNS name for the listen address, you must either leave a port open long enough for the WebLogic Server instance to connect to a DNS server and cache its mapping or you must specify the IP address mapping in a local file. If you specify an IP address for ListenAddress and then a client request specifies a DNS name, WebLogic Server will attempt to resolve the DNS name, but if it cannot access DNS name mapping, the request will fail.</p> <p>Note also that you are using the demo certificates in a multi-server domain, Managed Server instances will fail to boot if you specify the fully-qualified DNS name. For information about this limitation and suggested workarounds, see \"Limitation on CertGen Usage\" in <i>Securing Oracle WebLogic Server</i>. </dd> </dl> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getExternalDNSName"), BeanInfoHelper.encodeEntities("#getListenPort"), BeanInfoHelper.encodeEntities("#getInterfaceAddress"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#getListenPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getSSL"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getListenAddress")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("ListenDelaySecs")) {
         var3 = "getListenDelaySecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenDelaySecs";
         }

         var2 = new PropertyDescriptor("ListenDelaySecs", ServerMBean.class, var3, var4);
         var1.put("ListenDelaySecs", var2);
         var2.setValue("description", "<p>Perpetuated for compatibility with 6.1 only.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("deprecated", " ");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("ListenPort")) {
         var3 = "getListenPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenPort";
         }

         var2 = new PropertyDescriptor("ListenPort", ServerMBean.class, var3, var4);
         var1.put("ListenPort", var2);
         var2.setValue("description", "<p>The default TCP port that this server uses to listen for regular (non-SSL) incoming connections.</p>  <p>Administrators must have the right privileges before binding to a port or else this operation will not be successful and it will render the console un-reachable.</p>  <p>If this port is disabled, the SSL port must be enabled. Additional ports can be configured using network channels. The cluster (multicast) port is configured separately.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isListenPortEnabled"), BeanInfoHelper.encodeEntities("#getAdministrationPort"), BeanInfoHelper.encodeEntities("#getListenAddress"), BeanInfoHelper.encodeEntities("#getCluster"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#getListenPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ClusterMBean#getMulticastPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getListenPort")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(7001));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ListenThreadStartDelaySecs")) {
         var3 = "getListenThreadStartDelaySecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenThreadStartDelaySecs";
         }

         var2 = new PropertyDescriptor("ListenThreadStartDelaySecs", ServerMBean.class, var3, var4);
         var1.put("ListenThreadStartDelaySecs", var2);
         var2.setValue("description", "<p>Returns the maximum time that the server will wait for server sockets to bind before starting a listen thread.</p>  <p>Properties to consider for removal</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
      }

      if (!var1.containsKey("ListenersBindEarly")) {
         var3 = "getListenersBindEarly";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenersBindEarly";
         }

         var2 = new PropertyDescriptor("ListenersBindEarly", ServerMBean.class, var3, var4);
         var1.put("ListenersBindEarly", var2);
         var2.setValue("description", "<p>Determines whether the server should bind server sockets early.</p> Early binding detects port conflicts quickly and also gives user feedback on the default listen port as to the server state. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LoginTimeout")) {
         var3 = "getLoginTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoginTimeout";
         }

         var2 = new PropertyDescriptor("LoginTimeout", ServerMBean.class, var3, var4);
         var1.put("LoginTimeout", var2);
         var2.setValue("description", "This does nothing. ");
         setPropertyDescriptorDefault(var2, new Integer(1000));
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("LoginTimeoutMillis")) {
         var3 = "getLoginTimeoutMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoginTimeoutMillis";
         }

         var2 = new PropertyDescriptor("LoginTimeoutMillis", ServerMBean.class, var3, var4);
         var1.put("LoginTimeoutMillis", var2);
         var2.setValue("description", "<p>The login timeout for this server's default regular (non-SSL) listen port. This is the maximum amount of time allowed for a new connection to establish.</p>  <p>A value of <code>0</code> indicates there is no maximum.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#getLoginTimeoutMillis"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getSSL"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getLoginTimeoutMillis")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(5000));
         var2.setValue("secureValue", new Integer(5000));
         var2.setValue("legalMax", new Integer(100000));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LowMemoryGCThreshold")) {
         var3 = "getLowMemoryGCThreshold";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLowMemoryGCThreshold";
         }

         var2 = new PropertyDescriptor("LowMemoryGCThreshold", ServerMBean.class, var3, var4);
         var1.put("LowMemoryGCThreshold", var2);
         var2.setValue("description", "<p>The threshold level (in percent) that this server uses for logging low memory conditions and changing the server health state to <tt>Warning</tt>.</p>  <p>For example, if you specify 5, the server logs a low memory warning in the log file and changes the server health state to <tt>Warning</tt> after the average free memory reaches 5% of the initial free memory measured at the server's boot time.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("secureValue", new Integer(5));
         var2.setValue("legalMax", new Integer(99));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("LowMemoryGranularityLevel")) {
         var3 = "getLowMemoryGranularityLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLowMemoryGranularityLevel";
         }

         var2 = new PropertyDescriptor("LowMemoryGranularityLevel", ServerMBean.class, var3, var4);
         var1.put("LowMemoryGranularityLevel", var2);
         var2.setValue("description", "<p>The granularity level (in percent) that this server uses for logging low memory conditions and changing the server health state to <tt>Warning</tt>.</p>  <p>For example, if you specify 5 and the average free memory drops by 5% or more over two measured intervals, the server logs a low memory warning in the log file and changes the server health state to <tt>Warning</tt>.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("secureValue", new Integer(5));
         var2.setValue("legalMax", new Integer(100));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("LowMemorySampleSize")) {
         var3 = "getLowMemorySampleSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLowMemorySampleSize";
         }

         var2 = new PropertyDescriptor("LowMemorySampleSize", ServerMBean.class, var3, var4);
         var1.put("LowMemorySampleSize", var2);
         var2.setValue("description", "<p>The number of times this server samples free memory during the time period specified by LowMemoryTimeInterval.</p>  <p>Increasing the sample size can improve the accuracy of the reading.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("LowMemoryTimeInterval")) {
         var3 = "getLowMemoryTimeInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLowMemoryTimeInterval";
         }

         var2 = new PropertyDescriptor("LowMemoryTimeInterval", ServerMBean.class, var3, var4);
         var1.put("LowMemoryTimeInterval", var2);
         var2.setValue("description", "<p>The amount of time (in seconds) that defines the interval over which this server determines average free memory values.</p>  <p>By default, the server obtains an average free memory value every 3600 seconds. This interval is not used if the JRockit VM is used, as the memory samples are collected immediately after a VM-scheduled garbage collection. Taking memory samples after a garbage collection gives a more accurate average value of the free memory.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(3600));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(300));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("Machine")) {
         var3 = "getMachine";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMachine";
         }

         var2 = new PropertyDescriptor("Machine", ServerMBean.class, var3, var4);
         var1.put("Machine", var2);
         var2.setValue("description", "<p>The WebLogic Server host computer (machine) on which this server is meant to run.</p>  <p>If you want to use a Node Manager to start this server, you must assign the server to a machine and you must configure the machine for the Node Manager.</p>  <p>You cannot change this value if a server instance is already running.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.2.3.0", (String)null, this.targetVersion) && !var1.containsKey("NMSocketCreateTimeoutInMillis")) {
         var3 = "getNMSocketCreateTimeoutInMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNMSocketCreateTimeoutInMillis";
         }

         var2 = new PropertyDescriptor("NMSocketCreateTimeoutInMillis", ServerMBean.class, var3, var4);
         var1.put("NMSocketCreateTimeoutInMillis", var2);
         var2.setValue("description", "Returns the timeout value to be used by NodeManagerRuntime when creating a a socket connection to the agent. Default set high as SSH agent may require a high connection establishment time. ");
         setPropertyDescriptorDefault(var2, new Integer(180000));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("since", "9.2.3.0");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", ServerMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>An alphanumeric name for this server instance. (Spaces are not valid.)</p>  <p>The name must be unique for all configuration objects in the domain. Within a domain, each server, machine, cluster, JDBC connection pool, virtual host, and any other resource type must be named uniquely and must not use the same name as the domain.</p>  <p>The server name is not used as part of the URL for applications that are deployed on the server. It is for your identification purposes only. The server name displays in the Administration Console, and if you use WebLogic Server command-line utilities or APIs, you use this name to identify the server.</p>  <p>After you have created a server, you cannot change its name. Instead, clone the server and provide a new name for the clone.</p> ");
         var2.setValue("key", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("NetworkAccessPoints")) {
         var3 = "getNetworkAccessPoints";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNetworkAccessPoints";
         }

         var2 = new PropertyDescriptor("NetworkAccessPoints", ServerMBean.class, var3, var4);
         var1.put("NetworkAccessPoints", var2);
         var2.setValue("description", "<p>Network access points, or \"NAPs\", define additional ports and addresses that this server listens on. Additionally, if two servers both support the same channel for a given protocol, then new connections between them will use that channel.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyNetworkAccessPoint");
         var2.setValue("creator", "createNetworkAccessPoint");
         var2.setValue("setterDeprecated", "9.0.0.0 Use createNetworkAccessPoint() instead. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "7.0.0.0");
      }

      if (!var1.containsKey("OverloadProtection")) {
         var3 = "getOverloadProtection";
         var4 = null;
         var2 = new PropertyDescriptor("OverloadProtection", ServerMBean.class, var3, var4);
         var1.put("OverloadProtection", var2);
         var2.setValue("description", "get attributes related to server overload protection ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("PreferredSecondaryGroup")) {
         var3 = "getPreferredSecondaryGroup";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPreferredSecondaryGroup";
         }

         var2 = new PropertyDescriptor("PreferredSecondaryGroup", ServerMBean.class, var3, var4);
         var1.put("PreferredSecondaryGroup", var2);
         var2.setValue("description", "<p>Defines secondary clustered instances considered for hosting replicas of the primary HTTP session states created on the server.</p> ");
      }

      if (!var1.containsKey("ReliableDeliveryPolicy")) {
         var3 = "getReliableDeliveryPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReliableDeliveryPolicy";
         }

         var2 = new PropertyDescriptor("ReliableDeliveryPolicy", ServerMBean.class, var3, var4);
         var1.put("ReliableDeliveryPolicy", var2);
         var2.setValue("description", "<p>The reliable delivery policy for web services.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("ReplicationGroup")) {
         var3 = "getReplicationGroup";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReplicationGroup";
         }

         var2 = new PropertyDescriptor("ReplicationGroup", ServerMBean.class, var3, var4);
         var1.put("ReplicationGroup", var2);
         var2.setValue("description", "<p>Defines preferred clustered instances considered for hosting replicas of the primary HTTP session states created on the server.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("ReplicationPorts")) {
         var3 = "getReplicationPorts";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReplicationPorts";
         }

         var2 = new PropertyDescriptor("ReplicationPorts", ServerMBean.class, var3, var4);
         var1.put("ReplicationPorts", var2);
         var2.setValue("description", "When WLS is running on Exalogic machines, cluster replication traffic could go over multiple replication channels. However multiple replication channels need not be configured on each clustered server instance. Only one replication channel with explicit IP-Address needs to be configured for each server and replicationPorts range can be specified for each server. For eg. range 7001-7010 will create 10 replication channels with ports 7001 to 7010 for the given server. These channels inherits all the properties of the configured replication channel except the listen port.Names of these channels will be derived from the configured replication channel with suffic {x} added where x could be 1,2.. as per the number of ports specified. Public ports are same as the listen port for these additional channels. ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "10.3.4.0");
      }

      if (!var1.containsKey("RestartDelaySeconds")) {
         var3 = "getRestartDelaySeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRestartDelaySeconds";
         }

         var2 = new PropertyDescriptor("RestartDelaySeconds", ServerMBean.class, var3, var4);
         var1.put("RestartDelaySeconds", var2);
         var2.setValue("description", "<p>The number of seconds the Node Manager should wait before restarting this server.</p>  <p>After killing a server process, the system might need several seconds to release the TCP port(s) the server was using. If Node Manager attempts to restart the Managed Server while its ports are still active, the startup attempt fails.</p>  <p>If AutoMigration is enabled and RestartDelaySeconds is 0, the RestartDelaySeconds is automatically set to the lease time. This prevents the server from failing to restart after migration when the previous lease is still valid.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RestartIntervalSeconds")) {
         var3 = "getRestartIntervalSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRestartIntervalSeconds";
         }

         var2 = new PropertyDescriptor("RestartIntervalSeconds", ServerMBean.class, var3, var4);
         var1.put("RestartIntervalSeconds", var2);
         var2.setValue("description", "<p>The number of seconds during which this server can be restarted, up to the number of times specified in RestartMax.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getRestartMax")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(3600));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(300));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RestartMax")) {
         var3 = "getRestartMax";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRestartMax";
         }

         var2 = new PropertyDescriptor("RestartMax", ServerMBean.class, var3, var4);
         var1.put("RestartMax", var2);
         var2.setValue("description", "<p>The number of times that the Node Manager can restart this server within the interval specified in RestartIntervalSeconds.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(2));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerDebug")) {
         var3 = "getServerDebug";
         var4 = null;
         var2 = new PropertyDescriptor("ServerDebug", ServerMBean.class, var3, var4);
         var1.put("ServerDebug", var2);
         var2.setValue("description", "<p>The debug setting for this server.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ServerDiagnosticConfig")) {
         var3 = "getServerDiagnosticConfig";
         var4 = null;
         var2 = new PropertyDescriptor("ServerDiagnosticConfig", ServerMBean.class, var3, var4);
         var1.put("ServerDiagnosticConfig", var2);
         var2.setValue("description", "The diagnostic configuration for the servers ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("ServerLifeCycleTimeoutVal")) {
         var3 = "getServerLifeCycleTimeoutVal";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServerLifeCycleTimeoutVal";
         }

         var2 = new PropertyDescriptor("ServerLifeCycleTimeoutVal", ServerMBean.class, var3, var4);
         var1.put("ServerLifeCycleTimeoutVal", var2);
         var2.setValue("description", "<p>Number of seconds a force shutdown operation waits before timing out and killing itself. If the operation does not complete within the configured timeout seconds, the server will shutdown automatically if the state of the server at that time was <code>SHUTTING_DOWN</code>.</p>  <p>A value of <code>0</code> means that the server will wait indefinitely for life cycle operation to complete.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(30));
         var2.setValue("secureValue", new Integer(120));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerStart")) {
         var3 = "getServerStart";
         var4 = null;
         var2 = new PropertyDescriptor("ServerStart", ServerMBean.class, var3, var4);
         var1.put("ServerStart", var2);
         var2.setValue("description", "<p>Returns the ServerStartMBean that can be used to start up this server remotely.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ServerVersion")) {
         var3 = "getServerVersion";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServerVersion";
         }

         var2 = new PropertyDescriptor("ServerVersion", ServerMBean.class, var3, var4);
         var1.put("ServerVersion", var2);
         var2.setValue("description", "<p>The release identifier for the server. Since this is a configured attribute it is only as accurate as the configuration. The form of the version is major.minor.servicepack.rollingpatch. Not all parts of the version are required. i.e. \"7\" is acceptable.</p> ");
         setPropertyDescriptorDefault(var2, "unknown");
         var2.setValue("deprecated", "9.0.0.0 ");
      }

      if (BeanInfoHelper.isVersionCompliant("9.5.0.0", (String)null, this.targetVersion) && !var1.containsKey("SingleSignOnServices")) {
         var3 = "getSingleSignOnServices";
         var4 = null;
         var2 = new PropertyDescriptor("SingleSignOnServices", ServerMBean.class, var3, var4);
         var1.put("SingleSignOnServices", var2);
         var2.setValue("description", "<p>Gets the Single Sign-On Services MBean</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.5.0.0");
      }

      if (!var1.containsKey("StagingDirectoryName")) {
         var3 = "getStagingDirectoryName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStagingDirectoryName";
         }

         var2 = new PropertyDescriptor("StagingDirectoryName", ServerMBean.class, var3, var4);
         var1.put("StagingDirectoryName", var2);
         var2.setValue("description", "<p>The directory path on the Managed Server where all staged (prepared) applications are placed.</p>  <p>If an absolute directory name is not specified, the path is relative to the root directory \"/\". Once configured, you cannot change the staging directory name. Remove all applications from the server prior to changing this attribute. The default staging directory is \"stage\", relative to the server root.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("StagingMode")) {
         var3 = "getStagingMode";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStagingMode";
         }

         var2 = new PropertyDescriptor("StagingMode", ServerMBean.class, var3, var4);
         var1.put("StagingMode", var2);
         var2.setValue("description", "<p>The mode that specifies whether an application's files are copied from a source on the Administration Server to the Managed Server's staging area during application preparation.</p>  <p>During application preparation, the application's files are copied from the source on the Administration Server to the Managed Server's staging area. If you specify <code>nostage</code> or <code>external_stage</code>, the copy will not occur. This is useful when the staging area is a shared directory, already containing the application files, or if this is a single server domain. The administrator must ensure that the Managed Server's staging directory is set appropriately. Deployment errors will result if the application is not available during the preparation or activation of the application. Each application can override the staging mode specified here.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ApplicationMBean#getStagingMode()")};
         var2.setValue("see", var5);
         var2.setValue("legalValues", new Object[]{ServerMBean.DEFAULT_STAGE, "stage", "nostage", "external_stage"});
         var2.setValue("since", "7.0.0.0");
      }

      if (!var1.containsKey("StartupMode")) {
         var3 = "getStartupMode";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStartupMode";
         }

         var2 = new PropertyDescriptor("StartupMode", ServerMBean.class, var3, var4);
         var1.put("StartupMode", var2);
         var2.setValue("description", "<p>The state in which this server should be started. If you specify <tt>STANDBY</tt>, you must also enable the domain-wide administration port.</p>  <p>In the <code>RUNNING</code> state, a server offers its services to clients and can operate as a full member of a cluster. In the <code>ADMIN</code> state, the server is up and running, but available only for administration operations, allowing you to perform server and application-level administration tasks without risk to running applications. In the <code>STANDBY</code> state, a server instance does not process any request; its regular Listen Port is closed. The Administration Port is open. It only accepts life cycle commands that transition the server instance to either the <code>RUNNING</code> or the <code>SHUTDOWN</code> state. Other Administration requests are not accepted. A <code>STANDBY</code> server's only purpose is to resume into the <code>RUNNING</code> state quickly; it saves server startup time.</p> ");
         setPropertyDescriptorDefault(var2, "RUNNING");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("StartupTimeout")) {
         var3 = "getStartupTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStartupTimeout";
         }

         var2 = new PropertyDescriptor("StartupTimeout", ServerMBean.class, var3, var4);
         var1.put("StartupTimeout", var2);
         var2.setValue("description", "<p>Timeout value for server start and resume operations. If the server fails to start in the timeout period, it will force shutdown.</p>  <p>A value of <code>0</code> means that the server will wait indefinitely for the operation to complete.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("StdoutFormat")) {
         var3 = "getStdoutFormat";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutFormat";
         }

         var2 = new PropertyDescriptor("StdoutFormat", ServerMBean.class, var3, var4);
         var1.put("StdoutFormat", var2);
         var2.setValue("description", "<p>The output format to use when logging to the console.</p> ");
         setPropertyDescriptorDefault(var2, "standard");
         var2.setValue("legalValues", new Object[]{"standard", "noid"});
         var2.setValue("deprecated", " ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("StdoutSeverityLevel")) {
         var3 = "getStdoutSeverityLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutSeverityLevel";
         }

         var2 = new PropertyDescriptor("StdoutSeverityLevel", ServerMBean.class, var3, var4);
         var1.put("StdoutSeverityLevel", var2);
         var2.setValue("description", "<p>The minimum severity of a message that the server sends to standard out. (Requires you to enable sending messages to standard out.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isStdoutEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(32));
         var2.setValue("secureValue", new Integer(32));
         var2.setValue("legalValues", new Object[]{new Integer(256), new Integer(128), new Integer(64), new Integer(16), new Integer(8), new Integer(32), new Integer(4), new Integer(2), new Integer(1), new Integer(0)});
         var2.setValue("deprecated", "9.0.0.0 Replaced by LogMBean.StdoutSeverity.  For backward compatibility the changes to this attribute will be  propagated to the LogMBean. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("SystemPasswordEncrypted")) {
         var3 = "getSystemPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSystemPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("SystemPasswordEncrypted", ServerMBean.class, var3, var4);
         var1.put("SystemPasswordEncrypted", var2);
         var2.setValue("description", "<p>The password required to access administrative functions on this server.</p>  <p>To set this attribute,  use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("TransactionLogFilePrefix")) {
         var3 = "getTransactionLogFilePrefix";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTransactionLogFilePrefix";
         }

         var2 = new PropertyDescriptor("TransactionLogFilePrefix", ServerMBean.class, var3, var4);
         var1.put("TransactionLogFilePrefix", var2);
         var2.setValue("description", "<p>The path prefix for the server's JTA transaction log files. If the pathname is not absolute, the path is assumed to be relative to the server's root directory.</p>  <p>For a clustered server, if you plan to be able to migrate the Transaction Recovery Service from this server if it fails to another server (backup server) in the same cluster, you must store transaction log files on persistent storage, such as a Storage Area Network (SAN) device or a dual-ported disk, available to both servers.</p>  <p>Do not use an NFS file system to store transaction log files. Because of the caching scheme in NFS, transaction log files on disk may not always be current. Using transaction log files stored on an NFS device for recovery may cause data corruption. </p> ");
         setPropertyDescriptorDefault(var2, "./");
      }

      if (!var1.containsKey("TransactionLogFileWritePolicy")) {
         var3 = "getTransactionLogFileWritePolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTransactionLogFileWritePolicy";
         }

         var2 = new PropertyDescriptor("TransactionLogFileWritePolicy", ServerMBean.class, var3, var4);
         var1.put("TransactionLogFileWritePolicy", var2);
         var2.setValue("description", "<p>The policy that determines how transaction log file entries are written to disk. This policy can affect transaction performance. (Note: To be transactionally safe, the Direct-Write policy may require additional OS or environment changes on some Windows systems.)</p>  <p>WebLogic Server supports the following policies:</p>  <ul> <li> <p>Cache-Flush. Flushes operating system and on-disk caches after each write.</p> </li>  <li> <p>Direct-Write. Tells the operating system to write directly to disk with each write. Direct-Write performs better than Cache-Flush.</p> </li> </ul>  <p>If Direct-Write is not supported on the host platform, the policy becomes Cache-Flush and a log message is printed.</p>  <p><b>Note</b>: On Windows, the \"Direct-Write\" policy may leave transaction data in the on-disk cache without writing it to disk immediately. This is not transactionally safe because a power failure can cause loss of on-disk cache data. For transactionally safe writes using \"Direct-Write\" on Windows, either disable all write caching for the disk (enabled by default), or use a disk with a battery-backed cache.</p>  <p>The on-disk cache for a hard-drive on Windows can be disabled through system administration: Control-Panel -&gt; System -&gt; Hardware-tab -&gt; Device-Manager-button -&gt; Disk-Drives -&gt; name-of-drive -&gt; Policies-tab -&gt; \"Enable write caching on the disk\" check-box. Some file systems do not allow this value to be changed. For example, a RAID system that has a reliable cache.</p> ");
         setPropertyDescriptorDefault(var2, "Direct-Write");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("legalValues", new Object[]{"Cache-Flush", "Direct-Write"});
      }

      if (!var1.containsKey("TransactionLogJDBCStore")) {
         var3 = "getTransactionLogJDBCStore";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionLogJDBCStore", ServerMBean.class, var3, var4);
         var1.put("TransactionLogJDBCStore", var2);
         var2.setValue("description", "<p>The JDBC TLOG store used for transaction logging. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("TunnelingClientPingSecs")) {
         var3 = "getTunnelingClientPingSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTunnelingClientPingSecs";
         }

         var2 = new PropertyDescriptor("TunnelingClientPingSecs", ServerMBean.class, var3, var4);
         var1.put("TunnelingClientPingSecs", var2);
         var2.setValue("description", "<p>The interval (in seconds) at which to ping a tunneled client to see if it is still alive.</p>  <p>If you create network channels for this server, each channel can override this setting.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getTunnelingClientPingSecs")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(45));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TunnelingClientTimeoutSecs")) {
         var3 = "getTunnelingClientTimeoutSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTunnelingClientTimeoutSecs";
         }

         var2 = new PropertyDescriptor("TunnelingClientTimeoutSecs", ServerMBean.class, var3, var4);
         var1.put("TunnelingClientTimeoutSecs", var2);
         var2.setValue("description", "<p>The amount of time (in seconds) after which a missing tunneled client is considered dead.</p>  <p>If you create network channels for this server, each channel can override this setting.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getTunnelingClientTimeoutSecs")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(40));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UploadDirectoryName")) {
         var3 = "getUploadDirectoryName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUploadDirectoryName";
         }

         var2 = new PropertyDescriptor("UploadDirectoryName", ServerMBean.class, var3, var4);
         var1.put("UploadDirectoryName", var2);
         var2.setValue("description", "<p>The directory path on the Administration Server where all uploaded applications are placed.</p>  <p>If an absolute directory name is not specified, the path is relative to the root directory \"/\". The default staging directory is \"stage\", relative to the server root. On the Managed Server this returns null, and is not configurable.</p> ");
         var2.setValue("secureValue", "An absolute directory that is outside the root directory of any  WebLogic Server instance or application, and that resides on a physical disk  that is separate from the WebLogic Server host's system disk.");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("VerboseEJBDeploymentEnabled")) {
         var3 = "getVerboseEJBDeploymentEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setVerboseEJBDeploymentEnabled";
         }

         var2 = new PropertyDescriptor("VerboseEJBDeploymentEnabled", ServerMBean.class, var3, var4);
         var1.put("VerboseEJBDeploymentEnabled", var2);
         var2.setValue("description", "<p>Whether or not verbose deployment of EJBs is enabled.</p> ");
         setPropertyDescriptorDefault(var2, "false");
         var2.setValue("deprecated", "Deprecated as of 10.3.3.0 in favor of {@link ServerDebugMBean#getDebugEjbDeployment()} ");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.2.0", (String)null, this.targetVersion) && !var1.containsKey("VirtualMachineName")) {
         var3 = "getVirtualMachineName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setVirtualMachineName";
         }

         var2 = new PropertyDescriptor("VirtualMachineName", ServerMBean.class, var3, var4);
         var1.put("VirtualMachineName", var2);
         var2.setValue("description", "When WLS is running on JRVE, this specifies the name of the virtual machine running this server ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.3.2.0");
      }

      if (!var1.containsKey("WebServer")) {
         var3 = "getWebServer";
         var4 = null;
         var2 = new PropertyDescriptor("WebServer", ServerMBean.class, var3, var4);
         var1.put("WebServer", var2);
         var2.setValue("description", "<p>Returns the web server for this server. A server has exactly one WebServer. A server may also have one or more VirtualHosts. A VirtualHost is a subclass of WebServer.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("9.2.0.0", (String)null, this.targetVersion) && !var1.containsKey("WebService")) {
         var3 = "getWebService";
         var4 = null;
         var2 = new PropertyDescriptor("WebService", ServerMBean.class, var3, var4);
         var1.put("WebService", var2);
         var2.setValue("description", "<p>Gets Web service configuration for this server</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "9.2.0.0");
      }

      if (!var1.containsKey("XMLEntityCache")) {
         var3 = "getXMLEntityCache";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXMLEntityCache";
         }

         var2 = new PropertyDescriptor("XMLEntityCache", ServerMBean.class, var3, var4);
         var1.put("XMLEntityCache", var2);
         var2.setValue("description", "<p>The server's XML entity cache, which is used to configure the behavior of JAXP (Java API for XML Parsing).</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("XMLEntityCacheMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("XMLRegistry")) {
         var3 = "getXMLRegistry";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXMLRegistry";
         }

         var2 = new PropertyDescriptor("XMLRegistry", ServerMBean.class, var3, var4);
         var1.put("XMLRegistry", var2);
         var2.setValue("description", "<p>The server's XML registry, which is used to configure the behavior of JAXP (Java API for XML Parsing).</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("XMLRegistryMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("AdministrationPortEnabled")) {
         var3 = "isAdministrationPortEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdministrationPortEnabled";
         }

         var2 = new PropertyDescriptor("AdministrationPortEnabled", ServerMBean.class, var3, var4);
         var1.put("AdministrationPortEnabled", var2);
         var2.setValue("description", "<p>Indicates whether or not administration port is enabled for the server. This field is derived from the DomainMBean and has no setter here All the server (7.0 and later) in s single domain should either have an administration port or not The administration port uses SSL, so SSL must be configured and enabled properly for it to be active.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getAdministrationPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.DomainMBean#isAdministrationPortEnabled"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.DomainMBean#getAdministrationPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getSSL")};
         var2.setValue("see", var5);
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("AutoMigrationEnabled")) {
         var3 = "isAutoMigrationEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAutoMigrationEnabled";
         }

         var2 = new PropertyDescriptor("AutoMigrationEnabled", ServerMBean.class, var3, var4);
         var1.put("AutoMigrationEnabled", var2);
         var2.setValue("description", "<p>Specifies whether Node Manager can automatically restart this server and its services on another machine if the server fails.</p> ");
      }

      if (!var1.containsKey("COMEnabled")) {
         var3 = "isCOMEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCOMEnabled";
         }

         var2 = new PropertyDescriptor("COMEnabled", ServerMBean.class, var3, var4);
         var1.put("COMEnabled", var2);
         var2.setValue("description", "<p>Specifies whether COM support is enabled on the regular (non-SSL) port. COM is not supported on the SSL port. (The remaining fields on this page are relevant only if you check this box.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getListenPort")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("ClasspathServletDisabled")) {
         var3 = "isClasspathServletDisabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClasspathServletDisabled";
         }

         var2 = new PropertyDescriptor("ClasspathServletDisabled", ServerMBean.class, var3, var4);
         var1.put("ClasspathServletDisabled", var2);
         var2.setValue("description", "<p>The ClasspathServlet will serve any class file in the classpath and is registered by default in every Web application (including management). It does not need to be turned on for many applications though, and represents a security hole if unchecked.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (!var1.containsKey("ClientCertProxyEnabled")) {
         var3 = "isClientCertProxyEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClientCertProxyEnabled";
         }

         var2 = new PropertyDescriptor("ClientCertProxyEnabled", ServerMBean.class, var3, var4);
         var1.put("ClientCertProxyEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the <tt>HttpClusterServlet</tt> proxies the client certificate in a special header.</p>  <p>By default (or if you specify <code>false</code>), the <code>weblogic.xml</code> deployment descriptor for each web application that is deployed on this server determines whether the web application trusts certificates sent from the proxy server plugin. By default (or if the deployment descriptor specifies <code>false</code>), users cannot log in to the web application from a proxy server plugin.</p>  <p>A value of <code>true</code> causes proxy-server plugins to pass identity certifications from clients to all web applications that are deployed on this server instance. A proxy-server plugin encodes each identify certification in the <code>WL-Proxy-Client-Cert</code> header and passes the header to WebLogic Server instances. A WebLogic Server instance takes the certificate information from the header, trusting that it came from a secure source, and uses that information to authenticate the user.</p>  <p>If you specify <code>true</code>, use a <code>weblogic.security.net.ConnectionFilter</code> to ensure that this WebLogic Server instance accepts connections only from the machine on which the proxy-server plugin is running. Specifying <code>true</code> without using a connection filter creates a security vulnerability because the <code>WL-Proxy-Client-Cert</code> header can be spoofed.</p>  <p>A cluster can also specify whether the <tt>HttpClusterServlet</tt> proxies the client certificate in a special header. The cluster-level setting overrides the setting in individual servers that are part of the cluster.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.security.net.ConnectionFilter"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebAppContainerMBean#isClientCertProxyEnabled()"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ClusterMBean#isClientCertProxyEnabled()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("ConsoleInputEnabled")) {
         var3 = "isConsoleInputEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConsoleInputEnabled";
         }

         var2 = new PropertyDescriptor("ConsoleInputEnabled", ServerMBean.class, var3, var4);
         var1.put("ConsoleInputEnabled", var2);
         var2.setValue("description", "<p>True if commands can be typed at console. REMOVE?</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultInternalServletsDisabled")) {
         var3 = "isDefaultInternalServletsDisabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultInternalServletsDisabled";
         }

         var2 = new PropertyDescriptor("DefaultInternalServletsDisabled", ServerMBean.class, var3, var4);
         var1.put("DefaultInternalServletsDisabled", var2);
         var2.setValue("description", "<p>Specifies whether all default servlets in the servlet engine are disabled.</p>  <p>This includes: weblogic.servlet.ClasspathServlet weblogic.servlet.utils.iiop.GetIORServlet weblogic.rjvm.http.TunnelSendServlet weblogic.rjvm.http.TunnelRecvServlet weblogic.rjvm.http.TunnelLoginServlet weblogic.rjvm.http.TunnelCloseServlet If set to true, this property overrides the ClasspathServletDisabled property.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("EnabledForDomainLog")) {
         var3 = "isEnabledForDomainLog";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabledForDomainLog";
         }

         var2 = new PropertyDescriptor("EnabledForDomainLog", ServerMBean.class, var3, var4);
         var1.put("EnabledForDomainLog", var2);
         var2.setValue("description", "<p>Determines whether this server sends messages to the domain log (in addition to keeping its own log).</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("deprecated", "9.0.0.0 replaced by LogMBean.LogBroadcastSeverity, For backward compatibility the changes to this attribute will be propagated to the LogMBean. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("HttpTraceSupportEnabled")) {
         var3 = "isHttpTraceSupportEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHttpTraceSupportEnabled";
         }

         var2 = new PropertyDescriptor("HttpTraceSupportEnabled", ServerMBean.class, var3, var4);
         var1.put("HttpTraceSupportEnabled", var2);
         var2.setValue("description", "Returns the HttpTraceSupportEnabled value ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebAppContainerMBean#isHttpTraceSupportEnabled()"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ClusterMBean#isHttpTraceSupportEnabled()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("HttpdEnabled")) {
         var3 = "isHttpdEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHttpdEnabled";
         }

         var2 = new PropertyDescriptor("HttpdEnabled", ServerMBean.class, var3, var4);
         var1.put("HttpdEnabled", var2);
         var2.setValue("description", "<p>Whether or not HTTP support is enabled on the regular port or SSL port.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getListenPort"), BeanInfoHelper.encodeEntities("#isTunnelingEnabled"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#getListenPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getSSL")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
      }

      if (!var1.containsKey("IIOPEnabled")) {
         var3 = "isIIOPEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIIOPEnabled";
         }

         var2 = new PropertyDescriptor("IIOPEnabled", ServerMBean.class, var3, var4);
         var1.put("IIOPEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this server has IIOP support enabled for both the regular (non-SSL) and SSL ports.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getListenPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#getListenPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getSSL")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
      }

      if (!var1.containsKey("IgnoreSessionsDuringShutdown")) {
         var3 = "isIgnoreSessionsDuringShutdown";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIgnoreSessionsDuringShutdown";
         }

         var2 = new PropertyDescriptor("IgnoreSessionsDuringShutdown", ServerMBean.class, var3, var4);
         var1.put("IgnoreSessionsDuringShutdown", var2);
         var2.setValue("description", "<p>Indicates whether a graceful shutdown operation drops all HTTP sessions immediately.</p> <p>If this is set to <code>false</code>, a graceful shutdown operation waits for HTTP sessions to complete or timeout.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JDBCLoggingEnabled")) {
         var3 = "isJDBCLoggingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJDBCLoggingEnabled";
         }

         var2 = new PropertyDescriptor("JDBCLoggingEnabled", ServerMBean.class, var3, var4);
         var1.put("JDBCLoggingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this server maintains a JDBC log file.</p> ");
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("deprecated", "9.0.0.0 Use ServerDebugMBean.getJDBCDriverLogging ");
      }

      if (!var1.containsKey("JMSDefaultConnectionFactoriesEnabled")) {
         var3 = "isJMSDefaultConnectionFactoriesEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJMSDefaultConnectionFactoriesEnabled";
         }

         var2 = new PropertyDescriptor("JMSDefaultConnectionFactoriesEnabled", ServerMBean.class, var3, var4);
         var1.put("JMSDefaultConnectionFactoriesEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this server uses JMS default connection factories.</p>  <p>WebLogic Server provides the following JMS default connection factories:</p>  <ul> <li> <code>weblogic.jms.ConnectionFactory</code> </li>  <li> <code>weblogic.jms.XAConnectionFactory</code> An XA factory is required for JMS applications to use JTA user-transactions, but is not required for transacted sessions. All other preconfigured attributes for the default connection factories are set to the same default values as a user-defined connection factory. If the preconfigured settings of the default factories are appropriate for your application, you do not need to configure any additional factories for your application. </li> </ul> <p> <b>Note:</b> When using the default connection factories, you have no control over targeting the WebLogic Server instances where the connection factory may be deployed. However, you can disable the default connection factories on a per-server basis. To deploy a connection factory on independent servers, on specific servers within a cluster, or on an entire cluster, you need to configure a connection factory and specify the appropriate server targets.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ListenPortEnabled")) {
         var3 = "isListenPortEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenPortEnabled";
         }

         var2 = new PropertyDescriptor("ListenPortEnabled", ServerMBean.class, var3, var4);
         var1.put("ListenPortEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this server can be reached through the default plain-text (non-SSL) listen port.</p>  <p>If you disable this listen port, you must enable the default SSL listen port.</p>  <p>You can define additional listen ports for this server by configuring network channels.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getListenPort"), BeanInfoHelper.encodeEntities("#isAdministrationPortEnabled"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getSSL"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#isListenPortEnabled"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#isEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "7.0.0.0");
      }

      if (!var1.containsKey("MSIFileReplicationEnabled")) {
         var3 = "isMSIFileReplicationEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMSIFileReplicationEnabled";
         }

         var2 = new PropertyDescriptor("MSIFileReplicationEnabled", ServerMBean.class, var3, var4);
         var1.put("MSIFileReplicationEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the Administration Server replicates its configuration files to this Managed Server.</p>  <p>With file replication enabled, the Administration Server copies its configuration file and <code>SerializedSystemIni.dat</code> into the Managed Server's root directory every 5 minutes. This option does not replicate a boot identity file.</p>  <p>Regardless of the name of the configuration file that you used to start the Administration Server, the replicated file is always named <code>msi-config.xml</code>. For example, if you specified <code>-Dweblogic.ConfigFile=MyConfig.xml</code> when you started the Administration Server, if you have enabled file replication, the Administration Server copies <code>MyConfig.xml</code> and names the copy <code>msi-config.xml</code>.</p>  <p>Depending on your backup schemes and the frequency with which you update your domain's configuration, this option might not be worth the performance cost of copying potentially large files across a network.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("ManagedServerIndependenceEnabled")) {
         var3 = "isManagedServerIndependenceEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setManagedServerIndependenceEnabled";
         }

         var2 = new PropertyDescriptor("ManagedServerIndependenceEnabled", ServerMBean.class, var3, var4);
         var1.put("ManagedServerIndependenceEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this Managed Server can be started when the Administration Server is unavailable.</p>  <p>In such a case, the Managed Server retrieves its configuration by reading a configuration file and other files directly.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (!var1.containsKey("MessageIdPrefixEnabled")) {
         var3 = "isMessageIdPrefixEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessageIdPrefixEnabled";
         }

         var2 = new PropertyDescriptor("MessageIdPrefixEnabled", ServerMBean.class, var3, var4);
         var1.put("MessageIdPrefixEnabled", var2);
         var2.setValue("description", "<p>Indicates whether message IDs in logged messages will include a prefix. Message ids are 6 digit numeric strings that can be optionally presented in a log entry with a prefix. The prefix used by server messages is \"BEA-\".</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("StdoutDebugEnabled")) {
         var3 = "isStdoutDebugEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutDebugEnabled";
         }

         var2 = new PropertyDescriptor("StdoutDebugEnabled", ServerMBean.class, var3, var4);
         var1.put("StdoutDebugEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the server sends messages of the <code>DEBUG</code> severity to standard out in addition to the log file. (Requires you to enable sending messages to standard out.)</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 replaced by LogMBean.StdoutSeverity For backward compatibility the changes to this attribute will be propagated to the LogMBean. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("StdoutEnabled")) {
         var3 = "isStdoutEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutEnabled";
         }

         var2 = new PropertyDescriptor("StdoutEnabled", ServerMBean.class, var3, var4);
         var1.put("StdoutEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the server sends messages to standard out in addition to the log file.</p>  <p>Other settings configure the minimum severity of a message that the server sends to standard out.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isStdoutDebugEnabled"), BeanInfoHelper.encodeEntities("#getStdoutSeverityLevel")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("deprecated", "9.0.0.0 replaced by LogMBean.StdoutSeverity, for backward compatibility the changes to this attribute will be propagated to the LogMBean. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("StdoutLogStack")) {
         var3 = "isStdoutLogStack";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutLogStack";
         }

         var2 = new PropertyDescriptor("StdoutLogStack", ServerMBean.class, var3, var4);
         var1.put("StdoutLogStack", var2);
         var2.setValue("description", "<p>Specifies whether to dump stack traces to the console when included in logged message.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("TGIOPEnabled")) {
         var3 = "isTGIOPEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTGIOPEnabled";
         }

         var2 = new PropertyDescriptor("TGIOPEnabled", ServerMBean.class, var3, var4);
         var1.put("TGIOPEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this server supports Tuxedo GIOP (TGIOP) requests. (Requires you to configure WebLogic Tuxedo Connector (WTC) for this server.)</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (!var1.containsKey("TunnelingEnabled")) {
         var3 = "isTunnelingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTunnelingEnabled";
         }

         var2 = new PropertyDescriptor("TunnelingEnabled", ServerMBean.class, var3, var4);
         var1.put("TunnelingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether tunneling for the T3, T3S, HTTP, HTTPS, IIOP, and IIOPS protocols should be enabled for this server.</p>  <p>If you create network channels for this server, each channel can override this setting.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isHttpdEnabled"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#isTunnelingEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UseFusionForLLR")) {
         var3 = "isUseFusionForLLR";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseFusionForLLR";
         }

         var2 = new PropertyDescriptor("UseFusionForLLR", ServerMBean.class, var3, var4);
         var1.put("UseFusionForLLR", var2);
         var2.setValue("description", "<p>Enables the use of the <code>ADM_DDL </code> store procedure for LLR. The default value is <code>false</code> (not enabled). </p> <p>When enabled, a  <code>WLS_</code> prefix and <code>_DYD</code> suffix is is automatically added to the LLR table name at runtime so the LLR table name in server configuration is not consistent with the actual table name in database.</p> ");
      }

      if (!var1.containsKey("WeblogicPluginEnabled")) {
         var3 = "isWeblogicPluginEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWeblogicPluginEnabled";
         }

         var2 = new PropertyDescriptor("WeblogicPluginEnabled", ServerMBean.class, var3, var4);
         var1.put("WeblogicPluginEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this server uses the proprietary <tt>WL-Proxy-Client-IP</tt> header, which is recommended if the server instance will receive requests from a proxy plug-in.</p>  <p>If the server instance is a member of a cluster that will receive proxied requests, enable the WebLogic plugin at the cluster level. For servers that are members of a cluster, the setting at the cluster level overrides the server's setting.</p>  <p>When the WebLogic plugin is enabled, a call to <code>getRemoteAddr</code> will return the address of the browser client from the proprietary <code>WL-Proxy-Client-IP</code> header instead of the web server.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebAppContainerMBean#isWeblogicPluginEnabled()"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ClusterMBean#isWeblogicPluginEnabled()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion)) {
         var3 = ServerMBean.class.getMethod("createNetworkAccessPoint", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory to create a new NetworkAccessPoint instance for this Server.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "NetworkAccessPoints");
            var2.setValue("since", "7.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion)) {
         var3 = ServerMBean.class.getMethod("destroyNetworkAccessPoint", NetworkAccessPointMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("accessPoint", "to be destroyed ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Destroys a NetworkAccessPoint object.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "NetworkAccessPoints");
            var2.setValue("since", "7.0.0.0");
         }
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      String[] var6;
      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion)) {
         var3 = ServerMBean.class.getMethod("addNetworkAccessPoint", NetworkAccessPointMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("networkAccessPoint", "The feature to be added to the NetworkAccessPoint attribute ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var6 = new String[]{BeanInfoHelper.encodeEntities("#getNetworkAccessPoints")};
            var2.setValue("see", var6);
            var2.setValue("role", "collection");
            var2.setValue("property", "NetworkAccessPoints");
            var2.setValue("since", "7.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion)) {
         var3 = ServerMBean.class.getMethod("removeNetworkAccessPoint", NetworkAccessPointMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("networkAccessPoint", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var6 = new String[]{BeanInfoHelper.encodeEntities("#getNetworkAccessPoints")};
            var2.setValue("see", var6);
            var2.setValue("role", "collection");
            var2.setValue("property", "NetworkAccessPoints");
            var2.setValue("since", "7.0.0.0");
         }
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion)) {
         Method var3 = ServerMBean.class.getMethod("lookupNetworkAccessPoint", String.class);
         ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the key of the network access point. ")};
         String var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            MethodDescriptor var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Look up an NetworkAccessPoint by name</p> ");
            var2.setValue("role", "finder");
            var2.setValue("property", "NetworkAccessPoints");
            var2.setValue("since", "7.0.0.0");
         }
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = ServerMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = ServerMBean.class.getMethod("restoreDefaultValue", String.class);
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

      var3 = ServerMBean.class.getMethod("synchronousStart");
      String var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "9.0.0.0 Use {@link weblogic.management.runtime.ServerLifeCycleRuntimeMBean#start()} instead. ");
         var1.put(var6, var2);
         var2.setValue("description", "<p>Start this server. This is a blocking call. Returns String containing NodeManger log for starting the server.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ServerMBean.class.getMethod("synchronousKill");
      var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "9.0.0.0 Use {@link weblogic.management.runtime.ServerRuntimeMBean#forceShutdown()} instead ");
         var1.put(var6, var2);
         var2.setValue("description", "<p>Kill this server. This is a blocking call. Returns String containing NodeManger log for killing the server.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ServerMBean.class.getMethod("lookupServerLifeCycleRuntime");
      var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var6, var2);
         var2.setValue("description", "<p>Lookup a ServerLifeCycleRuntimeMbean.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
         String[] var7 = new String[]{BeanInfoHelper.encodeEntities("Deployer"), BeanInfoHelper.encodeEntities("Operator"), BeanInfoHelper.encodeEntities("Monitor")};
         var2.setValue("rolesAllowed", var7);
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
