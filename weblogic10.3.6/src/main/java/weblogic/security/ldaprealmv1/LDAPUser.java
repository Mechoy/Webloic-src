package weblogic.security.ldaprealmv1;

import weblogic.security.acl.BasicRealm;
import weblogic.security.acl.User;

/** @deprecated */
public final class LDAPUser extends User {
   private static final long serialVersionUID = -3912358838047955072L;
   BasicRealm owner;

   LDAPUser(String var1, BasicRealm var2) {
      super(var1);
      this.owner = var2;
   }

   public BasicRealm getRealm() {
      return this.owner;
   }
}
