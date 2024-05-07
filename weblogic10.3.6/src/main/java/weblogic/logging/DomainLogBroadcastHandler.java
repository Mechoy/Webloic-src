package weblogic.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class DomainLogBroadcastHandler extends Handler {
   private DomainLogBroadcasterClient domainLogBroadcasterClient = DomainLogBroadcasterClient.getInstance();

   public void publish(LogRecord var1) {
      WLLogRecord var2 = WLLogger.normalizeLogRecord(var1);
      if (this.isLoggable(var2)) {
         this.domainLogBroadcasterClient.broadcast(var2);
      }
   }

   public void flush() {
      this.domainLogBroadcasterClient.flush();
   }

   public void close() {
      this.domainLogBroadcasterClient.close();
   }
}
