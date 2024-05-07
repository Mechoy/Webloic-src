package weblogic.common;

import weblogic.utils.NestedException;

public class ResourceException extends NestedException {
   private static final long serialVersionUID = -7448966506307907440L;

   public ResourceException(String var1) {
      super(var1);
   }

   public ResourceException() {
      this((String)null, (Throwable)null);
   }

   public ResourceException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ResourceException(Throwable var1) {
      super(var1);
   }
}
