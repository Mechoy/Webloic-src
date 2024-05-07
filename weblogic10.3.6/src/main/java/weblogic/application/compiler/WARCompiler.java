package weblogic.application.compiler;

import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.compiler.flow.ManifestFlow;
import weblogic.application.compiler.flow.ModuleClassEnhanceFlow;
import weblogic.application.compiler.flow.SingleModuleCompileFlow;
import weblogic.utils.compiler.ToolFailureException;

final class WARCompiler implements Compiler {
   private final CompilerFlow[] flow;

   WARCompiler(CompilerCtx var1) {
      this.flow = new CompilerFlow[]{new SingleModuleCompileFlow(var1), new ModuleClassEnhanceFlow(var1), new ManifestFlow(var1)};
   }

   public void compile() throws ToolFailureException {
      (new FlowDriver()).run(this.flow);
   }
}
