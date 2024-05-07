package weblogic.cluster.singleton;

public class LeasingException extends Exception {
   private String message;

   public LeasingException(String var1) {
      super(var1);
      this.message = var1;
   }

   public LeasingException(String var1, Throwable var2) {
      super(var1, var2);
      this.message = var1;
   }

   public String toString() {
      return this.message;
   }
}
