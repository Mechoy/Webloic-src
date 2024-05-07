package weblogic.ejb.container.deployer.mbimpl;

import weblogic.ejb.container.persistence.spi.CmrField;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.j2ee.descriptor.EjbRelationshipRoleBean;

public final class RelationshipRoleImpl implements EjbRelationshipRole {
   private EjbRelationshipRoleBean m_bean;

   public RelationshipRoleImpl(EjbRelationshipRoleBean var1) {
      this.m_bean = var1;
   }

   public String[] getDescriptions() {
      return this.m_bean.getDescriptions();
   }

   public String getName() {
      return this.m_bean.getEjbRelationshipRoleName();
   }

   public String getMultiplicity() {
      return this.m_bean.getMultiplicity();
   }

   public boolean getCascadeDelete() {
      return this.m_bean.getCascadeDelete() != null;
   }

   public RoleSource getRoleSource() {
      return new MBeanRoleSourceImpl(this.m_bean);
   }

   public CmrField getCmrField() {
      MBeanCmrFieldImpl var1 = null;
      if (null != this.m_bean.getCmrField() && null != this.m_bean.getCmrField().getCmrFieldName()) {
         var1 = new MBeanCmrFieldImpl(this.m_bean);
      }

      return var1;
   }
}
