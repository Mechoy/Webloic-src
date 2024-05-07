package weblogic.management.utils;

import weblogic.utils.NestedException;

public final class InvalidParameterException extends NestedException {
   public InvalidParameterException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public InvalidParameterException(Throwable var1) {
      this("", var1);
   }

   public InvalidParameterException(String var1) {
      this(var1, (Throwable)null);
   }
}
