package weblogic.diagnostics.debug;

import weblogic.diagnostics.context.DiagnosticContext;
import weblogic.diagnostics.context.DiagnosticContextFactory;

final class DebugContextImpl implements DebugContext {
   private static final long UNAVAILABLE = 0L;

   public long getDyeVector() {
      DiagnosticContext var1 = DiagnosticContextFactory.getDiagnosticContext();
      return var1 != null ? var1.getDyeVector() : 0L;
   }
}
