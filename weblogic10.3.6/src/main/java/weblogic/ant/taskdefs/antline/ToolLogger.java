package weblogic.ant.taskdefs.antline;

import java.io.PrintStream;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildLogger;

public class ToolLogger implements BuildLogger {
   private PrintStream out;
   private PrintStream err;
   private int msgOutputLevel;

   public ToolLogger() {
      this.out = System.out;
      this.err = System.err;
      this.msgOutputLevel = 2;
   }

   public void buildStarted(BuildEvent var1) {
      if (AntTool.debug) {
         System.out.println("build-start: " + var1);
      }

   }

   public void buildFinished(BuildEvent var1) {
      if (AntTool.debug) {
         System.out.println("build-finished: " + var1);
      }

      Throwable var2 = var1.getException();
      if (var2 != null) {
         if (this.msgOutputLevel < 3 && var2 instanceof BuildException) {
            if (var2 instanceof BuildException) {
               this.err.println(var2.toString());
            } else {
               this.err.println(var2.getMessage());
            }
         } else {
            var2.printStackTrace(this.err);
         }
      }

   }

   public void targetStarted(BuildEvent var1) {
      if (AntTool.debug) {
         System.out.println("target-started: " + var1);
      }

   }

   public void targetFinished(BuildEvent var1) {
      if (AntTool.debug) {
         System.out.println("target-finished: " + var1);
      }

   }

   public void taskStarted(BuildEvent var1) {
      if (AntTool.debug) {
         System.out.println("task-started: " + var1);
      }

   }

   public void taskFinished(BuildEvent var1) {
      if (AntTool.debug) {
         System.out.println("task-finished: " + var1);
      }

   }

   public void messageLogged(BuildEvent var1) {
      PrintStream var2 = var1.getPriority() == 0 ? this.err : this.out;
      if (var1.getPriority() <= this.msgOutputLevel && var1.getTask() != null) {
         var2.println(var1.getMessage());
      }

   }

   public void setMessageOutputLevel(int var1) {
      if (AntTool.debug) {
         System.out.println("set-message-output-level: " + var1);
      }

      if (var1 >= 2) {
         this.msgOutputLevel = var1;
      }

   }

   public void setOutputPrintStream(PrintStream var1) {
      if (AntTool.debug) {
         System.out.println("set-output-print-stream: " + var1.getClass().getName());
      }

      this.out = var1;
   }

   public void setErrorPrintStream(PrintStream var1) {
      if (AntTool.debug) {
         System.out.println("set-error-print-stream: " + var1.getClass().getName());
      }

      this.err = var1;
   }

   public void setEmacsMode(boolean var1) {
      if (AntTool.debug) {
         System.out.println("set-emacs-mode: " + var1);
      }

   }
}
