package weblogic.wsee.reliability2.tube;

public class DispatchFactoryNotReadyException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public DispatchFactoryNotReadyException(String var1) {
      super(var1);
   }

   public DispatchFactoryNotReadyException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
