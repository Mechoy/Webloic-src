package weblogic.management;

import weblogic.utils.NestedException;

/** @deprecated */
public final class NoAccessException extends NestedException {
   private static final long serialVersionUID = 6933385751528674427L;

   public NoAccessException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public NoAccessException(Throwable var1) {
      this("", var1);
   }

   public NoAccessException(String var1) {
      this(var1, (Throwable)null);
   }

   public NoAccessException(String var1, String var2, String var3) {
      this(var1, var2, var3, (Throwable)null);
   }

   public NoAccessException(String var1, String var2, String var3, Throwable var4) {
      this("User " + var1 + " does not have " + var2 + " permission " + " on " + var3, var4);
   }
}
