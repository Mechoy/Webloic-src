package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class HttpsToken extends NestedSecurityPolicy12Assertion {
   public static final String HTTPS_TOKEN = "HttpsToken";

   public QName getName() {
      return new QName(this.getNamespace(), "HttpsToken", "sp");
   }

   public HttpBasicAuthentication getHttpBasicAuthentication() {
      return (HttpBasicAuthentication)this.getNestedAssertion(HttpBasicAuthentication.class);
   }

   public RequireClientCertificate getRequireClientCertificate() {
      return (RequireClientCertificate)this.getNestedAssertion(RequireClientCertificate.class);
   }

   public boolean isClientCertRequired() {
      return this.getRequireClientCertificate() != null;
   }
}
