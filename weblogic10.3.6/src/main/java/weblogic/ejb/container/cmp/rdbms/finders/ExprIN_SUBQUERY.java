package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public class ExprIN_SUBQUERY extends BaseExpr implements Expr, ExpressionTypes {
   private static final int SUBQUERY_IN_ON_BEAN = 0;
   private static final int SUBQUERY_IN_ON_FIELD = 1;
   private int inType = 1;
   private boolean notIn = false;
   private StringBuffer preCalcSQLBuf = null;

   protected ExprIN_SUBQUERY(int var1, Expr var2, Expr var3, boolean var4) {
      super(var1, var2, var3);
      this.notIn = var4;
      this.debugClassName = "ExprIN_SUBQUERY ";
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      requireTerm(this, 2);

      try {
         this.term1.init(this.globalContext, this.queryTree);
      } catch (Exception var3) {
         this.addCollectionException(var3);
      }

      try {
         this.term2.init(this.globalContext, this.queryTree);
      } catch (Exception var2) {
         this.addCollectionException(var2);
      }

      if (ExprEQUAL.isCalcEQonSubQuerySelectBean(this)) {
         this.inType = 0;
      } else {
         this.inType = 1;
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      this.preCalcSQLBuf = new StringBuffer();
      if (this.inType == 0) {
         ExprEQUAL.doCalcEQonSubQuerySelectBean(this.globalContext, this.queryTree, this, this.preCalcSQLBuf, !this.notIn, true);
      } else {
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
            this.term2.calculate();
         } catch (ErrorCollectionException var2) {
            this.addCollectionException(var2);
         }

      }
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      ExprIN_SUBQUERY var1;
      if (this.notIn) {
         var1 = new ExprIN_SUBQUERY(this.type, this.term1, this.term2, false);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("IN  ");
         var1.setPostEJBQLFrom(this);
         return var1;
      } else {
         var1 = new ExprIN_SUBQUERY(this.type, this.term1, this.term2, true);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("NOT IN ");
         var1.setPostEJBQLFrom(this);
         return var1;
      }
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      this.appendMainEJBQLTokenToList(var1);
      this.appendNewEJBQLTokenToList("( ", var1);
      if (this.term2 != null) {
         this.term2.appendEJBQLTokens(var1);
      }

      this.appendNewEJBQLTokenToList(") ", var1);
      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      this.clearSQLBuf();
      if (this.inType == 0) {
         return this.preCalcSQLBuf.toString();
      } else {
         this.appendSQLBuf(this.term1.toSQL());
         if (this.notIn) {
            this.appendSQLBuf("NOT ");
         }

         this.appendSQLBuf("IN ");
         this.appendSQLBuf(this.term2.toSQL());
         return this.getSQLBuf().toString();
      }
   }
}
