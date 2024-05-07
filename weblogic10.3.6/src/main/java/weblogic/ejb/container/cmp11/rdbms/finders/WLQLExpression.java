package weblogic.ejb.container.cmp11.rdbms.finders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class WLQLExpression implements WLQLExpressionTypes, Serializable {
   private static final long serialVersionUID = -1319193628507182620L;
   private int type;
   private List terms = new ArrayList();
   private String sval;
   private String specialName;

   public WLQLExpression(int var1) {
      this.type = var1;
   }

   protected WLQLExpression(int var1, WLQLExpression var2, WLQLExpression var3) {
      this.type = var1;
      this.addTerm(var2);
      this.addTerm(var3);
   }

   protected WLQLExpression(int var1, WLQLExpression var2) {
      this.type = var1;
      this.addTerm(var2);
   }

   protected WLQLExpression(int var1, List var2) {
      this.type = var1;
      this.terms.addAll(var2);
   }

   protected WLQLExpression(int var1, String var2) {
      this.type = var1;
      this.sval = var2;
   }

   protected WLQLExpression(String var1, List var2) {
      this.type = 12;
      this.specialName = var1;
      this.terms.addAll(var2);
   }

   public int type() {
      return this.type;
   }

   public int getType() {
      return this.type;
   }

   public void addTerm(WLQLExpression var1) {
      this.terms.add(var1);
   }

   public int numTerms() {
      return this.terms.size();
   }

   public WLQLExpression term(int var1) {
      return (WLQLExpression)this.terms.get(var1);
   }

   public Iterator getTerms() {
      return this.terms.iterator();
   }

   public String getSval() {
      return this.sval;
   }

   public void setSval(String var1) {
      this.sval = var1;
   }

   public String getSpecialName() {
      return this.specialName;
   }

   public void setSpecialName(String var1) {
      this.specialName = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof WLQLExpression)) {
         return false;
      } else {
         WLQLExpression var2 = (WLQLExpression)var1;
         if (this.type != var2.getType()) {
            return false;
         } else {
            switch (this.type) {
               case 9:
               case 10:
               case 11:
               case 13:
                  if (!this.sval.equals(var2.getSval())) {
                     return false;
                  }
                  break;
               case 12:
                  if (!this.specialName.equals(var2.getSpecialName())) {
                     return false;
                  }
            }

            Iterator var3 = this.getTerms();
            Iterator var4 = var2.getTerms();

            WLQLExpression var5;
            WLQLExpression var6;
            do {
               if (!var3.hasNext()) {
                  if (var4.hasNext()) {
                     return false;
                  }

                  return true;
               }

               if (!var4.hasNext()) {
                  return false;
               }

               var5 = (WLQLExpression)var3.next();
               var6 = (WLQLExpression)var4.next();
            } while(var5.equals(var6));

            return false;
         }
      }
   }

   public int hashCode() {
      int var1 = this.type;
      if (this.sval != null) {
         var1 ^= this.sval.hashCode();
      }

      return var1;
   }

   public void dump() {
      System.out.println("DUMPING");
      dump(this, 0);
   }

   private static void dump(WLQLExpression var0, int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         System.out.print("| ");
      }

      System.out.print(TYPE_NAMES[var0.type]);
      switch (var0.type()) {
         case 9:
            System.out.println(" -- " + var0.getSval());
            break;
         case 10:
            System.out.println(" -- \"" + var0.getSval() + "\"");
            break;
         case 11:
            System.out.println(" -- " + var0.getSval());
            break;
         case 12:
            System.out.println("-- " + var0.getSpecialName());
            break;
         case 13:
            System.out.println("-- " + var0.getSval());
            break;
         default:
            System.out.println();
      }

      Iterator var4 = var0.getTerms();

      while(var4.hasNext()) {
         WLQLExpression var3 = (WLQLExpression)var4.next();
         dump(var3, var1 + 1);
      }

   }
}
