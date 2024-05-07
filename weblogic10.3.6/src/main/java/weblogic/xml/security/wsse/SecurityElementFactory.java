package weblogic.xml.security.wsse;

import java.security.PrivateKey;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import weblogic.xml.security.NamedKey;
import weblogic.xml.security.UserInfo;
import weblogic.xml.security.encryption.EncryptionException;
import weblogic.xml.security.wsse.v200207.SecurityElementFactoryImpl;

/** @deprecated */
public abstract class SecurityElementFactory {
   private static SecurityElementFactory defaultFactory = null;

   public static final SecurityElementFactory getDefaultFactory() {
      if (defaultFactory == null) {
         defaultFactory = new SecurityElementFactoryImpl();
      }

      return defaultFactory;
   }

   public static final void setDefaultFactory(SecurityElementFactory var0) {
      defaultFactory = var0;
   }

   public static final SecurityElementFactory newInstance(String var0) {
      if (weblogic.xml.security.wsse.v200207.WSSEConstants.WSSE_URI.equals(var0)) {
         return new SecurityElementFactoryImpl();
      } else {
         throw new IllegalArgumentException("Unsupported namespace / version");
      }
   }

   public abstract Security createSecurity(String var1);

   public abstract Token createToken(String var1, String var2, String var3);

   public abstract Token createToken(String var1, String var2);

   public abstract Token createToken(UserInfo var1);

   public abstract Token createToken(X509Certificate var1, PrivateKey var2);

   public abstract Token createToken(X509Certificate[] var1, PrivateKey var2);

   public abstract Token createToken(CertPath var1, PrivateKey var2);

   public abstract NamedKey createKey(String var1, String var2) throws EncryptionException;

   public abstract NamedKey createKey(SecretKey var1, String var2);
}
