package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;
import weblogic.ejb.spi.EJBJarUtils;
import weblogic.j2ee.descriptor.ModuleBean;

final class EJBCompilerFactory implements CompilerFactory, MergerFactory, ModuleFactory, StandaloneModuleFactory {
   public Compiler createCompiler(CompilerCtx var1, File var2) throws IOException {
      return EJBJarUtils.isEJB(var2) ? new EJBCompiler(var1) : null;
   }

   public EARModule createModule(ModuleBean var1) {
      return var1.getEjb() == null ? null : new EJBModule(var1.getEjb(), var1.getAltDd());
   }

   public EARModule createModule(CompilerCtx var1, File var2) {
      try {
         if (EJBJarUtils.isEJB(var2)) {
            return new EJBModule(var1.getSourceName(), (String)null);
         }
      } catch (IOException var4) {
      }

      return null;
   }

   public Merger createMerger(CompilerCtx var1, File var2) throws IOException {
      if (EJBJarUtils.isEJB(var2)) {
         return (Merger)(var1.isReadOnlyInvocation() ? new ReadOnlyEjbMerger(var1) : new EJBMerger(var1));
      } else {
         return null;
      }
   }
}
