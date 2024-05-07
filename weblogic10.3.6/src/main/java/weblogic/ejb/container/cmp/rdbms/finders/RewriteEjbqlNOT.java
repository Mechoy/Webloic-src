package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.utils.ErrorCollectionException;

public class RewriteEjbqlNOT {
   private static final DebugLogger debugLogger;

   private static void d(String var0) {
      debugLogger.debug("[RewriteEjbqlNOT] " + var0);
   }

   public static ExprROOT rewriteEjbqlNOT(ExprROOT var0, boolean var1) throws ErrorCollectionException {
      List var2 = var0.getWHEREList();
      Iterator var3 = var2.iterator();

      ExprWHERE var5;
      for(int var4 = 0; var3.hasNext(); findAndRewriteNOT(var5, false, var1)) {
         ++var4;
         var5 = (ExprWHERE)var3.next();
         if (var1) {
            d(" processing WHERE #" + var4);
         }
      }

      return var0;
   }

   private static BaseExpr findAndRewriteNOT(BaseExpr var0, boolean var1, boolean var2) throws ErrorCollectionException {
      boolean var3 = var1;
      String var4 = var0.debugClassName;
      if (var2) {
         d(" processing expr '" + var4 + "'");
         d("            expr '" + var4 + "' numTerms is " + var0.numTerms());
      }

      BaseExpr var8;
      BaseExpr var9;
      BaseExpr var10;
      if (var0.numTerms() > 0) {
         int var5 = 0;
         var0.resetTermNumber();

         label148:
         while(true) {
            while(true) {
               if (!var0.hasMoreTerms()) {
                  break label148;
               }

               ++var5;
               BaseExpr var6 = (BaseExpr)var0.getNextTerm();
               String var7 = var6.debugClassName;
               if (var7 == null) {
                  var7 = " NULL CLASSNAME ";
               }

               if (var2) {
                  d(var4 + " processing child #" + var5 + ", type '" + var7 + "'");
               }

               if (var6.type() == 2) {
                  if (var2) {
                     d(var4 + " processing child #" + var5 + " is NOT, handle NOT");
                  }

                  boolean var16 = !var3;
                  var9 = (BaseExpr)var6.getTerm1();
                  if (var9 == null) {
                     if (var6.termVectSize() > 0) {
                        throw new AssertionError("expr NOT child has unexpected vector of children when we were expecting only term1 to exist.");
                     }

                     throw new AssertionError("expr NOT has " + var6.numTerms() + " children, but we were expecting it to have exactly 1 child.");
                  }

                  while(var9.type() == 2) {
                     if (var2) {
                        d(var4 + " encountered consecutive NOT, discard it. ");
                     }

                     var16 = !var16;
                     var9 = (BaseExpr)var9.getTerm1();
                  }

                  var10 = findAndRewriteNOT(var9, var16, var2);
                  var0.replaceTermAt(var10, var5);
               } else {
                  if (var2) {
                     d(var4 + " processing child #" + var5 + " as normal child");
                  }

                  var8 = findAndRewriteNOT(var6, var3, var2);
                  var0.replaceTermAt(var8, var5);
               }
            }
         }
      }

      if (var0.termVectSize() > 0) {
         Vector var12 = var0.getTermVector();
         Iterator var14 = var12.iterator();
         int var15 = -1;

         label125:
         while(true) {
            while(true) {
               if (!var14.hasNext()) {
                  break label125;
               }

               ++var15;
               var8 = (BaseExpr)var14.next();
               if (var2) {
                  d(var4 + " processing vector child #" + var15 + ", type '" + var8.debugClassName + "'");
               }

               if (var8.type() == 2) {
                  if (var2) {
                     d(var4 + " processing vector child #" + var15 + " is NOT, handle NOT");
                  }

                  boolean var17 = !var3;
                  var10 = (BaseExpr)var8.getTerm1();
                  if (var10 == null) {
                     if (var8.termVectSize() > 0) {
                        throw new AssertionError("expr NOT child has unexpected vector of children when we were expecting only term1 to exist.");
                     }

                     throw new AssertionError("expr NOT has " + var8.numTerms() + " children, but we were expecting it to have exactly 1 child.");
                  }

                  while(var10.type() == 2) {
                     if (var2) {
                        d(var4 + " encountered consecutive NOT, discard it. ");
                     }

                     var17 = !var17;
                     var10 = (BaseExpr)var10.getTerm1();
                  }

                  BaseExpr var11 = findAndRewriteNOT(var10, var17, var2);
                  var12.setElementAt(var11, var15);
               } else {
                  if (var2) {
                     d(var4 + " processing vector child #" + var15 + " as normal child");
                  }

                  var9 = findAndRewriteNOT(var8, var3, var2);
                  var12.setElementAt(var9, var15);
               }
            }
         }
      }

      if (var2) {
         d(var4 + " we're done with our children, now take care of ourselves ");
      }

      if (var3) {
         BaseExpr var13 = (BaseExpr)var0.invertForNOT();
         if (var2) {
            d(var4 + " we need to  !!!  INVERT  !!!  ourself, morphing into " + var13.debugClassName + "\n\n");
         }

         return var13;
      } else {
         if (var2) {
            d(var4 + " we do not need to invert ourself returning " + var0.debugClassName + "\n\n");
         }

         return var0;
      }
   }

   public static boolean hasNOTExpr(BaseExpr var0) {
      if (var0.numTerms() > 0) {
         var0.resetTermNumber();

         while(var0.hasMoreTerms()) {
            BaseExpr var1 = (BaseExpr)var0.getNextTerm();
            if (var1.type() == 2) {
               return true;
            }

            if (hasNOTExpr(var1)) {
               return true;
            }
         }
      }

      if (var0.termVectSize() > 0) {
         Vector var4 = var0.getTermVector();
         Iterator var2 = var4.iterator();

         while(var2.hasNext()) {
            BaseExpr var3 = (BaseExpr)var2.next();
            if (var3.type() == 2) {
               return true;
            }

            if (hasNOTExpr(var3)) {
               return true;
            }
         }
      }

      return false;
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }
}
