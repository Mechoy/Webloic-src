package weblogic.ejb.container.cmp.rdbms.compliance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.FieldGroup;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSRelation;
import weblogic.ejb.container.cmp.rdbms.RelationshipCaching;
import weblogic.ejb.container.cmp.rdbms.SqlShape;
import weblogic.ejb.container.cmp.rdbms.finders.RDBMSFinder;
import weblogic.ejb.container.compliance.BaseComplianceChecker;
import weblogic.ejb.container.dd.DDConstants;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.EntityBeanQuery;
import weblogic.ejb.container.persistence.PersistenceUtils;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.CmrField;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;
import weblogic.j2ee.validation.ComplianceException;
import weblogic.utils.AssertionError;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.StackTraceUtils;

public final class RDBMSBeanChecker extends BaseComplianceChecker {
   private Map beanMap = null;
   private Map relationMap = null;
   private Map rdbmsBeanMap = null;
   private Map rdbmsRelationMap = null;
   private Method[] methods = null;
   private WeblogicRdbmsJarBean cmpDesc = null;
   Set fieldNames = null;
   Set cmrFieldNames = null;
   private CMPBeanDescriptor testCMPB = null;
   private ErrorCollectionException errors = null;

   RDBMSBeanChecker(Map var1, Map var2, Map var3, Map var4, WeblogicRdbmsJarBean var5) {
      this.beanMap = var1;
      this.relationMap = var2;
      this.rdbmsBeanMap = var3;
      this.rdbmsRelationMap = var4;
      this.cmpDesc = var5;
      this.methods = this.getClass().getMethods();
      this.errors = new ErrorCollectionException();
   }

   public void runComplianceCheck() throws ErrorCollectionException {
      Iterator var1 = this.beanMap.values().iterator();

      while(var1.hasNext()) {
         this.testCMPB = (CMPBeanDescriptor)var1.next();
         this.computeFieldNames();

         for(int var2 = 0; var2 < this.methods.length; ++var2) {
            if (this.methods[var2].getName().startsWith("check")) {
               try {
                  this.methods[var2].invoke(this, (Object[])null);
               } catch (IllegalAccessException var5) {
                  throw new AssertionError(var5);
               } catch (InvocationTargetException var6) {
                  Throwable var4 = var6.getTargetException();
                  if (var4 instanceof ErrorCollectionException) {
                     this.errors.addCollection((ErrorCollectionException)var4);
                  } else if (var4 instanceof Exception) {
                     this.errors.add((Exception)var4);
                  } else {
                     this.errors.add(new Exception(StackTraceUtils.throwable2StackTrace(var4)));
                  }
               } catch (Throwable var7) {
                  if (var7 instanceof Exception) {
                     this.errors.add((Exception)var7);
                  } else {
                     this.errors.add(new Exception(StackTraceUtils.throwable2StackTrace(var7)));
                  }
               }
            }
         }
      }

      if (!this.errors.isEmpty()) {
         throw this.errors;
      }
   }

   private void computeFieldNames() {
      this.fieldNames = new HashSet();
      this.cmrFieldNames = new HashSet();
      Iterator var1 = this.relationMap.values().iterator();

      while(var1.hasNext()) {
         EjbRelation var2 = (EjbRelation)var1.next();
         Iterator var3 = var2.getAllEjbRelationshipRoles().iterator();
         EjbRelationshipRole var4 = (EjbRelationshipRole)var3.next();
         EjbRelationshipRole var5 = (EjbRelationshipRole)var3.next();
         RoleSource var6 = var4.getRoleSource();
         RoleSource var7 = var5.getRoleSource();
         CmrField var8 = var4.getCmrField();
         CmrField var9 = var5.getCmrField();
         this.perhapsAddCmrField(this.cmrFieldNames, var6, var8);
         this.perhapsAddCmrField(this.cmrFieldNames, var7, var9);
      }

      this.fieldNames.addAll(this.testCMPB.getCMFieldNames());
      this.fieldNames.addAll(this.cmrFieldNames);
   }

   private void perhapsAddCmrField(Set var1, RoleSource var2, CmrField var3) {
      if (var2.getEjbName().equals(this.testCMPB.getEJBName()) && var3 != null) {
         var1.add(var3.getName());
      }

   }

   public void checkBean() throws ComplianceException {
      this.checkEjBeanHasWLBean();
      this.checkNoExtraAbstractMethods();
      this.checkCMPFieldsForFieldMaps();
      this.checkMultiTableNoDupCMPFields();
      this.checkMultiTableAllHavePKFields();
      this.checkFieldGroupsHaveValidFields();
      this.checkRelationshipCachingRequireDatabaseType();
      this.checkDelayDatabaseInsertUntilConflictDelayUpdatesUntilEndOfTx();
      this.checkWeblogicQueriesHaveEjbQuery();
      this.checkQueriesHaveValidGroupNamesAndCachingNames();
      this.checkNoSqlSelectDistinctWithBlobClob();
      this.checkSupportedDatabaseForKeyGenerator();
      this.checkGenKeyPKIsIntegerOrLong();
      this.checkBlobClobSupportedDatabase();
      this.checkBatchOperations();
      this.checkOptimisticConcurrency();
      this.checkUseSelectForUpdate();
      this.checkValuesForTableAutoCreation();
      this.checkWLFindByPrimaryKey();
      this.checkTableAndColumnNames();
      this.checkFieldGroupsUnused();
      this.checkRelationshipCachesUnused();
      this.checkSqlShapeExists();
   }

   public static boolean validateCategoryFieldAvailible(RDBMSBean var0) {
      if (var0 == null) {
         return false;
      } else {
         String var1 = var0.getCategoryCmpField();
         if (var1 != null) {
            boolean var2 = true;
            if (!var0.getCmpFieldNames().contains(var1)) {
               log.logInfo("The field " + var1 + " specified in <category-cmp-field> of EJB " + var0.getEjbName() + " isn't a cmp field.");
               var2 = false;
            }

            if (!var0.isReadOnly()) {
               log.logInfo("The <category-cmp-field> " + var1 + " should be specified on Readonly Entity Bean only. " + var0.getEjbName() + " isn't a Readonly Entity Bean");
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }
   }

   private void checkSqlShapeExists() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      Iterator var3 = var2.getRdbmsFinders().values().iterator();

      while(var3.hasNext()) {
         RDBMSFinder var4 = (RDBMSFinder)var3.next();
         String var5 = var4.getSqlShapeName();
         if (var5 != null && var2.getSqlShape(var5) == null) {
            String var6 = "";
            if (var2.getSqlShapes() != null) {
               Iterator var7 = var2.getSqlShapes().values().iterator();

               while(var7.hasNext()) {
                  SqlShape var8 = (SqlShape)var7.next();
                  var6 = var6 + var8.getSqlShapeName();
                  if (var7.hasNext()) {
                     var6 = var6 + ", ";
                  }
               }
            }

            this.errors.add(new ComplianceException(this.fmt.SQL_SHAPE_DOES_NOT_EXIST(var1, var4.toString(), var5, var6)));
         }

         if (var4.usesSql()) {
            Map var9 = var4.getSqlQueries();
            if (var9.size() == 0) {
               this.errors.add(new ComplianceException(this.fmt.SQL_QUERY_NOT_SPECIFIED(var1, var4.toString())));
            }
         }
      }

   }

   private void checkTableAndColumnNames() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      Map var3 = var2.getTableName2CmpField2ColumnMap();
      Iterator var4 = var3.keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if (DDConstants.reservedWords.contains(var5.toUpperCase(Locale.ENGLISH))) {
            this.errors.add(new ComplianceException(this.fmt.RESERVED_WORD_USED_FOR_COLUMN_OR_TABLE(var1, var5)));
         }

         Map var6 = (Map)var3.get(var5);
         Iterator var7 = var6.keySet().iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            String var9 = (String)var6.get(var8);
            if (DDConstants.reservedWords.contains(var9.toUpperCase(Locale.ENGLISH))) {
               this.errors.add(new ComplianceException(this.fmt.RESERVED_WORD_USED_FOR_COLUMN_OR_TABLE(var1, var9)));
            }
         }
      }

   }

   private void checkValuesForTableAutoCreation() throws ComplianceException {
      String var1 = this.cmpDesc.getCreateDefaultDbmsTables();
      if (var1 != null && !var1.equalsIgnoreCase("DropAndCreate") && !var1.equalsIgnoreCase("DropAndCreateAlways") && !var1.equalsIgnoreCase("AlterOrCreate") && !var1.equalsIgnoreCase("CreateOnly") && !var1.equalsIgnoreCase("Disabled")) {
         throw new ComplianceException(this.fmt.WRONG_VALUE_FOR_DBMS_TABLE());
      }
   }

   private void checkEjBeanHasWLBean() throws ComplianceException {
      String var1 = this.testCMPB.getEJBName();
      if (!this.rdbmsBeanMap.containsKey(var1)) {
         throw new ComplianceException(this.fmt.NO_MATCHING_BEAN(var1), new DescriptorErrorInfo("<entity>", var1, var1));
      }
   }

   private void checkSupportedDatabaseForKeyGenerator() throws ComplianceException {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      if (var2.hasAutoKeyGeneration()) {
         if (var2.getGenKeyType() == 2) {
            switch (var2.getDatabaseType()) {
               case 1:
               case 3:
               case 4:
                  break;
               case 2:
               default:
                  throw new ComplianceException(this.fmt.GENKEY_PK_SEQUENCE_WITH_UNSUPPORTED_DB(this.testCMPB.getEJBName(), DDConstants.getDBNameForType(var2.getDatabaseType())), new DescriptorErrorInfo("<automatic-key-generation>", var1, var1));
            }
         }

         if (var2.getGenKeyType() == 1 && var2.getDatabaseType() == 1) {
            throw new ComplianceException(this.fmt.GENKEY_PK_IDENTITY_WITH_UNSUPPORTED_DB(this.testCMPB.getEJBName(), DDConstants.getDBNameForType(var2.getDatabaseType())), new DescriptorErrorInfo("<automatic-key-generation>", var1, var1));
         }
      }
   }

   private void checkGenKeyPKIsIntegerOrLong() throws ComplianceException {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      if (var2.hasAutoKeyGeneration()) {
         CMPBeanDescriptor var3 = (CMPBeanDescriptor)this.beanMap.get(var2.getEjbName());
         if (var3.hasComplexPrimaryKey()) {
            if (var3.getPrimaryKeyFieldNames().size() == 1) {
               String var7 = (String)var3.getPrimaryKeyFieldNames().iterator().next();
               Class var8 = var3.getFieldClass(var7);
               if (var8.equals(Integer.class) || var8.equals(Long.class) || var8.equals(Integer.TYPE) || var8.equals(Long.TYPE)) {
                  return;
               }
            }

            throw new ComplianceException(this.fmt.GENKEY_PK_IS_INTEGER_OR_LONG(this.testCMPB.getEJBName()), new DescriptorErrorInfo("<automatic-key-generation>", var1, var1));
         } else {
            Iterator var4 = var3.getPrimaryKeyFieldNames().iterator();
            String var5 = (String)var4.next();
            String var6 = ClassUtils.classToJavaSourceType(var3.getFieldClass(var5));
            if (!var6.equals("java.lang.Integer") && !var6.equals("java.lang.Long")) {
               throw new ComplianceException(this.fmt.GENKEY_PK_IS_INTEGER_OR_LONG(this.testCMPB.getEJBName()), new DescriptorErrorInfo("<automatic-key-generation>", var1, var1));
            } else if ((var2.getGenKeyType() == 2 || var2.getGenKeyType() == 3) && var2.getGenKeyCacheSize() == 0) {
               throw new ComplianceException(this.fmt.AUTO_PK_KEY_CACHE_SIZE_NOT_SPECIFIED(this.testCMPB.getEJBName()), new DescriptorErrorInfo("<automatic-key-generation>", var1, var1));
            }
         }
      }
   }

   private void checkBlobClobSupportedDatabase() throws ComplianceException {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      if (var2.hasBlobClobColumn()) {
         switch (var2.getDatabaseType()) {
            case 0:
               throw new ComplianceException(this.fmt.BLOB_CLOB_WITH_UNKNOWN_DB(var1), new DescriptorErrorInfo("<dbms-column-type>", var1, var1));
            case 1:
            case 4:
               return;
            case 2:
            case 3:
            default:
               throw new ComplianceException(this.fmt.BLOB_CLOB_WITH_UNSUPPORTED_DB(var1, DDConstants.getDBNameForType(var2.getDatabaseType())), new DescriptorErrorInfo("<dbms-column-type>", var1, var1));
         }
      }
   }

   private void checkBatchOperations() throws ComplianceException {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      if (var2.getEnableBatchOperations() || var2.getOrderDatabaseOperations()) {
         if (var2.hasAutoKeyGeneration() && var2.getGenKeyType() == 1) {
            EJBLogger var3 = new EJBLogger();
            EJBLogger.logWarningBatchOperationOffForAutoKeyGen(var1, "Identity");
         }

      }
   }

   private void checkOptimisticConcurrency() throws ComplianceException {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      CMPBeanDescriptor var3 = (CMPBeanDescriptor)this.beanMap.get(var2.getEjbName());
      Iterator var4 = var2.getTables().iterator();

      while(true) {
         EJBLogger var16;
         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            String var6 = var2.getVerifyColumns(var5);
            String var7 = var2.getVerifyRows(var5);
            String var8 = var2.getOptimisticColumn(var5);
            if (var3.isOptimistic()) {
               if (var6 == null) {
                  throw new ComplianceException(this.fmt.MISSING_VERIFY_COLUMNS(var1, var5), new DescriptorErrorInfo("<verify-fields>", var1, var5));
               }

               if (!var6.equalsIgnoreCase("read") && !var6.equalsIgnoreCase("modified") && !var6.equalsIgnoreCase("version") && !var6.equalsIgnoreCase("timestamp")) {
                  throw new ComplianceException(this.fmt.ILLEGAL_VERIFY_COLUMNS(var1, var5, var6), new DescriptorErrorInfo("<verify-fields>", var1, var5));
               }

               if (var7 != null) {
                  if (!var7.equalsIgnoreCase("read") && !var7.equalsIgnoreCase("modified")) {
                     throw new ComplianceException(this.fmt.ILLEGAL_VERIFY_ROWS(var1, var5, var7), new DescriptorErrorInfo("<verify-fields>", var1, var5));
                  }

                  if (var7.equalsIgnoreCase("read") && var6.equalsIgnoreCase("modified")) {
                     throw new ComplianceException(this.fmt.ILLEGAL_VERIFY_READ_MODIFIED(var1, var5), new DescriptorErrorInfo("<verify-fields>", var1, var5));
                  }
               }

               if (var6.equalsIgnoreCase("version") || var6.equalsIgnoreCase("timestamp")) {
                  if (!var2.hasOptimisticColumn(var5)) {
                     throw new ComplianceException(this.fmt.MISSING_OPTIMISTIC_COLUMN(var1, var5, var6), new DescriptorErrorInfo("<optimistic-column>", var1, var5));
                  }

                  String var9 = var2.getCmpField(var5, var8);
                  if (var9 != null) {
                     Class var10 = this.testCMPB.getFieldClass(var9);
                     if (var6.equalsIgnoreCase("version")) {
                        if (!var10.equals(Long.class)) {
                           throw new ComplianceException(this.fmt.VERSION_FIELD_WRONG_TYPE(var1, var9, var5, var8, var10.getName()));
                        }
                     } else if (!var10.equals(Timestamp.class)) {
                        throw new ComplianceException(this.fmt.TIMESTAMP_FIELD_WRONG_TYPE(var1, var9, var5, var8, var10.getName()));
                     }
                  }
               }

               if (var2.hasBlobClobColumn() && !var6.equalsIgnoreCase("version") && !var6.equalsIgnoreCase("timestamp")) {
                  var16 = new EJBLogger();
                  EJBLogger.logWarningOptimisticBlobBeanHasNoVersionTimestamp(var1);
               }
            } else {
               if (var6 != null) {
                  EJBLogger.logWarningNonOptimisticBeanUsesVerifyColumns(var1);
                  var2.setVerifyColumns(var5, (String)null);
               }

               if (var7 != null) {
                  EJBLogger.logWarningNonOptimisticBeanUsesVerifyRows(var1);
                  var2.setVerifyRows(var5, (String)null);
               }

               if (var8 != null) {
                  EJBLogger.logWarningNonOptimisticBeanUsesOptimisticColumn(var1);
                  var2.setOptimisticColumn(var5, (String)null);
               }
            }
         }

         EJBLogger var12;
         if (var3.isOptimistic()) {
            if (var2.getDatabaseType() != 1) {
               Iterator var11 = this.testCMPB.getAllQueries().iterator();

               label100:
               while(true) {
                  EntityBeanQuery var13;
                  RDBMSFinder var15;
                  do {
                     if (!var11.hasNext()) {
                        break label100;
                     }

                     var13 = (EntityBeanQuery)var11.next();
                     RDBMSFinder.FinderKey var14 = new RDBMSFinder.FinderKey(var13.getMethodName(), var13.getMethodParams());
                     var15 = (RDBMSFinder)var2.getRdbmsFinders().get(var14);
                  } while(var15 != null && !var15.getIncludeUpdates());

                  var16 = new EJBLogger();
                  EJBLogger.logWarningOptimisticBeanUsesIncludeUpdate(var1, DDUtils.getMethodSignature(var13.getMethodName(), var13.getMethodParams()), DDConstants.getDBNameForType(var2.getDatabaseType()));
               }
            }

            if (var2.getUseSelectForUpdate()) {
               var12 = new EJBLogger();
               EJBLogger.logWarningOptimisticBeanUsesUseSelectForUpdate(var1);
               var2.setUseSelectForUpdate(false);
            }

            if (var2.isClusterInvalidationDisabled() && !var2.getVerifyReads()) {
               var12 = new EJBLogger();
               EJBLogger.logWarningOCBeanIsVerifyModAndNoClustInvalidate(var1);
            }
         } else if (var2.isClusterInvalidationDisabled() && !var3.isReadOnly()) {
            var12 = new EJBLogger();
            EJBLogger.logWarningNonOCOrROBeanDisablesClustInvalidate(var1);
            var2.setClusterInvalidationDisabled(false);
         }

         return;
      }
   }

   private void checkUseSelectForUpdate() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      if (var2.getUseSelectForUpdate()) {
         CMPBeanDescriptor var3 = (CMPBeanDescriptor)this.beanMap.get(var2.getEjbName());
         if (var3.isReadOnly()) {
            EJBLogger.logWarningReadOnlyBeanUsesUseSelectForUpdate(var1);
         }

         if (var3.getConcurrencyStrategy() == 1) {
            EJBLogger.logWarningExclusiveBeanUsesUseSelectForUpdate(var1);
         }
      }

   }

   private void checkNoExtraAbstractMethods() {
      Class var1 = this.testCMPB.getBeanClass();
      HashSet var2 = new HashSet();
      Collection var3 = PersistenceUtils.getAbstractMethodCollection(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Method var5 = (Method)var4.next();
         int var6 = var5.getModifiers();
         if (var2.contains(var5.getName())) {
            this.errors.add(new ComplianceException(this.fmt.OVERLOADED_ABSTRACT_METHOD(this.testCMPB.getEJBName(), var5.getName())));
         }

         boolean var7 = false;
         if (var5.getName().startsWith("ejbSelect")) {
            var7 = true;
         } else if (!var5.getName().startsWith("get") && !var5.getName().startsWith("set")) {
            var7 = false;
         } else {
            var2.add(var5.getName());
            String var8 = var5.getName().substring(3);
            var8 = MethodUtils.decapitalize(var8);
            if (this.fieldNames.contains(var8)) {
               var7 = true;
            } else {
               var7 = false;
            }
         }

         if (!var7) {
            this.errors.add(new ComplianceException(this.fmt.EXTRA_ABSTRACT_METHOD(this.testCMPB.getEJBName(), DDUtils.getMethodSignature(var5))));
         }
      }

   }

   private void checkCMPFieldsForFieldMaps() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      CMPBeanDescriptor var3 = (CMPBeanDescriptor)this.beanMap.get(var2.getEjbName());
      Map var4 = var2.getCmpFieldToColumnMap();
      Map var5 = var2.getCmpFieldToColumnTypeMap();
      Iterator var6 = this.testCMPB.getCMFieldNames().iterator();

      String var8;
      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         if (!var4.containsKey(var7)) {
            this.errors.add(new ComplianceException(this.fmt.NO_MATCHING_FIELD_MAP(var1, var7), new DescriptorErrorInfo("<field-map>", var1, var7)));
         }

         if (var5.containsKey(var7) && var2.isClobCmpColumnTypeForField(var7)) {
            var8 = ClassUtils.classToJavaSourceType(var3.getFieldClass(var7));
            if (!var8.equals("java.lang.String")) {
               this.errors.add(new ComplianceException(this.fmt.FIELDCLASSTYPE_MUST_BE_STRING_FOR_ORACLECLOB_COLUMNTYPE(var1, var7, var8), new DescriptorErrorInfo("<dbms-column-type>", var1, var7)));
            }
         }
      }

      Iterator var9 = var4.keySet().iterator();

      while(var9.hasNext()) {
         var8 = (String)var9.next();
         if (!this.testCMPB.getCMFieldNames().contains(var8)) {
            this.errors.add(new ComplianceException(this.fmt.NO_MATCHING_CMP_FIELD(var1, var8), new DescriptorErrorInfo("<cmp-field>", var1, var8)));
         }
      }

   }

   private void checkMultiTableNoDupCMPFields() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      if (var2.hasMultipleTables()) {
         CMPBeanDescriptor var3 = (CMPBeanDescriptor)this.beanMap.get(var2.getEjbName());
         Map var4 = var2.getCmpFieldToColumnMap();
         Set var5 = var3.getPrimaryKeyFieldNames();
         Iterator var6 = this.testCMPB.getCMFieldNames().iterator();

         while(true) {
            String var7;
            do {
               if (!var6.hasNext()) {
                  return;
               }

               var7 = (String)var6.next();
            } while(var5.contains(var7));

            int var8 = 0;
            String var9 = "";
            Iterator var10 = var2.getTables().iterator();

            while(var10.hasNext()) {
               String var11 = (String)var10.next();
               Map var12 = var2.getCmpField2ColumnMap(var11);
               if (var12.containsKey(var7)) {
                  var9 = var9 + (var8 > 0 ? ", " : "") + var11;
                  ++var8;
               }
            }

            if (var8 > 1) {
               this.errors.add(new ComplianceException(this.fmt.NON_PK_CMP_FIELD_MAPPED_TO_MORE_THAN_ONE_TABLE(var1, var7, var8, var9), new DescriptorErrorInfo("<cmp-field>", var1, var7)));
            }
         }
      }
   }

   private void checkMultiTableAllHavePKFields() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      if (var2.hasMultipleTables()) {
         CMPBeanDescriptor var3 = (CMPBeanDescriptor)this.beanMap.get(var2.getEjbName());
         Map var4 = var2.getCmpFieldToColumnMap();
         Set var5 = var3.getPrimaryKeyFieldNames();
         Iterator var6 = var2.getTables().iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            Map var8 = var2.getCmpField2ColumnMap(var7);
            Iterator var9 = var5.iterator();

            while(var9.hasNext()) {
               String var10 = (String)var9.next();
               if (!var8.containsKey(var10)) {
                  this.errors.add(new ComplianceException(this.fmt.MISSING_MULTITABLE_PK_FIELD_MAP(var1, var7, var10), new DescriptorErrorInfo("TableNameForOneCMP", var1, var7)));
               }
            }
         }

      }
   }

   private void checkFieldGroupsHaveValidFields() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      Iterator var3 = var2.getFieldGroups().iterator();

      while(var3.hasNext()) {
         FieldGroup var4 = (FieldGroup)var3.next();
         Iterator var5 = var4.getCmpFields().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (!var2.hasCmpField(var6)) {
               this.errors.add(new ComplianceException(this.fmt.GROUP_CONTAINS_UNDEFINED_CMP_FIELD(var1, var4.getName(), var6), new DescriptorErrorInfo("<field-group>", var1, var4.getName())));
            }
         }

         Iterator var8 = var4.getCmrFields().iterator();

         while(var8.hasNext()) {
            String var7 = (String)var8.next();
            if (!this.cmrFieldNames.contains(var7)) {
               this.errors.add(new ComplianceException(this.fmt.GROUP_CONTAINS_UNDEFINED_CMR_FIELD(var1, var4.getName(), var7), new DescriptorErrorInfo("<field-group>", var1, var4.getName())));
            }
         }
      }

   }

   private void checkRelationshipCachingRequireDatabaseType() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      Iterator var3 = var2.getRelationshipCachings().iterator();
      if (var3.hasNext() && this.cmpDesc.getDatabaseType() == null) {
         this.errors.add(new ComplianceException(this.fmt.RELATIONSHIP_CACHING_REQUIRE_DATABASETYPE(var1), new DescriptorErrorInfo("<relationship-caching>", var1, var1)));
      }

   }

   private void checkDelayDatabaseInsertUntilConflictDelayUpdatesUntilEndOfTx() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      CMPBeanDescriptor var3 = (CMPBeanDescriptor)this.beanMap.get(var2.getEjbName());
      if (!var3.getBoxCarUpdates() && this.cmpDesc.isEnableBatchOperations() || !var3.getBoxCarUpdates() && this.cmpDesc.isOrderDatabaseOperations()) {
         this.errors.add(new ComplianceException(this.fmt.DelayDatabaseInsertUntilConflictEnableBatchOperations(var1), new DescriptorErrorInfo("<delay-updates-until-end-of-tx>", var1, var1)));
      }

   }

   private void checkGroupHasAllPkFields() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      Set var3 = this.testCMPB.getPrimaryKeyFieldNames();
      Iterator var4 = var2.getFieldGroups().iterator();

      while(var4.hasNext()) {
         FieldGroup var5 = (FieldGroup)var4.next();
         TreeSet var6 = var5.getCmpFields();
         Iterator var7 = var5.getCmpFields().iterator();
         boolean var8 = false;

         while(var7.hasNext() && !var8) {
            if (var3.contains(var7.next())) {
               var8 = true;
            }
         }

         if (var8 && !var6.containsAll(var3)) {
            this.errors.add(new ComplianceException(this.fmt.GROUP_CONTAINS_PK_SUBSET(var1, var5.getName()), new DescriptorErrorInfo("<field-group>", var1, var5.getName())));
         }
      }

   }

   private void checkWeblogicQueriesHaveEjbQuery() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      Iterator var3 = this.testCMPB.getAllQueries().iterator();
      HashSet var4 = new HashSet();

      RDBMSFinder.FinderKey var6;
      while(var3.hasNext()) {
         EntityBeanQuery var5 = (EntityBeanQuery)var3.next();
         var6 = new RDBMSFinder.FinderKey(var5.getMethodName(), var5.getMethodParams());
         var4.add(var6);
      }

      var4.add(new RDBMSFinder.FinderKey("findByPrimaryKey", new String[]{this.testCMPB.getPrimaryKeyClassName()}));
      Iterator var8 = var2.getRdbmsFinders().keySet().iterator();

      while(var8.hasNext()) {
         var6 = (RDBMSFinder.FinderKey)var8.next();
         if (!var4.contains(var6)) {
            EJBLogger var7 = new EJBLogger();
            EJBLogger.logWarningWeblogicQueryHasNoMatchingEjbQuery(var1, DDUtils.getMethodSignature(var6.getFinderName(), var6.getFinderParams()));
         }
      }

   }

   private void checkQueriesHaveValidGroupNamesAndCachingNames() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      Iterator var3 = var2.getRdbmsFinders().values().iterator();

      while(var3.hasNext()) {
         RDBMSFinder var4 = (RDBMSFinder)var3.next();
         if (var4.getGroupName() != null && var2.getFieldGroup(var4.getGroupName()) == null) {
            this.errors.add(new ComplianceException(this.fmt.QUERY_CONTAINS_UNDEFINED_GROUP(var1, var4.getGroupName()), new DescriptorErrorInfo("<group-name>", var1, var4.getFinderName())));
         }

         if (var4.getCachingName() != null && var2.getRelationshipCaching(var4.getCachingName()) == null) {
            this.errors.add(new ComplianceException(this.fmt.QUERY_CONTAINS_UNDEFINED_CACHING_NAME(var1, var4.getCachingName()), new DescriptorErrorInfo("<caching-name>", var1, var4.getFinderName())));
         }
      }

   }

   private void checkNoSqlSelectDistinctWithBlobClob() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      Iterator var3 = var2.getRdbmsFinders().values().iterator();

      while(true) {
         label29:
         while(true) {
            RDBMSFinder var4;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               var4 = (RDBMSFinder)var3.next();
            } while(!var4.getSqlSelectDistinct());

            String var5 = var4.getGroupName();
            if (var5 != null) {
               Iterator var6 = var2.getFieldGroup(var5).getCmpFields().iterator();

               while(true) {
                  String var7;
                  do {
                     if (!var6.hasNext()) {
                        continue label29;
                     }

                     var7 = (String)var6.next();
                  } while(!var2.isBlobCmpColumnTypeForField(var7) && !var2.isClobCmpColumnTypeForField(var7));

                  this.errors.add(new ComplianceException(this.fmt.NoSqlSelectDistinctWithBlobClobField(var1, var4.getFinderName(), var5)));
               }
            } else if (var2.hasBlobClobColumn()) {
               this.errors.add(new ComplianceException(this.fmt.NoSqlSelectDistinctWithBlobClobField(var1, var4.getFinderName(), "defaultGroup")));
            }
         }
      }
   }

   private void checkWLFindByPrimaryKey() {
      String var1 = this.testCMPB.getEJBName();
      RDBMSBean var2 = (RDBMSBean)this.rdbmsBeanMap.get(var1);
      String var3 = "findByPrimaryKey";
      String[] var4 = new String[]{this.testCMPB.getPrimaryKeyClassName()};
      RDBMSFinder var5 = (RDBMSFinder)((RDBMSFinder)var2.getRdbmsFinders().get(new RDBMSFinder.FinderKey(var3, var4)));
      if (var5 != null && var5.getEjbQlQuery() != null) {
         this.errors.add(new ComplianceException(this.fmt.WLQL_CANNOT_OVERRIDE_FINDBYPK_QL(var1, var5.getEjbQlQuery())));
      }
   }

   private void checkFieldGroupsUnused() {
      String var3 = this.testCMPB.getEJBName();
      RDBMSBean var4 = (RDBMSBean)this.rdbmsBeanMap.get(var3);
      CMPBeanDescriptor var5 = (CMPBeanDescriptor)this.beanMap.get(var3);
      boolean var6 = var5.getIsDynamicQueriesEnabled();
      if (!var6) {
         List var7 = var4.getFieldGroups();
         if (var7.size() > 0) {
            LinkedList var8 = new LinkedList();
            Iterator var9 = var7.iterator();

            while(var9.hasNext()) {
               var8.add(var9.next());
            }

            Map var10 = var4.getRdbmsFinders();
            Iterator var11 = var10.keySet().iterator();

            FieldGroup var1;
            String var2;
            while(var11.hasNext()) {
               RDBMSFinder var12 = (RDBMSFinder)var10.get(var11.next());
               var2 = var12.getGroupName();
               if (var2 != null && var2.length() > 0) {
                  var1 = this.getFieldGroupByName(var2, var8);
                  if (var1 != null) {
                     var8.remove(var1);
                  }
               }
            }

            Iterator var16 = this.rdbmsBeanMap.keySet().iterator();

            while(var16.hasNext()) {
               RDBMSBean var13 = (RDBMSBean)this.rdbmsBeanMap.get(var16.next());
               var9 = var13.getRelationshipCachings().iterator();

               while(var9.hasNext()) {
                  RelationshipCaching var14 = (RelationshipCaching)var9.next();
                  List var15 = var14.getCachingElements();
                  if (var15.size() > 0) {
                     this.filterFieldGroupInCachingElements(var8, var15);
                  }
               }
            }

            var9 = this.rdbmsRelationMap.values().iterator();

            while(var9.hasNext()) {
               RDBMSRelation var17 = (RDBMSRelation)var9.next();
               RDBMSRelation.RDBMSRole var19 = var17.getRole1();
               RDBMSRelation.RDBMSRole var21 = var17.getRole2();
               if (var19 != null) {
                  var2 = var19.getGroupName();
                  if (var2 != null && var2.length() > 0) {
                     var1 = this.getFieldGroupByName(var2, var8);
                     if (var1 != null) {
                        var8.remove(var1);
                     }
                  }
               }

               if (var21 != null) {
                  var2 = var21.getGroupName();
                  if (var2 != null && var2.length() > 0) {
                     var1 = this.getFieldGroupByName(var2, var8);
                     if (var1 != null) {
                        var8.remove(var1);
                     }
                  }
               }
            }

            if (var8.size() > 0) {
               StringBuffer var18 = new StringBuffer();
               var9 = var8.iterator();

               while(var9.hasNext()) {
                  var1 = (FieldGroup)var9.next();
                  var18.append(var1.getName());
                  if (var9.hasNext()) {
                     var18.append(", ");
                  }
               }

               EJBLogger var20 = new EJBLogger();
               EJBLogger.logWarningUnusedFieldGroups(var3, var18.toString());
            }

         }
      }
   }

   private void filterFieldGroupInCachingElements(List var1, List var2) {
      RelationshipCaching.CachingElement var4;
      if (var2.size() > 0) {
         for(Iterator var3 = var2.iterator(); var3.hasNext(); this.filterFieldGroupInCachingElements(var1, var4.getCachingElements())) {
            var4 = (RelationshipCaching.CachingElement)var3.next();
            String var5 = var4.getGroupName();
            if (var5 != null && var5.length() > 0) {
               FieldGroup var6 = this.getFieldGroupByName(var5, var1);
               if (var6 != null) {
                  var1.remove(var6);
               }
            }
         }
      }

   }

   private FieldGroup getFieldGroupByName(String var1, List var2) {
      if (var1 != null && var2 != null) {
         Iterator var3 = var2.iterator();

         FieldGroup var4;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            var4 = (FieldGroup)var3.next();
         } while(!var4.getName().equals(var1));

         return var4;
      } else {
         return null;
      }
   }

   private void checkRelationshipCachesUnused() {
      String var4 = this.testCMPB.getEJBName();
      RDBMSBean var5 = (RDBMSBean)this.rdbmsBeanMap.get(var4);
      CMPBeanDescriptor var6 = (CMPBeanDescriptor)this.beanMap.get(var4);
      boolean var7 = var6.getIsDynamicQueriesEnabled();
      if (!var7) {
         List var8 = var5.getRelationshipCachings();
         if (var8.size() > 0) {
            LinkedList var2 = new LinkedList();
            Iterator var9 = var8.iterator();

            while(var9.hasNext()) {
               var2.add(var9.next());
            }

            Map var10 = var5.getRdbmsFinders();
            Iterator var11 = var10.keySet().iterator();

            RelationshipCaching var1;
            while(var11.hasNext()) {
               RDBMSFinder var12 = (RDBMSFinder)var10.get(var11.next());
               String var3 = var12.getCachingName();
               if (var3 != null && var3.length() > 0) {
                  var1 = this.getRelationshipCachingByName(var3, var2);
                  if (var1 != null) {
                     var2.remove(var1);
                  }
               }
            }

            if (var2.size() > 0) {
               StringBuffer var14 = new StringBuffer();
               var9 = var2.iterator();

               while(var9.hasNext()) {
                  var1 = (RelationshipCaching)var9.next();
                  var14.append(var1.getCachingName());
                  if (var9.hasNext()) {
                     var14.append(", ");
                  }
               }

               EJBLogger var13 = new EJBLogger();
               EJBLogger.logWarningUnusedRelationshipCachings(var4, var14.toString());
            }

         }
      }
   }

   private RelationshipCaching getRelationshipCachingByName(String var1, List var2) {
      if (var1 != null && var2 != null) {
         Iterator var3 = var2.iterator();

         RelationshipCaching var4;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            var4 = (RelationshipCaching)var3.next();
         } while(!var4.getCachingName().equals(var1));

         return var4;
      } else {
         return null;
      }
   }
}
