package weblogic.application.compiler.flow;

import java.io.File;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.compiler.AppcUtils;
import weblogic.application.compiler.CompilerCtx;
import weblogic.j2ee.J2EELogger;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFile;

public final class DescriptorParsingFlow extends CompilerFlow {
   public DescriptorParsingFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      Getopt2 var1 = this.ctx.getOpts();
      VirtualJarFile var2 = this.ctx.getVSource();
      File var3 = null;
      if (var1.hasOption("altappdd")) {
         var3 = new File(var1.getOption("altappdd"));
         if (!var3.exists()) {
            throw new ToolFailureException(J2EELogger.logAppcMissingApplicationAltDDFileLoggable(var3.getPath()).getMessage());
         }
      }

      File var4 = null;
      if (var1.hasOption("altwlsappdd")) {
         var4 = new File(var1.getOption("altwlsappdd"));
         if (!var4.exists()) {
            throw new ToolFailureException(J2EELogger.logAppcMissingApplicationAltDDFileLoggable(var4.getPath()).getMessage());
         }
      }

      ApplicationDescriptor var5 = new ApplicationDescriptor(var2, this.ctx.getConfigDir(), this.ctx.getPlanBean(), this.ctx.getSourceName());
      AppcUtils.setDDs(var5, this.ctx);
      if (this.ctx.getApplicationDD() == null) {
         throw new ToolFailureException(J2EELogger.logAppcNoApplicationDDFoundLoggable(var2.getName()).getMessage());
      }
   }

   public void cleanup() {
   }
}
