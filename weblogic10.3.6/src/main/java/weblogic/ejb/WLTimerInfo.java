package weblogic.ejb;

public final class WLTimerInfo {
   public static final int REMOVE_TIMER_ACTION = 1;
   public static final int DISABLE_TIMER_ACTION = 2;
   public static final int SKIP_TIMEOUT_ACTION = 3;
   private int maxRetryAttempts = -1;
   private int maxTimeouts = 0;
   private long retryDelay = 0L;
   private int timeoutFailureAction = 2;

   public void setMaxRetryAttempts(int var1) {
      if (var1 < -1) {
         throw new IllegalArgumentException("" + var1);
      } else {
         this.maxRetryAttempts = var1;
      }
   }

   public int getMaxRetryAttempts() {
      return this.maxRetryAttempts;
   }

   public void setRetryDelay(long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("" + var1);
      } else {
         this.retryDelay = var1;
      }
   }

   public long getRetryDelay() {
      return this.retryDelay;
   }

   public void setMaxTimeouts(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("" + var1);
      } else {
         this.maxTimeouts = var1;
      }
   }

   public int getMaxTimeouts() {
      return this.maxTimeouts;
   }

   public void setTimeoutFailureAction(int var1) {
      if (var1 != 1 && var1 != 2 && var1 != 3) {
         throw new IllegalArgumentException("" + var1);
      } else {
         this.timeoutFailureAction = var1;
      }
   }

   public int getTimeoutFailureAction() {
      return this.timeoutFailureAction;
   }
}
