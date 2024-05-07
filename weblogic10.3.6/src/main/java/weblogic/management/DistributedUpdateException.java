package weblogic.management;

import java.io.PrintStream;
import java.io.PrintWriter;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import weblogic.utils.NestedException;

public abstract class DistributedUpdateException extends NestedException {
   private ObjectName remoteName;
   private MBeanServer remoteMBeanServer;

   public DistributedUpdateException(ObjectName var1, MBeanServer var2, Exception var3) {
      super(var3);
      this.remoteName = var1;
      this.remoteMBeanServer = var2;
   }

   public ObjectName getRemoteName() {
      return this.remoteName;
   }

   public MBeanServer getRemoteMBeanServer() {
      return this.remoteMBeanServer;
   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public void printStackTrace(PrintStream var1) {
      this.printStackTrace(new PrintWriter(var1));
   }

   public void printStackTrace(PrintWriter var1) {
      this.printExceptionInfo(var1);
      var1.println("Distributed update exception");
      var1.println("- remote object: " + this.getRemoteName());
      var1.println("- remote server: " + this.getRemoteMBeanServer());
      super.printStackTrace(var1);
   }

   public void printExceptionInfo(PrintWriter var1) {
      var1.println("Distributed update exception");
      var1.println("- remote object: " + this.getRemoteName());
      var1.println("- remote server: " + this.getRemoteMBeanServer());
   }
}
