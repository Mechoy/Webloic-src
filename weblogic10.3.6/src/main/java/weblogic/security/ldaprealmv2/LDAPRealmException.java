package weblogic.security.ldaprealmv2;

import weblogic.utils.NestedRuntimeException;

/** @deprecated */
public final class LDAPRealmException extends NestedRuntimeException {
   private static final long serialVersionUID = 2691943082269447250L;

   public LDAPRealmException() {
   }

   public LDAPRealmException(String var1) {
      super(var1);
   }

   public LDAPRealmException(Throwable var1) {
      super(var1);
   }

   public LDAPRealmException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
