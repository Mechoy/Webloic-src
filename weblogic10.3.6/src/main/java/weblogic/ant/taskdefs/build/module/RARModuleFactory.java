package weblogic.ant.taskdefs.build.module;

import java.io.File;
import org.apache.tools.ant.BuildException;
import weblogic.ant.taskdefs.build.BuildCtx;
import weblogic.connector.utils.RarUtils;

public final class RARModuleFactory extends ModuleFactory {
   Module claim(BuildCtx var1, File var2, File var3) throws BuildException {
      boolean var4 = RarUtils.isRar(var2);
      return var4 ? new RARModule(var1, var2, var3) : null;
   }
}
