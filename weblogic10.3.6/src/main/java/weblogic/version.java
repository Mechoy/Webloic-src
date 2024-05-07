package weblogic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import weblogic.common.internal.PackageInfo;
import weblogic.common.internal.VersionInfo;
import weblogic.common.internal.VersionInfoFactory;
import weblogic.server.ServiceActivator;
import weblogic.t3.srvr.ServerServices;
import weblogic.utils.StringUtils;

public class version {
   private static final String VERBOSE = "-verbose";
   private static final String WLS_TITLE = "WebLogic Server";
   private static PackageInfo WLS_PACKAGEINFO = null;

   private static VersionInfo getVersionInfo() {
      return VersionInfoFactory.getVersionInfo();
   }

   public static String getBuildVersion() {
      return getVersionInfo().getImplementationTitle();
   }

   public static String getReleaseBuildVersion() {
      return getVersionInfo().getImplementationVersion();
   }

   public static void main(String[] var0) {
      VersionInfoFactory.initialize(true);
      boolean var1 = var0 != null && var0.length >= 1 && "-verbose".equalsIgnoreCase(var0[0]);

      try {
         System.out.println("\n" + getVersions(var1));
         if (var1) {
            System.out.println("\n" + getServiceVersions());
         } else {
            System.out.println("\nUse 'weblogic.version -verbose' to get subsystem information");
            System.out.println("\nUse 'weblogic.utils.Versions' to get version information for all modules");
         }

         System.out.flush();
      } finally {
         System.exit(0);
      }

   }

   public static String getVersions() {
      return getVersions(false);
   }

   private static String getVersions(boolean var0) {
      StringBuffer var1 = new StringBuffer();
      PackageInfo[] var2 = getVersionInfo().getPackages();
      int var3 = var2.length;
      boolean var4 = true;

      for(int var5 = 0; var5 < var3; ++var5) {
         String var6 = var2[var5].getImplementationTitle();
         if (var0 || var6.startsWith("WebLogic Server")) {
            if (!var4) {
               var1.append("\n");
            }

            var1.append(var2[var5].getImplementationTitle());
            if (var0) {
               var1.append(" ImplVersion: " + var2[var5].getImplementationVersion());
            }

            var4 = false;
         }
      }

      return var1.toString();
   }

   private static String getServiceVersions() {
      StringBuffer var0 = new StringBuffer();
      var0.append(truncate("SERVICE NAME") + "\t" + "VERSION INFORMATION" + "\n");
      var0.append(truncate("============") + "\t" + "===================" + "\n");

      for(int var1 = 0; var1 < ServerServices.SERVICE_CLASS_NAMES.length; ++var1) {
         String var2 = ServerServices.SERVICE_CLASS_NAMES[var1];
         if (var2 != "standby_state" && var2 != "admin_state") {
            try {
               String var3 = null;
               String var4 = null;
               Class var5 = Class.forName(var2);
               if (ServiceActivator.class.isAssignableFrom(var5)) {
                  Field var6 = var5.getField("INSTANCE");
                  if (var6 != null) {
                     ServiceActivator var7 = (ServiceActivator)var6.get((Object)null);
                     if (var7 != null) {
                        var3 = var7.getName();
                        var4 = var7.getVersion();
                     }
                  }
               } else {
                  Object var10 = var5.newInstance();
                  Method var11 = var5.getMethod("getVersion", (Class[])null);
                  var4 = (String)var11.invoke(var10, (Object[])null);
                  Method var8 = var5.getMethod("getName", (Class[])null);
                  var3 = (String)var8.invoke(var10, (Object[])null);
               }

               if (var3 != null && var4 != null && var4.trim().length() > 0) {
                  var0.append(truncate(var3) + "\t" + var4 + "\n");
               }
            } catch (Throwable var9) {
            }
         }
      }

      return var0.toString();
   }

   private static String truncate(String var0) {
      return var0.length() >= 30 ? var0.substring(0, 30) : StringUtils.padStringWidth(var0, 30);
   }

   public static String[] getPLInfo() {
      String[] var0 = new String[2];
      PackageInfo[] var1 = getVersionInfo().getPackages();
      PackageInfo var2 = null;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3].getImplementationTitle();
         if (var4.startsWith("WebLogic Server")) {
            var2 = var1[var3];
            WLS_PACKAGEINFO = var2;
            break;
         }
      }

      var0[0] = "WebLogic Server";
      var0[1] = "" + var2.getMajor() + "." + var2.getMinor();
      return var0;
   }

   public static PackageInfo getWLSPackageInfo() {
      if (WLS_PACKAGEINFO == null) {
         getPLInfo();
      }

      return WLS_PACKAGEINFO;
   }

   public static final String getWebServerReleaseInfo() {
      PackageInfo[] var0 = getVersionInfo().getPackages();
      StringBuffer var1 = new StringBuffer();
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var0.length; ++var3) {
         String var4 = var0[var3].getImplementationTitle();
         String[] var5;
         if (var4.indexOf("Server") <= 0) {
            if (var4.indexOf("Temporary Patch") > 0) {
               var5 = StringUtils.splitCompletely(var4);

               for(int var8 = 0; var8 < var5.length; ++var8) {
                  if (var5[var8].startsWith("CR")) {
                     var2.append(' ');
                     var2.append(var5[var8]);
                     break;
                  }
               }
            }
         } else {
            var5 = StringUtils.splitCompletely(var4);
            StringBuffer var6 = new StringBuffer();

            for(int var7 = 0; var7 < var5.length; ++var7) {
               var6.append(var5[var7]);
               var6.append(' ');
            }

            var1.append(var6.toString());
         }
      }

      if (var2.length() > 0) {
         var1.append("with");
         var1.append(var2.toString());
      }

      return var1.toString();
   }
}
