package weblogic.diagnostics.harvester;

import weblogic.diagnostics.harvester.internal.MetricArchiver;

public final class HarvesterInternalAccess {
   private static WLDFToHarvester harvester = null;

   public static WLDFToHarvester getInstance() {
      if (harvester == null) {
         harvester = MetricArchiver.getInstance();
         if (harvester == null) {
            logHarvesterNotAvailable((Exception)null);
         }
      }

      return harvester;
   }

   private static void logHarvesterNotAvailable(Exception var0) {
      LogSupport.logUnexpectedException("Problem getting Harvester reference.", var0);
   }
}
