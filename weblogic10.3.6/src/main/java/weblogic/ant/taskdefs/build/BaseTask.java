package weblogic.ant.taskdefs.build;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

abstract class BaseTask extends Task {
   protected abstract void privateExecute() throws BuildException;

   public final void execute() throws BuildException {
      Thread var1 = Thread.currentThread();
      ClassLoader var2 = var1.getContextClassLoader();
      ClassLoader var3 = this.getClass().getClassLoader();

      try {
         var1.setContextClassLoader(var3);
         this.privateExecute();
      } finally {
         var1.setContextClassLoader(var2);
      }

   }
}
