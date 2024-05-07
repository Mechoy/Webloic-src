package weblogic.management;

import weblogic.utils.NestedError;

public final class ManagementError extends NestedError {
   private static final long serialVersionUID = 7024545900215683004L;

   public ManagementError(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ManagementError(Throwable var1) {
      this("", var1);
   }

   public ManagementError(String var1) {
      this(var1, (Throwable)null);
   }
}
