package weblogic.deployment.descriptors;

import weblogic.utils.NestedException;

public final class IllegalTypeException extends NestedException {
   private static final long serialVersionUID = -4660135151496284078L;

   public IllegalTypeException() {
   }

   public IllegalTypeException(String var1) {
      super(var1);
   }

   public IllegalTypeException(Throwable var1) {
      super(var1);
   }

   public IllegalTypeException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
