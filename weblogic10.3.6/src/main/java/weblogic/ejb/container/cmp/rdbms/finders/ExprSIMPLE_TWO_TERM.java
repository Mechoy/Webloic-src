package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public class ExprSIMPLE_TWO_TERM extends BaseExpr implements Expr, ExpressionTypes {
   private String ejbqlId;
   private String dealiasedEjbqlId;

   protected ExprSIMPLE_TWO_TERM(int var1, Expr var2, Expr var3) {
      super(var1, var2, var3);
      this.debugClassName = "ExprSIMPLE_TWO_TERM  ";
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
         if (this.term1 instanceof ExprID) {
            ((ExprID)this.term1).calcTableAndColumnForCmpField();
         } else {
            this.term1.calculate();
         }
      } catch (ErrorCollectionException var3) {
         this.addCollectionException(var3);
      }

      try {
         if (this.term2 instanceof ExprID) {
            ((ExprID)this.term2).calcTableAndColumnForCmpField();
         } else {
            this.term2.calculate();
         }
      } catch (ErrorCollectionException var2) {
         this.addCollectionException(var2);
      }

      this.throwCollectionException();
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      byte var1;
      String var2;
      switch (this.type()) {
         case 6:
            var1 = 9;
            var2 = ">= ";
            break;
         case 7:
            var1 = 8;
            var2 = "<= ";
            break;
         case 8:
            var1 = 7;
            var2 = "> ";
            break;
         case 9:
            var1 = 6;
            var2 = "< ";
            break;
         default:
            throw new AssertionError("unexpected operator type '" + this.type() + "' in ExprSIMPLE_TWO_TERM.invertForNOT().");
      }

      ExprSIMPLE_TWO_TERM var3 = new ExprSIMPLE_TWO_TERM(var1, this.term1, this.term2);
      var3.setPreEJBQLFrom(this);
      var3.setMainEJBQL(var2);
      var3.setPostEJBQLFrom(this);
      return var3;
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
      this.appendSQLBuf(this.term1.toSQL());
      this.appendSQLBuf(this.getOpString());
      this.appendSQLBuf(this.term2.toSQL());
      return this.getSQLBuf().toString();
   }

   private String getOpString() throws ErrorCollectionException {
      return BaseExpr.getComparisonOpStringFromType(this.type());
   }
}
