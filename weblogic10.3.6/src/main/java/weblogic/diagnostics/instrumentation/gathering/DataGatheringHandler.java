package weblogic.diagnostics.instrumentation.gathering;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import weblogic.logging.WLErrorManager;
import weblogic.logging.WLLevel;

public class DataGatheringHandler extends Handler {
   private boolean closed = false;

   public DataGatheringHandler(int var1) {
      this.setErrorManager(new WLErrorManager(this));
      this.setLevel(WLLevel.getLevel(var1));
   }

   public void publish(LogRecord var1) {
      if (this.isLoggable(var1)) {
         if (!this.closed) {
            DataGatheringManager.gatherLogRecord(var1);
         }

      }
   }

   public void flush() {
   }

   public void close() {
      this.closed = true;
   }
}
