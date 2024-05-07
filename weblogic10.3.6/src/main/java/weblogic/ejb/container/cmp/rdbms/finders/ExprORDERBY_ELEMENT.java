package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.ejb.container.EJBLogger;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public class ExprORDERBY_ELEMENT extends BaseExpr implements Expr, ExpressionTypes {
   protected ExprORDERBY_ELEMENT(int var1, Expr var2, ExprSIMPLE_QUALIFIER var3) {
      super(var1, var2, (Expr)var3);
      this.debugClassName = "ExprORDERBY_ELEMENT ";
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);

      try {
         this.term1.init(this.globalContext, this.queryTree);
         Expr var1 = this.term1;
         if (this.term1.type() == 70 || this.term1.type() == 71) {
            var1 = this.term1.getTerm1();
         }

         if (var1.type() != 17 && var1.type() != 19) {
            Loggable var2 = EJBLogger.logejbqlArgMustBeIDorINTLoggable("ORDERBY", BaseExpr.getTypeName(var1.type()));
            Exception var3 = new Exception(var2.getMessage());
            var1.markExcAndAddCollectionException(var3);
            throw var3;
         }
      } catch (Exception var5) {
         this.addCollectionException(var5);
      }

      if (this.term2 != null) {
         try {
            this.term2.init(this.globalContext, this.queryTree);
         } catch (Exception var4) {
            this.addCollectionException(var4);
         }
      }

   }

   public void calculate_method() throws ErrorCollectionException {
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      if (this.term2 != null) {
         this.term2.appendEJBQLTokens(var1);
      }

      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      return "";
   }
}
