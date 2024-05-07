package weblogic.application.compiler.flow;

import java.io.File;
import java.io.IOException;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.utils.PersistenceUtils;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.utils.classloaders.JarClassFinder;
import weblogic.utils.compiler.ToolFailureException;

public final class LibraryDirectoryFlow extends CompilerFlow {
   public LibraryDirectoryFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      ApplicationBean var1 = this.ctx.getApplicationDD();
      String var2 = var1.getLibraryDirectory();
      if (var2 != null) {
         try {
            File[] var3 = PersistenceUtils.getApplicationRoots(this.ctx.getApplicationContext().getAppClassLoader(), this.ctx.getApplicationContext().getApplicationId(), false);

            for(int var4 = var3.length - 1; var4 >= 0; --var4) {
               File var5 = new File(var3[var4], var2);
               if (var5.isDirectory()) {
                  File[] var6 = var5.listFiles();

                  for(int var7 = var6.length - 1; var7 >= 0; --var7) {
                     if (var6[var7].getName().endsWith(".jar")) {
                        this.ctx.getApplicationContext().getAppClassLoader().addClassFinderFirst(new JarClassFinder(var6[var7]));
                     }
                  }
               }
            }
         } catch (IOException var8) {
            throw new ToolFailureException("Unable process <library-directory> in application.xml", var8);
         }
      }

   }

   public void cleanup() {
   }
}
