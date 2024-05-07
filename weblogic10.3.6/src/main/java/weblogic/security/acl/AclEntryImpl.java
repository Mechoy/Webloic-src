package weblogic.security.acl;

import java.security.Principal;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.Vector;

/** @deprecated */
public final class AclEntryImpl implements AclEntry {
   private Principal user;
   private Vector permissionSet;
   private boolean negative;

   public AclEntryImpl(Principal var1) {
      this.permissionSet = new Vector();
      this.negative = false;
      this.user = var1;
   }

   public AclEntryImpl(Principal var1, Permission var2) {
      this(var1);
      this.addPermission(var2);
   }

   public AclEntryImpl(Principal var1, Permission[] var2) {
      this(var1);

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.addPermission(var2[var3]);
      }

   }

   public boolean setPrincipal(Principal var1) {
      boolean var2 = this.user != null;
      this.user = var1;
      return var2;
   }

   public void setNegativePermissions() {
      this.negative = true;
   }

   public boolean isNegative() {
      return this.negative;
   }

   public boolean addPermission(Permission var1) {
      if (this.permissionSet.contains(var1)) {
         return false;
      } else {
         this.permissionSet.addElement(var1);
         return true;
      }
   }

   public boolean removePermission(Permission var1) {
      return this.permissionSet.removeElement(var1);
   }

   public boolean checkPermission(Permission var1) {
      return this.permissionSet.contains(var1);
   }

   public Enumeration permissions() {
      return this.permissionSet.elements();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.negative ? "-" : "+").append(this.user instanceof Group ? "Group." : "User.").append(this.user).append("=");
      String var2 = "";

      for(Enumeration var3 = this.permissions(); var3.hasMoreElements(); var2 = ", ") {
         var1.append(var2).append(var3.nextElement());
      }

      return new String(var1);
   }

   public synchronized Object clone() {
      AclEntryImpl var1 = new AclEntryImpl(this.user);
      var1.permissionSet = (Vector)this.permissionSet.clone();
      var1.negative = this.negative;
      return var1;
   }

   public Principal getPrincipal() {
      return this.user;
   }
}
