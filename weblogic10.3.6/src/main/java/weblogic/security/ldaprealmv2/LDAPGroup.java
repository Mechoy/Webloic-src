package weblogic.security.ldaprealmv2;

import java.security.Principal;
import java.util.Enumeration;
import netscape.ldap.LDAPDN;
import weblogic.security.acl.FlatGroup;

/** @deprecated */
public final class LDAPGroup extends FlatGroup implements LDAPEntity {
   String name;
   String dn;
   LDAPDelegate delegate;

   LDAPGroup(String var1, String var2, LDAPDelegate var3, FlatGroup.Source var4) {
      super(var1, var4);
      this.name = var1;
      this.dn = var2;
      this.delegate = var3;
   }

   /** @deprecated */
   public boolean addMember(Principal var1) {
      return false;
   }

   /** @deprecated */
   public boolean removeMember(Principal var1) {
      return false;
   }

   protected Class getUserClass() {
      return LDAPUser.class;
   }

   public boolean isMember(Principal var1) {
      long var2 = FlatGroup.getCacheTTLMillis();
      if (var2 == 0L) {
         return var1 instanceof LDAPEntity ? this.delegate.groupContains(this.getDN(), ((LDAPEntity)var1).getDN()) : false;
      } else {
         return super.isMember(var1);
      }
   }

   public Enumeration members() {
      long var1 = FlatGroup.getCacheTTLMillis();
      return var1 == 0L ? this.delegate.groupMembers(this.getDN()) : super.members();
   }

   public String getDN() {
      return this.dn;
   }

   public String getName() {
      return this.name;
   }

   public boolean equals(Object var1) {
      return var1 != null && var1 instanceof LDAPGroup && LDAPDN.equals(((LDAPGroup)var1).dn, this.dn);
   }

   public String toString() {
      return this.name;
   }
}
