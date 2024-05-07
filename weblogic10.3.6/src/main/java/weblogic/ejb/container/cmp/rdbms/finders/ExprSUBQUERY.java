package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public final class ExprSUBQUERY extends BaseExpr implements Expr, ExpressionTypes {
   private int subqueryNumber = 0;
   private int queryTermCount = 0;
   private boolean hasQualifier = false;
   private boolean hasExistsQualifier = false;
   private String subqueryQualifier = null;
   private QueryNode subqueryTree = null;
   private ExprSIMPLE_QUALIFIER exprSIMPLE_QUALIFIER;
   private ExprEXISTS exprEXISTS;
   private ExprSELECT exprSELECT;
   private ExprFROM exprFROM;
   private ExprWHERE exprWHERE;
   private ExprGROUPBY exprGROUPBY;

   protected ExprSUBQUERY(int var1, ExprINTEGER var2, Expr var3) {
      super(var1, var2, (Expr)var3);
      this.queryTermCount = 1;
      this.debugClassName = "ExprSUBQUERY";
   }

   protected ExprSUBQUERY(int var1, ExprINTEGER var2, Expr var3, Expr var4) {
      super(var1, var2, var3, var4);
      this.queryTermCount = 2;
      this.debugClassName = "ExprSUBQUERY";
   }

   protected ExprSUBQUERY(int var1, ExprINTEGER var2, Expr var3, Expr var4, Expr var5) {
      super(var1, var2, var3, var4, var5);
      this.queryTermCount = 3;
      this.debugClassName = "ExprSUBQUERY";
   }

   protected ExprSUBQUERY(int var1, ExprINTEGER var2, Expr var3, Expr var4, Expr var5, Expr var6) {
      super(var1, var2, var3, var4, var5, var6);
      this.queryTermCount = 4;
      this.debugClassName = "ExprSUBQUERY";
   }

   protected ExprSUBQUERY(int var1, ExprINTEGER var2, Expr var3, Expr var4, Expr var5, Expr var6, Expr var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.queryTermCount = 5;
      this.debugClassName = "ExprSUBQUERY";
   }

   public void init_method() throws ErrorCollectionException {
      try {
         this.setExprVars();
      } catch (ErrorCollectionException var5) {
         this.addCollectionExceptionAndThrow(var5);
      }

      this.subqueryTree = this.globalContext.newQueryNode(this.queryTree, this.subqueryNumber);
      this.exprFROM.init(this.globalContext, this.subqueryTree);

      try {
         this.exprSELECT.init(this.globalContext, this.subqueryTree);
      } catch (ErrorCollectionException var4) {
         this.addCollectionException(var4);
      }

      if (this.exprWHERE != null) {
         try {
            this.exprWHERE.init(this.globalContext, this.subqueryTree);
         } catch (ErrorCollectionException var3) {
            this.addCollectionException(var3);
         }
      }

      try {
         if (this.exprGROUPBY != null) {
            this.exprGROUPBY.init(this.globalContext, this.subqueryTree);
         }
      } catch (ErrorCollectionException var2) {
         this.addCollectionException(var2);
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      try {
         this.exprSELECT.calculate();
      } catch (ErrorCollectionException var5) {
         this.addCollectionException(var5);
      }

      try {
         this.exprFROM.calculate();
      } catch (ErrorCollectionException var4) {
         this.addCollectionException(var4);
      }

      if (this.exprWHERE != null) {
         try {
            this.exprWHERE.calculate();
         } catch (ErrorCollectionException var3) {
            this.addCollectionException(var3);
         }
      }

      try {
         if (this.exprGROUPBY != null) {
            this.exprGROUPBY.calculate();
         }
      } catch (ErrorCollectionException var2) {
         this.addCollectionException(var2);
      }

   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      if (this.term2 != null) {
         this.term2.appendEJBQLTokens(var1);
      }

      if (this.term3 != null) {
         this.term3.appendEJBQLTokens(var1);
      }

      if (this.term4 != null) {
         this.term4.appendEJBQLTokens(var1);
      }

      if (this.term5 != null) {
         this.term5.appendEJBQLTokens(var1);
      }

      if (this.term6 != null) {
         this.term6.appendEJBQLTokens(var1);
      }

      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      StringBuffer var1 = new StringBuffer();
      if (this.hasQualifier) {
         var1.append(this.subqueryQualifier).append(" ");
      }

      if (this.hasExistsQualifier) {
         var1.append(this.exprEXISTS.toSQL());
      }

      try {
         var1.append("( ");
         this.toSQLselect(var1);
         this.toSQLfrom(var1);
         this.toSQLwhere(var1);
         this.toSQLgroupby(var1);
         var1.append(") ");
      } catch (ErrorCollectionException var3) {
         this.addCollectionExceptionAndThrow(var3);
      }

      return var1.toString();
   }

   private void setExprVars() throws ErrorCollectionException {
      int var1 = this.numTerms();
      this.resetTermNumber();
      Expr var2 = this.getNextTerm();
      String var3;
      Loggable var4;
      IllegalExpressionException var5;
      if (var2.type() != 19) {
         var3 = "SUBQUERY INTEGER";
         var4 = EJBLogger.logejbqlSubQueryMissingClauseLoggable(var3);
         var5 = new IllegalExpressionException(7, var4.getMessage());
         var2.markExcAndThrowCollectionException(var5);
      }

      this.subqueryNumber = (int)var2.getIval();
      var2 = this.getNextTerm();
      if (var2.type() == 65) {
         this.hasExistsQualifier = true;
         this.exprEXISTS = (ExprEXISTS)var2;
         this.subqueryQualifier = " <ExprSUBQUERY: NOTSET> ";
         this.exprEXISTS = (ExprEXISTS)var2;
         var2 = this.getNextTerm();
      } else if (var2.type() == 49) {
         this.hasQualifier = true;
         this.subqueryQualifier = "ALL";
         this.exprSIMPLE_QUALIFIER = (ExprSIMPLE_QUALIFIER)var2;
         var2 = this.getNextTerm();
      } else if (var2.type() == 64) {
         this.hasQualifier = true;
         this.subqueryQualifier = "ANY";
         this.exprSIMPLE_QUALIFIER = (ExprSIMPLE_QUALIFIER)var2;
         var2 = this.getNextTerm();
      }

      if (var2.type() != 34) {
         var3 = "SELECT";
         var4 = EJBLogger.logejbqlSubQueryMissingClauseLoggable(var3);
         var5 = new IllegalExpressionException(7, var4.getMessage());
         var2.markExcAndThrowCollectionException(var5);
      }

      this.exprSELECT = (ExprSELECT)var2;
      var2 = this.getNextTerm();
      if (var2.type() != 27) {
         var3 = "FROM";
         var4 = EJBLogger.logejbqlSubQueryMissingClauseLoggable(var3);
         var5 = new IllegalExpressionException(7, var4.getMessage());
         var2.markExcAndThrowCollectionException(var5);
      }

      this.exprFROM = (ExprFROM)var2;
      if (var1 > this.getCurrTermNumber()) {
         var2 = this.getNextTerm();
         this.exprWHERE = (ExprWHERE)var2;
      }

      if (var1 > this.getCurrTermNumber()) {
         var2 = this.getNextTerm();
         this.exprGROUPBY = (ExprGROUPBY)var2;
      }

   }

   private void toSQLselect(StringBuffer var1) throws ErrorCollectionException {
      var1.append("SELECT ");
      List var2 = this.subqueryTree.getSelectList();
      IllegalExpressionException var10;
      if (var2.size() > 1) {
         Loggable var9 = EJBLogger.logejbqlSubQuerySelectCanOnlyHaveOneItemLoggable();
         var10 = new IllegalExpressionException(7, var9.getMessage());
         throw new ErrorCollectionException(var10);
      } else {
         SelectNode var3 = (SelectNode)var2.get(0);
         if (var3.getIsAggregate()) {
            var1.append(var3.getSelectTypeName());
            var1.append("(");
            if (var3.getIsAggregateDistinct()) {
               var1.append("DISTINCT ");
            }

            var1.append(var3.getDbmsTarget()).append(" ");
            var1.append(") ");
         } else if (var3.getSelectType() == 17) {
            var1.append(var3.getDbmsTarget()).append(" ");
         } else {
            if (var3.getSelectType() != 61) {
               var10 = new IllegalExpressionException(7, " SELECT clause of a SubQuery has a SELECT target type: " + ExpressionTypes.TYPE_NAMES[var3.getSelectType()] + ".  This type is not supported as a SubQuery SELECT target. ");
               throw new ErrorCollectionException(var10);
            }

            String var4 = var3.getDbmsTarget();
            RDBMSBean var5 = var3.getSelectBean();
            List var6 = var5.getPrimaryKeyFields();
            String var7 = (String)var6.get(0);
            String var8 = var5.getCmpColumnForField(var7);
            var1.append(" ").append(var4).append(".").append(var8).append(" ");
         }

      }
   }

   private void toSQLfrom(StringBuffer var1) throws ErrorCollectionException {
      var1.append("FROM ");

      try {
         var1.append(this.subqueryTree.getFROMDeclaration(0));
      } catch (Exception var3) {
         throw new ErrorCollectionException(var3);
      }
   }

   private void toSQLwhere(StringBuffer var1) throws ErrorCollectionException {
      String var2;
      if (this.exprWHERE == null) {
         try {
            var2 = this.subqueryTree.getMainORJoinBuffer();
            if (var2.length() > 0) {
               var1.append("WHERE ");
               var1.append(var2);
            }

         } catch (Exception var3) {
            throw new ErrorCollectionException(var3);
         }
      } else {
         try {
            var1.append(this.exprWHERE.toSQL());
            var2 = this.subqueryTree.getMainORJoinBuffer();
            if (var2.length() > 0) {
               var1.append("AND ").append(var2);
            }

         } catch (Exception var4) {
            throw new ErrorCollectionException(var4);
         }
      }
   }

   ExprWHERE getExprWHERE() {
      return this.exprWHERE;
   }

   private void toSQLgroupby(StringBuffer var1) throws ErrorCollectionException {
      try {
         if (this.exprGROUPBY != null) {
            var1.append(this.exprGROUPBY.toSQL());
         }

      } catch (Exception var3) {
         throw new ErrorCollectionException(var3);
      }
   }
}
