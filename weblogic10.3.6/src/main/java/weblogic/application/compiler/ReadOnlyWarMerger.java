package weblogic.application.compiler;

import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.compiler.flow.ModuleViewerFlow;
import weblogic.application.compiler.flow.SingleModuleMergeFlow;
import weblogic.utils.compiler.ToolFailureException;

public class ReadOnlyWarMerger implements Merger {
   private final FlowDriver.CompilerFlowDriver driver;

   ReadOnlyWarMerger(CompilerCtx var1) {
      this.driver = new FlowDriver.CompilerFlowDriver(this.getFlows(var1));
   }

   protected CompilerFlow[] getFlows(CompilerCtx var1) {
      return new CompilerFlow[]{new SingleModuleMergeFlow(var1), new ModuleViewerFlow(var1)};
   }

   public void merge() throws ToolFailureException {
      this.driver.compile();
   }

   public void cleanup() throws ToolFailureException {
      this.driver.cleanup();
   }
}
