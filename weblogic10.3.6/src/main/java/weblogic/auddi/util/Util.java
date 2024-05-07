package weblogic.auddi.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class Util {
   public static void saveToFile(String var0, String var1) throws IOException {
      PrintWriter var2 = new PrintWriter(new FileWriter(var1));
      var2.println(var0);
      var2.close();
   }

   public static String getFileContent(String var0) throws IOException {
      if (var0 == null) {
         throw new IllegalArgumentException("fileName was null");
      } else {
         BufferedReader var1 = new BufferedReader(new FileReader(var0));
         StringBuffer var3 = new StringBuffer();

         String var2;
         while((var2 = var1.readLine()) != null) {
            var3.append(var2.trim());
         }

         return var3.toString();
      }
   }

   public static String getFileContentWithLines(String var0) throws IOException {
      if (var0 == null) {
         throw new IllegalArgumentException("fileName was null");
      } else {
         BufferedReader var1 = new BufferedReader(new FileReader(var0));
         StringBuffer var3 = new StringBuffer();

         String var2;
         while((var2 = var1.readLine()) != null) {
            var3.append(var2.trim()).append("\n");
         }

         return var3.toString();
      }
   }

   public static String getFileContent(File var0) throws IOException {
      if (var0 == null) {
         throw new IllegalArgumentException("fileName was null");
      } else {
         BufferedReader var1 = new BufferedReader(new FileReader(var0));
         StringBuffer var3 = new StringBuffer();

         String var2;
         while((var2 = var1.readLine()) != null) {
            var3.append(var2.trim());
         }

         return var3.toString();
      }
   }

   public static String getStreamContent(InputStream var0) throws IOException {
      BufferedReader var1 = new BufferedReader(new InputStreamReader(var0));
      StringBuffer var2 = new StringBuffer();

      String var3;
      while((var3 = var1.readLine()) != null) {
         var2.append(var3.trim());
      }

      return var2.toString();
   }

   public static String fixStringForXML(String var0) {
      var0 = replaceWithException(var0, "&", "&amp;");
      var0 = replace(var0, "<", "&lt;");
      var0 = replace(var0, ">", "&gt;");
      var0 = replace(var0, "\"", "&quot;");
      return var0;
   }

   public static boolean isEqual(Object var0, Object var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   public static boolean isEqual(Object[] var0, Object[] var1) {
      return var0 == null ? var1 == null : Arrays.equals(var0, var1);
   }

   public static int hashCode(Object var0) {
      return var0 == null ? 0 : var0.hashCode();
   }

   public static int hashCode(Object[] var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1 ^= var0[var2].hashCode();
         }

         return var1;
      }
   }

   public static String quoteMessage(String var0) {
      return !var0.startsWith("'") ? "'" + var0 + "'" : var0;
   }

   public static String replace(String var0, String var1, String var2) {
      int var3;
      String var4;
      for(var4 = null; (var3 = var0.indexOf(var1)) != -1; var0 = var0.substring(var3 + var1.length())) {
         if (var4 == null) {
            var4 = "";
         }

         var4 = var4 + var0.substring(0, var3) + var2;
      }

      if (var4 == null) {
         var4 = var0;
      } else {
         var4 = var4 + var0;
      }

      return var4;
   }

   public static String replaceWithException(String var0, String var1, String var2, String var3) {
      String var5 = null;

      int var4;
      while((var4 = var0.indexOf(var1)) != -1) {
         if (var5 == null) {
            var5 = "";
         }

         if (var0.substring(var4).startsWith(var3)) {
            var5 = var5 + var0.substring(0, var4 + var3.length());
            var0 = var0.substring(var4 + var3.length());
         } else {
            var5 = var5 + var0.substring(0, var4) + var2;
            var0 = var0.substring(var4 + var1.length());
         }
      }

      if (var5 == null) {
         var5 = var0;
      } else {
         var5 = var5 + var0;
      }

      return var5;
   }

   public static String replaceWithException(String var0, String var1, String var2) {
      return replaceWithException(var0, var1, var2, var2);
   }

   public static String replaceOnce(String var0, String var1, String var2) {
      int var3;
      if ((var3 = var0.indexOf(var1)) != -1) {
         var0 = var0.substring(0, var3) + var2 + var0.substring(var3 + var1.length());
      }

      return var0;
   }

   public static String getOSName() {
      return System.getProperty("os.name").replace(' ', '_');
   }

   public static String space(int var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0; ++var2) {
         var1.append(" ");
      }

      return var1.toString();
   }

   public static int getResourceSize(ResourceBundle var0) {
      int var1 = 0;

      for(Enumeration var2 = var0.getKeys(); var2.hasMoreElements(); ++var1) {
         var2.nextElement();
      }

      return var1;
   }

   public static void printNames(Object[] var0) {
      if (var0 != null) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            String var2 = null;
            if (var0[var1] != null) {
               var2 = var0[var1].toString();
            }

            System.err.println("  " + var1 + " : " + var2);
         }
      }

   }

   public static String getStackTrace(Throwable var0) {
      StringWriter var1 = new StringWriter();
      PrintWriter var2 = new PrintWriter(new BufferedWriter(var1));
      var2.println(var0.getMessage());
      var0.printStackTrace(var2);
      var2.flush();
      return var1.toString();
   }

   private Util() {
   }

   public static String[] getPair(String var0, String var1) {
      String[] var2 = new String[2];
      int var3 = var0.indexOf(var1);
      var2[0] = var0.substring(0, var3).trim();
      var2[1] = var0.substring(var3 + var1.length()).trim();
      return var2;
   }

   public static String[] getPair(String var0) {
      return getPair(var0, "=");
   }

   public static ResourceBundle getResource(Class var0) {
      String var1 = var0.getName();
      return getResource(insertResourceInString(var1));
   }

   public static ResourceBundle getResource(Object var0) {
      String var1 = var0.getClass().getName();
      return getResource(insertResourceInString(var1));
   }

   public static ResourceBundle getResource(String var0) {
      ResourceBundle var1 = ResourceBundle.getBundle(var0);
      return var1;
   }

   private static String insertResourceInString(String var0) {
      int var1 = var0.lastIndexOf(".");
      String var2 = var0.substring(0, var1);
      String var3 = var0.substring(var1 + 1);
      var0 = var2 + ".resources." + var3;
      return var0;
   }

   public static String compactFile(String var0) throws IOException {
      String var1 = getFileContent(var0);
      return var1;
   }

   public static void main(String[] var0) {
   }
}
