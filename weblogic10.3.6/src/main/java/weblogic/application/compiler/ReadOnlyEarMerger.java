package weblogic.application.compiler;

import weblogic.application.compiler.flow.ApplicationViewerFlow;
import weblogic.application.compiler.flow.CheckLibraryReferenceFlow;
import weblogic.application.compiler.flow.CleanupModulesFlow;
import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.compiler.flow.CustomModuleFlow;
import weblogic.application.compiler.flow.CustomModuleProviderFlow;
import weblogic.application.compiler.flow.DescriptorParsingFlow;
import weblogic.application.compiler.flow.EarClassLoaderFlow;
import weblogic.application.compiler.flow.ImportLibrariesFlow;
import weblogic.application.compiler.flow.InitModulesFlow;
import weblogic.application.compiler.flow.LibraryDirectoryFlow;
import weblogic.application.compiler.flow.MergeModuleFlow;
import weblogic.application.compiler.flow.ModuleClassLoaderFlow;
import weblogic.application.compiler.flow.PrepareModulesFlow;
import weblogic.utils.compiler.ToolFailureException;

public class ReadOnlyEarMerger implements Merger {
   private final FlowDriver.CompilerFlowDriver driver;

   ReadOnlyEarMerger(CompilerCtx var1) {
      this.driver = new FlowDriver.CompilerFlowDriver(this.getFlows(var1));
   }

   protected CompilerFlow[] getFlows(CompilerCtx var1) {
      return new CompilerFlow[]{new EarClassLoaderFlow(var1), new DescriptorParsingFlow(var1), new ImportLibrariesFlow(var1), new LibraryDirectoryFlow(var1), new CheckLibraryReferenceFlow(var1, var1.verifyLibraryReferences()), new CustomModuleProviderFlow(var1), new InitModulesFlow(var1), new PrepareModulesFlow(var1), new ModuleClassLoaderFlow(var1), new CustomModuleFlow(var1), new CleanupModulesFlow(var1), new MergeModuleFlow(var1), new ApplicationViewerFlow(var1)};
   }

   public void merge() throws ToolFailureException {
      this.driver.compile();
   }

   public void cleanup() throws ToolFailureException {
      this.driver.cleanup();
   }
}
