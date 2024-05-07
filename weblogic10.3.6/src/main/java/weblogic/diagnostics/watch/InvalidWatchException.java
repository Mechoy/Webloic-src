package weblogic.diagnostics.watch;

public class InvalidWatchException extends IllegalArgumentException {
   public InvalidWatchException() {
   }

   public InvalidWatchException(String var1) {
      super(var1);
   }

   public InvalidWatchException(Throwable var1) {
      this.initCause(var1);
   }

   public InvalidWatchException(String var1, Throwable var2) {
      super(var1);
      this.initCause(var2);
   }
}
