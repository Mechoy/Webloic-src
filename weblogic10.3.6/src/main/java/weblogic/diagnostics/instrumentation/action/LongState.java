package weblogic.diagnostics.instrumentation.action;

import weblogic.diagnostics.instrumentation.DiagnosticActionState;

public final class LongState implements DiagnosticActionState {
   private long value;

   public void setValue(long var1) {
      this.value = var1;
   }

   public long getValue() {
      return this.value;
   }
}
