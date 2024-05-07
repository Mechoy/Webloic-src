package weblogic.common.internal;

import java.util.logging.Level;
import weblogic.common.LogServicesDef;
import weblogic.logging.MessageLogger;
import weblogic.logging.WLLevel;
import weblogic.utils.PlatformConstants;

/** @deprecated */
public final class LogOutputStream extends weblogic.logging.LogOutputStream implements LogServicesDef {
   public LogOutputStream(String var1) {
      super(var1);
   }

   public void log(String var1) {
      this.info(var1);
   }

   public void log(String var1, Throwable var2) {
      this.error(var1, var2);
   }

   public void error(Throwable var1) {
      this.error("", var1);
   }

   public void error(String var1, String var2) {
      this.error(var1 + PlatformConstants.EOL + var2);
   }

   public void security(String var1) {
      MessageLogger.log((Level)WLLevel.INFO, (String)"Security", var1);
   }
}
