package com.bea.uddiexplorer;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import weblogic.servlet.security.Utils;

public class StringUtils {
   public static String repeatedChar(char var0, int var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.append(var0);
      }

      return var2.toString();
   }

   public static boolean equals(String var0, String var1) {
      boolean var2 = var0 == var1;
      if (!var2 && var0 != null) {
         var2 = var0.equals(var1);
      }

      return var2;
   }

   public static int compare(String var0, String var1) {
      boolean var2 = true;
      int var3;
      if (var0 == var1) {
         var3 = 0;
      } else if (var0 == null) {
         var3 = -1;
      } else if (var1 == null) {
         var3 = 1;
      } else {
         var3 = var0.compareTo(var1);
      }

      return var3;
   }

   public static int compareNoCase(String var0, String var1) {
      boolean var2 = true;
      int var3;
      if (var0 == var1) {
         var3 = 0;
      } else if (var0 == null) {
         var3 = -1;
      } else if (var1 == null) {
         var3 = 1;
      } else {
         var3 = var0.compareToIgnoreCase(var1);
      }

      return var3;
   }

   public static boolean isEmpty(String var0) {
      return var0 == null || var0.length() == 0;
   }

   public static boolean startsWith(String var0, String var1) {
      return var0 != null && var1 != null && var1.startsWith(var0);
   }

   public static boolean endsWith(String var0, String var1) {
      return var0 != null && var1 != null && var1.endsWith(var0);
   }

   public static String concat(String var0, String var1) {
      String var2 = null;
      if (var0 == null) {
         var2 = var1;
      } else if (var1 == null) {
         var2 = var0;
      } else {
         var2 = var0 + var1;
      }

      return var2;
   }

   public static String concat(String var0, char var1) {
      return concat(var0, toString(var1));
   }

   public static String toString(String var0) {
      return var0 == null ? "" : var0;
   }

   public static String toString(char var0) {
      return String.valueOf(var0);
   }

   public static String[] linesOf(String var0) {
      Vector var1 = new Vector();
      int var2 = 0;

      String var5;
      for(boolean var3 = false; var2 < var0.length(); var1.addElement(var5)) {
         byte var4 = 1;
         int var6 = var0.indexOf("\r\n", var2);
         if (var6 >= 0) {
            var4 = 2;
         }

         if (var6 < 0) {
            var6 = var0.indexOf(10, var2);
         }

         if (var6 < 0) {
            var6 = var0.indexOf(13, var2);
         }

         if (var6 >= 0) {
            var5 = var0.substring(var2, var6);
            var2 = var6 + var4;
         } else {
            var5 = var0.substring(var2);
            var2 = var0.length();
         }
      }

      String[] var7 = null;
      if (var1.size() > 0) {
         var7 = new String[var1.size()];
         var1.copyInto(var7);
      }

      return var7;
   }

   public static WordData wordsOf(String var0, String var1) {
      WordData var2 = new WordData();
      Vector var3 = new Vector();
      Vector var4 = new Vector();
      Vector var5 = new Vector();
      int var6 = 0;
      boolean var7 = true;
      boolean var8 = false;

      int var9;
      for(var9 = 0; var9 < var0.length(); ++var9) {
         char var10 = var0.charAt(var9);
         if (Character.isSpaceChar(var10) || var1 != null && var1.indexOf(var10) >= 0) {
            if (var8) {
               var8 = false;
               String var11 = var0.substring(var6, var9);
               var3.addElement(var11);
               var4.addElement(new Integer(var6));
               var5.addElement(new Integer(var9));
            }
         } else if (!var8) {
            var8 = true;
            var6 = var9;
         }
      }

      if (var8) {
         var8 = false;
         String var12 = var0.substring(var6);
         var3.addElement(var12);
         var4.addElement(new Integer(var6));
         var5.addElement(new Integer(var0.length() - 1));
      }

      var2.words = new String[var3.size()];
      var2.starts = new int[var4.size()];
      var2.ends = new int[var5.size()];
      var3.copyInto(var2.words);

      for(var9 = 0; var9 < var4.size(); ++var9) {
         var2.starts[var9] = (Integer)var4.elementAt(var9);
      }

      for(var9 = 0; var9 < var5.size(); ++var9) {
         var2.ends[var9] = (Integer)var5.elementAt(var9);
      }

      return var2;
   }

   public static String formatOf(String var0, Object[] var1) {
      MessageFormat var2 = new MessageFormat(var0);
      return var2.format(var1);
   }

   public static String formatOf(String var0, Object var1) {
      Object[] var2 = new Object[]{var1};
      return formatOf(var0, var2);
   }

   public static String formatOf(String var0, boolean var1) {
      return formatOf(var0, (Object)(new Boolean(var1)).toString());
   }

   public static String formatOf(String var0, boolean var1, boolean var2) {
      return formatOf(var0, (new Boolean(var1)).toString(), (new Boolean(var2)).toString());
   }

   public static String formatOf(String var0, int var1) {
      return formatOf(var0, (Object)Integer.toString(var1));
   }

   public static String formatOf(String var0, int var1, int var2) {
      return formatOf(var0, Integer.toString(var1), Integer.toString(var2));
   }

   public static String formatOf(String var0, Object var1, Object var2) {
      Object[] var3 = new Object[]{var1, var2};
      return formatOf(var0, var3);
   }

   public static String replace(String var0, String var1, String var2) {
      String var3 = var0;
      if (!isEmpty(var0) && !isEmpty(var1)) {
         int var4 = var0.indexOf(var1);
         if (var4 >= 0) {
            String var5 = var0.substring(0, var4);
            String var6 = var0.substring(var4 + var1.length());
            var3 = concat(concat(var5, var2), replace(var6, var1, var2));
         }
      }

      return var3;
   }

   public static String rightTrim(String var0) {
      if (!isEmpty(var0)) {
         int var1 = var0.length();

         for(int var2 = var0.length() - 1; var2 >= 0; --var2) {
            char var3 = var0.charAt(var2);
            if (!Character.isWhitespace(var3)) {
               break;
            }

            var0 = var0.substring(0, var2);
         }
      }

      return var0;
   }

   public static String[] splitString(String var0, String var1) {
      StringTokenizer var2 = new StringTokenizer(var0, var1);
      String[] var3 = new String[var2.countTokens()];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = var2.nextToken();
      }

      return var3;
   }

   public static String toString(Collection var0, String var1, String var2, String var3) {
      StringBuffer var4 = new StringBuffer();
      Iterator var5 = var0.iterator();

      for(boolean var6 = false; var5.hasNext(); var6 = true) {
         if (var6 && var3 != null) {
            var4.append(var3);
         }

         if (var1 != null) {
            var4.append(var1);
         }

         var4.append(var5.next().toString());
         if (var2 != null) {
            var4.append(var2);
         }
      }

      return var4.toString();
   }

   public static String toString(Collection var0, String var1) {
      return toString((Collection)var0, (String)null, (String)null, var1);
   }

   public static String toString(Object[] var0, String var1, String var2, String var3) {
      return toString((Collection)Arrays.asList(var0), var1, var2, var3);
   }

   public static String toString(Object[] var0, String var1) {
      return toString((Collection)Arrays.asList(var0), var1);
   }

   public static String capitalize(String var0) {
      if (isEmpty(var0)) {
         return "";
      } else {
         StringBuffer var2 = new StringBuffer(var0);
         char var3 = var0.charAt(0);
         var2.setCharAt(0, Character.toUpperCase(var3));
         return var2.toString();
      }
   }

   public static String encodeXSS(String var0) {
      return Utils.encodeXSS(var0);
   }

   public static boolean isEmptyRequestParameter(String var0) {
      return isEmpty(var0) || equals(var0, "null");
   }

   public static class WordData {
      public String[] words;
      public int[] starts;
      public int[] ends;
   }
}
