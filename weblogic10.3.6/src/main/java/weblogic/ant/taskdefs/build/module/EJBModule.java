package weblogic.ant.taskdefs.build.module;

import com.bea.wls.ejbgen.ant.EJBGenAntTask;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Move;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import weblogic.ant.taskdefs.build.BuildCtx;
import weblogic.utils.FileUtils;

public final class EJBModule extends Module {
   private final String srcDirPath;
   private final File[] ejbGenFiles;
   private static final String BEA_EJBGEN_NAME = "weblogic.ant.taskdefs.build.module.EJBModule.ejbgen";

   public EJBModule(BuildCtx var1, File var2, File var3, File[] var4) throws BuildException {
      super(var1, var2, var3);
      this.ejbGenFiles = var4;

      try {
         this.srcDirPath = var2.getCanonicalPath();
      } catch (IOException var6) {
         throw new BuildException(var6);
      }
   }

   public void addToClasspath(Path var1) {
      this.addToClasspath(var1, this.destDir);
   }

   private File getDestFile(File var1) throws IOException {
      String var2 = var1.getCanonicalPath();
      String var3 = var2.substring(this.srcDirPath.length());
      return new File(this.destDir, var3.substring(0, var3.length() - 3) + "java");
   }

   private EJBGenAntTask findOrCreateEJBGen() {
      EJBGenAntTask var1 = this.ctx.getEJBGen();
      if (var1 != null) {
         return (EJBGenAntTask)var1.clone();
      } else {
         Hashtable var2 = this.project.getTaskDefinitions();
         if (!var2.contains("weblogic.ant.taskdefs.build.module.EJBModule.ejbgen")) {
            this.project.addTaskDefinition("weblogic.ant.taskdefs.build.module.EJBModule.ejbgen", EJBGenAntTask.class);
         }

         var1 = (EJBGenAntTask)this.project.createTask("weblogic.ant.taskdefs.build.module.EJBModule.ejbgen");
         var1.setTaskName("ejbgen");
         return var1;
      }
   }

   private void ejbGen(Path var1) throws BuildException {
      for(int var2 = 0; var2 < this.ejbGenFiles.length; ++var2) {
         try {
            File var3 = this.getDestFile(this.ejbGenFiles[var2]);
            FileUtils.copy(this.ejbGenFiles[var2], var3);
         } catch (IOException var8) {
            throw new BuildException(var8);
         }
      }

      EJBGenAntTask var9 = this.findOrCreateEJBGen();
      Path var10 = var9.createClasspath();
      var10.append(var1);
      this.addToClasspath(var10, this.srcDir);
      if (var9.getOutputDir() == null) {
         var9.setOutputDir(this.destDir);
      }

      if (var9.getFileSet() == null) {
         FileSet var4 = new FileSet();
         var4.setDir(this.srcDir);
         var4.setIncludes("**/*.ejb");
         var9.addConfiguredFileSet(var4);
      }

      var9.setFork(true);
      var9.execute();
      File var11 = new File(this.destDir, "META-INF");
      var11.mkdir();
      Move var5 = (Move)this.project.createTask("move");
      var5.setTodir(var11);
      FileSet var6 = new FileSet();
      StringBuffer var7 = new StringBuffer();
      var7.append("ejb-jar.xml");
      if ((new File(this.destDir, "weblogic-ejb-jar.xml")).exists()) {
         var7.append(", weblogic-ejb-jar.xml");
      }

      if ((new File(this.destDir, "weblogic-cmp-rdbms-jar.xml")).exists()) {
         var7.append(", weblogic-cmp-rdbms-jar.xml");
      }

      var6.setDir(this.destDir);
      var6.setIncludes(var7.toString());
      var5.addFileset(var6);
      var5.execute();
   }

   public void build(Path var1) throws BuildException {
      this.log("Compiling module: " + this.getClass().getName() + ": " + this.srcDir);
      if (this.ejbGenFiles != null && this.ejbGenFiles.length > 0) {
         this.ejbGen(var1);
         this.javac(var1, this.destDir, this.destDir, this.srcDir);
      }

      this.javac(var1, this.srcDir, this.destDir);
   }
}
