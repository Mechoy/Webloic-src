package weblogic.auddi.util;

import weblogic.auddi.NestedException;

public class ShutdownException extends NestedException {
   public ShutdownException(Exception var1, String var2) {
      super(var1, var2);
   }

   public ShutdownException(Exception var1) {
      super((Throwable)var1);
   }
}
