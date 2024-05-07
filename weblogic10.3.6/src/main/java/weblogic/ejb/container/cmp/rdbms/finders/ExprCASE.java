package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.ejb.container.EJBTextTextFormatter;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.utils.ErrorCollectionException;

public final class ExprCASE extends BaseExpr implements Expr, ExpressionTypes, SingleExprIDHolder {
   protected ExprCASE(int var1, Expr var2) {
      super(var1, var2);
      this.debugClassName = "ExprSTRING_FUNCTION - " + getTypeName(var1);
   }

   public void init_method() throws ErrorCollectionException {
      requireTerm(this, 1);
      if (this.term1.type() != 19) {
         try {
            verifyStringExpressionTerm(this, 1);
         } catch (ErrorCollectionException var3) {
            this.addCollectionException(var3);
         }
      }

      try {
         this.term1.init(this.globalContext, this.queryTree);
      } catch (ErrorCollectionException var2) {
         this.addCollectionException(var2);
      }

      switch (this.term1.type()) {
         case 17:
            this.validateID((ExprID)this.term1);
         case 18:
         case 19:
            break;
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         default:
            throw new AssertionError("Unexpected type for Upper or Lower target: " + ExpressionTypes.TYPE_NAMES[this.term1.type()]);
         case 25:
            this.validateVARIABLE((ExprVARIABLE)this.term1);
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      this.term1.calculate();
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      this.appendMainEJBQLTokenToList(var1);
      this.appendNewEJBQLTokenToList("( ", var1);
      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      this.appendNewEJBQLTokenToList(") ", var1);
      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      StringBuffer var1 = new StringBuffer();
      switch (this.type()) {
         case 70:
            var1.append("UPPER( ");
            break;
         case 71:
            var1.append("LOWER( ");
            break;
         default:
            Exception var2 = new Exception("Internal Error in " + this.debugClassName + ", unknown function type " + this.type());
            this.markExcAndThrowCollectionException(var2);
      }

      var1.append(this.term1.toSQL());
      if (this.globalContext.getDatabaseType() == 4 && this.term1.type() == 25) {
         var1.append("||''");
      }

      var1.append(" ) ");
      return var1.toString();
   }

   public ExprID getExprID() {
      try {
         return (ExprID)this.term1;
      } catch (ClassCastException var2) {
         throw new AssertionError("InternalError: getExprID() can only be called when the argument is a path expression");
      }
   }

   private void validateVARIABLE(ExprVARIABLE var1) throws ErrorCollectionException {
      int var2 = var1.getVariableNumber();
      Class var3 = this.globalContext.getFinderParameterTypeAt(var2 - 1);
      if (var3 != String.class && var3 != Character.class && var3 != Character.TYPE) {
         EJBTextTextFormatter var4 = new EJBTextTextFormatter();
         IllegalExpressionException var5 = new IllegalExpressionException(7, var4.caseOperatorUsedOnNonStringField(this.globalContext.getEjbName(), "?" + var2));
         var1.markExcAndThrowCollectionException(var5);
      }

   }

   private void validateID(ExprID var1) throws ErrorCollectionException {
      String var2 = var1.getEjbqlID();
      if (var1.isPathExpressionEndingInCmpFieldWithNoSQLGen()) {
         JoinNode var3 = var1.getJoinNodeForLastCmrFieldWithSQLGen();
         RDBMSBean var4 = var3.getRDBMSBean();
         String var5 = var1.getLastField();
         CMPBeanDescriptor var6 = var4.getCMPBeanDescriptor();
         Class var7 = var6.getFieldClass(var5);
         if (var7 != String.class && var7 != Character.class && var7 != Character.TYPE) {
            EJBTextTextFormatter var8 = new EJBTextTextFormatter();
            IllegalExpressionException var9 = new IllegalExpressionException(7, var8.caseOperatorUsedOnNonStringField(this.globalContext.getEjbName(), var2));
            var1.markExcAndThrowCollectionException(var9);
         }
      } else {
         EJBTextTextFormatter var10 = new EJBTextTextFormatter();
         IllegalExpressionException var11 = new IllegalExpressionException(7, var10.caseOperatorUsedOnNonStringField(this.globalContext.getEjbName(), var2));
         var1.markExcAndThrowCollectionException(var11);
      }

   }
}
