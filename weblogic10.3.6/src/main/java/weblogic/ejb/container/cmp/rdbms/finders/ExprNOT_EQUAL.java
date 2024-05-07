package weblogic.ejb.container.cmp.rdbms.finders;

import weblogic.utils.ErrorCollectionException;

public final class ExprNOT_EQUAL extends ExprEQUAL implements Expr, ExpressionTypes {
   protected ExprNOT_EQUAL(int var1, Expr var2, Expr var3) {
      super(var1, var2, var3);
      this.isEqual = false;
      this.debugClassName = "ExprNOT_EQUAL ";
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      ExprEQUAL var1 = new ExprEQUAL(5, this.term1, this.term2);
      var1.setPreEJBQLFrom(this);
      var1.setMainEJBQL("= ");
      var1.setPostEJBQLFrom(this);
      return var1;
   }
}
