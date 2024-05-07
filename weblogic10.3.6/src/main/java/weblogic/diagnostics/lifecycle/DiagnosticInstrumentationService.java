package weblogic.diagnostics.lifecycle;

import weblogic.diagnostics.instrumentation.InstrumentationManager;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class DiagnosticInstrumentationService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      try {
         InstrumentationManager.getInstrumentationManager().initialize();
      } catch (Exception var2) {
         throw new ServiceFailureException(var2);
      }
   }
}
