package weblogic.diagnostics.lifecycle;

import weblogic.logging.ServerLoggingInitializer;
import weblogic.management.ManagementException;

public class ServerLoggingLifecycleImpl implements DiagnosticComponentLifecycle {
   private static ServerLoggingLifecycleImpl singleton = new ServerLoggingLifecycleImpl();
   private int status = 4;

   public static final DiagnosticComponentLifecycle getInstance() {
      return singleton;
   }

   public int getStatus() {
      return this.status;
   }

   public void initialize() throws DiagnosticComponentLifecycleException {
      try {
         ServerLoggingInitializer.initializeServerLogging();
      } catch (ManagementException var2) {
         throw new DiagnosticComponentLifecycleException(var2);
      }

      this.status = 1;
   }

   public void enable() throws DiagnosticComponentLifecycleException {
   }

   public void disable() throws DiagnosticComponentLifecycleException {
   }
}
