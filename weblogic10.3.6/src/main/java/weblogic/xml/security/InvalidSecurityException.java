package weblogic.xml.security;

import weblogic.utils.NestedRuntimeException;

/** @deprecated */
public class InvalidSecurityException extends NestedRuntimeException {
   public InvalidSecurityException() {
   }

   public InvalidSecurityException(String var1) {
      super(var1);
   }

   public InvalidSecurityException(Throwable var1) {
      super(var1);
   }

   public InvalidSecurityException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
