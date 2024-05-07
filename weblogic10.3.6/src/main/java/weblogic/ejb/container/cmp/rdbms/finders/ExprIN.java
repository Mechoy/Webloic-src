package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import weblogic.ejb.container.EJBLogger;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public class ExprIN extends BaseExpr implements Expr, ExpressionTypes {
   private int numRHSTerms;
   private boolean notIn;

   protected ExprIN(int var1, Expr var2, Vector var3, boolean var4) {
      super(var1, var2, var3);
      this.numRHSTerms = 0;
      this.notIn = false;
      this.numRHSTerms = var3.size();
      this.notIn = var4;
      this.debugClassName = "ExprIN ";
   }

   protected ExprIN(int var1, Expr var2, Vector var3) {
      this(var1, var2, var3, false);
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);

      try {
         this.term1.init(this.globalContext, this.queryTree);
      } catch (ErrorCollectionException var5) {
         this.addCollectionException(var5);
      }

      if (this.term1 instanceof ExprID) {
      }

      Loggable var1;
      if (this.terms == null) {
         var1 = EJBLogger.logExpressionRequiresXLoggable("IN", "Vector");
         this.markExcAndAddCollectionException(new IllegalExpressionException(7, var1.getMessage()));
      }

      if (this.numRHSTerms <= 0) {
         var1 = EJBLogger.logExpressionWrongNumberOfTermsLoggable("IN", Integer.toString(this.numRHSTerms));
         this.markExcAndAddCollectionException(new IllegalExpressionException(7, var1.getMessage()));
      }

      for(int var6 = 0; var6 < this.numRHSTerms; ++var6) {
         Expr var2 = (Expr)this.terms.elementAt(var6);

         try {
            var2.init(this.globalContext, this.queryTree);
         } catch (ErrorCollectionException var4) {
            this.addCollectionException(var4);
         }
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      try {
         if (this.term1 instanceof ExprID) {
            ((ExprID)this.term1).calcTableAndColumnForCmpField();
         } else {
            this.term1.calculate();
         }
      } catch (ErrorCollectionException var5) {
         this.addCollectionException(var5);
      }

      for(int var1 = 0; var1 < this.numRHSTerms; ++var1) {
         Expr var2 = (Expr)this.terms.elementAt(var1);

         try {
            if (var2 instanceof ExprID) {
               ((ExprID)var2).calcTableAndColumnForCmpField();
            } else {
               var2.calculate();
            }
         } catch (ErrorCollectionException var4) {
            this.addCollectionException(var4);
         }
      }

   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      ExprIN var1;
      if (this.notIn) {
         var1 = new ExprIN(13, this.term1, this.terms, false);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("IN ");
         var1.setPostEJBQLFrom(this);
         return var1;
      } else {
         var1 = new ExprIN(13, this.term1, this.terms, true);
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
      this.appendNewEJBQLTokenToList("(", var1);
      Enumeration var2 = this.terms.elements();

      while(var2.hasMoreElements()) {
         Expr var3 = (Expr)var2.nextElement();
         var3.appendEJBQLTokens(var1);
         if (var2.hasMoreElements()) {
            this.appendNewEJBQLTokenToList(", ", var1);
         }
      }

      this.appendNewEJBQLTokenToList(") ", var1);
      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      this.clearSQLBuf();
      if (this.notIn) {
         this.appendSQLBuf("NOT ( ");
      }

      this.appendSQLBuf(this.term1.toSQL());
      this.appendSQLBuf("IN ( ");

      for(int var1 = 0; var1 < this.numRHSTerms; ++var1) {
         if (var1 > 0) {
            this.appendSQLBuf(", ");
         }

         Expr var2 = (Expr)this.terms.elementAt(var1);
         this.appendSQLBuf(var2.toSQL());
      }

      this.appendSQLBuf(") ");
      if (this.notIn) {
         this.appendSQLBuf(") ");
      }

      return this.getSQLBuf().toString();
   }
}
