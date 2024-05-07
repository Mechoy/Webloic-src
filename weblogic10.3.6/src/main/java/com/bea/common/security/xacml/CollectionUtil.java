package com.bea.common.security.xacml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CollectionUtil {
   public static <X> boolean equals(Collection<X> one, Collection<X> two) {
      if (one == two) {
         return true;
      } else if (one != null && two != null && one.size() == two.size()) {
         Collection<X> temp = new ArrayList(one);
         Iterator<X> it = two.iterator();

         do {
            if (!it.hasNext()) {
               return temp.isEmpty();
            }
         } while(temp.remove(it.next()));

         return false;
      } else {
         return false;
      }
   }

   public static <X> boolean equalsWithSequence(Collection<X> one, Collection<X> two) {
      if (one == two) {
         return true;
      } else if (one != null && two != null && one.size() == two.size()) {
         Iterator<X> ite1 = one.iterator();
         Iterator<X> ite2 = two.iterator();

         do {
            if (!ite1.hasNext()) {
               return true;
            }
         } while(ite1.next().equals(ite2.next()));

         return false;
      } else {
         return false;
      }
   }
}
