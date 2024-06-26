package weblogic.diagnostics.instrumentation.gathering;

import com.bea.logging.LogLevel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import weblogic.diagnostics.flightrecorder.event.WLLogRecordEvent;

public class WLLog4jLogEventClassHelper {
   private static WLLog4jLogEventClassHelper SINGLETON;
   private Class wlLog4jLogEventClass = null;
   private boolean available = false;
   private boolean availabilityCheckDone = false;
   private Method isGatherable = null;
   private Method getDiagnosticVolume = null;
   private Method getLogMessage = null;
   private Method getId = null;
   private Method getLoggerName = null;
   private Method getSeverity = null;
   private Method getUserId = null;
   private Method getTransactionId = null;
   private Method getServerName = null;
   private Method getDiagnosticContextId = null;
   private Method getMachineName = null;

   public static WLLog4jLogEventClassHelper getInstance() {
      if (SINGLETON != null) {
         return SINGLETON;
      } else {
         Class var0 = WLLog4jLogEventClassHelper.class;
         synchronized(WLLog4jLogEventClassHelper.class) {
            if (SINGLETON == null) {
               SINGLETON = new WLLog4jLogEventClassHelper();
            }
         }

         return SINGLETON;
      }
   }

   private WLLog4jLogEventClassHelper() {
   }

   public boolean isAvailable(Object var1) {
      if (this.availabilityCheckDone) {
         return this.available;
      } else {
         synchronized(this) {
            if (this.availabilityCheckDone) {
               return this.available;
            }

            if (var1 != null) {
               try {
                  this.wlLog4jLogEventClass = Class.forName("weblogic.logging.log4j.WLLog4jLogEvent", true, var1.getClass().getClassLoader());
                  this.isGatherable = this.wlLog4jLogEventClass.getDeclaredMethod("isGatherable", (Class[])null);
                  this.getDiagnosticVolume = this.wlLog4jLogEventClass.getDeclaredMethod("getDiagnosticVolume", (Class[])null);
                  this.getLogMessage = this.wlLog4jLogEventClass.getDeclaredMethod("getLogMessage", (Class[])null);
                  this.getId = this.wlLog4jLogEventClass.getDeclaredMethod("getId", (Class[])null);
                  this.getSeverity = this.wlLog4jLogEventClass.getDeclaredMethod("getSeverity", (Class[])null);
                  this.getUserId = this.wlLog4jLogEventClass.getDeclaredMethod("getUserId", (Class[])null);
                  this.getTransactionId = this.wlLog4jLogEventClass.getDeclaredMethod("getTransactionId", (Class[])null);
                  this.getServerName = this.wlLog4jLogEventClass.getDeclaredMethod("getServerName", (Class[])null);
                  this.getDiagnosticContextId = this.wlLog4jLogEventClass.getDeclaredMethod("getDiagnosticContextId", (Class[])null);
                  this.getMachineName = this.wlLog4jLogEventClass.getDeclaredMethod("getMachineName", (Class[])null);
                  this.getLoggerName = this.wlLog4jLogEventClass.getMethod("getLoggerName", (Class[])null);
                  this.available = true;
               } catch (Exception var5) {
                  this.available = false;
               } catch (NoSuchMethodError var6) {
                  this.available = false;
               }
            }

            this.availabilityCheckDone = true;
         }

         return this.available;
      }
   }

   public boolean isInstance(Class var1) {
      if (var1 != null && this.wlLog4jLogEventClass != null) {
         return var1 == this.wlLog4jLogEventClass ? true : var1.isInstance(this.wlLog4jLogEventClass);
      } else {
         return false;
      }
   }

   public boolean isGatherable(Object var1) {
      if (this.available && var1 != null) {
         try {
            return (Boolean)this.isGatherable.invoke(var1, (Object[])null);
         } catch (Exception var3) {
            return false;
         }
      } else {
         return false;
      }
   }

   public String getDiagnosticVolume(Object var1) {
      if (this.available && var1 != null) {
         try {
            return (String)this.getDiagnosticVolume.invoke(var1, (Object[])null);
         } catch (Exception var3) {
            return "Off";
         }
      } else {
         return "Off";
      }
   }

   public WLLogRecordEvent populateWLLogRecordEvent(Object var1) {
      WLLogRecordEvent var2 = new WLLogRecordEvent();

      try {
         var2.message = (String)this.getLogMessage.invoke(var1, (Object[])null);
         var2.level = LogLevel.getLevel((Integer)((Integer)this.getSeverity.invoke(var1, (Object[])null))).toString();
         var2.id = (String)this.getId.invoke(var1, (Object[])null);
         var2.loggerName = (String)this.getLoggerName.invoke(var1, (Object[])null);
         var2.userId = (String)this.getUserId.invoke(var1, (Object[])null);
         var2.transactionId = (String)this.getTransactionId.invoke(var1, (Object[])null);
         var2.ECID = (String)this.getDiagnosticContextId.invoke(var1, (Object[])null);
      } catch (IllegalArgumentException var4) {
      } catch (IllegalAccessException var5) {
      } catch (InvocationTargetException var6) {
      }

      return var2;
   }
}
