package weblogic.t3.srvr;

import java.security.AccessController;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.server.channels.AdminPortService;

public final class EnableListenersIfAdminChannelAbsentService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      if (isRequired()) {
         EnableListenersHelper.getInstance().start();
      }

   }

   public static boolean isRequired() {
      return !AdminPortService.getInstance().listenersBound() && !startInRunningState();
   }

   public static boolean isOpenForManagementConnectionsEarly() {
      return AdminPortService.getInstance().listenersBound() || isRequired();
   }

   public static boolean startInRunningState() {
      if (T3Srvr.getT3Srvr().isAbortStartupAfterAdminState()) {
         return false;
      } else {
         String var0 = ManagementService.getRuntimeAccess(kernelId).getServer().getStartupMode();
         return var0 == null || "RUNNING".equalsIgnoreCase(var0);
      }
   }

   public void stop() {
      EnableListenersHelper.getInstance().stop();
   }

   public void halt() {
      EnableListenersHelper.getInstance().halt();
   }
}
