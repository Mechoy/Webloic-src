package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public final class ExprARITH_FUNCTION extends BaseExpr implements Expr, ExpressionTypes {
   protected ExprARITH_FUNCTION(int var1, Expr var2) {
      super(var1, var2);
      this.debugClassName = "ExprARITH_FUNCTION - " + getTypeName(var1);
   }

   protected ExprARITH_FUNCTION(int var1, Expr var2, Expr var3) {
      super(var1, var2, var3);
      this.debugClassName = "ExprARITH_FUNCTION - " + getTypeName(var1);
   }

   public void init_method() throws ErrorCollectionException {
      switch (this.type()) {
         case 58:
         case 59:
            requireTerm(this, 1);

            try {
               this.verifyArithmeticExpressionTerm(this, 1);
            } catch (ErrorCollectionException var7) {
               this.addCollectionException(var7);
            }

            this.throwCollectionException();

            try {
               this.term1.init(this.globalContext, this.queryTree);
            } catch (ErrorCollectionException var6) {
               this.addCollectionException(var6);
            }

            this.throwCollectionException();
            break;
         case 76:
            requireTerm(this, 1);
            requireTerm(this, 2);

            try {
               this.verifyArithmeticExpressionTerm(this, 1);
            } catch (ErrorCollectionException var5) {
               this.addCollectionException(var5);
            }

            try {
               this.verifyArithmeticExpressionTerm(this, 2);
            } catch (ErrorCollectionException var4) {
               this.addCollectionException(var4);
            }

            this.throwCollectionException();

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

            this.throwCollectionException();
            break;
         default:
            Exception var1 = new Exception("Internal Error in " + this.debugClassName + ", unknown function type " + this.type());
            this.markExcAndThrowCollectionException(var1);
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      switch (this.type()) {
         case 58:
         case 59:
            try {
               this.term1.calculate();
            } catch (ErrorCollectionException var4) {
               this.addCollectionException(var4);
            }

            this.throwCollectionException();
            break;
         case 76:
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

            this.throwCollectionException();
            break;
         default:
            Exception var1 = new Exception("Internal Error in " + this.debugClassName + ", unknown function type " + this.type());
            this.markExcAndThrowCollectionException(var1);
      }

   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      this.appendMainEJBQLTokenToList(var1);
      if (this.term1 != null) {
         this.appendNewEJBQLTokenToList("( ", var1);
         this.term1.appendEJBQLTokens(var1);
         if (this.term2 != null) {
            this.appendNewEJBQLTokenToList(", ", var1);
            this.term2.appendEJBQLTokens(var1);
         }

         this.appendNewEJBQLTokenToList(") ", var1);
      }

      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      StringBuffer var1 = new StringBuffer();
      switch (this.type()) {
         case 58:
            this.getAbsFunction(var1);
            break;
         case 59:
            this.getSqrtFunction(var1);
            break;
         case 76:
            this.getModFunction(var1);
            break;
         default:
            Exception var2 = new Exception("Internal Error in " + this.debugClassName + ", unknown function type " + this.type());
            this.markExcAndThrowCollectionException(var2);
      }

      return var1.toString();
   }

   private void getAbsFunction(StringBuffer var1) throws ErrorCollectionException {
      int var2 = this.globalContext.getDatabaseType();
      if (var2 != 4 && var2 != 1 && var2 != 2 && var2 != 7 && var2 != 5 && var2 != 9) {
         var1.append(" { fn ABS( ");
         var1.append(this.term1.toSQL());
         var1.append(" ) } ");
      } else {
         var1.append(" ABS( ");
         var1.append(this.term1.toSQL());
         var1.append(" )");
      }

   }

   private void getSqrtFunction(StringBuffer var1) throws ErrorCollectionException {
      int var2 = this.globalContext.getDatabaseType();
      if (var2 != 4 && var2 != 1 && var2 != 2 && var2 != 7 && var2 != 5 && var2 != 9) {
         var1.append(" { fn SQRT( ");
         var1.append(this.term1.toSQL());
         var1.append(" ) } ");
      } else {
         var1.append(" SQRT( ");
         var1.append(this.term1.toSQL());
         var1.append(" ) ");
      }

   }

   private void getModFunction(StringBuffer var1) throws ErrorCollectionException {
      int var2 = this.globalContext.getDatabaseType();
      if (var2 != 4 && var2 != 1 && var2 != 3 && var2 != 8 && var2 != 6 && var2 != 9) {
         if (var2 != 2 && var2 != 7 && var2 != 5) {
            var1.append(" { fn Mod( ");
            var1.append(this.term1.toSQL());
            var1.append(", ");
            var1.append(this.term2.toSQL());
            var1.append(" ) } ");
         } else {
            var1.append(" ( ");
            var1.append(this.term1.toSQL());
            var1.append(" % ");
            var1.append(this.term2.toSQL());
            var1.append(" ) ");
         }
      } else {
         var1.append(" MOD( ");
         var1.append(this.term1.toSQL());
         var1.append(", ");
         var1.append(this.term2.toSQL());
         var1.append(" ) ");
      }

   }
}
