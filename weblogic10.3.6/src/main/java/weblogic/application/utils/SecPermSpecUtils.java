package weblogic.application.utils;

import java.io.File;
import java.util.ArrayList;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.internal.flow.ModuleListenerInvoker;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.ejb.container.deployer.EJBDeployer;
import weblogic.utils.StringUtils;

public class SecPermSpecUtils {
   static String insertPermission(String var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      if (var0 != null && var0.trim().length() != 0) {
         if (var1 != null && var1.trim().length() != 0) {
            var2.append(var1);
            int var3 = var2.indexOf("}");
            if (var3 != -1) {
               var2.insert(var3 - 1, var0);
            }
         } else if (var0 != null && var0.trim().length() > 0) {
            var2.append("grant {");
            var2.append(var0);
            var2.append("};");
         }
      } else if (var1 != null && var1.trim().length() > 0) {
         var2.append(var1);
      }

      return var2.toString();
   }

   private static ArrayList<Module> findEJBModules(ApplicationContextInternal var0) {
      ArrayList var1 = null;
      if (var0 != null) {
         Module[] var2 = var0.getApplicationModules();
         if (var2 != null && var2.length > 0) {
            var1 = new ArrayList();
            Module[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Module var6 = var3[var5];
               if (var6 instanceof ModuleListenerInvoker) {
                  ModuleListenerInvoker var7 = (ModuleListenerInvoker)var6;
                  Module var8 = var7.getDelegate();
                  if (WebLogicModuleType.MODULETYPE_EJB.equalsIgnoreCase(var8.getType())) {
                     var1.add(var8);
                  }
               }
            }
         }
      }

      return var1;
   }

   private static String constructReadPermsForEJBCompilerCacheDirs(ApplicationContextInternal var0) {
      ArrayList var1 = findEJBModules(var0);
      StringBuilder var2 = null;
      if (var1 != null && var1.size() > 0) {
         var2 = new StringBuilder();
         File var3 = EJBDeployer.getEjbCompilerCacheDir();

         for(int var4 = 0; var4 < var1.size(); ++var4) {
            String var5 = var0.getApplicationId();
            String var6 = ((Module)var1.get(var4)).getId();
            File var7 = new File(var3, StringUtils.mangle(var5 + "_" + var6));
            var2.append("\npermission java.io.FilePermission \"" + var7.getAbsolutePath() + System.getProperty("file.separator") + "-\", \"read\";\n");
         }
      }

      return var2 != null ? var2.toString() : null;
   }

   public static String getNewSecurityPermissionSpec(ApplicationContextInternal var0, String var1) {
      return insertPermission(constructReadPermsForEJBCompilerCacheDirs(var0), var1);
   }
}
