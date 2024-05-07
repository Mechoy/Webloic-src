package weblogic.wtc.wls;

import com.bea.core.jatmi.intf.LogService;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.wtc.WTCLogger;

public final class WlsLogService implements LogService {
   private static int level = -1;
   private DebugLogger CorbaEx = DebugLogger.getDebugLogger("DebugWTCCorbaEx");
   private DebugLogger GwtEx = DebugLogger.getDebugLogger("DebugWTCGwtEx");
   private DebugLogger JatmiEx = DebugLogger.getDebugLogger("DebugWTCJatmiEx");
   private DebugLogger tBridgeEx = DebugLogger.getDebugLogger("DebugWTCtBridgeEx");
   private DebugLogger WtcConfig = DebugLogger.getDebugLogger("DebugWTCConfig");
   private DebugLogger WtcTdomPdu = DebugLogger.getDebugLogger("DebugWTCTdomPdu");
   private DebugLogger WtcUData = DebugLogger.getDebugLogger("DebugWTCUData");
   private boolean _debug = false;

   public void setTraceLevel(int var1) {
      level = var1;
   }

   public void doTrace(String var1) {
      if (this._debug) {
         WTCLogger.logDebugMsg("WlsLogService:" + var1);
      } else {
         WTCLogger.logDebugMsg(var1);
      }

   }

   private boolean typeStatus(int var1) {
      if ((var1 & 4) == 4) {
         if (this.JatmiEx.isDebugEnabled()) {
            return true;
         }

         if (level >= 55000) {
            return true;
         }
      }

      if ((var1 & 2) == 2) {
         if (this.GwtEx.isDebugEnabled()) {
            return true;
         }

         if (level >= 25000) {
            return true;
         }
      }

      if ((var1 & 1) == 1) {
         if (this.tBridgeEx.isDebugEnabled()) {
            return true;
         }

         if (level >= 15000) {
            return true;
         }
      }

      if ((var1 & 8) == 8) {
         if (this.CorbaEx.isDebugEnabled()) {
            return true;
         }

         if (level >= 65000) {
            return true;
         }
      }

      if ((var1 & 16) == 16 && this.WtcConfig.isDebugEnabled()) {
         return true;
      } else if ((var1 & 32) == 32 && this.WtcTdomPdu.isDebugEnabled()) {
         return true;
      } else {
         return (var1 & 64) == 64 && this.WtcUData.isDebugEnabled();
      }
   }

   public void doTrace(int var1, String var2) {
      if (this.typeStatus(var1)) {
         WTCLogger.logDebugMsg(var2);
      }

   }

   public void doTrace(int var1, int var2, String var3) {
      if (this.typeStatus(var2)) {
         WTCLogger.logDebugMsg(var3);
      }

   }

   public boolean isTraceEnabled(int var1) {
      switch (var1) {
         case 1:
            if (this.tBridgeEx.isDebugEnabled()) {
               return true;
            }

            if (level >= 15000) {
               return true;
            }
            break;
         case 2:
            if (this.GwtEx.isDebugEnabled()) {
               return true;
            }

            if (level >= 25000) {
               return true;
            }
            break;
         case 4:
            if (this.JatmiEx.isDebugEnabled()) {
               return true;
            }

            if (level >= 55000) {
               return true;
            }
            break;
         case 8:
            if (this.CorbaEx.isDebugEnabled()) {
               return true;
            }

            if (level >= 65000) {
               return true;
            }
            break;
         case 16:
            if (this.WtcConfig.isDebugEnabled()) {
               return true;
            }
            break;
         case 32:
            if (this.WtcTdomPdu.isDebugEnabled()) {
               return true;
            }
            break;
         case 64:
            if (this.WtcUData.isDebugEnabled()) {
               return true;
            }
      }

      return false;
   }

   public boolean isMixedTraceEnabled(int var1) {
      return this.typeStatus(var1);
   }
}
