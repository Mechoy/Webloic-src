package weblogic.jdbc.rowset;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.StringTokenizer;

public class SQLComparator implements Comparator, Serializable {
   private static final long serialVersionUID = 7517197886917898522L;
   ArrayList cols = new ArrayList();

   public SQLComparator(String var1) {
      if (var1 != null) {
         StringTokenizer var2 = new StringTokenizer(var1, ",");

         while(var2.hasMoreTokens()) {
            this.cols.add(var2.nextToken().trim());
         }

      }
   }

   public final int compare(Object var1, Object var2) {
      for(int var7 = 0; var7 < this.cols.size(); ++var7) {
         String var3 = (String)this.cols.get(var7);
         Object var4 = ((Map)var1).get(var3);
         Object var5 = ((Map)var2).get(var3);
         boolean var6 = false;

         try {
            if (var4 == null && var5 == null) {
               return 0;
            }

            if (var4 == null) {
               return -1;
            }

            if (var5 == null) {
               return 1;
            }

            Class var8 = var4.getClass();
            Class var9 = var5.getClass();
            int var11;
            if (var8 == var9 && Comparable.class.isAssignableFrom(var8)) {
               var11 = ((Comparable)var4).compareTo((Comparable)var5);
            } else if (!Number.class.isAssignableFrom(var8) && !Number.class.isAssignableFrom(var9)) {
               if (!Date.class.isAssignableFrom(var8) && !Date.class.isAssignableFrom(var9) && !Time.class.isAssignableFrom(var8) && !Time.class.isAssignableFrom(var9)) {
                  if (!Boolean.class.isAssignableFrom(var8) && !Boolean.class.isAssignableFrom(var9)) {
                     throw new RuntimeException(var8 + " can not be compared with " + var9);
                  }

                  if (String.class.isAssignableFrom(var8)) {
                     var4 = new Boolean(var4.toString().trim());
                  }

                  if (String.class.isAssignableFrom(var9)) {
                     var5 = new Boolean(var5.toString().trim());
                  }

                  var11 = ((Comparable)var4).compareTo((Comparable)var5);
               } else {
                  if (String.class.isAssignableFrom(var8)) {
                     var4 = Date.valueOf((String)var4);
                  }

                  if (String.class.isAssignableFrom(var9)) {
                     var5 = Date.valueOf((String)var5);
                  }

                  var11 = ((Comparable)var4).compareTo((Comparable)var5);
               }
            } else {
               if (Number.class.isAssignableFrom(var8) || String.class.isAssignableFrom(var8)) {
                  var4 = new BigDecimal(var4.toString().trim());
               }

               if (Number.class.isAssignableFrom(var9) || String.class.isAssignableFrom(var9)) {
                  var5 = new BigDecimal(var5.toString().trim());
               }

               var11 = ((Comparable)var4).compareTo((Comparable)var5);
            }

            if (var11 != 0) {
               return var11;
            }
         } catch (Throwable var10) {
            throw new RuntimeException(var4.getClass() + " can not be compared with " + var5.getClass());
         }
      }

      return 0;
   }
}
