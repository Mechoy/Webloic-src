package weblogic.management.utils;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.commo.AbstractCommoConfigurationBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class LDAPServerMBeanImplBeanInfo extends AbstractCommoConfigurationBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = LDAPServerMBean.class;

   public LDAPServerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public LDAPServerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = LDAPServerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.utils");
      String var3 = (new String("The LDAPServerMBean interface defines methods used to get/set the configuration attributes that are required to communicate with an external LDAP server.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.utils.LDAPServerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CacheSize")) {
         var3 = "getCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCacheSize";
         }

         var2 = new PropertyDescriptor("CacheSize", LDAPServerMBean.class, var3, var4);
         var1.put("CacheSize", var2);
         var2.setValue("description", "Returns the size of the cache in K. ");
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

         var2 = new PropertyDescriptor("CacheTTL", LDAPServerMBean.class, var3, var4);
         var1.put("CacheTTL", var2);
         var2.setValue("description", "Returns the time-to-live (TTL) of the cache in seconds. ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ConnectTimeout")) {
         var3 = "getConnectTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectTimeout";
         }

         var2 = new PropertyDescriptor("ConnectTimeout", LDAPServerMBean.class, var3, var4);
         var1.put("ConnectTimeout", var2);
         var2.setValue("description", "Returns the maximum number of seconds to wait for the LDAP connection to be established. ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ConnectionPoolSize")) {
         var3 = "getConnectionPoolSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionPoolSize";
         }

         var2 = new PropertyDescriptor("ConnectionPoolSize", LDAPServerMBean.class, var3, var4);
         var1.put("ConnectionPoolSize", var2);
         var2.setValue("description", "The LDAP connection pool size. Default is 6. ");
         setPropertyDescriptorDefault(var2, new Integer(6));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnectionRetryLimit")) {
         var3 = "getConnectionRetryLimit";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionRetryLimit";
         }

         var2 = new PropertyDescriptor("ConnectionRetryLimit", LDAPServerMBean.class, var3, var4);
         var1.put("ConnectionRetryLimit", var2);
         var2.setValue("description", "Specifies the number of times to attempt to connect to the LDAP server if the initial connection failed. ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Credential")) {
         var3 = "getCredential";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCredential";
         }

         var2 = new PropertyDescriptor("Credential", LDAPServerMBean.class, var3, var4);
         var1.put("Credential", var2);
         var2.setValue("description", "Returns the credential (generally a password) used to authenticate the LDAP user that is defined in the Principal attribute. ");
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

         var2 = new PropertyDescriptor("CredentialEncrypted", LDAPServerMBean.class, var3, var4);
         var1.put("CredentialEncrypted", var2);
         var2.setValue("description", "Returns the credential (generally a password) used to authenticate the LDAP user that is defined in the Principal attribute. ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Host")) {
         var3 = "getHost";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHost";
         }

         var2 = new PropertyDescriptor("Host", LDAPServerMBean.class, var3, var4);
         var1.put("Host", var2);
         var2.setValue("description", "Returns the host name or IP address of the LDAP server. ");
         setPropertyDescriptorDefault(var2, "localhost");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ParallelConnectDelay")) {
         var3 = "getParallelConnectDelay";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setParallelConnectDelay";
         }

         var2 = new PropertyDescriptor("ParallelConnectDelay", LDAPServerMBean.class, var3, var4);
         var1.put("ParallelConnectDelay", var2);
         var2.setValue("description", "Returns the number of seconds to delay when making concurrent attempts to connect to multiple servers. <p> If set to 0, connection attempts are serialized. An attempt is made to connect to the first server in the list. The next entry in the list is tried only if the attempt to connect to the current host fails. This might cause your application to block for unacceptably long time if a host is down. If set to greater than 0, another connection setup thread is started after this number of delay seconds has passed. ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Port")) {
         var3 = "getPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPort";
         }

         var2 = new PropertyDescriptor("Port", LDAPServerMBean.class, var3, var4);
         var1.put("Port", var2);
         var2.setValue("description", "Returns the port number on which the LDAP server is listening. ");
         setPropertyDescriptorDefault(var2, new Integer(389));
         var2.setValue("legalMax", new Integer(65534));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Principal")) {
         var3 = "getPrincipal";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrincipal";
         }

         var2 = new PropertyDescriptor("Principal", LDAPServerMBean.class, var3, var4);
         var1.put("Principal", var2);
         var2.setValue("description", "Returns the Distinguished Name (DN) of the LDAP user that is used by WebLogic Server to connect to the LDAP server. ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ResultsTimeLimit")) {
         var3 = "getResultsTimeLimit";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setResultsTimeLimit";
         }

         var2 = new PropertyDescriptor("ResultsTimeLimit", LDAPServerMBean.class, var3, var4);
         var1.put("ResultsTimeLimit", var2);
         var2.setValue("description", "Returns the maximum number of milliseconds to wait for results before timing out. If set to 0, there is no maximum time limit. ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("BindAnonymouslyOnReferrals")) {
         var3 = "isBindAnonymouslyOnReferrals";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBindAnonymouslyOnReferrals";
         }

         var2 = new PropertyDescriptor("BindAnonymouslyOnReferrals", LDAPServerMBean.class, var3, var4);
         var1.put("BindAnonymouslyOnReferrals", var2);
         var2.setValue("description", "Returns whether to anonymously bind when following referrals within the LDAP directory. If set to false, then the current Principal and Credential will be used. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CacheEnabled")) {
         var3 = "isCacheEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCacheEnabled";
         }

         var2 = new PropertyDescriptor("CacheEnabled", LDAPServerMBean.class, var3, var4);
         var1.put("CacheEnabled", var2);
         var2.setValue("description", "Returns whether to cache LDAP requests with the LDAP server. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("FollowReferrals")) {
         var3 = "isFollowReferrals";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFollowReferrals";
         }

         var2 = new PropertyDescriptor("FollowReferrals", LDAPServerMBean.class, var3, var4);
         var1.put("FollowReferrals", var2);
         var2.setValue("description", "Returns whether referrals will automatically be followed within the LDAP Directory. If set to false, then a Referral exception will be thrown when referrals are encountered during LDAP requests. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SSLEnabled")) {
         var3 = "isSSLEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSSLEnabled";
         }

         var2 = new PropertyDescriptor("SSLEnabled", LDAPServerMBean.class, var3, var4);
         var1.put("SSLEnabled", var2);
         var2.setValue("description", "Returns whether SSL will be used to connect to the LDAP server. ");
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
