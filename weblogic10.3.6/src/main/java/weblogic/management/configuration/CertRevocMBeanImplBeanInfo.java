package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class CertRevocMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = CertRevocMBean.class;

   public CertRevocMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CertRevocMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CertRevocMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("CertRevocCaMBean")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.management.configuration");
      String var4 = (new String("This MBean represents the configuration of the certificate revocation checking across all certificate authorities. Many of the attributes in this MBean may be overridden per certificate authority using the specific <code>CertRevocCaMBean</code>.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.CertRevocMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CertRevocCas")) {
         var3 = "getCertRevocCas";
         var4 = null;
         var2 = new PropertyDescriptor("CertRevocCas", CertRevocMBean.class, var3, var4);
         var1.put("CertRevocCas", var2);
         var2.setValue("description", "<p>Returns the CertRevocCaMBeans representing the certificate authority overrides, which have been configured to be part of this certificate revocation checking configuration.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createCertRevocCa");
         var2.setValue("destroyer", "destroyCertRevocCa");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CrlCacheRefreshPeriodPercent")) {
         var3 = "getCrlCacheRefreshPeriodPercent";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlCacheRefreshPeriodPercent";
         }

         var2 = new PropertyDescriptor("CrlCacheRefreshPeriodPercent", CertRevocMBean.class, var3, var4);
         var1.put("CrlCacheRefreshPeriodPercent", var2);
         var2.setValue("description", "Determines the refresh period for the CRL local cache, expressed as a percentage of the validity period of the CRL. <p> For example, for a validity period of 10 hours, a value of 10% specifies a refresh every 1 hour. <p> The validity period is determined by the CRL, and is calculated as the (next reported update time) - (this update time). <p> The valid range is 1 through 100. ");
         setPropertyDescriptorDefault(var2, new Integer(CertRevocMBean.DEFAULT_CRL_CACHE_REFRESH_PERIOD_PERCENT));
         var2.setValue("legalMax", new Integer(100));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CrlCacheType")) {
         var3 = "getCrlCacheType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlCacheType";
         }

         var2 = new PropertyDescriptor("CrlCacheType", CertRevocMBean.class, var3, var4);
         var1.put("CrlCacheType", var2);
         var2.setValue("description", "Determines the type of CRL cache, related to the physical storage of the CRLs. <p> The value specified in this attribute determines which related <code>CrlCacheType*</code> attributes apply. For example, if <code>CrlCacheType</code> is <code>ldap</code>, see related attributes like <code>{@link #getCrlCacheTypeLdapHostname}</code>. ");
         setPropertyDescriptorDefault(var2, CertRevocMBean.DEFAULT_CRL_CACHE_TYPE);
         var2.setValue("legalValues", new Object[]{CertRevocMBean.CRL_CACHE_TYPE_FILE, CertRevocMBean.CRL_CACHE_TYPE_LDAP});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CrlCacheTypeLdapHostname")) {
         var3 = "getCrlCacheTypeLdapHostname";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlCacheTypeLdapHostname";
         }

         var2 = new PropertyDescriptor("CrlCacheTypeLdapHostname", CertRevocMBean.class, var3, var4);
         var1.put("CrlCacheTypeLdapHostname", var2);
         var2.setValue("description", "Determines the remote hostname for the LDAP server containing CRLs. <p> This attribute applies when value <code>{@link CertRevocMBean#CRL_CACHE_TYPE_LDAP}</code> is returned from <code>{@link #getCrlCacheType}</code>. ");
         setPropertyDescriptorDefault(var2, CertRevocMBean.DEFAULT_CRL_CACHE_TYPE_LDAP_HOST_NAME);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CrlCacheTypeLdapPort")) {
         var3 = "getCrlCacheTypeLdapPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlCacheTypeLdapPort";
         }

         var2 = new PropertyDescriptor("CrlCacheTypeLdapPort", CertRevocMBean.class, var3, var4);
         var1.put("CrlCacheTypeLdapPort", var2);
         var2.setValue("description", "Determines the remote port for the LDAP server containing CRLs. <p> This attribute applies when value <code>{@link CertRevocMBean#CRL_CACHE_TYPE_LDAP}</code> is returned from <code>{@link #getCrlCacheType}</code>. <p> The valid range is -1, 1 through 65535. ");
         setPropertyDescriptorDefault(var2, new Integer(CertRevocMBean.DEFAULT_CRL_CACHE_TYPE_LDAP_PORT));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CrlCacheTypeLdapSearchTimeout")) {
         var3 = "getCrlCacheTypeLdapSearchTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlCacheTypeLdapSearchTimeout";
         }

         var2 = new PropertyDescriptor("CrlCacheTypeLdapSearchTimeout", CertRevocMBean.class, var3, var4);
         var1.put("CrlCacheTypeLdapSearchTimeout", var2);
         var2.setValue("description", "Determines how long to wait for CRL search results from the LDAP server. <p> This attribute applies when value <code>{@link CertRevocMBean#CRL_CACHE_TYPE_LDAP}</code> is returned from <code>{@link #getCrlCacheType}</code>. <p> The valid range is 1 thru 300 seconds. ");
         setPropertyDescriptorDefault(var2, new Integer(CertRevocMBean.DEFAULT_CRL_CACHE_TYPE_LDAP_SEARCH_TIMEOUT));
         var2.setValue("legalMax", new Integer(300));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CrlDpDownloadTimeout")) {
         var3 = "getCrlDpDownloadTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlDpDownloadTimeout";
         }

         var2 = new PropertyDescriptor("CrlDpDownloadTimeout", CertRevocMBean.class, var3, var4);
         var1.put("CrlDpDownloadTimeout", var2);
         var2.setValue("description", "Determines the overall timeout for the Distribution Point CRL download, expressed in seconds. <p> The valid range is 1 thru 300 seconds. ");
         setPropertyDescriptorDefault(var2, new Long(CertRevocMBean.DEFAULT_CRL_DP_DOWNLOAD_TIMEOUT));
         var2.setValue("legalMax", new Long(300L));
         var2.setValue("legalMin", new Long(1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MethodOrder")) {
         var3 = "getMethodOrder";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMethodOrder";
         }

         var2 = new PropertyDescriptor("MethodOrder", CertRevocMBean.class, var3, var4);
         var1.put("MethodOrder", var2);
         var2.setValue("description", "<p>Determines the certificate revocation checking method order.</p>  <p>NOTE THAT omission of a specific method disables that method.</p> ");
         setPropertyDescriptorDefault(var2, CertRevocMBean.DEFAULT_METHOD_ORDER);
         var2.setValue("legalValues", new Object[]{CertRevocMBean.METHOD_OCSP, CertRevocMBean.METHOD_CRL, CertRevocMBean.METHOD_OCSP_THEN_CRL, CertRevocMBean.METHOD_CRL_THEN_OCSP});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspResponseCacheCapacity")) {
         var3 = "getOcspResponseCacheCapacity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspResponseCacheCapacity";
         }

         var2 = new PropertyDescriptor("OcspResponseCacheCapacity", CertRevocMBean.class, var3, var4);
         var1.put("OcspResponseCacheCapacity", var2);
         var2.setValue("description", "Determines the maximum number of entries supported by the OCSP response local cache. The minimum value is 1. ");
         setPropertyDescriptorDefault(var2, new Integer(CertRevocMBean.DEFAULT_OCSP_RESPONSE_CACHE_CAPACITY));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspResponseCacheRefreshPeriodPercent")) {
         var3 = "getOcspResponseCacheRefreshPeriodPercent";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspResponseCacheRefreshPeriodPercent";
         }

         var2 = new PropertyDescriptor("OcspResponseCacheRefreshPeriodPercent", CertRevocMBean.class, var3, var4);
         var1.put("OcspResponseCacheRefreshPeriodPercent", var2);
         var2.setValue("description", "Determines the refresh period for the OCSP response local cache, expressed as a percentage of the validity period of the response. <p> For example, for a validity period of 10 hours, a value of 10% specifies a refresh every 1 hour. <p> The validity period is determined by the OCSP response, and is calculated as the (next reported update time) - (this update time). <p> The valid range is 1 through 100. ");
         setPropertyDescriptorDefault(var2, new Integer(CertRevocMBean.DEFAULT_OCSP_RESPONSE_CACHE_REFRESH_PERIOD_PERCENT));
         var2.setValue("legalMax", new Integer(100));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspResponseTimeout")) {
         var3 = "getOcspResponseTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspResponseTimeout";
         }

         var2 = new PropertyDescriptor("OcspResponseTimeout", CertRevocMBean.class, var3, var4);
         var1.put("OcspResponseTimeout", var2);
         var2.setValue("description", "Determines the timeout for the OCSP response, expressed in seconds. <p> The valid range is 1 thru 300 seconds. ");
         setPropertyDescriptorDefault(var2, new Long(CertRevocMBean.DEFAULT_OCSP_RESPONSE_TIMEOUT));
         var2.setValue("legalMax", new Long(300L));
         var2.setValue("legalMin", new Long(1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspTimeTolerance")) {
         var3 = "getOcspTimeTolerance";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspTimeTolerance";
         }

         var2 = new PropertyDescriptor("OcspTimeTolerance", CertRevocMBean.class, var3, var4);
         var1.put("OcspTimeTolerance", var2);
         var2.setValue("description", "Determines the time tolerance value for handling clock-skew differences between clients and responders, expressed in seconds. <p> The validity period of the response is extended both into the future and into the past by the specified amount of time, effectively widening the validity interval. <p> The value is &gt;=0 and &lt;=900. The maximum allowed tolerance is 15 minutes. ");
         setPropertyDescriptorDefault(var2, new Integer(CertRevocMBean.DEFAULT_OCSP_TIME_TOLERANCE));
         var2.setValue("legalMax", new Integer(900));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CheckingEnabled")) {
         var3 = "isCheckingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCheckingEnabled";
         }

         var2 = new PropertyDescriptor("CheckingEnabled", CertRevocMBean.class, var3, var4);
         var1.put("CheckingEnabled", var2);
         var2.setValue("description", "<p>Determines whether certificate revocation checking is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(CertRevocMBean.DEFAULT_CHECKING_ENABLED));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CrlDpBackgroundDownloadEnabled")) {
         var3 = "isCrlDpBackgroundDownloadEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlDpBackgroundDownloadEnabled";
         }

         var2 = new PropertyDescriptor("CrlDpBackgroundDownloadEnabled", CertRevocMBean.class, var3, var4);
         var1.put("CrlDpBackgroundDownloadEnabled", var2);
         var2.setValue("description", "Determines whether the CRL Distribution Point background downloading, to automatically update the local CRL cache, is enabled. ");
         setPropertyDescriptorDefault(var2, new Boolean(CertRevocMBean.DEFAULT_CRL_DP_BACKGROUND_DOWNLOAD_ENABLED));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CrlDpEnabled")) {
         var3 = "isCrlDpEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlDpEnabled";
         }

         var2 = new PropertyDescriptor("CrlDpEnabled", CertRevocMBean.class, var3, var4);
         var1.put("CrlDpEnabled", var2);
         var2.setValue("description", "Determines whether the CRL Distribution Point processing to update the local CRL cache is enabled. ");
         setPropertyDescriptorDefault(var2, new Boolean(CertRevocMBean.DEFAULT_CRL_DP_ENABLED));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FailOnUnknownRevocStatus")) {
         var3 = "isFailOnUnknownRevocStatus";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFailOnUnknownRevocStatus";
         }

         var2 = new PropertyDescriptor("FailOnUnknownRevocStatus", CertRevocMBean.class, var3, var4);
         var1.put("FailOnUnknownRevocStatus", var2);
         var2.setValue("description", "<p>Determines whether certificate path checking should fail, if revocation status could not be determined.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(CertRevocMBean.DEFAULT_FAIL_ON_UNKNOWN_REVOC_STATUS));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspNonceEnabled")) {
         var3 = "isOcspNonceEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspNonceEnabled";
         }

         var2 = new PropertyDescriptor("OcspNonceEnabled", CertRevocMBean.class, var3, var4);
         var1.put("OcspNonceEnabled", var2);
         var2.setValue("description", "<p>Determines whether a nonce is sent with OCSP requests, to force a fresh (not pre-signed) response.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(CertRevocMBean.DEFAULT_OCSP_NONCE_ENABLED));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspResponseCacheEnabled")) {
         var3 = "isOcspResponseCacheEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspResponseCacheEnabled";
         }

         var2 = new PropertyDescriptor("OcspResponseCacheEnabled", CertRevocMBean.class, var3, var4);
         var1.put("OcspResponseCacheEnabled", var2);
         var2.setValue("description", "<p>Determines whether the OCSP response local cache is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(CertRevocMBean.DEFAULT_OCSP_RESPONSE_CACHE_ENABLED));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = CertRevocMBean.class.getMethod("createCertRevocCa", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "Unique short name ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>This is the factory method for certificate revocation checking configuration CA overrides.</p>  <p>The short name, which is specified, must be unique among all object instances of type CertRevocCaMBean. The new CA override, which is created, will have this certificate revocation checking configuration as its parent and must be destroyed with the <code>{@link #destroyCertRevocCa}</code> method.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "CertRevocCas");
      }

      var3 = CertRevocMBean.class.getMethod("destroyCertRevocCa", CertRevocCaMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("child", "CertRevocCaMBean to destroy ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Destroys and removes a certificate authority override, which is a child of this certificate revocation checking configuration.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "CertRevocCas");
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = CertRevocMBean.class.getMethod("lookupCertRevocCa", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "Unique short name ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Lookup a particular CertRevocCaMBean from the list.</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "CertRevocCas");
      }

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
