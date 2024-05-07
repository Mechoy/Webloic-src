package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public class ExprAND extends BaseExpr implements Expr, ExpressionTypes {
   protected ExprAND(int var1, Expr var2, Expr var3) {
      super(var1, var2, var3);
      this.debugClassName = "ExprAND  ";
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      requireTerm(this, 2);

      try {
         this.term1.init(this.globalContext, this.queryTree);
      } catch (ErrorCollectionException var3) {
         this.addCollectionException(var3);
      }

      try {
         this.term2.init(this.globalContext, this.queryTree);
      } catch (ErrorCollectionException var2) {
         this.addCollectionException(var2);
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      try {
         this.term1.calculate();
      } catch (ErrorCollectionException var3) {
         this.addCollectionException(var3);
      }

      try {
         this.term2.calculate();
      } catch (ErrorCollectionException var2) {
         this.addCollectionException(var2);
      }

   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      ExprOR var1 = new ExprOR(1, this.term1, this.term2);
      var1.setPreEJBQLFrom(this);
      var1.setMainEJBQL("OR ");
      var1.setPostEJBQLFrom(this);
      return var1;
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
      this.appendSQLBuf("( ");
      this.appendSQLBuf(this.term1.toSQL());
      this.appendSQLBuf(") ");
      this.appendSQLBuf("AND ");
      this.appendSQLBuf("( ");
      this.appendSQLBuf(this.term2.toSQL());
      this.appendSQLBuf(") ");
      return this.getSQLBuf().toString();
   }
}
