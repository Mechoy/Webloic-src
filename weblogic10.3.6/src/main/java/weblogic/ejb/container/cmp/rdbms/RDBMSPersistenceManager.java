package weblogic.ejb.container.cmp.rdbms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import weblogic.dbeans.ConversationImpl;
import weblogic.dbeans.DataBeansException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.OptimisticConcurrencyException;
import weblogic.ejb.PreparedQuery;
import weblogic.ejb.WLQueryProperties;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.cache.MultiValueQueryCacheElement;
import weblogic.ejb.container.cache.QueryCacheElement;
import weblogic.ejb.container.cache.QueryCacheKey;
import weblogic.ejb.container.cmp.rdbms.finders.EjbqlFinder;
import weblogic.ejb.container.cmp.rdbms.finders.Finder;
import weblogic.ejb.container.cmp.rdbms.finders.ParamNode;
import weblogic.ejb.container.cmp.rdbms.finders.SqlFinder;
import weblogic.ejb.container.dd.DDConstants;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.WLCMPPersistenceManager;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.EntityEJBContextImpl;
import weblogic.ejb.container.internal.QueryCachingHandler;
import weblogic.ejb.container.manager.BaseEntityManager;
import weblogic.ejb.container.manager.TTLManager;
import weblogic.ejb.container.persistence.RSInfoImpl;
import weblogic.ejb.container.persistence.spi.CMPBean;
import weblogic.ejb.container.persistence.spi.CMPBeanManager;
import weblogic.ejb.container.persistence.spi.EjbEntityRef;
import weblogic.ejb.container.persistence.spi.PersistenceManager;
import weblogic.ejb.container.persistence.spi.RSInfo;
import weblogic.ejb.container.utils.TableVerifier;
import weblogic.ejb.container.utils.TableVerifierMetaData;
import weblogic.ejb.container.utils.TableVerifierSqlQuery;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.ejb20.cmp.rdbms.finders.InvalidFinderException;
import weblogic.ejb20.persistence.spi.PersistenceRuntimeException;
import weblogic.jdbc.common.internal.DataSourceMetaData;
import weblogic.jdbc.rowset.CachedRowSetMetaData;
import weblogic.jdbc.rowset.RowSetFactory;
import weblogic.jdbc.rowset.WLCachedRowSet;
import weblogic.logging.Loggable;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.collections.ArraySet;
import weblogic.utils.collections.ConcurrentHashMap;

public final class RDBMSPersistenceManager implements PersistenceManager, WLCMPPersistenceManager {
   private static final DebugLogger deploymentLogger;
   private static final DebugLogger runtimeLogger;
   private static final String EOL = "\n";
   private TableVerifier verifier;
   private RDBMSBean rbean = null;
   private int databaseType = 0;
   private Map variable2SQLType = new HashMap();
   private Map variable2nullable = new HashMap();
   private Map fkFieldNullableMap = new HashMap();
   private boolean fkColsNullable = true;
   private String[] snapshotStrings = null;
   private String[] nullSnapshotStrings = null;
   private Context ctx = null;
   private DataSource ds = null;
   private ClassLoader classLoader = null;
   private BaseEntityManager beanManager = null;
   private short genKeyType;
   private String genKeyWLGeneratorQuery = null;
   private String genKeyWLGeneratorUpdatePrefix = null;
   private String genKeyWLGeneratorUpdate = null;
   private String genKeyGeneratorName = null;
   private Map finderMap;
   private int genKeyCacheSize = 1;
   private int genKeyCurrCacheSize = 0;
   private int genKeyCurrValueInt = 0;
   private long genKeyCurrValueLong = 0L;
   private short genKeyPKFieldClassType;
   private boolean enableBatchOperations = true;
   private boolean orderDatabaseOperations = true;
   private boolean isOptimistic = false;
   private boolean findersReturnNulls = true;
   private int transactionTimeoutMS = 0;
   private String dataSourceName = null;
   private String ejbName = null;
   private boolean selectForUpdateSupported = false;
   private boolean selectFirstSeqKeyBeforeUpdate = false;
   private TransactionManager tm = null;
   private String[] verifyText = null;
   private String[] verifyTextWithXLock = null;
   private int[] verifyCount = null;
   private int[] verifyCur = null;
   private static final String ORACLE_JDBC_DRIVER_NAME = "Oracle JDBC driver";
   private String databaseProductName = "";
   private String databaseProductVersion = "";
   private String driverName = "";
   private String driverVersion = "";
   private int driverMajorVersion = -1;
   private int driverMinorVersion = -1;
   private boolean initialized = false;
   private static final byte[] byteArray;
   private static final char[] charArray;

   public void setup(BeanManager var1) throws Exception {
      if (deploymentLogger.isDebugEnabled()) {
         debugDeployment("RDBMSPersistenceManager.setup");
      }

      if (!this.initialized) {
         assert this.rbean != null : "rbean != null failed";

         this.beanManager = (BaseEntityManager)var1;
         this.classLoader = this.beanManager.getBeanInfo().getClassLoader();
         if ("MetaData".equalsIgnoreCase(this.getValidateDbSchemaWith())) {
            this.verifier = new TableVerifierMetaData();
         } else {
            this.verifier = new TableVerifierSqlQuery();
         }

         try {
            this.ctx = new InitialContext();
         } catch (NamingException var7) {
            throw new AssertionError(var7);
         }

         this.enableBatchOperations = this.rbean.getEnableBatchOperations();
         this.orderDatabaseOperations = this.rbean.getOrderDatabaseOperations();
         this.isOptimistic = this.rbean().getCMPBeanDescriptor().isOptimistic();
         this.transactionTimeoutMS = this.rbean().getCMPBeanDescriptor().getTransactionTimeoutMS();
         this.dataSourceName = this.rbean().getDataSourceName();
         this.ejbName = this.rbean().getEjbName();
         this.databaseType = this.rbean.getDatabaseType();
         this.findersReturnNulls = this.rbean.isFindersReturnNulls();

         try {
            this.ds = (DataSource)this.ctx.lookup("java:/app/jdbc/" + this.dataSourceName);
         } catch (NamingException var6) {
            try {
               this.ds = (DataSource)this.ctx.lookup(this.dataSourceName);
            } catch (NamingException var5) {
               Loggable var4 = EJBLogger.logDataSourceNotFoundLoggable(this.dataSourceName);
               throw new WLDeploymentException(var4.getMessage());
            }
         }

         this.verifyTXDataSource();
         this.tm = TxHelper.getTransactionManager();
         this.genKeyType = this.rbean().getGenKeyType();
         this.verifyDatabaseType();
         this.verifyTablesExist();
         this.verifyBatchUpdatesSupported();
         this.selectForUpdateSupported = this.verifySelectForUpdateSupported();
         if (this.rbean.getUseSelectForUpdate() && !this.selectForUpdateSupported) {
            Loggable var2 = EJBLogger.logselectForUpdateNotSupportedLoggable(this.ejbName);
            throw new WLDeploymentException(var2.getMessage());
         } else {
            this.populateSnapShotStrings();
            this.populateFieldSQLTypeMap();
            this.populateVerifyRows();
            this.genKeySetup();
            this.sqlFinderSetup();
            this.initialized = true;
            this.initializeDBProductAndDriverInfo();
         }
      }
   }

   private void sqlFinderSetup() throws WLDeploymentException {
      Iterator var1 = this.rbean.getFinders();

      while(var1.hasNext()) {
         Finder var2 = (Finder)var1.next();
         if (this.finderMap == null) {
            this.finderMap = new ConcurrentHashMap();
         }

         if (var2 instanceof SqlFinder) {
            SqlFinder var3 = (SqlFinder)var2;
            var3.setup(this.databaseType);
            if (deploymentLogger.isDebugEnabled()) {
               debugDeployment("EJB-" + this.rbean.getEjbName() + ": add a method to map- " + var3.getName());
               debugDeployment("method- " + var3.getMethod());
            }

            if (var3.isSelect()) {
               this.finderMap.put(this.rbean.getBeanInterfaceMethod(var3.getMethod()), var3);
            } else {
               this.finderMap.put(var3.getMethod(), var3);
               if (var3.getSecondMethod() != null) {
                  this.finderMap.put(var3.getSecondMethod(), var3);
               }
            }
         }
      }

   }

   public boolean isFindersReturnNulls() {
      return this.findersReturnNulls;
   }

   public Object findByPrimaryKey(EntityBean var1, Method var2, Object var3) throws Throwable {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("RDBMSPersistenceManager.findByPrimaryKey");
      }

      Debug.assertion(var1 != null);
      Debug.assertion(var2 != null);
      Debug.assertion(var3 != null);
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

   public EntityBean findByPrimaryKeyLoadBean(EntityBean var1, Method var2, Object var3) throws Throwable {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("RDBMSPersistenceManager.findByPrimaryKeyLoadBean");
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
         debugRuntime("RDBMSPersistenceManager.scalarFinder");
      }

      try {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("---------------------------searching for method- " + var2);
         }

         SqlFinder var4 = (SqlFinder)this.finderMap.get(var2);
         return var4 != null ? this.processSqlFinder(var4, var3, ((WLEnterpriseBean)var1).__WL_getIsLocal()) : var2.invoke(var1, var3);
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
         debugRuntime("RDBMSPersistenceManager.scalarFinderLoadBean");
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
         debugRuntime("RDBMSPersistenceManager.enumFinder");
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
         debugRuntime("RDBMSPersistenceManager.collectionFinder");
      }

      try {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("---------------------------searching for method- " + var2);
         }

         SqlFinder var4 = (SqlFinder)this.finderMap.get(var2);
         return var4 != null ? (Collection)this.processSqlFinder(var4, var3, ((WLEnterpriseBean)var1).__WL_getIsLocal()) : (Collection)var2.invoke(var1, var3);
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

   public Object dynamicSqlQuery(String var1, Object[] var2, WLQueryProperties var3, boolean var4, Class var5, ConversationImpl var6) {
      SqlFinder var7 = null;
      String var8 = null;
      boolean var9 = false;
      boolean var10 = true;
      boolean var11 = false;

      try {
         var8 = var3.getSqlShapeName();
         if (var8 != null && this.rbean.getSqlShape(var8) == null) {
            String var12 = "";
            if (this.rbean.getSqlShapes() != null) {
               Iterator var13 = this.rbean.getSqlShapes().values().iterator();

               while(var13.hasNext()) {
                  SqlShape var14 = (SqlShape)var13.next();
                  var12 = var12 + var14.getSqlShapeName();
                  if (var13.hasNext()) {
                     var12 = var12 + ", ";
                  }
               }
            }

            throw new EJBException(EJBLogger.logSqlShapeDoesNotExist(this.rbean.getEjbName(), "Dynamic SQL Query", var8, var12));
         }
      } catch (FinderException var22) {
         throw new AssertionError("should never get here");
      }

      int var23;
      try {
         var23 = var3.getMaxElements();
      } catch (FinderException var19) {
         throw new AssertionError("should never get here");
      }

      try {
         var10 = var3.getIncludeUpdates();
      } catch (FinderException var18) {
         throw new AssertionError("should never get here");
      }

      try {
         if (var4 && var3.isResultTypeRemote()) {
            var4 = false;
         }
      } catch (FinderException var21) {
         throw new AssertionError("should never get here");
      }

      try {
         var11 = var3.getEnableQueryCaching();
      } catch (FinderException var17) {
         throw new AssertionError("should never get here");
      }

      try {
         assert this.rbean != null;

         var7 = new SqlFinder("execute", (Map)null, var8, this.rbean, var6);
         var7.setReturnClassType(var5);
         var7.setMaxElements(var23);
         var7.setIncludeUpdates(var10);
      } catch (InvalidFinderException var20) {
         throw new DataBeansException(var20);
      }

      try {
         var7.setupDynamic(var1);
      } catch (WLDeploymentException var16) {
         throw new DataBeansException(var16);
      }

      var7.setQueryCachingEnabled(var11);

      try {
         return this.processSqlFinder(var7, var2, var4);
      } catch (FinderException var15) {
         throw new DataBeansException(var15);
      }
   }

   public Object processSqlFinder(Method var1, Object[] var2, boolean var3) throws FinderException {
      SqlFinder var4 = (SqlFinder)this.finderMap.get(var1);
      if (var4 != null) {
         return this.processSqlFinder(var4, var2, var3);
      } else {
         throw new AssertionError("no SqlFinder found for method:" + var1);
      }
   }

   private Object processSqlFinder(SqlFinder var1, Object[] var2, boolean var3) throws FinderException {
      Connection var4 = null;
      PreparedStatement var5 = null;
      ResultSet var6 = null;
      if (var1.getIncludeUpdates()) {
         this.flushModifiedBeans();
      }

      try {
         var4 = this.getConnection();
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime(var1.getName() + " got connection.");
         }
      } catch (Exception var51) {
         this.releaseResources(var4, var5, var6);
         Loggable var8 = EJBLogger.logDuringFindCannotGetConnectionLoggable(var1.getName(), var51.toString(), RDBMSUtils.throwable2StackTrace(var51));
         throw new FinderException(var8.getMessage());
      }

      String[] var7 = new String[1];

      Loggable var9;
      try {
         var5 = this.getStatement(var4, var1, var7);
      } catch (Exception var44) {
         this.releaseResources(var4, var5, var6);
         var9 = EJBLogger.logExceptionWhilePrepareingQueryLoggable(var1.getName(), var7[0], var44.toString(), RDBMSUtils.throwable2StackTrace(var44));
         throw new FinderException(var9.getMessage());
      }

      try {
         this.setParameters(var5, var1, var2);
      } catch (Exception var43) {
         this.releaseResources(var4, var5, var6);
         var9 = EJBLogger.logErrorSetQueryParametorLoggable(var1.getName(), var7[0], var43.toString(), RDBMSUtils.throwable2StackTrace(var43));
         throw new FinderException(var9.getMessage());
      }

      try {
         var6 = this.getResultSet(var5, var1);
      } catch (Exception var42) {
         this.releaseResources(var4, var5, var6);
         var9 = EJBLogger.logErrorExecuteQueryLoggable(var1.getName(), var42.toString(), RDBMSUtils.throwable2StackTrace(var42));
         throw new FinderException(var9.getMessage());
      }

      Object var52 = null;
      ArrayList var53 = null;
      WLCachedRowSet var10 = null;
      ArrayList var11 = new ArrayList();

      Object var55;
      try {
         var1.initializeMapping(var6);
         QueryCachingHandler var12 = null;
         if (var1.isQueryCachingEnabled()) {
            var12 = var1.getQueryCachingHandler(var2, (TTLManager)this.getBeanManager());
         } else {
            var12 = var1.getQueryCachingHandler((Object[])null, (TTLManager)null);
         }

         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime(var1.getName() + " mappings have been initialized.");
         }

         if (Collection.class.isAssignableFrom(var1.getReturnClassType())) {
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime(var1.getName() + " is returning a collection.");
            }

            var53 = new ArrayList();
         } else if (ResultSet.class.isAssignableFrom(var1.getReturnClassType())) {
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime(var1.getName() + " is returning a ResultSet.");
            }

            RowSetFactory var13 = RowSetFactory.newInstance();
            var10 = var13.newCachedRowSet();
            CachedRowSetMetaData var14 = new CachedRowSetMetaData();
            var14.setColumnCount(var1.getResultColumnCount());
            var10.populate(var14);
            var10.moveToInsertRow();
         }

         Object[] var54;
         for(; var6.next() && !var1.maxElementsReached(var53, var10); this.processRelationshipCaching(var1, var54, var12)) {
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime(var1.getName() + "result set contains data");
            }

            var54 = var1.getBeans();
            Object[] var58 = var1.getPrimaryKey();

            try {
               this.processSQLRow(var6, var1, var54, var58);
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime(var1.getName() + " processed row successfully.");
               }
            } catch (Exception var45) {
               var1.releaseBeans(var54, 0);
               throw var45;
            }

            Object[] var15 = null;
            QueryCacheElement[] var16 = null;
            QueryCacheElement var17 = null;
            Object var18 = null;

            for(int var19 = 0; var19 < var54.length; ++var19) {
               Object var20 = null;
               BaseEntityManager var21 = var1.getManager(var19);
               if (var21 != null) {
                  CMPBean var22 = (CMPBean)var54[var19];
                  Object var23 = var58[var19];
                  RSInfoImpl var24 = new RSInfoImpl(var22, var23);
                  CMPBean var25 = null;

                  try {
                     if (runtimeLogger.isDebugEnabled()) {
                        debugRuntime(var1.getName() + " caching bean " + var23 + ".");
                     }

                     var25 = (CMPBean)var21.getBeanFromRS(var23, var24);
                     if (var25 != var22) {
                        if (runtimeLogger.isDebugEnabled()) {
                           debugRuntime(var1.getName() + " returning bean " + var23 + " to the pool.");
                        }

                        var21.releaseBeanToPool((EntityBean)var22);
                        var54[var19] = var25;
                     }
                  } catch (Exception var46) {
                     var1.releaseBeans(var54, var19);
                     throw var46;
                  }

                  try {
                     if (var21.isBeanClassAbstract()) {
                        var20 = var21.finderGetEoFromBeanOrPk((EntityBean)var25, var23, var3);
                     } else {
                        var20 = var25;
                     }

                     if (var1.isQueryCachingEnabled()) {
                        TTLManager var26 = (TTLManager)var21;
                        QueryCacheElement var27 = new QueryCacheElement(var23, var26);
                        var27.setIncludable(false);
                        var12.addQueryCachingEntry(var26, var27);
                        var17 = new QueryCacheElement(var23, var26);
                        var17.setInvalidatable(false);
                     }
                  } catch (Exception var41) {
                     var1.releaseBeans(var54, var19 + 1);
                     throw var41;
                  }
               } else {
                  var20 = var54[var19];
                  if (var1.isQueryCachingEnabled()) {
                     var17 = new QueryCacheElement(var54[var19]);
                  }
               }

               if (var53 != null) {
                  if (runtimeLogger.isDebugEnabled()) {
                     debugRuntime(var1.getName() + " adding to collection result...." + var20);
                  }

                  if (var54.length > 1 && !var1.usesRelationshipCaching()) {
                     if (var19 == 0) {
                        var15 = new Object[var54.length];
                        var53.add(var15);
                        if (var1.isQueryCachingEnabled()) {
                           var16 = new QueryCacheElement[var54.length];
                           var18 = new MultiValueQueryCacheElement(var16);
                        }
                     }

                     var15[var19] = var20;
                     if (var1.isQueryCachingEnabled()) {
                        var16[var19] = var17;
                     }
                  } else if (var19 == 0) {
                     var53.add(var20);
                     if (var1.isQueryCachingEnabled()) {
                        var18 = var17;
                     }
                  }
               } else if (var10 != null) {
                  if (runtimeLogger.isDebugEnabled()) {
                     debugRuntime(var1.getName() + " adding to ResultSet result. " + var20);
                  }

                  if (var19 == 0 || !var1.usesRelationshipCaching()) {
                     var10.updateObject(var19 + 1, var20);
                  }
               } else {
                  if (runtimeLogger.isDebugEnabled()) {
                     debugRuntime(var1.getName() + " adding to single result.");
                  }

                  if (var19 == 0) {
                     if (var52 != null) {
                        if (var21 == null) {
                           if (!var52.equals(var20)) {
                              throw new FinderException("Error in '" + var1.getName() + "'.  The finder returns a single value, " + "but multiple rows were returned by the query " + "from the database (" + var7[0] + ").");
                           }
                        } else if (var52 != var20) {
                           throw new FinderException("Error in '" + var1.getName() + "'.  The finder returns a single value, " + "but multiple rows were returned by the query " + "from the database (" + var7[0] + ").");
                        }
                     }

                     var52 = var20;
                     if (var1.isQueryCachingEnabled()) {
                        var18 = var17;
                     }
                  } else if (!var1.usesRelationshipCaching()) {
                     throw new FinderException("Error in '" + var1.getName() + "'.  The finder returns a single value, " + "but multiple values are selected for each row " + "in the database.  The selected values must be mapped " + "to a single bean or relationship caching must be " + "used.");
                  }
               }
            }

            var11.add(var58);
            if (var1.isQueryCachingEnabled()) {
               var12.addQueryCachingEntry((TTLManager)this.getBeanManager(), (QueryCacheElement)var18);
            }

            if (var10 != null) {
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime(var1.getName() + " adding row to ResultSet result.");
               }

               var10.insertRow();
            }
         }

         this.unpin(var11, var1);
         var12.putInQueryCache();
         if (var53 != null) {
            ArrayList var57 = var53;
            return var57;
         }

         if (var10 != null) {
            var10.moveToCurrentRow();
            WLCachedRowSet var56 = var10;
            return var56;
         }

         if (var11.size() == 0) {
            throw new ObjectNotFoundException("Bean not found in " + var1.getName() + ".");
         }

         var55 = var52;
      } catch (SQLException var47) {
         throw new FinderException("Exception in finder " + var1.getName() + " while using " + "result set: '" + var6 + "'" + "\n" + var47.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var47));
      } catch (ObjectNotFoundException var48) {
         throw var48;
      } catch (Exception var49) {
         throw new FinderException("Exception executing finder " + var1.getName() + " : " + "\n" + var49.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var49));
      } finally {
         this.releaseResources(var4, var5, var6);
      }

      return var55;
   }

   private void unpin(Collection var1, SqlFinder var2) {
      int var3 = var2.getColumnCount();
      Object var4 = EJBRuntimeUtils.getInvokeTxOrThread();
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         Object[] var6 = (Object[])((Object[])var5.next());

         for(int var7 = 0; var7 < var6.length; ++var7) {
            BaseEntityManager var8 = var2.getManager(var7);
            if (var8 != null) {
               var8.unpin(var4, var6[var7]);
            }
         }
      }

   }

   private ResultSet getResultSet(PreparedStatement var1, SqlFinder var2) throws SQLException {
      ResultSet var3 = null;
      if (var2.usesStoredFunction()) {
         CallableStatement var4 = (CallableStatement)var1;
         var4.execute();
         var3 = (ResultSet)var4.getObject(1);
      } else {
         var3 = var1.executeQuery();
      }

      return var3;
   }

   private PreparedStatement getStatement(Connection var1, SqlFinder var2, String[] var3) throws SQLException {
      Object var4 = null;
      int var5 = this.getSelectForUpdateValue();
      var3[0] = var2.getQuery(var5);
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime(var2.getName() + " got query: " + var3[0]);
      }

      if (!var2.usesStoredProcedure() && !var2.usesStoredFunction()) {
         var4 = var1.prepareStatement(var3[0]);
      } else {
         var4 = var1.prepareCall(var3[0]);
      }

      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime(var2.getName() + " got PreparedStatement.");
      }

      return (PreparedStatement)var4;
   }

   private void setParameters(PreparedStatement var1, SqlFinder var2, Object[] var3) throws SQLException {
      int var4 = 1;
      if (var2.usesStoredFunction()) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime(var2.getName() + " setting out parameter for stored function.");
         }

         CallableStatement var5 = (CallableStatement)var1;
         switch (this.databaseType) {
            case 1:
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime(var2.getName() + " setting output parameter for Oracle.");
               }

               var5.registerOutParameter(var4, -10);
               ++var4;
               break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            default:
               throw new EJBException("Attempt to use stored function in '" + var2.getName() + "'.  Stored functions are only supported for Oracle.");
         }
      }

      for(int var7 = 0; var7 < var2.getNumQueryParams(); ++var4) {
         int var6 = var2.getMethodIndex(var7);
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("------------------------------------" + var2.getName() + ": query parameter" + var4 + ": method parameter" + var6 + " bound with value :" + var3[var6]);
         }

         if (var3[var6] != null && var3[var6].getClass().equals(Character.class)) {
            var1.setString(var4, var3[var6].toString());
         } else {
            var1.setObject(var4, var3[var6]);
         }

         ++var7;
      }

      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime(var2.getName() + " done setting input parameters");
      }

   }

   private void processSQLRow(ResultSet var1, SqlFinder var2, Object[] var3, Object[] var4) throws SQLException, IOException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime(var2.getName() + " processing row: column count-" + var2.getColumnCount());
      }

      for(int var5 = 0; var5 < var2.getColumnCount(); ++var5) {
         int var6 = var2.getResultIndex(var5);
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime(var2.getName() + " results index- " + var6);
         }

         Object var7 = this.processSqlColumn(var2, var1, var5, (CMPBean)var3[var6]);
         if (var3[var6] == null) {
            var3[var6] = var7;
         } else if (var2.setsPrimaryKey(var5)) {
            if (var4[var6] == null) {
               var4[var6] = var7;
            } else {
               Field var8 = var2.getPrimaryKeyField(var5);
               var8.set(var4[var6], var7);
               Debug.assertion(var7 != null);
            }
         }
      }

   }

   private void processRelationshipCaching(SqlFinder var1, Object[] var2, QueryCachingHandler var3) {
      if (var1.usesRelationshipCaching()) {
         int var4 = var1.getRelationCount();
         int[] var5 = var1.getRelationIndex1();
         int[] var6 = var1.getRelationIndex2();
         Method[] var7 = var1.getRelationMethod1();
         Method[] var8 = var1.getRelationMethod2();

         for(int var9 = 0; var9 < var4; ++var9) {
            Object var10 = var2[var5[var9]];
            Object var11 = var2[var6[var9]];
            Object var12 = var10;
            Object var13 = var11;
            RDBMSBean var14 = var1.getRDBMSBean(var5[var9]);
            RDBMSBean var15 = var1.getRDBMSBean(var6[var9]);
            if (var15.getCMPBeanDescriptor().isBeanClassAbstract()) {
               var13 = ((EntityEJBContextImpl)((CMPBean)var11).__WL_getEntityContext()).__WL_getEJBLocalObject();
            }

            try {
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("------------------setting relationship between " + var14.getEjbName() + " and " + var15.getEjbName());
                  debugRuntime("------------------ primary key " + ((CMPBean)var10).__WL_getPrimaryKey());
                  debugRuntime("------------------ primary key " + ((CMPBean)var11).__WL_getPrimaryKey());
               }

               var7[var9].invoke(var10, var13);
            } catch (Exception var23) {
               throw new AssertionError("exception while invoking method-" + DDUtils.getMethodSignature(var7[var9]), var23);
            }

            if (var14.getCMPBeanDescriptor().isBeanClassAbstract()) {
               var12 = ((EntityEJBContextImpl)((CMPBean)var10).__WL_getEntityContext()).__WL_getEJBLocalObject();
            }

            try {
               var8[var9].invoke(var11, var12);
            } catch (Exception var22) {
               throw new AssertionError("exception while invoking method-" + DDUtils.getMethodSignature(var8[var9]), var22);
            }

            String var16;
            Object[] var17;
            TTLManager var18;
            Object var19;
            QueryCacheKey var20;
            QueryCacheElement var21;
            if (var1.getCmrFieldFinderMethodName1(var9) != null) {
               var16 = var1.getCmrFieldFinderMethodName1(var9);
               var17 = new Object[]{((CMPBean)var10).__WL_getPrimaryKey()};
               var18 = (TTLManager)var1.getManager(var6[var9]);
               var19 = ((CMPBean)var11).__WL_getPrimaryKey();
               var20 = new QueryCacheKey(var16, var17, var18, var1.getCmrFieldFinderReturnType1(var9));
               var21 = new QueryCacheElement(var19, var18);
               var3.addQueryCachingEntry(var18, var20, var21);
            }

            if (var1.getCmrFieldFinderMethodName2(var9) != null) {
               var16 = var1.getCmrFieldFinderMethodName2(var9);
               var17 = new Object[]{((CMPBean)var11).__WL_getPrimaryKey()};
               var18 = (TTLManager)var1.getManager(var5[var9]);
               var19 = ((CMPBean)var10).__WL_getPrimaryKey();
               var20 = new QueryCacheKey(var16, var17, var18, var1.getCmrFieldFinderReturnType2(var9));
               var21 = new QueryCacheElement(var19, var18);
               var3.addQueryCachingEntry(var18, var20, var21);
            }
         }

      }
   }

   private Object processSqlColumn(SqlFinder var1, ResultSet var2, int var3, CMPBean var4) throws SQLException, IOException, IllegalAccessException, ClassNotFoundException {
      Class var5 = var1.getColumnClass(var3);
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime(var1.getName() + " targetClass-" + var5);
      }

      int var6 = var3 + 1;
      Object var7 = null;
      byte[] var8 = null;
      String var9;
      if (var5.isPrimitive()) {
         if (var5 == Boolean.TYPE) {
            var7 = new Boolean(var2.getBoolean(var6));
         } else if (var5 == Byte.TYPE) {
            var7 = new Byte(var2.getByte(var6));
         } else if (var5 == Character.TYPE) {
            var9 = var2.getString(var6);
            if (!var2.wasNull() && var9 != null && var9.length() != 0) {
               var7 = new Character(var9.charAt(0));
            } else {
               var7 = new Character('\u0000');
            }
         } else if (var5 == Short.TYPE) {
            var7 = new Short(var2.getShort(var6));
         } else if (var5 == Integer.TYPE) {
            var7 = new Integer(var2.getInt(var6));
         } else if (var5 == Long.TYPE) {
            var7 = new Long(var2.getLong(var6));
         } else if (var5 == Float.TYPE) {
            var7 = new Float(var2.getFloat(var6));
         } else if (var5 == Double.TYPE) {
            var7 = new Double(var2.getDouble(var6));
         }
      } else if (var5 == String.class) {
         if (var1.isClobColumn(var3)) {
            var7 = this.processClobColumn(var5, var6, var2);
         } else {
            var7 = var2.getString(var6);
            if (var2.wasNull()) {
               var7 = null;
            }
         }
      } else if (var5 == BigDecimal.class) {
         var7 = var2.getBigDecimal(var6);
         if (var2.wasNull()) {
            var7 = null;
         }
      } else if (var5 == Boolean.class) {
         boolean var12 = var2.getBoolean(var6);
         if (var2.wasNull()) {
            var7 = null;
         } else {
            var7 = new Boolean(var12);
         }
      } else {
         int var13;
         if (var5 == Byte.class) {
            var13 = var2.getByte(var6);
            if (var2.wasNull()) {
               var7 = null;
            } else {
               var7 = new Byte((byte)var13);
            }
         } else if (var5 == byteArray.getClass()) {
            if (var1.isBlobColumn(var3)) {
               var7 = this.processBlobColumn(var5, var6, var2);
            } else {
               var7 = var2.getBytes(var6);
               if (var2.wasNull()) {
                  var7 = null;
               }
            }
         } else if (var5 == Character.class) {
            var9 = var2.getString(var6);
            if (!var2.wasNull() && var9 != null && var9.length() != 0) {
               var7 = new Character(var9.charAt(0));
            } else {
               var7 = null;
            }
         } else if (var5 == Date.class) {
            var7 = var2.getDate(var6);
            if (var2.wasNull()) {
               var7 = null;
            }
         } else if (var5 == Double.class) {
            double var14 = var2.getDouble(var6);
            if (var2.wasNull()) {
               var7 = null;
            } else {
               var7 = new Double(var14);
            }
         } else if (var5 == Float.class) {
            float var15 = var2.getFloat(var6);
            if (var2.wasNull()) {
               var7 = null;
            } else {
               var7 = new Float(var15);
            }
         } else if (var5 == Integer.class) {
            var13 = var2.getInt(var6);
            if (var2.wasNull()) {
               var7 = null;
            } else {
               var7 = new Integer(var13);
            }
         } else if (var5 == Long.class) {
            long var16 = var2.getLong(var6);
            if (var2.wasNull()) {
               var7 = null;
            } else {
               var7 = new Long(var16);
            }
         } else if (var5 == Short.class) {
            short var17 = var2.getShort(var6);
            if (var2.wasNull()) {
               var7 = null;
            } else {
               var7 = new Short(var17);
            }
         } else if (var5 == Time.class) {
            var7 = var2.getTime(var6);
            if (var2.wasNull()) {
               var7 = null;
            }
         } else if (var5 == java.util.Date.class) {
            Timestamp var18 = var2.getTimestamp(var6);
            if (!var2.wasNull() && var18 != null) {
               var7 = new java.util.Date(var18.getTime());
            } else {
               var7 = null;
            }
         } else if (var1.isCharArrayMappedToString(var5)) {
            if (var1.isClobColumn(var3)) {
               var7 = this.processClobColumn(var5, var6, var2);
            } else {
               var9 = var2.getString(var6);
               if (!var2.wasNull() && var9 != null) {
                  var7 = var9.toCharArray();
               } else {
                  var7 = null;
               }
            }
         } else if (var4 == null) {
            var7 = var2.getObject(var6);
            if (var2.wasNull()) {
               var7 = null;
            }
         } else if (var1.isBlobColumn(var3)) {
            var7 = this.processBlobColumn(var5, var6, var2);
         } else {
            var8 = var2.getBytes(var6);
            if (!var2.wasNull() && var8 != null && var8.length != 0) {
               ByteArrayInputStream var19 = new ByteArrayInputStream(var8);
               RDBMSObjectInputStream var10 = new RDBMSObjectInputStream(var19, this.classLoader);
               var7 = var10.readObject();
            } else {
               var7 = null;
               var8 = null;
            }
         }
      }

      if (runtimeLogger.isDebugEnabled()) {
         if (var7 != null) {
            debugRuntime(var1.getName() + " processing column-" + var3 + " value- " + var7 + "type- " + var7.getClass().getName());
         } else {
            debugRuntime(var1.getName() + " processing column-" + var3 + " value- null ");
         }
      }

      if (var4 != null) {
         Field var20;
         if (var1.hasField(var3)) {
            var20 = var1.getField(var3);
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime(var1.getName() + " setting field: field- " + var20.getName() + " bean- " + var4.getClass().getName());
            }

            var20.set(var4, var7);
         } else {
            Method var21 = var1.getMethod(var3);

            try {
               var21.invoke(var4, var7);
            } catch (InvocationTargetException var11) {
               throw new AssertionError("exception while invoking method-" + var21);
            }
         }

         if (var1.isOptimistic(var3)) {
            var20 = var1.getOptimisticField(var3);
            if (var8 == null) {
               if (java.util.Date.class.isAssignableFrom(var5) && var7 != null) {
                  var20.set(var4, ((java.util.Date)var7).clone());
               } else if (var5 == byteArray.getClass() && var7 != null) {
                  var20.set(var4, ((byte[])((byte[])var7)).clone());
               } else if (var5 == charArray.getClass() && var7 != null) {
                  var20.set(var4, ((char[])((char[])var7)).clone());
               } else {
                  var20.set(var4, var7);
               }
            } else {
               var20.set(var4, var8);
            }
         }

         var4.__WL_setLoaded(var1.getIsLoadedIndex(var3), true);
      }

      return var7;
   }

   private Object processBlobColumn(Class var1, int var2, ResultSet var3) throws SQLException, IOException, ClassNotFoundException {
      Object var4 = null;

      try {
         Blob var5 = var3.getBlob(var2);
         if (var5 == null) {
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime("got NULL Blob, set result to null.");
            }
         } else {
            int var6 = (int)var5.length();
            if (var6 == 0) {
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("got zero length Blob");
               }

               if (byteArray.getClass().equals(var1)) {
                  var4 = new byte[0];
               }
            } else {
               byte[] var7 = new byte[var6];
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("got: " + var6 + " length Blob, now read data.");
               }

               InputStream var8 = var5.getBinaryStream();
               var8.read(var7);
               var8.close();
               if (byteArray.getClass().equals(var1)) {
                  var4 = var7;
               } else {
                  ByteArrayInputStream var9 = new ByteArrayInputStream(var7, 0, var6);
                  ObjectInputStream var10 = new ObjectInputStream(var9);

                  try {
                     var4 = var10.readObject();
                  } catch (ClassNotFoundException var18) {
                     if (runtimeLogger.isDebugEnabled()) {
                        debugRuntime("ClassNotFoundException for Blob" + var18.getMessage());
                     }

                     throw var18;
                  } finally {
                     var9.close();
                     var10.close();
                  }
               }
            }
         }

         return var4;
      } catch (IOException var20) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("IOException for Blob" + var20.getMessage());
         }

         throw var20;
      } catch (SQLException var21) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("SQLException for Blob" + var21.getMessage());
         }

         throw var21;
      }
   }

   private Object processClobColumn(Class var1, int var2, ResultSet var3) throws SQLException, IOException, ClassNotFoundException {
      Object var4 = null;

      try {
         Clob var5 = var3.getClob(var2);
         if (var5 == null) {
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime("got NULL Clob, set result to null.");
            }
         } else {
            int var6 = (int)var5.length();
            if (var6 == 0) {
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("got zero length Clob.");
               }

               if (var1 == String.class) {
                  var4 = new String("");
               } else {
                  var4 = new char[0];
               }
            } else {
               char[] var7 = new char[var6];
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("got: " + var6 + " length Clob, now read data.");
               }

               Reader var8 = var5.getCharacterStream();
               var8.read(var7);
               var8.close();
               if (var1 == String.class) {
                  var4 = new String(var7);
               } else {
                  var4 = var7;
               }
            }
         }

         return var4;
      } catch (IOException var9) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("IOException for Blob/Clob" + var9.getMessage());
         }

         throw var9;
      } catch (SQLException var10) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("SQLException for Blob/Clob" + var10.getMessage());
         }

         throw var10;
      }
   }

   public Map collectionFinderLoadBean(EntityBean var1, Method var2, Object[] var3) throws Throwable {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("RDBMSPersistenceManager.collectionFinderLoadBean");
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
         if (var2.usesCmpBean()) {
            CMPBean var3 = (CMPBean)var1;
            CMPBean var4 = var2.getCmpBean();
            var3.__WL_copyFrom(var4, true);
         } else {
            ((CMPBean)var1).__WL_loadGroupByIndex(var2.getGroupIndex(), var2.getRS(), var2.getOffset(), var2.getPK(), var1);
            if (var2.getCmrField() != null) {
               ((CMPBean)var1).__WL_loadCMRFieldByCmrField(var2.getCmrField(), var2.getRS(), var2.getCmrFieldOffset(), var1);
            }
         }
      } catch (Exception var5) {
         EJBRuntimeUtils.throwInternalException("Error load bean states from ResultSet", var5);
      }

   }

   public void updateClassLoader(ClassLoader var1) {
      this.classLoader = var1;
   }

   public void setRdbmsBean(RDBMSBean var1) {
      assert var1 != null;

      assert var1.getDataSourceName() != null : "No data source set for this RDBMS bean.";

      assert var1.getTableName() != null : "No table name set for this RDBMS bean.";

      this.rbean = var1;
   }

   public void cleanup() {
      if (deploymentLogger.isDebugEnabled()) {
         debugDeployment("**************************************cleanup()- " + this.ejbName);
      }

      assert !((EntityBeanInfo)this.beanManager.getEJBHome().getBeanInfo()).isDynamicQueriesEnabled();

      this.rbean.cleanup();
   }

   private RDBMSBean rbean() {
      if (this.rbean == null) {
         throw new AssertionError("Internal error: RDBMSBean is null in RDBMSPersistenceManager()");
      } else {
         return this.rbean;
      }
   }

   public boolean getVerifyReads() {
      return this.rbean().getVerifyReads();
   }

   public void setupParentBeanManagers() {
      Iterator var1 = this.rbean().getForeignKeyFieldNames().iterator();

      while(true) {
         String var2;
         do {
            if (!var1.hasNext()) {
               return;
            }

            var2 = (String)var1.next();
         } while(this.rbean.isManyToManyRelation(var2));

         RDBMSBean var3 = this.rbean().getRelatedRDBMSBean(var2);
         boolean var4 = true;
         BaseEntityManager var5 = var3.getRDBMSPersistenceManager().getBeanManager();
         if (!this.isSelfRelationship(var2)) {
            if (deploymentLogger.isDebugEnabled()) {
               debugDeployment(this.rbean.getEjbName() + ": adding " + var3.getEjbName() + " to its parentBeanManager");
            }

            this.beanManager.addParentBeanManager(var5);
            var5.addChildBeanManager(this.beanManager);
         }

         List var6 = this.rbean().getForeignKeyColNames(var2);
         String var7 = this.rbean().getTableForCmrField(var2);

         String var10;
         for(Iterator var8 = var6.iterator(); var8.hasNext(); var4 &= (Boolean)this.variable2nullable.get(var10)) {
            String var9 = (String)var8.next();
            var10 = this.rbean().getVariable(var7, var9);
         }

         this.fkFieldNullableMap.put(var2, new Boolean(var4));
         this.fkColsNullable &= var4;
         if (!this.isSelfRelationship(var2) && !var4) {
            if (deploymentLogger.isDebugEnabled()) {
               debugDeployment(this.rbean.getEjbName() + ": adding " + var3.getEjbName() + " to its notNullableParentBeanManager");
            }

            this.beanManager.addNotNullableParentBeanManager(var5);
            var5.addNotNullableChildBeanManager(this.beanManager);
         }
      }
   }

   public boolean isSelfRelationship(String var1) {
      return this.rbean().isSelfRelationship(var1);
   }

   public boolean isSelfRelationship() {
      return this.rbean().isSelfRelationship();
   }

   public boolean isFkColsNullable(String var1) {
      return (Boolean)this.fkFieldNullableMap.get(var1);
   }

   public boolean isFkColsNullable() {
      return this.fkColsNullable;
   }

   public void setCycleExists() {
      HashSet var1 = new HashSet();
      this.getBeanManager().setCycleExists(var1);
   }

   public void setupM2NBeanManagers() {
      Iterator var1 = this.rbean().getAllCmrFields().iterator();

      while(true) {
         while(true) {
            String var2;
            do {
               do {
                  if (!var1.hasNext()) {
                     return;
                  }

                  var2 = (String)var1.next();
               } while(!this.rbean().isDeclaredField(var2));
            } while(!this.rbean().isManyToManyRelation(var2));

            RDBMSBean var3 = this.rbean.getRelatedRDBMSBean(var2);
            String var4 = var3.getEjbName();
            BaseEntityManager var5 = var3.getRDBMSPersistenceManager().getBeanManager();
            String var6 = this.rbean.getEjbName();
            if (this.rbean().isSymmetricField(var2)) {
               if (deploymentLogger.isDebugEnabled()) {
                  debugDeployment("  adding M2N Bean Manager " + this.rbean.getEjbName() + " to exec delayed INSERTS for symmetric cmrf: " + var2);
               }

               this.beanManager.addM2NInsertSet(var2);
            } else if (this.rbean().equals(var3) && this.rbean().isBiDirectional(var2)) {
               String var7 = this.rbean().getRelatedFieldName(var2);
               if (!this.beanManager.isM2NInsertSet(var2) && !this.beanManager.isM2NInsertSet(var7)) {
                  if (deploymentLogger.isDebugEnabled()) {
                     debugDeployment("  adding M2N Bean Manager " + this.rbean.getEjbName() + " to exec delayed INSERTS for self-reflected" + " none-symmetric cmrf: " + var2);
                  }

                  this.beanManager.addM2NInsertSet(var2);
               }
            } else if (!this.rbean().isBiDirectional(var2)) {
               if (deploymentLogger.isDebugEnabled()) {
                  debugDeployment("  adding M2N Bean Manager '" + this.rbean().getEjbName() + "' to exec delayed INSERTS forunidirectional cmrf: '" + var2 + "', parent bean manager is: '" + var4 + "'");
               }

               this.beanManager.addM2NInsertSet(var2);
               this.beanManager.addParentBeanManager(var5);
               var5.addChildBeanManager(this.beanManager);
            } else if (var6.compareTo(var4) <= 0) {
               if (deploymentLogger.isDebugEnabled()) {
                  debugDeployment("  adding M2N Bean Manager " + var6 + " to exec delayed INSERTS for bidirectional cmrf: " + var2 + ", parent bean manager is: '" + var4 + "'");
               }

               this.beanManager.addM2NInsertSet(var2);
               this.beanManager.addParentBeanManager(var5);
               var5.addChildBeanManager(this.beanManager);
            } else if (deploymentLogger.isDebugEnabled()) {
               debugDeployment("  " + var6 + "  will defer to other M2N Bean Manager " + var4 + " to exec delayed INSERTS for cmrf: " + var2);
            }
         }
      }
   }

   private String getCreateDefaultDBMSTables() {
      return this.rbean().getCreateDefaultDBMSTables();
   }

   private String getValidateDbSchemaWith() {
      return this.rbean().getValidateDbSchemaWith();
   }

   private String getDefaultDbmsTablesDdl() {
      return this.rbean().getDefaultDbmsTablesDdl();
   }

   private void populateVerifyRows() {
      if (this.isOptimistic && this.rbean().getVerifyReads() || this.needsBatchOperationsWorkaround()) {
         if (!this.selectForUpdateSupported) {
            EJBLogger.logAnomalousRRBehaviorPossible(this.ejbName);
         }

         this.verifyText = new String[this.rbean().tableCount()];
         this.verifyTextWithXLock = new String[this.rbean().tableCount()];
         this.verifyCount = new int[this.rbean().tableCount()];
         this.verifyCur = new int[this.rbean().tableCount()];

         int var1;
         for(var1 = 0; var1 < this.rbean().tableCount(); ++var1) {
            String var2 = this.rbean().tableAt(var1);
            this.verifyText[var1] = "SELECT 7 FROM " + var2;
            this.verifyTextWithXLock[var1] = this.verifyText[var1];
            StringBuilder var10000;
            String[] var10002;
            if (this.selectForUpdateSupported) {
               switch (this.databaseType) {
                  case 2:
                  case 7:
                     var10000 = new StringBuilder();
                     var10002 = this.verifyTextWithXLock;
                     var10002[var1] = var10000.append(var10002[var1]).append(" WITH(UPDLOCK) ").toString();
                     break;
                  case 5:
                     var10000 = new StringBuilder();
                     var10002 = this.verifyTextWithXLock;
                     var10002[var1] = var10000.append(var10002[var1]).append(" HOLDLOCK ").toString();
               }
            }

            var10000 = new StringBuilder();
            var10002 = this.verifyText;
            var10002[var1] = var10000.append(var10002[var1]).append(" WHERE ").toString();
            var10000 = new StringBuilder();
            var10002 = this.verifyTextWithXLock;
            var10002[var1] = var10000.append(var10002[var1]).append(" WHERE ").toString();
            this.verifyCount[var1] = 0;
            this.verifyCur[var1] = 1;
         }

         if (deploymentLogger.isDebugEnabled()) {
            for(var1 = 0; var1 < this.verifyText.length; ++var1) {
               debugDeployment("verifyText[" + var1 + "]: " + this.verifyText[var1]);
               debugDeployment("verifyTextWithXLock[" + var1 + "]: " + this.verifyTextWithXLock[var1]);
            }
         }
      }

   }

   private void populateSnapShotStrings() {
      List var1 = this.rbean().getCmpFieldNames();
      List var2 = this.rbean().getForeignKeyFieldNames();
      if (this.rbean().getCMPBeanDescriptor().isOptimistic()) {
         this.snapshotStrings = new String[var1.size() + var2.size()];
         this.nullSnapshotStrings = new String[var1.size() + var2.size()];
      }

      Iterator var3 = var1.iterator();

      int var4;
      String var5;
      for(var4 = 0; var3.hasNext(); ++var4) {
         var5 = (String)var3.next();
         String var6 = this.rbean().getCmpColumnForField(var5);
         if (this.rbean().getCMPBeanDescriptor().isOptimistic()) {
            this.snapshotStrings[var4] = var6 + " = ?";
            this.nullSnapshotStrings[var4] = var6 + " is null";
         }
      }

      var3 = var2.iterator();

      while(true) {
         do {
            do {
               if (!var3.hasNext()) {
                  return;
               }

               var5 = (String)var3.next();
            } while(!this.rbean().containsFkField(var5));
         } while(this.rbean().isForeignCmpField(var5));

         Iterator var8 = this.rbean().getForeignKeyColNames(var5).iterator();
         if (this.rbean().getCMPBeanDescriptor().isOptimistic()) {
            this.snapshotStrings[var4] = "";
            this.nullSnapshotStrings[var4] = "";
         }

         while(var8.hasNext()) {
            String var7 = (String)var8.next();
            if (this.rbean().getCMPBeanDescriptor().isOptimistic()) {
               StringBuilder var10000 = new StringBuilder();
               String[] var10002 = this.snapshotStrings;
               var10002[var4] = var10000.append(var10002[var4]).append(var7).append(" = ?").toString();
               var10000 = new StringBuilder();
               var10002 = this.nullSnapshotStrings;
               var10002[var4] = var10000.append(var10002[var4]).append(var7).append(" is null").toString();
               if (var8.hasNext()) {
                  var10000 = new StringBuilder();
                  var10002 = this.snapshotStrings;
                  var10002[var4] = var10000.append(var10002[var4]).append(" AND ").toString();
                  var10000 = new StringBuilder();
                  var10002 = this.nullSnapshotStrings;
                  var10002[var4] = var10000.append(var10002[var4]).append(" AND ").toString();
               }
            }
         }

         ++var4;
      }
   }

   private void computeVariablesAndColumns(String var1, List var2, List var3) {
      Map var4 = this.rbean().getCmpField2ColumnMap(var1);
      String var7;
      if (var4 != null) {
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            var7 = (String)var4.get(var6);

            assert var7 != null;

            var2.add(var6);
            var3.add(var7);
         }
      }

      List var11 = this.rbean().getCmrFields(var1);
      if (var11 != null) {
         Iterator var12 = var11.iterator();

         while(var12.hasNext()) {
            var7 = (String)var12.next();
            Iterator var8 = this.rbean().getForeignKeyColNames(var7).iterator();

            while(var8.hasNext()) {
               String var9 = (String)var8.next();
               String var10 = this.rbean().variableForField(var7, var1, var9);
               if (!var2.contains(var10)) {
                  var2.add(var10);
               }

               if (!var3.contains(var9)) {
                  var3.add(var9);
               }
            }
         }
      }

   }

   private List getJoinTableColumns(String var1) {
      if (!this.rbean().isJoinTable(var1)) {
         throw new AssertionError(" Bean: '" + this.ejbName + "', passed in table name: '" + var1 + "'.  We were expecting a JoinTable " + "but apparently this isn't a JoinTable.");
      } else {
         ArrayList var2 = new ArrayList();
         String var3 = this.rbean().getCmrFieldForJoinTable(var1);
         Map var4 = this.rbean().getFkColumn2ClassMapForFkField(var3);
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            var2.add(var5.next());
         }

         if (this.rbean().isRemoteField(var3)) {
            String var6 = this.rbean().getRemoteColumn(var3);
            var2.add(var6);
         } else {
            String var7;
            if (this.rbean().isSymmetricField(var3)) {
               Map var9 = this.rbean().getSymmetricColumn2FieldName(var3);
               var5 = var9.keySet().iterator();

               while(var5.hasNext()) {
                  var7 = (String)var5.next();
                  var2.add(var7);
               }
            } else {
               RDBMSBean var10 = this.rbean().getRelatedRDBMSBean(var3);
               var7 = this.rbean().getRelatedFieldName(var3);
               var4 = var10.getFkColumn2ClassMapForFkField(var7);
               var5 = var4.keySet().iterator();

               while(var5.hasNext()) {
                  String var8 = (String)var5.next();
                  var2.add(var8);
               }
            }
         }

         return var2;
      }
   }

   private void populateFieldSQLTypeMap() throws WLDeploymentException {
      if (deploymentLogger.isDebugEnabled()) {
         debugDeployment("RDBMSPersistenceManager.populateFieldSQLTypeMap");
      }

      if (this.variable2SQLType.size() <= 0) {
         Connection var1 = null;

         try {
            Loggable var3;
            try {
               var1 = this.getConnection();
               Iterator var2 = this.rbean().getTables().iterator();

               while(var2.hasNext()) {
                  String var12 = (String)var2.next();
                  var12 = RDBMSUtils.escQuotedID(var12);
                  if (deploymentLogger.isDebugEnabled()) {
                     debugDeployment(" populateFieldSQLTypeMap call verify on Table: '" + var12 + "'");
                  }

                  ArrayList var4 = new ArrayList();
                  ArrayList var5 = new ArrayList();
                  this.rbean.computeVariablesAndColumns(var12, var4, var5, (Map)null);
                  this.verifier.verifyOrCreateOrAlterTable(this, var1, var12, var5, true, var4, this.variable2SQLType, this.variable2nullable, (String)null, false);
               }

               if (this.variable2SQLType.size() <= 0) {
                  var3 = EJBLogger.logCouldNotInitializeFieldSQLTypeMapWithoutExceptionLoggable();
                  throw new WLDeploymentException(var3.getMessage());
               }
            } catch (Exception var10) {
               var3 = EJBLogger.logCouldNotInitializeFieldSQLTypeMapLoggable(var10);
               throw new WLDeploymentException(var3.getMessage(), var10);
            }
         } finally {
            this.releaseResources(var1, (Statement)null, (ResultSet)null);
         }

      }
   }

   private void verifyTablesExist() throws WLDeploymentException {
      Connection var1 = null;

      try {
         var1 = this.getConnection();
         Iterator var2 = this.rbean().getTables().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var3 = RDBMSUtils.escQuotedID(var3);
            if (deploymentLogger.isDebugEnabled()) {
               debugDeployment(" verifyTablesExist call verify on Main Table: '" + var3 + "'");
            }

            ArrayList var4 = new ArrayList();
            ArrayList var5 = new ArrayList();
            this.rbean.computeVariablesAndColumns(var3, var4, var5, (Map)null);
            boolean var6 = this.rbean().getTriggerUpdatesOptimisticColumn(var3);
            this.verifier.verifyOrCreateOrAlterTable(this, var1, var3, var5, true, var4, this.variable2SQLType, this.variable2nullable, this.rbean().getCreateDefaultDBMSTables(), var6);
         }

         String var16;
         for(Iterator var15 = this.rbean().getJoinTableMap().values().iterator(); var15.hasNext(); this.verifier.verifyOrCreateOrAlterTable(this, var1, var16, this.getJoinTableColumns(var16), false, new ArrayList(), (Map)null, (Map)null, this.rbean().getCreateDefaultDBMSTables(), false)) {
            var16 = (String)var15.next();
            if (deploymentLogger.isDebugEnabled()) {
               debugDeployment(" verifyTablesExist call verify on Join Table: '" + var16 + "'");
            }
         }

      } catch (WLDeploymentException var12) {
         throw var12;
      } catch (Exception var13) {
         EJBLogger.logStackTraceAndMessage(var13.getMessage(), var13);
         throw new WLDeploymentException(var13.getMessage(), var13);
      } finally {
         this.releaseResources(var1, (Statement)null, (ResultSet)null);
      }
   }

   private boolean verifySelectForUpdateSupported() {
      Connection var1 = null;
      Statement var2 = null;
      boolean var3 = false;

      try {
         var1 = this.getConnection();
         var2 = var1.createStatement();
         switch (this.databaseType) {
            case 1:
               var2.executeQuery("SELECT 7 FROM " + this.rbean().getTableName() + " WHERE ROWNUM < 1 FOR UPDATE");
               break;
            case 2:
            case 7:
               var2.executeQuery("SELECT 7 FROM " + this.rbean().getTableName() + " WITH(UPDLOCK) WHERE (1=0)");
               break;
            case 3:
            case 6:
            case 8:
            default:
               var2.executeQuery("SELECT 7 FROM " + this.rbean().getTableName() + " WHERE (1=0) FOR UPDATE");
               break;
            case 4:
            case 9:
               if (!this.rbean.getUseSelectForUpdate() && !this.isOptimistic) {
                  boolean var12 = false;
                  return var12;
               }

               Iterator var4 = this.rbean().getCMPBeanDescriptor().getPrimaryKeyFieldNames().iterator();
               String var5 = this.rbean().getColumnForCmpFieldAndTable((String)var4.next(), this.rbean().getTableName()) + " is null";
               var2.executeQuery("SELECT 7 FROM " + this.rbean().getTableName() + " WHERE " + var5 + " FOR UPDATE");
               break;
            case 5:
               var2.executeQuery("SELECT 7 FROM " + this.rbean().getTableName() + " HOLDLOCK WHERE (1=0)");
         }

         var3 = true;
      } catch (SQLException var10) {
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("Exception while verifying select for update support: " + var10.getMessage());
         }
      } finally {
         this.releaseResources(var1, var2, (ResultSet)null);
      }

      return var3;
   }

   private void genKeySetup() throws WLDeploymentException {
      if (this.rbean().hasAutoKeyGeneration()) {
         this.genKeyPKFieldClassType = this.rbean().getGenKeyPKFieldClassType();
         this.genKeyWLGeneratorQuery = this.rbean().getGenKeyGeneratorQuery();
         Connection var1;
         Loggable var3;
         if (this.genKeyType == 2) {
            this.genKeyGeneratorName = this.rbean().getGenKeyGeneratorName();
            this.genKeyCacheSize = this.rbean().getGenKeyCacheSize();
            this.genKeyCurrCacheSize = 0;
            var1 = null;

            try {
               var1 = this.getConnection();
               this.genKeyGeneratorName = this.verifier.verifyOrCreateOrAlterSequence(var1, this.genKeyGeneratorName, this.genKeyCacheSize, this.rbean().getCreateDefaultDBMSTables(), this.databaseType);
               if (deploymentLogger.isDebugEnabled()) {
                  debugDeployment("RDBMSPersistenceManager will use sequence: '" + this.genKeyGeneratorName + "'");
               }

               switch (this.databaseType) {
                  case 1:
                     this.genKeyWLGeneratorQuery = this.rbean.getOracleSequenceGeneratorQuery(this.genKeyGeneratorName);
                     break;
                  case 2:
                  default:
                     throw new AssertionError("Database Type: " + DDConstants.getDBNameForType(this.databaseType) + " does not support the SEQUENCE key generator");
                  case 3:
                     this.genKeyWLGeneratorQuery = this.rbean.getInformixSequenceGeneratorQuery(this.genKeyGeneratorName);
                     break;
                  case 4:
                     this.genKeyWLGeneratorQuery = this.rbean.getDB2SequenceGeneratorQuery(this.genKeyGeneratorName);
               }
            } catch (Exception var22) {
               var3 = EJBLogger.logSequenceSetupFailureLoggable(this.genKeyGeneratorName, Integer.toString(this.genKeyCacheSize), var22.getMessage());
               throw new WLDeploymentException(var3.getMessage(), var22);
            } finally {
               this.releaseResources(var1, (Statement)null, (ResultSet)null);
            }
         } else if (this.genKeyType != 1 && this.genKeyType == 3) {
            this.genKeyCacheSize = this.rbean().getGenKeyCacheSize();
            this.genKeyWLGeneratorUpdatePrefix = this.rbean().getGenKeyGeneratorUpdatePrefix();
            this.genKeyWLGeneratorUpdate = this.genKeyWLGeneratorUpdatePrefix + this.genKeyCacheSize;
            this.genKeyGeneratorName = this.rbean().getGenKeyGeneratorName();
            this.selectFirstSeqKeyBeforeUpdate = this.rbean().getSelectFirstSeqKeyBeforeUpdate();
            var1 = null;
            Statement var2 = null;
            var3 = null;
            weblogic.transaction.TransactionManager var4 = null;

            try {
               Loggable var6;
               try {
                  if (TxHelper.getTransaction() == null) {
                     var4 = TxHelper.getTransactionManager();
                     var4.setTransactionTimeout(60);
                     var4.begin();
                  }

                  var1 = this.getConnection();
                  int var5 = this.verifier.checkTableAndColumns(this, var1, this.genKeyGeneratorName, new String[]{"SEQUENCE"}, false, (List)null, (Map)null, (Map)null);
                  if (var5 == 0 && !this.getCreateDefaultDBMSTables().equalsIgnoreCase("Disabled")) {
                     ArrayList var25 = new ArrayList();
                     var25.add("SEQUENCE");
                     this.verifier.verifyOrCreateOrAlterTable(this, var1, this.genKeyGeneratorName, var25, false, (List)null, (Map)null, (Map)null, this.getCreateDefaultDBMSTables(), false);
                     var2 = var1.createStatement();
                     var2.executeUpdate("INSERT INTO " + this.genKeyGeneratorName + " (SEQUENCE) VALUES (0)");
                     var2.close();
                     if (var4 != null) {
                        var4.commit();
                     }
                  }

                  var2 = var1.createStatement();
                  ResultSet var24 = var2.executeQuery(this.genKeyWLGeneratorQuery);
                  if (!var24.next()) {
                     var24.close();
                     var2.close();
                     var6 = EJBLogger.logGenKeySequenceTableEmptyLoggable(this.genKeyGeneratorName);
                     throw new WLDeploymentException(var6.getMessage());
                  }
               } catch (Exception var20) {
                  try {
                     if (var4 != null) {
                        var4.rollback();
                     }
                  } catch (Exception var19) {
                  }

                  var6 = EJBLogger.logGenKeySequenceTableSetupFailureLoggable(this.genKeyGeneratorName + "  " + var20.getMessage());
                  throw new WLDeploymentException(var6.getMessage(), var20);
               }
            } finally {
               this.releaseResources(var1, (Statement)null, (ResultSet)null);
            }
         }

      }
   }

   private Set createPrimaryKeyCols(String var1) {
      Map var2 = this.rbean().getCmpField2ColumnMap(var1);
      if (var2 == null) {
         return new ArraySet();
      } else {
         List var3 = this.rbean().getPrimaryKeyFields();
         Iterator var4 = var2.keySet().iterator();
         ArraySet var5 = new ArraySet();

         while(var4.hasNext()) {
            String var6 = (String)var4.next();
            String var7 = (String)var2.get(var6);
            if (var3.contains(var6)) {
               var5.add(var7);
            }
         }

         return var5;
      }
   }

   private String getSqltypeForCol(String var1, String var2) throws WLDeploymentException {
      String var3 = this.rbean().getCmpField(var1, var2);
      Class var4 = null;
      if (var3 != null) {
         var4 = this.rbean().getCmpFieldClass(var3);
      } else {
         var4 = this.rbean().getJavaClassTypeForFkCol(var1, var2);
         if (var4 == null) {
            RDBMSBean var5 = this.rbean().getRelatedBean(var1, var2);
            if (var5 == null) {
               throw new WLDeploymentException(" Bean: " + this.rbean().getEjbName() + ", could not get Column To Field Map for column " + var2);
            }

            var4 = var5.getJavaClassTypeForFkCol(var1, var2);
         }
      }

      if (null == var4) {
         throw new WLDeploymentException(" Bean: " + this.ejbName + ", could not get Column To Field Map for column " + var2);
      } else {
         try {
            return this.rbean.getDefaultDBMSColType(var4);
         } catch (Exception var6) {
            throw new WLDeploymentException("No Field class found for " + var2);
         }
      }
   }

   private boolean getSequenceTableColumns(String var1, StringBuffer var2) throws Exception {
      if (this.rbean().hasAutoKeyGeneration() && this.genKeyType == 3) {
         String var3 = this.rbean().getGenKeyGeneratorName();
         if (var3 != null && var1 != null) {
            if (var3.equals(var1)) {
               var2.append("SEQUENCE ").append(this.getGenKeySequenceDBColType());
               return true;
            } else {
               return false;
            }
         } else {
            throw new RDBMSException(" in getSequenceTableColumns: either the SEQUENCE_TABLE name in the RDBMSBean or the passed in table Name  is NULL for bean: " + this.ejbName);
         }
      } else {
         return false;
      }
   }

   private boolean getBeanOrJoinTableColumns(String var1, StringBuffer var2) throws Exception {
      HashSet var3 = new HashSet();
      Iterator var7;
      String var8;
      Class var11;
      String var23;
      if (this.rbean().isJoinTable(var1)) {
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment(" createDefaultDBMSTable: processing Join Table: " + var1);
         }

         String var4 = this.rbean().getCmrFieldForJoinTable(var1);
         if (null == var4) {
            throw new RDBMSException(" Bean: " + this.ejbName + ", could not get cmrField for Join Table " + var1);
         }

         Map var5 = this.rbean().getFkColumn2ClassMapForFkField(var4);
         if (null == var5) {
            throw new RDBMSException(" Bean: " + this.ejbName + ", could not get Column To Class Map for FK Field " + var4);
         }

         ArrayList var6 = new ArrayList();
         var7 = var5.keySet().iterator();

         Class var9;
         while(var7.hasNext()) {
            var8 = (String)var7.next();
            var9 = (Class)var5.get(var8);
            var2.append(var8 + " ");
            var2.append(this.rbean.getDefaultDBMSColType(var9));
            if (this.databaseType == 2 || this.databaseType == 7 || this.databaseType == 5 || this.databaseType == 4 || this.databaseType == 9) {
               var2.append(" NOT NULL ");
            }

            var2.append(", ");
            var3.add(var8);
            var6.add(var8);
         }

         String var10;
         if (this.rbean().isRemoteField(var4)) {
            if (deploymentLogger.isDebugEnabled()) {
               debugDeployment(" Do REMOTE RHS of Join Table ");
            }

            EjbEntityRef var19 = this.rbean().getEjbEntityRef(var4);
            var9 = null;
            var10 = this.rbean().getRemoteColumn(var4);
            var2.append(var10 + " ");
            var11 = var9;
            if (!this.rbean.isValidSQLType(var9) && Serializable.class.isAssignableFrom(var9)) {
               byte[] var12 = new byte[0];
               var11 = var12.getClass();
            }

            var2.append(this.rbean.getDefaultDBMSColType(var11));
            if (this.databaseType == 2 || this.databaseType == 7 || this.databaseType == 5) {
               var2.append(" NOT NULL ");
            }

            var3.add(var10);
         } else if (this.rbean().isSymmetricField(var4)) {
            if (deploymentLogger.isDebugEnabled()) {
               debugDeployment(" Do Symmetric RHS of Join Table ");
            }

            Map var20 = this.rbean().getSymmetricColumn2FieldName(var4);
            if (null == var20) {
               throw new RDBMSException(" Bean: " + this.ejbName + ", could not get Symmetric Column To Class " + "Map for FK Field " + var4);
            }

            var7 = var20.keySet().iterator();

            label270:
            while(true) {
               do {
                  if (!var7.hasNext()) {
                     break label270;
                  }

                  var23 = (String)var7.next();
               } while(var6.contains(var23));

               var2.append(var23 + " ");
               var10 = (String)var20.get(var23);
               var11 = this.rbean().getCmpFieldClass(var10);
               var2.append(this.rbean.getDefaultDBMSColType(var11));
               if (this.databaseType == 2 || this.databaseType == 7 || this.databaseType == 5) {
                  var2.append(" NOT NULL ");
               }

               var3.add(var23);
               if (var7.hasNext()) {
                  var2.append(", ");
               }
            }
         } else {
            if (deploymentLogger.isDebugEnabled()) {
               debugDeployment(" Do Normal non-Remote non-Symmetric RHS of Join Table ");
            }

            RDBMSBean var21 = this.rbean().getRelatedRDBMSBean(var4);
            var23 = this.rbean().getRelatedFieldName(var4);
            var5 = var21.getFkColumn2ClassMapForFkField(var23);
            if (null == var5) {
               throw new RDBMSException(" Bean: " + var21.getEjbName() + ", could not get Column To Class Map for FK Field " + var23);
            }

            var7 = var5.keySet().iterator();

            label255:
            while(true) {
               do {
                  if (!var7.hasNext()) {
                     break label255;
                  }

                  var10 = (String)var7.next();
               } while(var6.contains(var10));

               var11 = (Class)var5.get(var10);
               var2.append(var10 + " ");
               var2.append(this.rbean.getDefaultDBMSColType(var11));
               var3.add(var10);
               if (this.databaseType == 2 || this.databaseType == 7 || this.databaseType == 5 || this.databaseType == 4 || this.databaseType == 9) {
                  var2.append(" NOT NULL ");
               }

               if (var7.hasNext()) {
                  var2.append(", ");
               }
            }
         }
      } else {
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment(" createDefaultDBMSTable: processing Bean Table: " + var1);
         }

         List var14 = this.rbean().getPrimaryKeyFields();
         HashSet var16 = new HashSet();
         Map var18 = this.rbean().getCmpField2ColumnMap(var1);
         var7 = var18.keySet().iterator();

         label237:
         while(true) {
            do {
               if (!var7.hasNext()) {
                  List var22 = this.rbean().getCmrFields(var1);
                  if (var22 != null) {
                     var7 = var22.iterator();

                     label219:
                     while(true) {
                        do {
                           if (!var7.hasNext()) {
                              break label237;
                           }

                           var23 = (String)var7.next();
                        } while(!this.rbean().containsFkField(var23));

                        Iterator var25 = this.rbean().getForeignKeyColNames(var23).iterator();
                        Map var28 = this.rbean().getFkColumn2ClassMapForFkField(var23);

                        while(true) {
                           do {
                              while(true) {
                                 String var26;
                                 do {
                                    if (!var25.hasNext()) {
                                       continue label219;
                                    }

                                    var26 = (String)var25.next();
                                 } while(var16.contains(var26));

                                 var2.append(", ");
                                 var16.add(var26);
                                 var2.append(var26 + " ");
                                 Class var13 = (Class)var28.get(var26);
                                 var2.append(this.rbean.getDefaultDBMSColType(var13));
                                 if (var14.contains(var23)) {
                                    var3.add(var26);
                                    break;
                                 }

                                 if (this.databaseType == 5) {
                                    var2.append(" NULL ");
                                 }
                              }
                           } while(this.databaseType != 2 && this.databaseType != 7 && this.databaseType != 5);

                           var2.append(" NOT NULL ");
                        }
                     }
                  }
                  break label237;
               }

               var8 = (String)var7.next();
               var23 = (String)var18.get(var8);
            } while(var16.contains(var23));

            var16.add(var23);
            Class var24 = this.rbean().getCmpFieldClass(var8);
            var11 = null;
            String var27;
            if (this.rbean.isClobCmpColumnTypeForField(var8)) {
               var27 = "Clob";
            } else if (this.rbean.isBlobCmpColumnTypeForField(var8)) {
               var27 = "Blob";
            } else {
               var27 = this.rbean.getDefaultDBMSColType(var24);
            }

            var2.append(var23 + " ");
            if (!var14.contains(var8)) {
               var2.append(var27);
               if (this.databaseType == 5 && !"BIT".equals(var27)) {
                  var2.append(" NULL ");
               } else if (this.databaseType == 5 && "BIT".equals(var27)) {
                  var2.append(" NOT NULL ");
               }
            } else {
               var3.add(var23);
               switch (this.databaseType) {
                  case 0:
                     var2.append(var27);
                     break;
                  case 1:
                     var2.append(var27);
                     var2.append(" NOT NULL ");
                     break;
                  case 2:
                  case 7:
                     var2.append(var27);
                     var2.append(" NOT NULL ");
                     if (this.genKeyType == 1) {
                        var2.append("IDENTITY ");
                     }
                     break;
                  case 3:
                     if (this.genKeyType == 1) {
                        var2.append("SERIAL");
                     } else {
                        var2.append(var27);
                     }

                     var2.append(" NOT NULL ");
                     break;
                  case 4:
                  case 9:
                     var2.append(var27);
                     var2.append(" NOT NULL ");
                     if (this.genKeyType == 1) {
                        var2.append("GENERATED ALWAYS AS IDENTITY ");
                     }
                     break;
                  case 5:
                     if (this.genKeyType == 1) {
                        var2.append("NUMERIC IDENTITY");
                     } else {
                        var2.append(var27);
                     }

                     var2.append(" NOT NULL ");
                     break;
                  case 6:
                     var2.append(var27);
                     if (this.genKeyType == 1) {
                        var2.append(" IDENTITY");
                     }

                     var2.append(" NOT NULL ");
                     break;
                  case 8:
                     var2.append(var27);
                     var2.append(" NOT NULL ");
                     break;
                  default:
                     throw new AssertionError("Unknown DB Type: " + this.databaseType);
               }
            }

            if (var7.hasNext()) {
               var2.append(", ");
            }
         }
      }

      if (var3.size() > 0) {
         var2.append(",");
         if (this.databaseType == 6 || this.databaseType == 8 || this.databaseType == 2 || this.databaseType == 7 || this.databaseType == 5) {
            var2.append(" CONSTRAINT pk_" + var1);
         }

         var2.append(" PRIMARY KEY (");
         Iterator var15 = var3.iterator();

         while(var15.hasNext()) {
            String var17 = (String)var15.next();
            var2.append(var17);
            if (var15.hasNext()) {
               var2.append(", ");
            }
         }

         var2.append(")");
      }

      return true;
   }

   private Finder createDynamicFinder(String var1, WLQueryProperties var2, boolean var3, boolean var4, Class var5) throws Exception {
      EjbqlFinder var6 = null;
      if (var4) {
         var6 = new EjbqlFinder("execute", var1);
         var6.setIsSelect(true);
      } else {
         var6 = new EjbqlFinder("find", var1);
      }

      var6.parseExpression();
      var6.setFinderLoadsBean(this.rbean().getCMPBeanDescriptor().getFindersLoadBean());
      if (var3) {
         var6.setResultTypeMapping("Local");
      } else {
         var6.setResultTypeMapping("Remote");
      }

      var6.setRDBMSBean(this.rbean());
      if (var2 != null) {
         var6.setMaxElements(var2.getMaxElements());
         var6.setIncludeUpdates(var2.getIncludeUpdates());
         var6.setSqlSelectDistinct(var2.getSQLSelectDistinct());
         if (!var4) {
            String var7 = var2.getRelationshipCachingName();
            if (var7 != null) {
               if (this.rbean().getRelationshipCaching(var7) == null) {
                  Loggable var10 = EJBLogger.logInvalidRelationshipCachingNameLoggable(var7);
                  throw new FinderException(var10.getMessage());
               }

               var6.setCachingName(var7);
            }

            String var8 = var2.getFieldGroupName();
            if (var8 != null) {
               if (this.rbean().getFieldGroup(var8) == null) {
                  Loggable var9 = EJBLogger.loginvalidFieldGroupNameLoggable(var8);
                  throw new FinderException(var9.getMessage());
               }

               var6.setGroupName(var8);
            }

            if (var3 && var2.isResultTypeRemote()) {
               var6.setResultTypeMapping("Remote");
            }
         }
      } else {
         var6.setNativeQuery(true);
      }

      var6.setReturnClassType(var5);
      var6.setParameterClassTypes(new Class[0]);
      if (var2 != null) {
         var6.setQueryCachingEnabled(var2.getEnableQueryCaching());
      }

      var6.computeSQLQuery(this.rbean());
      return var6;
   }

   private Object getDynamicQueryResult(ResultSet var1, Finder var2, boolean var3, boolean var4) throws Exception {
      if (var4) {
         RowSetFactory var8 = RowSetFactory.newInstance();
         WLCachedRowSet var9 = var8.newCachedRowSet();
         var9.populate(var1);
         return var9;
      } else {
         boolean var5 = var2.hasLocalResultType();
         this.checkResultTypeMapping(this.rbean(), var5);
         QueryCachingHandler var6 = null;
         if (var2.isQueryCachingEnabled()) {
            var6 = var2.getQueryCachingHandler((Object[])null, (TTLManager)this.getBeanManager());
         } else {
            var6 = var2.getQueryCachingHandler((Object[])null, (TTLManager)null);
         }

         Collection var7 = this.loadBeansFromRS(var1, var2, this.rbean(), this.beanManager, var5, var6);
         var6.putInQueryCache();
         return var7;
      }
   }

   private void checkResultTypeMapping(RDBMSBean var1, boolean var2) throws FinderException {
      Loggable var3;
      if (var2) {
         if (!var1.getCMPBeanDescriptor().hasLocalClientView()) {
            var3 = EJBLogger.loginvalidResultTypeMappingLoggable(var1.getEjbName(), "Local");
            throw new FinderException(var3.getMessage());
         }
      } else if (!var1.getCMPBeanDescriptor().hasRemoteClientView()) {
         var3 = EJBLogger.loginvalidResultTypeMappingLoggable(var1.getEjbName(), "Remote");
         throw new FinderException(var3.getMessage());
      }

   }

   private Collection loadBeansFromRS(ResultSet var1, Finder var2, RDBMSBean var3, BeanManager var4, boolean var5, QueryCachingHandler var6) throws Exception {
      Object var7 = new ArrayList();
      CMPBeanManager var8 = (CMPBeanManager)var4;
      boolean var9 = var2.isSelectDistinct();
      HashSet var10 = null;
      boolean var11 = false;
      if (var9) {
         var10 = new HashSet();
      }

      CMPBean var12 = (CMPBean)var8.getBeanFromPool();

      try {
         if (var2.finderLoadsBean()) {
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime("FinderLoadsBean == true");
            }

            Debug.assertion(var2 instanceof EjbqlFinder);
            if (((EjbqlFinder)var2).getCachingName() != null) {
               var11 = true;
            }

            String var13 = ((EjbqlFinder)var2).getGroupName();
            int var14 = var3.getFieldGroup(var13).getIndex();
            int var15 = this.getGroupColumnCount(var3, var13);
            EntityBean var16 = null;
            Transaction var17 = TxHelper.getTransactionManager().getTransaction();
            int var18 = 1;

            while(var1.next()) {
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("Loading Bean: " + var18++);
               }

               Object var19 = var12.__WL_getPKFromRSInstance(var1, new Integer(0), this.classLoader);
               RSInfoImpl var20;
               TTLManager var21;
               if (var9) {
                  if (var10.add(var19)) {
                     var20 = new RSInfoImpl(var1, var14, 0, var19);
                     var16 = ((BaseEntityManager)var8).getBeanFromRS(var17, var19, var20);
                     ((Collection)var7).add(var8.finderGetEoFromBeanOrPk(var16, var19, var2.hasLocalResultType()));
                     if (var2.isQueryCachingEnabled()) {
                        var21 = (TTLManager)var8;
                        var6.addQueryCachingEntry(var21, new QueryCacheElement(var19, var21));
                     }
                  } else if (runtimeLogger.isDebugEnabled()) {
                     debugRuntime("Bean was already loaded ");
                  }
               } else {
                  var20 = new RSInfoImpl(var1, var14, 0, var19);
                  var16 = ((BaseEntityManager)var8).getBeanFromRS(var17, var19, var20);
                  ((Collection)var7).add(var8.finderGetEoFromBeanOrPk(var16, var19, var2.hasLocalResultType()));
                  if (var2.isQueryCachingEnabled()) {
                     var21 = (TTLManager)var8;
                     var6.addQueryCachingEntry(var21, new QueryCacheElement(var19, var21));
                  }
               }

               if (var11) {
                  if (runtimeLogger.isDebugEnabled()) {
                     debugRuntime("Dynamic finder has RelationshipCaching turned on, load the related beans");
                  }

                  ((CMPBean)var16).__WL_loadBeansRelatedToCachingName(((EjbqlFinder)var2).getCachingName(), var1, (CMPBean)var16, var15, var6);
               }
            }
         } else {
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime("FinderLoadsBean == false");
            }

            while(var1.next()) {
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("Loading PK");
               }

               Object var26 = var12.__WL_getPKFromRSInstance(var1, new Integer(0), this.classLoader);
               TTLManager var27;
               if (var9) {
                  if (var10.add(var26)) {
                     ((Collection)var7).add(var26);
                     if (var2.isQueryCachingEnabled()) {
                        var27 = (TTLManager)var8;
                        var6.addQueryCachingEntry(var27, new QueryCacheElement(var26, var27));
                     }
                  }
               } else {
                  ((Collection)var7).add(var26);
                  if (var2.isQueryCachingEnabled()) {
                     var27 = (TTLManager)var8;
                     var6.addQueryCachingEntry(var27, new QueryCacheElement(var26, var27));
                  }
               }
            }

            var7 = ((BaseEntityManager)var8).pkCollToColl((Collection)var7, var2.hasLocalResultType());
         }
      } finally {
         ((BaseEntityManager)var8).releaseBeanToPool((EntityBean)var12);
      }

      return (Collection)var7;
   }

   public int getGroupColumnCount(RDBMSBean var1, String var2) {
      FieldGroup var3 = var1.getFieldGroup(var2);
      if (var3 == null) {
         return var1.getPrimaryKeyFields().size();
      } else {
         HashSet var4 = new HashSet();
         TreeSet var5 = new TreeSet(var3.getCmpFields());
         var5.addAll(var1.getPrimaryKeyFields());
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            var4.add(var1.getCmpColumnForField(var7));
         }

         Iterator var11 = var3.getCmrFields().iterator();

         while(var11.hasNext()) {
            String var8 = (String)var11.next();
            Iterator var9 = var1.getForeignKeyColNames(var8).iterator();

            while(var9.hasNext()) {
               String var10 = (String)var9.next();
               var4.add(var10);
            }
         }

         return var4.size();
      }
   }

   public String getEjbName() {
      return this.ejbName;
   }

   public BaseEntityManager getBeanManager() {
      return this.beanManager;
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

   public String nativeQuery(String var1) throws FinderException {
      String var2 = null;
      Finder var3 = null;

      try {
         var3 = this.createDynamicFinder(var1, (WLQueryProperties)null, true, true, Collection.class);
         var2 = var3.getSQLQuery();
      } catch (Exception var19) {
         throw new FinderException("Error constructing query: \n" + var19.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var19));
      }

      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("EJB-QL produced SQL: " + var2);
      }

      Connection var4 = null;

      String var5;
      try {
         try {
            var4 = this.getConnection();
         } catch (Exception var17) {
            throw new FinderException("Couldn't get connection: \n" + var17.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var17));
         }

         try {
            var5 = var4.nativeSQL(var2);
         } catch (SQLException var16) {
            throw new FinderException("Error getting native SQL: \n" + var16.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var16));
         }
      } finally {
         try {
            this.releaseConnection(var4);
         } catch (SQLException var15) {
         }

      }

      return var5;
   }

   public String getDatabaseProductName() throws FinderException {
      Connection var1 = null;

      String var2;
      try {
         try {
            var1 = this.getConnection();
         } catch (Exception var13) {
            throw new FinderException("Couldn't get connection: \n" + var13.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var13));
         }

         try {
            var2 = var1.getMetaData().getDatabaseProductName();
         } catch (SQLException var12) {
            throw new FinderException("Error calling getDatabaseProductName: \n" + var12.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var12));
         }
      } finally {
         try {
            this.releaseConnection(var1);
         } catch (SQLException var11) {
         }

      }

      return var2;
   }

   public String getDatabaseProductVersion() throws FinderException {
      Connection var1 = null;

      String var2;
      try {
         try {
            var1 = this.getConnection();
         } catch (Exception var13) {
            throw new FinderException("Couldn't get connection: \n" + var13.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var13));
         }

         try {
            var2 = var1.getMetaData().getDatabaseProductVersion();
         } catch (SQLException var12) {
            throw new FinderException("Error calling getDatabaseProductVersion: \n" + var12.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var12));
         }
      } finally {
         try {
            this.releaseConnection(var1);
         } catch (SQLException var11) {
         }

      }

      return var2;
   }

   public Object executePreparedQuery(String var1, boolean var2, boolean var3, Map var4, Map var5, PreparedQuery var6) throws FinderException {
      Connection var7 = null;
      PreparedStatement var8 = null;
      ResultSet var9 = null;
      EjbqlFinder var10 = null;
      Object[] var11 = new Object[3];
      Object var12 = new TreeMap();
      Object var16;
      String var25;
      int var47;
      Iterator var48;
      int var50;
      if (var1 == null) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("\nParsing EJBQL for the first time");
         }

         String var14;
         try {
            if (var3) {
               var10 = new EjbqlFinder("execute", var6.getEjbql());
               var10.setIsSelect(true);
            } else {
               var10 = new EjbqlFinder("find", var6.getEjbql());
            }

            var10.setIsPreparedQueryFinder(true);
            var10.parseExpression();
            var10.setFinderLoadsBean(this.rbean().getCMPBeanDescriptor().getFindersLoadBean());
            var10.setRDBMSBean(this.rbean());
            if (var6 != null) {
               WLQueryProperties var13 = (WLQueryProperties)var6;
               var10.setSqlSelectDistinct(var13.getSQLSelectDistinct());
               if (!var3) {
                  var14 = var13.getRelationshipCachingName();
                  if (var14 != null) {
                     if (this.rbean().getRelationshipCaching(var14) == null) {
                        Loggable var54 = EJBLogger.logInvalidRelationshipCachingNameLoggable(var14);
                        throw new FinderException(var54.getMessage());
                     }

                     var10.setCachingName(var14);
                  }

                  String var15 = var13.getFieldGroupName();
                  if (var15 != null) {
                     if (this.rbean().getFieldGroup(var15) == null) {
                        Loggable var55 = EJBLogger.loginvalidFieldGroupNameLoggable(var15);
                        throw new FinderException(var55.getMessage());
                     }

                     var10.setGroupName(var15);
                  }
               }
            }

            Class[] var46 = new Class[var4.size()];
            var48 = var4.values().iterator();
            var50 = 0;

            while(true) {
               if (!var48.hasNext()) {
                  var10.setParameterClassTypes(var46);
                  var10.computeSQLQuery(this.rbean());
                  break;
               }

               var16 = var48.next();
               if (!(var16 instanceof EJBObject) && !(var16 instanceof EJBLocalObject)) {
                  var46[var50] = var16.getClass();
               } else {
                  var46[var50] = var16.getClass().getInterfaces()[0];
               }

               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("parameter class type " + var50 + " : set to: " + var46[var50]);
               }

               ++var50;
            }
         } catch (Exception var45) {
            throw new FinderException("Error constructing prepared query: \n" + var45.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var45));
         }

         var47 = this.getUpdateLockType();
         var14 = null;
         switch (var47) {
            case 4:
               var14 = var10.getSQLQuery();
               break;
            case 5:
               var14 = var10.getSQLQueryForUpdateSelective();
               if (var14 == null) {
                  if (this.rbean.getUseSelectForUpdate()) {
                     var14 = var10.getSQLQueryForUpdate();
                  } else {
                     var14 = var10.getSQLQuery();
                  }
               }
               break;
            case 6:
               var14 = var10.getSQLQueryForUpdate();
               break;
            case 7:
               var14 = var10.getSQLQueryForUpdateNoWait();
               break;
            default:
               throw new AssertionError("Undefined update lock value");
         }

         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("EJBQL converted to SQL: " + var14);
         }

         var1 = var14;
         var11[0] = var14;
         boolean var52 = false;
         List var53 = var10.getExternalMethodParmList();
         Iterator var17 = var53.iterator();

         for(int var18 = 1; var17.hasNext(); ++var18) {
            ParamNode var19 = (ParamNode)var17.next();
            if (!var19.hasCompoundKey()) {
               ((Map)var12).put(new Integer(var18), new DynamicEJBQLArgumentWrapper(var19.getParamName(), var19.isOracleNLSDataType()));
            } else {
               TreeMap var20 = new TreeMap();
               List var21 = var19.getParamSubList();
               int var22 = 1;

               for(Iterator var23 = var21.iterator(); var23.hasNext(); ++var22) {
                  ParamNode var24 = (ParamNode)var23.next();
                  var25 = var24.getId();
                  var20.put(new Integer(var22), new DynamicEJBQLArgumentWrapper(var25, var24.isOracleNLSDataType()));
               }

               var52 = true;
               ((Map)var12).put(new Integer(var18), var20);
            }
         }

         if (var52 && runtimeLogger.isDebugEnabled()) {
            debugRuntime("\n-------------------------");
            debugRuntime("The arguments have a compoundPK, the Flattened argument Map is: \n" + var12);
            debugRuntime("-------------------------\n");
         }

         var11[1] = var12;
      } else {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("\nEJBQL was already parsed and converted to SQL");
         }

         try {
            if (var3) {
               var10 = new EjbqlFinder("execute", "dummy");
            } else {
               var10 = new EjbqlFinder("find", "dummy");
            }
         } catch (Exception var43) {
            throw new FinderException("Error constructing dummy query while  executing PreparedStatement: \n" + var43.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var43));
         }

         var12 = var5;
      }

      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("findersLoadBean: " + this.rbean().getCMPBeanDescriptor().getFindersLoadBean());
         debugRuntime("isLocal: " + var2);
         debugRuntime("isResultTypeRemote: " + var6.isResultTypeRemote());
         debugRuntime("isSelect: " + var3);
      }

      var10.setFinderLoadsBean(this.rbean().getCMPBeanDescriptor().getFindersLoadBean());
      if (var2) {
         var10.setResultTypeMapping("Local");
      } else {
         var10.setResultTypeMapping("Remote");
      }

      if (var2 && var6 != null && var6.isResultTypeRemote()) {
         var10.setResultTypeMapping("Remote");
      }

      if (var3) {
         var10.setReturnClassType(ResultSet.class);
      } else {
         var10.setReturnClassType(Collection.class);
      }

      if (var6 != null && var6.getIncludeUpdates()) {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("Flushing modified beans before running the Dyn Query with PrepStmt");
         }

         this.flushModifiedBeans();
      }

      try {
         var7 = this.getConnection();
      } catch (Exception var42) {
         this.releaseResources(var7, (Statement)null, (ResultSet)null);
         throw new FinderException("Couldn't get connection: \n" + var42.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var42));
      }

      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("Creating PreparedStatement with sql: " + var1);
      }

      try {
         var8 = var7.prepareStatement(var1);
      } catch (Exception var41) {
         this.releaseResources(var7, var8, (ResultSet)null);
         throw new FinderException("Exception in executePreparedQuery while preparing statement: " + var8 + "'\n" + var41.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var41));
      }

      try {
         if (var6 != null && var6.getMaxElements() != 0) {
            var8.setMaxRows(var6.getMaxElements());
         }

         if (var12 == null) {
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime("arguments do not have a compound PK");
            }

            Iterator var49 = var4.entrySet().iterator();

            while(var49.hasNext()) {
               Map.Entry var59 = (Map.Entry)var49.next();
               var50 = (Integer)var59.getKey();
               var16 = var59.getValue();
               if (var16 instanceof EJBObject) {
                  var16 = ((EJBObject)var16).getPrimaryKey();
               }

               if (var16 instanceof EJBLocalObject) {
                  var16 = ((EJBLocalObject)var16).getPrimaryKey();
               }

               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("paramIndex: " + var50 + " binded with value: " + var16);
               }

               if (var16.getClass().equals(Character.class)) {
                  var8.setString(var50, var16.toString());
               } else {
                  var8.setObject(var50, var16);
               }
            }
         } else {
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime("arguments have a compound PK");
            }

            var47 = 1;
            var48 = ((Map)var12).entrySet().iterator();

            label524:
            while(true) {
               while(true) {
                  if (!var48.hasNext()) {
                     break label524;
                  }

                  Map.Entry var56 = (Map.Entry)var48.next();
                  int var57 = (Integer)var56.getKey();
                  Object var58 = var56.getValue();
                  if (var58 instanceof Map) {
                     Object var62 = var4.get(new Integer(var57));
                     Object var63 = ((EJBLocalObject)var62).getPrimaryKey();
                     Class var64 = var63.getClass();

                     for(Iterator var65 = ((Map)var58).entrySet().iterator(); var65.hasNext(); ++var47) {
                        Map.Entry var66 = (Map.Entry)var65.next();
                        int var67 = (Integer)var66.getKey();
                        DynamicEJBQLArgumentWrapper var68 = (DynamicEJBQLArgumentWrapper)var66.getValue();
                        var25 = var68.getArgumentName();
                        Field var26 = var64.getField(var25);
                        Object var27 = var26.get(var63);
                        if (runtimeLogger.isDebugEnabled()) {
                           debugRuntime("paramIndex: " + var47 + " binded with value: " + var27);
                        }

                        if (var68.isOracleNLSDataType()) {
                           this.invokeOracleSetFormOfUse(var47, var8);
                        }

                        if (var27.getClass().equals(Character.class)) {
                           var8.setString(var47, var27.toString());
                        } else {
                           var8.setObject(var47, var27);
                        }
                     }
                  } else {
                     DynamicEJBQLArgumentWrapper var61 = (DynamicEJBQLArgumentWrapper)var58;
                     var58 = var4.get(new Integer(var57));
                     if (runtimeLogger.isDebugEnabled()) {
                        debugRuntime("paramIndex: " + var47 + " binded with value: " + var58);
                     }

                     if (var61.isOracleNLSDataType()) {
                        this.invokeOracleSetFormOfUse(var47, var8);
                     }

                     if (var58.getClass().equals(Character.class)) {
                        var8.setString(var47, var58.toString());
                     } else {
                        var8.setObject(var47, var58);
                     }

                     ++var47;
                  }
               }
            }
         }

         var9 = var8.executeQuery();
      } catch (Exception var44) {
         this.releaseResources(var7, var8, var9);
         throw new FinderException("Error in executing PreparedQuery: PreparedStatement: '" + var8 + "'\n" + var44.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var44));
      }

      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("Dynamic Query with PreparedStatement executed ");
      }

      Object[] var60;
      try {
         Object var51 = this.getDynamicQueryResult(var9, var10, var2, var3);
         var11[2] = var51;
         var60 = var11;
      } catch (SQLException var38) {
         throw new FinderException("Exception in executePreparedQuery while using result set: '" + var9 + "'\n" + var38.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var38));
      } catch (Exception var39) {
         throw new FinderException("Exception executing executePreparedQuery: \n" + var39.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var39));
      } finally {
         this.releaseResources(var7, var8, var9);
      }

      return var60;
   }

   private void invokeOracleSetFormOfUse(int var1, PreparedStatement var2) throws Exception {
      if ("oracle.jdbc.OraclePreparedStatement".equals(var2.getClass().getName())) {
         Method var3 = var2.getClass().getMethod("setFormOfUse", Integer.TYPE, Short.TYPE);
         if (var3 != null) {
            var3.invoke(var2, var1, new Short((short)2));
         }
      }

   }

   public Object dynamicQuery(String var1, WLQueryProperties var2, boolean var3, boolean var4) throws FinderException {
      String var5 = null;
      Finder var6 = null;

      try {
         if (var4) {
            var6 = this.createDynamicFinder(var1, var2, var3, var4, ResultSet.class);
            var5 = var6.getSQLQuery();
         } else {
            var6 = this.createDynamicFinder(var1, var2, var3, var4, Collection.class);
            int var7 = this.getUpdateLockType();
            switch (var7) {
               case 4:
                  var5 = var6.getSQLQuery();
                  break;
               case 5:
                  var5 = var6.getSQLQueryForUpdateSelective();
                  if (var5 == null) {
                     if (this.rbean.getUseSelectForUpdate()) {
                        var5 = var6.getSQLQueryForUpdate();
                     } else {
                        var5 = var6.getSQLQuery();
                     }
                  }
                  break;
               case 6:
                  var5 = var6.getSQLQueryForUpdate();
                  break;
               case 7:
                  var5 = var6.getSQLQueryForUpdateNoWait();
                  break;
               default:
                  throw new AssertionError("Undefined update lock value");
            }
         }
      } catch (Exception var23) {
         throw new FinderException("Error constructing query: \n" + var23.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var23));
      }

      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("Dynamic Query produced statement string " + var5);
      }

      Connection var25 = null;
      PreparedStatement var8 = null;
      ResultSet var9 = null;
      if (var6.getIncludeUpdates()) {
         this.flushModifiedBeans();
      }

      try {
         var25 = this.getConnection();
      } catch (Exception var22) {
         this.releaseResources(var25, var8, var9);
         throw new FinderException("Couldn't get connection: \n" + var22.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var22));
      }

      try {
         var8 = var25.prepareStatement(var5);
         if (var6.getMaxElements() != 0) {
            var8.setMaxRows(var6.getMaxElements());
         }

         var9 = var8.executeQuery();
      } catch (Exception var24) {
         this.releaseResources(var25, var8, var9);
         throw new FinderException("Exception in dynamicQuery while preparing or executing statement: '" + var8 + "'\n" + var24.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var24));
      }

      Object var10;
      try {
         var10 = this.getDynamicQueryResult(var9, var6, var3, var4);
      } catch (SQLException var19) {
         throw new FinderException("Exception in dynamicQuery while using result set: '" + var9 + "'\n" + var19.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var19));
      } catch (Exception var20) {
         throw new FinderException("Exception executing dynamicQuery: \n" + var20.toString() + "\n" + RDBMSUtils.throwable2StackTrace(var20));
      } finally {
         this.releaseResources(var25, var8, var9);
      }

      return var10;
   }

   public Connection getConnection() throws SQLException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("RDBMSPersistenceManager.getConnection");
      }

      assert this.ds != null;

      Connection var1 = this.ds.getConnection();
      if (var1 == null) {
         Loggable var2 = EJBLogger.logCouldNotGetConnectionFromDataSourceLoggable(this.dataSourceName);
         throw new SQLException(var2.getMessage());
      } else {
         return var1;
      }
   }

   public Connection getConnection(Transaction var1) throws SQLException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("RDBMSPersistenceManager.getConnection");
      }

      assert this.ds != null;

      Connection var2 = this.ds.getConnection();
      if (var2 == null) {
         Loggable var3 = EJBLogger.logCouldNotGetConnectionFromDataSourceLoggable(this.dataSourceName);
         throw new SQLException(var3.getMessage());
      } else {
         return var2;
      }
   }

   public Integer getTransactionIsolationLevel() {
      weblogic.transaction.Transaction var1 = TxHelper.getTransaction();
      return var1 == null && Thread.currentThread() == EJBRuntimeUtils.getInvokeTxOrThread() ? null : (Integer)((TransactionImpl)var1).getProperty("ISOLATION LEVEL");
   }

   public Transaction suspendTransaction() throws PersistenceRuntimeException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("suspendTransaction");
      }

      if (TxHelper.getTransaction() == null && Thread.currentThread() == EJBRuntimeUtils.getInvokeTxOrThread()) {
         return null;
      } else {
         assert this.tm != null;

         assert TxHelper.getTransaction() != null;

         try {
            return this.tm.suspend();
         } catch (Exception var2) {
            throw new PersistenceRuntimeException(var2);
         }
      }
   }

   public void resumeTransaction(Transaction var1) throws PersistenceRuntimeException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("resumeTransaction");
      }

      if (var1 != null) {
         assert this.tm != null;

         assert var1 != null;

         try {
            this.tm.resume(var1);
         } catch (Exception var3) {
            throw new PersistenceRuntimeException(var3);
         }
      }
   }

   public boolean needsBatchOperationsWorkaround() {
      return this.isOptimistic && this.databaseType == 1 && this.enableBatchOperations;
   }

   public boolean perhapsUseSendBatchForOracle() {
      boolean var1 = false;
      Connection var2 = null;

      try {
         var2 = this.getConnection();
         if (this.driverName.equalsIgnoreCase("Oracle JDBC driver") && "oracle.jdbc.OracleConnection".equals(var2.getClass().getName())) {
            var1 = true;
         }
      } catch (SQLException var9) {
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("SQLException while checking for sendBatch API usage:" + var9.getMessage());
         }
      } catch (Throwable var10) {
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("Exception while checking for sendBatch API usage:" + var10.getMessage());
         }
      } finally {
         this.releaseResources(var2, (Statement)null, (ResultSet)null);
      }

      return this.isOptimistic && this.databaseType == 1 && this.enableBatchOperations && var1;
   }

   public int[] getVerifyCount() {
      return (int[])((int[])this.verifyCount.clone());
   }

   public int[] getVerifyCur() {
      return (int[])((int[])this.verifyCur.clone());
   }

   public StringBuffer[] getVerifySql(boolean var1) {
      StringBuffer[] var2 = new StringBuffer[this.verifyText.length];
      int var3;
      if (var1) {
         for(var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = new StringBuffer(this.verifyTextWithXLock[var3]);
         }
      } else {
         for(var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = new StringBuffer(this.verifyText[var3]);
         }
      }

      return var2;
   }

   public PreparedStatement[] prepareStatement(Connection var1, StringBuffer[] var2, int[] var3, boolean var4) throws SQLException {
      PreparedStatement[] var5 = new PreparedStatement[var2.length];

      for(int var6 = 0; var6 < var2.length; ++var6) {
         if (var3[var6] > 0) {
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime("prepareStatement: sql=" + var2[var6].toString());
            }

            if (var4 && this.selectForUpdateSupported && this.databaseType != 2 && this.databaseType != 7) {
               var2[var6].append(" FOR UPDATE");
            }

            var5[var6] = var1.prepareStatement(var2[var6].toString());
         }
      }

      return var5;
   }

   public ResultSet[] executeQuery(PreparedStatement[] var1) throws SQLException {
      ResultSet[] var2 = new ResultSet[var1.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var1[var3] != null) {
            var2[var3] = var1[var3].executeQuery();
         }
      }

      return var2;
   }

   public void checkResults(ResultSet[] var1, int[] var2) throws SQLException, OptimisticConcurrencyException {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var2[var3] > 0) {
            int var4;
            for(var4 = 0; var1[var3].next(); ++var4) {
            }

            if (var4 != var2[var3]) {
               Loggable var5 = EJBLogger.logoptimisticUpdateFailedLoggable(this.ejbName, "<unknown>");
               throw new OptimisticConcurrencyException(var5.getMessage());
            }
         }
      }

   }

   public String getSnapshotPredicate(int var1) {
      return this.snapshotStrings[var1];
   }

   public String getSnapshotPredicate(int var1, Object var2) {
      assert this.nullSnapshotStrings[var1] != null;

      assert this.snapshotStrings[var1] != null;

      return var2 == null ? this.nullSnapshotStrings[var1] : this.snapshotStrings[var1];
   }

   private void verifyDatabaseType() throws WLDeploymentException {
      Connection var1 = null;

      try {
         var1 = this.getConnection();
         this.databaseType = this.verifier.verifyDatabaseType(var1, this.databaseType);
         this.rbean.setDatabaseType(this.databaseType);
      } catch (Exception var7) {
         throw new WLDeploymentException(var7.getMessage());
      } finally {
         this.releaseResources(var1, (Statement)null, (ResultSet)null);
      }

   }

   private void verifyTXDataSource() throws WLDeploymentException {
      if (this.ds instanceof DataSourceMetaData) {
         if (!((DataSourceMetaData)this.ds).isTxDataSource()) {
            Loggable var9 = EJBLogger.logcmpBeanMustHaveTXDataSourceSpecifiedLoggable(this.dataSourceName, this.ejbName);
            throw new WLDeploymentException(var9.getMessage());
         }
      } else {
         Connection var1 = null;

         try {
            var1 = this.getConnection();
            if (var1.getAutoCommit()) {
               Loggable var2 = EJBLogger.logcmpBeanMustHaveTXDataSourceSpecifiedLoggable(this.dataSourceName, this.ejbName);
               throw new WLDeploymentException(var2.getMessage());
            }
         } catch (SQLException var7) {
            throw new WLDeploymentException(var7.getMessage());
         } finally {
            this.releaseResources(var1, (Statement)null, (ResultSet)null);
         }

      }
   }

   private void verifyBatchUpdatesSupported() {
      if (this.enableBatchOperations) {
         boolean var1 = false;
         Connection var2 = null;

         try {
            var2 = this.getConnection();
            DatabaseMetaData var3 = var2.getMetaData();
            var1 = var3.supportsBatchUpdates();
         } catch (SQLException var9) {
            var1 = false;
         } catch (AbstractMethodError var10) {
            var1 = false;
         } finally {
            this.releaseResources(var2, (Statement)null, (ResultSet)null);
         }

         if (deploymentLogger.isDebugEnabled()) {
            if (!var1) {
               debugDeployment("The database or JDBC driver doesn't support batch update.");
            } else {
               debugDeployment("The database or JDBC driver supports batch update.");
            }
         }

         if (this.orderDatabaseOperations && this.enableBatchOperations) {
            this.orderDatabaseOperations = var1;
            this.enableBatchOperations = var1;
            if (deploymentLogger.isDebugEnabled()) {
               debugDeployment("The orderDatabaseOperations and enableBatchOperations are set to " + var1);
            }
         }

      }
   }

   public boolean getEnableBatchOperations() {
      return this.enableBatchOperations;
   }

   public boolean getOrderDatabaseOperations() {
      return this.orderDatabaseOperations;
   }

   public boolean setParamNull(PreparedStatement var1, int var2, Object var3, String var4) throws SQLException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("RDBMSPersistenceManager.setParamNull");
      }

      if (var3 == null) {
         assert this.variable2SQLType != null;

         assert this.variable2SQLType.get(var4) != null : "No field->SQLType mapping for field " + var4;

         Integer var5 = (Integer)this.variable2SQLType.get(var4);
         int var6 = var5;
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("setting field- " + var4 + " null (" + var2 + ", " + RDBMSUtils.sqlTypeToString(var6) + ")");
         }

         if (var6 != 2007) {
            var1.setNull(var2, var6);
         } else {
            var1.setNull(var2, 2007, "XMLTYPE");
         }

         return true;
      } else {
         if (runtimeLogger.isDebugEnabled()) {
            debugRuntime("field- " + var4 + " is not null.");
         }

         return false;
      }
   }

   public Object getNextSequenceKey() throws EJBException {
      return this.getNextGenKeyPreFetch(2);
   }

   public Object getNextSequenceTableKey() throws EJBException {
      return this.getNextGenKeyPreFetch(3);
   }

   public synchronized Object getNextGenKeyPreFetch(int var1) throws EJBException {
      if (this.genKeyCurrCacheSize <= 0) {
         switch (this.genKeyPKFieldClassType) {
            case 0:
               if (var1 == 2) {
                  this.genKeyCurrValueInt = (Integer)this.execGenKeyQuery();
               } else {
                  if (var1 != 3) {
                     throw new EJBException(" Internal Error, unknown genKeyType: " + var1);
                  }

                  this.genKeyCurrValueInt = (Integer)this.execGenKeySequenceTableUpdateAndQuery();
               }

               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("  refresh Key Cache.  New Value: " + this.genKeyCurrValueInt);
               }
               break;
            case 1:
               if (var1 == 2) {
                  this.genKeyCurrValueLong = (Long)this.execGenKeyQuery();
               } else {
                  if (var1 != 3) {
                     throw new EJBException(" Internal Error, unknown genKeyType: " + var1);
                  }

                  this.genKeyCurrValueLong = (Long)this.execGenKeySequenceTableUpdateAndQuery();
               }

               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("  refresh Key Cache.  New Value: " + this.genKeyCurrValueLong);
               }
               break;
            default:
               throw new EJBException(" Internal Error, unknown genKeyPKFieldClassType: " + this.genKeyPKFieldClassType);
         }

         this.genKeyCurrCacheSize = this.genKeyCacheSize;
      }

      if (--this.genKeyCurrCacheSize < 0) {
         if (this.genKeyPKFieldClassType == 0) {
            this.genKeyCurrValueLong = (long)this.genKeyCurrValueInt;
         }

         throw new EJBException("Error in auto PK generator key caching  genKeyCurrCacheSize = " + this.genKeyCurrCacheSize + "  is less than zero !   " + "genKeyCurrVal Integer/Long = " + this.genKeyCurrValueLong + ", genKeyCacheSize = " + this.genKeyCacheSize);
      } else {
         switch (this.genKeyPKFieldClassType) {
            case 0:
               return new Integer(this.genKeyCurrValueInt++);
            case 1:
               return new Long((long)(this.genKeyCurrValueLong++));
            default:
               throw new EJBException(" Internal Error, unknown genKeyPKFieldClassType: " + this.genKeyPKFieldClassType);
         }
      }
   }

   private Object execGenKeyQuery() throws EJBException {
      Connection var1 = null;
      Statement var2 = null;
      ResultSet var3 = null;
      boolean var4 = false;

      try {
         var1 = this.getConnection();
         var2 = var1.createStatement();
         var3 = var2.executeQuery(this.genKeyWLGeneratorQuery);
         if (!var3.next()) {
            Loggable var14 = EJBLogger.logExecGenKeyErrorLoggable(this.genKeyWLGeneratorQuery);
            throw new EJBException(var14.getMessage());
         } else {
            switch (this.genKeyPKFieldClassType) {
               case 0:
                  Integer var13 = new Integer(var3.getInt(1));
                  return var13;
               case 1:
                  Long var5 = new Long(var3.getLong(1));
                  return var5;
               default:
                  throw new EJBException(" Internal Error, unknown genKeyPKFieldClassType: " + this.genKeyPKFieldClassType);
            }
         }
      } catch (SQLException var11) {
         EJBException var6 = new EJBException(var11.getMessage());
         throw var6;
      } finally {
         this.releaseResources(var1, var2, var3);
      }
   }

   private synchronized Object execGenKeySequenceTableUpdateAndQuery() throws EJBException {
      Connection var1 = null;
      Statement var2 = null;
      ResultSet var3 = null;
      boolean var4 = false;
      weblogic.transaction.Transaction var5 = null;
      Transaction var6 = null;

      Object var63;
      try {
         weblogic.transaction.TransactionManager var7 = TxHelper.getTransactionManager();
         var5 = TxHelper.getTransaction();

         try {
            var7.suspend();
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime("execGenKeySequenceTableUpdateAndQuery: suspended Caller TX");
            }

            int var8 = 120000;
            if (this.transactionTimeoutMS <= 0) {
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("execGenKeySequenceTableUpdateAndQuery:  WARNING !  transactionTimeoutMS = " + this.transactionTimeoutMS + " forcing to " + var8);
               }

               this.transactionTimeoutMS = var8;
            }

            var7.setTransactionTimeout(this.transactionTimeoutMS / 1000);
            var7.begin();
            var6 = var7.getTransaction();
            if (runtimeLogger.isDebugEnabled()) {
               debugRuntime("execGenKeySequenceTableUpdateAndQuery: began and obtained new Local TX for UPDATE and QUERY");
            }

            ((weblogic.transaction.Transaction)var6).setProperty("LOCAL_ENTITY_TX", "true");
         } catch (Exception var57) {
            try {
               var7.resume(var5);
            } catch (Exception var56) {
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("execGenKeySequenceTableUpdateAndQuery:  Error encountered while attempting to Resume caller's TX.  Do forceResume of Caller's TX after encountering Exception: " + var56.getMessage());
               }

               ((weblogic.transaction.TransactionManager)var7).forceResume(var5);
            }

            Loggable var9 = EJBLogger.logGenKeySequenceTableNewTxFailureLoggable(var57.getMessage());
            throw new EJBException(var9.getMessage());
         }

         var1 = this.getConnection();
         var2 = var1.createStatement();
         String var61 = this.genKeyWLGeneratorUpdate;

         Loggable var11;
         Object var62;
         try {
            int var10 = var2.executeUpdate(var61);
            if (var10 < 1) {
               var11 = EJBLogger.logExecGenKeyErrorLoggable(var61);
               throw new EJBException(var11.getMessage());
            }

            var3 = var2.executeQuery(this.genKeyWLGeneratorQuery);
            if (!var3.next()) {
               var11 = EJBLogger.logExecGenKeyErrorLoggable(this.genKeyWLGeneratorQuery);
               throw new EJBException(var11.getMessage());
            }

            switch (this.genKeyPKFieldClassType) {
               case 0:
                  if (this.selectFirstSeqKeyBeforeUpdate) {
                     var62 = new Integer(var3.getInt(1) - this.genKeyCacheSize + 1);
                  } else {
                     var62 = new Integer(var3.getInt(1));
                  }
                  break;
               case 1:
                  if (this.selectFirstSeqKeyBeforeUpdate) {
                     var62 = new Long(var3.getLong(1) - (long)this.genKeyCacheSize + 1L);
                  } else {
                     var62 = new Long(var3.getLong(1));
                  }
                  break;
               default:
                  throw new EJBException(" Internal Error, unknown genKeyPKFieldClassType: " + this.genKeyPKFieldClassType);
            }
         } catch (Exception var58) {
            var11 = EJBLogger.logGenKeySequenceTableUpdateFailureLoggable(this.genKeyGeneratorName, var58.toString());
            throw new SQLException(var11.getMessage());
         }

         try {
            var6.commit();
         } catch (Exception var55) {
            var11 = EJBLogger.logGenKeySequenceTableLocalCommitFailureLoggable(this.genKeyGeneratorName, var55.getMessage());
            throw new SQLException(var11.getMessage());
         }

         var63 = var62;
      } catch (SQLException var59) {
         if (var6 != null) {
            try {
               var6.rollback();
            } catch (Exception var54) {
            }
         }

         throw new EJBException(var59);
      } finally {
         boolean var14 = false;
         if (var5 != null && var6 != null) {
            try {
               if (runtimeLogger.isDebugEnabled()) {
                  debugRuntime("execGenKeySequenceTableUpdateAndQuery: in finally: Now resume Caller TX");
               }

               EJBRuntimeUtils.resumeCallersTransaction(var5, var6);
            } catch (InternalException var52) {
               Loggable var16 = EJBLogger.logGenKeySequenceTableTxResumeFailureLoggable(this.genKeyGeneratorName, var52.getMessage());
               throw new EJBException(var16.getMessage());
            } finally {
               this.releaseResources(var1, var2, var3);
               var14 = true;
            }
         }

         if (!var14) {
            this.releaseResources(var1, var2, var3);
            var14 = true;
         }

      }

      return var63;
   }

   public void updateKeyCacheSize(int var1) {
      if (this.genKeyType == 3) {
         if (var1 <= 0) {
            var1 = 10;
         }

         synchronized(this) {
            this.genKeyCacheSize = var1;
            this.genKeyWLGeneratorUpdate = this.genKeyWLGeneratorUpdatePrefix + this.genKeyCacheSize;
         }
      }

   }

   public String getGenKeySequenceDBColType() {
      return "DECIMAL";
   }

   public int getUpdateLockType() {
      if (this.isOptimistic) {
         return 4;
      } else {
         try {
            Transaction var1 = this.tm.getTransaction();
            TransactionImpl var2 = (TransactionImpl)var1;
            if (var2 == null) {
               return 5;
            } else {
               Integer var3 = (Integer)var2.getProperty("ISOLATION LEVEL");
               if (var3 != null && var3 == 8) {
                  return 4;
               } else {
                  var3 = (Integer)var2.getProperty("SELECT_FOR_UPDATE");
                  if (var3 == null) {
                     return 5;
                  } else if (var3 == 1) {
                     return 6;
                  } else {
                     return var3 == 2 ? 7 : 5;
                  }
               }
            }
         } catch (Exception var4) {
            throw new PersistenceRuntimeException(var4);
         }
      }
   }

   public int getSelectForUpdateValue() {
      if (this.isOptimistic) {
         return 0;
      } else {
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
   }

   public String selectForUpdate() {
      return RDBMSUtils.selectForUpdateToString(this.getSelectForUpdateValue());
   }

   public String selectForUpdateOrForUpdateNowait() {
      try {
         Transaction var1 = this.tm.getTransaction();
         TransactionImpl var2 = (TransactionImpl)var1;
         Integer var3 = null;
         if (var2 != null) {
            var3 = (Integer)var2.getProperty("SELECT_FOR_UPDATE");
         }

         if (var3 == null) {
            return " FOR UPDATE ";
         } else {
            switch (var3) {
               case 2:
                  return " FOR UPDATE NOWAIT ";
               default:
                  return " FOR UPDATE ";
            }
         }
      } catch (Exception var4) {
         throw new PersistenceRuntimeException(var4);
      }
   }

   public void disableTransactionStatusCheck() {
      try {
         this.beanManager.disableTransactionStatusCheck();
      } catch (Exception var2) {
         throw new PersistenceRuntimeException(var2);
      }
   }

   public void enableTransactionStatusCheck() {
      try {
         this.beanManager.enableTransactionStatusCheck();
      } catch (Exception var2) {
         throw new PersistenceRuntimeException(var2);
      }
   }

   public void registerModifiedBean(Object var1) {
      try {
         Transaction var2 = TxHelper.getTransactionManager().getTransaction();
         this.beanManager.registerModifiedBean(var1, var2);
      } catch (RuntimeException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new EJBException(var4);
      }
   }

   public void registerInvalidatedBean(Object var1) {
      try {
         Object var2 = null;
         var2 = TxHelper.getTransactionManager().getTransaction();
         if (var2 == null) {
            var2 = Thread.currentThread();
         }

         this.beanManager.registerInvalidatedBean(var1, var2);
      } catch (RuntimeException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new EJBException(var4);
      }
   }

   public void unregisterModifiedBean(Object var1) {
      try {
         Transaction var2 = TxHelper.getTransactionManager().getTransaction();
         this.beanManager.unregisterModifiedBean(var1, var2);
      } catch (RuntimeException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new EJBException(var4);
      }
   }

   public void flushModifiedBeans() {
      try {
         Transaction var1 = TxHelper.getTransactionManager().getTransaction();
         this.beanManager.flushModifiedBeans(var1);
      } catch (RuntimeException var2) {
         throw var2;
      } catch (Exception var3) {
         throw new EJBException(var3);
      }
   }

   public void registerM2NJoinTableInsert(String var1, Object var2) {
      try {
         Transaction var3 = TxHelper.getTransactionManager().getTransaction();
         this.beanManager.registerM2NJoinTableInsert(var1, var2, var3);
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new EJBException(var5);
      }
   }

   public void releaseResources(Connection var1, Statement var2, ResultSet var3) {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("RDBMSPersistenceManager.releaseResources");
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

   public void releaseArrayResources(Connection var1, Statement[] var2, ResultSet[] var3) {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("RDBMSPersistenceManager.releaseResources");
      }

      int var4;
      if (var3 != null) {
         for(var4 = 0; var4 < var3.length; ++var4) {
            try {
               this.releaseResultSet(var3[var4]);
            } catch (SQLException var8) {
            }
         }
      }

      if (var2 != null) {
         for(var4 = 0; var4 < var2.length; ++var4) {
            try {
               this.releaseStatement(var2[var4]);
            } catch (SQLException var7) {
            }
         }
      }

      try {
         this.releaseConnection(var1);
      } catch (SQLException var6) {
      }

   }

   public void releaseConnection(Connection var1) throws SQLException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("RDBMSPersistenceManager.releaseConnection");
      }

      if (var1 != null) {
         var1.close();
      }

   }

   public void releaseStatement(PreparedStatement var1) throws SQLException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("RDBMSPersistenceManager.releaseStatement");
      }

      if (var1 != null) {
         var1.close();
      }

   }

   public void releaseStatement(Statement var1) throws SQLException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("RDBMSPersistenceManager.releaseStatement");
      }

      if (var1 != null) {
         var1.close();
      }

   }

   public void releaseResultSet(ResultSet var1) throws SQLException {
      if (runtimeLogger.isDebugEnabled()) {
         debugRuntime("RDBMSPersistenceManager.releaseResultSet");
      }

      if (var1 != null) {
         var1.close();
      }

   }

   public boolean createDefaultDBMSTable(String var1) throws WLDeploymentException {
      StringBuffer var2 = new StringBuffer("CREATE TABLE " + var1 + " (");
      Connection var3 = null;
      Statement var4 = null;

      try {
         if (!this.getSequenceTableColumns(var1, var2) && !this.getBeanOrJoinTableColumns(var1, var2)) {
            throw new RDBMSException(" Unknown Error while attempting to get DB Columns for table '" + var1 + "'");
         }

         var2.append(",");
         var2.append("WLS_TEMP");
         var2.append(" int");
         if (this.databaseType == 5) {
            var2.append(" NULL ");
         }

         var2.append(" )");
         String var5 = var2.toString();
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment(" full DEFAULT TABLE CREATE QUERY: '" + var5 + "'");
         }

         var3 = this.getConnection();
         var4 = var3.createStatement();
         var4.executeUpdate(var5);
      } catch (Exception var11) {
         Loggable var6 = EJBLogger.logerrorCreatingDefaultDBMSTableLoggable(var1, var11.getMessage());
         var6.log();
         throw new WLDeploymentException(var6.getMessage(), var11);
      } finally {
         this.releaseResources(var3, var4, (ResultSet)null);
      }

      return true;
   }

   public void dropAndCreateDefaultDBMSTable(String var1) throws WLDeploymentException {
      StringBuffer var2 = new StringBuffer("DROP TABLE " + var1);
      Connection var3 = null;
      Statement var4 = null;

      try {
         var3 = this.getConnection();
         var4 = var3.createStatement();
         var4.executeUpdate(var2.toString());
      } catch (Exception var10) {
      } finally {
         this.releaseResources(var3, var4, (ResultSet)null);
      }

      this.createDefaultDBMSTable(var1);
   }

   public void alterDefaultDBMSTable(String var1, Set var2, Set var3) throws WLDeploymentException {
      if (var2.isEmpty() && var3.isEmpty()) {
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("Table not changed so no alter table");
         }

      } else {
         switch (this.databaseType) {
            case 1:
               this.alterOracleDefaultDBMSTable(var1, var2, var3);
               break;
            case 2:
            case 7:
               this.alterMSSqlServerDefaultDBMSTable(var1, var2, var3);
               break;
            case 3:
               this.alterInformixDefaultDBMSTable(var1, var2, var3);
               break;
            case 4:
               Loggable var5 = EJBLogger.logalterTableNotSupportedLoggable("DB2");
               var5.log();
               throw new WLDeploymentException(var5.getMessage());
            case 5:
               Loggable var4 = EJBLogger.logalterTableNotSupportedLoggable("Sybase");
               var4.log();
               throw new WLDeploymentException(var4.getMessage());
            case 6:
               this.alterPointbaseDefaultDBMSTable(var1, var2, var3);
               break;
            case 8:
               this.alterMySQLDefaultDBMSTable(var1, var2, var3);
               break;
            default:
               this.alterOracleDefaultDBMSTable(var1, var2, var3);
         }

      }
   }

   private void alterPointbaseDefaultDBMSTable(String var1, Set var2, Set var3) throws WLDeploymentException {
      Set var4 = this.createPrimaryKeyCols(var1);
      boolean var5 = this.isAnyNewColAPKCol(var4, var2);
      if (var4.size() > 1 && var5) {
         EJBLogger.logalterTableNotSupportedForPointbaseLoggable();
      } else {
         this.addPointbaseColumns(var1, var2, var4, var5);
         this.removePointbaseColumns(var1, var3);
         this.dropMSSqlServerPrimaryKeyConstraint(var1);
         this.createMSSqlServerPrimaryKeyConstraint(var1);
      }
   }

   private boolean isAnyNewColAPKCol(Set var1, Set var2) {
      Iterator var3 = var2.iterator();

      String var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (String)var3.next();
      } while(!var1.contains(var4));

      return true;
   }

   private List getOldPrimaryKeys(Connection var1, String var2) {
      try {
         DatabaseMetaData var3 = var1.getMetaData();
         ResultSet var4 = var3.getPrimaryKeys((String)null, (String)null, var2.toUpperCase(Locale.ENGLISH));
         ArrayList var5 = new ArrayList();

         while(var4.next()) {
            var5.add(var4.getString("COLUMN_NAME"));
         }

         return var5;
      } catch (Exception var6) {
         return null;
      }
   }

   private void addPointbaseColumns(String var1, Set var2, Set var3, boolean var4) throws WLDeploymentException {
      Connection var5 = null;
      Statement var6 = null;

      try {
         var5 = this.getConnection();
         List var7 = null;
         boolean var18 = false;
         if (var4) {
            var7 = this.getOldPrimaryKeys(var5, var1);
            if (var7 != null) {
               if (var7.size() > 1) {
                  EJBLogger.logalterTableNotSupportedForPointbaseLoggable();
                  return;
               }

               if (var7.size() == 1) {
                  var18 = true;
               }
            }
         }

         StringBuffer var9 = new StringBuffer("alter table " + var1 + " ");
         if (!var2.isEmpty()) {
            Iterator var10 = var2.iterator();

            while(var10.hasNext()) {
               var9.append("add  ");
               String var11 = (String)var10.next();
               var9.append(var11 + " " + this.getSqltypeForCol(var1, var11));
               if (var4 && var3.contains(var11) && var18) {
                  var9.append(" DEFAULT '" + var7.get(0) + "' NOT NULL ");
               }

               if (var10.hasNext()) {
                  var9.append(",");
               }
            }

            if (deploymentLogger.isDebugEnabled()) {
               debugDeployment("The alter table command is ..." + var9);
            }

            var6 = var5.createStatement();
            var6.executeUpdate(var9.toString());
         }
      } catch (Exception var16) {
         Loggable var8 = EJBLogger.logerrorAlteringDefaultDBMSTableLoggable(var1, var16.getMessage());
         var8.log();
         throw new WLDeploymentException(var8.getMessage(), var16);
      } finally {
         this.releaseResources(var5, var6, (ResultSet)null);
      }
   }

   private void removePointbaseColumns(String var1, Set var2) throws WLDeploymentException {
      StringBuffer var3 = new StringBuffer("alter table " + var1 + " ");
      if (!var2.isEmpty()) {
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            var3.append("drop  ");
            String var5 = (String)var4.next();
            var3.append(var5);
            if (var4.hasNext()) {
               var3.append(",");
            }
         }

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
            throw new WLDeploymentException(var7.getMessage(), var12);
         } finally {
            this.releaseResources(var14, var15, (ResultSet)null);
         }

      }
   }

   private void alterMySQLDefaultDBMSTable(String var1, Set var2, Set var3) throws WLDeploymentException {
      Set var4 = this.createPrimaryKeyCols(var1);
      boolean var5 = this.isAnyNewColAPKCol(var4, var2);
      if (var4.size() > 1 && var5) {
         EJBLogger.logalterTableNotSupportedForPointbaseLoggable();
      } else {
         this.addMySQLColumns(var1, var2, var4, var5);
         this.removeMySQLColumns(var1, var3);
         this.dropMSSqlServerPrimaryKeyConstraint(var1);
         this.createMSSqlServerPrimaryKeyConstraint(var1);
      }
   }

   private void addMySQLColumns(String var1, Set var2, Set var3, boolean var4) throws WLDeploymentException {
      Connection var5 = null;
      Statement var6 = null;

      try {
         var5 = this.getConnection();
         List var7 = null;
         boolean var18 = false;
         if (var4) {
            var7 = this.getOldPrimaryKeys(var5, var1);
            if (var7 != null) {
               if (var7.size() > 1) {
                  EJBLogger.logalterTableNotSupportedForPointbaseLoggable();
                  return;
               }

               if (var7.size() == 1) {
                  var18 = true;
               }
            }
         }

         StringBuffer var9 = new StringBuffer("alter table " + var1 + " ");
         if (var2.isEmpty()) {
            return;
         }

         Iterator var10 = var2.iterator();

         while(var10.hasNext()) {
            String var11 = (String)var10.next();
            var9.append("add column ");
            var9.append(var11 + " " + this.getSqltypeForCol(var1, var11));
            if (var4 && var3.contains(var11) && var18) {
               var9.append(" DEFAULT '" + var7.get(0) + "' NOT NULL ");
            }

            if (var10.hasNext()) {
               var9.append(",");
            }
         }

         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("The alter table command is ..." + var9);
         }

         var6 = var5.createStatement();
         var6.executeUpdate(var9.toString());
      } catch (Exception var16) {
         Loggable var8 = EJBLogger.logerrorAlteringDefaultDBMSTableLoggable(var1, var16.getMessage());
         var8.log();
         throw new WLDeploymentException(var8.getMessage(), var16);
      } finally {
         this.releaseResources(var5, var6, (ResultSet)null);
      }

   }

   private void removeMySQLColumns(String var1, Set var2) throws WLDeploymentException {
      StringBuffer var3 = new StringBuffer("alter table " + var1 + " ");
      if (!var2.isEmpty()) {
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var3.append("drop column ");
            var3.append(var5);
            if (var4.hasNext()) {
               var3.append(",");
            }
         }

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
            throw new WLDeploymentException(var7.getMessage(), var12);
         } finally {
            this.releaseResources(var14, var15, (ResultSet)null);
         }

      }
   }

   private void alterMSSqlServerDefaultDBMSTable(String var1, Set var2, Set var3) throws WLDeploymentException {
      this.dropMSSqlServerPrimaryKeyConstraint(var1);
      this.addMSSqlServerColumns(var1, var2);
      this.removeMSSqlServerColumns(var1, var3);
      this.createMSSqlServerPrimaryKeyConstraint(var1);
   }

   private void dropMSSqlServerPrimaryKeyConstraint(String var1) throws WLDeploymentException {
      StringBuffer var2 = new StringBuffer("alter table ");
      var2.append(var1);
      var2.append(" drop CONSTRAINT pk_" + var1);
      Connection var3 = null;
      Statement var4 = null;
      if (deploymentLogger.isDebugEnabled()) {
         debugDeployment("the alter table command is .." + var2);
      }

      try {
         var3 = this.getConnection();
         var4 = var3.createStatement();
         var4.executeUpdate(var2.toString());
      } catch (Exception var10) {
      } finally {
         this.releaseResources(var3, var4, (ResultSet)null);
      }

   }

   private void createMSSqlServerPrimaryKeyConstraint(String var1) throws WLDeploymentException {
      Set var2 = this.createPrimaryKeyCols(var1);
      if (var2.size() > 0) {
         StringBuffer var3 = new StringBuffer("alter table ");
         var3.append(var1);
         var3.append(" add constraint pk_" + var1 + " PRIMARY KEY(");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var3.append(var5);
            if (var4.hasNext()) {
               var3.append(", ");
            }
         }

         var3.append(")");
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("the alter table command is .." + var3);
         }

         Connection var15 = null;
         Statement var6 = null;

         try {
            var15 = this.getConnection();
            var6 = var15.createStatement();
            var6.executeUpdate(var3.toString());
         } catch (Exception var13) {
            Loggable var8 = EJBLogger.logerrorAlteringDefaultDBMSTableLoggable(var1, var13.getMessage());
            var8.log();
            throw new WLDeploymentException(var8.getMessage(), var13);
         } finally {
            this.releaseResources(var15, var6, (ResultSet)null);
         }
      }

   }

   private void removeMSSqlServerColumns(String var1, Set var2) throws WLDeploymentException {
      StringBuffer var3 = new StringBuffer("alter table " + var1 + " ");
      if (!var2.isEmpty()) {
         var3.append("drop column ");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var3.append(var5);
            if (var4.hasNext()) {
               var3.append(",");
            }
         }

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
            throw new WLDeploymentException(var7.getMessage(), var12);
         } finally {
            this.releaseResources(var14, var15, (ResultSet)null);
         }

      }
   }

   private void addMSSqlServerColumns(String var1, Set var2) throws WLDeploymentException {
      StringBuffer var3 = new StringBuffer("alter table " + var1 + " ");
      if (!var2.isEmpty()) {
         var3.append("add  ");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var3.append(var5 + " " + this.getSqltypeForCol(var1, var5));
            if (var4.hasNext()) {
               var3.append(",");
            }
         }

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
            throw new WLDeploymentException(var7.getMessage(), var12);
         } finally {
            this.releaseResources(var14, var15, (ResultSet)null);
         }

      }
   }

   private void alterInformixDefaultDBMSTable(String var1, Set var2, Set var3) throws WLDeploymentException {
      this.addOracleColumns(var1, var2);
      this.removeOracleColumns(var1, var3);
      this.alterInformixPrimaryKeyConstraints(var1);
   }

   private void alterOracleDefaultDBMSTable(String var1, Set var2, Set var3) throws WLDeploymentException {
      this.addOracleColumns(var1, var2);
      this.removeOracleColumns(var1, var3);
      this.alterPrimaryKeyConstraints(var1, var2, var3);
   }

   private void alterInformixPrimaryKeyConstraints(String var1) throws WLDeploymentException {
      Set var2 = this.createPrimaryKeyCols(var1);
      if (var2.size() > 0) {
         this.createInformixPrimaryKeyConstraint(var1, var2);
      }

   }

   private void createInformixPrimaryKeyConstraint(String var1, Set var2) throws WLDeploymentException {
      if (var2.size() > 0) {
         StringBuffer var3 = new StringBuffer("alter table ");
         var3.append(var1);
         var3.append(" add constraint PRIMARY KEY (");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var3.append(var5);
            if (var4.hasNext()) {
               var3.append(", ");
            }
         }

         var3.append(") CONSTRAINT pk_" + var1);
         Connection var15 = null;
         Statement var6 = null;

         try {
            var15 = this.getConnection();
            var6 = var15.createStatement();
            var6.executeUpdate(var3.toString());
         } catch (Exception var13) {
            Loggable var8 = EJBLogger.logerrorAlteringDefaultDBMSTableLoggable(var1, var13.getMessage());
            throw new WLDeploymentException(var8.getMessage(), var13);
         } finally {
            this.releaseResources(var15, var6, (ResultSet)null);
         }
      }

   }

   private void alterPrimaryKeyConstraints(String var1, Set var2, Set var3) throws WLDeploymentException {
      Set var4 = this.createPrimaryKeyCols(var1);
      if (var4.size() > 0) {
         this.dropOraclePrimaryKeyConstraint(var1);
         this.createOraclePrimaryKeyConstraint(var1, var4);
      }

   }

   private void createOraclePrimaryKeyConstraint(String var1, Set var2) throws WLDeploymentException {
      if (var2.size() > 0) {
         StringBuffer var3 = new StringBuffer("alter table ");
         var3.append(var1);
         var3.append(" add constraint pk_" + var1 + " PRIMARY KEY(");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var3.append(var5);
            if (var4.hasNext()) {
               var3.append(", ");
            }
         }

         var3.append(")");
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("The alter command is ..." + var3.toString());
         }

         Connection var15 = null;
         Statement var6 = null;

         try {
            var15 = this.getConnection();
            var6 = var15.createStatement();
            var6.executeUpdate(var3.toString());
         } catch (Exception var13) {
            Loggable var8 = EJBLogger.logerrorAlteringDefaultDBMSTableLoggable(var1, var13.getMessage());
            var8.log();
            throw new WLDeploymentException(var8.getMessage(), var13);
         } finally {
            this.releaseResources(var15, var6, (ResultSet)null);
         }
      }

   }

   private void dropOraclePrimaryKeyConstraint(String var1) throws WLDeploymentException {
      StringBuffer var2 = new StringBuffer("alter table ");
      var2.append(var1);
      var2.append(" drop PRIMARY KEY CASCADE");
      Connection var3 = null;
      Statement var4 = null;
      if (deploymentLogger.isDebugEnabled()) {
         debugDeployment("the alter command is ..." + var2);
      }

      try {
         var3 = this.getConnection();
         var4 = var3.createStatement();
         var4.executeUpdate(var2.toString());
      } catch (Exception var10) {
      } finally {
         this.releaseResources(var3, var4, (ResultSet)null);
      }

   }

   private void removeOracleColumns(String var1, Set var2) throws WLDeploymentException {
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
            throw new WLDeploymentException(var7.getMessage(), var12);
         } finally {
            this.releaseResources(var14, var15, (ResultSet)null);
         }

      }
   }

   private void addOracleColumns(String var1, Set var2) throws WLDeploymentException {
      StringBuffer var3 = new StringBuffer("alter table " + var1 + " ");
      if (!var2.isEmpty()) {
         var3.append("add ( ");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var3.append(var5 + " " + this.getSqltypeForCol(var1, var5));
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
            this.releaseResources(var14, var15, (ResultSet)null);
         }

      }
   }

   private static void debugDeployment(String var0) {
      deploymentLogger.debug("[RDBMSPersistenceManager] " + var0);
   }

   private static void debugRuntime(String var0) {
      runtimeLogger.debug("[RDBMSPersistenceManager] " + var0);
   }

   private static void debugRuntime(String var0, Throwable var1) {
      runtimeLogger.debug("[RDBMSPersistenceManager] " + var0, var1);
   }

   public RDBMSBean getRDBMSBean() {
      return this.rbean;
   }

   public int getDatabaseType() {
      return this.databaseType;
   }

   private void initializeDBProductAndDriverInfo() {
      Connection var1 = null;

      try {
         var1 = this.getConnection();
         DatabaseMetaData var2 = var1.getMetaData();
         this.driverName = var2.getDriverName();
         this.driverVersion = var2.getDriverVersion();
         this.databaseProductName = var2.getDatabaseProductName();
         this.databaseProductVersion = var2.getDatabaseProductVersion();
         this.driverMajorVersion = var2.getDriverMajorVersion();
         this.driverMinorVersion = var2.getDriverMinorVersion();
      } catch (SQLException var8) {
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("SQLException while initializing DatabaseMetaData related product/driver info: " + var8.getMessage());
         }
      } catch (Throwable var9) {
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("Exception while initializing DatabaseMetaData related product/driver info: " + var9.getMessage());
         }
      } finally {
         if (deploymentLogger.isDebugEnabled()) {
            debugDeployment("Printing the DatabaseMetadata related product/driver info");
            if (var1 != null) {
               debugDeployment("Connection class is " + var1.getClass().getName());
            }

            debugDeployment("DatabaseProductName is  :  " + this.databaseProductName);
            debugDeployment("DatabaseProductVersion is  :  " + this.databaseProductVersion);
            debugDeployment("DriverName is  :  " + this.driverName);
            debugDeployment("DriverVersion is  :  " + this.driverVersion);
            debugDeployment("DriverMajorVersion is  :  " + this.driverMajorVersion);
            debugDeployment("DriverMinorVersion is  :  " + this.driverMinorVersion);
         }

         this.releaseResources(var1, (Statement)null, (ResultSet)null);
      }

   }

   public boolean perhapsUseSetStringForClobForOracle() {
      return this.driverName.equalsIgnoreCase("Oracle JDBC driver") && this.driverMajorVersion >= 10;
   }

   static {
      deploymentLogger = EJBDebugService.cmpDeploymentLogger;
      runtimeLogger = EJBDebugService.cmpRuntimeLogger;
      byteArray = new byte[0];
      charArray = new char[0];
   }
}
