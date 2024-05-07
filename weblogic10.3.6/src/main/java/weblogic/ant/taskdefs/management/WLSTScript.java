package weblogic.ant.taskdefs.management;

import org.apache.tools.ant.Task;

public class WLSTScript extends Task {
   private String line = null;
   private boolean replaceProperties = false;

   public void addText(String var1) {
      this.line = var1;
   }

   public void setReplaceProperties(boolean var1) {
      this.replaceProperties = var1;
   }

   public String getScript() {
      return this.replaceProperties ? this.getProject().replaceProperties(this.line) : this.line;
   }
}
