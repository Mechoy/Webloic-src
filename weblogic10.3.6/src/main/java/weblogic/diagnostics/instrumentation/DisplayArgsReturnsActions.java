package weblogic.diagnostics.instrumentation;

public final class DisplayArgsReturnsActions extends AbstractDiagnosticAction implements AroundDiagnosticAction {
   public DisplayArgsReturnsActions() {
      this.setType("DisplayArgsReturnsActions");
   }

   public String[] getAttributeNames() {
      return null;
   }

   public DiagnosticActionState createState() {
      return new ArgsState();
   }

   public boolean requiresArgumentsCapture() {
      return true;
   }

   public void preProcess(JoinPoint var1, DiagnosticActionState var2) {
      ArgsState var3 = (ArgsState)var2;
      var3.setArguments(((DynamicJoinPoint)var1).getArguments());
   }

   public void postProcess(JoinPoint var1, DiagnosticActionState var2) {
      ArgsState var3 = (ArgsState)var2;
      InstrumentationEvent var4 = this.createInstrumentationEvent(var1, true);
      if (var4 != null) {
         EventQueue.getInstance().enqueue(var4);
      }
   }

   class ArgsState implements DiagnosticActionState {
      private Object[] args;

      void setArguments(Object[] var1) {
         this.args = var1;
      }

      Object[] getArguments() {
         return this.args;
      }
   }
}
