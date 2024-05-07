package weblogic.ejb.container.cmp.rdbms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import weblogic.ejb.container.cmp.rdbms.finders.RDBMSFinder;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.j2ee.descriptor.wl.AutomaticKeyGenerationBean;
import weblogic.j2ee.descriptor.wl.CachingElementBean;
import weblogic.j2ee.descriptor.wl.ColumnMapBean;
import weblogic.j2ee.descriptor.wl.DatabaseSpecificSqlBean;
import weblogic.j2ee.descriptor.wl.FieldGroupBean;
import weblogic.j2ee.descriptor.wl.FieldMapBean;
import weblogic.j2ee.descriptor.wl.QueryMethodBean;
import weblogic.j2ee.descriptor.wl.RelationshipCachingBean;
import weblogic.j2ee.descriptor.wl.RelationshipRoleMapBean;
import weblogic.j2ee.descriptor.wl.SqlQueryBean;
import weblogic.j2ee.descriptor.wl.SqlShapeBean;
import weblogic.j2ee.descriptor.wl.TableBean;
import weblogic.j2ee.descriptor.wl.TableMapBean;
import weblogic.j2ee.descriptor.wl.UnknownPrimaryKeyFieldBean;
import weblogic.j2ee.descriptor.wl.WeblogicQueryBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsRelationBean;
import weblogic.j2ee.descriptor.wl.WeblogicRelationshipRoleBean;

public final class RDBMSDeploymentInfo {
   private WeblogicRdbmsJarBean wlJar;
   private Map rdbmsBeanMap = new HashMap();
   private Map rdbmsRelationMap = new HashMap();
   private String fileName;
   protected static EJBComplianceTextFormatter fmt;

   public RDBMSDeploymentInfo(WeblogicRdbmsJarBean var1, Map var2, String var3) throws RDBMSException {
      this.wlJar = var1;
      this.fileName = var3;
      fmt = new EJBComplianceTextFormatter();
      WeblogicRdbmsBeanBean[] var4 = var1.getWeblogicRdbmsBeans();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         CMPBeanDescriptor var6 = (CMPBeanDescriptor)var2.get(var4[var5].getEjbName());
         if (var6 != null) {
            boolean var7 = false;
            if (var6.getPrimaryKeyClassName().equals("java.lang.Object")) {
               UnknownPrimaryKeyFieldBean var8 = var4[var5].getUnknownPrimaryKeyField();
               if (var8 == null) {
                  throw new RDBMSException(fmt.UNKNOWN_PK_NEVER_ASSIGNED(var4[var5].getEjbName()));
               }

               var7 = !var6.getCMFieldNames().contains(var8.getCmpField());
               var6.setPrimaryKeyField(var8.getCmpField());
            }

            RDBMSBean var9 = new RDBMSBean();
            var9.setEjbName(var4[var5].getEjbName());
            var9.setDataSourceName(var4[var5].getDataSourceJndiName());
            this.processTableMaps(var4[var5], var9);
            this.processFieldGroups(var4[var5], var9);
            this.processRelationshipCaching(var4[var5], var9);
            this.processSqlShapes(var4[var5], var9);
            this.processQueries(var4[var5], var9, var6);
            var9.setDelayInsertUntil(var4[var5].getDelayDatabaseInsertUntil());
            var9.setUseSelectForUpdate(var4[var5].isUseSelectForUpdate());
            var9.setLockOrder(var4[var5].getLockOrder());
            var9.setInstanceLockOrder(var4[var5].getInstanceLockOrder());
            var9.setCheckExistsOnMethod(var4[var5].isCheckExistsOnMethod());
            var9.setClusterInvalidationDisabled(var4[var5].isClusterInvalidationDisabled());
            var9.setUseInnerJoin(var4[var5].isUseInnerJoin());
            var9.setCategoryCmpField(var4[var5].getCategoryCmpField());
            this.processAutoKeyGeneration(var4[var5], var9);
            if (var7 && !var9.hasAutoKeyGeneration()) {
               throw new RDBMSException(fmt.UNKNOWN_PK_WITHOUT_AUTO_KEY(var4[var5].getEjbName()));
            }

            this.rdbmsBeanMap.put(var9.getEjbName(), var9);
         }
      }

      this.processRelations(var1);
   }

   public RDBMSBean getRDBMSBean(String var1) {
      return (RDBMSBean)this.rdbmsBeanMap.get(var1);
   }

   public Map getRDBMSBeanMap() {
      return this.rdbmsBeanMap;
   }

   public Map getRDBMSRelationMap() {
      return this.rdbmsRelationMap;
   }

   private void processRelations(WeblogicRdbmsJarBean var1) throws RDBMSException {
      WeblogicRdbmsRelationBean[] var2 = var1.getWeblogicRdbmsRelations();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         RDBMSRelation var4 = new RDBMSRelation();
         var4.setName(var2[var3].getRelationName());
         var4.setTableName(var2[var3].getTableName());
         WeblogicRelationshipRoleBean[] var5 = var2[var3].getWeblogicRelationshipRoles();
         HashSet var6 = new HashSet();

         for(int var7 = 0; var7 < var5.length; ++var7) {
            RDBMSRelation.RDBMSRole var8 = new RDBMSRelation.RDBMSRole();
            String var9 = var5[var7].getRelationshipRoleName();
            if (!var6.add(var9)) {
               throw new RDBMSException(fmt.DuplicateRelationshipRoleNamesDetected(var2[var3].getRelationName(), var9));
            }

            var8.setName(var9);
            var8.setGroupName(var5[var7].getGroupName());
            var8.setDBCascadeDelete(var5[var7].getDbCascadeDelete() != null);
            var8.setQueryCachingEnabled(var5[var7].getEnableQueryCaching());
            RelationshipRoleMapBean var10 = var5[var7].getRelationshipRoleMap();
            if (var10 != null) {
               var8.setForeignKeyTableName(var10.getForeignKeyTable());
               var8.setPrimaryKeyTableName(var10.getPrimaryKeyTable());
               ColumnMapBean[] var11 = var10.getColumnMaps();

               for(int var12 = 0; var12 < var11.length; ++var12) {
                  Map var13 = var8.getColumnMap();
                  var13.put(var11[var12].getForeignKeyColumn(), var11[var12].getKeyColumn());
               }
            }

            if (null == var4.getRole1()) {
               var4.setRole1(var8);
            } else {
               var4.setRole2(var8);
            }
         }

         this.rdbmsRelationMap.put(var4.getName(), var4);
      }

   }

   private void processAutoKeyGeneration(WeblogicRdbmsBeanBean var1, RDBMSBean var2) {
      AutomaticKeyGenerationBean var3 = var1.getAutomaticKeyGeneration();
      if (var3 != null) {
         var2.setGenKeyType(var3.getGeneratorType());
         var2.setGenKeyGeneratorName(var3.getGeneratorName());
         var2.setGenKeyCacheSize(var3.getKeyCacheSize());
         var2.setSelectFirstSeqKeyBeforeUpdate(var3.getSelectFirstSequenceKeyBeforeUpdate());
      }

   }

   private void processQueries(WeblogicRdbmsBeanBean var1, RDBMSBean var2, CMPBeanDescriptor var3) throws RDBMSException {
      WeblogicQueryBean[] var4 = var1.getWeblogicQueries();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         RDBMSFinder var6 = new RDBMSFinder();
         QueryMethodBean var7 = var4[var5].getQueryMethod();
         var6.setFinderName(var7.getMethodName());
         var6.setFinderParams(var7.getMethodParams().getMethodParams());
         if (var4[var5].getEjbQlQuery() != null) {
            var6.setEjbQlQuery(var4[var5].getEjbQlQuery().getWeblogicQl());
            var6.setGroupName(var4[var5].getEjbQlQuery().getGroupName());
            var6.setCachingName(var4[var5].getEjbQlQuery().getCachingName());
         } else if (var4[var5].getSqlQuery() != null) {
            SqlQueryBean var8 = var4[var5].getSqlQuery();
            var6.setSqlShapeName(var8.getSqlShapeName());
            HashMap var9 = new HashMap();
            if (var8.getSql() != null) {
               var9.put(new Integer(0), var8.getSql());
            }

            DatabaseSpecificSqlBean[] var10 = var8.getDatabaseSpecificSqls();

            for(int var11 = 0; var11 < var10.length; ++var11) {
               int var12 = MethodUtils.dbmsType2int(var10[var11].getDatabaseType());
               var9.put(new Integer(var12), var10[var11].getSql());
            }

            var6.setSqlQueries(var9);
         }

         var6.setMaxElements(var4[var5].getMaxElements());
         if (var3.getConcurrencyStrategy() == 6) {
            if (var4[var5].isIncludeUpdatesSet()) {
               var6.setIncludeUpdates(var4[var5].isIncludeUpdates());
            } else {
               var6.setIncludeUpdates(false);
            }
         } else {
            var6.setIncludeUpdates(var4[var5].isIncludeUpdates());
         }

         var6.setSqlSelectDistinct(var4[var5].isSqlSelectDistinct());
         var6.setQueryCachingEnabled(var4[var5].getEnableQueryCaching());
         var6.setEnableEagerRefresh(var4[var5].getEnableEagerRefresh());
         var6.setIncludeResultCacheHint(var4[var5].isIncludeResultCacheHint());
         if (var2.containsRdbmsFinder(var6)) {
            throw new RDBMSException(fmt.DuplicateWeblogicQueryElementsDetected(var6.toString(), var2.getEjbName(), this.fileName));
         }

         var2.addRdbmsFinder(var6);
      }

   }

   private void processRelationshipCaching(WeblogicRdbmsBeanBean var1, RDBMSBean var2) throws RDBMSException {
      HashSet var3 = new HashSet();
      RelationshipCachingBean[] var4 = var1.getRelationshipCachings();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         RelationshipCaching var6 = new RelationshipCaching();
         if (!var3.add(var4[var5].getCachingName())) {
            throw new RDBMSException("Duplicate caching name '" + var4[var5].getCachingName() + "' found in RDBMS CMP deployment descriptor '" + this.fileName + "' while reading information for '" + var1.getEjbName() + "'.");
         }

         var6.setCachingName(var4[var5].getCachingName());
         CachingElementBean[] var7 = var4[var5].getCachingElements();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            var6.addCachingElement(this.processCachingElement(var7[var8]));
         }

         var2.addRelationshipCaching(var6);
      }

   }

   private RelationshipCaching.CachingElement processCachingElement(CachingElementBean var1) {
      RelationshipCaching.CachingElement var2 = new RelationshipCaching.CachingElement();
      var2.setCmrField(var1.getCmrField());
      var2.setGroupName(var1.getGroupName());
      if (var1.getGroupName() == null || var1.getGroupName().length() == 0) {
         var2.setGroupName("defaultGroup");
      }

      CachingElementBean[] var3 = var1.getCachingElements();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2.addCachingElement(this.processCachingElement(var3[var4]));
      }

      return var2;
   }

   private void processSqlShapes(WeblogicRdbmsBeanBean var1, RDBMSBean var2) throws RDBMSException {
      HashSet var3 = new HashSet();
      SqlShapeBean[] var4 = var1.getSqlShapes();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         SqlShape var6 = new SqlShape();
         if (!var3.add(var4[var5].getSqlShapeName())) {
            throw new RDBMSException(fmt.DUPLICATE_SQL_CACHING_NAME(var1.getEjbName(), this.fileName, var4[var5].getSqlShapeName()));
         }

         var6.setSqlShapeName(var4[var5].getSqlShapeName());
         TableBean[] var7 = var4[var5].getTables();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            SqlShape.Table var9 = new SqlShape.Table();
            var9.setName(var7[var8].getTableName());
            List var10 = Arrays.asList((Object[])var7[var8].getDbmsColumns());
            var9.setColumns(var10);
            if (var7[var8].getEjbRelationshipRoleName() != null) {
               var9.addEjbRelationshipRoleName(var7[var8].getEjbRelationshipRoleName());
            }

            var6.addTable(var9);
         }

         var6.setEjbRelationNames(var4[var5].getEjbRelationNames());
         var6.setPassThroughColumns(var4[var5].getPassThroughColumns());
         var2.addSqlShape(var6);
      }

   }

   private void processFieldGroups(WeblogicRdbmsBeanBean var1, RDBMSBean var2) throws RDBMSException {
      HashSet var3 = new HashSet();
      FieldGroupBean[] var4 = var1.getFieldGroups();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (!var3.add(var4[var5].getGroupName())) {
            throw new RDBMSException("Duplicate group name '" + var4[var5].getGroupName() + "' found in RDBMS CMP deployment descriptor '" + this.fileName + "' while reading information for '" + var1.getEjbName() + "'.");
         }

         FieldGroup var6 = new FieldGroup();
         var6.setName(var4[var5].getGroupName());
         String[] var7 = var4[var5].getCmpFields();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            var6.addCmpField(var7[var8]);
         }

         String[] var10 = var4[var5].getCmrFields();

         for(int var9 = 0; var9 < var10.length; ++var9) {
            var6.addCmrField(var10[var9]);
         }

         var2.addFieldGroup(var6);
      }

   }

   private void processTableMaps(WeblogicRdbmsBeanBean var1, RDBMSBean var2) throws RDBMSException {
      TableMapBean[] var3 = var1.getTableMaps();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         String var5 = var3[var4].getTableName();
         if (var2.hasTable(var5)) {
            throw new RDBMSException("In EJB, " + var1.getEjbName() + ", an attempt was made to specify a <table-name> of, " + var5 + ", in multiple <table-map> elements. " + "Each <table-map> element must specify a unique <table-name> " + "and should contain all <field-map> elements for that table.");
         }

         var2.addTable(var5);
         FieldMapBean[] var6 = var3[var4].getFieldMaps();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            var2.addTableFieldColumnMapping(var5, var6[var7].getCmpField(), var6[var7].getDbmsColumn());
            if (var6[var7].getDbmsColumnType() != null) {
               var2.addFieldColumnTypeMapping(var6[var7].getCmpField(), var6[var7].getDbmsColumnType());
            }

            if (var6[var7].isDbmsDefaultValue()) {
               var2.addDbmsDefaultValueField(var6[var7].getCmpField());
            }
         }

         if (var3[var4].getVerifyRows() != null) {
            var2.setVerifyRows(var5, var3[var4].getVerifyRows());
         }

         if (var3[var4].getVerifyColumns() != null) {
            var2.setVerifyColumns(var5, var3[var4].getVerifyColumns());
         }

         if (var3[var4].getOptimisticColumn() != null) {
            var2.setOptimisticColumn(var5, var3[var4].getOptimisticColumn());
         }

         var2.setTriggerUpdatesOptimisticColumn(var5, var3[var4].isTriggerUpdatesOptimisticColumn());
         var2.setVersionColumnInitialValue(var5, var3[var4].getVersionColumnInitialValue());
      }

   }
}
