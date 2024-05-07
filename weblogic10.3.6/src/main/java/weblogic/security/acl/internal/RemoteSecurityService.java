package weblogic.security.acl.internal;

import java.rmi.RemoteException;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class RemoteSecurityService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      try {
         ServerHelper.exportObject(SecurityServiceImpl.getSingleton());
      } catch (RemoteException var2) {
         throw new ServiceFailureException(var2);
      }
   }
}
