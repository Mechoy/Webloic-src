package weblogic.security.unixrealm;

import weblogic.security.acl.BasicRealm;
import weblogic.security.acl.User;

public final class UnixUser extends User {
   private static final long serialVersionUID = 3248835693633099658L;
   BasicRealm owner;

   UnixUser(String var1, BasicRealm var2) {
      super(var1);
      this.owner = var2;
   }

   public BasicRealm getRealm() {
      return this.owner;
   }
}
