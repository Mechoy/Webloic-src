package weblogic.application.compiler;

import weblogic.application.compiler.flow.ApplicationViewerFlow;
import weblogic.application.compiler.flow.CheckLibraryReferenceFlow;
import weblogic.application.compiler.flow.CleanupModulesFlow;
import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.compiler.flow.CopyLibrariesFlow;
import weblogic.application.compiler.flow.CustomModuleFlow;
import weblogic.application.compiler.flow.CustomModuleProviderFlow;
import weblogic.application.compiler.flow.DescriptorParsingFlow;
import weblogic.application.compiler.flow.EarClassLoaderFlow;
import weblogic.application.compiler.flow.ExplodeModulesFlow;
import weblogic.application.compiler.flow.ImportLibrariesFlow;
import weblogic.application.compiler.flow.InitModulesFlow;
import weblogic.application.compiler.flow.LibraryDirectoryFlow;
import weblogic.application.compiler.flow.MergeModuleFlow;
import weblogic.application.compiler.flow.ModuleClassLoaderFlow;
import weblogic.application.compiler.flow.PrepareModulesFlow;
import weblogic.application.compiler.flow.VerifyOutputDirFlow;
import weblogic.application.compiler.flow.WriteDescriptorsFlow;
import weblogic.application.compiler.flow.WriteModulesFlow;
import weblogic.utils.compiler.ToolFailureException;

class EARMerger implements Merger {
   private final FlowDriver.CompilerFlowDriver driver;

   EARMerger(CompilerCtx var1) {
      CompilerFlow[] var2 = new CompilerFlow[]{new EarClassLoaderFlow(var1), new VerifyOutputDirFlow(var1), new DescriptorParsingFlow(var1), new ImportLibrariesFlow(var1), new LibraryDirectoryFlow(var1), new CheckLibraryReferenceFlow(var1, var1.verifyLibraryReferences()), new CustomModuleProviderFlow(var1), new InitModulesFlow(var1), new PrepareModulesFlow(var1), new CopyLibrariesFlow(var1), new ModuleClassLoaderFlow(var1), new CustomModuleFlow(var1), new ExplodeModulesFlow(var1, true), new CleanupModulesFlow(var1), new MergeModuleFlow(var1), new WriteModulesFlow(var1), new WriteDescriptorsFlow(var1), new ApplicationViewerFlow(var1)};
      this.driver = new FlowDriver.CompilerFlowDriver(var2);
   }

   public void merge() throws ToolFailureException {
      this.driver.compile();
   }

   public void cleanup() throws ToolFailureException {
      this.driver.cleanup();
   }
}
