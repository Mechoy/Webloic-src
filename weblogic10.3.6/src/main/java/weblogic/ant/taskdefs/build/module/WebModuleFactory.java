package weblogic.ant.taskdefs.build.module;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import weblogic.ant.taskdefs.build.BuildCtx;
import weblogic.servlet.utils.WarUtils;

public final class WebModuleFactory extends ModuleFactory {
   Module claim(BuildCtx var1, File var2, File var3) throws BuildException {
      try {
         if ((new File(var2, "WEB-INF")).exists() || WarUtils.isWar(var2)) {
            return new WebModule(var1, var2, var3);
         }
      } catch (IOException var5) {
      }

      return null;
   }
}
