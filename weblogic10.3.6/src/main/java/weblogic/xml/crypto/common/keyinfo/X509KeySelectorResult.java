package weblogic.xml.crypto.common.keyinfo;

import java.security.Key;
import java.security.cert.X509Certificate;

public class X509KeySelectorResult extends KeySelectorResultImpl {
   private final X509Certificate certificate;

   public X509KeySelectorResult(Key var1, X509Certificate var2) {
      super(var1);
      this.certificate = var2;
   }

   public X509Certificate getCertificate() {
      return this.certificate;
   }
}
