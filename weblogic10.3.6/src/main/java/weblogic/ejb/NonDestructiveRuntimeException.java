package weblogic.ejb;

public final class NonDestructiveRuntimeException extends RuntimeException {
   private static final long serialVersionUID = 7729973007125942760L;

   public NonDestructiveRuntimeException() {
   }

   public NonDestructiveRuntimeException(String var1) {
      super(var1);
   }

   public NonDestructiveRuntimeException(Throwable var1) {
      super(var1);
   }

   public NonDestructiveRuntimeException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
