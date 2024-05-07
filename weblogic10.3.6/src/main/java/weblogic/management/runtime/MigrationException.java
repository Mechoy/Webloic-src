package weblogic.management.runtime;

import weblogic.utils.NestedException;

public final class MigrationException extends NestedException {
   public MigrationException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public MigrationException(Throwable var1) {
      this("", var1);
   }

   public MigrationException(String var1) {
      this(var1, (Throwable)null);
   }
}
