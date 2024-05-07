package weblogic.management.utils;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import weblogic.Home;
import weblogic.common.internal.VersionInfo;
import weblogic.common.internal.VersionInfoFactory;
import weblogic.utils.FileUtils;

public final class PDevHelper {
   private static final String UTILS_CONFIG_DIR;
   private static final String UPGRADE_LAUNCH_JAR = "upgrade-launch.jar";

   public static ClassLoader getPDevClassLoader(ClassLoader var0) {
      try {
         File var1 = new File(getUpgradeLaunchLocation());
         URL var2 = var1.toURI().toURL();
         URL[] var3 = new URL[]{var2};
         return new URLClassLoader(var3, var0);
      } catch (MalformedURLException var4) {
         throw new AssertionError(var4);
      }
   }

   public static String addPDevLibraryToClasspath(String var0) {
      String var1 = var0;
      if (var0 == null) {
         var1 = "";
      }

      var1 = var1 + System.getProperty("path.separator") + getUpgradeLaunchLocation();
      return var1;
   }

   private static String getUpgradeLaunchLocation() {
      String var0 = (new File(Home.getFile().getParentFile().getParentFile().getAbsolutePath())).getAbsolutePath();
      String var1 = var0 + File.separator + UTILS_CONFIG_DIR;
      String var2 = findUpgradeLaunchJarLocation(var1);
      if (var2 != null) {
         return var2;
      } else {
         String var3 = (new File(Home.getMiddlewareHomePath())).getAbsolutePath();
         var1 = var3 + File.separator + UTILS_CONFIG_DIR;
         var2 = findUpgradeLaunchJarLocation(var1);
         if (var2 != null) {
            return var2;
         } else {
            var2 = System.getenv("FMWLAUNCH_CLASSPATH");
            return var2 != null ? var2 : "upgrade-launch.jar";
         }
      }
   }

   private static String findUpgradeLaunchJarLocation(String var0) {
      File var1 = new File(var0);
      if (var1.exists() && !var1.isFile()) {
         File var2 = new File(var0 + File.separator + VersionInfoFactory.getVersionInfo().getMajor() + "." + VersionInfo.theOne().getMinor() + File.separator + "upgrade-launch.jar");
         if (var2.exists()) {
            return var2.getAbsolutePath();
         } else {
            File[] var3 = FileUtils.find(var1, new FileFilter() {
               public boolean accept(File var1) {
                  return var1.isFile() && var1.getName().equals("upgrade-launch.jar");
               }
            });
            return var3 != null && var3.length > 0 ? var3[0].getAbsolutePath() : null;
         }
      } else {
         return null;
      }
   }

   static {
      UTILS_CONFIG_DIR = "utils" + File.separator + "config";
   }
}
