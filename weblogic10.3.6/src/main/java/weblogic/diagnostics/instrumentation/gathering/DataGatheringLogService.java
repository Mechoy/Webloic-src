package weblogic.diagnostics.instrumentation.gathering;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Handler;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.logging.JDKLoggerFactory;
import weblogic.logging.LoggingHelper;

final class DataGatheringLogService {
   private static DebugLogger debugLog = DebugLogger.getDebugLogger("DebugDiagnosticDataGathering");
   private static Object logHandler = null;

   static void registerToServerLogger(int var0) {
      int var1 = Math.max(var0, 64);
      if (JDKLoggerFactory.isLog4jEnabled()) {
         try {
            if (debugLog.isDebugEnabled()) {
               debugLog.debug("DataGatheringLogService.registerToServerLogger(" + var0 + ") registering log4j appender with severity " + var1);
            }

            Class var2 = Class.forName("weblogic.logging.log4j.Log4jLoggingHelper", true, Thread.currentThread().getContextClassLoader());
            Method var3 = var2.getMethod("getLog4jServerLogger");
            Object var4 = var3.invoke((Object)null);
            Class var5 = Class.forName("org.apache.log4j.Logger", true, Thread.currentThread().getContextClassLoader());
            Class var6 = Class.forName("weblogic.diagnostics.instrumentation.gathering.DataGatheringAppender", true, Thread.currentThread().getContextClassLoader());
            Constructor var7 = var6.getConstructor(var5, Integer.TYPE);
            Object var8 = var7.newInstance(var4, new Integer(var1));
            Method var9 = var5.getMethod("addAppender", Class.forName("org.apache.log4j.Appender", true, Thread.currentThread().getContextClassLoader()));
            var9.invoke(var4, var8);
            logHandler = var8;
            return;
         } catch (Exception var10) {
            DiagnosticsLogger.logErrorRegisteringLog4jDataGatheringAppender(var10);
         }
      } else {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("DataGatheringLogService.registerToServerLogger(" + var0 + ") registering log handler with severity " + var1);
         }

         logHandler = new DataGatheringHandler(var1);
         LoggingHelper.getServerLogger().addHandler((Handler)logHandler);
      }

   }

   static void deregisterFromServerLogger() {
      if (debugLog.isDebugEnabled()) {
         debugLog.debug("DataGatheringLogService.deregisterFromServerLogger()");
      }

      if (logHandler != null) {
         if (JDKLoggerFactory.isLog4jEnabled()) {
            try {
               Class var0 = Class.forName("weblogic.logging.log4j.Log4jLoggingHelper", true, Thread.currentThread().getContextClassLoader());
               Method var1 = var0.getMethod("getLog4jServerLogger");
               Object var2 = var1.invoke((Object)null);
               Class var3 = Class.forName("org.apache.log4j.Logger", true, Thread.currentThread().getContextClassLoader());
               Method var4 = var3.getMethod("removeAppender", Class.forName("org.apache.log4j.Appender", true, Thread.currentThread().getContextClassLoader()));
               var4.invoke(var2, logHandler);
            } catch (Exception var5) {
               DiagnosticsLogger.logErrorRegisteringLog4jDataGatheringAppender(var5);
            }
         } else {
            LoggingHelper.getServerLogger().removeHandler((Handler)logHandler);
         }

      }
   }
}
