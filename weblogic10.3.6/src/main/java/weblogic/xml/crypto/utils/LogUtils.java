package weblogic.xml.crypto.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.logging.LoggingHelper;

public class LogUtils {
   public static final String DSIG_VERBOSE_PROPERTY = "weblogic.xml.crypto.dsig.verbose";
   public static final boolean DSIG_VERBOSE = Boolean.getBoolean("weblogic.xml.crypto.dsig.verbose");
   public static final String ENCRYPT_VERBOSE_PROPERTY = "weblogic.xml.crypto.encrypt.verbose";
   public static final boolean ENCRYPT_VERBOSE = Boolean.getBoolean("weblogic.xml.crypto.encrypt.verbose");
   public static final String WSS_VERBOSE_PROPERTY = "weblogic.xml.crypto.wss.verbose";
   public static final boolean WSS_VERBOSE = Boolean.getBoolean("weblogic.xml.crypto.wss.verbose");
   public static final String WSS_DEBUG_PROPERTY = "weblogic.xml.crypto.wss.debug";
   public static final boolean WSS_DEBUG = Boolean.getBoolean("weblogic.xml.crypto.wss.debug");
   public static final String KEYINFO_VERBOSE_PROPERTY = "weblogic.xml.crypto.keyinfo.verbose";
   public static final boolean KEYINFO_VERBOSE = Boolean.getBoolean("weblogic.xml.crypto.keyinfo.verbose");
   private static Logger logger = null;
   private static final Level logLevel;
   public static final String INCLUDE_METHOD_INFO_PROPERTY = "weblogic.xml.crypto.utils.LogUtils.INCLUDE_METHOD_INFO";
   public static final boolean INCLUDE_METHOD_INFO;

   public static void logDsig(String var0) {
      if (DSIG_VERBOSE) {
         log(var0);
      }

   }

   public static void logDsig(Object var0) {
      if (DSIG_VERBOSE) {
         log(var0.toString());
      }

   }

   public static void logDsig(String var0, Object var1) {
      if (DSIG_VERBOSE) {
         if (var0 == null) {
            log(var1.toString());
         } else {
            log(var0 + var1);
         }
      }

   }

   public static void logDsig(LogMethod var0) {
      if (DSIG_VERBOSE) {
         log(var0.log());
      }

   }

   public static void logEncrypt(String var0) {
      if (ENCRYPT_VERBOSE) {
         log(var0);
      }

   }

   public static void logEncrypt(LogMethod var0) {
      if (ENCRYPT_VERBOSE) {
         log(var0.log());
      }

   }

   public static void logWss(String var0) {
      if (WSS_VERBOSE) {
         log(var0);
      }

   }

   public static void logWss(String var0, Object var1) {
      if (WSS_VERBOSE) {
         log(var0 + var1);
      }

   }

   public static void debugWss(String var0) {
      if (WSS_DEBUG) {
         log(var0);
      }

   }

   public static void logKeyInfo(String var0) {
      if (KEYINFO_VERBOSE) {
         log(var0);
      }

   }

   private static void log(String var0) {
      if (INCLUDE_METHOD_INFO) {
         StackTraceElement[] var1 = (new Exception()).getStackTrace();
         StringBuilder var2 = new StringBuilder(var0);
         var2.append("<").append(shortName(var1[2].getClassName()));
         var2.append(".");
         var2.append(var1[2].getMethodName());
         int var3 = var1[2].getLineNumber();
         if (var3 > -1) {
            var2.append(":");
            var2.append(var3);
         }

         var2.append(">");
         var0 = var2.toString();
      }

      logger.log(logLevel, var0);
   }

   private static String shortName(String var0) {
      int var1 = var0.lastIndexOf(46);
      if (var1 != -1) {
         var0 = var0.substring(var1 + 1, var0.length());
      }

      return var0;
   }

   static {
      logLevel = Level.INFO;
      INCLUDE_METHOD_INFO = Boolean.getBoolean("weblogic.xml.crypto.utils.LogUtils.INCLUDE_METHOD_INFO");
      logger = LoggingHelper.getServerLogger();
      if (logger == null) {
         logger = LoggingHelper.getClientLogger();
      }

   }

   public interface LogMethod {
      String log();
   }
}
