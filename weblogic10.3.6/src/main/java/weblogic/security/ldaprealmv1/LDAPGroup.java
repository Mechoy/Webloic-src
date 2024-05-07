package weblogic.security.ldaprealmv1;

import java.util.Hashtable;
import weblogic.security.acl.FlatGroup;

/** @deprecated */
public final class LDAPGroup extends FlatGroup {
   LDAPGroup(String var1, FlatGroup.Source var2, Hashtable var3) {
      super(var1, var2, var3);
   }

   LDAPGroup(String var1, FlatGroup.Source var2) {
      super(var1, var2);
   }

   protected Class getUserClass() {
      return LDAPUser.class;
   }
}
