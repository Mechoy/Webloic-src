package weblogic.wsee.util.jspgen;

import java.util.StringTokenizer;

class Directive extends Tag {
   private ResourceProvider provider;
   private LightJspParser parser;

   public Directive(ResourceProvider var1, LightJspParser var2) {
      this.provider = var1;
      this.parser = var2;
   }

   public LightJspParser getParser() {
      return this.parser;
   }

   private String toString(StringTokenizer var1) {
      StringBuffer var2 = new StringBuffer();

      while(var1.hasMoreTokens()) {
         var2.append(var1.nextToken());
         var2.append(" ");
      }

      return var2.toString();
   }

   public void generate(StringBuffer var1, StringBuffer var2, StringBuffer var3) throws ScriptException {
      String var4 = this.getContent();
      var4 = var4.substring(3, var4.length() - 2);
      StringTokenizer var5 = new StringTokenizer(var4);
      String var6 = null;
      if (var5.hasMoreTokens()) {
         var6 = var5.nextToken();
         if ("include".equals(var6)) {
            Include var7 = new Include(this.provider);
            var7.setContent(this.toString(var5));
            var7.generate(var1, var2, var3);
         } else if ("page".equals(var6)) {
            Page var8 = new Page(this.parser);
            var8.setContent(this.toString(var5));
            var8.generate(var1, var2, var3);
         }

      } else {
         throw new ScriptException("failed to parse: usage <%@ include|page ... %>");
      }
   }
}
