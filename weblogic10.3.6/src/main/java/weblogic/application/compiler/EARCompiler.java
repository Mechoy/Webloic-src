package weblogic.application.compiler;

import weblogic.application.compiler.flow.CheckLibraryReferenceFlow;
import weblogic.application.compiler.flow.CleanupModulesFlow;
import weblogic.application.compiler.flow.CompileModuleFlow;
import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.compiler.flow.DescriptorParsingFlow;
import weblogic.application.compiler.flow.EarClassLoaderFlow;
import weblogic.application.compiler.flow.ExplodeModulesFlow;
import weblogic.application.compiler.flow.ImportLibrariesFlow;
import weblogic.application.compiler.flow.InitModulesFlow;
import weblogic.application.compiler.flow.LibraryDirectoryFlow;
import weblogic.application.compiler.flow.ManifestFlow;
import weblogic.application.compiler.flow.ModuleClassEnhanceFlow;
import weblogic.application.compiler.flow.ModuleClassLoaderFlow;
import weblogic.application.compiler.flow.PrepareModulesFlow;
import weblogic.application.compiler.flow.WriteInferredDescriptorFlow;
import weblogic.utils.compiler.ToolFailureException;

public final class EARCompiler implements Compiler {
   private final CompilerFlow[] flow;

   EARCompiler(CompilerCtx var1) {
      this.flow = new CompilerFlow[]{new EarClassLoaderFlow(var1), new DescriptorParsingFlow(var1), new ImportLibrariesFlow(var1), new LibraryDirectoryFlow(var1), new CheckLibraryReferenceFlow(var1, false), new InitModulesFlow(var1), new PrepareModulesFlow(var1), new ExplodeModulesFlow(var1), new CleanupModulesFlow(var1), new ModuleClassLoaderFlow(var1), new CompileModuleFlow(var1), new WriteInferredDescriptorFlow(var1), new ManifestFlow(var1), new ModuleClassEnhanceFlow(var1)};
   }

   public void compile() throws ToolFailureException {
      (new FlowDriver()).run(this.flow);
   }
}
