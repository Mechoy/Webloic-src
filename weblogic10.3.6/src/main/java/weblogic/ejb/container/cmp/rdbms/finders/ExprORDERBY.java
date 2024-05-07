package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import weblogic.ejb.container.EJBLogger;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public class ExprORDERBY extends BaseExpr implements Expr, ExpressionTypes {
   private boolean isResultSetFinder = false;
   private boolean hasIntegerArgument = false;
   private List selectList = null;
   private StringBuffer select_clause_buffer;
   private StringBuffer where_clause_buffer;

   protected ExprORDERBY(int var1, Expr var2, Vector var3) {
      super(var1, var2, var3);
      this.debugClassName = "ExprORDERBY ";
   }

   public void init_method() throws ErrorCollectionException {
      this.isResultSetFinder = this.globalContext.isResultSetFinder();
      this.selectList = this.queryTree.getSelectList();
      Enumeration var1 = this.terms.elements();

      while(var1.hasMoreElements()) {
         Expr var2 = (Expr)var1.nextElement();

         try {
            var2.init(this.globalContext, this.queryTree);
         } catch (Exception var7) {
            var2.markExcAndAddCollectionException(var7);
            this.addCollectionException(var7);
         }

         if (var2.getTerm1().type() == 70 || var2.getTerm1().type() == 71) {
            var2 = var2.getTerm1();
         }

         if (var2.getTerm1().type() == 19) {
            this.hasIntegerArgument = true;
            if (!this.isResultSetFinder) {
               int var3 = (int)var2.getTerm1().getIval();
               String var4 = Integer.toString(var3);
               Loggable var5 = EJBLogger.lognonResultSetFinderHasIntegerOrderByOrGroupByArgLoggable("ORDERBY", var4);
               IllegalExpressionException var6 = new IllegalExpressionException(7, var5.getMessage());
               var2.getTerm1().markExcAndThrowCollectionException(var6);
            }
         }
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      this.where_clause_buffer = new StringBuffer();
      if (!this.isResultSetFinder) {
         this.select_clause_buffer = new StringBuffer();
      }

      Enumeration var1 = this.terms.elements();

      while(true) {
         Loggable var11;
         while(var1.hasMoreElements()) {
            Expr var2 = (Expr)var1.nextElement();
            Expr var3 = var2;
            if (var2.getTerm1().type() == 70 || var2.getTerm1().type() == 71) {
               var3 = var2.getTerm1();
            }

            if (var3.getTerm1().type() == 19) {
               int var4 = (int)var3.getTerm1().getIval();
               String var5 = Integer.toString(var4);
               if (this.selectList.size() < var4) {
                  Loggable var6 = EJBLogger.logintegerOrderByOrGroupByArgExceedsSelectListSizeLoggable("ORDERBY", var5, this.selectList.size());
                  IllegalExpressionException var7 = new IllegalExpressionException(7, var6.getMessage());
                  var3.getTerm1().markExcAndAddCollectionException(var7);
                  this.addCollectionException(var7);
                  continue;
               }

               this.where_clause_buffer.append(" ").append(var2.getTerm1().toSQL());
               this.hasIntegerArgument = true;
            } else {
               if (!(var3.getTerm1() instanceof ExprID)) {
                  throw new AssertionError("unexpected codepath");
               }

               IllegalExpressionException var12;
               if (!((ExprID)var3.getTerm1()).isPathExpressionEndingInCmpFieldWithNoSQLGen()) {
                  var11 = EJBLogger.logejbqlArgNotACmpFieldLoggable("ORDERBY", var3.getTerm1().getSval());
                  var12 = new IllegalExpressionException(7, var11.getMessage());
                  var3.getTerm1().markExcAndAddCollectionException(var12);
                  this.addCollectionException(var12);
                  continue;
               }

               if (((ExprID)var3.getTerm1()).isPathExpressionEndingInBlobClobColumnWithSQLGen()) {
                  var11 = EJBLogger.logcannotSpecifyBlobClobInOrderbyLoggable(((ExprID)var3.getTerm1()).getEjbqlID());
                  var12 = new IllegalExpressionException(7, var11.getMessage());
                  var3.getTerm1().markExcAndAddCollectionException(var12);
                  this.addCollectionException(var12);
                  continue;
               }

               String var10 = var2.getTerm1().toSQL();
               this.where_clause_buffer.append(var10);
               if (!this.isResultSetFinder) {
                  this.select_clause_buffer.append(var10);
               }
            }

            if (var2.getTerm2() != null) {
               if (var2.getTerm2().type() == 67) {
                  this.where_clause_buffer.append(" DESC ");
               } else if (var2.getTerm2().type() == 66) {
                  this.where_clause_buffer.append(" ASC ");
               }
            }

            if (var1.hasMoreElements()) {
               this.where_clause_buffer.append(", ");
               if (!this.isResultSetFinder) {
                  this.select_clause_buffer.append(", ");
               }
            }
         }

         if (this.hasIntegerArgument) {
            Iterator var8 = this.selectList.iterator();

            while(var8.hasNext()) {
               SelectNode var9 = (SelectNode)var8.next();
               if (var9.getSelectType() == 61) {
                  var11 = EJBLogger.logejbqlClauseNotAllowedInResultSetQueriesReturningBeansLoggable("ORDERBY");
                  this.markExcAndThrowCollectionException(new IllegalExpressionException(7, var11.getMessage()));
               }
            }
         }

         this.where_clause_buffer.append(" ");
         if (!this.isResultSetFinder) {
            this.select_clause_buffer.append(" ");
         }

         if (!this.isResultSetFinder) {
            this.globalContext.setOrderbyColBuf(this.select_clause_buffer.toString());
         }

         this.globalContext.setOrderbySql(" ORDER BY " + this.where_clause_buffer.toString() + " ");
         return;
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

   public String toSQL() throws ErrorCollectionException {
      return "";
   }
}
