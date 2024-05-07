package weblogic.server.channels;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.Debug;

public class EnableAdminListenersService extends AbstractServerService {
   private static EnableAdminListenersService singleton;

   public EnableAdminListenersService() {
      singleton = this;
   }

   static EnableAdminListenersService getInstance() {
      Debug.assertion(singleton != null);
      return singleton;
   }

   public void start() throws ServiceFailureException {
      if (!AdminPortService.getInstance().listenersBound()) {
         AdminPortService.getInstance().bindListeners();
      }

      AdminPortService.getInstance().enable();
   }
}
