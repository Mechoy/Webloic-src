package weblogic.security.acl;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;

/** @deprecated */
public abstract class DefaultGroupImpl implements Group {
   private String name;

   public DefaultGroupImpl(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public boolean addMember(Principal var1) {
      throw new UnsupportedOperationException("group modification not supported");
   }

   public boolean removeMember(Principal var1) {
      throw new UnsupportedOperationException("group modification not supported");
   }

   public abstract boolean isMember(Principal var1);

   public abstract Enumeration members();

   public String toString() {
      return this.name;
   }
}
