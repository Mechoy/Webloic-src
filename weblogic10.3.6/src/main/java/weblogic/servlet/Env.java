package weblogic.servlet;

import java.util.ArrayList;
import weblogic.utils.StringUtils;

public final class Env {
   private String[][] envStrings;

   public static synchronized native String globalGetenv(String var0);

   public static synchronized native void globalPutenv(String var0);

   private static synchronized native String getenvIndex(int var0);

   public static Env getenv() {
      return new Env();
   }

   public synchronized ArrayList getWholeEnv() {
      ArrayList var1 = new ArrayList();
      int var3 = 0;

      String var2;
      while((var2 = getenvIndex(var3)) != null) {
         ++var3;
         var1.add(var2);
      }

      return var1;
   }

   public synchronized String[] getWholeStrEnv() {
      ArrayList var1 = this.getWholeEnv();
      String[] var2 = new String[var1.size()];
      var1.toArray(var2);
      return var2;
   }

   public void putenv(String var1) {
      int var2;
      if ((var2 = var1.indexOf(61)) < 0) {
         throw new IllegalArgumentException("Illegal env string: '" + var1 + "', it doesn't contain '='");
      } else {
         String var3 = var1.substring(0, var2);
         String var4;
         if (var2 <= var1.length() - 1) {
            var4 = "";
         } else {
            var4 = var1.substring(var2 + 1);
         }

         this.putenv(var3, var4);
      }
   }

   public synchronized void putenv(String var1, String var2) {
      if (this.envStrings == null) {
         this.envStrings = this.getenvSplit();
      }

      int var3 = this.envStrings.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         if (var1.equals(this.envStrings[var4][0])) {
            this.envStrings[var4][1] = var2;
            return;
         }
      }

      String[][] var5 = new String[var3 + 1][];
      System.arraycopy(this.envStrings, 0, var5, 0, var3);
      var5[var3][0] = var1;
      var5[var3][1] = var2;
      this.envStrings = var5;
   }

   public synchronized String getenv(String var1) {
      if (this.envStrings == null) {
         this.envStrings = this.getenvSplit();
      }

      int var2 = this.envStrings.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var1.equals(this.envStrings[var3][0])) {
            return this.envStrings[var3][1];
         }
      }

      return null;
   }

   private synchronized String[][] getenvSplit() {
      ArrayList var1 = new ArrayList();
      int var3 = 0;

      String var2;
      while((var2 = getenvIndex(var3)) != null) {
         ++var3;
         var1.add(var2);
      }

      String[][] var4 = new String[var3][];

      for(int var5 = 0; var5 < var3; ++var5) {
         var4[var5] = StringUtils.split((String)var1.get(var5), '=');
      }

      return var4;
   }

   static {
      System.loadLibrary("wlenv");
   }
}
