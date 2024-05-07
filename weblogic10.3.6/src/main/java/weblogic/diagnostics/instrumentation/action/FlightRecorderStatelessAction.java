package weblogic.diagnostics.instrumentation.action;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.flightrecorder.FlightRecorderManager;
import weblogic.diagnostics.instrumentation.DiagnosticMonitor;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.StatelessDiagnosticAction;
import weblogic.diagnostics.instrumentation.gathering.FlightRecorderEventHelper;

public class FlightRecorderStatelessAction implements StatelessDiagnosticAction {
   private static DebugLogger debugLog = DebugLogger.getDebugLogger("DebugDiagnosticDataGathering");
   private static final long serialVersionUID = 1L;
   private DiagnosticMonitor monitor;
   private String type;

   public FlightRecorderStatelessAction() {
      this.setType("FlightRecorderStatelessAction");
   }

   public boolean requiresArgumentsCapture() {
      return true;
   }

   public void setDiagnosticMonitor(DiagnosticMonitor var1) {
      this.monitor = var1;
   }

   public DiagnosticMonitor getDiagnosticMonitor() {
      return this.monitor;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String getType() {
      return this.type;
   }

   public void process(JoinPoint var1) {
      if (FlightRecorderManager.isRecordingPossible()) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("FlightRecorderStatelessAction.process()");
         }

         FlightRecorderEventHelper.getInstance().recordStatelessEvent(this.monitor, var1);
      }
   }
}
