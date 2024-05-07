package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;

final class JMSCompilerFactory implements CompilerFactory, MergerFactory, WLModuleFactory, StandaloneModuleFactory {
   public Compiler createCompiler(CompilerCtx var1, File var2) {
      if (var2.isDirectory()) {
         return null;
      } else {
         return var2.getName().endsWith("-jms.xml") ? new JMSCompiler(var1) : null;
      }
   }

   public EARModule createModule(WeblogicModuleBean var1) {
      if (!var1.getType().equals("JMS")) {
         return null;
      } else {
         String var2 = var1.getPath();
         Object var3 = null;
         JMSModule var4 = new JMSModule(var2, (String)var3);
         var4.setModuleName(var1.getName());
         return var4;
      }
   }

   public EARModule createModule(CompilerCtx var1, File var2) {
      if (var2.isDirectory()) {
         return null;
      } else {
         return var2.getName().endsWith("-jms.xml") ? new JMSModule(var2.getPath(), (String)null) : null;
      }
   }

   public Merger createMerger(CompilerCtx var1, File var2) throws IOException {
      if (var2.isDirectory()) {
         return null;
      } else {
         return var2.getName().endsWith("-jms.xml") ? new JMSMerger(var1) : null;
      }
   }
}
