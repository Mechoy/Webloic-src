package weblogic.management.upgrade;

import com.bea.xml.XmlValidationError;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import weblogic.version;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.VersionConstants;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.PDevHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ConfigFileHelper implements ConfigFileHelperConstants {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static int configurationVersion;
   private static boolean productionModeEnabled;
   private static byte[] old_format_version = "ConfigurationVersion=\"".getBytes();
   private static byte[] new_format_version = "configuration-version>".getBytes();
   private static byte[] old_format_mode = "ProductionModeEnabled=\"".getBytes();
   private static byte[] new_format_mode = "production-mode-enabled>".getBytes();
   private static int UTF8_ZERO = 48;
   private static int UTF8_NINE = 57;

   public static boolean isUpgradeNeeded() throws ConfigFileException {
      if (!ManagementService.getPropertyService(kernelId).isAdminServer()) {
         return false;
      } else {
         File var0 = new File(DomainDir.getRootDir());
         boolean var1 = isUpgradeNeeded(var0);
         if (!var1) {
            return false;
         } else {
            boolean var2 = false;
            if (System.getProperty("weblogic.ForceImplicitUpgradeIfNeeded") != null) {
               var2 = Boolean.getBoolean("weblogic.ForceImplicitUpgradeIfNeeded");
            }

            if (var2) {
               return true;
            } else {
               try {
                  ClassLoader var3 = Thread.currentThread().getContextClassLoader();

                  label122: {
                     boolean var8;
                     try {
                        ClassLoader var4 = PDevHelper.getPDevClassLoader(ConfigFileHelper.class.getClassLoader());
                        Thread.currentThread().setContextClassLoader(var4);
                        Class var26 = var4.loadClass("com.oracle.cie.domain.DomainTypeDetector");
                        Method var6 = var26.getMethod("inspectDomain", File.class);
                        Object var7 = var6.invoke(var26, var0);
                        if (var7 != null) {
                           Class var27 = var4.loadClass("com.oracle.cie.domain.PlatformDomainInfo");
                           Method var9 = var27.getMethod("isPureWLS");
                           Object var10 = var9.invoke(var7);
                           if (var10 != null && var10 instanceof Boolean && (Boolean)var10) {
                              break label122;
                           }

                           ManagementTextTextFormatter var11 = ManagementTextTextFormatter.getInstance();
                           throw new ConfigFileException(var11.getNotPureWLSDomainText(var0.getPath()));
                        }

                        var8 = true;
                     } catch (ClassNotFoundException var19) {
                        throw new AssertionError(var19);
                     } catch (NoSuchMethodException var20) {
                        throw new AssertionError(var20);
                     } catch (IllegalAccessException var21) {
                        throw new AssertionError(var21);
                     } catch (InvocationTargetException var22) {
                        Throwable var5 = var22.getTargetException();
                        throw new ConfigFileException(var5.getMessage(), var5);
                     } finally {
                        Thread.currentThread().setContextClassLoader(var3);
                     }

                     return var8;
                  }

                  boolean var25 = getResponseFromUser();
                  if (!var25) {
                     throw new UpgradeNotWantedException(ManagementLogger.logUpgradeCancelledByUserLoggable().getMessage());
                  } else {
                     return true;
                  }
               } catch (NoClassDefFoundError var24) {
                  throw new ConfigFileException(ManagementLogger.logUpgradeClassNotFoundLoggable(var24).getMessage());
               }
            }
         }
      }
   }

   public static boolean isUpgradeNeeded(File var0) throws ConfigFileException {
      boolean var1 = false;
      ensureOneOrFewerConfigFiles(var0);
      File var2 = new File(var0, "config");
      File var3 = new File(var2, "config.xml");
      File var4 = new File(var0, BootStrap.getConfigFileName());
      BufferedReader var5;
      int var6;
      String var7;
      if (var3.exists()) {
         var5 = getBufferedReader(var3);
         if (var5 != null) {
            var6 = getConfigVersion(var5);
            configurationVersion = var6;
            if (var6 < 9) {
               ensureClosed(var5);
               var5 = getBufferedReader(var3);
               if (isOldFormat(var5)) {
                  var7 = ManagementLogger.logExpectedVersion9Loggable(var6).getMessage();
                  throw new ConfigFileException(var7);
               }
            }

            ensureClosed(var5);
         }

         var5 = getBufferedReader(var3);
         if (var5 != null) {
            productionModeEnabled = getProductionMode(var5);
            ensureClosed(var5);
         }

         return false;
      } else if (var4.exists()) {
         var5 = getBufferedReader(var4);
         if (var5 != null) {
            productionModeEnabled = getProductionMode(var5);
            ensureClosed(var5);
         }

         var5 = getBufferedReader(var4);
         var6 = -1;
         if (var5 != null) {
            var6 = getConfigVersion(var5);
            configurationVersion = var6;
            ensureClosed(var5);
            if (var6 == 9) {
               var7 = ManagementLogger.logExpectedPreVersion9Loggable(var6).getMessage();
               throw new ConfigFileException(var7);
            }
         }

         if (var6 >= 6 && var6 <= 8) {
            return true;
         } else {
            var7 = var4.getAbsolutePath();
            String var8 = ManagementLogger.logUnexpectedConfigVersionLoggable(var6, var7).getMessage();
            throw new ConfigFileException(var8);
         }
      } else if (!ManagementService.getPropertyService(kernelId).isAdminServer()) {
         return false;
      } else {
         throw new ConfigFileException("Unable to locate config.xml.");
      }
   }

   public static int getConfigurationVersion() {
      return configurationVersion;
   }

   public static boolean getProductionModeEnabled() {
      return productionModeEnabled;
   }

   public static int getConfigurationVersionFromNewFormat(File var0) throws ConfigFileException {
      BufferedReader var1 = getBufferedReader(var0);
      boolean var2 = true;
      if (var1 != null) {
         int var3 = getConfigVersion(var1);
         ensureClosed(var1);
         return var3;
      } else {
         throw new ConfigFileException("Unable to locate config.xml.");
      }
   }

   private static BufferedReader getBufferedReader(File var0) {
      if (!var0.exists()) {
         return null;
      } else {
         BufferedReader var1 = null;
         InputStreamReader var2 = null;
         FileInputStream var3 = null;

         try {
            var3 = new FileInputStream(var0);
            var2 = new InputStreamReader(var3);
            var1 = new BufferedReader(var2);
            return var1;
         } catch (FileNotFoundException var9) {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (IOException var8) {
               }
            }

            if (var2 != null) {
               try {
                  var2.close();
               } catch (IOException var7) {
               }
            }

            if (var3 != null) {
               try {
                  var3.close();
               } catch (IOException var6) {
               }
            }

            return null;
         }
      }
   }

   private static int getConfigVersion(BufferedReader var0) throws ConfigFileException {
      boolean var1 = isNewFormat(var0);

      try {
         int var2 = -1;
         int var3 = var0.read();
         int var4 = 0;

         for(int var5 = 0; var3 != -1; var3 = var0.read()) {
            if (var3 == old_format_version[var4]) {
               ++var4;
            } else {
               var4 = 0;
            }

            if (var4 >= old_format_version.length) {
               var2 = readInt(var0);
               break;
            }

            if (var3 == new_format_version[var5]) {
               ++var5;
            } else {
               var5 = 0;
            }

            if (var5 >= new_format_version.length) {
               var2 = readInt(var0);
               break;
            }
         }

         if (var2 == -1) {
            return var1 ? 9 : 8;
         } else if (var2 >= 9 && var1) {
            return var2;
         } else if (var2 < 9 && !var1) {
            return var2;
         } else if (var2 == 9 && !var1) {
            return 8;
         } else {
            String var6 = var1 ? "new format" : "old format";
            String var7 = "Found " + var6 + " config with invalid version: " + var2;
            throw new ConfigFileException(var7);
         }
      } catch (IOException var8) {
         throw new ConfigFileException("Error searching for config version", var8);
      }
   }

   private static boolean getProductionMode(BufferedReader var0) throws ConfigFileException {
      boolean var1 = isNewFormat(var0);

      try {
         boolean var2 = false;
         int var3 = var0.read();
         int var4 = 0;

         for(int var5 = 0; var3 != -1; var3 = var0.read()) {
            if (var3 == old_format_mode[var4]) {
               ++var4;
            } else {
               var4 = 0;
            }

            if (var4 >= old_format_mode.length) {
               var2 = readBoolean(var0);
               break;
            }

            if (var3 == new_format_mode[var5]) {
               ++var5;
            } else {
               var5 = 0;
            }

            if (var5 >= new_format_mode.length) {
               var2 = readBoolean(var0);
               break;
            }
         }

         return var2;
      } catch (IOException var6) {
         throw new ConfigFileException("Error searching for config version", var6);
      }
   }

   private static boolean isNewFormat(BufferedReader var0) throws ConfigFileException {
      try {
         String var1 = null;
         boolean var2 = false;

         while((var1 = var0.readLine()) != null) {
            int var3;
            for(var3 = 0; var3 < VersionConstants.KNOWN_NAMESPACE_PREFIXES.length; ++var3) {
               if (var1.contains(VersionConstants.KNOWN_NAMESPACE_PREFIXES[var3])) {
                  var2 = true;
                  break;
               }
            }

            for(var3 = 0; var3 < VersionConstants.NAMESPACE_DOMAIN_SUPPORTED_VERSIONS.length; ++var3) {
               if (var1.contains(VersionConstants.NAMESPACE_DOMAIN_SUPPORTED_VERSIONS[var3])) {
                  var2 = true;
                  break;
               }
            }

            if (var2) {
               break;
            }
         }

         return var2;
      } catch (IOException var4) {
         throw new ConfigFileException("Unable to determine config file format", var4);
      }
   }

   private static int readInt(BufferedReader var0) throws ConfigFileException {
      int var1 = -1;

      try {
         for(int var2 = var0.read(); var2 != -1 && var2 >= UTF8_ZERO && var2 <= UTF8_NINE; var2 = var0.read()) {
            if (var1 < 0) {
               var1 = var2 - UTF8_ZERO;
            } else {
               var1 *= 10;
               var1 += var2 - UTF8_ZERO;
            }
         }
      } catch (IOException var3) {
         throw new ConfigFileException("Error reading config file version", var3);
      }

      if (var1 == -1) {
         throw new ConfigFileException("Unable to read config file version number.");
      } else {
         return var1;
      }
   }

   private static boolean readBoolean(BufferedReader var0) throws ConfigFileException {
      boolean var1 = false;

      try {
         int var2 = var0.read();
         if (var2 != 34 && var2 != 70 && var2 != 102) {
            if (var2 == 84 || var2 == 116) {
               var1 = true;
            }
         } else {
            var1 = false;
         }

         return var1;
      } catch (IOException var3) {
         throw new ConfigFileException("Error reading config file version", var3);
      }
   }

   private static void ensureClosed(BufferedReader var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var2) {
         }

      }
   }

   private static void ensureOneOrFewerConfigFiles(File var0) throws ConfigFileException {
      File var1 = new File(var0, "config");
      File var2 = new File(var0, BootStrap.getConfigFileName());
      File var3 = findParentConfig(var0);
      new File(var1, "config.xml");
      if (var2.exists()) {
      }

   }

   private static File findParentConfig(File var0) {
      File var1 = null;
      String var2 = var0.getParent();
      if (var2 != null) {
         var1 = new File(var2, BootStrap.getConfigFileName());
      }

      return var1;
   }

   private static boolean getResponseFromUser() {
      ManagementTextTextFormatter var0 = ManagementTextTextFormatter.getInstance();
      String var1 = var0.getAffirmitaveGenerateConfigText();
      String var2 = var0.getNegativeGenerateConfigText();
      int var3 = 1;

      while(var3 < 4) {
         String var4 = null;

         try {
            BufferedReader var5 = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("\n" + var0.getImplicitUpgradePrompt(var1, var2, version.getReleaseBuildVersion()) + ": ");
            var4 = var5.readLine();
            if (var4 != null && var4.equalsIgnoreCase(var1)) {
               return true;
            }

            if (var4 != null && var4.equalsIgnoreCase(var2)) {
               return false;
            }

            System.out.println("\n" + var0.getPleaseConfirmDeny(var1, var2));
            ++var3;
         } catch (IOException var6) {
            return false;
         }
      }

      return false;
   }

   private static boolean isOldFormat(BufferedReader var0) throws ConfigFileException {
      try {
         String var1 = null;
         boolean var2 = false;

         while((var1 = var0.readLine()) != null) {
            if (var1.contains("<Domain")) {
               var2 = true;
               break;
            }

            if (var2) {
               break;
            }
         }

         return var2;
      } catch (IOException var3) {
         throw new ConfigFileException("Unable to determine config file format", var3);
      }
   }

   public static boolean isAcceptableXmlValidationError(XmlValidationError var0) {
      String var1 = var0.getMessage();
      if (var1 != null && var1.contains("is not derived from")) {
         String[] var2 = var1.split("is not derived from");

         for(int var3 = 0; var3 < VersionConstants.NAMESPACE_MAPPING.length; ++var3) {
            if (var2.length > 1 && var2[1].contains(VersionConstants.NAMESPACE_MAPPING[var3][1])) {
               return true;
            }
         }
      }

      return false;
   }

   public static void main(String[] var0) throws Exception {
      if (isUpgradeNeeded()) {
         System.out.println("NEED TO UPDATE");
      } else {
         System.out.println("config/config.xml is up to date");
      }

   }

   public static class UpgradeNotWantedException extends ConfigFileException {
      public UpgradeNotWantedException(String var1) {
         super(var1);
      }
   }

   static class ConfigFileException extends ManagementException {
      public ConfigFileException(String var1) {
         super(var1);
      }

      public ConfigFileException(String var1, Throwable var2) {
         super(var1, var2);
      }
   }
}
