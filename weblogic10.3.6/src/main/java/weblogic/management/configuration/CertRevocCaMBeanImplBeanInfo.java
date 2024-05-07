package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class CertRevocCaMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = CertRevocCaMBean.class;

   public CertRevocCaMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CertRevocCaMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CertRevocCaMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("CertRevocMBean")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.management.configuration");
      String var4 = (new String("This MBean represents the configuration of certificate revocation checking for a specific certificate authority. Default values for attributes in this MBean are derived from <code>CertRevocMBean</code>.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.CertRevocCaMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CrlDpDownloadTimeout")) {
         var3 = "getCrlDpDownloadTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlDpDownloadTimeout";
         }

         var2 = new PropertyDescriptor("CrlDpDownloadTimeout", CertRevocCaMBean.class, var3, var4);
         var1.put("CrlDpDownloadTimeout", var2);
         var2.setValue("description", "For this CA, determines the overall timeout for the Distribution Point CRL download, expressed in seconds. <p> The valid range is 1 thru 300 seconds. ");
         var2.setValue("legalMax", new Long(300L));
         var2.setValue("legalMin", new Long(1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("CrlDpUrl")) {
         var3 = "getCrlDpUrl";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlDpUrl";
         }

         var2 = new PropertyDescriptor("CrlDpUrl", CertRevocCaMBean.class, var3, var4);
         var1.put("CrlDpUrl", var2);
         var2.setValue("description", "For this CA, determines the CRL Distribution Point URL to use as failover or override for the URL found in the CRLDistributionPoints extension in the certificate. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getCrlDpUrlUsage")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, CertRevocCaMBean.DEFAULT_CRL_DP_URL);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CrlDpUrlUsage")) {
         var3 = "getCrlDpUrlUsage";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlDpUrlUsage";
         }

         var2 = new PropertyDescriptor("CrlDpUrlUsage", CertRevocCaMBean.class, var3, var4);
         var1.put("CrlDpUrlUsage", var2);
         var2.setValue("description", "For this CA, determines how <code>getCrlDpUrl</code> is used: as failover in case the URL in the certificate CRLDistributionPoints extension is invalid or not found, or as a value overriding the URL found in the certificate CRLDistributionPoints extension. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getCrlDpUrl")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, CertRevocCaMBean.DEFAULT_CRL_DP_URL_USAGE);
         var2.setValue("legalValues", new Object[]{CertRevocCaMBean.USAGE_FAILOVER, CertRevocCaMBean.USAGE_OVERRIDE});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DistinguishedName")) {
         var3 = "getDistinguishedName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDistinguishedName";
         }

         var2 = new PropertyDescriptor("DistinguishedName", CertRevocCaMBean.class, var3, var4);
         var1.put("DistinguishedName", var2);
         var2.setValue("description", "Determines the identity of this per-CA configuration using the distinguished name (defined in RFC 2253), which is used in certificates issued by the represented certificate authority. <p> For example:<br> &quot;CN=CertGenCAB, OU=FOR TESTING ONLY, O=MyOrganization, L=MyTown, ST=MyState, C=US&quot; <p> This will be used to match this configuration to issued certificates requiring revocation checking. ");
         setPropertyDescriptorDefault(var2, CertRevocCaMBean.DEFAULT_DISTINGUISHED_NAME);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MethodOrder")) {
         var3 = "getMethodOrder";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMethodOrder";
         }

         var2 = new PropertyDescriptor("MethodOrder", CertRevocCaMBean.class, var3, var4);
         var1.put("MethodOrder", var2);
         var2.setValue("description", "<p>For this CA, determines the certificate revocation checking method order.</p>  <p>NOTE THAT omission of a specific method disables that method.</p> ");
         var2.setValue("legalValues", new Object[]{CertRevocMBean.METHOD_OCSP, CertRevocMBean.METHOD_CRL, CertRevocMBean.METHOD_OCSP_THEN_CRL, CertRevocMBean.METHOD_CRL_THEN_OCSP});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspResponderCertIssuerName")) {
         var3 = "getOcspResponderCertIssuerName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspResponderCertIssuerName";
         }

         var2 = new PropertyDescriptor("OcspResponderCertIssuerName", CertRevocCaMBean.class, var3, var4);
         var1.put("OcspResponderCertIssuerName", var2);
         var2.setValue("description", "For this CA, determines the explicitly trusted OCSP responder certificate issuer name, when the attribute returned by <code>getOcspResponderExplicitTrustMethod</code> is “USE_ISSUER_SERIAL_NUMBER”. <p> The issuer name is formatted as a distinguished name per RFC 2253, for example \"CN=CertGenCAB, OU=FOR TESTING ONLY, O=MyOrganization, L=MyTown, ST=MyState, C=US\". <p> When <code>{@link #getOcspResponderCertIssuerName}</code> returns a non-null value then the <code>{@link #getOcspResponderCertSerialNumber}</code> must also be set.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getOcspResponderExplicitTrustMethod")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_CERT_ISSUER_NAME);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspResponderCertSerialNumber")) {
         var3 = "getOcspResponderCertSerialNumber";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspResponderCertSerialNumber";
         }

         var2 = new PropertyDescriptor("OcspResponderCertSerialNumber", CertRevocCaMBean.class, var3, var4);
         var1.put("OcspResponderCertSerialNumber", var2);
         var2.setValue("description", "For this CA, determines the explicitly trusted OCSP responder certificate serial number, when the attribute returned by <code>getOcspResponderExplicitTrustMethod</code> is “USE_ISSUER_SERIAL_NUMBER”. <p> The serial number is formatted as a hexidecimal string, with optional colon or space separators, for example \"2A:FF:00\". <p> When <code>{@link #getOcspResponderCertSerialNumber}</code> returns a non-null value then the <code>{@link #getOcspResponderCertIssuerName}</code> must also be set.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getOcspResponderExplicitTrustMethod")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_CERT_SERIAL_NUMBER);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspResponderCertSubjectName")) {
         var3 = "getOcspResponderCertSubjectName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspResponderCertSubjectName";
         }

         var2 = new PropertyDescriptor("OcspResponderCertSubjectName", CertRevocCaMBean.class, var3, var4);
         var1.put("OcspResponderCertSubjectName", var2);
         var2.setValue("description", "For this CA, determines the explicitly trusted OCSP responder certificate subject name, when the attribute returned by <code>getOcspResponderExplicitTrustMethod</code> is “USE_SUBJECT”. <p> The subject name is formatted as a distinguished name per RFC 2253, for example \"CN=CertGenCAB, OU=FOR TESTING ONLY, O=MyOrganization, L=MyTown, ST=MyState, C=US\". <p> In cases where the subject name alone is not sufficient to uniquely identify the certificate, then both the <code>{@link #getOcspResponderCertIssuerName}</code> and <code>{@link #getOcspResponderCertSerialNumber}</code> may be used instead. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getOcspResponderExplicitTrustMethod")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_CERT_SUBJECT_NAME);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspResponderExplicitTrustMethod")) {
         var3 = "getOcspResponderExplicitTrustMethod";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspResponderExplicitTrustMethod";
         }

         var2 = new PropertyDescriptor("OcspResponderExplicitTrustMethod", CertRevocCaMBean.class, var3, var4);
         var1.put("OcspResponderExplicitTrustMethod", var2);
         var2.setValue("description", "For this CA, determines whether the OCSP Explicit Trust model is enabled and how the trusted certificate is specified. <p> The valid values:<br/> <dl> <dt>\"NONE\" <dd>Explicit Trust is disabled <dt>“USE_SUBJECT” <dd>Identify the trusted certificate using the subject DN specified in the attribute <code>{@link #getOcspResponderCertSubjectName}</code>. <dt>“USE_ISSUER_SERIAL_NUMBER” <dd>Identify the trusted certificate using the issuer DN and certificate serial number specified in the attributes <code>{@link #getOcspResponderCertIssuerName}</code> and <code>{@link #getOcspResponderCertSerialNumber}</code>, respectively. ");
         setPropertyDescriptorDefault(var2, CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_EXPLICIT_TRUST_METHOD);
         var2.setValue("legalValues", new Object[]{CertRevocCaMBean.OCSP_EXPLICIT_TRUST_METHOD_NONE, CertRevocCaMBean.OCSP_EXPLICIT_TRUST_METHOD_USE_SUBJECT, CertRevocCaMBean.OCSP_EXPLICIT_TRUST_METHOD_USE_ISSUER_SERIAL_NUMBER});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspResponderUrl")) {
         var3 = "getOcspResponderUrl";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspResponderUrl";
         }

         var2 = new PropertyDescriptor("OcspResponderUrl", CertRevocCaMBean.class, var3, var4);
         var1.put("OcspResponderUrl", var2);
         var2.setValue("description", "<p>For this CA, determines the OCSP responder URL to use as failover or override for the URL found in the certificate AIA. The usage is determined by <code>getOcspResponderUrlUsage</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getOcspResponderUrlUsage")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_URL);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspResponderUrlUsage")) {
         var3 = "getOcspResponderUrlUsage";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspResponderUrlUsage";
         }

         var2 = new PropertyDescriptor("OcspResponderUrlUsage", CertRevocCaMBean.class, var3, var4);
         var1.put("OcspResponderUrlUsage", var2);
         var2.setValue("description", "<p>For this CA, determines how <code>getOcspResponderUrl</code> is used: as failover in case the URL in the certificate AIA is invalid or not found, or as a value overriding the URL found in the certificate AIA.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getOcspResponderUrl")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_URL_USAGE);
         var2.setValue("legalValues", new Object[]{CertRevocCaMBean.USAGE_FAILOVER, CertRevocCaMBean.USAGE_OVERRIDE});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspResponseTimeout")) {
         var3 = "getOcspResponseTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspResponseTimeout";
         }

         var2 = new PropertyDescriptor("OcspResponseTimeout", CertRevocCaMBean.class, var3, var4);
         var1.put("OcspResponseTimeout", var2);
         var2.setValue("description", "For this CA, determines the timeout for the OCSP response, expressed in seconds. <p> The valid range is 1 thru 300 seconds. ");
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

         var2 = new PropertyDescriptor("OcspTimeTolerance", CertRevocCaMBean.class, var3, var4);
         var1.put("OcspTimeTolerance", var2);
         var2.setValue("description", "For this CA, determines the time tolerance value for handling clock-skew differences between clients and responders, expressed in seconds. <p> The validity period of the response is extended both into the future and into the past by the specified amount of time, effectively widening the validity interval. <p> The value is &gt;=0 and &lt;=900. The maximum allowed tolerance is 15 minutes. ");
         var2.setValue("legalMax", new Integer(900));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CheckingDisabled")) {
         var3 = "isCheckingDisabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCheckingDisabled";
         }

         var2 = new PropertyDescriptor("CheckingDisabled", CertRevocCaMBean.class, var3, var4);
         var1.put("CheckingDisabled", var2);
         var2.setValue("description", "<p>For this CA, determines whether certificate revocation checking is disabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(CertRevocCaMBean.DEFAULT_CHECKING_DISABLED));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CrlDpBackgroundDownloadEnabled")) {
         var3 = "isCrlDpBackgroundDownloadEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlDpBackgroundDownloadEnabled";
         }

         var2 = new PropertyDescriptor("CrlDpBackgroundDownloadEnabled", CertRevocCaMBean.class, var3, var4);
         var1.put("CrlDpBackgroundDownloadEnabled", var2);
         var2.setValue("description", "For this CA, determines whether the CRL Distribution Point background downloading, to automatically update the local CRL cache, is enabled. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CrlDpEnabled")) {
         var3 = "isCrlDpEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrlDpEnabled";
         }

         var2 = new PropertyDescriptor("CrlDpEnabled", CertRevocCaMBean.class, var3, var4);
         var1.put("CrlDpEnabled", var2);
         var2.setValue("description", "For this CA, determines whether the CRL Distribution Point processing to update the local CRL cache is enabled. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FailOnUnknownRevocStatus")) {
         var3 = "isFailOnUnknownRevocStatus";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFailOnUnknownRevocStatus";
         }

         var2 = new PropertyDescriptor("FailOnUnknownRevocStatus", CertRevocCaMBean.class, var3, var4);
         var1.put("FailOnUnknownRevocStatus", var2);
         var2.setValue("description", "<p>For this CA, determines whether certificate path checking should fail, if revocation status could not be determined.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspNonceEnabled")) {
         var3 = "isOcspNonceEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspNonceEnabled";
         }

         var2 = new PropertyDescriptor("OcspNonceEnabled", CertRevocCaMBean.class, var3, var4);
         var1.put("OcspNonceEnabled", var2);
         var2.setValue("description", "<p>For this CA, determines whether a nonce is sent with OCSP requests, to force a fresh (not pre-signed) response.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OcspResponseCacheEnabled")) {
         var3 = "isOcspResponseCacheEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOcspResponseCacheEnabled";
         }

         var2 = new PropertyDescriptor("OcspResponseCacheEnabled", CertRevocCaMBean.class, var3, var4);
         var1.put("OcspResponseCacheEnabled", var2);
         var2.setValue("description", "<p>For this CA, determines whether the OCSP response local cache is enabled.</p> ");
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
