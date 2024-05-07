package weblogic.marathon.fs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FSUtils {
   static void p(String var0) {
      System.err.println("[fsu]: " + var0);
   }

   public static String[] getPaths(FS var0, String var1, String var2, boolean var3, boolean var4) throws IOException {
      ArrayList var5 = new ArrayList();
      getPaths(var0.getRootEntry(), var1, var2, var5, var3, var4);
      String[] var6 = new String[var5.size()];
      var5.toArray(var6);
      return var6;
   }

   public static String[] getPaths(FS var0, String var1, String var2) throws IOException {
      return getPaths(var0, var1, var2, false, false);
   }

   private static void getPaths(Entry var0, String var1, String var2, List var3, boolean var4, boolean var5) throws IOException {
      Entry[] var6 = var0.list();
      if (var6 != null && var6.length != 0) {
         for(int var7 = 0; var7 < var6.length; ++var7) {
            String var8 = var6[var7].getPath();
            if (var8.startsWith("/")) {
               var8 = var8.substring(1);
            }

            String var9;
            String var10;
            if (var1.length() > var8.length()) {
               if (var4) {
                  var9 = var1.toLowerCase();
                  var10 = var8.toLowerCase();
                  if (var9.startsWith(var10)) {
                     getPaths(var6[var7], var1, var2, var3, var4, var5);
                  }
               } else if (var1.startsWith(var8)) {
                  getPaths(var6[var7], var1, var2, var3, var4, var5);
               }
            } else {
               if (var4) {
                  var9 = var1.toLowerCase();
                  var10 = var8.toLowerCase();
                  if (!var10.startsWith(var9)) {
                     continue;
                  }
               } else if (!var8.startsWith(var1)) {
                  continue;
               }

               getPaths(var6[var7], var1, var2, var3, var4, var5);
               if (var5) {
                  var9 = var8.toLowerCase();
                  var10 = var2.toLowerCase();
                  if (var9.endsWith(var10)) {
                     var3.add(var8);
                  }
               } else if (var8.endsWith(var2)) {
                  var3.add(var8);
               }
            }
         }

      }
   }

   public static String[] trimPrefix(String[] var0, String var1) {
      int var2 = var1.length();
      String[] var3 = new String[var0.length];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = var0[var4].substring(var2);
      }

      return var3;
   }

   public static void main(String[] var0) throws Exception {
      FS var1 = FS.mount(new File(var0[0]));
      String[] var2 = getPaths(var1, "WEB-INF/classes/", ".class");
      var2 = trimPrefix(var2, "WEB-INF/classes/");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         System.err.println(var2[var3]);
      }

   }
}
