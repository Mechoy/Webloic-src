package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.utils.ToStringUtils;
import weblogic.ejb20.cmp.rdbms.finders.ExpressionParserException;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public class ExprLIKE extends BaseExpr implements Expr, ExpressionTypes {
   private boolean hasEscape;
   private boolean notLike;

   protected ExprLIKE(int var1, Expr var2, Expr var3, Expr var4, boolean var5) {
      super(var1, var2, var3, var4);
      this.hasEscape = false;
      this.notLike = false;
      this.notLike = var5;
      this.debugClassName = "ExprLIKE ";
   }

   protected ExprLIKE(int var1, Expr var2, Expr var3, Expr var4) {
      this(var1, var2, var3, var4, false);
   }

   public void init_method() throws ErrorCollectionException {
      Loggable var1;
      if (this.term1 == null) {
         var1 = EJBLogger.logLIKEmissingArgumentLoggable();
         this.markExcAndThrowCollectionException(new ExpressionParserException(var1.getMessage()));
      }

      try {
         this.term1.init(this.globalContext, this.queryTree);
      } catch (Exception var4) {
         this.addCollectionException(var4);
      }

      if (this.term1 instanceof ExprID) {
      }

      if (this.term2 == null) {
         var1 = EJBLogger.logLIKEmissingArgumentLoggable();
         this.markExcAndThrowCollectionException(new ExpressionParserException(var1.getMessage()));
      }

      try {
         this.term2.init(this.globalContext, this.queryTree);
      } catch (Exception var3) {
         this.addCollectionException(var3);
      }

      if (this.term3 != null) {
         this.hasEscape = true;

         try {
            this.term3.init(this.globalContext, this.queryTree);
         } catch (Exception var2) {
            this.addCollectionException(var2);
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
      } catch (Exception var4) {
         this.addCollectionException(var4);
      }

      try {
         this.term2.calculate();
      } catch (Exception var3) {
         this.addCollectionException(var3);
      }

      if (this.hasEscape) {
         try {
            this.term3.calculate();
         } catch (Exception var2) {
            this.addCollectionException(var2);
         }
      }

   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      ExprLIKE var1;
      if (this.notLike) {
         var1 = new ExprLIKE(11, this.term1, this.term2, this.term3, false);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("LIKE ");
         var1.setPostEJBQLFrom(this);
         return var1;
      } else {
         var1 = new ExprLIKE(11, this.term1, this.term2, this.term3, true);
         var1.setPreEJBQLFrom(this);
         var1.setMainEJBQL("NOT LIKE ");
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
      if (this.term2 != null) {
         this.term2.appendEJBQLTokens(var1);
      }

      if (this.term3 != null) {
         this.term3.appendEJBQLTokens(var1);
      }

      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      this.clearSQLBuf();
      if (this.notLike) {
         this.appendSQLBuf("NOT ( ");
      }

      this.appendSQLBuf("( ");
      this.appendSQLBuf(this.term1.toSQL());
      this.appendSQLBuf("LIKE ");
      String var1 = this.term2.toSQL();
      String var2 = "";
      if (this.hasEscape) {
         var2 = this.term3.toSQL();
         if (var2 != null && var2.length() > 0 && var2.equals("'\\'")) {
            if (this.term2.type() == 18) {
               var1 = ToStringUtils.escapeBackSlash(var1);
            }

            var2 = "'\\\\'";
         }
      }

      this.appendSQLBuf(var1);
      this.appendSQLBuf(" ");
      if (var2.length() > 0) {
         this.appendSQLBuf("ESCAPE ");
         this.appendSQLBuf(var2);
         this.appendSQLBuf(" ");
      }

      this.appendSQLBuf(") ");
      if (this.notLike) {
         this.appendSQLBuf(") ");
      }

      return this.getSQLBuf().toString();
   }
}
