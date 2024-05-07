package weblogic.application.compiler.flow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import weblogic.application.compiler.CompilerCtx;
import weblogic.deploy.internal.DeploymentPlanDescriptorLoader;
import weblogic.j2ee.J2EELogger;
import weblogic.utils.compiler.ToolFailureException;

public final class InitPlanFlow extends CompilerFlow {
   private File pf;

   public InitPlanFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      this.pf = this.ctx.getPlanFile();
      if (this.pf != null) {
         try {
            DeploymentPlanDescriptorLoader var1 = new DeploymentPlanDescriptorLoader(this.pf);
            this.ctx.setPlanBean(var1.getDeploymentPlanBean());
            String var2 = this.ctx.getPlanBean().getConfigRoot();
            if (this.ctx.getOpts().hasOption("plandir")) {
               var2 = this.ctx.getOpts().getOption("plandir");
            }

            if (var2 != null) {
               this.ctx.setConfigDir(new File(var2));
            }

         } catch (FileNotFoundException var3) {
            throw new ToolFailureException(J2EELogger.logAppcPlanFileNotAccessibleLoggable(this.pf.getPath(), var3.getMessage()).getMessage(), var3);
         } catch (IOException var4) {
            throw new ToolFailureException(J2EELogger.logAppcPlanParseErrorLoggable(this.pf.getPath(), var4.getMessage()).getMessage(), var4);
         } catch (XMLStreamException var5) {
            throw new ToolFailureException(J2EELogger.logAppcPlanParseErrorLoggable(this.pf.getPath(), var5.getMessage()).getMessage(), var5);
         }
      }
   }

   public void cleanup() {
   }
}
