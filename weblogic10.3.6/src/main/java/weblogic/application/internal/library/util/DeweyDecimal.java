package weblogic.application.internal.library.util;

import java.io.Serializable;

public class DeweyDecimal implements Comparable<DeweyDecimal>, Serializable {
   private static final long serialVersionUID = -1547973439851595348L;
   private static final String FORMAT = "int(.int)*, int >=0";
   private static final String FORMAT_ERROR = "DeweyDecimal must be of format: int(.int)*, int >=0";
   private final int[] decimals;
   private final String stringRepr;

   public DeweyDecimal(Float var1) {
      this(String.valueOf(var1));
   }

   public DeweyDecimal(String var1) {
      this.validate(var1);
      this.decimals = this.init(var1);
      this.stringRepr = this.decimalsToString();
   }

   private void validate(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("DeweyDecimal cannot be constructed from null String");
      } else if (var1.startsWith(".") || var1.endsWith(".")) {
         throw new NumberFormatException("DeweyDecimal must be of format: int(.int)*, int >=0");
      }
   }

   private int[] init(String var1) {
      String var2 = var1;
      int var3 = var1.lastIndexOf(".");
      if (var3 == -1) {
         return new int[]{this.parseInt(var1)};
      } else {
         int var4 = 0;
         int var5 = var1.length();

         int[] var6;
         for(var6 = null; var3 > -1; --var4) {
            int var7 = this.parseInt(var2.substring(var3 + 1, var5));
            if (var7 != 0 || var6 != null) {
               if (var6 == null) {
                  var4 = this.countNumDots(var2);
                  var6 = new int[var4 + 1];
               }

               var6[var4] = var7;
            }

            var2 = var2.substring(0, var3);
            var5 = var3;
            var3 = var2.lastIndexOf(".");
         }

         if (var6 == null) {
            var6 = new int[1];
         }

         var6[0] = this.parseInt(var2);
         return var6;
      }
   }

   private int parseInt(String var1) {
      int var2 = Integer.parseInt(var1);
      if (var2 < 0) {
         throw new NumberFormatException(var2 + " " + "DeweyDecimal must be of format: int(.int)*, int >=0");
      } else {
         return var2;
      }
   }

   private int countNumDots(String var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         if (var1.charAt(var3) == '.') {
            ++var2;
         }
      }

      return var2;
   }

   private String decimalsToString() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.decimals.length; ++var2) {
         var1.append(this.decimals[var2]);
         if (var2 < this.decimals.length - 1) {
            var1.append(".");
         }
      }

      return var1.toString();
   }

   public int hashCode() {
      return this.stringRepr.hashCode();
   }

   public boolean equals(Object var1) {
      return !(var1 instanceof DeweyDecimal) ? false : this.equalsInternal(var1);
   }

   public String toString() {
      return this.stringRepr;
   }

   private int[] getDecimals() {
      return this.decimals;
   }

   private boolean equalsInternal(Object var1) {
      return this.hashCode() == var1.hashCode();
   }

   public int compareTo(DeweyDecimal var1) {
      if (this.equalsInternal(var1)) {
         return 0;
      } else {
         for(int var2 = 0; var2 != var1.getDecimals().length; ++var2) {
            if (var2 == this.getDecimals().length) {
               return -1;
            }

            if (this.getDecimals()[var2] > var1.getDecimals()[var2]) {
               return 1;
            }

            if (this.getDecimals()[var2] < var1.getDecimals()[var2]) {
               return -1;
            }
         }

         return 1;
      }
   }
}
