package weblogic.application.compiler;

import weblogic.application.compiler.flow.ApplicationViewerFlow;
import weblogic.application.compiler.flow.CleanupModulesFlow;
import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.compiler.flow.CustomModuleFlow;
import weblogic.application.compiler.flow.CustomModuleProviderFlow;
import weblogic.application.compiler.flow.DescriptorParsingFlow;
import weblogic.application.compiler.flow.EarClassLoaderFlow;
import weblogic.application.compiler.flow.InitModulesFlow;
import weblogic.application.compiler.flow.MergeModuleFlow;
import weblogic.application.compiler.flow.ModuleClassLoaderFlow;
import weblogic.application.compiler.flow.PrepareModulesFlow;

public class EarReader extends ReadOnlyEarMerger implements Merger {
   public EarReader(CompilerCtx var1) {
      super(var1);
   }

   protected CompilerFlow[] getFlows(CompilerCtx var1) {
      return new CompilerFlow[]{new EarClassLoaderFlow(var1), new DescriptorParsingFlow(var1), new CustomModuleProviderFlow(var1), new InitModulesFlow(var1), new PrepareModulesFlow(var1), new ModuleClassLoaderFlow(var1), new CustomModuleFlow(var1), new CleanupModulesFlow(var1), new MergeModuleFlow(var1), new ApplicationViewerFlow(var1)};
   }
}
