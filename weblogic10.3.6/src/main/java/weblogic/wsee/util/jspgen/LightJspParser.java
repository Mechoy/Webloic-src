package weblogic.wsee.util.jspgen;

import java.util.ArrayList;
import java.util.Iterator;

public class LightJspParser {
   private String page;
   private ResourceProvider provider;
   private String packageName;
   private String base;

   public LightJspParser(String var1, ResourceProvider var2) {
      this.page = var1;
      this.provider = var2;
   }

   String getPackageName() {
      return this.packageName;
   }

   public void setPackage(String var1) {
      this.packageName = var1;
   }

   public void parse(StringBuffer var1, StringBuffer var2, StringBuffer var3) throws ScriptException {
      var1.append("  public void generate() \n");
      var1.append("   throws weblogic.wsee.util");
      var1.append(".jspgen.ScriptException{\n");
      this.generate(var1, var2, var3);
      var1.append("  }\n");
   }

   public void generate(StringBuffer var1, StringBuffer var2, StringBuffer var3) throws ScriptException {
      Iterator var4 = this.getTags();

      while(var4.hasNext()) {
         Tag var5 = (Tag)var4.next();
         var5.generate(var1, var2, var3);
      }

   }

   private Iterator getTags() throws ScriptException {
      new StringBuffer();
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.split(this.page);

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Tag var5 = this.createTag(var4);
         var5.setContent(var4);
         var2.add(var5);
      }

      return var2.iterator();
   }

   private Iterator split(String var1) throws ScriptException {
      String var2 = "<%";
      String var3 = "%>";
      ArrayList var4 = new ArrayList();
      int var5 = 0;
      boolean var6 = false;

      while(!var6) {
         int var7 = var1.indexOf(var2, var5);
         if (var7 == -1) {
            var4.add(var1.substring(var5, var1.length()));
            var6 = true;
         } else {
            if (var5 != var7) {
               var4.add(var1.substring(var5, var7));
            }

            int var8 = var1.indexOf(var3, var7);
            if (var8 == -1) {
               throw new ScriptException("unable to find the end tag " + var7 + " " + (var4.size() == 0 ? "at start" : var4.get(var4.size() - 1)));
            }

            var4.add(var1.substring(var7, var8 + 2));
            var5 = var8 + 2;
         }
      }

      return var4.iterator();
   }

   private Tag createTag(String var1) {
      if (var1.startsWith("<%--")) {
         return new Comment();
      } else if (var1.startsWith("<%=")) {
         return new Expression();
      } else if (var1.startsWith("<%!")) {
         return new Declaration();
      } else if (var1.startsWith("<%@")) {
         return new Directive(this.provider, this);
      } else {
         return (Tag)(var1.startsWith("<%") ? new Scriptlet() : new Text());
      }
   }

   public void setBaseClass(String var1) {
      this.base = var1;
   }

   String getBaseClass() {
      return this.base;
   }
}
