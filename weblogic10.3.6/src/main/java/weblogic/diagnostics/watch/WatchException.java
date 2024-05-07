package weblogic.diagnostics.watch;

import weblogic.diagnostics.type.DiagnosticException;

public class WatchException extends DiagnosticException {
   public WatchException() {
   }

   public WatchException(String var1) {
      super(var1);
   }

   public WatchException(Throwable var1) {
      super(var1);
   }

   public WatchException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
