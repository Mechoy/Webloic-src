package weblogic.diagnostics.instrumentation;

import weblogic.diagnostics.context.DiagnosticContext;
import weblogic.diagnostics.context.DiagnosticContextFactory;

public abstract class AbstractDiagnosticAction implements DiagnosticAction {
   static final long serialVersionUID = 4064514881509087899L;
   private static final boolean noEventGen = Boolean.getBoolean("weblogic.diagnostics.internal.noEventGen");
   private DiagnosticMonitor monitor;
   private String type;

   public final String getType() {
      return this.type;
   }

   public final void setType(String var1) {
      this.type = var1;
   }

   public boolean requiresArgumentsCapture() {
      return false;
   }

   public void setDiagnosticMonitor(DiagnosticMonitor var1) {
      this.monitor = var1;
   }

   public DiagnosticMonitor getDiagnosticMonitor() {
      return this.monitor;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof DiagnosticAction) {
         String var2 = ((DiagnosticAction)var1).getType();
         return this.getType().equals(var2);
      } else {
         return false;
      }
   }

   protected InstrumentationEvent createInstrumentationEvent(JoinPoint var1, boolean var2) {
      if (noEventGen) {
         return null;
      } else {
         DiagnosticContext var3 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
         InstrumentationEvent var4 = new InstrumentationEvent(this, var1, var2);
         if (var3 != null) {
            var4.setContextId(var3.getContextId());
         }

         return var4;
      }
   }
}
