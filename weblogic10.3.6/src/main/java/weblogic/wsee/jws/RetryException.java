package weblogic.wsee.jws;

public class RetryException extends RuntimeException {
   public static final String DEFAULT_DELAY = "5 seconds";
   private String _retryDelay;

   public RetryException(String var1, long var2) {
      super(var1);
      this._retryDelay = var2 + " seconds";
   }

   public RetryException(String var1, String var2) {
      super(var1);
      this._retryDelay = var2;
   }

   public RetryException(String var1) {
      this(var1, "5 seconds");
   }

   public String getRetryDelay() {
      return this._retryDelay;
   }
}
