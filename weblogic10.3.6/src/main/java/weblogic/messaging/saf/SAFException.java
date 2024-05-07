package weblogic.messaging.saf;

public class SAFException extends Exception {
   static final long serialVersionUID = -7167007358028207744L;
   private boolean retry;
   private int resultCode;

   public SAFException(String var1) {
      super(var1);
      this.resultCode = -1;
   }

   public SAFException(String var1, int var2) {
      this(var1);
      this.resultCode = var2;
   }

   public SAFException(String var1, Throwable var2) {
      super(var1, var2);
      this.resultCode = -1;
   }

   public SAFException(String var1, Throwable var2, int var3) {
      this(var1, var2);
      this.resultCode = var3;
   }

   public SAFException(String var1, boolean var2, Throwable var3) {
      super(var1, var3);
      this.resultCode = -1;
      this.retry = var2;
   }

   public SAFException(Throwable var1) {
      super(var1);
      this.resultCode = -1;
   }

   public final int getResultCode() {
      return this.resultCode;
   }

   public final boolean shouldRetry() {
      return this.retry;
   }
}
