package weblogic.security.acl;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;
import java.security.acl.LastOwnerException;
import java.security.acl.NotOwnerException;
import java.security.acl.Owner;
import java.util.Enumeration;

/** @deprecated */
public class OwnerImpl implements Owner, Serializable {
   private Group ownerGroup = new GroupImpl("AclOwners");

   public OwnerImpl(Principal var1) {
      this.ownerGroup.addMember(var1);
   }

   public synchronized boolean addOwner(Principal var1, Principal var2) throws NotOwnerException {
      if (!this.isOwner(var1)) {
         throw new NotOwnerException();
      } else {
         return this.ownerGroup.addMember(var2);
      }
   }

   public synchronized boolean deleteOwner(Principal var1, Principal var2) throws NotOwnerException, LastOwnerException {
      if (!this.isOwner(var1)) {
         throw new NotOwnerException();
      } else {
         Enumeration var3 = this.ownerGroup.members();
         var3.nextElement();
         if (var3.hasMoreElements()) {
            return this.ownerGroup.removeMember(var2);
         } else {
            throw new LastOwnerException();
         }
      }
   }

   public synchronized boolean isOwner(Principal var1) {
      return this.ownerGroup.isMember(var1);
   }
}
