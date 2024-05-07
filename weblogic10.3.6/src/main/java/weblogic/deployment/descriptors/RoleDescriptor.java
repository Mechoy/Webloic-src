package weblogic.deployment.descriptors;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RoleDescriptor extends BaseDescriptor implements DDValidationErrorCodes {
   private String description;
   private String roleName;
   private Set principals;

   public RoleDescriptor() {
      this("", "");
   }

   public RoleDescriptor(String var1, String var2) {
      super("weblogic.deployment.descriptors.DDValidationBundle");
      this.principals = new HashSet();
      this.description = var1;
      this.roleName = var2;
   }

   public RoleDescriptor(String var1) {
      super("weblogic.deployment.descriptors.DDValidationBundle");
      this.principals = new HashSet();
      this.roleName = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public String getName() {
      return this.roleName;
   }

   public void setName(String var1) {
      this.roleName = var1;
   }

   public void addSecurityPrincipal(String var1) {
      this.principals.add(var1);
   }

   public boolean hasSecurityPrincipal(String var1) {
      return this.principals.contains(var1);
   }

   public void addSecurityPrincipals(Map var1) {
      this.principals.addAll(var1.values());
   }

   public void addSecurityPrincipals(Collection var1) {
      this.principals.addAll(var1);
   }

   public void removeSecurityPrincipal(String var1) {
      this.principals.remove(var1);
   }

   public void removeAllSecurityPrincipals() {
      this.principals.clear();
   }

   public Collection getAllSecurityPrincipals() {
      return this.principals;
   }

   public void validateSelf() {
      if (this.roleName == null || this.roleName.length() == 0) {
         this.addError("NO_ROLE_NAME_SET");
      }

   }

   public Iterator getSubObjectsIterator() {
      return Collections.EMPTY_SET.iterator();
   }
}
