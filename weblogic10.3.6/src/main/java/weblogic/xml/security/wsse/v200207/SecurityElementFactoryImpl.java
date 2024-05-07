package weblogic.xml.security.wsse.v200207;

import java.security.PrivateKey;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import weblogic.xml.security.NamedKey;
import weblogic.xml.security.UserInfo;
import weblogic.xml.security.encryption.EncryptionAlgorithm;
import weblogic.xml.security.encryption.EncryptionException;
import weblogic.xml.security.encryption.EncryptionMethod;
import weblogic.xml.security.wsse.Security;
import weblogic.xml.security.wsse.SecurityElementFactory;
import weblogic.xml.security.wsse.Token;

public class SecurityElementFactoryImpl extends SecurityElementFactory {
   public final Security createSecurity(String var1) {
      return new SecurityImpl(var1);
   }

   public Token createToken(X509Certificate[] var1, PrivateKey var2) {
      return new BinarySecurityTokenImpl(var1, var2);
   }

   public Token createToken(CertPath var1, PrivateKey var2) {
      return new BinarySecurityTokenImpl(var1, var2);
   }

   public final Token createToken(UserInfo var1) {
      return this.createToken(var1.getUsername(), var1.getPassword());
   }

   public final Token createToken(String var1, String var2) {
      return new UsernameTokenImpl(var1, var2);
   }

   public NamedKey createKey(String var1, String var2) throws EncryptionException {
      EncryptionMethod var3 = EncryptionMethod.get(var1);
      SecretKey var4 = (SecretKey)((EncryptionAlgorithm)var3).generateKey();
      return this.createKey(var4, var2);
   }

   public NamedKey createKey(SecretKey var1, String var2) {
      return new NamedKey(var1, var2);
   }

   public final Token createToken(String var1, String var2, String var3) {
      return new UsernameTokenImpl(var1, var2, var3);
   }

   public final Token createToken(X509Certificate var1, PrivateKey var2) {
      return new BinarySecurityTokenImpl(var1, var2);
   }
}
