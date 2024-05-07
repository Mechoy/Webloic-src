package weblogic.ejb.container.deployer.mbimpl;

import java.util.HashMap;
import java.util.Map;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.persistence.spi.EjbEntityRef;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.descriptor.CmrFieldBean;
import weblogic.j2ee.descriptor.EjbRelationBean;
import weblogic.j2ee.descriptor.EjbRelationshipRoleBean;
import weblogic.j2ee.descriptor.RelationshipsBean;
import weblogic.logging.Loggable;

public final class RelationshipsImpl implements Relationships {
   private String[] m_description;
   private RelationshipsBean m_mBean;
   private Map m_ejbEntityRefs = new HashMap();
   private Map m_relations = new HashMap();

   public RelationshipsImpl(RelationshipsBean var1, Map var2) throws WLDeploymentException {
      this.m_mBean = var1;
      if (null != var1) {
         this.m_description = var1.getDescriptions();
         EjbRelationBean[] var3 = var1.getEjbRelations();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            this.validateEjbRelationshipRoleNames(var3[var4]);
            String var5 = var3[var4].getEjbRelationName();
            if (var5 == null || var5.length() == 0) {
               this.createEjbRelationName(var3[var4]);
               var5 = var3[var4].getEjbRelationName();
            }

            this.m_relations.put(var5, new RelationImpl(var3[var4]));
         }
      }

   }

   private void createEjbRelationName(EjbRelationBean var1) {
      EjbRelationshipRoleBean[] var2 = var1.getEjbRelationshipRoles();
      String var3 = this.defaultRoleName(var2[0]);
      String var4 = this.defaultRoleName(var2[1]);
      if (var3.compareTo(var4) < 0) {
         var1.setEjbRelationName(var3 + "-" + var4);
      } else {
         if (var3.compareTo(var4) <= 0) {
            throw new AssertionError("Error: role names " + var3 + " and " + var4 + " are equal in relationship.");
         }

         var1.setEjbRelationName(var4 + "-" + var3);
      }

   }

   private void validateEjbRelationshipRoleNames(EjbRelationBean var1) throws WLDeploymentException {
      EjbRelationshipRoleBean[] var2 = var1.getEjbRelationshipRoles();
      String var3 = var2[0].getEjbRelationshipRoleName();
      String var4 = var2[1].getEjbRelationshipRoleName();
      if (var3 == null) {
         var3 = this.defaultRoleName(var2[0]);
         var2[0].setEjbRelationshipRoleName(var3);
      }

      if (var4 == null) {
         var4 = this.defaultRoleName(var2[1]);
         var2[1].setEjbRelationshipRoleName(var4);
      }

      if (var3.compareTo(var4) == 0) {
         String var5 = var2[0].getRelationshipRoleSource().getEjbName();
         CmrFieldBean var6 = var2[0].getCmrField();
         String var7 = var6 == null ? "" : var6.getCmrFieldName();
         Loggable var8 = EJBLogger.logduplicateRelationshipRoleNameLoggable(var5, var7);
         throw new WLDeploymentException(var8.getMessage());
      }
   }

   private String defaultRoleName(EjbRelationshipRoleBean var1) {
      String var2 = var1.getRelationshipRoleSource().getEjbName();
      CmrFieldBean var3 = var1.getCmrField();
      String var4 = null;
      if (var3 == null) {
         var4 = var2;
      } else {
         var4 = var2 + "." + var3.getCmrFieldName();
      }

      return var4;
   }

   public String[] getDescriptions() {
      return this.m_description;
   }

   public EjbEntityRef getEjbEntityRef(String var1) {
      EjbEntityRef var2 = (EjbEntityRef)this.m_ejbEntityRefs.get(var1);
      return var2;
   }

   public Map getAllEjbEntityRefs() {
      return this.m_ejbEntityRefs;
   }

   public EjbRelation getEjbRelation(String var1) {
      return (EjbRelation)this.m_relations.get(var1);
   }

   public Map getAllEjbRelations() {
      return this.m_relations;
   }
}
