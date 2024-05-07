package weblogic.ejb.container.deployer.mbimpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.interfaces.NoSuchRoleException;
import weblogic.ejb.container.interfaces.SecurityRoleMapping;

public final class SecurityRoleMappingImpl implements SecurityRoleMapping {
   private final Map securityRoles = new HashMap();
   private Set externallyDefinedRoles = null;

   public final Collection getSecurityRoleNames() {
      return this.securityRoles.keySet();
   }

   public boolean hasRole(String var1) {
      return this.securityRoles.get(var1) != null;
   }

   public final Collection getSecurityRolePrincipalNames(String var1) throws NoSuchRoleException {
      Collection var2 = (Collection)this.securityRoles.get(var1);
      if (var2 == null) {
         throw new NoSuchRoleException(var1 + " is not a recognized role");
      } else {
         return var2;
      }
   }

   public void addRoleToPrincipalsMapping(String var1, Collection var2) {
      this.securityRoles.put(var1, var2);
   }

   public boolean isRoleMappedToPrincipals(String var1) throws NoSuchRoleException {
      if (!this.securityRoles.containsKey(var1)) {
         throw new NoSuchRoleException(var1 + " is not a recognized role");
      } else {
         Collection var2 = (Collection)this.securityRoles.get(var1);
         return var2 != null && !var2.isEmpty();
      }
   }

   public void addExternallyDefinedRole(String var1) {
      if (this.externallyDefinedRoles == null) {
         this.externallyDefinedRoles = new HashSet();
      }

      this.externallyDefinedRoles.add(var1);
   }

   public boolean isExternallyDefinedRole(String var1) throws NoSuchRoleException {
      if (!this.securityRoles.containsKey(var1)) {
         throw new NoSuchRoleException(var1 + " is not a recognized role");
      } else {
         return this.externallyDefinedRoles == null ? false : this.externallyDefinedRoles.contains(var1);
      }
   }

   public String toString() {
      String var1 = new String("SecurityRoleMapping:\n");
      Iterator var2 = this.securityRoles.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1 = var1 + "   Role:" + var3 + "\n";
         Collection var4 = (Collection)this.securityRoles.get(var3);

         String var6;
         for(Iterator var5 = var4.iterator(); var5.hasNext(); var1 = var1 + "      " + var6 + "\n") {
            var6 = (String)var5.next();
         }
      }

      return var1;
   }
}
