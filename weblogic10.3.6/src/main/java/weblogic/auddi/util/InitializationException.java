package weblogic.auddi.util;

import weblogic.auddi.NestedException;

public class InitializationException extends NestedException {
   public InitializationException(Exception var1, String var2) {
      super(var1, var2);
   }

   public InitializationException(Exception var1) {
      super((Throwable)var1);
   }

   public InitializationException(String var1) {
      super((Throwable)null, var1);
   }
}
