package weblogic.diagnostics.watch;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Handler;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.logging.JDKLoggerFactory;
import weblogic.logging.LoggingHelper;

final class WatchLogService {
   private static Object logHandler = null;

   static void registerToServerLogger(WatchManager var0, int var1) {
      if (JDKLoggerFactory.isLog4jEnabled()) {
         try {
            Class var2 = Class.forName("weblogic.logging.log4j.Log4jLoggingHelper", true, Thread.currentThread().getContextClassLoader());
            Method var3 = var2.getMethod("getLog4jServerLogger");
            Object var4 = var3.invoke((Object)null);
            Class var5 = Class.forName("org.apache.log4j.Logger", true, Thread.currentThread().getContextClassLoader());
            Class var6 = Class.forName("weblogic.diagnostics.watch.WatchLogAppender", true, Thread.currentThread().getContextClassLoader());
            Constructor var7 = var6.getConstructor(var5, WatchManager.class, Integer.TYPE);
            Object var8 = var7.newInstance(var4, var0, new Integer(var1));
            Method var9 = var5.getMethod("addAppender", Class.forName("org.apache.log4j.Appender", true, Thread.currentThread().getContextClassLoader()));
            var9.invoke(var4, var8);
            logHandler = var8;
            return;
         } catch (Exception var10) {
            DiagnosticsLogger.logWatchErrorInvokingLog4j(var10);
         }
      } else {
         logHandler = new WatchLogHandler(var0, var1);
         LoggingHelper.getServerLogger().addHandler((Handler)logHandler);
      }

   }

   static void deregisterFromServerLogger() {
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
               DiagnosticsLogger.logWatchErrorInvokingLog4j(var5);
            }
         } else {
            LoggingHelper.getServerLogger().removeHandler((Handler)logHandler);
         }

      }
   }
}
