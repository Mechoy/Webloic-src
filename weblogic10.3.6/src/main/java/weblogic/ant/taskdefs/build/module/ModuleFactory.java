package weblogic.ant.taskdefs.build.module;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import weblogic.ant.taskdefs.build.BuildCtx;

public abstract class ModuleFactory {
   private static ModuleFactory[] factories = new ModuleFactory[]{new WebModuleFactory(), new RARModuleFactory(), new EJBModuleFactory(), new JavaModuleFactory()};

   protected static void logVerbose(Project var0, String var1) {
      var0.log(var1, 3);
   }

   abstract Module claim(BuildCtx var1, File var2, File var3) throws BuildException;

   public static List[] createModules(BuildCtx var0, File[] var1) throws BuildException {
      Project var2 = var0.getProject();
      logVerbose(var2, "Creating Modules");
      List[] var3 = new List[factories.length];
      File var4 = var0.getDestDir();

      for(int var5 = 0; var5 < var1.length; ++var5) {
         Module var6 = null;
         Object var7 = null;

         for(int var8 = 0; var8 < factories.length; ++var8) {
            var6 = factories[var8].claim(var0, var1[var5], new File(var4, var1[var5].getName()));
            if (var6 != null) {
               logVerbose(var2, "Adding module: " + var6.getClass().getName() + " with srcdir: " + var6.getSrcdir() + " and destdir: " + var6.getDestdir());
               if (var3[var8] == null) {
                  var3[var8] = new ArrayList();
               }

               var3[var8].add(var6);
               break;
            }
         }

         if (var6 == null) {
            logVerbose(var2, "Unable to determine module type of directory " + var1[var5].getAbsolutePath());
         }
      }

      return reverse(var3);
   }

   private static List[] reverse(List[] var0) {
      int var1 = var0.length;
      List[] var2 = new List[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = var0[var1 - var3 - 1];
      }

      return var2;
   }
}
