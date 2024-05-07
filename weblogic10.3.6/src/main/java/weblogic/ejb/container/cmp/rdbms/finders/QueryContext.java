package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb20.cmp.rdbms.finders.EJBQLCompilerException;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public class QueryContext {
   private static final DebugLogger debugLogger;
   private static RewriteEjbqlNOT notRewriter;
   private RDBMSBean bean;
   private EjbqlFinder finder;
   private Expr exprTree;
   private QueryNode queryTree;
   private Map tableAliasMap;
   private int tableAliasCount;
   private List tableAliasExclusionList;
   private String ejbqlErrorString;
   private String ejbqlErrorStar;
   private Map idAliasMap;
   private String originalEjbql;
   private boolean ejbqlRewritten = false;
   private int ejbqlRewrittenReasons = 0;
   private ErrorCollectionException warnings;

   public QueryContext(RDBMSBean var1, EjbqlFinder var2, Expr var3) {
      this.bean = var1;
      this.finder = var2;
      this.exprTree = var3;
      this.tableAliasMap = new HashMap();
      this.tableAliasExclusionList = new ArrayList();
      JoinNode var4 = JoinNode.makeJoinRoot(this.bean, this);
      this.queryTree = new QueryNode(this.finder, this, (QueryNode)null, var4);
   }

   public void generateQuery() throws EJBQLCompilerException {
      try {
         this.originalEjbql = ((ExprROOT)this.exprTree).getEJBQLText();
         this.factorOutNOT();
         this.setupForORCrossProducts();
         this.exprTree.init(this, this.getMainQueryTree());
         this.exprTree.calculate();
      } catch (ErrorCollectionException var4) {
         EJBQLCompilerException var2 = this.newEJBQLCompilerException(var4);
         if (debugLogger.isDebugEnabled()) {
            debug("\n\n\n\n");
            debug(" generateQuery() encountered Exception, process Exceptions !");
            String var3 = var2.getMessage();
            debug(var3 + "\n\n");
            debug("\n\n\n\n");
         }

         throw var2;
      }
   }

   private void factorOutNOT() throws ErrorCollectionException {
      RewriteEjbqlNOT var10000 = notRewriter;
      if (RewriteEjbqlNOT.hasNOTExpr((BaseExpr)this.exprTree)) {
         this.ejbqlRewritten = true;
         this.ejbqlRewrittenReasons |= 1;
         if (debugLogger.isDebugEnabled()) {
            debug("\n\n -----------  finder: '" + this.finder.getName() + "'");
            debug("\n\n------------------------------------------QueryContext finder method '" + this.finder.getName() + "' before rewriteNOT \n");
            debug(this.exprTree.dumpString());
            debug("\n");
         }

         RewriteEjbqlNOT var10001 = notRewriter;
         this.exprTree = RewriteEjbqlNOT.rewriteEjbqlNOT((ExprROOT)this.exprTree, debugLogger.isDebugEnabled());
         if (debugLogger.isDebugEnabled()) {
            debug("\n\n QueryContext finder method '" + this.finder.getName() + "' after rewriteNOT \n");
            debug(this.exprTree.dumpString());
            debug("\n");
            debug("--------------------------------------------");
         }

      }
   }

   public String getWhereSql() throws IllegalExpressionException {
      try {
         return ((ExprROOT)this.exprTree).getWhereSql();
      } catch (ErrorCollectionException var4) {
         EJBQLCompilerException var2 = this.newEJBQLCompilerException(var4);
         if (debugLogger.isDebugEnabled()) {
            debug("\n\n\n\n");
            debug(" getWhereSql() encountered Exception !, process Exceptions !");
            String var3 = var2.getMessage();
            debug(var3 + "\n\n");
            debug("\n\n\n");
         }

         throw new IllegalExpressionException(7, var2.getMessage());
      }
   }

   boolean getEjbqlRewritten() {
      return this.ejbqlRewritten;
   }

   int getEjbqlRewrittenReasons() {
      return this.ejbqlRewrittenReasons;
   }

   String getOriginalEjbql() {
      return this.originalEjbql;
   }

   public List getSQLGenEJBQLTokenList() {
      return ((ExprROOT)this.exprTree).getEJBQLTokenList();
   }

   public void addWarning(Exception var1) {
      if (this.warnings == null) {
         this.warnings = new ErrorCollectionException(var1);
      } else {
         this.warnings.add(var1);
      }
   }

   public ErrorCollectionException getWarnings() {
      return this.warnings;
   }

   private EJBQLCompilerException newEJBQLCompilerException(Exception var1) {
      return this.finder.newEJBQLCompilerException(var1, this);
   }

   public QueryNode getMainQueryTree() {
      return this.queryTree;
   }

   private void setupForORCrossProducts() {
      List var1 = ((ExprROOT)this.exprTree).getWHEREList();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ExprWHERE var3 = (ExprWHERE)var2.next();
         Expr var4 = var3.getTerm1();
         if (var4 != null) {
            if (debugLogger.isDebugEnabled()) {
               debug("  setup FirstOR for WHERE " + var3.printEJBQLTree());
            }

            this.findAndSetupFirstOR(var4);
         }
      }

   }

   private void findAndSetupFirstOR(Expr var1) {
      if (var1 instanceof ExprNOT) {
         var1 = var1.getTerm1();
      }

      if (var1 instanceof ExprOR || var1 instanceof ExprAND) {
         if (var1 instanceof ExprOR) {
            if (debugLogger.isDebugEnabled()) {
               debug("findAndSetupFirstOR GOT TOPMOST OR for subtree.  setting ORJoinDataList for OR " + var1.printEJBQLTree());
            }

            ArrayList var4 = new ArrayList();
            ((ExprOR)var1).setOrJoinDataList(var4);
            Expr var3 = var1.getTerm1();
            this.markORSubTree(var4, var3);
            var3 = var1.getTerm2();
            this.markORSubTree(var4, var3);
         } else {
            if (debugLogger.isDebugEnabled()) {
               debug(" findAndSetupFirstOR  term not OR, descend Left and Right ");
            }

            Expr var2 = var1.getTerm1();
            this.findAndSetupFirstOR(var2);
            var2 = var1.getTerm2();
            this.findAndSetupFirstOR(var2);
         }
      }
   }

   private void markORSubTree(List var1, Expr var2) {
      if (var2 instanceof ExprOR || var2 instanceof ExprAND) {
         if (var2 instanceof ExprOR) {
            if (debugLogger.isDebugEnabled()) {
               debug("markORSubTree    OR found setOrJoinDataList on " + var2.printEJBQLTree());
            }

            ((ExprOR)var2).setOrJoinDataList(var1);
         }

         Expr var3 = var2.getTerm1();
         this.markORSubTree(var1, var3);
         var3 = var2.getTerm2();
         this.markORSubTree(var1, var3);
      }
   }

   public QueryNode newQueryNode(QueryNode var1, int var2) {
      return QueryNode.newQueryNode(this.getFinder(), this, var1, var2);
   }

   public List getMainQuerySelectList() {
      return this.queryTree.getSelectList();
   }

   public List getMainQuerySelectListForCachingElement() {
      return this.queryTree.getSelectListForCachingElement();
   }

   public boolean mainQueryContainsInSelectListForCachingElement(RDBMSBean var1, RDBMSBean var2) {
      return this.queryTree.containsInSelectListForCachingElement(var1, var2);
   }

   public JoinNode getRootJoinNodeForMainQuery(String var1) throws IllegalExpressionException {
      return this.queryTree.getJoinNodeForFirstId(var1);
   }

   public JoinNode getJoinTreeForMainQuery() {
      return this.queryTree.getJoinTree();
   }

   public List getTableAliasExclusionListForMainQuery() {
      return this.queryTree.getTableAliasExclusionList();
   }

   public List getTableAndFKColumnListForLocal11or1NPath(QueryNode var1, String var2) throws IllegalExpressionException {
      return JoinNode.getTableAndFKColumnListForLocal11or1NPath(var1, var2);
   }

   public List getTableAndFKColumnListForLocal11or1NPathForMainQuery(String var1) throws IllegalExpressionException {
      return JoinNode.getTableAndFKColumnListForLocal11or1NPath(this.queryTree, var1);
   }

   public String getMainQueryJoinBuffer() throws IllegalExpressionException {
      return this.queryTree.getMainORJoinBuffer();
   }

   public RDBMSBean getRDBMSBean() {
      return this.bean;
   }

   public EjbqlFinder getFinder() {
      return this.finder;
   }

   public String getEjbName() {
      return this.bean.getEjbName();
   }

   public int getDatabaseType() {
      return this.bean.getDatabaseType();
   }

   public Map getGlobalTableAliasMap() {
      return this.tableAliasMap;
   }

   String getTableNameForAlias(String var1) {
      return (String)this.getGlobalTableAliasMap().get(var1);
   }

   String getAliasForTableName(String var1) {
      String var2 = null;
      String var3 = null;
      Iterator var4 = this.tableAliasMap.keySet().iterator();

      while(var4.hasNext()) {
         var2 = (String)var4.next();
         var3 = (String)this.tableAliasMap.get(var2);
         if (var3.equals(var1)) {
            break;
         }
      }

      return var2;
   }

   public String getFinderMethodName() {
      return this.finder.getName();
   }

   public Class getFinderParameterTypeAt(int var1) {
      return this.finder.getParameterTypeAt(var1);
   }

   public boolean queryIsSelect() {
      return this.finder.isSelect();
   }

   public boolean queryIsSelectInEntity() {
      return this.finder.isSelectInEntity();
   }

   public boolean isKeyFinder() {
      return this.finder.isKeyFinder();
   }

   public void setQueryIsSelectThisBean() {
      this.finder.setQueryType(2);
   }

   public boolean getQueryIsSelectThisBean() {
      return this.finder.getQueryType() == 2;
   }

   public void setQueryIsSelectLocalBean() {
      this.finder.setQueryType(4);
   }

   public boolean getQueryIsSelectLocalBean() {
      return this.finder.getQueryType() == 4;
   }

   public void setQueryIsSelectThisBeanField() {
      this.finder.setQueryType(3);
   }

   public void setQueryIsSelectLocalBeanField() {
      this.finder.setQueryType(5);
   }

   public void setQueryIsFinderLocalBean() {
      this.finder.setQueryType(0);
   }

   public boolean getQueryIsFinderLocalBean() {
      return this.finder.getQueryType() == 0;
   }

   public void setQuerySelectBeanTarget(RDBMSBean var1) {
      this.finder.setSelectBeanTarget(var1);
   }

   public void setQuerySelectFieldTableAndColumn(String var1) {
      this.finder.setSelectFieldColumn(var1);
   }

   public void setQuerySelectFieldClass(Class var1) {
      this.finder.setSelectFieldClass(var1);
   }

   public String registerTable(String var1) {
      String var2 = "WL" + this.tableAliasCount++;
      this.tableAliasMap.put(var2, var1);
      return var2;
   }

   public void addFinderInternalQueryParmList(ParamNode var1) {
      this.finder.addInternalQueryParmList(var1);
   }

   public void addFinderRemoteBeanParamList(ParamNode var1) {
      this.finder.addRemoteBeanParamList(var1);
   }

   public void setFinderRemoteBeanCommandEQ(boolean var1) {
      if (var1) {
         this.finder.setRemoteBeanCommand(2);
      } else {
         this.finder.setRemoteBeanCommand(3);
      }

   }

   public String replaceIdAliases(String var1) {
      if (var1 == null) {
         return null;
      } else if (this.idAliasMap != null && var1.length() != 0) {
         StringBuffer var2 = new StringBuffer();
         StringTokenizer var3 = new StringTokenizer(var1, ".");

         while(var3.hasMoreTokens()) {
            String var4 = var3.nextToken();
            String var5 = var4;
            String var6 = (String)this.idAliasMap.get(var4);
            if (var6 != null) {
               var5 = var6;
            }

            var2.append(var5);
            var2.append(".");
         }

         var2.setLength(var2.length() - 1);
         return var2.toString();
      } else {
         return var1;
      }
   }

   public void addIdAlias(String var1, String var2) throws IllegalExpressionException {
      if (this.idAliasMap == null) {
         this.idAliasMap = new HashMap();
      }

      if (this.idAliasMap.get(var1) != null) {
         throw new IllegalExpressionException(7, " Correlation variable '" + var2 + "' is defined more than once ", new DescriptorErrorInfo("<ejb-ql>", this.bean.getEjbName(), this.finder.getName()));
      } else {
         this.idAliasMap.put(var1, var2);
      }
   }

   public void addGlobalRangeVariable(String var1, String var2) throws IllegalExpressionException {
      this.finder.addGlobalRangeVariable(var1, var2);
   }

   public int globalRangeVariableMapSize() {
      return this.finder.globalRangeVariableMapSize();
   }

   public String getGlobalRangeVariableMap(String var1) throws IllegalExpressionException {
      return this.finder.getGlobalRangeVariableMap(var1);
   }

   public List getGlobalRangeVariableMapIdList() {
      return this.finder.getGlobalRangeVariableMapIdList();
   }

   public List getIDsFromGlobalRangeVariableMapForSchema(String var1) {
      return this.finder.getIDsFromGlobalRangeVariableMapForSchema(var1);
   }

   public boolean identifierIsRangeVariable(String var1) {
      String var2 = null;

      try {
         var2 = this.finder.getGlobalRangeVariableMap(var1);
      } catch (IllegalExpressionException var4) {
         return false;
      }

      return var2 != null;
   }

   public boolean pathExpressionEndsInField(QueryNode var1, String var2) throws IllegalExpressionException {
      if (var2 == null) {
         return false;
      } else if (var2.length() == 0) {
         return false;
      } else {
         JoinNode var3 = var1.getJoinTreeForId(var2);
         String var4;
         if (var3 == null) {
            var4 = JoinNode.getFirstFieldFromId(var2);
            throw new IllegalExpressionException(7, "The pathExpression/Identifier '" + var2 + "', contains a root: '" + var4 + "' that is not defined in an AS declaration " + "in the FROM clause.");
         } else {
            var4 = this.replaceIdAliases(var2);
            return JoinNode.endsInField(var3, var4);
         }
      }
   }

   public boolean pathExpressionEndsInRemoteInterface(QueryNode var1, String var2) throws IllegalExpressionException {
      if (var2 == null) {
         return false;
      } else if (var2.length() == 0) {
         return false;
      } else {
         JoinNode var3 = var1.getJoinTreeForId(var2);
         String var4;
         if (var3 == null) {
            var4 = JoinNode.getFirstFieldFromId(var2);
            throw new IllegalExpressionException(7, "The pathExpression/Identifier '" + var2 + "', contains a root: '" + var4 + "' that is not defined in an AS declaration " + "in the FROM clause.");
         } else {
            var4 = this.replaceIdAliases(var2);
            return JoinNode.endsInRemoteInterface(var3, var4);
         }
      }
   }

   public String getTableAndColumnFromMainQuery(String var1) throws IllegalExpressionException {
      String var2 = null;

      try {
         var2 = ExprID.calcTableAndColumn(this, this.getMainQueryTree(), var1);
         return var2;
      } catch (Exception var5) {
         IllegalExpressionException var4 = new IllegalExpressionException(7, var5.getMessage());
         throw var4;
      }
   }

   String getFROMClauseSelectForUpdate(int var1) {
      return RDBMSUtils.getFROMClauseSelectForUpdate(this.bean.getDatabaseType(), var1);
   }

   public String getMainJoinBuffer() throws IllegalExpressionException {
      return this.queryTree.getMainORJoinBuffer();
   }

   public void setMainQuerySelectDistinct() {
      this.finder.setSelectDistinct(true);
   }

   public void setOrderbyColBuf(String var1) {
      this.finder.setOrderbyColBuf(var1);
   }

   public void setOrderbySql(String var1) {
      this.finder.setOrderbySql(var1);
   }

   public void setGroupbySql(String var1) {
      this.finder.setGroupbySql(var1);
   }

   public void setSelectHint(String var1) {
      this.finder.setSelectHint(var1);
   }

   public boolean isResultSetFinder() {
      return this.finder.isResultSetFinder();
   }

   public void setMainQueryResultSetFinder() {
      this.queryTree.setQueryType(6);
   }

   public String getRelationshipCachingName() {
      return this.finder.getCachingName();
   }

   JoinNode makeTrialJoinRoot(JoinNode var1, String var2) throws IllegalExpressionException {
      if (var2 == null) {
         throw new IllegalExpressionException(7, " <cmr-field> " + var2 + " could not get RDBMSBean ! ");
      } else {
         QueryContext var3 = new QueryContext(this.bean, this.finder, this.exprTree);
         JoinNode var4 = new JoinNode((JoinNode)null, "", this.bean, "", "", -1, false, false, "", var3, new ArrayList());
         String var5 = JoinNode.getFirstFieldFromId(var2);
         JoinNode var6 = JoinNode.getFirstNode(var1, var2);
         String var7 = var6.getTableName();
         String var8 = var3.registerTable(var7);
         if (debugLogger.isDebugEnabled()) {
            debug(" makeTrialJoinRoot:  added 1st child JoinNode to trialRoot.  id: '" + var2 + "', the child JoinNode cmr-field was: '" + var6.getPrevCMRField() + "', the child tableName is: '" + var7 + "', the child tableNameAlias is: '" + var8 + "'");
         }

         JoinNode var9 = new JoinNode(var4, var5, var6.getRDBMSBean(), var7, var8, -1, false, false, "", var3, new ArrayList());
         var4.putChild(var5, var9);
         return var4;
      }
   }

   public void addFinderBeanInputParameter(int var1, RDBMSBean var2) throws IllegalExpressionException {
      Class var3 = this.finder.getParameterTypeAt(var1 - 1);
      CMPBeanDescriptor var4 = var2.getCMPBeanDescriptor();
      List var5 = var2.getPrimaryKeyFields();
      boolean var6 = var4.hasComplexPrimaryKey();
      Class var7 = null;
      if (var6) {
         var7 = var4.getPrimaryKeyClass();
      }

      String var8 = "param" + (var1 - 1);
      ParamNode var9 = new ParamNode(var2, var8, var1, var3, "", "", true, false, var7, var6, false);
      int var10 = var5.size();

      for(int var11 = 0; var11 < var10; ++var11) {
         String var12 = (String)var5.get(var11);
         var7 = var4.getFieldClass(var12);
         if (var7 == null) {
            if (debugLogger.isDebugEnabled()) {
               debug("  PK CLASS: " + var12 + " is NULL !!!!");
            }

            Loggable var15 = EJBLogger.logfinderNoPKClassForFieldLoggable(var12);
            throw new IllegalExpressionException(7, "Bean: " + var2.getEjbName() + " " + var15.getMessage());
         }

         boolean var13 = this.isOracleNLSDataType(var12);
         if (var11 == 0 && !var6) {
            var9.setPrimaryKeyClass(var7);
            var9.setOracleNLSDataType(var13);
         }

         ParamNode var14 = new ParamNode(var2, "N_A", var1, var7, var12, "", false, false, var7, false, var13);
         var9.addParamSubList(var14);
      }

      this.finder.addInternalQueryParmList(var9);
   }

   public ExprID setupForLHSForeignKeysWithNoReferenceToRHS(ExprID var1) throws ErrorCollectionException {
      QueryNode var2 = var1.getQueryTree();
      if (var2 == null) {
         throw new ErrorCollectionException("Internal Error, setupForLHSForeignKeysWithNoReferenceToRHS called with ExprID before ExprID.init() has been called.  queryTree is NULL.");
      } else {
         int var3;
         try {
            var3 = var2.getRelationshipTypeForPathExpressionWithNoSQLGen(var1.getDealiasedEjbqlID());
         } catch (Exception var10) {
            throw new ErrorCollectionException(var10);
         }

         if (var3 != 2 && var3 != 5) {
            throw new ErrorCollectionException("Internal Error, setupForLHSForeignKeysWithNoReferenceToRHS called with ExprID  which is not a relationship of type: 'RDBMSUtils.ONE_TO_ONE_RELATION_FK_ON_LHS' or 'RDBMSUtils.MANY_TO_ONE_RELATION', type is: '" + RDBMSUtils.relationshipTypeToString(var3));
         } else {
            String var4 = getLastFieldFromId(var1.getDealiasedEjbqlID());
            ExprID var5 = this.prepareTruncatedPathExpression(var2, var1.getDealiasedEjbqlID());
            JoinNode var6 = null;

            try {
               var6 = var2.getJoinNodeForLastId(var5.getDealiasedEjbqlID());
               RDBMSBean var7 = var6.getRDBMSBean();
               String var8 = var7.getTableForCmrField(var4);
               var6.forceInternalMultiTableJoinMaybe(var2, var8);
               return var5;
            } catch (Exception var9) {
               throw new ErrorCollectionException(var9);
            }
         }
      }
   }

   public ExprID setupForLHSPrimaryKeysWithNoReferenceToRHS(ExprID var1) throws ErrorCollectionException {
      QueryNode var2 = var1.getQueryTree();
      if (var2 == null) {
         throw new ErrorCollectionException("Internal Error, setupForLHSPrimaryKeysWithNoReferenceToRHS called with ExprID before ExprID.init() has been called.  queryTree is NULL.");
      } else {
         int var3;
         try {
            var3 = var2.getRelationshipTypeForPathExpressionWithNoSQLGen(var1.getDealiasedEjbqlID());
         } catch (Exception var6) {
            throw new ErrorCollectionException(var6);
         }

         if (var3 != 4 && var3 != 6 && var3 != 8) {
            throw new ErrorCollectionException("Internal Error, setupForLHSPrimaryKeysWithNoReferenceToRHS called with ExprID  which is not a relationship of type: 'RDBMSUtils.ONE_TO_MANY_RELATION' or 'RDBMSUtils.MANY_TO_MANY_RELATION' or 'RDBMSUtils.REMOTE_RELATION_W_JOIN_TABLE', type is: '" + RDBMSUtils.relationshipTypeToString(var3));
         } else {
            String var4 = getLastFieldFromId(var1.getDealiasedEjbqlID());
            ExprID var5 = this.prepareTruncatedPathExpression(var2, var1.getDealiasedEjbqlID());
            return var5;
         }
      }
   }

   private ExprID prepareTruncatedPathExpression(QueryNode var1, String var2) throws ErrorCollectionException {
      int var3 = var2.lastIndexOf(".");
      if (var3 == -1) {
         Loggable var6 = EJBLogger.logFinderNotNullOnBadPathLoggable(var2);
         throw new ErrorCollectionException(var6.getMessage());
      } else {
         String var4 = var2.substring(0, var3);
         ExprID var5 = ExprID.newInitExprID(this, var1, var4);
         var5.prepareIdentifierForSQLGen();
         return var5;
      }
   }

   public boolean isOracleNLSDataType(String var1) {
      return RDBMSUtils.isOracleNLSDataType(this.bean, this.replaceIdAliases(var1), this.finder.getGlobalRangeVariableMap());
   }

   public static String getFirstFieldFromId(String var0) {
      return JoinNode.getFirstFieldFromId(var0);
   }

   public static String getLastFieldFromId(String var0) {
      return JoinNode.getLastFieldFromId(var0);
   }

   public static Class getInterfaceClass(RDBMSBean var0) {
      CMPBeanDescriptor var1 = var0.getCMPBeanDescriptor();
      return var1.hasLocalClientView() ? var1.getLocalInterfaceClass() : var1.getRemoteInterfaceClass();
   }

   private static void debug(String var0) {
      debugLogger.debug("[QueryContext] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
      notRewriter = new RewriteEjbqlNOT();
   }
}
