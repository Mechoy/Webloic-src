package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public class ExprTIMES extends ExprSIMPLE_ARITH implements Expr, ExpressionTypes {
   private static final int NOTHING_SPECIAL = 0;
   private static final int TWO_INTEGERS_EITHER_IS_MINUS_1 = 1;
   private static final int MINUS_1_INTEGER_TIMES_FLOAT = 2;
   private int specialHandling = 0;

   protected ExprTIMES(int var1, Expr var2, Expr var3) {
      super(var1, var2, var3);
      this.debugClassName = "ExprTIMES  ";
   }

   public void init_method() throws ErrorCollectionException {
      super.init_method();
      long var1;
      if (this.term1.type() == 19 && this.term2.type() == 19) {
         var1 = this.term1.getIval();
         long var3 = this.term2.getIval();
         if (var1 == -1L || var3 == -1L) {
            this.specialHandling = 1;
         }
      } else if (this.term1.type() == 19 && this.term2.type() == 20) {
         var1 = this.term1.getIval();
         if (var1 == -1L) {
            this.specialHandling = 2;
         }
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      super.calculate_method();
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      if (this.specialHandling == 2) {
         this.appendMainEJBQLTokenToList(var1);
         if (this.term2 != null) {
            this.term2.appendEJBQLTokens(var1);
         }

         this.appendPostEJBQLTokensToList(var1);
      } else {
         if (this.term1 != null) {
            this.term1.appendEJBQLTokens(var1);
         }

         this.appendMainEJBQLTokenToList(var1);
         if (this.term2 != null) {
            this.term2.appendEJBQLTokens(var1);
         }

         this.appendPostEJBQLTokensToList(var1);
      }
   }

   public String toSQL() throws ErrorCollectionException {
      this.clearSQLBuf();
      long var1;
      Exception var5;
      switch (this.specialHandling) {
         case 0:
            return super.toSQL();
         case 1:
            var1 = this.term1.getIval();
            long var3 = this.term2.getIval();
            if (var1 == -1L) {
               this.appendSQLBuf(Long.toString(-1L * var3) + " ");
            } else if (var3 == -1L) {
               this.appendSQLBuf(Long.toString(-1L * var1) + " ");
            } else {
               var5 = new Exception("Internal Error in " + this.debugClassName + ", attempt to perform toSQL for mode " + "TWO_INTEGERS_EITHER_IS_MINUS_1, encountered error. " + "term1 is '" + this.term1.getIval() + "', term2 is '" + this.term2.getIval() + "'");
               this.markExcAndThrowCollectionException(var5);
            }

            return this.getSQLBuf().toString();
         case 2:
            var1 = this.term1.getIval();
            this.appendSQLBuf("-" + this.term2.getSval() + " ");
            return this.getSQLBuf().toString();
         default:
            var5 = new Exception("Internal Error in " + this.debugClassName + ", attempt to perform toSQL using an unknown " + "operand type code: '" + this.type() + "'.  " + "term1 is '" + this.term1.getIval() + "', term2 is '" + this.term2.getIval() + "'");
            this.markExcAndThrowCollectionException(var5);
            return "";
      }
   }
}
