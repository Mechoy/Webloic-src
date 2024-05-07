package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public final class ExprCOLLECTION_MEMBER extends BaseExpr implements Expr, ExpressionTypes {
   private String ejbql_id;
   private String expanded_id;

   protected ExprCOLLECTION_MEMBER(int var1, Expr var2, Expr var3, String var4) {
      super(var1, var2, var3);
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      requireTerm(this, 2);
      ExprID var1 = (ExprID)this.term1;
      ExprID var2 = (ExprID)this.term2;

      try {
         var1.init(this.globalContext, this.queryTree);
      } catch (Exception var9) {
         this.addCollectionException(var9);
      }

      try {
         var2.init(this.globalContext, this.queryTree);
      } catch (Exception var8) {
         this.addCollectionException(var8);
      }

      this.throwCollectionException();
      String var3 = var1.getEjbqlID();
      String var4 = var2.getEjbqlID();
      if (debugLogger.isDebugEnabled()) {
         debug("+++ SQLExp: processing 'IN(apath.b)a' Corr var: " + var4 + "  :  " + var3);
      }

      String var5 = this.globalContext.replaceIdAliases(var3);
      if (debugLogger.isDebugEnabled()) {
         debug("+++ SQLExp: set Corr var: " + var4 + "  :  " + var5);
      }

      try {
         this.globalContext.addIdAlias(var4, var5);
         this.queryTree.addCollectionMember(var4);
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
      this.appendMainEJBQLTokenToList(var1);
      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      if (this.term2 != null) {
         this.term2.appendEJBQLTokens(var1);
      }

      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() {
      return "";
   }

   private static void debug(String var0) {
      debugLogger.debug("[ExprCOLLECTION_MEMBER] " + var0);
   }
}
