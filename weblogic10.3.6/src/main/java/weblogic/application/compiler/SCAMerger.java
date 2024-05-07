package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;
import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.compiler.flow.SCACompositeViewerFlow;
import weblogic.application.utils.IOUtils;
import weblogic.application.utils.ModuleDiscovery;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

final class SCAMerger extends EarReader {
   private final FlowDriver.CompilerFlowDriver driver;
   private final CompilerCtx ctx;

   SCAMerger(CompilerCtx var1) {
      super(var1);
      this.ctx = var1;
      CompilerFlow[] var2 = new CompilerFlow[]{new SCACompositeViewerFlow(var1)};
      this.driver = new FlowDriver.CompilerFlowDriver(var2);
   }

   public void merge() throws ToolFailureException {
      if (this.containsJ2EEModules()) {
         super.merge();
      }

      this.driver.compile();
   }

   private boolean containsJ2EEModules() throws ToolFailureException {
      Getopt2 var1 = this.ctx.getOpts();
      String[] var2 = var1.args();
      if (var2.length != 1) {
         throw new IllegalArgumentException("more than one argument specified!");
      } else {
         try {
            File var3 = (new File(var2[0])).getCanonicalFile();
            this.ctx.setSourceFile(var3);
            VirtualJarFile var4 = VirtualJarFactory.createVirtualJar(var3);
            this.ctx.setVSource(var4);
            ApplicationBean var5 = ModuleDiscovery.discoverModules(this.ctx.getVSource());
            return var5 != null;
         } catch (IOException var6) {
            throw new ToolFailureException("Error processing source " + var2[0], var6);
         }
      }
   }

   public void cleanup() throws ToolFailureException {
      try {
         super.cleanup();
         this.driver.cleanup();
      } finally {
         IOUtils.forceClose(this.ctx.getVSource());
      }

   }
}
