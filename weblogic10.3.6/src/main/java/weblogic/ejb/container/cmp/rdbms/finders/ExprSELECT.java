package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RelationshipCaching;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public final class ExprSELECT extends BaseExpr implements Expr, ExpressionTypes {
   private boolean processSelectListForInitializationDone = false;
   private List completeSelectList = null;

   protected ExprSELECT(int var1, Expr var2, Vector var3) {
      super(var1, var2, var3);
      this.debugClassName = "ExprSELECT";
   }

   public void init_method() throws ErrorCollectionException {
      this.setMainQuerySelectDistinct();
      this.setMainQueryResultSetFinder();
      this.createEjbQLSelectList();
      this.createRelationshipCachingList();
      this.processSelectListForInitialization();
      this.processSelectPathsEndingInCmrFields();
      this.mainQueryResultSetFinderCheck();
   }

   public void calculate_method() throws ErrorCollectionException {
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      this.appendMainEJBQLTokenToList(var1);
      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      Enumeration var2 = this.terms.elements();

      while(var2.hasMoreElements()) {
         Expr var3 = (Expr)var2.nextElement();
         var3.appendEJBQLTokens(var1);
         if (var2.hasMoreElements()) {
            this.appendNewEJBQLTokenToList(", ", var1);
         }
      }

      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL(Properties var1) {
      return "";
   }

   private void setMainQuerySelectDistinct() throws ErrorCollectionException {
      if (this.queryTree.isMainQuery()) {
         if (this.term1.type() == 51) {
            this.globalContext.setMainQuerySelectDistinct();
         }

      }
   }

   private void setMainQueryResultSet() throws ErrorCollectionException {
      if (this.queryTree.isMainQuery()) {
         if (this.globalContext.isResultSetFinder()) {
            this.globalContext.setMainQueryResultSetFinder();
         }

      }
   }

   private void createEjbQLSelectList() throws ErrorCollectionException {
      boolean var1 = false;
      Vector var2 = this.terms;
      if (var2.size() < 1) {
         Loggable var3 = EJBLogger.logExpressionRequiresXLoggable("SELECT", "target");
         IllegalExpressionException var4 = new IllegalExpressionException(7, var3.getMessage());
         this.markExcAndThrowCollectionException(var4);
      }

      Iterator var10 = var2.iterator();

      while(var10.hasNext()) {
         Expr var11 = (Expr)var10.next();
         var11.init(this.globalContext, this.queryTree);
         SelectNode var5 = new SelectNode();
         this.queryTree.addSelectList(var5);
         var5.setSelectItemBaseExpr(var11);
         if (debugLogger.isDebugEnabled()) {
            debug(" create Select list item for: '" + TYPE_NAMES[var11.type()] + "'");
         }

         if (var11 instanceof ExprAGGREGATE) {
            var5.setIsAggregate(true);
            if (var11.getTerm2() != null) {
               var11.getTerm2().init(this.globalContext, this.queryTree);
               if (var11.getTerm2().type() == 51) {
                  var5.setIsAggregateDistinct(true);
               }
            }
         }

         IllegalExpressionException var7;
         if (var11 instanceof ExprCASE && !(var11.getTerm1() instanceof ExprID)) {
            Loggable var6 = EJBLogger.logejbqlSelectCaseMustBePathExpressionLoggable(this.globalContext.getEjbName(), var11.getTerm1().getMainEJBQL());
            var7 = new IllegalExpressionException(7, var6.getMessage());
            var11.getTerm1().markExcAndThrowCollectionException(var7);
         }

         var5.setSelectType(var11.type());
         var5.setSelectTypeName(ExpressionTypes.TYPE_NAMES[var11.type()]);
         String var12 = "";
         var7 = null;

         try {
            ExprID var13 = BaseExpr.getExprIDFromSingleExprIDHolder(var11);
            var13.init(this.globalContext, this.queryTree);
            var12 = var13.getEjbqlID();
            if (debugLogger.isDebugEnabled()) {
               debug(" got from ExprID: '" + var12 + "'");
            }

            if (var11 instanceof ExprAGGREGATE) {
               ((ExprAGGREGATE)var11).validate();
            }
         } catch (Exception var9) {
            this.markExcAndAddCollectionException(var9);
            continue;
         }

         var5.setSelectTarget(var12);
      }

      this.throwCollectionException();
   }

   private void processSelectListForInitialization() throws ErrorCollectionException {
      boolean var1 = false;
      List var2 = this.getCompleteSelectList();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         SelectNode var4 = (SelectNode)var3.next();
         Expr var5 = var4.getSelectItemBaseExpr();
         if (debugLogger.isDebugEnabled()) {
            debug("  processSelectListForInitialization  Expr: '" + TYPE_NAMES[var5.type()] + "'");
         }

         try {
            ExprID var6 = BaseExpr.getExprIDFromSingleExprIDHolder(var5);
            var6.init(this.globalContext, this.queryTree);
            String var7 = var6.getEjbqlID();
            int var8;
            Loggable var9;
            IllegalExpressionException var10;
            if (var5.type() == 61) {
               var8 = var6.countPathNodes();
               if (var8 == 1 && !this.queryTree.containsCollectionMember(var7) && this.queryTree.getRangeVariableMap(var7) == null) {
                  var9 = EJBLogger.logejbqlSelectObjectMustBeRangeOrCollectionIdLoggable(var7);
                  var10 = new IllegalExpressionException(7, var9.getMessage());
                  var6.markExcAndThrowCollectionException(var10);
               }
            }

            if (var5.type() == 17) {
               var8 = var6.countPathNodes();
               if (var8 < 2) {
                  if (this.queryTree.thisQueryNodeOwnsId(var7)) {
                     var9 = EJBLogger.logejbqlSELECTmustUseOBJECTargumentLoggable(var7);
                     var10 = new IllegalExpressionException(6, var9.getMessage());
                     this.globalContext.addWarning(var10);
                  } else {
                     var9 = EJBLogger.logInvalidEJBQLSELECTExpressionLoggable(var7);
                     var10 = new IllegalExpressionException(7, var9.getMessage());
                     var6.markExcAndThrowCollectionException(var10);
                  }
               }
            }

            if (debugLogger.isDebugEnabled()) {
               debug("  about to  exprID.prepareIdentifierForSQLGen()  Expr: '" + TYPE_NAMES[var6.type()] + "' val: '" + var6.getEjbqlID() + "'");
            }

            var6.prepareIdentifierForSQLGen();
            Expr var12;
            if (this.globalContext.identifierIsRangeVariable(var7)) {
               if (debugLogger.isDebugEnabled()) {
                  debug("  '" + var7 + "' is Range Variable");
               }

               if (!var4.getIsAggregate() && !var4.getIsAggregateDistinct()) {
                  this.initializeRangeVariable(var4, var6);
               } else {
                  var12 = var4.getSelectItemBaseExpr();
                  if (var12.type() == 48) {
                     this.initializeCountRangeVariable(var4, var6);
                  }
               }
            } else if (var6.isPathExpressionEndingInCmpFieldWithNoSQLGen()) {
               this.initializeCmpField(var4, var6);
            } else if (!var4.getIsAggregate() && !var4.getIsAggregateDistinct()) {
               this.initializeCmrField(var4, var6);
            } else {
               var12 = var4.getSelectItemBaseExpr();
               if (var12.type() == 48) {
                  this.initializeCountCmrField(var4, var6);
               }
            }
         } catch (ErrorCollectionException var11) {
            this.addCollectionException(var11);
         }
      }

      this.processSelectListForInitializationDone = true;
      this.throwCollectionException();
   }

   private void processSelectPathsEndingInCmrFields() throws ErrorCollectionException {
      if (!this.processSelectListForInitializationDone) {
         throw new AssertionError(" processSelectsOnPathsEndingInCmrFields() can  only be called after processSelectListForInitialization() has completed. \n The globalContext \n   QueryIsFinderLocalBean or \n   QueryIsSelectLocalBean \n information must be determined by processSelectListForInitialization()  first before calling processSelectsOnPathsEndingInCmrFields()");
      } else if (this.queryTree.isMainQuery()) {
         if (this.globalContext.getQueryIsSelectThisBean() || this.globalContext.getQueryIsSelectLocalBean() || this.globalContext.getQueryIsFinderLocalBean()) {
            List var1 = this.getCompleteSelectList();
            if (var1.size() <= 1) {
               Iterator var2 = var1.iterator();
               SelectNode var3 = (SelectNode)var2.next();
               Expr var4 = var3.getSelectItemBaseExpr();
               ExprID var5 = BaseExpr.getExprIDFromSingleExprIDHolder(var4);
               String var6 = var5.getDealiasedEjbqlID();
               int var7 = var5.countPathNodes();
               if (var7 > 1) {
                  int var8 = var7 - 1;
                  String var9 = JoinNode.getFirstNFieldsFromId(var6, var8);
                  String var10 = var6;
                  JoinNode var11 = this.queryTree.getJoinTree();

                  try {
                     JoinNode.markDoLeftOuterJoins(var11, var9, var10);
                  } catch (IllegalExpressionException var15) {
                     Loggable var13 = EJBLogger.logMayNotComplyWithEJB21_11_2_7_1_mustReturnAnyNullBeansLoggable(var6);
                     IllegalExpressionException var14 = new IllegalExpressionException(6, var13.getMessage());
                     this.globalContext.addWarning(var14);
                  }

               }
            }
         }
      }
   }

   private void initializeCountRangeVariable(SelectNode var1, ExprID var2) throws ErrorCollectionException {
      String var3 = var2.getEjbqlID();

      assert var3.indexOf(".") == -1 : "Attempt to call initializeCountRangeVariable on a path expression with more than 1 path member '" + var3 + "'";

      JoinNode var4 = this.getRangeVariableJoinNode(var2);
      RDBMSBean var5 = var4.getRDBMSBean();
      List var6 = var5.getPrimaryKeyFields();
      String var7 = (String)var6.get(0);
      var3 = var3 + "." + var7;
      if (debugLogger.isDebugEnabled()) {
         debug("  COUNT new argument is: '" + var3 + "'");
      }

      var2.setEjbqlID(var3);
      var2.init(this.globalContext, this.queryTree);
      var2.prepareIdentifierForSQLGen();
      this.initializeCmpField(var1, var2);
   }

   private void initializeRangeVariable(SelectNode var1, ExprID var2) throws ErrorCollectionException {
      JoinNode var3 = this.getRangeVariableJoinNode(var2);
      var1.setDbmsTarget(var3.getTableAlias());
      RDBMSBean var4 = var3.getRDBMSBean();
      var1.setSelectBean(var4);
      byte var5 = 61;
      var1.setSelectType(var5);
      var1.setSelectTypeName(ExpressionTypes.TYPE_NAMES[var5]);
      if (this.globalContext.queryIsSelect() || this.globalContext.queryIsSelectInEntity()) {
         if (var4.getEjbName().equals(this.globalContext.getRDBMSBean().getEjbName())) {
            if (this.queryTree.isMainQuery()) {
               this.globalContext.setQueryIsSelectThisBean();
            }

            this.queryTree.setQueryType(2);
         } else {
            if (this.queryTree.isMainQuery()) {
               this.globalContext.setQueryIsSelectLocalBean();
            }

            this.queryTree.setQueryType(4);
         }

         if (this.queryTree.isMainQuery()) {
            this.globalContext.setQuerySelectBeanTarget(var4);
         }
      }

   }

   private void initializeCmpField(SelectNode var1, ExprID var2) throws ErrorCollectionException {
      String var3 = var2.getEjbqlID();
      if (debugLogger.isDebugEnabled()) {
         debug("  EJB QL SELECT CLAUSE: " + var3 + "  is a field");
      }

      if (var1.getSelectType() == 61) {
         Loggable var4 = EJBLogger.logejbqlSelectObjectMustBeIdentificationVarNotCMPFieldLoggable(var2.getEjbqlID());
         IllegalExpressionException var5 = new IllegalExpressionException(7, var4.getMessage());
         var2.markExcAndAddCollectionException(var5);
         this.addCollectionExceptionAndThrow(var5);
      }

      String var12 = var2.calcTableAndColumnForCmpField();
      if (debugLogger.isDebugEnabled()) {
         debug("  SELECT Table and Field : " + var12);
      }

      if (this.queryTree.isMainQuery()) {
         this.globalContext.setQuerySelectFieldTableAndColumn(var12);
      }

      var1.setDbmsTarget(var12);
      JoinNode var13 = var2.getJoinNodeForLastCmrFieldWithSQLGen();
      RDBMSBean var6 = var13.getRDBMSBean();
      var1.setSelectBean(var6);
      String var7 = var2.getLastField();
      CMPBeanDescriptor var8 = var6.getCMPBeanDescriptor();
      Class var9 = var8.getFieldClass(var7);
      if (var9 == null) {
         Loggable var10 = EJBLogger.logFinderCouldNotGetClassForIdBeanLoggable("SELECT", var7, var2.getEjbqlID(), var6.getEjbName());
         IllegalExpressionException var11 = new IllegalExpressionException(7, var10.getMessage());
         var2.markExcAndAddCollectionException(var11);
         this.addCollectionExceptionAndThrow(var11);
      }

      if (this.queryTree.isMainQuery()) {
         this.globalContext.setQuerySelectBeanTarget(var6);
         this.globalContext.setQuerySelectFieldClass(var9);
      }

      var1.setSelectClass(var9);
      if (var3.indexOf(".") == -1) {
         this.queryTree.setQueryType(3);
         if (this.queryTree.isMainQuery()) {
            this.globalContext.setQueryIsSelectThisBeanField();
         }
      } else {
         this.queryTree.setQueryType(5);
         if (this.queryTree.isMainQuery()) {
            this.globalContext.setQueryIsSelectLocalBeanField();
         }
      }

   }

   private void initializeCountCmrField(SelectNode var1, ExprID var2) throws ErrorCollectionException {
      String var3 = var2.getEjbqlID();

      assert var3.indexOf(".") != -1 : "Attempt to call initializeCountCmrField on a path expression with only 1 path member '" + var3 + "'";

      JoinNode var4 = null;

      try {
         var4 = var2.getJoinNodeForLastCmrFieldWithSQLGen();
      } catch (Exception var8) {
         var2.markExcAndAddCollectionException(var8);
         this.addCollectionExceptionAndThrow(var8);
      }

      RDBMSBean var5 = var4.getRDBMSBean();
      List var6 = var5.getPrimaryKeyFields();
      String var7 = (String)var6.get(0);
      var3 = var3 + "." + var7;
      if (debugLogger.isDebugEnabled()) {
         debug("  COUNT new argument is: '" + var3 + "'");
      }

      var2.setEjbqlID(var3);
      var2.init(this.globalContext, this.queryTree);
      var2.prepareIdentifierForSQLGen();
      this.initializeCmpField(var1, var2);
   }

   private void initializeCmrField(SelectNode var1, ExprID var2) throws ErrorCollectionException {
      if (debugLogger.isDebugEnabled()) {
         debug("  EJB QL SELECT CLAUSE: " + var2.getEjbqlID() + "  is not a field, it must be a Bean");
      }

      byte var3 = 61;
      var1.setSelectType(var3);
      var1.setSelectTypeName(ExpressionTypes.TYPE_NAMES[var3]);
      JoinNode var4 = null;

      try {
         var4 = var2.getJoinNodeForLastCmrFieldWithSQLGen();
      } catch (Exception var12) {
         var2.markExcAndAddCollectionException(var12);
         this.addCollectionExceptionAndThrow(var12);
      }

      RDBMSBean var5 = var4.getRDBMSBean();
      if (this.queryTree.isMainQuery()) {
         this.globalContext.setQuerySelectBeanTarget(var5);
      }

      var1.setSelectBean(var5);
      var1.setDbmsTarget(var4.getTableAlias());
      if (!this.globalContext.queryIsSelect() && !this.globalContext.queryIsSelectInEntity()) {
         if (this.queryTree.isMainQuery() && !var5.getEjbName().equals(this.globalContext.getRDBMSBean().getEjbName()) && !this.queryTree.containsInSelectListForCachingElement(var1)) {
            Loggable var6 = EJBLogger.logFinderSelectWrongBeanLoggable(this.globalContext.getFinderMethodName(), this.globalContext.getRDBMSBean().getEjbName(), var5.getEjbName());
            IllegalExpressionException var7 = new IllegalExpressionException(7, var6.getMessage());
            var2.markExcAndAddCollectionException(var7);
            this.addCollectionExceptionAndThrow(var7);
         }

         this.queryTree.setQueryType(0);
         if (this.queryTree.isMainQuery()) {
            this.globalContext.setQueryIsFinderLocalBean();
         }

         if (debugLogger.isDebugEnabled()) {
            debug("  EJB QL FINDER LOCAL BEAN set to " + var5.getEjbName());
         }

         List var13 = null;

         try {
            JoinNode var14 = this.queryTree.getJoinTree();
            var13 = JoinNode.getTableAliasListFromChildren(var14);
         } catch (RDBMSException var11) {
            Loggable var8 = EJBLogger.logFinderCouldNotGetXForYLoggable("Table Alias List", "Join Children", var11.getMessage());
            IllegalExpressionException var9 = new IllegalExpressionException(7, var8.getMessage());
            var2.markExcAndAddCollectionException(var9);
            this.addCollectionExceptionAndThrow(var9);
         }

         int var15 = 0;
         String var16 = "WL0";
         Iterator var17 = var13.iterator();

         while(var17.hasNext()) {
            String var10 = (String)var17.next();
            if (var10 != null && var10.equals(var16)) {
               ++var15;
            }
         }

         if (var15 == 0) {
            this.queryTree.addTableAliasExclusionList(var16);
         }
      } else {
         this.queryTree.setQueryType(4);
         if (this.queryTree.isMainQuery()) {
            this.globalContext.setQueryIsSelectLocalBean();
         }

         if (debugLogger.isDebugEnabled()) {
            debug("  EJB QL SELECT LOCAL BEAN set to " + var5.getEjbName());
         }
      }

   }

   private JoinNode getRangeVariableJoinNode(ExprID var1) throws ErrorCollectionException {
      var1.prepareIdentifierForSQLGen();
      String var2 = var1.getEjbqlID();
      JoinNode var3 = null;

      try {
         var3 = this.queryTree.getJoinNodeForFirstId(var2);
      } catch (Exception var6) {
         var1.markExcAndThrowCollectionException(var6);
      }

      if (var3 == null) {
         Loggable var4 = EJBLogger.logFinderSelectTargetNoJoinNodeLoggable(var2);
         IllegalExpressionException var5 = new IllegalExpressionException(7, var4.getMessage());
         var1.markExcAndAddCollectionException(var5);
         this.addCollectionExceptionAndThrow(var5);
      }

      return var3;
   }

   private void setMainQueryResultSetFinder() {
      if (this.queryTree.isMainQuery() && this.globalContext.isResultSetFinder()) {
         this.globalContext.setMainQueryResultSetFinder();
      }

   }

   private void mainQueryResultSetFinderCheck() throws ErrorCollectionException {
      if (this.queryTree.isMainQuery() && this.globalContext.isResultSetFinder()) {
         List var1 = this.queryTree.getSelectList();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            SelectNode var3 = (SelectNode)var2.next();
            if (var3.getSelectType() == 61) {
               Loggable var4 = EJBLogger.logejbqlResultSetFinderCannotSelectBeansLoggable(var3.getSelectTarget(), var3.getSelectTypeName());
               IllegalExpressionException var5 = new IllegalExpressionException(7, var4.getMessage());
               this.markExcAndThrowCollectionException(var5);
            }
         }
      }

   }

   private List getCompleteSelectList() {
      if (this.completeSelectList != null) {
         return this.completeSelectList;
      } else {
         ArrayList var1 = new ArrayList();
         var1.addAll(this.queryTree.getSelectList());
         var1.addAll(this.queryTree.getSelectListForCachingElement());
         return var1;
      }
   }

   private void createRelationshipCachingList() throws ErrorCollectionException {
      if (this.queryTree.isMainQuery()) {
         String var1 = this.globalContext.getRelationshipCachingName();
         if (var1 != null) {
            this.parseRelationshipCaching(this.globalContext.getRDBMSBean(), var1);
         }
      }
   }

   public void parseRelationshipCaching(RDBMSBean var1, String var2) throws ErrorCollectionException {
      RelationshipCaching var3 = var1.getRelationshipCaching(var2);
      if (var3 != null) {
         SelectNode var4 = (SelectNode)this.queryTree.getSelectList().get(0);
         if (var4.getSelectType() != 61) {
            Loggable var5 = EJBLogger.logrelationshipCachingCannotEnableIfSelectTypeIsNotObjectLoggable(var1.getEjbName(), this.globalContext.getFinderMethodName());
            IllegalExpressionException var6 = new IllegalExpressionException(7, var5.getMessage());
            this.markExcAndThrowCollectionException(var6);
         }

         Iterator var7 = var3.getCachingElements().iterator();
         this.parseCachingElements(var1, var7, var4.getSelectTarget());
      }
   }

   private void parseCachingElements(RDBMSBean var1, Iterator var2, String var3) throws ErrorCollectionException {
      while(true) {
         try {
            if (var2.hasNext()) {
               RelationshipCaching.CachingElement var4 = (RelationshipCaching.CachingElement)var2.next();
               String var5 = var4.getCmrField();
               String var6 = var4.getGroupName();
               RDBMSBean var7 = var1.getRelatedRDBMSBean(var5);
               SelectNode var8 = new SelectNode();
               String var9 = var3 + "." + var5;
               var8.setSelectTarget(var9);
               var8.setSelectBean(var7);
               var8.setCachingElementGroupName(var6);
               var8.setPrevBean(var1);
               ExprID var10 = new ExprID(17, var9);
               var10.init(this.globalContext, this.queryTree);
               var8.setSelectItemBaseExpr(var10);
               this.queryTree.addSelectListForCachingElement(var8);
               Iterator var11 = var4.getCachingElements().iterator();
               if (var11.hasNext()) {
                  this.parseCachingElements(var7, var11, var3 + "." + var5);
               }
               continue;
            }
         } catch (Exception var12) {
            this.markExcAndThrowCollectionException(var12);
         }

         return;
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[ExprSELECT] " + var0);
   }
}
