package weblogic.messaging.saf.internal;

public final class RetryController {
   private final double multiplier;
   private final long base;
   private final long maximum;
   private long nextDelay;

   public RetryController(long var1, long var3, double var5) {
      this.base = var1;
      this.maximum = var3;
      this.multiplier = var5;
      this.nextDelay = var1;
   }

   public synchronized long getNextRetry() {
      long var1 = this.nextDelay;
      this.nextDelay = (long)((double)this.nextDelay * this.multiplier);
      if (this.nextDelay > this.maximum) {
         this.nextDelay = this.maximum;
      }

      return var1;
   }

   public synchronized void reset() {
      this.nextDelay = this.base;
   }
}
