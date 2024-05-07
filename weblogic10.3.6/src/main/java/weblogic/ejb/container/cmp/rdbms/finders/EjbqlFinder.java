package weblogic.ejb.container.cmp.rdbms.finders;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.FieldGroup;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.ejb.container.cmp.rdbms.RelationshipCaching;
import weblogic.ejb.container.internal.QueryCachingHandler;
import weblogic.ejb.container.manager.TTLManager;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb20.cmp.rdbms.finders.EJBQLCompilerException;
import weblogic.ejb20.cmp.rdbms.finders.EJBQLToken;
import weblogic.ejb20.cmp.rdbms.finders.InvalidFinderException;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;
import weblogic.utils.ErrorCollectionException;

public class EjbqlFinder extends Finder {
   private String stopOnMethod = "findTestDisjointOR";
   public static final int FINDER_EXPRESSION_IN = 0;
   public static final int FINDER_EXPRESSION_NOT_IN = 1;
   public static final int REMOTE_BEAN_EQ = 2;
   public static final int REMOTE_BEAN_NOT_EQ = 3;
   public static final int EJBQL_REWRITE_REASON_FACTOR_OUT_NOT = 1;
   public static final String EJBQL_REWRITE_REASON_FACTOR_OUT_NOT_TEXT = "EJB QL 'NOT' Expressions have been factored out and removed to preserve any generated SQL joins.";
   public static final String EJBQL_REWRITE_REASON_DEFAULT_TEXT = "(Reason for rewrite not specified)";
   private String ejbQuery = null;
   private String whereSql = null;
   private String orderbySql = null;
   private String groupbySql = null;
   private String selectHint = null;
   private boolean includeResultCacheHint = false;
   private String sqlQuery = null;
   private String sqlQueryForUpdate = null;
   private String sqlQueryForUpdateSelective = null;
   private String sqlQueryForUpdateNoWait = null;
   private Expression ejbqlExpression = null;
   private Expr ejbqlExpr = null;
   private RDBMSBean keyBean = null;
   protected Map corrVarMap = null;
   protected Map globalRangeVariableMap = null;
   protected List tableJoinList = null;
   private String groupName = null;
   private String cachingName = null;
   private String orderbyColBuf = null;
   private StringBuffer subQueryColumnBuf = null;
   private QueryContext queryContext = null;
   private boolean testParser = false;
   private boolean isNativeQuery = false;
   private boolean isGeneratedRelationFinder = false;
   private boolean isAggregateQuery = false;
   private String selectTablePK = null;
   private String selectJoinBuf = null;
   private List remoteFinderNodes = null;
   private List remoteBeanParamList = null;
   private int remoteBeanCommand = 0;
   private boolean isPreparedQueryFinder = false;

   public EjbqlFinder(String var1, String var2, boolean var3) throws InvalidFinderException {
      super(var1, false);
      this.testParser = var3;
      this.setEjbQuery(var2);
   }

   public EjbqlFinder(String var1, String var2) throws InvalidFinderException {
      super(var1, false);
      this.setEjbQuery(var2);
   }

   public int getRemoteBeanCommand() {
      return this.remoteBeanCommand;
   }

   public void setRemoteBeanCommand(int var1) {
      this.remoteBeanCommand = var1;
   }

   public void addGlobalASMap(String var1, String var2) throws IllegalExpressionException {
      this.addGlobalRangeVariable(var1, var2);
   }

   public void addGlobalRangeVariable(String var1, String var2) throws IllegalExpressionException {
      if (this.globalRangeVariableMap == null) {
         this.globalRangeVariableMap = new HashMap();
      }

      if (this.globalRangeVariableMap.containsKey(var1)) {
         Loggable var3 = EJBLogger.logduplicateRangeVariableDefinitionLoggable(var1);
         throw new IllegalExpressionException(7, var3.getMessage());
      } else {
         this.globalRangeVariableMap.put(var1, var2);
      }
   }

   public int GlobalASMapSize() {
      return this.globalRangeVariableMapSize();
   }

   public int globalRangeVariableMapSize() {
      if (this.globalRangeVariableMap == null) {
         this.globalRangeVariableMap = new HashMap();
      }

      return this.globalRangeVariableMap.size();
   }

   public String getGlobalASMap(String var1) throws IllegalExpressionException {
      return this.getGlobalRangeVariableMap(var1);
   }

   public String getGlobalRangeVariableMap(String var1) throws IllegalExpressionException {
      if (this.globalRangeVariableMap == null) {
         Loggable var4 = EJBLogger.logejbqlMissingRangeVariableDeclarationLoggable(var1);
         throw new IllegalExpressionException(7, var4.getMessage());
      } else {
         String var2 = (String)this.globalRangeVariableMap.get(var1);
         if (var2 != null) {
            return var2;
         } else {
            Loggable var3 = EJBLogger.logejbqlMissingRangeVariableDeclarationLoggable(var1);
            throw new IllegalExpressionException(7, var3.getMessage());
         }
      }
   }

   public void setMethods(Method[] var1) throws Exception {
      assert var1[0] != null;

      super.setMethod(var1[0]);
      if (this.isFindByPrimaryKey()) {
         Class var2 = this.getKeyBean().getCMPBeanDescriptor().getPrimaryKeyClass();
         this.setParameterClassTypes(new Class[]{var2});
      } else {
         this.setParameterClassTypes(var1[0].getParameterTypes());
      }

   }

   public List getGlobalASMapIdList() {
      return this.getGlobalRangeVariableMapIdList();
   }

   public Map getGlobalRangeVariableMap() {
      return this.globalRangeVariableMap;
   }

   public List getGlobalRangeVariableMapIdList() {
      ArrayList var1 = new ArrayList();
      if (this.globalRangeVariableMap == null) {
         return var1;
      } else {
         Iterator var2 = this.globalRangeVariableMap.keySet().iterator();

         while(var2.hasNext()) {
            var1.add(var2.next());
         }

         return var1;
      }
   }

   public List getIDsFromGlobalASMapForSchema(String var1) {
      return this.getIDsFromGlobalRangeVariableMapForSchema(var1);
   }

   public List getIDsFromGlobalRangeVariableMapForSchema(String var1) {
      ArrayList var2 = new ArrayList();
      if (this.globalRangeVariableMap == null) {
         return var2;
      } else {
         Iterator var3 = this.globalRangeVariableMap.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            String var5 = (String)this.globalRangeVariableMap.get(var4);
            if (var5.equals(var1)) {
               var2.add(var4);
            }
         }

         return var2;
      }
   }

   public boolean isGeneratedRelationFinder() {
      return this.isGeneratedRelationFinder;
   }

   public void setIsGeneratedRelationFinder(boolean var1) {
      this.isGeneratedRelationFinder = var1;
   }

   public boolean isAggregateQuery() {
      return this.isAggregateQuery;
   }

   public void setSelectHint(String var1) {
      this.selectHint = var1;
   }

   public String getSelectHint() {
      return this.selectHint;
   }

   public void setIncludeResultCacheHint(boolean var1) {
      this.includeResultCacheHint = var1;
   }

   public void setKeyBean(RDBMSBean var1) {
      this.keyBean = var1;
   }

   public RDBMSBean getKeyBean() {
      return this.keyBean;
   }

   public void setSelectJoinBuf(String var1) {
      this.selectJoinBuf = var1;
   }

   public String getSelectJoinBuf() {
      return this.selectJoinBuf;
   }

   public void setOrderbyColBuf(String var1) {
      this.orderbyColBuf = var1;
   }

   public String getOrderbyColBuf() {
      return this.orderbyColBuf;
   }

   public void setEjbQuery(String var1) {
      this.ejbQuery = var1;
      this.whereSql = null;
   }

   public String getEjbQuery() {
      return this.ejbQuery;
   }

   public String getFromSql(int var1) throws IllegalExpressionException {
      return this.generateTableSQL(var1);
   }

   public String getWhereSql() {
      return this.whereSql;
   }

   public ParamNode getParamNodeForVariableNumber(int var1) {
      List var2 = this.getExternalMethodParmList();
      ParamNode var3 = null;
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         ParamNode var5 = (ParamNode)var4.next();
         if (var5.getVariableNumber() == var1) {
            var3 = var5;
            break;
         }
      }

      return var3;
   }

   public void setOrderbySql(String var1) {
      this.orderbySql = var1;
   }

   public String getOrderbySql() {
      return this.orderbySql;
   }

   public void setGroupbySql(String var1) {
      this.groupbySql = var1;
   }

   public String getGroupbySql() {
      return this.groupbySql;
   }

   public void addRemoteBeanParamList(ParamNode var1) {
      if (this.remoteBeanParamList == null) {
         this.remoteBeanParamList = new ArrayList();
      }

      this.remoteBeanParamList.add(var1);
   }

   public ParamNode getRemoteBeanParam() {
      return !this.hasRemoteBeanParam() ? null : (ParamNode)this.remoteBeanParamList.get(0);
   }

   public boolean hasRemoteBeanParam() {
      if (this.remoteBeanParamList == null) {
         return false;
      } else {
         return this.remoteBeanParamList.size() != 0;
      }
   }

   public void addSubQueryColumnBuf(String var1) {
      if (this.subQueryColumnBuf == null) {
         this.subQueryColumnBuf = new StringBuffer();
      }

      if (this.subQueryColumnBuf.length() > 0) {
         this.subQueryColumnBuf.append(", ");
      }

      this.subQueryColumnBuf.append(var1).append(" ");
   }

   public String getSubQueryColumnBuf() {
      return this.subQueryColumnBuf == null ? "" : this.subQueryColumnBuf.toString();
   }

   public String getSQLQuery() {
      return this.sqlQuery;
   }

   public String getSQLQueryForUpdateSelective() {
      return this.sqlQueryForUpdateSelective;
   }

   public String getSQLQueryForUpdate() {
      return this.sqlQueryForUpdate;
   }

   public String getSQLQueryForUpdateNoWait() {
      return this.sqlQueryForUpdateNoWait;
   }

   public String getTableAndFieldForCmpField(String var1) throws IllegalExpressionException {
      return this.queryContext.getTableAndColumnFromMainQuery(var1);
   }

   public String getMainJoinBuffer() throws IllegalExpressionException {
      return this.queryContext.getMainJoinBuffer();
   }

   public Expression getWLQLExpression() {
      return this.ejbqlExpression;
   }

   public Expr getEJBQLExpr() {
      return this.ejbqlExpr;
   }

   public void setIsPreparedQueryFinder(boolean var1) {
      this.isPreparedQueryFinder = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof EjbqlFinder)) {
         return false;
      } else {
         EjbqlFinder var2 = (EjbqlFinder)var1;
         if (!this.getName().equals(var2.getName())) {
            return false;
         } else {
            if (this.getEJBQLExpr() == null) {
               if (var2.getEJBQLExpr() != null) {
                  return false;
               }
            } else if (!this.getEJBQLExpr().equals(var2.getEJBQLExpr())) {
               return false;
            }

            Class[] var3 = this.getParameterClassTypes();
            Class[] var4 = var2.getParameterClassTypes();
            if (var4.length != var3.length) {
               return false;
            } else {
               for(int var5 = 0; var5 < var3.length; ++var5) {
                  if (!var4[var5].equals(var3[var5])) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   public int hashCode() {
      return this.getName().hashCode() ^ this.getEJBQLExpr().hashCode();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString());
      var1.append("[EjbqlFinder ");
      var1.append("ejbQuery = " + this.ejbQuery + "; ");
      var1.append("Expr = " + this.getEJBQLExpr() + "; ");
      var1.append("whereSql = " + this.whereSql + " ");
      var1.append("]]");
      return var1.toString();
   }

   public void parseExpression() throws EJBQLCompilerException {
      ExprParser var1 = null;
      var1 = new ExprParser(this);
      Expr var2 = null;
      if (!this.ejbQuery.equals("")) {
         if (this.testParser) {
            System.out.println("\n\n+++++++++ PARSE QUERY: \n     " + this.ejbQuery);
         }

         if (debugLogger.isDebugEnabled()) {
            debug("\n Parse EJB QL: " + this.ejbQuery);
         }

         var2 = var1.parse(this.ejbQuery);
         if (this.testParser && var2 != null) {
            var2.dump();
         }
      }

      this.ejbqlExpr = var2;
   }

   private static void get_line(String var0) {
      System.out.print(var0);
      System.out.flush();

      try {
         while(true) {
            if (System.in.read() != 10) {
               continue;
            }
         }
      } catch (Exception var3) {
      }

   }

   public void computeSQLQuery(RDBMSBean var1) throws EJBQLCompilerException {
      try {
         if (debugLogger.isDebugEnabled()) {
            debug("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n ");
            debug(" -------------------------------------------------------  ");
            debug("\n           computeSQLQuery for " + this.getName() + "  EJB QL: " + this.ejbQuery + "\n");
            if (this.getName().startsWith(this.stopOnMethod)) {
               get_line("              press key to continue ");
            }

            ((BaseExpr)this.ejbqlExpr).dump();
         }

         this.queryContext = new QueryContext(var1, this, this.ejbqlExpr);
         String var2 = "";

         try {
            this.queryContext.generateQuery();
         } catch (EJBQLCompilerException var23) {
            throw var23;
         }

         var2 = this.queryContext.getWhereSql();
         var2 = var2.trim();
         if (var2.compareTo("WHERE") == 0) {
            var2 = "";
         }

         this.initExternalMethodParmList();
         List var3 = null;
         var3 = this.queryContext.getMainQuerySelectList();
         Loggable var29;
         IllegalExpressionException var31;
         if (!this.isSelect && !this.isSelectInEntity && var3.size() > 0) {
            SelectNode var4 = (SelectNode)var3.get(0);
            if (var4.getSelectType() != 61) {
               var29 = EJBLogger.logFinderDoesNotReturnBeanLoggable(this.getName(), this.getEjbQuery());
               var31 = new IllegalExpressionException(7, var29.getMessage(), new DescriptorErrorInfo("<ejb-ql>", var1.getEjbName(), this.getName()));
               throw this.newEJBQLCompilerException(var31, (QueryContext)this.queryContext);
            }

            String var5 = var4.getSelectTarget();
            RDBMSBean var6 = var4.getSelectBean();
            if (var6 != null && !var6.getEjbName().equals(var1.getEjbName())) {
               Loggable var32 = EJBLogger.logFinderReturnsBeanOfWrongTypeLoggable(this.getName(), var1.getEjbName(), var6.getEjbName());
               IllegalExpressionException var33 = new IllegalExpressionException(7, var32.getMessage(), new DescriptorErrorInfo("<ejb-ql>", var1.getEjbName(), this.getName()));
               throw this.newEJBQLCompilerException(var33, (QueryContext)this.queryContext);
            }
         }

         if (this.isSelect && !this.isNativeQuery) {
            Iterator var25 = var3.iterator();

            while(var25.hasNext()) {
               SelectNode var27 = (SelectNode)var25.next();
               if (var27.isCorrelatedSubQuery()) {
                  var25.remove();
               }
            }

            if (var3.size() > 1 && !this.isResultSetFinder()) {
               var29 = EJBLogger.logSelectMultipleFieldsButReturnCollectionLoggable(this.returnClassType.getName());
               var31 = new IllegalExpressionException(7, var29.getMessage(), new DescriptorErrorInfo("<ejb-ql>", var1.getEjbName(), this.getName()));
               throw this.newEJBQLCompilerException(var31, (QueryContext)this.queryContext);
            }
         }

         String var26 = this.recomputeSelectInEntity(var1);
         StringBuffer var28 = new StringBuffer();
         var28.append("SELECT ");
         if (this.selectHint != null) {
            var28.append(this.selectHint).append(" ");
         } else if (this.includeResultCacheHint) {
            var28.append("/*+ RESULT_CACHE */ ");
         }

         int var30 = var28.length();
         if (this.isSelectDistinct() || this.isSetFinder()) {
            var28.append("DISTINCT ");
         }

         int var7 = var28.length();
         CMPBeanDescriptor var8 = var1.getCMPBeanDescriptor();
         Iterator var9 = var3.iterator();
         int var10 = 0;

         SelectNode var11;
         while(var9.hasNext()) {
            ++var10;
            if (var10 > 1) {
               var28.append(", ");
            }

            var11 = (SelectNode)var9.next();
            if (var11.getSelectType() == 61) {
               if (this.finderLoadsBean) {
                  var28.append(this.generateFieldGroupSQLForFinder(var11.getSelectBean(), var11.getSelectTarget(), this.getGroupName(), false, (RDBMSBean)null));
               } else {
                  var28.append(this.generatePrimaryKeySQL(var11.getSelectBean(), var11.getSelectTarget()));
               }
            } else if (var11.getIsAggregate()) {
               this.isAggregateQuery = true;
               var28.append(var11.getSelectTypeName());
               var28.append("(");
               if (var11.getIsAggregateDistinct()) {
                  var28.append("DISTINCT ");
               }

               var28.append(var11.getDbmsTarget()).append(" ");
               var28.append(") ");
            } else {
               switch (var11.getSelectType()) {
                  case 17:
                     var28.append(var11.getDbmsTarget()).append(" ");
                     break;
                  case 70:
                  case 71:
                     var28.append(var11.getSelectTypeName());
                     var28.append("( ");
                     var28.append(var11.getDbmsTarget()).append(" ");
                     var28.append(") ");
                     break;
                  default:
                     throw new AssertionError("Unknown type!");
               }
            }
         }

         if (this.finderLoadsBean) {
            var11 = null;
            List var34 = this.queryContext.getMainQuerySelectListForCachingElement();
            Iterator var12 = var34.iterator();

            while(var12.hasNext()) {
               SelectNode var13 = (SelectNode)var12.next();
               if (debugLogger.isDebugEnabled()) {
                  debug("\n ------------------  BEGIN  relationship caching for caching element " + var13.getSelectTarget() + "----");
               }

               var28.append(", ");
               var28.append(this.generateFieldGroupSQLForFinder(var13.getSelectBean(), var13.getSelectTarget(), var13.getCachingElementGroupName(), true, var13.getPrevBean()));
               if (debugLogger.isDebugEnabled()) {
                  debug("\n ------------------  END    relationship caching for caching element " + var13.getSelectTarget() + "----\n");
               }
            }
         }

         String var35 = this.getOrderbyColBuf();
         if (var35 != null) {
            var28.append(", ");
            var28.append(var35);
         }

         var28.append(" FROM ");
         int var36 = var28.length();
         String var37 = this.generateTableSQL(0);
         String var14 = this.generateTableSQL(1);
         var28.append(var37);
         int var15 = var28.length();
         new StringBuffer();
         if (var2.length() > 0) {
            var28.append(var2);
         }

         if (var26.length() > 0) {
            if (var2.length() > 0) {
               var28.append(" AND ");
            } else {
               var2 = " WHERE ";
               var28.append(var2);
            }

            var28.append(var26);
         }

         String var17 = this.queryContext.getMainQueryJoinBuffer();
         if (var17.length() > 0) {
            if (var2.length() > 0) {
               var28.append(" AND ");
            } else {
               var2 = "WHERE ";
               var28.append(var2);
            }

            var28.append(var17);
            var28.append(" ");
         }

         if (this.groupbySql != null) {
            var28.append(this.groupbySql).append(" ");
         }

         if (this.orderbySql != null) {
            if (var1.getUseSelectForUpdate()) {
               Loggable var38 = EJBLogger.logselectForUpdateSpecifiedWithOrderByLoggable(this.getName(), var1.getEjbName());
               throw new IllegalExpressionException(7, var38.getMessage(), new DescriptorErrorInfo("<ejb-ql>", var1.getEjbName(), this.getName()));
            }

            var28.append(this.orderbySql);
         }

         this.sqlQuery = var28.toString();
         if (var30 < var7) {
            var28.replace(var30, var7, " ");
         }

         int var18 = var1.getDatabaseType();
         switch (var18) {
            case 0:
            case 3:
            case 6:
            case 8:
            case 9:
               this.sqlQueryForUpdate = var28.toString() + RDBMSUtils.selectForUpdateToString(1);
               this.sqlQueryForUpdateNoWait = var28.toString() + RDBMSUtils.selectForUpdateToString(2);
               this.assignSqlQueryForUpdateSelective();
               break;
            case 1:
               this.sqlQueryForUpdate = var28.toString() + RDBMSUtils.selectForUpdateToString(1);
               if (this.cachingName != null) {
                  if (this.allOrAnyBeansHaveSelectForUpdate(false)) {
                     this.sqlQueryForUpdateSelective = this.sqlQueryForUpdate;
                  } else {
                     String var19 = this.perhapsAppendForUpdateOf();
                     if (!var19.equals("")) {
                        this.sqlQueryForUpdateSelective = this.sqlQueryForUpdate + var19;
                     } else {
                        this.sqlQueryForUpdateSelective = this.sqlQuery;
                     }
                  }
               } else if (this.rdbmsBean.getUseSelectForUpdate()) {
                  this.sqlQueryForUpdateSelective = this.sqlQueryForUpdate;
               } else {
                  this.sqlQueryForUpdateSelective = this.sqlQuery;
               }

               this.sqlQueryForUpdateNoWait = var28.toString() + RDBMSUtils.selectForUpdateToString(2);
               break;
            case 2:
            case 5:
            case 7:
               this.sqlQueryForUpdateNoWait = var28.toString() + RDBMSUtils.selectForUpdateToString(2);
               var28.replace(var36, var15, var14);
               this.sqlQueryForUpdate = var28.toString();
               this.assignSqlQueryForUpdateSelective();
               break;
            case 4:
               this.sqlQueryForUpdate = var28.toString() + " FOR READ ONLY WITH RS USE AND KEEP UPDATE LOCKS";
               this.sqlQueryForUpdateNoWait = var28.toString() + RDBMSUtils.selectForUpdateToString(2);
               this.assignSqlQueryForUpdateSelective();
               break;
            default:
               throw new AssertionError("Undefined database type " + var18);
         }

         if (debugLogger.isDebugEnabled()) {
            List var39 = this.queryContext.getSQLGenEJBQLTokenList();
            debug("\n\n\n\n +++++++++  view the EJBQL Token List +++++++++++=\n");
            StringBuffer var20 = new StringBuffer();
            var9 = var39.iterator();

            while(var9.hasNext()) {
               var20.append(((EJBQLToken)var9.next()).getTokenText());
            }

            debug("  '" + var20.toString() + "'\n\n\n +++++++++++++++++++++++++++++++++++++++++++++\n");
            debug("SQL Query is: " + this.sqlQuery);
            debug("SQL Query with FOR UPDATE is: " + this.sqlQueryForUpdate);
            debug("SQL Query with FOR UPDATE NOWAIT is: " + this.sqlQueryForUpdateNoWait);
            if (this.cachingName != null && var18 == 1) {
               debug("SQL Query for case SelectForUpdateDisabled for dbtype Oracle is: " + this.sqlQueryForUpdateSelective);
            }

            debug("              ------------------------------------------------------- \n\n\n\n\n\n");
            if (this.getName().equals(this.stopOnMethod)) {
               get_line("              press key to continue ");
            }
         }

         QueryNode var40 = this.queryContext.getMainQueryTree();
         var40.checkAllORCrossProducts();
         ErrorCollectionException var41 = this.queryContext.getWarnings();
         if (var41 != null) {
            EJBQLCompilerException var21 = this.newEJBQLCompilerException(var41, (QueryContext)this.queryContext);
            EJBLogger var22 = new EJBLogger();
            EJBLogger.logWarningFromEJBQLCompiler(var21.getMessage());
         }

      } catch (IllegalExpressionException var24) {
         var24.setDescriptorErrorInfo(new DescriptorErrorInfo("<ejb-ql>", var1.getEjbName(), this.getName()));
         throw this.newEJBQLCompilerException(var24, (QueryContext)this.queryContext);
      }
   }

   public boolean allOrAnyBeansHaveSelectForUpdate(boolean var1) {
      List var2 = null;
      var2 = this.queryContext.getMainQuerySelectListForCachingElement();
      if (var2.size() != 0) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            SelectNode var4 = (SelectNode)var3.next();
            RDBMSBean var5 = var4.getSelectBean();
            if (var1) {
               if (var5.getUseSelectForUpdate()) {
                  return true;
               }
            } else if (!var5.getUseSelectForUpdate()) {
               return false;
            }
         }

         if (this.rdbmsBean.getUseSelectForUpdate()) {
            return true;
         }
      }

      return false;
   }

   private void assignSqlQueryForUpdateSelective() {
      if (this.cachingName != null && this.allOrAnyBeansHaveSelectForUpdate(true)) {
         this.sqlQueryForUpdateSelective = this.sqlQueryForUpdate;
      } else if (this.rdbmsBean.getUseSelectForUpdate()) {
         this.sqlQueryForUpdateSelective = this.sqlQueryForUpdate;
      } else {
         this.sqlQueryForUpdateSelective = this.sqlQuery;
      }

   }

   public String perhapsAppendForUpdateOf() {
      StringBuffer var1 = new StringBuffer();
      List var2 = null;
      String var3 = null;
      String var4 = null;
      String var5 = null;
      String var6 = null;
      int var7 = 0;
      ArrayList var8 = new ArrayList();
      var2 = this.queryContext.getMainQuerySelectListForCachingElement();
      Iterator var9 = var2.iterator();

      while(var9.hasNext()) {
         SelectNode var10 = (SelectNode)var9.next();
         RDBMSBean var11 = var10.getSelectBean();
         var8.add(var11);
      }

      var8.add(this.rdbmsBean);
      Comparator var16 = new Comparator() {
         public int compare(Object var1, Object var2) {
            RDBMSBean var3 = (RDBMSBean)var1;
            RDBMSBean var4 = (RDBMSBean)var2;
            return var3.getLockOrder() - var4.getLockOrder();
         }
      };
      Collections.sort(var8, var16);
      Iterator var17 = var8.iterator();

      while(true) {
         RDBMSBean var12;
         do {
            if (!var17.hasNext()) {
               if (debugLogger.isDebugEnabled()) {
                  debug("perhapsAppendForUpdateOf returns: " + var1.toString());
               }

               return var1.toString();
            }

            var12 = (RDBMSBean)var17.next();
         } while(!var12.getUseSelectForUpdate());

         for(Iterator var13 = var12.getTables().iterator(); var13.hasNext(); ++var7) {
            var5 = (String)var13.next();
            var6 = this.queryContext.getAliasForTableName(var5);
            Map var14 = var12.getPKCmpf2ColumnForTable(var5);

            for(Iterator var15 = var14.keySet().iterator(); var15.hasNext(); var4 = (String)var14.get(var3)) {
               var3 = (String)var15.next();
            }

            if (var7 == 0) {
               var1.append("OF ");
            } else {
               var1.append(", ");
            }

            var1.append(var6 + "." + var4);
         }
      }
   }

   private String recomputeSelectInEntity(RDBMSBean var1) throws IllegalExpressionException {
      StringBuffer var2 = new StringBuffer();
      if (this.isSelectInEntity) {
         CMPBeanDescriptor var3 = var1.getCMPBeanDescriptor();
         Class var4 = var3.getBeanClass();
         boolean var5 = var3.hasComplexPrimaryKey();
         Class var6 = null;
         if (var5) {
            var6 = var3.getPrimaryKeyClass();
         }

         int var7 = this.externalMethodParmList.size();
         String var8 = "param" + var7;
         int var9 = var7 + 1;
         ParamNode var10 = new ParamNode(var1, var8, var9, Object.class, "", "", false, true, var6, var5, false);
         if (debugLogger.isDebugEnabled()) {
            debug(" created inEntity ParamNode: " + var10.toString());
         }

         List var11 = var1.getPrimaryKeyFields();
         int var12 = var11.size();

         for(int var13 = 0; var13 < var12; ++var13) {
            String var14 = (String)var11.get(var13);
            var6 = var3.getFieldClass(var14);
            if (var6 == null) {
               throw new IllegalExpressionException(7, "finder: " + this.getName() + ", the EJB QL for ejbSelect<>InEntity Finders, query recompute: " + "could not get pkClass for pkField: " + var14, new DescriptorErrorInfo("<ejb-ql>", var1.getEjbName(), this.getName()));
            }

            boolean var15 = RDBMSUtils.isOracleNLSDataType(var1, var14);
            if (var13 == 0 && !var5) {
               var10.setPrimaryKeyClass(var6);
               var10.setOracleNLSDataType(var15);
            }

            ParamNode var16 = new ParamNode(var1, var8, var9, var6, var14, "", false, false, var6, false, var15);
            if (debugLogger.isDebugEnabled()) {
               debug(" added Sub ParamNode to inEntity ParamNode: " + var16.toString());
            }

            var10.addParamSubList(var16);
            String var17 = null;
            List var18 = this.getIDsFromGlobalRangeVariableMapForSchema(var1.getAbstractSchemaName());
            if (var18.size() < 1) {
               Loggable var23 = EJBLogger.lograngeVariableNotFoundLoggable(var1.getEjbName(), var1.getAbstractSchemaName());
               throw new IllegalExpressionException(7, var23.getMessage(), new DescriptorErrorInfo("<ejb-ql>", var1.getEjbName(), this.getName()));
            }

            String var19 = (String)var18.get(0);
            String var20 = var19 + "." + var14;

            try {
               var17 = this.queryContext.getTableAndColumnFromMainQuery(var20);
            } catch (Exception var22) {
               throw new IllegalExpressionException(7, "In Bean Parameter processing.  Could not get table and Field for path expression: " + var20, new DescriptorErrorInfo("<ejb-ql>", var1.getEjbName(), this.getName()));
            }

            var2.append(var17).append(" = ? ");
            var2.append(" AND ");
         }

         if (var2.length() > 5) {
            var2.setLength(var2.length() - 5);
         }

         if (debugLogger.isDebugEnabled()) {
            debug(" ejbSelect<>InEntity SQL is: " + var2.toString());
         }

         this.addInternalInEntityParmList(var10);
      }

      return var2.toString();
   }

   public List getExternalMethodAndInEntityParmList() {
      ArrayList var1 = new ArrayList(this.externalMethodParmList);
      Iterator var2 = this.internalInEntityParmList.iterator();

      while(var2.hasNext()) {
         ParamNode var3 = (ParamNode)var2.next();
         var1.add(var3);
      }

      return var1;
   }

   public int getPKOrGroupColumnCount() {
      return this.finderLoadsBean ? this.getGroupColumnCount() : this.getPKColumnCount();
   }

   public int getPKColumnCount() {
      return this.rdbmsBean.getPrimaryKeyFields().size();
   }

   public int getGroupColumnCount() {
      String var1 = this.getGroupName();
      FieldGroup var2 = this.rdbmsBean.getFieldGroup(var1);
      if (var2 == null) {
         return this.rdbmsBean.getPrimaryKeyFields().size();
      } else {
         HashSet var3 = new HashSet();
         TreeSet var4 = new TreeSet(var2.getCmpFields());
         var4.addAll(this.rdbmsBean.getPrimaryKeyFields());
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            var3.add(this.rdbmsBean.getCmpColumnForField(var6));
         }

         Iterator var10 = var2.getCmrFields().iterator();

         while(var10.hasNext()) {
            String var7 = (String)var10.next();
            Iterator var8 = this.rdbmsBean.getForeignKeyColNames(var7).iterator();

            while(var8.hasNext()) {
               String var9 = (String)var8.next();
               var3.add(var9);
            }
         }

         return var3.size();
      }
   }

   public String getGroupName() {
      return this.groupName == null ? "defaultGroup" : this.groupName;
   }

   public void setGroupName(String var1) {
      this.groupName = var1;
   }

   public String getCachingName() {
      return this.cachingName;
   }

   public void setCachingName(String var1) {
      this.cachingName = var1;
   }

   public void setSqlSelectDistinct(boolean var1) {
      if (var1) {
         RDBMSBean var2 = this.getRDBMSBean();
         Loggable var3 = null;
         if (var2 != null) {
            var3 = EJBLogger.logSqlSelectDistinctDeprecatedLoggable(var2.getEjbName(), this.getName());
         } else {
            var3 = EJBLogger.logSqlSelectDistinctDeprecatedLoggable("UNKNOWN", this.getName());
         }

         var3.log();
         this.setSelectDistinct(var1);
      }

   }

   public void setNativeQuery(boolean var1) {
      this.isNativeQuery = var1;
   }

   public String generateFieldGroupSQLForFinder(RDBMSBean var1, String var2, String var3, boolean var4, RDBMSBean var5) throws IllegalExpressionException {
      return this.generateFieldGroupSQL(var1, var2, var3, var4, var5, true);
   }

   public String generateFieldGroupSQLForNonFinder() throws IllegalExpressionException {
      Iterator var1 = null;
      var1 = this.queryContext.getMainQuerySelectList().iterator();
      if (var1.hasNext()) {
         SelectNode var2 = (SelectNode)var1.next();
         if (var1.hasNext()) {
            throw new IllegalExpressionException(7, "Internal Error during Non-Finder FieldGroup SQL Generation:  found more than one SelectNode for FieldGroup: '" + this.getGroupName() + "', query: '" + this.getEjbQuery() + "'", new DescriptorErrorInfo("<ejb-ql>", this.getRDBMSBean().getEjbName(), this.getName()));
         } else {
            return this.generateFieldGroupSQLForNonFinder(var2.getSelectBean(), var2.getSelectTarget(), this.getGroupName(), false, (RDBMSBean)null);
         }
      } else {
         throw new IllegalExpressionException(7, "Internal Error during Non-Finder FieldGroup SQL Generation:  could not get SelectNode for FieldGroup: '" + this.getGroupName() + "', query: '" + this.getEjbQuery() + "'", new DescriptorErrorInfo("<ejb-ql>", this.getRDBMSBean().getEjbName(), this.getName()));
      }
   }

   public String generateFieldGroupSQLForNonFinder(RDBMSBean var1, String var2, String var3, boolean var4, RDBMSBean var5) throws IllegalExpressionException {
      return this.generateFieldGroupSQL(var1, var2, var3, var4, var5, false);
   }

   private String generateFieldGroupSQL(RDBMSBean var1, String var2, String var3, boolean var4, RDBMSBean var5, boolean var6) throws IllegalExpressionException {
      StringBuffer var7 = new StringBuffer();
      FieldGroup var8 = var1.getFieldGroup(var3);
      if (debugLogger.isDebugEnabled()) {
         debug("rdbms ejb name- " + var1.getEjbName());
         debug("ejb name- " + var1.getCMPBeanDescriptor().getEJBName());
         debug("groupName- " + var3);
      }

      HashSet var9 = new HashSet();
      ArrayList var10;
      if (var6) {
         var10 = new ArrayList(var1.getPrimaryKeyFields());
      } else {
         var10 = new ArrayList();
      }

      TreeSet var11 = new TreeSet(var8.getCmpFields());
      Iterator var12 = var11.iterator();

      while(var12.hasNext()) {
         String var13 = (String)var12.next();
         if (!var10.contains(var13)) {
            var10.add(var13);
         }
      }

      Iterator var19 = var10.iterator();

      String var14;
      String var15;
      String var16;
      while(var19.hasNext()) {
         var14 = (String)var19.next();
         var15 = var2 + "." + var14;
         if (debugLogger.isDebugEnabled()) {
            debug("  generateFieldGroupSQL: lookup: SQL SELECT ID for '" + var15 + "'");
         }

         var16 = null;
         var16 = this.queryContext.getTableAndColumnFromMainQuery(var15);
         var9.add(var16);
         if (debugLogger.isDebugEnabled()) {
            debug("fieldName- " + var14 + ", columnName- " + var16);
         }

         assert var16 != null;

         var7.append(var16);
         var7.append(", ");
      }

      var19 = var8.getCmrFields().iterator();

      Iterator var17;
      String var18;
      List var20;
      while(var19.hasNext()) {
         var14 = (String)var19.next();
         var15 = null;
         var16 = null;
         var15 = this.queryContext.replaceIdAliases(var2) + "." + var14;
         var20 = this.queryContext.getTableAndFKColumnListForLocal11or1NPathForMainQuery(var15);
         var17 = var20.iterator();

         while(var17.hasNext()) {
            var18 = (String)var17.next();
            if (!var9.contains(var18)) {
               var7.append(var18);
               var7.append(", ");
            }
         }
      }

      if (var4) {
         if (debugLogger.isDebugEnabled()) {
            debug("\n--- BEGIN Caching Element FK Column Insertion ---");
            debug("selectTarget=" + var2);
            debug("start sb=" + var7);
         }

         var14 = var2.substring(var2.lastIndexOf(".") + 1);
         if (var1 == var5.getRelatedRDBMSBean(var14)) {
            var15 = null;
            var16 = null;
            var15 = this.queryContext.replaceIdAliases(var2);
            var20 = this.queryContext.getTableAndFKColumnListForLocal11or1NPathForMainQuery(var15);
            var17 = var20.iterator();

            while(var17.hasNext()) {
               var18 = (String)var17.next();
               if (debugLogger.isDebugEnabled()) {
                  debug(" adding Caching Element Column: '" + var18 + "'");
               }

               var7.append(var18);
               var7.append(", ");
            }
         }

         if (debugLogger.isDebugEnabled()) {
            debug("end   sb=" + var7 + "\n");
            debug("\n--- END   Caching Element FK Column Insertion ---");
         }
      }

      var7.deleteCharAt(var7.length() - 2);
      if (debugLogger.isDebugEnabled()) {
         debug("returning: " + var7.toString());
      }

      return var7.toString();
   }

   private String generatePrimaryKeySQL(RDBMSBean var1, String var2) throws IllegalExpressionException {
      Iterator var3 = var1.getPrimaryKeyFields().iterator();
      boolean var4 = false;
      StringBuffer var5 = new StringBuffer();

      while(var3.hasNext()) {
         String var6 = (String)var3.next();
         String var7 = var2 + "." + var6;
         String var8 = null;
         var8 = this.queryContext.getTableAndColumnFromMainQuery(var7);

         assert var8 != null;

         var5.append(var8);
         if (var3.hasNext()) {
            var5.append(", ");
         }
      }

      var5.append(" ");
      return var5.toString();
   }

   private String generateTableSQL(int var1) throws IllegalExpressionException {
      return this.queryContext.getMainQueryTree().getFROMDeclaration(var1);
   }

   public String replaceCorrVars(String var1) {
      if (this.corrVarMap != null && var1.length() != 0) {
         int var2 = var1.indexOf("=>");
         StringBuffer var3;
         if (var2 != -1) {
            var3 = new StringBuffer(var1.substring(0, var2));
            var3.append(".");
            String var4 = "";
            if (var1.length() > var2 + 2) {
               var4 = var1.substring(var2 + 2);
            }

            var3.append(var4);
            var1 = var3.toString();
         }

         var3 = new StringBuffer();
         StringTokenizer var8 = new StringTokenizer(var1, ".");

         while(var8.hasMoreTokens()) {
            String var5 = var8.nextToken();
            String var6 = var5;
            CorrelationVarInfo var7 = (CorrelationVarInfo)this.corrVarMap.get(var5);
            if (var7 != null) {
               var6 = var7.getValue();
            }

            var3.append(var6);
            var3.append(".");
         }

         var3.setLength(var3.length() - 1);
         return var3.toString();
      } else {
         return var1;
      }
   }

   public void addToCorrVarMap(String var1, CorrelationVarInfo var2) throws IllegalExpressionException {
      if (this.corrVarMap == null) {
         this.corrVarMap = new HashMap();
      }

      if (this.corrVarMap.get(var1) != null) {
         Loggable var3 = EJBLogger.logcorrelationVarDefinedMultipleTimesLoggable(var1);
         throw new IllegalExpressionException(7, var3.getMessage(), new DescriptorErrorInfo("<ejb-ql>", this.getRDBMSBean().getEjbName(), this.getName()));
      } else {
         this.corrVarMap.put(var1, var2);
      }
   }

   public void updateTableJoinList(String var1) {
      if (this.tableJoinList == null) {
         this.tableJoinList = new ArrayList();
      }

      if (!this.tableJoinList.contains(var1)) {
         this.tableJoinList.add(var1);
      }

   }

   private void initExternalMethodParmList() {
      for(int var1 = 0; var1 < this.parameterClassTypes.length; ++var1) {
         int var2 = var1 + 1;
         ParamNode var3 = this.getInternalQueryParmNode(var1);
         if (var3 == null) {
            var3 = new ParamNode((RDBMSBean)null, "param" + var1, var2, this.parameterClassTypes[var1], "", "", false, false, (Class)null, false, false);
            this.externalMethodParmList.add(var3);
         } else {
            ParamNode var4 = new ParamNode(var3.getRDBMSBean(), "param" + var1, var2, this.parameterClassTypes[var1], var3.getId(), var3.getRemoteHomeName(), var3.isBeanParam(), var3.isSelectInEntity(), var3.getPrimaryKeyClass(), var3.hasCompoundKey(), var3.isOracleNLSDataType());
            if (var3.hasCompoundKey()) {
               Iterator var5 = var3.getParamSubList().iterator();

               while(var5.hasNext()) {
                  ParamNode var6 = (ParamNode)var5.next();
                  ParamNode var7 = new ParamNode(var6.getRDBMSBean(), "param" + var1, var2, this.parameterClassTypes[var1], var6.getId(), var6.getRemoteHomeName(), var6.isBeanParam(), var6.isSelectInEntity(), var6.getPrimaryKeyClass(), var6.hasCompoundKey(), var6.isOracleNLSDataType());
                  var4.addParamSubList(var7);
               }
            }

            this.externalMethodParmList.add(var4);
         }
      }

   }

   public String toUserLevelString(boolean var1) {
      String var2 = "N/A";
      if (this.rdbmsBean != null) {
         var2 = this.rdbmsBean.getEjbName();
      }

      StringBuffer var3 = new StringBuffer();
      var3.append("Query:");
      if (var1) {
         var3.append("\n\t");
      } else {
         var3.append(", ");
      }

      if (var2 != null) {
         var3.append("EJB Name:        " + var2);
         if (var1) {
            var3.append("\n\t");
         } else {
            var3.append(", ");
         }
      }

      if (this.methodName != null) {
         var3.append("Method Name:     " + this.methodName);
         if (var1) {
            var3.append("\n\t");
         } else {
            var3.append(", ");
         }
      }

      if (this.parameterClassTypes != null) {
         var3.append("Parameter Types: (");

         for(int var4 = 0; var4 < this.parameterClassTypes.length; ++var4) {
            var3.append("" + this.parameterClassTypes[var4].getName());
            if (var4 < this.parameterClassTypes.length - 1) {
               var3.append(", ");
            }
         }

         var3.append(")");
         if (var1) {
            var3.append("\n\t");
         } else {
            var3.append(", ");
         }
      }

      return var3.toString();
   }

   public boolean isContentValid() {
      if (this.ejbQuery != null && !this.ejbQuery.equals("")) {
         this.sqlQuery = null;

         try {
            this.parseExpression();
            return true;
         } catch (EJBQLCompilerException var2) {
            return false;
         }
      } else {
         return false;
      }
   }

   public EJBQLCompilerException newEJBQLCompilerException(Exception var1, List var2) {
      ErrorCollectionException var3;
      if (!(var1 instanceof ErrorCollectionException)) {
         var3 = new ErrorCollectionException(var1);
      } else {
         var3 = (ErrorCollectionException)var1;
      }

      return this.newEJBQLCompilerException(var3, false, 0, "", var2);
   }

   public EJBQLCompilerException newEJBQLCompilerException(Exception var1, QueryContext var2) {
      ErrorCollectionException var3;
      if (!(var1 instanceof ErrorCollectionException)) {
         var3 = new ErrorCollectionException(var1);
      } else {
         var3 = (ErrorCollectionException)var1;
      }

      return this.newEJBQLCompilerException(var3, var2.getEjbqlRewritten(), var2.getEjbqlRewrittenReasons(), var2.getOriginalEjbql(), var2.getSQLGenEJBQLTokenList());
   }

   public EJBQLCompilerException newEJBQLCompilerException(ErrorCollectionException var1, boolean var2, int var3, String var4, List var5) {
      String var6 = this.decodeEjbqlRewrittenReasons(var2, var3);
      return new EJBQLCompilerException(var1, var2, var6, var4, var5, this.toUserLevelString(true), this.newDescriptorErrorInfo());
   }

   private String decodeEjbqlRewrittenReasons(boolean var1, int var2) {
      if (!var1) {
         return "";
      } else {
         String var3 = "\n       ";
         StringBuffer var4 = new StringBuffer();
         var4.append(var3);
         int var5 = var2;
         if ((1 | var2) != 0) {
            Loggable var6 = EJBLogger.logEJBQL_REWRITE_REASON_FACTOR_OUT_NOT_TEXTLoggable();
            String var7 = var6.getMessage();
            var4.append(var7).append(var3);
            var5 = var2 - 1;
         }

         if (var4.length() <= var3.length()) {
            var4.append("(Reason for rewrite not specified)");
         }

         if (debugLogger.isDebugEnabled() && var5 != 0) {
            throw new AssertionError(" unhandled rewrite reason in EjbqlFinder.decodeEjbqlRewrittenReasons().  Remaining reasons integer codes are " + var5);
         } else {
            return var4.toString();
         }
      }
   }

   public DescriptorErrorInfo newDescriptorErrorInfo() {
      String var1 = "";
      String var2 = "";
      if (this.rdbmsBean != null) {
         var2 = this.rdbmsBean.getEjbName();
      }

      if (this.getName() != null) {
         var1 = this.getName();
      }

      return new DescriptorErrorInfo("<ejb-ql>", var2, var1);
   }

   public static void main(String[] var0) {
      String[] var1 = new String[]{"WHERE aa.bb.cc.name = 'www'", "WHERE x > y", "WHERE x = 2 AND y <> -3", "WHERE $x_id = ?1", "WHERE _$x_id <= fieldx", "WHERE xxx IN ('a', 'b', 'c')", "WHERE xxx IN (1, 2, 3)", "WHERE xxx IN (BeanB>>findByName())", "WHERE xxx IN (BeanB>>findByName(?1, ?2))", "WHERE xxx IN (BeanB>>findByName('param1-literal', 'param2-literal'))", "FROM O IN orders WHERE O.orderID = ?1 ORDERBY accountBean.id", "SELECT S FROM S FOR subAccount, I FOR S.institution WHERE ( I.zip = '94702' AND S.subBalance > 20000 ) ORDERBY I.zip, S.subBalance", "WHERE (a = ?1 AND b = ?2) AND (c = ?3 AND d = ?4)", "WHERE a = ?1 AND b = ?2 AND c = ?3", "WHERE ( a = ?1 AND b = ?2 ) AND c = ?3 AND d = ?4 AND e = ?5", "SELECT S FROM S FOR subAccount WHERE S.subAccountType = ?1 AND S.subBalance > ?2", "SELECT stud.firstname FROM StudentBean AS stud WHERE stud.firstname IN (SELECT stud1.firstname FROM StudentBean AS stud1 WHERE stud1.firstname LIKE '%d%' OR stud1.firstname LIKE '%i%' AND stud1.testid = ?1)"};
      if (var0.length == 1) {
         var1 = var0;
      }

      int var2 = var1.length;
      boolean var3 = true;
      if (var0.length == 1) {
         var1 = var0;
      }

      Localizer var4 = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.ejb.container.EJBTextTextLocalizer");

      for(int var5 = 0; var5 < var2; ++var5) {
         EjbqlFinder var6 = null;

         try {
            var6 = new EjbqlFinder("findTest", var1[var5], var3);
         } catch (InvalidFinderException var9) {
            System.out.println(var9.getMessage());
            continue;
         }

         try {
            var6.parseExpression();
         } catch (EJBQLCompilerException var8) {
            System.out.println(var4.get("error") + var8.toString());
         }
      }

      System.out.println(var4.get("completeButCheck"));
   }

   public static String printQueryType(int var0) {
      switch (var0) {
         case 0:
            return "IS_FINDER_LOCAL_BEAN";
         case 1:
         default:
            return "UNKNOWN_QUERY_TYPE";
         case 2:
            return "IS_SELECT_THIS_BEAN";
         case 3:
            return "IS_SELECT_THIS_BEAN_FIELD";
         case 4:
            return "IS_SELECT_LOCAL_BEAN";
         case 5:
            return "IS_SELECT_LOCAL_BEAN_FIELD";
         case 6:
            return "IS_SELECT_RESULT_SET";
      }
   }

   public static String generateGroupSQLNonFinder(RDBMSBean var0, String var1, int var2) throws IllegalExpressionException {
      StringBuffer var3 = new StringBuffer();
      String var4 = "bean";
      List var5 = var0.getPrimaryKeyFields();
      String var6 = "SELECT OBJECT(" + var4 + ") FROM " + var0.getAbstractSchemaName() + " AS " + var4;

      try {
         EjbqlFinder var7 = new EjbqlFinder("find__WL_Group_" + var1, var6);
         var7.setRDBMSBean(var0);
         var7.setGroupName(var1);
         var7.setFinderLoadsBean(false);
         var7.setParameterClassTypes(new Class[0]);
         var7.parseExpression();
         if (debugLogger.isDebugEnabled()) {
            debug("groupSqlNonFinder: group: '" + var1 + "',parsed expression: '" + var6 + "'");
         }

         var7.computeSQLQuery(var0);
         if (debugLogger.isDebugEnabled()) {
            debug("groupSqlNonFinder: group: '" + var1 + "', computed WHERE clause for expression: '" + var6 + "'");
         }

         var3.append("SELECT ");
         var3.append(var7.generateFieldGroupSQLForNonFinder());
         StringBuffer var8 = new StringBuffer();
         var8.append(" WHERE ");
         Iterator var9 = var5.iterator();

         String var10;
         while(var9.hasNext()) {
            var10 = (String)var9.next();
            String var11 = var7.getTableAndFieldForCmpField(var4 + "." + var10);
            if (debugLogger.isDebugEnabled()) {
               debug("groupSqlNonFinder: group: '" + var1 + "', for Pk field: '" + var10 + "', got Pk column: '" + var11 + "'");
            }

            var8.append(var11).append(" = ? ");
            if (var9.hasNext()) {
               var8.append(" AND ");
            }
         }

         var3.append(" FROM ");
         var3.append(var7.getFromSql(var2));
         var3.append(var8.toString());
         var10 = var7.getMainJoinBuffer();
         if (var10.length() > 0) {
            var3.append(" AND ");
         }

         var3.append(var10);
         int var13 = var0.getDatabaseType();
         switch (var13) {
            case 0:
            case 1:
            case 3:
            case 6:
            case 8:
            case 9:
               var3.append(RDBMSUtils.selectForUpdateToString(var2));
               break;
            case 2:
            case 5:
            case 7:
               if (var2 != 0 && var2 != 1 && var2 == 2) {
                  var3.append(RDBMSUtils.selectForUpdateToString(var2));
               }
               break;
            case 4:
               if (var2 == 1) {
                  var3.append(" FOR READ ONLY WITH RS USE AND KEEP UPDATE LOCKS");
               } else if (var2 == 2) {
                  var3.append(RDBMSUtils.selectForUpdateToString(2));
               }
               break;
            default:
               throw new AssertionError("Undefined database type " + var13);
         }

         if (debugLogger.isDebugEnabled()) {
            debug("groupSqlNonFinder: group: '" + var1 + "'  query is: '" + var3.toString() + "'");
         }
      } catch (Exception var12) {
         throw new IllegalExpressionException(7, "Internal Error while attempting to generate an Internal Finder for FieldGroup: '" + var1 + "'.  With Query: '" + var3.toString() + "'    " + var12.toString(), new DescriptorErrorInfo("<group-name>", var0.getEjbName(), var1));
      }

      return var3.toString();
   }

   public CorrelationVarInfo newCorrelationVarInfo(String var1, String var2, boolean var3) {
      return new CorrelationVarInfo(var1, var2, var3);
   }

   public QueryCachingHandler getQueryCachingHandler(Object[] var1, TTLManager var2) {
      return !this.isQueryCachingEnabled() ? new QueryCachingHandler(this) : new QueryCachingHandler(this.getEjbQuery(), this.getMaxElements(), this, var2);
   }

   protected boolean checkIfQueryCachingLegal(RDBMSBean var1) {
      if (!super.checkIfQueryCachingLegal(var1)) {
         return false;
      } else if (this.isPreparedQueryFinder) {
         this.log.logWarning(this.fmt.QUERY_CACHING_NOT_SUPORTED_FOR_PREPARED_QUERY_FINDER(var1.getEjbName(), this.getName()));
         return false;
      } else {
         String var2 = this.getCachingName();
         if (var2 == null) {
            return true;
         } else {
            RelationshipCaching var3 = var1.getRelationshipCaching(var2);
            List var4 = var3.getCachingElements();

            for(int var5 = 0; var5 < var4.size(); ++var5) {
               RelationshipCaching.CachingElement var6 = (RelationshipCaching.CachingElement)var4.get(var5);
               if (!this.checkIfQueryCachingLegal(var6, var1.getRelatedRDBMSBean(var6.getCmrField()))) {
                  this.log.logWarning(this.fmt.QUERY_CACHING_FINDER_HAS_RW_CACHING_ELEMENT_CMR_FIELD(var1.getEjbName(), this.getName(), var6.toString()));
                  return false;
               }
            }

            return true;
         }
      }
   }

   private boolean checkIfQueryCachingLegal(RelationshipCaching.CachingElement var1, RDBMSBean var2) {
      String var3 = var1.getCmrField();
      if (!var2.isReadOnly()) {
         return false;
      } else {
         List var4 = var1.getCachingElements();
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.size(); ++var5) {
               RelationshipCaching.CachingElement var6 = (RelationshipCaching.CachingElement)var4.get(var5);
               if (!this.checkIfQueryCachingLegal(var6, var2.getRelatedRDBMSBean(var6.getCmrField()))) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[EjbqlFinder] " + var0);
   }

   public class CorrelationVarInfo {
      String id = "";
      String value = "";
      boolean isCollectionValue = false;
      boolean isRemoteInterfaceRef = false;

      public CorrelationVarInfo(String var2, String var3, boolean var4) {
         this.id = var2;
         this.value = var3;
         this.isCollectionValue = var4;
      }

      public void setIsRemoteInterfaceRef(boolean var1) {
         this.isRemoteInterfaceRef = var1;
      }

      public String getValue() {
         return this.value;
      }
   }
}
