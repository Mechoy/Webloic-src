package weblogic.wsee.util.jspgen;

import java.util.StringTokenizer;

class Include extends Tag {
   private ResourceProvider provider;

   public Include(ResourceProvider var1) {
      this.provider = var1;
   }

   public void generate(StringBuffer var1, StringBuffer var2, StringBuffer var3) throws ScriptException {
      String var4 = this.getContent();
      StringTokenizer var5 = new StringTokenizer(var4);
      if (var5.hasMoreTokens()) {
         String var6 = var5.nextToken();
         if (var6.startsWith("file=")) {
            var6 = var6.substring("file=".length(), var6.length());
            if (var6.charAt(0) == '"') {
               var6 = var6.substring(1, var6.length());
            }

            if (var6.charAt(var6.length() - 1) == '"') {
               var6 = var6.substring(0, var6.length() - 1);
            }

            (new LightJspParser(this.provider.getResource(var6), this.provider)).generate(var1, var2, var3);
         }
      }

   }
}
