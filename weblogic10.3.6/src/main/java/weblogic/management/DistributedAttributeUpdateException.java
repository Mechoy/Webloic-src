package weblogic.management;

import java.io.PrintWriter;
import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/** @deprecated */
public final class DistributedAttributeUpdateException extends DistributedUpdateException {
   private static final long serialVersionUID = -3780351746236507273L;
   private static final String NONE = "none specified";
   private Attribute attribute;

   public DistributedAttributeUpdateException(ObjectName var1, MBeanServer var2, Exception var3, Attribute var4) {
      super(var1, var2, var3);
      this.attribute = var4;
   }

   public Attribute getAttribute() {
      return this.attribute;
   }

   public void printExceptionInfo(PrintWriter var1) {
      super.printExceptionInfo(var1);
      String var2 = "none specified";
      if (this.getAttribute() != null) {
         var2 = this.getAttribute().getName();
      }

      var1.println("- attribute: " + var2);
   }
}
