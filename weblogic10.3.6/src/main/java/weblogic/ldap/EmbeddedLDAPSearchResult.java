package weblogic.ldap;

import netscape.ldap.LDAPEntry;

public class EmbeddedLDAPSearchResult {
   private LDAPEntry entry;

   EmbeddedLDAPSearchResult(LDAPEntry var1) {
      this.entry = var1;
   }

   public LDAPEntry getEntry() {
      return this.entry;
   }
}
