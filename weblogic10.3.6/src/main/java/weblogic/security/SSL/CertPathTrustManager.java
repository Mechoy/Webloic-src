package weblogic.security.SSL;

import java.security.cert.X509Certificate;
import weblogic.security.utils.CertPathTrustManagerUtils;

public final class CertPathTrustManager implements TrustManager {
   private int certPathValStyle = 0;

   public void setBuiltinSSLValidationAndCertPathValidators() {
      this.certPathValStyle = 1;
   }

   public void setBuiltinSSLValidationOnly() {
      this.certPathValStyle = 2;
   }

   public void setUseConfiguredSSLValidation() {
      this.certPathValStyle = 0;
   }

   public boolean certificateCallback(X509Certificate[] var1, int var2) {
      return CertPathTrustManagerUtils.certificateCallback(this.certPathValStyle, var1, var2);
   }
}
