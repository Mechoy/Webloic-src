package weblogic.deployment.descriptors;

import java.util.ArrayList;
import java.util.Iterator;

public class SecurityRoleReference extends BaseDescriptor implements DDValidationErrorCodes {
   private String refDesc;
   private String roleName;
   private RoleDescriptor referencedRole;

   public SecurityRoleReference(String var1, String var2, RoleDescriptor var3) {
      super("weblogic.deployment.descriptors.DDValidationBundle");
      this.refDesc = var1;
      this.roleName = var2;
      this.referencedRole = var3;
   }

   public void setDescription(String var1) {
      this.refDesc = var1;
   }

   public String getDescription() {
      return this.refDesc;
   }

   public void setRoleName(String var1) {
      this.roleName = var1;
   }

   public String getRoleName() {
      return this.roleName;
   }

   public void setReferencedRole(RoleDescriptor var1) {
      this.referencedRole = var1;
   }

   public RoleDescriptor getReferencedRole() {
      return this.referencedRole;
   }

   public void validateSelf() {
      if (this.roleName == null || this.roleName.length() == 0) {
         this.addError("NO_SEC_ROLE_REF_NAME");
      }

      if (this.referencedRole == null) {
         this.addError("NO_SEC_ROLE_REF_ROLE");
      }

   }

   public Iterator getSubObjectsIterator() {
      ArrayList var1 = new ArrayList(1);
      var1.add(this.referencedRole);
      return var1.iterator();
   }
}
