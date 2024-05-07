package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.persistence.PersistenceUtils;
import weblogic.ejb.container.persistence.spi.CmrField;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.utils.Debug;
import weblogic.utils.ErrorCollectionException;

public final class RelationChecker extends BaseComplianceChecker {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   private EjbRelation rel;
   private EjbRelationshipRole role1;
   private EjbRelationshipRole role2;
   private RoleSource src1;
   private RoleSource src2;
   private CmrField field1;
   private CmrField field2;
   private DeploymentInfo di;
   private Relationships relationships;
   private Map ejbEntityRefs;
   private Map methods1;
   private Map methods2;

   public RelationChecker(EjbRelation var1, DeploymentInfo var2) {
      this.rel = var1;
      this.di = var2;
      Iterator var3 = var1.getAllEjbRelationshipRoles().iterator();
      this.role1 = (EjbRelationshipRole)var3.next();
      this.role2 = (EjbRelationshipRole)var3.next();
      this.src1 = this.role1.getRoleSource();
      this.src2 = this.role2.getRoleSource();
      this.field1 = this.role1.getCmrField();
      this.field2 = this.role2.getCmrField();
      this.relationships = var2.getRelationships();
      this.ejbEntityRefs = this.relationships.getAllEjbEntityRefs();
      if (this.ejbEntityRefs == null) {
         this.ejbEntityRefs = new HashMap();
      }

      if (verbose) {
         Debug.say("constructed RelationChecker for: " + var1.getEjbRelationName());
      }

   }

   public void checkRelation() throws ErrorCollectionException {
      this.checkLocalBeanInRoleExists();
      this.checkLocalBeanInRoleIsEntityBean();
      this.checkLocalBeanInRoleNotBM();
      this.checkLocalBeanInRoleUses20CMP();
      this.checkAtLeastUniDirectional();
      this.checkCmrFieldNotACmpField();
      this.checkManyHasType();
      this.check1ManyNoDupFieldForSameBean();
      this.checkCmrFieldExists();
      this.checkEjbCascadeDelete();
   }

   private void check1ManyNoDupFieldForSameBean() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      int var2 = 0;
      int var3 = 0;
      if (this.role1.getMultiplicity().equalsIgnoreCase("one")) {
         ++var2;
      } else if (this.role1.getMultiplicity().equalsIgnoreCase("many")) {
         ++var3;
      }

      if (this.role2.getMultiplicity().equalsIgnoreCase("one")) {
         ++var2;
      } else if (this.role2.getMultiplicity().equalsIgnoreCase("many")) {
         ++var3;
      }

      if (var2 == 1 && var3 == 1 && this.src1.getEjbName().compareTo(this.src2.getEjbName()) == 0 && this.field1 != null && this.field2 != null && this.field1.getName().compareTo(this.field2.getName()) == 0) {
         var1.add(new ComplianceException(this.fmt.N1_RELATION_HAS_DUP_FIELD_FOR_SAME_BEAN(this.rel.getEjbRelationName() + " <cmr-field>: " + this.field1.getName()), new DescriptorErrorInfo("<cmr-field>", this.rel.getEjbRelationName(), this.rel.getEjbRelationName())));
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkLocalBeanInRoleExists() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      if (this.di.getBeanInfo(this.src1.getEjbName()) == null) {
         var1.add(new ComplianceException(this.fmt.NON_EXISTENT_BEAN_IN_ROLE(this.src1.getEjbName(), this.rel.getEjbRelationName()), new DescriptorErrorInfo("<relationship-role-source>", this.rel.getEjbRelationName(), this.src1.getEjbName())));
      }

      if (this.di.getBeanInfo(this.src2.getEjbName()) == null) {
         var1.add(new ComplianceException(this.fmt.NON_EXISTENT_BEAN_IN_ROLE(this.src2.getEjbName(), this.rel.getEjbRelationName()), new DescriptorErrorInfo("<relationship-role-source>", this.rel.getEjbRelationName(), this.src2.getEjbName())));
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkLocalBeanInRoleIsEntityBean() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      BeanInfo var2 = this.di.getBeanInfo(this.src1.getEjbName());
      if (!(var2 instanceof EntityBeanInfo)) {
         var1.add(new ComplianceException(this.fmt.NON_ENTITY_BEAN_IN_ROLE(this.rel.getEjbRelationName()), new DescriptorErrorInfo("<relationship-role-source>", this.rel.getEjbRelationName(), this.src1.getEjbName())));
      }

      var2 = this.di.getBeanInfo(this.src2.getEjbName());
      if (!(var2 instanceof EntityBeanInfo)) {
         var1.add(new ComplianceException(this.fmt.NON_ENTITY_BEAN_IN_ROLE(this.rel.getEjbRelationName()), new DescriptorErrorInfo("<relationship-role-source>", this.rel.getEjbRelationName(), this.src2.getEjbName())));
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkLocalBeanInRoleNotBM() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      EntityBeanInfo var2 = (EntityBeanInfo)this.di.getBeanInfo(this.src1.getEjbName());
      if (var2.getIsBeanManagedPersistence()) {
         var1.add(new ComplianceException(this.fmt.BM_BEAN_IN_ROLE(this.rel.getEjbRelationName()), new DescriptorErrorInfo("<relationship-role-source>", this.rel.getEjbRelationName(), this.src1.getEjbName())));
      }

      var2 = (EntityBeanInfo)this.di.getBeanInfo(this.src2.getEjbName());
      if (var2.getIsBeanManagedPersistence()) {
         var1.add(new ComplianceException(this.fmt.BM_BEAN_IN_ROLE(this.rel.getEjbRelationName()), new DescriptorErrorInfo("<relationship-role-source>", this.rel.getEjbRelationName(), this.src2.getEjbName())));
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkLocalBeanInRoleUses20CMP() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      EntityBeanInfo var2 = (EntityBeanInfo)this.di.getBeanInfo(this.src1.getEjbName());
      CMPInfo var3 = var2.getCMPInfo();
      if (!var3.uses20CMP()) {
         var1.add(new ComplianceException(this.fmt.CMP11_BEAN_IN_ROLE(this.rel.getEjbRelationName()), new DescriptorErrorInfo("<relationship-role-source>", this.rel.getEjbRelationName(), this.src1.getEjbName())));
      }

      var2 = (EntityBeanInfo)this.di.getBeanInfo(this.src2.getEjbName());
      var3 = var2.getCMPInfo();
      if (!var3.uses20CMP()) {
         var1.add(new ComplianceException(this.fmt.CMP11_BEAN_IN_ROLE(this.rel.getEjbRelationName()), new DescriptorErrorInfo("<relationship-role-source>", this.rel.getEjbRelationName(), this.src2.getEjbName())));
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkAtLeastUniDirectional() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      if (this.field1 == null && this.field2 == null) {
         var1.add(new ComplianceException(this.fmt.FIELD_NOT_DEFINED_FOR_ROLE(this.rel.getEjbRelationName()), new DescriptorErrorInfo("<cmr-field>", this.rel.getEjbRelationName(), this.rel.getEjbRelationName())));
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkManyRoleHasType(EjbRelationshipRole var1, EjbRelationshipRole var2, ErrorCollectionException var3) {
      CmrField var4 = var1.getCmrField();
      if (var2.getMultiplicity().equalsIgnoreCase("many") && var4 != null && var4.getType() == null) {
         var3.add(new ComplianceException(this.fmt.COLLECTION_FIELD_HAS_NO_TYPE(this.rel.getEjbRelationName()), new DescriptorErrorInfo("<cmr-field>", this.rel.getEjbRelationName(), this.rel.getEjbRelationName())));
      }

   }

   private void checkOneRoleHasNoType(EjbRelationshipRole var1, EjbRelationshipRole var2, ErrorCollectionException var3) {
      CmrField var4 = var1.getCmrField();
      if (var2.getMultiplicity().equalsIgnoreCase("one") && var4 != null && var4.getType() != null) {
         var3.add(new ComplianceException(this.fmt.SINGLETON_FIELD_HAS_TYPE(this.rel.getEjbRelationName()), new DescriptorErrorInfo("<cmr-field>", this.rel.getEjbRelationName(), this.rel.getEjbRelationName())));
      }

   }

   private void checkManyHasType() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      this.checkManyRoleHasType(this.role1, this.role2, var1);
      this.checkManyRoleHasType(this.role2, this.role1, var1);
      this.checkOneRoleHasNoType(this.role1, this.role2, var1);
      this.checkOneRoleHasNoType(this.role2, this.role1, var1);
      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkThatRoleFieldExistsOnBean(CmrField var1, RoleSource var2, RoleSource var3, ErrorCollectionException var4) {
      if (var1 != null) {
         EntityBeanInfo var5 = (EntityBeanInfo)this.di.getBeanInfo(var2.getEjbName());
         Class var6 = var5.getBeanClass();
         String var7 = var5.getEJBName();
         String var8 = var1.getName();
         String var9 = MethodUtils.getMethodName(var8);
         boolean var10 = false;
         CMPInfo var11 = var5.getCMPInfo();

         assert var11 != null;

         if (var11 != null) {
            var10 = var11.isBeanClassAbstract();
         }

         Class var12 = null;
         EntityBeanInfo var13;
         if (var1.getType() == null) {
            var13 = (EntityBeanInfo)this.di.getBeanInfo(var3.getEjbName());
            if (var13.hasLocalClientView()) {
               var12 = var13.getLocalInterfaceClass();
            } else if (var13.hasRemoteClientView()) {
               var12 = var13.getRemoteInterfaceClass();
            } else {
               var12 = var13.getBeanClass();
            }
         } else if (var1.getType().equals("java.util.Collection")) {
            var12 = Collection.class;
         } else {
            var12 = Set.class;
         }

         var13 = null;
         Method var15 = PersistenceUtils.getMethodIncludeSuper(var6, var9, new Class[0]);
         if (var15 == null) {
            var4.add(new ComplianceException(this.fmt.GET_METHOD_NOT_DEFINED_FOR_ROLE(var7, var9, this.rel.getEjbRelationName()), new DescriptorErrorInfo("<cmr-field>", this.rel.getEjbRelationName(), var9)));
         }

         if (var15 != null && !var15.getReturnType().equals(var12)) {
            var4.add(new ComplianceException(this.fmt.GET_METHOD_HAS_WRONG_RETURN_TYPE(var7, var9, this.rel.getEjbRelationName())));
         }

         int var14;
         if (var15 != null) {
            var14 = var15.getModifiers();
            if (!Modifier.isAbstract(var14) && var10) {
               var4.add(new ComplianceException(this.fmt.GET_METHOD_IS_NOT_ABSTRACT(var7, var9, this.rel.getEjbRelationName())));
            }
         }

         if (var15 != null) {
            var14 = var15.getModifiers();
            if (!Modifier.isPublic(var14)) {
               var4.add(new ComplianceException(this.fmt.GET_METHOD_IS_NOT_PUBLIC(var7, var9, this.rel.getEjbRelationName())));
            }
         }

         var9 = MethodUtils.setMethodName(var8);
         var15 = PersistenceUtils.getMethodIncludeSuper(var6, var9, new Class[]{var12});
         if (var15 == null) {
            var4.add(new ComplianceException(this.fmt.SET_METHOD_NOT_DEFINED_FOR_ROLE(var7, var9, this.rel.getEjbRelationName()), new DescriptorErrorInfo("<cmr-field>", this.rel.getEjbRelationName(), var9)));
         }

         if (var15 != null && !var15.getReturnType().equals(Void.TYPE)) {
            var4.add(new ComplianceException(this.fmt.SET_METHOD_HAS_WRONG_RETURN_TYPE(var7, var9, this.rel.getEjbRelationName())));
         }

         if (var15 != null) {
            var14 = var15.getModifiers();
            if (!Modifier.isAbstract(var14) && var10) {
               var4.add(new ComplianceException(this.fmt.SET_METHOD_IS_NOT_ABSTRACT(var7, var9, this.rel.getEjbRelationName())));
            }
         }

         if (var15 != null) {
            var14 = var15.getModifiers();
            if (!Modifier.isPublic(var14)) {
               var4.add(new ComplianceException(this.fmt.SET_METHOD_IS_NOT_PUBLIC(var7, var9, this.rel.getEjbRelationName())));
            }
         }
      }

   }

   private void checkCmrFieldExists() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      this.checkThatRoleFieldExistsOnBean(this.field1, this.src1, this.src2, var1);
      this.checkThatRoleFieldExistsOnBean(this.field2, this.src2, this.src1, var1);
      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkCmrFieldNotCmp(CmrField var1, RoleSource var2, ErrorCollectionException var3) {
      if (var1 != null) {
         EntityBeanInfo var4 = (EntityBeanInfo)this.di.getBeanInfo(var2.getEjbName());
         CMPInfo var5 = var4.getCMPInfo();
         Collection var6 = var5.getAllContainerManagedFieldNames();
         String var7 = var1.getName();
         if (var6.contains(var7)) {
            var3.add(new ComplianceException(this.fmt.CMR_FIELD_SAME_AS_CMP_FIELD(this.rel.getEjbRelationName()), new DescriptorErrorInfo("<cmr-field>", this.rel.getEjbRelationName(), this.rel.getEjbRelationName())));
         }
      }

   }

   private void checkCmrFieldNotACmpField() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      this.checkCmrFieldNotCmp(this.field1, this.src1, var1);
      this.checkCmrFieldNotCmp(this.field2, this.src2, var1);
      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkEjbCascadeDelete() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      if (this.role1.getMultiplicity().equalsIgnoreCase("many") && this.role2.getCascadeDelete()) {
         var1.add(new ComplianceException(this.fmt.CASCADE_DELETE_CANNOT_BE_SPECIFIED(this.role2.getName()), new DescriptorErrorInfo("<ejb-relationship-role>", this.rel.getEjbRelationName(), this.role2.getName())));
      }

      if (this.role2.getMultiplicity().equalsIgnoreCase("many") && this.role1.getCascadeDelete()) {
         var1.add(new ComplianceException(this.fmt.CASCADE_DELETE_CANNOT_BE_SPECIFIED(this.role1.getName()), new DescriptorErrorInfo("<ejb-relationship-role>", this.rel.getEjbRelationName(), this.role1.getName())));
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }
}
