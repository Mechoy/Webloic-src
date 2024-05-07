package weblogic.management.utils;

import weblogic.utils.NestedException;

public final class InvalidPasswordException extends NestedException {
   public InvalidPasswordException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public InvalidPasswordException(Throwable var1) {
      this("", var1);
   }

   public InvalidPasswordException(String var1) {
      this(var1, (Throwable)null);
   }
}
