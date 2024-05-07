package weblogic.common.internal;

import weblogic.common.T3Exception;
import weblogic.common.T3Executable;
import weblogic.t3.srvr.ExecutionContext;

public final class LogProxy implements T3Executable {
   public void initialize() {
   }

   public void destroy() {
   }

   public Object execute(ExecutionContext var1, Object var2) throws Exception {
      LogMsg var3 = (LogMsg)var2;
      switch (var3.cmd) {
         case 0:
            var1.getServices().log().info(var3.logmsg);
            return null;
         case 1:
            if (var3.logmsg != null && var3.exception != null) {
               var1.getServices().log().error(var3.logmsg, var3.exception);
            } else if (var3.logmsg != null) {
               var1.getServices().log().error(var3.logmsg);
            } else {
               var1.getServices().log().error(var3.exception);
            }

            return null;
         case 2:
            var1.getServices().log().warning(var3.logmsg);
            return null;
         case 3:
            var1.getServices().log().security(var3.logmsg);
            return null;
         case 4:
            var1.getServices().log().debug(var3.logmsg);
            return null;
         default:
            throw new T3Exception("Unknown LogMsg Command: " + var3.cmd);
      }
   }
}
