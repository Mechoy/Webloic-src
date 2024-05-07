package weblogic.diagnostics.instrumentation.action;

import weblogic.diagnostics.instrumentation.AbstractDiagnosticAction;
import weblogic.diagnostics.instrumentation.AroundDiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DiagnosticMonitor;
import weblogic.diagnostics.instrumentation.EventQueue;
import weblogic.diagnostics.instrumentation.InstrumentationEvent;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.utils.time.Timer;

public final class TraceElapsedTimeAction extends AbstractDiagnosticAction implements AroundDiagnosticAction {
   public TraceElapsedTimeAction() {
      this.setType("TraceElapsedTimeAction");
   }

   public String[] getAttributeNames() {
      return null;
   }

   public DiagnosticActionState createState() {
      return new TraceElapsedTimeActionState();
   }

   public void preProcess(JoinPoint var1, DiagnosticActionState var2) {
      TraceElapsedTimeActionState var3 = (TraceElapsedTimeActionState)var2;
      Timer var4 = Timer.createTimer();
      var3.setValue(var4.timestamp());
      DiagnosticMonitor var5 = this.getDiagnosticMonitor();
      if (var5 != null) {
         InstrumentationEvent var6 = this.createInstrumentationEvent(var1, false);
         if (var6 == null) {
            return;
         }

         var6.setEventType(this.getType() + "-Before-" + var3.getId());
         EventQueue.getInstance().enqueue(var6);
      }

   }

   public void postProcess(JoinPoint var1, DiagnosticActionState var2) {
      InstrumentationEvent var3 = this.createInstrumentationEvent(var1, false);
      if (var3 != null) {
         TraceElapsedTimeActionState var4 = (TraceElapsedTimeActionState)var2;
         Timer var5 = Timer.createTimer();
         var3.setPayload(new Long(var5.timestamp() - var4.getValue()));
         var3.setEventType(this.getType() + "-After-" + var4.getId());
         EventQueue.getInstance().enqueue(var3);
      }
   }

   private static class TraceElapsedTimeActionState implements DiagnosticActionState {
      private static int seqNum;
      private int id = genId();
      private long value;

      TraceElapsedTimeActionState() {
      }

      int getId() {
         return this.id;
      }

      long getValue() {
         return this.value;
      }

      void setValue(long var1) {
         this.value = var1;
      }

      private static synchronized int genId() {
         ++seqNum;
         return seqNum;
      }
   }
}
