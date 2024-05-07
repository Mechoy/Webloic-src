package weblogic.security.acl;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.NotOwnerException;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.Vector;
import weblogic.utils.UnsyncHashtable;

/** @deprecated */
public class AclImpl extends OwnerImpl implements Acl, Serializable {
   private AclGroup info;
   private String name;

   public AclImpl(Principal var1, String var2) {
      super(var1);
      this.name = var2;
   }

   public void setName(Principal var1, String var2) throws NotOwnerException {
      if (!this.isOwner(var1)) {
         throw new NotOwnerException();
      } else {
         this.name = var2;
      }
   }

   public String getName() {
      return this.name;
   }

   public synchronized boolean addEntry(Principal var1, AclEntry var2) throws NotOwnerException {
      if (this.info == null) {
         this.info = new AclGroup();
      }

      if (!this.isOwner(var1)) {
         throw new NotOwnerException();
      } else {
         Principal var3 = var2.getPrincipal();
         PermissionSet var10000;
         Enumeration var4;
         int var5;
         if (var2.isNegative()) {
            if (this.info.hasNegativeEntry(var3)) {
               return false;
            }

            for(var4 = var2.permissions(); var4.hasMoreElements(); var10000.notDenied &= ~(1 << (var5 & 31))) {
               var5 = this.info.getPermissionIndex(var4.nextElement());
               var10000 = this.info.getPermissionSet(var3, var5);
            }
         } else {
            if (this.info.hasPositiveEntry(var3)) {
               return false;
            }

            for(var4 = var2.permissions(); var4.hasMoreElements(); var10000.granted |= 1 << (var5 & 31)) {
               var5 = this.info.getPermissionIndex(var4.nextElement());
               var10000 = this.info.getPermissionSet(var3, var5);
            }
         }

         return true;
      }
   }

   public synchronized boolean removeEntry(Principal var1, AclEntry var2) throws NotOwnerException {
      if (this.info == null) {
         this.info = new AclGroup();
      }

      if (!this.isOwner(var1)) {
         throw new NotOwnerException();
      } else {
         Principal var3 = var2.getPrincipal();
         Enumeration var4;
         int var5;
         if (var2.isNegative()) {
            var4 = var2.permissions();

            while(var4.hasMoreElements()) {
               var5 = this.info.getPermissionIndex(var4.nextElement());
               if ((this.info.getPermissionSet(var3, var5).notDenied & 1 << (var5 & 31)) != 0) {
                  return false;
               }
            }
         } else {
            var4 = var2.permissions();

            while(var4.hasMoreElements()) {
               var5 = this.info.getPermissionIndex(var4.nextElement());
               if ((this.info.getPermissionSet(var3, var5).granted & 1 << (var5 & 31)) == 0) {
                  return false;
               }
            }
         }

         var4 = var2.permissions();

         while(var4.hasMoreElements()) {
            var5 = this.info.getPermissionIndex(var4.nextElement());
            PermissionSet var10000;
            if (var2.isNegative()) {
               var10000 = this.info.getPermissionSet(var3, var5);
               var10000.notDenied |= 1 << (var5 & 31);
            } else {
               var10000 = this.info.getPermissionSet(var3, var5);
               var10000.granted &= ~(1 << (var5 & 31));
            }
         }

         return true;
      }
   }

   public synchronized Enumeration getPermissions(Principal var1) {
      Vector var2 = new Vector();
      return this.info != null ? this.info.getPermissions(var1, var2).elements() : var2.elements();
   }

   public synchronized Enumeration entries() {
      if (this.info == null) {
         this.info = new AclGroup();
      }

      Vector var1 = new Vector();
      Enumeration var2 = this.info.getPrincipals(new UnsyncHashtable()).keys();

      while(var2.hasMoreElements()) {
         Principal var3 = (Principal)var2.nextElement();
         AclEntryImpl var4 = new AclEntryImpl(var3);
         AclEntryImpl var5 = new AclEntryImpl(var3);
         var5.setNegativePermissions();
         this.info.fillEntries(var3, var4, var5);
         if (var4.permissions().hasMoreElements()) {
            var1.addElement(var4);
         }

         if (var5.permissions().hasMoreElements()) {
            var1.addElement(var5);
         }
      }

      return var1.elements();
   }

   public synchronized boolean checkPermission(Principal var1, Permission var2) {
      return var1 != null && var2 != null && this.info != null && this.info.getPermission(var1, var2) > 0;
   }

   protected synchronized int getPermission(Principal var1, Permission var2) {
      return this.info != null ? this.info.getPermission(var1, var2) : 0;
   }

   public synchronized String toString() {
      if (this.info == null) {
         this.info = new AclGroup();
      }

      StringBuffer var1 = new StringBuffer("ACL ");
      var1.append(this.name);
      return this.info.toString(var1);
   }
}
