package weblogic.management;

import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.ConfigLogger;
import weblogic.management.internal.Utils;
import weblogic.management.provider.UpdateException;

public class SpecialPropertiesProcessor {
   public static void updateConfiguration(DomainMBean var0) throws UpdateException {
      String var1 = Utils.findServerName(var0);
      if (var1 == null) {
         throw new UpdateException(ConfigLogger.logCouldNotDetermineServerNameLoggable().getMessage());
      } else {
         ServerMBean var2 = var0.lookupServer(var1);
         if (var2 == null) {
            throw new UpdateException(ConfigLogger.logServerNameNotFoundLoggable(var1, var0.getName()).getMessage());
         } else {
            boolean var3 = var2.getCluster() != null;
            if (var3) {
               SpecialPropertiesHelper.configureFromSystemProperties(var2, var3, false);
            } else {
               SpecialPropertiesHelper.configureFromSystemProperties(var2);
            }

         }
      }
   }
}
