package weblogic.application.compiler.flow;

import java.io.File;
import java.io.IOException;
import weblogic.application.compiler.AppcUtils;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.Merger;
import weblogic.application.compiler.MergerFactory;
import weblogic.application.compiler.ToolsFactoryManager;
import weblogic.j2ee.J2EELogger;
import weblogic.logging.Loggable;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.compiler.ToolFailureException;

public final class AppMergerFlow extends CompilerFlow {
   private File sourceFile;
   private Merger merger;

   public AppMergerFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      this.sourceFile = this.ctx.getSourceFile();
      this.mergeInput(this.ctx);
   }

   public void cleanup() throws ToolFailureException {
      this.merger.cleanup();
      if (!this.ctx.isReadOnlyInvocation() && this.ctx.getTargetArchive() != null) {
         AppcUtils.createOutputArchive(this.ctx.getTargetArchive(), this.ctx.getOutputDir());
      }

   }

   private Merger createMerger(CompilerCtx var1) throws ToolFailureException {
      File var2 = var1.getSourceFile();
      MergerFactory[] var3 = ToolsFactoryManager.mergerFactories;

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4] instanceof MergerFactory) {
            Merger var5 = null;

            try {
               var5 = var3[var4].createMerger(var1, var2);
            } catch (IOException var7) {
               throw new ToolFailureException(StackTraceUtils.throwable2StackTrace(var7));
            }

            if (var5 != null) {
               return var5;
            }
         }
      }

      Loggable var8 = J2EELogger.logAppcNoValidModuleFoundInDirectoryLoggable(this.sourceFile.getAbsolutePath());
      throw new ToolFailureException(var8.getMessage());
   }

   private void mergeInput(CompilerCtx var1) throws ToolFailureException {
      this.merger = this.createMerger(var1);

      try {
         this.merger.merge();
      } catch (ToolFailureException var4) {
         throw var4;
      } catch (Throwable var5) {
         Loggable var3 = J2EELogger.logAppcErrorProcessingFileLoggable(this.sourceFile.getAbsolutePath(), StackTraceUtils.throwable2StackTrace(var5));
         throw new ToolFailureException(var3.getMessage(), var5);
      }
   }
}
