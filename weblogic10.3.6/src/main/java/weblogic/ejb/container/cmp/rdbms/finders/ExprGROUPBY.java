package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import weblogic.ejb.container.EJBLogger;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public class ExprGROUPBY extends BaseExpr implements Expr, ExpressionTypes {
   private boolean isResultSetFinder = false;
   private boolean hasIntegerArgument = false;
   private List selectList = null;
   private StringBuffer where_clause_buffer;

   protected ExprGROUPBY(int var1, ExprHAVING var2, Vector var3) {
      super(var1, var2, (Vector)var3);
      this.debugClassName = "ExprGROUPBY ";
   }

   public void init_method() throws ErrorCollectionException {
      this.selectList = this.queryTree.getSelectList();
      Iterator var1 = this.selectList.iterator();

      while(var1.hasNext()) {
         SelectNode var2 = (SelectNode)var1.next();
         if (var2.getSelectType() == 61) {
            Loggable var3 = EJBLogger.logejbqlClauseNotAllowedInResultSetQueriesReturningBeansLoggable("GROUP_BY");
            this.markExcAndThrowCollectionException(new Exception(var3.getMessage()));
         }
      }

      this.isResultSetFinder = this.globalContext.isResultSetFinder();
      Vector var9 = this.terms;
      var1 = var9.iterator();

      while(var1.hasNext()) {
         Expr var10 = (Expr)var1.next();

         try {
            var10.init(this.globalContext, this.queryTree);
         } catch (Exception var8) {
            var10.markExcAndAddCollectionException(var8);
            this.addCollectionException(var8);
            continue;
         }

         if (var10.type() == 70 || var10.type() == 71) {
            var10 = var10.getTerm1();
         }

         if (var10.type() == 19) {
            int var4 = (int)var10.getIval();
            String var5 = Integer.toString(var4);
            Loggable var6;
            if (!this.isResultSetFinder) {
               var6 = EJBLogger.lognonResultSetFinderHasIntegerOrderByOrGroupByArgLoggable("GROUPBY", var5);
               this.markExcAndAddCollectionException(new IllegalExpressionException(7, var6.getMessage()));
            }

            if (this.selectList.size() < var4) {
               var6 = EJBLogger.logintegerOrderByOrGroupByArgExceedsSelectListSizeLoggable("GROUPBY", var5, this.selectList.size());
               this.markExcAndAddCollectionException(new IllegalExpressionException(7, var6.getMessage()));
            }
         } else {
            Loggable var11;
            if (var10.type() == 17) {
               if (!((ExprID)var10).isPathExpressionEndingInCmpFieldWithNoSQLGen()) {
                  var11 = EJBLogger.logejbqlArgNotACmpFieldLoggable("GROUP_BY", var10.getSval());
                  this.markExcAndAddCollectionException(new IllegalExpressionException(7, var11.getMessage()));
               }
            } else {
               var11 = EJBLogger.logejbqlArgMustBeIDorINTLoggable("GROUP BY", var10.getTypeName());
               this.markExcAndAddCollectionException(new IllegalExpressionException(7, var11.getMessage()));
            }
         }
      }

      if (this.term1 != null) {
         try {
            this.term1.init(this.globalContext, this.queryTree);
         } catch (ErrorCollectionException var7) {
            this.addCollectionException(var7);
         }
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      this.where_clause_buffer = new StringBuffer();
      Vector var1 = this.terms;
      if (var1.size() >= 1) {
         this.where_clause_buffer.append(" GROUP BY ");
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Expr var3 = (Expr)var2.next();

            try {
               var3.calculate();
               this.where_clause_buffer.append(var3.toSQL());
            } catch (ErrorCollectionException var6) {
               if (var3.type() == 70 || var3.type() == 71) {
                  var3 = var3.getTerm1();
               }

               var3.markExcAndAddCollectionException(var6);
               this.addCollectionException(var6);
            }

            if (var2.hasNext()) {
               this.where_clause_buffer.append(", ");
            }
         }

         if (this.term1 != null) {
            try {
               this.term1.calculate();
               this.where_clause_buffer.append(this.term1.toSQL()).append(" ");
            } catch (ErrorCollectionException var5) {
               this.addCollectionException(var5);
            }
         }

         this.throwCollectionException();
         if (this.queryTree.getQueryId() == 0) {
            this.globalContext.setGroupbySql(this.where_clause_buffer.toString());
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

      if (this.term1 != null) {
         this.term1.appendEJBQLTokens(var1);
      }

      this.appendPostEJBQLTokensToList(var1);
   }

   public String toSQL() throws ErrorCollectionException {
      return this.queryTree.getQueryId() == 0 ? "" : this.where_clause_buffer.toString();
   }
}
