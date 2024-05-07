package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;
import weblogic.connector.utils.RarUtils;
import weblogic.j2ee.descriptor.ModuleBean;

final class RARCompilerFactory implements CompilerFactory, MergerFactory, ModuleFactory, StandaloneModuleFactory {
   public Compiler createCompiler(CompilerCtx var1, File var2) throws IOException {
      return RarUtils.isRar(var2) ? new RARCompiler(var1) : null;
   }

   public EARModule createModule(ModuleBean var1) {
      return var1.getConnector() == null ? null : new RARModule(var1.getConnector(), var1.getAltDd());
   }

   public EARModule createModule(CompilerCtx var1, File var2) {
      return RarUtils.isRar(var2) ? new RARModule(var1.getSourceName(), (String)null) : null;
   }

   public Merger createMerger(CompilerCtx var1, File var2) throws IOException {
      return RarUtils.isRar(var2) ? new RARMerger(var1) : null;
   }
}
