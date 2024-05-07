package weblogic.security.utils;

import javax.net.ssl.SSLSession;

public class SSLWLSWildcardHostnameVerifier extends SSLWLSHostnameVerifier.DefaultHostnameVerifier {
   public SSLWLSWildcardHostnameVerifier() {
      if (SSLSetup.isDebugEnabled(3)) {
         SSLSetup.info("HostnameVerifier: allowing wildcarded certificates");
      }

   }

   public boolean verify(String var1, SSLSession var2) {
      if (var1 != null && var2 != null) {
         String var3 = SSLCertUtility.getCommonName(var2);
         if (var3 != null && var3.length() != 0) {
            if (isLegalWildcarded(var1, var3)) {
               return true;
            } else {
               return super.verify(var1, var2);
            }
         } else {
            return super.verify(var1, var2);
         }
      } else {
         return false;
      }
   }

   private static boolean isLegalWildcarded(String var0, String var1) {
      if (var1.indexOf("*") == -1) {
         if (SSLSetup.isDebugEnabled(3)) {
            SSLSetup.info("HostnameVerifier: no wildcard present, wildcard validation not performed.");
         }

         return false;
      } else {
         return var1.indexOf(".") != var1.lastIndexOf(".") && var1.startsWith("*.") && var1.indexOf("*") == var1.lastIndexOf("*") && domainMatchesDomain(var1, var0);
      }
   }

   private static boolean domainMatchesDomain(String var0, String var1) {
      int var2 = var0.indexOf("*");
      if (var2 == -1) {
         return false;
      } else {
         String var3 = var0.substring(var2 + 1).toLowerCase();
         String var4 = var1.toLowerCase();
         if (!var4.endsWith(var3)) {
            return false;
         } else if (var4.lastIndexOf(var3) == -1) {
            return false;
         } else {
            String var5 = var4.substring(0, var4.length() - var3.length());
            if (var5.length() <= 0) {
               return false;
            } else {
               return var5.indexOf(".") == -1;
            }
         }
      }
   }
}
