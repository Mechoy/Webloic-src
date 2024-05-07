package weblogic.entitlement.util;

public class Escaping {
   private char[] SpecialChars;

   public Escaping(char[] var1) {
      this.SpecialChars = new char[var1.length];
      System.arraycopy(var1, 0, this.SpecialChars, 0, var1.length);
   }

   public String escapeString(String var1) {
      if (var1 == null) {
         return null;
      } else {
         char[] var2 = var1.toCharArray();
         char[] var3 = new char[var2.length * 2];
         int var4 = 0;

         for(int var5 = 0; var5 < var2.length; ++var5) {
            var3[var4] = var2[var5];

            for(int var6 = 0; var6 < this.SpecialChars.length; ++var6) {
               if (this.SpecialChars[var6] == var2[var5]) {
                  var3[var4++] = this.SpecialChars[0];
                  var3[var4] = (char)(65 + var6);
                  break;
               }
            }

            ++var4;
         }

         String var7 = var4 == var2.length ? var1 : new String(var3, 0, var4);
         return var7;
      }
   }

   public String unescapeString(String var1) {
      char[] var2 = var1.toCharArray();
      int var3 = -1;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (var2[var4] == this.SpecialChars[0]) {
            if (var3 == -1) {
               var3 = var4;
            }

            int var10001 = var3++;
            ++var4;
            var2[var10001] = this.SpecialChars[var2[var4] - 65];
         } else if (var3 >= 0) {
            var2[var3++] = var2[var4];
         }
      }

      String var5 = var3 > 0 ? new String(var2, 0, var3) : var1;
      return var5;
   }
}
