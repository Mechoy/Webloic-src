package weblogic.security.acl;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.Group;
import java.security.acl.Permission;

/** @deprecated */
public interface ManageableRealm extends ListableRealm, Serializable {
   User newUser(String var1, Object var2, Object var3) throws SecurityException;

   Group newGroup(String var1) throws SecurityException;

   Acl newAcl(Principal var1, String var2) throws SecurityException;

   Permission newPermission(String var1) throws SecurityException;

   void deleteUser(User var1) throws SecurityException;

   void deleteGroup(Group var1) throws SecurityException;

   void deleteAcl(Principal var1, Acl var2) throws SecurityException;

   void deletePermission(Permission var1) throws SecurityException;

   void setPermission(Acl var1, Principal var2, Permission var3, boolean var4);
}
