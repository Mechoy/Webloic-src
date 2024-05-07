package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.Enumeration;
import java.util.Vector;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;

public final class Expression implements ExpressionTypes {
   private static final DebugLogger debugLogger;
   public int type;
   public Expression term1 = null;
   public Expression term2 = null;
   public Expression term3 = null;
   public Expression term4 = null;
   public Expression term5 = null;
   public Expression term6 = null;
   public Vector terms = null;
   private long ival;
   private double fval;
   private String sval;
   public int termCount;
   private int termVectorSize = 0;
   private int nextTerm = 1;

   public Expression(int var1) {
      this.type = var1;
      this.termCount = 0;
   }

   protected Expression(int var1, Expression var2) {
      this.type = var1;
      this.term1 = var2;
      this.termCount = 1;
   }

   protected Expression(int var1, Expression var2, Expression var3) {
      this.type = var1;
      this.term1 = var2;
      this.term2 = var3;
      this.termCount = 2;
   }

   protected Expression(int var1, Expression var2, Expression var3, Expression var4) {
      this.type = var1;
      this.term1 = var2;
      this.term2 = var3;
      this.term3 = var4;
      this.termCount = 3;
   }

   protected Expression(int var1, Expression var2, Expression var3, Expression var4, Expression var5) {
      this.type = var1;
      this.term1 = var2;
      this.term2 = var3;
      this.term3 = var4;
      this.term4 = var5;
      this.termCount = 4;
   }

   protected Expression(int var1, Expression var2, Expression var3, Expression var4, Expression var5, Expression var6) {
      this.type = var1;
      this.term1 = var2;
      this.term2 = var3;
      this.term3 = var4;
      this.term4 = var5;
      this.term5 = var6;
      this.termCount = 5;
   }

   protected Expression(int var1, Expression var2, Expression var3, Expression var4, Expression var5, Expression var6, Expression var7) {
      this.type = var1;
      this.term1 = var2;
      this.term2 = var3;
      this.term3 = var4;
      this.term4 = var5;
      this.term5 = var6;
      this.term6 = var7;
      this.termCount = 6;
   }

   protected Expression(int var1, Expression var2, Vector var3) {
      this.type = var1;
      this.term1 = var2;
      this.terms = var3;
      this.termVectorSize = var3.size();
      this.termCount = 1 + this.termVectorSize;
   }

   protected Expression(int var1, String var2) {
      this.type = var1;
      if (var1 == 18) {
         this.sval = var2.substring(1, var2.length() - 1);
      } else {
         this.sval = var2;
      }

      if (var1 == 19) {
         this.sval = var2;
         this.ival = (long)Double.valueOf(var2).intValue();
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

   public int type() {
      return this.type;
   }

   public int numTerms() {
      return this.termCount + this.termVectorSize;
   }

   public int termVectSize() {
      return this.termVectorSize;
   }

   public void resetTermNumber() {
      this.nextTerm = 1;
   }

   public void setNextTerm(int var1) {
      this.nextTerm = var1;
   }

   public Expression getNextTerm() {
      return this.getTerm(this.nextTerm++);
   }

   public Expression getTerm(int var1) {
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

   public boolean hasMoreTerms() {
      return this.nextTerm <= this.termCount;
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

   public static int numType(String var0) {
      return var0.indexOf(46) == -1 && var0.indexOf(69) == -1 ? 19 : 20;
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

   public static Expression getExpressionFromTerms(Expression var0, int var1) {
      var0.resetTermNumber();

      Expression var2;
      do {
         if (!var0.hasMoreTerms()) {
            return null;
         }

         var2 = var0.getNextTerm();
      } while(var2.type != var1);

      return var2;
   }

   public static Expression getAggregateExpressionFromSubQuerySelect(Expression var0) {
      if (var0.type != 43) {
         return null;
      } else {
         Expression var1 = null;
         var0.resetTermNumber();

         while(var0.hasMoreTerms()) {
            var1 = var0.getNextTerm();
            if (var1.type == 34) {
               break;
            }

            var1 = null;
         }

         if (var1 == null) {
            return null;
         } else if (var1.term2 == null) {
            return null;
         } else {
            return var1.term2.type != 44 && var1.term2.type != 45 && var1.term2.type != 46 && var1.term2.type != 47 && var1.term2.type != 48 ? null : var1.term2;
         }
      }
   }

   public String toString() {
      String var1 = this.toVal();
      return TYPE_NAMES[this.type] + ": " + (var1 == null ? this.term1 + "," + this.term2 : var1);
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
      dump(this, 0);
   }

   private static void dump(Expression var0, int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         System.out.print("| ");
      }

      System.out.print(TYPE_NAMES[var0.type()]);
      switch (var0.type()) {
         case 16:
            System.out.println("-- NULL");
            break;
         case 17:
         case 25:
         case 35:
         case 40:
         case 53:
         case 61:
         case 62:
            System.out.println(" -- " + var0.getSval());
            break;
         case 18:
            System.out.println(" -- \"" + var0.getSval() + "\"");
            break;
         case 19:
            System.out.println(" -- " + var0.getIval());
            break;
         case 20:
            System.out.println(" -- " + var0.getFval());
            break;
         default:
            System.out.println();
      }

      if (var0.term1 != null) {
         dump(var0.term1, var1 + 1);
      }

      if (var0.term2 != null) {
         dump(var0.term2, var1 + 1);
      }

      if (var0.term3 != null) {
         dump(var0.term3, var1 + 1);
      }

      if (var0.term4 != null) {
         dump(var0.term4, var1 + 1);
      }

      if (var0.term5 != null) {
         dump(var0.term5, var1 + 1);
      }

      if (var0.term6 != null) {
         dump(var0.term6, var1 + 1);
      }

      if (var0.terms != null) {
         Enumeration var3 = var0.terms.elements();

         while(var3.hasMoreElements()) {
            dump((Expression)var3.nextElement(), var1 + 1);
         }
      }

   }

   public static Expression find(Expression var0, int var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("Checking NODE: " + TYPE_NAMES[var0.type()] + "  for type: " + TYPE_NAMES[var1]);
      }

      if (var0.type() == var1) {
         return var0;
      } else {
         Expression var2 = null;
         if (var0.term1 != null) {
            var2 = find(var0.term1, var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.term2 != null) {
            var2 = find(var0.term2, var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.term3 != null) {
            var2 = find(var0.term3, var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.term4 != null) {
            var2 = find(var0.term4, var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.term5 != null) {
            var2 = find(var0.term5, var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.terms != null) {
            Enumeration var3 = var0.terms.elements();

            while(var3.hasMoreElements()) {
               var2 = find((Expression)var3.nextElement(), var1);
               if (var2 != null) {
                  return var2;
               }
            }
         }

         return null;
      }
   }

   public static String findIdForVariable(Expression var0, int var1) {
      if (var0.type() == 5 && var0.term2 != null && var0.term2.type() == 25 && var0.term2.getIval() == (long)var1 && var0.term1 != null && var0.term1.type() == 17) {
         if (debugLogger.isDebugEnabled()) {
            debug(" VARIABLE NODE: " + var0.term2.getIval() + ".  returning ID: " + var0.term1.getSval());
         }

         return var0.term1.getSval();
      } else {
         String var2 = null;
         if (var0.term1 != null) {
            var2 = findIdForVariable(var0.term1, var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.term2 != null) {
            var2 = findIdForVariable(var0.term2, var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.term3 != null) {
            var2 = findIdForVariable(var0.term3, var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.term4 != null) {
            var2 = findIdForVariable(var0.term4, var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.term5 != null) {
            var2 = findIdForVariable(var0.term5, var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.term6 != null) {
            var2 = findIdForVariable(var0.term6, var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var0.terms != null) {
            Enumeration var3 = var0.terms.elements();

            while(var3.hasMoreElements()) {
               var2 = findIdForVariable((Expression)var3.nextElement(), var1);
               if (var2 != null) {
                  return var2;
               }
            }
         }

         return var2;
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[Expression] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }
}
