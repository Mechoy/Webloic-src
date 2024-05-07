package weblogic.cluster.migration;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class GroupActivationException extends MigrationException {
   private List causes = new ArrayList();

   public GroupActivationException() {
      super("");
   }

   public GroupActivationException(String var1) {
      super(var1);
   }

   public void addCause(Exception var1) {
      this.causes.add(var1);
   }

   public List getCauses() {
      return this.causes;
   }

   public void printStackTrace(PrintStream var1) {
      Iterator var2 = this.causes.iterator();

      while(var2.hasNext()) {
         ((Throwable)var2.next()).printStackTrace(var1);
      }

   }

   public void printStackTrace(PrintWriter var1) {
      Iterator var2 = this.causes.iterator();

      while(var2.hasNext()) {
         ((Throwable)var2.next()).printStackTrace(var1);
      }

   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public String getMessage() {
      StringBuffer var1 = new StringBuffer(super.getMessage());
      Iterator var2 = this.causes.iterator();

      while(var2.hasNext()) {
         var1.append("cause is: " + ((Exception)var2.next()).getMessage());
      }

      return var1.toString();
   }
}
