package weblogic.diagnostics.instrumentation;

public class StandardMonitorControl extends DiagnosticMonitorControl implements StandardMonitor {
   public StandardMonitorControl(StandardMonitorControl var1) {
      super((DiagnosticMonitorControl)var1);
   }

   public StandardMonitorControl(String var1) {
      this("", var1);
   }

   public StandardMonitorControl(String var1, String var2) {
      super(var1, var2);
   }

   public synchronized boolean merge(DiagnosticMonitorControl var1) {
      return super.merge(var1);
   }
}
