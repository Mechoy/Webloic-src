package weblogic.ejb.container.deployer.mbimpl;

import weblogic.ejb.container.persistence.spi.CmrField;
import weblogic.j2ee.descriptor.EjbRelationshipRoleBean;

class MBeanCmrFieldImpl implements CmrField {
   EjbRelationshipRoleBean m_bean;

   public MBeanCmrFieldImpl(EjbRelationshipRoleBean var1) {
      this.m_bean = var1;
   }

   public String[] getDescriptions() {
      return this.m_bean.getCmrField().getDescriptions();
   }

   public String getName() {
      String var1 = this.m_bean.getCmrField().getCmrFieldName();
      return var1;
   }

   public String getType() {
      return this.m_bean.getCmrField().getCmrFieldType();
   }
}
