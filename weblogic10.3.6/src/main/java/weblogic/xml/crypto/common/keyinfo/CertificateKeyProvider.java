package weblogic.xml.crypto.common.keyinfo;

import java.math.BigInteger;
import java.security.Key;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.wss.BSTUtils;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.security.utils.Utils;

public class CertificateKeyProvider extends KeypairProvider {
   private final X509Certificate cert;

   public CertificateKeyProvider(X509Certificate var1, PrivateKey var2, String var3, String var4) {
      this(var1, var2, var3, var4, (SecurityToken)null);
   }

   public CertificateKeyProvider(X509Certificate var1, PrivateKey var2, String var3, String var4, SecurityToken var5) {
      super(var1.getPublicKey(), var2, var3, Utils.getSubjectKeyIdentifier(var1), var4, var5);
      this.cert = var1;
   }

   public CertificateKeyProvider(BinarySecurityToken var1, String var2, String var3) {
      this(var1.getCertificate(), var1.getPrivateKey(), var2, var3, var1);
   }

   public KeySelectorResult getKeyBySubjectName(String var1, String var2, KeySelector.Purpose var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("SubjectName cannot be null");
      } else {
         Principal var4 = this.cert.getSubjectDN();
         String var5 = var4.getName();
         return var1.equals(var5) ? this.getKey(var2, var3) : null;
      }
   }

   public KeySelectorResult getKeyByIdentifier(byte[] var1, String var2, KeySelector.Purpose var3) {
      KeySelectorResult var4 = super.getKeyByIdentifier(var1, var2, var3);
      return var4 == null && BSTUtils.matchesThumbprint(this.cert, var1) ? this.getKey(var2, var3) : var4;
   }

   public KeySelectorResult getKeyByIssuerSerial(String var1, BigInteger var2, String var3, KeySelector.Purpose var4) {
      if (var1 == null) {
         throw new IllegalArgumentException("IssuerName cannot be null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("Serial number cannot be null");
      } else {
         BigInteger var5 = this.cert.getSerialNumber();
         if (!var2.equals(var5)) {
            return null;
         } else {
            return !BSTUtils.matches(var1, this.cert) ? null : this.getKey(var3, var4);
         }
      }
   }

   public KeySelectorResult getKeyByName(String var1, String var2, KeySelector.Purpose var3) {
      return this.getKeyBySubjectName(var1, var2, var3);
   }

   protected KeySelectorResult getResult(Key var1) {
      X509KeySelectorResult var2 = new X509KeySelectorResult(var1, this.cert);
      var2.setSecurityToken(this.getSecurityToken());
      return var2;
   }
}
