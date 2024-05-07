package weblogic.ant.taskdefs.build;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.types.FileSet;

public class SearchAndBuildTask extends Task {
   private FileSet m_fileSet;
   private String m_target;

   public void execute() throws BuildException {
      String[] var1 = this.m_fileSet.getDirectoryScanner(this.project).getIncludedFiles();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         System.out.println("###################################################");
         System.out.println("currently executing build file: " + var1[var2]);
         Ant var3 = (Ant)this.project.createTask("ant");
         String var4 = var1[var2].substring(0, var1[var2].lastIndexOf(File.separatorChar));
         var4 = this.project.getBaseDir().getPath() + File.separatorChar + var4;
         File var5 = new File(var4);
         var3.setDir(var5);
         if (this.m_target != null) {
            var3.setTarget(this.m_target);
         }

         String var6 = var1[var2].substring(var1[var2].lastIndexOf(File.separatorChar) + 1);
         System.out.println("ant file: " + var6);
         var3.setAntfile(var6);
         var3.setInheritAll(false);
         var3.execute();
      }

   }

   public void setTarget(String var1) {
      this.m_target = var1;
   }

   public void addConfiguredFileSet(FileSet var1) {
      this.m_fileSet = var1;
   }
}
