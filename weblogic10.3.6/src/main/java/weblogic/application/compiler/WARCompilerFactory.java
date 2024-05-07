package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.servlet.utils.WarUtils;

final class WARCompilerFactory implements CompilerFactory, MergerFactory, ModuleFactory, StandaloneModuleFactory {
   public Compiler createCompiler(CompilerCtx var1, File var2) throws IOException {
      return WarUtils.isWar(var2) ? new WARCompiler(var1) : null;
   }

   public EARModule createModule(ModuleBean var1) {
      return var1.getWeb() == null ? null : new WARModule(var1.getWeb().getWebUri(), var1.getAltDd(), var1.getWeb().getContextRoot());
   }

   public EARModule createModule(CompilerCtx var1, File var2) {
      try {
         if (WarUtils.isWar(var2)) {
            return new WARModule(var1.getSourceName(), (String)null, (String)null);
         }
      } catch (IOException var4) {
      }

      return null;
   }

   public Merger createMerger(CompilerCtx var1, File var2) throws IOException {
      if (WarUtils.isWar(var2)) {
         return (Merger)(var1.isReadOnlyInvocation() ? new ReadOnlyWarMerger(var1) : new WARMerger(var1));
      } else {
         return null;
      }
   }
}
