package weblogic.ejb.container.compliance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.persistence.spi.CmrField;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.utils.Debug;
import weblogic.utils.ErrorCollectionException;

public final class GlobalRelationsChecker extends BaseComplianceChecker {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   private EjbRelation rel;
   private EjbRelationshipRole role1;
   private EjbRelationshipRole role2;
   private RoleSource src1;
   private RoleSource src2;
   private CmrField field1;
   private CmrField field2;
   private Relationships relationships;
   private DeploymentInfo di;

   public GlobalRelationsChecker(DeploymentInfo var1) {
      this.di = var1;
      this.relationships = var1.getRelationships();
   }

   public void checkRelations() throws ErrorCollectionException {
      this.checkNoDupCmrFields();
   }

   private void checkNoDupCmrFields() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      HashMap var2 = new HashMap();
      Iterator var3 = this.relationships.getAllEjbRelations().values().iterator();

      while(var3.hasNext()) {
         this.rel = (EjbRelation)var3.next();
         if (verbose) {
            Debug.say("constructed RelationChecker for: " + this.rel.getEjbRelationName());
         }

         Iterator var4 = this.rel.getAllEjbRelationshipRoles().iterator();
         this.role1 = (EjbRelationshipRole)var4.next();
         this.role2 = (EjbRelationshipRole)var4.next();
         this.src1 = this.role1.getRoleSource();
         this.src2 = this.role2.getRoleSource();
         this.field1 = this.role1.getCmrField();
         this.field2 = this.role2.getCmrField();
         this.dupCMRCheck(var2, this.src1, this.field1, var1);
         if (!this.isSymmetric(this.src1, this.field1, this.src2, this.field2)) {
            this.dupCMRCheck(var2, this.src2, this.field2, var1);
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void dupCMRCheck(Map var1, RoleSource var2, CmrField var3, ErrorCollectionException var4) {
      if (var3 != null) {
         String var5 = this.getEncodedName(var2);
         String var6 = var3.getName();
         if (var1.containsKey(var5)) {
            Set var7 = (Set)var1.get(var5);
            if (var7.contains(var6)) {
               var4.add(new ComplianceException(this.fmt.DUPLICATE_CMR_FIELD(this.rel.getEjbRelationName() + " <relationship-role-source>: " + var2.getEjbName() + " <cmr-field>: " + var6), new DescriptorErrorInfo("<relationship-role-source>", this.rel.getEjbRelationName(), var2.getEjbName())));
            } else {
               var7.add(var6);
            }
         } else {
            HashSet var8 = new HashSet();
            var1.put(var5, var8);
            var8.add(var6);
         }

      }
   }

   private String getEncodedName(RoleSource var1) {
      return "EJBNAME_" + var1.getEjbName();
   }

   private boolean isSymmetric(RoleSource var1, CmrField var2, RoleSource var3, CmrField var4) {
      boolean var5 = var2 != null && var4 != null;
      return var1.getEjbName().equals(var3.getEjbName()) && var5 && var2.getName().equals(var4.getName());
   }
}
