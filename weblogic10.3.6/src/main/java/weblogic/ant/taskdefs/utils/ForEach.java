package weblogic.ant.taskdefs.utils;

import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Ant;

public class ForEach extends Ant {
   private String _values;
   private String _current = "current";
   private String _delimiter = ",";

   public void setDelimiter(String var1) {
      this._delimiter = var1;
   }

   public void setValues(String var1) {
      this._values = var1;
   }

   public void setCurrent(String var1) {
      this._current = var1;
   }

   public void execute() throws BuildException {
      if (this._values == null) {
         throw new BuildException("Values property is not set");
      } else {
         String var1 = this.getProject().replaceProperties(this._values);
         StringTokenizer var2 = new StringTokenizer(var1, this._delimiter);

         while(var2.hasMoreTokens()) {
            String var3 = var2.nextToken();
            this.getProject().setProperty(this._current, var3);
            super.execute();
         }

      }
   }
}
