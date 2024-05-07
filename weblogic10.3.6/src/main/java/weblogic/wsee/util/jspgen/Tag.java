package weblogic.wsee.util.jspgen;

abstract class Tag {
   private String content;

   public String getContent() {
      return this.content;
   }

   public void setContent(String var1) {
      this.content = var1;
   }

   public abstract void generate(StringBuffer var1, StringBuffer var2, StringBuffer var3) throws ScriptException;
}
