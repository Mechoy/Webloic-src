package weblogic.spring.monitoring.actions;

import weblogic.diagnostics.instrumentation.AbstractDiagnosticAction;
import weblogic.diagnostics.instrumentation.AroundDiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.JoinPoint;

public abstract class BaseElapsedTimeAction extends AbstractDiagnosticAction implements AroundDiagnosticAction {
   protected BaseElapsedTimeAction() {
      this.setType("SpringBaseElapsedTimeAction");
   }

   protected BaseElapsedTimeAction(String var1) {
      this.setType(var1);
   }

   public String[] getAttributeNames() {
      return null;
   }

   public DiagnosticActionState createState() {
      return new ElapsedTimeActionState();
   }

   public boolean requiresArgumentsCapture() {
      return true;
   }

   public void preProcess(JoinPoint var1, DiagnosticActionState var2) {
      this.setArguments(((DynamicJoinPoint)var1).getArguments(), var2);
   }

   public void postProcess(JoinPoint var1, DiagnosticActionState var2) {
      this.postProcessUpdateState(var2);
      this.updateRuntimeMBean(var2);
   }

   protected void setArguments(Object[] var1, DiagnosticActionState var2) {
      ElapsedTimeActionState var3 = (ElapsedTimeActionState)var2;
      if (var1 != null && var1.length != 0) {
         var3.setSpringBean(var1[0]);
      }
   }

   protected void preProcessUpdateState(DiagnosticActionState var1) {
      ((ElapsedTimeActionState)var1).startTimer();
   }

   protected void postProcessUpdateState(DiagnosticActionState var1) {
      ((ElapsedTimeActionState)var1).stopTimer();
   }

   protected abstract void updateRuntimeMBean(DiagnosticActionState var1);
}
