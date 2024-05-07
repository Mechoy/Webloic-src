package weblogic.application.compiler;

import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.compiler.flow.LightWeightDeploymentViewerFlow;
import weblogic.utils.compiler.ToolFailureException;

class LightWeightMerger implements Merger {
   private final FlowDriver.CompilerFlowDriver driver;

   LightWeightMerger(CompilerCtx var1) {
      CompilerFlow[] var2 = new CompilerFlow[]{new LightWeightDeploymentViewerFlow(var1)};
      this.driver = new FlowDriver.CompilerFlowDriver(var2);
   }

   public void merge() throws ToolFailureException {
      this.driver.compile();
   }

   public void cleanup() throws ToolFailureException {
      this.driver.cleanup();
   }
}
