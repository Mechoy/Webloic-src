package weblogic.wsee.jaxws.framework;

import java.io.Serializable;

public final class RetryDelayCalculator implements Serializable {
   private static final long serialVersionUID = 1L;
   private final double multiplier;
   private final long base;
   private final long maximum;
   private long currentDelay;

   public RetryDelayCalculator(long var1, long var3, double var5) {
      this.base = var1;
      this.maximum = var3;
      this.multiplier = var5;
      this.currentDelay = var1;
   }

   public synchronized long getNextRetryDelayMillis() {
      long var1 = this.currentDelay;
      this.currentDelay = (long)((double)this.currentDelay * this.multiplier);
      if (this.currentDelay > this.maximum) {
         this.currentDelay = this.maximum;
      }

      return var1;
   }

   public synchronized long getCurrentRetryDelayMillis() {
      return this.currentDelay;
   }

   public synchronized void reset() {
      this.currentDelay = this.base;
   }
}
