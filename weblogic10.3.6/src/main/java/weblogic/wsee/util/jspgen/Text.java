package weblogic.wsee.util.jspgen;

import java.util.StringTokenizer;

class Text extends Tag {
   private String clean(String var1) {
      if ("\n".equals(var1)) {
         return "\\n";
      } else {
         StringTokenizer var2 = new StringTokenizer(var1, "\"", true);
         StringBuffer var3 = new StringBuffer();

         while(var2.hasMoreTokens()) {
            String var4 = var2.nextToken();
            if ("\"".equals(var4)) {
               var3.append("\\\"");
            } else {
               var3.append(var4);
            }
         }

         return var3.toString();
      }
   }

   public void generate(StringBuffer var1, StringBuffer var2, StringBuffer var3) throws ScriptException {
      String var4 = this.getContent();
      if (var4 != null) {
         var4 = this.removeNewLines(var4);
         StringBuffer var5 = new StringBuffer();
         StringTokenizer var6 = new StringTokenizer(var4, "\r\n\f", true);

         while(var6.hasMoreTokens()) {
            String var7 = var6.nextToken();
            if (!"\r".equals(var7) && !"\f".equals(var7)) {
               var5.append("  out.print( \"");
               var5.append(this.clean(var7));
               var5.append("\" );\n");
            }
         }

         var1.append(var5);
      }
   }

   private String removeNewLines(String var1) {
      if (var1.startsWith("\n")) {
         var1 = var1.substring(1, var1.length());
      } else if (var1.startsWith("\r\n")) {
         var1 = var1.substring(2, var1.length());
      }

      if (var1.endsWith("\n")) {
         var1 = var1.substring(0, var1.length() - 1);
      } else if (var1.endsWith("\r\n")) {
         var1 = var1.substring(0, var1.length() - 2);
      }

      return var1;
   }
}
