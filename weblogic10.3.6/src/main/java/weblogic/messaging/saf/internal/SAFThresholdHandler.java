package weblogic.messaging.saf.internal;

import weblogic.messaging.common.ThresholdHandler;
import weblogic.messaging.kernel.Kernel;
import weblogic.messaging.kernel.Threshold;
import weblogic.messaging.saf.SAFLogger;

public final class SAFThresholdHandler extends ThresholdHandler {
   public SAFThresholdHandler(Kernel var1, String var2) {
      super(var1, var2);
   }

   public synchronized void onThreshold(Threshold var1, boolean var2) {
      if (var2) {
         ++this.count;
         if (var1 == this.bytes) {
            SAFLogger.logBytesThresholdHighAgent(this.targetName);
         } else {
            SAFLogger.logMessagesThresholdHighAgent(this.targetName);
         }
      } else {
         --this.count;
         if (var1 == this.bytes) {
            SAFLogger.logBytesThresholdLowAgent(this.targetName);
         } else {
            SAFLogger.logMessagesThresholdLowAgent(this.targetName);
         }
      }

   }
}
