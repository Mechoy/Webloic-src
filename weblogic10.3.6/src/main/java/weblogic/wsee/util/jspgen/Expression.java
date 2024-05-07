package weblogic.wsee.util.jspgen;

class Expression extends Tag {
   public void generate(StringBuffer var1, StringBuffer var2, StringBuffer var3) throws ScriptException {
      String var4 = this.getContent();
      var4 = var4.substring(3, var4.length() - 2);
      var1.append("\n out.print( " + var4 + " );");
   }
}
