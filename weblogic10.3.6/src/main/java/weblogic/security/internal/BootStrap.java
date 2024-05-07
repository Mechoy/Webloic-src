package weblogic.security.internal;

import java.io.File;
import java.security.AccessController;
import weblogic.management.DomainDir;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.ProviderUtils;

public class BootStrap {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private static String getAbsolutePath(String var0) {
      File var1 = new File(var0);
      return var1.exists() ? var1.getAbsolutePath() : null;
   }

   public static boolean isMigrationMode() {
      return ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().isCompatibilityMode();
   }

   public static String getLocalFileAbsolutePath(String var0) {
      return getAbsolutePath(DomainDir.getPathRelativeRootDir(var0));
   }

   public static String getGlobalFileAbsolutePath(String var0) {
      return getAbsolutePath(weblogic.management.bootstrap.BootStrap.getPathRelativeWebLogicHome("lib") + File.separator + var0);
   }

   public static String getBootStrapFileAbsolutePath(String var0, String var1) {
      String var2 = var0 + "BootStrap" + var1;
      String var3 = getLocalFileAbsolutePath(var2);
      if (var3 != null) {
         return var3;
      } else {
         String var4 = var0 + ProviderUtils.getModeString(isMigrationMode()) + var1;
         return getGlobalFileAbsolutePath(var4);
      }
   }
}
