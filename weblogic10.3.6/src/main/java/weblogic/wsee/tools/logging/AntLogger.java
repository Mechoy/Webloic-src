package weblogic.wsee.tools.logging;

import java.io.File;
import org.apache.tools.ant.Task;

public class AntLogger implements Logger {
   protected Task task;

   public AntLogger(Task var1) {
      this.task = var1;
   }

   public Task getTask() {
      return this.task;
   }

   public void log(EventLevel var1, LogEvent var2) {
      assert var1 != null;

      assert var2 != null;

      this.log(var1, this.getMessage(var1, var2));
   }

   public void log(EventLevel var1, String var2) {
      byte var3 = 0;
      switch (var1) {
         case DEBUG:
            var3 = 4;
            break;
         case INFO:
            var3 = 2;
            break;
         case WARNING:
            var3 = 1;
            break;
         case ERROR:
            var3 = 0;
            break;
         case VERBOSE:
            var3 = 3;
      }

      this.task.log(var2, var3);
   }

   private String getMessage(EventLevel var1, LogEvent var2) {
      StringBuffer var3 = new StringBuffer();
      if (var2.getSourceURI() != null) {
         File var4 = new File(var2.getSourceURI());
         var3.append(var4.getAbsolutePath());
         var3.append(' ');
         var3.append(var2.getLine());
         var3.append(':');
         var3.append(var2.getColumn());
      }

      var3.append('\n');
      var3.append('[');
      var3.append(var1);
      var3.append("] - ");
      return var3.toString() + var2.getText();
   }
}
