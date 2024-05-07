package weblogic.nodemanager.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import weblogic.utils.StringUtils;

public class DataFormat {
   public static final char EOS_CHAR = '.';
   public static final String EOL = "\r\n";
   public static final String OK = "+OK ";
   public static final String ERR = "-ERR ";

   public static void readProperties(BufferedReader var0, Properties var1) throws IOException {
      String var2;
      while((var2 = readLine(var0)) != null) {
         int var3 = var2.indexOf(61);
         if (var3 == -1) {
            throw new IOException("Bad properties data format");
         }

         var1.setProperty(var2.substring(0, var3), var2.substring(var3 + 1));
      }

   }

   public static void writeProperties(Writer var0, Properties var1) throws IOException {
      Map.Entry var3;
      String var4;
      for(Iterator var2 = var1.entrySet().iterator(); var2.hasNext(); writeLine(var0, (String)var3.getKey() + '=' + var4)) {
         var3 = (Map.Entry)var2.next();
         var4 = (String)var3.getValue();
         if (var4.indexOf("\n") > -1) {
            var4 = var4.replace('\n', ' ');
         }
      }

      writeEOS(var0);
      var0.flush();
   }

   public static void copy(BufferedReader var0, Writer var1, boolean var2) throws IOException {
      String var3;
      while((var3 = readLine(var0)) != null) {
         var1.write(var3);
         var1.write("\r\n");
         if (var2) {
            var1.flush();
         }
      }

   }

   public static void copy(BufferedReader var0, Writer var1) throws IOException {
      copy(var0, var1, false);
   }

   public static String readLine(BufferedReader var0) throws IOException {
      String var1 = var0.readLine();
      if (var1 != null) {
         int var2 = var1.length();
         if (var2 > 0 && var1.charAt(0) == '.') {
            var1 = var2 == 1 ? null : var1.substring(1);
         }
      }

      return var1;
   }

   public static void writeLine(Writer var0, String var1) throws IOException {
      if (var1.length() > 0 && var1.charAt(0) == '.') {
         var1 = '.' + var1;
      }

      var0.write(var1 + "\r\n");
   }

   public static void writeEOS(Writer var0) throws IOException {
      var0.write(".\r\n");
   }

   public static void writeOK(Writer var0, String var1) throws IOException {
      var0.write("+OK " + var1 + "\r\n");
      var0.flush();
   }

   public static void writeERR(Writer var0, String var1) throws IOException {
      var0.write("-ERR " + var1 + "\r\n");
      var0.flush();
   }

   public static void writeScriptERR(Writer var0, int var1) throws IOException {
      var0.write("-ERR " + var1 + "\r\n");
      var0.flush();
   }

   public static String parseOK(String var0) {
      return var0.startsWith("+OK ") ? var0.substring("+OK ".length()) : null;
   }

   public static String parseERR(String var0) {
      return var0.startsWith("-ERR ") ? var0.substring("-ERR ".length()) : null;
   }

   public static int parseScriptERR(String var0) {
      if (var0.startsWith("-ERR ")) {
         try {
            return Integer.parseInt(var0.substring("-ERR ".length()));
         } catch (NumberFormatException var2) {
         }
      }

      return 0;
   }

   public static void writeCommand(Writer var0, Command var1, String[] var2) throws IOException {
      String var3 = var1.toString() + " ";
      if (var2 != null) {
         var3 = var3 + StringUtils.join(var2, " ");
      }

      var0.write(var3 + "\r\n");
      var0.flush();
   }
}
