package weblogic.ejb.container.cmp11.rdbms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.ejb.EntityBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.WLCMPPersistenceManager;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.manager.BaseEntityManager;
import weblogic.ejb.container.persistence.spi.CMPBean;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.PersistenceManager;
import weblogic.ejb.container.persistence.spi.RSInfo;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.ejb.container.utils.TableVerifier;
import weblogic.ejb.container.utils.TableVerifierMetaData;
import weblogic.ejb.container.utils.TableVerifierSqlQuery;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.persistence.spi.PersistenceRuntimeException;
import weblogic.logging.Loggable;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.utils.AssertionError;

public final class PersistenceManagerImpl implements PersistenceManager, WLCMPPersistenceManager {
   private static final DebugLogger deploymentLogger;
   private static final DebugLogger runtimeLogger;
   private static final int GET_FIELD_INFO = 0;
   private static final int GET_COLUMN_INFO = 1;
   private BaseEntityManager beanManager = null;
   CMPBeanDescriptor bd = null;
   private RDBMSBean bean = null;
   private Driver jtsDriver = null;
   private Context ctx = null;
   private DataSource ds = null;
   private ClassLoader classLoader = null;
   private boolean usingJtsDriver = false;
   private TableVerifier verifier;
   private int databaseType = 0;
   private Map variable2SQLType = new HashMap();
   private Map variable2nullable = new HashMap();
   private TransactionManager tm = null;
   private String[] indexColumnMap = null;

   public void setup(BeanManager var1) throws Exception {
      if (deploymentLogger.isDebugEnabled()) {
         debugDeployment("PersistenceManagerImpl.setup");
      }

      assert this.bd != null : "bd != null failed ";

      assert this.bean != null : "bean != null failed";

      this.beanManager = (BaseEntityManager)var1;
      this.classLoader = this.beanManager.getEJBHome().getBeanInfo().getClassLoader();
      if ("MetaData".equalsIgnoreCase(this.getValidateDbSchemaWith())) {
         this.verifier = new TableVerifierMetaData();
      } else {
         this.verifier = new TableVerifierSqlQuery();
      }

      Loggable var3;
      if (this.bean.getPoolName() != null) {
         try {
            this.jtsDriver = (Driver)Class.forName("weblogic.jdbc.jts.Driver").newInstance();
         } catch (Exception var6) {
            var3 = EJBLogger.logUnableToLoadJTSDriverLoggable(var6);
            throw new WLDeploymentException(var3.getMessage(), var6);
         }

         assert this.jtsDriver != null;

         this.usingJtsDriver = true;
      } else {
         assert this.bean.getDataSourceName() != null;

         try {
            this.ctx = new InitialContext();
         } catch (NamingException var5) {
            throw new AssertionError(var5);
         }

         try {
            this.ds = (DataSource)this.ctx.lookup(this.bean.getDataSourceName());
         } catch (NamingException var4) {
            var3 = EJBLogger.logDataSourceNotFoundLoggable(this.bean.getDataSourceName());
            throw new WLDeploymentException(var3.getMessage(), var4);
         }
      }

      this.databaseType = this.bean.getDatabaseType();
      this.tm = TxHelper.getTransactionManager();
      this.verifyDatabaseType();
      this.verifyTablesExist();
      this.populateIndexColumnMap();
      this.populateFieldSQLTypeMap();
   }

   public String getCreateDefaultDBMSTables() {
      return this.bean.getCreateDefaultDBMSTables();
   }

   public String getValidateDbSchemaWith() {
      return this.bean.getValidateDbSchemaWith();
   }

   public String getEjbName() {
      return this.bean.getEjbName();
   }

   public EntityBean getBeanFromPool() throws InternalException {
      return this.beanManager.getBeanFromPool();
   }

   public EntityBean getBeanFromRS(Object var1, RSInfo var2) throws InternalException {
      return this.beanManager.getBeanFromRS(var1, var2);
   }

   public Object finderGetEoFromBeanOrPk(EntityBean var1, Object var2, boolean var3) throws InternalException {
      return this.beanManager.finderGetEoFromBeanOrPk(var1, var2, var3);
   }

   public ClassLoader getClassLoader() {
      return this.classLoader;
   }

   public Object findByPrimaryKey(EntityBean var1, Method var2, Object var3) throws Throwable {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.findByPrimaryKey");
      }

      assert var1 != null;

      assert var2 != null;

      assert var3 != null;

      Object[] var4 = new Object[]{var3};

      try {
         return var2.invoke(var1, var4);
      } catch (InvocationTargetException var7) {
         Throwable var6 = var7.getTargetException();
         throw var6;
      } catch (Exception var8) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var8);
         }

         throw var8;
      }
   }

   public int getSelectForUpdateValue() {
      try {
         Transaction var1 = this.tm.getTransaction();
         TransactionImpl var2 = (TransactionImpl)var1;
         Integer var3 = null;
         if (var2 != null) {
            var3 = (Integer)var2.getProperty("SELECT_FOR_UPDATE");
         }

         return var3 == null ? 0 : var3;
      } catch (Exception var4) {
         throw new PersistenceRuntimeException(var4);
      }
   }

   public String selectForUpdate() {
      return RDBMSUtils.selectForUpdateToString(this.getSelectForUpdateValue());
   }

   private void verifyDatabaseType() throws WLDeploymentException {
      Connection var1 = null;

      try {
         var1 = this.getConnection();
         this.databaseType = this.verifier.verifyDatabaseType(var1, this.databaseType);
      } catch (Exception var7) {
         throw new WLDeploymentException(var7.getMessage(), var7);
      } finally {
         this.releaseResources(var1, (PreparedStatement)null, (ResultSet)null);
      }

   }

   private void verifyTablesExist() throws WLDeploymentException {
      Connection var1 = null;

      try {
         var1 = this.getConnection();
         this.verifier.verifyOrCreateOrAlterTable(this, var1, this.bean.getQualifiedTableName(), this.getPersistentFieldsOrColumns(1), true, this.getPersistentFieldsOrColumns(0), this.variable2SQLType, this.variable2nullable, this.bean.getCreateDefaultDBMSTables(), false);
      } catch (Exception var7) {
         throw new WLDeploymentException(var7.getMessage(), var7);
      } finally {
         this.releaseResources(var1, (PreparedStatement)null, (ResultSet)null);
      }

   }

   public EntityBean findByPrimaryKeyLoadBean(EntityBean var1, Method var2, Object var3) throws Throwable {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.findByPrimaryKeyLoadBean");
      }

      try {
         Object[] var4 = new Object[]{var3};
         return (EntityBean)var2.invoke(var1, var4);
      } catch (InvocationTargetException var6) {
         Throwable var5 = var6.getTargetException();
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var5);
         }

         throw var5;
      } catch (Exception var7) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var7);
         }

         throw var7;
      }
   }

   public Object scalarFinder(EntityBean var1, Method var2, Object[] var3) throws Throwable {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.scalarFinder");
      }

      try {
         return var2.invoke(var1, var3);
      } catch (InvocationTargetException var6) {
         Throwable var5 = var6.getTargetException();
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var5);
         }

         throw var5;
      } catch (Exception var7) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var7);
         }

         throw var7;
      }
   }

   public Map scalarFinderLoadBean(EntityBean var1, Method var2, Object[] var3) throws Throwable {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.scalarFinderLoadBean");
      }

      try {
         return (Map)var2.invoke(var1, var3);
      } catch (InvocationTargetException var6) {
         Throwable var5 = var6.getTargetException();
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var5);
         }

         throw var5;
      } catch (Exception var7) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var7);
         }

         throw var7;
      }
   }

   public Enumeration enumFinder(EntityBean var1, Method var2, Object[] var3) throws Throwable {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.enumFinder");
      }

      try {
         return (Enumeration)var2.invoke(var1, var3);
      } catch (InvocationTargetException var6) {
         Throwable var5 = var6.getTargetException();
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var5);
         }

         throw var5;
      } catch (Exception var7) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var7);
         }

         throw var7;
      }
   }

   public Collection collectionFinder(EntityBean var1, Method var2, Object[] var3) throws Throwable {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.collectionFinder");
      }

      try {
         return (Collection)var2.invoke(var1, var3);
      } catch (InvocationTargetException var6) {
         Throwable var5 = var6.getTargetException();
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var5);
         }

         throw var5;
      } catch (Exception var7) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var7);
         }

         throw var7;
      }
   }

   public Map collectionFinderLoadBean(EntityBean var1, Method var2, Object[] var3) throws Throwable {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.collectionFinderLoadBean");
      }

      try {
         return (Map)var2.invoke(var1, var3);
      } catch (InvocationTargetException var6) {
         Throwable var5 = var6.getTargetException();
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var5);
         }

         throw var5;
      } catch (Exception var7) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Error invoking finder: ", var7);
         }

         throw var7;
      }
   }

   public void loadBeanFromRS(EntityBean var1, RSInfo var2) throws InternalException {
      try {
         ((CMPBean)var1).__WL_loadGroupByIndex(var2.getGroupIndex(), var2.getRS(), var2.getOffset(), var2.getPK(), var1);
      } catch (Exception var4) {
         EJBRuntimeUtils.throwInternalException("Error load bean states from ResultSet", var4);
      }

   }

   public Connection getConnection() throws SQLException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.getConnection");
      }

      Connection var1 = null;
      if (this.usingJtsDriver) {
         assert this.getDriver() != null;

         assert this.bean.getPoolName() != null;

         assert this.getDriver().acceptsURL("jdbc:weblogic:jts:" + this.bean.getPoolName());

         var1 = this.getDriver().connect("jdbc:weblogic:jts:" + this.bean.getPoolName(), (Properties)null);
      } else {
         var1 = this.ds.getConnection();
      }

      if (var1 == null) {
         Loggable var2 = EJBLogger.logCouldNotGetConnectionFromLoggable(this.connectionProducerType() + " '" + this.connectionProducerName() + "'");
         throw new SQLException(var2.getMessage());
      } else {
         return var1;
      }
   }

   public void setBeanInfo(RDBMSBean var1) {
      assert var1 != null;

      assert var1.getPoolName() != null || var1.getDataSourceName() != null : "No pool or data source set for this bean.";

      assert var1.getTableName() != null : "No table name set for this bean.";

      this.bean = var1;
   }

   public RDBMSBean getBeanInfo() {
      return this.bean;
   }

   private Driver getDriver() {
      return this.jtsDriver;
   }

   private String connectionProducerType() {
      return this.usingJtsDriver ? "connection pool" : "data source";
   }

   private String connectionProducerName() {
      return this.usingJtsDriver ? this.bean.getPoolName() : this.bean.getDataSourceName();
   }

   private void populateIndexColumnMap() {
      if (deploymentLogger.isDebugEnabled()) {
         debugDeployment("PersistenceManagerImpl.populateIndexColumnMap(");
      }

      Map var1 = this.getBeanInfo().getFieldToColumnMap();
      String[] var2 = (String[])((String[])this.bean.getFieldNamesList().toArray(new String[0]));
      this.indexColumnMap = new String[var2.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.indexColumnMap[var3] = (String)var1.get(var2[var3]);
      }

   }

   public String getColumnName(int var1) {
      if (deploymentLogger.isDebugEnabled()) {
         debugDeployment("PersistenceManagerImpl.getColumnName");
      }

      assert this.indexColumnMap != null;

      if (deploymentLogger.isDebugEnabled()) {
         debugDeployment("modifiedIndex: " + var1 + " indexColumnMap.length: " + this.indexColumnMap.length);
      }

      assert var1 < this.indexColumnMap.length;

      if (deploymentLogger.isDebugEnabled()) {
         debugDeployment("returning: " + this.indexColumnMap[var1]);
      }

      return this.indexColumnMap[var1];
   }

   private List getPersistentFieldsOrColumns(int var1) {
      List var2 = this.getBeanInfo().getFieldNamesList();
      ArrayList var3 = new ArrayList();
      Map var4 = this.getBeanInfo().getFieldToColumnMap();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         String var7 = (String)var4.get(var6);

         assert var7 != null;

         var3.add(var7);
      }

      switch (var1) {
         case 0:
            return var2;
         case 1:
            return var3;
         default:
            throw new AssertionError("Unknown returnType: " + var1 + " encountered in " + "RDBMSersistenceManager." + "getPersistentFieldsOrColumns(int returnType)");
      }
   }

   private void populateFieldSQLTypeMap() throws WLDeploymentException {
      if (deploymentLogger.isDebugEnabled()) {
         debugDeployment("PersistenceManagerImpl.populateFieldSQLTypeMap");
      }

      if (this.variable2SQLType.size() <= 0) {
         Connection var1 = null;

         try {
            var1 = this.getConnection();
            this.verifier.verifyOrCreateOrAlterTable(this, var1, this.getBeanInfo().getQualifiedTableName(), this.getPersistentFieldsOrColumns(1), true, this.getPersistentFieldsOrColumns(0), this.variable2SQLType, this.variable2nullable, (String)null, false);
            if (this.variable2SQLType.size() <= 0) {
               Loggable var2 = EJBLogger.logCouldNotInitializeFieldSQLTypeMapWithoutExceptionLoggable();
               throw new WLDeploymentException(var2.getMessage());
            }
         } catch (Exception var8) {
            Loggable var3 = EJBLogger.logCouldNotInitializeFieldSQLTypeMapLoggable(var8);
            throw new WLDeploymentException(var3.getMessage(), var8);
         } finally {
            this.releaseResources(var1, (PreparedStatement)null, (ResultSet)null);
         }

      }
   }

   public boolean setParamNull(PreparedStatement var1, int var2, Object var3, String var4) throws SQLException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.setParamNull");
      }

      if (var3 == null) {
         assert this.variable2SQLType != null;

         assert this.variable2SQLType.get(var4) != null : "No field->SQLType mapping for field " + var4;

         Integer var5 = (Integer)this.variable2SQLType.get(var4);
         int var6 = var5;
         var1.setNull(var2, var6);
         return true;
      } else {
         return false;
      }
   }

   public void releaseResources(Connection var1, PreparedStatement var2, ResultSet var3) {
      this.releaseResources(var1, (Statement)var2, var3);
   }

   public void releaseResources(Connection var1, Statement var2, ResultSet var3) {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.releaseResources");
      }

      try {
         this.releaseResultSet(var3);
      } catch (SQLException var7) {
      }

      try {
         this.releaseStatement(var2);
      } catch (SQLException var6) {
      }

      try {
         this.releaseConnection(var1);
      } catch (SQLException var5) {
      }

   }

   public void releaseConnection(Connection var1) throws SQLException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.releaseConnection");
      }

      if (var1 != null && !var1.isClosed()) {
         var1.close();
      }

   }

   public void releaseStatement(Statement var1) throws SQLException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.releaseStatement");
      }

      if (var1 != null) {
         var1.close();
      }

   }

   public void releaseResultSet(ResultSet var1) throws SQLException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("PersistenceManagerImpl.releaseResultSet");
      }

      if (var1 != null) {
         var1.close();
      }

   }

   public void dropAndCreateDefaultDBMSTable(String var1) throws WLDeploymentException {
      StringBuffer var2 = new StringBuffer("DROP TABLE " + var1);
      Connection var3 = null;
      Statement var4 = null;

      try {
         var3 = this.getConnection();
         var4 = var3.createStatement();
         var4.executeUpdate(var2.toString());
      } catch (Exception var11) {
         Loggable var6 = EJBLogger.logerrorDroppingDefaultDBMSTableLoggable(var1, var11.getMessage());
         var6.log();
      } finally {
         this.releaseResources(var3, (Statement)var4, (ResultSet)null);
      }

      this.createDefaultDBMSTable(var1);
   }

   public void alterDefaultDBMSTable(String var1, Set var2, Set var3) throws WLDeploymentException {
      if (var2.isEmpty() && var3.isEmpty()) {
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("Table not changed so no alter table");
         }

      } else {
         this.alterOracleDefaultDBMSTable(var1, var2, var3);
      }
   }

   private void alterOracleDefaultDBMSTable(String var1, Set var2, Set var3) throws WLDeploymentException {
      this.addColumns(var1, var2);
      this.removeColumns(var1, var3);
   }

   private void removeColumns(String var1, Set var2) throws WLDeploymentException {
      StringBuffer var3 = new StringBuffer("alter table " + var1 + " ");
      if (!var2.isEmpty()) {
         var3.append("drop ( ");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var3.append(var5);
            if (var4.hasNext()) {
               var3.append(",");
            }
         }

         var3.append(" ) ");
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("The alter table command is ..." + var3);
         }

         Connection var14 = null;
         Statement var15 = null;

         try {
            var14 = this.getConnection();
            var15 = var14.createStatement();
            var15.executeUpdate(var3.toString());
         } catch (Exception var12) {
            Loggable var7 = EJBLogger.logerrorAlteringDefaultDBMSTableLoggable(var1, var12.getMessage());
            var7.log();
            throw new WLDeploymentException(var7.getMessage(), var12);
         } finally {
            this.releaseResources(var14, (Statement)var15, (ResultSet)null);
         }

      }
   }

   private void addColumns(String var1, Set var2) throws WLDeploymentException {
      StringBuffer var3 = new StringBuffer("alter table " + var1 + " ");
      if (!var2.isEmpty()) {
         var3.append("add ( ");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var3.append(var5 + " " + this.getSqltypeForCol(var5));
            if (var4.hasNext()) {
               var3.append(",");
            }
         }

         var3.append(" ) ");
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("The alter table command is ..." + var3);
         }

         Connection var14 = null;
         Statement var15 = null;

         try {
            var14 = this.getConnection();
            var15 = var14.createStatement();
            var15.executeUpdate(var3.toString());
         } catch (Exception var12) {
            Loggable var7 = EJBLogger.logerrorAlteringDefaultDBMSTableLoggable(var1, var12.getMessage());
            var7.log();
            throw new WLDeploymentException(var7.getMessage(), var12);
         } finally {
            this.releaseResources(var14, (Statement)var15, (ResultSet)null);
         }

      }
   }

   private String getSqltypeForCol(String var1) throws WLDeploymentException {
      Class var2 = this.bean.getCmpFieldClass(var1);
      if (null == var2) {
         throw new WLDeploymentException(" Bean: " + this.bean.getEjbName() + ", could not get Column To Field Map for column ");
      } else {
         try {
            return MethodUtils.getDefaultDBMSColType(var2, this.databaseType);
         } catch (Exception var4) {
            throw new WLDeploymentException("No Field class found for " + var1);
         }
      }
   }

   public boolean createDefaultDBMSTable(String var1) throws WLDeploymentException {
      StringBuffer var2 = new StringBuffer("CREATE TABLE " + var1 + " (");
      Connection var3 = null;
      Statement var4 = null;
      List var5 = this.bean.getPrimaryKeyFields();
      HashSet var6 = new HashSet();

      try {
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment(" createDefaultDBMSTable: processing Bean Table: " + var1);
         }

         Iterator var7 = this.bean.getFieldNames();

         String var17;
         while(var7.hasNext()) {
            var17 = (String)var7.next();
            String var9 = this.bean.getColumnForField(var17);
            var2.append(var9 + " ");
            if (var5.contains(var17)) {
               var6.add(var9);
            }

            Class var10 = this.bd.getFieldClass(var17);
            var2.append(MethodUtils.getDefaultDBMSColType(var10, this.databaseType));
            if (var7.hasNext()) {
               var2.append(", ");
            }
         }

         if (var6.size() > 0) {
            var2.append(", PRIMARY KEY (");
            var7 = var6.iterator();

            while(var7.hasNext()) {
               var17 = (String)var7.next();
               var2.append(var17);
               if (var7.hasNext()) {
                  var2.append(", ");
               }
            }

            var2.append(")");
         }

         var2.append(")");
         var17 = var2.toString();
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment(" full CREATE TABLE QUERY: '" + var17 + "'");
         }

         var3 = this.getConnection();
         var4 = var3.createStatement();
         var4.executeUpdate(var17);
      } catch (Exception var15) {
         Loggable var8 = EJBLogger.logerrorCreatingDefaultDBMSTableLoggable(var1, var15.getMessage());
         var8.log();
         throw new WLDeploymentException(var8.getMessage(), var15);
      } finally {
         this.releaseResources(var3, (Statement)var4, (ResultSet)null);
      }

      return true;
   }

   public void updateClassLoader(ClassLoader var1) {
      this.classLoader = var1;
   }

   private static void debugDeployment(String var0) {
      deploymentLogger.debug("[PersistenceManagerImpl] " + var0);
   }

   private static void debugRuntime(String var0) {
      runtimeLogger.debug("[PersistenceManagerImpl] " + var0);
   }

   private static void debugRuntime(String var0, Throwable var1) {
      runtimeLogger.debug("[PersistenceManagerImpl] " + var0, var1);
   }

   static {
      deploymentLogger = EJBDebugService.cmpDeploymentLogger;
      runtimeLogger = EJBDebugService.cmpRuntimeLogger;
   }
}
