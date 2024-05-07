package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;
import weblogic.application.utils.IOUtils;
import weblogic.utils.jars.JarFileUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class SCACompilerFactory implements CompilerFactory, MergerFactory {
   private String SCA_CONTRIBUTION_URI = "META-INF/sca-contribution.xml";

   public Compiler createCompiler(CompilerCtx var1, File var2) {
      return null;
   }

   public Merger createMerger(CompilerCtx var1, File var2) throws IOException {
      if (var2.isFile() && !JarFileUtils.isJar(var2)) {
         return null;
      } else {
         VirtualJarFile var3 = null;

         SCAMerger var4;
         try {
            var3 = VirtualJarFactory.createVirtualJar(var2);
            if (var3.getEntry(this.SCA_CONTRIBUTION_URI) == null) {
               return null;
            }

            var4 = new SCAMerger(var1);
         } finally {
            IOUtils.forceClose(var3);
         }

         return var4;
      }
   }
}
