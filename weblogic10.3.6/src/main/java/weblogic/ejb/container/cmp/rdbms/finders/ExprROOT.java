package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb20.cmp.rdbms.finders.EJBQLToken;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public final class ExprROOT extends BaseExpr implements Expr, ExpressionTypes {
   private ExprSELECT exprSELECT;
   private ExprFROM exprFROM;
   private ExprWHERE exprWHERE;
   private ExprORDERBY exprORDERBY;
   private ExprGROUPBY exprGROUPBY;
   private ExprSELECT_HINT exprSELECT_HINT;
   private List ejbqlTokenList = null;

   protected ExprROOT(int var1, Expr var2) {
      super(var1, var2);
      this.debugClassName = "ExprRoot";
   }

   protected ExprROOT(int var1, Expr var2, Expr var3) {
      super(var1, var2, var3);
      this.debugClassName = "ExprRoot";
   }

   protected ExprROOT(int var1, Expr var2, Expr var3, Expr var4) {
      super(var1, var2, var3, var4);
      this.debugClassName = "ExprRoot";
   }

   protected ExprROOT(int var1, Expr var2, Expr var3, Expr var4, Expr var5) {
      super(var1, var2, var3, var4, var5);
      this.debugClassName = "ExprRoot";
   }

   protected ExprROOT(int var1, Expr var2, Expr var3, Expr var4, Expr var5, Expr var6) {
      super(var1, var2, var3, var4, var5, var6);
      this.debugClassName = "ExprRoot";
   }

   public void init_method() throws ErrorCollectionException {
      this.setExprVars();
      this.exprFROM.init(this.globalContext, this.queryTree);

      try {
         this.exprSELECT.init(this.globalContext, this.queryTree);
      } catch (ErrorCollectionException var6) {
         this.addCollectionException(var6);
      }

      try {
         if (this.exprWHERE != null) {
            this.exprWHERE.init(this.globalContext, this.queryTree);
         }
      } catch (ErrorCollectionException var5) {
         this.addCollectionException(var5);
      }

      try {
         if (this.exprORDERBY != null) {
            this.exprORDERBY.init(this.globalContext, this.queryTree);
         }
      } catch (ErrorCollectionException var4) {
         this.addCollectionException(var4);
      }

      try {
         if (this.exprGROUPBY != null) {
            this.exprGROUPBY.init(this.globalContext, this.queryTree);
         }
      } catch (ErrorCollectionException var3) {
         this.addCollectionException(var3);
      }

      try {
         if (this.exprSELECT_HINT != null) {
            this.exprSELECT_HINT.init(this.globalContext, this.queryTree);
         }
      } catch (ErrorCollectionException var2) {
         this.addCollectionException(var2);
      }

   }

   public void calculate_method() throws ErrorCollectionException {
      try {
         this.exprSELECT.calculate();
      } catch (ErrorCollectionException var6) {
         this.addCollectionException(var6);
      }

      this.exprFROM.calculate();

      try {
         if (this.exprWHERE != null) {
            this.exprWHERE.calculate();
         }
      } catch (ErrorCollectionException var5) {
         this.addCollectionException(var5);
      }

      try {
         if (this.exprORDERBY != null) {
            this.exprORDERBY.calculate();
         }
      } catch (ErrorCollectionException var4) {
         this.addCollectionException(var4);
      }

      try {
         if (this.exprGROUPBY != null) {
            this.exprGROUPBY.calculate();
         }
      } catch (ErrorCollectionException var3) {
         this.addCollectionException(var3);
      }

      try {
         if (this.exprSELECT_HINT != null) {
            this.exprSELECT_HINT.calculate();
         }
      } catch (ErrorCollectionException var2) {
         this.addCollectionException(var2);
      }

   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public void appendEJBQLTokens(List var1) {
      this.resetTermNumber();

      while(this.hasMoreTerms()) {
         Expr var2 = this.getNextTerm();
         var2.appendEJBQLTokens(var1);
      }

   }

   public List getEJBQLTokenList() {
      this.ejbqlTokenList = new ArrayList();
      this.appendEJBQLTokens(this.ejbqlTokenList);
      return this.ejbqlTokenList;
   }

   protected String getEJBQLText() {
      List var1 = this.getEJBQLTokenList();
      StringBuffer var2 = new StringBuffer();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         EJBQLToken var4 = (EJBQLToken)var3.next();
         var2.append(var4.getTokenText());
      }

      return var2.toString();
   }

   public String toSQL(Properties var1) {
      return "";
   }

   private void setExprVars() throws ErrorCollectionException {
      int var1 = this.numTerms();
      int var2 = 0;
      Loggable var3;
      if (var1 > 0) {
         if (this.term1.type() != 34) {
            var3 = EJBLogger.logSelectClauseRequiredLoggable();
            this.term1.markExcAndThrowCollectionException(new IllegalExpressionException(7, var3.getMessage()));
         }

         this.exprSELECT = (ExprSELECT)this.term1;
         ++var2;
         if (var1 < 2) {
            var3 = EJBLogger.logFromClauseRequiredLoggable();
            this.term1.markExcAndThrowCollectionException(new IllegalExpressionException(7, var3.getMessage()));
         }
      }

      if (this.term2.type() != 27) {
         var3 = EJBLogger.logFromClauseRequiredLoggable();
         this.term2.markExcAndThrowCollectionException(new IllegalExpressionException(7, var3.getMessage()));
      }

      this.exprFROM = (ExprFROM)this.term2;
      ++var2;
      this.setNextTerm(3);

      while(this.hasMoreTerms()) {
         Expr var4 = this.getNextTerm();
         switch (var4.type()) {
            case 26:
               this.exprWHERE = (ExprWHERE)var4;
               break;
            case 36:
               this.exprORDERBY = (ExprORDERBY)var4;
               break;
            case 60:
               this.exprSELECT_HINT = (ExprSELECT_HINT)var4;
               break;
            case 68:
               this.exprGROUPBY = (ExprGROUPBY)var4;
               break;
            default:
               this.markExcAndThrowCollectionException(new IllegalExpressionException(7, " unknown expr type: '" + var4.type() + "' '" + BaseExpr.getTypeName(var4.type()) + "' "));
         }
      }

   }

   public String getWhereSql() throws ErrorCollectionException {
      return this.exprWHERE == null ? "" : this.exprWHERE.toSQL();
   }

   ExprWHERE getExprWHERE() {
      return this.exprWHERE;
   }

   List getWHEREList() {
      ArrayList var1 = new ArrayList();
      ExprWHERE var2 = this.getWHEREchildFromParent(this);
      if (var2 == null) {
         return var1;
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("\n\nadding main WHERE clause to whereExprList");
         }

         var1.add(var2);
         this.addWHEREfromSUBQUERYs(var1, var2);
         return var1;
      }
   }

   private void addWHEREfromSUBQUERYs(List var1, Expr var2) {
      if (var2 instanceof ExprSUBQUERY) {
         if (debugLogger.isDebugEnabled()) {
            debug("addWHEREfromSUBQUERYs, found SUBQUERY");
         }

         ExprWHERE var3 = this.getWHEREchildFromParent(var2);
         if (var3 == null) {
            if (debugLogger.isDebugEnabled()) {
               debug("addWHEREfromSUBQUERYs   SUBQUERY has no WHERE clause terminate searching.");
            }

            return;
         }

         if (debugLogger.isDebugEnabled()) {
            debug("addWHEREfromSUBQUERYs  found SUBQUERY WHERE adding.");
         }

         var1.add(var3);
         this.addWHEREfromSUBQUERYs(var1, var3);
      } else {
         if (var2.numTerms() == 0) {
            if (debugLogger.isDebugEnabled()) {
               debug("addWHEREfromSUBQUERYs,  term NOT SUBQUERY, no subterms, exiting.");
            }

            return;
         }

         var2.resetTermNumber();

         while(var2.hasMoreTerms()) {
            Expr var6 = var2.getNextTerm();
            if (var6 == null) {
               break;
            }

            this.addWHEREfromSUBQUERYs(var1, var6);
         }

         if (var2.termVectSize() > 0) {
            Vector var7 = var2.getTermVector();
            Enumeration var4 = var7.elements();

            while(var4.hasMoreElements()) {
               Expr var5 = (Expr)var4.nextElement();
               if (var5 == null) {
                  break;
               }

               this.addWHEREfromSUBQUERYs(var1, var5);
            }
         }
      }

   }

   private ExprWHERE getWHEREchildFromParent(Expr var1) {
      var1.resetTermNumber();

      Expr var2;
      do {
         if (!var1.hasMoreTerms()) {
            return null;
         }

         var2 = var1.getNextTerm();
      } while(!(var2 instanceof ExprWHERE));

      return (ExprWHERE)var2;
   }

   private static void debug(String var0) {
      debugLogger.debug("[ExprROOT] " + var0);
   }
}
