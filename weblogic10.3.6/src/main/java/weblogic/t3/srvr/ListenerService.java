package weblogic.t3.srvr;

import java.security.AccessController;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.server.channels.DynamicListenThreadManager;
import weblogic.utils.Debug;

public class ListenerService extends AbstractServerService {
   private static ListenerService singleton;

   public ListenerService() {
      singleton = this;
   }

   static ListenerService getInstance() {
      Debug.assertion(singleton != null);
      return singleton;
   }

   public void start() throws ServiceFailureException {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (ManagementService.getRuntimeAccess(var1).getServer().getListenersBindEarly()) {
         this.bindListeners();
      }

   }

   void bindListeners() throws ServiceFailureException {
      DynamicListenThreadManager.getInstance().start();
   }
}
