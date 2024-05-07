package weblogic.servlet.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.KernelMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManagerFactory;

public class CompleteMessageTimeoutTrigger implements TimerListener {
   private static final int CLEANUP_TRIGGER_TIMEPERIOD_LOW = 2000;
   private static final int CLEANUP_TRIGGER_TIMEPERIOD_HIGH = 30000;
   private ConcurrentHashMap sockets = new ConcurrentHashMap(1024);
   private long writeTimeoutInterval;
   private long triggerInterval;

   public CompleteMessageTimeoutTrigger() {
      KernelMBean var1 = Kernel.getConfig();
      this.writeTimeoutInterval = (long)(1000 * (var1.getCompleteHTTPMessageTimeout() == -1 ? var1.getCompleteMessageTimeout() : var1.getCompleteHTTPMessageTimeout()));
      this.triggerInterval = this.writeTimeoutInterval / 10L;
      if (this.triggerInterval < 2000L) {
         this.triggerInterval = 2000L;
      } else if (this.triggerInterval > 30000L) {
         this.triggerInterval = 30000L;
      }

      if (HTTPDebugLogger.isEnabled()) {
         HTTPLogger.logDebug("Setting the writetimeout interval to " + this.writeTimeoutInterval);
      }

      TimerManager var2 = TimerManagerFactory.getTimerManagerFactory().getTimerManager(CompleteMessageTimeoutTrigger.class.getName(), WorkManagerFactory.getInstance().getSystem());
      var2.schedule(this, 0L, this.triggerInterval);
   }

   void register(OutputStream var1) {
      this.sockets.put(var1, new Long(System.currentTimeMillis()));
   }

   void unregister(OutputStream var1) {
      this.sockets.remove(var1);
   }

   public void timerExpired(Timer var1) {
      long var2 = System.currentTimeMillis();
      Iterator var4 = this.sockets.keySet().iterator();

      while(var4.hasNext()) {
         OutputStream var5 = (OutputStream)var4.next();
         Long var6 = (Long)this.sockets.get(var5);
         if (var6 != null && var2 - var6 > this.writeTimeoutInterval && this.sockets.remove(var5, var6)) {
            try {
               var5.close();
            } catch (IOException var8) {
            }

            HTTPLogger.logClosingTimeoutSocket();
         }
      }

   }
}
