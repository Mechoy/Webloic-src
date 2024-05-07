package weblogic.wsee.util.jspgen;

class Scriptlet extends Tag {
   public void generate(StringBuffer var1, StringBuffer var2, StringBuffer var3) throws ScriptException {
      String var4 = this.getContent();
      var1.append("\n" + var4.substring(2, var4.length() - 2));
   }
}
