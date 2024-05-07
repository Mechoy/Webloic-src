package weblogic.auddi.util;

public class IntervalTimer extends Thread {
   private long interval;
   private TimerListener timerListener;
   private boolean active;

   public IntervalTimer(TimerListener var1, long var2) {
      Logger.debug("Thread created that will call back " + var1 + " every " + var2 / 1000L + " seconds");
      this.timerListener = var1;
      this.interval = var2;
      this.active = true;
      this.setDaemon(true);
   }

   public void run() {
      while(this.active) {
         this.timerListener.onTimer();

         try {
            sleep(this.interval);
         } catch (InterruptedException var2) {
            Logger.debug("Interruped exception in IntervalTimer thread " + this);
         }
      }

   }

   public void stopThread() {
      this.active = false;
   }

   public long getInterval() {
      return this.interval;
   }

   public void setInterval(long var1) {
      this.interval = var1;
   }

   public String toString() {
      return "IntervalTimer { alive = " + this.isAlive() + ", " + "interval = " + this.interval + " }";
   }
}
