package weblogic.security.pki.revocation.common;

import java.security.cert.X509Certificate;

abstract class AbstractRevocChecker {
   private final AbstractCertRevocContext context;

   AbstractRevocChecker(AbstractCertRevocContext var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Non-null AbstractCertRevocContext expected.");
      } else {
         this.context = var1;
      }
   }

   final AbstractCertRevocContext getContext() {
      return this.context;
   }

   abstract CertRevocStatus getCertRevocStatus(X509Certificate var1, X509Certificate var2);
}
