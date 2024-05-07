package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public final class ExprSELECT_HINT extends BaseExpr implements Expr, ExpressionTypes {
   protected ExprSELECT_HINT(int var1, ExprSTRING var2) {
      super(var1, (Expr)var2);
      this.debugClassName = "ExprSELECT_HINT";
   }

   public void init_method() throws ErrorCollectionException {
   }

   public void calculate_method() throws ErrorCollectionException {
      this.globalContext.setSelectHint(this.term1.getSval());
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() {
      return "";
   }
}
