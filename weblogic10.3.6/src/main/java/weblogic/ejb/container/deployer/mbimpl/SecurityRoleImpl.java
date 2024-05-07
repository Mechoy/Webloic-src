package weblogic.ejb.container.deployer.mbimpl;

import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;

public final class SecurityRoleImpl {
   private SecurityRoleBean m_bean;
   private String m_referencedRole = null;

   public SecurityRoleImpl(EjbDescriptorBean var1, SecurityRoleBean var2) {
      this.m_bean = var2;
   }

   public String[] getDescriptions() {
      return this.m_bean.getDescriptions();
   }

   public String getRoleName() {
      return this.m_bean.getRoleName();
   }

   public String getReferencedRole() {
      return this.m_referencedRole;
   }
}
