package weblogic.transaction.internal;

import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;

public class JTAMTCustomValidator {
   public static void validateUserPreferredServer(JTAMigratableTargetMBean var0, ServerMBean var1) {
      ServerMBean var2 = (ServerMBean)var0.getParent();
      if (var1 != null && !var2.equals(var1)) {
         throw new IllegalArgumentException(TXExceptionLogger.logInvalidUserPreferredServerLoggable(var2.getName(), var1.getName()).getMessage());
      }
   }
}
