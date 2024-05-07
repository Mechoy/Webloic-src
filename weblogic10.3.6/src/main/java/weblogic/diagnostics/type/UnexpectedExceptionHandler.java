package weblogic.diagnostics.type;

import weblogic.diagnostics.i18n.DiagnosticsLogger;

public class UnexpectedExceptionHandler {
   private UnexpectedExceptionHandler() {
   }

   public static void handle(String var0, Throwable var1) {
      DiagnosticsLogger.logUnexpectedException(var0, var1);
   }
}
