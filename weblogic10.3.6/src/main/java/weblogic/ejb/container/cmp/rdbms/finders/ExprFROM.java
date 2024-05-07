package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import weblogic.utils.ErrorCollectionException;

public final class ExprFROM extends BaseExpr implements Expr, ExpressionTypes {
   private String objectId;

   protected ExprFROM(int var1, Expr var2, Vector var3, String var4) {
      super(var1, var2, var3);
      this.debugClassName = "ExprFROM";
   }

   public void init_method() throws ErrorCollectionException {
      if (this.terms.size() <= 0) {
         IllegalExpressionException var1 = new IllegalExpressionException(7, " Error: the FROM Clause of an EJB QL Query cannot be empty. ");
         this.markExcAndThrowCollectionException(var1);
      }

      Enumeration var5 = this.terms.elements();

      while(var5.hasMoreElements()) {
         Expr var2 = (Expr)var5.nextElement();
         if (debugLogger.isDebugEnabled()) {
            debug(" init on '" + var2.getTypeName() + "'");
         }

         try {
            var2.init(this.globalContext, this.queryTree);
         } catch (ErrorCollectionException var4) {
            this.addCollectionException(var4);
         }
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      Enumeration var1 = this.terms.elements();

      while(var1.hasMoreElements()) {
         Expr var2 = (Expr)var1.nextElement();

         try {
            var2.calculate();
         } catch (ErrorCollectionException var4) {
            this.addCollectionException(var4);
         }
      }

   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      this.appendMainEJBQLTokenToList(var1);
      Enumeration var2 = this.terms.elements();

      while(var2.hasMoreElements()) {
         Expr var3 = (Expr)var2.nextElement();
         var3.appendEJBQLTokens(var1);
         if (var2.hasMoreElements()) {
            this.appendNewEJBQLTokenToList(", ", var1);
         }
      }

      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() {
      return "";
   }

   private static void debug(String var0) {
      debugLogger.debug("[ExprFROM] " + var0);
   }
}
