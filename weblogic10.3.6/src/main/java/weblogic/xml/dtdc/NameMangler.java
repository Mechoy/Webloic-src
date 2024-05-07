package weblogic.xml.dtdc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.StringTokenizer;

public class NameMangler {
   public static String file2URL(String var0) throws MalformedURLException {
      var0 = var0.replace(File.separatorChar, '/');
      String var1 = System.getProperty("user.dir").replace(File.separatorChar, '/') + "/";
      if (var1.charAt(0) != '/') {
         var1 = "/" + var1;
      }

      URL var2 = new URL("file", "", var1);
      if (var0.length() >= 2 && var0.charAt(1) == ':') {
         char var3 = Character.toUpperCase(var0.charAt(0));
         if (var3 >= 'A' && var3 <= 'Z') {
            var0 = "file:///" + var0;
         }
      }

      return var0.equals("") ? var2.toString() : (new URL(var2, var0)).toString();
   }

   public static String upcase(String var0) {
      return var0.substring(0, 1).toUpperCase(Locale.ENGLISH) + (var0.length() > 1 ? var0.substring(1) : "").replace('-', '_').replace('.', '_');
   }

   public static String getpackage(String var0) {
      String var1 = "";

      String var3;
      for(StringTokenizer var2 = new StringTokenizer(var0, ":"); var2.hasMoreTokens(); var1 = var1 + "." + var3) {
         var3 = var2.nextToken();
         if (!var2.hasMoreTokens()) {
            break;
         }
      }

      return var1;
   }

   public static String depackage(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0, ":");

      while(var1.hasMoreTokens()) {
         String var2 = var1.nextToken();
         if (!var1.hasMoreTokens()) {
            var0 = var2;
            break;
         }
      }

      return deunderbar(var0);
   }

   public static String deunderbar(String var0) {
      StringBuffer var1 = new StringBuffer();
      StringTokenizer var2 = new StringTokenizer(var0, "-_.");
      int var3 = 0;

      while(var2.hasMoreTokens()) {
         String var4 = var2.nextToken();
         if (var3++ != 0) {
            var4 = upcase(var4);
         }

         if (!var2.hasMoreTokens()) {
            var0 = var4;
            break;
         }

         var1.append(var4);
      }

      var1.append(var0);
      return var1.toString();
   }
}
