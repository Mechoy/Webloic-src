package weblogic.security.acl.internal;

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.Group;
import java.security.acl.NotOwnerException;
import java.security.acl.Permission;
import weblogic.security.acl.BasicRealm;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.User;
import weblogic.security.acl.UserInfo;

public final class ClusterRealm implements BasicRealm {
   public static ClusterRealm THE_ONE;
   private String secret;

   public void init(String var1, Object var2) throws NotOwnerException {
      if (this.secret != null && !this.secret.equals(var2)) {
         throw new NotOwnerException();
      } else {
         if (var2 instanceof DefaultUserInfoImpl) {
            this.secret = ((DefaultUserInfoImpl)var2).getPassword();
         } else {
            if (!(var2 instanceof String)) {
               throw new NotOwnerException();
            }

            this.secret = (String)var2;
         }

      }
   }

   public String getName() {
      return "wl_realm";
   }

   public User getUser(String var1) {
      return null;
   }

   public User getUser(UserInfo var1) {
      try {
         AuthenticatedUser var2 = (AuthenticatedUser)var1;
         return var2.verify(this.secret) ? weblogic.security.acl.Security.getRealm().getUser(var2.getName()) : null;
      } catch (ClassCastException var3) {
         return null;
      }
   }

   public Principal getAclOwner(Object var1) {
      return null;
   }

   public Group getGroup(String var1) {
      return null;
   }

   public Acl getAcl(String var1) {
      return null;
   }

   public Acl getAcl(String var1, char var2) {
      return null;
   }

   public Permission getPermission(String var1) {
      return null;
   }

   public void load(String var1, Object var2) throws ClassNotFoundException, IOException, NotOwnerException {
   }

   public void save(String var1) throws IOException {
   }

   public AuthenticatedUser certify(String var1) {
      return new AuthenticatedUser(var1, this.secret);
   }

   public boolean verify(AuthenticatedUser var1) throws SecurityException {
      return var1.verify(this.secret);
   }

   public AuthenticatedUser createForEJB(String var1) {
      return new AuthenticatedUser(var1, this.secret);
   }
}
