package weblogic.ejb.container.cmp.rdbms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.finders.EjbqlFinder;
import weblogic.ejb.container.cmp.rdbms.finders.Finder;
import weblogic.ejb.container.cmp.rdbms.finders.RDBMSFinder;
import weblogic.ejb.container.cmp.rdbms.finders.SqlFinder;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.compliance.Log;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.CmrField;
import weblogic.ejb.container.persistence.spi.EjbEntityRef;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.ejb20.cmp.rdbms.finders.EJBQLCompilerException;
import weblogic.ejb20.cmp.rdbms.finders.InvalidFinderException;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.StackTraceUtils;

public final class RDBMSBean implements Cloneable {
   private static final DebugLogger debugLogger;
   private String dataSourceName;
   private List cmpFieldNames;
   private Map cmpFieldClasses;
   private Map tableNames;
   private List tableNamesList;
   private Map tableName2cmpFieldName2columnName;
   private Map tableName2verifyRows;
   private Map tableName2verifyColumns;
   private Map tableName2optimisticColumn;
   private Map tableName2optimisticColumnTrigger;
   private Map tableName2versionColumnInitialValue;
   private boolean verifyReads;
   private List cmpColumnNames;
   private Map fieldName2columnName;
   private Map columnName2fieldName;
   private Map fieldName2columnTypeName;
   private Map cmpFieldName2groupName;
   private Set dbmsDefaultValueFields;
   private List fieldGroups;
   private List relationshipCachings;
   private Map rdbmsFinders;
   private String delayInsertUntil;
   private boolean useSelectForUpdate;
   private int lockOrder;
   private String instanceLockOrder;
   private short genKeyType;
   private String genKeyGeneratorName;
   private int genKeyCacheSize;
   private boolean selectFirstSeqKeyBeforeUpdate;
   protected boolean orderDatabaseOperations;
   protected boolean enableBatchOperations;
   private String createDefaultDBMSTables;
   private String ddlFileName;
   private int databaseType;
   private boolean checkExistsOnMethod;
   private boolean hasBlobColumn;
   private boolean hasClobColumn;
   private boolean hasNClobColumn;
   private boolean hasBlobClobColumn;
   private boolean byteArrayIsSerializedToOracleBlob;
   private boolean loadRelatedBeansFromDbInPostCreate;
   private boolean allowReadonlyCreateAndRemove;
   private boolean charArrayIsSerializedToBytes;
   private boolean disableStringTrimming;
   private boolean findersReturnNulls;
   private String validateDbSchemaWith;
   private Map sqlShapes;
   private CMPBeanDescriptor bd;
   private String ejbName;
   private List primaryKeyFieldList;
   private List finderList;
   private boolean hasResultSetFinder;
   private Map beanMap;
   private Relationships relationships;
   private Map dependentMap;
   private Map rdbmsBeanMap;
   private Map rdbmsRelationMap;
   private Map rdbmsDependentMap;
   private boolean normalizeMultiTables_done;
   private boolean fieldsLoadedViaMultiTable;
   private Map table2cmpf2columnPKsOnly;
   private Map table2cmpf2columnNoPKs;
   private Map table2column2cmpf;
   private Map table2column2variable;
   private Map cmpf2Table;
   private Map pkCmpF2Table2Column;
   private Map column2tables;
   private Map variableName2table;
   private Map variableName2cmrField;
   int numFields;
   private Map fieldNameToIsModifiedIndex;
   private Map isModifiedIndexToFieldName;
   private List isModifiedPKIndexList;
   private List isModifiedIndexToTableNumber;
   private List[] tableIndexToFieldList;
   private List[] tableIndexToNonPKFieldList;
   private List[] tableIndexToCMPFieldList;
   private List[] tableIndexToCMRFieldList;
   private boolean synthesized;
   private Map fieldName2class;
   private List cmrFieldNames;
   private List fkFieldNames;
   private List fkPkFieldNames;
   private List fkCmpFieldNames;
   private List declaredFieldNames;
   private List remoteFieldNames;
   private Set one2one;
   private Set one2many;
   private Set many2many;
   private Set biDirectional;
   private Map fieldName2cascadeDelete;
   private Map fieldName2DBCascadeDelete;
   private boolean selfRelationship;
   private Map fieldName2selfRel;
   private Map fkField2fkColumns;
   private Map fkField2fkColumn2Class;
   private Map fkField2fkColumn2FieldName;
   private Map fieldName2relatedDescriptor;
   private Map fieldName2relatedMultiplicity;
   private Map fieldName2relatedFieldOwnsFk;
   private Map fieldName2relatedClassName;
   private Map fieldName2relatedRDBMSBean;
   private Map fieldName2RelatedFieldName;
   private Map fieldName2groupName;
   private Map variableName2columnName;
   private Map fieldName2tableName;
   private Map groupName2tableNames;
   private Map fieldName2entityRef;
   private Map fieldName2remoteColumn;
   private Map fkField2symColumn2FieldName;
   private Map fkField2symColumns;
   private Set cmrMappedcmpFields;
   private Map cmrMapeedRelationFinder;
   private Map table2cmrf;
   private Map cmrf2table;
   private List cmrfHasMultiPkTable;
   private Map cmrf2pkTable2fkColumn2pkColumn;
   private Map fkField2pkTable2symColumns;
   private Map fkField2pktable2symColumn2field;
   private Map fkField2pkTable2symFkColumn2pkColumn;
   private Map table2fkCol2fkClass;
   private Map table2fkCol2RelatedBean;
   private List ejbSelectInternalList;
   private String synthAbstractSchemaName;
   private boolean genKeyBeforeInsert;
   private boolean genKeyExcludePKColumn;
   private String genKeyDefaultColumnVal;
   private String genKeyWLGeneratorQuery;
   private String genKeyWLGeneratorUpdatePrefix;
   private String genKeyPKField;
   private short genKeyPKFieldClassType;
   private Class generatedBeanInterface;
   private RDBMSPersistenceManager pm;
   private int sqlFinderCount;
   private Set qcEnabledCmrFields;
   private Log log;
   private EJBComplianceTextFormatter fmt;
   private boolean hasSqlFinder;
   private boolean clusterInvalidationDisabled;
   private boolean useInnerJoin;
   private String categoryCmpField;
   private List relFinders;

   public RDBMSBean() {
      this("");
   }

   public RDBMSBean(String var1) {
      this.dataSourceName = null;
      this.cmpFieldNames = null;
      this.cmpFieldClasses = null;
      this.tableNames = null;
      this.tableNamesList = null;
      this.tableName2cmpFieldName2columnName = null;
      this.tableName2verifyRows = null;
      this.tableName2verifyColumns = null;
      this.tableName2optimisticColumn = null;
      this.tableName2optimisticColumnTrigger = null;
      this.tableName2versionColumnInitialValue = null;
      this.verifyReads = false;
      this.cmpColumnNames = null;
      this.fieldName2columnName = null;
      this.columnName2fieldName = null;
      this.fieldName2columnTypeName = null;
      this.cmpFieldName2groupName = null;
      this.dbmsDefaultValueFields = null;
      this.fieldGroups = null;
      this.relationshipCachings = null;
      this.rdbmsFinders = null;
      this.delayInsertUntil = "ejbPostCreate";
      this.useSelectForUpdate = false;
      this.lockOrder = 0;
      this.instanceLockOrder = "AccessOrder";
      this.genKeyType = -1;
      this.genKeyGeneratorName = null;
      this.genKeyCacheSize = 0;
      this.selectFirstSeqKeyBeforeUpdate = false;
      this.orderDatabaseOperations = true;
      this.enableBatchOperations = true;
      this.createDefaultDBMSTables = "Disabled";
      this.ddlFileName = null;
      this.databaseType = 0;
      this.checkExistsOnMethod = true;
      this.hasBlobColumn = false;
      this.hasClobColumn = false;
      this.hasNClobColumn = false;
      this.hasBlobClobColumn = false;
      this.byteArrayIsSerializedToOracleBlob = false;
      this.loadRelatedBeansFromDbInPostCreate = false;
      this.allowReadonlyCreateAndRemove = false;
      this.charArrayIsSerializedToBytes = false;
      this.disableStringTrimming = false;
      this.findersReturnNulls = true;
      this.validateDbSchemaWith = "";
      this.sqlShapes = null;
      this.bd = null;
      this.ejbName = null;
      this.primaryKeyFieldList = null;
      this.finderList = null;
      this.hasResultSetFinder = false;
      this.beanMap = null;
      this.relationships = null;
      this.dependentMap = null;
      this.rdbmsBeanMap = null;
      this.rdbmsRelationMap = null;
      this.rdbmsDependentMap = null;
      this.normalizeMultiTables_done = false;
      this.fieldsLoadedViaMultiTable = true;
      this.table2cmpf2columnPKsOnly = null;
      this.table2cmpf2columnNoPKs = null;
      this.table2column2cmpf = null;
      this.table2column2variable = null;
      this.cmpf2Table = null;
      this.pkCmpF2Table2Column = null;
      this.column2tables = null;
      this.variableName2table = null;
      this.variableName2cmrField = null;
      this.numFields = 0;
      this.fieldNameToIsModifiedIndex = null;
      this.isModifiedIndexToFieldName = null;
      this.isModifiedPKIndexList = null;
      this.isModifiedIndexToTableNumber = new ArrayList();
      this.synthesized = false;
      this.fieldName2class = null;
      this.cmrFieldNames = null;
      this.fkFieldNames = null;
      this.fkPkFieldNames = null;
      this.fkCmpFieldNames = null;
      this.declaredFieldNames = null;
      this.remoteFieldNames = null;
      this.one2one = null;
      this.one2many = null;
      this.many2many = null;
      this.biDirectional = null;
      this.fieldName2cascadeDelete = null;
      this.fieldName2DBCascadeDelete = null;
      this.selfRelationship = false;
      this.fieldName2selfRel = null;
      this.fkField2fkColumns = null;
      this.fkField2fkColumn2Class = null;
      this.fkField2fkColumn2FieldName = null;
      this.fieldName2relatedDescriptor = null;
      this.fieldName2relatedMultiplicity = null;
      this.fieldName2relatedFieldOwnsFk = null;
      this.fieldName2relatedClassName = null;
      this.fieldName2relatedRDBMSBean = null;
      this.fieldName2RelatedFieldName = null;
      this.fieldName2groupName = null;
      this.variableName2columnName = null;
      this.fieldName2tableName = null;
      this.groupName2tableNames = null;
      this.fieldName2entityRef = null;
      this.fieldName2remoteColumn = null;
      this.fkField2symColumn2FieldName = null;
      this.fkField2symColumns = null;
      this.cmrMappedcmpFields = null;
      this.cmrMapeedRelationFinder = null;
      this.table2cmrf = null;
      this.cmrf2table = null;
      this.cmrfHasMultiPkTable = null;
      this.cmrf2pkTable2fkColumn2pkColumn = null;
      this.fkField2pkTable2symColumns = null;
      this.fkField2pktable2symColumn2field = null;
      this.fkField2pkTable2symFkColumn2pkColumn = null;
      this.table2fkCol2fkClass = null;
      this.table2fkCol2RelatedBean = null;
      this.ejbSelectInternalList = null;
      this.synthAbstractSchemaName = null;
      this.genKeyBeforeInsert = true;
      this.genKeyExcludePKColumn = false;
      this.genKeyDefaultColumnVal = null;
      this.genKeyWLGeneratorQuery = "";
      this.genKeyWLGeneratorUpdatePrefix = "";
      this.genKeyPKField = null;
      this.generatedBeanInterface = null;
      this.pm = null;
      this.sqlFinderCount = 0;
      this.qcEnabledCmrFields = null;
      this.hasSqlFinder = false;
      this.clusterInvalidationDisabled = false;
      this.useInnerJoin = false;
      this.categoryCmpField = null;
      this.relFinders = null;
      this.setDataSourceName(var1);
      this.finderList = new LinkedList();
      this.cmpFieldNames = new ArrayList();
      this.cmpFieldClasses = new HashMap();
      this.cmpColumnNames = new ArrayList();
      this.fieldName2columnName = new HashMap();
      this.columnName2fieldName = new HashMap();
      this.fieldName2columnTypeName = new HashMap();
      this.cmpFieldName2groupName = new HashMap();
      this.fieldGroups = new ArrayList();
      this.relationshipCachings = new ArrayList();
      this.rdbmsFinders = new HashMap();
      this.tableNames = new HashMap();
      this.tableNamesList = new ArrayList();
      this.tableName2cmpFieldName2columnName = new HashMap();
      this.tableName2verifyRows = new HashMap();
      this.tableName2verifyColumns = new HashMap();
      this.tableName2optimisticColumn = new HashMap();
      this.tableName2optimisticColumnTrigger = new HashMap();
      this.tableName2versionColumnInitialValue = new HashMap();
      this.table2cmpf2columnPKsOnly = new HashMap();
      this.table2cmpf2columnNoPKs = new HashMap();
      this.table2column2cmpf = new HashMap();
      this.table2column2variable = new HashMap();
      this.cmpf2Table = new HashMap();
      this.pkCmpF2Table2Column = new HashMap();
      this.column2tables = new HashMap();
      this.table2cmrf = new HashMap();
      this.cmrf2table = new HashMap();
      this.cmrfHasMultiPkTable = new ArrayList();
      this.cmrf2pkTable2fkColumn2pkColumn = new HashMap();
      this.fkField2pkTable2symFkColumn2pkColumn = new HashMap();
      this.variableName2table = new HashMap();
      this.variableName2cmrField = new HashMap();
      this.table2fkCol2fkClass = new HashMap();
      this.table2fkCol2RelatedBean = new HashMap();
      this.fieldNameToIsModifiedIndex = new HashMap();
      this.isModifiedIndexToFieldName = new HashMap();
      this.isModifiedPKIndexList = new ArrayList();
      this.fkField2fkColumn2Class = new HashMap();
      this.one2one = new HashSet();
      this.one2many = new HashSet();
      this.many2many = new HashSet();
      this.biDirectional = new HashSet();
      this.fieldName2cascadeDelete = new HashMap();
      this.fieldName2DBCascadeDelete = new HashMap();
      this.fieldName2selfRel = new HashMap();
      this.fieldName2RelatedFieldName = new HashMap();
      this.fkField2fkColumn2FieldName = new HashMap();
      this.fkField2symColumn2FieldName = new HashMap();
      this.fieldName2relatedDescriptor = new HashMap();
      this.fieldName2relatedMultiplicity = new HashMap();
      this.fieldName2relatedFieldOwnsFk = new HashMap();
      this.fieldName2relatedClassName = new HashMap();
      this.fieldName2groupName = new HashMap();
      this.variableName2columnName = new HashMap();
      this.cmrFieldNames = new ArrayList();
      this.declaredFieldNames = new ArrayList();
      this.fkFieldNames = new ArrayList();
      this.fkPkFieldNames = new ArrayList();
      this.fkCmpFieldNames = new ArrayList();
      this.fieldName2class = new HashMap();
      this.fkField2fkColumns = new HashMap();
      this.fkField2symColumns = new HashMap();
      this.fieldName2relatedRDBMSBean = new HashMap();
      this.fieldName2tableName = new HashMap();
      this.groupName2tableNames = new HashMap();
      this.fieldName2entityRef = new HashMap();
      this.remoteFieldNames = new ArrayList();
      this.fieldName2remoteColumn = new HashMap();
      this.cmrMappedcmpFields = new HashSet();
      this.cmrMapeedRelationFinder = new HashMap();
      this.log = new Log();
      this.fmt = new EJBComplianceTextFormatter();
   }

   public void setRDBMSPersistenceManager(RDBMSPersistenceManager var1) {
      this.pm = var1;
   }

   public RDBMSPersistenceManager getRDBMSPersistenceManager() {
      return this.pm;
   }

   public String getAbstractSchemaName() {
      if (this.bd.getAbstractSchemaName() != null) {
         return this.bd.getAbstractSchemaName();
      } else {
         if (this.synthAbstractSchemaName == null) {
            this.synthAbstractSchemaName = this.genSynthAbstractSchemaName();
         }

         return this.synthAbstractSchemaName;
      }
   }

   private String genSynthAbstractSchemaName() {
      return "_WL_abstractSchemaName_" + this.getEjbName().replace('.', '_');
   }

   public Map getBeanMap() {
      return this.beanMap;
   }

   public CMPBeanDescriptor getCMPBeanDescriptor() {
      assert this.bd != null;

      return this.bd;
   }

   public void setEjbName(String var1) {
      this.ejbName = var1;
   }

   public String getEjbName() {
      return this.ejbName;
   }

   public void setDataSourceName(String var1) {
      this.dataSourceName = var1;
   }

   public String getDataSourceName() {
      return this.dataSourceName;
   }

   public void addToEjbSelectInternalList(Finder var1) {
      if (this.ejbSelectInternalList == null) {
         this.ejbSelectInternalList = new ArrayList();
      }

      this.ejbSelectInternalList.add(var1);
   }

   public List getEjbSelectInternalList() {
      if (this.ejbSelectInternalList == null) {
         this.ejbSelectInternalList = new ArrayList();
      }

      return this.ejbSelectInternalList;
   }

   public boolean hasAutoKeyGeneration() {
      return this.genKeyType != -1;
   }

   public boolean getGenKeyBeforeInsert() {
      return this.genKeyBeforeInsert;
   }

   public String getGenKeyDefaultColumnVal() {
      return this.genKeyDefaultColumnVal;
   }

   public boolean genKeyExcludePKColumn() {
      return this.genKeyExcludePKColumn;
   }

   public void setGenKeyCacheSize(int var1) {
      this.genKeyCacheSize = var1;
   }

   public int getGenKeyCacheSize() {
      return this.genKeyCacheSize;
   }

   public void setGenKeyGeneratorName(String var1) {
      this.genKeyGeneratorName = var1;
   }

   public String getGenKeyGeneratorName() {
      return this.genKeyGeneratorName;
   }

   public String getGenKeyPKField() {
      return this.genKeyPKField;
   }

   public String getGenKeyPKClassName() {
      return this.getCmpFieldClass(this.genKeyPKField).getName();
   }

   public short getGenKeyPKFieldClassType() {
      return this.genKeyPKFieldClassType;
   }

   public void setGenKeyType(String var1) {
      this.genKeyType = RDBMSUtils.getGenKeyTypeAsConstant(var1);
   }

   public short getGenKeyType() {
      return this.genKeyType;
   }

   public String getGenKeyGeneratorQuery() {
      return this.genKeyWLGeneratorQuery;
   }

   public String getGenKeyGeneratorUpdatePrefix() {
      return this.genKeyWLGeneratorUpdatePrefix;
   }

   public boolean getSelectFirstSeqKeyBeforeUpdate() {
      return this.selectFirstSeqKeyBeforeUpdate;
   }

   public void setSelectFirstSeqKeyBeforeUpdate(boolean var1) {
      this.selectFirstSeqKeyBeforeUpdate = var1;
   }

   public boolean hasMultipleTables() {
      return this.tableNamesList.size() > 1;
   }

   public int tableCount() {
      return this.tableNamesList.size();
   }

   public void addTable(String var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("adding table name: '" + var1 + "'");
      }

      if (this.hasTable(var1)) {
         throw new AssertionError("a duplicate tablename was detected: tableName-" + var1 + " ejbName-" + this.getEjbName());
      } else {
         this.tableNames.put(var1, var1);
         this.tableNamesList.add(var1);
      }
   }

   public boolean hasTable(String var1) {
      return this.tableNames.containsKey(var1);
   }

   public List getTables() {
      return this.tableNamesList;
   }

   public String tableAt(int var1) {
      return var1 >= this.tableNamesList.size() ? null : (String)this.tableNamesList.get(var1);
   }

   public int tableIndex(String var1) {
      return this.tableNamesList.indexOf(var1);
   }

   public String chooseTableAsJoinTarget() {
      String var1 = this.getTableName();
      List var2 = (List)this.table2cmrf.get(var1);
      int var3 = 0;
      if (var2 != null) {
         var3 = var2.size();
      }

      Iterator var4 = this.getTables().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var2 = (List)this.table2cmrf.get(var5);
         int var6 = 0;
         if (var2 != null) {
            var6 = var2.size();
         }

         if (var6 > var3) {
            var1 = var5;
         }
      }

      return var1;
   }

   public List getTableNamesForColumn(String var1) {
      return (List)this.column2tables.get(var1);
   }

   public Map getPKCmpf2ColumnForTable(String var1) {
      return (Map)this.table2cmpf2columnPKsOnly.get(var1);
   }

   public String getPKColumnName(String var1, String var2) {
      Map var3 = (Map)this.table2cmpf2columnPKsOnly.get(var1);
      return (String)var3.get(var2);
   }

   public boolean cmrfIsMultiPKTable(String var1) {
      return this.cmrfHasMultiPkTable.contains(var1);
   }

   public String getTableForCmrField(String var1) {
      return (String)this.cmrf2table.get(var1);
   }

   public int getTableIndexForCmrf(String var1) {
      String var2 = this.getTableForCmrField(var1);
      return var2 == null ? -1 : this.tableIndex(var2);
   }

   public List getCmrFields(String var1) {
      return (List)this.table2cmrf.get(var1);
   }

   public String getField(String var1) {
      return this.fieldName2columnName.get(var1) != null ? var1 : (String)this.variableName2cmrField.get(var1);
   }

   public Map getColumnMapForCmrfAndPkTable(String var1, String var2) {
      Map var3 = (Map)this.cmrf2pkTable2fkColumn2pkColumn.get(var1);
      return var3 == null ? null : (Map)var3.get(var2);
   }

   public Map getSymColumnMapForCmrfAndPkTable(String var1, String var2) {
      Map var3 = (Map)this.fkField2pkTable2symFkColumn2pkColumn.get(var1);
      return var3 == null ? null : (Map)var3.get(var2);
   }

   public void setPrimaryKeyFields(List var1) {
      this.primaryKeyFieldList = var1;
   }

   public List getPrimaryKeyFields() {
      return this.primaryKeyFieldList;
   }

   public boolean isPrimaryKeyField(String var1) {
      return this.primaryKeyFieldList.contains(var1);
   }

   public String getTableName() {
      return this.tableAt(0);
   }

   public String getQuotedTableName() {
      return RDBMSUtils.escQuotedID(this.getTableName());
   }

   public void addFieldGroup(FieldGroup var1) {
      this.fieldGroups.add(var1);
   }

   public List getFieldGroups() {
      return this.fieldGroups;
   }

   public FieldGroup getFieldGroup(String var1) {
      Iterator var2 = this.getFieldGroups().iterator();

      FieldGroup var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (FieldGroup)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void setupFieldGroupIndexes() {
      int var1 = 0;
      Iterator var2 = this.getFieldGroups().iterator();

      while(var2.hasNext()) {
         FieldGroup var3 = (FieldGroup)var2.next();
         var3.setIndex(var1++);
      }

   }

   public void addCmpFieldGroupNameMapping(String var1, String var2) {
      this.cmpFieldName2groupName.put(var1, var2);
   }

   public String getGroupNameForCmpField(String var1) {
      String var2 = null;
      var2 = (String)this.cmpFieldName2groupName.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         Iterator var3 = this.getFieldGroups().iterator();

         FieldGroup var4;
         boolean var5;
         do {
            if (!var3.hasNext()) {
               throw new AssertionError("RDBMSBean.getGroupNameForCmpField didn't find a group for field '" + var1 + "'.");
            }

            var4 = (FieldGroup)var3.next();
            var5 = var4.getCmpFields().contains(var1);
            var5 |= var4.getCmrFields().contains(var1);
         } while(!var5);

         var2 = var4.getName();
         return var2;
      }
   }

   public void addRelationshipCaching(RelationshipCaching var1) {
      this.relationshipCachings.add(var1);
   }

   public List getRelationshipCachings() {
      return this.relationshipCachings;
   }

   public RelationshipCaching getRelationshipCaching(String var1) {
      Iterator var2 = this.getRelationshipCachings().iterator();

      RelationshipCaching var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (RelationshipCaching)var2.next();
      } while(!var3.getCachingName().equals(var1));

      return var3;
   }

   public void setVerifyRows(String var1, String var2) {
      this.tableName2verifyRows.put(var1, var2);
      if ("read".equalsIgnoreCase(var2)) {
         this.verifyReads = true;
      } else {
         this.verifyReads = false;
      }

   }

   public String getVerifyRows(String var1) {
      return (String)this.tableName2verifyRows.get(var1);
   }

   public boolean getVerifyReads() {
      return this.verifyReads;
   }

   public void setVerifyColumns(String var1, String var2) {
      this.tableName2verifyColumns.put(var1, var2);
   }

   public String getVerifyColumns(String var1) {
      return (String)this.tableName2verifyColumns.get(var1);
   }

   public RDBMSBean getRDBMSBeanForAbstractSchema(String var1) {
      String var2 = this.getAbstractSchemaName();
      if (var2.equals(var1)) {
         return this;
      } else {
         Map var3 = this.getBeanMap();
         Iterator var4 = var3.keySet().iterator();

         RDBMSBean var6;
         do {
            if (!var4.hasNext()) {
               return null;
            }

            String var5 = (String)var4.next();
            var6 = (RDBMSBean)this.rdbmsBeanMap.get(var5);
            var2 = var6.getAbstractSchemaName();
         } while(var2 == null || !var2.equals(var1));

         return var6;
      }
   }

   public boolean containsRdbmsFinder(RDBMSFinder var1) {
      return this.rdbmsFinders.containsKey(new RDBMSFinder.FinderKey(var1));
   }

   public void addRdbmsFinder(RDBMSFinder var1) {
      this.rdbmsFinders.put(new RDBMSFinder.FinderKey(var1), var1);
   }

   public Map getRdbmsFinders() {
      return this.rdbmsFinders;
   }

   public boolean isQueryCachingEnabledForCMRField(String var1) {
      CMPBeanDescriptor var2 = this.getRelatedDescriptor(var1);
      if (!var2.isReadOnly()) {
         return false;
      } else {
         if (this.qcEnabledCmrFields != null && this.qcEnabledCmrFields.contains(var1)) {
            RDBMSBean var3 = this.getRelatedRDBMSBean(var1);
            String var4 = this.getRelatedFieldName(var1);
            if (!var3.relatedFieldIsFkOwner(var4) || this.isManyToManyRelation(var1)) {
               return true;
            }

            this.qcEnabledCmrFields.remove(var1);
         }

         return false;
      }
   }

   private String methodPrefix() {
      return "findBy";
   }

   public String finderMethodName(CMPBeanDescriptor var1, String var2) {
      String var3 = MethodUtils.tail(var1.getGeneratedBeanClassName());
      return this.methodPrefix() + var3 + "_" + var2 + "__WL_";
   }

   public String variableForField(String var1, String var2, String var3) {
      Debug.assertion(var1 != null);
      Debug.assertion(var2 != null);
      Debug.assertion(var3 != null);
      String var4 = null;
      if (this.hasCmpField(var2, var3)) {
         var4 = this.getCmpField(var2, var3);
      } else {
         var4 = "__WL_" + var1;
         if (!this.isRemoteField(var1)) {
            var4 = var4 + "_" + this.getRelatedPkFieldName(var1, var3);
         }
      }

      return var4;
   }

   private void addReverseColumnMapping(String var1, String var2, String var3) {
      Object var4 = (Map)this.table2column2variable.get(var1);
      if (var4 == null) {
         var4 = new HashMap();
         this.table2column2variable.put(var1, var4);
      }

      ((Map)var4).put(var2, var3);
   }

   public boolean isReadOnly() {
      return this.bd.isReadOnly();
   }

   public boolean isOptimistic() {
      return this.bd.isOptimistic();
   }

   public boolean getCacheBetweenTransactions() {
      return this.bd.getCacheBetweenTransactions();
   }

   public boolean hasOptimisticColumn(String var1) {
      if (!"version".equalsIgnoreCase(this.getVerifyColumns(var1)) && !"timestamp".equalsIgnoreCase(this.getVerifyColumns(var1))) {
         return false;
      } else {
         assert this.tableName2optimisticColumn.get(var1) != null;

         return this.tableName2optimisticColumn.get(var1) != null;
      }
   }

   public String getOptimisticColumn(String var1) {
      return (String)this.tableName2optimisticColumn.get(var1);
   }

   public boolean normalizeMultiTables_done() {
      return this.normalizeMultiTables_done;
   }

   private void addOptimisticFields() {
      Iterator var1 = this.bd.getCMFieldNames().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         Class var3 = this.bd.getFieldClass(var2);
         this.cmpFieldClasses.put(var2, var3);
      }

      Iterator var5 = this.tableNamesList.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         if (this.hasOptimisticColumn(var6)) {
            String var4 = this.getCmpField(var6, this.getOptimisticColumn(var6));
            if (var4 == null) {
               var4 = "__WL_optimisticField" + this.tableIndex(var6);
               this.addTableFieldColumnMapping(var6, var4, this.getOptimisticColumn(var6));
               if (this.getVerifyColumns(var6).equalsIgnoreCase("version")) {
                  this.cmpFieldClasses.put(var4, Long.class);
               } else {
                  if (!this.getVerifyColumns(var6).equalsIgnoreCase("timestamp")) {
                     throw new AssertionError("invalid value for verify-columns: " + this.getVerifyColumns(var6));
                  }

                  this.cmpFieldClasses.put(var4, Timestamp.class);
               }
            }
         }
      }

   }

   public void normalizeMultiTables(CMPBeanDescriptor var1) {
      if (!this.normalizeMultiTables_done) {
         this.normalizeMultiTables_done = true;
         this.bd = var1;
         this.addOptimisticFields();
         Iterator var2 = this.tableNamesList.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            HashMap var4 = new HashMap();
            HashMap var5 = new HashMap();
            Map var6 = (Map)this.tableName2cmpFieldName2columnName.get(var3);
            Iterator var7 = var6.keySet().iterator();

            while(var7.hasNext()) {
               String var8 = (String)var7.next();
               String var9 = (String)var6.get(var8);
               if (this.primaryKeyFieldList.contains(var8)) {
                  var4.put(var8, var9);
                  Object var10 = (Map)this.pkCmpF2Table2Column.get(var8);
                  if (var10 == null) {
                     var10 = new HashMap();
                     this.pkCmpF2Table2Column.put(var8, var10);
                  }

                  ((Map)var10).put(var3, var9);
               } else {
                  var5.put(var8, var9);
               }
            }

            this.table2cmpf2columnPKsOnly.put(var3, var4);
            this.table2cmpf2columnNoPKs.put(var3, var5);
         }

      }
   }

   public boolean initialized() {
      return this.synthesized;
   }

   public void processDescriptors(Map var1, Relationships var2, Map var3, Map var4, Map var5, Map var6) throws RDBMSException {
      if (debugLogger.isDebugEnabled()) {
         debug("called RDBMSBean.processDescriptors");
      }

      assert var1 != null;

      assert var4 != null;

      assert var5 != null;

      if (!this.synthesized) {
         this.synthesized = true;
         this.beanMap = var1;
         this.relationships = var2;
         this.dependentMap = var3;
         this.rdbmsBeanMap = var4;
         this.rdbmsRelationMap = var5;
         this.rdbmsDependentMap = var6;

         assert this.bd != null;

         if (var2 != null) {
            if (debugLogger.isDebugEnabled()) {
               debug("processing relationships...");
            }

            Map var7 = var2.getAllEjbRelations();
            Iterator var8 = var7.keySet().iterator();

            label216:
            while(true) {
               while(true) {
                  if (!var8.hasNext()) {
                     break label216;
                  }

                  String var9 = (String)var8.next();
                  if (debugLogger.isDebugEnabled()) {
                     debug("processing relationship: " + var9);
                  }

                  EjbRelation var10 = (EjbRelation)var7.get(var9);
                  RDBMSRelation var11 = (RDBMSRelation)var5.get(var9);

                  assert var11 != null;

                  Iterator var12 = var10.getAllEjbRelationshipRoles().iterator();
                  EjbRelationshipRole var13 = (EjbRelationshipRole)var12.next();
                  EjbRelationshipRole var14 = (EjbRelationshipRole)var12.next();

                  assert var13 != null;

                  assert var14 != null;

                  RDBMSRelation.RDBMSRole var15 = var11.getRole1();
                  RDBMSRelation.RDBMSRole var16 = var11.getRole2();
                  Debug.assertion(var15 != null);
                  if (!var15.getName().equals(var13.getName())) {
                     RDBMSRelation.RDBMSRole var17 = var15;
                     var15 = var16;
                     var16 = var17;
                  }

                  assert var15 == null || var13.getName().equals(var15.getName());

                  assert var16 == null || var14.getName().equals(var16.getName());

                  if (var15 != null && var15.getColumnMap().size() > 0) {
                     this.processRole(var13, var14, var15, var16, var11);
                     this.processRole(var14, var13, var16, var15, var11);
                  } else {
                     this.processRole(var14, var13, var16, var15, var11);
                     this.processRole(var13, var14, var15, var16, var11);
                  }
               }
            }
         }

         this.calculateLoadModifyIndex();
         Iterator var18 = this.getFieldGroups().iterator();

         FieldGroup var19;
         HashSet var21;
         Iterator var23;
         String var25;
         Iterator var27;
         String var28;
         String var29;
         label189:
         while(var18.hasNext()) {
            var19 = (FieldGroup)var18.next();
            var21 = new HashSet();
            this.groupName2tableNames.put(var19.getName(), var21);
            var23 = var19.getCmrFields().iterator();

            while(true) {
               do {
                  if (!var23.hasNext()) {
                     var27 = ((Collection)var19.getCmpFields().clone()).iterator();

                     String var30;
                     while(var27.hasNext()) {
                        var28 = (String)var27.next();
                        var29 = this.getTableForCmpField(var28);
                        var21.add(var29);
                        if (this.hasOptimisticColumn(var29)) {
                           var30 = this.getCmpField(var29, this.getOptimisticColumn(var29));
                           if (!var19.getCmpFields().contains(var30)) {
                              var19.addCmpField(var30);
                           }
                        }
                     }

                     var23 = ((Collection)var19.getCmrFields().clone()).iterator();

                     while(var23.hasNext()) {
                        var28 = (String)var23.next();
                        var29 = this.getTableForCmrField(var28);
                        var21.add(var29);
                        if (this.hasOptimisticColumn(var29)) {
                           var30 = this.getCmpField(var29, this.getOptimisticColumn(var29));
                           if (!var19.getCmpFields().contains(var30)) {
                              var19.addCmpField(var30);
                           }
                        }
                     }
                     continue label189;
                  }

                  var25 = (String)var23.next();
               } while(this.isForeignKeyField(var25) && this.containsFkField(var25));

               var23.remove();
            }
         }

         if (this.isOptimistic()) {
            HashSet var20 = new HashSet();
            Iterator var22 = this.tableName2optimisticColumnTrigger.keySet().iterator();

            while(var22.hasNext()) {
               String var24 = (String)var22.next();
               if (this.getTriggerUpdatesOptimisticColumn(var24) && "timestamp".equalsIgnoreCase(this.getVerifyColumns(var24))) {
                  var20.add(var24);
               }
            }

            if (!var20.isEmpty()) {
               FieldGroup var26 = new FieldGroup();
               var26.setName("optimisticTimestampTriggerGroup");
               this.groupName2tableNames.put(var26.getName(), var20);

               for(var27 = var20.iterator(); var27.hasNext(); var26.addCmpField(var29)) {
                  var28 = (String)var27.next();
                  var29 = this.getCmpField(var28, this.getOptimisticColumn(var28));
                  if (var29 == null) {
                     var29 = "__WL_optimisticField" + this.tableIndex(var28);
                  }
               }

               this.addFieldGroup(var26);
            }
         }

         var19 = new FieldGroup();
         var19.setName("defaultGroup");
         var21 = new HashSet();
         this.groupName2tableNames.put(var19.getName(), var21);
         var23 = this.getCmpFieldNames().iterator();

         while(var23.hasNext()) {
            var25 = (String)var23.next();
            var28 = this.getTableForCmpField(var25);
            var21.add(var28);
            var19.addCmpField(var25);
         }

         var27 = this.getForeignKeyFieldNames().iterator();

         while(var27.hasNext()) {
            var28 = (String)var27.next();
            if (this.containsFkField(var28)) {
               var29 = this.getTableForCmrField(var28);
               var21.add(var29);
               var19.addCmrField(var28);
            }
         }

         this.addFieldGroup(var19);
         if (debugLogger.isDebugEnabled()) {
            this.printDebugInfo();
         }

         if (debugLogger.isDebugEnabled()) {
            this.printCmpFieldDebugInfo();
         }

      }
   }

   private void processRole(EjbRelationshipRole var1, EjbRelationshipRole var2, RDBMSRelation.RDBMSRole var3, RDBMSRelation.RDBMSRole var4, RDBMSRelation var5) throws RDBMSException {
      if (debugLogger.isDebugEnabled()) {
         debug("processing role: " + var1.getName());
         debug("other role is: " + var2.getName());
         debug(var3 == null ? "wlRole is null" : "wlRole=" + var3.getName());
         debug(var4 == null ? "wlOther is null" : "wlOther=" + var4.getName());
      }

      RoleSource var6 = var1.getRoleSource();
      String var7 = var6.getEjbName();
      if (this.getEjbName().equals(var7)) {
         CmrField var8 = var1.getCmrField();
         RoleSource var9 = var2.getRoleSource();
         String var10 = var9.getEjbName();
         CmrField var11 = var2.getCmrField();
         Class var12 = null;
         String var13 = null;
         boolean var14 = var1.getMultiplicity().equals("Many") && var2.getMultiplicity().equals("Many");
         boolean var15 = var1.getMultiplicity().equals("One") && var2.getMultiplicity().equals("Many");
         boolean var16 = var1.getMultiplicity().equals("One") && var2.getMultiplicity().equals("One");
         String var17 = RDBMSUtils.getCmrFieldName(var1, var2);
         if (this.cmrFieldNames.contains(var17)) {
            this.processSymmetricRole(var16, var14, var17, var1, var2, var3, var4);
         } else {
            this.cmrFieldNames.add(var17);
            RDBMSBean var18 = null;
            if (var14) {
               var18 = this;
            } else {
               var18 = (RDBMSBean)this.rdbmsBeanMap.get(var10);
            }

            if (var18.hasMultipleTables()) {
               this.cmrfHasMultiPkTable.add(var17);
            }

            if (!var14 && var7.equalsIgnoreCase(var10)) {
               this.fieldName2selfRel.put(var17, new Boolean(true));
               this.selfRelationship = true;
            } else {
               this.fieldName2selfRel.put(var17, new Boolean(false));
            }

            this.fieldName2cascadeDelete.put(var17, new Boolean(var2.getCascadeDelete()));
            if (var4 != null) {
               this.fieldName2DBCascadeDelete.put(var17, new Boolean(var4.getDBCascadeDelete()));
            } else {
               this.fieldName2DBCascadeDelete.put(var17, new Boolean(false));
            }

            if (var8 != null) {
               if (debugLogger.isDebugEnabled()) {
                  debug("processing declared field: " + var1.getName());
               }

               var13 = var8.getType();
               this.declaredFieldNames.add(var17);
            }

            if (var13 == null) {
               if (!var15 && !var14) {
                  var12 = this.getElementalClass(var2);
               } else {
                  var12 = Collection.class;
               }
            } else if ("java.util.Collection".equals(var13)) {
               var12 = Collection.class;
            } else {
               if (!"java.util.Set".equals(var13)) {
                  throw new AssertionError("invalid className: " + var13);
               }

               var12 = Set.class;
            }

            this.fieldName2class.put(var17, var12);
            this.calculateRelationshipType(var17, var1, var2, var3, var4);
            if (var3 != null) {
               this.processWeblogicRole_PhaseOne(var17, var1, var3, var14, var18);
               this.processWeblogicRole_PhaseTwo(var17, var1, var2, var3, var4, var5, var14, var15, var9);
               if (var3.isQueryCachingEnabled()) {
                  String var19 = var8.getName();
                  if (!var18.isReadOnly()) {
                     this.log.logWarning(this.fmt.QUERY_CACHING_ENABLED_FOR_CMR_TO_RW_BEAN(this.getEjbName(), var3.getName(), this.getRelatedRDBMSBean(var19).getEjbName()));
                  } else {
                     if (this.qcEnabledCmrFields == null) {
                        this.qcEnabledCmrFields = new HashSet();
                     }

                     this.qcEnabledCmrFields.add(var19);
                  }
               }
            }

         }
      }
   }

   public boolean isSelfRelationship(String var1) {
      return (Boolean)this.fieldName2selfRel.get(var1);
   }

   public boolean isSelfRelationship() {
      return this.selfRelationship;
   }

   private void processWeblogicRole_PhaseOne(String var1, EjbRelationshipRole var2, RDBMSRelation.RDBMSRole var3, boolean var4, RDBMSBean var5) throws RDBMSException {
      this.initializeRole(var3, var4);
      if (var3.getGroupName() != null) {
         this.fieldName2groupName.put(var1, var3.getGroupName());
      }

      assert var3.getColumnMap() != null;

      if (var3.getColumnMap().size() > 0) {
         if (debugLogger.isDebugEnabled()) {
            debug("processing foreign key owner: " + var2.getName());
            debug("foreign key field name: " + var1);
         }

         String var6 = var3.getForeignKeyTableName();
         if (!var4) {
            Object var7 = (List)this.table2cmrf.get(var6);
            if (var7 == null) {
               var7 = new ArrayList();
               this.table2cmrf.put(var6, var7);
            }

            ((List)var7).add(var1);
            this.cmrf2table.put(var1, var6);
         }

         Map var18 = var3.getColumnMap();
         this.normalizeColumnNames(var4, var6, var18);
         ArrayList var8 = new ArrayList(var18.keySet());
         this.fkField2fkColumns.put(var1, var8);
         Debug.assertion(this.cmrf2pkTable2fkColumn2pkColumn.get(var1) == null);
         HashMap var9 = new HashMap();
         this.cmrf2pkTable2fkColumn2pkColumn.put(var1, var9);
         String var10 = var3.getPrimaryKeyTableName();
         if (var10 == null) {
            var10 = var5.getTableName();
         }

         Debug.assertion(var10 != null);
         Iterator var11 = var5.getTables().iterator();

         while(var11.hasNext()) {
            String var12 = (String)var11.next();
            Debug.assertion(var9.get(var12) == null);
            HashMap var13 = new HashMap();
            var9.put(var12, var13);

            String var15;
            String var16;
            for(Iterator var14 = var18.keySet().iterator(); var14.hasNext(); var13.put(var15, var16)) {
               var15 = (String)var14.next();
               var16 = (String)var18.get(var15);
               if (!var10.equalsIgnoreCase(var12)) {
                  String var17 = var5.getCmpField(var10, var16);
                  var16 = var5.getColumnForCmpFieldAndTable(var17, var12);
               }
            }
         }
      }

   }

   private void processWeblogicRole_PhaseTwo(String var1, EjbRelationshipRole var2, EjbRelationshipRole var3, RDBMSRelation.RDBMSRole var4, RDBMSRelation.RDBMSRole var5, RDBMSRelation var6, boolean var7, boolean var8, RoleSource var9) throws RDBMSException {
      assert var4.getColumnMap() != null;

      if (var4.getColumnMap().size() > 0) {
         if (debugLogger.isDebugEnabled()) {
            debug("processing foreign key owner: " + var2.getName());
            debug("foreign key field name: " + var1);
         }

         this.fkFieldNames.add(var1);
         RDBMSBean var10 = this.getTargetBean(var7, var1, var6, var9, var5);
         CMPBeanDescriptor var11 = var10.getCMPBeanDescriptor();
         String var12 = var4.getForeignKeyTableName();
         Map var13 = var4.getColumnMap();
         String var14 = null;
         if (var10.hasMultipleTables()) {
            var14 = var4.getPrimaryKeyTableName();
         } else {
            var14 = var10.getTableName();
         }

         assert var14 != null;

         HashMap var15 = new HashMap();
         HashMap var16 = new HashMap();
         this.fkField2fkColumn2Class.put(var1, var15);
         this.fkField2fkColumn2FieldName.put(var1, var16);
         if (var6.getTableName() != null) {
            this.table2fkCol2fkClass.put(var6.getTableName(), var15);
         } else {
            this.table2fkCol2fkClass.put(var12, var15);
         }

         assert this.getForeignKeyColNames(var1) != null;

         Iterator var17 = this.getForeignKeyColNames(var1).iterator();
         boolean var18 = false;

         boolean var19;
         String var20;
         Class var24;
         for(var19 = false; var17.hasNext(); var15.put(var20, var24)) {
            var20 = (String)var17.next();
            String var21 = (String)var13.get(var20);
            if (debugLogger.isDebugEnabled()) {
               debug("processing column pair ( Fk Column: '" + var20 + "', Pk Column '" + var21 + "') and primary key table-" + var14);
            }

            String var22 = var10.getCmpField(var14, var21);

            assert var22 != null;

            assert var11.getPrimaryKeyFieldNames().contains(var22);

            if (debugLogger.isDebugEnabled()) {
               debug("found key column field pair (" + var21 + " " + var22 + ")");
            }

            var16.put(var20, var22);
            Class var23 = var11.getFieldClass(var22);
            var24 = ClassUtils.getObjectClass(var23);
            if (!var7) {
               String var25 = this.variableForField(var1, var12, var20);
               this.variableName2columnName.put(var25, var20);
               this.variableName2table.put(var25, var12);
               this.variableName2cmrField.put(var25, var1);
               this.addReverseColumnMapping(var12, var20, var25);
               if (this.hasCmpField(var12, var20)) {
                  String var26 = this.getCmpField(var12, var20);
                  var19 = true;
                  this.cmrMappedcmpFields.add(var26);
                  var24 = this.bd.getFieldClass(var26);
                  if (this.bd.getPrimaryKeyFieldNames().contains(var26)) {
                     var18 = true;
                  }
               }
            }
         }

         if (var18) {
            this.fkPkFieldNames.add(var1);
         }

         if (var19) {
            this.fkCmpFieldNames.add(var1);
         }
      }

   }

   private void calculateLoadModifyIndex() {
      int var1 = this.tableCount();
      this.tableIndexToFieldList = new List[var1];
      this.tableIndexToCMPFieldList = new List[var1];
      this.tableIndexToCMRFieldList = new List[var1];
      this.tableIndexToNonPKFieldList = new List[var1];
      Iterator var2 = this.getTables().iterator();

      int var3;
      for(var3 = 0; var2.hasNext(); ++var3) {
         var2.next();
         this.tableIndexToFieldList[var3] = new ArrayList();
         this.tableIndexToCMPFieldList[var3] = new ArrayList();
         this.tableIndexToCMRFieldList[var3] = new ArrayList();
         this.tableIndexToNonPKFieldList[var3] = new ArrayList();
      }

      String var6;
      List var8;
      List var9;
      for(Iterator var4 = this.cmpFieldNames.iterator(); var4.hasNext(); ++this.numFields) {
         String var5 = (String)var4.next();
         this.fieldNameToIsModifiedIndex.put(var5, new Integer(this.numFields));
         this.isModifiedIndexToFieldName.put(new Integer(this.numFields), var5);
         var6 = this.getTableForCmpField(var5);
         var3 = this.tableIndex(var6);
         this.isModifiedIndexToTableNumber.add(new Integer(var3));
         if (this.isPrimaryKeyField(var5)) {
            this.isModifiedPKIndexList.add(new Integer(this.numFields));

            for(int var12 = 0; var12 < this.tableCount(); ++var12) {
               var8 = this.tableIndexToFieldList[var12];
               var8.add(var5);
               var9 = this.tableIndexToCMPFieldList[var12];
               var9.add(var5);
            }
         } else {
            List var7 = this.tableIndexToFieldList[var3];
            var7.add(var5);
            var8 = this.tableIndexToCMPFieldList[var3];
            var8.add(var5);
            var9 = this.tableIndexToNonPKFieldList[var3];
            var9.add(var5);
         }
      }

      Iterator var11 = this.getForeignKeyFieldNames().iterator();

      while(var11.hasNext()) {
         var6 = (String)var11.next();
         if (this.containsFkField(var6) && !this.isForeignCmpField(var6)) {
            this.fieldNameToIsModifiedIndex.put(var6, new Integer(this.numFields));
            this.isModifiedIndexToFieldName.put(new Integer(this.numFields), var6);
            String var13 = this.getTableForCmrField(var6);
            var3 = this.tableIndex(var13);
            this.isModifiedIndexToTableNumber.add(new Integer(var3));
            var8 = this.tableIndexToFieldList[var3];
            var8.add(var6);
            var9 = this.tableIndexToCMRFieldList[var3];
            var9.add(var6);
            List var10 = this.tableIndexToNonPKFieldList[var3];
            var10.add(var6);
            ++this.numFields;
         }
      }

   }

   public void setupRelatedBeanMap() {
      if (this.relationships != null) {
         if (debugLogger.isDebugEnabled()) {
            debug("processing relationships...");
         }

         Map var1 = this.relationships.getAllEjbRelations();
         Iterator var2 = var1.keySet().iterator();

         while(true) {
            EjbRelation var4;
            RDBMSRelation var5;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               String var3 = (String)var2.next();
               if (debugLogger.isDebugEnabled()) {
                  debug("processing relationship: " + var3);
               }

               var4 = (EjbRelation)var1.get(var3);
               var5 = (RDBMSRelation)this.rdbmsRelationMap.get(var3);
            } while(var5.getTableName() == null);

            assert var5 != null;

            Iterator var6 = var4.getAllEjbRelationshipRoles().iterator();
            EjbRelationshipRole var7 = (EjbRelationshipRole)var6.next();
            EjbRelationshipRole var8 = (EjbRelationshipRole)var6.next();
            if (debugLogger.isDebugEnabled()) {
               Debug.assertion(var7 != null);
            }

            if (debugLogger.isDebugEnabled()) {
               Debug.assertion(var8 != null);
            }

            RDBMSRelation.RDBMSRole var9 = var5.getRole1();
            RDBMSRelation.RDBMSRole var10 = var5.getRole2();
            Debug.assertion(var9 != null);
            if (!var9.getName().equals(var7.getName())) {
               RDBMSRelation.RDBMSRole var11 = var9;
               var9 = var10;
               var10 = var11;
            }

            boolean var14 = var7.getMultiplicity().equals("Many") && var8.getMultiplicity().equals("Many");
            if (var14) {
               RoleSource var12 = var7.getRoleSource();
               String var13 = var12.getEjbName();
               if (this.getEjbName().equals(var13)) {
                  this.createRelatedBeanMap(var7, var8, var9, var10, var5);
               } else {
                  this.createRelatedBeanMap(var8, var7, var10, var9, var5);
               }
            }
         }
      }
   }

   private void createRelatedBeanMap(EjbRelationshipRole var1, EjbRelationshipRole var2, RDBMSRelation.RDBMSRole var3, RDBMSRelation.RDBMSRole var4, RDBMSRelation var5) {
      RoleSource var6 = var2.getRoleSource();
      String var7 = var6.getEjbName();
      RDBMSBean var8 = (RDBMSBean)this.rdbmsBeanMap.get(var7);
      String var9 = var5.getTableName();
      HashMap var10 = new HashMap();
      Map var11 = var4.getColumnMap();
      Set var12 = var11.entrySet();
      Iterator var13 = var12.iterator();

      while(var13.hasNext()) {
         Map.Entry var14 = (Map.Entry)var13.next();
         String var15 = (String)var14.getKey();
         String var16 = (String)var14.getValue();
         var10.put(var15, var8);
      }

      this.table2fkCol2RelatedBean.put(var9, var10);
   }

   private void initializeRole(RDBMSRelation.RDBMSRole var1, boolean var2) {
      if (!var2 && var1.getColumnMap().size() > 0 && !this.hasMultipleTables()) {
         var1.setForeignKeyTableName(this.getTableName());
      }

   }

   private void processSymmetricRole(boolean var1, boolean var2, String var3, EjbRelationshipRole var4, EjbRelationshipRole var5, RDBMSRelation.RDBMSRole var6, RDBMSRelation.RDBMSRole var7) {
      Debug.assertion(var1 || var2);
      if (var2) {
         this.computeSymmetricColumnInfo(var3, var6, var5);
      }

      if (var1) {
         this.fieldName2cascadeDelete.put(var3, new Boolean(var4.getCascadeDelete() || var5.getCascadeDelete()));
         boolean var8 = var6 == null ? false : var6.getDBCascadeDelete();
         boolean var9 = var7 == null ? false : var7.getDBCascadeDelete();
         this.fieldName2DBCascadeDelete.put(var3, new Boolean(var8 || var9));
      }

   }

   private RDBMSBean getTargetBean(boolean var1, String var2, RDBMSRelation var3, RoleSource var4, RDBMSRelation.RDBMSRole var5) throws RDBMSException {
      String var6 = var4.getEjbName();
      RDBMSBean var7 = null;
      if (var1) {
         this.fieldName2tableName.put(var2, var3.getTableName());
         var7 = this;
      } else {
         String var8 = var3.getTableName();
         if (var8 != null && var8.length() > 0) {
            Loggable var9 = EJBLogger.logshouldNotDefineJoinTableForOneToManyLoggable(var3.getName(), var8);
            throw new RDBMSException(var9.getMessage());
         }

         var7 = (RDBMSBean)this.rdbmsBeanMap.get(var6);
      }

      return var7;
   }

   private void normalizeColumnNames(boolean var1, String var2, Map var3) {
      if (!var1) {
         Map var4 = (Map)((HashMap)var3).clone();
         Map var5 = (Map)this.table2column2cmpf.get(var2);
         Iterator var6 = var4.keySet().iterator();

         while(true) {
            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               Iterator var8 = var5.keySet().iterator();

               while(var8.hasNext()) {
                  String var9 = (String)var8.next();
                  if (var9.equalsIgnoreCase(var7) && !var9.equals(var7)) {
                     String var10 = (String)var3.get(var7);
                     var3.remove(var7);
                     var3.put(var9, var10);
                     break;
                  }
               }
            }

            return;
         }
      }
   }

   private void calculateRelationshipType(String var1, EjbRelationshipRole var2, EjbRelationshipRole var3, RDBMSRelation.RDBMSRole var4, RDBMSRelation.RDBMSRole var5) {
      if (var2.getMultiplicity().equals("One")) {
         if (var3.getMultiplicity().equals("One")) {
            this.one2one.add(var1);
         } else {
            this.one2many.add(var1);
         }
      } else if (var3.getMultiplicity().equals("Many")) {
         this.many2many.add(var1);
      } else {
         this.one2many.add(var1);
      }

      if (var2.getCmrField() != null && var3.getCmrField() != null) {
         this.biDirectional.add(var1);
      }

      this.fieldName2relatedMultiplicity.put(var1, var3.getMultiplicity());
      this.fieldName2relatedFieldOwnsFk.put(var1, new Boolean(var5 != null && var5.getColumnMap().size() > 0));
      String var6 = RDBMSUtils.getCmrFieldName(var3, var2);
      this.fieldName2RelatedFieldName.put(var1, var6);
      RoleSource var7 = var3.getRoleSource();
      String var8 = var7.getEjbName();
      CMPBeanDescriptor var9 = (CMPBeanDescriptor)this.beanMap.get(var8);
      this.fieldName2relatedDescriptor.put(var1, var9);
      this.fieldName2relatedRDBMSBean.put(var1, this.rdbmsBeanMap.get(var8));
      this.fieldName2relatedClassName.put(var1, var9.getGeneratedBeanClassName());
   }

   private Class getElementalClass(EjbRelationshipRole var1) throws RDBMSException {
      RoleSource var2 = var1.getRoleSource();
      CMPBeanDescriptor var3 = (CMPBeanDescriptor)this.beanMap.get(var2.getEjbName());
      Debug.assertion(var3 != null);
      if (var3.hasLocalClientView()) {
         return var3.getLocalInterfaceClass();
      } else {
         return var3.isEJB30() ? var3.getBeanClass() : var3.getRemoteInterfaceClass();
      }
   }

   private void computeSymmetricColumnInfo(String var1, RDBMSRelation.RDBMSRole var2, EjbRelationshipRole var3) {
      Map var4 = var2.getColumnMap();
      ArrayList var5 = new ArrayList(var4.keySet());
      this.fkField2symColumns.put(var1, var5);
      HashMap var6 = new HashMap();
      this.fkField2symColumn2FieldName.put(var1, var6);
      Iterator var7 = var5.iterator();

      while(var7.hasNext()) {
         String var8 = (String)var7.next();
         String var9 = (String)var4.get(var8);
         if (debugLogger.isDebugEnabled()) {
            debug("processing column pair (" + var8 + " " + var9 + ")");
         }

         String var10 = this.getCmpFieldForColumn(var9);
         if (debugLogger.isDebugEnabled()) {
            debug("found key column field pair (" + var9 + " " + var10 + ")");
         }

         assert var10 != null;

         var6.put(var8, var10);
      }

      Object var19 = (Map)this.fkField2pkTable2symFkColumn2pkColumn.get(var1);
      if (var19 == null) {
         var19 = new HashMap();
         this.fkField2pkTable2symFkColumn2pkColumn.put(var1, var19);
      }

      RDBMSBean var20 = this;
      Map var21 = var2.getColumnMap();
      String var11 = null;
      if (var2.getPrimaryKeyTableName() != null) {
         var11 = var2.getPrimaryKeyTableName();
      } else {
         var11 = this.getTableName();
      }

      Iterator var12 = this.getTables().iterator();

      while(var12.hasNext()) {
         String var13 = (String)var12.next();
         Object var14 = (Map)((Map)var19).get(var13);
         if (var14 == null) {
            var14 = new HashMap();
            ((Map)var19).put(var13, var14);
         }

         Iterator var15 = var21.keySet().iterator();

         while(var15.hasNext()) {
            String var16 = (String)var15.next();
            String var17 = (String)var21.get(var16);
            if (!var13.equals(var11)) {
               String var18 = var20.getCmpField(var11, var17);
               var17 = var20.getColumnForCmpFieldAndTable(var18, var13);
            }

            this.getCmpField(var13, var17);
            ((Map)var14).put(var16, var17);
         }
      }

   }

   public void printDebugInfo() {
      debug("ejbName- " + this.getEjbName());
      Iterator var1 = this.cmrFieldNames.iterator();
      debug("All Field Names-------------------------------------");

      while(var1.hasNext()) {
         debug((String)var1.next());
      }

      var1 = this.declaredFieldNames.iterator();
      debug("Declared Field Names--------------------------------");

      while(var1.hasNext()) {
         debug((String)var1.next());
      }

      var1 = this.fkFieldNames.iterator();
      debug("Foreign Key Field Names-----------------------------");

      while(var1.hasNext()) {
         debug((String)var1.next());
      }

      var1 = this.fkPkFieldNames.iterator();
      debug("Foreign Key Primary Key Field Names-----------------");

      while(var1.hasNext()) {
         debug((String)var1.next());
      }

      var1 = this.fkCmpFieldNames.iterator();
      debug("Foreign Key CMP Field Names-------------------------");

      while(var1.hasNext()) {
         debug((String)var1.next());
      }

      var1 = this.fieldName2class.keySet().iterator();
      debug("Field Name To Class Mapping-------------------------");

      String var2;
      while(var1.hasNext()) {
         var2 = (String)var1.next();
         Debug.assertion(this.fieldName2class.get(var2) != null);
         debug(var2 + ", \t" + ((Class)this.fieldName2class.get(var2)).getName());
      }

      var1 = this.fkField2fkColumns.keySet().iterator();
      debug("FK Field Name To Column Name Mapping----------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         Iterator var3 = ((List)this.fkField2fkColumns.get(var2)).iterator();

         while(var3.hasNext()) {
            debug(var2 + ", \t" + (String)var3.next());
         }
      }

      var1 = this.fieldName2relatedClassName.keySet().iterator();
      debug("Field Name To Related Class Name  Mapping-----------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2 + ", \t" + (String)this.fieldName2relatedClassName.get(var2));
      }

      var1 = this.fieldName2relatedFieldOwnsFk.keySet().iterator();
      debug("Field Name To Related Field Owns FK Mapping---------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2 + ", \t" + this.fieldName2relatedFieldOwnsFk.get(var2));
      }

      var1 = this.fieldName2relatedMultiplicity.keySet().iterator();
      debug("Field Name To Related Multiplicity Mapping----------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2 + ", \t" + this.fieldName2relatedMultiplicity.get(var2));
      }

      var1 = this.fieldName2relatedDescriptor.keySet().iterator();
      debug("Field Name To Related Descriptor Mapping------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2 + ", \t" + ((CMPBeanDescriptor)this.fieldName2relatedDescriptor.get(var2)).getEJBName());
      }

      var1 = this.fieldName2relatedRDBMSBean.keySet().iterator();
      debug("Field Name To Related RDBMS Bean  Mapping------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2 + ", \t" + ((RDBMSBean)this.fieldName2relatedRDBMSBean.get(var2)).getEjbName());
      }

      var1 = this.cmrf2table.keySet().iterator();
      debug("CMR Field Name To Table Mapping----------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2 + ", \t" + (String)this.cmrf2table.get(var2));
      }

      var1 = this.fkField2fkColumn2FieldName.keySet().iterator();
      debug("FK Field Name To Column Name Mapping----------------");

      Iterator var4;
      String var5;
      Map var9;
      while(var1.hasNext()) {
         var2 = (String)var1.next();
         var9 = (Map)this.fkField2fkColumn2FieldName.get(var2);
         var4 = var9.keySet().iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            debug(var2 + ", \t" + var5 + ", \t" + (String)var9.get(var5));
         }
      }

      var1 = this.fieldName2RelatedFieldName.keySet().iterator();
      debug("Field Name To Related Field Name  Mapping-----------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2 + ", \t" + (String)this.fieldName2RelatedFieldName.get(var2));
      }

      var1 = this.biDirectional.iterator();
      debug("Bidirectional fields--------------------------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2);
      }

      var1 = this.one2one.iterator();
      debug("1 to 1 fields--------------------------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2);
      }

      var1 = this.one2many.iterator();
      debug("1 to N fields--------------------------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2);
      }

      var1 = this.many2many.iterator();
      debug("N to M fields--------------------------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2);
      }

      var1 = this.fkField2fkColumn2Class.keySet().iterator();
      debug("FK Field Name To Column To Class-------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         var9 = (Map)this.fkField2fkColumn2Class.get(var2);
         var4 = var9.keySet().iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            debug(var2 + ", \t" + var5 + ", \t" + ((Class)var9.get(var5)).getName());
         }
      }

      var1 = this.fkField2symColumns.keySet().iterator();
      debug("Symmetric M-N Field To FK Columns------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2);
         List var10 = (List)this.fkField2symColumns.get(var2);
         var4 = var10.iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            debug(" \t" + var5);
         }
      }

      var1 = this.fkField2symColumn2FieldName.keySet().iterator();
      debug("Symmetric M-N Field To FK Column To Field--------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         var9 = (Map)this.fkField2symColumn2FieldName.get(var2);
         var4 = var9.keySet().iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            debug(var2 + ", \t" + var5 + ", \t" + (String)var9.get(var5));
         }
      }

      var1 = this.fkField2pkTable2symFkColumn2pkColumn.keySet().iterator();
      debug("Symmetric M-N Field To PK Table To FK Column To PK Column ----");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(" Symmetric CMR Field: " + var2);
         var9 = (Map)this.fkField2pkTable2symFkColumn2pkColumn.get(var2);
         var4 = var9.keySet().iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            debug("    Dest PK Table: " + var5);
            Map var6 = (Map)var9.get(var5);
            Iterator var7 = var6.keySet().iterator();

            while(var7.hasNext()) {
               String var8 = (String)var7.next();
               debug("      FK Column: " + var8 + "   PK Column: " + var6.get(var8));
            }
         }
      }

      var1 = this.fieldName2columnName.keySet().iterator();
      debug("CMP Field Name To Column Mapping------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2 + ", \t" + (String)this.fieldName2columnName.get(var2));
      }

      var1 = this.fieldName2columnTypeName.keySet().iterator();
      debug("CMP Field Name To ColumnType Mapping------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2 + ", \t" + (String)this.fieldName2columnTypeName.get(var2));
      }

      var1 = this.cmpFieldName2groupName.keySet().iterator();
      debug("CMP Field Name To GroupName Mapping------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2 + ", \t" + (String)this.cmpFieldName2groupName.get(var2));
      }

      var1 = this.fieldName2tableName.keySet().iterator();
      debug("CMR Field Name To Join Table Name Mapping------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2 + ", \t" + (String)this.fieldName2tableName.get(var2));
      }

      var1 = this.remoteFieldNames.iterator();
      debug("RemoteField Names--------------------------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(var2);
      }

      var1 = this.remoteFieldNames.iterator();
      debug("RemoteField Names To Remote Column----------------------");

      String var11;
      while(var1.hasNext()) {
         var2 = (String)var1.next();
         var11 = (String)this.fieldName2remoteColumn.get(var2);
         if (null != var11) {
            debug(var2 + ", \t" + var11);
         }
      }

      var1 = this.fieldName2entityRef.keySet().iterator();
      debug("Field Name To Remote Name--------------------------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         EjbEntityRef var12 = (EjbEntityRef)this.fieldName2entityRef.get(var2);
         debug(var2 + ", " + var12.getRemoteEjbName());
      }

      var1 = this.fieldName2groupName.keySet().iterator();
      debug("Field Name To Group Name--------------------------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         var11 = (String)this.fieldName2groupName.get(var2);
         debug(var2 + ", " + var11);
      }

      var1 = this.fieldName2cascadeDelete.keySet().iterator();
      debug("Field Name To Cascade Delete--------------------------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         Boolean var13 = (Boolean)this.fieldName2cascadeDelete.get(var2);
         debug(var2 + ", " + var13.toString());
      }

      debug("*****************************************************");
   }

   public void printCmpFieldDebugInfo() {
      debug("*****************************************************");
      debug("      CMP FIELD information ");
      debug("*****************************************************");
      debug("ejbName- " + this.getEjbName());
      Iterator var1 = this.tableName2cmpFieldName2columnName.keySet().iterator();
      debug("Table Name To cmp-field To DBMS Column Name  ALL--------------");

      String var2;
      Map var3;
      Iterator var4;
      String var5;
      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(" Table Name: " + var2);
         var3 = (Map)this.tableName2cmpFieldName2columnName.get(var2);
         var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            debug("      " + var5 + ", " + var3.get(var5));
         }
      }

      var1 = this.table2cmpf2columnPKsOnly.keySet().iterator();
      debug("Table Name To cmp-field To DBMS Column Name  PKs only --------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(" Table Name: " + var2);
         var3 = (Map)this.table2cmpf2columnPKsOnly.get(var2);
         var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            debug("      " + var5 + ", " + var3.get(var5));
         }
      }

      var1 = this.table2cmpf2columnNoPKs.keySet().iterator();
      debug("Table Name To cmp-field To DBMS Column Name  No PKs ----------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(" Table Name: " + var2);
         var3 = (Map)this.table2cmpf2columnNoPKs.get(var2);
         var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            debug("      " + var5 + ", " + var3.get(var5));
         }
      }

      var1 = this.cmpf2Table.keySet().iterator();
      debug("cmp-field To Table--------------------------------------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug("      " + var2 + ", " + this.cmpf2Table.get(var2));
      }

      var1 = this.pkCmpF2Table2Column.keySet().iterator();
      debug("PK cmp-field To Table Name To DBMS Column Name----------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(" PK cmp-field: " + var2);
         var3 = (Map)this.pkCmpF2Table2Column.get(var2);
         var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            debug("      " + var5 + ", " + var3.get(var5));
         }
      }

      var1 = this.column2tables.keySet().iterator();
      debug("DBMS Column To Tables Containing the Column-------------------");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(" DBMS Column: " + var2);
         List var9 = (List)this.column2tables.get(var2);
         var4 = var9.iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            debug("      " + var5);
         }
      }

      var1 = this.cmrf2pkTable2fkColumn2pkColumn.keySet().iterator();
      debug("CMR Field To PKTable (or JoinTable) To FKColumn to PKColumn----");

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         debug(" CMR Field: " + var2);
         var3 = (Map)this.cmrf2pkTable2fkColumn2pkColumn.get(var2);
         var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            debug("    Dest PK Table: " + var5);
            Map var6 = (Map)var3.get(var5);
            Iterator var7 = var6.keySet().iterator();

            while(var7.hasNext()) {
               String var8 = (String)var7.next();
               debug("      FK Column: " + var8 + "   PK Column: " + var6.get(var8));
            }
         }
      }

      debug("*****************************************************\n");
   }

   public void addTableFieldColumnMapping(String var1, String var2, String var3) {
      this.fieldsLoadedViaMultiTable = true;
      if (debugLogger.isDebugEnabled()) {
         debug(" adding TableFieldColumn Mapping for table: '" + var1 + "', field: '" + var2 + "', column: '" + var3 + "'");
      }

      Object var4 = (Map)this.tableName2cmpFieldName2columnName.get(var1);
      if (var4 == null) {
         var4 = new HashMap();
         this.tableName2cmpFieldName2columnName.put(var1, var4);
      }

      ((Map)var4).put(var2, var3);
      if (debugLogger.isDebugEnabled()) {
         debug(" added TableFieldColumn Mapping for table: '" + var1 + "' done.");
      }

      var4 = (Map)this.table2column2cmpf.get(var1);
      if (var4 == null) {
         var4 = new HashMap();
         this.table2column2cmpf.put(var1, var4);
      }

      ((Map)var4).put(var3, var2);
      this.addReverseColumnMapping(var1, var3, var2);
      Object var5 = (List)this.column2tables.get(var3);
      if (var5 == null) {
         var5 = new ArrayList();
         this.column2tables.put(var3, var5);
      }

      ((List)var5).add(var1);
      this.cmpf2Table.put(var2, var1);
      if (!this.cmpFieldNames.contains(var2)) {
         this.cmpFieldNames.add(var2);
      }

      this.columnName2fieldName.put(var3, var2);
      this.fieldName2columnName.put(var2, var3);
      this.cmpColumnNames.add(var3);
   }

   public void setOptimisticColumn(String var1, String var2) {
      this.tableName2optimisticColumn.put(var1, var2);
   }

   public void setTriggerUpdatesOptimisticColumn(String var1, boolean var2) {
      this.tableName2optimisticColumnTrigger.put(var1, new Boolean(var2));
   }

   public boolean getTriggerUpdatesOptimisticColumn(String var1) {
      Boolean var2 = (Boolean)this.tableName2optimisticColumnTrigger.get(var1);
      return var2;
   }

   public void setVersionColumnInitialValue(String var1, int var2) {
      this.tableName2versionColumnInitialValue.put(var1, new Integer(var2));
   }

   public int getVersionColumnInitialValue(String var1) {
      Integer var2 = (Integer)this.tableName2versionColumnInitialValue.get(var1);
      return var2;
   }

   public void addFieldColumnTypeMapping(String var1, String var2) {
      if (var2.equals("Blob")) {
         this.hasBlobColumn = true;
      } else if (var2.equals("Clob")) {
         this.hasClobColumn = true;
      } else if (var2.equals("NClob")) {
         this.hasClobColumn = true;
         this.hasNClobColumn = true;
      }

      if (this.hasBlobColumn || this.hasClobColumn) {
         this.hasBlobClobColumn = true;
      }

      this.fieldName2columnTypeName.put(var1, var2);
   }

   public void addDbmsDefaultValueField(String var1) {
      if (this.dbmsDefaultValueFields == null) {
         this.dbmsDefaultValueFields = new HashSet();
      }

      this.dbmsDefaultValueFields.add(var1);
   }

   public boolean isDbmsDefaultValueField(String var1) {
      if (this.dbmsDefaultValueFields == null) {
         return false;
      } else {
         return this.dbmsDefaultValueFields.contains(var1) && !this.isPrimaryKeyField(var1);
      }
   }

   public boolean allowReadonlyCreateAndRemove() {
      return this.allowReadonlyCreateAndRemove;
   }

   public void setAllowReadonlyCreateAndRemove(boolean var1) {
      this.allowReadonlyCreateAndRemove = var1;
   }

   public void setByteArrayIsSerializedToOracleBlob(boolean var1) {
      this.byteArrayIsSerializedToOracleBlob = var1;
   }

   public boolean getByteArrayIsSerializedToOracleBlob() {
      return this.byteArrayIsSerializedToOracleBlob;
   }

   public void setLoadRelatedBeansFromDbInPostCreate(boolean var1) {
      this.loadRelatedBeansFromDbInPostCreate = var1;
   }

   public boolean getLoadRelatedBeansFromDbInPostCreate() {
      return this.loadRelatedBeansFromDbInPostCreate;
   }

   public boolean hasBlobClobColumn() {
      return this.hasBlobClobColumn;
   }

   public boolean hasBlobColumn() {
      return this.hasBlobColumn;
   }

   public boolean hasClobColumn() {
      return this.hasClobColumn;
   }

   public boolean hasNClobColumn() {
      return this.hasNClobColumn;
   }

   public String getTableForCmpField(String var1) {
      return (String)this.cmpf2Table.get(var1);
   }

   public int getTableIndexForCmpField(String var1) {
      String var2 = this.getTableForCmpField(var1);
      return var2 == null ? -1 : this.tableIndex(var2);
   }

   public Map getCmpField2ColumnMap(String var1) {
      return (Map)this.tableName2cmpFieldName2columnName.get(var1);
   }

   public Map getTableName2CmpField2ColumnMap() {
      return this.tableName2cmpFieldName2columnName;
   }

   public String getColumnForCmpFieldAndTable(String var1, String var2) {
      Map var3 = (Map)this.tableName2cmpFieldName2columnName.get(var2);
      return var3 == null ? null : (String)var3.get(var1);
   }

   public String getCmpColumnForField(String var1) {
      return (String)this.fieldName2columnName.get(var1);
   }

   public String getCmpFieldForColumn(String var1) {
      return (String)this.columnName2fieldName.get(var1);
   }

   public String getPkCmpFieldForColumn(String var1) {
      Iterator var2 = this.table2cmpf2columnPKsOnly.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Map var4 = (Map)this.table2cmpf2columnPKsOnly.get(var3);
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            String var7 = (String)var4.get(var6);
            if (var7.equals(var1)) {
               return var6;
            }
         }
      }

      return null;
   }

   public boolean hasCmpField(String var1, String var2) {
      return this.getCmpField(var1, var2) != null;
   }

   public String getCmpField(String var1, String var2) {
      Map var3 = (Map)this.table2column2cmpf.get(var1);
      return var3 == null ? null : (String)var3.get(var2);
   }

   public String getVariable(String var1, String var2) {
      Map var3 = (Map)this.table2column2variable.get(var1);
      return var3 == null ? null : (String)var3.get(var2);
   }

   public String getCmpColumnTypeForField(String var1) {
      return (String)this.fieldName2columnTypeName.get(var1);
   }

   public boolean isBlobCmpColumnTypeForField(String var1) {
      return "Blob".equalsIgnoreCase(this.getCmpColumnTypeForField(var1));
   }

   public boolean isClobCmpColumnTypeForField(String var1) {
      return "Clob".equalsIgnoreCase(this.getCmpColumnTypeForField(var1)) || this.isNClobCmpColumnTypeForField(var1);
   }

   public boolean isNClobCmpColumnTypeForField(String var1) {
      return "NClob".equalsIgnoreCase(this.getCmpColumnTypeForField(var1));
   }

   public boolean isBlobCmpColumnTypeForColumn(String var1) {
      String var2 = (String)this.columnName2fieldName.get(var1);
      return this.isBlobCmpColumnTypeForField(var2);
   }

   public boolean isClobCmpColumnTypeForColumn(String var1) {
      String var2 = (String)this.columnName2fieldName.get(var1);
      return this.isClobCmpColumnTypeForField(var2);
   }

   public Map getCmpColumnToFieldMap() {
      return this.columnName2fieldName;
   }

   public Map getCmpFieldToColumnMap() {
      return this.fieldName2columnName;
   }

   public Map getCmpFieldToColumnTypeMap() {
      return this.fieldName2columnTypeName;
   }

   public Class getCmpFieldClass(String var1) {
      assert this.cmpFieldClasses.get(var1) != null;

      return (Class)this.cmpFieldClasses.get(var1);
   }

   public List getCmpFieldNames() {
      return this.cmpFieldNames;
   }

   public List getCmpColumnNames() {
      return this.cmpColumnNames;
   }

   public boolean isCmpFieldName(String var1) {
      return this.cmpFieldNames.contains(var1);
   }

   public boolean hasCmpField(String var1) {
      return this.fieldName2columnName.get(var1) != null;
   }

   public boolean hasCmpColumnType(String var1) {
      return this.fieldName2columnTypeName.get(var1) != null;
   }

   public boolean hasPkColumn(String var1) {
      return this.getPkCmpFieldForColumn(var1) != null;
   }

   public boolean isCmrMappedCmpField(String var1) {
      return this.cmrMappedcmpFields.contains(var1);
   }

   public String getTableForVariable(String var1) {
      return (String)this.variableName2table.get(var1);
   }

   public String getCmpColumnForVariable(String var1) {
      return (String)this.variableName2columnName.get(var1);
   }

   public boolean isJoinTable(String var1) {
      return this.fieldName2tableName.containsValue(var1);
   }

   public String getCmrFieldForJoinTable(String var1) {
      Iterator var2 = this.fieldName2tableName.keySet().iterator();

      String var3;
      String var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (String)var2.next();
         var4 = (String)this.fieldName2tableName.get(var3);
      } while(!var4.equals(var1));

      return var3;
   }

   public Map getJoinTableMap() {
      return this.fieldName2tableName;
   }

   public Map getFkColumn2ClassMapForFkField(String var1) {
      return (Map)this.fkField2fkColumn2Class.get(var1);
   }

   public void setOrderDatabaseOperations(boolean var1) {
      this.orderDatabaseOperations = var1;
   }

   public boolean getOrderDatabaseOperations() {
      return this.orderDatabaseOperations;
   }

   public void setEnableBatchOperations(boolean var1) {
      this.enableBatchOperations = var1;
      if (this.getDelayInsertUntil().equals("commit")) {
         this.enableBatchOperations = true;
      }

      if (this.enableBatchOperations) {
         this.orderDatabaseOperations = true;
      }

      if (this.hasAutoKeyGeneration() && this.genKeyType == 1) {
         this.enableBatchOperations = false;
         this.orderDatabaseOperations = false;
      }

   }

   public boolean getEnableBatchOperations() {
      return this.enableBatchOperations;
   }

   public String getValidateDbSchemaWith() {
      return this.validateDbSchemaWith;
   }

   public void setValidateDbSchemaWith(String var1) {
      this.validateDbSchemaWith = var1;
   }

   public String getCreateDefaultDBMSTables() {
      return this.createDefaultDBMSTables;
   }

   public void setCreateDefaultDBMSTables(String var1) {
      this.createDefaultDBMSTables = var1;
   }

   public String getDefaultDbmsTablesDdl() {
      return this.ddlFileName;
   }

   public void setDefaultDbmsTablesDdl(String var1) {
      this.ddlFileName = var1;
   }

   public int getDatabaseType() {
      return this.databaseType;
   }

   public void setDatabaseType(int var1) {
      this.databaseType = var1;
   }

   public void setCheckExistsOnMethod(boolean var1) {
      this.checkExistsOnMethod = var1;
   }

   public boolean getCheckExistsOnMethod() {
      return this.checkExistsOnMethod;
   }

   public void setClusterInvalidationDisabled(boolean var1) {
      this.clusterInvalidationDisabled = var1;
   }

   public boolean isClusterInvalidationDisabled() {
      return this.clusterInvalidationDisabled;
   }

   public void setDelayInsertUntil(String var1) {
      this.delayInsertUntil = var1;
   }

   public String getDelayInsertUntil() {
      return this.delayInsertUntil;
   }

   public void setUseSelectForUpdate(boolean var1) {
      this.useSelectForUpdate = var1;
   }

   public boolean getUseSelectForUpdate() {
      return this.useSelectForUpdate;
   }

   public void setLockOrder(int var1) {
      this.lockOrder = var1;
   }

   public int getLockOrder() {
      return this.lockOrder;
   }

   public void setInstanceLockOrder(String var1) {
      this.instanceLockOrder = var1;
   }

   public String getInstanceLockOrder() {
      return this.instanceLockOrder;
   }

   public int getFieldCount() {
      return this.numFields;
   }

   public Integer getIsModifiedIndex(String var1) {
      return (Integer)this.fieldNameToIsModifiedIndex.get(var1);
   }

   public Map getFieldNameToIsModifiedIndex() {
      return this.fieldNameToIsModifiedIndex;
   }

   public String getFieldName(Integer var1) {
      return (String)this.isModifiedIndexToFieldName.get(var1);
   }

   public List getNonPKFields(int var1) {
      return this.tableIndexToNonPKFieldList[var1];
   }

   public List getFields(int var1) {
      return this.tableIndexToFieldList[var1];
   }

   public List getCMPFields(int var1) {
      return this.tableIndexToCMPFieldList[var1];
   }

   public List getCMRFields(int var1) {
      return this.tableIndexToCMRFieldList[var1];
   }

   public List getIsModifiedIndices_PK() {
      return this.isModifiedPKIndexList;
   }

   public Integer getTableNumber(int var1) {
      return (Integer)this.isModifiedIndexToTableNumber.get(var1);
   }

   public List getIsModifiedIndexToTableNumber() {
      return this.isModifiedIndexToTableNumber;
   }

   public void addFinder(Finder var1) {
      this.finderList.add(var1);
      if (var1.isResultSetFinder()) {
         this.hasResultSetFinder = true;
      }

      if (var1.isSqlFinder()) {
         this.hasSqlFinder = true;
      }

   }

   public Finder createFinder(String var1, String[] var2, String var3) throws InvalidFinderException {
      RDBMSFinder var5 = (RDBMSFinder)this.getRdbmsFinders().get(new RDBMSFinder.FinderKey(var1, var2));
      if (debugLogger.isDebugEnabled()) {
         debug("******************** ejb- " + this.getEjbName() + ", finder - " + var1);
      }

      Object var4;
      if (var5 != null) {
         if (var5.getSqlQueries() != null) {
            if (debugLogger.isDebugEnabled()) {
               debug("sql ******************** ejb- " + this.getEjbName() + ", finder - " + var1);
            }

            SqlFinder var6 = new SqlFinder(var1, var5.getSqlQueries(), var5.getSqlShapeName(), this);
            var4 = var6;
         } else {
            EjbqlFinder var7 = new EjbqlFinder(var1, var3);
            if (var5.getEjbQlQuery() != null) {
               if (debugLogger.isDebugEnabled()) {
                  debug("wl ql ******************** ejb- " + this.getEjbName() + ", finder - " + var1);
               }

               var7.setEjbQuery(var5.getEjbQlQuery());
            }

            var7.setGroupName(var5.getGroupName());
            var7.setCachingName(var5.getCachingName());
            var7.setSqlSelectDistinct(var5.getSqlSelectDistinct());
            var7.setIncludeResultCacheHint(var5.isIncludeResultCacheHint());
            var4 = var7;
         }

         ((Finder)var4).setMaxElements(var5.getMaxElements());
         ((Finder)var4).setIncludeUpdates(var5.getIncludeUpdates());
      } else {
         var4 = new EjbqlFinder(var1, var3);
      }

      ((Finder)var4).setRDBMSBean(this);
      return (Finder)var4;
   }

   public void perhapsSetQueryCachingEnabled(Finder var1) {
      RDBMSFinder var2 = (RDBMSFinder)this.getRdbmsFinders().get(new RDBMSFinder.FinderKey(var1));
      if (var2 != null) {
         var1.setQueryCachingEnabled(var2, this);
      } else {
         var1.setQueryCachingEnabled(false);
      }

   }

   public Iterator getFinders() {
      return this.finderList.iterator();
   }

   public List getFinderList() {
      return this.finderList;
   }

   public void setFinderList(List var1) {
      this.finderList = var1;
   }

   public boolean hasResultSetFinder() {
      return this.hasResultSetFinder;
   }

   public void generateFinderSQLStatements(Iterator var1) throws ErrorCollectionException {
      EJBQLParsingException var2 = new EJBQLParsingException();

      while(var1.hasNext()) {
         Finder var3 = (Finder)var1.next();
         if (var3 instanceof EjbqlFinder) {
            EjbqlFinder var4 = (EjbqlFinder)var3;

            try {
               var4.computeSQLQuery(this);
               if (debugLogger.isDebugEnabled()) {
                  String var5 = var3.getSQLQuery();
                  if (var5 == null) {
                     debug("finder.computSQLQuery: None generated.  NULL !");
                  } else {
                     debug("finder.computSQLQuery: " + var5);
                  }
               }
            } catch (EJBQLCompilerException var6) {
               var2.add(var6);
            }
         }
      }

      if (var2.getExceptions().size() > 0) {
         throw var2;
      }
   }

   public List getCmrFieldNames() {
      return this.cmrFieldNames;
   }

   public List getDeclaredFieldNames() {
      return this.declaredFieldNames;
   }

   public List getForeignKeyFieldNames() {
      return this.fkFieldNames;
   }

   public List getForeignPrimaryKeyFieldNames() {
      return this.fkPkFieldNames;
   }

   public List getRemoteFieldNames() {
      return this.remoteFieldNames;
   }

   public boolean isRemoteField(String var1) {
      return this.remoteFieldNames.contains(var1);
   }

   public EjbEntityRef getEjbEntityRef(String var1) {
      return (EjbEntityRef)this.fieldName2entityRef.get(var1);
   }

   public Class getForeignKeyColClass(String var1, String var2) {
      assert this.fkField2fkColumn2Class.get(var1) != null;

      assert ((Map)this.fkField2fkColumn2Class.get(var1)).get(var2) != null;

      return (Class)((Map)this.fkField2fkColumn2Class.get(var1)).get(var2);
   }

   public List getForeignKeyColNames(String var1) {
      assert this.fkField2fkColumns.get(var1) != null;

      return (List)this.fkField2fkColumns.get(var1);
   }

   public Class getJavaClassTypeForFkCol(String var1, String var2) {
      Map var3 = (Map)this.table2fkCol2fkClass.get(var1);
      return var3 == null ? null : (Class)var3.get(var2);
   }

   public boolean hasCmrField(String var1, String var2) {
      return this.getJavaClassTypeForFkCol(var1, var2) != null;
   }

   public RDBMSBean getRelatedBean(String var1, String var2) {
      Map var3 = (Map)this.table2fkCol2RelatedBean.get(var1);
      return var3 == null ? null : (RDBMSBean)var3.get(var2);
   }

   public List getSymmetricKeyColNames(String var1) {
      assert this.fkField2fkColumns.get(var1) != null;

      assert this.fkField2symColumns.get(var1) != null;

      return (List)this.fkField2symColumns.get(var1);
   }

   public Map getSymmetricColumn2FieldName(String var1) {
      return (Map)this.fkField2symColumn2FieldName.get(var1);
   }

   public boolean isSymmetricField(String var1) {
      return this.fkField2symColumns.get(var1) != null;
   }

   public Class getCmrFieldClass(String var1) {
      return (Class)this.fieldName2class.get(var1);
   }

   public boolean isDeclaredField(String var1) {
      return this.declaredFieldNames.contains(var1);
   }

   public boolean isForeignKeyField(String var1) {
      return this.fkFieldNames.contains(var1);
   }

   public boolean isForeignPrimaryKeyField(String var1) {
      return this.fkPkFieldNames.contains(var1);
   }

   public boolean isForeignCmpField(String var1) {
      return this.fkCmpFieldNames.contains(var1);
   }

   public boolean isOneToOneRelation(String var1) {
      return this.one2one.contains(var1);
   }

   public boolean isOneToManyRelation(String var1) {
      return this.one2many.contains(var1);
   }

   public boolean isManyToManyRelation(String var1) {
      return this.many2many.contains(var1);
   }

   public boolean isBiDirectional(String var1) {
      return this.biDirectional.contains(var1);
   }

   public boolean isCascadeDelete(String var1) {
      return (Boolean)this.fieldName2cascadeDelete.get(var1);
   }

   public boolean isCascadeDelete() {
      Iterator var1 = this.getCmrFieldNames().iterator();

      String var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (String)var1.next();
      } while(!this.isCascadeDelete(var2));

      return true;
   }

   public boolean isDBCascadeDelete(String var1) {
      return (Boolean)this.fieldName2DBCascadeDelete.get(var1);
   }

   public String getRemoteColumn(String var1) {
      assert this.fieldName2remoteColumn.get(var1) != null;

      return (String)this.fieldName2remoteColumn.get(var1);
   }

   public String getRelatedFieldName(String var1) {
      return (String)this.fieldName2RelatedFieldName.get(var1);
   }

   public String getRelatedPkFieldName(String var1, String var2) {
      assert this.isForeignKeyField(var1);

      assert this.fkField2fkColumn2FieldName.get(var1) != null;

      assert ((Map)this.fkField2fkColumn2FieldName.get(var1)).get(var2) != null;

      return (String)((Map)this.fkField2fkColumn2FieldName.get(var1)).get(var2);
   }

   public CMPBeanDescriptor getRelatedDescriptor(String var1) {
      assert this.fieldName2relatedDescriptor.get(var1) != null;

      return (CMPBeanDescriptor)this.fieldName2relatedDescriptor.get(var1);
   }

   public RDBMSBean getRelatedRDBMSBean(String var1) {
      assert this.fieldName2relatedRDBMSBean.get(var1) != null;

      return (RDBMSBean)this.fieldName2relatedRDBMSBean.get(var1);
   }

   public Set getAllCmrFields() {
      return this.fieldName2relatedRDBMSBean.keySet();
   }

   public String getRelatedMultiplicity(String var1) {
      return (String)this.fieldName2relatedMultiplicity.get(var1);
   }

   public boolean relatedFieldIsFkOwner(String var1) {
      assert this.fieldName2relatedFieldOwnsFk.get(var1) != null;

      return (Boolean)this.fieldName2relatedFieldOwnsFk.get(var1);
   }

   public String getRelatedBeanClassName(String var1) {
      assert this.fieldName2relatedClassName.get(var1) != null;

      return (String)this.fieldName2relatedClassName.get(var1);
   }

   public String getJoinTableName(String var1) {
      assert this.fieldName2tableName.get(var1) != null;

      return (String)this.fieldName2tableName.get(var1);
   }

   public String getGroupName(String var1) {
      return (String)this.fieldName2groupName.get(var1);
   }

   public Set getTableNamesForGroup(String var1) {
      return (Set)this.groupName2tableNames.get(var1);
   }

   public boolean containsFkField(String var1) {
      assert this.isForeignKeyField(var1);

      boolean var2 = this.isOneToOneRelation(var1);
      boolean var3 = this.isOneToManyRelation(var1) && this.getRelatedMultiplicity(var1).equals("One");
      return var2 || var3;
   }

   public String findByPrimaryKeyQuery() {
      StringBuffer var1 = new StringBuffer();
      var1.append("SELECT OBJECT(bean) ");
      var1.append("FROM ").append(this.getAbstractSchemaName());
      var1.append(" AS bean ");
      var1.append("WHERE ");
      int var2 = 1;
      Iterator var3 = this.bd.getPrimaryKeyFieldNames().iterator();

      assert var3.hasNext();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();

         assert var4 != null;

         String var5 = " ( bean." + var4 + " = ?" + var2++ + " ) ";
         var1.append(var5);
         if (var3.hasNext()) {
            var1.append(" AND ");
         }
      }

      return var1.toString();
   }

   private String findByForeignKeyQuery(String var1) {
      StringBuffer var2 = new StringBuffer();
      TreeSet var3 = new TreeSet();
      HashMap var4 = new HashMap();
      Iterator var5 = this.getForeignKeyColNames(var1).iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         String var7 = this.getRelatedPkFieldName(var1, var6);
         var3.add(var7);
         var4.put(var7, var6);
      }

      var2.append("SELECT OBJECT(bean) ");
      var2.append("FROM ").append(this.getAbstractSchemaName());
      var2.append(" AS bean ");
      var2.append("WHERE (");
      int var10 = 1;
      Iterator var11 = var3.iterator();

      while(var11.hasNext()) {
         String var8 = (String)var11.next();
         String var9 = (String)var4.get(var8);
         if (debugLogger.isDebugEnabled()) {
            debug("processing foreign key column: " + var9);
         }

         var2.append("bean.");
         var2.append(this.variableForField(var1, this.getTableForCmrField(var1), var9) + " = ?" + var10++);
         if (var11.hasNext()) {
            var2.append(" AND ");
         }
      }

      var2.append(")");
      if (debugLogger.isDebugEnabled()) {
         debug("relation Finder query: " + var2);
      }

      return var2.toString();
   }

   public void createRelationFinders() {
      if (debugLogger.isDebugEnabled()) {
         debug("createRelationFinders() called...");
      }

      this.relFinders = new ArrayList();
      Iterator var1 = this.getCmrFieldNames().iterator();
      if (debugLogger.isDebugEnabled()) {
         debug(var1.hasNext() ? "have a field" : "no fields");
      }

      while(true) {
         String var2;
         do {
            do {
               if (!var1.hasNext()) {
                  return;
               }

               var2 = (String)var1.next();
               if (debugLogger.isDebugEnabled()) {
                  debug("processing field: " + var2);
               }
            } while(this.isRemoteField(var2));
         } while(!this.isOneToOneRelation(var2) && !this.isOneToManyRelation(var2));

         String var3 = null;
         if (this.relatedFieldIsFkOwner(var2)) {
            var3 = this.findByPrimaryKeyQuery();
         } else {
            var3 = this.findByForeignKeyQuery(var2);
         }

         EjbqlFinder var4 = null;
         RDBMSBean var5 = this.getRelatedRDBMSBean(var2);
         CMPBeanDescriptor var6 = this.getRelatedDescriptor(var2);
         String var7 = this.getRelatedFieldName(var2);

         try {
            var4 = new EjbqlFinder(this.finderMethodName(var6, var7), var3);
            var4.setIncludeUpdates(false);
            var4.parseExpression();
         } catch (Exception var9) {
            throw new AssertionError(StackTraceUtils.throwable2StackTrace(var9));
         }

         var4.setModifierString("public ");
         Class var8 = var5.getCmrFieldClass(var7);

         assert var8 != null;

         if (Collection.class.isAssignableFrom(var8)) {
            var4.setReturnClassType(Collection.class);
         } else {
            var4.setReturnClassType(var8);
         }

         var4.setExceptionClassTypes(new Class[]{Exception.class});
         var4.setKeyFinder(true);
         if (this.relatedFieldIsFkOwner(var2)) {
            var4.setParameterClassTypes(new Class[]{this.bd.getPrimaryKeyClass()});
            var4.setKeyBean(this);
         } else {
            var4.setParameterClassTypes(new Class[]{var6.getPrimaryKeyClass()});
            var4.setKeyBean(var5);
         }

         var4.setFinderLoadsBean(this.bd.getFindersLoadBean());
         var4.setRDBMSBean(this);
         var4.setGroupName(this.getGroupName(var2));
         var4.setIsGeneratedRelationFinder(true);
         this.relFinders.add(var4);
         if (this.isCascadeDelete(var2)) {
            var4.setCachingName(this.getRelationshipCachingForField(var2));
         }

         this.cmrMapeedRelationFinder.put(var2, var4);
      }
   }

   public Finder getRelatedFinder(String var1) {
      return (Finder)this.cmrMapeedRelationFinder.get(var1);
   }

   public String getRelationshipCachingForField(String var1) {
      Iterator var2 = this.getRelationshipCachings().iterator();

      while(var2.hasNext()) {
         RelationshipCaching var3 = (RelationshipCaching)var2.next();
         Iterator var4 = var3.getCachingElements().iterator();

         while(var4.hasNext()) {
            RelationshipCaching.CachingElement var5 = (RelationshipCaching.CachingElement)var4.next();
            if (var5.getCmrField() != null && var5.getCmrField().equals(var1)) {
               return var3.getCachingName();
            }
         }
      }

      return null;
   }

   public void createRelationFinders2() {
      if (debugLogger.isDebugEnabled()) {
         debug("createRelationFinders2() called...");
      }

      Iterator var1 = this.getForeignKeyFieldNames().iterator();
      if (debugLogger.isDebugEnabled()) {
         debug(var1.hasNext() ? "have a foreign key" : "no foreign keys");
      }

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         if (debugLogger.isDebugEnabled()) {
            debug("processing foreign key field: " + var2);
         }

         if (this.isManyToManyRelation(var2) && !this.isRemoteField(var2)) {
            this.getRelatedRDBMSBean(var2);
            this.getRelatedFieldName(var2);
            StringBuffer var5 = new StringBuffer();
            var5.append("SELECT OBJECT(bean." + var2 + ") ");
            var5.append("FROM ").append(this.getAbstractSchemaName());
            var5.append(" AS bean ");
            String var6 = var5.toString();
            if (debugLogger.isDebugEnabled()) {
               debug("relation Finder query: " + var6);
            }

            EjbqlFinder var7 = null;

            try {
               String var8 = this.finderMethodName(this.bd, var2);
               var7 = new EjbqlFinder(var8, var6);
               var7.setIncludeUpdates(false);
               var7.parseExpression();
            } catch (Exception var9) {
               throw new AssertionError(StackTraceUtils.throwable2StackTrace(var9));
            }

            var7.setParameterClassTypes(new Class[0]);
            var7.setModifierString("public ");
            var7.setReturnClassType(Collection.class);
            var7.setExceptionClassTypes(new Class[]{Exception.class});
            var7.setIsSelectInEntity(true);
            var7.setQueryType(4);
            var7.setFinderLoadsBean(this.bd.getFindersLoadBean());
            var7.setRDBMSBean(this);
            var7.setIsGeneratedRelationFinder(true);
            this.relFinders.add(var7);
         }
      }

   }

   public List getRelationFinderList() {
      if (this.relFinders == null) {
         this.relFinders = new ArrayList();
      }

      return this.relFinders;
   }

   public Iterator getRelationFinders() {
      return this.getRelationFinderList().iterator();
   }

   public void cleanup() {
      this.dataSourceName = null;
      this.finderList = null;
      this.cmpFieldNames = null;
      this.cmpFieldClasses = null;
      this.cmpColumnNames = null;
      this.cmpFieldName2groupName = null;
      this.fieldGroups = null;
      this.relationshipCachings = null;
      this.rdbmsFinders = null;
      this.dbmsDefaultValueFields = null;
      this.tableName2verifyRows = null;
      this.tableName2optimisticColumnTrigger = null;
      this.tableName2versionColumnInitialValue = null;
      this.table2cmpf2columnNoPKs = null;
      this.cmpf2Table = null;
      this.pkCmpF2Table2Column = null;
      this.column2tables = null;
      this.cmrf2table = null;
      this.cmrfHasMultiPkTable = null;
      this.cmrf2pkTable2fkColumn2pkColumn = null;
      this.fkField2pkTable2symFkColumn2pkColumn = null;
      this.variableName2table = null;
      this.fkField2fkColumn2Class = null;
      this.one2one = null;
      this.one2many = null;
      this.many2many = null;
      this.biDirectional = null;
      this.fieldName2cascadeDelete = null;
      this.fieldName2DBCascadeDelete = null;
      this.fieldName2selfRel = null;
      this.fieldName2RelatedFieldName = null;
      this.fkField2fkColumn2FieldName = null;
      this.fkField2symColumn2FieldName = null;
      this.fieldName2relatedDescriptor = null;
      this.fieldName2relatedMultiplicity = null;
      this.fieldName2relatedFieldOwnsFk = null;
      this.fieldName2relatedClassName = null;
      this.fieldName2groupName = null;
      this.variableName2columnName = null;
      this.cmrFieldNames = null;
      this.declaredFieldNames = null;
      this.fkFieldNames = null;
      this.fkPkFieldNames = null;
      this.fkCmpFieldNames = null;
      this.fieldName2class = null;
      this.fkField2symColumns = null;
      this.fieldName2relatedRDBMSBean = null;
      this.fieldName2tableName = null;
      this.groupName2tableNames = null;
      this.fieldName2entityRef = null;
      this.remoteFieldNames = null;
      this.fieldName2remoteColumn = null;
      this.cmrMappedcmpFields = null;
      if (!this.hasSqlFinder) {
         this.table2cmrf = null;
         this.fkField2fkColumns = null;
         this.columnName2fieldName = null;
         this.tableName2cmpFieldName2columnName = null;
      }

   }

   public void setupAutoKeyGen() throws RDBMSException {
      if (this.hasAutoKeyGeneration()) {
         this.setGenKeyPKField();
         if (debugLogger.isDebugEnabled()) {
            debug(" AutoKey Generation is ON");
         }

         Loggable var1;
         switch (this.getGenKeyType()) {
            case 1:
               this.genKeyBeforeInsert = false;
               this.genKeyExcludePKColumn = true;
               this.genKeyDefaultColumnVal = null;
               if (this.databaseType == 2) {
                  this.genKeyWLGeneratorQuery = "SELECT @@IDENTITY";
               } else if (this.databaseType == 7) {
                  this.genKeyWLGeneratorQuery = "SELECT SCOPE_IDENTITY()";
               }

               if (debugLogger.isDebugEnabled()) {
                  debug(" Generated Key Query: " + this.genKeyWLGeneratorQuery);
               }

               return;
            case 2:
               if (this.genKeyGeneratorName == null) {
                  var1 = EJBLogger.logBadAutoKeyGeneratorNameLoggable("Sequence", "database Sequence");
                  throw new RDBMSException(var1.getMessage());
               }

               this.genKeyBeforeInsert = true;
               this.genKeyExcludePKColumn = false;
               this.genKeyDefaultColumnVal = null;
               if (debugLogger.isDebugEnabled()) {
                  debug(" Generated Key Query: for Oracle, this is deferred to deployment time");
               }

               return;
            case 3:
               this.genKeyBeforeInsert = true;
               this.genKeyExcludePKColumn = false;
               this.genKeyDefaultColumnVal = null;
               if (this.genKeyGeneratorName == null) {
                  var1 = EJBLogger.logBadAutoKeyGeneratorNameLoggable("SequenceTable", "database sequence table");
                  throw new RDBMSException(var1.getMessage());
               }

               this.perhapsSetGenKeyCacheDefault();
               this.genKeyWLGeneratorQuery = "SELECT SEQUENCE FROM " + this.genKeyGeneratorName;
               this.genKeyWLGeneratorUpdatePrefix = "UPDATE " + this.genKeyGeneratorName + " SET SEQUENCE = SEQUENCE + ";
               if (debugLogger.isDebugEnabled()) {
                  debug(" Generated Key Query:  " + this.genKeyWLGeneratorQuery);
               }

               if (debugLogger.isDebugEnabled()) {
                  debug(" Generated Key Update: " + this.genKeyWLGeneratorUpdatePrefix);
               }

               return;
            default:
               throw new AssertionError("Unknown auto-key generator for " + this.ejbName);
         }
      }
   }

   String getOracleSequenceGeneratorQuery(String var1) {
      return "SELECT " + var1 + ".nextval FROM DUAL";
   }

   String getInformixSequenceGeneratorQuery(String var1) {
      return "SELECT " + var1 + ".nextval FROM SYSTABLES WHERE tabid=1";
   }

   String getDB2SequenceGeneratorQuery(String var1) {
      return "SELECT NEXTVAL for " + var1 + " FROM SYSIBM.SYSDUMMY1";
   }

   private void setGenKeyPKField() throws RDBMSException {
      Iterator var1 = this.bd.getPrimaryKeyFieldNames().iterator();

      assert var1.hasNext();

      this.genKeyPKField = (String)var1.next();
      if (this.isForeignPrimaryKeyField(this.genKeyPKField)) {
         Loggable var3 = EJBLogger.logAutoKeyCannotBePartOfFKLoggable();
         throw new RDBMSException(var3.getMessage());
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug(" Gen Key PK Field: " + this.genKeyPKField);
         }

         Class var2 = this.getCmpFieldClass(this.genKeyPKField);
         if (!var2.equals(Integer.class) && !var2.equals(Integer.TYPE)) {
            if (!var2.equals(Long.class) && !var2.equals(Long.TYPE)) {
               throw new AssertionError("invalid pk class: " + var2.getName());
            }

            this.genKeyPKFieldClassType = 1;
         } else {
            this.genKeyPKFieldClassType = 0;
         }

      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof RDBMSBean)) {
         return false;
      } else {
         RDBMSBean var2 = (RDBMSBean)var1;
         if (!this.dataSourceName.equals(var2.getDataSourceName())) {
            return false;
         } else {
            return this.finderList.equals(var2.getFinderList());
         }
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(150);
      var1.append("[weblogic.cmp.rdbms.RDBMSBean {");
      var1.append("\n\tname = " + this.ejbName);
      var1.append("\n\tdata source = " + this.dataSourceName);
      var1.append("\n\ttableNames = ");
      Iterator var2 = this.getTables().iterator();

      while(var2.hasNext()) {
         var1.append((String)var2.next() + ", ");
      }

      var1.append("\n\tfinderList = " + this.finderList);
      var1.append("\n\tField Name to Column Name = ");
      if (this.fieldName2columnName != null) {
         var2 = this.fieldName2columnName.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append("\n\t\tfield- " + var3 + " column- " + this.fieldName2columnName.get(var3));
         }
      }

      var1.append("\n} end RDBMSBean ]\n");
      return var1.toString();
   }

   public int hashCode() {
      return this.dataSourceName.hashCode() ^ this.getTableName().hashCode();
   }

   private void perhapsSetGenKeyCacheDefault() {
      if (this.genKeyCacheSize <= 0) {
         this.genKeyCacheSize = 10;
      }

   }

   public static void deleteDefaultDbmsTableDdlFile(String var0) {
      try {
         File var1 = new File(var0);
         var1.delete();
      } catch (Exception var2) {
         EJBLogger.logUnableToDeleteDDLFile(var0);
      }

   }

   public void addTableDefToDDLFile() {
      try {
         Iterator var1 = this.getTables().iterator();
         StringBuffer var2 = new StringBuffer();

         String var3;
         while(var1.hasNext()) {
            var3 = (String)var1.next();
            var3 = RDBMSUtils.escQuotedID(var3);
            this.createDefaultDBMSTable(var3, var2);
         }

         var1 = this.getJoinTableMap().values().iterator();

         while(var1.hasNext()) {
            var3 = (String)var1.next();
            this.createDefaultDBMSTable(var3, var2);
         }

         this.writeToDDLFile(var2.toString());
      } catch (Exception var4) {
         EJBLogger.logUnableToWriteToDDLFile(this.ddlFileName);
      }

   }

   private void writeToDDLFile(String var1) {
      BufferedWriter var2 = null;

      try {
         var2 = new BufferedWriter(new FileWriter(this.ddlFileName, true));
         var2.write(var1 + "\n\n");
         var2.flush();
      } catch (Exception var13) {
         EJBLogger.logUnableToCreateDDLFile(var13);
      } finally {
         try {
            var2.close();
         } catch (Exception var12) {
         }

      }

   }

   private void createDefaultDBMSTable(String var1, StringBuffer var2) throws Exception {
      var2.append("\n DROP TABLE " + var1 + "\n");
      var2.append("\nCREATE TABLE " + var1 + " (");
      this.addBeanOrJoinTableColumns(var1, var2);
      var2.append(" )\n");
   }

   private void addSequenceTableColumns(String var1, StringBuffer var2) throws Exception {
      if (this.hasAutoKeyGeneration() && this.getGenKeyType() == 3) {
         String var3 = this.getGenKeyGeneratorName();
         if (var3 == null || var1 == null) {
            throw new RDBMSException(" in getSequenceTableColumns: either the SEQUENCE_TABLE name in the RDBMSBean or the passed in table Name  is NULL for bean: " + this.ejbName);
         }

         if (var3.equals(var1)) {
            var2.append("SEQUENCE ").append("DECIMAL");
         }
      }

   }

   private void addBeanOrJoinTableColumns(String var1, StringBuffer var2) throws Exception {
      HashSet var3 = new HashSet();
      if (this.isJoinTable(var1)) {
         this.addJoinTableColumns(var1, var2, var3);
      } else {
         this.addBeanTableColumns(var1, var2, var3);
      }

      if (var3.size() > 0) {
         var2.append(",");
         if (this.databaseType == 2 || this.databaseType == 7) {
            var2.append(" CONSTRAINT pk_" + var1);
         }

         var2.append(" PRIMARY KEY (");
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var2.append(var5);
            if (var4.hasNext()) {
               var2.append(", ");
            }
         }

         var2.append(")");
      }

   }

   private void addJoinTableColumns(String var1, StringBuffer var2, Set var3) throws Exception {
      if (debugLogger.isDebugEnabled()) {
         debug(" createDefaultDBMSTable: processing Join Table: " + var1);
      }

      String var4 = this.getCmrFieldForJoinTable(var1);
      if (null == var4) {
         throw new RDBMSException(" Bean: " + this.ejbName + ", could not get cmrField for Join Table " + var1);
      } else {
         Map var5 = this.getFkColumn2ClassMapForFkField(var4);
         if (null == var5) {
            throw new RDBMSException(" Bean: " + this.ejbName + ", could not get Column To Class Map for FK Field " + var4);
         } else {
            ArrayList var6 = new ArrayList();
            Iterator var7 = var5.keySet().iterator();

            Class var9;
            while(var7.hasNext()) {
               String var8 = (String)var7.next();
               var9 = (Class)var5.get(var8);
               var2.append(var8 + " ");
               var2.append(this.getDefaultDBMSColType(var9));
               if (this.databaseType == 2 || this.databaseType == 7) {
                  var2.append(" NOT NULL ");
               }

               var2.append(", ");
               var3.add(var8);
               var6.add(var8);
            }

            String var10;
            Class var11;
            if (this.isRemoteField(var4)) {
               if (debugLogger.isDebugEnabled()) {
                  debug(" Do REMOTE RHS of Join Table ");
               }

               this.getEjbEntityRef(var4);
               var9 = null;
               var10 = this.getRemoteColumn(var4);
               var2.append(var10 + " ");
               var11 = var9;
               if (!this.isValidSQLType(var9) && Serializable.class.isAssignableFrom(var9)) {
                  byte[] var12 = new byte[0];
                  var11 = var12.getClass();
               }

               var2.append(this.getDefaultDBMSColType(var11));
               var3.add(var10);
            } else {
               String var15;
               if (this.isSymmetricField(var4)) {
                  if (debugLogger.isDebugEnabled()) {
                     debug(" Do Symmetric RHS of Join Table ");
                  }

                  Map var13 = this.getSymmetricColumn2FieldName(var4);
                  if (null == var13) {
                     throw new RDBMSException(" Bean: " + this.ejbName + ", could not get Symmetric Column To Class " + "Map for FK Field " + var4);
                  }

                  var7 = var13.keySet().iterator();

                  while(var7.hasNext()) {
                     var15 = (String)var7.next();
                     if (!var6.contains(var15)) {
                        var2.append(var15 + " ");
                        var10 = (String)var13.get(var15);
                        var11 = this.getCmpFieldClass(var10);
                        var2.append(this.getDefaultDBMSColType(var11));
                        var3.add(var15);
                        if (var7.hasNext()) {
                           var2.append(", ");
                        }
                     }
                  }
               } else {
                  if (debugLogger.isDebugEnabled()) {
                     debug(" Do Normal non-Remote non-Symmetric RHS of Join Table ");
                  }

                  RDBMSBean var14 = this.getRelatedRDBMSBean(var4);
                  var15 = this.getRelatedFieldName(var4);
                  var5 = var14.getFkColumn2ClassMapForFkField(var15);
                  if (null == var5) {
                     throw new RDBMSException(" Bean: " + var14.getEjbName() + ", could not get Column To Class Map for FK Field " + var15);
                  }

                  var7 = var5.keySet().iterator();

                  while(var7.hasNext()) {
                     var10 = (String)var7.next();
                     if (!var6.contains(var10)) {
                        var11 = (Class)var5.get(var10);
                        var2.append(var10 + " ");
                        var2.append(this.getDefaultDBMSColType(var11));
                        var3.add(var10);
                        if (var7.hasNext()) {
                           var2.append(", ");
                        }
                     }
                  }
               }
            }

         }
      }
   }

   private void addBeanTableColumns(String var1, StringBuffer var2, Set var3) throws Exception {
      if (debugLogger.isDebugEnabled()) {
         debug(" createDefaultDBMSTable: processing Bean Table: " + var1);
      }

      List var4 = this.getPrimaryKeyFields();
      HashSet var5 = new HashSet();
      Map var6 = this.getCmpField2ColumnMap(var1);
      Iterator var7 = var6.keySet().iterator();

      while(true) {
         String var8;
         String var9;
         do {
            if (!var7.hasNext()) {
               List var14 = this.getCmrFields(var1);
               if (var14 != null) {
                  var7 = var14.iterator();

                  label63:
                  while(true) {
                     do {
                        if (!var7.hasNext()) {
                           return;
                        }

                        var9 = (String)var7.next();
                     } while(!this.containsFkField(var9));

                     Iterator var15 = this.getForeignKeyColNames(var9).iterator();
                     Map var11 = this.getFkColumn2ClassMapForFkField(var9);

                     while(true) {
                        do {
                           String var12;
                           do {
                              do {
                                 if (!var15.hasNext()) {
                                    continue label63;
                                 }

                                 var12 = (String)var15.next();
                              } while(var5.contains(var12));

                              var2.append(", ");
                              var5.add(var12);
                              var2.append(var12 + " ");
                              Class var13 = (Class)var11.get(var12);
                              var2.append(this.getDefaultDBMSColType(var13));
                           } while(!var4.contains(var9));

                           var3.add(var12);
                        } while(this.databaseType != 2 && this.databaseType != 7);

                        var2.append(" NOT NULL ");
                     }
                  }
               }

               return;
            }

            var8 = (String)var7.next();
            var9 = (String)var6.get(var8);
         } while(var5.contains(var9));

         var5.add(var9);
         var2.append(var9 + " ");
         Class var10 = this.getCmpFieldClass(var8);
         var2.append(this.getDefaultDBMSColType(var10));
         if (var4.contains(var8)) {
            var3.add(var9);
            if (this.databaseType == 2 || this.databaseType == 7 || this.databaseType == 4 || this.databaseType == 9) {
               var2.append(" NOT NULL ");
            }
         }

         if (var7.hasNext()) {
            var2.append(", ");
         }
      }
   }

   public void setCharArrayIsSerializedToBytes(boolean var1) {
      this.charArrayIsSerializedToBytes = var1;
   }

   public void setDisableStringTrimming(boolean var1) {
      this.disableStringTrimming = var1;
   }

   public void setFindersReturnNulls(boolean var1) {
      this.findersReturnNulls = var1;
   }

   public boolean isFindersReturnNulls() {
      return this.findersReturnNulls;
   }

   public boolean isStringTrimmingEnabled() {
      return !this.disableStringTrimming;
   }

   public boolean isCharArrayMappedToString(Class var1) {
      return var1.isArray() && var1.getComponentType() == Character.TYPE && !this.charArrayIsSerializedToBytes;
   }

   public String getDefaultDBMSColType(Class var1) throws Exception {
      if (this.isCharArrayMappedToString(var1)) {
         var1 = String.class;
      }

      return MethodUtils.getDefaultDBMSColType(var1, this.databaseType);
   }

   public boolean isValidSQLType(Class var1) {
      return ClassUtils.isValidSQLType(var1) || this.isCharArrayMappedToString(var1);
   }

   private static void p(String var0) {
      System.out.println("***<RDBMSBean> " + var0);
   }

   public void addSqlShape(SqlShape var1) {
      if (this.sqlShapes == null) {
         this.sqlShapes = new HashMap();
      }

      this.sqlShapes.put(var1.getSqlShapeName(), var1);
   }

   public SqlShape getSqlShape(String var1) {
      return this.sqlShapes == null ? null : (SqlShape)this.sqlShapes.get(var1);
   }

   public Map getSqlShapes() {
      return this.sqlShapes;
   }

   public Map getRdbmsBeanMap() {
      return this.rdbmsBeanMap;
   }

   public EjbRelation getEjbRelation(String var1) {
      return this.relationships == null ? null : this.relationships.getEjbRelation(var1);
   }

   private static void debug(String var0) {
      debugLogger.debug("[RDBMSBean] " + var0);
   }

   public Class getBeanInterface() {
      try {
         if (this.generatedBeanInterface == null) {
            this.generatedBeanInterface = Thread.currentThread().getContextClassLoader().loadClass(this.bd.getGeneratedBeanInterfaceName());
         }
      } catch (ClassNotFoundException var2) {
         throw new AssertionError("Could not find generated bean interface name:" + this.bd.getGeneratedBeanInterfaceName());
      }

      return this.generatedBeanInterface;
   }

   public Method getBeanInterfaceMethod(Method var1) {
      Class var2 = this.getBeanInterface();
      Method var3 = null;

      try {
         var3 = var2.getMethod(var1.getName(), var1.getParameterTypes());
         return var3;
      } catch (NoSuchMethodException var5) {
         throw new AssertionError("Could not find bean interface method:" + var1.getName());
      }
   }

   public void computeAllTableColumns(Map var1) {
      Iterator var2 = this.getTables().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         TreeMap var4 = new TreeMap(String.CASE_INSENSITIVE_ORDER);
         this.computeVariablesAndColumns(var3, (List)null, (List)null, var4);
         var1.put(var3, var4);
      }

   }

   public void computeVariablesAndColumns(String var1, List var2, List var3, Map var4) {
      Map var5 = this.getCmpField2ColumnMap(var1);
      String var8;
      if (var5 != null) {
         Iterator var6 = var5.keySet().iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            var8 = (String)var5.get(var7);

            assert var8 != null;

            if (var2 != null) {
               var2.add(var7);
            }

            if (var3 != null) {
               var3.add(var8);
            }

            if (var4 != null) {
               var4.put(var8, var8);
            }
         }
      }

      List var12 = this.getCmrFields(var1);
      if (var12 != null) {
         Iterator var13 = var12.iterator();

         while(var13.hasNext()) {
            var8 = (String)var13.next();
            Iterator var9 = this.getForeignKeyColNames(var8).iterator();

            while(var9.hasNext()) {
               String var10 = (String)var9.next();
               if (var2 != null) {
                  String var11 = this.variableForField(var8, var1, var10);
                  if (!var2.contains(var11)) {
                     var2.add(var11);
                  }
               }

               if (var3 != null && !var3.contains(var10)) {
                  var3.add(var10);
               }

               if (var4 != null && !var4.containsKey(var10)) {
                  var4.put(var10, var10);
               }
            }
         }
      }

   }

   public boolean isUseInnerJoin() {
      return this.useInnerJoin;
   }

   public void setUseInnerJoin(boolean var1) {
      this.useInnerJoin = var1;
   }

   public String getCategoryCmpField() {
      return this.categoryCmpField;
   }

   public void setCategoryCmpField(String var1) {
      this.categoryCmpField = var1;
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }
}
