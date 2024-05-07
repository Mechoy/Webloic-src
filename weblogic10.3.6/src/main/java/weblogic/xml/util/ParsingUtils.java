package weblogic.xml.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.ArrayList;

public final class ParsingUtils implements XMLConstants {
   private static final boolean debug = System.getProperty("weblogic.xml.debug") != null;

   public static boolean readWS(PushbackReader var0) throws IOException {
      int var1;
      do {
         var1 = var0.read();
         if (var1 < 0) {
            throw new EOFException();
         }
      } while(Character.isWhitespace((char)var1));

      var0.unread(var1);
      return true;
   }

   public static String read(PushbackReader var0, String var1, boolean var2) throws IOException {
      char[] var3 = var1.toCharArray();
      int var4 = 0;
      boolean var5 = false;

      StringBuffer var6;
      int var7;
      for(var6 = new StringBuffer(); !var5; var5 = true) {
         do {
            var7 = var0.read();
            if (var7 < 0) {
               throw new EOFException();
            }

            char var8 = (char)var7;
            var6.append(var8);
            if (var8 == var3[var4]) {
               ++var4;
            } else {
               var4 = 0;
            }
         } while(var4 != var1.length());
      }

      if (!var2) {
         var0.unread(var3);
         var7 = var6.length();
         var6.delete(var7 - var3.length, var7);
      }

      return var6.toString();
   }

   public static String read(PushbackReader var0, String[] var1, boolean var2) throws IOException {
      weblogic.utils.Debug.assertion(var1 != null);
      weblogic.utils.Debug.assertion(var1.length > 0);
      int[] var3 = new int[var1.length];
      ArrayList var4 = new ArrayList(var1.length);

      for(int var5 = 0; var5 < var1.length; ++var5) {
         var4.add(var1[var5].toCharArray());
      }

      StringBuffer var11 = new StringBuffer();
      boolean var6 = true;

      while(true) {
         int var7 = var0.read();
         if (var7 < 0) {
            throw new EOFException();
         }

         int var8 = (char)var7;
         var11.append((char)var8);

         for(int var9 = 0; var9 < var1.length; ++var9) {
            char[] var10 = (char[])((char[])var4.get(var9));
            if (var8 == var10[var3[var9]]) {
               int var10002 = var3[var9]++;
            } else {
               var3[var9] = 0;
            }

            if (var3[var9] >= var10.length) {
               if (!var2) {
                  var7 = ((char[])((char[])var4.get(var9))).length;
                  var0.unread((char[])((char[])var4.get(var9)));
                  var8 = var11.length();
                  var11.delete(var8 - var7, var8);
               }

               return var11.toString();
            }
         }
      }
   }

   public static String readUntilWS(PushbackReader var0) throws IOException {
      StringBuffer var1 = new StringBuffer();

      while(true) {
         int var2 = var0.read();
         if (var2 < 0) {
            throw new EOFException();
         }

         if (Character.isWhitespace((char)var2)) {
            var0.unread(var2);
            return var1.toString();
         }

         var1.append((char)var2);
      }
   }

   public static String peek(PushbackReader var0, int var1) throws IOException {
      if (debug) {
         System.out.println("ParsingUtils.peek(" + var1 + ") = ");
      }

      char[] var2 = new char[var1];
      var0.read(var2, 0, var1);
      var0.unread(var2, 0, var1);
      String var3 = new String(var2);
      if (debug) {
         System.out.println("\"" + var3 + "\"");
      }

      return new String(var2);
   }

   public static void main(String[] var0) throws Exception {
      if (debug) {
         if (var0.length == 0) {
            System.err.println("Usage: ParsingUtils <string to parse> <stop1> <stop2> ...");
            System.exit(1);
         }

         PushbackReader var1 = new PushbackReader(new StringReader(var0[0]));
         String[] var2 = new String[var0.length - 1];

         for(int var3 = 1; var3 < var0.length; ++var3) {
            var2[var3 - 1] = var0[var3];
         }

         System.out.println(read(var1, var2, true));
      }

   }
}
