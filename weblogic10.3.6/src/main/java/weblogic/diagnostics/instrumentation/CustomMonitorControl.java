package weblogic.diagnostics.instrumentation;

import weblogic.diagnostics.instrumentation.engine.MonitorSpecification;

public class CustomMonitorControl extends DelegatingMonitorControl implements CustomMonitor {
   static final long serialVersionUID = -851465013676026651L;
   private String pointcutExpr;
   private MonitorSpecification monitorSpecification;

   public CustomMonitorControl(CustomMonitorControl var1) {
      super((DelegatingMonitorControl)var1);
      this.pointcutExpr = var1.pointcutExpr;
      this.monitorSpecification = var1.monitorSpecification;
   }

   public CustomMonitorControl(String var1) {
      this("", var1);
   }

   public CustomMonitorControl(String var1, String var2) {
      super(var1, var2);
   }

   public String[] getCompatibleActionTypes() {
      String[] var1 = new String[0];
      InstrumentationLibrary var2 = InstrumentationLibrary.getInstrumentationLibrary();
      switch (this.getLocationType()) {
         case 1:
         case 2:
            return var2.getStatelessDiagnosticActionTypes();
         case 3:
            return var2.getAroundDiagnosticActionTypes();
         default:
            return var1;
      }
   }

   public String getPointcut() {
      return this.pointcutExpr;
   }

   public void setPointcut(String var1) throws InvalidPointcutException {
      this.pointcutExpr = var1;
   }

   void setMonitorSpecification(MonitorSpecification var1) {
      this.monitorSpecification = var1;
   }

   MonitorSpecification getMonitorSpecification() {
      return this.monitorSpecification;
   }

   public synchronized boolean merge(DiagnosticMonitorControl var1) {
      boolean var2 = false;
      if (super.merge(var1) && var1 instanceof CustomMonitorControl) {
         CustomMonitorControl var3 = (CustomMonitorControl)var1;
         this.pointcutExpr = var3.pointcutExpr;
         this.monitorSpecification = var3.monitorSpecification;
         var2 = true;
      }

      return var2;
   }

   boolean isStructurallyDifferent(CustomMonitorControl var1) {
      if (this.pointcutExpr != null && !this.pointcutExpr.equals(var1.pointcutExpr)) {
         return true;
      } else if (var1.pointcutExpr != null && !var1.pointcutExpr.equals(this.pointcutExpr)) {
         return true;
      } else {
         int var2 = this.monitorSpecification != null ? this.monitorSpecification.getLocation() : -1;
         int var3 = var1.monitorSpecification != null ? var1.monitorSpecification.getLocation() : -1;
         return var2 != var3;
      }
   }
}
