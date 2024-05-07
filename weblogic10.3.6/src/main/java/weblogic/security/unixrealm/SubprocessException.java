package weblogic.security.unixrealm;

import weblogic.utils.NestedRuntimeException;

public final class SubprocessException extends NestedRuntimeException {
   private static final long serialVersionUID = -2515789528681978879L;

   public SubprocessException() {
   }

   public SubprocessException(String var1) {
      super(var1);
   }

   public SubprocessException(Throwable var1) {
      super(var1);
   }

   public SubprocessException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
