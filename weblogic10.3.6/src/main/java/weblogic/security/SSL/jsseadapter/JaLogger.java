package weblogic.security.SSL.jsseadapter;

import java.text.MessageFormat;
import java.util.logging.Level;
import weblogic.security.utils.SSLSetupLogging;

final class JaLogger {
   private static final String LOGGER_NAME = JaLogger.class.getPackage().getName();

   static boolean isLoggable(Level var0) {
      return SSLSetupLogging.isDebugEnabled();
   }

   static void log(Level var0, Component var1, String var2, Object... var3) {
      log(var0, var1, (Throwable)null, var2, var3);
   }

   static void log(Level var0, Component var1, Throwable var2, String var3, Object... var4) {
      if (isLoggable(var0)) {
         if (null == var1) {
            var1 = JaLogger.Component.UNKNOWN;
         }

         if (null != var3) {
            var3 = MessageFormat.format(var3, var4);
         } else {
            var3 = "";
         }

         String var6 = MessageFormat.format("[{0}]{1}: {2}: {3}", Thread.currentThread().toString(), LOGGER_NAME, var1, var3);
         if (null != var2) {
            SSLSetupLogging.debug(toSSLSetupLoggingLevel(var0), var2, var6);
         } else {
            SSLSetupLogging.debug(toSSLSetupLoggingLevel(var0), var6);
         }

      }
   }

   private static int toSSLSetupLoggingLevel(Level var0) {
      if (null == var0) {
         throw new IllegalArgumentException("Illegal log level: null");
      } else if (var0 == Level.OFF) {
         throw new IllegalArgumentException("Illegal log level: " + Level.OFF);
      } else if (var0 == Level.SEVERE) {
         return 0;
      } else if (var0 == Level.WARNING) {
         return 2;
      } else if (var0 == Level.INFO) {
         return 3;
      } else if (var0 == Level.CONFIG) {
         return 3;
      } else if (var0 == Level.FINE) {
         return 1;
      } else if (var0 == Level.FINER) {
         return 2;
      } else if (var0 == Level.FINEST) {
         return 3;
      } else if (var0 == Level.ALL) {
         throw new IllegalArgumentException("Illegal log level: " + Level.ALL);
      } else {
         return 2;
      }
   }

   static enum Component {
      KEYSTORE,
      KEYSTORE_MANAGER,
      TRUSTSTORE,
      TRUSTSTORE_MANAGER,
      SSLCONTEXT,
      SSLENGINE,
      SSLSERVERSOCKETFACTORY,
      SSLSERVERSOCKET,
      NIOSSLSERVERSOCKET,
      SSLSOCKETFACTORY,
      SSLSOCKET,
      UNKNOWN;
   }
}
