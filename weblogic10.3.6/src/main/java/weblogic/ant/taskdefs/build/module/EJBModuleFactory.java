package weblogic.ant.taskdefs.build.module;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import weblogic.ant.taskdefs.build.BuildCtx;
import weblogic.ejb.spi.EJBJarUtils;
import weblogic.utils.FileUtils;

public final class EJBModuleFactory extends ModuleFactory {
   private static FileFilter ejbGenFilter = new FileFilter() {
      public boolean accept(File var1) {
         return !var1.isDirectory() && var1.getName().endsWith(".ejb");
      }
   };

   Module claim(BuildCtx var1, File var2, File var3) throws BuildException {
      boolean var4 = false;

      try {
         var4 = EJBJarUtils.isEJB(var2);
      } catch (IOException var7) {
      }

      try {
         if (!var4) {
            var4 = EJBJarUtils.hasEJBSources(var2);
         }
      } catch (IOException var6) {
      }

      File[] var5 = FileUtils.find(var2, ejbGenFilter);
      if (var5 != null && var5.length > 0) {
         var4 = true;
      }

      if (var2.isDirectory() && var2.getName().toLowerCase().endsWith(".jar")) {
         var4 = true;
      }

      return var4 ? new EJBModule(var1, var2, var3, var5) : null;
   }
}
