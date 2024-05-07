package weblogic.management.utils;

import weblogic.utils.NestedException;

public final class CreateException extends NestedException {
   public CreateException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public CreateException(Throwable var1) {
      this("", var1);
   }

   public CreateException(String var1) {
      this(var1, (Throwable)null);
   }
}
