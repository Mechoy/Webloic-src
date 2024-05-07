package weblogic.logging.jms;

import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.utils.time.Timer;

public class JMSMessageLogger extends Logger {
   private long startMillis;
   private long startNano;
   private Timer timer;

   public JMSMessageLogger(String var1) {
      super(var1, (String)null);
      super.setLevel(Level.ALL);
      this.timer = Timer.createTimer();
      this.startMillis = this.timer.isNative() ? System.currentTimeMillis() : 0L;
      this.startNano = this.timer.isNative() ? this.timer.timestamp() : 0L;
   }

   public long getStartMillisTime() {
      return this.startMillis;
   }

   public long getStartNanoTime() {
      return this.startNano;
   }

   public boolean doNano() {
      return this.timer.isNative();
   }
}
