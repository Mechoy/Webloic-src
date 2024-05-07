package weblogic.ejb.container.deployer.mbimpl;

import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.j2ee.descriptor.EjbRelationshipRoleBean;
import weblogic.j2ee.descriptor.RelationshipRoleSourceBean;

class MBeanRoleSourceImpl implements RoleSource {
   private EjbRelationshipRoleBean m_bean;
   private boolean isRemote = false;

   public MBeanRoleSourceImpl(EjbRelationshipRoleBean var1) {
      this.m_bean = var1;
   }

   public String[] getDescriptions() {
      return this.m_bean.getRelationshipRoleSource().getDescriptions();
   }

   public String getEjbName() {
      RelationshipRoleSourceBean var1 = this.m_bean.getRelationshipRoleSource();
      String var2 = var1.getEjbName();
      return var2;
   }
}
