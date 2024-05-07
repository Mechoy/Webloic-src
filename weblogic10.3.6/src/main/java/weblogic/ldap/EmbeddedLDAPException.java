package weblogic.ldap;

import weblogic.utils.NestedRuntimeException;

class EmbeddedLDAPException extends NestedRuntimeException {
   public EmbeddedLDAPException() {
   }

   public EmbeddedLDAPException(String var1) {
      super(var1);
   }

   public EmbeddedLDAPException(Throwable var1) {
      super(var1);
   }

   public EmbeddedLDAPException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
