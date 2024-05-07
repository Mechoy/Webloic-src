package weblogic.wsee.persistence;

public class StoreRuntimeException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public StoreRuntimeException() {
   }

   public StoreRuntimeException(String var1) {
      super(var1);
   }

   public StoreRuntimeException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public StoreRuntimeException(Throwable var1) {
      super(var1);
   }
}
