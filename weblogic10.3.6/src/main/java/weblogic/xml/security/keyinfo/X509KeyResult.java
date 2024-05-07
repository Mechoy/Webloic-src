package weblogic.xml.security.keyinfo;

import java.security.Key;
import java.security.cert.X509Certificate;

public class X509KeyResult extends KeyResult {
   private final X509Certificate certificate;

   public X509KeyResult(Key var1, X509Certificate var2) {
      super(var1);
      this.certificate = var2;
   }

   public X509Certificate getCertificate() {
      return this.certificate;
   }
}
