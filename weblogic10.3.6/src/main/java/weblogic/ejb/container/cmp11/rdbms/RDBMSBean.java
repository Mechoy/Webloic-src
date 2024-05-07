package weblogic.ejb.container.cmp11.rdbms;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.cmp11.rdbms.finders.Finder;
import weblogic.ejb.container.cmp11.rdbms.finders.IllegalExpressionException;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.utils.ErrorCollectionException;

public final class RDBMSBean implements Cloneable {
   private static final DebugLogger debugLogger;
   private String ejbName;
   private String fileName;
   private List cmpFieldNames;
   private List primaryKeyFieldList;
   private List cmpColumnNames;
   private String dataSourceName;
   private String poolName;
   private Integer isolationLevel;
   private String schemaName;
   private String tableName;
   private boolean dbIsShared;
   private List attributeMap;
   private List finderList;
   private boolean useQuotedNames;
   private String createDefaultDBMSTable;
   private String validateDbSchemaWith;
   private boolean useTunedUpdates;
   private int databaseType;
   private CMPBeanDescriptor bd;

   public RDBMSBean() {
      this((String)null, "");
   }

   public RDBMSBean(String var1, String var2) {
      this.ejbName = null;
      this.fileName = null;
      this.cmpFieldNames = null;
      this.primaryKeyFieldList = null;
      this.cmpColumnNames = null;
      this.dataSourceName = null;
      this.poolName = null;
      this.isolationLevel = null;
      this.schemaName = null;
      this.tableName = null;
      this.dbIsShared = true;
      this.attributeMap = null;
      this.finderList = null;
      this.useQuotedNames = false;
      this.createDefaultDBMSTable = "false";
      this.validateDbSchemaWith = "";
      this.useTunedUpdates = true;
      this.databaseType = 0;
      this.bd = null;
      this.setSchemaName(var1);
      this.setTableName(var2);
      this.attributeMap = new ArrayList();
      this.finderList = new LinkedList();
      this.cmpFieldNames = new ArrayList();
      this.cmpColumnNames = new ArrayList();
   }

   public void setEjbName(String var1) {
      this.ejbName = var1;
   }

   public String getEjbName() {
      return this.ejbName;
   }

   public void setFileName(String var1) {
      this.fileName = var1;
   }

   public String getFileName() {
      return this.fileName;
   }

   public void setPoolName(String var1) {
      this.poolName = var1;
   }

   public String getPoolName() {
      return this.poolName;
   }

   public void setDataSourceName(String var1) {
      this.dataSourceName = var1;
   }

   public String getDataSourceName() {
      return this.dataSourceName;
   }

   public boolean useTunedUpdates() {
      return this.useTunedUpdates;
   }

   public void setEnableTunedUpdates(boolean var1) {
      this.useTunedUpdates = var1;
   }

   public int getDatabaseType() {
      return this.databaseType;
   }

   public void setDatabaseType(int var1) {
      this.databaseType = var1;
   }

   public void setTransactionIsolation(Integer var1) {
      this.isolationLevel = var1;
   }

   public Integer getTransactionIsolation() {
      return this.isolationLevel;
   }

   public void setSchemaName(String var1) {
      this.schemaName = var1;
   }

   public String getSchemaName() {
      return this.schemaName;
   }

   public void setTableName(String var1) {
      this.tableName = var1;
   }

   public String getTableName() {
      return this.tableName;
   }

   public String getQualifiedTableName() {
      return this.getSchemaName() != null && !this.getSchemaName().equals("") ? this.getSchemaName() + "." + this.getTableName() : this.getTableName();
   }

   public void setPrimaryKeyFields(List var1) {
      this.primaryKeyFieldList = var1;
   }

   public List getPrimaryKeyFields() {
      return this.primaryKeyFieldList;
   }

   public void setUseQuotedNames(boolean var1) {
      this.useQuotedNames = var1;
   }

   public boolean getUseQuotedNames() {
      return this.useQuotedNames;
   }

   public void addObjectLink(ObjectLink var1) {
      this.attributeMap.add(var1);
      String var2 = var1.getBeanField();
      String var3 = var1.getDBMSColumn();
      this.cmpFieldNames.add(var2);
      this.cmpColumnNames.add(var3);
   }

   public void addObjectLink(String var1, String var2) {
      ObjectLink var3 = new ObjectLink(var1, var2);
      this.addObjectLink(var3);
   }

   public Iterator getObjectLinks() {
      return this.attributeMap.iterator();
   }

   public List getCmpFieldNames() {
      return this.cmpFieldNames;
   }

   public List getCmpColumnNames() {
      return this.cmpColumnNames;
   }

   public String getColumnForField(String var1) {
      Iterator var2 = this.getObjectLinks();

      ObjectLink var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ObjectLink)var2.next();
      } while(!var3.getBeanField().equals(var1));

      return var3.getDBMSColumn();
   }

   public String getFieldForColumn(String var1) {
      Iterator var2 = this.getObjectLinks();

      ObjectLink var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ObjectLink)var2.next();
      } while(!var3.getDBMSColumn().equals(var1));

      return var3.getBeanField();
   }

   public Map getFieldToColumnMap() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.getObjectLinks();

      while(var2.hasNext()) {
         ObjectLink var3 = (ObjectLink)var2.next();
         var1.put(var3.getBeanField(), var3.getDBMSColumn());
      }

      return var1;
   }

   public Map getColumnToFieldMap() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.getObjectLinks();

      while(var2.hasNext()) {
         ObjectLink var3 = (ObjectLink)var2.next();
         var1.put(var3.getDBMSColumn(), var3.getBeanField());
      }

      return var1;
   }

   public List getFieldNamesList() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.getObjectLinks();

      while(var2.hasNext()) {
         ObjectLink var3 = (ObjectLink)var2.next();
         var1.add(var3.getBeanField());
      }

      return var1;
   }

   public Iterator getFieldNames() {
      List var1 = this.getFieldNamesList();
      return var1.iterator();
   }

   public String getCreateDefaultDBMSTables() {
      return this.createDefaultDBMSTable;
   }

   public void setCreateDefaultDBMSTables(String var1) {
      this.createDefaultDBMSTable = var1;
   }

   public String getValidateDbSchemaWith() {
      return this.validateDbSchemaWith;
   }

   public void setValidateDbSchemaWith(String var1) {
      this.validateDbSchemaWith = var1;
   }

   public void addFinder(Finder var1) {
      this.finderList.add(var1);
   }

   public void replaceFinder(Finder var1, Finder var2) throws IllegalArgumentException {
      int var3 = this.finderList.indexOf(var1);
      if (var3 == -1) {
         throw new IllegalArgumentException();
      } else {
         this.finderList.set(var3, var2);
      }
   }

   public Iterator getFinders() {
      return this.finderList.iterator();
   }

   public List getFinderList() {
      return this.finderList;
   }

   public void generateFinderSQLStatements() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Iterator var2 = this.getFinders();

      while(var2.hasNext()) {
         Finder var3 = (Finder)var2.next();

         try {
            var3.computeSQLQuery(this.getMapTable());
            if (debugLogger.isDebugEnabled()) {
               String var4 = var3.getSQLQuery();
               if (var4 == null) {
                  debug("finder.computSQLQuery: None generated.  NULL !");
               } else {
                  debug("finder.computSQLQuery: " + var4);
               }
            }
         } catch (IllegalExpressionException var5) {
            var1.add(var5);
         }
      }

      if (var1.getExceptions().size() > 0) {
         throw var1;
      }
   }

   public Finder getFinderForMethod(Method var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("RDBMSBean.getFinderForMethod(" + var1 + ")");
      }

      Iterator var2 = this.getFinders();

      Finder var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Finder)var2.next();
      } while(!var3.methodIsEquivalent(var1));

      return var3;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof RDBMSBean)) {
         return false;
      } else {
         RDBMSBean var2 = (RDBMSBean)var1;
         return this.ejbName.equals(var2.getEjbName());
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(150);
      var1.append("[weblogic.cmp.rdbms.RDBMSBean {");
      var1.append("\n\tejbName = " + this.ejbName);
      var1.append("\n\tpoolName = " + this.poolName);
      var1.append("\n\tschemaName = " + this.schemaName);
      var1.append("\n\ttableName = " + this.tableName);
      var1.append("\n\tisolationLevel = " + RDBMSUtils.isolationLevelToString(this.isolationLevel));
      var1.append("\n\tattributeMap = " + this.attributeMap);
      var1.append("\n\tfinderList = " + this.finderList);
      var1.append("\n\tuseQuotedNames = " + this.useQuotedNames);
      var1.append("\n\tdbIsShared = " + this.dbIsShared);
      var1.append("\n} end RDBMSBean ]\n");
      return var1.toString();
   }

   public int hashCode() {
      return this.ejbName.hashCode();
   }

   private Hashtable getMapTable() {
      Hashtable var1 = new Hashtable();
      Iterator var2 = this.getObjectLinks();

      while(var2.hasNext()) {
         ObjectLink var3 = (ObjectLink)var2.next();
         var1.put(var3.getBeanField(), var3.getDBMSColumn());
      }

      return var1;
   }

   public void setCMPBeanDescriptor(CMPBeanDescriptor var1) {
      this.bd = var1;
   }

   public Class getCmpFieldClass(String var1) {
      Iterator var2 = this.getObjectLinks();

      ObjectLink var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ObjectLink)var2.next();
      } while(!var1.equalsIgnoreCase(var3.getDBMSColumn()));

      return this.bd.getFieldClass(var3.getBeanField());
   }

   private static void debug(String var0) {
      debugLogger.debug("[RDBMSBean] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }

   public static class ObjectLink {
      public static final boolean verbose = false;
      public static final boolean debug = false;
      private String beanField = null;
      private String dbmsColumn = null;

      public ObjectLink(String var1, String var2) {
         this.setBeanField(var1);
         this.setDBMSColumn(var2);
      }

      private void setBeanField(String var1) {
         this.beanField = var1;
      }

      public String getBeanField() {
         return this.beanField;
      }

      public void setDBMSColumn(String var1) {
         this.dbmsColumn = var1;
      }

      public String getDBMSColumn() {
         return this.dbmsColumn;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof ObjectLink)) {
            return false;
         } else {
            ObjectLink var2 = (ObjectLink)var1;
            if (!this.beanField.equals(var2.getBeanField())) {
               return false;
            } else {
               return this.dbmsColumn.equals(var2.getDBMSColumn());
            }
         }
      }

      public int hashCode() {
         return this.beanField.hashCode() | this.dbmsColumn.hashCode();
      }

      public String toString() {
         return "[ObjectLink: field<" + this.beanField + "> to column <" + this.dbmsColumn + ">]";
      }
   }
}
