package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class LDAPRealmMBeanImplBeanInfo extends BasicRealmMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = LDAPRealmMBean.class;

   public LDAPRealmMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public LDAPRealmMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = LDAPRealmMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "7.0.0.0 ");
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean specifies the configuration attributes for the LDAP security realm. This MBean is associated with a CachingRealmMBean.  Deprecated in WebLogic Server version 7.0. Replaced by the new Security architecture that includes Authentication, Authorization, and Auditing providers. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.LDAPRealmMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AuthProtocol")) {
         var3 = "getAuthProtocol";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthProtocol";
         }

         var2 = new PropertyDescriptor("AuthProtocol", LDAPRealmMBean.class, var3, var4);
         var1.put("AuthProtocol", var2);
         var2.setValue("description", "<p>The type of authentication used to authenticate the LDAP server.</p>  <p>The following values are available:</p>  <ul> <li> <p><code>None</code> for no authentication.</p> </li>  <li> <p><code>Simple</code> for password authentication.</p> </li>  <li> <p><code>CRAM-MD5</code> for certificate authentication.</p> </li> </ul>  <p>Netscape Directory Server supports <code>CRAM-MD5</code>. Microsoft Site Server and Novell NDS support <code>Simple</code>.</p> ");
         setPropertyDescriptorDefault(var2, "none");
         var2.setValue("secureValue", "simple");
         var2.setValue("legalValues", new Object[]{"none", "simple", "CRAM-MD5"});
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("Credential")) {
         var3 = "getCredential";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCredential";
         }

         var2 = new PropertyDescriptor("Credential", LDAPRealmMBean.class, var3, var4);
         var1.put("Credential", var2);
         var2.setValue("description", "<p>The password that authenticates the LDAP user defined in the Principal attribute.</p>  <p>As of 8.1 sp4, when you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>CredentialEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted credential as a String. </ol>  <p>Using this attribute (<code>Credential</code>) is a potential security risk because the String object (which contains the unencrypted credential) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>CredentialEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getCredentialEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("CredentialEncrypted")) {
         var3 = "getCredentialEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCredentialEncrypted";
         }

         var2 = new PropertyDescriptor("CredentialEncrypted", LDAPRealmMBean.class, var3, var4);
         var1.put("CredentialEncrypted", var2);
         var2.setValue("description", "<p>The encrypted password that authenticates the LDAP user defined in the Principal attribute.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("<a href=\"../../javadocs/weblogic/management/EncryptionHelper.html\">weblogic.management.EncryptionHelper</a>")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("GroupDN")) {
         var3 = "getGroupDN";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGroupDN";
         }

         var2 = new PropertyDescriptor("GroupDN", LDAPRealmMBean.class, var3, var4);
         var1.put("GroupDN", var2);
         var2.setValue("description", "<p>The list of attributes that, when combined with the value of the Group Name Attribute field, uniquely identifies a group in the LDAP directory.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("GroupIsContext")) {
         var3 = "getGroupIsContext";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGroupIsContext";
         }

         var2 = new PropertyDescriptor("GroupIsContext", LDAPRealmMBean.class, var3, var4);
         var1.put("GroupIsContext", var2);
         var2.setValue("description", "<p>Specifies how group membership is recorded in the LDAP directory.</p>  <p>Set the value to:</p>  <ul> <li> <p><code>True</code> if each group entry contains one user.</p> </li>  <li> <p><code>False</code> if there is one group entry containing an attribute for each group member.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("GroupNameAttribute")) {
         var3 = "getGroupNameAttribute";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGroupNameAttribute";
         }

         var2 = new PropertyDescriptor("GroupNameAttribute", LDAPRealmMBean.class, var3, var4);
         var1.put("GroupNameAttribute", var2);
         var2.setValue("description", "<p>The name of a group in the LDAP directory. The value is usually the common name.</p> ");
         setPropertyDescriptorDefault(var2, "cn");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("GroupUsernameAttribute")) {
         var3 = "getGroupUsernameAttribute";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGroupUsernameAttribute";
         }

         var2 = new PropertyDescriptor("GroupUsernameAttribute", LDAPRealmMBean.class, var3, var4);
         var1.put("GroupUsernameAttribute", var2);
         var2.setValue("description", "<p>The name of the attribute that contains a group member in a group entry.</p> ");
         setPropertyDescriptorDefault(var2, "member");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LDAPURL")) {
         var3 = "getLDAPURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLDAPURL";
         }

         var2 = new PropertyDescriptor("LDAPURL", LDAPRealmMBean.class, var3, var4);
         var1.put("LDAPURL", var2);
         var2.setValue("description", "<p>The location of the LDAP server.</p>  <p>Change the URL to the name of the computer on which the LDAP server is running and the number of the port at which the LDAP server is listening.</p>  <p>If you want WebLogic Server to connect to the LDAP server using the SSL protocol, use the SSL port of the LDAP server in the server URL.</p> ");
         setPropertyDescriptorDefault(var2, "ldap://ldapserver:389");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LdapProvider")) {
         var3 = "getLdapProvider";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLdapProvider";
         }

         var2 = new PropertyDescriptor("LdapProvider", LDAPRealmMBean.class, var3, var4);
         var1.put("LdapProvider", var2);
         var2.setValue("description", "<p>The name of the class that implements an LDAP directory server. This attribute allows you to use an LDAP directory server other than the one supplied by Sun Microsystems. WebLogic Server supports the following LDAP directory servers: Open LDAP, Netscape iPlanet, Microsoft Site Server, and Novell NDs.</p> ");
         setPropertyDescriptorDefault(var2, "com.sun.jndi.ldap.LdapCtxFactory");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", LDAPRealmMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Principal")) {
         var3 = "getPrincipal";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrincipal";
         }

         var2 = new PropertyDescriptor("Principal", LDAPRealmMBean.class, var3, var4);
         var1.put("Principal", var2);
         var2.setValue("description", "<p>The distinguished name (DN) of the LDAP user that WebLogic Server uses to connect to the LDAP server. This user must be able to list LDAP users and groups.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("RealmClassName")) {
         var3 = "getRealmClassName";
         var4 = null;
         var2 = new PropertyDescriptor("RealmClassName", LDAPRealmMBean.class, var3, var4);
         var1.put("RealmClassName", var2);
         var2.setValue("description", "<p>Gets the realmClassName attribute of the BasicRealmMBean object</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SSLEnable")) {
         var3 = "getSSLEnable";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSSLEnable";
         }

         var2 = new PropertyDescriptor("SSLEnable", LDAPRealmMBean.class, var3, var4);
         var1.put("SSLEnable", var2);
         var2.setValue("description", "<p>Specifies whether the SSL protocol is used to protect communications between the LDAP server and WebLogic Server.</p>  <p>Keep in mind the following:</p>  <ul> <li> <p>Disable this attribute if the LDAP server is not configured to use the SSL protocol.</p> </li>  <li> <p>If you set the User Authentication attribute to <tt>external</tt>, this attribute must be enabled.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("UserAuthentication")) {
         var3 = "getUserAuthentication";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserAuthentication";
         }

         var2 = new PropertyDescriptor("UserAuthentication", LDAPRealmMBean.class, var3, var4);
         var1.put("UserAuthentication", var2);
         var2.setValue("description", "<p>The method for allowing or denying a user the right to communicate with WebLogic Server.</p>  <p>The following values are available:</p>  <ul> <li> <p><code>Bind</code>--The LDAP security realm retrieves user data, including the password for the LDAP server, and checks the password in WebLogic Server.</p> </li>  <li> <p><code>External</code>--The LDAP security realm authenticates a user by attempting to bind to the LDAP server with the username and password supplied by the WebLogic client.</p> </li>  <li> <p><code>Local</code>--The LDAP security realm authenticates a user by looking up the UserPassword attribute in the LDAP directory and checking its value against a set of passwords in WebLogic Server.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, "bind");
         var2.setValue("secureValue", "bind");
         var2.setValue("legalValues", new Object[]{"bind", "external", "local"});
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("UserDN")) {
         var3 = "getUserDN";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserDN";
         }

         var2 = new PropertyDescriptor("UserDN", LDAPRealmMBean.class, var3, var4);
         var1.put("UserDN", var2);
         var2.setValue("description", "<p>The list of attributes that, when combined with the attribute named in the User Name Attribute field, uniquely identifies a user in the LDAP directory.</p>  <p>When specifying this attribute, use the following format:</p>  <p><tt>ou=Barb.Klock, u=acme.com</tt></p> ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("UserNameAttribute")) {
         var3 = "getUserNameAttribute";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserNameAttribute";
         }

         var2 = new PropertyDescriptor("UserNameAttribute", LDAPRealmMBean.class, var3, var4);
         var1.put("UserNameAttribute", var2);
         var2.setValue("description", "<p>The login name of a user for the LDAP directory. The value can be the common name of a user in the LDAP directory. However, it is generally an abbreviated string, such as a User ID.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("UserPasswordAttribute")) {
         var3 = "getUserPasswordAttribute";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserPasswordAttribute";
         }

         var2 = new PropertyDescriptor("UserPasswordAttribute", LDAPRealmMBean.class, var3, var4);
         var1.put("UserPasswordAttribute", var2);
         var2.setValue("description", "<p>If the User Authentication field is set to <tt>local</tt>, this attribute finds the attribute in the LDAP user objects that contains the passwords of the LDAP users.</p> ");
         setPropertyDescriptorDefault(var2, "userpassword");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
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
      Method var3 = LDAPRealmMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = LDAPRealmMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
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
