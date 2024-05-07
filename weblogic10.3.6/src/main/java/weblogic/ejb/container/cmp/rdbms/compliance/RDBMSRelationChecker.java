package weblogic.ejb.container.cmp.rdbms.compliance;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.cmp.rdbms.FieldGroup;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSRelation;
import weblogic.ejb.container.compliance.BaseComplianceChecker;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.j2ee.validation.ComplianceException;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.ErrorCollectionException;

public final class RDBMSRelationChecker extends BaseComplianceChecker {
   public static final boolean verbose = false;
   public static final boolean debug = false;
   private Map beanMap = null;
   private Map relationMap = null;
   private Map rdbmsBeanMap = null;
   private Map rdbmsRelationMap = null;
   private Method[] methods = null;
   private EjbRelation testEjbRel = null;
   private RDBMSRelation testRDBMSRel = null;
   private ErrorCollectionException errors = null;
   private static final short REL_IS_UNKNOWN = 0;
   private static final short REL_IS_1_1 = 1;
   private static final short REL_IS_1_N = 2;
   private static final short REL_IS_M_N = 3;

   RDBMSRelationChecker(Map var1, Map var2, Map var3, Map var4) {
      this.beanMap = var1;
      this.relationMap = var2;
      this.rdbmsBeanMap = var3;
      this.rdbmsRelationMap = var4;
      this.methods = this.getClass().getMethods();
      this.errors = new ErrorCollectionException();
   }

   public void runComplianceCheck() throws ErrorCollectionException {
      this.runWLDrivenCheck();
      if (!this.errors.isEmpty()) {
         throw this.errors;
      } else {
         this.runEJBDrivenCheck();
         if (!this.errors.isEmpty()) {
            throw this.errors;
         }
      }
   }

   private void runWLDrivenCheck() {
      Iterator var1 = this.rdbmsRelationMap.values().iterator();

      while(var1.hasNext()) {
         this.testRDBMSRel = (RDBMSRelation)var1.next();

         try {
            this.checkWLRelHasEjbRel();
            this.checkWLRelRolesHaveEJBRelRoles();
            if (!this.errors.isEmpty()) {
               return;
            }

            this.checkWLMNHasBothRoles();
            this.checkWLMNRelHasTableName();
            this.checkWLRolesHaveValidGroup();
            this.checkWLRolesHaveValidMap();
            this.checkWLDBCascadeDelete();
         } catch (ErrorCollectionException var3) {
            this.errors.add(var3);
         }
      }

   }

   private void runEJBDrivenCheck() {
      Iterator var1 = this.relationMap.values().iterator();

      while(var1.hasNext()) {
         this.testEjbRel = (EjbRelation)var1.next();

         try {
            this.checkEjbRelHasWLRel();
            this.checkEjb1NRoleHasWLRole();
            this.checkEjbNo1MappingOn1N();
            this.checkEjb1To1RelHasWLRoleMap();
            this.checkRelatedBeansSameDataSource();
         } catch (ErrorCollectionException var3) {
            this.errors.add(var3);
         }
      }

   }

   private void checkEjb1NRoleHasWLRole() throws ErrorCollectionException {
      if (this.getRelType(this.testEjbRel) == 2) {
         ErrorCollectionException var1 = new ErrorCollectionException();
         EjbRelationshipRole var2 = this.getManyEjbRole(this.testEjbRel);
         if (var2 == null) {
            var1.add(new ComplianceException(this.fmt.roleNotFound(this.testEjbRel.getEjbRelationName())));
            throw var1;
         } else {
            String var3 = var2.getName();
            RDBMSRelation.RDBMSRole var4 = this.getRDBMSRoleForEjbRole(this.testEjbRel, var2);
            if (var4 == null) {
               var1.add(new ComplianceException(this.fmt.NO_MATCHING_WL_RELATIONSHIP_ROLE(this.testEjbRel.getEjbRelationName(), var3), new DescriptorErrorInfo("<weblogic-relationship-role>", this.testEjbRel.getEjbRelationName(), var3)));
               throw var1;
            } else {
               Map var5 = var4.getColumnMap();
               if (var5 == null) {
                  var1.add(new ComplianceException(this.fmt.MANY_SIDE_OF_M_1_MUST_HAVE_FOREIGN_KEY_MAP(var4.getName()), new DescriptorErrorInfo("<column-map>", this.testEjbRel.getEjbRelationName(), var4.getName())));
               } else if (var5.size() < 1) {
                  var1.add(new ComplianceException(this.fmt.MANY_SIDE_OF_M_1_MUST_HAVE_FOREIGN_KEY_MAP(var4.getName()), new DescriptorErrorInfo("<column-map>", this.testEjbRel.getEjbRelationName(), var4.getName())));
               }

               if (!var1.isEmpty()) {
                  throw var1;
               }
            }
         }
      }
   }

   private void checkEjbNo1MappingOn1N() throws ErrorCollectionException {
      if (this.getRelType(this.testEjbRel) == 2) {
         ErrorCollectionException var1 = new ErrorCollectionException();
         EjbRelationshipRole var2 = this.getOneEjbRole(this.testEjbRel);
         EjbRelationshipRole var3 = this.getManyEjbRole(this.testEjbRel);
         if (var2 == null) {
            throw new AssertionError(this.testEjbRel.getEjbRelationName() + ": " + " checker could not find the EjbRelationShip ONE ROLE " + "in a 1-N Relationship");
         } else if (var3 == null) {
            throw new AssertionError(this.testEjbRel.getEjbRelationName() + ": " + " checker could not find the EjbRelationShip MANY ROLE " + "in a 1-N Relationship");
         } else {
            RoleSource var4 = var3.getRoleSource();
            String var5 = var2.getName();
            RDBMSRelation.RDBMSRole var6 = this.getRDBMSRoleForEjbRole(this.testEjbRel, var2);
            if (var6 != null) {
               Map var7 = var6.getColumnMap();
               if (var7 != null && var7.size() > 0) {
                  var1.add(new ComplianceException(this.fmt.ONE_SIDE_OF_M_1_MUST_NOT_HAVE_FOREIGN_KEY_MAP(var6.getName()), new DescriptorErrorInfo("<column-map>", this.testEjbRel.getEjbRelationName(), var6.getName())));
               }

               if (!var1.isEmpty()) {
                  throw var1;
               }
            }
         }
      }
   }

   private void checkEjb1To1RelHasWLRoleMap() throws ErrorCollectionException {
      if (this.getRelType(this.testEjbRel) == 1) {
         Iterator var1 = this.testEjbRel.getAllEjbRelationshipRoles().iterator();

         while(var1.hasNext()) {
            EjbRelationshipRole var2 = (EjbRelationshipRole)var1.next();
            RDBMSRelation.RDBMSRole var3 = this.getRDBMSRoleForEjbRole(this.testEjbRel, var2);
            if (var3 != null) {
               Map var4 = var3.getColumnMap();
               if (var4 != null && var4.size() > 0) {
                  return;
               }
            }
         }

         this.errors.add(new ComplianceException(this.fmt.missingRelationshipRoleMap(this.testEjbRel.getEjbRelationName())));
      }
   }

   private void checkEjbRelHasWLRel() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      String var2 = this.testEjbRel.getEjbRelationName();
      if (!this.rdbmsRelationMap.containsKey(var2)) {
         var1.add(new ComplianceException(this.fmt.NO_MATCHING_WL_RELATION(var2), new DescriptorErrorInfo("<weblogic-rdbms-relation>", this.testEjbRel.getEjbRelationName(), var2)));
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkWLRelHasEjbRel() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      String var2 = this.testRDBMSRel.getName();
      if (!this.relationMap.containsKey(var2)) {
         var1.add(new ComplianceException(this.fmt.NO_MATCHING_EJB_RELATION_IN_EJB_DD(var2)));
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkWLRelRolesHaveEJBRelRoles() {
      new ErrorCollectionException();
      String var2 = this.testRDBMSRel.getName();
      EjbRelation var3 = (EjbRelation)this.relationMap.get(var2);
      RDBMSRelation.RDBMSRole var4 = this.testRDBMSRel.getRole1();
      if (var4 != null) {
         this.doWLRoleCheck(var4, var3, this.errors);
      }

      var4 = this.testRDBMSRel.getRole2();
      if (var4 != null) {
         this.doWLRoleCheck(var4, var3, this.errors);
      }

   }

   private void checkWLMNHasBothRoles() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      String var2 = this.testRDBMSRel.getName();
      EjbRelation var3 = (EjbRelation)this.relationMap.get(var2);
      if (this.getRelType(var3) == 3) {
         RDBMSRelation.RDBMSRole var4 = this.testRDBMSRel.getRole1();
         if (var4 == null) {
            var1.add(new ComplianceException(this.fmt.MANY_TO_MANY_RELATIONSHIP_MUST_HAVE_BOTH_WL_ROLES(var2), new DescriptorErrorInfo("<weblogic-relationship-role>", var3.getEjbRelationName(), var2)));
            throw var1;
         }

         var4 = this.testRDBMSRel.getRole2();
         if (var4 == null) {
            var1.add(new ComplianceException(this.fmt.MANY_TO_MANY_RELATIONSHIP_MUST_HAVE_BOTH_WL_ROLES(var2), new DescriptorErrorInfo("<weblogic-relationship-role>", var3.getEjbRelationName(), var2)));
            throw var1;
         }
      }

   }

   private void checkWLMNRelHasTableName() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      String var2 = this.testRDBMSRel.getName();
      EjbRelation var3 = (EjbRelation)this.relationMap.get(var2);
      if (this.getRelType(var3) == 3) {
         String var4 = this.testRDBMSRel.getTableName();
         if (var4 == null) {
            var1.add(new ComplianceException(this.fmt.MANY_TO_MANY_RELATIONSHIP_MISSING_TABLE_NAME(var2), new DescriptorErrorInfo("<table-name>", var3.getEjbRelationName(), var2)));
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkWLRolesHaveValidGroup() {
      String var1 = this.testRDBMSRel.getName();
      EjbRelation var2 = (EjbRelation)this.relationMap.get(var1);
      Iterator var3 = var2.getAllEjbRelationshipRoles().iterator();

      while(var3.hasNext()) {
         EjbRelationshipRole var4 = (EjbRelationshipRole)var3.next();
         RDBMSRelation.RDBMSRole var5 = this.getRDBMSRoleForEjbRole(var2, var4);
         if (var5 != null) {
            String var6 = var5.getGroupName();
            if (var6 != null) {
               RoleSource var7 = var4.getRoleSource();
               FieldGroup var8 = null;
               RDBMSBean var9 = (RDBMSBean)this.rdbmsBeanMap.get(var7.getEjbName());
               var8 = var9.getFieldGroup(var6);
               if (var8 == null) {
                  this.errors.add(new ComplianceException(this.fmt.RELATIONSHIP_ROLE_HAS_INVALID_GROUP(var1, var6), new DescriptorErrorInfo("<weblogic-relationship-role>", var2.getEjbRelationName(), var1)));
               }
            }
         }
      }

   }

   private void checkWLRolesHaveValidMap() throws ErrorCollectionException {
      String var1 = this.testRDBMSRel.getName();
      EjbRelation var2 = (EjbRelation)this.relationMap.get(var1);
      Iterator var3 = var2.getAllEjbRelationshipRoles().iterator();
      EjbRelationshipRole var4 = (EjbRelationshipRole)var3.next();
      EjbRelationshipRole var5 = (EjbRelationshipRole)var3.next();
      RDBMSRelation.RDBMSRole var6 = this.getRDBMSRoleForEjbRole(var2, var4);
      RDBMSRelation.RDBMSRole var7 = this.getRDBMSRoleForEjbRole(var2, var5);
      this.checkWLRoleHasValidTableName(var2, var4, var5, var6);
      this.checkWLRoleHasValidTableName(var2, var5, var4, var7);
      this.checkWLRolesHaveMap(var2, var4, var5, var6, var7);
      if (this.errors.isEmpty()) {
         this.checkWLRoleHasValidMap(var2, var4, var5, var6);
         this.checkWLRoleHasValidMap(var2, var5, var4, var7);
         this.checkWLRoleHasValidFkColumns(var2, var4, var6, var5);
         this.checkWLRoleHasValidFkColumns(var2, var5, var7, var4);
         this.checkForeignKeyTableSpecified(var2, var4, var6);
         this.checkForeignKeyTableSpecified(var2, var5, var7);
      }
   }

   private void checkWLDBCascadeDelete() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      String var2 = this.testRDBMSRel.getName();
      EjbRelation var3 = (EjbRelation)this.relationMap.get(var2);
      short var4 = this.getRelType(var3);
      Iterator var5 = var3.getAllEjbRelationshipRoles().iterator();
      if (var4 != 3) {
         while(var5.hasNext()) {
            EjbRelationshipRole var6 = (EjbRelationshipRole)var5.next();
            RDBMSRelation.RDBMSRole var7 = this.getRDBMSRoleForEjbRole(var3, var6);
            if (var7 != null) {
               if (var7.getDBCascadeDelete() && !var6.getCascadeDelete()) {
                  var1.add(new ComplianceException(this.fmt.CASCADE_DELETE_MUST_SPECIFIED_IF_DB_CASCADE_DELETE_SPECIFIED(var2), new DescriptorErrorInfo("<cascade-delete>", var3.getEjbRelationName(), var2)));
               }

               if (var7.getDBCascadeDelete()) {
                  Map var8 = var7.getColumnMap();
                  if (var8 == null) {
                     var1.add(new ComplianceException(this.fmt.CASCADE_DELETE_MUST_HAVE_FOREIGN_KEY_MAP(var7.getName()), new DescriptorErrorInfo("<foreign-key-column>", var7.getName(), var7.getName())));
                  }

                  if (var8 != null && var8.size() < 1) {
                     var1.add(new ComplianceException(this.fmt.CASCADE_DELETE_MUST_HAVE_FOREIGN_KEY_MAP(var7.getName()), new DescriptorErrorInfo("<foreign-key-column>", var7.getName(), var7.getName())));
                  }
               }
            }
         }

         if (!var1.isEmpty()) {
            throw var1;
         }
      }
   }

   private void checkWLRolesHaveMap(EjbRelation var1, EjbRelationshipRole var2, EjbRelationshipRole var3, RDBMSRelation.RDBMSRole var4, RDBMSRelation.RDBMSRole var5) {
      String var6 = var1.getEjbRelationName();
      short var7 = this.getRelType(var1);
      int var8;
      if (var7 == 1) {
         var8 = 0;
         if (var4 != null && var4.getColumnMap().size() > 0) {
            ++var8;
         }

         if (var5 != null && var5.getColumnMap().size() > 0) {
            ++var8;
         }

         if (var8 == 0) {
            this.errors.add(new ComplianceException(this.fmt.ONE_ONE_MISSING_COLUMN_MAP(var6)));
         }
      } else if (var7 == 3) {
         var8 = 0;
         if (var4 != null && var4.getColumnMap().size() > 0) {
            ++var8;
         }

         if (var5 != null && var5.getColumnMap().size() > 0) {
            ++var8;
         }

         if (var8 < 2) {
            this.errors.add(new ComplianceException(this.fmt.MANY_MANY_MISSING_COLUMN_MAP(var6)));
         }
      }

   }

   private void checkWLRoleHasValidTableName(EjbRelation var1, EjbRelationshipRole var2, EjbRelationshipRole var3, RDBMSRelation.RDBMSRole var4) throws ErrorCollectionException {
      if (var4 != null) {
         boolean var5 = false;
         short var6 = this.getRelType(var1);
         String var7 = var1.getEjbRelationName();
         String var8 = var2.getName();
         RoleSource var9 = var2.getRoleSource();
         String var10 = var9.getEjbName();
         RDBMSBean var11 = (RDBMSBean)this.rdbmsBeanMap.get(var10);
         RoleSource var12 = var3.getRoleSource();
         String var13 = var12.getEjbName();
         RDBMSBean var14 = (RDBMSBean)this.rdbmsBeanMap.get(var13);
         String var15 = null;
         String var16 = null;
         var15 = var4.getPrimaryKeyTableName();
         var16 = var4.getForeignKeyTableName();
         RDBMSBean var17;
         EjbRelationshipRole var18;
         String var19;
         if (var15 != null) {
            if (var6 == 1) {
               if (!var11.hasTable(var15) && !var14.hasTable(var15)) {
                  this.errors.add(new ComplianceException(this.fmt.ROLE_11_SPECIFIES_INVALID_PK_TABLE_NAME(var7, var8, var15, var10, var13), new DescriptorErrorInfo("<weblogic-relationship-role>", var7, var8)));
                  var5 = true;
               }
            } else if (var6 == 2) {
               var17 = null;
               var18 = this.getOneEjbRole(var1);
               Debug.assertion(var18 != null);
               var19 = var18.getName();
               if (var8.equals(var19)) {
                  var17 = var11;
               } else {
                  var17 = var14;
               }

               if (!var17.hasTable(var15)) {
                  this.errors.add(new ComplianceException(this.fmt.ROLE_1N_SPECIFIES_INVALID_PK_TABLE_NAME(var7, var19, var15, var17.getEjbName()), new DescriptorErrorInfo("<weblogic-relationship-role>", var7, var8)));
                  var5 = true;
               }
            } else if (var6 == 3 && !var11.hasTable(var15)) {
               this.errors.add(new ComplianceException(this.fmt.ROLE_MN_SPECIFIES_INVALID_PK_TABLE_NAME(var7, var8, var15, var10), new DescriptorErrorInfo("<weblogic-relationship-role>", var7, var8)));
               var5 = true;
            }
         }

         if (var16 != null) {
            if (var6 == 1) {
               if (!var11.hasTable(var16) && !var14.hasTable(var16)) {
                  this.errors.add(new ComplianceException(this.fmt.ROLE_11_SPECIFIES_INVALID_FK_TABLE_NAME(var7, var8, var16, var10, var13), new DescriptorErrorInfo("<weblogic-relationship-role>", var7, var8)));
                  var5 = true;
               }
            } else if (var6 == 2) {
               var17 = null;
               var18 = this.getManyEjbRole(var1);
               Debug.assertion(var18 != null);
               var19 = var18.getName();
               if (var8.equals(var19)) {
                  var17 = var11;
               } else {
                  var17 = var14;
               }

               if (!var17.hasTable(var16)) {
                  this.errors.add(new ComplianceException(this.fmt.ROLE_1N_SPECIFIES_INVALID_FK_TABLE_NAME(var7, var19, var16, var17.getEjbName()), new DescriptorErrorInfo("<weblogic-relationship-role>", var7, var8)));
                  var5 = true;
               }
            } else if (var6 == 3) {
               this.errors.add(new ComplianceException(this.fmt.ROLE_MN_SPECIFIES_INVALID_FK_TABLE_NAME(var7, var8, var16), new DescriptorErrorInfo("<weblogic-relationship-role>", var7, var8)));
               var5 = true;
            }
         }

         if (var5) {
            throw this.errors;
         } else {
            boolean var20 = false;
            if (var6 == 1 && var16 != null && var15 != null) {
               var20 = true;
               if (var11.hasTable(var16)) {
                  if (var14.hasTable(var15)) {
                     var20 = false;
                  }
               } else if (var14.hasTable(var16) && var11.hasTable(var15)) {
                  var20 = false;
               }
            }

            if (var20) {
               this.errors.add(new ComplianceException(this.fmt.ROLE_11_SPECIFIES_FK_AND_PK_TABLE_NAMES_NOT_IN_DIFFERENT_ROLES(var7, var8, var15, var16), new DescriptorErrorInfo("<weblogic-relationship-role>", var7, var8)));
               throw this.errors;
            }
         }
      }
   }

   private void checkWLRoleHasValidMap(EjbRelation var1, EjbRelationshipRole var2, EjbRelationshipRole var3, RDBMSRelation.RDBMSRole var4) {
      String var5 = var1.getEjbRelationName();
      short var6 = this.getRelType(var1);
      RoleSource var7 = var2.getRoleSource();
      RoleSource var8 = var3.getRoleSource();
      Object var9 = null;
      Set var10 = null;
      String var11 = null;
      Map var12 = null;
      if (var4 != null) {
         var12 = var4.getColumnMap();
         var11 = var4.getPrimaryKeyTableName();
      }

      if (var6 == 3) {
         var10 = this.getPrimaryKeyColumns(var7.getEjbName(), var11);
      } else if ((var6 == 2 || var6 == 1) && var4 != null && var4.getColumnMap().size() > 0) {
         var10 = this.getPrimaryKeyColumns(var8.getEjbName(), var11);
      }

      if (var10 != null) {
         if (var12 == null) {
            this.errors.add(new ComplianceException(this.fmt.ROLE_HAS_NO_COLUMN_MAP(var5, var2.getName()), new DescriptorErrorInfo("<column-map>", var5, var2.getName())));
            return;
         }

         if (var10.size() != var12.size()) {
            this.errors.add(new ComplianceException(this.fmt.ROLE_HAS_WRONG_NUMBER_OF_COLUMNS_IN_MAP(var5, var2.getName()), new DescriptorErrorInfo("<weblogic-relationship-role>", var1.getEjbRelationName(), var2.getName())));
         } else {
            Iterator var13 = var12.values().iterator();

            while(var13.hasNext()) {
               String var14 = (String)var13.next();
               if (!var10.contains(var14)) {
                  this.errors.add(new ComplianceException(this.fmt.ROLE_HAS_INVALID_KEY_COLUMN_IN_MAP2(var5, var2.getName(), var14, var8.getEjbName()), new DescriptorErrorInfo("<column-map>", var5, var2.getName())));
               }
            }
         }
      }

   }

   private void checkWLRoleHasValidFkColumns(EjbRelation var1, EjbRelationshipRole var2, RDBMSRelation.RDBMSRole var3, EjbRelationshipRole var4) {
      String var5 = var1.getEjbRelationName();
      short var6 = this.getRelType(var1);
      RoleSource var7 = var2.getRoleSource();
      if ((var6 == 2 || var6 == 1) && var3 != null && var3.getColumnMap().size() > 0) {
         String var10 = null;
         RDBMSBean var11 = (RDBMSBean)this.rdbmsBeanMap.get(var7.getEjbName());
         Set var8;
         Set var9;
         if (var11.hasMultipleTables()) {
            var10 = var3.getForeignKeyTableName();
            if (var10 == null) {
               this.errors.add(new ComplianceException(this.fmt.MISSING_FK_TABLE_NAME_FOR_MULTITABLE_BEAN(var5, var2.getName(), var7.getEjbName()), new DescriptorErrorInfo("<foreign-key-column>", var5, var2.getName())));
               return;
            }

            var8 = this.getPrimaryKeyColumns(var7.getEjbName(), var10);
            var9 = this.getCmpColumns(var7.getEjbName(), var10);
         } else {
            var8 = this.getPrimaryKeyColumns(var7.getEjbName());
            var9 = this.getCmpColumns(var7.getEjbName());
         }

         Iterator var12 = var3.getColumnMap().keySet().iterator();
         int var13 = 0;
         int var14 = 0;

         while(true) {
            RDBMSBean var16;
            String var19;
            Class var20;
            String var23;
            Class var24;
            do {
               do {
                  CMPBeanDescriptor var17;
                  do {
                     String var21;
                     String var22;
                     do {
                        String var15;
                        do {
                           if (!var12.hasNext()) {
                              if (var13 != 0 && var14 != 0) {
                                 this.errors.add(new ComplianceException(this.fmt.FKCOLUMNS_MIX_OF_PK_NONPK_COLUMNS(var5, var2.getName()), new DescriptorErrorInfo("<foreign-key-column>", var5, var2.getName())));
                              }

                              return;
                           }

                           var15 = (String)var12.next();
                           if (var8.contains(var15)) {
                              ++var13;
                           }

                           if (!var8.contains(var15) && var9.contains(var15)) {
                              ++var14;
                           }
                        } while(!var9.contains(var15));

                        var16 = (RDBMSBean)this.rdbmsBeanMap.get(var4.getRoleSource().getEjbName());
                        var17 = (CMPBeanDescriptor)this.beanMap.get(var16.getEjbName());
                        CMPBeanDescriptor var18 = (CMPBeanDescriptor)this.beanMap.get(var11.getEjbName());
                        if (var10 == null) {
                           var10 = var11.getTableName();
                        }

                        var19 = var11.getCmpField(var10, var15);
                        var20 = var18.getFieldClass(var19);
                        var21 = var3.getPrimaryKeyTableName();
                        if (var21 == null) {
                           var21 = var16.getTableName();
                        }

                        var22 = (String)var3.getColumnMap().get(var15);
                     } while(var22 == null);

                     var23 = var16.getCmpField(var21, var22);
                  } while(var23 == null);

                  var24 = var17.getFieldClass(var23);
               } while(var24.equals(var20));

               if (var24.isPrimitive()) {
                  var24 = ClassUtils.getObjectClass(var24);
               }

               if (var20.isPrimitive()) {
                  var20 = ClassUtils.getObjectClass(var20);
               }
            } while(var20.isAssignableFrom(var24) && var24.isAssignableFrom(var20));

            this.errors.add(new ComplianceException(this.fmt.INVALID_FOREIGN_KEY_FIELD_CLASS(var11.getEjbName(), var19, var16.getEjbName(), var23), new DescriptorErrorInfo("<foreign-key-column>", var5, var2.getName())));
         }
      }
   }

   private void checkForeignKeyTableSpecified(EjbRelation var1, EjbRelationshipRole var2, RDBMSRelation.RDBMSRole var3) {
      String var4 = var1.getEjbRelationName();
      short var5 = this.getRelType(var1);
      if ((var5 == 2 || var5 == 1) && var3 != null && var3.getColumnMap().size() > 0) {
         RoleSource var6 = var2.getRoleSource();
         RDBMSBean var7 = (RDBMSBean)this.rdbmsBeanMap.get(var6.getEjbName());
         String var8 = null;
         if (var7.hasMultipleTables()) {
            var8 = var3.getForeignKeyTableName();
            if (var8 == null) {
               this.errors.add(new ComplianceException(this.fmt.MISSING_FK_TABLE_NAME_FOR_MULTITABLE_BEAN(var4, var2.getName(), var6.getEjbName()), new DescriptorErrorInfo("<foreign-key-column>", var4, var2.getName())));
               return;
            }
         }
      }

   }

   private void checkRelatedBeansSameDataSource() {
      new ErrorCollectionException();
      String var2 = this.testEjbRel.getEjbRelationName();
      EjbRelation var3 = (EjbRelation)this.relationMap.get(var2);
      Iterator var4 = var3.getAllEjbRelationshipRoles().iterator();
      EjbRelationshipRole var5 = (EjbRelationshipRole)var4.next();
      if (var4.hasNext()) {
         EjbRelationshipRole var6 = (EjbRelationshipRole)var4.next();
         RoleSource var7 = var5.getRoleSource();
         RoleSource var8 = var6.getRoleSource();
         String var9 = var7.getEjbName();
         String var10 = var8.getEjbName();
         RDBMSBean var11 = (RDBMSBean)this.rdbmsBeanMap.get(var9);
         RDBMSBean var12 = (RDBMSBean)this.rdbmsBeanMap.get(var10);
         String var13 = var11.getDataSourceName();
         String var14 = var12.getDataSourceName();
         if (var13 != null && var14 != null) {
            if (!var13.equals(var14)) {
               this.errors.add(new ComplianceException(this.fmt.RELATED_BEANS_MUST_SHARE_DATASOURCE(var2, var5.getName(), var9, var13, var6.getName(), var10, var14), new DescriptorErrorInfo("<weblogic-relationship-role>", var2, var5.getName())));
            }
         }
      }
   }

   private Set getPrimaryKeyColumns(String var1) {
      CMPBeanDescriptor var2 = (CMPBeanDescriptor)this.beanMap.get(var1);
      RDBMSBean var3 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      HashSet var4 = new HashSet();
      Iterator var5 = var2.getPrimaryKeyFieldNames().iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         String var7 = var3.getCmpColumnForField(var6);
         var4.add(var7);
      }

      return var4;
   }

   private Set getPrimaryKeyColumns(String var1, String var2) {
      if (var2 == null) {
         return this.getPrimaryKeyColumns(var1);
      } else {
         CMPBeanDescriptor var3 = (CMPBeanDescriptor)this.beanMap.get(var1);
         RDBMSBean var4 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
         HashSet var5 = new HashSet();
         Iterator var6 = var3.getPrimaryKeyFieldNames().iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            String var8 = var4.getColumnForCmpFieldAndTable(var7, var2);
            if (var8 != null) {
               var5.add(var8);
            }
         }

         return var5;
      }
   }

   private Set getCmpColumns(String var1) {
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      return new HashSet(var2.getCmpFieldToColumnMap().values());
   }

   private Set getCmpColumns(String var1, String var2) {
      if (var2 == null) {
         return this.getCmpColumns(var1);
      } else {
         RDBMSBean var3 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
         HashSet var4 = new HashSet();
         Map var5 = var3.getCmpField2ColumnMap(var2);
         Iterator var6 = var5.values().iterator();

         while(var6.hasNext()) {
            var4.add(var6.next());
         }

         return var4;
      }
   }

   private void doWLRoleCheck(RDBMSRelation.RDBMSRole var1, EjbRelation var2, ErrorCollectionException var3) {
      EjbRelationshipRole var4 = var2.getEjbRelationshipRole(var1.getName());
      if (var4 == null) {
         var3.add(new ComplianceException(this.fmt.NO_MATCHING_EJB_RELATIONSHIP_ROLE(var2.getEjbRelationName(), var1.getName()), new DescriptorErrorInfo("<ejb-relationship-role>", var2.getEjbRelationName(), var1.getName())));
      }

   }

   private EjbRelationshipRole getManyEjbRole(EjbRelation var1) {
      Iterator var2 = var1.getAllEjbRelationshipRoles().iterator();

      EjbRelationshipRole var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (EjbRelationshipRole)var2.next();
      } while(!var3.getMultiplicity().equalsIgnoreCase("many"));

      return var3;
   }

   private EjbRelationshipRole getOneEjbRole(EjbRelation var1) {
      Iterator var2 = var1.getAllEjbRelationshipRoles().iterator();

      EjbRelationshipRole var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (EjbRelationshipRole)var2.next();
      } while(!var3.getMultiplicity().equalsIgnoreCase("one"));

      return var3;
   }

   private RDBMSRelation.RDBMSRole getRDBMSRoleForEjbRole(EjbRelation var1, EjbRelationshipRole var2) {
      RDBMSRelation var3 = (RDBMSRelation)this.rdbmsRelationMap.get(var1.getEjbRelationName());
      if (var3 == null) {
         return null;
      } else {
         String var4 = var2.getName();
         RDBMSRelation.RDBMSRole var5 = var3.getRole1();
         if (var5 != null && var5.getName().compareTo(var4) == 0) {
            return var5;
         } else {
            var5 = var3.getRole2();
            return var5 != null && var5.getName().compareTo(var4) == 0 ? var5 : null;
         }
      }
   }

   private short getRelType(EjbRelation var1) {
      Collection var2 = var1.getAllEjbRelationshipRoles();
      if (var2.size() == 2) {
         short var3 = 0;
         short var4 = 0;
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            EjbRelationshipRole var6 = (EjbRelationshipRole)var5.next();
            if (var6.getMultiplicity().equalsIgnoreCase("one")) {
               ++var3;
            } else if (var6.getMultiplicity().equalsIgnoreCase("many")) {
               ++var4;
            }
         }

         if (var3 == 2) {
            return 1;
         }

         if (var4 == 2) {
            return 3;
         }

         if (var3 == 1 && var4 == 1) {
            return 2;
         }
      }

      return 0;
   }
}
