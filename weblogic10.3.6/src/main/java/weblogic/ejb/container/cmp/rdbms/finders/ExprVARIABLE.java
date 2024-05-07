package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.ejb.container.EJBLogger;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public final class ExprVARIABLE extends BaseExpr implements Expr, ExpressionTypes {
   private String ejbqlId;
   private String dealiasedEjbqlId;
   private String fieldName;
   private int variableNumber;

   protected ExprVARIABLE(int var1, String var2, String var3) {
      super(var1, var2);
      this.ejbqlId = var2;
      this.debugClassName = "ExprVARIABLE -  ?" + var2;
      this.fieldName = var3;
   }

   public void init_method() throws ErrorCollectionException {
      this.variableNumber = Integer.parseInt(this.getSval());
      if (this.variableNumber < 1) {
         Loggable var1 = EJBLogger.logFinderParamsMustBeGTOneLoggable(this.globalContext.getFinderMethodName(), Integer.toString(this.variableNumber));
         IllegalExpressionException var2 = new IllegalExpressionException(7, var1.getMessage());
         this.markExcAndThrowCollectionException(var2);
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      this.clearSQLBuf();
      if (this.globalContext.isKeyFinder() && this.variableNumber > 1) {
         this.appendSQLBuf("? ");
      } else {
         Class var1 = this.globalContext.getFinderParameterTypeAt(this.variableNumber - 1);
         if (var1 == null) {
            Loggable var2 = EJBLogger.logFinderParamMissingLoggable(this.globalContext.getFinderMethodName(), Integer.toString(this.variableNumber));
            IllegalExpressionException var3 = new IllegalExpressionException(7, var2.getMessage());
            this.markExcAndThrowCollectionException(var3);
         }

         String var4 = "param" + (this.variableNumber - 1);
         ParamNode var5 = new ParamNode(this.globalContext.getRDBMSBean(), var4, this.variableNumber, var1, "", "", false, false, (Class)null, false, this.globalContext.isOracleNLSDataType(this.fieldName));
         this.globalContext.addFinderInternalQueryParmList(var5);
         this.appendSQLBuf("? ");
      }
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      this.appendMainEJBQLTokenToList(var1);
      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() {
      return this.getSQLBuf().toString();
   }

   public int getVariableNumber() {
      return this.variableNumber;
   }
}
