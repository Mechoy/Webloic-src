package weblogic.ant.taskdefs.build.module;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import weblogic.ant.taskdefs.build.BuildCtx;

public final class JavaModule extends Module {
   private static final String APPINF_CLASSES;

   public JavaModule(BuildCtx var1, File var2, File var3) {
      super(var1, var2, var3);
   }

   public void addToClasspath(Path var1) {
      this.addToClasspath(var1, new File(this.destDir, APPINF_CLASSES));
   }

   public void build(Path var1) throws BuildException {
      this.log("Compiling module: " + this.getClass().getName() + ": " + this.srcDir);
      File var2 = this.destDir.getParentFile();
      if (var2 == null) {
         var2 = this.destDir;
      }

      this.javac(var1, this.srcDir, new File(var2, APPINF_CLASSES));
   }

   static {
      APPINF_CLASSES = File.separatorChar + "APP-INF" + File.separatorChar + "classes";
   }
}
