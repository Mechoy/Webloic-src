package weblogic.logging;

import com.bea.logging.MsgIdPrefixConverter;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.i18n.Localizer;
import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.LogMessage;
import weblogic.i18ntools.L10nLookup;

public final class MessageLogger {
   private static final boolean USE_PREFIX = true;
   private static final String PREFIX_PROP = "weblogic.MessageIdPrefixEnabled";
   private static boolean usePrefix = true;
   private static Method getLoggerMethod;

   public static void log(String var0, Object[] var1, String var2) {
      log(new CatalogMessage(var0, var1, var2));
   }

   public static void log(CatalogMessage var0) {
      Object[] var1 = var0.getArguments();
      WLLogRecord var2 = new WLLogRecord(Level.OFF, (String)null);
      var2.setParameters(var1);

      try {
         String var3 = var0.getMessageId();
         if (usePrefix) {
            try {
               String var4 = var0.getMessageIdPrefix();
               if (var4 != null) {
                  var3 = MsgIdPrefixConverter.convertMsgIdPrefix(var4) + "-" + var0.getMessageId();
               }
            } catch (NumberFormatException var6) {
            }
         }

         var2.setId(var3);
         Level var9 = WLLevel.getLevel(var0.getSeverity());
         var2.setLevel(var9);
         var2.setLoggerName(var0.getSubsystem());
         var2.setMessage(var0.getMessage());
         var2.setParameters(var1);
         var2.setDiagnosticVolume(var0.getDiagnosticVolume());
      } catch (MissingResourceException var7) {
         var2.setLoggerName("Unknown");
         var2.setMessage("Message text not found - " + var7.getMessage());
      }

      if (var0.isStackTraceEnabled()) {
         var2.setThrown(getThrowable(var1));
      }

      try {
         Logger var8 = (Logger)getLoggerMethod.invoke((Object)null, (Object[])null);
         var8.log(var2);
      } catch (Exception var5) {
         throw new AssertionError(var5);
      }
   }

   public static void log(LogMessage var0) {
      if (var0 instanceof CatalogMessage) {
         CatalogMessage var6 = (CatalogMessage)var0;
         log(var6);
      } else {
         WLLogRecord var1 = new WLLogRecord(Level.OFF, (String)null);
         String var2 = var0.getMessageId();
         if (var2 != null && var2.length() > 0) {
            String var3 = usePrefix ? var0.getMessageIdPrefix() : "";
            if (var3 != null && var3.length() > 0) {
               var2 = MsgIdPrefixConverter.convertMsgIdPrefix(var3) + "-" + var0.getMessageId();
            }
         }

         var1.setId(var2);
         Level var7 = WLLevel.getLevel(var0.getSeverity());
         var1.setLevel(var7);
         var1.setLoggerName(var0.getSubsystem());
         var1.setMessage(var0.getMessage());
         var1.setThrown(var0.getThrowable());

         try {
            Logger var4 = (Logger)getLoggerMethod.invoke((Object)null, (Object[])null);
            var4.log(var1);
         } catch (Exception var5) {
            throw new AssertionError(var5);
         }
      }
   }

   private static Throwable getThrowable(Object[] var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length - 1;
         if (var1 >= 0) {
            Object var2 = var0[var1];
            if (var2 instanceof Throwable) {
               return (Throwable)var2;
            }
         }

         return null;
      }
   }

   public static void log(Level var0, String var1, String var2) {
      log(var0, var1, var2, (Throwable)null);
   }

   public static void log(Level var0, String var1, String var2, Throwable var3) {
      WLLogRecord var4 = new WLLogRecord(var0, var2, var3);
      var4.setLoggerName(var1);

      try {
         Logger var5 = (Logger)getLoggerMethod.invoke((Object)null, (Object[])null);
         var5.log(var4);
      } catch (Exception var6) {
         throw new AssertionError(var6);
      }
   }

   public static void setUsePrefix(boolean var0) {
      usePrefix = var0;
   }

   public static String localizeMessage(String var0, Object[] var1, String var2) {
      try {
         Localizer var3 = L10nLookup.getLocalizer(Locale.getDefault(), var2);
         String var4 = var3.getBody(var0);
         return MessageFormat.format(var4, var1);
      } catch (MissingResourceException var5) {
         return "Error message text was not found: " + var5.getMessage();
      }
   }

   static {
      try {
         String var0 = System.getProperty("weblogic.MessageIdPrefixEnabled");
         if (var0 != null) {
            setUsePrefix(Boolean.getBoolean("weblogic.MessageIdPrefixEnabled"));
         }
      } catch (Exception var2) {
      }

      try {
         Class var3 = Class.forName("weblogic.kernel.KernelLogManager");
         getLoggerMethod = var3.getMethod("getLogger", (Class[])null);
      } catch (Exception var1) {
         throw new AssertionError(var1);
      }
   }
}
