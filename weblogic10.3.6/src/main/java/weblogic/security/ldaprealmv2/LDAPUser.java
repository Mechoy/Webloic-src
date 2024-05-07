package weblogic.security.ldaprealmv2;

import java.security.Identity;
import weblogic.security.acl.BasicRealm;
import weblogic.security.acl.User;

/** @deprecated */
public final class LDAPUser extends User implements LDAPEntity {
   private static final long serialVersionUID = 5998212766555818415L;
   BasicRealm owner;
   String dn;

   LDAPUser(String var1, String var2, BasicRealm var3) {
      super(var1);
      this.dn = var2;
      this.owner = var3;
   }

   public BasicRealm getRealm() {
      return this.owner;
   }

   public String getDN() {
      return this.dn;
   }

   public int hashCode() {
      return this.dn.hashCode();
   }

   protected boolean identityEquals(Identity var1) {
      return var1 != null && var1 instanceof LDAPUser && ((LDAPUser)var1).dn.equals(this.dn);
   }

   public String toString() {
      return this.getName();
   }
}
