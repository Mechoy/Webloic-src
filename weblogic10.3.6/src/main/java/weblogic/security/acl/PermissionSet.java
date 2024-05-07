package weblogic.security.acl;

import java.io.Serializable;

class PermissionSet implements Serializable {
   int granted = 0;
   int notDenied = -1;

   final void add(PermissionSet var1) {
      this.granted |= var1.granted;
      this.notDenied &= var1.notDenied;
   }
}
