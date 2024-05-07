package weblogic.diagnostics.harvester.internal;

import weblogic.diagnostics.lifecycle.DiagnosticComponentLifecycle;
import weblogic.diagnostics.lifecycle.DiagnosticComponentLifecycleException;

public class HarvesterLifecycleImpl implements DiagnosticComponentLifecycle {
   private static final HarvesterLifecycleImpl SINGLETON = new HarvesterLifecycleImpl();
   private static final MetricArchiver HARVESTER = MetricArchiver.getInstance();

   public static final DiagnosticComponentLifecycle getInstance() {
      return SINGLETON;
   }

   public int getStatus() {
      return HARVESTER.getStatus();
   }

   public void initialize() throws DiagnosticComponentLifecycleException {
      HARVESTER.initialize();
   }

   public void enable() throws DiagnosticComponentLifecycleException {
      HARVESTER.enable();
   }

   public void disable() throws DiagnosticComponentLifecycleException {
      HARVESTER.disable();
   }
}
