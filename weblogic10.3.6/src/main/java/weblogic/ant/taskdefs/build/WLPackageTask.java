package weblogic.ant.taskdefs.build;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.types.FileSet;
import weblogic.application.SplitDirectoryConstants;

public final class WLPackageTask extends BaseTask implements SplitDirectoryConstants {
   private File toFile;
   private File toDir;
   private File srcDir;
   private File destDir;
   private List filesets = new ArrayList();

   public void setTofile(File var1) {
      this.toFile = var1;
   }

   public void setTodir(File var1) {
      this.toDir = var1;
   }

   public void setSrcdir(File var1) {
      this.srcDir = var1;
   }

   public void setDestdir(File var1) {
      this.destDir = var1;
   }

   public void addFileset(FileSet var1) {
      this.filesets.add(var1);
   }

   private void validateDir(String var1, File var2) throws BuildException {
      if (var2 == null) {
         throw new BuildException("Parameter " + var1 + " must be set.");
      } else if (!var2.exists()) {
         throw new BuildException(var1 + ": " + var2.getAbsolutePath() + " does not exist.");
      } else if (!var2.isDirectory()) {
         throw new BuildException(var1 + ": " + var2.getAbsolutePath() + " exists, but it is not a directory.");
      }
   }

   private void validateParameters() throws BuildException {
      if (this.toDir == null && this.toFile == null) {
         throw new BuildException("Either toFile or toDir must be set");
      } else if (this.toDir != null && this.toFile != null) {
         throw new BuildException("Either toFile or toDir cannot both be set");
      } else {
         if (this.toDir != null) {
            if (this.toDir.exists()) {
               if (!this.toDir.isDirectory()) {
                  throw new BuildException("toDir: " + this.toDir.getAbsolutePath() + " exists, but is not a directory");
               }
            } else if (!this.toDir.mkdirs()) {
               throw new BuildException("toDir: " + this.toDir.getAbsolutePath() + " does not exist, and we were unable to create it.");
            }
         }

         this.validateDir("srcdir", this.srcDir);
         this.validateDir("destdir", this.destDir);
      }
   }

   private FileSet[] buildFileSet() {
      if (this.filesets.size() > 0) {
         return (FileSet[])((FileSet[])this.filesets.toArray(new FileSet[this.filesets.size()]));
      } else {
         FileSet[] var1 = new FileSet[]{new FileSet(), null};
         var1[0].setDir(this.srcDir);
         var1[0].setExcludes("**/*.java build.xml .beabuild.txt");
         var1[1] = new FileSet();
         var1[1].setDir(this.destDir);
         var1[1].setExcludes("**/*.java .beabuild.txt");
         return var1;
      }
   }

   private void copy(FileSet[] var1, File var2) {
      Copy var3 = (Copy)this.project.createTask("copy");
      var3.setTodir(var2);
      var3.setIncludeEmptyDirs(false);
      var3.setPreserveLastModified(true);

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var3.addFileset(var1[var4]);
      }

      var3.execute();
   }

   private void jar(File var1, FileSet[] var2) {
      Jar var3 = (Jar)this.project.createTask("jar");
      var3.setDestFile(var1);
      File var4 = new File(this.srcDir, "META-INF/MANIFEST.MF");
      if (var4.exists()) {
         var3.setManifest(var4);
      }

      for(int var5 = 0; var5 < var2.length; ++var5) {
         var3.addFileset(var2[var5]);
      }

      var3.execute();
   }

   public void privateExecute() throws BuildException {
      this.validateParameters();
      FileSet[] var1 = this.buildFileSet();
      if (this.toFile != null) {
         this.jar(this.toFile, var1);
      } else {
         this.copy(var1, this.toDir);
      }

   }
}
