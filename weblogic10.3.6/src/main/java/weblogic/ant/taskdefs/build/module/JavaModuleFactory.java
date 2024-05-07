package weblogic.ant.taskdefs.build.module;

import java.io.File;
import java.io.FileFilter;
import org.apache.tools.ant.BuildException;
import weblogic.ant.taskdefs.build.BuildCtx;
import weblogic.utils.FileUtils;

public final class JavaModuleFactory extends ModuleFactory {
   private static FileFilter JAVA_FILTER = new FileFilter() {
      public boolean accept(File var1) {
         return !var1.isDirectory() && var1.getName().endsWith(".java");
      }
   };

   Module claim(BuildCtx var1, File var2, File var3) throws BuildException {
      return this.hasJavaFiles(var2) ? new JavaModule(var1, var2, var3) : null;
   }

   private boolean hasJavaFiles(File var1) {
      if (var1 == null) {
         return false;
      } else {
         File[] var2 = var1.listFiles(JAVA_FILTER);
         if (var2 != null && var2.length > 0) {
            return true;
         } else {
            File[] var3 = var1.listFiles(FileUtils.DIR);
            if (var3 != null) {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  if (this.hasJavaFiles(var3[var4])) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }
}
