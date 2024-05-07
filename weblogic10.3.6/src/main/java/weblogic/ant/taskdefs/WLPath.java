package weblogic.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import weblogic.Home;

public class WLPath extends Task {
   private String path = null;
   private String property = null;

   public void setPath(String var1) {
      this.path = var1;
   }

   public void setProperty(String var1) {
      this.property = var1;
   }

   public void execute() throws BuildException {
      File var1 = Home.getFile();
      if (!var1.isDirectory()) {
         throw new BuildException("Invalid WebLogic Home: " + var1.getPath());
      } else {
         File var2 = this.path != null ? new File(var1, this.path) : var1;
         if (this.property != null && var2.exists()) {
            this.getProject().setProperty(this.property, var2.getPath());
         }

      }
   }
}
