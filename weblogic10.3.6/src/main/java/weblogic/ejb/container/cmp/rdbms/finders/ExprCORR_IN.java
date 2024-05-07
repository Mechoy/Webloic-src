package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public final class ExprCORR_IN extends BaseExpr implements Expr, ExpressionTypes {
   protected ExprCORR_IN(int var1, Expr var2, Expr var3, String var4) {
      super(var1, var2, var3);
      this.debugClassName = "CORR_IN";
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      requireTerm(this, 2);
      ExprID var1 = (ExprID)this.term1;
      ExprID var2 = (ExprID)this.term2;
      var1.init(this.globalContext, this.queryTree);
      var2.init(this.globalContext, this.queryTree);
      String var3 = var1.getEjbqlID();
      String var4 = var2.getEjbqlID();
      if (debugLogger.isDebugEnabled()) {
         debug("+++ SQLExp: processing old style 'a IN apath.b' Corr var: " + var3 + "  :  " + var4);
      }

      String var5 = this.globalContext.replaceIdAliases(var4);
      if (debugLogger.isDebugEnabled()) {
         debug("+++ SQLExp: set Corr var: " + var3 + "  :  " + var5);
      }

      try {
         this.globalContext.addIdAlias(var3, var5);
         this.queryTree.addCollectionMember(var3);
      } catch (IllegalExpressionException var7) {
         this.markExcAndThrowCollectionException(var7);
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

      this.appendMainEJBQLTokenToList(var1);
      if (this.term2 != null) {
         this.term2.appendEJBQLTokens(var1);
      }

      this.appendPostEJBQLTokensToList(var1);
   }

   private static void debug(String var0) {
      debugLogger.debug("[ExprCORR_IN] " + var0);
   }
}
