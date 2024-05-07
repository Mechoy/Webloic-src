package weblogic.ejb.container.deployer.mbimpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.j2ee.descriptor.EjbRelationBean;
import weblogic.j2ee.descriptor.EjbRelationshipRoleBean;
import weblogic.utils.Debug;

public final class RelationImpl implements EjbRelation {
   private EjbRelationBean m_bean;
   private Map m_roles = new HashMap();

   public RelationImpl(EjbRelationBean var1) {
      this.m_bean = var1;
      EjbRelationshipRoleBean[] var2 = var1.getEjbRelationshipRoles();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.m_roles.put(var2[var3].getEjbRelationshipRoleName(), new RelationshipRoleImpl(var2[var3]));
      }

   }

   public String[] getDescriptions() {
      return this.m_bean.getDescriptions();
   }

   public String getTableName() {
      Debug.assertion(false);
      return null;
   }

   public String getEjbRelationName() {
      return this.m_bean.getEjbRelationName();
   }

   public EjbRelationshipRole getEjbRelationshipRole(String var1) {
      EjbRelationshipRole var2 = (EjbRelationshipRole)this.m_roles.get(var1);
      return var2;
   }

   public Collection getAllEjbRelationshipRoles() {
      return this.m_roles.values();
   }
}
