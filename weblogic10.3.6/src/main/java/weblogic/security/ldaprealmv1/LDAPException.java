package weblogic.security.ldaprealmv1;

import weblogic.utils.NestedRuntimeException;

/** @deprecated */
public final class LDAPException extends NestedRuntimeException {
   private static final long serialVersionUID = 2272225834732254227L;

   public LDAPException() {
   }

   public LDAPException(String var1) {
      super(var1);
   }

   public LDAPException(Throwable var1) {
      super(var1);
   }

   public LDAPException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
