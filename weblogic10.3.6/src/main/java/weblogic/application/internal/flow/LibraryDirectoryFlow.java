package weblogic.application.internal.flow;

import java.io.File;
import java.io.IOException;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.PersistenceUtils;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.management.DeploymentException;
import weblogic.utils.classloaders.JarClassFinder;

public final class LibraryDirectoryFlow extends BaseFlow {
   public LibraryDirectoryFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      ApplicationBean var1 = this.appCtx.getApplicationDD();
      String var2 = var1 != null ? var1.getLibraryDirectory() : null;
      if (var2 != null) {
         try {
            File[] var3 = PersistenceUtils.getApplicationRoots(this.appCtx.getAppClassLoader(), this.appCtx.getApplicationId(), false);

            for(int var4 = var3.length - 1; var4 >= 0; --var4) {
               File var5 = new File(var3[var4], var2);
               if (var5.isDirectory()) {
                  File[] var6 = var5.listFiles();

                  for(int var7 = var6.length - 1; var7 >= 0; --var7) {
                     if (var6[var7].getName().endsWith(".jar")) {
                        this.appCtx.getAppClassLoader().addClassFinderFirst(new JarClassFinder(var6[var7]));
                     }
                  }
               }
            }
         } catch (IOException var8) {
            throw new DeploymentException(var8);
         }
      }

   }
}
