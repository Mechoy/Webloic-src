package weblogic.jndi.internal;

import java.security.AccessController;
import java.util.Properties;
import weblogic.jndi.WLInitialContextFactory;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class NamingService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static NamingService singleton;
   private String urlPkgPrefixes;
   private int state = 0;

   public static NamingService getNamingService() {
      return singleton;
   }

   public NamingService() {
      singleton = this;
   }

   public void start() throws ServiceFailureException {
      Properties var1 = System.getProperties();
      var1.put("java.naming.factory.initial", WLInitialContextFactory.class.getName());
      String var2 = (String)var1.get("java.naming.factory.url.pkgs");
      if (var2 != null && var2.length() > 0) {
         this.urlPkgPrefixes = var2 + ":" + "weblogic.jndi.factories:weblogic.corba.j2ee.naming.url";
      } else {
         this.urlPkgPrefixes = "weblogic.jndi.factories:weblogic.corba.j2ee.naming.url";
      }

      var1.put("java.naming.factory.url.pkgs", this.urlPkgPrefixes);
      WLNamingManager.initialize();
      RootNamingNode.initialize();
      RemoteContextFactoryImpl.initialize();
      this.changeState(2);
   }

   public String getUrlPkgPrefixes() {
      return this.urlPkgPrefixes;
   }

   public void stop() throws ServiceFailureException {
      this.changeState(0);
   }

   public void halt() throws ServiceFailureException {
      this.changeState(0);
   }

   private synchronized void changeState(int var1) {
      this.state = var1;
      this.notifyAll();
   }

   boolean isRunning() {
      return !ManagementService.getRuntimeAccess(kernelId).getServerRuntime().isShuttingDown();
   }
}
