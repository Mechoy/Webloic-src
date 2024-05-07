package weblogic.security;

import weblogic.utils.NestedRuntimeException;

public class SecurityInitializationException extends NestedRuntimeException {
   public SecurityInitializationException() {
   }

   public SecurityInitializationException(String var1) {
      super(var1);
   }

   public SecurityInitializationException(Throwable var1) {
      super(var1);
   }

   public SecurityInitializationException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
