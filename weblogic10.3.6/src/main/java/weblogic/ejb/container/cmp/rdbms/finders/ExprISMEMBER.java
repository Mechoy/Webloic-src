package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public class ExprISMEMBER extends BaseExpr implements Expr, ExpressionTypes {
   private int lhsRelationshipType;
   private int lhsArgType;
   private boolean lhsIsVariable = false;
   private int databaseType;
   private String lhsFullEjbqlId;
   private String lhsFullDealiasedEjbqlId;
   private String lhsLastPathExpressionElement;
   RDBMSBean lhsBean = null;
   Class lhsInterface = null;
   String lhsInterfaceName = null;
   ExprID truncatedLhsExprID = null;
   ExprID lhsExprIDForQuery = null;
   RDBMSBean lhsPrevBean = null;
   String lhsPrevTableName = null;
   String lhsPrevTableAlias = null;
   private boolean lhsIsRangeVariableIdentifier = false;
   private boolean lhsIsCollectionMemberIdentifier = false;
   private String rhsEjbqlId;
   private String rhsDealiasedEjbqlId;
   private String rhsLastPathExpressionElement;
   protected boolean isMember = true;
   boolean lhsRhsShareRoot = false;
   int lhsNumberOfPathNodes = 0;
   boolean rhsSharesSelectRoot = false;
   SelectNode rhsSelectNodeMatch = null;
   RDBMSBean rhsBean = null;
   Class rhsInterface = null;
   String rhsInterfaceName = null;
   List rhsPKFieldList = null;
   String rhsLastTableName = null;
   String rhsLocalSubqueryLastTableAlias = null;
   private StringBuffer preCalcSQLBuf = null;

   protected ExprISMEMBER(int var1, Expr var2, ExprID var3, boolean var4) {
      super(var1, var2, (Expr)var3);
      this.isMember = var4;
      this.debugClassName = "ExprISMEMBER ";
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      requireTerm(this, 2);
      String var1;
      Loggable var2;
      IllegalExpressionException var3;
      if (this.term1.type() != 17 && this.term1.type() != 25) {
         var1 = this.term1.getSval();
         if (var1 == null) {
            var1 = "???";
         }

         var2 = EJBLogger.logfinderMemberLHSWrongTypeLoggable(var1);
         var3 = new IllegalExpressionException(7, var2.getMessage());
         this.term1.markExcAndAddCollectionException(var3);
         this.addCollectionExceptionAndThrow(var3);
      }

      if (this.term2.type() != 17) {
         var1 = this.term2.getSval();
         if (var1 == null) {
            var1 = "???";
         }

         var2 = EJBLogger.logfinderMemberRHSWrongTypeLoggable(var1);
         Exception var11 = new Exception(var2.getMessage());
         this.term2.markExcAndAddCollectionException(var11);
         this.addCollectionExceptionAndThrow(var11);
      }

      try {
         this.term1.init(this.globalContext, this.queryTree);
      } catch (Exception var7) {
         this.addCollectionException(var7);
      }

      try {
         this.term2.init(this.globalContext, this.queryTree);
      } catch (Exception var6) {
         this.addCollectionException(var6);
      }

      this.throwCollectionException();
      this.lhsArgType = this.term1.type();
      this.databaseType = this.globalContext.getDatabaseType();
      if (this.lhsArgType == 25) {
         this.lhsIsVariable = true;
         this.lhsRelationshipType = -1;
      } else {
         this.lhsFullEjbqlId = ((ExprID)this.term1).getEjbqlID();
         this.lhsFullDealiasedEjbqlId = ((ExprID)this.term1).getDealiasedEjbqlID();

         try {
            this.lhsRelationshipType = ((ExprID)this.term1).getRelationshipTypeForPathExpressionWithNoSQLGen();
         } catch (Exception var5) {
            this.term1.markExcAndAddCollectionException(var5);
            this.addCollectionExceptionAndThrow(var5);
         }

         this.lhsIsRangeVariableIdentifier = this.queryTree.isRangeVariableInScope(this.lhsFullDealiasedEjbqlId);
         this.lhsIsCollectionMemberIdentifier = this.queryTree.isCollectionMemberInScope(this.lhsFullDealiasedEjbqlId);
         if (this.lhsRelationshipType != 2 && this.lhsRelationshipType != 3 && this.lhsRelationshipType != 5 && !this.lhsIsRangeVariableIdentifier && !this.lhsIsCollectionMemberIdentifier) {
            Loggable var9 = EJBLogger.logfinderMemberLHSWrongTypeLoggable(this.lhsFullEjbqlId);
            IllegalExpressionException var8 = new IllegalExpressionException(7, var9.getMessage());
            this.term1.markExcAndAddCollectionException(var8);
            this.addCollectionExceptionAndThrow(var8);
         }

         this.lhsLastPathExpressionElement = QueryContext.getLastFieldFromId(this.lhsFullDealiasedEjbqlId);
         this.lhsNumberOfPathNodes = ((ExprID)this.term1).countPathNodes();
         var1 = ((ExprID)this.term1).getFirstField();
         String var10 = ((ExprID)this.term2).getFirstField();
         if (var1.equals(var10)) {
            this.lhsRhsShareRoot = true;
         } else {
            this.lhsRhsShareRoot = false;
         }
      }

      this.rhsEjbqlId = ((ExprID)this.term2).getEjbqlID();
      this.rhsDealiasedEjbqlId = ((ExprID)this.term2).getDealiasedEjbqlID();
      int var12 = 0;

      try {
         var12 = ((ExprID)this.term2).getRelationshipTypeForPathExpressionWithNoSQLGen();
      } catch (Exception var4) {
         this.term2.markExcAndAddCollectionException(var4);
         this.addCollectionExceptionAndThrow(var4);
      }

      if (var12 != 6 && var12 != 4) {
         var2 = EJBLogger.logfinderMemberRHSWrongTypeLoggable(this.rhsEjbqlId);
         var3 = new IllegalExpressionException(7, var2.getMessage());
         this.term2.markExcAndAddCollectionException(var3);
         this.addCollectionExceptionAndThrow(var3);
      }

      this.rhsSelectNodeMatch = this.queryTree.selectListRootMatch(this.rhsDealiasedEjbqlId);
      if (this.rhsSelectNodeMatch != null) {
         this.rhsSharesSelectRoot = true;
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      try {
         this.term2.calculate();
      } catch (Exception var10) {
         this.addCollectionException(var10);
      }

      this.throwCollectionException();
      this.preCalcSQLBuf = new StringBuffer();

      try {
         this.rhsBean = this.queryTree.getLastRDBMSBeanForPathExpressionWithNoSQLGen(this.rhsDealiasedEjbqlId);
      } catch (Exception var9) {
         this.term2.markExcAndAddCollectionException(var9);
         this.addCollectionExceptionAndThrow(var9);
      }

      this.rhsInterface = QueryContext.getInterfaceClass(this.rhsBean);
      this.rhsInterfaceName = this.rhsInterface.getName();
      this.rhsPKFieldList = this.rhsBean.getPrimaryKeyFields();
      this.rhsLastTableName = this.rhsBean.getTableName();
      String var1 = ((ExprID)this.term2).getFirstField();
      Loggable var2;
      if (!this.queryTree.isRangeVariableInScope(var1)) {
         var2 = EJBLogger.logejbqlMissingRangeVariableDeclarationLoggable(var1);
         IllegalExpressionException var3 = new IllegalExpressionException(7, var2.getMessage());
         this.term2.markExcAndAddCollectionException(var3);
         this.addCollectionExceptionAndThrow(var3);
      }

      if (this.lhsIsVariable) {
         try {
            this.globalContext.addFinderBeanInputParameter(((ExprVARIABLE)this.term1).getVariableNumber(), this.rhsBean);
         } catch (Exception var8) {
            this.term1.markExcAndAddCollectionException(var8);
            this.addCollectionExceptionAndThrow(var8);
         }
      } else {
         try {
            this.term1.calculate();
         } catch (Exception var7) {
            this.addCollectionException(var7);
         }

         this.throwCollectionException();

         try {
            this.lhsBean = this.queryTree.getLastRDBMSBeanForPathExpressionWithNoSQLGen(this.lhsFullDealiasedEjbqlId);
         } catch (Exception var6) {
            this.term1.markExcAndAddCollectionException(var6);
            this.addCollectionExceptionAndThrow(var6);
         }

         this.lhsInterface = QueryContext.getInterfaceClass(this.lhsBean);
         this.lhsInterfaceName = this.lhsInterface.getName();
         if (!this.lhsInterfaceName.equals(this.rhsInterfaceName)) {
            var2 = EJBLogger.logfinderMemberMismatchLoggable(this.lhsInterfaceName, this.rhsInterfaceName);
            this.markExcAndThrowCollectionException(new Exception(var2.getMessage()));
         }

         if (this.lhsRelationshipType != 2 && this.lhsRelationshipType != 5) {
            try {
               this.lhsExprIDForQuery = (ExprID)this.term1;
               this.lhsExprIDForQuery.prepareIdentifierForSQLGen();
            } catch (Exception var4) {
               this.term1.markExcAndAddCollectionException(var4);
               this.addCollectionExceptionAndThrow(var4);
            }
         } else {
            var2 = null;

            try {
               this.truncatedLhsExprID = this.globalContext.setupForLHSForeignKeysWithNoReferenceToRHS((ExprID)this.term1);
               this.lhsExprIDForQuery = this.truncatedLhsExprID;
               JoinNode var11 = this.queryTree.getJoinNodeForLastId(this.truncatedLhsExprID.getDealiasedEjbqlID());
               this.lhsPrevBean = var11.getRDBMSBean();
               this.lhsPrevTableName = this.lhsPrevBean.getTableForCmrField(this.lhsLastPathExpressionElement);
               this.lhsPrevTableAlias = var11.getFKTableAliasAndSQLForCmrf(this.lhsLastPathExpressionElement);
            } catch (Exception var5) {
               this.term1.markExcAndAddCollectionException(var5);
               this.addCollectionExceptionAndThrow(var5);
            }
         }
      }

      if (this.isMember) {
         if (this.lhsIsVariable) {
            this.compute_member_11_fk_on_rhs(this.preCalcSQLBuf);
            return;
         }

         switch (this.lhsRelationshipType) {
            case 1:
            case 3:
               this.compute_member_11_fk_on_rhs(this.preCalcSQLBuf);
               return;
            case 2:
            case 5:
               this.compute_member_11orN1_fk_on_lhs(this.preCalcSQLBuf);
               return;
            case 4:
            default:
               Exception var13 = new Exception("Internal Error,  [NOT] MEMBER [OF] cannot handle lhs relationship type number '" + this.lhsRelationshipType + "'  " + RDBMSUtils.relationshipTypeToString(this.lhsRelationshipType));
               this.term1.markExcAndAddCollectionException(var13);
               this.addCollectionExceptionAndThrow(var13);
         }
      }

      if (!this.isMember) {
         this.setup_not_member();
         QueryNode var14 = this.setup_rhs_local_subquery();
         ExprID var12 = ExprID.newInitExprID(this.globalContext, var14, this.rhsDealiasedEjbqlId);
         if (this.rhsPKFieldList.size() == 1) {
            this.compute_not_member_rhs_simple_key(this.preCalcSQLBuf, var14, var12);
         } else {
            this.compute_not_member_rhs_compound_key(this.preCalcSQLBuf, var14, var12);
         }
      }
   }

   private void compute_member_11orN1_fk_on_lhs(StringBuffer var1) throws ErrorCollectionException {
      JoinNode var2 = null;
      String var3 = null;

      try {
         var2 = this.queryTree.getJoinNodeForLastId(this.rhsDealiasedEjbqlId);
         var3 = var2.getTableAlias();
      } catch (Exception var8) {
         this.term2.markExcAndAddCollectionException(var8);
         this.addCollectionExceptionAndThrow(var8);
      }

      Map var4 = this.lhsPrevBean.getColumnMapForCmrfAndPkTable(this.lhsLastPathExpressionElement, this.rhsLastTableName);
      if (var4 == null) {
         Exception var5 = new Exception("Internal Error !  In MEMBER OF: Could not find Map of Foreign Keys to Primary Keys for Foreign Key Bean: '" + this.lhsPrevBean.getEjbName() + "', To Primary Key Table Name: '" + this.rhsLastTableName + "'");
         this.term2.markExcAndAddCollectionException(var5);
         this.addCollectionExceptionAndThrow(var5);
      }

      Iterator var9 = var4.keySet().iterator();

      while(var9.hasNext()) {
         String var6 = (String)var9.next();
         String var7 = (String)var4.get(var6);
         var1.append(this.lhsPrevTableAlias).append(".").append(var6).append(" = ");
         var1.append(var3).append(".").append(var7).append(" ");
         if (var9.hasNext()) {
            var1.append(" AND ");
         }
      }

   }

   private void compute_member_11_fk_on_rhs(StringBuffer var1) throws ErrorCollectionException {
      Iterator var2 = this.rhsPKFieldList.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         String var4 = "?";
         if (!this.lhsIsVariable) {
            var4 = ExprID.calcTableAndColumn(this.globalContext, this.queryTree, this.lhsFullDealiasedEjbqlId + "." + var3);
         }

         var1.append(var4);
         var1.append(" = ");
         String var5 = ExprID.calcTableAndColumn(this.globalContext, this.queryTree, this.rhsDealiasedEjbqlId + "." + var3);
         var1.append(var5);
         if (var2.hasNext()) {
            var1.append(" AND ");
         }
      }

      var1.append(" ");
   }

   private void setup_not_member() throws ErrorCollectionException {
      if (!this.lhsIsVariable && this.lhsExprIDForQuery.countPathNodes() > 1) {
         if (debugLogger.isDebugEnabled()) {
            debug(" doMember: LHS path node count for parsedLHSId: '" + this.lhsExprIDForQuery.getEjbqlID() + "'  = '" + this.lhsExprIDForQuery.countPathNodes() + "'");
         }

         String var1 = this.lhsExprIDForQuery.getDealiasedEjbqlID();
         boolean var2 = false;
         boolean var3 = false;
         String var4 = "";
         Iterator var5 = this.queryTree.getSelectList().iterator();

         while(var5.hasNext()) {
            SelectNode var6 = (SelectNode)var5.next();
            int var7 = var6.getSelectType();
            String var8 = var6.getSelectTarget();
            var8 = this.globalContext.replaceIdAliases(var8);
            JoinNode var9 = null;

            try {
               var9 = this.queryTree.getJoinTreeForId(var8);
               var8 = JoinNode.getPathWithoutTrailingCmpField(var9, var8);
            } catch (Exception var13) {
               this.markExcAndThrowCollectionException(var13);
            }

            int var10 = JoinNode.comparePaths(var1, var8);
            switch (var10) {
               case -1:
                  break;
               case 0:
                  var3 = true;
                  var4 = var8;
                  break;
               case 1:
                  if (JoinNode.countPathNodes(var8) < JoinNode.countPathNodes(var1)) {
                     if (JoinNode.countPathNodes(var8) > JoinNode.countPathNodes(var4)) {
                        var3 = true;
                        var4 = var8;
                     }
                  } else {
                     var3 = true;
                     var2 = true;
                  }
                  break;
               default:
                  Exception var11 = new Exception("Internal Error !  unknown return type from JoinNode.comparePaths encountered in NOT MEMBER OF calculation, while examining LHS: '" + ((ExprID)this.term1).getEjbqlID() + "' and SELECT id: '" + var8 + "'");
                  this.term1.markExcAndAddCollectionException(var11);
                  this.addCollectionExceptionAndThrow(var11);
            }
         }

         if (!var3) {
            Loggable var14 = EJBLogger.logNotMemberOfLHSNotInSelectLoggable(((ExprID)this.term1).getEjbqlID());
            Exception var15 = new Exception(var14.getMessage());
            this.term1.markExcAndAddCollectionException(var15);
            this.addCollectionExceptionAndThrow(var15);
         }

         if (!var2) {
            if (debugLogger.isDebugEnabled()) {
               debug(" doMember: selectPathLongerThanParsedLHS, mark LeftOuter Joins  from: '" + var4 + "'  to  '" + var1 + "'");
            }

            try {
               JoinNode.markDoLeftOuterJoins(this.lhsExprIDForQuery.getJoinTreeForID(), var4, var1);
            } catch (Exception var12) {
               this.term1.markExcAndAddCollectionException(var12);
               this.addCollectionExceptionAndThrow(var12);
            }
         }
      }

   }

   private QueryNode setup_rhs_local_subquery() throws ErrorCollectionException {
      QueryNode var1 = this.globalContext.newQueryNode((QueryNode)null, -1);
      JoinNode var2 = var1.getJoinTree();
      RDBMSBean var3 = var2.getRDBMSBean();
      RDBMSBean var4 = null;
      String var5 = null;
      String var6 = null;
      String var7 = var3.getAbstractSchemaName();
      if (var7 == null) {
         Loggable var8 = EJBLogger.logFinderCouldNotGetAbstractSchemaNameForBeanLoggable(var3.getEjbName());
         Exception var9 = new Exception(var8.getMessage());
         this.term2.markExcAndAddCollectionException(var9);
         this.addCollectionExceptionAndThrow(var9);
      }

      String var14 = ((ExprID)this.term2).getFirstField();
      String var15 = null;

      try {
         var15 = this.globalContext.getGlobalRangeVariableMap(var14);
      } catch (IllegalExpressionException var13) {
         this.term2.markExcAndAddCollectionException(var13);
         this.addCollectionExceptionAndThrow(var13);
      }

      if (var15.equals(var7)) {
         var4 = var3;
      } else {
         var4 = var3.getRDBMSBeanForAbstractSchema(var15);
         if (null == var4) {
            Loggable var10 = EJBLogger.logFinderCouldNotGetRDBMSBeanForAbstractSchemaNameLoggable(var15);
            Exception var11 = new Exception(var10.getMessage());
            this.term2.markExcAndAddCollectionException(var11);
            this.addCollectionExceptionAndThrow(var11);
         }
      }

      var5 = var4.getTableName();
      var6 = this.globalContext.registerTable(var5);
      JoinNode var16 = new JoinNode(var2, var14, var4, var5, var6, -1, false, false, "", this.globalContext, new ArrayList());
      var2.putChild(var14, var16);

      try {
         var1.addRangeVariable(var14, var15);
      } catch (IllegalExpressionException var12) {
         this.term2.markExcAndAddCollectionException(var12);
         this.addCollectionExceptionAndThrow(var12);
      }

      return var1;
   }

   private void compute_not_member_rhs_simple_key(StringBuffer var1, QueryNode var2, ExprID var3) throws ErrorCollectionException {
      Iterator var4 = this.rhsPKFieldList.iterator();
      String var5 = (String)var4.next();
      String var6 = ExprID.calcTableAndColumn(this.globalContext, var2, this.rhsDealiasedEjbqlId + "." + var5);
      String var15;
      if (this.lhsRelationshipType != 2 && this.lhsRelationshipType != 5) {
         var4 = this.rhsPKFieldList.iterator();
         var5 = (String)var4.next();
         String var14 = "?";
         if (!this.lhsIsVariable) {
            var14 = ExprID.calcTableAndColumn(this.globalContext, this.queryTree, this.lhsFullDealiasedEjbqlId + "." + var5);
         }

         var1.append(var14).append(" ");
      } else {
         if (this.truncatedLhsExprID == null) {
            Exception var7 = new Exception("Internal Error, in compute_not_member_rhs_simple_key truncatedLhsExprID is NULL.  It should NOT be.");
            this.term1.markExcAndAddCollectionException(var7);
            this.addCollectionExceptionAndThrow(var7);
         }

         Map var13 = this.lhsPrevBean.getColumnMapForCmrfAndPkTable(this.lhsLastPathExpressionElement, this.rhsLastTableName);
         if (var13 == null) {
            Exception var8 = new Exception("Internal Error !  In NOT MEMBER OF: Could not find Map of Foreign Keys to Primary Keys for Foreign Key Bean: '" + this.lhsPrevBean.getEjbName() + "', To Primary Key Table Name: '" + this.rhsLastTableName + "'");
            this.term1.markExcAndAddCollectionException(var8);
            this.addCollectionExceptionAndThrow(var8);
         }

         var4 = var13.keySet().iterator();
         var15 = (String)var4.next();
         var1.append(this.lhsPrevTableAlias).append(".").append(var15).append(" ");
      }

      var1.append("NOT IN ");
      var1.append("(SELECT ");
      var1.append(var6).append(" ");
      var1.append("FROM ");
      StringBuffer var16 = new StringBuffer();

      try {
         var1.append(var2.getFROMDeclaration(0));
         var16.append(var2.getMainJoinBuffer());
      } catch (Exception var12) {
         this.markExcAndThrowCollectionException(var12);
      }

      if (this.lhsRhsShareRoot) {
         var15 = this.getSubQueryCorrelatedRootSQL(this.lhsExprIDForQuery.getDealiasedEjbqlID(), this.rhsDealiasedEjbqlId, this.queryTree, var2);
         if (var15.length() > 0) {
            if (var16.length() > 0) {
               var16.append(" AND ");
            }

            var16.append(var15);
         }
      }

      if (this.rhsSharesSelectRoot) {
         var15 = this.rhsSelectNodeMatch.getSelectTarget();
         Object var9 = null;

         try {
            this.queryTree.getJoinTreeForId(var15);
         } catch (Exception var11) {
            this.markExcAndThrowCollectionException(var11);
         }

         String var10 = this.getSubQueryCorrelatedRootSQL(var15, this.rhsDealiasedEjbqlId, this.queryTree, var2);
         if (var10.length() > 0) {
            if (var16.length() > 0) {
               var16.append(" AND ");
            }

            var16.append(var10);
         }
      }

      if (var16.length() > 0) {
         var1.append("WHERE ");
         var1.append(var16);
      }

      var1.append(") ");
   }

   private void compute_not_member_rhs_compound_key(StringBuffer var1, QueryNode var2, ExprID var3) throws ErrorCollectionException {
      JoinNode var4 = null;

      try {
         var4 = var2.getJoinNodeForLastId(this.rhsDealiasedEjbqlId);
      } catch (Exception var16) {
         this.term2.markExcAndAddCollectionException(var16);
         this.addCollectionExceptionAndThrow(var16);
      }

      String var5 = var4.getTableAlias();
      String var6 = var4.getTableName();
      var1.append("NOT ");
      var1.append("EXISTS ");
      var1.append("(SELECT ");
      String var9;
      String var10;
      String var20;
      if (this.lhsRelationshipType != 2 && this.lhsRelationshipType != 5) {
         Iterator var17 = this.rhsPKFieldList.iterator();
         var20 = (String)var17.next();
         var9 = ExprID.calcTableAndColumn(this.globalContext, var2, this.rhsDealiasedEjbqlId + "." + var20);
         var1.append(var9).append(" ");
         var1.append("FROM ");

         try {
            var1.append(var2.getFROMDeclaration(0));
         } catch (Exception var14) {
            this.markExcAndThrowCollectionException(var14);
         }

         var1.append("WHERE ");
         var17 = this.rhsPKFieldList.iterator();

         while(var17.hasNext()) {
            var20 = (String)var17.next();
            var10 = "?";
            if (!this.lhsIsVariable) {
               var10 = ExprID.calcTableAndColumn(this.globalContext, this.queryTree, this.lhsFullDealiasedEjbqlId + "." + var20);
            }

            var1.append(var10);
            var1.append(" = ");
            var9 = ExprID.calcTableAndColumn(this.globalContext, var2, this.rhsDealiasedEjbqlId + "." + var20);
            var1.append(var9);
            if (var17.hasNext()) {
               var1.append(" AND ");
            } else {
               var1.append(" ");
            }
         }
      } else {
         Map var7 = this.lhsPrevBean.getColumnMapForCmrfAndPkTable(this.lhsLastPathExpressionElement, var6);
         if (var7 == null) {
            Exception var8 = new Exception("Internal Error !  In NOT MEMBER OF: Could not find Map of Foreign Keys to Primary Keys for Foreign Key Bean: '" + this.lhsPrevBean.getEjbName() + "', To Primary Key Table Name: '" + this.rhsLastTableName + "'");
            this.term2.markExcAndAddCollectionException(var8);
            this.addCollectionExceptionAndThrow(var8);
         }

         Iterator var19 = var7.keySet().iterator();
         var9 = (String)var19.next();
         var10 = (String)var7.get(var9);
         var1.append(var5).append(".").append(var10).append(" ");
         var1.append("FROM ");

         try {
            var1.append(var2.getFROMDeclaration(0));
         } catch (Exception var15) {
            this.term2.markExcAndAddCollectionException(var15);
            this.addCollectionExceptionAndThrow(var15);
         }

         var1.append("WHERE ");
         var1.append(this.lhsPrevTableAlias).append(".").append(var9);
         var1.append(" = ");
         var1.append(var5).append(".").append(var10);

         while(var19.hasNext()) {
            var1.append(" AND ");
            var9 = (String)var19.next();
            var10 = (String)var7.get(var9);
            var1.append(this.lhsPrevTableAlias).append(".").append(var9);
            var1.append(" = ");
            var1.append(var5).append(".").append(var10);
         }

         var1.append(" ");
      }

      StringBuffer var18 = new StringBuffer();

      try {
         var18.append(var2.getMainJoinBuffer());
      } catch (Exception var13) {
         this.markExcAndThrowCollectionException(var13);
      }

      if (this.lhsRhsShareRoot) {
         var20 = this.getSubQueryCorrelatedRootSQL(this.lhsExprIDForQuery.getDealiasedEjbqlID(), this.rhsDealiasedEjbqlId, this.queryTree, var2);
         if (var20.length() > 0) {
            if (var18.length() > 0) {
               var18.append(" AND ");
            }

            var18.append(var20);
         }
      }

      if (this.rhsSharesSelectRoot) {
         var20 = this.rhsSelectNodeMatch.getSelectTarget();
         var9 = null;

         try {
            this.queryTree.getJoinTreeForId(var20);
         } catch (Exception var12) {
            this.markExcAndThrowCollectionException(var12);
         }

         var10 = this.getSubQueryCorrelatedRootSQL(var20, this.rhsDealiasedEjbqlId, this.queryTree, var2);
         if (var10.length() > 0) {
            if (var18.length() > 0) {
               var18.append(" AND ");
            }

            var18.append(var10);
         }
      }

      if (var18.length() > 0) {
         var1.append("AND ");
         var1.append(var18);
      }

      var1.append(") ");
   }

   private String getSubQueryCorrelatedRootSQL(String var1, String var2, QueryNode var3, QueryNode var4) throws ErrorCollectionException {
      StringBuffer var5 = new StringBuffer();
      JoinNode var6 = var3.getJoinTree();
      String var7 = JoinNode.getFirstFieldFromId(var1);
      String var8 = JoinNode.getFirstFieldFromId(var2);
      JoinNode var9 = null;
      RDBMSBean var10 = null;

      try {
         var9 = JoinNode.getFirstNode(var6, var1);
         var10 = var9.getRDBMSBean();
      } catch (Exception var16) {
         this.markExcAndThrowCollectionException(var16);
      }

      List var11 = var10.getPrimaryKeyFields();
      Iterator var12 = var11.iterator();

      while(var12.hasNext()) {
         String var13 = (String)var12.next();
         String var14 = ExprID.calcTableAndColumn(this.globalContext, var3, var7 + "." + var13);
         String var15 = ExprID.calcTableAndColumn(this.globalContext, var4, var8 + "." + var13);
         var5.append(var14).append(" = ").append(var15);
         if (var12.hasNext()) {
            var5.append(" AND ");
         }
      }

      return var5.toString();
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      ExprISMEMBER var1;
      if (this.isMember) {
         var1 = new ExprISMEMBER(63, this.term1, (ExprID)this.term2, false);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("IS NOT MEMBER ");
         var1.setPostEJBQLFrom(this);
         return var1;
      } else {
         var1 = new ExprISMEMBER(62, this.term1, (ExprID)this.term2, true);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("IS MEMBER ");
         var1.setPostEJBQLFrom(this);
         return var1;
      }
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      this.appendMainEJBQLTokenToList(var1);
      if (this.term2 != null) {
         this.term2.appendEJBQLTokens(var1);
      }

      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      if (this.preCalcSQLBuf == null) {
         throw new ErrorCollectionException("Internal Error !  ExprISMEMBER.toSQL  preCalcSQLBuf is null !");
      } else {
         return this.preCalcSQLBuf.toString();
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[ExprISMEMBER] " + var0);
   }
}
