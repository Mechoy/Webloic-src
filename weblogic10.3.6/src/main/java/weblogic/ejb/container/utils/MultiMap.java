package weblogic.ejb.container.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MultiMap {
   private Map map = new HashMap();

   public int put(Object var1, Object var2) {
      List var3 = this.get(var1);
      var3.add(var2);
      return var3.size();
   }

   public List get(Object var1) {
      Object var2 = (List)this.map.get(var1);
      if (var2 == null) {
         var2 = new ArrayList();
         this.map.put(var1, var2);
      }

      return (List)var2;
   }

   public Object get(Object var1, Object var2) {
      List var3 = this.get(var1);
      Iterator var4 = var3.iterator();

      Object var5;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         var5 = var4.next();
      } while(var5 == null || !var5.equals(var2));

      return var5;
   }

   public Object remove(Object var1, Object var2) {
      List var3 = this.get(var1);
      if (var3.size() == 0) {
         return null;
      } else {
         Iterator var4 = var3.iterator();
         boolean var5 = false;

         while(var4.hasNext()) {
            Object var6 = var4.next();
            if (var6 != null && var6.equals(var2)) {
               var3.remove(var6);
               var5 = true;
            }
         }

         if (var5) {
            return var2;
         } else {
            return null;
         }
      }
   }

   public Set keySet() {
      return this.map.keySet();
   }
}
