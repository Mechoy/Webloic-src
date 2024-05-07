package weblogic.ant.taskdefs.build.module;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;
import weblogic.ant.taskdefs.build.BuildCtx;

public abstract class Module {
   protected BuildCtx ctx;
   protected Project project;
   protected final File srcDir;
   protected final File destDir;

   public Module(BuildCtx var1, File var2, File var3) {
      this.ctx = var1;
      this.project = var1.getProject();
      this.srcDir = var2;
      this.destDir = var3;
   }

   public File getSrcdir() {
      return this.srcDir;
   }

   public File getDestdir() {
      return this.destDir;
   }

   public abstract void addToClasspath(Path var1);

   public abstract void build(Path var1) throws BuildException;

   protected void addToClasspath(Path var1, File var2) {
      Path.PathElement var3 = var1.createPathElement();
      var3.setLocation(var2);
   }

   protected void log(String var1) {
      this.project.log(var1, 3);
   }

   protected void javac(Path var1, File var2, File var3) {
      this.javac(var1, var2, var3, (File)null);
   }

   protected void javac(Path var1, File var2, File var3, File var4) {
      this.log("javac javacDir: " + var2 + " outDir: " + var3);
      Javac var5 = this.ctx.getJavacTask();
      Javac var6 = (Javac)this.project.createTask("javac");
      if (var5 != null) {
         JavacCloner.getJavaCloner().copy(var5, var6);
      }

      var6.setSrcdir(new Path(this.project, var2.getAbsolutePath()));
      var3.mkdirs();
      var6.setDestdir(var3);
      var6.setClasspath(var1);
      if (var4 != null) {
         var6.setSourcepath(new Path(this.project, var4.getAbsolutePath()));
      }

      var6.execute();
   }
}
