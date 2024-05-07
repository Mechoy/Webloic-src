package weblogic.security.shared;

import java.util.HashMap;

public final class LoggerWrapper {
   Object logger = null;
   private static RuntimeUtilities runtimeUtilities = null;
   private static HashMap wrappers = new HashMap();
   private static LoggerWrapper nullWrapper = null;
   private static LoggerAdapter adapter = null;

   private LoggerWrapper() {
   }

   private LoggerWrapper(Object var1) {
      this.logger = var1;
   }

   public static synchronized LoggerWrapper getInstance(Class var0) throws IllegalStateException {
      return var0 != null ? getInstance(var0.getName()) : getInstance((String)null);
   }

   public static synchronized LoggerWrapper getInstance(String var0) throws IllegalStateException {
      if (adapter == null) {
         runtimeUtilities = RuntimeEnvironment.getRuntimeUtilities();
         adapter = runtimeUtilities.getLoggerAdapter();
      }

      if (var0 != null) {
         LoggerWrapper var1 = (LoggerWrapper)wrappers.get(var0);
         if (var1 != null) {
            return var1;
         } else {
            var1 = new LoggerWrapper(adapter.getLogger(var0));
            wrappers.put(var0, var1);
            return var1;
         }
      } else {
         if (nullWrapper == null) {
            nullWrapper = new LoggerWrapper(adapter.getLogger((String)null));
         }

         return nullWrapper;
      }
   }

   public boolean isDebugEnabled() {
      return adapter != null ? adapter.isDebugEnabled(this.logger) : false;
   }

   public void debug(Object var1) {
      if (adapter != null) {
         adapter.debug(this.logger, var1);
      }

   }

   public void debug(Object var1, Throwable var2) {
      if (adapter != null) {
         adapter.debug(this.logger, var1, var2);
      }

   }

   public void info(Object var1) {
      if (adapter != null) {
         adapter.info(this.logger, var1);
      }

   }

   public void info(Object var1, Throwable var2) {
      if (adapter != null) {
         adapter.info(this.logger, var1, var2);
      }

   }

   public void warn(Object var1) {
      if (adapter != null) {
         adapter.warn(this.logger, var1);
      }

   }

   public void warn(Object var1, Throwable var2) {
      if (adapter != null) {
         adapter.warn(this.logger, var1, var2);
      }

   }

   public void error(Object var1) {
      if (adapter != null) {
         adapter.error(this.logger, var1);
      }

   }

   public void error(Object var1, Throwable var2) {
      if (adapter != null) {
         adapter.error(this.logger, var1, var2);
      }

   }

   public void severe(Object var1) {
      if (adapter != null) {
         adapter.severe(this.logger, var1);
      }

   }

   public void severe(Object var1, Throwable var2) {
      if (adapter != null) {
         adapter.severe(this.logger, var1, var2);
      }

   }
}
