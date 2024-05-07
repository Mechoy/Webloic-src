package weblogic.application.compiler.flow;

import java.io.File;
import java.io.IOException;
import weblogic.application.SingleModuleFileManager;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.application.compiler.ToolsFactoryManager;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.JarFileUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public abstract class SingleModuleFlow extends CompilerFlow {
   public SingleModuleFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      File var1 = this.ctx.getSourceFile();
      if (this.ctx.getApplicationContext().getApplicationFileManager() == null) {
         this.ctx.getApplicationContext().setApplicationFileManager(new SingleModuleFileManager(var1));
      }

      EARModule var2 = ToolsFactoryManager.createStandaloneModule(this.ctx, var1);
      this.prepareModule(var2);
      if (var1 != null) {
         this.ctx.setModules(new EARModule[]{var2});
         var2.initModuleClassLoader(this.ctx, this.ctx.getApplicationContext().getAppClassLoader());
         this.proecessModule(var2);
      }

   }

   private void prepareModule(EARModule var1) throws ToolFailureException {
      try {
         if (var1 != null && (var1.getModuleType().equals(WebLogicModuleType.JMS) || var1.getModuleType().equals(WebLogicModuleType.JDBC))) {
            var1.setArchive(false);
            var1.setOutputDir(this.ctx.getOutputDir());
            var1.setOutputFileName(this.ctx.getSourceFile().getPath());
         } else {
            VirtualJarFile var2 = VirtualJarFactory.createVirtualJar(this.ctx.getSourceFile());
            var1.setVirtualJarFile(var2);
            var1.setArchive(this.ctx.getSourceFile().isFile());
            var1.setOutputDir(this.ctx.getOutputDir());
            var1.setOutputFileName(this.ctx.getOutputDir().getPath());
            if (!this.ctx.getSourceFile().equals(this.ctx.getOutputDir())) {
               JarFileUtils.extract(var2, this.ctx.getOutputDir());
            }

         }
      } catch (IOException var3) {
         throw new ToolFailureException(var3.getMessage(), var3);
      }
   }

   public void cleanup() {
      EARModule[] var1 = this.ctx.getModules();
      if (var1.length != 1) {
         throw new AssertionError("SingleModuleMergerFlow can be invoked for standalone modules only");
      } else {
         var1[0].cleanup();
      }
   }

   protected abstract void proecessModule(EARModule var1) throws ToolFailureException;
}
