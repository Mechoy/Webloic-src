package weblogic.xml.security.keyinfo;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import weblogic.xml.security.wsse.BinarySecurityToken;
import weblogic.xml.security.wsse.Token;

public class KeyProviderFactory {
   public static KeyProvider create(PrivateKey var0) {
      return new KeypairProvider((PublicKey)null, var0, (String)null, (byte[])null, (String)null);
   }

   public static KeyProvider create(BinarySecurityToken var0) {
      return new CertificateKeyProvider(var0.getCertificate(), var0.getPrivateKey(), (String)null, getURI(var0));
   }

   public static KeyProvider create(X509Certificate var0, PrivateKey var1) {
      Object var2;
      if (var0 == null) {
         var2 = create(var1);
      } else {
         var2 = new CertificateKeyProvider(var0, var1, (String)null, (String)null);
      }

      return (KeyProvider)var2;
   }

   private static String getURI(Token var0) {
      String var1 = var0.getId();
      String var2 = var1 != null ? "#" + var0.getId() : null;
      return var2;
   }
}
