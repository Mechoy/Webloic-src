package weblogic.security.acl;

import java.util.Enumeration;

/** @deprecated */
public interface ListableRealm extends BasicRealm {
   Enumeration getUsers();

   Enumeration getGroups();

   Enumeration getAcls();

   Enumeration getPermissions();
}
