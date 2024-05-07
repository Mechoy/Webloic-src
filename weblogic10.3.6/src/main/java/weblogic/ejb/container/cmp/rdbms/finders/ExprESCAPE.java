package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public class ExprESCAPE extends BaseExpr implements Expr, ExpressionTypes {
   protected ExprESCAPE(int var1, Expr var2) {
      super(var1, var2);
      this.debugClassName = "ExprESCAPE ";
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      this.term1.init(this.globalContext, this.queryTree);
   }

   public void calculate_method() throws ErrorCollectionException {
      this.term1.calculate();
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

      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      return this.term1 != null ? this.term1.toSQL() : "";
   }
}
