package weblogic.deployment;

import weblogic.utils.NestedException;

public final class EnvironmentException extends NestedException {
   private static final long serialVersionUID = 5585967321340200145L;

   public EnvironmentException() {
   }

   public EnvironmentException(String var1) {
      super(var1);
   }

   public EnvironmentException(Throwable var1) {
      super(var1);
   }

   public EnvironmentException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
