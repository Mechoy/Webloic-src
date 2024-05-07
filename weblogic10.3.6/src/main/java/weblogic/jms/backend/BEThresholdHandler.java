package weblogic.jms.backend;

import weblogic.jms.JMSLogger;
import weblogic.messaging.common.ThresholdHandler;
import weblogic.messaging.kernel.Destination;
import weblogic.messaging.kernel.Kernel;
import weblogic.messaging.kernel.Threshold;

final class BEThresholdHandler extends ThresholdHandler {
   private final boolean isDestination;
   private String destName;

   BEThresholdHandler(String var1, String var2) {
      super(var1);
      this.isDestination = true;
      this.destName = var2;
   }

   BEThresholdHandler(Kernel var1, String var2) {
      super(var1, var2);
      this.isDestination = false;
   }

   void setTarget(Destination var1) {
      this.statistics = var1.getStatistics();
      this.replaceBytesThreshold();
      this.replaceMessagesThreshold();
   }

   public synchronized void onThreshold(Threshold var1, boolean var2) {
      if (var2) {
         ++this.count;
         if (var1 == this.bytes) {
            if (this.isDestination) {
               JMSLogger.logBytesThresholdHighDestination(this.targetName, this.destName);
            } else {
               JMSLogger.logBytesThresholdHighServer(this.targetName);
            }
         } else if (this.isDestination) {
            JMSLogger.logMessagesThresholdHighDestination(this.targetName, this.destName);
         } else {
            JMSLogger.logMessagesThresholdHighServer(this.targetName);
         }
      } else {
         --this.count;
         if (var1 == this.bytes) {
            if (this.isDestination) {
               JMSLogger.logBytesThresholdLowDestination(this.targetName, this.destName);
            } else {
               JMSLogger.logBytesThresholdLowServer(this.targetName);
            }
         } else if (this.isDestination) {
            JMSLogger.logMessagesThresholdLowDestination(this.targetName, this.destName);
         } else {
            JMSLogger.logMessagesThresholdLowServer(this.targetName);
         }
      }

   }
}
