package weblogic.security.utils;

import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.security.auth.x500.X500Principal;
import weblogic.security.SecurityMessagesTextFormatter;

public class X509Utils {
   private static SecurityMessagesTextFormatter formatter = SecurityMessagesTextFormatter.getInstance();

   public static boolean isEmpty(CertPath var0) {
      if (var0 != null) {
         List var1 = var0.getCertificates();
         if (var1 != null && var1.size() > 0) {
            return false;
         }
      }

      return true;
   }

   public static boolean containsNonX509Certificate(CertPath var0) {
      if (isEmpty(var0)) {
         return false;
      } else {
         List var1 = var0.getCertificates();

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            Object var3 = var1.get(var2);
            if (!(var3 instanceof X509Certificate)) {
               return true;
            }
         }

         return false;
      }
   }

   public static X509Certificate[] getCertificates(CertPath var0) {
      if (isEmpty(var0)) {
         return new X509Certificate[0];
      } else {
         List var1 = var0.getCertificates();
         return (X509Certificate[])((X509Certificate[])var1.toArray(new X509Certificate[var1.size()]));
      }
   }

   public static void validateOrdered(CertPath var0) throws CertificateException {
      if (!isEmpty(var0)) {
         if (containsNonX509Certificate(var0)) {
            throw new AssertionError("Received a cert path containing a non-X509 certificate");
         } else {
            X509Certificate[] var1 = getCertificates(var0);
            if (var1 != null && var1.length >= 2) {
               for(int var2 = 0; var2 < var1.length - 1; ++var2) {
                  if (isSelfSigned(var1[var2])) {
                     throw new CertificateException(formatter.getSelfSignedCertificateInChainError(var1[var2].toString()));
                  }

                  validateIssuedBy(var1[var2], var1[var2 + 1]);
               }

            }
         }
      }
   }

   public static boolean isOrdered(CertPath var0) {
      try {
         validateOrdered(var0);
         return true;
      } catch (CertificateException var2) {
         return false;
      }
   }

   public static String getName(X500Principal var0) {
      return var0 != null ? var0.getName("RFC2253") : null;
   }

   public static boolean sameX500Principal(X500Principal var0, X500Principal var1) {
      if (var0 == null && var1 == null) {
         return true;
      } else if (var0 != null && var1 == null) {
         return false;
      } else if (var0 == null && var1 != null) {
         return false;
      } else {
         String var2 = getName(var0);
         String var3 = getName(var1);
         return var2.equals(var3);
      }
   }

   public static String getSubjectDN(X509Certificate var0) {
      return getName(var0.getSubjectX500Principal());
   }

   public static String getIssuerDN(X509Certificate var0) {
      return getName(var0.getIssuerX500Principal());
   }

   public static void validateIssuedBy(X509Certificate var0, X509Certificate var1) throws CertificateException {
      if (!getIssuerDN(var0).equals(getSubjectDN(var1))) {
         throw new CertificateException(formatter.getIssuerDNMismatchError(var0.toString(), var1.toString()));
      } else {
         try {
            var0.verify(var1.getPublicKey());
         } catch (Exception var3) {
            throw new CertificateException(formatter.getCertificateNotSignedByIssuerError(var0.toString(), var1.toString()));
         }
      }
   }

   public static boolean isIssuedBy(X509Certificate var0, X509Certificate var1) {
      try {
         validateIssuedBy(var0, var1);
         return true;
      } catch (CertificateException var3) {
         return false;
      }
   }

   public static boolean isSelfSigned(X509Certificate var0) {
      return isIssuedBy(var0, var0);
   }
}
