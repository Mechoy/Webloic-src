package weblogic.wsee.jws.conversation;

public class StoreException extends Exception {
   public StoreException(String var1) {
      super(var1);
   }

   public StoreException(Throwable var1) {
      this(var1.getMessage());
      this.initCause(var1);
   }

   public StoreException(String var1, Exception var2) {
      this(var1);
      this.initCause(var2);
   }

   public StoreException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
