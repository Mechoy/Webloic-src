package weblogic.rjvm;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.WebLogicServletContext;

public final class InternalWebAppListener implements ServletContextListener {
   private final Map EMPTY_MAP = new HashMap();
   private static final String TUNNEL_PACKAGE = "weblogic.rjvm.http.";
   private static final String IIOP_TUNNEL_PACKAGE = "weblogic.corba.iiop.http.";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void contextInitialized(ServletContextEvent var1) {
      WebLogicServletContext var2 = (WebLogicServletContext)var1.getServletContext();
      ServerMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServer();

      try {
         var2.registerServlet("HTTPClntSend", "weblogic.rjvm.http.TunnelSendServlet", new String[]{"/HTTPClntSend/*"}, this.EMPTY_MAP, -1);
         var2.registerServlet("HTTPClntRecv", "weblogic.rjvm.http.TunnelRecvServlet", new String[]{"/HTTPClntRecv/*"}, this.EMPTY_MAP, -1);
         var2.registerServlet("HTTPClntLogin", "weblogic.rjvm.http.TunnelLoginServlet", new String[]{"/HTTPClntLogin/*"}, this.EMPTY_MAP, -1);
         var2.registerServlet("HTTPClntClose", "weblogic.rjvm.http.TunnelCloseServlet", new String[]{"/HTTPClntClose/*"}, this.EMPTY_MAP, -1);
      } catch (DeploymentException var7) {
         throw new AssertionError("Unexpected exception registering t3 tunnelling servlets" + var7);
      }

      try {
         if (var3.isIIOPEnabled()) {
            var2.registerServlet("ClientSend", "weblogic.corba.iiop.http.TunnelSendServlet", new String[]{"/iiop/ClientSend/*"}, this.EMPTY_MAP, -1);
            var2.registerServlet("ClientRecv", "weblogic.corba.iiop.http.TunnelRecvServlet", new String[]{"/iiop/ClientRecv/*"}, this.EMPTY_MAP, -1);
            var2.registerServlet("ClientLogin", "weblogic.corba.iiop.http.TunnelLoginServlet", new String[]{"/iiop/ClientLogin/*"}, this.EMPTY_MAP, -1);
            var2.registerServlet("ClientClose", "weblogic.corba.iiop.http.TunnelCloseServlet", new String[]{"/iiop/ClientClose/*"}, this.EMPTY_MAP, -1);
         }
      } catch (DeploymentException var9) {
         throw new AssertionError("Unexpected exception registering IIOP tunnelling servlets" + var9);
      }

      try {
         if (var3.isCOMEnabled()) {
            var2.registerServlet("COM", "weblogic.com.GetORMServlet", new String[]{"/com/*"}, this.EMPTY_MAP, -1);
         }
      } catch (DeploymentException var6) {
         throw new AssertionError("Unexpected exception COM Servlet" + var6);
      }

      try {
         if (var3.getIIOP().getEnableIORServlet()) {
            var2.registerServlet("getior", "weblogic.servlet.utils.iiop.GetIORServlet", new String[]{"/getior/*"}, this.EMPTY_MAP, -1);
         }
      } catch (DeploymentException var8) {
         throw new AssertionError("Unexpected exception IORServlet " + var8);
      }

      try {
         if (!var3.isClasspathServletDisabled()) {
            var2.registerServlet("classes", "weblogic.servlet.ClasspathServlet", new String[]{"/classes/*"}, this.EMPTY_MAP, -1);
         }

      } catch (DeploymentException var5) {
         throw new AssertionError("Unexpected exception registering classpath servlet" + var5);
      }
   }

   public void contextDestroyed(ServletContextEvent var1) {
   }
}
