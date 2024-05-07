package weblogic.application.utils;

import java.io.File;
import weblogic.application.ApplicationAccess;
import weblogic.application.internal.FlowContext;
import weblogic.j2ee.J2EEApplicationService;

public final class PathUtils {
   public static File getAppTempDir(String var0, String var1) {
      return getAppTempDir(var0, var1, (String)null);
   }

   public static File getAppTempDir(String var0, String var1, String var2) {
      return getAppTempDir(generateTempPath(var0, var1, var2));
   }

   public static File getAppTempDir(String var0) {
      FlowContext var1 = (FlowContext)ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
      boolean var2 = var1 == null ? false : var1.isInternalApp();
      return getAppTempDir(var0, var2);
   }

   public static File getAppTempDir(String var0, boolean var1) {
      return new File(getRootTempDir(var1), var0);
   }

   private static File getRootTempDir(boolean var0) {
      return var0 ? PathUtils.TempPaths.internalAppTmpRoot : PathUtils.TempPaths.userAppTmpRoot;
   }

   public static String generateTempPath(String var0, String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      if (var0 != null) {
         var3.append(var0);
      }

      if (var1 != null) {
         var3.append("_").append(var1);
      }

      if (var2 != null) {
         var3.append("_").append(var2);
      }

      return var1 + File.separator + Long.toString((long)Math.abs(var3.toString().hashCode()), 36);
   }

   public static File generateDescriptorCacheDir(String var0, String var1, boolean var2) {
      return new File(getRootTempDir(var2), generateTempPath(var0, ApplicationVersionUtils.replaceDelimiter(var1, '_'), "__WL_DescriptorCache"));
   }

   private static final class TempPaths {
      private static final File tmpRoot = J2EEApplicationService.getTempDir();
      private static final File userAppTmpRoot;
      private static final File internalAppTmpRoot;

      static {
         userAppTmpRoot = new File(tmpRoot, "_WL_user");
         internalAppTmpRoot = new File(tmpRoot, "_WL_internal");
      }
   }
}
