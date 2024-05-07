package weblogic.ant.taskdefs.path;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

public class PathModTask extends Task {
   private String m_oldPathPrefix;
   private String m_newPathPrefix;
   private String m_propertyName;
   private String m_inputPath;

   public void setOldPathPrefix(String var1) {
      this.m_oldPathPrefix = var1;
      if (System.getProperty("os.name").indexOf("Windows") > -1) {
         this.m_oldPathPrefix = this.m_oldPathPrefix.replace('/', File.separatorChar);
      }

   }

   public void setNewPathPrefix(String var1) {
      this.m_newPathPrefix = var1;
      if (System.getProperty("os.name").indexOf("Windows") > -1) {
         this.m_newPathPrefix = this.m_newPathPrefix.replace('/', File.separatorChar);
      }

   }

   public void setPropertyName(String var1) {
      this.m_propertyName = var1;
   }

   public void setInputPath(String var1) {
      this.m_inputPath = var1;
   }

   public void execute() throws BuildException {
      Project var1 = this.getProject();
      String var2 = "";
      System.out.println("input source path - " + this.m_inputPath);
      boolean var3 = this.m_inputPath.regionMatches(true, 0, this.m_oldPathPrefix, 0, this.m_oldPathPrefix.length());
      if (var3) {
         String var4 = this.m_inputPath.substring(this.m_oldPathPrefix.length());
         var2 = this.m_newPathPrefix + var4;
         var2 = var2.replace(File.separatorChar, '/');
         var1.setUserProperty(this.m_propertyName, var2);
         System.out.println("output build path - " + var2);
      } else {
         throw new BuildException("Unable to match old path prefix: " + this.m_oldPathPrefix + " to input path: " + this.m_inputPath);
      }
   }
}
