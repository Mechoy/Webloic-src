package weblogic.security.utils;

import com.octetstring.vde.util.PasswordEncryptor;
import java.io.File;
import java.io.IOException;
import weblogic.management.bootstrap.BootStrap;
import weblogic.security.SecurityLogger;

public final class AdminAccount {
   private static final boolean debug = false;
   private static final String DEFAULT_TEMPLATE = "DefaultAuthenticatorInit.ldift";
   private static final String DEFAULT_BASE_TEMPLATE;

   public static void main(String[] var0) {
      if (var0.length >= 3 && var0.length <= 4) {
         try {
            String var1 = null;
            if (var0.length == 4) {
               var1 = var0[3];
            }

            setupAdminAccount(var0[0], var0[1], var0[2], var1);
         } catch (Exception var2) {
            System.out.println("Error: " + var2);
         }

      } else {
         System.out.println("Error: Invalid arguments");
      }
   }

   public static void setupAdminAccount(String var0, String var1, String var2, String var3) throws IOException {
      if (var0 != null) {
         var0 = var0.trim();
      }

      if (var1 != null) {
         var1 = var1.trim();
      }

      if (var2 != null) {
         var2 = var2.trim();
      }

      if (var3 != null) {
         var3 = var3.trim();
      }

      if (var0 != null && var0.length() != 0 && var1 != null && var1.length() != 0 && var2 != null && var2.length() != 0) {
         String var4 = var3;
         if (var3 == null || var3.length() == 0) {
            var4 = DEFAULT_BASE_TEMPLATE;
         }

         File var5 = new File(var2);
         if (var5.exists() && var5.isDirectory()) {
            File var6 = new File(var4);
            if (var6.exists() && var6.isFile()) {
               String var7 = var2;
               if (!var2.endsWith(File.separator)) {
                  var7 = var2 + File.separator;
               }

               var7 = var7 + "DefaultAuthenticatorInit.ldift";
               String var8 = null;

               try {
                  var8 = PasswordEncryptor.doSSHA(var1);
               } catch (Exception var10) {
                  throw new IOException(var10.toString());
               }

               if (var8 == null) {
                  throw new IOException(SecurityLogger.getEncryptionError());
               } else {
                  var8 = "{ssha}" + var8;
                  ProviderUtils.convertBaseLDIFTemplate(var0, var8, var7, var4);
               }
            } else {
               throw new IOException(SecurityLogger.getInvalidFileParameterAdminAccount(var4));
            }
         } else {
            throw new IOException(SecurityLogger.getInvalidFileParameterAdminAccount(var2));
         }
      } else {
         throw new IOException(SecurityLogger.getInvalidParameterAdminAccount());
      }
   }

   static {
      DEFAULT_BASE_TEMPLATE = BootStrap.getPathRelativeWebLogicHome("lib" + File.separator + "Authenticator" + "Base.ldift");
   }
}
