package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.HttpsToken;
import weblogic.wsee.security.wssp.HttpsTokenAssertion;

public class HttpsTokenAssertionImpl implements HttpsTokenAssertion {
   boolean isHttpBasicAuthenticationRequired = false;
   boolean isClientCertificateRequired = false;

   HttpsTokenAssertionImpl(HttpsToken var1) {
      if (var1 != null) {
         this.isHttpBasicAuthenticationRequired = var1.getHttpBasicAuthentication() != null;
         this.isClientCertificateRequired = var1.isClientCertRequired();
      }

   }

   public boolean isHttpBasicAuthenticationRequired() {
      return this.isHttpBasicAuthenticationRequired;
   }

   public boolean isClientCertificateRequired() {
      return this.isClientCertificateRequired;
   }
}
