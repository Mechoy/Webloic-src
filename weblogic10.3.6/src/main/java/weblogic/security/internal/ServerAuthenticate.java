package weblogic.security.internal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.AccessController;
import java.util.Properties;
import weblogic.management.Admin;
import weblogic.security.SecurityLogger;
import weblogic.security.SecurityMessagesTextFormatter;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.io.TerminalIO;

public final class ServerAuthenticate {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private static boolean creatingDomain(String[] var0) {
      for(int var1 = 0; var0 != null && var1 < var0.length; ++var1) {
         if ("domainCreation".equals(var0[var1])) {
            return true;
         }
      }

      return false;
   }

   public static void main(String[] var0) {
      String var1 = getCommandLineProperty("weblogic.management.username");
      String var2 = getCommandLineProperty("weblogic.management.password");
      boolean var3 = var1 != null && var2 != null;
      boolean var4 = false;
      if (getCommandLineProperty("weblogic.security.TrustKeyStore") != null || getCommandLineProperty("weblogic.security.CustomTrustKeyStoreFileName") != null || getCommandLineProperty("weblogic.security.CustomTrustKeyStoreType") != null || getCommandLineProperty("weblogic.security.CustomTrustKeyStorePassPhrase") != null || getCommandLineProperty("weblogic.security.JavaStandardTrustKeyStorePassPhrase") != null) {
         var4 = true;
      }

      if (!var3 || !var4) {
         boolean var5 = "true".equalsIgnoreCase(System.getProperty("weblogic.system.NodeManagerBoot"));
         String var6 = System.getProperty("weblogic.system.BootIdentityFile");
         SerializedSystemIni.upgradeSSI();
         if (BootProperties.exists(var6) && (var5 || SerializedSystemIni.exists())) {
            BootProperties.load(var6, var5);
         }

         BootProperties.upgradeBP(var6);
         BootProperties var7 = BootProperties.getBootProperties();
         if (!var4 && var7 != null) {
            setCommandLineProperty("weblogic.security.TrustKeyStore", var7.getTrustKeyStore());
            setCommandLineProperty("weblogic.security.CustomTrustKeyStoreFileName", var7.getCustomTrustKeyStoreFileName());
            setCommandLineProperty("weblogic.security.CustomTrustKeyStoreType", var7.getCustomTrustKeyStoreType());
            setCommandLineProperty("weblogic.security.CustomTrustKeyStorePassPhrase", var7.getCustomTrustKeyStorePassPhrase());
            setCommandLineProperty("weblogic.security.JavaStandardTrustKeyStorePassPhrase", var7.getJavaStandardTrustKeyStorePassPhrase());
         }

         if (!var3) {
            if (var5 && var7 != null) {
               String var8 = var7.getOne(kernelId);
               if (var8 == null) {
                  var8 = "";
               }

               String var9 = var7.getTwo(kernelId);
               if (var9 == null) {
                  var9 = "";
               }

               BootProperties.unload(true);
               Properties var10 = System.getProperties();
               var10.setProperty("weblogic.management.username", var8);
               var10.setProperty("weblogic.management.password", var9);
            } else {
               initUserNameAndPassword(creatingDomain(var0), BootProperties.getBootProperties());
            }
         }
      }
   }

   private static void initUserNameAndPassword(boolean var0, BootProperties var1) {
      SecurityMessagesTextFormatter var2 = SecurityMessagesTextFormatter.getInstance();
      String var3 = null;
      String var4 = null;
      if (var1 != null) {
         var3 = var1.getOne(kernelId);
         var4 = var1.getTwo(kernelId);
      } else {
         SecurityLogger.logGettingBootIdentityFromUser();
      }

      if (var3 == null || "".equals(var3.trim())) {
         var3 = System.getProperty("weblogic.management.username");
         if (var3 == null || "".equals(var3.trim())) {
            var3 = promptValue(var2.getUsernamePromptMessage(), true);
         }

         if (var3 == null) {
            var3 = "";
         }

         var3 = var3.trim();
      }

      if (var4 == null) {
         boolean var5 = TerminalIO.isNoEchoAvailable();
         if (!var5) {
            boolean var6 = Admin.getInstance().isProductionModeEnabled();
            boolean var7 = Admin.getInstance().isPasswordEchoAllowed();
            if (var6 && !var7) {
               SecurityLogger.logErrorProductionModeNoEcho();
               System.exit(-1);
            }

            if (!var6 && !var7) {
               SecurityLogger.logErrorDevModeNoEcho();
               System.exit(-1);
            }
         }

         var4 = promptForPassword(var0);
      }

      Properties var8 = System.getProperties();
      var8.put("weblogic.management.username", var3);
      var8.put("weblogic.management.password", var4);
   }

   private static String promptForPassword(boolean var0) {
      SecurityMessagesTextFormatter var1 = SecurityMessagesTextFormatter.getInstance();
      String var2 = promptValue(var1.getPasswordPromptMessage(), false);
      if (var2 == null) {
         var2 = "";
      }

      if (var0) {
         String var3 = promptValue(var1.getPasswordPromptMessageRenter(), false);
         if (!var2.equals(var3)) {
            System.out.println(var1.getPasswordsNoMatch());
            String var4 = promptValue(var1.getPasswordPromptMessageRenter(), false);
            if (!var2.equals(var4)) {
               System.err.println("***************************************************************************");
               System.err.println(var1.getPasswordsNoMatchBoom());
               System.err.println("***************************************************************************");
               System.exit(-1);
            }
         }
      }

      return var2;
   }

   public static String promptValue(String var0, boolean var1) {
      String var2 = null;

      try {
         System.out.print(var0);
         if (!var1 && TerminalIO.isNoEchoAvailable()) {
            try {
               var2 = TerminalIO.readTerminalNoEcho();
               System.out.println("");
            } catch (Error var4) {
               System.err.println("Error: Failed to get value from Standard Input");
            }
         } else {
            BufferedReader var3 = new BufferedReader(new InputStreamReader(System.in));
            var2 = var3.readLine();
         }
      } catch (Exception var5) {
         System.err.println("Error: Failed to get value from Standard Input");
      }

      return var2;
   }

   private static String getCommandLineProperty(String var0) {
      String var1 = System.getProperty(var0);
      return var1 != null && var1.length() > 0 ? var1 : null;
   }

   private static void setCommandLineProperty(String var0, String var1) {
      Properties var2 = System.getProperties();
      if (var1 != null && var1.length() > 0) {
         var2.setProperty(var0, var1);
      } else if (getCommandLineProperty(var0) != null) {
         var2.remove(var0);
      }

   }
}
