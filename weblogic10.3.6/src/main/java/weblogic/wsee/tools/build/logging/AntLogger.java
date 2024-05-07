package weblogic.wsee.tools.build.logging;

import org.apache.tools.ant.Task;

public class AntLogger implements LogEventListener {
   protected Task task;

   public AntLogger(Task var1) {
      this.task = var1;
   }

   public Task getTask() {
      return this.task;
   }

   public void log(EventSeverity var1, LogEvent var2) {
      assert var1 != null;

      assert var2 != null;

      byte var3 = 0;
      if (var1 == EventSeverity.DEBUG) {
         var3 = 4;
      } else if (var1 == EventSeverity.INFO) {
         var3 = 2;
      } else if (var1 == EventSeverity.WARNING) {
         var3 = 1;
      } else if (var1 == EventSeverity.ERROR) {
         var3 = 0;
      } else if (var1 == EventSeverity.VERBOSE) {
         var3 = 3;
      }

      this.task.log(var2.toString(), var3);
   }
}
