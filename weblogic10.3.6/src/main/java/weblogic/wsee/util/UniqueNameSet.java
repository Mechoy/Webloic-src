package weblogic.wsee.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UniqueNameSet {
   private Set values = new HashSet();

   public String add(String var1) {
      return this.add(var1, new NumberResolver());
   }

   public String add(String var1, UniqueNameResolver var2) {
      while(this.values.contains(var1)) {
         var1 = var2.resolve(var1);
      }

      this.values.add(var1);
      return var1;
   }

   public boolean contains(String var1) {
      return this.values.contains(var1);
   }

   public Iterator getValues() {
      return this.values.iterator();
   }

   private static class NumberResolver implements UniqueNameResolver {
      int surfix = 1;

      NumberResolver() {
      }

      public String resolve(String var1) {
         return var1 + this.surfix++;
      }
   }
}
