package weblogic.ejb.container.deployer.mbimpl;

import weblogic.ejb.container.interfaces.SecurityRoleReference;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.SecurityRoleRefBean;

public final class SecurityRoleRefImpl implements SecurityRoleReference {
   private String m_roleName;
   private String m_referencedRole;

   public SecurityRoleRefImpl(EjbDescriptorBean var1, SecurityRoleRefBean var2) {
      this.m_roleName = var2.getRoleName();
      this.m_referencedRole = var2.getRoleLink();
   }

   public void setRoleName(String var1) {
      this.m_roleName = var1;
   }

   public String getRoleName() {
      return this.m_roleName;
   }

   public void setReferencedRole(String var1) {
      this.m_referencedRole = var1;
   }

   public String getReferencedRole() {
      return this.m_referencedRole;
   }
}
