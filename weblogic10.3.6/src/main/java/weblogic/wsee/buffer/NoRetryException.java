package weblogic.wsee.buffer;

public class NoRetryException extends RuntimeException {
   public NoRetryException(Throwable var1) {
      super(var1);
   }

   public NoRetryException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
