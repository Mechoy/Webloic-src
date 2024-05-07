package weblogic.security.acl;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;

/** @deprecated */
public final class Everyone extends User implements Group {
   private static final long serialVersionUID = -5189325963730077457L;
   ListableRealm realm;

   public boolean isMember(Principal var1) {
      return true;
   }

   public Everyone(ListableRealm var1) {
      super("everyone");
      this.realm = var1;
   }

   public boolean addMember(Principal var1) {
      return true;
   }

   public boolean removeMember(Principal var1) {
      throw new IllegalArgumentException("Membership in the " + this.getName() + " group is mandatory.");
   }

   public Enumeration members() {
      return this.realm.getUsers();
   }
}
