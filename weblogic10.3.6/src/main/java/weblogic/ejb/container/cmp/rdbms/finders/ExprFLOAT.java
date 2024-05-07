package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public final class ExprFLOAT extends BaseExpr implements Expr, ExpressionTypes {
   protected ExprFLOAT(int var1, String var2) {
      super(var1, var2);
      this.debugClassName = "ExprFLOAT - " + var2;
   }

   public void init_method() throws ErrorCollectionException {
   }

   public void calculate_method() throws ErrorCollectionException {
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      this.appendMainEJBQLTokenToList(var1);
      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() {
      return this.getSval() + " ";
   }
}
