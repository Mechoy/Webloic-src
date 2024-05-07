package weblogic.management.utils;

import weblogic.utils.NestedException;

public final class RemoveException extends NestedException {
   public RemoveException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public RemoveException(Throwable var1) {
      this("", var1);
   }

   public RemoveException(String var1) {
      this(var1, (Throwable)null);
   }
}
