package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes.Name;
import weblogic.application.utils.IOUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

final class CARCompilerFactory implements CompilerFactory, MergerFactory, StandaloneModuleFactory {
   public Compiler createCompiler(CompilerCtx var1, File var2) throws IOException {
      return null;
   }

   public EARModule createModule(CompilerCtx var1, File var2) {
      if (var2.isDirectory() || var2.getName().endsWith(".jar")) {
         VirtualJarFile var3 = null;

         CARModule var4;
         try {
            var3 = VirtualJarFactory.createVirtualJar(var2);
            if ((var3.getManifest() == null || var3.getManifest().getMainAttributes().get(Name.MAIN_CLASS) == null) && var3.getEntry("META-INF/application-client.xml") == null) {
               return null;
            }

            var4 = new CARModule(var1.getSourceName(), (String)null);
         } catch (IOException var9) {
            return null;
         } finally {
            IOUtils.forceClose(var3);
         }

         return var4;
      } else {
         return null;
      }
   }

   public Merger createMerger(CompilerCtx var1, File var2) throws IOException {
      if (var2.isDirectory() || var2.getName().endsWith(".jar")) {
         VirtualJarFile var3 = null;

         CARMerger var4;
         try {
            var3 = VirtualJarFactory.createVirtualJar(var2);
            if ((var3.getManifest() == null || var3.getManifest().getMainAttributes().get(Name.MAIN_CLASS) == null) && var3.getEntry("META-INF/application-client.xml") == null) {
               return null;
            }

            var4 = new CARMerger(var1);
         } catch (IOException var9) {
            return null;
         } finally {
            IOUtils.forceClose(var3);
         }

         return var4;
      } else {
         return null;
      }
   }
}
