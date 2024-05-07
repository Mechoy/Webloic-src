package weblogic.diagnostics.instrumentation.action;

import com.oracle.jrockit.jfr.TimedEvent;
import weblogic.diagnostics.flightrecorder.FlightRecorderManager;
import weblogic.diagnostics.instrumentation.AroundDiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DiagnosticMonitor;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.gathering.DataGatheringManager;
import weblogic.diagnostics.instrumentation.gathering.FlightRecorderEventHelper;

public class FlightRecorderAroundAction implements AroundDiagnosticAction {
   private static final long serialVersionUID = 1L;
   private DiagnosticMonitor monitor;
   private String type;
   private static final FlightRecorderAroundActionState disabledActionState = new FlightRecorderAroundActionState(false);

   public FlightRecorderAroundAction() {
      this.setType("FlightRecorderAroundAction");
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

   public DiagnosticActionState createState() {
      return DataGatheringManager.getDiagnosticVolume() != 0 && FlightRecorderManager.isRecordingPossible() ? new FlightRecorderAroundActionState(true) : disabledActionState;
   }

   public void preProcess(JoinPoint var1, DiagnosticActionState var2) {
      if (var2 != null) {
         FlightRecorderAroundActionState var3 = (FlightRecorderAroundActionState)var2;
         var3.begin(this.monitor, var1);
      }
   }

   public void postProcess(JoinPoint var1, DiagnosticActionState var2) {
      if (var2 != null) {
         FlightRecorderAroundActionState var3 = (FlightRecorderAroundActionState)var2;
         var3.finishAndRecord(this.monitor, var1);
      }
   }

   private static class FlightRecorderAroundActionState implements DiagnosticActionState {
      private boolean enabled;
      private TimedEvent aroundEvent;

      private FlightRecorderAroundActionState(boolean var1) {
         this.enabled = false;
         this.aroundEvent = null;
         this.enabled = var1;
      }

      private void begin(DiagnosticMonitor var1, JoinPoint var2) {
         if (this.enabled) {
            this.aroundEvent = FlightRecorderEventHelper.getInstance().getTimedEvent(var1, var2);
            if (this.aroundEvent != null) {
               this.aroundEvent.begin();
            }

         }
      }

      private void finishAndRecord(DiagnosticMonitor var1, JoinPoint var2) {
         if (this.enabled && this.aroundEvent != null) {
            this.aroundEvent.end();
            FlightRecorderEventHelper.getInstance().recordTimedEvent(var1, var2, this.aroundEvent);
         }
      }

      // $FF: synthetic method
      FlightRecorderAroundActionState(boolean var1, Object var2) {
         this(var1);
      }
   }
}
