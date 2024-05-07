package weblogic.security.utils;

import java.security.Security;
import weblogic.security.SecurityLogger;
import weblogic.security.service.ContextHandler;
import weblogic.security.shared.LoggerWrapper;

public abstract class SecurityUtils {
   private static final boolean DUMP_CTX = Boolean.getBoolean("weblogic.security.DumpContextHandler");

   public static boolean logContextHandlerEnabled() {
      return DUMP_CTX;
   }

   public static void logContextHandler(String var0, LoggerWrapper var1, ContextHandler var2) {
      if (var1 != null) {
         if (var2 == null) {
            if (var1.isDebugEnabled()) {
               var1.debug("ContextHandler for " + var0 + "is null.");
            }

         } else {
            if (var1.isDebugEnabled()) {
               String[] var3 = var2.getNames();
               var1.debug("Logging ContextHandler for " + var0);

               for(int var4 = 0; var4 < var3.length; ++var4) {
                  var1.debug("\t" + var3[var4] + "=" + var2.getValue(var3[var4]).toString());
               }
            }

         }
      }
   }

   private static boolean setCryptoJSecurityProperty(String var0, String var1, String var2) {
      boolean var3 = false;
      String var4 = Security.getProperty(var0);
      if ((var4 == null || var4.trim().length() == 0) && !Boolean.getBoolean(var2)) {
         Security.setProperty(var0, var1);
         var3 = true;
      }

      return var3;
   }

   public static void turnOffCryptoJDefaultJCEVerification() {
      String var0 = "weblogic.security.allowCryptoJDefaultJCEVerification";
      String var1 = "com.rsa.cryptoj.jce.no.verify.jar";
      if (setCryptoJSecurityProperty(var1, "true", var0)) {
         SecurityLogger.logDisallowingCryptoJDefaultJCEVerification(var0);
      }

   }

   public static void changeCryptoJDefaultPRNG() {
      String var0 = "weblogic.security.allowCryptoJDefaultPRNG";
      String var1 = "com.rsa.crypto.default.random";
      String var2 = "ECDRBG";
      String var3 = "FIPS186PRNG";
      if (setCryptoJSecurityProperty(var1, var3, var0)) {
         SecurityLogger.logChangingCryptoJDefaultPRNG(var0, var2, var3);
      }

   }
}
