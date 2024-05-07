package weblogic.ejb20.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public final class OrderedSet extends ArrayList implements Set {
   private static final long serialVersionUID = 4253358509937919074L;

   public OrderedSet() {
   }

   public OrderedSet(Collection var1) {
      this.addAll(var1);
   }

   public boolean add(Object var1) {
      if (null == var1) {
         return false;
      } else {
         return this.contains(var1) ? false : super.add(var1);
      }
   }

   public boolean addAll(Collection var1) {
      if (null == var1) {
         return false;
      } else {
         boolean var2 = false;
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (this.add(var4)) {
               var2 = true;
            }
         }

         return var2;
      }
   }
}
