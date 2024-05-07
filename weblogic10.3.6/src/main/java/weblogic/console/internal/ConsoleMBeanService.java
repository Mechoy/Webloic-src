package weblogic.console.internal;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class ConsoleMBeanService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      try {
         ConsoleRuntimeImpl.initialize();
      } catch (Exception var2) {
         throw new ServiceFailureException(var2);
      }
   }
}
