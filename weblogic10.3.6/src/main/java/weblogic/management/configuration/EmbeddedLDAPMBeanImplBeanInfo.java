package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class EmbeddedLDAPMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = EmbeddedLDAPMBean.class;

   public EmbeddedLDAPMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public EmbeddedLDAPMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = EmbeddedLDAPMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>The MBean that defines the configuration properties for the embedded LDAP server for the WebLogic domain.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.EmbeddedLDAPMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("BackupCopies")) {
         var3 = "getBackupCopies";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBackupCopies";
         }

         var2 = new PropertyDescriptor("BackupCopies", EmbeddedLDAPMBean.class, var3, var4);
         var1.put("BackupCopies", var2);
         var2.setValue("description", "<p>The maximum number of backup copies that should be made for the embedded LDAP server.</p>  <p>This value limits the number of zip files in the ldap/backup directory.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(7));
         var2.setValue("legalMax", new Integer(65534));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("BackupHour")) {
         var3 = "getBackupHour";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBackupHour";
         }

         var2 = new PropertyDescriptor("BackupHour", EmbeddedLDAPMBean.class, var3, var4);
         var1.put("BackupHour", var2);
         var2.setValue("description", "<p>The hour at which the embedded LDAP server should be backed up.</p>  <p>The Backup Hour value is used in conjunction with the Backup Minute value to determine the time at which the embedded LDAP server data files are backed up. At the specified time, WebLogic Server suspends writes to the embedded LDAP server, backs up the data files into a zip files in the ldap/backup directory, and then resumes writes.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(23));
         var2.setValue("legalMax", new Integer(23));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("BackupMinute")) {
         var3 = "getBackupMinute";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBackupMinute";
         }

         var2 = new PropertyDescriptor("BackupMinute", EmbeddedLDAPMBean.class, var3, var4);
         var1.put("BackupMinute", var2);
         var2.setValue("description", "<p>The minute at which the embedded LDAP server should be backed up.</p>  <p>The Backup Minute value is used in conjunction with the Back Up Hour value to determine the time at which the embedded LDAP server data files are backed up</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("legalMax", new Integer(59));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CacheSize")) {
         var3 = "getCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCacheSize";
         }

         var2 = new PropertyDescriptor("CacheSize", EmbeddedLDAPMBean.class, var3, var4);
         var1.put("CacheSize", var2);
         var2.setValue("description", "<p>The size of the cache (in kilobytes) that is used with the embedded LDAP server.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(32));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CacheTTL")) {
         var3 = "getCacheTTL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCacheTTL";
         }

         var2 = new PropertyDescriptor("CacheTTL", EmbeddedLDAPMBean.class, var3, var4);
         var1.put("CacheTTL", var2);
         var2.setValue("description", "<p>The time-to-live of the cache (in seconds) that is used with the embedded LDAP server.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Credential")) {
         var3 = "getCredential";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCredential";
         }

         var2 = new PropertyDescriptor("Credential", EmbeddedLDAPMBean.class, var3, var4);
         var1.put("Credential", var2);
         var2.setValue("description", "<p>The credential (usually a password) used to connect to the embedded LDAP server.</p>  <p>If this credential has not been set, WebLogic Server generates a password at startup, initializes the attribute, and saves the configuration to the config.xml file. If you want to connect to the embedded LDAP server using an external LDAP browser and the embedded LDAP administrator account (cn=Admin), change this attribute from the generated value.</p> <p>As of 8.1 sp4, when you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>CredentialEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>CredentialEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using the <code>Credential</code> attribute is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>CredentialEncrypted()</code>.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#getCredentialEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("CredentialEncrypted")) {
         var3 = "getCredentialEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCredentialEncrypted";
         }

         var2 = new PropertyDescriptor("CredentialEncrypted", EmbeddedLDAPMBean.class, var3, var4);
         var1.put("CredentialEncrypted", var2);
         var2.setValue("description", "<p>The credential (usually password) used to connect to the embedded LDAP server.</p>  <p>If this credential has not been set, WebLogic Server generates a password at startup, initializes the attribute, and saves the configuration to the config.xml file. If you want to connect to the embedded LDAP server using an external LDAP browser and the embedded LDAP administrator account (cn=Admin), change this attribute from the generated value.</p> <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Timeout")) {
         var3 = "getTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTimeout";
         }

         var2 = new PropertyDescriptor("Timeout", EmbeddedLDAPMBean.class, var3, var4);
         var1.put("Timeout", var2);
         var2.setValue("description", "<p>Specifies the maximum number of seconds to wait for results from the embedded LDAP server before timing out. If this option is set to 0, there is no maximum time limit.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("AnonymousBindAllowed")) {
         var3 = "isAnonymousBindAllowed";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAnonymousBindAllowed";
         }

         var2 = new PropertyDescriptor("AnonymousBindAllowed", EmbeddedLDAPMBean.class, var3, var4);
         var1.put("AnonymousBindAllowed", var2);
         var2.setValue("description", "Specifies whether the embedded LDAP server should allow anonymous connections. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CacheEnabled")) {
         var3 = "isCacheEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCacheEnabled";
         }

         var2 = new PropertyDescriptor("CacheEnabled", EmbeddedLDAPMBean.class, var3, var4);
         var1.put("CacheEnabled", var2);
         var2.setValue("description", "<p>Specifies whether a cache is used with the embedded LDAP server.</p>  <p>This cache is used when a managed server is reading or writing to the master embedded LDAP server that is running on the Administration server.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MasterFirst")) {
         var3 = "isMasterFirst";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMasterFirst";
         }

         var2 = new PropertyDescriptor("MasterFirst", EmbeddedLDAPMBean.class, var3, var4);
         var1.put("MasterFirst", var2);
         var2.setValue("description", "<p>Specifies whether a Managed Server should always connect to the master LDAP server (contained in the Administration Server), instead of connecting to the local replicated LDAP server (contained in the Managed Server).</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("RefreshReplicaAtStartup")) {
         var3 = "isRefreshReplicaAtStartup";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRefreshReplicaAtStartup";
         }

         var2 = new PropertyDescriptor("RefreshReplicaAtStartup", EmbeddedLDAPMBean.class, var3, var4);
         var1.put("RefreshReplicaAtStartup", var2);
         var2.setValue("description", "<p>Specifies whether a Managed Server should refresh all replicated data at boot time. (This is useful if you have made a large amount of changes when the Managed Server was not active, and you want to download the entire replica instead of having the Administration Server push each change to the Managed Server.)</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
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
