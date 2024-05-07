package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.ejb.container.utils.ToStringUtils;
import weblogic.utils.ErrorCollectionException;

public final class ExprSTRING extends BaseExpr implements Expr, ExpressionTypes {
   private String ejbqlId;
   private String dealiasedEjbqlId;

   protected ExprSTRING(int var1, String var2) {
      super(var1, var2);
      this.ejbqlId = var2;
      this.debugClassName = "ExprSTRING - " + var2;
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
      StringBuffer var1 = new StringBuffer();
      var1.append("'");
      var1.append(ToStringUtils.escapedQuotesToString(this.getSval()));
      var1.append("'");
      return var1.toString();
   }
}
