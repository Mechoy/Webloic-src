package weblogic.health;

import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

class HealthDebug {
   private static final DebugCategory debug = Debug.getCategory("weblogic.HealthMonitoring");

   static void log(String var0) {
      if (debug.isEnabled()) {
         HealthLogger.logDebugMsg(var0);
      }

   }
}
