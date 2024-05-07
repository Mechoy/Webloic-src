package weblogic.security.acl;

import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.Group;
import java.security.acl.Permission;

/** @deprecated */
public abstract class AbstractManageableRealm extends AbstractListableRealm implements ManageableRealm {
   protected AbstractManageableRealm(String var1) {
      super(var1);
   }

   public User newUser(String var1, Object var2, Object var3) throws SecurityException {
      throw new UnsupportedOperationException("newUser not supported");
   }

   public Group newGroup(String var1) throws SecurityException {
      throw new UnsupportedOperationException("newGroup not supported");
   }

   public Acl newAcl(Principal var1, String var2) throws SecurityException {
      throw new UnsupportedOperationException("newAcl not supported");
   }

   public Permission newPermission(String var1) throws SecurityException {
      throw new UnsupportedOperationException("newPermission not supported");
   }

   public void deleteUser(User var1) throws SecurityException {
      throw new UnsupportedOperationException("deleteUser not supported");
   }

   public void deleteGroup(Group var1) throws SecurityException {
      throw new UnsupportedOperationException("deleteGroup not supported");
   }

   public void deleteAcl(Principal var1, Acl var2) throws SecurityException {
      throw new UnsupportedOperationException("deleteAcl not supported");
   }

   public void deletePermission(Permission var1) throws SecurityException {
      throw new UnsupportedOperationException("deletePermission not supported");
   }

   public void setPermission(Acl var1, Principal var2, Permission var3, boolean var4) {
      throw new UnsupportedOperationException("setPermission not supported");
   }
}
