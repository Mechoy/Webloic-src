package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public class ExprOR extends BaseExpr implements Expr, ExpressionTypes {
   private ORJoinData orDataTerm1 = null;
   private ORJoinData orDataTerm2 = null;
   private List orJoinDataList = null;

   protected ExprOR(int var1, Expr var2, Expr var3) {
      super(var1, var2, var3);
      this.debugClassName = "ExprOR  ";
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      requireTerm(this, 2);
      if (this.orJoinDataList != null) {
         this.queryTree.addORJoinDataListList(this.orJoinDataList);
      }

      try {
         this.orDataTerm1 = this.init_OR_term(this.globalContext, this.queryTree, this.term1);
      } catch (ErrorCollectionException var3) {
         this.addCollectionException(var3);
      }

      try {
         this.orDataTerm2 = this.init_OR_term(this.globalContext, this.queryTree, this.term2);
      } catch (ErrorCollectionException var2) {
         this.addCollectionException(var2);
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      try {
         this.calculate_OR_term(this.term1, this.orDataTerm1);
      } catch (ErrorCollectionException var3) {
         this.addCollectionException(var3);
      }

      try {
         this.calculate_OR_term(this.term2, this.orDataTerm2);
      } catch (ErrorCollectionException var2) {
         this.addCollectionException(var2);
      }

   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      ExprAND var1 = new ExprAND(0, this.term1, this.term2);
      var1.setPreEJBQLFrom(this);
      var1.setMainEJBQL("AND ");
      var1.setPostEJBQLFrom(this);
      return var1;
   }

   private ORJoinData init_OR_term(QueryContext var1, QueryNode var2, Expr var3) throws ErrorCollectionException {
      var2.pushOR(var3);

      try {
         var3.init(var1, var2);
      } catch (ErrorCollectionException var5) {
         var2.popOR();
         throw var5;
      }

      ORJoinData var4 = var2.popOR();
      if (!(var3 instanceof ExprOR)) {
         this.orJoinDataList.add(var4);
      }

      return var4;
   }

   private void calculate_OR_term(Expr var1, ORJoinData var2) throws ErrorCollectionException {
      this.queryTree.pushOR(var2);

      try {
         var1.calculate();
      } catch (ErrorCollectionException var4) {
         this.queryTree.popOR();
         throw var4;
      }

      this.queryTree.popOR();
   }

   private void toSQL_OR_term(Expr var1, ORJoinData var2) throws ErrorCollectionException {
      String var3 = null;
      this.queryTree.pushOR(var2);

      try {
         this.appendSQLBuf("(");
         this.appendSQLBuf(var1.toSQL());
         var3 = this.queryTree.getCurrentORJoinBuffer();
      } catch (IllegalExpressionException var5) {
         this.queryTree.popOR();
         var1.markExcAndAddCollectionException(var5);
         this.addCollectionExceptionAndThrow(var5);
      }

      if (var3.length() > 0) {
         this.appendSQLBuf(" AND ");
         this.appendSQLBuf(var3);
      }

      this.appendSQLBuf(") ");
      this.queryTree.popOR();
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
      this.clearSQLBuf();
      this.toSQL_OR_term(this.term1, this.orDataTerm1);
      this.appendSQLBuf(" OR ");
      this.toSQL_OR_term(this.term2, this.orDataTerm2);
      return this.getSQLBuf().toString();
   }

   void setOrJoinDataList(List var1) {
      this.orJoinDataList = var1;
   }
}
