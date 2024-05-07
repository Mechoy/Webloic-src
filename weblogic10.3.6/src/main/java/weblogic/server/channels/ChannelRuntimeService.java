package weblogic.server.channels;

import java.rmi.RemoteException;
import java.security.AccessController;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerChannelManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class ChannelRuntimeService extends AbstractServerService {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      try {
         ChannelService var1 = (ChannelService)ServerChannelManager.getServerChannelManager();
         var1.registerRuntimeService();
         ManagementService.getPropertyService(kernelId).setChannelServiceReady();
      } catch (ManagementException var2) {
         throw new ServiceFailureException(var2);
      } catch (RemoteException var3) {
         throw new ServiceFailureException(var3);
      }
   }
}
