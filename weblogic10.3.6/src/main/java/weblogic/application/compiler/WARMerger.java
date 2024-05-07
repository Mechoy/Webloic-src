package weblogic.application.compiler;

import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.compiler.flow.ModuleViewerFlow;
import weblogic.application.compiler.flow.SingleModuleMergeFlow;
import weblogic.application.compiler.flow.WriteDescriptorsFlow;
import weblogic.application.compiler.flow.WriteModulesFlow;
import weblogic.utils.compiler.ToolFailureException;

final class WARMerger implements Merger {
   private final FlowDriver.CompilerFlowDriver driver;

   public WARMerger(CompilerCtx var1) {
      CompilerFlow[] var2 = new CompilerFlow[]{new SingleModuleMergeFlow(var1), new WriteModulesFlow(var1), new WriteDescriptorsFlow(var1), new ModuleViewerFlow(var1)};
      this.driver = new FlowDriver.CompilerFlowDriver(var2);
   }

   public void merge() throws ToolFailureException {
      this.driver.compile();
   }

   public void cleanup() throws ToolFailureException {
      this.driver.cleanup();
   }
}
