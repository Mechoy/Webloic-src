package weblogic.security.pki.revocation.common;

import com.rsa.certj.CertJVersion;
import com.rsa.certj.cert.CertificateException;
import com.rsa.certj.cert.X509CRL;
import com.rsa.certj.cert.X509Certificate;
import com.rsa.certj.cert.X509V3Extensions;
import com.rsa.certj.cert.extensions.X509V3Extension;
import com.rsa.jsafe.CryptoJ;
import com.rsa.jsafe.JSAFE_PrivateKey;
import java.security.PrivateKey;
import java.util.logging.Level;

final class RsaUtil {
   private static final String DEVICE_NAME = "Java";

   static String getCryptoJDeviceList() {
      return "Java";
   }

   static boolean isFIPS140UsageOk(LogListener var0) {
      if (CertJVersion.isFIPS140Compliant()) {
         if (!CryptoJ.selfTestPassed()) {
            if (null != var0 && var0.isLoggable(Level.FINE)) {
               var0.log(Level.FINE, "Using FIPS 140 compliant implementation, however self-tests failed.");
            }

            return false;
         }

         if (null != var0 && var0.isLoggable(Level.FINEST)) {
            var0.log(Level.FINEST, "Using FIPS 140 compliant implementation, self-tests passed.");
         }
      } else if (null != var0 && var0.isLoggable(Level.FINEST)) {
         var0.log(Level.FINEST, "Not using FIPS 140 compliant implementation.");
      }

      return true;
   }

   static X509Certificate toRsaCert(java.security.cert.X509Certificate var0, LogListener var1) {
      Util.checkNotNull("java.security.cert.X509Certificate", var0);

      try {
         byte var2 = 0;
         byte var3 = 0;
         return new X509Certificate(var0.getEncoded(), var2, var3);
      } catch (Exception var4) {
         if (null != var1 && var1.isLoggable(Level.FINE)) {
            var1.log(Level.FINE, var4, "Unable to convert java.security.cert.X509Certificate {0}.", var0);
         }

         return null;
      }
   }

   static JSAFE_PrivateKey toRsaPrivateKey(PrivateKey var0, LogListener var1) {
      Util.checkNotNull("java.security.PrivateKey", var0);

      try {
         byte var2 = 0;
         String var3 = getCryptoJDeviceList();
         return JSAFE_PrivateKey.getInstance(var0.getEncoded(), var2, var3);
      } catch (Exception var4) {
         if (null != var1 && var1.isLoggable(Level.FINE)) {
            var1.log(Level.FINE, var4, "Unable to convert java.security.PrivateKey.");
         }

         return null;
      }
   }

   static Boolean evalRevocStatusCode(CertRevocCheckMethodList.SelectableMethod var0, int var1, LogListener var2) {
      Util.checkNotNull("SelectableMethod", var0);
      switch (var1) {
         case 0:
            return Boolean.FALSE;
         case 1:
            return Boolean.TRUE;
         case 2:
            if (null != var2 && var2.isLoggable(Level.FINEST)) {
               var2.log(Level.FINEST, "Revocation status unknown from {0}.", var0);
            }

            return null;
         default:
            if (null != var2 && var2.isLoggable(Level.FINE)) {
               var2.log(Level.FINE, "Revocation status unknown from {0}, unknown revocation status code {1}.", var0, var1);
            }

            return null;
      }
   }

   static X509V3Extension getExtension(X509CRL var0, int var1) {
      return var0 == null ? null : getExtension(var0.getExtensions(), var1);
   }

   static X509V3Extension getExtension(X509Certificate var0, int var1) {
      return var0 == null ? null : getExtension(var0.getExtensions(), var1);
   }

   static X509V3Extension getExtension(X509V3Extensions var0, int var1) {
      X509V3Extension var2 = null;
      if (var0 != null) {
         try {
            var2 = var0.getExtensionByType(var1);
         } catch (CertificateException var4) {
         }
      }

      return var2;
   }
}
