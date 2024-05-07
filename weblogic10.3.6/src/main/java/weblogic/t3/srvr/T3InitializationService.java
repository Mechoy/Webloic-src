package weblogic.t3.srvr;

import weblogic.common.internal.T3BindableServices;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class T3InitializationService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      BootServicesImpl.initialize();
      T3BindableServices.initialize();
   }
}
