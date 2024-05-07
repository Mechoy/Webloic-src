package weblogic.wsee.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollectionUtil {
   private CollectionUtil() {
   }

   public static <T> List<T> asList(Iterator<T> var0) {
      ArrayList var1 = new ArrayList();

      while(var0.hasNext()) {
         var1.add(var0.next());
      }

      return var1;
   }
}
