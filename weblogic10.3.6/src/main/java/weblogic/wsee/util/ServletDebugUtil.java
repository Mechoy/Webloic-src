package weblogic.wsee.util;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletDebugUtil {
   public static void printRequest(HttpServletRequest var0) {
      StringBuilder var1 = new StringBuilder("HTTP REQUEST\n");
      var1.append("  ");
      var1.append(var0.getMethod());
      var1.append(" ");
      var1.append(var0.getRequestURI());
      var1.append(" \n");
      Enumeration var2 = var0.getHeaderNames();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         String var4 = var0.getHeader(var3);
         var1.append("  ");
         var1.append(var3);
         var1.append(": ");
         var1.append(var4);
         var1.append("\n");
      }

      var1.append("\n");
      Verbose.log((Object)var1.toString());
   }

   public static void printResponse(HttpServletResponse var0) {
      StringBuilder var1 = new StringBuilder("HTTP RESPONSE\n");
      var1.append("  ContentType= " + var0.getContentType() + "\n");
      var1.append("  CharacterEncoding= " + var0.getCharacterEncoding() + "\n");
      Verbose.log((Object)var1.toString());
   }
}
