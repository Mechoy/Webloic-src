package weblogic.ant.taskdefs.utils;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;

public class FindTask extends MatchingTask {
   private File targetFile;
   private File sourceDirectory;
   private String propertyName;
   private boolean matchAll = false;

   public void setTargetFile(File var1) {
      this.targetFile = var1;
   }

   public void setSourceDirectory(File var1) {
      this.sourceDirectory = var1;
   }

   public void setPropertyName(String var1) {
      this.propertyName = var1;
   }

   public void setMatchAll(boolean var1) {
      this.matchAll = var1;
   }

   public void execute() throws BuildException {
      Project var1 = this.getProject();

      try {
         StringBuffer var2 = new StringBuffer(128);
         DirectoryScanner var3 = this.getDirectoryScanner(this.sourceDirectory);
         var3.scan();
         long var4 = this.targetFile.lastModified();
         String[] var6 = var3.getIncludedFiles();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            File var8 = new File(this.sourceDirectory, var6[var7]);
            var1.log("Testing " + var8.getAbsolutePath(), 3);
            if (this.matchAll || var8.lastModified() > var4 && !var8.getCanonicalPath().equals(this.targetFile.getCanonicalPath())) {
               var1.log("Matched " + var8.getAbsolutePath(), 2);
               var2.append(" " + var8.getAbsolutePath());
            }
         }

         this.getProject().setProperty(this.propertyName, var2.toString());
      } catch (Exception var9) {
         var9.printStackTrace();
         throw new BuildException("Build generation failure", var9);
      }
   }
}
