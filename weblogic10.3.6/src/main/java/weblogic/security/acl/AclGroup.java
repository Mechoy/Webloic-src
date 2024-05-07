package weblogic.security.acl;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.Vector;
import weblogic.utils.UnsyncHashtable;

class AclGroup implements Serializable {
   AclGroup next;
   private UnsyncHashtable individualConstraints = new UnsyncHashtable();
   private UnsyncHashtable groupConstraints = new UnsyncHashtable();
   Vector permissions = new Vector();

   final int getPermissionIndexFromHere(Object var1) {
      int var2 = this.permissions.indexOf(var1);
      if (var2 >= 0) {
         return var2;
      } else if (this.permissions.size() == 32) {
         return 32;
      } else {
         int var3 = this.permissions.size();
         if (var3 == 32) {
            return 32;
         } else {
            this.permissions.addElement(var1);
            return var3;
         }
      }
   }

   int getPermissionIndex(Object var1) {
      int var2 = this.getPermissionIndexFromHere(var1);
      if (var2 < 32) {
         return var2;
      } else {
         if (this.next == null && this.next == null) {
            this.next = new AclGroup();
         }

         return 32 + this.next.getPermissionIndex(var1);
      }
   }

   final PermissionSet checkPermissionSet(Principal var1) {
      UnsyncHashtable var2 = var1 instanceof Group ? this.groupConstraints : this.individualConstraints;
      return (PermissionSet)var2.get(var1);
   }

   final PermissionSet getPermissionSet(Principal var1, int var2) {
      if (var2 >= 32) {
         return this.next.getPermissionSet(var1, var2 - 32);
      } else {
         UnsyncHashtable var3 = var1 instanceof Group ? this.groupConstraints : this.individualConstraints;
         PermissionSet var4 = (PermissionSet)var3.get(var1);
         if (var4 != null) {
            return var4;
         } else {
            var4 = (PermissionSet)var3.get(var1);
            if (var4 != null) {
               return var4;
            } else {
               var4 = new PermissionSet();
               var3.put(var1, var4);
               return var4;
            }
         }
      }
   }

   boolean hasPositiveEntry(Principal var1) {
      PermissionSet var2;
      return this.next != null && this.next.hasPositiveEntry(var1) || (var2 = this.checkPermissionSet(var1)) != null && var2.granted != 0;
   }

   boolean hasNegativeEntry(Principal var1) {
      PermissionSet var2;
      return this.next != null && this.next.hasNegativeEntry(var1) || (var2 = this.checkPermissionSet(var1)) != null && var2.notDenied != -1;
   }

   Vector getPermissions(Principal var1, Vector var2) {
      if (this.next != null) {
         var2 = this.next.getPermissions(var1, var2);
      }

      PermissionSet var3 = (PermissionSet)this.individualConstraints.get(var1);
      if (var3 == null) {
         var3 = new PermissionSet();
      }

      PermissionSet var4 = new PermissionSet();
      Enumeration var5 = this.groupConstraints.keys();

      while(true) {
         Group var6;
         do {
            if (!var5.hasMoreElements()) {
               int var7 = var3.granted & var3.notDenied | (var3.granted | var3.notDenied) & var4.granted & var4.notDenied;
               return this.addPermissions(var7, var2);
            }

            var6 = (Group)var5.nextElement();
         } while(!var1.equals(var6) && !var6.isMember(var1));

         var4.add((PermissionSet)this.groupConstraints.get(var6));
      }
   }

   private Vector addPermissions(int var1, Vector var2) {
      for(int var3 = 0; var1 != 0 && var3 < this.permissions.size(); ++var3) {
         if ((var1 & 1 << var3) != 0) {
            var1 ^= 1 << var3;
            var2.addElement(this.permissions.elementAt(var3));
         }
      }

      return var2;
   }

   int getPermission(Principal var1, Permission var2) {
      int var3 = this.permissions.indexOf(var2);
      if (var3 < 0) {
         return this.next == null ? 0 : this.next.getPermission(var1, var2);
      } else {
         int var4 = 1 << var3;
         PermissionSet var5 = (PermissionSet)this.individualConstraints.get(var1);
         if (var5 != null) {
            if ((var5.granted & var5.notDenied & var4) != 0) {
               return 1;
            }

            if ((~var5.granted & ~var5.notDenied & var4) != 0) {
               return -1;
            }
         }

         int var6 = 0;
         Group var7 = null;
         Enumeration var8 = this.groupConstraints.keys();

         while(true) {
            while(var8.hasMoreElements()) {
               Group var9 = (Group)var8.nextElement();
               if (var9.getName().equalsIgnoreCase("Everyone")) {
                  var7 = var9;
               } else if (var9.equals(var1) || var9.isMember(var1)) {
                  var5 = (PermissionSet)this.groupConstraints.get(var9);
                  if ((~var5.notDenied & var4) != 0) {
                     return -1;
                  }

                  var6 |= var5.granted;
               }
            }

            if ((var6 & var4) == 1) {
               return 1;
            }

            if (var7 != null) {
               var5 = (PermissionSet)this.groupConstraints.get(var7);
               if ((~var5.notDenied & var4) != 0) {
                  return -1;
               }

               var6 |= var5.granted;
            }

            return (var6 & var4) != 0 ? 1 : 0;
         }
      }
   }

   private StringBuffer printTable(UnsyncHashtable var1, StringBuffer var2) {
      Enumeration var3 = var1.keys();

      while(var3.hasMoreElements()) {
         Object var4 = var3.nextElement();
         var2.append("\n");
         var2.append(var4);
         var2.append(": ");
         PermissionSet var5 = (PermissionSet)var1.get(var4);

         for(int var6 = 0; var6 < 32; ++var6) {
            if ((1 << var6 & var5.granted) != 0) {
               var2.append("+").append(this.permissions.elementAt(var6));
            }

            if ((1 << var6 & var5.notDenied) == 0) {
               var2.append("-").append(this.permissions.elementAt(var6));
            }
         }
      }

      return var2;
   }

   String toString(StringBuffer var1) {
      if (this.next != null) {
         var1 = new StringBuffer(this.next.toString(var1));
      }

      this.printTable(this.individualConstraints, var1);
      this.printTable(this.groupConstraints, var1);
      var1.append("\n");
      return var1.toString();
   }

   UnsyncHashtable getPrincipals(UnsyncHashtable var1) {
      if (this.next != null) {
         var1 = this.next.getPrincipals(var1);
      }

      Enumeration var2 = this.individualConstraints.keys();

      while(var2.hasMoreElements()) {
         var1.put(var2.nextElement(), "");
      }

      var2 = this.groupConstraints.keys();

      while(var2.hasMoreElements()) {
         var1.put(var2.nextElement(), "");
      }

      return var1;
   }

   void fillEntries(Principal var1, AclEntry var2, AclEntry var3) {
      if (this.next != null) {
         this.next.fillEntries(var1, var2, var3);
      }

      PermissionSet var4 = this.checkPermissionSet(var1);
      if (var4 != null) {
         Enumeration var5 = this.addPermissions(var4.granted, new Vector()).elements();

         while(var5.hasMoreElements()) {
            var2.addPermission((Permission)var5.nextElement());
         }

         var5 = this.addPermissions(~var4.notDenied, new Vector()).elements();

         while(var5.hasMoreElements()) {
            var3.addPermission((Permission)var5.nextElement());
         }

      }
   }
}
