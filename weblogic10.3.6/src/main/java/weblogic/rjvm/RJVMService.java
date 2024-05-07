package weblogic.rjvm;

import java.net.UnknownHostException;
import java.security.AccessController;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.provider.ManagementService;
import weblogic.rjvm.t3.ProtocolHandlerT3;
import weblogic.rjvm.t3.ProtocolHandlerT3S;
import weblogic.rmi.utils.io.Codebase;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.servlet.internal.WebService;

public class RJVMService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      try {
         this.setJVMID();
         this.initGenericClassLoader();
      } catch (UnknownHostException var2) {
         throw new ServiceFailureException(var2);
      } catch (ConfigurationException var3) {
         throw new ServiceFailureException(var3);
      }
   }

   public void setJVMID() throws UnknownHostException, ConfigurationException {
      String var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getListenAddress();
      String var2 = ManagementService.getRuntimeAccess(kernelId).getServer().getExternalDNSName();
      ProtocolHandlerT3.getProtocolHandler();
      ProtocolHandlerT3S.getProtocolHandler();
      JVMID.setLocalID(var1, var2, ManagementService.getRuntimeAccess(kernelId).getDomainName(), ManagementService.getRuntimeAccess(kernelId).getServerName());
   }

   private void initGenericClassLoader() {
      Codebase.setDefaultCodebase(this.getServerURL(false));
   }

   private String getServerURL(boolean var1) {
      JVMID var2 = JVMID.localID();
      String var3 = var2.getClusterAddress();
      if (var3 == null) {
         var3 = var2.address().getHostAddress();
      }

      int var4 = var1 ? ManagementService.getRuntimeAccess(kernelId).getServer().getSSL().getListenPort() : ManagementService.getRuntimeAccess(kernelId).getServer().getListenPort();
      StringBuffer var5 = new StringBuffer(var1 ? "https://" : "http://");
      var5.append(var3).append(':').append(var4).append(WebService.getInternalWebAppContextPath()).append("/classes").append('/');
      return var5.toString();
   }
}
