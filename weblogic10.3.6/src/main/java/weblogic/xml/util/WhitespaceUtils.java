package weblogic.xml.util;

import java.util.ArrayList;
import java.util.List;
import weblogic.utils.StringUtils;

public final class WhitespaceUtils {
   private static final boolean DEBUG = false;
   private static final char TAB = '\t';
   private static final char LF = '\n';
   private static final char CR = '\r';
   private static final char SPACE = ' ';
   private static final String EMPTY_STRING = "";

   public static String collapse(String var0) {
      if (var0 == null) {
         throw new NullPointerException("string must not be null");
      } else {
         int var1 = var0.length();
         if (var1 == 0) {
            return "";
         } else {
            char[] var2 = new char[var1];
            int var3 = 0;
            boolean var4 = false;

            for(int var5 = 0; var5 < var1; ++var5) {
               char var6 = var0.charAt(var5);
               if (isWhitespace(var6)) {
                  if (var4) {
                     var2[var3++] = var6;
                  }

                  var4 = false;
               } else {
                  var2[var3++] = var6;
                  var4 = true;
               }
            }

            --var3;
            if (var3 >= 0) {
               char var7 = var2[var3];
               if (isWhitespace(var7)) {
                  --var3;
               }
            }

            ++var3;
            if (var3 <= 0) {
               return "";
            } else {
               return new String(var2, 0, var3);
            }
         }
      }
   }

   public static boolean isWhitespace(char var0) {
      return var0 == ' ' || var0 == '\t' || var0 == '\n' || var0 == '\r';
   }

   public static String replace(String var0) {
      int var1 = var0.length();
      if (var1 == 0) {
         return "";
      } else {
         char[] var2 = new char[var1];
         int var3 = 0;

         for(int var4 = 0; var4 < var1; ++var4) {
            char var5 = var0.charAt(var4);
            if (var5 == '\t' || var5 == '\n' || var5 == '\r') {
               var5 = ' ';
            }

            var2[var3++] = var5;
         }

         return new String(var2, 0, var3);
      }
   }

   public static String removeAllWhitespaces(String var0) {
      if (var0 == null) {
         throw new NullPointerException("string must not be null");
      } else {
         String var1 = "";
         if (var0.length() == 0) {
            return var1;
         } else {
            String[] var2 = StringUtils.splitCompletely(var0);
            StringBuilder var3 = new StringBuilder();

            for(int var4 = 0; var4 < var2.length; ++var4) {
               var3.append(var2[var4]);
            }

            return var3.toString();
         }
      }
   }

   public static List splitOnXMLWhiteSpace(String var0) {
      ArrayList var1 = new ArrayList();
      if (var0 == null) {
         return var1;
      } else {
         int var2 = 0;
         int var3 = 0;
         boolean var4 = false;
         int var5 = var0.length();

         for(int var6 = 0; var6 < var5; ++var6) {
            char var7 = var0.charAt(var6);
            if (isWhitespace(var7)) {
               if (var4) {
                  String var8 = var0.substring(var2, 1 + var3);
                  var1.add(var8);
               }

               var4 = false;
            } else {
               if (!var4) {
                  var2 = var6;
               }

               var4 = true;
               var3 = var6;
            }
         }

         if (var4) {
            String var9 = var0.substring(var2, 1 + var3);
            var1.add(var9);
         }

         return var1;
      }
   }

   public static void main(String[] var0) throws Exception {
      String[] var1 = new String[]{"   ", "  1234\tabcd\tXYZ    ABC", "  fd   fds    d   ", "  fd   fds    d   Z", " ", "", "1234 abcd hijk", "1234", "Z", "fda asdf s d f"};

      for(int var2 = 0; var2 < var1.length; ++var2) {
         List var3 = splitOnXMLWhiteSpace(var1[var2]);
         System.out.println(var1[var2] + " => " + var3);
      }

   }
}
