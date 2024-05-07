package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.ejb20.cmp.rdbms.finders.EJBQLToken;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public abstract class BaseExpr implements Expr, ExpressionTypes {
   protected static final DebugLogger debugLogger;
   protected boolean encounteredException = false;
   protected ErrorCollectionException collectionException = null;
   private EJBQLToken mainEJBQLToken = null;
   private EJBQLToken preEJBQLToken = null;
   private EJBQLToken postEJBQLToken = null;
   protected QueryContext globalContext;
   protected QueryNode queryTree;
   protected int type;
   protected Expr term1 = null;
   protected Expr term2 = null;
   protected Expr term3 = null;
   protected Expr term4 = null;
   protected Expr term5 = null;
   protected Expr term6 = null;
   protected Vector terms = null;
   protected long ival;
   protected double fval;
   protected String sval;
   protected int termCount;
   protected int termVectorSize = 0;
   private int nextTerm = 1;
   private StringBuffer sqlStringBuffer;
   public String debugClassName = "BaseExpr";

   public BaseExpr(int var1) {
      if (debugLogger.isDebugEnabled()) {
         debug(" Expr Constructor called for: '" + TYPE_NAMES[var1] + "'");
      }

      this.type = var1;
      this.termCount = 0;
   }

   protected BaseExpr(int var1, Expr var2) {
      if (debugLogger.isDebugEnabled()) {
         debug(" Expr Constructor called for: '" + TYPE_NAMES[var1] + "'");
      }

      this.type = var1;
      this.termCount = 0;
      this.term1 = var2;
      if (var2 != null) {
         ++this.termCount;
      }

   }

   protected BaseExpr(int var1, Expr var2, Expr var3) {
      if (debugLogger.isDebugEnabled()) {
         debug(" Expr Constructor called for: '" + TYPE_NAMES[var1] + "'");
      }

      this.type = var1;
      this.termCount = 0;
      this.term1 = var2;
      if (var2 != null) {
         ++this.termCount;
      }

      this.term2 = var3;
      if (var3 != null) {
         ++this.termCount;
      }

   }

   protected BaseExpr(int var1, Expr var2, Expr var3, Expr var4) {
      if (debugLogger.isDebugEnabled()) {
         debug(" Expr Constructor called for: '" + TYPE_NAMES[var1] + "'");
      }

      this.type = var1;
      this.termCount = 0;
      this.term1 = var2;
      if (var2 != null) {
         ++this.termCount;
      }

      this.term2 = var3;
      if (var3 != null) {
         ++this.termCount;
      }

      this.term3 = var4;
      if (var4 != null) {
         ++this.termCount;
      }

   }

   protected BaseExpr(int var1, Expr var2, Expr var3, Expr var4, Expr var5) {
      if (debugLogger.isDebugEnabled()) {
         debug(" Expr Constructor called for: '" + TYPE_NAMES[var1] + "'");
      }

      this.type = var1;
      this.termCount = 0;
      this.term1 = var2;
      if (var2 != null) {
         ++this.termCount;
      }

      this.term2 = var3;
      if (var3 != null) {
         ++this.termCount;
      }

      this.term3 = var4;
      if (var4 != null) {
         ++this.termCount;
      }

      this.term4 = var5;
      if (var5 != null) {
         ++this.termCount;
      }

   }

   protected BaseExpr(int var1, Expr var2, Expr var3, Expr var4, Expr var5, Expr var6) {
      if (debugLogger.isDebugEnabled()) {
         debug(" Expr Constructor called for: '" + TYPE_NAMES[var1] + "'");
      }

      this.type = var1;
      this.termCount = 0;
      this.term1 = var2;
      if (var2 != null) {
         ++this.termCount;
      }

      this.term2 = var3;
      if (var3 != null) {
         ++this.termCount;
      }

      this.term3 = var4;
      if (var4 != null) {
         ++this.termCount;
      }

      this.term4 = var5;
      if (var5 != null) {
         ++this.termCount;
      }

      this.term5 = var6;
      if (var6 != null) {
         ++this.termCount;
      }

   }

   protected BaseExpr(int var1, Expr var2, Expr var3, Expr var4, Expr var5, Expr var6, Expr var7) {
      if (debugLogger.isDebugEnabled()) {
         debug(" Expr Constructor called for: '" + TYPE_NAMES[var1] + "'");
      }

      this.type = var1;
      this.termCount = 0;
      this.term1 = var2;
      if (var2 != null) {
         ++this.termCount;
      }

      this.term2 = var3;
      if (var3 != null) {
         ++this.termCount;
      }

      this.term3 = var4;
      if (var4 != null) {
         ++this.termCount;
      }

      this.term4 = var5;
      if (var5 != null) {
         ++this.termCount;
      }

      this.term5 = var6;
      if (var6 != null) {
         ++this.termCount;
      }

      this.term6 = var7;
      if (var7 != null) {
         ++this.termCount;
      }

   }

   protected BaseExpr(int var1, Expr var2, Vector var3) {
      if (debugLogger.isDebugEnabled()) {
         debug(" Expr Constructor called for: '" + TYPE_NAMES[var1] + "'");
      }

      this.type = var1;
      this.term1 = var2;
      if (var2 != null) {
         ++this.termCount;
      }

      this.terms = var3;
      this.termVectorSize = var3.size();
   }

   protected BaseExpr(int var1, String var2) {
      if (debugLogger.isDebugEnabled()) {
         debug(" Expr Constructor called for: '" + TYPE_NAMES[var1] + "'");
      }

      this.type = var1;
      if (var1 == 18) {
         this.sval = var2.substring(1, var2.length() - 1);
      } else {
         this.sval = var2;
      }

      if (var1 == 19) {
         this.sval = var2;
         this.ival = Long.parseLong(var2);
      } else if (var1 == 20) {
         this.fval = Double.valueOf(var2);
         this.sval = var2;
      } else if (var1 == 25) {
         this.sval = var2;
         this.ival = (long)Integer.valueOf(var2);
      } else if (var1 == 53) {
         this.sval = var2;
      }

   }

   public final void init(QueryContext var1, QueryNode var2) throws ErrorCollectionException {
      this.globalContext = var1;
      this.queryTree = var2;
      this.init_method();
      this.throwCollectionException();
   }

   public final void calculate() throws ErrorCollectionException {
      this.calculate_method();
      this.throwCollectionException();
   }

   public abstract void init_method() throws ErrorCollectionException;

   public abstract void calculate_method() throws ErrorCollectionException;

   protected abstract Expr invertForNOT() throws ErrorCollectionException;

   public void addCollectionException(Exception var1) {
      if (this.collectionException == null) {
         this.collectionException = new ErrorCollectionException(var1);
      } else {
         this.collectionException.add(var1);
      }
   }

   public void addCollectionExceptionAndThrow(Exception var1) throws ErrorCollectionException {
      this.addCollectionException(var1);
      throw this.collectionException;
   }

   public void throwCollectionException() throws ErrorCollectionException {
      if (this.collectionException != null) {
         throw this.collectionException;
      }
   }

   public void markExcAndAddCollectionException(Exception var1) {
      this.encounteredException = true;
      this.getMainEJBQLToken().setHadException(true);
      this.addCollectionException(var1);
   }

   public void markExcAndThrowCollectionException(Exception var1) throws ErrorCollectionException {
      this.markExcAndAddCollectionException(var1);
      this.throwCollectionException();
   }

   public String getMainEJBQL() {
      return this.mainEJBQLToken == null ? "" : this.getMainEJBQLToken().getTokenText();
   }

   public void setMainEJBQL(String var1) {
      new EJBQLToken();
      this.appendMainEJBQL(var1);
   }

   private EJBQLToken getMainEJBQLToken() {
      if (this.mainEJBQLToken == null) {
         this.mainEJBQLToken = new EJBQLToken();
      }

      return this.mainEJBQLToken;
   }

   private EJBQLToken getPreEJBQLToken() {
      if (this.preEJBQLToken == null) {
         this.preEJBQLToken = new EJBQLToken();
      }

      return this.preEJBQLToken;
   }

   private EJBQLToken getPostEJBQLToken() {
      if (this.postEJBQLToken == null) {
         this.postEJBQLToken = new EJBQLToken();
      }

      return this.postEJBQLToken;
   }

   public void prependPreEJBQL(String var1) {
      this.getPreEJBQLToken().prependTokenText(var1);
   }

   public void appendMainEJBQL(String var1) {
      if (var1 != null) {
         if (var1.length() > 0) {
            this.getMainEJBQLToken().appendTokenText(var1);
         }
      }
   }

   public void appendMainEJBQL(EJBQLToken var1) {
      if (var1 != null) {
         String var2 = var1.getTokenText();
         if (var2 != null) {
            if (var2.length() > 0) {
               this.getMainEJBQLToken().appendTokenText(var2);
            }
         }
      }
   }

   public void appendPostEJBQL(String var1) {
      this.getPostEJBQLToken().appendTokenText(var1);
   }

   public abstract void appendEJBQLTokens(List var1);

   protected void appendNewEJBQLTokenToList(String var1, List var2) {
      EJBQLToken var3 = new EJBQLToken(var1);
      var2.add(var3);
   }

   protected void appendPreEJBQLTokensToList(List var1) {
      if (this.preEJBQLToken != null) {
         var1.add(this.getPreEJBQLToken());
      }
   }

   protected void appendMainEJBQLTokenToList(List var1) {
      if (this.mainEJBQLToken != null) {
         var1.add(this.getMainEJBQLToken());
      }
   }

   protected void appendPostEJBQLTokensToList(List var1) {
      if (this.postEJBQLToken != null) {
         var1.add(this.getPostEJBQLToken());
      }
   }

   protected void setPreEJBQLFrom(BaseExpr var1) {
      this.preEJBQLToken = null;
      String var2 = var1.getPreEJBQLToken().getTokenText();
      if (var2 != null && var2.length() > 0) {
         this.prependPreEJBQL(var2);
      }

   }

   protected void setPostEJBQLFrom(BaseExpr var1) {
      this.postEJBQLToken = null;
      String var2 = var1.getPostEJBQLToken().getTokenText();
      if (var2 != null && var2.length() > 0) {
         this.appendPostEJBQL(var2);
      }

   }

   public String printEJBQLTree() {
      ArrayList var1 = new ArrayList();
      this.appendEJBQLTokens(var1);
      StringBuffer var2 = new StringBuffer();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         EJBQLToken var4 = (EJBQLToken)var3.next();
         var2.append(var4.getTokenText());
      }

      return var2.toString();
   }

   protected void clearSQLBuf() {
      if (this.sqlStringBuffer != null) {
         this.sqlStringBuffer.setLength(0);
      }

   }

   protected void appendSQLBuf(String var1) {
      if (this.sqlStringBuffer == null) {
         this.sqlStringBuffer = new StringBuffer();
      }

      this.sqlStringBuffer.append(var1);
   }

   public StringBuffer getSQLBuf() {
      if (this.sqlStringBuffer == null) {
         this.sqlStringBuffer = new StringBuffer();
      }

      return this.sqlStringBuffer;
   }

   public String toSQL() throws ErrorCollectionException {
      return "";
   }

   public boolean encounteredException() {
      return this.encounteredException;
   }

   public QueryContext getGlobalContext() {
      return this.globalContext;
   }

   public QueryNode getQueryTree() {
      return this.queryTree;
   }

   public int type() {
      return this.type;
   }

   public Expr getTerm1() {
      return this.term1;
   }

   public Expr getTerm2() {
      return this.term2;
   }

   public Expr getTerm3() {
      return this.term3;
   }

   public Expr getTerm4() {
      return this.term4;
   }

   public Expr getTerm5() {
      return this.term5;
   }

   public Expr getTerm6() {
      return this.term6;
   }

   public int numTerms() {
      return this.termCount;
   }

   public boolean hasMoreTerms() {
      return this.nextTerm <= this.termCount;
   }

   public Expr getNextTerm() {
      return this.getTerm(this.nextTerm++);
   }

   public Expr getTerm(int var1) {
      switch (var1) {
         case 1:
            return this.term1;
         case 2:
            return this.term2;
         case 3:
            return this.term3;
         case 4:
            return this.term4;
         case 5:
            return this.term5;
         case 6:
            return this.term6;
         default:
            return null;
      }
   }

   public int getCurrTermNumber() {
      return this.nextTerm - 1;
   }

   public void setNextTerm(int var1) {
      this.nextTerm = var1;
   }

   public void replaceTermAt(Expr var1, int var2) {
      switch (var2) {
         case 1:
            this.term1 = var1;
            break;
         case 2:
            this.term2 = var1;
            break;
         case 3:
            this.term3 = var1;
            break;
         case 4:
            this.term4 = var1;
            break;
         case 5:
            this.term5 = var1;
            break;
         case 6:
            this.term6 = var1;
      }

   }

   public void resetTermNumber() {
      this.nextTerm = 1;
   }

   public Vector getTermVector() {
      return this.terms;
   }

   public int termVectSize() {
      return this.termVectorSize;
   }

   public String getSval() {
      return this.sval;
   }

   public void setSval(String var1) {
      this.sval = var1;
   }

   public long getIval() {
      return this.ival;
   }

   public double getFval() {
      return this.fval;
   }

   public String getTypeName() {
      return getTypeName(this.type);
   }

   public static void addBlanksForStringLength(StringBuffer var0, String var1) {
      if (var1 != null) {
         int var2 = var1.length();
         if (var2 > 0) {
            var0.append(getBlankString(var2));
         }
      }
   }

   public static String getBlankString(int var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0; ++var2) {
         var1.append(" ");
      }

      return var1.toString();
   }

   public static int numType(String var0) {
      return var0.indexOf(46) == -1 && var0.indexOf(69) == -1 ? 19 : 20;
   }

   public static String getTypeName(int var0) {
      return var0 > TYPE_NAMES.length ? "UNKNOWN TYPE" : TYPE_NAMES[var0];
   }

   public static int finderStringOrId(String var0) {
      if (var0.startsWith("@@")) {
         return 40;
      } else {
         int var1 = var0.indexOf(">>");
         if (var1 == -1) {
            return 17;
         } else {
            return var0.indexOf("find", var1) != -1 ? 35 : 17;
         }
      }
   }

   public static String getEjbNameFromFinderString(String var0) {
      int var1 = var0.indexOf(">>");
      return var1 == -1 ? "" : var0.substring(0, var1);
   }

   public static String getSelectCast(String var0) {
      if (var0.length() < 3) {
         return "";
      } else {
         return 40 != finderStringOrId(var0) ? "" : var0.substring(2);
      }
   }

   public static Expr getExpressionFromTerms(Expr var0, int var1) {
      var0.resetTermNumber();

      Expr var2;
      do {
         if (!var0.hasMoreTerms()) {
            return null;
         }

         var2 = var0.getNextTerm();
      } while(var2.type() != var1);

      return var2;
   }

   public static ExprID getExprIDFromSingleExprIDHolder(Expr var0) throws ErrorCollectionException {
      if (!(var0 instanceof SingleExprIDHolder)) {
         IllegalExpressionException var1 = new IllegalExpressionException(7, " Internal Error:  attempt to execute: SingleExprIDHolder.getExprID() on an Expr of Class: '" + var0.getClass().getName() + "' which does not implement SingleExprIDHolder");
         var0.markExcAndThrowCollectionException(var1);
      }

      return ((SingleExprIDHolder)var0).getExprID();
   }

   public static Expr getAggregateExpressionFromSubQuerySelect(Expr var0) {
      if (var0.type() != 43) {
         return null;
      } else {
         Expr var1 = null;
         var0.resetTermNumber();

         while(var0.hasMoreTerms()) {
            var1 = var0.getNextTerm();
            if (var1.type() == 34) {
               break;
            }

            var1 = null;
         }

         if (var1 == null) {
            return null;
         } else if (var1.getTermVector() == null) {
            return null;
         } else {
            Enumeration var2 = var1.getTermVector().elements();

            do {
               if (!var2.hasMoreElements()) {
                  return null;
               }

               var1 = (Expr)var2.nextElement();
            } while(var1.type() != 44 && var1.type() != 45 && var1.type() != 46 && var1.type() != 47 && var1.type() != 48);

            return var1;
         }
      }
   }

   public static String getComparisonOpStringFromType(int var0) throws ErrorCollectionException {
      switch (var0) {
         case 5:
            return "= ";
         case 6:
            return "< ";
         case 7:
            return "> ";
         case 8:
            return "<= ";
         case 9:
            return ">= ";
         case 10:
            return "<> ";
         default:
            throw new ErrorCollectionException("Internal Error, attempt to perform toSQL using an unknown operand type code: '" + var0 + "', '" + getTypeName(var0) + "'.");
      }
   }

   public String toString() {
      String var1 = this.toVal();
      return TYPE_NAMES[this.type()] + ": " + (var1 == null ? this.term1 + "," + this.term2 : var1);
   }

   private String toVal() {
      switch (this.type()) {
         case 17:
         case 18:
         case 25:
         case 40:
            return this.getSval();
         case 19:
            return this.getIval() + "";
         case 20:
            return this.getFval() + "";
         default:
            return null;
      }
   }

   public void dump() {
      StringBuffer var1 = new StringBuffer();
      dump(this, 0, var1);
      System.out.println(var1.toString());
   }

   public String dumpString() {
      StringBuffer var1 = new StringBuffer();
      dump(this, 0, var1);
      return var1.toString();
   }

   private static void dump(Expr var0, int var1, StringBuffer var2) {
      if (var0.type() != 72) {
         for(int var3 = 0; var3 < var1; ++var3) {
            var2.append("| ");
         }

         var2.append(TYPE_NAMES[var0.type()]);
         switch (var0.type()) {
            case 16:
               var2.append("-- NULL").append("\n");
               break;
            case 17:
            case 25:
            case 35:
            case 40:
            case 53:
            case 61:
            case 62:
               var2.append(" -- " + var0.getSval()).append("\n");
               break;
            case 18:
               var2.append(" -- \"" + var0.getSval() + "\"").append("\n");
               break;
            case 19:
               var2.append(" -- " + var0.getIval()).append("\n");
               break;
            case 20:
               var2.append(" -- " + var0.getFval()).append("\n");
               break;
            default:
               var2.append("\n");
         }

         if (var0.getTerm1() != null) {
            dump(var0.getTerm1(), var1 + 1, var2);
         }

         if (var0.getTerm2() != null) {
            dump(var0.getTerm2(), var1 + 1, var2);
         }

         if (var0.getTerm3() != null) {
            dump(var0.getTerm3(), var1 + 1, var2);
         }

         if (var0.getTerm4() != null) {
            dump(var0.getTerm4(), var1 + 1, var2);
         }

         if (var0.getTerm5() != null) {
            dump(var0.getTerm5(), var1 + 1, var2);
         }

         if (var0.getTerm6() != null) {
            dump(var0.getTerm6(), var1 + 1, var2);
         }

         if (var0.getTermVector() != null) {
            Enumeration var4 = var0.getTermVector().elements();

            while(var4.hasMoreElements()) {
               dump((Expr)var4.nextElement(), var1 + 1, var2);
            }
         }

      }
   }

   public static Expr find(Expr var0, int var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("Checking NODE: " + TYPE_NAMES[var0.type()] + "  for type: " + TYPE_NAMES[var1]);
      }

      if (var0.type() == var1) {
         return var0;
      } else {
         Expr var2 = null;
         if (var0.getTerm1() != null) {
            var2 = find(var0.getTerm1(), var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.getTerm2() != null) {
            var2 = find(var0.getTerm2(), var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.getTerm3() != null) {
            var2 = find(var0.getTerm3(), var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.getTerm4() != null) {
            var2 = find(var0.getTerm4(), var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.getTerm5() != null) {
            var2 = find(var0.getTerm5(), var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.getTerm6() != null) {
            var2 = find(var0.getTerm6(), var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.getTermVector() != null) {
            Enumeration var3 = var0.getTermVector().elements();

            while(var3.hasMoreElements()) {
               var2 = find((Expr)var3.nextElement(), var1);
               if (var2 != null) {
                  return var2;
               }
            }
         }

         return null;
      }
   }

   public static String findIdForVariable(Expr var0, int var1) {
      if (var0.type() == 5 && var0.getTerm2() != null && var0.getTerm2().type() == 25 && var0.getTerm2().getIval() == (long)var1 && var0.getTerm1() != null && var0.getTerm1().type() == 17) {
         if (debugLogger.isDebugEnabled()) {
            debug(" VARIABLE NODE: " + var0.getTerm2().getIval() + ".  returning ID: " + var0.getTerm1().getSval());
         }

         return var0.getTerm1().getSval();
      } else {
         String var2 = null;
         if (var0.getTerm1() != null) {
            var2 = findIdForVariable(var0.getTerm1(), var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.getTerm2() != null) {
            var2 = findIdForVariable(var0.getTerm2(), var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.getTerm3() != null) {
            var2 = findIdForVariable(var0.getTerm3(), var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.getTerm4() != null) {
            var2 = findIdForVariable(var0.getTerm4(), var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.getTerm5() != null) {
            var2 = findIdForVariable(var0.getTerm5(), var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.getTerm6() != null) {
            var2 = findIdForVariable(var0.getTerm6(), var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.getTermVector() != null) {
            Enumeration var3 = var0.getTermVector().elements();

            while(var3.hasMoreElements()) {
               var2 = findIdForVariable((Expr)var3.nextElement(), var1);
               if (var2 != null) {
                  return var2;
               }
            }
         }

         return var2;
      }
   }

   protected static void requireTerm(Expr var0, int var1) throws ErrorCollectionException {
      Expr var2 = null;
      IllegalExpressionException var3;
      switch (var1) {
         case 1:
            var2 = var0.getTerm1();
            break;
         case 2:
            var2 = var0.getTerm2();
            break;
         case 3:
            var2 = var0.getTerm3();
            break;
         default:
            var3 = new IllegalExpressionException(7, ExpressionTypes.TYPE_NAMES[var0.type()] + " error attempt to requireTerm " + "on term number " + var1);
            var0.markExcAndThrowCollectionException(var3);
      }

      if (var2 == null) {
         var3 = new IllegalExpressionException(7, ExpressionTypes.TYPE_NAMES[var0.type()] + " expression has term Number" + var1 + " == null, this is required to be non-null");
         var0.markExcAndThrowCollectionException(var3);
      }

   }

   protected static void verifyStringExpressionTerm(Expr var0, int var1) throws ErrorCollectionException {
      Expr var2 = null;
      switch (var1) {
         case 1:
            var2 = var0.getTerm1();
            break;
         case 2:
            var2 = var0.getTerm2();
            break;
         case 3:
            var2 = var0.getTerm3();
            break;
         default:
            IllegalExpressionException var3 = new IllegalExpressionException(7, var0.getTypeName() + " error attempt to verifyStringExpressionTerm " + "on term number " + var1);
            var0.markExcAndThrowCollectionException(var3);
      }

      try {
         verifyStringExpressionType(var2);
      } catch (RDBMSException var5) {
         IllegalExpressionException var4 = new IllegalExpressionException(7, var0.getTypeName() + " function argument " + var1 + " is of the wrong type: " + var5.toString());
         var2.markExcAndThrowCollectionException(var4);
      }

   }

   private static void verifyStringExpressionType(Expr var0) throws RDBMSException {
      if (var0 != null && var0.type() != 17 && var0.type() != 18 && var0.type() != 25 && var0.type() != 54 && var0.type() != 55) {
         Loggable var1 = EJBLogger.logfinderInvalidStringExpressionLoggable();
         throw new RDBMSException(var0.getTypeName() + "  " + var1.getMessage());
      }
   }

   protected void verifyArithmeticExpressionTerm(Expr var1, int var2) throws ErrorCollectionException {
      Expr var3 = null;
      switch (var2) {
         case 1:
            var3 = var1.getTerm1();
            break;
         case 2:
            var3 = var1.getTerm2();
            break;
         case 3:
            var3 = var1.getTerm3();
            break;
         default:
            IllegalExpressionException var4 = new IllegalExpressionException(7, ExpressionTypes.TYPE_NAMES[var1.type()] + " error attempt to verifyArithmeticExpressionTerm " + "on term number " + var2);
            var1.markExcAndThrowCollectionException(var4);
      }

      try {
         this.verifyArithmeticExpressionType(var3);
      } catch (RDBMSException var6) {
         IllegalExpressionException var5 = new IllegalExpressionException(7, ExpressionTypes.TYPE_NAMES[var1.type()] + " function argument " + var2 + " is of the wrong type: " + var6.toString());
         var3.markExcAndThrowCollectionException(var5);
      }

   }

   private void verifyArithmeticExpressionType(Expr var1) throws RDBMSException {
      if (var1 != null && var1.type() != 17 && var1.type() != 19 && var1.type() != 20 && var1.type() != 23 && var1.type() != 24 && var1.type() != 22 && var1.type() != 21 && var1.type() != 25 && var1.type() != 57 && var1.type() != 56 && var1.type() != 58 && var1.type() != 59 && var1.type() != 76) {
         Loggable var2 = EJBLogger.logfinderInvalidArithExpressionLoggable();
         throw new RDBMSException(ExpressionTypes.TYPE_NAMES[var1.type()] + "  " + var2.getMessage());
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[BaseExpr] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }
}
