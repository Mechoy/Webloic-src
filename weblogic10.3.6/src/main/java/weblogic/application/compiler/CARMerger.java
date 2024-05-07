package weblogic.application.compiler;

import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.compiler.flow.SingleModuleMergeFlow;
import weblogic.utils.compiler.ToolFailureException;

final class CARMerger implements Merger {
   private final FlowDriver.CompilerFlowDriver driver;

   public CARMerger(CompilerCtx var1) {
      CompilerFlow[] var2 = new CompilerFlow[]{new SingleModuleMergeFlow(var1)};
      this.driver = new FlowDriver.CompilerFlowDriver(var2);
   }

   public void merge() throws ToolFailureException {
      this.driver.compile();
   }

   public void cleanup() throws ToolFailureException {
      this.driver.cleanup();
   }
}
