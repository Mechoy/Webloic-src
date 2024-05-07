package weblogic.management.j2ee.internal;

import java.io.File;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import weblogic.Home;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class InternalAppDataCacheService extends AbstractServerService {
   private static final Map appToSource = new HashMap(7);
   private static final String LIB;
   private static AuthenticatedSubject kernelId;

   public void start() throws ServiceFailureException {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
         AppDeploymentMBean[] var2 = AppDeploymentHelper.getAppsAndLibs(ManagementService.getRuntimeAccess(kernelId).getDomain());

         for(int var3 = 0; var3 < var2.length; ++var3) {
            AppDeploymentMBean var4 = var2[var3];
            if (var4.isInternalApp()) {
               appToSource.put(var4.getName(), var4.getAbsoluteSourcePath());
            }
         }

         appToSource.put("bea_wls_cluster_internal", LIB);
      }
   }

   public void stop() throws ServiceFailureException {
   }

   public void halt() throws ServiceFailureException {
   }

   public static String getSourceLocation(String var0) {
      return (String)appToSource.get(var0);
   }

   public static boolean isInternalApp(String var0) {
      return appToSource.containsKey(var0);
   }

   static {
      LIB = Home.getPath() + File.separator + "lib";
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }
}
