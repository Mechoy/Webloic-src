package weblogic.xml.security.keyinfo;

import java.math.BigInteger;
import java.security.Key;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import weblogic.xml.security.wsse.BinarySecurityToken;

public class CertificateKeyProvider extends KeypairProvider {
   private final X509Certificate cert;

   public CertificateKeyProvider(X509Certificate var1, PrivateKey var2, String var3, String var4) {
      super(var1.getPublicKey(), var2, var3, weblogic.xml.security.utils.Utils.getSubjectKeyIdentifier(var1), var4);
      this.cert = var1;
   }

   public CertificateKeyProvider(BinarySecurityToken var1, String var2, String var3) {
      this(var1.getCertificate(), var1.getPrivateKey(), var2, var3);
   }

   public KeyResult getKeyBySubjectName(String var1, String var2, KeyPurpose var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("SubjectName cannot be null");
      } else {
         Principal var4 = this.cert.getSubjectDN();
         String var5 = var4.getName();
         return var1.equals(var5) ? this.getKey(var2, var3) : null;
      }
   }

   public KeyResult getKeyByIssuerSerial(String var1, BigInteger var2, String var3, KeyPurpose var4) {
      if (var1 == null) {
         throw new IllegalArgumentException("IssuerName cannot be null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("Serial number cannot be null");
      } else {
         Principal var5 = this.cert.getIssuerDN();
         String var6 = var5.getName();
         if (!var1.equals(var6)) {
            return null;
         } else {
            BigInteger var7 = this.cert.getSerialNumber();
            return !var2.equals(var7) ? null : this.getKey(var3, var4);
         }
      }
   }

   protected KeyResult getResult(Key var1) {
      return new X509KeyResult(var1, this.cert);
   }
}
