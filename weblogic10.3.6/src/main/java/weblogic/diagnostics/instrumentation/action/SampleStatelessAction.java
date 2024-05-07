package weblogic.diagnostics.instrumentation.action;

import weblogic.diagnostics.instrumentation.AbstractDiagnosticAction;
import weblogic.diagnostics.instrumentation.EventQueue;
import weblogic.diagnostics.instrumentation.InstrumentationEvent;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.StatelessDiagnosticAction;

public final class SampleStatelessAction extends AbstractDiagnosticAction implements StatelessDiagnosticAction {
   public SampleStatelessAction() {
      this.setType("SampleStatelessAction");
   }

   public String[] getAttributeNames() {
      return null;
   }

   public void process(JoinPoint var1) {
      System.out.println("TRACE [SampleStatelessAction] " + var1);
      InstrumentationEvent var2 = this.createInstrumentationEvent(var1, false);
      if (var2 != null) {
         EventQueue.getInstance().enqueue(var2);
      }

   }
}
