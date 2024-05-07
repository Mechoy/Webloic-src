package weblogic.security.principal;

import java.security.acl.Group;
import weblogic.security.acl.BasicRealm;
import weblogic.security.acl.Security;
import weblogic.security.acl.User;
import weblogic.security.spi.InvalidPrincipalException;
import weblogic.security.spi.WLSUser;

public final class RealmAdapterUser extends WLSAbstractPrincipal implements WLSUser, RealmAdapterUserInterface {
   static final long serialVersionUID = 8241522729442054197L;

   public RealmAdapterUser(String var1) {
      this.setName(var1);
   }

   public RealmAdapterUser(String var1, byte[] var2, byte[] var3) {
      this.setName(var1);
      this.setSalt(var2);
      this.setSignature(var3);
   }

   public boolean isUserInGroup(String var1) {
      BasicRealm var2 = Security.getRealm();
      if (var2 == null) {
         throw new SecurityException("Realm Adapter Realm Not Configured");
      } else {
         User var3 = var2.getUser(this.getName());
         if (var3 == null) {
            throw new InvalidPrincipalException("Realm Adapter User " + this.getName() + " invalid in Realm Adapter realm " + var2.getName());
         } else {
            Group var4 = var2.getGroup(var1);
            return var4 == null ? false : var4.isMember(var3);
         }
      }
   }
}
