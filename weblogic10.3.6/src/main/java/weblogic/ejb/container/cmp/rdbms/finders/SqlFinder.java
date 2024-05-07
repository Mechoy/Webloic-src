package weblogic.ejb.container.cmp.rdbms.finders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EntityBean;
import javax.ejb.FinderException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.dbeans.ConversationImpl;
import weblogic.dbeans.DataBeansException;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.ejb.container.cmp.rdbms.SqlShape;
import weblogic.ejb.container.cmp.rdbms.codegen.CodeGenUtils;
import weblogic.ejb.container.dd.DDConstants;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.internal.QueryCachingHandler;
import weblogic.ejb.container.internal.TxManager;
import weblogic.ejb.container.manager.BaseEntityManager;
import weblogic.ejb.container.manager.TTLManager;
import weblogic.ejb.container.persistence.spi.CMPBean;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.cmp.rdbms.finders.InvalidFinderException;
import weblogic.jdbc.rowset.WLCachedRowSet;
import weblogic.logging.Loggable;
import weblogic.transaction.TransactionHelper;
import weblogic.utils.AssertionError;

public class SqlFinder extends Finder {
   private Method method;
   private Method secondMethod;
   private Map sqlQueries;
   private String sqlShapeName;
   private String query;
   private RDBMSBean rdbmsBean;
   private ConversationImpl conversation;
   private int[] query2method;
   private boolean initialized = false;
   private int columnCount = 0;
   private Field[] columnFields;
   private Method[] columnMethods;
   private int[] columnIndices;
   private boolean[] columnSetsPrimaryKey;
   private Field[] columnPrimaryKeyFields;
   private Field[] columnOptimisticFields;
   private Method[] columnOptimisticMethods;
   private String[] columnTypes;
   private Class[] columnClasses;
   private int[] columnIsLoadedIndices;
   private BaseEntityManager[] resultManagers;
   private RDBMSBean[] resultBeans;
   private int[] relationIndex1;
   private int[] relationIndex2;
   private Method[] relationMethod1;
   private Method[] relationMethod2;
   private TransactionHelper txHelper = TransactionHelper.getTransactionHelper();
   private List externalMethodParmList = null;
   private boolean queryTypeInitialized = false;
   private boolean usesStoredProcedure = false;
   private boolean usesStoredFunction = false;
   private boolean usesRelationshipCaching = false;
   private boolean isDynamicFinder = false;
   private String[] cmrFieldFinderMethodNames1;
   private String[] cmrFieldFinderMethodNames2;
   private int[] cmrFieldFinderReturnTypes1;
   private int[] cmrFieldFinderReturnTypes2;
   private static final Pattern FROM_PATTERN = Pattern.compile("(?i)\\bfrom\\s+([a-zA-Z_0-9.]+)");
   private static int NO_MAPPING = -1;

   public SqlFinder(String var1, Map var2, String var3, RDBMSBean var4) throws InvalidFinderException {
      super(var1, true);
      this.sqlShapeName = var3;
      this.sqlQueries = var2;
      this.rdbmsBean = var4;
      this.conversation = null;
   }

   public SqlFinder(String var1, Map var2, String var3, RDBMSBean var4, ConversationImpl var5) throws InvalidFinderException {
      super(var1, true);
      this.sqlShapeName = var3;
      this.sqlQueries = var2;
      this.rdbmsBean = var4;
      this.conversation = var5;
   }

   public void setMethods(Method[] var1) throws Exception {
      assert var1[0] != null;

      super.setMethod(var1[0]);
      this.setParameterClassTypes(var1[0].getParameterTypes());
      this.method = var1[0];
      this.secondMethod = var1[1];
   }

   public void setup(int var1) throws WLDeploymentException {
      this.query = (String)this.sqlQueries.get(new Integer(var1));
      if (this.query == null) {
         this.query = (String)this.sqlQueries.get(new Integer(0));
         if (this.query == null) {
            throw new WLDeploymentException("Error: no SQL query specified for " + DDConstants.getDBNameForType(var1) + "database type in finder method " + this.getName() + "of EJB " + this.rdbmsBean.getEjbName());
         }
      }

      if (Pattern.matches("\\{\\s*call.*\\}", this.query)) {
         this.usesStoredProcedure = true;
      } else if (Pattern.matches("\\{\\s*\\?\\s*=\\s*call.*\\}", this.query)) {
         this.usesStoredFunction = true;
      }

      if ((this.usesStoredProcedure || this.usesStoredFunction) && !this.hasSqlShape()) {
         Loggable var9 = EJBLogger.logNoSqlShapeSpecifiedLoggable(this.getName());
         throw new EJBException(var9.getMessage());
      } else {
         this.sqlQuery = this.query;
         this.sqlQueryForUpdate = this.query;
         this.sqlQueryForUpdateNoWait = this.query;
         Pattern var2 = Pattern.compile("\\?\\d+");
         int var3 = 0;
         ArrayList var4 = new ArrayList();
         Matcher var5 = var2.matcher(this.query);
         if (debugLogger.isDebugEnabled()) {
            debug("starting query- " + this.query);
         }

         while(var5.find(var3)) {
            String var6 = var5.group();
            if (debugLogger.isDebugEnabled()) {
               debug("parameter- " + var6);
            }

            var3 = var5.end();
            String var7 = var6.substring(1);
            Integer var8 = new Integer(Integer.parseInt(var7) - 1);
            var4.add(var8);
         }

         this.query2method = new int[var4.size()];

         for(int var10 = 0; var10 < var4.size(); ++var10) {
            this.query2method[var10] = (Integer)var4.get(var10);
            if (debugLogger.isDebugEnabled()) {
               debug("query indghostlyex- " + var10 + "method index- " + this.query2method[var10]);
            }
         }

         this.query = var5.replaceAll(" ? ");
         if (debugLogger.isDebugEnabled()) {
            debug("ending query- " + this.query);
         }

         if (this.sqlShapeName != null) {
            SqlShape var11 = this.rdbmsBean.getSqlShape(this.sqlShapeName);
            String[] var12 = var11.getEjbRelationNames();
            this.usesRelationshipCaching = var12 != null;
         }

      }
   }

   public void setupDynamic(String var1) throws WLDeploymentException {
      this.query = var1;
      Pattern var2 = Pattern.compile("\\?");
      int var3 = 0;
      int var4 = 0;
      ArrayList var5 = new ArrayList();
      Matcher var6 = var2.matcher(this.query);
      if (debugLogger.isDebugEnabled()) {
         debug("starting query- " + this.query);
      }

      while(var6.find(var3)) {
         String var7 = var6.group();
         if (debugLogger.isDebugEnabled()) {
            debug("parameter- " + var7);
         }

         var3 = var6.end();
         var5.add(new Integer(var4++));
      }

      this.query2method = new int[var5.size()];

      for(int var8 = 0; var8 < var5.size(); ++var8) {
         this.query2method[var8] = (Integer)var5.get(var8);
         if (debugLogger.isDebugEnabled()) {
            debug("query indghostlyex- " + var8 + "method index- " + this.query2method[var8]);
         }
      }

      this.isDynamicFinder = true;
      if (debugLogger.isDebugEnabled()) {
         debug("ending query- " + this.query);
      }

   }

   private boolean alreadyMapped(List var1, int var2, String var3, String var4) {
      if (var2 == NO_MAPPING) {
         return false;
      } else {
         Map var5 = (Map)var1.get(var2);
         List var6 = (List)var5.get(var3);
         return var6 == null ? false : var6.contains(var4);
      }
   }

   private void updateMapped(List var1, int var2, String var3, String var4) {
      Map var5 = (Map)var1.get(var2);

      assert var5 != null;

      Object var6 = (List)var5.get(var3);
      if (var6 == null) {
         var6 = new ArrayList();
         var5.put(var3, var6);
      }

      if (!((List)var6).contains(var4)) {
         ((List)var6).add(var4);
      }

   }

   private boolean hasSqlShape() {
      return this.sqlShapeName != null;
   }

   private void getSqlMetadata(ResultSet var1, List var2, List var3, List var4) throws SQLException {
      ResultSetMetaData var5 = var1.getMetaData();
      int var6 = var5.getColumnCount();
      if (this.hasSqlShape()) {
         SqlShape var13 = this.rdbmsBean.getSqlShape(this.sqlShapeName);
         Iterator var14 = var13.getTables().iterator();

         while(true) {
            while(var14.hasNext()) {
               SqlShape.Table var15 = (SqlShape.Table)var14.next();
               if (var15 == null) {
                  var2.add((Object)null);
                  var3.add((Object)null);
                  var4.add((Object)null);
               } else {
                  Iterator var18 = var15.getColumns().iterator();

                  while(var18.hasNext()) {
                     String var11 = (String)var18.next();
                     var2.add(var11);
                     var3.add(var15.getName());
                     var4.add(var15.getEjbRelationshipRoleNames());
                  }
               }
            }

            for(int var16 = 0; var16 < var13.getPassThroughColumns(); ++var16) {
               var2.add((Object)null);
               var3.add((Object)null);
               var4.add((Object)null);
            }

            if (var2.size() != var6) {
               Loggable var17 = EJBLogger.logErrorExecuteFinderLoggable(this.getName(), var13.getSqlShapeName(), var2.size() + "", var6 + "");
               throw new EJBException(var17.getMessage());
            }
            break;
         }
      } else {
         String var7 = null;
         if (this.isSingletonSelect()) {
            if (var6 != 1) {
               throw new EJBException("Too many columns (" + var6 + ") selected by query '" + this.query + "'.  " + this.getName() + " returns a value of type " + this.getReturnClassType().getName() + " and requires and SQL query that selects a single value.");
            }

            var2.add((Object)null);
            var3.add((Object)null);
         } else {
            for(int var8 = 1; var8 <= var6; ++var8) {
               String var9 = null;
               String var10 = null;

               try {
                  var9 = var5.getColumnName(var8);
                  var10 = var5.getTableName(var8);
               } catch (Exception var12) {
               }

               if (var9 == null || var9.length() == 0) {
                  throw new EJBException("Unknown column name for column- " + var9 + "of query- " + this.query + ".  Use a SqlShape element to specify the column name.");
               }

               if (var10 == null || var10.length() == 0) {
                  if (var7 == null) {
                     var7 = this.guessTableName();
                     if (var7 == null || var7.length() == 0) {
                        throw new EJBException("Unknown table name for column- " + var9 + "of query- " + this.query + ".  Use a SqlShape element to specifying the table name.");
                     }

                     var7 = var7.trim();
                  }

                  var10 = var7;
               }

               var2.add(var9);
               var3.add(var10);
               var4.add((Object)null);
            }
         }
      }

   }

   private String guessTableName() {
      Matcher var1 = FROM_PATTERN.matcher(this.query);
      if (!var1.find()) {
         return null;
      } else {
         String var2 = var1.group(1);
         return var2;
      }
   }

   public synchronized void initializeMapping(ResultSet var1) throws FinderException, SQLException {
      if (!this.initialized) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         ArrayList var4 = new ArrayList();
         HashMap var5 = new HashMap();
         HashMap var6 = new HashMap();
         this.getSqlMetadata(var1, var2, var3, var4);
         this.columnCount = var2.size();
         if (debugLogger.isDebugEnabled()) {
            debug("column count-" + this.columnCount);
         }

         ArrayList var7 = new ArrayList();
         ArrayList var8 = new ArrayList();
         ArrayList var9 = new ArrayList();
         ArrayList var10 = new ArrayList();
         ArrayList var11 = new ArrayList();
         this.columnFields = new Field[this.columnCount];
         this.columnMethods = new Method[this.columnCount];
         this.columnIndices = new int[this.columnCount];
         this.columnSetsPrimaryKey = new boolean[this.columnCount];
         this.columnPrimaryKeyFields = new Field[this.columnCount];
         this.columnIsLoadedIndices = new int[this.columnCount];
         this.columnOptimisticFields = new Field[this.columnCount];
         this.columnOptimisticMethods = new Method[this.columnCount];
         this.columnTypes = new String[this.columnCount];
         this.columnClasses = new Class[this.columnCount];

         int var12;
         for(var12 = 0; var12 < this.columnCount; ++var12) {
            if (debugLogger.isDebugEnabled()) {
               debug("current column- " + var12);
            }

            String var13 = (String)var2.get(var12);
            String var14 = (String)var3.get(var12);
            if (debugLogger.isDebugEnabled()) {
               debug("column name-" + var13);
               debug("table name-" + var14);
            }

            List var15 = null;
            boolean var16 = false;
            if (var14 == null && var13 == null) {
               this.columnIndices[var12] = var9.size();
               var7.add((Object)null);
               var8.add((Object)null);
               var9.add((Object)null);
               var10.add((Object)null);
               var11.add((Object)null);
               if (this.isSingletonSelect()) {
                  this.columnClasses[var12] = this.getReturnClassType();
               } else {
                  this.columnClasses[var12] = Object.class;
               }

               var16 = true;
            }

            if (!var16) {
               var15 = (List)var4.get(var12);
               int var17 = 0;

               for(Iterator var18 = var8.iterator(); var18.hasNext() && !var16; var16 = this.mapTableAndColumn((RDBMSBean)var18.next(), var14, var13, var15, var12, var7, var8, var9, var10, var11, var17++, var5, var6)) {
               }
            }

            if (!var16) {
               var16 = this.mapTableAndColumn(this.rdbmsBean, var14, var13, var15, var12, var7, var8, var9, var10, var11, NO_MAPPING, var5, var6);
            }

            RDBMSBean var21;
            if (!var16) {
               for(Iterator var19 = this.rdbmsBean.getRdbmsBeanMap().values().iterator(); var19.hasNext() && !var16; var16 = this.mapTableAndColumn(var21, var14, var13, var15, var12, var7, var8, var9, var10, var11, NO_MAPPING, var5, var6)) {
                  var21 = (RDBMSBean)var19.next();
               }
            }

            if (!var16) {
               Loggable var20 = EJBLogger.logErrorMapColumnLoggable(var14, var13, this.query);
               throw new FinderException(var20.getMessage());
            }
         }

         this.resultManagers = (BaseEntityManager[])((BaseEntityManager[])var7.toArray(new BaseEntityManager[0]));
         this.resultBeans = (RDBMSBean[])((RDBMSBean[])var8.toArray(new RDBMSBean[0]));
         this.validateMapping(this.resultBeans, var9);
         this.initializeRelationshipCaching(var8, var11);
         if (this.isQueryCachingEnabled()) {
            for(var12 = 0; var12 < this.resultManagers.length; ++var12) {
               if (this.resultManagers[var12] != null && !this.resultManagers[var12].isReadOnly()) {
                  this.log.logWarning(this.fmt.QUERY_CACHING_SQLFINDER_RETURNS_RW_BEAN(this.rdbmsBean.getEjbName(), this.getName(), this.resultManagers[var12].getBeanInfo().getEJBName()));
                  this.setQueryCachingEnabled(false);
               }
            }
         }

         this.initialized = true;
      }

      this.initializeTxListeners();
   }

   private void initializeRelationshipCaching(List var1, List var2) {
      if (this.sqlShapeName != null) {
         if (debugLogger.isDebugEnabled()) {
            debug("------------------initializeRelationshipCaching");
         }

         SqlShape var3 = this.rdbmsBean.getSqlShape(this.sqlShapeName);
         String[] var4 = var3.getEjbRelationNames();
         if (var4 != null) {
            this.relationIndex1 = new int[var4.length];
            this.relationIndex2 = new int[var4.length];
            this.relationMethod1 = new Method[var4.length];
            this.relationMethod2 = new Method[var4.length];
            this.cmrFieldFinderMethodNames1 = new String[var4.length];
            this.cmrFieldFinderMethodNames2 = new String[var4.length];
            this.cmrFieldFinderReturnTypes1 = new int[var4.length];
            this.cmrFieldFinderReturnTypes2 = new int[var4.length];

            for(int var5 = 0; var5 < var4.length; ++var5) {
               EjbRelation var6 = this.rdbmsBean.getEjbRelation(var4[var5]);
               if (debugLogger.isDebugEnabled()) {
                  debug("------------------relation name" + var6.getEjbRelationName());
               }

               Iterator var7 = var6.getAllEjbRelationshipRoles().iterator();
               EjbRelationshipRole var8 = (EjbRelationshipRole)var7.next();
               EjbRelationshipRole var9 = (EjbRelationshipRole)var7.next();
               RoleSource var10 = var8.getRoleSource();
               RoleSource var11 = var9.getRoleSource();
               String var12 = var10.getEjbName();
               String var13 = var11.getEjbName();
               int var14 = this.getIndex(var12, var8.getName(), var1, var2, NO_MAPPING, var12.equals(var13), var6.getEjbRelationName());
               int var15 = this.getIndex(var13, var9.getName(), var1, var2, var14, var12.equals(var13), var6.getEjbRelationName());

               assert var14 != var15;

               this.relationIndex1[var5] = var14;
               this.relationIndex2[var5] = var15;
               String var16 = RDBMSUtils.getCmrFieldName(var8, var9);
               String var17 = RDBMSUtils.getCmrFieldName(var9, var8);
               String var18 = CodeGenUtils.cacheRelationshipMethodName(var16);
               String var19 = CodeGenUtils.cacheRelationshipMethodName(var17);
               this.setRelationMethod(this.relationMethod1, var5, var14, var18);
               this.setRelationMethod(this.relationMethod2, var5, var15, var19);
               Class var20;
               if (this.isQueryCachingEnabled() || this.getRDBMSBean(var14).isQueryCachingEnabledForCMRField(var16)) {
                  this.cmrFieldFinderMethodNames1[var5] = CodeGenUtils.getCMRFieldFinderMethodName(this.getRDBMSBean(var14), var16);
                  var20 = this.getRDBMSBean(var14).getCmrFieldClass(var16);
                  if (Set.class.isAssignableFrom(var20)) {
                     this.cmrFieldFinderReturnTypes1[var5] = 1;
                  } else {
                     this.cmrFieldFinderReturnTypes1[var5] = 3;
                  }

                  if (debugLogger.isDebugEnabled()) {
                     debug("CMRField1: " + var16);
                     debug("CMRFieldFinderMethod: " + this.cmrFieldFinderMethodNames1[var5]);
                     debug("CMRFieldFinderReturnType: " + this.cmrFieldFinderReturnTypes1[var5]);
                     debug("MGR: " + this.resultManagers[this.relationIndex2[var5]].getBeanInfo().getEJBName());
                  }
               }

               if (this.isQueryCachingEnabled() || this.getRDBMSBean(var15).isQueryCachingEnabledForCMRField(var17)) {
                  this.cmrFieldFinderMethodNames2[var5] = CodeGenUtils.getCMRFieldFinderMethodName(this.getRDBMSBean(var15), var17);
                  var20 = this.getRDBMSBean(var15).getCmrFieldClass(var17);
                  if (Set.class.isAssignableFrom(var20)) {
                     this.cmrFieldFinderReturnTypes2[var5] = 1;
                  } else {
                     this.cmrFieldFinderReturnTypes2[var5] = 3;
                  }

                  if (debugLogger.isDebugEnabled()) {
                     debug("CMRField: " + var17);
                     debug("CMRFieldFinderMethod: " + this.cmrFieldFinderMethodNames2[var5]);
                     debug("CMRFieldFinderReturnType: " + this.cmrFieldFinderReturnTypes2[var5]);
                     debug("MGR: " + this.resultManagers[this.relationIndex1[var5]].getBeanInfo().getEJBName());
                  }
               }
            }
         }

      }
   }

   private void setRelationMethod(Method[] var1, int var2, int var3, String var4) {
      ClientDrivenBeanInfo var5 = (ClientDrivenBeanInfo)this.resultManagers[var3].getBeanInfo();
      Class var6 = var5.getGeneratedBeanInterface();

      try {
         var1[var2] = var6.getMethod(var4, Object.class);
      } catch (NoSuchMethodException var8) {
         throw new AssertionError("the legal relationship setting method: name-" + var4);
      }
   }

   private int getIndex(String var1, String var2, List var3, List var4, int var5, boolean var6, String var7) {
      if (debugLogger.isDebugEnabled()) {
         debug("ejbName-" + var1);
         debug("role-" + var2);
         debug("taken-" + var5);
         debug("requiresRole-" + var6);
         debug("relationship-" + var7);
      }

      int var8;
      RDBMSBean var9;
      for(var8 = 0; var8 < var3.size(); ++var8) {
         var9 = (RDBMSBean)var3.get(var8);
         Set var10 = (Set)var4.get(var8);
         if (var1.equals(var9.getEjbName()) && var10.contains(var2) && (var5 == NO_MAPPING || var5 != var8)) {
            return var8;
         }
      }

      if (!var6) {
         for(var8 = 0; var8 < var3.size(); ++var8) {
            var9 = (RDBMSBean)var3.get(var8);
            if (var1.equals(var9.getEjbName()) && (var5 == NO_MAPPING || var5 != var8)) {
               return var8;
            }
         }
      }

      String var12 = "";

      for(int var13 = 0; var13 < var3.size(); ++var13) {
         RDBMSBean var15 = (RDBMSBean)var3.get(var13);
         Set var11 = (Set)var4.get(var13);
         if (var13 > 0) {
            var12 = var12 + ", ";
         }

         var12 = var12 + var15.getEjbName();
      }

      Loggable var14 = EJBLogger.logErrorMapRelatioshipLoggable(this.getName(), var7, var2, this.query, var12);
      throw new EJBException(var14.getMessage());
   }

   public void initializeTxListeners() {
      if (this.conversation != null) {
         Transaction var1 = this.txHelper.getTransaction();

         for(int var2 = 0; var2 < this.resultManagers.length; ++var2) {
            TxManager var3 = this.resultManagers[var2].getTxManager();

            try {
               var3.setConversation(var1, this.conversation);
            } catch (RollbackException var5) {
               throw new DataBeansException(var5);
            } catch (SystemException var6) {
               throw new DataBeansException(var6);
            }
         }
      }

   }

   private void validateMapping(RDBMSBean[] var1, List var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         RDBMSBean var4 = var1[var3];
         if (var4 != null) {
            CMPBeanDescriptor var5 = var4.getCMPBeanDescriptor();
            HashSet var6 = new HashSet(var5.getPrimaryKeyFieldNames());
            Map var7 = (Map)var2.get(var3);
            Iterator var8 = var7.keySet().iterator();

            String var9;
            Iterator var10;
            while(var8.hasNext()) {
               var9 = (String)var8.next();
               var10 = ((List)var7.get(var9)).iterator();

               while(var10.hasNext()) {
                  String var11 = (String)var10.next();
                  String var12 = var4.getCmpField(var9, var11);
                  var6.remove(var12);
               }
            }

            if (!var6.isEmpty()) {
               var9 = "";
               var10 = var6.iterator();

               while(var10.hasNext()) {
                  var9 = var9 + var10.next();
                  if (var10.hasNext()) {
                     var9 = var9 + ", ";
                  }
               }

               Loggable var13 = EJBLogger.logNotSelectForAllPrimaryKeyLoggable(this.getName(), this.query, var4.getEjbName(), var9);
               throw new EJBException(var13.getMessage());
            }
         }
      }

   }

   private String getTable(RDBMSBean var1, String var2, Map var3) {
      Object var4 = (Map)var3.get(var1.getEjbName());
      if (var4 == null) {
         var4 = new TreeMap(String.CASE_INSENSITIVE_ORDER);
         Iterator var5 = var1.getTables().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            ((Map)var4).put(var6, var6);
         }

         var3.put(var1.getEjbName(), var4);
      }

      return (String)((Map)var4).get(var2);
   }

   private String getColumn(RDBMSBean var1, String var2, String var3, Map var4) {
      Object var5 = (Map)var4.get(var1.getEjbName());
      if (var5 == null) {
         var5 = new TreeMap(String.CASE_INSENSITIVE_ORDER);
         var1.computeAllTableColumns((Map)var5);
         var4.put(var1.getEjbName(), var5);
      }

      Map var6 = (Map)((Map)var5).get(var2);
      return var6 == null ? null : (String)var6.get(var3);
   }

   private boolean mapTableAndColumn(RDBMSBean var1, String var2, String var3, List var4, int var5, List var6, List var7, List var8, List var9, List var10, int var11, Map var12, Map var13) {
      if (debugLogger.isDebugEnabled()) {
         debug("checking bean- " + var1.getEjbName());
      }

      boolean var14 = false;
      String var15 = this.getTable(var1, var2, var12);
      if (var15 != null) {
         var2 = var15;
         var15 = this.getColumn(var1, var15, var3, var13);
         if (var15 != null) {
            var3 = var15;
            if (!this.alreadyMapped(var8, var11, var2, var15)) {
               var14 = true;
            }
         }
      }

      if (var14) {
         if (debugLogger.isDebugEnabled()) {
            debug("bean as field for column-" + var3);
         }

         CMPBeanDescriptor var16 = var1.getCMPBeanDescriptor();
         BaseEntityManager var17 = var1.getRDBMSPersistenceManager().getBeanManager();
         if (var11 == NO_MAPPING) {
            if (debugLogger.isDebugEnabled()) {
               debug("beginning bean: EJB-" + var1.getEjbName());
            }

            this.columnIndices[var5] = var8.size();
            var6.add(var17);
            var7.add(var1);
            var8.add(new HashMap());
            var9.add(new HashSet());
            var10.add(new HashSet());
         } else {
            if (debugLogger.isDebugEnabled()) {
               debug("found column for bean- " + var1.getEjbName());
            }

            this.columnIndices[var5] = var11;
         }

         this.updateMapped(var8, this.columnIndices[var5], var2, var3);
         ClientDrivenBeanInfo var18 = (ClientDrivenBeanInfo)var17.getBeanInfo();
         Class var19 = var18.getGeneratedBeanClass();
         boolean var20 = var1.isOptimistic() && (var1.hasOptimisticColumn(var2) && var3.equalsIgnoreCase(var1.getOptimisticColumn(var2)) || !var1.hasOptimisticColumn(var2) && (!var1.hasCmpField(var2, var3) || !var1.isPrimaryKeyField(var1.getCmpField(var2, var3))));
         String var21 = null;
         String var22;
         Class var23;
         if (!var16.isBeanClassAbstract() && var1.hasCmpField(var2, var3)) {
            var22 = null;
            var23 = null;
            String var24 = null;
            var22 = var1.getCmpField(var2, var3);
            var23 = var16.getFieldClass(var22);
            var24 = "__WL_super_" + MethodUtils.setMethodName(var22);
            var21 = var22;

            try {
               this.columnMethods[var5] = var19.getMethod(var24, var23);
            } catch (NoSuchMethodException var28) {
               throw new AssertionError("illegal setter method: name-" + var24 + " argument type-" + var23.getName());
            }

            this.columnClasses[var5] = var23;
         } else {
            var21 = var1.getVariable(var2, var3);
            if (debugLogger.isDebugEnabled()) {
               debug("variable-" + var21);
            }

            try {
               this.columnFields[var5] = var19.getField(var21);
            } catch (NoSuchFieldException var29) {
               throw new AssertionError("illegal field value- " + var21);
            }

            this.columnClasses[var5] = this.columnFields[var5].getType();
         }

         if (var20) {
            var22 = CodeGenUtils.snapshotNameForVar(var21);
            if (debugLogger.isDebugEnabled()) {
               debug("-------------------------optimistic Field" + var22);
            }

            try {
               this.columnOptimisticFields[var5] = var19.getField(var22);
            } catch (NoSuchFieldException var27) {
               throw new AssertionError("illegal optimistic field value- " + var22);
            }
         }

         if (var1.hasCmpField(var21)) {
            if (var1.hasCmpColumnType(var21)) {
               this.columnTypes[var5] = var1.getCmpColumnTypeForField(var21);
            }

            if (var1.isPrimaryKeyField(var21)) {
               Set var30 = (Set)var9.get(this.columnIndices[var5]);
               if (!var30.contains(var21)) {
                  this.columnSetsPrimaryKey[var5] = true;
                  if (var16.hasComplexPrimaryKey()) {
                     var23 = var16.getPrimaryKeyClass();

                     try {
                        this.columnPrimaryKeyFields[var5] = var23.getField(var21);
                     } catch (NoSuchFieldException var26) {
                        throw new AssertionError("illegal primary key field value- " + var21);
                     }
                  }
               } else {
                  this.columnSetsPrimaryKey[var5] = false;
               }
            }
         }

         if (var4 != null) {
            ((List)var10.get(this.columnIndices[var5])).addAll(var4);
         }

         var22 = var1.getField(var21);
         this.columnIsLoadedIndices[var5] = var1.getIsModifiedIndex(var22);
      }

      return var14;
   }

   public String getQuery(int var1) {
      String var2 = null;
      switch (var1) {
         case 0:
            var2 = this.query;
            break;
         case 1:
            var2 = this.query;
            break;
         case 2:
            var2 = this.query;
            break;
         default:
            throw new AssertionError("Unknown selectForUpdate type: '" + var1 + "'");
      }

      return var2;
   }

   public Object[] getBeans() throws InternalException {
      Object[] var1 = new Object[this.resultManagers.length];

      for(int var2 = 0; var2 < this.resultManagers.length; ++var2) {
         if (this.resultManagers[var2] != null) {
            var1[var2] = this.resultManagers[var2].getBeanFromPool();
            ((CMPBean)var1[var2]).__WL_initialize();
         }
      }

      return var1;
   }

   public void releaseBeans(Object[] var1, int var2) {
      for(int var3 = var2; var3 < this.resultManagers.length; ++var3) {
         if (this.resultManagers[var3] != null) {
            this.resultManagers[var3].releaseBeanToPool((EntityBean)var1[var3]);
         }
      }

   }

   public Object[] getPrimaryKey() throws InstantiationException, IllegalAccessException {
      Object[] var1 = new Object[this.resultManagers.length];

      for(int var2 = 0; var2 < this.resultManagers.length; ++var2) {
         if (this.resultBeans[var2] != null && this.resultBeans[var2].getCMPBeanDescriptor().hasComplexPrimaryKey()) {
            var1[var2] = this.resultBeans[var2].getCMPBeanDescriptor().getPrimaryKeyClass().newInstance();
         }
      }

      return var1;
   }

   public Method getMethod() {
      return this.method;
   }

   public Method getSecondMethod() {
      return this.secondMethod;
   }

   public Field getField(int var1) {
      return this.columnFields[var1];
   }

   public boolean hasField(int var1) {
      return this.columnFields[var1] != null;
   }

   public Method getMethod(int var1) {
      return this.columnMethods[var1];
   }

   public int getNumQueryParams() {
      return this.query2method.length;
   }

   public int getMethodIndex(int var1) {
      assert var1 < this.query2method.length;

      return this.query2method[var1];
   }

   public int getColumnCount() {
      return this.columnCount;
   }

   public Class getColumnClass(int var1) {
      return this.columnClasses[var1];
   }

   public int getResultIndex(int var1) {
      return this.columnIndices[var1];
   }

   public boolean setsPrimaryKey(int var1) {
      return this.columnSetsPrimaryKey[var1];
   }

   public Field getPrimaryKeyField(int var1) {
      return this.columnPrimaryKeyFields[var1];
   }

   public int getIsLoadedIndex(int var1) {
      return this.columnIsLoadedIndices[var1];
   }

   public BaseEntityManager getManager(int var1) {
      return this.resultManagers[var1];
   }

   public RDBMSBean getRDBMSBean(int var1) {
      return this.resultBeans[var1];
   }

   public boolean isOptimistic(int var1) {
      return this.columnOptimisticFields[var1] != null;
   }

   public Field getOptimisticField(int var1) {
      return this.columnOptimisticFields[var1];
   }

   public boolean isCharArrayMappedToString(Class var1) {
      return this.rdbmsBean.isCharArrayMappedToString(var1);
   }

   private static void debug(String var0) {
      debugLogger.debug("[SqlFinder] " + var0);
   }

   public RDBMSBean getSelectBeanTarget() {
      return this.rdbmsBean;
   }

   public List getExternalMethodParmList() {
      if (this.externalMethodParmList == null) {
         Class[] var1 = this.method.getParameterTypes();
         this.externalMethodParmList = new ArrayList();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.externalMethodParmList.add(new ParamNode((RDBMSBean)null, "param" + var2, 0, var1[var2], (String)null, (String)null, false, false, (Class)null, false, false));
         }
      }

      return this.externalMethodParmList;
   }

   public List getExternalMethodAndInEntityParmList() {
      return this.getExternalMethodParmList();
   }

   public boolean isSingletonSelect() {
      return this.isSelect() && !Collection.class.isAssignableFrom(this.getReturnClassType()) && !ResultSet.class.isAssignableFrom(this.getReturnClassType()) && !EJBObject.class.isAssignableFrom(this.getReturnClassType()) && !EJBLocalObject.class.isAssignableFrom(this.getReturnClassType());
   }

   public boolean maxElementsReached(Collection var1, WLCachedRowSet var2) {
      if (this.maxElements == 0) {
         return false;
      } else if (var1 == null && var2 == null) {
         return false;
      } else if (var1 != null && var1.size() < this.maxElements) {
         return false;
      } else {
         return var2 == null || var2.size() >= this.maxElements;
      }
   }

   public int getResultColumnCount() {
      return this.resultManagers.length;
   }

   public boolean usesStoredProcedure() {
      return this.usesStoredProcedure;
   }

   public boolean usesStoredFunction() {
      return this.usesStoredFunction;
   }

   public boolean usesRelationshipCaching() {
      return this.usesRelationshipCaching;
   }

   public int getRelationCount() {
      return this.relationIndex1.length;
   }

   public String getCmrFieldFinderMethodName1(int var1) {
      return this.cmrFieldFinderMethodNames1[var1];
   }

   public String getCmrFieldFinderMethodName2(int var1) {
      return this.cmrFieldFinderMethodNames2[var1];
   }

   public int getCmrFieldFinderReturnType1(int var1) {
      return this.cmrFieldFinderReturnTypes1[var1];
   }

   public int getCmrFieldFinderReturnType2(int var1) {
      return this.cmrFieldFinderReturnTypes2[var1];
   }

   public int[] getRelationIndex1() {
      return this.relationIndex1;
   }

   public int[] getRelationIndex2() {
      return this.relationIndex2;
   }

   public Method[] getRelationMethod1() {
      return this.relationMethod1;
   }

   public Method[] getRelationMethod2() {
      return this.relationMethod2;
   }

   public boolean isBlobColumn(int var1) {
      return "blob".equalsIgnoreCase(this.columnTypes[var1]);
   }

   public boolean isClobColumn(int var1) {
      return "clob".equalsIgnoreCase(this.columnTypes[var1]);
   }

   public QueryCachingHandler getQueryCachingHandler(Object[] var1, TTLManager var2) {
      if (!this.isQueryCachingEnabled()) {
         return new QueryCachingHandler(this);
      } else {
         return this.isDynamicFinder ? new QueryCachingHandler(this.query, this.getMaxElements(), this, var2) : new QueryCachingHandler(this.getFinderIndex(), var1, this, var2);
      }
   }

   protected boolean checkIfQueryCachingLegal(RDBMSBean var1) {
      if (!super.checkIfQueryCachingLegal(var1)) {
         return false;
      } else {
         SqlShape var2 = this.rdbmsBean.getSqlShape(this.sqlShapeName);
         if (var2 == null) {
            return true;
         } else {
            String[] var3 = var2.getEjbRelationNames();
            if (var3 == null) {
               return true;
            } else {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  EjbRelation var5 = this.rdbmsBean.getEjbRelation(var3[var4]);
                  Iterator var6 = var5.getAllEjbRelationshipRoles().iterator();
                  String var7 = null;

                  while(var6.hasNext()) {
                     EjbRelationshipRole var8 = (EjbRelationshipRole)var6.next();
                     RoleSource var9 = var8.getRoleSource();
                     String var10 = var9.getEjbName();
                     if (var10.equals(this.rdbmsBean.getEjbName())) {
                        var7 = var8.getCmrField().getName();
                        break;
                     }
                  }

                  RDBMSBean var11 = this.rdbmsBean.getRelatedRDBMSBean(var7);
                  if (var11 == null) {
                     throw new AssertionError("Related RDBMS bean not found for cmr-field " + var7 + " from " + this.rdbmsBean.getEjbName());
                  }

                  if (!var11.isReadOnly()) {
                     this.log.logWarning(this.fmt.QUERY_CACHING_SQLFINDER_HAS_RW_RELATED_BEAN(var1.getEjbName(), this.getName(), var2.getSqlShapeName(), var5.getEjbRelationName()));
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }
}
