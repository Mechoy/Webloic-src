package weblogic.diagnostics.type;

public class DiagnosticRuntimeException extends RuntimeException {
   public DiagnosticRuntimeException() {
   }

   public DiagnosticRuntimeException(String var1) {
      super(var1);
   }

   public DiagnosticRuntimeException(Throwable var1) {
      super(var1);
   }

   public DiagnosticRuntimeException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
