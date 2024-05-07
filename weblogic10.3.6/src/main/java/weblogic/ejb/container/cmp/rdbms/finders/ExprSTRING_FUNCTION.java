package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public final class ExprSTRING_FUNCTION extends BaseExpr implements Expr, ExpressionTypes {
   protected ExprSTRING_FUNCTION(int var1, Expr var2) {
      super(var1, var2);
      this.debugClassName = "ExprSTRING_FUNCTION - " + getTypeName(var1);
   }

   protected ExprSTRING_FUNCTION(int var1, Expr var2, Expr var3) {
      super(var1, var2, var3);
      this.debugClassName = "ExprSTRING_FUNCTION - " + getTypeName(var1);
   }

   protected ExprSTRING_FUNCTION(int var1, Expr var2, Expr var3, Expr var4) {
      super(var1, var2, var3, var4);
      this.debugClassName = "ExprSTRING_FUNCTION - " + getTypeName(var1);
   }

   public void init_method() throws ErrorCollectionException {
      switch (this.type()) {
         case 54:
            requireTerm(this, 1);
            requireTerm(this, 2);

            try {
               verifyStringExpressionTerm(this, 1);
            } catch (ErrorCollectionException var19) {
               this.addCollectionException(var19);
            }

            try {
               verifyStringExpressionTerm(this, 2);
            } catch (ErrorCollectionException var18) {
               this.addCollectionException(var18);
            }

            this.throwCollectionException();

            try {
               this.term1.init(this.globalContext, this.queryTree);
            } catch (ErrorCollectionException var17) {
               this.addCollectionException(var17);
            }

            try {
               this.term2.init(this.globalContext, this.queryTree);
            } catch (ErrorCollectionException var16) {
               this.addCollectionException(var16);
            }

            this.throwCollectionException();
            break;
         case 55:
            requireTerm(this, 1);
            requireTerm(this, 2);
            requireTerm(this, 3);

            try {
               verifyStringExpressionTerm(this, 1);
            } catch (ErrorCollectionException var15) {
               this.addCollectionException(var15);
            }

            try {
               this.verifyArithmeticExpressionTerm(this, 2);
            } catch (ErrorCollectionException var14) {
               this.addCollectionException(var14);
            }

            try {
               this.verifyArithmeticExpressionTerm(this, 3);
            } catch (ErrorCollectionException var13) {
               this.addCollectionException(var13);
            }

            this.throwCollectionException();

            try {
               this.term1.init(this.globalContext, this.queryTree);
            } catch (ErrorCollectionException var12) {
               this.addCollectionException(var12);
            }

            try {
               this.term2.init(this.globalContext, this.queryTree);
            } catch (ErrorCollectionException var11) {
               this.addCollectionException(var11);
            }

            try {
               this.term3.init(this.globalContext, this.queryTree);
            } catch (ErrorCollectionException var10) {
               this.addCollectionException(var10);
            }
            break;
         case 56:
            requireTerm(this, 1);
            requireTerm(this, 2);

            try {
               verifyStringExpressionTerm(this, 1);
            } catch (ErrorCollectionException var7) {
               this.addCollectionException(var7);
            }

            try {
               verifyStringExpressionTerm(this, 2);
            } catch (ErrorCollectionException var6) {
               this.addCollectionException(var6);
            }

            if (this.term3 != null) {
               try {
                  this.verifyArithmeticExpressionTerm(this, 3);
               } catch (ErrorCollectionException var5) {
                  this.addCollectionException(var5);
               }
            }

            this.throwCollectionException();

            try {
               this.term1.init(this.globalContext, this.queryTree);
            } catch (ErrorCollectionException var4) {
               this.addCollectionException(var4);
            }

            try {
               this.term2.init(this.globalContext, this.queryTree);
            } catch (ErrorCollectionException var3) {
               this.addCollectionException(var3);
            }

            if (this.term3 != null) {
               try {
                  this.term3.init(this.globalContext, this.queryTree);
               } catch (ErrorCollectionException var2) {
                  this.addCollectionException(var2);
               }
            }
            break;
         case 57:
            requireTerm(this, 1);

            try {
               verifyStringExpressionTerm(this, 1);
            } catch (ErrorCollectionException var9) {
               this.addCollectionException(var9);
            }

            try {
               this.term1.init(this.globalContext, this.queryTree);
            } catch (ErrorCollectionException var8) {
               this.addCollectionException(var8);
            }
            break;
         default:
            Exception var1 = new Exception("Internal Error in " + this.debugClassName + ", unknown function type " + this.type());
            this.markExcAndThrowCollectionException(var1);
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      switch (this.type()) {
         case 54:
            try {
               this.term1.calculate();
            } catch (ErrorCollectionException var9) {
               this.addCollectionException(var9);
            }

            try {
               this.term2.calculate();
            } catch (ErrorCollectionException var8) {
               this.addCollectionException(var8);
            }

            this.throwCollectionException();
            break;
         case 55:
            try {
               this.term1.calculate();
            } catch (ErrorCollectionException var7) {
               this.addCollectionException(var7);
            }

            try {
               this.term2.calculate();
            } catch (ErrorCollectionException var6) {
               this.addCollectionException(var6);
            }

            try {
               this.term3.calculate();
            } catch (ErrorCollectionException var5) {
               this.addCollectionException(var5);
            }

            this.throwCollectionException();
            break;
         case 56:
            try {
               this.term1.calculate();
            } catch (ErrorCollectionException var4) {
               this.addCollectionException(var4);
            }

            try {
               this.term2.calculate();
            } catch (ErrorCollectionException var3) {
               this.addCollectionException(var3);
            }

            if (this.term3 != null) {
               try {
                  this.term3.calculate();
               } catch (ErrorCollectionException var2) {
                  this.addCollectionException(var2);
               }
            }
            break;
         case 57:
            this.term1.calculate();
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
      this.appendNewEJBQLTokenToList("( ", var1);
      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      if (this.term2 != null) {
         this.appendNewEJBQLTokenToList(", ", var1);
         this.term2.appendEJBQLTokens(var1);
      }

      if (this.term3 != null) {
         this.appendNewEJBQLTokenToList(", ", var1);
         this.term3.appendEJBQLTokens(var1);
      }

      this.appendNewEJBQLTokenToList(") ", var1);
      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      StringBuffer var1 = new StringBuffer();
      switch (this.type()) {
         case 54:
            this.getConcatFunction(var1);
            break;
         case 55:
            this.getSubstringFunction(var1);
            break;
         case 56:
            this.getLocateFunction(var1);
            break;
         case 57:
            this.getLengthFunction(var1);
            break;
         default:
            Exception var2 = new Exception("Internal Error in " + this.debugClassName + ", unknown function type " + this.type());
            this.markExcAndThrowCollectionException(var2);
      }

      return var1.toString();
   }

   private void getConcatFunction(StringBuffer var1) throws ErrorCollectionException {
      switch (this.globalContext.getDatabaseType()) {
         case 1:
         case 4:
         case 9:
            var1.append(" CONCAT( ");
            var1.append(this.term1.toSQL()).append(", ");
            var1.append(this.term2.toSQL());
            var1.append(" ) ");
            break;
         case 2:
         case 5:
         case 7:
            var1.append(this.term1.toSQL()).append("+");
            var1.append(this.term2.toSQL());
            break;
         case 3:
         case 6:
         case 8:
         default:
            var1.append(" { fn CONCAT( ");
            var1.append(this.term1.toSQL()).append(", ");
            var1.append(this.term2.toSQL());
            var1.append(" ) } ");
      }

   }

   private void getSubstringFunction(StringBuffer var1) throws ErrorCollectionException {
      switch (this.globalContext.getDatabaseType()) {
         case 1:
         case 4:
         case 9:
            var1.append(" SUBSTR( ");
            var1.append(this.term1.toSQL()).append(", ");
            var1.append(this.term2.toSQL());
            if (this.term3 != null) {
               var1.append(", ");
               var1.append(this.term3.toSQL());
            }

            var1.append(" ) ");
            break;
         case 2:
         case 5:
         case 7:
            var1.append(" SUBSTRING( ");
            var1.append(this.term1.toSQL()).append(", ");
            var1.append(this.term2.toSQL()).append(", ");
            var1.append(this.term3.toSQL());
            var1.append(" ) ");
            break;
         case 3:
            var1.append(" SUBSTRING( ");
            var1.append(this.term1.toSQL()).append(" FROM ");
            var1.append(this.term2.toSQL()).append(" FOR ");
            var1.append(this.term3.toSQL());
            var1.append(" ) ");
            break;
         case 6:
         case 8:
         default:
            var1.append(" { fn SUBSTRING( ");
            var1.append(this.term1.toSQL()).append(", ");
            var1.append(this.term2.toSQL()).append(", ");
            var1.append(this.term3.toSQL());
            var1.append(" ) } ");
      }

   }

   private void getLengthFunction(StringBuffer var1) throws ErrorCollectionException {
      switch (this.globalContext.getDatabaseType()) {
         case 1:
         case 3:
         case 4:
         case 9:
            var1.append(" LENGTH( ");
            var1.append(this.term1.toSQL());
            var1.append(" ) ");
            break;
         case 2:
         case 7:
            var1.append(" LEN( ");
            var1.append(this.term1.toSQL());
            var1.append(" ) ");
            break;
         case 5:
            var1.append(" CHAR_LENGTH( ");
            var1.append(this.term1.toSQL());
            var1.append(" ) ");
            break;
         case 6:
         case 8:
         default:
            var1.append(" { fn LENGTH( ");
            var1.append(this.term1.toSQL());
            var1.append(" ) } ");
      }

   }

   private void getLocateFunction(StringBuffer var1) throws ErrorCollectionException {
      switch (this.globalContext.getDatabaseType()) {
         case 1:
            var1.append(" INSTR( ");
            var1.append(this.term2.toSQL()).append(", ");
            var1.append(this.term1.toSQL());
            var1.append(" )  ");
            break;
         case 2:
         case 7:
            var1.append(" CHARINDEX(");
            var1.append(this.term1.toSQL()).append(", ");
            var1.append(this.term2.toSQL());
            if (this.term3 != null) {
               var1.append(", ");
               var1.append(this.term3.toSQL());
            }

            var1.append(" ) ");
            break;
         case 3:
         case 6:
         case 8:
         default:
            var1.append(" { fn LOCATE( ");
            var1.append(this.term1.toSQL()).append(", ");
            var1.append(this.term2.toSQL());
            if (this.term3 != null) {
               var1.append(", ");
               var1.append(this.term3.toSQL());
            }

            var1.append(" ) } ");
            break;
         case 4:
         case 9:
            var1.append(" LOCATE(");
            var1.append(this.term1.toSQL()).append(", ");
            var1.append(this.term2.toSQL());
            if (this.term3 != null) {
               var1.append(", ");
               var1.append(this.term3.toSQL());
            }

            var1.append(" ) ");
            break;
         case 5:
            var1.append(" CHARINDEX(");
            var1.append(this.term1.toSQL()).append(", ");
            var1.append(this.term2.toSQL());
            var1.append(" ) ");
      }

   }
}
