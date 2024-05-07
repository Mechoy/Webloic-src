package weblogic.management.utils;

import weblogic.utils.NestedException;

public final class AlreadyExistsException extends NestedException {
   public AlreadyExistsException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public AlreadyExistsException(Throwable var1) {
      this("", var1);
   }

   public AlreadyExistsException(String var1) {
      this(var1, (Throwable)null);
   }
}
