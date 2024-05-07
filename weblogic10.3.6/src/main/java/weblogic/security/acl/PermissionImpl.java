package weblogic.security.acl;

import java.io.Serializable;
import java.security.acl.Permission;

/** @deprecated */
public final class PermissionImpl implements Permission, Serializable {
   private static final long serialVersionUID = -9019841447023711250L;
   private String name;

   public PermissionImpl(String var1) {
      this.name = var1;
   }

   public boolean equals(Object var1) {
      return var1 instanceof Permission && this.name.equals(var1.toString());
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public String toString() {
      return this.name;
   }

   public String getName() {
      return this.name;
   }
}
