package weblogic.management.utils;

import weblogic.utils.NestedException;

public final class InvalidCursorException extends NestedException {
   public InvalidCursorException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public InvalidCursorException(Throwable var1) {
      this("", var1);
   }

   public InvalidCursorException(String var1) {
      this(var1, (Throwable)null);
   }
}
