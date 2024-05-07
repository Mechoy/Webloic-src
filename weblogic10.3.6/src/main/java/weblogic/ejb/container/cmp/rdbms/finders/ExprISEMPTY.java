package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public class ExprISEMPTY extends BaseExpr implements Expr, ExpressionTypes {
   protected boolean isEmpty = true;
   private int relationshipType;
   private String fullEjbqlId;
   private String fullDealiasedEjbqlId;
   private ExprID lhsExprID = null;
   private String lhsPathExpression = null;
   private JoinNode lhsJoinNode = null;
   private RDBMSBean lhsBean = null;
   private String lhsTableAlias = null;
   private String lhsBeanCMRField = null;
   private StringBuffer preCalcSQLBuf = null;

   protected ExprISEMPTY(int var1, Expr var2, boolean var3) {
      super(var1, var2);
      this.isEmpty = var3;
      this.debugClassName = "ExprISEMPTY ";
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      if (this.term1.type() != 17) {
         Loggable var1 = EJBLogger.logfinderArgMustBeCollectionValuedLoggable("IS [NOT] EMPTY");
         Exception var2 = new Exception(var1.getMessage());
         this.term1.markExcAndAddCollectionException(var2);
         this.addCollectionExceptionAndThrow(var2);
      }

      try {
         this.term1.init(this.globalContext, this.queryTree);
      } catch (Exception var5) {
         this.addCollectionExceptionAndThrow(var5);
      }

      this.fullEjbqlId = ((ExprID)this.term1).getEjbqlID();
      this.fullDealiasedEjbqlId = ((ExprID)this.term1).getDealiasedEjbqlID();

      try {
         this.relationshipType = ((ExprID)this.term1).getRelationshipTypeForPathExpressionWithNoSQLGen();
      } catch (Exception var4) {
         this.term1.markExcAndAddCollectionException(var4);
         this.addCollectionExceptionAndThrow(var4);
      }

      if (this.relationshipType != 4 && this.relationshipType != 6 && this.relationshipType != 8) {
         String var6 = RDBMSUtils.relationshipTypeToString(this.relationshipType);
         Loggable var7 = EJBLogger.logfinderArgMustBeCollectionValuedLoggable("IS (NOT) EMPTY");
         Exception var3 = new Exception(var7.getMessage());
         this.term1.markExcAndAddCollectionException(var3);
         this.addCollectionExceptionAndThrow(var3);
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      this.term1.calculate();
      this.lhsExprID = this.globalContext.setupForLHSPrimaryKeysWithNoReferenceToRHS((ExprID)this.term1);
      this.lhsPathExpression = this.lhsExprID.getDealiasedEjbqlID();

      try {
         this.lhsJoinNode = this.queryTree.getJoinNodeForLastId(this.lhsPathExpression);
      } catch (Exception var2) {
         this.term1.markExcAndThrowCollectionException(var2);
         this.addCollectionExceptionAndThrow(var2);
      }

      this.lhsBean = this.lhsJoinNode.getRDBMSBean();
      this.lhsTableAlias = this.lhsJoinNode.getTableAlias();
      this.lhsBeanCMRField = QueryContext.getLastFieldFromId(this.fullDealiasedEjbqlId);
      this.preCalcSQLBuf = new StringBuffer();
      switch (this.relationshipType) {
         case 4:
            this.compute_one_to_many(this.preCalcSQLBuf);
            return;
         case 5:
         case 7:
         default:
            this.markExcAndThrowCollectionException(new Exception("Internal Error,  IS [NOT] EMPTY cannot handle lhs relationship type number '" + this.relationshipType + "'  " + RDBMSUtils.relationshipTypeToString(this.relationshipType)));
            return;
         case 6:
            this.compute_many_to_many(this.preCalcSQLBuf);
            return;
         case 8:
            this.compute_remote_w_join_table(this.preCalcSQLBuf);
      }
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      ExprISEMPTY var1;
      if (this.isEmpty) {
         var1 = new ExprISEMPTY(41, this.term1, false);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("IS EMPTY ");
         var1.setPostEJBQLFrom(this);
         return var1;
      } else {
         var1 = new ExprISEMPTY(41, this.term1, true);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("IS NOT EMPTY ");
         var1.setPostEJBQLFrom(this);
         return var1;
      }
   }

   private void compute_remote_w_join_table(StringBuffer var1) throws ErrorCollectionException {
      if (this.lhsBean.containsFkField(this.lhsBeanCMRField)) {
         Loggable var2 = EJBLogger.logfinderArgMustBeCollectionValuedLoggable("IS (NOT) EMPTY");
         Exception var3 = new Exception(var2.getMessage());
         this.term1.markExcAndAddCollectionException(var3);
         this.addCollectionExceptionAndThrow(var3);
      }

      String var7 = this.lhsBean.getJoinTableName(this.lhsBeanCMRField);
      Exception var4;
      Loggable var8;
      if (var7 == null) {
         var8 = EJBLogger.logfinderCouldNotGetJoinTableLoggable();
         var4 = new Exception(" IS (NOT) EMPTY  '" + this.fullEjbqlId + "'  " + var8.getMessage());
         this.term1.markExcAndAddCollectionException(var4);
         this.addCollectionExceptionAndThrow(var4);
      }

      if (!this.lhsBean.isForeignKeyField(this.lhsBeanCMRField)) {
         var8 = EJBLogger.logfinderCMRFieldNotFKLoggable(this.lhsBean.getEjbName(), this.lhsBeanCMRField, RDBMSUtils.relationshipTypeToString(this.relationshipType));
         var4 = new Exception(var8.getMessage());
         this.term1.markExcAndAddCollectionException(var4);
         this.addCollectionExceptionAndThrow(var4);
      }

      Iterator var9 = this.lhsBean.getForeignKeyColNames(this.lhsBeanCMRField).iterator();
      Map var10 = this.lhsBean.getCmpFieldToColumnMap();
      var1.append(" ( 0 ");
      if (this.isEmpty) {
         var1.append("=");
      } else {
         var1.append("<");
      }

      var1.append(" (SELECT COUNT(*)  FROM ").append(var7).append(" WHERE ");

      String var6;
      for(; var9.hasNext(); this.addToSelectList(this.lhsBean, this.lhsTableAlias + "." + RDBMSUtils.escQuotedID(var6))) {
         String var5 = (String)var9.next();
         var6 = (String)var10.get(this.lhsBean.getRelatedPkFieldName(this.lhsBeanCMRField, var5));
         var1.append(this.lhsTableAlias + "." + RDBMSUtils.escQuotedID(var6) + " = " + RDBMSUtils.escQuotedID(var7) + "." + RDBMSUtils.escQuotedID(var5));
         if (var9.hasNext()) {
            var1.append(" AND ");
         } else {
            var1.append(" ");
         }
      }

      var1.append(") ) ");
   }

   private void compute_many_to_many(StringBuffer var1) throws ErrorCollectionException {
      String var2 = this.lhsBean.getJoinTableName(this.lhsBeanCMRField);
      Loggable var3;
      Exception var4;
      if (var2 == null) {
         var3 = EJBLogger.logfinderCouldNotGetJoinTableLoggable();
         var4 = new Exception(" IS (NOT) EMPTY  '" + this.fullEjbqlId + "'  " + var3.getMessage());
         this.term1.markExcAndAddCollectionException(var4);
         this.addCollectionExceptionAndThrow(var4);
      }

      if (!this.lhsBean.isForeignKeyField(this.lhsBeanCMRField)) {
         var3 = EJBLogger.logfinderCMRFieldNotFKLoggable(this.lhsBean.getEjbName(), this.lhsBeanCMRField, RDBMSUtils.relationshipTypeToString(this.relationshipType));
         var4 = new Exception(var3.getMessage());
         this.term1.markExcAndAddCollectionException(var4);
         this.addCollectionExceptionAndThrow(var4);
      }

      Iterator var7 = this.lhsBean.getForeignKeyColNames(this.lhsBeanCMRField).iterator();
      Map var8 = this.lhsBean.getCmpFieldToColumnMap();
      var1.append(" ( 0 ");
      if (this.isEmpty) {
         var1.append("=");
      } else {
         var1.append("<");
      }

      var1.append(" (SELECT COUNT(*)  FROM ").append(var2).append(" WHERE ");

      String var6;
      for(; var7.hasNext(); this.addToSelectList(this.lhsBean, this.lhsTableAlias + "." + RDBMSUtils.escQuotedID(var6))) {
         String var5 = (String)var7.next();
         var6 = (String)var8.get(this.lhsBean.getRelatedPkFieldName(this.lhsBeanCMRField, var5));
         var1.append(this.lhsTableAlias + "." + RDBMSUtils.escQuotedID(var6) + " = " + RDBMSUtils.escQuotedID(var2) + "." + RDBMSUtils.escQuotedID(var5));
         if (var7.hasNext()) {
            var1.append(" AND ");
         } else {
            var1.append(" ");
         }
      }

      var1.append(") ) ");
   }

   private void compute_one_to_many(StringBuffer var1) throws ErrorCollectionException {
      RDBMSBean var2 = null;

      try {
         var2 = this.queryTree.getLastRDBMSBeanForPathExpressionWithNoSQLGen(this.fullDealiasedEjbqlId);
      } catch (Exception var10) {
         Loggable var4 = EJBLogger.logFinderCouldNotGetLastJoinNodeLoggable(RDBMSUtils.relationshipTypeToString(this.relationshipType), this.fullEjbqlId, var10.toString());
         Exception var5 = new Exception(var4.getMessage());
         this.term1.markExcAndAddCollectionException(var5);
         this.addCollectionExceptionAndThrow(var5);
      }

      Map var3 = this.lhsBean.getCmpFieldToColumnMap();
      String var11 = null;

      try {
         var11 = this.lhsBean.getRelatedFieldName(this.lhsBeanCMRField);
      } catch (Exception var9) {
         this.term1.markExcAndAddCollectionException(var9);
         this.addCollectionExceptionAndThrow(var9);
      }

      if (var11.length() < 1) {
         Loggable var12 = EJBLogger.logFinderCouldNotFindCMRPointingToBeanLoggable(var2.getEjbName(), this.lhsBean.getEjbName());
         Exception var6 = new Exception(var12.getMessage());
         this.term1.markExcAndAddCollectionException(var6);
         this.addCollectionExceptionAndThrow(var6);
      }

      String var13 = var2.getTableForCmrField(var11);
      if (var13 == null) {
         Loggable var14 = EJBLogger.logfinderCouldNotGetFKTableLoggable();
         Exception var7 = new Exception(" cmr-field: '" + var11 + "'  " + var14.getMessage());
         this.term1.markExcAndAddCollectionException(var7);
         this.addCollectionExceptionAndThrow(var7);
      }

      Iterator var15 = var2.getForeignKeyColNames(var11).iterator();
      var1.append(" ( 0 ");
      if (this.isEmpty) {
         var1.append("=");
      } else {
         var1.append("<");
      }

      var1.append(" (SELECT COUNT(*)  FROM ").append(RDBMSUtils.escQuotedID(var13)).append(" WHERE ");

      String var8;
      for(; var15.hasNext(); this.addToSelectList(this.lhsBean, this.lhsTableAlias + "." + RDBMSUtils.escQuotedID(var8))) {
         String var16 = (String)var15.next();
         var8 = (String)var3.get(var2.getRelatedPkFieldName(var11, var16));
         var1.append(this.lhsTableAlias + "." + RDBMSUtils.escQuotedID(var8) + " = " + var13 + "." + RDBMSUtils.escQuotedID(var16));
         if (var15.hasNext()) {
            var1.append(" AND ");
         } else {
            var1.append(" ");
         }
      }

      var1.append(") ) ");
   }

   private void addToSelectList(RDBMSBean var1, String var2) {
      SelectNode var3 = new SelectNode();
      var3.setSelectType(17);
      var3.setSelectTypeName(ExpressionTypes.TYPE_NAMES[17]);
      var3.setSelectBean(var1);
      var3.setDbmsTarget(var2);
      var3.setCorrelatedSubQuery();
      this.queryTree.addSelectList(var3);
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      this.appendMainEJBQLTokenToList(var1);
      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      return this.preCalcSQLBuf.toString();
   }
}
