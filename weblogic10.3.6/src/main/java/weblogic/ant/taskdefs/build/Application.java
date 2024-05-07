package weblogic.ant.taskdefs.build;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
import weblogic.ant.taskdefs.build.module.Module;
import weblogic.ant.taskdefs.build.module.ModuleFactory;
import weblogic.application.SplitDirectoryConstants;
import weblogic.utils.FileUtils;

public class Application implements SplitDirectoryConstants {
   private final BuildCtx ctx;
   private final Project project;
   private final File srcDir;
   private final File destDir;
   private static final String APPINF_CLASSES;
   private static final String APPINF_LIB;

   public Application(BuildCtx var1) {
      this.ctx = var1;
      this.project = var1.getProject();
      this.srcDir = var1.getSrcDir();
      this.destDir = var1.getDestDir();
   }

   public void build(Path var1, Set var2) throws BuildException {
      List[] var3 = ModuleFactory.createModules(this.ctx, this.srcDir.listFiles(FileUtils.DIR));

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4] != null) {
            this.compileModules(var3[var4], var1, var2);
         }
      }

   }

   protected void addToClasspath(Path var1, File var2) {
      if (var2.exists()) {
         Path.PathElement var3 = var1.createPathElement();
         var3.setLocation(var2);
      }

   }

   private void addAppInfLib(Path var1, File var2) {
      FileFilter var3 = FileUtils.makeExtensionFilter(".jar");
      File[] var4 = (new File(var2, APPINF_LIB)).listFiles(var3);
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            this.addToClasspath(var1, var4[var5]);
         }
      }

   }

   private void compileModules(List var1, Path var2, Set var3) throws BuildException {
      this.addToClasspath(var2, new File(this.srcDir, APPINF_CLASSES));
      this.addToClasspath(var2, new File(this.destDir, APPINF_CLASSES));
      this.addAppInfLib(var2, this.srcDir);
      this.addAppInfLib(var2, this.destDir);
      Iterator var4 = var1.iterator();

      Module var5;
      while(var4.hasNext()) {
         var5 = (Module)var4.next();
         var5.addToClasspath(var2);
      }

      var4 = var1.iterator();

      while(var4.hasNext()) {
         var5 = (Module)var4.next();
         if (var3.contains(var5.getSrcdir())) {
            var5.build(var2);
         }
      }

   }

   static {
      APPINF_CLASSES = File.separatorChar + "APP-INF" + File.separatorChar + "classes";
      APPINF_LIB = File.separatorChar + "APP-INF" + File.separatorChar + "lib";
   }
}
