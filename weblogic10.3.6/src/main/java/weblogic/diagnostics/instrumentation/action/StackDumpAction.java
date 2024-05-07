package weblogic.diagnostics.instrumentation.action;

import weblogic.diagnostics.instrumentation.AbstractDiagnosticAction;
import weblogic.diagnostics.instrumentation.EventQueue;
import weblogic.diagnostics.instrumentation.InstrumentationEvent;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.StatelessDiagnosticAction;
import weblogic.diagnostics.type.StackTraceUtility;

public final class StackDumpAction extends AbstractDiagnosticAction implements StatelessDiagnosticAction {
   private static final String INST_PKG = "weblogic.diagnostics.instrumentation";

   public StackDumpAction() {
      this.setType("StackDumpAction");
   }

   public String[] getAttributeNames() {
      return null;
   }

   public void process(JoinPoint var1) {
      InstrumentationEvent var2 = this.createInstrumentationEvent(var1, false);
      if (var2 != null) {
         Exception var3 = new Exception();
         int var4 = StackTraceUtility.getMatchingFrames(var3, "weblogic.diagnostics.instrumentation");
         String var5 = StackTraceUtility.removeFrames(var3, var4);
         var2.setPayload(var5);
         EventQueue.getInstance().enqueue(var2);
      }

   }
}
