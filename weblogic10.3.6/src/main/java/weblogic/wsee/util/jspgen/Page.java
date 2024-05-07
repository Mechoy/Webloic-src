package weblogic.wsee.util.jspgen;

import java.util.StringTokenizer;

class Page extends Tag {
   private LightJspParser parser;

   public Page(LightJspParser var1) {
      this.parser = var1;
   }

   public void generate(StringBuffer var1, StringBuffer var2, StringBuffer var3) throws ScriptException {
      String var4 = this.getContent();
      String var5 = var4.trim();
      if (var5.startsWith("base=")) {
         var5 = var5.substring("base=".length(), var5.length());
         var5 = trim(var5);
         this.parser.setBaseClass(var5);
      } else if (var5.startsWith("package=")) {
         var5 = var5.substring("package=".length(), var5.length());
         var5 = trim(var5);
         this.parser.setPackage(var5);
         var3.insert(0, "package " + var5 + ";\n");
      } else {
         if (!var5.startsWith("import=")) {
            throw new ScriptException("usage: <%@ page import=\"<className>\" %>");
         }

         var5 = var5.substring("import=".length(), var5.length());
         var5 = trim(var5);
         StringTokenizer var6 = new StringTokenizer(var5, ",");

         while(var6.hasMoreTokens()) {
            String var7 = var6.nextToken().trim();
            var3.append("import ");
            var3.append(var7);
            var3.append(";\n");
         }
      }

   }

   private static String trim(String var0) {
      if (var0.charAt(0) == '"') {
         var0 = var0.substring(1, var0.length());
      }

      if (var0.charAt(var0.length() - 1) == '"') {
         var0 = var0.substring(0, var0.length() - 1);
      }

      return var0;
   }
}
