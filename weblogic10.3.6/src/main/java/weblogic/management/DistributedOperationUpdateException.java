package weblogic.management;

import java.io.PrintWriter;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/** @deprecated */
public final class DistributedOperationUpdateException extends DistributedUpdateException {
   private static final long serialVersionUID = -1592802979693240641L;
   private String actionName;
   private Object[] params;
   private String[] signature;

   public DistributedOperationUpdateException(ObjectName var1, MBeanServer var2, Exception var3, String var4, Object[] var5, String[] var6) {
      super(var1, var2, var3);
      this.actionName = var4;
      this.params = var5;
      this.signature = var6;
   }

   public String getActionName() {
      return this.actionName;
   }

   public Object[] getParams() {
      return this.params;
   }

   public String[] getSignature() {
      return this.signature;
   }

   public void printExceptionInfo(PrintWriter var1) {
      super.printExceptionInfo(var1);
      var1.println("- actionName: " + this.getActionName());
      var1.println("- params: " + this.getParams());
      var1.println("- signature: " + this.getSignature());
   }
}
