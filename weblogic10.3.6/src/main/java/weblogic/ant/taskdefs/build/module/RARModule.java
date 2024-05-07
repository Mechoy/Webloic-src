package weblogic.ant.taskdefs.build.module;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import weblogic.ant.taskdefs.build.BuildCtx;

public final class RARModule extends Module {
   private final String srcDirPath;

   public RARModule(BuildCtx var1, File var2, File var3) throws BuildException {
      super(var1, var2, var3);

      try {
         this.srcDirPath = var2.getCanonicalPath();
      } catch (IOException var5) {
         throw new BuildException(var5);
      }
   }

   public void addToClasspath(Path var1) {
      this.addToClasspath(var1, this.destDir);
   }

   public void build(Path var1) throws BuildException {
      this.log("Compiling module: " + this.getClass().getName() + ": " + this.srcDir);
      this.destDir.mkdir();
      this.javac(var1, this.srcDir, this.destDir);
   }
}
