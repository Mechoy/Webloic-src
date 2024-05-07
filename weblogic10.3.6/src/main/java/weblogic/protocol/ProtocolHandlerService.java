package weblogic.protocol;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServerLogger;
import weblogic.server.ServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.jars.ManifestManager;

public class ProtocolHandlerService extends AbstractServerService {
   private ServerService[] handlers;

   public void start() throws ServiceFailureException {
      ArrayList var1 = ManifestManager.getServices(AbstractProtocolService.class);
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      ServerLogger.logProtocolConfigured(Arrays.asList(ManagementService.getRuntimeAccess(var2).getServer().getSupportedProtocols()).toString());
      this.handlers = (ServerService[])((ServerService[])var1.toArray(new ServerService[var1.size()]));
      HashSet var3 = new HashSet(this.handlers.length);

      for(int var4 = 0; var4 < this.handlers.length; ++var4) {
         if (!var3.contains(this.handlers[var4].getName())) {
            this.handlers[var4].start();
            var3.add(this.handlers[var4].getName());
         }
      }

   }

   public void stop() throws ServiceFailureException {
      for(int var1 = 0; var1 < this.handlers.length; ++var1) {
         this.handlers[var1].stop();
      }

   }

   public void halt() throws ServiceFailureException {
      for(int var1 = 0; var1 < this.handlers.length; ++var1) {
         this.handlers[var1].halt();
      }

   }
}
