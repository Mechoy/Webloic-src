package weblogic.management.configuration;

import java.math.BigInteger;
import java.net.URI;
import javax.security.auth.x500.X500Principal;

public class CertRevocValidator {
   public static void validateCertRevoc(CertRevocMBean var0) throws IllegalArgumentException {
      validateUniqueDn(var0);
   }

   private static void validateUniqueDn(CertRevocMBean var0) throws IllegalArgumentException {
      CertRevocCaMBean[] var1 = var0.getCertRevocCas();
      if (null != var1) {
         CertRevocCaMBean[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            CertRevocCaMBean var5 = var2[var4];
            if (null != var5) {
               X500Principal var6 = tryGetX500Principal(var5);
               if (null != var6) {
                  CertRevocCaMBean[] var7 = var1;
                  int var8 = var1.length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     CertRevocCaMBean var10 = var7[var9];
                     if (null != var10 && var5 != var10) {
                        X500Principal var11 = tryGetX500Principal(var10);
                        if (null != var11 && var6.equals(var11)) {
                           throw new IllegalArgumentException("Illegal duplicate distinguished name: " + var6.getName());
                        }
                     }
                  }
               }
            }
         }

      }
   }

   private static X500Principal tryGetX500Principal(CertRevocCaMBean var0) {
      X500Principal var1 = null;

      try {
         var1 = new X500Principal(var0.getDistinguishedName());
      } catch (Exception var3) {
      }

      return var1;
   }

   public static void validatePort(int var0) {
      if (var0 != -1 && (var0 < 1 || var0 > 65535)) {
         throw new IllegalArgumentException("Illegal value for port: " + var0);
      }
   }

   public static void validateX500PrincipalDN(String var0) {
      if (null != var0) {
         if (var0.length() == 0) {
            throw new IllegalArgumentException("Illegal value for distinguished name: " + var0);
         } else {
            try {
               new X500Principal(var0);
            } catch (Exception var2) {
               throw new IllegalArgumentException("Illegal value for distinguished name: " + var0, var2);
            }
         }
      }
   }

   public static void validateURL(String var0) {
      if (null != var0) {
         if (var0.length() == 0) {
            throw new IllegalArgumentException("Illegal value for URL: " + var0);
         } else {
            URI var1;
            try {
               var1 = new URI(var0);
            } catch (Exception var3) {
               throw new IllegalArgumentException("Illegal value for URL: " + var0, var3);
            }

            if (!var1.isAbsolute()) {
               throw new IllegalArgumentException("Illegal value for URL, must be absolute: " + var0);
            } else if (var1.isOpaque()) {
               throw new IllegalArgumentException("Illegal value for URL, must not be opaque: " + var0);
            }
         }
      }
   }

   public static void validateCertSerialNumber(String var0) {
      if (null != var0) {
         if (var0.length() == 0) {
            throw new IllegalArgumentException("Illegal value for serial number: " + var0);
         } else {
            String var1 = var0.replaceAll("[ :]", "");
            if (var1.length() == 0) {
               throw new IllegalArgumentException("Illegal value for serial number: " + var0);
            } else {
               try {
                  new BigInteger(var1, 16);
               } catch (Exception var3) {
                  throw new IllegalArgumentException("Illegal value for serial number: " + var0, var3);
               }
            }
         }
      }
   }
}
