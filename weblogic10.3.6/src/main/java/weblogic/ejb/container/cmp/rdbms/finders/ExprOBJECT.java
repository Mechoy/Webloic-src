package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.utils.ErrorCollectionException;

public final class ExprOBJECT extends BaseExpr implements Expr, SingleExprIDHolder, ExpressionTypes {
   private String ejbqlId;
   private String dealiasedEjbqlId;

   protected ExprOBJECT(int var1, ExprID var2) {
      super(var1, (Expr)var2);
      this.debugClassName = "ExprOBJECT - " + var2.getEjbqlID() + " ";
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      this.term1.init(this.globalContext, this.queryTree);
   }

   public void calculate_method() throws ErrorCollectionException {
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      this.appendMainEJBQLTokenToList(var1);
      this.appendNewEJBQLTokenToList("(", var1);
      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      this.appendNewEJBQLTokenToList(") ", var1);
      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() {
      return "";
   }

   public ExprID getExprID() {
      return (ExprID)this.term1;
   }

   public String getEjbqlID() {
      return ((ExprID)this.term1).getEjbqlID();
   }

   public String getDealiasedEjbqlID() {
      return ((ExprID)this.term1).getDealiasedEjbqlID();
   }
}
