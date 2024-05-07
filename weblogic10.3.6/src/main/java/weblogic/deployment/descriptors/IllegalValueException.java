package weblogic.deployment.descriptors;

import weblogic.utils.NestedException;

public final class IllegalValueException extends NestedException {
   private static final long serialVersionUID = 834593634172907989L;

   public IllegalValueException() {
   }

   public IllegalValueException(String var1) {
      super(var1);
   }

   public IllegalValueException(Throwable var1) {
      super(var1);
   }

   public IllegalValueException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
