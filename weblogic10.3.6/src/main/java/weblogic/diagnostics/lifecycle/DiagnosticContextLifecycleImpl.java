package weblogic.diagnostics.lifecycle;

public class DiagnosticContextLifecycleImpl implements DiagnosticComponentLifecycle {
   private static DiagnosticContextLifecycleImpl singleton = new DiagnosticContextLifecycleImpl();

   public static final DiagnosticComponentLifecycle getInstance() {
      return singleton;
   }

   public int getStatus() {
      return 4;
   }

   public void initialize() throws DiagnosticComponentLifecycleException {
   }

   public void enable() throws DiagnosticComponentLifecycleException {
   }

   public void disable() throws DiagnosticComponentLifecycleException {
   }
}
