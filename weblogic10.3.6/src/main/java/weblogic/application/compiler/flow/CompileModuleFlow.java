package weblogic.application.compiler.flow;

import weblogic.application.compiler.AppcUtils;
import weblogic.application.compiler.BuildtimeApplicationContext;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.application.utils.ClassLoaderUtils;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.validation.EARValidator;
import weblogic.utils.Debug;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.compiler.jdt.JDTJavaCompilerFactory;

public final class CompileModuleFlow extends CompilerFlow {
   private final BuildtimeApplicationContext libCtx;
   private EARModule[] modules = null;
   private EARValidator earValidator = null;
   private GenericClassLoader cl;

   public CompileModuleFlow(CompilerCtx var1) {
      super(var1);
      this.libCtx = (BuildtimeApplicationContext)var1.getApplicationContext();
   }

   public void compile() throws ToolFailureException {
      this.modules = this.ctx.getModules();
      this.earValidator = new EARValidator(this.ctx.getApplicationDD(), this.ctx.getWLApplicationDD());
      this.cl = this.getApplicationClassLoader();
      ClassLoaderUtils.initFilterPatterns(this.ctx.getWLApplicationDD() != null ? this.ctx.getWLApplicationDD().getPreferApplicationPackages() : null, this.ctx.getWLApplicationDD() != null ? this.ctx.getWLApplicationDD().getPreferApplicationResources() : null, this.cl);
      this.compileModules(this.cl);

      try {
         this.earValidator.validate();
      } catch (ErrorCollectionException var2) {
         throw new ToolFailureException(J2EELogger.logAppcErrorsValidatingEarLoggable(var2.toString()).getMessage(), var2);
      }
   }

   private GenericClassLoader getApplicationClassLoader() {
      MultiClassFinder var1 = new MultiClassFinder();
      var1.addFinder(this.ctx.getEar().getClassFinder());

      for(int var2 = 0; var2 < this.modules.length; ++var2) {
         var1.addFinder(this.modules[var2].getClassFinder());
      }

      var1.addFinder(this.libCtx.getClassFinder());
      return AppcUtils.getClassLoaderForApplication(var1, this.ctx, this.ctx.getApplicationContext().getApplicationId());
   }

   private void compileModules(GenericClassLoader var1) throws ToolFailureException {
      boolean var2 = this.ctx.getOpts().hasOption("quiet");
      String var3 = this.ctx.getOpts().getOption("moduleUri");
      boolean var4 = false;

      for(int var5 = 0; var5 < this.modules.length; ++var5) {
         EARModule var6 = this.modules[var5];
         if (var3 == null || var3.equals(var6.getURI())) {
            var4 = true;
            this.earValidator.addModuleValidationInfo(var6.getModuleValidationInfo());
            if ((this.ctx.isVerbose() || !var2) && !var6.isLibrary()) {
               J2EELogger.logCompilingEarModule(var6.getURI());
            }

            if (debug && !var6.isLibrary()) {
               Debug.say("Compiling: " + var6.getURI() + " from: " + var6.getOutputFileName());
            }

            var6.compile(var1, this.ctx);
         }
      }

      if (!var4) {
         throw new ToolFailureException(J2EELogger.logModuleUriDoesNotExistLoggable(var3).getMessage());
      }
   }

   public void cleanup() {
      if (this.cl != null) {
         this.cl.close();
         JDTJavaCompilerFactory.getInstance().resetCache(this.cl);
      }

   }
}
