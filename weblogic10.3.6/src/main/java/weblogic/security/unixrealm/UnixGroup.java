package weblogic.security.unixrealm;

import java.util.Hashtable;
import weblogic.security.acl.FlatGroup;

public final class UnixGroup extends FlatGroup {
   UnixGroup(String var1, FlatGroup.Source var2, Hashtable var3) {
      super(var1, var2, var3);
   }

   UnixGroup(String var1, FlatGroup.Source var2) {
      super(var1, var2);
   }

   protected Class getUserClass() {
      return UnixUser.class;
   }
}
