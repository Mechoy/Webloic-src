package weblogic.application.compiler.flow;

import java.io.File;
import java.io.IOException;
import weblogic.application.compiler.AppcUtils;
import weblogic.application.compiler.Compiler;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.CompilerFactory;
import weblogic.application.compiler.ToolsFactoryManager;
import weblogic.j2ee.J2EELogger;
import weblogic.logging.Loggable;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.application.WarDetector;
import weblogic.utils.compiler.ToolFailureException;

public final class AppCompilerFlow extends CompilerFlow {
   private File sourceFile;

   public AppCompilerFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      this.sourceFile = this.ctx.getSourceFile();
      this.compileInput(this.ctx);
      if (this.ctx.getTargetArchive() != null) {
         AppcUtils.createOutputArchive(this.ctx.getTargetArchive(), this.ctx.getOutputDir());
      }

      if (this.ctx.isVerbose()) {
         J2EELogger.logCompilationComplete();
      }

   }

   public void cleanup() {
   }

   private Compiler createCompiler(CompilerCtx var1) throws ToolFailureException {
      CompilerFactory[] var2 = ToolsFactoryManager.compilerFactories;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         try {
            Compiler var4 = var2[var3].createCompiler(var1, this.sourceFile);
            if (var4 != null) {
               return var4;
            }
         } catch (IOException var5) {
            throw new ToolFailureException(StackTraceUtils.throwable2StackTrace(var5));
         }
      }

      if (this.sourceFile.isDirectory()) {
         Loggable var7 = J2EELogger.logAppcNoValidModuleFoundInDirectoryLoggable(this.sourceFile.getAbsolutePath());
         throw new ToolFailureException(var7.getMessage());
      } else {
         String var6 = this.sourceFile.getName().toLowerCase();
         Loggable var8;
         if (var6.endsWith(".jar")) {
            var8 = J2EELogger.logAppcJarNotValidLoggable(this.sourceFile.getAbsolutePath());
            throw new ToolFailureException(var8.getMessage());
         } else if (WarDetector.instance.suffixed(var6)) {
            var8 = J2EELogger.logAppcWarNotValidLoggable(this.sourceFile.getAbsolutePath());
            throw new ToolFailureException(var8.getMessage());
         } else if (var6.endsWith(".rar")) {
            var8 = J2EELogger.logAppcRarNotValidLoggable(this.sourceFile.getAbsolutePath());
            throw new ToolFailureException(var8.getMessage());
         } else {
            var8 = J2EELogger.logAppcEarNotValidLoggable(this.sourceFile.getAbsolutePath());
            throw new ToolFailureException(var8.getMessage());
         }
      }
   }

   private void compileInput(CompilerCtx var1) throws ToolFailureException {
      Compiler var2 = this.createCompiler(var1);

      try {
         var2.compile();
      } catch (ToolFailureException var5) {
         throw var5;
      } catch (Throwable var6) {
         Loggable var4 = J2EELogger.logAppcErrorProcessingFileLoggable(this.sourceFile.getAbsolutePath(), StackTraceUtils.throwable2StackTrace(var6));
         throw new ToolFailureException(var4.getMessage(), var6);
      }
   }
}
