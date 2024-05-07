package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.ejb.container.EJBLogger;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public final class ExprAGGREGATE extends BaseExpr implements Expr, SingleExprIDHolder, ExpressionTypes {
   private String AGGREGATEId;

   protected ExprAGGREGATE(int var1, ExprID var2) {
      super(var1, (Expr)var2);
   }

   protected ExprAGGREGATE(int var1, ExprID var2, Expr var3) {
      super(var1, var2, (Expr)var3);
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
   }

   public void calculate_method() throws ErrorCollectionException {
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void validate() throws ErrorCollectionException {
      if (this.type != 48 && !((ExprID)this.term1).isPathExpressionEndingInCmpFieldWithNoSQLGen()) {
         Loggable var1 = EJBLogger.logAggregateFunctionMustHaveCMPFieldArgLoggable(this.globalContext.getEjbName(), this.term1.getMainEJBQL());
         IllegalExpressionException var2 = new IllegalExpressionException(7, var1.getMessage());
         this.term1.markExcAndAddCollectionException(var2);
      }

   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      this.appendMainEJBQLTokenToList(var1);
      this.appendNewEJBQLTokenToList("( ", var1);
      if (this.term2 != null) {
         this.term2.appendEJBQLTokens(var1);
      }

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

   public String getEjbqlID() throws ErrorCollectionException {
      if (this.term1.type() != 17) {
         IllegalExpressionException var1 = new IllegalExpressionException(7, " Internal Error in ExprAGGREGATE.getEjbqlID(), term1 is NOT of type: ExpressionTypes.ID");
         this.markExcAndThrowCollectionException(var1);
      }

      return ((ExprID)this.term1).getEjbqlID();
   }
}
