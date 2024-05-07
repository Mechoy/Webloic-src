package weblogic.security.utils;

import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import weblogic.security.shared.LoggerWrapper;

public class SSLSetupLogging {
   public static final int DEBUG_FATAL = 0;
   public static final int DEBUG_ERROR = 1;
   public static final int DEBUG_WARN = 2;
   public static final int DEBUG_INFO = 3;
   private static LoggerWrapper LOGGER = LoggerWrapper.getInstance("SecuritySSL");
   private static LoggerWrapper EATENLOGGER = LoggerWrapper.getInstance("SecuritySSLEaten");
   private static int debugLevel = 0;

   public static boolean getDebugEaten() {
      return EATENLOGGER.isDebugEnabled();
   }

   public static final boolean isDebugEnabled() {
      return LOGGER.isDebugEnabled();
   }

   public static final boolean isDebugEnabled(int var0) {
      return LOGGER.isDebugEnabled();
   }

   public static final void info(String var0) {
      if (LOGGER.isDebugEnabled()) {
         LOGGER.debug(var0);
      }

   }

   public static final void info(Throwable var0, String var1) {
      if (LOGGER.isDebugEnabled()) {
         LOGGER.debug(var1, var0);
      }

   }

   public static final void debug(int var0, String var1) {
      if (LOGGER.isDebugEnabled()) {
         LOGGER.debug(var1);
      }

   }

   public static final void debug(int var0, boolean var1, String var2) {
      if (LOGGER.isDebugEnabled()) {
         if (var1) {
            Throwable var3 = new Throwable("Stack trace");
            LOGGER.debug(var2, var3);
         } else {
            LOGGER.debug(var2);
         }

      }
   }

   public static final void debug(int var0, Throwable var1, String var2) {
      if (LOGGER.isDebugEnabled()) {
         LOGGER.debug(var2, var1);
      }

   }

   protected static synchronized void debug(String var0, Throwable var1) {
      if (LOGGER.isDebugEnabled()) {
         LOGGER.debug(var0, var1);
      }

   }

   public static boolean logSSLRejections() {
      return true;
   }

   public static void debugPrivateKey(PrivateKey var0) {
      if (isDebugEnabled()) {
         String var1 = "Private key dump\n   Key info: " + var0;
         if (var0 instanceof RSAPrivateCrtKey) {
            var1 = var1 + "   is a java.security.interfaces.RSAPrivateCrtKey";
         } else if (var0 instanceof RSAPrivateKey) {
            var1 = var1 + "   is a java.security.interfaces.RSAPrivateKey";
         }

         debug(3, var1);
      }

   }
}
