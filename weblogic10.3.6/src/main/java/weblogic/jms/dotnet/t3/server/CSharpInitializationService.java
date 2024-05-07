package weblogic.jms.dotnet.t3.server;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class CSharpInitializationService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      CSharpServicesImpl.initialize();
   }
}
